/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONArray
 *  com.mdd.common.enums.ErrorEnum
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.plugin.wechat.WxMnpDriver
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.front.service.IWechatService
 *  com.mdd.front.service.impl.WechatServiceImpl
 *  org.springframework.stereotype.Service
 */
package com.mdd.front.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.plugin.wechat.WxMnpDriver;
import com.mdd.common.util.ConfigUtils;
import com.mdd.front.service.IWechatService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class WechatServiceImpl
implements IWechatService {
    public Map<String, Object> jsConfig(String url) throws Exception {
        try {
            String appId = ConfigUtils.get((String)"oa_setting", (String)"app_id");
            String accessToken = WxMnpDriver.oa().getAccessToken();
            String jsapiTicket = WxMnpDriver.getJsSdkGetTicket((String)accessToken);
            String timestamp = Long.toString(System.currentTimeMillis() / 1000L);
            String nonceStr = UUID.randomUUID().toString();
            String signature = WxMnpDriver.buildJSSDKSignature((String)jsapiTicket, (String)timestamp, (String)nonceStr, (String)url);
            List<String> array = Arrays.asList("onMenuShareTimeline", "onMenuShareAppMessage", "onMenuShareQQ", "onMenuShareWeibo", "onMenuShareQZone", "openLocation", "getLocation", "chooseWXPay", "updateAppMessageShareData", "updateTimelineShareData", "openAddress", "scanQRCode");
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("url", url);
            map.put("jsapi_ticket", jsapiTicket);
            map.put("nonceStr", nonceStr);
            map.put("timestamp", timestamp);
            map.put("signature", signature);
            map.put("appId", appId);
            map.put("jsApiList", array);
            map.put("openTagList", new JSONArray());
            map.put("debug", false);
            return map;
        }
        catch (Exception e) {
            throw new OperateException(e.getMessage(), Integer.valueOf(ErrorEnum.FAILED.getCode()), Integer.valueOf(-1));
        }
    }
}

