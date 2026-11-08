/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.annotation.TableName
 */
package com.mdd.common.entity.instructor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

@TableName(value="la_instructor_withdrawal")
public class InstructorWithdrawal
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    private Integer id;
    private Integer instructorId;
    private String sn;
    private BigDecimal amount;
    private String bankName;
    private String bankCard;
    private String cardHolder;
    private Integer status;
    private Integer auditId;
    private String auditName;
    private String auditRemark;
    private Long auditTime;
    private Long createTime;
    private Long updateTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getInstructorId() {
        return this.instructorId;
    }

    public String getSn() {
        return this.sn;
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

    public Long getAuditTime() {
        return this.auditTime;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public void setSn(String sn) {
        this.sn = sn;
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

    public void setAuditTime(Long auditTime) {
        this.auditTime = auditTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InstructorWithdrawal)) {
            return false;
        }
        InstructorWithdrawal other = (InstructorWithdrawal)o;
        if (!other.canEqual(this)) {
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
        Long this$auditTime = this.getAuditTime();
        Long other$auditTime = other.getAuditTime();
        if (this$auditTime == null ? other$auditTime != null : !((Object)this$auditTime).equals(other$auditTime)) {
            return false;
        }
        Long this$createTime = this.getCreateTime();
        Long other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Long this$updateTime = this.getUpdateTime();
        Long other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        String this$sn = this.getSn();
        String other$sn = other.getSn();
        if (this$sn == null ? other$sn != null : !this$sn.equals(other$sn)) {
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
        return !(this$auditRemark == null ? other$auditRemark != null : !this$auditRemark.equals(other$auditRemark));
    }

    protected boolean canEqual(Object other) {
        return other instanceof InstructorWithdrawal;
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
        Long $auditTime = this.getAuditTime();
        result = result * 59 + ($auditTime == null ? 43 : ((Object)$auditTime).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : $sn.hashCode());
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
        return result;
    }

    public String toString() {
        return "InstructorWithdrawal(id=" + this.getId() + ", instructorId=" + this.getInstructorId() + ", sn=" + this.getSn() + ", amount=" + this.getAmount() + ", bankName=" + this.getBankName() + ", bankCard=" + this.getBankCard() + ", cardHolder=" + this.getCardHolder() + ", status=" + this.getStatus() + ", auditRemark=" + this.getAuditRemark() + ", auditTime=" + this.getAuditTime() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

