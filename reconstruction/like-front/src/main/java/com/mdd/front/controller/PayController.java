/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.binarywang.wxpay.bean.notify.SignatureHeader
 *  com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result$DecryptNotifyResult
 *  com.github.binarywang.wxpay.exception.WxPayException
 *  com.github.binarywang.wxpay.service.WxPayService
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.entity.RechargeOrder
 *  com.mdd.common.enums.ClientEnum
 *  com.mdd.common.enums.PaymentEnum
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.RechargeOrderMapper
 *  com.mdd.common.plugin.wechat.WxPayDriver
 *  com.mdd.common.util.StringUtils
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.PayController
 *  com.mdd.front.service.IPayService
 *  com.mdd.front.validate.PaymentValidate
 *  com.mdd.front.vo.pay.PayStatusVo
 *  com.mdd.front.vo.pay.PayWayListVo
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  javax.servlet.http.HttpServletRequest
 *  javax.validation.constraints.NotNull
 *  org.springframework.util.Assert
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.front.controller;

import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.service.WxPayService;
import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.enums.ClientEnum;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.enums.PaymentEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.exception.PaymentException;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.plugin.wechat.WxPayDriver;
import com.mdd.common.util.SensitiveUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IPayService;
import com.mdd.common.util.ParamUtils;
import com.mdd.front.validate.PaymentValidate;
import com.mdd.front.vo.pay.PayStatusVo;
import com.mdd.front.vo.pay.PayWayListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.Serializable;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/pay"})
@Api(tags={"\u652f\u4ed8\u7ba1\u7406"})
public class PayController {
    private static final Logger log = LoggerFactory.getLogger(PayController.class);

    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    IPayService iPayService;

    @GetMapping(value={"/payWay"})
    @ApiOperation(value="\u652f\u4ed8\u65b9\u5f0f")
    public AjaxResult<PayWayListVo> payWay(
            @Validated @NotNull(message="from\u53c2\u6570\u4e22\u5931") @RequestParam @NotNull(message="from\u53c2\u6570\u4e22\u5931") String from,
            @RequestParam(value="orderId", required=false) String orderId,
            @RequestParam(value="order_id", required=false) String legacyOrderId) {
        Integer terminal = LikeFrontThreadLocal.getTerminal();
        Integer userId = LikeFrontThreadLocal.getUserId();
        Integer resolvedOrderId = this.resolveOrderId(orderId, legacyOrderId);
        PayWayListVo list = this.iPayService.payWay(from, resolvedOrderId, terminal, userId);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/payStatus"})
    @ApiOperation(value="\u652f\u4ed8\u72b6\u6001")
    public AjaxResult<PayStatusVo> payStatus(
            @Validated @NotNull(message="from\u53c2\u6570\u4e22\u5931") @RequestParam @NotNull(message="from\u53c2\u6570\u4e22\u5931") String from,
            @RequestParam(value="orderId", required=false) String orderId,
            @RequestParam(value="order_id", required=false) String legacyOrderId) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Integer resolvedOrderId = this.resolveOrderId(orderId, legacyOrderId);
        PayStatusVo vo = this.iPayService.payStatus(from, resolvedOrderId, userId);
        return AjaxResult.success(vo);
    }

    /**
     * \u8ba2\u5355\u53c2\u6570\u517c\u5bb9\uff1a\u65b0\u5c0f\u7a0b\u5e8f\u9002\u914d\u5c42\u7edf\u4e00\u7528 camelCase {@code orderId}\uff0c
     * \u5386\u53f2\u63a5\u53e3/\u811a\u672c\u4ecd\u4f7f\u7528\u4e0b\u5212\u7ebf {@code order_id}\uff1b\u4e24\u79cd\u547d\u540d\u90fd\u63a5\u53d7\uff0c
     * \u540c\u65f6\u4f20\u5165\u65f6\u4f18\u5148 camelCase\u3002\u7f3a\u53c2/\u975e\u6570\u5b57\u8fd4\u56de\u660e\u786e\u4e1a\u52a1\u9519\u8bef\u3002
     */
    private Integer resolveOrderId(String orderId, String legacyOrderId) {
        String raw = orderId != null && !orderId.trim().isEmpty() ? orderId : legacyOrderId;
        return ParamUtils.requiredInt(raw, "\u8ba2\u5355ID\uff08orderId/order_id\uff09");
    }

