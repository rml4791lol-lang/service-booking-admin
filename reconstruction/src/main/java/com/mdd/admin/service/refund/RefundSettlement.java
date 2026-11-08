package com.mdd.admin.service.refund;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mdd.admin.service.IFinanceRechargerService;
import com.mdd.admin.service.IRefundTaskService;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.course.CourseRefund;
import com.mdd.common.entity.refund.RefundTask;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.refund.RefundTaskMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * #220 退款出款唯一入口：课程 / 券 / 活动三条退款链路统一到这里分派渠道。
 *
 * <p>调用方（{@code CourseRefundServiceImpl} 审核通过后的三条分支）只构造退款意图
 * {@link Intent}——退款单、原订单、审核人、业务线与出款金额；渠道分派、幂等放行与
 * 失败记录语义全部收敛在本模块，新增支付渠道只改这一处（#219 方案 1）。</p>
 *
 * <p>渠道分派（与重构前三条链路的行为逐一对齐）：</p>
 * <ul>
 *   <li><b>微信</b>（payWay=2 且有交易号）→ #60 outbox 异步任务：审核事务内不直连微信，
 *       失败记 {@code la_refund_task.last_error} 并按重试策略自动/人工重试；
 *       放行前 #152 同单唯一（订单行锁 + 既有任务检查，失败任务必须走 retry 复用
 *       原 out_refund_no，绝不另起第二笔）；同 refund_id 已有任务时直接复用
 *       （活动退款重试补偿语义），不二次放行。</li>
 *   <li><b>钱包</b>（payWay=1）→ {@code FinanceRechargerService.refund} 同步退回余额，
 *       REQUIRES_NEW 先提交退款记录与订单状态；失败异常原样上抛，审核事务整体回滚。</li>
 *   <li><b>其它</b>（线下转账/历史无交易号）→ 条件更新订单 refund_status 0→1
 *       （#152 并发闸门），审核即视为退款完成，不建任务。</li>
 *   <li>金额非正或订单不可退（未支付/已退款/已删）→ 不出款（NONE），与课程链路
 *       「无需线上退款不建任务」的既有行为一致。</li>
 * </ul>
 *
 * <p>失败记录语义统一：异步渠道失败记 outbox 任务（last_error + retry），
 * 同步渠道失败以异常上抛回滚审核；每次分派落一条同格式日志供对账。</p>
 */
@Service
public class RefundSettlement {
    private static final Logger log = LoggerFactory.getLogger(RefundSettlement.class);

    @Resource
    RefundTaskMapper refundTaskMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    IRefundTaskService refundTaskService;
    @Resource
    IFinanceRechargerService financeRechargerService;

    /** 退款业务线：决定不出款时的展示口径与对账归集，不改变渠道分派规则。 */
    public enum BusinessType {
        /** 课程退课（course_class_id>0）。 */
        COURSE,
        /** 活动报名退款（#213，course_class_id=0 且订单 activity_id>0）。 */
        ACTIVITY,
        /** 券订单退款（#110，course_class_id=0）。 */
        COUPON
    }

    /** 出款渠道。 */
    public enum Channel {
        /** 无需出款：金额非正或订单不可退。 */
        NONE,
        /** 微信 outbox 异步任务（#60）。 */
        WECHAT_OUTBOX,
        /** 钱包余额同步退回。 */
        WALLET,
        /** 其它渠道（线下转账等），审核即完成。 */
        OFFLINE
    }

    /** 退款意图：调用方构造，本模块据此分派。 */
    public static final class Intent {
        private final CourseRefund refund;
        private final RechargeOrder order;
        private final Integer adminId;
        private final BusinessType businessType;
        private final BigDecimal amount;

        private Intent(CourseRefund refund, RechargeOrder order, Integer adminId,
                       BusinessType businessType, BigDecimal amount) {
            this.refund = refund;
            this.order = order;
            this.adminId = adminId;
            this.businessType = businessType;
            this.amount = amount;
        }

        /**
         * @param refund       退款单（outbox 任务号、任务金额从其派生）
         * @param order        原支付订单（渠道按其 payWay/transactionId 判定）
         * @param adminId      审核人（钱包退款记账用）
         * @param businessType 业务线（课程/活动/券）
         * @param amount       出款金额；调用方已按业务口径解析（活动为空回落整单金额）
         */
        public static Intent of(CourseRefund refund, RechargeOrder order, Integer adminId,
                                BusinessType businessType, BigDecimal amount) {
            return new Intent(refund, order, adminId, businessType, amount);
        }

        public CourseRefund refund() {
            return refund;
        }

        public RechargeOrder order() {
            return order;
        }

        public Integer adminId() {
            return adminId;
        }

        public BusinessType businessType() {
            return businessType;
        }

        public BigDecimal amount() {
            return amount;
        }
    }

    /** 出款结果：渠道 + 微信任务是否复用既有任务。 */
    public static final class Result {
        private final Channel channel;
        private final boolean reused;

        private Result(Channel channel, boolean reused) {
            this.channel = channel;
            this.reused = reused;
        }

        public Channel channel() {
            return channel;
        }

        public boolean reused() {
            return reused;
        }
    }

