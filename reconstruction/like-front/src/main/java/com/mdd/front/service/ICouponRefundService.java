package com.mdd.front.service;

import java.util.Map;

/**
 * 券退款申请（#110 T12）。
 *
 * <p>未使用的单券：写一条 {@code la_course_refund}（course_class_id=0）等待后台审核，
 * 审核通过后复用课程退款的 outbox（{@code la_refund_task}）原路退回；
 * 已使用 / 10 节课券包：只返回客服路径，不写退款单。</p>
 */
public interface ICouponRefundService {

    /**
     * 提交券退款申请。
     *
     * @param userId 当前登录用户（小程序家长）
     * @param params {orderId, reason, detail}
     * @return {mode: refund|service, refundId, message, orderId}
     */
    Map<String, Object> applyRefund(Integer userId, Map<String, Object> params);

    /** 开关：like.coupon.refund-enabled，默认 true。 */
    boolean isRefundEnabled();
}
