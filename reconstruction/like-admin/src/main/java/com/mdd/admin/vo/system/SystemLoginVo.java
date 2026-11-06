/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.mdd.admin.vo.system.SystemLoginVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u7cfb\u7edf\u767b\u5f55Vo")
public class SystemLoginVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u7ba1\u7406\u5458ID")
    private Integer id;
    @ApiModelProperty(value="\u767b\u5f55\u4ee4\u724c")
    private String token;
    @ApiModelProperty(value="\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u7528\u6237\u5934\u50cf")
    private String avatar;
    @ApiModelProperty(value="\u7528\u6237\u5934\u50cf")
    @JsonProperty(value="role_name")
    private String roleName;

    public Integer getId() {
        return this.id;
    }

    public String getToken() {
        return this.token;
    }

    public String getName() {
        return this.name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JsonProperty(value="role_name")
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemLoginVo)) {
            return false;
        }
        SystemLoginVo other = (SystemLoginVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$token = this.getToken();
        String other$token = other.getToken();
        if (this$token == null ? other$token != null : !this$token.equals(other$token)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
            return false;
        }
        String this$roleName = this.getRoleName();
        String other$roleName = other.getRoleName();
        return !(this$roleName == null ? other$roleName != null : !this$roleName.equals(other$roleName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SystemLoginVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $token = this.getToken();
        result = result * 59 + ($token == null ? 43 : $token.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $roleName = this.getRoleName();
        result = result * 59 + ($roleName == null ? 43 : $roleName.hashCode());
        return result;
    }

    public String toString() {
        return "SystemLoginVo(id=" + this.getId() + ", token=" + this.getToken() + ", name=" + this.getName() + ", avatar=" + this.getAvatar() + ", roleName=" + this.getRoleName() + ")";
    }
}

