/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.system.SystemAuthAdminSelvesVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="\u5f53\u524d\u7cfb\u7edf\u7ba1\u7406\u5458Vo")
public class SystemAuthAdminSelvesVo {
    @ApiModelProperty(value="\u7528\u6237\u4fe1\u606f")
    private Object user;
    @ApiModelProperty(value="\u6743\u9650\u96c6\u5408: [[*]=>\u6240\u6709\u6743\u9650, ['article:add']=>\u90e8\u5206\u6743\u9650]")
    private Object permissions;

    public Object getUser() {
        return this.user;
    }

    public Object getPermissions() {
        return this.permissions;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public void setPermissions(Object permissions) {
        this.permissions = permissions;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemAuthAdminSelvesVo)) {
            return false;
        }
        SystemAuthAdminSelvesVo other = (SystemAuthAdminSelvesVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Object this$user = this.getUser();
        Object other$user = other.getUser();
        if (this$user == null ? other$user != null : !this$user.equals(other$user)) {
            return false;
        }
        Object this$permissions = this.getPermissions();
        Object other$permissions = other.getPermissions();
        return !(this$permissions == null ? other$permissions != null : !this$permissions.equals(other$permissions));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SystemAuthAdminSelvesVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $user = this.getUser();
        result = result * 59 + ($user == null ? 43 : $user.hashCode());
        Object $permissions = this.getPermissions();
        result = result * 59 + ($permissions == null ? 43 : $permissions.hashCode());
        return result;
    }

    public String toString() {
        return "SystemAuthAdminSelvesVo(user=" + this.getUser() + ", permissions=" + this.getPermissions() + ")";
    }
}

