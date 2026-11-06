package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.coupon.CouponFlow;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.entity.course.CourseRefund;
import com.mdd.common.entity.user.User;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.coupon.CouponFlowMapper;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;
import com.mdd.common.mapper.course.CourseRefundMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.ParamUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.front.service.ICouponRefundService;
import com.mdd.front.util.CouponRefundRules;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 券退款申请实现（#110 T12）。
 *
 * <p>设计要点（见 {@code B-revenue-coupon.md} §3.3 建议）：</p>
 * <ol>
 *   <li>不新建表、不改 {@code CourseClassServiceImpl#applyRefund}：券订单没有
 *       {@code la_user_course}，复用会污染课程退款统计；</li>
 *   <li>复用 {@code la_course_refund}：{@code course_class_id=0} + {@code class_name='券退款-<券名>'}
 *       作为券退款的来源标记，后台列表据此展示「来源：券」；</li>
 *   <li>复用 {@code la_refund_task} outbox：审核通过时按 {@code refund_id} 建任务（UNIQUE 天然一单一任务），
 *       真实/模拟渠道由后台执行器决定；</li>
 *   <li>开关 {@code like.coupon.refund-enabled}（默认 true）：关闭时接口直接拒绝、小程序隐藏入口。</li>
 * </ol>
 */
@Service
public class CouponRefundServiceImpl implements ICouponRefundService {

    private static final int STATUS_PENDING = 0;

    /** 券流水类型：2=核销扣减，5=退回（DDL 已预留）。 */
    private static final int FLOW_CONSUME = 2;

    @Value("${like.coupon.refund-enabled:true}")
    private boolean refundEnabled;

    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    CouponInstanceMapper couponInstanceMapper;
    @Resource
    CouponFlowMapper couponFlowMapper;
    @Resource
    CourseRefundMapper courseRefundMapper;
    @Resource
    UserMapper userMapper;

    @Override
    public boolean isRefundEnabled() {
        return refundEnabled;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> applyRefund(Integer userId, Map<String, Object> params) {
        if (!refundEnabled) {
            throw new OperateException("券退款功能暂未开启，请联系客服");
        }
        if (userId == null) {
            throw new OperateException("用户未登录");
        }
        Map<String, Object> input = params == null ? new LinkedHashMap<String, Object>() : params;
        Integer orderId = ParamUtils.intOrDefault(input.get("orderId"), 0, "订单ID");
        String reason = input.get("reason") == null ? "" : String.valueOf(input.get("reason")).trim();
        String detail = input.get("detail") == null ? "" : String.valueOf(input.get("detail"));
        if (orderId <= 0) {
            throw new OperateException("订单不存在");
        }
        if (reason.isEmpty()) {
            throw new OperateException("请填写退款原因");
        }

        RechargeOrder order = rechargeOrderMapper.selectById(orderId);
        if (order == null || order.getDeleteTime() != null || !userId.equals(order.getUserId())) {
            throw new OperateException("订单不存在");
        }
        if (order.getCouponGoodsId() == null || order.getCouponGoodsId() <= 0) {
            throw new OperateException("该订单不是课程券订单，请从订单页申请课程退费");
        }
        if (order.getPayStatus() == null || order.getPayStatus() != 1) {
            throw new OperateException("订单未支付，无法申请退款");
        }

        List<CouponInstance> instances = couponInstanceMapper.selectList(new QueryWrapper<CouponInstance>()
                .eq("order_id", orderId).orderByAsc("id"));
        if (instances.isEmpty()) {
            throw new OperateException("订单券实例不存在，请联系客服");
        }

        // 重复申请：同一订单已有待审批申请时拒绝，避免后台重复审核导致重复退款。
        CourseRefund pending = courseRefundMapper.selectOne(new QueryWrapper<CourseRefund>()
                .eq("user_id", userId).eq("order_id", orderId).eq("status", STATUS_PENDING)
                .isNull("delete_time").last("limit 1"));
        if (pending != null) {
            throw new OperateException("您已有一条待审批的券退款申请，请勿重复提交");
        }

        boolean consumed = couponFlowMapper.selectCount(new QueryWrapper<CouponFlow>()
                .eq("order_id", orderId).eq("type", FLOW_CONSUME)) > 0;
        boolean used = CouponRefundRules.used(instances, consumed);
        boolean pack = CouponRefundRules.pack(instances);
        String mode = CouponRefundRules.mode(instances.size(), used, pack,
                order.getPayStatus(), order.getRefundStatus());
        String hint = CouponRefundRules.hint(mode, instances.size(), used, pack);

        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("orderId", orderId);
        if (!CouponRefundRules.MODE_REFUND.equals(mode)) {
            // 已使用 / 券包 / 已退款：只返回客服路径，不写退款单（AC2）。
            result.put("mode", CouponRefundRules.MODE_SERVICE);
            result.put("refundId", null);
            result.put("message", hint);
            return result;
        }

        com.mdd.front.util.RefundTextLimits.validate(reason, detail);
        BigDecimal payAmount = order.getOrderAmount() == null ? BigDecimal.ZERO : order.getOrderAmount();
        String title = instances.get(0).getTitle() == null ? "课程券" : instances.get(0).getTitle();
        User user = userMapper.selectById(userId);
        long now = TimeUtils.timestamp();
        CourseRefund refund = new CourseRefund();
        refund.setUserId(userId);
        // 券退款标记：course_class_id=0（列 NOT NULL DEFAULT 0），后台「来源」列据此判定。
        refund.setCourseClassId(Integer.valueOf(0));
        refund.setOrderId(orderId);
        refund.setClassName("券退款-" + title);
        refund.setUserName(user != null && user.getNickname() != null ? user.getNickname() : "");
        refund.setReason(reason);
        refund.setDetail(detail);
        refund.setPayAmount(payAmount);
        // 未使用前提下的全额退回；部分使用不进入本分支。
        refund.setRefundAmount(payAmount);
        refund.setTotalLessons(Integer.valueOf(instances.size()));
        refund.setUsedLessons(Integer.valueOf(0));
        refund.setStatus(Integer.valueOf(STATUS_PENDING));
        refund.setCreateTime(now);
        refund.setUpdateTime(now);
        try {
            courseRefundMapper.insert(refund);
        } catch (DuplicateKeyException e) {
            // #152：pending 校验是 select-then-insert，并发双申请由 V023 uk_pending_order
            // （同单至多一笔待审批）在库层拦下；这里转业务错误，不暴露 SQL 细节。
            throw new OperateException("您已有一条待审批的券退款申请，请勿重复提交");
        }

        result.put("mode", CouponRefundRules.MODE_REFUND);
        result.put("refundId", refund.getId());
        result.put("message", "退款申请已提交，客服将审核并按原支付方式退回");
        return result;
    }
}