    /**
     * 唯一出款入口：按意图分派微信 outbox / 钱包同步 / 其它渠道。
     *
     * <p>不吞异常：钱包退款失败原样上抛（审核事务回滚）；微信放行守卫失败同样上抛，
     * 不落「假成功」任务。出款金额非正或订单不可退时不出款。</p>
     */
    public Result settle(Intent intent) {
        BigDecimal amount = intent.amount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new Result(Channel.NONE, false);
        }
        RechargeOrder order = intent.order();
        if (!isRefundable(order)) {
            log.info("退款出款跳过（订单不可退）: type={} refundId={} orderId={} amount={}",
                    intent.businessType(), intent.refund().getId(), order.getId(), amount);
            return new Result(Channel.NONE, false);
        }
        boolean wechatPaid = order.getPayWay() != null && order.getPayWay() == 2
                && StringUtils.isNotEmpty(order.getTransactionId());
        boolean walletPaid = order.getPayWay() != null && order.getPayWay() == 1;
        if (wechatPaid) {
            return settleWechat(intent, order);
        }
        if (walletPaid) {
            // 钱包退款：REQUIRES_NEW 先提交退款记录/订单状态；失败上抛回滚审核。
            this.financeRechargerService.refund(order.getId(), intent.adminId(), amount);
            logSettlement(intent, Channel.WALLET, false);
            return new Result(Channel.WALLET, false);
        }
        // 其它渠道（线下转账/历史无交易号）：审核即视为退款完成，不建任务。
        // #152：条件更新（refund_status 0→1），并发双审核只有一个置位成功。
        this.rechargeOrderMapper.update(null, new UpdateWrapper<RechargeOrder>()
                .eq("id", order.getId())
                .eq("refund_status", 0)
                .isNull("delete_time")
                .set("refund_status", 1)
                .set("update_time", TimeUtils.timestamp()));
        logSettlement(intent, Channel.OFFLINE, false);
        return new Result(Channel.OFFLINE, false);
    }

    private Result settleWechat(Intent intent, RechargeOrder order) {
        // 同一退款申请已有任务时直接复用（重试补偿），不再二次放行/二次退款。
        RefundTask existingTask = this.refundTaskMapper.selectOne(new QueryWrapper<RefundTask>()
                .eq("refund_id", intent.refund().getId()).last("limit 1"));
        if (existingTask != null) {
            logSettlement(intent, Channel.WECHAT_OUTBOX, true);
            return new Result(Channel.WECHAT_OUTBOX, true);
        }
        claimOrderForWechatRefund(order);
        this.refundTaskService.createForRefund(intent.refund(), order);
        logSettlement(intent, Channel.WECHAT_OUTBOX, false);
        return new Result(Channel.WECHAT_OUTBOX, false);
    }

    private boolean isRefundable(RechargeOrder order) {
        return order != null
                && Integer.valueOf(1).equals(order.getPayStatus())
                && Integer.valueOf(0).equals(order.getRefundStatus() == null
                        ? Integer.valueOf(0) : order.getRefundStatus())
                && order.getDeleteTime() == null;
    }

    private void logSettlement(Intent intent, Channel channel, boolean reused) {
        log.info("退款出款: type={} channel={} reused={} refundId={} orderId={} amount={} adminId={}",
                intent.businessType(), channel, reused, intent.refund().getId(),
                intent.order().getId(), intent.amount(), intent.adminId());
    }

    /**
     * #152 结算侧同单唯一放行：微信退款任务落库前对 order 行加锁（SELECT ... FOR UPDATE，
     * 当前读，不受 REPEATABLE READ 快照影响），再以当前读检查同单（order_id）是否已存在退款任务。
     *
     * <p>任意状态的既有任务（待执行/执行中/成功/失败）都算：失败任务必须走 retry 复用原
     * out_refund_no（微信侧幂等），绝不允许第二笔申请另起 out_refund_no 再放一笔。
     * 并发审核在同一 order 行锁上串行：后到者要么看到 refund_status!=0（已退款），
     * 要么看到同单已有任务（已放行未到账），均抛业务错误，保证同单只放行一笔退款任务。</p>
     *
     * <p>只能在「微信支付」分支内调用。钱包分支禁止调用：FinanceRechargerService 用
     * REQUIRES_NEW 独立事务更新 order 行，外层先锁 order 行会让内层条件更新等待自身锁，
     * 锁超时；钱包路径由该条件更新（refund_status 0→1，affected!=1 即拒绝）保证同单只退一次。</p>
     */
    private void claimOrderForWechatRefund(RechargeOrder order) {
        RechargeOrder locked = this.rechargeOrderMapper.selectOne(new QueryWrapper<RechargeOrder>()
                .eq("id", order.getId()).last("limit 1 for update"));
        if (locked == null || locked.getDeleteTime() != null
                || !Integer.valueOf(1).equals(locked.getPayStatus())
                || !Integer.valueOf(0).equals(locked.getRefundStatus() == null
                        ? Integer.valueOf(0) : locked.getRefundStatus())) {
            throw new OperateException("订单退款状态已变化，请刷新后重试");
        }
        RefundTask existingTask = this.refundTaskMapper.selectOne(new QueryWrapper<RefundTask>()
                .eq("order_id", order.getId()).orderByAsc("id").last("limit 1 for update"));
        if (existingTask != null) {
            throw new OperateException("该订单已存在退款任务（退款处理中或已完成），请勿重复审核退款");
        }
    }
}
