/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.util;

public final class UserCourseBalanceUtil {
    private static final int STATUS_LEARNING = 0;
    private static final int STATUS_COMPLETED = 1;
    private static final int STATUS_REFUNDED = 2;

    private UserCourseBalanceUtil() {
    }

    public static Snapshot resolve(Integer totalLessons, Integer storedUsedLessons, Integer storedRemainLessons, Integer effectiveUsedLessons, Integer storedStatus) {
        int total = UserCourseBalanceUtil.nonNegative(totalLessons);
        int effectiveUsed = UserCourseBalanceUtil.clamp(UserCourseBalanceUtil.nonNegative(effectiveUsedLessons), 0, total);
        int storedRemain = storedRemainLessons != null && storedRemainLessons > 0 ? storedRemainLessons : 0;
        int used = effectiveUsed;
        int remain = Math.max(total - used, 0);
        if ((storedStatus == null || storedStatus != 2) && remain == 0 && storedRemain > 0) {
            remain = total > 0 ? Math.min(storedRemain, total) : storedRemain;
            int n = used = total > 0 ? Math.max(total - remain, 0) : used;
        }
        int status = storedStatus != null && storedStatus == 2 ? 2 : (total > 0 && remain == 0 ? 1 : 0);
        return new Snapshot(total, used, remain, status);
    }

    public static int statusAfterLessonReturned(Integer currentStatus, Integer remainLessons) {
        if (currentStatus != null && currentStatus == 2) {
            return 2;
        }
        if (remainLessons != null && remainLessons > 0) {
            return 0;
        }
        return currentStatus != null ? currentStatus : 0;
    }

    private static int nonNegative(Integer value) {
        return value != null && value > 0 ? value : 0;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    public static final class Snapshot {
        private final int totalLessons;
        private final int usedLessons;
        private final int remainLessons;
        private final int status;

        private Snapshot(int totalLessons, int usedLessons, int remainLessons, int status) {
            this.totalLessons = totalLessons;
            this.usedLessons = usedLessons;
            this.remainLessons = remainLessons;
            this.status = status;
        }

        public int getTotalLessons() {
            return this.totalLessons;
        }

        public int getUsedLessons() {
            return this.usedLessons;
        }

        public int getRemainLessons() {
            return this.remainLessons;
        }

        public int getStatus() {
            return this.status;
        }
    }
}

