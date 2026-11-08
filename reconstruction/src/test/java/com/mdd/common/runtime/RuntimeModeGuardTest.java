package com.mdd.common.runtime;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

/**
 * #124 P4 运行时模式守卫契约测试。
 *
 * <p>本仓库离线 Maven 仓库里没有 junit / spring-boot-starter-test，因此沿用
 * {@code AssetRefContractTest} 的「带 main 的纯 Java 断言器」：不新增依赖，
 * 直接实例化真实的 {@link RuntimeModeGuard}（{@link StandardEnvironment} 注入
 * profile 与配置），断言“该失败的启动失败、错误信息可定位到具体键”。</p>
 *
 * <p>运行：{@code bash tools/runtime-mode/run-guard-test.sh}（mvn test-compile + java）。</p>
 *
 * <p>覆盖：</p>
 * <ol>
 *   <li>生产/体验 profile（prod/production/experience）下四个开关分别误开 → 启动失败；</li>
 *   <li>{@code like.refund.mock-enabled} 单独误开必须被拦截（旧守卫忽略项）；</li>
 *   <li>多个开关同时误开时，错误信息列出全部违规键；</li>
 *   <li>dev/test/local 测试 profile 下 mock 登录/支付/退款可用（不阻断启动）；</li>
 *   <li>未声明 profile / 非测试 profile 下开 mock → 启动失败并给出 profile 指引；</li>
 *   <li>全部关闭时生产/测试 profile 都能正常启动。</li>
 * </ol>
 */
public final class RuntimeModeGuardTest {

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        section("生产/体验 profile：任一 mock 开关误开都必须启动失败");
        expectFailure("prod + 登录 mock", new String[]{"prod"}, props(RuntimeModeGuard.LOGIN_MOCK, true),
                RuntimeModeGuard.LOGIN_MOCK, "生产/体验");
        expectFailure("prod + 支付 mock", new String[]{"prod"}, props(RuntimeModeGuard.PAY_MOCK, true),
                RuntimeModeGuard.PAY_MOCK, "生产/体验");
        expectFailure("prod + 退款 mock（旧守卫漏项）", new String[]{"prod"},
                props(RuntimeModeGuard.REFUND_MOCK, true),
                RuntimeModeGuard.REFUND_MOCK, "生产/体验");
        expectFailure("prod + 测试通知", new String[]{"prod"}, props(RuntimeModeGuard.TEST_NOTIFY, true),
                RuntimeModeGuard.TEST_NOTIFY, "生产/体验");
        expectFailure("production（大小写不敏感）+ 退款 mock", new String[]{"Production"},
                props(RuntimeModeGuard.REFUND_MOCK, true), RuntimeModeGuard.REFUND_MOCK, "生产/体验");
        expectFailure("experience + 支付 mock", new String[]{"experience"},
                props(RuntimeModeGuard.PAY_MOCK, true), RuntimeModeGuard.PAY_MOCK, "生产/体验");
        expectFailure("prod + 多开（登录+退款）列出全部违规键", new String[]{"prod"},
                props(RuntimeModeGuard.LOGIN_MOCK, true, RuntimeModeGuard.REFUND_MOCK, true),
                RuntimeModeGuard.LOGIN_MOCK, RuntimeModeGuard.REFUND_MOCK);

        section("测试 profile：mock 登录/支付/退款仍可用");
        expectPass("dev + 登录/支付 mock", new String[]{"dev"},
                props(RuntimeModeGuard.LOGIN_MOCK, true, RuntimeModeGuard.PAY_MOCK, true));
        expectPass("dev + 退款 mock", new String[]{"dev"}, props(RuntimeModeGuard.REFUND_MOCK, true));
        expectPass("test + 全部 mock", new String[]{"test"},
                props(RuntimeModeGuard.LOGIN_MOCK, true, RuntimeModeGuard.PAY_MOCK, true,
                        RuntimeModeGuard.REFUND_MOCK, true, RuntimeModeGuard.TEST_NOTIFY, true));
        expectPass("local + 支付 mock", new String[]{"local"}, props(RuntimeModeGuard.PAY_MOCK, true));

