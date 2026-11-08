package com.mdd.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.coupon.CouponFlow;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.enums.PaymentEnum;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.coupon.CouponFlowMapper;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;
import com.mdd.common.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 超时未支付订单关单（#214 资金一致性）。
 *
 * <p>关单不是单纯把 {@code la_recharge_order.pay_status} 置 4：订单可能已经占用
 * 券实例（{@code la_coupon_instance.order_id}）或活动报名名额
 * （{@code la_activity_booking} / {@code la_activity_signup}）。本服务在同一个事务里
 * 完成「条件关单 + 释放资源」，避免订单已取消但券/名额永久泄漏。</p>
 *
 * <p>并发/幂等：关单本身是 {@code pay_status=0 → 4} 的条件更新，只有抢到状态变更的
 * 调用才继续释放；券作废用 {@code status<>4} 条件更新，活动释放复用
 * {@link ActivitySignupCancelService#releaseUnpaidOrder(Integer)}（批次 PENDING→CANCELLED
 * 是名额释放唯一闸门），重复执行不会二次扣减。</p>
 */
@Service
public class UnpaidOrderCancelService {

    private static final Logger log = LoggerFactory.getLogger(UnpaidOrderCancelService.class);

    /** 券实例状态：4=已作废（与 CouponGoodsServiceImpl / CourseRefundServiceImpl 同口径）。 */
    private static final int COUPON_STATUS_VOID = 4;
    /** 券流水类型：4=作废。 */
    private static final int COUPON_FLOW_VOID = 4;

    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    CouponInstanceMapper couponInstanceMapper;
    @Resource
    CouponFlowMapper couponFlowMapper;
    @Resource
    ActivitySignupCancelService activitySignupCancelService;

    /**
     * 取消一笔超时未支付订单并释放其占用的券/活动名额。
     *
     * @param orderId    订单ID
     * @param expireTime 超时阈值（create_time 必须 ≤ 该时间才允许关单）
     * @return 是否由本次调用完成关单（false=订单已支付/已取消/未超时/不存在）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelExpired(Integer orderId, long expireTime) {
        if (orderId == null || orderId <= 0) {
            return false;
        }
        long now = TimeUtils.timestamp();
        int cancelled = rechargeOrderMapper.update(null, new UpdateWrapper<RechargeOrder>()
                .set("pay_status", PaymentEnum.CLOSED.getCode())
                .set("update_time", now)
                .eq("id", orderId)
                .eq("pay_status", PaymentEnum.UN_PAID.getCode())
                .le("create_time", expireTime)
                .isNull("delete_time"));
        if (cancelled <= 0) {
            return false;
        }
        RechargeOrder order = rechargeOrderMapper.selectById(orderId);
        int voidedCoupons = releaseCoupons(order, now);
        int releasedSeats = activitySignupCancelService.releaseUnpaidOrder(orderId);
        log.info("超时未支付订单自动取消: orderId={} voidedCoupons={} releasedSeats={}",
                orderId, voidedCoupons, releasedSeats);
        return true;
    }

    /**
     * 作废订单关联的券实例并写 type=4 作废流水。正常链路未支付订单不会有券实例，
     * 这里兜住「回调部分成功/人工补数据」留下的脏实例，避免券永久可用。
     */
    private int releaseCoupons(RechargeOrder order, long now) {
        if (order == null) {
            return 0;
        }
        List<CouponInstance> instances = couponInstanceMapper.selectList(new QueryWrapper<CouponInstance>()
                .eq("order_id", order.getId())
                .ne("status", COUPON_STATUS_VOID)
                .orderByAsc("id"));
        int voided = 0;
        for (CouponInstance instance : instances) {
            int affected = couponInstanceMapper.update(null, new UpdateWrapper<CouponInstance>()
                    .eq("id", instance.getId())
                    .ne("status", COUPON_STATUS_VOID)
                    .set("status", COUPON_STATUS_VOID)
                    .set("remaining", 0)
                    .set("update_time", now));
            if (affected <= 0) {
                continue;
            }
            CouponFlow flow = new CouponFlow();
            flow.setCouponId(instance.getId());
            flow.setUserId(instance.getUserId());
            flow.setStudentId(instance.getStudentId());
            flow.setType(COUPON_FLOW_VOID);
            flow.setTimes(1);
            flow.setBalance(0);
            flow.setOrderId(order.getId());
            flow.setOrderSn(order.getSn() == null ? "" : order.getSn());
            flow.setRef("");
            flow.setOperatorType("system");
            flow.setOperatorId(0);
            flow.setRemark("订单超时未支付自动取消，券已作废");
            flow.setCreateTime(now);
            couponFlowMapper.insert(flow);
            voided++;
        }
        return voided;
    }
}
