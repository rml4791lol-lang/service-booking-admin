package com.mdd.common.util;

import java.math.BigDecimal;

/**
 * 注销账号前置校验断言器（纯 Java，无 JUnit）。
 *
 * <p>契约：POST /api/user/cancelAccount。存在以下任一资产时拒绝注销，并给出明确原因
 * （原因顺序固定：待支付订单 → 退款未完成 → 未结束预约 → 未使用次数券 → 余额）。</p>
 *
 * <p>执行：bash tools/user/run-final-backend-tests.sh</p>
 */
public final class UserCancelAccountGuardTest {

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        noAssetsCanCancel();
        pendingOrderBlocks();
        refundInProgressBlocks();
        futureBookingBlocks();
        unusedCouponBlocks();
        balanceBlocks();
        reasonPriorityFollowsRiskOrder();

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void noAssetsCanCancel() {
        System.out.println("[无资产 → 允许注销]");
        String reason = UserCancelAccountGuard.check(0, 0, 0, 0, new BigDecimal("0.00"));
        check("原因=null", reason == null, "reason=" + reason);
    }

    private static void pendingOrderBlocks() {
        System.out.println("[待支付订单]");
        String reason = UserCancelAccountGuard.check(2, 0, 0, 0, BigDecimal.ZERO);
        check("提示待支付订单", reason != null && reason.contains("待支付") && reason.contains("2"),
                "reason=" + reason);
    }

    private static void refundInProgressBlocks() {
        System.out.println("[退款未完成]");
        String reason = UserCancelAccountGuard.check(0, 1, 0, 0, BigDecimal.ZERO);
        check("提示退款未完成", reason != null && reason.contains("退款") && reason.contains("1"),
                "reason=" + reason);
    }

    private static void futureBookingBlocks() {
        System.out.println("[未结束预约]");
        String reason = UserCancelAccountGuard.check(0, 0, 3, 0, BigDecimal.ZERO);
        check("提示课程预约", reason != null && reason.contains("预约") && reason.contains("3"),
                "reason=" + reason);
    }

    private static void unusedCouponBlocks() {
        System.out.println("[未使用次数券]");
        String reason = UserCancelAccountGuard.check(0, 0, 0, 2, BigDecimal.ZERO);
        check("提示次数券", reason != null && reason.contains("次数券") && reason.contains("2"),
                "reason=" + reason);
    }

    private static void balanceBlocks() {
        System.out.println("[有余额]");
        String reason = UserCancelAccountGuard.check(0, 0, 0, 0, new BigDecimal("12.30"));
        check("提示余额并引导联系客服",
                reason != null && reason.contains("12.30") && reason.contains("客服"),
                "reason=" + reason);
    }

    private static void reasonPriorityFollowsRiskOrder() {
        System.out.println("[多资产同时命中：先报风险最高的原因]");
        String reason = UserCancelAccountGuard.check(1, 1, 1, 1, new BigDecimal("1.00"));
        check("先报待支付订单", reason != null && reason.contains("待支付"), "reason=" + reason);
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