    @PostMapping(value={"/prepay"})
    @ApiOperation(value="\u53d1\u8d77\u652f\u4ed8")
    public AjaxResult<Object> prepay(@Validated @RequestBody PaymentValidate requestObj) {
        if (StringUtils.isNull(requestObj.getScene())) {
            requestObj.setScene("recharge");
        }
        String scene = requestObj.getScene();
        Integer payWay = requestObj.getPayWay();
        Integer orderId = requestObj.getOrderId();
        Integer terminal = LikeFrontThreadLocal.getTerminal();
        Integer currentUserId = LikeFrontThreadLocal.getUserId();
        String code = requestObj.getCode();
        requestObj.setTerminal(terminal);
        int payStatus = 0;
        RechargeOrder payOrder = null;
        switch (scene) {
            case "recharge": {
                RechargeOrder rechargeOrder = this.requireOwnOrder(orderId, currentUserId);
                Assert.isTrue((!payWay.equals(PaymentEnum.WALLET_PAY.getCode()) ? 1 : 0) != 0, (String)"\u652f\u4ed8\u7c7b\u578b\u4e0d\u88ab\u652f\u6301");
                requestObj.setUserId(rechargeOrder.getUserId());
                requestObj.setOutTradeNo(rechargeOrder.getSn());
                requestObj.setOrderAmount(rechargeOrder.getOrderAmount());
                requestObj.setDescription("\u4f59\u989d\u5145\u503c");
                requestObj.setAttach(scene);
                payStatus = rechargeOrder.getPayStatus();
                payOrder = rechargeOrder;
                break;
            }
            case "course": {
                RechargeOrder courseOrder = this.requireOwnOrder(orderId, currentUserId);
                requestObj.setUserId(courseOrder.getUserId());
                requestObj.setOutTradeNo(courseOrder.getSn());
                requestObj.setOrderAmount(courseOrder.getOrderAmount());
                requestObj.setDescription(this.iPayService.buildCoursePayDescription(courseOrder));
                requestObj.setAttach(scene);
                payStatus = courseOrder.getPayStatus();
                payOrder = courseOrder;
                break;
            }
            case "coupon": {
                // T29.4 \u6b21\u6570\u5238\uff1a\u590d\u7528 la_recharge_order + \u73b0\u6709\u652f\u4ed8\u57fa\u5efa\uff0cattach=coupon \u7531\u56de\u8c03\u53d1\u5238\u3002
                RechargeOrder couponOrder = this.requireOwnOrder(orderId, currentUserId);
                Assert.isTrue(couponOrder.getCouponGoodsId() != null && couponOrder.getCouponGoodsId() > 0, (String)"\u8ba2\u5355\u4e0d\u662f\u5238\u8ba2\u5355");
                requestObj.setUserId(couponOrder.getUserId());
                requestObj.setOutTradeNo(couponOrder.getSn());
                requestObj.setOrderAmount(couponOrder.getOrderAmount());
                requestObj.setDescription(this.iPayService.buildCouponPayDescription(couponOrder));
                requestObj.setAttach(scene);
                payStatus = couponOrder.getPayStatus();
                payOrder = couponOrder;
                break;
            }
            case "activity": {
                // T29.7 \u6d3b\u52a8\u62a5\u540d\uff1a\u590d\u7528 la_recharge_order\uff0cattach=activity \u7531\u56de\u8c03\u63a8\u8fdb\u62a5\u540d\u660e\u7ec6\u3002
                RechargeOrder activityOrder = this.requireOwnOrder(orderId, currentUserId);
                Assert.isTrue(activityOrder.getActivityId() != null && activityOrder.getActivityId() > 0, (String)"\u8ba2\u5355\u4e0d\u662f\u6d3b\u52a8\u8ba2\u5355");
                requestObj.setUserId(activityOrder.getUserId());
                requestObj.setOutTradeNo(activityOrder.getSn());
                requestObj.setOrderAmount(activityOrder.getOrderAmount());
                requestObj.setDescription(this.iPayService.buildActivityPayDescription(activityOrder));
                requestObj.setAttach(scene);
                payStatus = activityOrder.getPayStatus();
                payOrder = activityOrder;
                break;
            }
        }
        if (payStatus == PaymentEnum.OK_PAID.getCode()) {
            throw new OperateException("\u8ba2\u5355\u5df2\u652f\u4ed8");
        }
        if (payStatus == PaymentEnum.CLOSED.getCode()) {
            throw new OperateException("\u8ba2\u5355\u5df2\u53d6\u6d88\uff0c\u8bf7\u91cd\u65b0\u4e0b\u5355");
        }
        if (payStatus != PaymentEnum.UN_PAID.getCode()) {
            throw new OperateException("\u8ba2\u5355\u72b6\u6001\u5f02\u5e38\uff0c\u8bf7\u5237\u65b0\u540e\u91cd\u8bd5");
        }
        // \u53ea\u6709\u5f85\u652f\u4ed8\u8ba2\u5355\u624d\u8bb0\u5f55\u672c\u6b21\u9009\u62e9\u7684\u652f\u4ed8\u65b9\u5f0f\uff0c\u907f\u514d\u5df2\u652f\u4ed8/\u5df2\u53d6\u6d88\u8ba2\u5355\u88ab\u5237\u5199 pay_way\u3002
        if (payOrder != null) {
            payOrder.setPayWay(payWay);
            this.rechargeOrderMapper.updateById(payOrder);
        }
        Object result = this.iPayService.prepay(requestObj, terminal, code);
        return AjaxResult.success(result);
    }

