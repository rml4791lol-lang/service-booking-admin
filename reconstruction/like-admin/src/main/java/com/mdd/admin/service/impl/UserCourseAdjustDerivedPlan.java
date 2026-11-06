package com.mdd.admin.service.impl;

/**
 * 老课包课时调整的纯计算规则（#222 流水口径）。
 *
 * <p>调整以「流水派生已用」（消课流水 FIFO + 已有调整增量 ΣΔ）为锚点，产出：
 * 写入字段（total/used/remain，保持三列自洽 used = total - remain）与调整增量
 * Δused（写入 la_user_course_adjust_log.change_used，使流水重放
 * fifo + ΣΔ + Δused = 写入 used，即「调整→流水→派生一致」）。</p>
 *
 * <ul>
 *     <li>显式给 remain：视为管理员修正已用——targetUsed = total - remain，
 *         Δused 补齐流水口径与目标值的差距；</li>
 *     <li>只给 total：视为增减课时（赠送/缩减）——已用保持流水口径不变，
 *         remain = total - 流水口径已用；若流水已用超过新总课时则拒绝
 *         （缩量不能抹掉已发生的消课，需显式改剩余）；</li>
 *     <li>三列自洽与「剩余∈[0,total]」「非负」校验沿用
 *         {@link UserCourseLessonAdjustment#resolve}（17 项既有断言不改）。</li>
 * </ul>
 */
public final class UserCourseAdjustDerivedPlan {

    private UserCourseAdjustDerivedPlan() {
    }

    /**
     * @param oldTotal     调整前字段总课时（非负）
     * @param oldRemain    调整前字段剩余课时（非负）
     * @param fifoUsed     有效消课流水数（FIFO 分配到本课包，未并入调整增量）
     * @param priorDelta   已有调整增量 ΣΔ（la_user_course_adjust_log.change_used 之和）
     * @param totalLessons 入参总课时（空 = 不调整）
     * @param remainLessons 入参剩余课时（空 = 不调整）
     */
    public static Plan resolve(int oldTotal, int oldRemain, int fifoUsed, int priorDelta,
                               Integer totalLessons, Integer remainLessons) {
        // 复用既有校验与归一化（两项都空 / 负数 / remain>total 的文案与老接口一致）
        UserCourseLessonAdjustment.Result base =
                UserCourseLessonAdjustment.resolve(oldTotal, oldRemain, totalLessons, remainLessons);
        int newTotal = base.getTotalLessons();
        int rawDerivedUsed = fifoUsed + priorDelta;
        int targetUsed;
        int newRemain;
        if (remainLessons != null) {
            // 显式指定剩余 = 修正已用
            targetUsed = base.getUsedLessons();
            newRemain = base.getRemainLessons();
        } else {
            // 只调总课时：已用保持流水口径
            if (rawDerivedUsed > newTotal) {
                throw new IllegalArgumentException("总课时不能小于流水已用课时" + rawDerivedUsed);
            }
            targetUsed = Math.max(rawDerivedUsed, 0);
            newRemain = newTotal - targetUsed;
        }
        // Δused 以「未 clamp 的流水口径」为锚，保证重放精确：fifo + ΣΔ + Δused = targetUsed
        int changeUsed = targetUsed - rawDerivedUsed;
        return new Plan(newTotal, targetUsed, newRemain, changeUsed);
    }

    public static final class Plan {
        private final int totalLessons;
        private final int usedLessons;
        private final int remainLessons;
        private final int changeUsed;

        private Plan(int totalLessons, int usedLessons, int remainLessons, int changeUsed) {
            this.totalLessons = totalLessons;
            this.usedLessons = usedLessons;
            this.remainLessons = remainLessons;
            this.changeUsed = changeUsed;
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

        public int getChangeUsed() {
            return changeUsed;
        }
    }
}
