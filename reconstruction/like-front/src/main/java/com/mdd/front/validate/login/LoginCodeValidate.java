/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.login.LoginCodeValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.front.validate.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u5fae\u4fe1\u767b\u5f55\u53c2\u6570")
public class LoginCodeValidate {
    @NotNull(message="code\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u5fae\u4fe1code", required=true)
    private @NotNull(message="code\u53c2\u6570\u7f3a\u5931") String code;
    @ApiModelProperty(value="\u7ec8\u7aef")
    private Integer terminal;

    @NotNull(message="code\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="code\u53c2\u6570\u7f3a\u5931") String getCode() {
        return this.code;
    }

    public Integer getTerminal() {
        return this.terminal;
    }

    public void setCode(@NotNull(message="code\u53c2\u6570\u7f3a\u5931") @NotNull(message="code\u53c2\u6570\u7f3a\u5931") String code) {
        this.code = code;
    }

    public void setTerminal(Integer terminal) {
        this.terminal = terminal;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LoginCodeValidate)) {
            return false;
        }
        LoginCodeValidate other = (LoginCodeValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$terminal = this.getTerminal();
        Integer other$terminal = other.getTerminal();
        if (this$terminal == null ? other$terminal != null : !((Object)this$terminal).equals(other$terminal)) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        return !(this$code == null ? other$code != null : !this$code.equals(other$code));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LoginCodeValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $terminal = this.getTerminal();
        result = result * 59 + ($terminal == null ? 43 : ((Object)$terminal).hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        return result;
    }

    public String toString() {
        return "LoginCodeValidate(code=" + this.getCode() + ", terminal=" + this.getTerminal() + ")";
    }
}

