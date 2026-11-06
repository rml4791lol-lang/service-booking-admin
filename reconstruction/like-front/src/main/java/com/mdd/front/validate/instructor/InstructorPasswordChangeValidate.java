package com.mdd.front.validate.instructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

@ApiModel("教练修改登录密码参数")
public class InstructorPasswordChangeValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "请输入当前密码")
    @ApiModelProperty(value = "当前密码", required = true)
    private String oldPassword;

    @NotEmpty(message = "请输入新密码")
    @Length(min = 8, max = 20, message = "新密码长度必须为8~20位")
    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;

    @NotEmpty(message = "请再次输入新密码")
    @ApiModelProperty(value = "确认新密码", required = true)
    private String confirmPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
