/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.login.LoginScanValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.front.validate.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u626b\u7801\u767b\u5f55\u9a8c\u8bc1")
public class LoginScanValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="code\u53c2\u6570\u7f3a\u5931")
    @NotEmpty(message="code\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u5fae\u4fe1code", required=true)
    private @NotNull(message="code\u53c2\u6570\u7f3a\u5931") @NotEmpty(message="code\u4e0d\u80fd\u4e3a\u7a7a") String code;
    @NotNull(message="\u4e8c\u7ef4\u7801\u5df2\u5931\u6548\u6216\u4e0d\u5b58\u5728,\u8bf7\u91cd\u65b0\u64cd\u4f5c")
    @NotEmpty(message="\u4e8c\u7ef4\u7801\u5df2\u5931\u6548\u6216\u4e0d\u5b58\u5728,\u8bf7\u91cd\u65b0\u64cd\u4f5c")
    @ApiModelProperty(value="state\u7801", required=true)
    private @NotNull(message="\u4e8c\u7ef4\u7801\u5df2\u5931\u6548\u6216\u4e0d\u5b58\u5728,\u8bf7\u91cd\u65b0\u64cd\u4f5c") @NotEmpty(message="\u4e8c\u7ef4\u7801\u5df2\u5931\u6548\u6216\u4e0d\u5b58\u5728,\u8bf7\u91cd\u65b0\u64cd\u4f5c") String state;

    @NotNull(message="code\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="code\u53c2\u6570\u7f3a\u5931") String getCode() {
        return this.code;
    }

    @NotNull(message="\u4e8c\u7ef4\u7801\u5df2\u5931\u6548\u6216\u4e0d\u5b58\u5728,\u8bf7\u91cd\u65b0\u64cd\u4f5c")
    public @NotNull(message="\u4e8c\u7ef4\u7801\u5df2\u5931\u6548\u6216\u4e0d\u5b58\u5728,\u8bf7\u91cd\u65b0\u64cd\u4f5c") String getState() {
        return this.state;
    }

    public void setCode(@NotNull(message="code\u53c2\u6570\u7f3a\u5931") @NotNull(message="code\u53c2\u6570\u7f3a\u5931") String code) {
        this.code = code;
    }

    public void setState(@NotNull(message="\u4e8c\u7ef4\u7801\u5df2\u5931\u6548\u6216\u4e0d\u5b58\u5728,\u8bf7\u91cd\u65b0\u64cd\u4f5c") @NotNull(message="\u4e8c\u7ef4\u7801\u5df2\u5931\u6548\u6216\u4e0d\u5b58\u5728,\u8bf7\u91cd\u65b0\u64cd\u4f5c") String state) {
        this.state = state;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LoginScanValidate)) {
            return false;
        }
        LoginScanValidate other = (LoginScanValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) {
            return false;
        }
        String this$state = this.getState();
        String other$state = other.getState();
        return !(this$state == null ? other$state != null : !this$state.equals(other$state));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LoginScanValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        String $state = this.getState();
        result = result * 59 + ($state == null ? 43 : $state.hashCode());
        return result;
    }

    public String toString() {
        return "LoginScanValidate(code=" + this.getCode() + ", state=" + this.getState() + ")";
    }
}

