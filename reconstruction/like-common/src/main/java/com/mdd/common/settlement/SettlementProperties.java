package com.mdd.common.settlement;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 分账上报适配器配置。
 *
 * <p>所有配置都支持 Spring 环境变量覆盖，例如
 * {@code LIKE_SETTLEMENT_ENABLED=true}、{@code LIKE_SETTLEMENT_MODE=http}、
 * {@code LIKE_SETTLEMENT_ENDPOINT=...}、{@code LIKE_SETTLEMENT_APP_ID=...}、
 * {@code LIKE_SETTLEMENT_SECRET=...}。默认关闭，不改变既有业务流程。</p>
 */
@Component
public class SettlementProperties {

    public static final String MODE_STUB = "stub";
    public static final String MODE_HTTP = "http";

    private final boolean enabled;
    private final String mode;
    private final String endpoint;
    private final String appId;
    private final String secret;
    private final int connectTimeoutMs;
    private final int readTimeoutMs;
    private final int maxAttempts;
    private final long retryBackoffMs;
    private final long maxRetryBackoffMs;
    private final int timestampToleranceSeconds;
    private final boolean verifyResponseSignature;

    public SettlementProperties(
            @Value("${like.settlement.enabled:false}") String enabled,
            @Value("${like.settlement.mode:stub}") String mode,
            @Value("${like.settlement.endpoint:}") String endpoint,
            @Value("${like.settlement.app-id:}") String appId,
            @Value("${like.settlement.secret:}") String secret,
            @Value("${like.settlement.connect-timeout-ms:3000}") String connectTimeoutMs,
            @Value("${like.settlement.read-timeout-ms:5000}") String readTimeoutMs,
            @Value("${like.settlement.max-attempts:3}") String maxAttempts,
            @Value("${like.settlement.retry-backoff-ms:500}") String retryBackoffMs,
            @Value("${like.settlement.max-retry-backoff-ms:8000}") String maxRetryBackoffMs,
            @Value("${like.settlement.timestamp-tolerance-seconds:300}") String timestampToleranceSeconds,
            @Value("${like.settlement.verify-response-signature:true}") String verifyResponseSignature) {
        this.enabled = parseBoolean(enabled);
        this.mode = normalizeMode(mode);
        this.endpoint = trim(endpoint);
        this.appId = trim(appId);
        this.secret = trim(secret);
        this.connectTimeoutMs = clamp(parseInt(connectTimeoutMs, 3000), 200, 60000);
        this.readTimeoutMs = clamp(parseInt(readTimeoutMs, 5000), 200, 120000);
        this.maxAttempts = clamp(parseInt(maxAttempts, 3), 1, 10);
        this.retryBackoffMs = clamp(parseLong(retryBackoffMs, 500L), 0L, 60000L);
        this.maxRetryBackoffMs = clamp(parseLong(maxRetryBackoffMs, 8000L), this.retryBackoffMs, 120000L);
        this.timestampToleranceSeconds = clamp(parseInt(timestampToleranceSeconds, 300), 30, 3600);
        this.verifyResponseSignature = verifyResponseSignature == null || Boolean.parseBoolean(verifyResponseSignature.trim());
    }

    private static String normalizeMode(String value) {
        String mode = trim(value).toLowerCase();
        if (MODE_HTTP.equals(mode)) {
            return MODE_HTTP;
        }
        return MODE_STUB;
    }

    private static boolean parseBoolean(String value) {
        return value != null && Boolean.parseBoolean(value.trim());
    }

    private static int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(trim(value));
        } catch (Exception ignored) {
            return fallback;
        }
    }

    private static long parseLong(String value, long fallback) {
        try {
            return Long.parseLong(trim(value));
        } catch (Exception ignored) {
            return fallback;
        }
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static long clamp(long value, long min, long max) {
        return Math.max(min, Math.min(max, value));
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getMode() {
        return mode;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getAppId() {
        return appId;
    }

    public String getSecret() {
        return secret;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public long getRetryBackoffMs() {
        return retryBackoffMs;
    }

    public long getMaxRetryBackoffMs() {
        return maxRetryBackoffMs;
    }

    public int getTimestampToleranceSeconds() {
        return timestampToleranceSeconds;
    }

    public boolean isVerifyResponseSignature() {
        return verifyResponseSignature;
    }

    public boolean isHttpMode() {
        return MODE_HTTP.equals(mode);
    }

    /**
     * 返回配置是否有缺口（只用于 http 模式）。不返回 secret 内容。
     */
    public String configIssue() {
        if (!enabled) {
            return "适配器未启用（like.settlement.enabled=false）";
        }
        if (!isHttpMode()) {
            return null;
        }
        if (endpoint.isEmpty()) {
            return "缺少 like.settlement.endpoint（可用 LIKE_SETTLEMENT_ENDPOINT 注入）";
        }
        if (!endpoint.startsWith("https://") && !endpoint.startsWith("http://")) {
            return "like.settlement.endpoint 必须以 http:// 或 https:// 开头";
        }
        if (appId.isEmpty()) {
            return "缺少 like.settlement.app-id（可用 LIKE_SETTLEMENT_APP_ID 注入）";
        }
        if (secret.isEmpty()) {
            return "缺少 like.settlement.secret（可用 LIKE_SETTLEMENT_SECRET 注入）";
        }
        return null;
    }

    /**
     * 给后台状态接口使用的脱敏配置视图，不包含 secret。
     */
    public Map<String, Object> statusView() {
        Map<String, Object> status = new LinkedHashMap<String, Object>();
        status.put("enabled", enabled);
        status.put("mode", enabled ? mode : "disabled");
        status.put("endpointConfigured", !endpoint.isEmpty());
        status.put("endpointHost", hostOf(endpoint));
        status.put("appIdConfigured", !appId.isEmpty());
        status.put("secretConfigured", !secret.isEmpty());
        status.put("connectTimeoutMs", connectTimeoutMs);
        status.put("readTimeoutMs", readTimeoutMs);
        status.put("maxAttempts", maxAttempts);
        status.put("retryBackoffMs", retryBackoffMs);
        status.put("maxRetryBackoffMs", maxRetryBackoffMs);
        status.put("timestampToleranceSeconds", timestampToleranceSeconds);
        status.put("verifyResponseSignature", verifyResponseSignature);
        status.put("configIssue", configIssue());
        return status;
    }

    private String hostOf(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }
        try {
            java.net.URL parsed = new java.net.URL(url);
            return parsed.getProtocol() + "://" + parsed.getHost()
                    + (parsed.getPort() > 0 ? ":" + parsed.getPort() : "");
        } catch (Exception ignored) {
            return "(invalid-url)";
        }
    }
}