    /**
     * \u8ba2\u5355\u5f52\u5c5e\u6821\u9a8c\uff1a\u53ea\u5141\u8bb8\u64cd\u4f5c\u5f53\u524d\u767b\u5f55\u8d26\u53f7\u81ea\u5df1\u7684\u8ba2\u5355\uff0c\u9632\u6b62\u6309 id \u64cd\u4f5c\u4ed6\u4eba\u8ba2\u5355\u3002
     */
    private RechargeOrder requireOwnOrder(Integer orderId, Integer userId) {
        RechargeOrder order = orderId == null || orderId <= 0
                ? null : (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)orderId);
        if (order == null || order.getDeleteTime() != null
                || userId == null || !userId.equals(order.getUserId())) {
            throw new OperateException("\u8ba2\u5355\u4e0d\u5b58\u5728");
        }
        return order;
    }

    @NotLogin
    @PostMapping(value={"/notifyMnp"})
    @ApiOperation(value="\u5fae\u4fe1\u652f\u4ed8\u56de\u8c03")
    public AjaxResult<Object> notifyMnp(@RequestBody String jsonData, HttpServletRequest request,
                                        HttpServletResponse response) {
        try {
            SignatureHeader signatureHeader = new SignatureHeader();
            signatureHeader.setSignature(request.getHeader("wechatpay-signature"));
            signatureHeader.setNonce(request.getHeader("wechatpay-nonce"));
            signatureHeader.setSerial(request.getHeader("wechatpay-serial"));
            signatureHeader.setTimeStamp(request.getHeader("wechatpay-timestamp"));
            WxPayService wxPayService = WxPayDriver.handler((Integer)ClientEnum.MNP.getCode());
            WxPayOrderNotifyV3Result.DecryptNotifyResult notifyResult =
                    wxPayService.parseOrderNotifyV3Result(jsonData, signatureHeader).getResult();
            // 防跨商户重放：微信签名的通知可能来自其他商户/AppID，必须与本栈配置逐项一致。
            String notifyMchId = notifyResult.getMchid();
            String notifyAppId = notifyResult.getAppid();
            String expectedMchId = wxPayService.getConfig().getMchId();
            String expectedAppId = wxPayService.getConfig().getAppId();
            if (notifyMchId == null || notifyAppId == null
                    || !notifyMchId.equals(expectedMchId)
                    || !notifyAppId.equals(expectedAppId)) {
                log.warn("微信支付回调商户/AppID不匹配 mchId={} appId={}",
                        SensitiveUtils.maskIdentifier(notifyMchId), SensitiveUtils.maskIdentifier(notifyAppId));
                return AjaxResult.failed(ErrorEnum.SYSTEM_ERROR.getCode(), "回调商户不匹配");
            }
            String transactionId = notifyResult.getTransactionId();
            String outTradeNo = notifyResult.getOutTradeNo();
            String attach = notifyResult.getAttach();
            String tradeState = notifyResult.getTradeState();
            if (tradeState != null && !"SUCCESS".equalsIgnoreCase(tradeState)) {
                // 只处理支付成功通知；其它状态（如 REFUND）不触发发券/记账。
                log.warn("忽略非 SUCCESS 微信支付通知 orderSn={} tradeState={}",
                        SensitiveUtils.maskOrderSn(outTradeNo), tradeState);
                return AjaxResult.success();
            }
            if (notifyResult.getAmount() == null || notifyResult.getAmount().getTotal() == null) {
                throw new PaymentException("支付回调缺少金额");
            }
            Integer paidAmountFen = notifyResult.getAmount().getTotal();
            this.iPayService.handlePaidNotify(attach, outTradeNo, transactionId, paidAmountFen);
            return AjaxResult.success();
        } catch (Exception e) {
            // 验签/解密/金额校验/DB 异常都返回 500，让微信按策略重试；
            // 不能返回 200，否则微信会认为通知已消费，支付成功但订单永远待支付。
            log.error("微信支付回调处理失败 type={}",
                    e.getClass().getSimpleName(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return AjaxResult.failed(ErrorEnum.SYSTEM_ERROR.getCode(), "回调处理失败");
        }
    }
}

