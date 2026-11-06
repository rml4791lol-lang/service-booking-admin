package com.mdd.common.util;

/**
 * 文本小工具（#60/#71 复审收敛重复实现）。
 */
public final class TextUtils {

    private TextUtils() {
    }

    /**
     * 按字符数截断（不按字节，适用于日志/错误列宽保护）；null 返回空串。
     */
    public static String truncate(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}
