package com.mdd.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 课节场次时间判定（注销账号「未完成预约」与退款后「未上课预约取消」共用口径）。
 *
 * <p>预约 {@code la_course_booking.status=1} 表示已预约未点名；只有场次结束时间
 * 还没到的预约才算「未完成/未上课」。场次结束时间 = {@code lesson_date + time_slot}
 * 的结束时刻（如 {@code 2026-11-05 09:00-10:00} → 2026-11-05 10:00）。</p>
 *
 * <p>保守策略：时段/日期缺失或解析失败时一律视为<b>未结束</b>——宁可拦下注销、
 * 多取消一条预约，也不把仍在进行的课当成已结束漏掉（漏取消会让已退费用户继续上课）。</p>
 */
public final class CourseBookingUtil {

    /** 预约状态：1=已预约（未点名）。 */
    public static final int STATUS_BOOKED = 1;
    /** 预约状态：3=已取消。 */
    public static final int STATUS_CANCELLED = 3;

    /** 时段里的钟点可能写成 9:00（单数字小时），ISO_LOCAL_TIME 不接受，统一按 H:mm 解析。 */
    private static final DateTimeFormatter CLOCK = DateTimeFormatter.ofPattern("H:mm");

    private CourseBookingUtil() {
    }

    public static boolean isLessonEnded(String lessonDate, String timeSlot) {
        return isLessonEnded(lessonDate, timeSlot, LocalDateTime.now());
    }

    public static boolean isLessonEnded(String lessonDate, String timeSlot, LocalDateTime now) {
        if (lessonDate == null || lessonDate.trim().isEmpty()) {
            return false;
        }
        LocalDate date;
        try {
            date = LocalDate.parse(lessonDate.trim());
        } catch (DateTimeParseException e) {
            return false;
        }
        String[] parts = splitTimeSlot(timeSlot);
        if (parts == null) {
            // 没有可用时段：当天无法判断结束时刻，按未结束处理；只有日期已过才算结束。
            return date.isBefore(now.toLocalDate());
        }
        LocalTime start;
        LocalTime end;
        try {
            start = LocalTime.parse(parts[0], CLOCK);
            end = LocalTime.parse(parts[1], CLOCK);
        } catch (DateTimeParseException e) {
            return date.isBefore(now.toLocalDate());
        }
        LocalDateTime endDateTime = LocalDateTime.of(date, end);
        if (end.isBefore(start)) {
            // 跨天时段（如 22:00-01:00）：结束落在次日
            endDateTime = endDateTime.plusDays(1);
        }
        return now.isAfter(endDateTime);
    }

    /** "09:00-10:00" / " 9:00 - 10:30 " → ["09:00","10:30"]；格式不对返回 null。 */
    private static String[] splitTimeSlot(String timeSlot) {
        if (timeSlot == null) {
            return null;
        }
        String slot = timeSlot.trim();
        int dash = slot.indexOf('-');
        if (dash <= 0 || dash >= slot.length() - 1) {
            return null;
        }
        String start = slot.substring(0, dash).trim();
        String end = slot.substring(dash + 1).trim();
        if (start.isEmpty() || end.isEmpty()) {
            return null;
        }
        return new String[]{start, end};
    }
}
