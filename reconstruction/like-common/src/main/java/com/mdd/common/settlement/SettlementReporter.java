package com.mdd.common.settlement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 分账上报编排：选客户端、生成幂等键、失败重试、记录最近一次结果。
 *
 * <p>默认关闭；关闭时 {@link #push(SettlementBatch)} 直接返回 disabled，不触网、不抛异常，
 * 不影响任何既有业务。只有显式调用本服务（后台受控入口）才会产生网络请求。</p>
 */
@Component
public class SettlementReporter {

    private static final Logger log = LoggerFactory.getLogger(SettlementReporter.class);

    private final SettlementProperties properties;
    private final StubSettlementReportClient stubClient;
    private final HttpSettlementReportClient httpClient;
    private final AtomicReference<Map<String, Object>> lastResult = new AtomicReference<Map<String, Object>>();

    public SettlementReporter(SettlementProperties properties) {
        this.properties = properties;
        this.stubClient = new StubSettlementReportClient();
        this.httpClient = new HttpSettlementReportClient(properties);
    }

    public SettlementPushResult push(SettlementBatch batch) {
        long startedAt = System.currentTimeMillis();
        SettlementPushResult result;
        if (!properties.isEnabled()) {
            result = SettlementPushResult.failure("L0001", "分账上报适配器未启用（like.settlement.enabled=false）", false);
            result.setDisabled(true);
            result.setBatchNo(batch == null ? "" : batch.getBatchNo());
            result.setElapsedMs(System.currentTimeMillis() - startedAt);
            remember(batch, result);
            return result;
        }
        if (batch == null || batch.getItems() == null || batch.getItems().isEmpty()) {
            result = SettlementPushResult.failure("L0008", "没有可上报的 READY 分账数据", false);
            result.setElapsedMs(System.currentTimeMillis() - startedAt);
            remember(batch, result);
            return result;
        }
        String configIssue = properties.configIssue();
        if (configIssue != null) {
            result = SettlementPushResult.failure("L0002", configIssue, false);
            result.setElapsedMs(System.currentTimeMillis() - startedAt);
            remember(batch, result);
            return result;
        }

        prepareKeys(batch);
        SettlementReportClient client = properties.isHttpMode() ? httpClient : stubClient;
        int maxAttempts = properties.getMaxAttempts();
        result = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            result = client.push(batch, batch.getIdempotencyKey());
            result.setAttempts(attempt);
            result.setBatchNo(batch.getBatchNo());
            result.setIdempotencyKey(batch.getIdempotencyKey());
            if (result.isSuccess()) {
                break;
            }
            boolean canRetry = result.isRetryable() && attempt < maxAttempts;
            log.warn("分账上报失败 attempt={}/{} code={} retryable={} msg={}",
                    attempt, maxAttempts, result.getCode(), result.isRetryable(), result.getMessage());
            if (!canRetry) {
                if (result.isRetryable()) {
                    result.setCode("L0007");
                    result.setMessage(result.getMessage() + "（重试次数已用尽）");
                }
                break;
            }
            long backoff = backoffMs(attempt);
            if (result.getRetryAfterMs() > 0) {
                backoff = Math.max(backoff, Math.min(result.getRetryAfterMs(), 30000L));
            }
            if (!sleep(backoff)) {
                result.setSuccess(false);
                result.setRetryable(false);
                result.setCode("L0004");
                result.setMessage("分账上报等待重试时被中断");
                break;
            }
        }
        if (result == null) {
            result = SettlementPushResult.failure("L0005", "分账上报未执行", false);
        }
        result.setElapsedMs(System.currentTimeMillis() - startedAt);
        remember(batch, result);
        return result;
    }

    private void prepareKeys(SettlementBatch batch) {
        if (batch.getScope() == null) {
            batch.setScope(SettlementScope.all());
        }
        if (batch.getBatchNo() == null || batch.getBatchNo().isEmpty()) {
            batch.setBatchNo(SettlementIdempotency.batchNo(batch.getPeriodStart(), batch.getPeriodEnd(),
                    batch.getScope().getType(), batch.getScope().getId(), batch.getBatchType(),
                    batch.getOriginalBatchNo(), batch.getItems()));
        }
        if (batch.getIdempotencyKey() == null || batch.getIdempotencyKey().isEmpty()) {
            batch.setIdempotencyKey(SettlementIdempotency.key(batch.getPeriodStart(), batch.getPeriodEnd(),
                    batch.getScope().getType(), batch.getScope().getId(), batch.getBatchType(),
                    batch.getOriginalBatchNo(), batch.getItems()));
        }
        if (batch.getGeneratedAt() == null || batch.getGeneratedAt().isEmpty()) {
            batch.setGeneratedAt(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date()));
        }
        batch.setItemCount(batch.getItems() == null ? 0 : batch.getItems().size());
    }

    private long backoffMs(int attempt) {
        long base = properties.getRetryBackoffMs();
        long max = properties.getMaxRetryBackoffMs();
        long delay = base;
        for (int i = 1; i < attempt; i++) {
            delay = delay * 2L;
            if (delay >= max) {
                return max;
            }
        }
        return Math.min(delay, max);
    }

    private boolean sleep(long millis) {
        if (millis <= 0) {
            return true;
        }
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private void remember(SettlementBatch batch, SettlementPushResult result) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("batchNo", batch == null ? "" : batch.getBatchNo());
        map.put("idempotencyKey", batch == null ? "" : batch.getIdempotencyKey());
        map.put("itemCount", batch == null ? 0 : batch.getItemCount());
        map.put("success", result.isSuccess());
        map.put("duplicate", result.isDuplicate());
        map.put("code", result.getCode());
        map.put("message", result.getMessage());
        map.put("attempts", result.getAttempts());
        map.put("httpStatus", result.getHttpStatus());
        map.put("receiptNo", result.getReceiptNo());
        map.put("receiptBatchNo", result.getReceiptBatchNo());
        map.put("receivedItemCount", result.getReceivedItemCount());
        map.put("itemCountExpected", result.getItemCountExpected());
        map.put("itemCountMismatch", result.isItemCountMismatch());
        map.put("elapsedMs", result.getElapsedMs());
        lastResult.set(map);
    }

    /**
     * 适配器状态，供后台只读查看；包含脱敏配置与最近一次上报结果，不含 secret。
     */
    public Map<String, Object> status() {
        Map<String, Object> status = new LinkedHashMap<String, Object>();
        status.putAll(properties.statusView());
        status.put("clientMode", properties.isEnabled() ? (properties.isHttpMode() ? "http" : "stub") : "disabled");
        status.put("stub", stubClient.statusView());
        status.put("lastPush", lastResult.get());
        return status;
    }

    public SettlementProperties getProperties() {
        return properties;
    }
}
