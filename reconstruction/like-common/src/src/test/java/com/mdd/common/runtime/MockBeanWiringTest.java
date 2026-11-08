package com.mdd.common.runtime;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.MapPropertySource;

/**
 * #124 P4 mock bean 装配边界测试（真实 Spring 条件装配，不启动完整应用）。
 *
 * <p>用 {@link AnnotationConfigApplicationContext} 注册真实 mock 实现类：Spring 在
 * 注册阶段即评估 {@code @Profile} / {@code @ConditionalOnProperty}，因此可以在
 * 不连接 MySQL/Redis、不实例化任何 bean 的前提下断言“生产/体验根本不装配 mock bean”。</p>
 *
 * <p>like-front / like-admin 的类由 tools/runtime-mode/run-guard-test.sh 先编译并加入
 * classpath；本测试不编译期依赖这两个模块，避免 like-common 反向依赖上层服务。</p>
 *
 * <p>覆盖：</p>
 * <ol>
 *   <li>mock 登录 / 支付 / 退款：prod/experience + 开关=true → 无 bean 定义；</li>
 *   <li>同一开关在 dev（测试 profile）下 → bean 定义存在（离线 e2e 仍可用）；</li>
 *   <li>开关=false 时任何 profile 都不装配 mock bean；</li>
 *   <li>真实实现（WxLoginServiceImpl / WechatRefundTaskExecutor）始终装配；</li>
 *   <li>真实登录实现里不再残留 mock 字段；mock 登录实现是 @Primary（两者并存时优先）。</li>
 * </ol>
 */
public final class MockBeanWiringTest {

    private static final String PAY_MOCK_CLASS = "com.mdd.front.service.impl.PayMockGatewayServiceImpl";
    private static final String LOGIN_MOCK_CLASS = "com.mdd.front.service.impl.WxMockLoginServiceImpl";
    private static final String LOGIN_REAL_CLASS = "com.mdd.front.service.impl.WxLoginServiceImpl";
    private static final String REFUND_MOCK_CLASS = "com.mdd.admin.service.refund.MockRefundTaskExecutor";
    private static final String REFUND_REAL_CLASS = "com.mdd.admin.service.refund.WechatRefundTaskExecutor";

    private static final String PAY_SWITCH = RuntimeModeGuard.PAY_MOCK;
    private static final String LOGIN_SWITCH = RuntimeModeGuard.LOGIN_MOCK;
    private static final String REFUND_SWITCH = RuntimeModeGuard.REFUND_MOCK;

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        Class<?> payMock = requireClass(PAY_MOCK_CLASS);
        Class<?> loginMock = requireClass(LOGIN_MOCK_CLASS);
        Class<?> loginReal = requireClass(LOGIN_REAL_CLASS);
        Class<?> refundMock = requireClass(REFUND_MOCK_CLASS);
        Class<?> refundReal = requireClass(REFUND_REAL_CLASS);

        section("生产/体验：mock bean 不装配（即使开关误开）");
        expectBeanCount("pay mock @ prod + 开关=true", payMock, "prod", PAY_SWITCH, 0);
        expectBeanCount("pay mock @ experience + 开关=true", payMock, "experience", PAY_SWITCH, 0);
        expectBeanCount("login mock @ prod + 开关=true", loginMock, "prod", LOGIN_SWITCH, 0);
        expectBeanCount("login mock @ experience + 开关=true", loginMock, "experience", LOGIN_SWITCH, 0);
        expectBeanCount("refund mock @ prod + 开关=true", refundMock, "prod", REFUND_SWITCH, 0);
        expectBeanCount("refund mock @ experience + 开关=true", refundMock, "experience", REFUND_SWITCH, 0);
        // 未显式声明 profile（admin 容器的默认形态）不得装配 mock bean。
        expectBeanCount("pay mock @ 无 profile + 开关=true", payMock, null, PAY_SWITCH, 0);
        expectBeanCount("refund mock @ 无 profile + 开关=true", refundMock, null, REFUND_SWITCH, 0);

