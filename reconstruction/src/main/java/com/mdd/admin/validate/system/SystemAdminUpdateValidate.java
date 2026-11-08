/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.system.SystemAdminUpdateValidate
 *  com.mdd.common.validator.annotation.IDMust
 *  com.mdd.common.validator.annotation.IntegerContains
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.DecimalMin
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.system;

import com.mdd.common.validator.annotation.IDMust;
import com.mdd.common.validator.annotation.IntegerContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u7cfb\u7edf\u7ba1\u7406\u5458\u66f4\u65b0\u53c2\u6570")
public class SystemAdminUpdateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @IDMust(message="id\u53c2\u6570\u5fc5\u4f20\u4e14\u9700\u5927\u4e8e0")
    @ApiModelProperty(value="ID", required=true)
    private Integer id;
    @NotEmpty(message="\u8d26\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=2, max=20, message="\u8d26\u53f7\u5fc5\u987b\u57282~20\u4e2a\u5b57\u7b26\u5185")
    @ApiModelProperty(value="\u767b\u5f55\u8d26\u53f7", required=true)
    private @NotEmpty(message="\u8d26\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=2, max=20, message="\u8d26\u53f7\u5fc5\u987b\u57282~20\u4e2a\u5b57\u7b26\u5185") String account;
    @NotEmpty(message="\u6635\u79f0\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=2, max=30, message="\u6635\u79f0\u5fc5\u987b\u57282~30\u4e2a\u5b57\u7b26\u5185")
    @ApiModelProperty(value="\u7528\u6237\u6635\u79f0", required=true)
    private @NotEmpty(message="\u6635\u79f0\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=2, max=30, message="\u6635\u79f0\u5fc5\u987b\u57282~30\u4e2a\u5b57\u7b26\u5185") String name;
    @ApiModelProperty(value="\u767b\u5f55\u5bc6\u7801")
    private String password;
    @NotNull(message="\u8bf7\u9009\u62e9\u662f\u5426\u7981\u7528")
    @IntegerContains(values={0, 1})
    @ApiModelProperty(value="\u662f\u5426\u7981\u7528", required=true)
    private @NotNull(message="\u8bf7\u9009\u62e9\u662f\u5426\u7981\u7528") Integer disable;
    @NotNull(message="\u8bf7\u9009\u62e9\u662f\u5426\u652f\u6301\u591a\u7aef\u767b\u5f55")
    @IntegerContains(values={0, 1})
    @ApiModelProperty(value="\u652f\u6301\u591a\u7aef", required=true)
    private @NotNull(message="\u8bf7\u9009\u62e9\u662f\u5426\u652f\u6301\u591a\u7aef\u767b\u5f55") Integer multipointLogin;
    @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    @DecimalMin(value="0", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5c11\u4e8e0")
    @ApiModelProperty(value="\u6392\u5e8f", required=true)
    private @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @DecimalMin(value="0", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5c11\u4e8e0") Integer sort = 0;
    @Length(max=200, message="\u5934\u50cf\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u5934\u50cf")
    private @Length(max=200, message="\u5934\u50cf\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26") String avatar = "";
    @ApiModelProperty(value="\u89d2\u8272ID")
    private List<Integer> roleId;
    @ApiModelProperty(value="\u90e8\u95e8ID")
    private List<Integer> deptId;
    @ApiModelProperty(value="\u5c97\u4f4dID")
    private List<Integer> jobsId;
    private Integer root;

    public Integer getId() {
        return this.id;
    }

    public String getAccount() {
        return this.account;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    @NotNull(message="\u8bf7\u9009\u62e9\u662f\u5426\u7981\u7528")
    public @NotNull(message="\u8bf7\u9009\u62e9\u662f\u5426\u7981\u7528") Integer getDisable() {
        return this.disable;
    }

    @NotNull(message="\u8bf7\u9009\u62e9\u662f\u5426\u652f\u6301\u591a\u7aef\u767b\u5f55")
    public @NotNull(message="\u8bf7\u9009\u62e9\u662f\u5426\u652f\u6301\u591a\u7aef\u767b\u5f55") Integer getMultipointLogin() {
        return this.multipointLogin;
    }

    @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") Integer getSort() {
        return this.sort;
    }

    public String getAvatar() {
        return this.avatar;
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

    public Integer getRoot() {
        return this.root;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDisable(@NotNull(message="\u8bf7\u9009\u62e9\u662f\u5426\u7981\u7528") @NotNull(message="\u8bf7\u9009\u62e9\u662f\u5426\u7981\u7528") Integer disable) {
        this.disable = disable;
    }

    public void setMultipointLogin(@NotNull(message="\u8bf7\u9009\u62e9\u662f\u5426\u652f\u6301\u591a\u7aef\u767b\u5f55") @NotNull(message="\u8bf7\u9009\u62e9\u662f\u5426\u652f\u6301\u591a\u7aef\u767b\u5f55") Integer multipointLogin) {
        this.multipointLogin = multipointLogin;
    }

    public void setSort(@NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") Integer sort) {
        this.sort = sort;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public void setRoot(Integer root) {
        this.root = root;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemAdminUpdateValidate)) {
            return false;
        }
        SystemAdminUpdateValidate other = (SystemAdminUpdateValidate)o;
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
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
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
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
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
        return !(this$jobsId == null ? other$jobsId != null : !((Object)this$jobsId).equals(other$jobsId));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SystemAdminUpdateValidate;
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
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        Integer $root = this.getRoot();
        result = result * 59 + ($root == null ? 43 : ((Object)$root).hashCode());
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        List $roleId = this.getRoleId();
        result = result * 59 + ($roleId == null ? 43 : ((Object)$roleId).hashCode());
        List $deptId = this.getDeptId();
        result = result * 59 + ($deptId == null ? 43 : ((Object)$deptId).hashCode());
        List $jobsId = this.getJobsId();
        result = result * 59 + ($jobsId == null ? 43 : ((Object)$jobsId).hashCode());
        return result;
    }

    public String toString() {
        return "SystemAdminUpdateValidate(id=" + this.getId() + ", account=" + this.getAccount() + ", name=" + this.getName() + ", password=" + this.getPassword() + ", disable=" + this.getDisable() + ", multipointLogin=" + this.getMultipointLogin() + ", sort=" + this.getSort() + ", avatar=" + this.getAvatar() + ", roleId=" + this.getRoleId() + ", deptId=" + this.getDeptId() + ", jobsId=" + this.getJobsId() + ", root=" + this.getRoot() + ")";
    }
}

