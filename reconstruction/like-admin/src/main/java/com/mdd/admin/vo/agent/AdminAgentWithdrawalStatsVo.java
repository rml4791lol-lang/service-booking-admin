/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.agent.AdminAgentWithdrawalStatsVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.agent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u7ba1\u7406\u5458\u4ee3\u7406\u63d0\u73b0\u5ba1\u6838\u7edf\u8ba1Vo")
public class AdminAgentWithdrawalStatsVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u672c\u6708\u63d0\u73b0\u603b\u989d")
    private BigDecimal monthWithdrawalAmount;
    @ApiModelProperty(value="\u672c\u6708\u63d0\u73b0\u603b\u7b14\u6570")
    private Integer monthWithdrawalCount;
    @ApiModelProperty(value="\u5f85\u5904\u7406\u7b14\u6570")
    private Integer pendingCount;
    @ApiModelProperty(value="\u603b\u63d0\u73b0\u603b\u989d")
    private BigDecimal totalWithdrawalAmount;

    public BigDecimal getMonthWithdrawalAmount() {
        return this.monthWithdrawalAmount;
    }

    public Integer getMonthWithdrawalCount() {
        return this.monthWithdrawalCount;
    }

    public Integer getPendingCount() {
        return this.pendingCount;
    }

    public BigDecimal getTotalWithdrawalAmount() {
        return this.totalWithdrawalAmount;
    }

    public void setMonthWithdrawalAmount(BigDecimal monthWithdrawalAmount) {
        this.monthWithdrawalAmount = monthWithdrawalAmount;
    }

    public void setMonthWithdrawalCount(Integer monthWithdrawalCount) {
        this.monthWithdrawalCount = monthWithdrawalCount;
    }

    public void setPendingCount(Integer pendingCount) {
        this.pendingCount = pendingCount;
    }

    public void setTotalWithdrawalAmount(BigDecimal totalWithdrawalAmount) {
        this.totalWithdrawalAmount = totalWithdrawalAmount;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AdminAgentWithdrawalStatsVo)) {
            return false;
        }
        AdminAgentWithdrawalStatsVo other = (AdminAgentWithdrawalStatsVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$monthWithdrawalCount = this.getMonthWithdrawalCount();
        Integer other$monthWithdrawalCount = other.getMonthWithdrawalCount();
        if (this$monthWithdrawalCount == null ? other$monthWithdrawalCount != null : !((Object)this$monthWithdrawalCount).equals(other$monthWithdrawalCount)) {
            return false;
        }
        Integer this$pendingCount = this.getPendingCount();
        Integer other$pendingCount = other.getPendingCount();
        if (this$pendingCount == null ? other$pendingCount != null : !((Object)this$pendingCount).equals(other$pendingCount)) {
            return false;
        }
        BigDecimal this$monthWithdrawalAmount = this.getMonthWithdrawalAmount();
        BigDecimal other$monthWithdrawalAmount = other.getMonthWithdrawalAmount();
        if (this$monthWithdrawalAmount == null ? other$monthWithdrawalAmount != null : !((Object)this$monthWithdrawalAmount).equals(other$monthWithdrawalAmount)) {
            return false;
        }
        BigDecimal this$totalWithdrawalAmount = this.getTotalWithdrawalAmount();
        BigDecimal other$totalWithdrawalAmount = other.getTotalWithdrawalAmount();
        return !(this$totalWithdrawalAmount == null ? other$totalWithdrawalAmount != null : !((Object)this$totalWithdrawalAmount).equals(other$totalWithdrawalAmount));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AdminAgentWithdrawalStatsVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $monthWithdrawalCount = this.getMonthWithdrawalCount();
        result = result * 59 + ($monthWithdrawalCount == null ? 43 : ((Object)$monthWithdrawalCount).hashCode());
        Integer $pendingCount = this.getPendingCount();
        result = result * 59 + ($pendingCount == null ? 43 : ((Object)$pendingCount).hashCode());
        BigDecimal $monthWithdrawalAmount = this.getMonthWithdrawalAmount();
        result = result * 59 + ($monthWithdrawalAmount == null ? 43 : ((Object)$monthWithdrawalAmount).hashCode());
        BigDecimal $totalWithdrawalAmount = this.getTotalWithdrawalAmount();
        result = result * 59 + ($totalWithdrawalAmount == null ? 43 : ((Object)$totalWithdrawalAmount).hashCode());
        return result;
    }

    public String toString() {
        return "AdminAgentWithdrawalStatsVo(monthWithdrawalAmount=" + this.getMonthWithdrawalAmount() + ", monthWithdrawalCount=" + this.getMonthWithdrawalCount() + ", pendingCount=" + this.getPendingCount() + ", totalWithdrawalAmount=" + this.getTotalWithdrawalAmount() + ")";
    }
}

