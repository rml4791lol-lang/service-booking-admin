package com.mdd.admin.service.impl;

/**
 * 老课包课时调整的纯计算规则。
 *
 * <p>表里同时存 total/used/remain，三列必须自洽。调整时以「总课时 + 剩余课时」为入参，
 * 已用课时一律重算为 {@code total - remain}，避免只改剩余导致 used 漂移。</p>
 */
public final class UserCourseLessonAdjustment {

    private UserCourseLessonAdjustment() {
    }

    public static Result resolve(int oldTotal, int oldRemain, Integer totalLessons, Integer remainLessons) {
        if (totalLessons == null && remainLessons == null) {
            throw new IllegalArgumentException("请至少调整总课时或剩余课时");
        }
        int total = totalLessons != null ? totalLessons : oldTotal;
        int remain = remainLessons != null ? remainLessons : oldRemain;
        if (total < 0 || remain < 0) {
            throw new IllegalArgumentException("课时不能为负数");
        }
        if (remain > total) {
            throw new IllegalArgumentException("剩余课时不能大于总课时");
        }
        return new Result(total, total - remain, remain);
    }

    public static final class Result {
        private final int totalLessons;
        private final int usedLessons;
        private final int remainLessons;

        private Result(int totalLessons, int usedLessons, int remainLessons) {
            this.totalLessons = totalLessons;
            this.usedLessons = usedLessons;
            this.remainLessons = remainLessons;
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
    }
}
