/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.mdd.admin.vo.auth.AuthMySelfVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value="\u7ba1\u7406\u5458")
public class AuthMySelfVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    @ApiModelProperty(value="\u8d26\u53f7")
    private String account;
    @ApiModelProperty(value="\u7528\u6237\u5934\u50cf")
    private String avatar;
    @ApiModelProperty(value="\u90e8\u95e8\u5173\u8054ID")
    @JsonProperty(value="dept_id")
    private List<Integer> deptId;
    @ApiModelProperty(value="\u5f53\u524d\u7ba1\u7406\u5458\u89d2\u8272\u62e5\u6709\u7684\u83dc\u5355")
    private Integer disable;
    @ApiModelProperty(value="\u5f53\u524d\u7ba1\u7406\u5458\u89d2\u8272\u62e5\u6709\u7684\u83dc\u5355")
    @JsonProperty(value="jobs_id")
    private List<Integer> jobsId;
    @ApiModelProperty(value="\u5f53\u524d\u7ba1\u7406\u5458\u89d2\u8272\u62e5\u6709\u7684\u83dc\u5355")
    @JsonProperty(value="multipoint_login")
    private Integer multipointLogin;
    @ApiModelProperty(value="\u5f53\u524d\u7ba1\u7406\u5458\u89d2\u8272\u62e5\u6709\u7684\u83dc\u5355")
    private String name;
    @ApiModelProperty(value="\u5f53\u524d\u7ba1\u7406\u5458\u89d2\u8272\u62e5\u6709\u7684\u83dc\u5355")
    @JsonProperty(value="role_id")
    private List<Integer> roleId;
    @ApiModelProperty(value="\u5f53\u524d\u7ba1\u7406\u5458\u89d2\u8272\u62e5\u6709\u7684\u83dc\u5355")
    private Integer root;

    public Integer getId() {
        return this.id;
    }

    public String getAccount() {
        return this.account;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public List<Integer> getDeptId() {
        return this.deptId;
    }

    public Integer getDisable() {
        return this.disable;
    }

    public List<Integer> getJobsId() {
        return this.jobsId;
    }

    public Integer getMultipointLogin() {
        return this.multipointLogin;
    }

    public String getName() {
        return this.name;
    }

    public List<Integer> getRoleId() {
        return this.roleId;
    }

    public Integer getRoot() {
        return this.root;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JsonProperty(value="dept_id")
    public void setDeptId(List<Integer> deptId) {
        this.deptId = deptId;
    }

    public void setDisable(Integer disable) {
        this.disable = disable;
    }

    @JsonProperty(value="jobs_id")
    public void setJobsId(List<Integer> jobsId) {
        this.jobsId = jobsId;
    }

    @JsonProperty(value="multipoint_login")
    public void setMultipointLogin(Integer multipointLogin) {
        this.multipointLogin = multipointLogin;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty(value="role_id")
    public void setRoleId(List<Integer> roleId) {
        this.roleId = roleId;
    }

    public void setRoot(Integer root) {
        this.root = root;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AuthMySelfVo)) {
            return false;
        }
        AuthMySelfVo other = (AuthMySelfVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$disable = this.getDisable();
        Integer other$disable = other.getDisable();
        if (this$disable == null ? other$disable != null : !((Object)this$disable).equals(other$disable)) {
            return false;
        }
        Integer this$multipointLogin = this.getMultipointLogin();
        Integer other$multipointLogin = other.getMultipointLogin();
        if (this$multipointLogin == null ? other$multipointLogin != null : !((Object)this$multipointLogin).equals(other$multipointLogin)) {
            return false;
        }
        Integer this$root = this.getRoot();
        Integer other$root = other.getRoot();
        if (this$root == null ? other$root != null : !((Object)this$root).equals(other$root)) {
            return false;
        }
        String this$account = this.getAccount();
        String other$account = other.getAccount();
        if (this$account == null ? other$account != null : !this$account.equals(other$account)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
            return false;
        }
        List this$deptId = this.getDeptId();
        List other$deptId = other.getDeptId();
        if (this$deptId == null ? other$deptId != null : !((Object)this$deptId).equals(other$deptId)) {
            return false;
        }
        List this$jobsId = this.getJobsId();
        List other$jobsId = other.getJobsId();
        if (this$jobsId == null ? other$jobsId != null : !((Object)this$jobsId).equals(other$jobsId)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        List this$roleId = this.getRoleId();
        List other$roleId = other.getRoleId();
        return !(this$roleId == null ? other$roleId != null : !((Object)this$roleId).equals(other$roleId));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AuthMySelfVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $disable = this.getDisable();
        result = result * 59 + ($disable == null ? 43 : ((Object)$disable).hashCode());
        Integer $multipointLogin = this.getMultipointLogin();
        result = result * 59 + ($multipointLogin == null ? 43 : ((Object)$multipointLogin).hashCode());
        Integer $root = this.getRoot();
        result = result * 59 + ($root == null ? 43 : ((Object)$root).hashCode());
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        List $deptId = this.getDeptId();
        result = result * 59 + ($deptId == null ? 43 : ((Object)$deptId).hashCode());
        List $jobsId = this.getJobsId();
        result = result * 59 + ($jobsId == null ? 43 : ((Object)$jobsId).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        List $roleId = this.getRoleId();
        result = result * 59 + ($roleId == null ? 43 : ((Object)$roleId).hashCode());
        return result;
    }

    public String toString() {
        return "AuthMySelfVo(id=" + this.getId() + ", account=" + this.getAccount() + ", avatar=" + this.getAvatar() + ", deptId=" + this.getDeptId() + ", disable=" + this.getDisable() + ", jobsId=" + this.getJobsId() + ", multipointLogin=" + this.getMultipointLogin() + ", name=" + this.getName() + ", roleId=" + this.getRoleId() + ", root=" + this.getRoot() + ")";
    }
}

