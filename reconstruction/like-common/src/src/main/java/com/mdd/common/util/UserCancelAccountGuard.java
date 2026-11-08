package com.mdd.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 注销账号前置校验（唯一口径）。
 *
 * <p>账号注销会软删用户并解除微信绑定，不可自助恢复；因此存在任何在途资产/交易时
 * 必须先处理完，避免钱和课在注销后悬空：</p>
 * <ol>
 *     <li>待支付订单 —— 先支付或取消；</li>
 *     <li>已支付但退款未完成 —— 等退款到账；</li>
 *     <li>未结束的预约 —— 先取消预约（否则注销后仍占用教练/场地）；</li>
 *     <li>未使用次数券 —— 先使用或转人工处理；</li>
 *     <li>账户余额 —— 必须联系客服线下处理（预付费合规）。</li>
 * </ol>
 *
 * <p>命中多条时按上述风险顺序返回第一条，保证用户拿到的是最该先处理的原因。</p>
 */
public final class UserCancelAccountGuard {

    private UserCancelAccountGuard() {
    }

    /**
     * @return null 表示可以注销；否则为可直接返回给用户的拒绝原因
     */
    public static String check(int pendingOrders,
                               int refundsInProgress,
                               int futureBookings,
                               int unusedCoupons,
                               BigDecimal balance) {
        if (pendingOrders > 0) {
            return "存在" + pendingOrders + "笔待支付订单，请先完成支付或取消订单后再申请注销";
        }
        if (refundsInProgress > 0) {
            return "存在" + refundsInProgress + "笔退款未完成的订单，请待退款到账后再申请注销";
        }
        if (futureBookings > 0) {
            return "存在" + futureBookings + "节未结束的课程预约，请先取消预约后再申请注销";
        }
        if (unusedCoupons > 0) {
            return "存在" + unusedCoupons + "张未使用的次数券，请先使用或联系客服处理后再申请注销";
        }
        if (balance != null && balance.compareTo(BigDecimal.ZERO) > 0) {
            return "账号余额 ¥" + balance.setScale(2, RoundingMode.HALF_UP).toPlainString()
                    + " 需先联系客服处理后再申请注销";
        }
        return null;
    }
}
