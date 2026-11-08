/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONArray
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.mdd.admin.vo.auth.AdminMySelfVo
 *  com.mdd.admin.vo.auth.AuthMySelfVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.auth;

import com.alibaba.fastjson2.JSONArray;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mdd.admin.vo.auth.AuthMySelfVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value="adminVo")
public class AdminMySelfVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u5f53\u524d\u7ba1\u7406\u5458\u89d2\u8272\u62e5\u6709\u7684\u83dc\u5355")
    private JSONArray menu;
    @ApiModelProperty(value="\u5f53\u524d\u7ba1\u7406\u5458\u6a58\u8272\u62e5\u6709\u7684\u6309\u94ae\u6743\u9650")
    private List<String> permissions;
    @ApiModelProperty(value="user")
    private AuthMySelfVo user;
    @ApiModelProperty(value="\u662f\u5426\u521d\u6b21\u767b\u5f55")
    @JsonProperty(value="first_login")
    private Integer firstLogin;

    public JSONArray getMenu() {
        return this.menu;
    }

    public List<String> getPermissions() {
        return this.permissions;
    }

    public AuthMySelfVo getUser() {
        return this.user;
    }

    public Integer getFirstLogin() {
        return this.firstLogin;
    }

    public void setMenu(JSONArray menu) {
        this.menu = menu;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public void setUser(AuthMySelfVo user) {
        this.user = user;
    }

    @JsonProperty(value="first_login")
    public void setFirstLogin(Integer firstLogin) {
        this.firstLogin = firstLogin;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AdminMySelfVo)) {
            return false;
        }
        AdminMySelfVo other = (AdminMySelfVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$firstLogin = this.getFirstLogin();
        Integer other$firstLogin = other.getFirstLogin();
        if (this$firstLogin == null ? other$firstLogin != null : !((Object)this$firstLogin).equals(other$firstLogin)) {
            return false;
        }
        JSONArray this$menu = this.getMenu();
        JSONArray other$menu = other.getMenu();
        if (this$menu == null ? other$menu != null : !this$menu.equals(other$menu)) {
            return false;
        }
        List this$permissions = this.getPermissions();
        List other$permissions = other.getPermissions();
        if (this$permissions == null ? other$permissions != null : !((Object)this$permissions).equals(other$permissions)) {
            return false;
        }
        AuthMySelfVo this$user = this.getUser();
        AuthMySelfVo other$user = other.getUser();
        return !(this$user == null ? other$user != null : !this$user.equals(other$user));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AdminMySelfVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $firstLogin = this.getFirstLogin();
        result = result * 59 + ($firstLogin == null ? 43 : ((Object)$firstLogin).hashCode());
        JSONArray $menu = this.getMenu();
        result = result * 59 + ($menu == null ? 43 : $menu.hashCode());
        List $permissions = this.getPermissions();
        result = result * 59 + ($permissions == null ? 43 : ((Object)$permissions).hashCode());
        AuthMySelfVo $user = this.getUser();
        result = result * 59 + ($user == null ? 43 : $user.hashCode());
        return result;
    }

    public String toString() {
        return "AdminMySelfVo(menu=" + this.getMenu() + ", permissions=" + this.getPermissions() + ", user=" + this.getUser() + ", firstLogin=" + this.getFirstLogin() + ")";
    }
}

