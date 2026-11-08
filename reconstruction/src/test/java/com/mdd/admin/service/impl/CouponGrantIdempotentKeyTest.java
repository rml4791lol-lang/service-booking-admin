package com.mdd.admin.service.impl;

/**
 * #205 后台发券幂等键：必须按学员维度隔离。
 *
 * <p>本仓库离线 Maven 仓库没有 junit / surefire provider，按仓库惯例写成
 * 「带 main 的纯 Java 断言器」，由 tools/coupon/run-coupon-grant-idem-test.sh 执行。</p>
 *
 * <p>回归场景：同一管理员在 60 秒内给同一手机号的不同学员发同商品同张数，
 * 不应被当成重复提交；键中必须包含 studentId。</p>
 */
public final class CouponGrantIdempotentKeyTest {

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        sameRequestIsSameKey();
        differentStudentIsDifferentKey();
        otherDimensionsStillIsolate();

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void sameRequestIsSameKey() {
        System.out.println("[同一次提交重试 → 同键]");
        String first = CouponGoodsServiceImpl.grantIdempotentKey(7, 45, "13800001234", 101, 2);
        String second = CouponGoodsServiceImpl.grantIdempotentKey(7, 45, "13800001234", 101, 2);
        check("完全相同参数得到相同幂等键", first.equals(second), first);
        check("键带 grant:idem 前缀", first.contains("coupon:grant:idem:"), first);
    }

    private static void differentStudentIsDifferentKey() {
        System.out.println("[同学员维度]");
        String studentA = CouponGoodsServiceImpl.grantIdempotentKey(7, 45, "13800001234", 101, 2);
        String studentB = CouponGoodsServiceImpl.grantIdempotentKey(7, 45, "13800001234", 102, 2);
        check("同手机号不同学员 → 不同幂等键", !studentA.equals(studentB), studentA + " vs " + studentB);
        String accountLevel = CouponGoodsServiceImpl.grantIdempotentKey(7, 45, "13800001234", 0, 2);
        check("账号级（0）与指定学员 → 不同幂等键", !studentA.equals(accountLevel),
                studentA + " vs " + accountLevel);
    }

    private static void otherDimensionsStillIsolate() {
        System.out.println("[其他维度]");
        String base = CouponGoodsServiceImpl.grantIdempotentKey(7, 45, "13800001234", 101, 2);
        check("不同张数 → 不同键",
                !base.equals(CouponGoodsServiceImpl.grantIdempotentKey(7, 45, "13800001234", 101, 3)), "");
        check("不同商品 → 不同键",
                !base.equals(CouponGoodsServiceImpl.grantIdempotentKey(7, 46, "13800001234", 101, 2)), "");
        check("不同手机号 → 不同键",
                !base.equals(CouponGoodsServiceImpl.grantIdempotentKey(7, 45, "13800005678", 101, 2)), "");
        check("不同管理员 → 不同键",
                !base.equals(CouponGoodsServiceImpl.grantIdempotentKey(8, 45, "13800001234", 101, 2)), "");
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
