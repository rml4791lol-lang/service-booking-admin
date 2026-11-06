package com.mdd.common.util;

import com.mdd.common.exception.OperateException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * 接口参数统一解析工具（#58）。
 *
 * <p>小程序端/后台接口大量以 {@code Map<String, Object>} 接收 body，历史上直接
 * {@code Integer.parseInt(params.get(...).toString())} / {@code LocalDate.parse(...)}，
 * 收到 {@code abc}、{@code 2024-13} 之类的脏参数时会抛未捕获异常，最终只返回
 * “系统繁忙”并带请求 ID，定位困难。</p>
 *
 * <p>本工具把“缺参/类型错误”统一转成 {@link OperateException}（中文业务文案），
 * 由全局异常处理直接回给客户端；字段标签只用于提示，不参与业务。</p>
 */
public final class ParamUtils {

    private static final Pattern MONTH_PATTERN = Pattern.compile("\\d{4}-\\d{2}");
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    private ParamUtils() {
    }

    /**
     * 解析可选整数：null/空串返回 null；格式非法抛业务异常。
     */
    public static Integer optionalInt(Object value, String label) {
        if (value == null) {
            return null;
        }
        String text = String.valueOf(value).trim();
        if (text.isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException e) {
            throw new OperateException("参数「" + label + "」格式错误，必须为整数");
        }
    }

    /**
     * 解析必填整数：缺失或格式非法均抛业务异常。
     */
    public static Integer requiredInt(Object value, String label) {
        Integer parsed = optionalInt(value, label);
        if (parsed == null) {
            throw new OperateException("缺少必填参数：" + label);
        }
        return parsed;
    }

    /**
     * 解析整数，缺失时返回默认值；格式非法仍抛业务异常。
     */
    public static Integer intOrDefault(Object value, Integer defaultValue, String label) {
        Integer parsed = optionalInt(value, label);
        return parsed == null ? defaultValue : parsed;
    }

    /**
     * 静默解析整数，仅供展示历史数据（如班级 week_days）时降级使用：
     * 非法值返回 null，不抛异常，避免单条脏数据拖垮整个列表。
     */
    public static Integer parseIntOrNullQuietly(Object value) {
        if (value == null) {
            return null;
        }
        String text = String.valueOf(value).trim();
        if (text.isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 解析 {@code yyyy-MM} 月份为当月 1 号；空值返回当前月。
     * 格式非法（含 2024-13 这类非法月份）抛业务异常。
     */
    public static LocalDate monthStart(String month) {
        if (month == null || month.trim().isEmpty()) {
            return LocalDate.now().withDayOfMonth(1);
        }
        String text = month.trim();
        if (!MONTH_PATTERN.matcher(text).matches()) {
            throw new OperateException("参数「月份」格式错误，应为 YYYY-MM");
        }
        try {
            return YearMonth.parse(text).atDay(1);
        } catch (DateTimeParseException e) {
            throw new OperateException("参数「月份」格式错误，应为 YYYY-MM");
        }
    }

    /**
     * 校验可选日期 {@code yyyy-MM-dd}：空值返回 null；非法日期抛业务异常。
     */
    public static String optionalDate(Object value, String label) {
        if (value == null) {
            return null;
        }
        String text = String.valueOf(value).trim();
        if (text.isEmpty()) {
            return null;
        }
        if (!DATE_PATTERN.matcher(text).matches()) {
            throw new OperateException("参数「" + label + "」格式错误，应为 YYYY-MM-DD");
        }
        try {
            LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw new OperateException("参数「" + label + "」格式错误，应为 YYYY-MM-DD");
        }
        return text;
    }

    /**
     * 解析必填金额：缺失/非数字抛业务异常。
     */
    public static BigDecimal requiredDecimal(Object value, String label) {
        if (value == null) {
            throw new OperateException("缺少必填参数：" + label);
        }
        String text = String.valueOf(value).trim();
        if (text.isEmpty()) {
            throw new OperateException("缺少必填参数：" + label);
        }
        try {
            return new BigDecimal(text);
        } catch (NumberFormatException e) {
            throw new OperateException("参数「" + label + "」格式错误，必须为数字");
        }
    }
}