        section("测试 profile：mock bean 仅在开关打开时装配");
        expectBeanCount("pay mock @ dev + 开关=true", payMock, "dev", PAY_SWITCH, 1);
        expectBeanCount("pay mock @ dev + 开关=false", payMock, "dev", PAY_SWITCH, false, 0);
        expectBeanCount("login mock @ dev + 开关=true", loginMock, "dev", LOGIN_SWITCH, 1);
        expectBeanCount("login mock @ dev + 开关=false", loginMock, "dev", LOGIN_SWITCH, false, 0);
        expectBeanCount("refund mock @ dev + 开关=true", refundMock, "dev", REFUND_SWITCH, 1);
        expectBeanCount("refund mock @ dev + 开关=false", refundMock, "dev", REFUND_SWITCH, false, 0);

        section("真实实现始终装配");
        expectBeanCount("login real @ prod", loginReal, "prod", LOGIN_SWITCH, 1);
        expectBeanCount("refund real @ prod", refundReal, "prod", REFUND_SWITCH, 1);

        section("真实登录实现不残留 mock 分支");
        check("WxLoginServiceImpl 无 mock 配置字段", !hasField(loginReal, "mockEnabled")
                && !hasField(loginReal, "mockOpenid") && !hasField(loginReal, "mockErrorCodes"));
        check("WxMockLoginServiceImpl 是 @Primary（与真实 bean 并存时优先）",
                loginMock.getAnnotation(Primary.class) != null);

        summary();
    }

    /**
     * 断言注册后上下文里的 bean 定义数量。@Profile/@ConditionalOnProperty 在
     * AnnotationConfigApplicationContext.register() 阶段求值：被跳过 = 0，装配 = 1。
     */
    private static void expectBeanCount(String name, Class<?> type, String profile, String switchKey, int expected) {
        expectBeanCount(name, type, profile, switchKey, true, expected);
    }

    private static void expectBeanCount(String name, Class<?> type, String profile, String switchKey,
                                        boolean enabled, int expected) {
        int actual = beanDefinitionCount(type, profile, switchKey, enabled);
        if (actual == expected) {
            passed++;
            System.out.println("  ✓ " + name + " → beanDefinitions=" + actual);
            return;
        }
        failed++;
        System.out.println("  ✗ " + name + "：预期 beanDefinitions=" + expected + "，实际 " + actual);
    }

    private static int beanDefinitionCount(Class<?> type, String profile, String switchKey, boolean enabled) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        if (profile != null && !profile.isEmpty()) {
            context.getEnvironment().setActiveProfiles(profile);
        }
        Map<String, Object> properties = new LinkedHashMap<String, Object>();
        properties.put(switchKey, enabled ? "true" : "false");
        context.getEnvironment().getPropertySources()
                .addFirst(new MapPropertySource("mock-wiring-test", properties));
        // 上下文自带 environment/systemProperties 等 5 个内部 bean 定义，取注册前后差值。
        int baseline = context.getBeanDefinitionCount();
        context.register(type);
        int count = context.getBeanDefinitionCount() - baseline;
        context.close();
        return count;
    }

    private static boolean hasField(Class<?> type, String name) {
        try {
            Field field = type.getDeclaredField(name);
            return !Modifier.isStatic(field.getModifiers());
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    private static Class<?> requireClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("缺少类 " + name
                    + "：请先用 run-guard-test.sh 编译 like-front/like-admin（脚本已包含该步骤）", e);
        }
    }

    // ---- 断言器 -------------------------------------------------------------

    private static void section(String title) {
        System.out.println();
        System.out.println("== " + title + " ==");
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            return;
        }
        failed++;
        System.out.println("  ✗ " + name);
    }

    private static void summary() {
        System.out.println();
        System.out.println("汇总：通过 " + passed + " 项，失败 " + failed + " 项");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private MockBeanWiringTest() {
    }
}
