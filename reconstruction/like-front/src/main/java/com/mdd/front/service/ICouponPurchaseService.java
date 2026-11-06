package com.mdd.front.service;

import com.mdd.common.entity.RechargeOrder;

import java.util.Map;

/**
 * 次数券购买与支付回调发券（T29.4 / #103）。
 *
 * <p>下单只创建 {@code la_recharge_order} 待支付订单，券在支付成功回调里发放：
 * 订单 {@code coupon_goods_id} 标记券商品；#103 起一个订单按商品次数 N 发 N 张
 * 独立券实例（每张 1 次），幂等由 {@code uk_order_seq(order_id, order_seq)} 保证
 * 同一订单的同一张券只发一次（并发重复回调也只成功一次）。</p>
 */
public interface ICouponPurchaseService {

    /**
     * 券商品下单（免费券直接发放）。
     *
     * @param productId 券商品 ID（字符串或数字）
     * @param studentId 持券学员 ID（字符串或数字）
     * @return orderId/orderSn/orderAmount/free/couponId/couponIds/couponCount/mockPay
     */
    Map<String, Object> placeOrder(Integer userId, Integer terminal, String productId, String studentId);

    /**
     * 支付成功回调发券（幂等）。由 IPayService.handlePaidNotify("coupon", sn, tradeNo) 调用。
     */
    void issueForPaidOrder(String outTradeNo, String transactionId);

    /**
     * mock 支付回调前的订单校验：订单存在、属于当前用户、且是券订单。
     */
    RechargeOrder requireMockableOrder(Integer userId, String orderId);

    /**
     * 是否开启 mock 支付回调（like.pay.mock-callback-enabled，默认 false，生产必须关闭）。
     */
    boolean isMockCallbackEnabled();
}
