/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.alipay.api.AlipayApiException
 *  com.github.binarywang.wxpay.exception.WxPayException
 *  com.mdd.common.entity.RechargeOrder
 *  com.mdd.front.service.IPayService
 *  com.mdd.front.validate.PaymentValidate
 *  com.mdd.front.vo.pay.PayStatusVo
 *  com.mdd.front.vo.pay.PayWayListVo
 */
package com.mdd.front.service;

import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayApiException;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.front.validate.PaymentValidate;
import com.mdd.front.vo.pay.PayStatusVo;
import com.mdd.front.vo.pay.PayWayListVo;

public interface IPayService {
    public PayWayListVo payWay(String var1, Integer var2, Integer var3, Integer var4);

    public PayStatusVo payStatus(String var1, Integer var2, Integer var3);

    public Object prepay(PaymentValidate var1, Integer var2, String var3);

    public void handlePaidNotify(String var1, String var2, String var3) throws WxPayException;

    /**
     * 真实微信回调入口：额外校验实付金额（分）。
     * mock/余额支付传 null 表示不校验金额。
     */
    public void handlePaidNotify(String attach, String outTradeNo, String transactionId,
                                 Integer paidAmountFen) throws WxPayException;

    public JSONObject createAliH5Order(PaymentValidate var1) throws AlipayApiException;

    public String buildCoursePayDescription(RechargeOrder var1);

    /**
     * 次数券订单的支付描述（T29.4）：券名-学员名。
     */
    public String buildCouponPayDescription(RechargeOrder var1);

    /**
     * 活动报名订单的支付描述（T29.7）：活动标题-活动报名。
     */
    public String buildActivityPayDescription(RechargeOrder var1);
}

