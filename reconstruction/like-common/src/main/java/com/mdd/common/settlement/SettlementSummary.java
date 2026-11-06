package com.mdd.common.settlement;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 分账批次汇总。所有金额均已按四舍五入保留两位小数。
 */
public class SettlementSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer itemCount = 0;
    private Integer readyItemCount = 0;
    private Integer holdItemCount = 0;
    private Integer orderCount = 0;
    private BigDecimal paidOrderAmount = BigDecimal.ZERO;
    private BigDecimal settlementBaseAmount = BigDecimal.ZERO;
    private BigDecimal communityShareAmount = BigDecimal.ZERO;
    private BigDecimal agentShareAmount = BigDecimal.ZERO;
    private BigDecimal instructorShareAmount = BigDecimal.ZERO;
    private BigDecimal platformShareAmount = BigDecimal.ZERO;
    private BigDecimal shareTotalAmount = BigDecimal.ZERO;
    private Boolean balanced = Boolean.TRUE;
    private String currency = "CNY";

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getReadyItemCount() {
        return readyItemCount;
    }

    public void setReadyItemCount(Integer readyItemCount) {
        this.readyItemCount = readyItemCount;
    }

    public Integer getHoldItemCount() {
        return holdItemCount;
    }

    public void setHoldItemCount(Integer holdItemCount) {
        this.holdItemCount = holdItemCount;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getPaidOrderAmount() {
        return paidOrderAmount;
    }

    public void setPaidOrderAmount(BigDecimal paidOrderAmount) {
        this.paidOrderAmount = paidOrderAmount;
    }

    public BigDecimal getSettlementBaseAmount() {
        return settlementBaseAmount;
    }

    public void setSettlementBaseAmount(BigDecimal settlementBaseAmount) {
        this.settlementBaseAmount = settlementBaseAmount;
    }

    public BigDecimal getCommunityShareAmount() {
        return communityShareAmount;
    }

    public void setCommunityShareAmount(BigDecimal communityShareAmount) {
        this.communityShareAmount = communityShareAmount;
    }

    public BigDecimal getAgentShareAmount() {
        return agentShareAmount;
    }

    public void setAgentShareAmount(BigDecimal agentShareAmount) {
        this.agentShareAmount = agentShareAmount;
    }

    public BigDecimal getInstructorShareAmount() {
        return instructorShareAmount;
    }

    public void setInstructorShareAmount(BigDecimal instructorShareAmount) {
        this.instructorShareAmount = instructorShareAmount;
    }

    public BigDecimal getPlatformShareAmount() {
        return platformShareAmount;
    }

    public void setPlatformShareAmount(BigDecimal platformShareAmount) {
        this.platformShareAmount = platformShareAmount;
    }

    public BigDecimal getShareTotalAmount() {
        return shareTotalAmount;
    }

    public void setShareTotalAmount(BigDecimal shareTotalAmount) {
        this.shareTotalAmount = shareTotalAmount;
    }

    public Boolean getBalanced() {
        return balanced;
    }

    public void setBalanced(Boolean balanced) {
        this.balanced = balanced;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
