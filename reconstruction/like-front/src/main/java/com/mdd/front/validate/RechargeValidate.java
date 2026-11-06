/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.RechargeValidate
 *  io.swagger.annotations.ApiModel
 *  javax.validation.constraints.NotNull
 */
package com.mdd.front.validate;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u65b0\u7528\u6237\u66f4\u65b0\u4fe1\u606f\u53c2\u6570")
public class RechargeValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="money\u53c2\u6570\u7f3a\u5931")
    private @NotNull(message="money\u53c2\u6570\u7f3a\u5931") BigDecimal money;

    @NotNull(message="money\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="money\u53c2\u6570\u7f3a\u5931") BigDecimal getMoney() {
        return this.money;
    }

    public void setMoney(@NotNull(message="money\u53c2\u6570\u7f3a\u5931") @NotNull(message="money\u53c2\u6570\u7f3a\u5931") BigDecimal money) {
        this.money = money;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RechargeValidate)) {
            return false;
        }
        RechargeValidate other = (RechargeValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        BigDecimal this$money = this.getMoney();
        BigDecimal other$money = other.getMoney();
        return !(this$money == null ? other$money != null : !((Object)this$money).equals(other$money));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RechargeValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        BigDecimal $money = this.getMoney();
        result = result * 59 + ($money == null ? 43 : ((Object)$money).hashCode());
        return result;
    }

    public String toString() {
        return "RechargeValidate(money=" + this.getMoney() + ")";
    }
}

