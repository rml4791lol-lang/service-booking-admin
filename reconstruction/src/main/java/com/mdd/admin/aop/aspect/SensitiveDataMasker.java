package com.mdd.admin.aop.aspect;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 操作日志敏感数据脱敏器。
 *
 * <p>日志不得保存原始请求体：结构化参数按字段名脱敏，字符串中的手机号、身份证号、
 * 银行卡号再做值脱敏；无法解析的查询串/文本同样兜底。输出长度受限，避免大请求体
 * 长期占用日志存储。</p>
 */
public final class SensitiveDataMasker {
    private static final String REDACTED = "<redacted>";
    private static final int MAX_LOG_LENGTH = 4096;
    private static final Pattern MOBILE = Pattern.compile("(?<!\\d)(1[3-9]\\d)\\d{4}(\\d{4})(?!\\d)");
    private static final Pattern ID_CARD = Pattern.compile("(?<!\\d)(\\d{6})\\d{8}([0-9Xx]{4})(?!\\d)");
    private static final Pattern BANK_CARD = Pattern.compile("(?<!\\d)(\\d{4})\\d{8,11}(\\d{4})(?!\\d)");
    private static final Pattern PLAIN_KEY_VALUE = Pattern.compile(
            "(?i)(password|passwd|pwd|token|secret|id[_-]?card|identity[_-]?card|bank[_-]?card|card[_-]?no|mobile|phone|tel|email|address)\\s*=\\s*([^&\\s,;]+)");
    private static final Pattern QUOTED_JSON_VALUE = Pattern.compile(
            "(?i)(\"(?:password|passwd|pwd|token|secret|id[_-]?card|identity[_-]?card|bank[_-]?card|card[_-]?no|mobile|phone|tel|email|address)\"\\s*:\\s*)\"[^\"]*\"");

    private SensitiveDataMasker() {
    }

    public static String maskJson(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return raw == null ? "" : raw;
        }
        String masked;
        try {
            Object parsed = JSON.parse(raw);
            maskNode(parsed);
            masked = JSON.toJSONString(parsed);
        } catch (Exception ignored) {
            masked = maskPlain(raw);
        }
        return truncate(masked);
    }

    public static String maskMobile(String value) {
        if (value == null || value.length() < 7) {
            return value;
        }
        return MOBILE.matcher(value).replaceAll("$1****$2");
    }

    public static String maskBankCard(String value) {
        if (value == null || value.length() < 9) {
            return value;
        }
        return BANK_CARD.matcher(value).replaceAll("$1 **** **** $2");
    }

    public static String maskIdCard(String value) {
        if (value == null || value.length() < 10) {
            return value;
        }
        return ID_CARD.matcher(value).replaceAll("$1********$2");
    }

    private static void maskNode(Object node) {
        if (node instanceof JSONObject) {
            JSONObject object = (JSONObject) node;
            List<String> keys = new ArrayList<>(object.keySet());
            for (String key : keys) {
                Object value = object.get(key);
                if (isSensitiveKey(key)) {
                    object.put(key, REDACTED);
                } else if (value instanceof String) {
                    object.put(key, maskPlain((String) value));
                } else {
                    maskNode(value);
                }
            }
            return;
        }
        if (node instanceof JSONArray) {
            JSONArray array = (JSONArray) node;
            for (int i = 0; i < array.size(); i++) {
                Object item = array.get(i);
                if (item instanceof String) {
                    // 数组内的裸字符串无法判断字段语义，只做值级识别。
                    array.set(i, maskPlain((String) item));
                    continue;
                }
                maskNode(item);
            }
        }
    }

    private static boolean isSensitiveKey(String key) {
        if (key == null) {
            return false;
        }
        String normalized = key.replace("_", "").replace("-", "").toLowerCase(Locale.ROOT);
        return normalized.contains("password")
                || normalized.contains("passwd")
                || normalized.equals("pwd")
                || normalized.contains("token")
                || normalized.contains("secret")
                || normalized.contains("credential")
                || normalized.contains("account")
                || normalized.contains("idcard")
                || normalized.contains("identitycard")
                || normalized.contains("certno")
                || normalized.contains("bankcard")
                || normalized.contains("cardno")
                || normalized.contains("mobile")
                || normalized.contains("phone")
                || normalized.equals("tel")
                || normalized.contains("email")
                || normalized.contains("address")
                || normalized.contains("openid")
                || normalized.contains("unionid")
                || normalized.contains("realname");
    }

    private static String maskPlain(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        String masked = QUOTED_JSON_VALUE.matcher(text).replaceAll("$1\"" + REDACTED + "\"");
        masked = replaceKeyValue(masked, PLAIN_KEY_VALUE);
        masked = ID_CARD.matcher(masked).replaceAll("$1********$2");
        masked = BANK_CARD.matcher(masked).replaceAll("$1 **** **** $2");
        masked = MOBILE.matcher(masked).replaceAll("$1****$2");
        return masked;
    }

    private static String replaceKeyValue(String input, Pattern pattern) {
        Matcher matcher = pattern.matcher(input);
        StringBuffer output = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(output, Matcher.quoteReplacement(matcher.group(1) + "=" + REDACTED));
        }
        matcher.appendTail(output);
        return output.toString();
    }

    private static String truncate(String value) {
        if (value == null || value.length() <= MAX_LOG_LENGTH) {
            return value;
        }
        return value.substring(0, MAX_LOG_LENGTH) + "...<truncated>";
    }
}
