package com.mdd.admin.vo.instructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "教练账号状态Vo")
public class InstructorAccountVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "教练ID")
    private Integer instructorId;
    @ApiModelProperty(value = "教练姓名")
    private String name;
    @ApiModelProperty(value = "联系电话/登录手机号")
    private String phone;
    @ApiModelProperty(value = "资质状态:0=停用,1=正常,2=待审核")
    private Integer instructorStatus;
    @ApiModelProperty(value = "资质状态文案")
    private String instructorStatusLabel;
    @ApiModelProperty(value = "登录账号ID")
    private Integer userId;
    @ApiModelProperty(value = "登录账号")
    private String account;
    @ApiModelProperty(value = "是否已绑定登录账号")
    private Boolean accountLinked;
    @ApiModelProperty(value = "登录账号是否存在")
    private Boolean accountExists;
    @ApiModelProperty(value = "是否已设置登录密码")
    private Boolean hasPassword;
    @ApiModelProperty(value = "是否首次登录待改密")
    private Boolean mustChangePassword;
    @ApiModelProperty(value = "登录账号是否被禁用")
    private Boolean userDisabled;
    @ApiModelProperty(value = "账号状态:UNOPENED/PENDING_CHANGE/ACTIVE/DISABLED/PENDING_REVIEW/BLOCKED")
    private String accountStatus;
    @ApiModelProperty(value = "账号状态文案")
    private String accountStatusLabel;
    @ApiModelProperty(value = "当前是否允许登录")
    private Boolean loginAllowed;
    @ApiModelProperty(value = "不可登录原因")
    private String loginBlockReason;
    @ApiModelProperty(value = "账号开通时间")
    private String accountOpenedTime;
    @ApiModelProperty(value = "密码最后更新时间")
    private String passwordUpdatedTime;
    @ApiModelProperty(value = "最后登录时间")
    private String lastLoginTime;

    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getInstructorStatus() {
        return instructorStatus;
    }

    public void setInstructorStatus(Integer instructorStatus) {
        this.instructorStatus = instructorStatus;
    }

    public String getInstructorStatusLabel() {
        return instructorStatusLabel;
    }

    public void setInstructorStatusLabel(String instructorStatusLabel) {
        this.instructorStatusLabel = instructorStatusLabel;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Boolean getAccountLinked() {
        return accountLinked;
    }

    public void setAccountLinked(Boolean accountLinked) {
        this.accountLinked = accountLinked;
    }

    public Boolean getAccountExists() {
        return accountExists;
    }

    public void setAccountExists(Boolean accountExists) {
        this.accountExists = accountExists;
    }

    public Boolean getHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(Boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public Boolean getMustChangePassword() {
        return mustChangePassword;
    }

    public void setMustChangePassword(Boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    public Boolean getUserDisabled() {
        return userDisabled;
    }

    public void setUserDisabled(Boolean userDisabled) {
        this.userDisabled = userDisabled;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccountStatusLabel() {
        return accountStatusLabel;
    }

    public void setAccountStatusLabel(String accountStatusLabel) {
        this.accountStatusLabel = accountStatusLabel;
    }

    public Boolean getLoginAllowed() {
        return loginAllowed;
    }

    public void setLoginAllowed(Boolean loginAllowed) {
        this.loginAllowed = loginAllowed;
    }

    public String getLoginBlockReason() {
        return loginBlockReason;
    }

    public void setLoginBlockReason(String loginBlockReason) {
        this.loginBlockReason = loginBlockReason;
    }

    public String getAccountOpenedTime() {
        return accountOpenedTime;
    }

    public void setAccountOpenedTime(String accountOpenedTime) {
        this.accountOpenedTime = accountOpenedTime;
    }

    public String getPasswordUpdatedTime() {
        return passwordUpdatedTime;
    }

    public void setPasswordUpdatedTime(String passwordUpdatedTime) {
        this.passwordUpdatedTime = passwordUpdatedTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
