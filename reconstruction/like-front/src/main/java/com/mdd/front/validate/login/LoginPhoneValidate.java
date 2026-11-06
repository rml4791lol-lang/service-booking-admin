/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.login.LoginPhoneValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  javax.validation.constraints.Pattern
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.front.validate.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u624b\u673a\u53f7\u767b\u5f55\u53c2\u6570")
public class LoginPhoneValidate {
    @NotNull(message="mobile\u53c2\u6570\u7f3a\u5931")
    @NotEmpty(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=11, max=11, message="\u624b\u673a\u53f7\u53ea\u80fd\u4e3a11\u4f4d")
    @Pattern(regexp="^[1][3,4,5,6,7,8,9][0-9]{9}$", message="\u624b\u673a\u53f7\u683c\u5f0f\u6709\u8bef")
    @ApiModelProperty(value="\u624b\u673a\u53f7", required=true)
    private @NotNull(message="mobile\u53c2\u6570\u7f3a\u5931") @NotEmpty(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=11, max=11, message="\u624b\u673a\u53f7\u53ea\u80fd\u4e3a11\u4f4d") @Pattern(regexp="^[1][3,4,5,6,7,8,9][0-9]{9}$", message="\u624b\u673a\u53f7\u683c\u5f0f\u6709\u8bef") String mobile;
    @NotNull(message="code\u53c2\u6570\u7f3a\u5931")
    @NotEmpty(message="code\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=4, max=6, message="\u9a8c\u8bc1\u7801\u957f\u5ea6\u4e0d\u7b26\u5408")
    @ApiModelProperty(value="\u9a8c\u8bc1\u7801", required=true)
    private @NotNull(message="code\u53c2\u6570\u7f3a\u5931") @NotEmpty(message="code\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=4, max=6, message="\u9a8c\u8bc1\u7801\u957f\u5ea6\u4e0d\u7b26\u5408") String code;

    @NotNull(message="mobile\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="mobile\u53c2\u6570\u7f3a\u5931") String getMobile() {
        return this.mobile;
    }

    @NotNull(message="code\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="code\u53c2\u6570\u7f3a\u5931") String getCode() {
        return this.code;
    }

    public void setMobile(@NotNull(message="mobile\u53c2\u6570\u7f3a\u5931") @NotNull(message="mobile\u53c2\u6570\u7f3a\u5931") String mobile) {
        this.mobile = mobile;
    }

    public void setCode(@NotNull(message="code\u53c2\u6570\u7f3a\u5931") @NotNull(message="code\u53c2\u6570\u7f3a\u5931") String code) {
        this.code = code;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LoginPhoneValidate)) {
            return false;
        }
        LoginPhoneValidate other = (LoginPhoneValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        if (this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile)) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        return !(this$code == null ? other$code != null : !this$code.equals(other$code));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LoginPhoneValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        return result;
    }

    public String toString() {
        return "LoginPhoneValidate(mobile=" + this.getMobile() + ", code=" + this.getCode() + ")";
    }
}

