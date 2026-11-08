/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.marketing.MarketingRechargeVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.marketing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u8425\u9500\u5145\u503cVo")
public class MarketingRechargeVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u662f\u5426\u5f00\u542f\u5145\u503c: 0=\u5426,1=\u662f")
    private Integer status;
    @ApiModelProperty(value="\u6700\u4f4e\u5145\u503c\u91d1\u989d")
    private BigDecimal minAmount;

    public Integer getStatus() {
        return this.status;
    }

    public BigDecimal getMinAmount() {
        return this.minAmount;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MarketingRechargeVo)) {
            return false;
        }
        MarketingRechargeVo other = (MarketingRechargeVo)o;
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
        return !(this$minAmount == null ? other$minAmount != null : !((Object)this$minAmount).equals(other$minAmount));
    }

    protected boolean canEqual(Object other) {
        return other instanceof MarketingRechargeVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        BigDecimal $minAmount = this.getMinAmount();
        result = result * 59 + ($minAmount == null ? 43 : ((Object)$minAmount).hashCode());
        return result;
    }

    public String toString() {
        return "MarketingRechargeVo(status=" + this.getStatus() + ", minAmount=" + this.getMinAmount() + ")";
    }
}

