/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.marketing.MarketingRechargeValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.admin.validate.marketing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u8ba2\u5355\u641c\u7d22\u53c2\u6570")
public class MarketingRechargeValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="status\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u662f\u5426\u5f00\u542f\u5145\u503c: 0=\u5426,1=\u662f")
    private @NotNull(message="status\u53c2\u6570\u7f3a\u5931") Integer status;
    @NotNull(message="minRechargeMoney\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u6700\u4f4e\u5145\u503c\u91d1\u989d")
    private @NotNull(message="minRechargeMoney\u53c2\u6570\u7f3a\u5931") BigDecimal minAmount;

    @NotNull(message="status\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="status\u53c2\u6570\u7f3a\u5931") Integer getStatus() {
        return this.status;
    }

    @NotNull(message="minRechargeMoney\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="minRechargeMoney\u53c2\u6570\u7f3a\u5931") BigDecimal getMinAmount() {
        return this.minAmount;
    }

    public void setStatus(@NotNull(message="status\u53c2\u6570\u7f3a\u5931") @NotNull(message="status\u53c2\u6570\u7f3a\u5931") Integer status) {
        this.status = status;
    }

    public void setMinAmount(@NotNull(message="minRechargeMoney\u53c2\u6570\u7f3a\u5931") @NotNull(message="minRechargeMoney\u53c2\u6570\u7f3a\u5931") BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MarketingRechargeValidate)) {
            return false;
        }
        MarketingRechargeValidate other = (MarketingRechargeValidate)o;
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
        return other instanceof MarketingRechargeValidate;
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
        return "MarketingRechargeValidate(status=" + this.getStatus() + ", minAmount=" + this.getMinAmount() + ")";
    }
}

