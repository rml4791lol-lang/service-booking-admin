package com.mdd.common.settlement;

import java.io.Serializable;

/**
 * 一次分账上报的结果（含重试后的最终结果）。
 */
public class SettlementPushResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private boolean duplicate;
    private boolean retryable;
    private boolean disabled;
    private boolean dryRun;
    private String code = "";
    private String message = "";
    private int attempts;
    private Integer httpStatus;
    private String responseCode = "";
    private String requestId = "";
    private String receiptNo = "";
    private String receiptBatchNo = "";
    private String batchNo = "";
    private String idempotencyKey = "";
    private Integer receivedItemCount;
    private Integer itemCountExpected;
    private boolean itemCountMismatch;
    private boolean responseSignatureVerified;
    private long elapsedMs;
    private long retryAfterMs;

    public static SettlementPushResult failure(String code, String message, boolean retryable) {
        SettlementPushResult result = new SettlementPushResult();
        result.setSuccess(false);
        result.setRetryable(retryable);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }

    public boolean isRetryable() {
        return retryable;
    }

    public void setRetryable(boolean retryable) {
        this.retryable = retryable;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDryRun() {
        return dryRun;
    }

    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getReceiptBatchNo() {
        return receiptBatchNo;
    }

    public void setReceiptBatchNo(String receiptBatchNo) {
        this.receiptBatchNo = receiptBatchNo;
    }

    public Integer getReceivedItemCount() {
        return receivedItemCount;
    }

    public void setReceivedItemCount(Integer receivedItemCount) {
        this.receivedItemCount = receivedItemCount;
    }

    public Integer getItemCountExpected() {
        return itemCountExpected;
    }

    public void setItemCountExpected(Integer itemCountExpected) {
        this.itemCountExpected = itemCountExpected;
    }

    public boolean isItemCountMismatch() {
        return itemCountMismatch;
    }

    public void setItemCountMismatch(boolean itemCountMismatch) {
        this.itemCountMismatch = itemCountMismatch;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public boolean isResponseSignatureVerified() {
        return responseSignatureVerified;
    }

    public void setResponseSignatureVerified(boolean responseSignatureVerified) {
        this.responseSignatureVerified = responseSignatureVerified;
    }

    public long getElapsedMs() {
        return elapsedMs;
    }

    public long getRetryAfterMs() {
        return retryAfterMs;
    }

    public void setRetryAfterMs(long retryAfterMs) {
        this.retryAfterMs = retryAfterMs;
    }

    public void setElapsedMs(long elapsedMs) {
        this.elapsedMs = elapsedMs;
    }
}
