/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request
 *  com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request$Amount
 *  com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request
 *  com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request$Amount
 *  com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request$H5Info
 *  com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request$Payer
 *  com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request$SceneInfo
 *  com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result
 *  com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum
 *  com.github.binarywang.wxpay.config.WxPayConfig
 *  com.github.binarywang.wxpay.exception.WxPayException
 *  com.github.binarywang.wxpay.service.WxPayService
 *  javax.annotation.Resource
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 */
package com.mdd.common.plugin.wechat;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.mdd.common.entity.setting.DevPayConfig;
import com.mdd.common.enums.ClientEnum;
import com.mdd.common.mapper.setting.DevPayConfigMapper;
import com.mdd.common.plugin.wechat.request.PaymentRequestV3;
import com.mdd.common.plugin.wechat.request.RefundRequestV3;
import com.mdd.common.util.AmountUtil;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.IpUtils;
import com.mdd.common.util.MapUtils;
import com.mdd.common.util.RequestUtils;
import com.mdd.common.util.SensitiveUtils;
import com.mdd.common.util.StringUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WxPayDriver {
    private static final Logger log = LoggerFactory.getLogger(WxPayDriver.class);
    private static DevPayConfigMapper devPayConfigMapper;
    private static WxPayService wxPayService;
    private static WxPayService wxPayMnpService;

    @Resource
    public void setDevPayConfigMapper(DevPayConfigMapper devPayConfigMapper) {
        WxPayDriver.devPayConfigMapper = devPayConfigMapper;
    }

    @Resource
    public void setWxPayService(WxPayService wxPayService) {
        WxPayDriver.wxPayService = wxPayService;
    }

    @Autowired
    public void setWxPayMnpService(WxPayService wxPayMnpService) {
        WxPayDriver.wxPayMnpService = wxPayMnpService;
    }

    public static Object unifiedOrder(PaymentRequestV3 requestV3) throws Exception {
        Integer terminal = requestV3.getTerminal();
        String openId = requestV3.getOpenId();
        String attach = requestV3.getAttach();
        String outTradeNo = requestV3.getOutTradeNo();
        BigDecimal orderAmount = requestV3.getOrderAmount();
        String description = requestV3.getDescription();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Long expireTime = System.currentTimeMillis() + 1800000L;
        String timeExpire = format.format(expireTime) + "+08:00";
        WxPayUnifiedOrderV3Request wxPayUnifiedOrderV3Request = new WxPayUnifiedOrderV3Request();
        wxPayUnifiedOrderV3Request.setOutTradeNo(outTradeNo);
        wxPayUnifiedOrderV3Request.setDescription(description);
        wxPayUnifiedOrderV3Request.setTimeExpire(timeExpire);
        wxPayUnifiedOrderV3Request.setAttach(attach);
        wxPayUnifiedOrderV3Request.setNotifyUrl(RequestUtils.uri() + "/api/pay/notifyMnp");
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        amount.setTotal(Integer.valueOf(AmountUtil.yuan2Fen(orderAmount.toPlainString())));
        amount.setCurrency("CNY");
        wxPayUnifiedOrderV3Request.setAmount(amount);
        WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
        payer.setOpenid(openId);
        log.info("微信下单参数 notifyUrl={}, outTradeNo={}, totalFen={}, openIdLen={}",
                wxPayUnifiedOrderV3Request.getNotifyUrl(), outTradeNo, amount.getTotal(),
                openId == null ? 0 : openId.length());
        TradeTypeEnum tradeTypeEnum = TradeTypeEnum.JSAPI;
        if (terminal.intValue() == ClientEnum.H5.getCode()) {
            tradeTypeEnum = TradeTypeEnum.H5;
            WxPayUnifiedOrderV3Request.SceneInfo sceneInfo = new WxPayUnifiedOrderV3Request.SceneInfo();
            WxPayUnifiedOrderV3Request.H5Info h5Info = new WxPayUnifiedOrderV3Request.H5Info();
            h5Info.setType(RequestUtils.device());
            sceneInfo.setH5Info(h5Info);
            sceneInfo.setPayerClientIp(IpUtils.getHostIp());
            wxPayUnifiedOrderV3Request.setSceneInfo(sceneInfo);
        }
        if (terminal.intValue() == ClientEnum.PC.getCode()) {
            payer.setOpenid(null);
            tradeTypeEnum = TradeTypeEnum.NATIVE;
        }
        WxPayService wxPayService = WxPayDriver.handler(terminal);
        wxPayUnifiedOrderV3Request.setPayer(payer);
        // v3 createOrderV3 对 JSAPI 返回 WxPayUnifiedOrderV3Result.JsapiResult，
        // 该对象已含 appId/timeStamp/nonceStr/packageValue/signType/paySign；
        // 不能再强转 WxPayUnifiedOrderV3Result 或调用基类 getPayInfo（会 ClassCastException）。
        // 直接返回，由 PayServiceImpl 把 packageValue 映射为小程序需要的 package。
        return wxPayService.createOrderV3(tradeTypeEnum, wxPayUnifiedOrderV3Request);
    }

    public static WxPayRefundV3Result refund(RefundRequestV3 request) throws WxPayException {
        WxPayRefundV3Request requestObj = new WxPayRefundV3Request();
        requestObj.setTransactionId(request.getTransactionId());
        requestObj.setOutTradeNo(request.getOutTradeNo());
        requestObj.setOutRefundNo(request.getOutRefundNo());
        requestObj.setReason(request.getReason());
        requestObj.setNotifyUrl(request.getNotifyUrl());
        requestObj.setSubMchid(request.getSubMchid());
        requestObj.setGoodsDetails(request.getGoodsDetails());
        WxPayRefundV3Request.Amount amount = new WxPayRefundV3Request.Amount();
        amount.setRefund(request.getRefundAmount());
        amount.setTotal(request.getTotalAmount());
        amount.setCurrency(StringUtils.isEmpty(request.getCurrency()) ? "CNY" : request.getCurrency());
        requestObj.setAmount(amount);
        WxPayService wxPayService = WxPayDriver.handler(4);
        return wxPayService.refundV3(requestObj);
    }

    /** 微信退款对账查询（#214）：按商户退款单号查询渠道最终状态。 */
    public static WxPayRefundQueryV3Result queryRefund(String outRefundNo) throws WxPayException {
        return WxPayDriver.handler(4).refundQueryV3(outRefundNo);
    }

    public static WxPayService handler(Integer terminal) {
        if (ClientEnum.MNP.getCode() == terminal.intValue()) {
            WxPayDriver.resetConfig("mnp");
            return wxPayMnpService;
        }
        WxPayDriver.resetConfig("oa");
        return wxPayService;
    }

    private static void resetConfig(String type) {
        DevPayConfig config = (DevPayConfig)devPayConfigMapper.selectOne(new QueryWrapper<DevPayConfig>().eq("pay_way", (Object)2).last("limit 1"));
        if (config == null || config.getConfig() == null) {
            log.error("\u5fae\u4fe1\u652f\u4ed8\u914d\u7f6e\u4e0d\u5b58\u5728");
            return;
        }
        String scene = type.equals("oa") ? "oa_setting" : "mnp_setting";
        String appId = ConfigUtils.get(scene, "app_id", "");
        Map<String, String> params = MapUtils.jsonToMap(config.getConfig().toString());
        String mchId = params.get("mch_id");
        String paySignKey = params.get("pay_sign_key");
        byte[] privateCert = params.getOrDefault("apiclient_cert", params.getOrDefault("private_cert", "")).getBytes();
        byte[] privateKey = params.getOrDefault("apiclient_key", params.getOrDefault("private_key", "")).getBytes();
        // 只记录“是否已配置”，不打印 AppID/商户号/证书内容/私钥内容。
        log.info("微信支付配置加载 type={}, appId={}, mchId={}, certProvided={}, keyProvided={}",
                type, SensitiveUtils.maskIdentifier(appId), SensitiveUtils.maskIdentifier(mchId),
                privateCert.length > 0, privateKey.length > 0);
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(appId);
        payConfig.setMchId(mchId);
        payConfig.setApiV3Key(paySignKey);
        payConfig.setPrivateKeyContent(privateKey);
        payConfig.setPrivateCertContent(privateCert);
        payConfig.setUseSandboxEnv(false);
        if (!type.equals("oa")) {
            wxPayMnpService.setConfig(payConfig);
        } else {
            wxPayService.setConfig(payConfig);
        }
    }
}

