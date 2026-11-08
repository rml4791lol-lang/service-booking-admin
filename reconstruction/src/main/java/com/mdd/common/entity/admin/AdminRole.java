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

@ApiModel(value="\u89d2\u8272\u5173\u8054\u8868\u5b9e\u4f53")
public class AdminRole
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u7ba1\u7406\u5458id")
    private Integer adminId;
    @ApiModelProperty(value="\u89d2\u8272id")
    private Integer roleId;

    public Integer getAdminId() {
        return this.adminId;
    }

    public Integer getRoleId() {
        return this.roleId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AdminRole)) {
            return false;
        }
        AdminRole other = (AdminRole)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$adminId = this.getAdminId();
        Integer other$adminId = other.getAdminId();
        if (this$adminId == null ? other$adminId != null : !((Object)this$adminId).equals(other$adminId)) {
            return false;
        }
        Integer this$roleId = this.getRoleId();
        Integer other$roleId = other.getRoleId();
        return !(this$roleId == null ? other$roleId != null : !((Object)this$roleId).equals(other$roleId));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AdminRole;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $adminId = this.getAdminId();
        result = result * 59 + ($adminId == null ? 43 : ((Object)$adminId).hashCode());
        Integer $roleId = this.getRoleId();
        result = result * 59 + ($roleId == null ? 43 : ((Object)$roleId).hashCode());
        return result;
    }

    public String toString() {
        return "AdminRole(adminId=" + this.getAdminId() + ", roleId=" + this.getRoleId() + ")";
    }
}

