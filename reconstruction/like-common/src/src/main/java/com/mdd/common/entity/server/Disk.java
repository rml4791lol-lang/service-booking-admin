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

@ApiModel(value="\u7cfb\u7edf\u6587\u4ef6\u4fe1\u606f\u5b9e\u4f53")
public class Disk
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u76d8\u7b26\u8def\u5f84")
    private String dirName;
    @ApiModelProperty(value="\u76d8\u7b26\u7c7b\u578b")
    private String sysTypeName;
    @ApiModelProperty(value="\u6587\u4ef6\u7c7b\u578b")
    private String typeName;
    @ApiModelProperty(value="\u603b\u5927\u5c0f")
    private String total;
    @ApiModelProperty(value="\u5269\u4f59\u5927\u5c0f")
    private String free;
    @ApiModelProperty(value="\u5df2\u7ecf\u4f7f\u7528\u91cf")
    private String used;
    @ApiModelProperty(value="\u8d44\u6e90\u7684\u4f7f\u7528\u7387")
    private double usage;

    public String getDirName() {
        return this.dirName;
    }

    public String getSysTypeName() {
        return this.sysTypeName;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public String getTotal() {
        return this.total;
    }

    public String getFree() {
        return this.free;
    }

    public String getUsed() {
        return this.used;
    }

    public double getUsage() {
        return this.usage;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public void setSysTypeName(String sysTypeName) {
        this.sysTypeName = sysTypeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public void setUsage(double usage) {
        this.usage = usage;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Disk)) {
            return false;
        }
        Disk other = (Disk)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (Double.compare(this.getUsage(), other.getUsage()) != 0) {
            return false;
        }
        String this$dirName = this.getDirName();
        String other$dirName = other.getDirName();
        if (this$dirName == null ? other$dirName != null : !this$dirName.equals(other$dirName)) {
            return false;
        }
        String this$sysTypeName = this.getSysTypeName();
        String other$sysTypeName = other.getSysTypeName();
        if (this$sysTypeName == null ? other$sysTypeName != null : !this$sysTypeName.equals(other$sysTypeName)) {
            return false;
        }
        String this$typeName = this.getTypeName();
        String other$typeName = other.getTypeName();
        if (this$typeName == null ? other$typeName != null : !this$typeName.equals(other$typeName)) {
            return false;
        }
        String this$total = this.getTotal();
        String other$total = other.getTotal();
        if (this$total == null ? other$total != null : !this$total.equals(other$total)) {
            return false;
        }
        String this$free = this.getFree();
        String other$free = other.getFree();
        if (this$free == null ? other$free != null : !this$free.equals(other$free)) {
            return false;
        }
        String this$used = this.getUsed();
        String other$used = other.getUsed();
        return !(this$used == null ? other$used != null : !this$used.equals(other$used));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Disk;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $usage = Double.doubleToLongBits(this.getUsage());
        result = result * 59 + (int)($usage >>> 32 ^ $usage);
        String $dirName = this.getDirName();
        result = result * 59 + ($dirName == null ? 43 : $dirName.hashCode());
        String $sysTypeName = this.getSysTypeName();
        result = result * 59 + ($sysTypeName == null ? 43 : $sysTypeName.hashCode());
        String $typeName = this.getTypeName();
        result = result * 59 + ($typeName == null ? 43 : $typeName.hashCode());
        String $total = this.getTotal();
        result = result * 59 + ($total == null ? 43 : $total.hashCode());
        String $free = this.getFree();
        result = result * 59 + ($free == null ? 43 : $free.hashCode());
        String $used = this.getUsed();
        result = result * 59 + ($used == null ? 43 : $used.hashCode());
        return result;
    }

    public String toString() {
        return "Disk(dirName=" + this.getDirName() + ", sysTypeName=" + this.getSysTypeName() + ", typeName=" + this.getTypeName() + ", total=" + this.getTotal() + ", free=" + this.getFree() + ", used=" + this.getUsed() + ", usage=" + this.getUsage() + ")";
    }
}