        section("非生产也非测试 profile：mock 必须被拒绝");
        expectFailure("未声明 profile + 登录 mock", new String[]{},
                props(RuntimeModeGuard.LOGIN_MOCK, true), "dev/test/local", "未激活");
        expectFailure("staging + 退款 mock", new String[]{"staging"},
                props(RuntimeModeGuard.REFUND_MOCK, true), "dev/test/local", "staging");

        section("全部关闭：生产/测试 profile 均可启动");
        expectPass("prod + 全部关闭", new String[]{"prod"}, props());
        expectPass("experience + 全部关闭", new String[]{"experience"}, props());
        expectPass("dev + 全部关闭", new String[]{"dev"}, props());
        expectPass("未声明 profile + 全部关闭", new String[]{}, props());

        section("违规项计算（不依赖 Spring 环境）");
        Map<String, Boolean> refundOn = allOff();
        refundOn.put(RuntimeModeGuard.REFUND_MOCK, Boolean.TRUE);
        check("生产 profile 的退款 mock 计入违规",
                RuntimeModeGuard.violations(java.util.Arrays.asList("prod"), refundOn).size() == 1);
        check("测试 profile 的退款 mock 不计违规",
                RuntimeModeGuard.violations(java.util.Arrays.asList("test"), refundOn).isEmpty());
        check("mock bean 的 @Profile 只排除生产/体验",
                !RuntimeModeGuard.hasProdLikeProfile(java.util.Arrays.asList("dev", "test"))
                        && RuntimeModeGuard.hasProdLikeProfile(java.util.Arrays.asList("experience")));

        summary();
    }

    // ---- 场景 ---------------------------------------------------------------

    private static void expectFailure(String name, String[] profiles, Map<String, String> properties,
                                      String... expectedFragments) {
        try {
            new RuntimeModeGuard(environment(profiles, properties));
            failed++;
            System.out.println("  ✗ " + name + "：预期启动失败，实际通过");
        } catch (IllegalStateException error) {
            String message = error.getMessage() == null ? "" : error.getMessage();
            boolean ok = message.contains("运行时模式启动失败");
            for (String fragment : expectedFragments) {
                ok = ok && message.contains(fragment);
            }
            if (ok) {
                passed++;
                System.out.println("  ✓ " + name + " → " + compact(message));
            } else {
                failed++;
                System.out.println("  ✗ " + name + "：错误信息缺少关键片段，实际：" + message);
            }
        }
    }

    private static void expectPass(String name, String[] profiles, Map<String, String> properties) {
        try {
            new RuntimeModeGuard(environment(profiles, properties));
            passed++;
            System.out.println("  ✓ " + name);
        } catch (RuntimeException error) {
            failed++;
            System.out.println("  ✗ " + name + "：预期可启动，实际失败：" + error.getMessage());
        }
    }

    private static StandardEnvironment environment(String[] profiles, Map<String, String> properties) {
        StandardEnvironment environment = new StandardEnvironment();
        Map<String, Object> values = new LinkedHashMap<String, Object>();
        values.putAll(properties);
        if (!values.isEmpty()) {
            environment.getPropertySources().addFirst(new MapPropertySource("runtime-mode-test", values));
        }
        if (profiles.length > 0) {
            environment.setActiveProfiles(profiles);
        }
        return environment;
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

    private static String compact(String message) {
        String text = message.replace('\n', ' ').trim();
        return text.length() <= 88 ? text : text.substring(0, 85) + "...";
    }

    private static Map<String, String> props(Object... pairs) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (int i = 0; i + 1 < pairs.length; i += 2) {
            map.put(String.valueOf(pairs[i]), String.valueOf(pairs[i + 1]));
        }
        return map;
    }

    private static Map<String, Boolean> allOff() {
        Map<String, Boolean> map = new LinkedHashMap<String, Boolean>();
        map.put(RuntimeModeGuard.LOGIN_MOCK, Boolean.FALSE);
        map.put(RuntimeModeGuard.PAY_MOCK, Boolean.FALSE);
        map.put(RuntimeModeGuard.REFUND_MOCK, Boolean.FALSE);
        map.put(RuntimeModeGuard.TEST_NOTIFY, Boolean.FALSE);
        return map;
    }

    private RuntimeModeGuardTest() {
    }
}
