/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.agent.AgentFinanceStatsVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.agent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u4ee3\u7406\u8d22\u52a1\u7edf\u8ba1Vo")
public class AgentFinanceStatsVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u672c\u6708\u603b\u6536\u76ca")
    private BigDecimal totalRevenue;
    @ApiModelProperty(value="\u6210\u529f\u63d0\u73b0\u7b14\u6570")
    private Integer successCount;
    @ApiModelProperty(value="\u5f85\u5904\u7406\u63d0\u73b0\u7b14\u6570")
    private Integer pendingCount;
    @ApiModelProperty(value="\u62d2\u7edd\u63d0\u73b0\u7b14\u6570")
    private Integer refundCount;
    @ApiModelProperty(value="\u53ef\u63d0\u73b0\u4f59\u989d")
    private BigDecimal withdrawableAmount;
    @ApiModelProperty(value="\u4ee3\u7406\u540d\u79f0")
    private String agentName;
    @ApiModelProperty(value="\u63d0\u73b0\u94f6\u884c\u540d\u79f0")
    private String bankName;
    @ApiModelProperty(value="\u63d0\u73b0\u94f6\u884c\u5361\u53f7")
    private String bankCard;

    public BigDecimal getTotalRevenue() {
        return this.totalRevenue;
    }

    public Integer getSuccessCount() {
        return this.successCount;
    }

    public Integer getPendingCount() {
        return this.pendingCount;
    }

    public Integer getRefundCount() {
        return this.refundCount;
    }

    public BigDecimal getWithdrawableAmount() {
        return this.withdrawableAmount;
    }

    public String getAgentName() {
        return this.agentName;
    }

    public String getBankName() {
        return this.bankName;
    }

    public String getBankCard() {
        return this.bankCard;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public void setPendingCount(Integer pendingCount) {
        this.pendingCount = pendingCount;
    }

    public void setRefundCount(Integer refundCount) {
        this.refundCount = refundCount;
    }

    public void setWithdrawableAmount(BigDecimal withdrawableAmount) {
        this.withdrawableAmount = withdrawableAmount;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AgentFinanceStatsVo)) {
            return false;
        }
        AgentFinanceStatsVo other = (AgentFinanceStatsVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$successCount = this.getSuccessCount();
        Integer other$successCount = other.getSuccessCount();
        if (this$successCount == null ? other$successCount != null : !((Object)this$successCount).equals(other$successCount)) {
            return false;
        }
        Integer this$pendingCount = this.getPendingCount();
        Integer other$pendingCount = other.getPendingCount();
        if (this$pendingCount == null ? other$pendingCount != null : !((Object)this$pendingCount).equals(other$pendingCount)) {
            return false;
        }
        Integer this$refundCount = this.getRefundCount();
        Integer other$refundCount = other.getRefundCount();
        if (this$refundCount == null ? other$refundCount != null : !((Object)this$refundCount).equals(other$refundCount)) {
            return false;
        }
        BigDecimal this$totalRevenue = this.getTotalRevenue();
        BigDecimal other$totalRevenue = other.getTotalRevenue();
        if (this$totalRevenue == null ? other$totalRevenue != null : !((Object)this$totalRevenue).equals(other$totalRevenue)) {
            return false;
        }
        BigDecimal this$withdrawableAmount = this.getWithdrawableAmount();
        BigDecimal other$withdrawableAmount = other.getWithdrawableAmount();
        if (this$withdrawableAmount == null ? other$withdrawableAmount != null : !((Object)this$withdrawableAmount).equals(other$withdrawableAmount)) {
            return false;
        }
        String this$agentName = this.getAgentName();
        String other$agentName = other.getAgentName();
        if (this$agentName == null ? other$agentName != null : !this$agentName.equals(other$agentName)) {
            return false;
        }
        String this$bankName = this.getBankName();
        String other$bankName = other.getBankName();
        if (this$bankName == null ? other$bankName != null : !this$bankName.equals(other$bankName)) {
            return false;
        }
        String this$bankCard = this.getBankCard();
        String other$bankCard = other.getBankCard();
        return !(this$bankCard == null ? other$bankCard != null : !this$bankCard.equals(other$bankCard));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AgentFinanceStatsVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $successCount = this.getSuccessCount();
        result = result * 59 + ($successCount == null ? 43 : ((Object)$successCount).hashCode());
        Integer $pendingCount = this.getPendingCount();
        result = result * 59 + ($pendingCount == null ? 43 : ((Object)$pendingCount).hashCode());
        Integer $refundCount = this.getRefundCount();
        result = result * 59 + ($refundCount == null ? 43 : ((Object)$refundCount).hashCode());
        BigDecimal $totalRevenue = this.getTotalRevenue();
        result = result * 59 + ($totalRevenue == null ? 43 : ((Object)$totalRevenue).hashCode());
        BigDecimal $withdrawableAmount = this.getWithdrawableAmount();
        result = result * 59 + ($withdrawableAmount == null ? 43 : ((Object)$withdrawableAmount).hashCode());
        String $agentName = this.getAgentName();
        result = result * 59 + ($agentName == null ? 43 : $agentName.hashCode());
        String $bankName = this.getBankName();
        result = result * 59 + ($bankName == null ? 43 : $bankName.hashCode());
        String $bankCard = this.getBankCard();
        result = result * 59 + ($bankCard == null ? 43 : $bankCard.hashCode());
        return result;
    }

    public String toString() {
        return "AgentFinanceStatsVo(totalRevenue=" + this.getTotalRevenue() + ", successCount=" + this.getSuccessCount() + ", pendingCount=" + this.getPendingCount() + ", refundCount=" + this.getRefundCount() + ", withdrawableAmount=" + this.getWithdrawableAmount() + ", agentName=" + this.getAgentName() + ", bankName=" + this.getBankName() + ", bankCard=" + this.getBankCard() + ")";
    }
}

