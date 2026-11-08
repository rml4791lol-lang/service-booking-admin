package com.mdd.admin.service.impl;

/**
 * 老课包课时调整规则断言器（纯 Java，无 JUnit；本仓库离线仓库无 surefire）。
 *
 * <p>红线：剩余课时必须落在 [0, 总课时]；写入 used_lessons = total - remain，
 * 保证三列自洽，避免出现「剩余改了、已用没改」的账面漂移。</p>
 *
 * <p>执行：bash tools/admin/run-user-course-adjust-test.sh</p>
 */
public final class UserCourseLessonAdjustmentTest {

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        remainOnlyRecomputesUsed();
        totalOnlyKeepsRemain();
        bothFields();
        boundariesAllowed();
        remainAboveTotalRejected();
        negativeRejected();
        emptyAdjustmentRejected();

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void remainOnlyRecomputesUsed() {
        System.out.println("[只调剩余课时]");
        UserCourseLessonAdjustment.Result r = UserCourseLessonAdjustment.resolve(15, 4, null, 3);
        check("总课时不变", r.getTotalLessons() == 15, "total=" + r.getTotalLessons());
        check("剩余=3", r.getRemainLessons() == 3, "remain=" + r.getRemainLessons());
        check("已用重算为 12", r.getUsedLessons() == 12, "used=" + r.getUsedLessons());
    }

    private static void totalOnlyKeepsRemain() {
        System.out.println("[只调总课时]");
        UserCourseLessonAdjustment.Result raise = UserCourseLessonAdjustment.resolve(15, 4, 20, null);
        check("总课时=20", raise.getTotalLessons() == 20, "total=" + raise.getTotalLessons());
        check("剩余保持 4", raise.getRemainLessons() == 4, "remain=" + raise.getRemainLessons());
        check("已用=16", raise.getUsedLessons() == 16, "used=" + raise.getUsedLessons());
    }

    private static void bothFields() {
        System.out.println("[同时调总课时和剩余]");
        UserCourseLessonAdjustment.Result r = UserCourseLessonAdjustment.resolve(15, 4, 20, 5);
        check("total=20", r.getTotalLessons() == 20, String.valueOf(r.getTotalLessons()));
        check("remain=5", r.getRemainLessons() == 5, String.valueOf(r.getRemainLessons()));
        check("used=15", r.getUsedLessons() == 15, String.valueOf(r.getUsedLessons()));
    }

    private static void boundariesAllowed() {
        System.out.println("[合法边界]");
        UserCourseLessonAdjustment.Result full = UserCourseLessonAdjustment.resolve(15, 4, null, 15);
        check("剩余=总课时（已用 0）", full.getRemainLessons() == 15 && full.getUsedLessons() == 0,
                "remain=" + full.getRemainLessons() + " used=" + full.getUsedLessons());
        UserCourseLessonAdjustment.Result zero = UserCourseLessonAdjustment.resolve(15, 4, null, 0);
        check("剩余=0（全部上完）", zero.getRemainLessons() == 0 && zero.getUsedLessons() == 15,
                "remain=" + zero.getRemainLessons() + " used=" + zero.getUsedLessons());
        UserCourseLessonAdjustment.Result zeroTotal = UserCourseLessonAdjustment.resolve(0, 0, 0, 0);
        check("总课时/剩余都为 0 合法", zeroTotal.getTotalLessons() == 0 && zeroTotal.getRemainLessons() == 0,
                "total=" + zeroTotal.getTotalLessons() + " remain=" + zeroTotal.getRemainLessons());
    }

    private static void remainAboveTotalRejected() {
        System.out.println("[非法：剩余 > 总课时]");
        checkRejected("remain 16 > total 15", 15, 4, null, 16, "剩余课时不能大于总课时");
        checkRejected("调低总课时后 remain 超界", 15, 4, 3, 5, "剩余课时不能大于总课时");
    }

    private static void negativeRejected() {
        System.out.println("[非法：负数]");
        checkRejected("remain=-1", 15, 4, null, -1, "课时不能为负数");
        checkRejected("total=-1", 15, 4, -1, null, "课时不能为负数");
    }

    private static void emptyAdjustmentRejected() {
        System.out.println("[非法：两个字段都没填]");
        checkRejected("无调整项", 15, 4, null, null, "请至少调整总课时或剩余课时");
    }

    private static void checkRejected(String name, int oldTotal, int oldRemain,
                                      Integer newTotal, Integer newRemain, String expectedMessage) {
        try {
            UserCourseLessonAdjustment.resolve(oldTotal, oldRemain, newTotal, newRemain);
            check(name, false, "未抛异常");
        } catch (RuntimeException e) {
            check(name, expectedMessage.equals(e.getMessage()), "message=" + e.getMessage());
        }
    }

    private static void check(String name, boolean ok, String detail) {
        if (ok) {
            passed += 1;
            System.out.println("  ✓ " + name + (detail.isEmpty() ? "" : "（" + detail + "）"));
        } else {
            failed += 1;
            System.out.println("  ✗ " + name + (detail.isEmpty() ? "" : "（" + detail + "）"));
        }
    }
}
