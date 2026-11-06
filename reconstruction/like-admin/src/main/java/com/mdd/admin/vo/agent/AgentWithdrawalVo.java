/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.agent.AgentWithdrawalVo
 */
package com.mdd.admin.vo.agent;

import java.io.Serializable;
import java.math.BigDecimal;

public class AgentWithdrawalVo
implements Serializable {
    private Integer id;
    private String agentName;
    private BigDecimal withdrawAmount;
    private String bankName;
    private String bankCard;
    private Integer status;
    private String auditRemark;
    private String submitTime;
    private String auditTime;

    public Integer getId() {
        return this.id;
    }

    public String getAgentName() {
        return this.agentName;
    }

    public BigDecimal getWithdrawAmount() {
        return this.withdrawAmount;
    }

    public String getBankName() {
        return this.bankName;
    }

    public String getBankCard() {
        return this.bankCard;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getAuditRemark() {
        return this.auditRemark;
    }

    public String getSubmitTime() {
        return this.submitTime;
    }

    public String getAuditTime() {
        return this.auditTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AgentWithdrawalVo)) {
            return false;
        }
        AgentWithdrawalVo other = (AgentWithdrawalVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$agentName = this.getAgentName();
        String other$agentName = other.getAgentName();
        if (this$agentName == null ? other$agentName != null : !this$agentName.equals(other$agentName)) {
            return false;
        }
        BigDecimal this$withdrawAmount = this.getWithdrawAmount();
        BigDecimal other$withdrawAmount = other.getWithdrawAmount();
        if (this$withdrawAmount == null ? other$withdrawAmount != null : !((Object)this$withdrawAmount).equals(other$withdrawAmount)) {
            return false;
        }
        String this$bankName = this.getBankName();
        String other$bankName = other.getBankName();
        if (this$bankName == null ? other$bankName != null : !this$bankName.equals(other$bankName)) {
            return false;
        }
        String this$bankCard = this.getBankCard();
        String other$bankCard = other.getBankCard();
        if (this$bankCard == null ? other$bankCard != null : !this$bankCard.equals(other$bankCard)) {
            return false;
        }
        String this$auditRemark = this.getAuditRemark();
        String other$auditRemark = other.getAuditRemark();
        if (this$auditRemark == null ? other$auditRemark != null : !this$auditRemark.equals(other$auditRemark)) {
            return false;
        }
        String this$submitTime = this.getSubmitTime();
        String other$submitTime = other.getSubmitTime();
        if (this$submitTime == null ? other$submitTime != null : !this$submitTime.equals(other$submitTime)) {
            return false;
        }
        String this$auditTime = this.getAuditTime();
        String other$auditTime = other.getAuditTime();
        return !(this$auditTime == null ? other$auditTime != null : !this$auditTime.equals(other$auditTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AgentWithdrawalVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $agentName = this.getAgentName();
        result = result * 59 + ($agentName == null ? 43 : $agentName.hashCode());
        BigDecimal $withdrawAmount = this.getWithdrawAmount();
        result = result * 59 + ($withdrawAmount == null ? 43 : ((Object)$withdrawAmount).hashCode());
        String $bankName = this.getBankName();
        result = result * 59 + ($bankName == null ? 43 : $bankName.hashCode());
        String $bankCard = this.getBankCard();
        result = result * 59 + ($bankCard == null ? 43 : $bankCard.hashCode());
        String $auditRemark = this.getAuditRemark();
        result = result * 59 + ($auditRemark == null ? 43 : $auditRemark.hashCode());
        String $submitTime = this.getSubmitTime();
        result = result * 59 + ($submitTime == null ? 43 : $submitTime.hashCode());
        String $auditTime = this.getAuditTime();
        result = result * 59 + ($auditTime == null ? 43 : $auditTime.hashCode());
        return result;
    }

    public String toString() {
        return "AgentWithdrawalVo(id=" + this.getId() + ", agentName=" + this.getAgentName() + ", withdrawAmount=" + this.getWithdrawAmount() + ", bankName=" + this.getBankName() + ", bankCard=" + this.getBankCard() + ", status=" + this.getStatus() + ", auditRemark=" + this.getAuditRemark() + ", submitTime=" + this.getSubmitTime() + ", auditTime=" + this.getAuditTime() + ")";
    }
}

