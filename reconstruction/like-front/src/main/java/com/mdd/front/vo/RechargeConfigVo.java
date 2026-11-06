/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.vo.RechargeConfigVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u5145\u503c\u914d\u7f6eVo")
public class RechargeConfigVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u662f\u5426\u5f00\u542f\u5145\u503c: 0=\u5426,1=\u662f")
    private Integer status;
    @ApiModelProperty(value="\u6700\u4f4e\u5145\u503c\u91d1\u989d")
    private BigDecimal minAmount;
    @ApiModelProperty(value="\u7528\u6237\u94b1\u5305")
    private BigDecimal userMoney;

    public Integer getStatus() {
        return this.status;
    }

    public BigDecimal getMinAmount() {
        return this.minAmount;
    }

    public BigDecimal getUserMoney() {
        return this.userMoney;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public void setUserMoney(BigDecimal userMoney) {
        this.userMoney = userMoney;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RechargeConfigVo)) {
            return false;
        }
        RechargeConfigVo other = (RechargeConfigVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        BigDecimal this$minAmount = this.getMinAmount();
        BigDecimal other$minAmount = other.getMinAmount();
        if (this$minAmount == null ? other$minAmount != null : !((Object)this$minAmount).equals(other$minAmount)) {
            return false;
        }
        BigDecimal this$userMoney = this.getUserMoney();
        BigDecimal other$userMoney = other.getUserMoney();
        return !(this$userMoney == null ? other$userMoney != null : !((Object)this$userMoney).equals(other$userMoney));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RechargeConfigVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        BigDecimal $minAmount = this.getMinAmount();
        result = result * 59 + ($minAmount == null ? 43 : ((Object)$minAmount).hashCode());
        BigDecimal $userMoney = this.getUserMoney();
        result = result * 59 + ($userMoney == null ? 43 : ((Object)$userMoney).hashCode());
        return result;
    }

    public String toString() {
        return "RechargeConfigVo(status=" + this.getStatus() + ", minAmount=" + this.getMinAmount() + ", userMoney=" + this.getUserMoney() + ")";
    }
}

