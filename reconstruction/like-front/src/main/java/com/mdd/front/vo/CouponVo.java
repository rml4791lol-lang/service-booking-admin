/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.vo.CouponVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u4f18\u60e0\u5238Vo")
public class CouponVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u5238\u7f16\u53f7")
    private String sn;
    @ApiModelProperty(value="\u5238\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u62b5\u6263\u91d1\u989d")
    private BigDecimal amount;
    @ApiModelProperty(value="\u6700\u4f4e\u6d88\u8d39\u91d1\u989d")
    private BigDecimal minAmount;
    @ApiModelProperty(value="\u72b6\u6001:0=\u672a\u4f7f\u7528,1=\u5df2\u4f7f\u7528,2=\u5df2\u8fc7\u671f")
    private Integer status;
    @ApiModelProperty(value="\u72b6\u6001\u63cf\u8ff0")
    private String statusDesc;
    @ApiModelProperty(value="\u751f\u6548\u65f6\u95f4")
    private String startTime;
    @ApiModelProperty(value="\u8fc7\u671f\u65f6\u95f4")
    private String expireTime;

    public Integer getId() {
        return this.id;
    }

    public String getSn() {
        return this.sn;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public BigDecimal getMinAmount() {
        return this.minAmount;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getStatusDesc() {
        return this.statusDesc;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getExpireTime() {
        return this.expireTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CouponVo)) {
            return false;
        }
        CouponVo other = (CouponVo)o;
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
        String this$sn = this.getSn();
        String other$sn = other.getSn();
        if (this$sn == null ? other$sn != null : !this$sn.equals(other$sn)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        BigDecimal this$amount = this.getAmount();
        BigDecimal other$amount = other.getAmount();
        if (this$amount == null ? other$amount != null : !((Object)this$amount).equals(other$amount)) {
            return false;
        }
        BigDecimal this$minAmount = this.getMinAmount();
        BigDecimal other$minAmount = other.getMinAmount();
        if (this$minAmount == null ? other$minAmount != null : !((Object)this$minAmount).equals(other$minAmount)) {
            return false;
        }
        String this$statusDesc = this.getStatusDesc();
        String other$statusDesc = other.getStatusDesc();
        if (this$statusDesc == null ? other$statusDesc != null : !this$statusDesc.equals(other$statusDesc)) {
            return false;
        }
        String this$startTime = this.getStartTime();
        String other$startTime = other.getStartTime();
        if (this$startTime == null ? other$startTime != null : !this$startTime.equals(other$startTime)) {
            return false;
        }
        String this$expireTime = this.getExpireTime();
        String other$expireTime = other.getExpireTime();
        return !(this$expireTime == null ? other$expireTime != null : !this$expireTime.equals(other$expireTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CouponVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : $sn.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        BigDecimal $amount = this.getAmount();
        result = result * 59 + ($amount == null ? 43 : ((Object)$amount).hashCode());
        BigDecimal $minAmount = this.getMinAmount();
        result = result * 59 + ($minAmount == null ? 43 : ((Object)$minAmount).hashCode());
        String $statusDesc = this.getStatusDesc();
        result = result * 59 + ($statusDesc == null ? 43 : $statusDesc.hashCode());
        String $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : $startTime.hashCode());
        String $expireTime = this.getExpireTime();
        result = result * 59 + ($expireTime == null ? 43 : $expireTime.hashCode());
        return result;
    }

    public String toString() {
        return "CouponVo(id=" + this.getId() + ", sn=" + this.getSn() + ", name=" + this.getName() + ", amount=" + this.getAmount() + ", minAmount=" + this.getMinAmount() + ", status=" + this.getStatus() + ", statusDesc=" + this.getStatusDesc() + ", startTime=" + this.getStartTime() + ", expireTime=" + this.getExpireTime() + ")";
    }
}

