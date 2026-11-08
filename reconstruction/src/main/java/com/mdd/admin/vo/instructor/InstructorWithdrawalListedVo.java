/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.instructor.InstructorWithdrawalListedVo
 */
package com.mdd.admin.vo.instructor;

import java.io.Serializable;
import java.math.BigDecimal;

public class InstructorWithdrawalListedVo
implements Serializable {
    private Integer id;
    private String sn;
    private Integer instructorId;
    private String instructorName;
    private String instructorAvatar;
    private String instructorPhone;
    private BigDecimal amount;
    private String bankName;
    private String bankCard;
    private String cardHolder;
    private Integer status;
    private Integer auditId;
    private String auditName;
    private String auditRemark;
    private String auditTime;
    private String createTime;

    public Integer getId() {
        return this.id;
    }

    public String getSn() {
        return this.sn;
    }

    public Integer getInstructorId() {
        return this.instructorId;
    }

    public String getInstructorName() {
        return this.instructorName;
    }

    public String getInstructorAvatar() {
        return this.instructorAvatar;
    }

    public String getInstructorPhone() {
        return this.instructorPhone;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public String getBankName() {
        return this.bankName;
    }

    public String getBankCard() {
        return this.bankCard;
    }

    public String getCardHolder() {
        return this.cardHolder;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Integer getAuditId() {
        return this.auditId;
    }

    public String getAuditName() {
        return this.auditName;
    }

    public String getAuditRemark() {
        return this.auditRemark;
    }

    public String getAuditTime() {
        return this.auditTime;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public void setInstructorAvatar(String instructorAvatar) {
        this.instructorAvatar = instructorAvatar;
    }

    public void setInstructorPhone(String instructorPhone) {
        this.instructorPhone = instructorPhone;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InstructorWithdrawalListedVo)) {
            return false;
        }
        InstructorWithdrawalListedVo other = (InstructorWithdrawalListedVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$instructorId = this.getInstructorId();
        Integer other$instructorId = other.getInstructorId();
        if (this$instructorId == null ? other$instructorId != null : !((Object)this$instructorId).equals(other$instructorId)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$sn = this.getSn();
        String other$sn = other.getSn();
        if (this$sn == null ? other$sn != null : !this$sn.equals(other$sn)) {
            return false;
        }
        String this$instructorName = this.getInstructorName();
        String other$instructorName = other.getInstructorName();
        if (this$instructorName == null ? other$instructorName != null : !this$instructorName.equals(other$instructorName)) {
            return false;
        }
        String this$instructorAvatar = this.getInstructorAvatar();
        String other$instructorAvatar = other.getInstructorAvatar();
        if (this$instructorAvatar == null ? other$instructorAvatar != null : !this$instructorAvatar.equals(other$instructorAvatar)) {
            return false;
        }
        String this$instructorPhone = this.getInstructorPhone();
        String other$instructorPhone = other.getInstructorPhone();
        if (this$instructorPhone == null ? other$instructorPhone != null : !this$instructorPhone.equals(other$instructorPhone)) {
            return false;
        }
        BigDecimal this$amount = this.getAmount();
        BigDecimal other$amount = other.getAmount();
        if (this$amount == null ? other$amount != null : !((Object)this$amount).equals(other$amount)) {
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
        String this$cardHolder = this.getCardHolder();
        String other$cardHolder = other.getCardHolder();
        if (this$cardHolder == null ? other$cardHolder != null : !this$cardHolder.equals(other$cardHolder)) {
            return false;
        }
        String this$auditRemark = this.getAuditRemark();
        String other$auditRemark = other.getAuditRemark();
        if (this$auditRemark == null ? other$auditRemark != null : !this$auditRemark.equals(other$auditRemark)) {
            return false;
        }
        String this$auditTime = this.getAuditTime();
        String other$auditTime = other.getAuditTime();
        if (this$auditTime == null ? other$auditTime != null : !this$auditTime.equals(other$auditTime)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof InstructorWithdrawalListedVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $instructorId = this.getInstructorId();
        result = result * 59 + ($instructorId == null ? 43 : ((Object)$instructorId).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : $sn.hashCode());
        String $instructorName = this.getInstructorName();
        result = result * 59 + ($instructorName == null ? 43 : $instructorName.hashCode());
        String $instructorAvatar = this.getInstructorAvatar();
        result = result * 59 + ($instructorAvatar == null ? 43 : $instructorAvatar.hashCode());
        String $instructorPhone = this.getInstructorPhone();
        result = result * 59 + ($instructorPhone == null ? 43 : $instructorPhone.hashCode());
        BigDecimal $amount = this.getAmount();
        result = result * 59 + ($amount == null ? 43 : ((Object)$amount).hashCode());
        String $bankName = this.getBankName();
        result = result * 59 + ($bankName == null ? 43 : $bankName.hashCode());
        String $bankCard = this.getBankCard();
        result = result * 59 + ($bankCard == null ? 43 : $bankCard.hashCode());
        String $cardHolder = this.getCardHolder();
        result = result * 59 + ($cardHolder == null ? 43 : $cardHolder.hashCode());
        String $auditRemark = this.getAuditRemark();
        result = result * 59 + ($auditRemark == null ? 43 : $auditRemark.hashCode());
        String $auditTime = this.getAuditTime();
        result = result * 59 + ($auditTime == null ? 43 : $auditTime.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        return result;
    }

    public String toString() {
        return "InstructorWithdrawalListedVo(id=" + this.getId() + ", sn=" + this.getSn() + ", instructorId=" + this.getInstructorId() + ", instructorName=" + this.getInstructorName() + ", instructorAvatar=" + this.getInstructorAvatar() + ", instructorPhone=" + this.getInstructorPhone() + ", amount=" + this.getAmount() + ", bankName=" + this.getBankName() + ", bankCard=" + this.getBankCard() + ", cardHolder=" + this.getCardHolder() + ", status=" + this.getStatus() + ", auditRemark=" + this.getAuditRemark() + ", auditTime=" + this.getAuditTime() + ", createTime=" + this.getCreateTime() + ")";
    }
}

