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

@TableName(value="la_instructor_account_log")
public class InstructorAccountLog
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    private Integer id;
    private Integer instructorId;
    private Integer type;
    private BigDecimal amount;
    private BigDecimal beforeBalance;
    private BigDecimal afterBalance;
    private Integer relationId;
    private String settlementKey;
    private String remark;
    private Long createTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getInstructorId() {
        return this.instructorId;
    }

    public Integer getType() {
        return this.type;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public BigDecimal getBeforeBalance() {
        return this.beforeBalance;
    }

    public BigDecimal getAfterBalance() {
        return this.afterBalance;
    }

    public Integer getRelationId() {
        return this.relationId;
    }

    public String getSettlementKey() {
        return this.settlementKey;
    }

    public String getRemark() {
        return this.remark;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setBeforeBalance(BigDecimal beforeBalance) {
        this.beforeBalance = beforeBalance;
    }

    public void setAfterBalance(BigDecimal afterBalance) {
        this.afterBalance = afterBalance;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public void setSettlementKey(String settlementKey) {
        this.settlementKey = settlementKey;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InstructorAccountLog)) {
            return false;
        }
        InstructorAccountLog other = (InstructorAccountLog)o;
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
        Integer this$type = this.getType();
        Integer other$type = other.getType();
        if (this$type == null ? other$type != null : !((Object)this$type).equals(other$type)) {
            return false;
        }
        Integer this$relationId = this.getRelationId();
        Integer other$relationId = other.getRelationId();
        if (this$relationId == null ? other$relationId != null : !((Object)this$relationId).equals(other$relationId)) {
            return false;
        }
        Long this$createTime = this.getCreateTime();
        Long other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        BigDecimal this$amount = this.getAmount();
        BigDecimal other$amount = other.getAmount();
        if (this$amount == null ? other$amount != null : !((Object)this$amount).equals(other$amount)) {
            return false;
        }
        BigDecimal this$beforeBalance = this.getBeforeBalance();
        BigDecimal other$beforeBalance = other.getBeforeBalance();
        if (this$beforeBalance == null ? other$beforeBalance != null : !((Object)this$beforeBalance).equals(other$beforeBalance)) {
            return false;
        }
        BigDecimal this$afterBalance = this.getAfterBalance();
        BigDecimal other$afterBalance = other.getAfterBalance();
        if (this$afterBalance == null ? other$afterBalance != null : !((Object)this$afterBalance).equals(other$afterBalance)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        return !(this$remark == null ? other$remark != null : !this$remark.equals(other$remark));
    }

    protected boolean canEqual(Object other) {
        return other instanceof InstructorAccountLog;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $instructorId = this.getInstructorId();
        result = result * 59 + ($instructorId == null ? 43 : ((Object)$instructorId).hashCode());
        Integer $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)$type).hashCode());
        Integer $relationId = this.getRelationId();
        result = result * 59 + ($relationId == null ? 43 : ((Object)$relationId).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        BigDecimal $amount = this.getAmount();
        result = result * 59 + ($amount == null ? 43 : ((Object)$amount).hashCode());
        BigDecimal $beforeBalance = this.getBeforeBalance();
        result = result * 59 + ($beforeBalance == null ? 43 : ((Object)$beforeBalance).hashCode());
        BigDecimal $afterBalance = this.getAfterBalance();
        result = result * 59 + ($afterBalance == null ? 43 : ((Object)$afterBalance).hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        return result;
    }

    public String toString() {
        return "InstructorAccountLog(id=" + this.getId() + ", instructorId=" + this.getInstructorId() + ", type=" + this.getType() + ", amount=" + this.getAmount() + ", beforeBalance=" + this.getBeforeBalance() + ", afterBalance=" + this.getAfterBalance() + ", relationId=" + this.getRelationId() + ", remark=" + this.getRemark() + ", createTime=" + this.getCreateTime() + ")";
    }
}
