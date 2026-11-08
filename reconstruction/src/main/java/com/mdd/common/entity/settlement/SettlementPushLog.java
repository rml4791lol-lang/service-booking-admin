package com.mdd.common.entity.settlement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 分账上报结果日志（la_settlement_push_log，#56 / #71）。
 *
 * <p>后台每次调用 {@code POST /adminapi/finance.settlement/push} 写一条，
 * 包括演练（dryRun）、开关关闭、无 READY 数据、业务拒绝与重试耗尽。
 * #71 起按 {@code dedup_key}（批次幂等键 + 演练标记）做幂等：同一周期、同一范围的
 * 相同内容重复上报复用同一行，成功结果直接返回缓存、不再重复下发；
 * 失败/未出站/陈旧占位在同一行上重试，不新增记录。</p>
 *
 * <p>{@code receivedItemCount} 取自社区系统回执（契约 §5），-1 表示回执未提供；
 * 摘要字段只保存脱敏 JSON，不保存完整请求体、明细或 secret。</p>
 */
@TableName("la_settlement_push_log")
public class SettlementPushLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String batchNo;
    private String idempotencyKey;
    /**
     * 重复上报去重键（#71）：批次幂等键 + {@code :DRY}/{@code :LIVE}；
     * 无 READY 数据时以 {@code SKIP:} 前缀标识。唯一索引 {@code uk_dedup_key}
     * 保证同一逻辑批次的重复上报只保留一行记录（重试复用同一行）。
     */
    private String dedupKey;
    private String periodStart;
    private String periodEnd;
    private String batchType;
    private String originalBatchNo;
    private String scopeType;
    private Integer scopeId;
    private Integer itemCount;
    private Integer dryRun;
    private String clientMode;
    private String status;
    private Integer success;
    private Integer duplicate;
    private String code;
    private String message;
    private Integer httpStatus;
    private String responseCode;
    private String requestId;
    private String receiptNo;
    private String receiptBatchNo;
    private Integer receivedItemCount;
    private Integer itemCountMismatch;
    private Integer attempts;
    private Long elapsedMs;
    private String requestSummary;
    private String responseSummary;
    private Integer operatorAdminId;
    private String operatorName;
    private Integer createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDedupKey() {
        return dedupKey;
    }

    public void setDedupKey(String dedupKey) {
        this.dedupKey = dedupKey;
    }

    public String getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(String periodStart) {
        this.periodStart = periodStart;
    }

    public String getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(String periodEnd) {
        this.periodEnd = periodEnd;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }

    public String getOriginalBatchNo() {
        return originalBatchNo;
    }

    public void setOriginalBatchNo(String originalBatchNo) {
        this.originalBatchNo = originalBatchNo;
    }

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public Integer getScopeId() {
        return scopeId;
    }

    public void setScopeId(Integer scopeId) {
        this.scopeId = scopeId;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getDryRun() {
        return dryRun;
    }

    public void setDryRun(Integer dryRun) {
        this.dryRun = dryRun;
    }

    public String getClientMode() {
        return clientMode;
    }

    public void setClientMode(String clientMode) {
        this.clientMode = clientMode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(Integer duplicate) {
        this.duplicate = duplicate;
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

    public Integer getItemCountMismatch() {
        return itemCountMismatch;
    }

    public void setItemCountMismatch(Integer itemCountMismatch) {
        this.itemCountMismatch = itemCountMismatch;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Long getElapsedMs() {
        return elapsedMs;
    }

    public void setElapsedMs(Long elapsedMs) {
        this.elapsedMs = elapsedMs;
    }

    public String getRequestSummary() {
        return requestSummary;
    }

    public void setRequestSummary(String requestSummary) {
        this.requestSummary = requestSummary;
    }

    public String getResponseSummary() {
        return responseSummary;
    }

    public void setResponseSummary(String responseSummary) {
        this.responseSummary = responseSummary;
    }

    public Integer getOperatorAdminId() {
        return operatorAdminId;
    }

    public void setOperatorAdminId(Integer operatorAdminId) {
        this.operatorAdminId = operatorAdminId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}
