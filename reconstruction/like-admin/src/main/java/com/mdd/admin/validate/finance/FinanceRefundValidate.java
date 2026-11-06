/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.finance.FinanceRefundValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.finance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u9000\u6b3e\u8bb0\u5f55\u641c\u7d22\u53c2\u6570")
public class FinanceRefundValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u5145\u503c\u8ba2\u5355id")
    private Integer recharge_id;

    public Integer getRecharge_id() {
        return this.recharge_id;
    }

    public void setRecharge_id(Integer recharge_id) {
        this.recharge_id = recharge_id;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FinanceRefundValidate)) {
            return false;
        }
        FinanceRefundValidate other = (FinanceRefundValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$recharge_id = this.getRecharge_id();
        Integer other$recharge_id = other.getRecharge_id();
        return !(this$recharge_id == null ? other$recharge_id != null : !((Object)this$recharge_id).equals(other$recharge_id));
    }

    protected boolean canEqual(Object other) {
        return other instanceof FinanceRefundValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $recharge_id = this.getRecharge_id();
        result = result * 59 + ($recharge_id == null ? 43 : ((Object)$recharge_id).hashCode());
        return result;
    }

    public String toString() {
        return "FinanceRefundValidate(recharge_id=" + this.getRecharge_id() + ")";
    }
}

