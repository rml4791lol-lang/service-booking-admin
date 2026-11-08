/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.system.SystemAuthAdminDetailVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value="\u7ba1\u7406\u5458\u8be6\u60c5Vo")
public class SystemAuthAdminDetailVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u4e3b\u952e")
    private Integer id;
    @ApiModelProperty(value="\u662f\u5426\u8d85\u7ea7\u7ba1\u7406\u5458 0-\u5426 1-\u662f")
    private Integer root;
    @ApiModelProperty(value="\u89d2\u8272ID")
    private List<Integer> roleId;
    @ApiModelProperty(value="\u90e8\u95e8ID")
    private List<Integer> deptId;
    @ApiModelProperty(value="\u5c97\u4f4dID")
    private List<Integer> jobsId;
    @ApiModelProperty(value="\u8d26\u53f7")
    private String account;
    @ApiModelProperty(value="\u6635\u79f0")
    private String name;
    @ApiModelProperty(value="\u5934\u50cf")
    private String avatar;
    @ApiModelProperty(value="\u662f\u5426\u7981\u7528: [0=\u5426, 1=\u662f]")
    private Integer disable;
    @ApiModelProperty(value="\u6700\u540e\u767b\u5f55IP")
    private String loginIp;
    @ApiModelProperty(value="\u6700\u540e\u767b\u5f55\u65f6\u95f4")
    private String loginTime;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private String updateTime;
    @ApiModelProperty(value="\u662f\u5426\u652f\u6301\u591a\u5904\u767b\u5f55\uff1a1-\u662f\uff1b0-\u5426\uff1b")
    private Integer multipointLogin;
    @ApiModelProperty(value="\u662f\u5426\u4e3a\u9996\u6b21\u767b\u5f55")
    private Integer firstLogin;

    public Integer getId() {
        return this.id;
    }

    public Integer getRoot() {
        return this.root;
    }

    public List<Integer> getRoleId() {
        return this.roleId;
    }

    public List<Integer> getDeptId() {
        return this.deptId;
    }

    public List<Integer> getJobsId() {
        return this.jobsId;
    }

    public String getAccount() {
        return this.account;
    }

    public String getName() {
        return this.name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public Integer getDisable() {
        return this.disable;
    }

    public String getLoginIp() {
        return this.loginIp;
    }

    public String getLoginTime() {
        return this.loginTime;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public Integer getMultipointLogin() {
        return this.multipointLogin;
    }

    public Integer getFirstLogin() {
        return this.firstLogin;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoot(Integer root) {
        this.root = root;
    }

    public void setRoleId(List<Integer> roleId) {
        this.roleId = roleId;
    }

    public void setDeptId(List<Integer> deptId) {
        this.deptId = deptId;
    }

    public void setJobsId(List<Integer> jobsId) {
        this.jobsId = jobsId;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setDisable(Integer disable) {
        this.disable = disable;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setMultipointLogin(Integer multipointLogin) {
        this.multipointLogin = multipointLogin;
    }

    public void setFirstLogin(Integer firstLogin) {
        this.firstLogin = firstLogin;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemAuthAdminDetailVo)) {
            return false;
        }
        SystemAuthAdminDetailVo other = (SystemAuthAdminDetailVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$root = this.getRoot();
        Integer other$root = other.getRoot();
        if (this$root == null ? other$root != null : !((Object)this$root).equals(other$root)) {
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
        Integer this$firstLogin = this.getFirstLogin();
        Integer other$firstLogin = other.getFirstLogin();
        if (this$firstLogin == null ? other$firstLogin != null : !((Object)this$firstLogin).equals(other$firstLogin)) {
            return false;
        }
        List this$roleId = this.getRoleId();
        List other$roleId = other.getRoleId();
        if (this$roleId == null ? other$roleId != null : !((Object)this$roleId).equals(other$roleId)) {
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
        String this$account = this.getAccount();
        String other$account = other.getAccount();
        if (this$account == null ? other$account != null : !this$account.equals(other$account)) {
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
        String this$loginIp = this.getLoginIp();
        String other$loginIp = other.getLoginIp();
        if (this$loginIp == null ? other$loginIp != null : !this$loginIp.equals(other$loginIp)) {
            return false;
        }
        String this$loginTime = this.getLoginTime();
        String other$loginTime = other.getLoginTime();
        if (this$loginTime == null ? other$loginTime != null : !this$loginTime.equals(other$loginTime)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime)) {
            return false;
        }
        String this$updateTime = this.getUpdateTime();
        String other$updateTime = other.getUpdateTime();
        return !(this$updateTime == null ? other$updateTime != null : !this$updateTime.equals(other$updateTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SystemAuthAdminDetailVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $root = this.getRoot();
        result = result * 59 + ($root == null ? 43 : ((Object)$root).hashCode());
        Integer $disable = this.getDisable();
        result = result * 59 + ($disable == null ? 43 : ((Object)$disable).hashCode());
        Integer $multipointLogin = this.getMultipointLogin();
        result = result * 59 + ($multipointLogin == null ? 43 : ((Object)$multipointLogin).hashCode());
        Integer $firstLogin = this.getFirstLogin();
        result = result * 59 + ($firstLogin == null ? 43 : ((Object)$firstLogin).hashCode());
        List $roleId = this.getRoleId();
        result = result * 59 + ($roleId == null ? 43 : ((Object)$roleId).hashCode());
        List $deptId = this.getDeptId();
        result = result * 59 + ($deptId == null ? 43 : ((Object)$deptId).hashCode());
        List $jobsId = this.getJobsId();
        result = result * 59 + ($jobsId == null ? 43 : ((Object)$jobsId).hashCode());
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $loginIp = this.getLoginIp();
        result = result * 59 + ($loginIp == null ? 43 : $loginIp.hashCode());
        String $loginTime = this.getLoginTime();
        result = result * 59 + ($loginTime == null ? 43 : $loginTime.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        String $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        return result;
    }

    public String toString() {
        return "SystemAuthAdminDetailVo(id=" + this.getId() + ", root=" + this.getRoot() + ", roleId=" + this.getRoleId() + ", deptId=" + this.getDeptId() + ", jobsId=" + this.getJobsId() + ", account=" + this.getAccount() + ", name=" + this.getName() + ", avatar=" + this.getAvatar() + ", disable=" + this.getDisable() + ", loginIp=" + this.getLoginIp() + ", loginTime=" + this.getLoginTime() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", multipointLogin=" + this.getMultipointLogin() + ", firstLogin=" + this.getFirstLogin() + ")";
    }
}

