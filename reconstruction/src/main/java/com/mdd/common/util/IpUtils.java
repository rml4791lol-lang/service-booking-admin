/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  javax.servlet.http.HttpServletRequest
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.mdd.common.util;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.util.HttpUtils;
import com.mdd.common.util.RequestUtils;
import com.mdd.common.util.StringUtils;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IpUtils {
    private static final Logger log = LoggerFactory.getLogger(IpUtils.class);

    public static String getIpAddress() {
        HttpServletRequest request = RequestUtils.handler();
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : IpUtils.getMultistageReverseProxyIp(ip);
    }

    public static String getRealAddressByIP(String ip) {
        String IP_URL = "https://whois.pconline.com.cn/ipJson.jsp";
        String UNKNOWN = "XX XX";
        if (IpUtils.internalIp(ip)) {
            return "\u5185\u7f51IP";
        }
        if (GlobalConfig.isAddressEnabled.booleanValue()) {
            try {
                String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", "GBK");
                if (StringUtils.isEmpty(rspStr)) {
                    log.error("\u83b7\u53d6\u5730\u7406\u4f4d\u7f6e\u5f02\u5e38 {}", (Object)ip);
                    return UNKNOWN;
                }
                JSONObject obj = JSONObject.parseObject((String)rspStr);
                String region = obj.getString("pro");
                String city = obj.getString("city");
                return String.format("%s %s", region, city);
            }
            catch (Exception e) {
                log.error("\u83b7\u53d6\u5730\u7406\u4f4d\u7f6e\u5f02\u5e38 {}", (Object)ip);
            }
        }
        return UNKNOWN;
    }

    public static boolean internalIp(String ip) {
        byte[] address = IpUtils.textToNumericFormatV4(ip);
        return IpUtils.internalIp(address) || "127.0.0.1".equals(ip);
    }

    private static boolean internalIp(byte[] address) {
        if (address == null || address.length < 2) {
            return true;
        }
        byte b0 = address[0];
        byte b1 = address[1];
        int SECTION_1 = 10;
        int SECTION_2 = -84;
        int SECTION_3 = 16;
        int SECTION_4 = 31;
        int SECTION_5 = -64;
        int SECTION_6 = -88;
        switch (b0) {
            case 10: {
                return true;
            }
            case -84: {
                if (b1 >= 16 && b1 <= 31) {
                    return true;
                }
            }
            case -64: {
                if (b1 != -88) break;
                return true;
            }
        }
        return false;
    }

    public static byte[] textToNumericFormatV4(String text) {
        if (text.length() == 0) {
            return null;
        }
        byte[] bytes = new byte[4];
        String[] elements = text.split("\\.", -1);
        try {
            switch (elements.length) {
                case 1: {
                    long l = Long.parseLong(elements[0]);
                    if (l < 0L || l > 0xFFFFFFFFL) {
                        return null;
                    }
                    bytes[0] = (byte)(l >> 24 & 0xFFL);
                    bytes[1] = (byte)((l & 0xFFFFFFL) >> 16 & 0xFFL);
                    bytes[2] = (byte)((l & 0xFFFFL) >> 8 & 0xFFL);
                    bytes[3] = (byte)(l & 0xFFL);
                    break;
                }
                case 2: {
                    long l = Integer.parseInt(elements[0]);
                    if (l < 0L || l > 255L) {
                        return null;
                    }
                    bytes[0] = (byte)(l & 0xFFL);
                    l = Integer.parseInt(elements[1]);
                    if (l < 0L || l > 0xFFFFFFL) {
                        return null;
                    }
                    bytes[1] = (byte)(l >> 16 & 0xFFL);
                    bytes[2] = (byte)((l & 0xFFFFL) >> 8 & 0xFFL);
                    bytes[3] = (byte)(l & 0xFFL);
                    break;
                }
                case 3: {
                    long l;
                    for (int i = 0; i < 2; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if (l < 0L || l > 255L) {
                            return null;
                        }
                        bytes[i] = (byte)(l & 0xFFL);
                    }
                    l = Integer.parseInt(elements[2]);
                    if (l < 0L || l > 65535L) {
                        return null;
                    }
                    bytes[2] = (byte)(l >> 8 & 0xFFL);
                    bytes[3] = (byte)(l & 0xFFL);
                    break;
                }
                case 4: {
                    for (int i = 0; i < 4; ++i) {
                        long l = Integer.parseInt(elements[i]);
                        if (l < 0L || l > 255L) {
                            return null;
                        }
                        bytes[i] = (byte)(l & 0xFFL);
                    }
                    break;
                }
                default: {
                    return null;
                }
            }
        }
        catch (NumberFormatException e) {
            return null;
        }
        return bytes;
    }

    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException unknownHostException) {
            return "127.0.0.1";
        }
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException unknownHostException) {
            return "\u672a\u77e5";
        }
    }

    public static String getMultistageReverseProxyIp(String ip) {
        if (ip != null && ip.indexOf(",") > 0) {
            String[] ips;
            for (String subIp : ips = ip.trim().split(",")) {
                if (IpUtils.isUnknown(subIp)) continue;
                ip = subIp;
                break;
            }
        }
        return ip;
    }

    public static boolean isUnknown(String checkString) {
        return StringUtils.isBlank((CharSequence)checkString) || "unknown".equalsIgnoreCase(checkString);
    }
}

