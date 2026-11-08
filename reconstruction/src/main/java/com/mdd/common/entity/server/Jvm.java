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

@ApiModel(value="JVM\u76f8\u5173\u4fe1\u606f\u5b9e\u4f53")
public class Jvm
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u5f53\u524dJVM\u5360\u7528\u7684\u5185\u5b58\u603b\u6570(M)")
    private double total;
    @ApiModelProperty(value="JVM\u6700\u5927\u53ef\u7528\u5185\u5b58\u603b\u6570(M)")
    private double max;
    @ApiModelProperty(value="JVM\u7a7a\u95f2\u5185\u5b58(M)")
    private double free;
    @ApiModelProperty(value="JVM\u5185\u5b58\u4f7f\u7528\u7387")
    private double usage;
    @ApiModelProperty(value="JDK\u7248\u672c")
    private String version;
    @ApiModelProperty(value="JDK\u8def\u5f84")
    private String home;
    @ApiModelProperty(value="JDK\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u8fd0\u884c\u53c2\u6570")
    private String inputArgs;
    @ApiModelProperty(value="JDK\u8fd0\u884c\u65f6\u95f4")
    private String runTime;
    @ApiModelProperty(value="JDK\u542f\u52a8\u65f6\u95f4")
    private String startTime;

    public double getTotal() {
        return this.total;
    }

    public double getMax() {
        return this.max;
    }

    public double getFree() {
        return this.free;
    }

    public double getUsage() {
        return this.usage;
    }

    public String getVersion() {
        return this.version;
    }

    public String getHome() {
        return this.home;
    }

    public String getName() {
        return this.name;
    }

    public String getInputArgs() {
        return this.inputArgs;
    }

    public String getRunTime() {
        return this.runTime;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setFree(double free) {
        this.free = free;
    }

    public void setUsage(double usage) {
        this.usage = usage;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInputArgs(String inputArgs) {
        this.inputArgs = inputArgs;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Jvm)) {
            return false;
        }
        Jvm other = (Jvm)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (Double.compare(this.getTotal(), other.getTotal()) != 0) {
            return false;
        }
        if (Double.compare(this.getMax(), other.getMax()) != 0) {
            return false;
        }
        if (Double.compare(this.getFree(), other.getFree()) != 0) {
            return false;
        }
        if (Double.compare(this.getUsage(), other.getUsage()) != 0) {
            return false;
        }
        String this$version = this.getVersion();
        String other$version = other.getVersion();
        if (this$version == null ? other$version != null : !this$version.equals(other$version)) {
            return false;
        }
        String this$home = this.getHome();
        String other$home = other.getHome();
        if (this$home == null ? other$home != null : !this$home.equals(other$home)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$inputArgs = this.getInputArgs();
        String other$inputArgs = other.getInputArgs();
        if (this$inputArgs == null ? other$inputArgs != null : !this$inputArgs.equals(other$inputArgs)) {
            return false;
        }
        String this$runTime = this.getRunTime();
        String other$runTime = other.getRunTime();
        if (this$runTime == null ? other$runTime != null : !this$runTime.equals(other$runTime)) {
            return false;
        }
        String this$startTime = this.getStartTime();
        String other$startTime = other.getStartTime();
        return !(this$startTime == null ? other$startTime != null : !this$startTime.equals(other$startTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Jvm;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $total = Double.doubleToLongBits(this.getTotal());
        result = result * 59 + (int)($total >>> 32 ^ $total);
        long $max = Double.doubleToLongBits(this.getMax());
        result = result * 59 + (int)($max >>> 32 ^ $max);
        long $free = Double.doubleToLongBits(this.getFree());
        result = result * 59 + (int)($free >>> 32 ^ $free);
        long $usage = Double.doubleToLongBits(this.getUsage());
        result = result * 59 + (int)($usage >>> 32 ^ $usage);
        String $version = this.getVersion();
        result = result * 59 + ($version == null ? 43 : $version.hashCode());
        String $home = this.getHome();
        result = result * 59 + ($home == null ? 43 : $home.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $inputArgs = this.getInputArgs();
        result = result * 59 + ($inputArgs == null ? 43 : $inputArgs.hashCode());
        String $runTime = this.getRunTime();
        result = result * 59 + ($runTime == null ? 43 : $runTime.hashCode());
        String $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : $startTime.hashCode());
        return result;
    }

    public String toString() {
        return "Jvm(total=" + this.getTotal() + ", max=" + this.getMax() + ", free=" + this.getFree() + ", usage=" + this.getUsage() + ", version=" + this.getVersion() + ", home=" + this.getHome() + ", name=" + this.getName() + ", inputArgs=" + this.getInputArgs() + ", runTime=" + this.getRunTime() + ", startTime=" + this.getStartTime() + ")";
    }
}

