package com.mdd.front.service;

import com.github.binarywang.wxpay.exception.WxPayException;

import java.util.Map;

/**
 * 本地/测试支付 mock 网关（T30.11）。
 *
 * <p>只替换“支付结果通知”这一外部依赖，业务代码仍走真实
 * {@link IPayService#handlePaidNotify(String, String, String, Integer)}，因此能验证：</p>
 * <ul>
 *   <li>成功：下单 -> 回调 -> 发券；</li>
 *   <li>失败/取消：订单关闭为 pay_status=4，不发券；</li>
 *   <li>重复回调：同一订单只发一张券、只写一条发放流水。</li>
 * </ul>
 *
 * <p>开关 {@code like.pay.mock-callback-enabled} 默认 false，生产必须关闭。
 * 本网关只接受已登录用户操作自己的订单。</p>
 */
public interface IPayMockGatewayService {

    /**
     * 模拟一次支付结果通知。
     *
     * @param action success/fail/cancel/duplicate（大小写不敏感，缺省 success）
     * @param couponOnly 仅允许券订单（保留 /api/coupon/mockNotify 的旧语义）
     * @return orderId/orderSn/action/payStatus/couponId/couponCount/flowCount/idempotent
     */
    Map<String, Object> notify(Integer userId, Integer orderId, String action, boolean couponOnly)
            throws WxPayException;

    /** 是否开启 mock 支付网关（like.pay.mock-callback-enabled，默认 false）。 */
    boolean isMockCallbackEnabled();
}
