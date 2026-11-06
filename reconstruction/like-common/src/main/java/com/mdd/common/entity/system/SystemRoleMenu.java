/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u89d2\u8272\u83dc\u5355\u5173\u7cfb\u5b9e\u4f53")
public class SystemRoleMenu
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u89d2\u8272ID")
    private Integer roleId;
    @ApiModelProperty(value="\u83dc\u5355ID")
    private Integer menuId;

    public Integer getRoleId() {
        return this.roleId;
    }

    public Integer getMenuId() {
        return this.menuId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemRoleMenu)) {
            return false;
        }
        SystemRoleMenu other = (SystemRoleMenu)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$roleId = this.getRoleId();
        Integer other$roleId = other.getRoleId();
        if (this$roleId == null ? other$roleId != null : !((Object)this$roleId).equals(other$roleId)) {
            return false;
        }
        Integer this$menuId = this.getMenuId();
        Integer other$menuId = other.getMenuId();
        return !(this$menuId == null ? other$menuId != null : !((Object)this$menuId).equals(other$menuId));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SystemRoleMenu;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $roleId = this.getRoleId();
        result = result * 59 + ($roleId == null ? 43 : ((Object)$roleId).hashCode());
        Integer $menuId = this.getMenuId();
        result = result * 59 + ($menuId == null ? 43 : ((Object)$menuId).hashCode());
        return result;
    }

    public String toString() {
        return "SystemRoleMenu(roleId=" + this.getRoleId() + ", menuId=" + this.getMenuId() + ")";
    }
}

