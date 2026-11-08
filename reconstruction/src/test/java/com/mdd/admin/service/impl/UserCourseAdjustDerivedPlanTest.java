package com.mdd.admin.service.impl;

/**
 * 老课包课时调整（流水口径）断言器（纯 Java，无 JUnit）。
 *
 * <p>#222：调整以「流水派生已用」为锚点写调整流水，而不是直接拿 total-remain 当已用。
 * given (字段 total/remain, 流水 fifo 已用, 已有调整增量 ΣΔ, 入参 total/remain)：</p>
 *
 * <ul>
 *     <li>显式给 remain：视为修正已用——targetUsed = total - remain，Δused = targetUsed - 流水口径已用；</li>
 *     <li>只给 total：视为增减课时——已用保持流水口径不变，remain = total - 流水口径已用；</li>
 *     <li>三列自洽红线不破：写入 used = total - remain；且流水重放（fifo + ΣΔ + Δused）= 写入 used。</li>
 * </ul>
 *
 * <p>执行：bash tools/admin/run-user-course-adjust-test.sh</p>
 */
public final class UserCourseAdjustDerivedPlanTest {

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        remainOnlyAnchorsOnLogs();
        remainOnlyDriftedRowFabricatesDelta();
        totalOnlyKeepsDerivedUsed();
        totalOnlyDriftedRow();
        bothFields();
        negativeDeltaAnchorReplaysExactly();
        validations();

        System.out.println();
        System.out.println("UserCourseAdjustDerivedPlanTest passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    /** 自洽行（流水口径已用 = total - 字段 remain）：行为与旧调整一致。 */
    private static void remainOnlyAnchorsOnLogs() {
        System.out.println("[只调剩余（自洽行）]");
        UserCourseAdjustDerivedPlan.Plan p = UserCourseAdjustDerivedPlan.resolve(15, 4, 11, 0, null, 3);
        check("total=15", p.getTotalLessons() == 15, String.valueOf(p.getTotalLessons()));
        check("used=12", p.getUsedLessons() == 12, "used=" + p.getUsedLessons());
        check("remain=3", p.getRemainLessons() == 3, "remain=" + p.getRemainLessons());
        check("Δused=+1", p.getChangeUsed() == 1, "Δ=" + p.getChangeUsed());
        check("流水重放=写入 used", 11 + 0 + p.getChangeUsed() == p.getUsedLessons(), "replay");
    }

    /** 漂移行（流水 0 消课、字段 used=11）：显式改剩余 = 管理员确认口径，Δused 补齐差距。 */
    private static void remainOnlyDriftedRowFabricatesDelta() {
        System.out.println("[只调剩余（漂移行：老数据无流水）]");
        UserCourseAdjustDerivedPlan.Plan p = UserCourseAdjustDerivedPlan.resolve(15, 4, 0, 0, null, 3);
        check("used=12", p.getUsedLessons() == 12, "used=" + p.getUsedLessons());
        check("remain=3", p.getRemainLessons() == 3, "remain=" + p.getRemainLessons());
        check("Δused=+12（补齐流水与账面差距）", p.getChangeUsed() == 12, "Δ=" + p.getChangeUsed());
        check("流水重放=写入 used", 0 + 0 + p.getChangeUsed() == p.getUsedLessons(), "replay");
    }

    /** 只调总课时（赠送/缩减）：已用保持流水口径，剩余随之重算。 */
    private static void totalOnlyKeepsDerivedUsed() {
        System.out.println("[只调总课时（赠送 5 节）]");
        UserCourseAdjustDerivedPlan.Plan p = UserCourseAdjustDerivedPlan.resolve(15, 4, 11, 0, 20, null);
        check("total=20", p.getTotalLessons() == 20, String.valueOf(p.getTotalLessons()));
        check("used 不变=11（流水口径）", p.getUsedLessons() == 11, "used=" + p.getUsedLessons());
        check("remain=9", p.getRemainLessons() == 9, "remain=" + p.getRemainLessons());
        check("Δused=0", p.getChangeUsed() == 0, "Δ=" + p.getChangeUsed());
    }

    private static void totalOnlyDriftedRow() {
        System.out.println("[只调总课时（漂移行：流水已用超出新总课时 → 拒绝）]");
        try {
            UserCourseAdjustDerivedPlan.resolve(15, 4, 16, 0, 10, null);
            check("total<流水已用被拒", false, "未抛异常");
        } catch (RuntimeException e) {
            check("total<流水已用被拒", String.valueOf(e.getMessage()).contains("总课时不能小于流水已用"),
                    "message=" + e.getMessage());
        }
    }

    private static void bothFields() {
        System.out.println("[同时调总课时和剩余]");
        UserCourseAdjustDerivedPlan.Plan p = UserCourseAdjustDerivedPlan.resolve(15, 4, 11, 0, 20, 5);
        check("total=20", p.getTotalLessons() == 20, String.valueOf(p.getTotalLessons()));
        check("used=15", p.getUsedLessons() == 15, "used=" + p.getUsedLessons());
        check("remain=5", p.getRemainLessons() == 5, "remain=" + p.getRemainLessons());
        check("Δused=+4", p.getChangeUsed() == 4, "Δ=" + p.getChangeUsed());
        check("流水重放=写入 used", 11 + 0 + p.getChangeUsed() == p.getUsedLessons(), "replay");
    }

    /** 已有负增量（此前退回过课时）：锚点用未截断的流水口径，保证重放精确。 */
    private static void negativeDeltaAnchorReplaysExactly() {
        System.out.println("[已有调整增量 ΣΔ=-3]");
        UserCourseAdjustDerivedPlan.Plan p = UserCourseAdjustDerivedPlan.resolve(15, 4, 11, -3, null, 2);
        check("流水口径已用=8", 11 - 3 == 8, "raw=" + (11 - 3));
        check("used=13", p.getUsedLessons() == 13, "used=" + p.getUsedLessons());
        check("remain=2", p.getRemainLessons() == 2, "remain=" + p.getRemainLessons());
        check("Δused=+5（相对流水口径 8）", p.getChangeUsed() == 5, "Δ=" + p.getChangeUsed());
        check("流水重放=写入 used", 11 - 3 + p.getChangeUsed() == p.getUsedLessons(), "replay");
    }

    private static void validations() {
        System.out.println("[非法入参]");
        checkRejected("两个字段都没填", 15, 4, 11, 0, null, null, "请至少调整总课时或剩余课时");
        checkRejected("remain > total", 15, 4, 11, 0, null, 16, "剩余课时不能大于总课时");
        checkRejected("负数 total", 15, 4, 11, 0, -1, null, "课时不能为负数");
        checkRejected("负数 remain", 15, 4, 11, 0, null, -1, "课时不能为负数");
    }

    private static void checkRejected(String name, int oldTotal, int oldRemain, int fifoUsed, int priorDelta,
                                      Integer newTotal, Integer newRemain, String expectedMessage) {
        try {
            UserCourseAdjustDerivedPlan.resolve(oldTotal, oldRemain, fifoUsed, priorDelta, newTotal, newRemain);
            check(name, false, "未抛异常");
        } catch (RuntimeException e) {
            check(name, expectedMessage.equals(String.valueOf(e.getMessage())), "message=" + e.getMessage());
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
