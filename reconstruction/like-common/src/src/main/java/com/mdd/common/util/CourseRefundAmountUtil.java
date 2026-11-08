package com.mdd.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 退课应退金额重算（资金口径，唯一实现）。
 *
 * <p>退款金额 = 实付金额 - 已用课时 × 单节课时费。<b>已用课时以实际消课流水重算</b>
 * （由 {@code CourseConsumeEffectiveService} 按 la_course_consume_log 的 FIFO 分配得出，
 * 口径与「我的课程」的 effectiveUsedLessons 一致），不能直接读
 * {@code la_user_course.used_lessons}：老核销链路可能没扣课时，导致上完课仍按 0 节全退。</p>
 *
 * <p>注意这里<b>不再</b>套用 {@link UserCourseBalanceUtil#resolve} 的历史兜底
 * （流水已满但库里 remain_lessons>0 时回填库里的剩余）：该兜底会把「上完了课但老链路
 * 没扣剩余」还原成「一节没上」，正是本次要修的全退漏洞。</p>
 */
public final class CourseRefundAmountUtil {

    private CourseRefundAmountUtil() {
    }

    /**
     * @param payAmount            实付金额（订单金额）
     * @param lessonFee            单节课时费（fee_per_lesson，缺失时由调用方按总费用/总课时估算）
     * @param totalLessons         购买总课时（调用方需先回填，0/空视为 0）
     * @param effectiveUsedLessons 按实际消课流水重算出的已用课时
     */
    public static Result resolve(BigDecimal payAmount,
                                 BigDecimal lessonFee,
                                 Integer totalLessons,
                                 int effectiveUsedLessons) {
        int total = nonNegative(totalLessons);
        int used = Math.max(0, Math.min(effectiveUsedLessons, total));
        int remain = Math.max(total - used, 0);
        BigDecimal pay = payAmount == null ? BigDecimal.ZERO : payAmount;
        BigDecimal fee = lessonFee == null ? BigDecimal.ZERO : lessonFee;
        BigDecimal usedCost = fee.multiply(new BigDecimal(used));
        BigDecimal refund = pay.subtract(usedCost);
        if (refund.signum() < 0) {
            refund = BigDecimal.ZERO;
        }
        refund = refund.setScale(2, RoundingMode.HALF_UP);
        return new Result(total, used, remain, refund);
    }

    private static int nonNegative(Integer value) {
        return value != null && value > 0 ? value : 0;
    }

    /**
     * 单节课时费：优先 fee_per_lesson；未维护时用总费用 / 总课时估算（两位小数）。
     * 申请与审核必须用同一口径，否则同一申请两次算出的应退金额会不一致。
     */
    public static BigDecimal resolveLessonFee(BigDecimal feePerLesson, BigDecimal totalFee, Integer totalLessons) {
        if (feePerLesson != null && feePerLesson.signum() > 0) {
            return feePerLesson;
        }
        if (totalFee != null && totalFee.signum() > 0 && totalLessons != null && totalLessons > 0) {
            return totalFee.divide(new BigDecimal(totalLessons), 2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public static final class Result {
        private final int totalLessons;
        private final int usedLessons;
        private final int remainLessons;
        private final BigDecimal refundAmount;

        private Result(int totalLessons, int usedLessons, int remainLessons, BigDecimal refundAmount) {
            this.totalLessons = totalLessons;
            this.usedLessons = usedLessons;
            this.remainLessons = remainLessons;
            this.refundAmount = refundAmount;
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

        public BigDecimal getRefundAmount() {
            return refundAmount;
        }
    }
}
