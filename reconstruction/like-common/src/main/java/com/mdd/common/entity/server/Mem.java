/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.server;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u5167\u5b58\u76f8\u5173\u4fe1\u606f\u5b9e\u4f53")
public class Mem
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u5185\u5b58\u603b\u91cf")
    private double total;
    @ApiModelProperty(value="\u5df2\u7528\u5185\u5b58")
    private double used;
    @ApiModelProperty(value="\u5269\u4f59\u5185\u5b58")
    private double free;
    @ApiModelProperty(value="\u4f7f\u7528\u7387")
    private double usage;

    public double getTotal() {
        return this.total;
    }

    public double getUsed() {
        return this.used;
    }

    public double getFree() {
        return this.free;
    }

    public double getUsage() {
        return this.usage;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setUsed(double used) {
        this.used = used;
    }

    public void setFree(double free) {
        this.free = free;
    }

    public void setUsage(double usage) {
        this.usage = usage;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Mem)) {
            return false;
        }
        Mem other = (Mem)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (Double.compare(this.getTotal(), other.getTotal()) != 0) {
            return false;
        }
        if (Double.compare(this.getUsed(), other.getUsed()) != 0) {
            return false;
        }
        if (Double.compare(this.getFree(), other.getFree()) != 0) {
            return false;
        }
        return Double.compare(this.getUsage(), other.getUsage()) == 0;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Mem;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $total = Double.doubleToLongBits(this.getTotal());
        result = result * 59 + (int)($total >>> 32 ^ $total);
        long $used = Double.doubleToLongBits(this.getUsed());
        result = result * 59 + (int)($used >>> 32 ^ $used);
        long $free = Double.doubleToLongBits(this.getFree());
        result = result * 59 + (int)($free >>> 32 ^ $free);
        long $usage = Double.doubleToLongBits(this.getUsage());
        result = result * 59 + (int)($usage >>> 32 ^ $usage);
        return result;
    }

    public String toString() {
        return "Mem(total=" + this.getTotal() + ", used=" + this.getUsed() + ", free=" + this.getFree() + ", usage=" + this.getUsage() + ")";
    }
}

