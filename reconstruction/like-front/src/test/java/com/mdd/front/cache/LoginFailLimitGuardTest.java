package com.mdd.front.cache;

/**
 * #157 教练密码登录防爆破：限频纯逻辑断言器（带 main，不依赖 junit）。
 *
 * <p>本仓库离线 Maven 仓库里没有 junit / surefire provider，测试按仓库惯例写成
 * 「带 main 的纯 Java 断言器」，由 tools/runtime-mode/run-login-guard-test.sh
 * 编译 like-front 后直接跑（与 tools/assetref/run-contract-test.sh 同款）。</p>
 *
 * <p>覆盖（接口行为——限频触发/解除、文案码一致——由 API 探针实跑验证，
 * 见 tools/instructor/login-guard-e2e.mjs）：</p>
 * <ol>
 *   <li>键构造：账号+IP 决定唯一键；账号或 IP 不同则键不同；null 安全；</li>
 *   <li>阈值判断：null/未达阈值放行，达到 MAX_ATTEMPTS 及以上拦截；</li>
 *   <li>常量约束：窗口/阈值为正，统一文案不含「不存在」类可枚举字样。</li>
 * </ol>
 */
public final class LoginFailLimitGuardTest {

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        keyBuilding();
        threshold();
        constants();
        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void keyBuilding() {
        System.out.println("[键构造]");
        check("同账号同IP → 同键",
                LoginFailLimitCache.failKey("13800000001", "127.0.0.1")
                        .equals(LoginFailLimitCache.failKey("13800000001", "127.0.0.1")));
        check("同账号不同IP → 异键",
                !LoginFailLimitCache.failKey("13800000001", "127.0.0.1")
                        .equals(LoginFailLimitCache.failKey("13800000001", "192.168.1.2")));
        check("不同账号同IP → 异键",
                !LoginFailLimitCache.failKey("13800000001", "127.0.0.1")
                        .equals(LoginFailLimitCache.failKey("13800000002", "127.0.0.1")));
        check("账号前后空白归一前由调用方 trim（键函数对原串哈希）",
                !LoginFailLimitCache.failKey("13800000001 ", "127.0.0.1")
                        .equals(LoginFailLimitCache.failKey("13800000001", "127.0.0.1")));
        check("null 账号不抛异常",
                LoginFailLimitCache.failKey(null, "127.0.0.1") != null);
        check("null IP不抛异常",
                LoginFailLimitCache.failKey("13800000001", null) != null);
        check("键以集中定义的前缀开头",
                LoginFailLimitCache.failKey("13800000001", "127.0.0.1")
                        .startsWith(LoginFailLimitCache.KEY_PREFIX));
    }

    private static void threshold() {
        System.out.println("[阈值判断]");
        check("无记录(null) → 放行", !LoginFailLimitCache.isBlocked(null));
        check("失败 0 次 → 放行", !LoginFailLimitCache.isBlocked(0L));
        check("失败 MAX-1 次 → 放行", !LoginFailLimitCache.isBlocked(LoginFailLimitCache.MAX_ATTEMPTS - 1));
        check("失败恰 MAX 次 → 拦截", LoginFailLimitCache.isBlocked(LoginFailLimitCache.MAX_ATTEMPTS));
        check("失败超过 MAX 次 → 拦截", LoginFailLimitCache.isBlocked(LoginFailLimitCache.MAX_ATTEMPTS + 1));
    }

    private static void constants() {
        System.out.println("[常量约束]");
        check("阈值 > 0", LoginFailLimitCache.MAX_ATTEMPTS > 0);
        check("窗口 > 0", LoginFailLimitCache.WINDOW_SECONDS > 0);
        check("窗口不小于 60s（避免过快自动解除）", LoginFailLimitCache.WINDOW_SECONDS >= 60L);
        check("统一文案不含「不存在」（否则仍可枚举手机号）",
                !LoginFailLimitCache.MSG_LOGIN_FAILED.contains("不存在"));
        check("统一文案与限频文案不同",
                !LoginFailLimitCache.MSG_LOGIN_FAILED.equals(LoginFailLimitCache.MSG_RATE_LIMITED));
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  ✓ " + name);
            return;
        }
        failed++;
        System.out.println("  ✗ " + name);
    }
}
