package com.mdd.common.util;

import com.mdd.common.config.GlobalConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日志/终端输出脱敏工具（T30.11）。
 *
 * <p>安全红线：微信 openid、手机号、金额、token、AppSecret、APIv3 密钥、商户证书
 * 不得以原文进入日志。所有对外输出（服务日志、验证脚本、报告）统一走这里。</p>
 *
 * <p>开关在 {@code like.log.*}：默认全部开启；本地排障可显式关闭，生产必须保持开启。
 * 无论开关如何，密钥类内容（secret/cert/key/token）永远不输出原文。</p>
 */
public final class SensitiveUtils {

    /** URL 查询串中的敏感参数（微信 code2session、OAuth、支付配置等）。 */
    private static final Pattern URL_SECRET_PARAM = Pattern.compile(
            "(?i)([?&](?:secret|app_?id|app_?secret|api_?v3_?key|pay_?sign_?key|access_?token|"
                    + "session_?key|password|token|js_?code|code)=)([^&#]*)");

    /** JSON 中的敏感字段（HttpUtils 会打印微信响应）。 */
    private static final Pattern JSON_SECRET_FIELD = Pattern.compile(
            "(?i)(\"(?:openid|unionid|session_key|access_token|app_?id|app_?secret|secret|"
                    + "api_?v3_?key|pay_?sign_?key|password|mobile|phone|js_?code)\"\\s*:\\s*\")([^\"]*)(\")");

    private static final Pattern MOBILE = Pattern.compile("(?<!\\d)(1[3-9]\\d{9})(?!\\d)");

    private SensitiveUtils() {
    }

    /** 微信 openid/unionid：保留前 4 后 4。 */
    public static String maskOpenid(String value) {
        if (!Boolean.TRUE.equals(GlobalConfig.maskOpenid)) {
            return value;
        }
        return maskMiddle(value, 4, 4);
    }

    /** 手机号：保留前 3 后 4。 */
    public static String maskMobile(String value) {
        if (!Boolean.TRUE.equals(GlobalConfig.maskMobile)) {
            return value;
        }
        if (value == null || value.trim().isEmpty()) {
            return value;
        }
        String text = value.trim();
        if (text.length() == 11 && MOBILE.matcher(text).matches()) {
            return text.substring(0, 3) + "****" + text.substring(7);
        }
        return maskMiddle(text, 2, 2);
    }

    /** 金额：开启脱敏后只给占位，避免日志被用于对账/侧信道。 */
    public static String maskAmount(Object value) {
        if (!Boolean.TRUE.equals(GlobalConfig.maskAmount)) {
            return value == null ? "" : String.valueOf(value);
        }
        return value == null ? "" : "***";
    }

    /** token/secret/证书：永不输出原文，只保留长度或固定占位。 */
    public static String maskSecret(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        return "***(" + value.length() + ")";
    }

    /** AppID/商户号：保留前 4 后 2；它们不是密钥，但也没必要原样进日志。 */
    public static String maskIdentifier(String value) {
        return maskMiddle(value, 4, 2);
    }

    /** 订单号：保留前 4 后 4，排查时仍可与后台单据对上。 */
    public static String maskOrderSn(String value) {
        return maskMiddle(value, 4, 4);
    }

    /** 把 URL 里的密钥/一次性 code 替换成 ***，保留主机与路径便于排查。 */
    public static String maskUrl(String url) {
        if (url == null || url.isEmpty()) {
            return url;
        }
        Matcher matcher = URL_SECRET_PARAM.matcher(url);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(matcher.group(1) + "***"));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    /** 把微信 JSON 响应里的敏感字段替换成 ***，其余字段保留。 */
    public static String maskJson(String json) {
        if (json == null || json.isEmpty()) {
            return json;
        }
        Matcher matcher = JSON_SECRET_FIELD.matcher(json);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(matcher.group(1) + "***" + matcher.group(3)));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    /** 文本里的中国大陆手机号。 */
    public static String maskMobileInText(String text) {
        if (text == null || text.isEmpty() || !Boolean.TRUE.equals(GlobalConfig.maskMobile)) {
            return text;
        }
        Matcher matcher = MOBILE.matcher(text);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String mobile = matcher.group(1);
            matcher.appendReplacement(buffer,
                    Matcher.quoteReplacement(mobile.substring(0, 3) + "****" + mobile.substring(7)));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private static String maskMiddle(String value, int prefix, int suffix) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        String text = value.trim();
        if (text.length() <= prefix + suffix) {
            return "***";
        }
        return text.substring(0, prefix) + "***" + text.substring(text.length() - suffix);
    }
}
