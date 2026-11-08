/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.binarywang.wx.miniapp.api.WxMaService
 *  cn.binarywang.wx.miniapp.config.WxMaConfig
 *  cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl
 *  javax.annotation.Resource
 *  me.chanjar.weixin.mp.api.WxMpService
 *  me.chanjar.weixin.mp.config.WxMpConfigStorage
 *  me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl
 *  org.springframework.stereotype.Component
 */
package com.mdd.common.plugin.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.HttpUtils;
import com.mdd.common.util.MapUtils;
import com.mdd.common.util.StringUtils;
import java.security.MessageDigest;
import java.util.Map;
import javax.annotation.Resource;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.stereotype.Component;

@Component
public class WxMnpDriver {
    private static WxMaService wxMaService;
    private static WxMpService wxMpService;

    @Resource
    public void setWxMaService(WxMaService wxMaService) {
        WxMnpDriver.wxMaService = wxMaService;
    }

    @Resource
    public void setWxOaService(WxMpService wxMpService) {
        WxMnpDriver.wxMpService = wxMpService;
    }

    public static WxMaService mnp() {
        Map<String, String> config = ConfigUtils.get("mnp_setting");
        WxMaDefaultConfigImpl wxConfig = new WxMaDefaultConfigImpl();
        wxConfig.setAppid(config.getOrDefault("app_id", ""));
        wxConfig.setSecret(config.getOrDefault("app_secret", ""));
        wxMaService.setWxMaConfig((WxMaConfig)wxConfig);
        return wxMaService;
    }

    public static WxMpService oa() {
        Map<String, String> config = ConfigUtils.get("oa_setting");
        WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
        wxMpDefaultConfig.setAppId(config.getOrDefault("app_id", "").trim());
        wxMpDefaultConfig.setSecret(config.getOrDefault("app_secret", "").trim());
        wxMpDefaultConfig.setToken(config.getOrDefault("token", "").trim());
        wxMpDefaultConfig.setAesKey(config.getOrDefault("encoding_aes_key", "").trim());
        wxMpService.setWxMpConfigStorage((WxMpConfigStorage)wxMpDefaultConfig);
        return wxMpService;
    }

    public static WxMpService openSetting() {
        Map<String, String> config = ConfigUtils.get("open_platform");
        WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
        wxMpDefaultConfig.setAppId(config.getOrDefault("app_id", "").trim());
        wxMpDefaultConfig.setSecret(config.getOrDefault("app_secret", "").trim());
        wxMpDefaultConfig.setToken(config.getOrDefault("token", "").trim());
        wxMpDefaultConfig.setAesKey(config.getOrDefault("encoding_aes_key", "").trim());
        wxMpService.setWxMpConfigStorage((WxMpConfigStorage)wxMpDefaultConfig);
        return wxMpService;
    }

    public static String getJsSdkGetTicket(String accessToken) {
        String jsSdkGetTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
        String url = String.format(jsSdkGetTicketUrl, accessToken);
        String results = HttpUtils.sendGet(url);
        Map<String, String> resultMap = MapUtils.jsonToMap(results);
        String jsapi_ticket = null;
        if (resultMap != null && StringUtils.isNotNull(resultMap.get("ticket"))) {
            jsapi_ticket = resultMap.get("ticket");
        }
        return jsapi_ticket;
    }

    public static String buildJSSDKSignature(String ticket, String timestamp, String nonceStr, String url) throws Exception {
        String orderedString = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
        return WxMnpDriver.sha1(orderedString);
    }

    private static String sha1(String orderedString) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(orderedString.getBytes());
        String ciphertext = WxMnpDriver.byteToStr(digest);
        return ciphertext.toLowerCase();
    }

    private static String byteToStr(byte[] byteArray) {
        StringBuilder strDigest = new StringBuilder();
        for (byte b : byteArray) {
            strDigest.append(WxMnpDriver.byteToHexStr(b));
        }
        return strDigest.toString();
    }

    private static String byteToHexStr(byte mByte) {
        char[] Digit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[]{Digit[mByte >>> 4 & 0xF], Digit[mByte & 0xF]};
        return new String(tempArr);
    }
}

