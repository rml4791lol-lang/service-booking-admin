package com.mdd.front.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel("教练账号信息Vo")
public class InstructorAccountInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "教练ID")
    private Integer instructorId;
    @ApiModelProperty(value = "教练姓名")
    private String name;
    @ApiModelProperty(value = "预留手机号（脱敏）")
    private String phone;
    @ApiModelProperty(value = "资料状态:0=停用,1=正常,2=待审核")
    private Integer status;
    @ApiModelProperty(value = "资料状态文案")
    private String statusLabel;
    @ApiModelProperty(value = "是否首次登录必须改密")
    private Boolean mustChangePassword;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public Boolean getMustChangePassword() {
        return mustChangePassword;
    }

    public void setMustChangePassword(Boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
