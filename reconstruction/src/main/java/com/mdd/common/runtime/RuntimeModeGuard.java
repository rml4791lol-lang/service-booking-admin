package com.mdd.common.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 运行时模式守卫（#124）：把「真/假模式」收成一个结构性边界，而不是散落的布尔开关。
 *
 * <p>mock 登录 / 支付 / 退款 bean 只在显式打开对应开关、且不在生产/体验 profile 时装配
 * （见 {@code WxMockLoginServiceImpl}、{@code PayMockGatewayServiceImpl}、
 * {@code MockRefundTaskExecutor} 的 {@code @ConditionalOnProperty + @Profile}）。
 * 本守卫是最后一道网：配置写错时启动直接失败，不让服务带着「假成功」能力跑起来。</p>
 *
 * <p>规则：</p>
 * <ol>
 *   <li>生产/体验 profile（{@code prod}/{@code production}/{@code experience}）下，
 *       任一 mock/测试开关为 true 即启动失败，错误信息列出全部违规键
 *       （含此前被忽略的 {@code like.refund.mock-enabled}）；</li>
 *   <li>mock 开关只在测试 profile（{@code dev}/{@code test}/{@code local}）下允许；
 *       其它 profile 或未声明 profile 时打开 mock 同样启动失败；</li>
 *   <li>全部关闭时只输出一条 info，便于上线检查清单 C12 用启动日志核对。</li>
 * </ol>
 */
@Component
public class RuntimeModeGuard {

    private static final Logger log = LoggerFactory.getLogger(RuntimeModeGuard.class);

    /** 微信登录 mock（code2session 不请求微信，伪造 openid）。 */
    public static final String LOGIN_MOCK = "like.wechat.login.mock-enabled";
    /** 微信支付结果通知 mock（绕过微信回调）。 */
    public static final String PAY_MOCK = "like.pay.mock-callback-enabled";
    /** 微信退款执行器 mock（伪造退款受理成功）。 */
    public static final String REFUND_MOCK = "like.refund.mock-enabled";
    /** 测试通知接口（只允许给自己发订阅消息）。 */
    public static final String TEST_NOTIFY = "like.test-notify.enabled";

    /** 生产/体验 profile：结构上不允许任何 mock/测试开关。 */
    private static final List<String> PROD_LIKE_PROFILES =
            Collections.unmodifiableList(Arrays.asList("prod", "production", "experience"));

    /** 允许 mock 的测试 profile：本地联调 / 自动化 e2e 只能在这几个 profile 下开 mock。 */
    private static final List<String> MOCK_PROFILES =
            Collections.unmodifiableList(Arrays.asList("dev", "test", "local"));

    private static final Map<String, Boolean> SWITCHES = buildSwitches();

    public RuntimeModeGuard(Environment environment) {
        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
        Map<String, Boolean> enabled = new LinkedHashMap<String, Boolean>();
        for (String key : SWITCHES.keySet()) {
            enabled.put(key, Boolean.valueOf(
                    environment.getProperty(key, Boolean.class, Boolean.FALSE).booleanValue()));
        }

        List<String> violations = violations(activeProfiles, enabled);
        if (!violations.isEmpty()) {
            throw new IllegalStateException(failureMessage(activeProfiles, violations));
        }

        if (anyEnabled(enabled)) {
            log.error("本地 mock 已开启：{}；仅限测试 profile（dev/test/local）与测试构建，"
                            + "生产/体验严禁开启，上线前请按 docs/release/上线前检查清单.md 的 C12 项确认开关已关闭。",
                    describeEnabled(enabled));
        } else {
            log.info("微信 mock 开关已关闭：login.mock-enabled=false, pay.mock-callback-enabled=false, "
                    + "refund.mock-enabled=false, test-notify.enabled=false（生产/体验必须保持 false）");
        }
    }

    /**
     * 计算违规项。生产/体验 profile 下每一项开启的开关都是违规；其它 profile 下
     * 只有测试 profile（dev/test/local）才允许开 mock。
     *
     * @return 违规描述；空列表 = 通过
     */
    static List<String> violations(List<String> activeProfiles, Map<String, Boolean> enabled) {
        List<String> violations = new ArrayList<String>();
        List<String> enabledKeys = enabledKeys(enabled);
        if (enabledKeys.isEmpty()) {
            return violations;
        }
        if (hasProdLikeProfile(activeProfiles)) {
            for (String key : enabledKeys) {
                violations.add(key + "=true（生产/体验禁止 mock）");
            }
            return violations;
        }
        if (!hasMockProfile(activeProfiles)) {
            violations.add("mock 开关只在测试 profile（dev/test/local）下允许，当前 profile="
                    + profileLabel(activeProfiles) + "：" + String.join(", ", enabledKeys));
        }
        return violations;
    }

    static boolean hasProdLikeProfile(List<String> activeProfiles) {
        return containsAny(activeProfiles, PROD_LIKE_PROFILES);
    }

    static boolean hasMockProfile(List<String> activeProfiles) {
        return containsAny(activeProfiles, MOCK_PROFILES);
    }

    private static boolean containsAny(List<String> activeProfiles, List<String> candidates) {
        if (activeProfiles == null) {
            return false;
        }
        for (String profile : activeProfiles) {
            if (profile == null) {
                continue;
            }
            String normalized = profile.trim().toLowerCase(Locale.ROOT);
            for (String candidate : candidates) {
                if (candidate.equals(normalized)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String failureMessage(List<String> activeProfiles, List<String> violations) {
        return "运行时模式启动失败：" + String.join("；", violations)
                + "。mock 登录/支付/退款/测试通知只在测试 profile（dev/test/local）与测试构建中存在；"
                + "生产/体验（prod/production/experience）必须全部为 false，"
                + "否则会伪造登录、支付或退款成功。当前 profile=" + profileLabel(activeProfiles)
                + "。请修改配置/启动参数后重启。";
    }

    private static String profileLabel(List<String> activeProfiles) {
        if (activeProfiles == null || activeProfiles.isEmpty()) {
            return "（未激活，等同非测试环境）";
        }
        List<String> normalized = new ArrayList<String>();
        for (String profile : activeProfiles) {
            if (profile != null && !profile.trim().isEmpty()) {
                normalized.add(profile.trim());
            }
        }
        return normalized.isEmpty() ? "（未激活，等同非测试环境）" : String.join(",", normalized);
    }

    private static String describeEnabled(Map<String, Boolean> enabled) {
        List<String> parts = new ArrayList<String>();
        for (Map.Entry<String, Boolean> entry : enabled.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                parts.add(entry.getKey() + "=true");
            }
        }
        return String.join(", ", parts);
    }

    private static List<String> enabledKeys(Map<String, Boolean> enabled) {
        List<String> keys = new ArrayList<String>();
        if (enabled == null) {
            return keys;
        }
        for (Map.Entry<String, Boolean> entry : enabled.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    private static boolean anyEnabled(Map<String, Boolean> enabled) {
        return !enabledKeys(enabled).isEmpty();
    }

    private static Map<String, Boolean> buildSwitches() {
        Map<String, Boolean> switches = new LinkedHashMap<String, Boolean>();
        switches.put(LOGIN_MOCK, Boolean.FALSE);
        switches.put(PAY_MOCK, Boolean.FALSE);
        switches.put(REFUND_MOCK, Boolean.FALSE);
        switches.put(TEST_NOTIFY, Boolean.FALSE);
        return Collections.unmodifiableMap(switches);
    }
}
