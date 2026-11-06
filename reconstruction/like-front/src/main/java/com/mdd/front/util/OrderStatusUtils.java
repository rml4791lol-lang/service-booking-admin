package com.mdd.front.util;

/**
 * 小程序订单中心状态映射（#63）。
 *
 * <p>订单状态以 {@code la_recharge_order} 的支付/退款字段为准，并与既有退款链路保持一致：</p>
 * <ul>
 *   <li>{@code pay_status=0} 待支付；{@code pay_status=1} 已支付；{@code pay_status=4} 已取消（超时/主动取消）；</li>
 *   <li>{@code refund_status=1} 只有「已支付」订单才表示已退款；
 *       课程未支付取消也复用该字段，因此必须先判断支付状态，避免把未付取消误展示为已退款；</li>
 *   <li>{@code la_refund_task.status=2}（#60 退款任务成功）同样视为已退款；
 *       任务待执行/执行中/失败仍按已支付展示，但把任务状态透出给页面提示，不吞掉退款进度；</li>
 *   <li>活动报名历史数据可能只把报名明细置为已取消（{@code la_activity_signup.status=3}），
 *       由读模型传入 {@code activityCancelled} 兜底展示「已取消」。</li>
 * </ul>
 *
 * <p>文案与用户服务协议/客服口径一致：退款按原支付方式退回，到账时间以微信支付/银行为准，
 * 不承诺具体时限；退款进度引导用户联系客服。</p>
 */
public final class OrderStatusUtils {

    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_PAID = "paid";
    public static final String STATUS_REFUNDED = "refunded";
    public static final String STATUS_CANCELLED = "cancelled";

    public static final String TYPE_COUPON = "coupon";
    public static final String TYPE_ACTIVITY = "activity";
    public static final String TYPE_COURSE = "course";
    public static final String TYPE_OTHER = "other";

    private OrderStatusUtils() {
    }

    /**
     * 解析订单中心展示状态。
     *
     * @param payStatus        支付状态：0=待支付，1=已支付，4=已取消
     * @param refundStatus     退款状态：0=未退款，1=已退款（未支付订单上用 1 表示取消）
     * @param refundTaskStatus #60 退款任务状态：0=待执行，1=执行中，2=成功，3=失败
     * @param activityCancelled 活动报名明细是否已全部取消（历史数据兜底）
     */
    public static String resolveStatus(Integer payStatus, Integer refundStatus,
                                       Integer refundTaskStatus, boolean activityCancelled) {
        boolean paid = payStatus != null && payStatus.intValue() == 1;
        boolean closed = payStatus != null && payStatus.intValue() == 4;
        if (!paid) {
            if (closed || (refundStatus != null && refundStatus.intValue() == 1) || activityCancelled) {
                return STATUS_CANCELLED;
            }
            return STATUS_PENDING;
        }
        if (refundStatus != null && refundStatus.intValue() == 1) {
            return STATUS_REFUNDED;
        }
        if (refundTaskStatus != null && refundTaskStatus.intValue() == 2) {
            return STATUS_REFUNDED;
        }
        return STATUS_PAID;
    }

    public static String statusText(String status) {
        if (STATUS_REFUNDED.equals(status)) {
            return "已退款";
        }
        if (STATUS_CANCELLED.equals(status)) {
            return "已取消";
        }
        if (STATUS_PAID.equals(status)) {
            return "已支付";
        }
        return "待支付";
    }

    /** 列表/详情统一说明，客服口径：原路退回、到账时间以支付渠道为准、进度联系客服。 */
    public static String statusHint(String status, String type) {
        if (STATUS_REFUNDED.equals(status)) {
            return "退款已按原支付方式退回，到账时间以微信支付/银行处理结果为准；退款进度可联系客服查询。";
        }
        if (STATUS_CANCELLED.equals(status)) {
            if (TYPE_ACTIVITY.equals(type)) {
                return "报名已取消，名额已释放，未产生实际扣款；如需参加请重新报名。";
            }
            return "订单已取消，未产生实际扣款；如需购买请重新下单。";
        }
        if (STATUS_PAID.equals(status)) {
            return "支付成功，服务安排以订单约定和客服通知为准；如需退款请联系客服。";
        }
        return "订单待支付，请在有效期内完成支付，超时未支付将自动取消。";
    }

    /**
     * 已支付但退款任务未完成时的说明（#60）：不把处理中/失败误展示成普通「已支付」，
     * 也不提前承诺退款到账。
     */
    public static String statusHint(String status, String type, Integer refundTaskStatus) {
        if (STATUS_PAID.equals(status) && refundTaskStatus != null) {
            if (refundTaskStatus.intValue() == 0 || refundTaskStatus.intValue() == 1) {
                return "退款已提交，正在原支付渠道处理，到账时间以微信支付/银行处理结果为准；进度可联系客服查询。";
            }
            if (refundTaskStatus.intValue() == 3) {
                return "退款处理失败，客服正在跟进；如需帮助请联系客服。";
            }
        }
        return statusHint(status, type);
    }

    /** #60 退款任务状态文案；无任务返回 null，页面只对有任务的订单展示。 */
    public static String refundTaskStatusText(Integer status) {
        if (status == null) {
            return null;
        }
        switch (status.intValue()) {
            case 0:
                return "待退款";
            case 1:
                return "退款处理中";
            case 2:
                return "退款成功";
            case 3:
                return "退款失败，请联系客服";
            default:
                return null;
        }
    }

    /** 订单类型：活动 > 课程 > 券 > 其他（订单创建时只会命中一种）。 */
    public static String resolveType(Integer activityId, Integer courseClassId, Integer couponGoodsId) {
        if (activityId != null && activityId.intValue() > 0) {
            return TYPE_ACTIVITY;
        }
        if (courseClassId != null && courseClassId.intValue() > 0) {
            return TYPE_COURSE;
        }
        if (couponGoodsId != null && couponGoodsId.intValue() > 0) {
            return TYPE_COUPON;
        }
        return TYPE_OTHER;
    }

    public static String typeText(String type) {
        if (TYPE_COUPON.equals(type)) {
            return "课程券";
        }
        if (TYPE_ACTIVITY.equals(type)) {
            return "活动报名";
        }
        if (TYPE_COURSE.equals(type)) {
            return "课程";
        }
        return "订单";
    }
}
