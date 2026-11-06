/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.users.UserPhoneMnpValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.front.validate.users;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u5fae\u4fe1\u624b\u673a\u53c2\u6570")
public class UserPhoneMnpValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="code\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u5fae\u4fe1code", required=true)
    private @NotNull(message="code\u53c2\u6570\u7f3a\u5931") String code;

    @NotNull(message="code\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="code\u53c2\u6570\u7f3a\u5931") String getCode() {
        return this.code;
    }

    public void setCode(@NotNull(message="code\u53c2\u6570\u7f3a\u5931") @NotNull(message="code\u53c2\u6570\u7f3a\u5931") String code) {
        this.code = code;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserPhoneMnpValidate)) {
            return false;
        }
        UserPhoneMnpValidate other = (UserPhoneMnpValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        return !(this$code == null ? other$code != null : !this$code.equals(other$code));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserPhoneMnpValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        return result;
    }

    public String toString() {
        return "UserPhoneMnpValidate(code=" + this.getCode() + ")";
    }
}

