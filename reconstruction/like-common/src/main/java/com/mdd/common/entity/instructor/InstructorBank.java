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

@TableName(value="la_instructor_bank")
public class InstructorBank
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    private Integer id;
    private Integer instructorId;
    private String bankName;
    private String bankBranch;
    private String cardNo;
    private String cardHolder;
    private Integer isDefault;
    private Long createTime;
    private Long updateTime;
    private Long deleteTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getInstructorId() {
        return this.instructorId;
    }

    public String getBankName() {
        return this.bankName;
    }

    public String getBankBranch() {
        return this.bankBranch;
    }

    public String getCardNo() {
        return this.cardNo;
    }

    public String getCardHolder() {
        return this.cardHolder;
    }

    public Integer getIsDefault() {
        return this.isDefault;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public Long getDeleteTime() {
        return this.deleteTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleteTime(Long deleteTime) {
        this.deleteTime = deleteTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InstructorBank)) {
            return false;
        }
        InstructorBank other = (InstructorBank)o;
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
        Integer this$isDefault = this.getIsDefault();
        Integer other$isDefault = other.getIsDefault();
        if (this$isDefault == null ? other$isDefault != null : !((Object)this$isDefault).equals(other$isDefault)) {
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
        Long this$deleteTime = this.getDeleteTime();
        Long other$deleteTime = other.getDeleteTime();
        if (this$deleteTime == null ? other$deleteTime != null : !((Object)this$deleteTime).equals(other$deleteTime)) {
            return false;
        }
        String this$bankName = this.getBankName();
        String other$bankName = other.getBankName();
        if (this$bankName == null ? other$bankName != null : !this$bankName.equals(other$bankName)) {
            return false;
        }
        String this$bankBranch = this.getBankBranch();
        String other$bankBranch = other.getBankBranch();
        if (this$bankBranch == null ? other$bankBranch != null : !this$bankBranch.equals(other$bankBranch)) {
            return false;
        }
        String this$cardNo = this.getCardNo();
        String other$cardNo = other.getCardNo();
        if (this$cardNo == null ? other$cardNo != null : !this$cardNo.equals(other$cardNo)) {
            return false;
        }
        String this$cardHolder = this.getCardHolder();
        String other$cardHolder = other.getCardHolder();
        return !(this$cardHolder == null ? other$cardHolder != null : !this$cardHolder.equals(other$cardHolder));
    }

    protected boolean canEqual(Object other) {
        return other instanceof InstructorBank;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $instructorId = this.getInstructorId();
        result = result * 59 + ($instructorId == null ? 43 : ((Object)$instructorId).hashCode());
        Integer $isDefault = this.getIsDefault();
        result = result * 59 + ($isDefault == null ? 43 : ((Object)$isDefault).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        String $bankName = this.getBankName();
        result = result * 59 + ($bankName == null ? 43 : $bankName.hashCode());
        String $bankBranch = this.getBankBranch();
        result = result * 59 + ($bankBranch == null ? 43 : $bankBranch.hashCode());
        String $cardNo = this.getCardNo();
        result = result * 59 + ($cardNo == null ? 43 : $cardNo.hashCode());
        String $cardHolder = this.getCardHolder();
        result = result * 59 + ($cardHolder == null ? 43 : $cardHolder.hashCode());
        return result;
    }

    public String toString() {
        return "InstructorBank(id=" + this.getId() + ", instructorId=" + this.getInstructorId() + ", bankName=" + this.getBankName() + ", bankBranch=" + this.getBankBranch() + ", cardNo=" + this.getCardNo() + ", cardHolder=" + this.getCardHolder() + ", isDefault=" + this.getIsDefault() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ")";
    }
}

