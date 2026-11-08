/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.system.SystemAuthAdminListedVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value="\u7ba1\u7406\u5458\u5217\u8868Vo")
public class SystemAuthAdminListedVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u4e3b\u952e")
    private Integer id;
    @ApiModelProperty(value="\u8d26\u53f7")
    private String account;
    @ApiModelProperty(value="\u6635\u79f0")
    private String name;
    @ApiModelProperty(value="\u5934\u50cf")
    private String avatar;
    @ApiModelProperty(value="\u90e8\u95e8")
    private String deptName;
    @ApiModelProperty(value="\u89d2\u8272")
    private String roleName;
    @ApiModelProperty(value="\u5c97\u4f4d")
    private String jobsName;
    private List<Integer> roleId;
    private List<Integer> deptId;
    private List<Integer> jobsId;
    @ApiModelProperty(value="\u591a\u7aef\u767b\u5f55: [0=\u5426, 1=\u662f]")
    private Integer multipointLogin;
    @ApiModelProperty(value="\u662f\u5426\u7981\u7528: [0=\u5426, 1=\u662f]")
    private Integer disable;
    private String disableDesc;
    private Integer root;
    @ApiModelProperty(value="\u6700\u540e\u767b\u5f55IP")
    private String loginIp;
    @ApiModelProperty(value="\u6700\u540e\u767b\u5f55\u65f6\u95f4")
    private String loginTime;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private String updateTime;

    public Integer getId() {
        return this.id;
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

    public String getDeptName() {
        return this.deptName;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public String getJobsName() {
        return this.jobsName;
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

    public Integer getMultipointLogin() {
        return this.multipointLogin;
    }

    public Integer getDisable() {
        return this.disable;
    }

    public String getDisableDesc() {
        return this.disableDesc;
    }

    public Integer getRoot() {
        return this.root;
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

    public void setId(Integer id) {
        this.id = id;
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

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setJobsName(String jobsName) {
        this.jobsName = jobsName;
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

    public void setMultipointLogin(Integer multipointLogin) {
        this.multipointLogin = multipointLogin;
    }

    public void setDisable(Integer disable) {
        this.disable = disable;
    }

    public void setDisableDesc(String disableDesc) {
        this.disableDesc = disableDesc;
    }

    public void setRoot(Integer root) {
        this.root = root;
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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemAuthAdminListedVo)) {
            return false;
        }
        SystemAuthAdminListedVo other = (SystemAuthAdminListedVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$multipointLogin = this.getMultipointLogin();
        Integer other$multipointLogin = other.getMultipointLogin();
        if (this$multipointLogin == null ? other$multipointLogin != null : !((Object)this$multipointLogin).equals(other$multipointLogin)) {
            return false;
        }
        Integer this$disable = this.getDisable();
        Integer other$disable = other.getDisable();
        if (this$disable == null ? other$disable != null : !((Object)this$disable).equals(other$disable)) {
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
        String this$deptName = this.getDeptName();
        String other$deptName = other.getDeptName();
        if (this$deptName == null ? other$deptName != null : !this$deptName.equals(other$deptName)) {
            return false;
        }
        String this$roleName = this.getRoleName();
        String other$roleName = other.getRoleName();
        if (this$roleName == null ? other$roleName != null : !this$roleName.equals(other$roleName)) {
            return false;
        }
        String this$jobsName = this.getJobsName();
        String other$jobsName = other.getJobsName();
        if (this$jobsName == null ? other$jobsName != null : !this$jobsName.equals(other$jobsName)) {
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
        String this$disableDesc = this.getDisableDesc();
        String other$disableDesc = other.getDisableDesc();
        if (this$disableDesc == null ? other$disableDesc != null : !this$disableDesc.equals(other$disableDesc)) {
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
        return other instanceof SystemAuthAdminListedVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $multipointLogin = this.getMultipointLogin();
        result = result * 59 + ($multipointLogin == null ? 43 : ((Object)$multipointLogin).hashCode());
        Integer $disable = this.getDisable();
        result = result * 59 + ($disable == null ? 43 : ((Object)$disable).hashCode());
        Integer $root = this.getRoot();
        result = result * 59 + ($root == null ? 43 : ((Object)$root).hashCode());
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $deptName = this.getDeptName();
        result = result * 59 + ($deptName == null ? 43 : $deptName.hashCode());
        String $roleName = this.getRoleName();
        result = result * 59 + ($roleName == null ? 43 : $roleName.hashCode());
        String $jobsName = this.getJobsName();
        result = result * 59 + ($jobsName == null ? 43 : $jobsName.hashCode());
        List $roleId = this.getRoleId();
        result = result * 59 + ($roleId == null ? 43 : ((Object)$roleId).hashCode());
        List $deptId = this.getDeptId();
        result = result * 59 + ($deptId == null ? 43 : ((Object)$deptId).hashCode());
        List $jobsId = this.getJobsId();
        result = result * 59 + ($jobsId == null ? 43 : ((Object)$jobsId).hashCode());
        String $disableDesc = this.getDisableDesc();
        result = result * 59 + ($disableDesc == null ? 43 : $disableDesc.hashCode());
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
        return "SystemAuthAdminListedVo(id=" + this.getId() + ", account=" + this.getAccount() + ", name=" + this.getName() + ", avatar=" + this.getAvatar() + ", deptName=" + this.getDeptName() + ", roleName=" + this.getRoleName() + ", jobsName=" + this.getJobsName() + ", roleId=" + this.getRoleId() + ", deptId=" + this.getDeptId() + ", jobsId=" + this.getJobsId() + ", multipointLogin=" + this.getMultipointLogin() + ", disable=" + this.getDisable() + ", disableDesc=" + this.getDisableDesc() + ", root=" + this.getRoot() + ", loginIp=" + this.getLoginIp() + ", loginTime=" + this.getLoginTime() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

