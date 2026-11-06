/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.users.ResetPasswordValidate
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
public class ResetPasswordValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="password\u53c2\u6570\u7f3a\u5931")
    @Pattern(message="\u5bc6\u7801\u5fc5\u987b\u662f6-20\u5b57\u6bcd+\u6570\u5b57\u7ec4\u5408!", regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$")
    @ApiModelProperty(value="\u65b0\u5bc6\u7801", required=true)
    private @NotNull(message="password\u53c2\u6570\u7f3a\u5931") @Pattern(message="\u5bc6\u7801\u5fc5\u987b\u662f6-20\u5b57\u6bcd+\u6570\u5b57\u7ec4\u5408!", regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$") String password;
    @NotNull(message="\u8bf7\u786e\u8ba4\u5bc6\u7801")
    @Pattern(message="\u786e\u8ba4\u5bc6\u7801\u5bc6\u7801\u5fc5\u987b\u662f6-20\u5b57\u6bcd+\u6570\u5b57\u7ec4\u5408!", regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$")
    @ApiModelProperty(value="\u786e\u8ba4\u5bc6\u7801", required=true)
    private @NotNull(message="\u8bf7\u786e\u8ba4\u5bc6\u7801") @Pattern(message="\u786e\u8ba4\u5bc6\u7801\u5bc6\u7801\u5fc5\u987b\u662f6-20\u5b57\u6bcd+\u6570\u5b57\u7ec4\u5408!", regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$") String passwordConfirm;
    @NotNull(message="\u9a8c\u8bc1\u7801\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="code", required=true)
    private @NotNull(message="\u9a8c\u8bc1\u7801\u4e0d\u80fd\u4e3a\u7a7a") String code;
    @NotNull(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="mobile", required=true)
    private @NotNull(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a") String mobile;

    @NotNull(message="password\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="password\u53c2\u6570\u7f3a\u5931") String getPassword() {
        return this.password;
    }

    @NotNull(message="\u8bf7\u786e\u8ba4\u5bc6\u7801")
    public @NotNull(message="\u8bf7\u786e\u8ba4\u5bc6\u7801") String getPasswordConfirm() {
        return this.passwordConfirm;
    }

    @NotNull(message="\u9a8c\u8bc1\u7801\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u9a8c\u8bc1\u7801\u4e0d\u80fd\u4e3a\u7a7a") String getCode() {
        return this.code;
    }

    @NotNull(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a") String getMobile() {
        return this.mobile;
    }

    public void setPassword(@NotNull(message="password\u53c2\u6570\u7f3a\u5931") @NotNull(message="password\u53c2\u6570\u7f3a\u5931") String password) {
        this.password = password;
    }

    public void setPasswordConfirm(@NotNull(message="\u8bf7\u786e\u8ba4\u5bc6\u7801") @NotNull(message="\u8bf7\u786e\u8ba4\u5bc6\u7801") String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public void setCode(@NotNull(message="\u9a8c\u8bc1\u7801\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u9a8c\u8bc1\u7801\u4e0d\u80fd\u4e3a\u7a7a") String code) {
        this.code = code;
    }

    public void setMobile(@NotNull(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a") String mobile) {
        this.mobile = mobile;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ResetPasswordValidate)) {
            return false;
        }
        ResetPasswordValidate other = (ResetPasswordValidate)o;
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
        String this$code = this.getCode();
        String other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) {
            return false;
        }
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        return !(this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ResetPasswordValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $passwordConfirm = this.getPasswordConfirm();
        result = result * 59 + ($passwordConfirm == null ? 43 : $passwordConfirm.hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        return result;
    }

    public String toString() {
        return "ResetPasswordValidate(password=" + this.getPassword() + ", passwordConfirm=" + this.getPasswordConfirm() + ", code=" + this.getCode() + ", mobile=" + this.getMobile() + ")";
    }
}

