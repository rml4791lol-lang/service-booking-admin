/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.agent.AgentWithdrawalApplyValidate
 *  javax.validation.constraints.DecimalMin
 *  javax.validation.constraints.NotNull
 */
package com.mdd.admin.validate.agent;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class AgentWithdrawalApplyValidate
implements Serializable {
    @NotNull(message="\u63d0\u73b0\u91d1\u989d\u4e0d\u80fd\u4e3a\u7a7a")
    @DecimalMin(value="0.01", message="\u63d0\u73b0\u91d1\u989d\u6700\u5c0f\u4e3a0.01")
    private @NotNull(message="\u63d0\u73b0\u91d1\u989d\u4e0d\u80fd\u4e3a\u7a7a") @DecimalMin(value="0.01", message="\u63d0\u73b0\u91d1\u989d\u6700\u5c0f\u4e3a0.01") BigDecimal amount;

    @NotNull(message="\u63d0\u73b0\u91d1\u989d\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u63d0\u73b0\u91d1\u989d\u4e0d\u80fd\u4e3a\u7a7a") BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(@NotNull(message="\u63d0\u73b0\u91d1\u989d\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u63d0\u73b0\u91d1\u989d\u4e0d\u80fd\u4e3a\u7a7a") BigDecimal amount) {
        this.amount = amount;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AgentWithdrawalApplyValidate)) {
            return false;
        }
        AgentWithdrawalApplyValidate other = (AgentWithdrawalApplyValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        BigDecimal this$amount = this.getAmount();
        BigDecimal other$amount = other.getAmount();
        return !(this$amount == null ? other$amount != null : !((Object)this$amount).equals(other$amount));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AgentWithdrawalApplyValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        BigDecimal $amount = this.getAmount();
        result = result * 59 + ($amount == null ? 43 : ((Object)$amount).hashCode());
        return result;
    }

    public String toString() {
        return "AgentWithdrawalApplyValidate(amount=" + this.getAmount() + ")";
    }
}

