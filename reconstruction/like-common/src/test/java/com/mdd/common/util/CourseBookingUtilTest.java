package com.mdd.common.util;

import java.time.LocalDateTime;

/**
 * 课节「场次是否已结束」断言器（纯 Java，无 JUnit）。
 *
 * <p>注销账号的「未完成预约」判定与退款通过后的「未上课预约取消」共用同一口径：
 * 只有 status=1（已预约未点名）且场次未结束的预约才算「未来/未上课」。</p>
 *
 * <p>口径：lesson_date + time_slot 的结束时间之前都算未结束；时段缺失时按
 * 「日期未过完」处理（当天仍算未结束，宁可拦下也不误取消）；日期缺失/非法
 * 一律按未结束处理（保守，避免把仍在进行的课当成已结束）。</p>
 *
 * <p>执行：bash tools/user/run-final-backend-tests.sh</p>
 */
public final class CourseBookingUtilTest {

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        endedAfterTimeSlot();
        notEndedBeforeTimeSlot();
        notEndedSameDayWithoutTimeSlot();
        endedBeforeTodayWithoutTimeSlot();
        futureLessonNotEnded();
        invalidDateNotEnded();
        slotWithSpacesAndSingleDigitHour();
        overnightSlotEndsNextDay();

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void endedAfterTimeSlot() {
        System.out.println("[时段结束后=已结束]");
        check("10:00:01 已过 09:00-10:00",
                CourseBookingUtil.isLessonEnded("2026-11-05", "09:00-10:00",
                        LocalDateTime.of(2026, 11, 5, 10, 0, 1)));
    }

    private static void notEndedBeforeTimeSlot() {
        System.out.println("[时段结束前=未结束]");
        check("09:59 还在 09:00-10:00 内",
                !CourseBookingUtil.isLessonEnded("2026-11-05", "09:00-10:00",
                        LocalDateTime.of(2026, 11, 5, 9, 59)));
    }

    private static void notEndedSameDayWithoutTimeSlot() {
        System.out.println("[当天无时段=未结束]");
        check("当天无法判断结束时间，保守算未结束",
                !CourseBookingUtil.isLessonEnded("2026-11-05", "",
                        LocalDateTime.of(2026, 11, 5, 23, 30)));
    }

    private static void endedBeforeTodayWithoutTimeSlot() {
        System.out.println("[昨天无时段=已结束]");
        check("日期早于今天，按已结束处理",
                CourseBookingUtil.isLessonEnded("2026-11-04", "",
                        LocalDateTime.of(2026, 11, 5, 8, 0)));
    }

    private static void futureLessonNotEnded() {
        System.out.println("[未来课节=未结束]");
        check("明天 09:00-10:00 未结束",
                !CourseBookingUtil.isLessonEnded("2026-11-06", "09:00-10:00",
                        LocalDateTime.of(2026, 11, 5, 23, 59)));
    }

    private static void invalidDateNotEnded() {
        System.out.println("[非法日期=未结束]");
        check("日期无法解析时不得误判已结束",
                !CourseBookingUtil.isLessonEnded("not-a-date", "09:00-10:00",
                        LocalDateTime.of(2026, 11, 5, 12, 0)));
    }

    private static void slotWithSpacesAndSingleDigitHour() {
        System.out.println("[时段容忍空格/单数字小时]");
        check("' 9:00 - 10:30 ' 解析为 10:30 结束",
                !CourseBookingUtil.isLessonEnded("2026-11-05", " 9:00 - 10:30 ",
                        LocalDateTime.of(2026, 11, 5, 10, 29)));
        check("10:31 已结束",
                CourseBookingUtil.isLessonEnded("2026-11-05", " 9:00 - 10:30 ",
                        LocalDateTime.of(2026, 11, 5, 10, 31)));
    }

    private static void overnightSlotEndsNextDay() {
        System.out.println("[跨天时段]");
        check("22:00-01:00 次日 00:30 未结束",
                !CourseBookingUtil.isLessonEnded("2026-11-05", "22:00-01:00",
                        LocalDateTime.of(2026, 11, 6, 0, 30)));
        check("22:00-01:00 次日 01:01 已结束",
                CourseBookingUtil.isLessonEnded("2026-11-05", "22:00-01:00",
                        LocalDateTime.of(2026, 11, 6, 1, 1)));
    }

    private static void check(String name, boolean ok) {
        if (ok) {
            passed++;
            System.out.println("  PASS " + name);
        } else {
            failed++;
            System.out.println("  FAIL " + name);
        }
    }
}
