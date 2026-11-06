/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.alipay.api.internal.util.AlipaySignature
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.config.AlipayConfig
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.front.controller.AliPayController
 *  com.mdd.front.service.IRechargeService
 *  javax.annotation.Resource
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.front.controller;

import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.mdd.common.aop.NotLogin;
import com.mdd.common.config.AlipayConfig;
import com.mdd.common.util.ConfigUtils;
import com.mdd.front.service.IRechargeService;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/pay/ali"})
public class AliPayController {
    private static final Logger log = LoggerFactory.getLogger(AliPayController.class);
    @Resource
    IRechargeService iRechargeService;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @PostMapping(value={"/notify/order"})
    @NotLogin
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        try {
            request.setCharacterEncoding("utf-8");
            HashMap<String, String> params = new HashMap<String, String>(8);
            Map<String, String[]> requestParams = request.getParameterMap();
            for (Map.Entry<String, String[]> stringEntry : requestParams.entrySet()) {
                String[] values = (String[])stringEntry.getValue();
                String valueStr = "";
                for (int i = 0; i < values.length; ++i) {
                    valueStr = i == values.length - 1 ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put((String)stringEntry.getKey(), valueStr);
            }
            boolean signVerified = AlipaySignature.rsaCheckV1(params, (String)ConfigUtils.getAliDevPay((String)"ali_public_key"), (String)AlipayConfig.CHARSET, (String)AlipayConfig.SIGN_TYPE);
            if (!signVerified) {
                log.error("\u9a8c\u7b7e\u5931\u8d25");
                out.print("fail");
                return;
            }
            String notifyId = new String(((String)params.get("notify_id")).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            String notifyTime = new String(((String)params.get("notify_time")).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            String outTradeNo = new String(((String)params.get("out_trade_no")).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            String tradeNo = new String(((String)params.get("trade_no")).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            String totalAmount = new String(((String)params.get("total_amount")).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            String tradeStatus = new String(((String)params.get("trade_status")).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            JSONObject passbackParams = JSONObject.parseObject((String)new String(((String)params.get("passback_params")).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            String from = passbackParams.getString("from");
            if ("TRADE_FINISHED".equals(tradeStatus)) {
            } else if ("TRADE_SUCCESS".equals(tradeStatus)) {
                if (from.equals("recharge")) {
                    this.iRechargeService.updatePayOrderStatusToPaid(outTradeNo, tradeNo);
                }
                if (from.equals("member")) {
                    return;
                }
            }
        }
        finally {
            out.print("fail");
        }
    }
}

