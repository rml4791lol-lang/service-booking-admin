package com.mdd.common.settlement;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 真实社区系统 HTTP 客户端（占位实现，契约 v1）。
 *
 * <p>沿用 JDK 自带的 {@link HttpURLConnection}（与项目 {@code HttpUtils} 同源），
 * 因为签名头、HTTP 状态码与响应头是契约的一部分，通用 HttpUtils 无法表达。
 * 不引入任何第三方 HTTP 依赖。</p>
 *
 * <p>安全约束：日志只记录 batchNo / key / HTTP 状态 / 业务码，不打印请求体与 secret。</p>
 */
public class HttpSettlementReportClient implements SettlementReportClient {

    private static final Logger log = LoggerFactory.getLogger(HttpSettlementReportClient.class);
    private static final int MAX_RESPONSE_BYTES = 64 * 1024;

    private final SettlementProperties properties;

    public HttpSettlementReportClient(SettlementProperties properties) {
        this.properties = properties;
    }

    @Override
    public SettlementPushResult push(SettlementBatch batch, String idempotencyKey) {
        String body = JSON.toJSONString(batch);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
        String nonce = SettlementSigner.newNonce();
        String canonical = SettlementSigner.canonical(properties.getAppId(), timestamp, nonce,
                idempotencyKey, body);
        String signature = SettlementSigner.sign(properties.getSecret(), canonical);

        HttpURLConnection connection = null;
        try {
            URL url = new URL(properties.getEndpoint());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(properties.getConnectTimeoutMs());
            connection.setReadTimeout(properties.getReadTimeoutMs());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("X-LXT-App-Id", properties.getAppId());
            connection.setRequestProperty("X-LXT-Timestamp", timestamp);
            connection.setRequestProperty("X-LXT-Nonce", nonce);
            connection.setRequestProperty("X-LXT-Idempotency-Key", idempotencyKey);
            connection.setRequestProperty("X-LXT-Signature-Version", SettlementSigner.SIGNATURE_VERSION);
            connection.setRequestProperty("X-LXT-Signature", signature);

            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            connection.setFixedLengthStreamingMode(bytes.length);
            OutputStream output = connection.getOutputStream();
            try {
                output.write(bytes);
                output.flush();
            } finally {
                closeQuietly(output);
            }

            int httpStatus = connection.getResponseCode();
            String responseBody = readBody(httpStatus >= 400
                    ? connection.getErrorStream() : connection.getInputStream());
            log.info("分账上报 HTTP batchNo={} key={} status={}", batch.getBatchNo(), idempotencyKey, httpStatus);

            SettlementPushResult result = parseResponse(httpStatus, responseBody,
                    connection.getHeaderField("Retry-After"), batch, idempotencyKey);
            verifyResponseSignature(result, connection, batch);
            if (result.isSuccess() && properties.isVerifyResponseSignature() && !result.isResponseSignatureVerified()) {
                result.setSuccess(false);
                result.setRetryable(false);
                result.setCode("L0006");
                result.setMessage("社区系统响应签名缺失或校验失败");
            }
            return result;
        } catch (SocketTimeoutException e) {
            log.warn("分账上报超时 batchNo={} key={} endpoint={}", batch.getBatchNo(), idempotencyKey,
                    properties.statusView().get("endpointHost"));
            SettlementPushResult result = SettlementPushResult.failure("L0004", "连接或读取社区系统超时", true);
            result.setHttpStatus(null);
            return result;
        } catch (IOException e) {
            log.warn("分账上报网络异常 batchNo={} key={} error={}",
                    batch.getBatchNo(), idempotencyKey, e.getMessage());
            SettlementPushResult result = SettlementPushResult.failure("L0003", "调用社区系统网络异常", true);
            return result;
        } catch (Exception e) {
            log.error("分账上报客户端异常 batchNo={} key={}", batch.getBatchNo(), idempotencyKey, e);
            return SettlementPushResult.failure("L0005", "分账上报客户端异常", false);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private SettlementPushResult parseResponse(int httpStatus, String body, String retryAfter,
                                               SettlementBatch batch, String idempotencyKey) {
        SettlementPushResult result = new SettlementPushResult();
        result.setHttpStatus(httpStatus);
        result.setBatchNo(batch.getBatchNo());
        result.setIdempotencyKey(idempotencyKey);
        result.setAttempts(1);
        result.setItemCountExpected(batch.getItemCount());

        JSONObject json = null;
        try {
            if (body != null && body.trim().startsWith("{")) {
                json = JSON.parseObject(body);
            }
        } catch (Exception e) {
            log.warn("分账响应不是合法 JSON status={}", httpStatus);
        }
        String code = json == null ? "" : firstText(json, "code", "status", "errorCode");
        String message = json == null ? "" : firstText(json, "message", "msg", "errorMessage");
        boolean duplicateFlag = json != null && Boolean.TRUE.equals(json.getBoolean("duplicate"));
        result.setResponseCode(code);
        result.setRequestId(json == null ? "" : firstText(json, "requestId", "request_id", "traceId"));
        result.setReceiptNo(json == null ? "" : firstText(json, "receiptNo", "receipt_no", "settlementNo"));
        String echoedBatchNo = json == null ? "" : firstText(json, "batchNo", "batch_no");
        result.setReceiptBatchNo(echoedBatchNo);
        result.setReceivedItemCount(json == null ? null
                : firstInt(json, "receivedItemCount", "received_item_count"));
        if (!echoedBatchNo.isEmpty() && !echoedBatchNo.equals(batch.getBatchNo())) {
            result.setSuccess(false);
            result.setRetryable(false);
            result.setCode("L0005");
            result.setMessage("社区系统回执批次号与请求不一致");
            return result;
        }

        boolean httpSuccess = httpStatus >= 200 && httpStatus < 300;
        boolean duplicateCode = isDuplicateCode(code) || duplicateFlag;
        if ((httpSuccess || httpStatus == 409) && (isSuccessCode(code) || duplicateCode)) {
            result.setSuccess(true);
            result.setDuplicate(duplicateCode);
            result.setCode(duplicateCode ? "S1002" : "S0000");
            result.setMessage(message.isEmpty() ? (duplicateCode ? "重复请求，返回原回执" : "已接收") : message);
            result.setRetryable(false);
            applyItemCountCheck(result, batch);
            return result;
        }

        result.setSuccess(false);
        result.setRetryable(isRetryableHttp(httpStatus) || isRetryableCode(code));
        result.setCode(code.isEmpty() ? ("L0005") : code);
        result.setMessage(message.isEmpty() ? "社区系统返回异常（HTTP " + httpStatus + "）" : message);
        result.setRetryAfterMs(parseRetryAfterMs(retryAfter));
        return result;
    }

    private void verifyResponseSignature(SettlementPushResult result, HttpURLConnection connection,
                                         SettlementBatch batch) {
        String timestamp = connection.getHeaderField("X-LXT-Timestamp");
        String requestId = connection.getHeaderField("X-LXT-Request-Id");
        String responseSignature = connection.getHeaderField("X-LXT-Signature");
        if (timestamp == null || requestId == null || responseSignature == null) {
            result.setResponseSignatureVerified(false);
            return;
        }
        if (!result.getRequestId().isEmpty() && !result.getRequestId().equals(requestId)) {
            result.setResponseSignatureVerified(false);
            return;
        }
        String code = result.getResponseCode().isEmpty() ? result.getCode() : result.getResponseCode();
        String canonical = SettlementSigner.responseCanonical(requestId, timestamp, code, batch.getBatchNo());
        String expected = SettlementSigner.sign(properties.getSecret(), canonical);
        result.setResponseSignatureVerified(SettlementSigner.constantTimeEquals(expected, responseSignature));
    }

    private boolean isSuccessCode(String code) {
        if (code == null || code.isEmpty()) {
            return false;
        }
        String normalized = code.trim().toUpperCase(Locale.ROOT);
        return "S0000".equals(normalized) || "ACCEPTED".equals(normalized)
                || "SUCCESS".equals(normalized) || "OK".equals(normalized) || "0".equals(normalized);
    }

    private boolean isDuplicateCode(String code) {
        if (code == null) {
            return false;
        }
        String normalized = code.trim().toUpperCase(Locale.ROOT);
        return "S1002".equals(normalized) || "DUPLICATE".equals(normalized);
    }

    private boolean isRetryableHttp(int status) {
        return status == 408 || status == 425 || status == 429 || status >= 500;
    }

    private boolean isRetryableCode(String code) {
        if (code == null) {
            return false;
        }
        String normalized = code.trim().toUpperCase(Locale.ROOT);
        return "S4001".equals(normalized) || "S4290".equals(normalized)
                || "INTERNAL_ERROR".equals(normalized) || "RATE_LIMITED".equals(normalized);
    }

    private long parseRetryAfterMs(String retryAfter) {
        if (retryAfter == null || retryAfter.trim().isEmpty()) {
            return 0L;
        }
        try {
            long seconds = Long.parseLong(retryAfter.trim());
            return Math.max(0L, Math.min(seconds, 300L)) * 1000L;
        } catch (NumberFormatException ignored) {
            // HTTP-date 形式的 Retry-After 暂不解析，交回退策略处理
            return 0L;
        }
    }

    private String firstText(JSONObject json, String... keys) {
        for (String key : keys) {
            if (json.containsKey(key)) {
                Object value = json.get(key);
                return value == null ? "" : String.valueOf(value);
            }
        }
        return "";
    }

    private Integer firstInt(JSONObject json, String... keys) {
        for (String key : keys) {
            if (!json.containsKey(key)) {
                continue;
            }
            Object value = json.get(key);
            if (value == null) {
                return null;
            }
            try {
                return Integer.valueOf(String.valueOf(value).trim());
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }

    /**
     * 校验回执条数：契约 §5 的 {@code receivedItemCount} 是实际接收条数。
     * 与请求条数不一致时标记 result 并转失败，避免静默误判成功；
     * 回执未提供该字段时只记 warning，保持对旧版社区系统的兼容。
     */
    private void applyItemCountCheck(SettlementPushResult result, SettlementBatch batch) {
        Integer expected = batch == null ? null : batch.getItemCount();
        result.setItemCountExpected(expected);
        Integer received = result.getReceivedItemCount();
        if (received == null) {
            log.warn("分账回执缺少 receivedItemCount batchNo={} expected={}",
                    batch == null ? "" : batch.getBatchNo(), expected);
            return;
        }
        if (expected != null && !expected.equals(received)) {
            log.warn("分账回执条数不一致 batchNo={} expected={} received={}",
                    batch == null ? "" : batch.getBatchNo(), expected, received);
            result.setItemCountMismatch(true);
            result.setSuccess(false);
            result.setRetryable(false);
            result.setCode("L0009");
            result.setMessage("社区系统回执条数 " + received + " 与请求条数 " + expected + " 不一致，请人工对账");
        }
    }

    private String readBody(InputStream input) throws IOException {
        if (input == null) {
            return "";
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int total = 0;
        int read;
        try {
            while ((read = input.read(buffer)) != -1) {
                total += read;
                if (total > MAX_RESPONSE_BYTES) {
                    break;
                }
                output.write(buffer, 0, read);
            }
            return new String(output.toByteArray(), StandardCharsets.UTF_8);
        } finally {
            closeQuietly(input);
        }
    }

    private void closeQuietly(java.io.Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
                // ignore
            }
        }
    }

    @Override
    public String mode() {
        return SettlementProperties.MODE_HTTP;
    }
}
