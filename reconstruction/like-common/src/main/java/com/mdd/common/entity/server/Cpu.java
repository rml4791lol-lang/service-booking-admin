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

@ApiModel(value="CPU\u76f8\u5173\u4fe1\u606f\u5b9e\u4f53")
public class Cpu
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u6838\u5fc3\u6570")
    private int cpuNum;
    @ApiModelProperty(value="CPU\u603b\u7684\u4f7f\u7528\u7387")
    private double total;
    @ApiModelProperty(value="CPU\u7cfb\u7edf\u4f7f\u7528\u7387")
    private double sys;
    @ApiModelProperty(value="CPU\u7528\u6237\u4f7f\u7528\u7387")
    private double used;
    @ApiModelProperty(value="CPU\u5f53\u524d\u7b49\u5f85\u7387")
    private double wait;
    @ApiModelProperty(value="CPU\u5f53\u524d\u7a7a\u95f2\u7387")
    private double free;

    public int getCpuNum() {
        return this.cpuNum;
    }

    public double getTotal() {
        return this.total;
    }

    public double getSys() {
        return this.sys;
    }

    public double getUsed() {
        return this.used;
    }

    public double getWait() {
        return this.wait;
    }

    public double getFree() {
        return this.free;
    }

    public void setCpuNum(int cpuNum) {
        this.cpuNum = cpuNum;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setSys(double sys) {
        this.sys = sys;
    }

    public void setUsed(double used) {
        this.used = used;
    }

    public void setWait(double wait) {
        this.wait = wait;
    }

    public void setFree(double free) {
        this.free = free;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Cpu)) {
            return false;
        }
        Cpu other = (Cpu)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getCpuNum() != other.getCpuNum()) {
            return false;
        }
        if (Double.compare(this.getTotal(), other.getTotal()) != 0) {
            return false;
        }
        if (Double.compare(this.getSys(), other.getSys()) != 0) {
            return false;
        }
        if (Double.compare(this.getUsed(), other.getUsed()) != 0) {
            return false;
        }
        if (Double.compare(this.getWait(), other.getWait()) != 0) {
            return false;
        }
        return Double.compare(this.getFree(), other.getFree()) == 0;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Cpu;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getCpuNum();
        long $total = Double.doubleToLongBits(this.getTotal());
        result = result * 59 + (int)($total >>> 32 ^ $total);
        long $sys = Double.doubleToLongBits(this.getSys());
        result = result * 59 + (int)($sys >>> 32 ^ $sys);
        long $used = Double.doubleToLongBits(this.getUsed());
        result = result * 59 + (int)($used >>> 32 ^ $used);
        long $wait = Double.doubleToLongBits(this.getWait());
        result = result * 59 + (int)($wait >>> 32 ^ $wait);
        long $free = Double.doubleToLongBits(this.getFree());
        result = result * 59 + (int)($free >>> 32 ^ $free);
        return result;
    }

    public String toString() {
        return "Cpu(cpuNum=" + this.getCpuNum() + ", total=" + this.getTotal() + ", sys=" + this.getSys() + ", used=" + this.getUsed() + ", wait=" + this.getWait() + ", free=" + this.getFree() + ")";
    }
}

