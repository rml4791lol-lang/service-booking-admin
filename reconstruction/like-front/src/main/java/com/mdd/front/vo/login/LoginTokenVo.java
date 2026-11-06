/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.vo.login.LoginTokenVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.vo.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u7cfb\u7edf\u767b\u5f55Vo")
public class LoginTokenVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u7528\u6237ID")
    private Integer id;
    @ApiModelProperty(value="\u7ed1\u5b9a\u624b\u673a")
    private Boolean isBindMobile;
    @ApiModelProperty(value="\u7ed1\u5b9a\u624b\u673a")
    private String mobile;
    @ApiModelProperty(value="\u767b\u5f55\u4ee4\u724c")
    private String token;
    @ApiModelProperty(value="\u662f\u5426\u4e3a\u65b0\u7528\u6237")
    private Integer isNew;
    @ApiModelProperty(value="\u6559\u7ec3ID(\u4ec5\u6559\u7ec3\u767b\u5f55\u8fd4\u56de)")
    private Integer instructorId;
    @ApiModelProperty(value="\u6559\u7ec3\u59d3\u540d(\u4ec5\u6559\u7ec3\u767b\u5f55\u8fd4\u56de)")
    private String name;
    @ApiModelProperty(value="\u9996\u6b21\u767b\u5f55\u662f\u5426\u5fc5\u987b\u4fee\u6539\u521d\u59cb\u5bc6\u7801")
    private Boolean mustChangePassword;

    public Integer getId() {
        return this.id;
    }

    public Boolean getIsBindMobile() {
        return this.isBindMobile;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getToken() {
        return this.token;
    }

    public Integer getIsNew() {
        return this.isNew;
    }

    public Integer getInstructorId() {
        return this.instructorId;
    }

    public String getName() {
        return this.name;
    }

    public Boolean getMustChangePassword() {
        return this.mustChangePassword;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIsBindMobile(Boolean isBindMobile) {
        this.isBindMobile = isBindMobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMustChangePassword(Boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LoginTokenVo)) {
            return false;
        }
        LoginTokenVo other = (LoginTokenVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Boolean this$isBindMobile = this.getIsBindMobile();
        Boolean other$isBindMobile = other.getIsBindMobile();
        if (this$isBindMobile == null ? other$isBindMobile != null : !((Object)this$isBindMobile).equals(other$isBindMobile)) {
            return false;
        }
        Integer this$isNew = this.getIsNew();
        Integer other$isNew = other.getIsNew();
        if (this$isNew == null ? other$isNew != null : !((Object)this$isNew).equals(other$isNew)) {
            return false;
        }
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        if (this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile)) {
            return false;
        }
        String this$token = this.getToken();
        String other$token = other.getToken();
        return !(this$token == null ? other$token != null : !this$token.equals(other$token));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LoginTokenVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Boolean $isBindMobile = this.getIsBindMobile();
        result = result * 59 + ($isBindMobile == null ? 43 : ((Object)$isBindMobile).hashCode());
        Integer $isNew = this.getIsNew();
        result = result * 59 + ($isNew == null ? 43 : ((Object)$isNew).hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        String $token = this.getToken();
        result = result * 59 + ($token == null ? 43 : $token.hashCode());
        return result;
    }

    public String toString() {
        return "LoginTokenVo(id=" + this.getId() + ", isBindMobile=" + this.getIsBindMobile() + ", mobile=" + this.getMobile() + ", token=" + this.getToken() + ", isNew=" + this.getIsNew() + ")";
    }
}

