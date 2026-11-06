/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.users.UserChangePwdValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 *  javax.validation.constraints.Pattern
 */
package com.mdd.front.validate.users;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(value="\u4fee\u6539\u5bc6\u7801\u53c2\u6570")
public class UserChangePwdValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="password\u53c2\u6570\u7f3a\u5931")
    @Pattern(message="\u5bc6\u7801\u5fc5\u987b\u662f6-20\u4f4d,\u5305\u542b\u5b57\u6bcd\u548c\u6570\u5b57!", regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?~`]{6,20}$")
    @ApiModelProperty(value="\u65b0\u5bc6\u7801", required=true)
    private @NotNull(message="password\u53c2\u6570\u7f3a\u5931") @Pattern(message="\u5bc6\u7801\u5fc5\u987b\u662f6-20\u4f4d,\u5305\u542b\u5b57\u6bcd\u548c\u6570\u5b57!", regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?~`]{6,20}$") String password;
    @NotNull(message="\u8bf7\u786e\u8ba4\u5bc6\u7801")
    @Pattern(message="\u786e\u8ba4\u5bc6\u7801\u5fc5\u987b\u662f6-20\u4f4d,\u5305\u542b\u5b57\u6bcd\u548c\u6570\u5b57!", regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?~`]{6,20}$")
    @ApiModelProperty(value="\u786e\u8ba4\u5bc6\u7801", required=true)
    private @NotNull(message="\u8bf7\u786e\u8ba4\u5bc6\u7801") @Pattern(message="\u786e\u8ba4\u5bc6\u7801\u5fc5\u987b\u662f6-20\u4f4d,\u5305\u542b\u5b57\u6bcd\u548c\u6570\u5b57!", regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?~`]{6,20}$") String passwordConfirm;
    @ApiModelProperty(value="\u65e7\u5bc6\u7801")
    private String oldPassword = "";

    @NotNull(message="password\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="password\u53c2\u6570\u7f3a\u5931") String getPassword() {
        return this.password;
    }

    @NotNull(message="\u8bf7\u786e\u8ba4\u5bc6\u7801")
    public @NotNull(message="\u8bf7\u786e\u8ba4\u5bc6\u7801") String getPasswordConfirm() {
        return this.passwordConfirm;
    }

    public String getOldPassword() {
        return this.oldPassword;
    }

    public void setPassword(@NotNull(message="password\u53c2\u6570\u7f3a\u5931") @NotNull(message="password\u53c2\u6570\u7f3a\u5931") String password) {
        this.password = password;
    }

    public void setPasswordConfirm(@NotNull(message="\u8bf7\u786e\u8ba4\u5bc6\u7801") @NotNull(message="\u8bf7\u786e\u8ba4\u5bc6\u7801") String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserChangePwdValidate)) {
            return false;
        }
        UserChangePwdValidate other = (UserChangePwdValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        String this$passwordConfirm = this.getPasswordConfirm();
        String other$passwordConfirm = other.getPasswordConfirm();
        if (this$passwordConfirm == null ? other$passwordConfirm != null : !this$passwordConfirm.equals(other$passwordConfirm)) {
            return false;
        }
        String this$oldPassword = this.getOldPassword();
        String other$oldPassword = other.getOldPassword();
        return !(this$oldPassword == null ? other$oldPassword != null : !this$oldPassword.equals(other$oldPassword));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserChangePwdValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $passwordConfirm = this.getPasswordConfirm();
        result = result * 59 + ($passwordConfirm == null ? 43 : $passwordConfirm.hashCode());
        String $oldPassword = this.getOldPassword();
        result = result * 59 + ($oldPassword == null ? 43 : $oldPassword.hashCode());
        return result;
    }

    public String toString() {
        return "UserChangePwdValidate(password=" + this.getPassword() + ", passwordConfirm=" + this.getPasswordConfirm() + ", oldPassword=" + this.getOldPassword() + ")";
    }
}

