package com.mdd.common.util;

/**
 * 课时派生余额（#222：消课流水是唯一事实，{@code la_user_course} 的 used/remain 降级为展示缓存）。
 *
 * <p>口径（与退款重算、后台课时账户、小程序我的课程共用）：</p>
 * <ol>
 *     <li>used = 有效消课流水（{@link CourseConsumeEffectiveService} 按 FIFO 分配）+ 调整流水增量
 *         （la_user_course_adjust_log.change_used 之和），clamp 到 [0, total]；</li>
 *     <li>remain = total - used（<b>不回填</b>库里 remain_lessons：旧
 *         {@link UserCourseBalanceUtil#resolve} 的「库里 remain 回填」兜底会把「上完课但未扣剩余」
 *         还原成一节没上）；</li>
 *     <li>status：已退课保持 2；否则 total&gt;0 且 remain=0 → 已完成(1)，学习中(0)。</li>
 * </ol>
 *
 * <p>纯计算无副作用；流水/增量的读取由 {@link CourseConsumeEffectiveService} 负责。</p>
 */
public final class CourseDerivedBalance {

    private final int totalLessons;
    private final int usedLessons;
    private final int remainLessons;
    private final int status;

    private CourseDerivedBalance(int totalLessons, int usedLessons, int remainLessons, int status) {
        this.totalLessons = totalLessons;
        this.usedLessons = usedLessons;
        this.remainLessons = remainLessons;
        this.status = status;
    }

    /**
     * @param totalLessons     购买总课时（空/非正按 0）
     * @param effectiveUsedRaw 有效消课流水数（FIFO 分配后，未并入调整增量）
     * @param adjustUsedDelta  调整流水增量（la_user_course_adjust_log.change_used 之和，可为负）
     * @param storedStatus     la_user_course.status（2=已退课）
     */
    public static CourseDerivedBalance derive(Integer totalLessons, int effectiveUsedRaw, int adjustUsedDelta, Integer storedStatus) {
        int total = nonNegative(totalLessons);
        int rawUsed = effectiveUsedRaw + adjustUsedDelta;
        int used = Math.max(0, Math.min(rawUsed, total));
        int remain = Math.max(total - used, 0);
        int status = storedStatus != null && storedStatus == 2 ? 2 : (total > 0 && remain == 0 ? 1 : 0);
        return new CourseDerivedBalance(total, used, remain, status);
    }

    private static int nonNegative(Integer value) {
        return value != null && value > 0 ? value : 0;
    }

    public int getTotalLessons() {
        return totalLessons;
    }

    public int getUsedLessons() {
        return usedLessons;
    }

    public int getRemainLessons() {
        return remainLessons;
    }

    public int getStatus() {
        return status;
    }
}
