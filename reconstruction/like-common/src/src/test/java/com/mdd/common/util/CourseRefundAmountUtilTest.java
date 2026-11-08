package com.mdd.common.util;

import java.math.BigDecimal;

/**
 * 退课金额重算口径断言器（纯 Java，无 JUnit）。
 *
 * <p>红线（资金风险）：退课 / 退款的「已用课时」必须按实际消课流水重算，不能沿用
 * {@code la_user_course.used_lessons}（老核销可能没扣课时，上完课仍被全退）。
 * 重算口径与「我的课程」的 effectiveUsedLessons 一致；流水是唯一事实来源——
 * 即使库里 remain_lessons 还挂着「剩余」，流水已满也必须按已用完处理。</p>
 *
 * <p>执行：bash tools/user/run-final-backend-tests.sh</p>
 */
public final class CourseRefundAmountUtilTest {

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        usedZeroButHasConsumeLogs();
        noConsumeLogsFullRefund();
        effectiveUsedClampedToTotal();
        usedCostAbovePayClampedToZero();
        roundingToCent();
        lessonFeePrefersFeePerLesson();
        lessonFeeFallsBackToAverage();
        flowsOverrideStoredRemain();
        nullAmountsAreSafe();

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    /** 场景：used_lessons=0（老核销没扣课时），但存在 2 条消课流水 → 按 2 节扣减。 */
    private static void usedZeroButHasConsumeLogs() {
        System.out.println("[used=0 但有 2 条消课流水]");
        CourseRefundAmountUtil.Result r = CourseRefundAmountUtil.resolve(
                new BigDecimal("1000.00"), new BigDecimal("100.00"), 10, 2);
        check("已用按流水=2", r.getUsedLessons() == 2, "used=" + r.getUsedLessons());
        check("剩余=8", r.getRemainLessons() == 8, "remain=" + r.getRemainLessons());
        check("应退=1000-2*100=800", eq(r.getRefundAmount(), "800.00"), "refund=" + r.getRefundAmount());
    }

    /** 场景：没有任何消课流水 → 未上课，全退。 */
    private static void noConsumeLogsFullRefund() {
        System.out.println("[无消课流水 → 全退]");
        CourseRefundAmountUtil.Result r = CourseRefundAmountUtil.resolve(
                new BigDecimal("600.00"), new BigDecimal("120.00"), 5, 0);
        check("已用=0", r.getUsedLessons() == 0, "used=" + r.getUsedLessons());
        check("应退=600", eq(r.getRefundAmount(), "600.00"), "refund=" + r.getRefundAmount());
    }

    /** 流水条数超过总课时（重复核销等异常）时按总课时封顶，不允许负数退款。 */
    private static void effectiveUsedClampedToTotal() {
        System.out.println("[流水数 > 总课时]");
        CourseRefundAmountUtil.Result r = CourseRefundAmountUtil.resolve(
                new BigDecimal("500.00"), new BigDecimal("50.00"), 10, 12);
        check("已用封顶=10", r.getUsedLessons() == 10, "used=" + r.getUsedLessons());
        check("剩余=0", r.getRemainLessons() == 0, "remain=" + r.getRemainLessons());
        check("应退=0", eq(r.getRefundAmount(), "0.00"), "refund=" + r.getRefundAmount());
    }

    /** 已消耗课时费大于实付（打折/赠送课时）时，应退金额不能为负。 */
    private static void usedCostAbovePayClampedToZero() {
        System.out.println("[已消耗费用 > 实付]");
        CourseRefundAmountUtil.Result r = CourseRefundAmountUtil.resolve(
                new BigDecimal("150.00"), new BigDecimal("100.00"), 10, 3);
        check("已用=3", r.getUsedLessons() == 3, "used=" + r.getUsedLessons());
        check("应退=0（不为负）", eq(r.getRefundAmount(), "0.00"), "refund=" + r.getRefundAmount());
    }

    /** 金额保留两位，四舍五入。 */
    private static void roundingToCent() {
        System.out.println("[金额两位小数]");
        CourseRefundAmountUtil.Result r = CourseRefundAmountUtil.resolve(
                new BigDecimal("100.00"), new BigDecimal("33.333"), 3, 1);
        check("应退=100-33.333→66.67", eq(r.getRefundAmount(), "66.67"), "refund=" + r.getRefundAmount());
    }

    /** 单节课时费优先取 fee_per_lesson（与课程详情口径一致）。 */
    private static void lessonFeePrefersFeePerLesson() {
        System.out.println("[课时费优先 fee_per_lesson]");
        BigDecimal fee = CourseRefundAmountUtil.resolveLessonFee(
                new BigDecimal("80.00"), new BigDecimal("1000.00"), 10);
        check("单价=80", fee.compareTo(new BigDecimal("80.00")) == 0, "fee=" + fee);
    }

    /** fee_per_lesson 未维护时按 总费用/总课时 估算。 */
    private static void lessonFeeFallsBackToAverage() {
        System.out.println("[课时费回退 总费用/总课时]");
        BigDecimal fee = CourseRefundAmountUtil.resolveLessonFee(
                null, new BigDecimal("1000.00"), 3);
        check("单价=333.33", fee.compareTo(new BigDecimal("333.33")) == 0, "fee=" + fee);
        BigDecimal none = CourseRefundAmountUtil.resolveLessonFee(null, null, 0);
        check("无费用数据=0", none.compareTo(BigDecimal.ZERO) == 0, "fee=" + none);
    }

    /**
     * 流水是事实来源：老库 remain_lessons 还挂着 2 节，但流水已满 10 节（上完了、
     * 老链路没扣剩余）→ 已用=10、应退 0。这正是「上完课仍可全退」的漏洞场景。
     */
    private static void flowsOverrideStoredRemain() {
        System.out.println("[流水满但库里 remain>0 → 仍按已用完]");
        CourseRefundAmountUtil.Result r = CourseRefundAmountUtil.resolve(
                new BigDecimal("1000.00"), new BigDecimal("100.00"), 10, 10);
        check("已用=10", r.getUsedLessons() == 10, "used=" + r.getUsedLessons());
        check("剩余=0", r.getRemainLessons() == 0, "remain=" + r.getRemainLessons());
        check("应退=0", eq(r.getRefundAmount(), "0.00"), "refund=" + r.getRefundAmount());
    }

    /** 空金额/空课时不抛异常（历史脏数据兜底）。 */
    private static void nullAmountsAreSafe() {
        System.out.println("[空值兜底]");
        CourseRefundAmountUtil.Result r = CourseRefundAmountUtil.resolve(null, null, null, 2);
        check("总课时=0", r.getTotalLessons() == 0, "total=" + r.getTotalLessons());
        check("已用=0（无总课时无法扣减）", r.getUsedLessons() == 0, "used=" + r.getUsedLessons());
        check("应退=0", eq(r.getRefundAmount(), "0.00"), "refund=" + r.getRefundAmount());
    }

    private static boolean eq(BigDecimal actual, String expected) {
        return actual != null && actual.compareTo(new BigDecimal(expected)) == 0;
    }

    private static void check(String name, boolean ok, String detail) {
        if (ok) {
            passed++;
            System.out.println("  PASS " + name);
        } else {
            failed++;
            System.out.println("  FAIL " + name + " — " + detail);
        }
    }
}
