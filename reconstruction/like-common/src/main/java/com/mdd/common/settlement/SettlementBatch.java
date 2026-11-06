package com.mdd.common.settlement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 出站分账数据批次。
 *
 * <p>批次是幂等的：相同期间、相同范围、相同明细则生成相同的 batchNo 与 idempotencyKey。
 * generatedAt 仅用于审计，不参与幂等键计算。</p>
 */
public class SettlementBatch implements Serializable {

    private static final long serialVersionUID = 1L;

    private String schemaVersion = "1.0";
    private String batchNo = "";
    private String idempotencyKey = "";
    /** NORMAL=正常分账；REVERSAL=冲正/回滚，金额为负数。 */
    private String batchType = "NORMAL";
    private String originalBatchNo = "";
    private String sourceSystem = "luoxiaotong-sports";
    private String tenant = "lxt";
    private String generatedAt = "";
    private String periodStart = "";
    private String periodEnd = "";
    private SettlementScope scope = SettlementScope.all();
    private String currency = "CNY";
    private Integer itemCount = 0;
    private SettlementSummary summary = new SettlementSummary();
    private List<SettlementItem> items = new ArrayList<SettlementItem>();

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
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

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
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

    public SettlementScope getScope() {
        return scope;
    }

    public void setScope(SettlementScope scope) {
        this.scope = scope;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public SettlementSummary getSummary() {
        return summary;
    }

    public void setSummary(SettlementSummary summary) {
        this.summary = summary;
    }

    public List<SettlementItem> getItems() {
        return items;
    }

    public void setItems(List<SettlementItem> items) {
        this.items = items;
    }
}
