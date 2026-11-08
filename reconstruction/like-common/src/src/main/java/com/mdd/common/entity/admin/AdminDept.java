/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u90e8\u95e8\u5173\u8054\u8868\u5b9e\u4f53")
public class AdminDept
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u7ba1\u7406\u5458id")
    private Integer adminId;
    @ApiModelProperty(value="\u90e8\u95e8id")
    private Integer deptId;

    public Integer getAdminId() {
        return this.adminId;
    }

    public Integer getDeptId() {
        return this.deptId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AdminDept)) {
            return false;
        }
        AdminDept other = (AdminDept)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$adminId = this.getAdminId();
        Integer other$adminId = other.getAdminId();
        if (this$adminId == null ? other$adminId != null : !((Object)this$adminId).equals(other$adminId)) {
            return false;
        }
        Integer this$deptId = this.getDeptId();
        Integer other$deptId = other.getDeptId();
        return !(this$deptId == null ? other$deptId != null : !((Object)this$deptId).equals(other$deptId));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AdminDept;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $adminId = this.getAdminId();
        result = result * 59 + ($adminId == null ? 43 : ((Object)$adminId).hashCode());
        Integer $deptId = this.getDeptId();
        result = result * 59 + ($deptId == null ? 43 : ((Object)$deptId).hashCode());
        return result;
    }

    public String toString() {
        return "AdminDept(adminId=" + this.getAdminId() + ", deptId=" + this.getDeptId() + ")";
    }
}

