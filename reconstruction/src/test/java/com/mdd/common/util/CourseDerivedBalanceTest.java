package com.mdd.common.util;

/**
 * 课时派生余额断言器（纯 Java，无 JUnit；本仓库离线仓库无 surefire）。
 *
 * <p>#222：课时以消课流水为唯一事实。used = 有效消课流水 + 调整流水增量（clamp 到
 * [0, total]），remain = total - used，<b>不再回填库里 remain_lessons</b>（
 * {@link UserCourseBalanceUtil#resolve} 的历史兜底会把「上完课但未扣剩余」还原成没上过）。</p>
 *
 * <p>执行：bash tools/admin/run-user-course-adjust-test.sh（与调整计划断言器同批）</p>
 */
public final class CourseDerivedBalanceTest {

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        derivedFromLogsOnly();
        adjustDeltaIncluded();
        negativeDeltaReturnsLesson();
        clampLow();
        clampHigh();
        refundedKeepsStatus();
        completedWhenRemainZero();
        missingTotalTreatedAsZero();
        noStoredRemainBackfill();

        System.out.println();
        System.out.println("CourseDerivedBalanceTest passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void derivedFromLogsOnly() {
        System.out.println("[派生：只看流水]");
        CourseDerivedBalance d = CourseDerivedBalance.derive(15, 11, 0, 0);
        check("used=流水 11", d.getUsedLessons() == 11, "used=" + d.getUsedLessons());
        check("remain=total-used=4", d.getRemainLessons() == 4, "remain=" + d.getRemainLessons());
        check("status=学习中", d.getStatus() == 0, "status=" + d.getStatus());
    }

    private static void adjustDeltaIncluded() {
        System.out.println("[派生：调整流水增量并入 used]");
        CourseDerivedBalance d = CourseDerivedBalance.derive(15, 11, 1, 0);
        check("used=11+1=12", d.getUsedLessons() == 12, "used=" + d.getUsedLessons());
        check("remain=3", d.getRemainLessons() == 3, "remain=" + d.getRemainLessons());
    }

    private static void negativeDeltaReturnsLesson() {
        System.out.println("[派生：负增量（退回课时）]");
        CourseDerivedBalance d = CourseDerivedBalance.derive(15, 2, -1, 0);
        check("used=2-1=1", d.getUsedLessons() == 1, "used=" + d.getUsedLessons());
        check("remain=14", d.getRemainLessons() == 14, "remain=" + d.getRemainLessons());
    }

    private static void clampLow() {
        System.out.println("[派生：下界 clamp 到 0]");
        CourseDerivedBalance d = CourseDerivedBalance.derive(15, 2, -5, 0);
        check("used=0", d.getUsedLessons() == 0, "used=" + d.getUsedLessons());
        check("remain=15", d.getRemainLessons() == 15, "remain=" + d.getRemainLessons());
    }

    private static void clampHigh() {
        System.out.println("[派生：上界 clamp 到 total]");
        CourseDerivedBalance d = CourseDerivedBalance.derive(15, 14, 3, 0);
        check("used=15", d.getUsedLessons() == 15, "used=" + d.getUsedLessons());
        check("remain=0", d.getRemainLessons() == 0, "remain=" + d.getRemainLessons());
        check("remain=0 且 total>0 → 已完成", d.getStatus() == 1, "status=" + d.getStatus());
    }

    private static void refundedKeepsStatus() {
        System.out.println("[派生：已退课保持 status=2]");
        CourseDerivedBalance d = CourseDerivedBalance.derive(10, 3, 0, 2);
        check("status=2", d.getStatus() == 2, "status=" + d.getStatus());
        check("used 仍按流水=3", d.getUsedLessons() == 3, "used=" + d.getUsedLessons());
    }

    private static void completedWhenRemainZero() {
        System.out.println("[派生：上完→已完成]");
        CourseDerivedBalance d = CourseDerivedBalance.derive(10, 10, 0, 0);
        check("status=1", d.getStatus() == 1, "status=" + d.getStatus());
    }

    private static void missingTotalTreatedAsZero() {
        System.out.println("[派生：total 缺失按 0]");
        CourseDerivedBalance d = CourseDerivedBalance.derive(null, 3, 0, 0);
        check("total=0", d.getTotalLessons() == 0, "total=" + d.getTotalLessons());
        check("used=0", d.getUsedLessons() == 0, "used=" + d.getUsedLessons());
        check("remain=0", d.getRemainLessons() == 0, "remain=" + d.getRemainLessons());
    }

    private static void noStoredRemainBackfill() {
        System.out.println("[红线：不回填库里 remain_lessons（旧 resolve 兜底会还原成没上过）]");
        // 场景：total=2、两条有效流水、库里 remain_lessons=2（老链路没扣剩余）。
        // 旧 UserCourseBalanceUtil.resolve 回填 remain=2/used=0；新派生必须 used=2/remain=0。
        CourseDerivedBalance d = CourseDerivedBalance.derive(2, 2, 0, 0);
        check("used=2（不回填）", d.getUsedLessons() == 2, "used=" + d.getUsedLessons());
        check("remain=0（不回填）", d.getRemainLessons() == 0, "remain=" + d.getRemainLessons());
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
