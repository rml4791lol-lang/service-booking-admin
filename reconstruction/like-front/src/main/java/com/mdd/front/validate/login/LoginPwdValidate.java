/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.login.LoginPwdValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.front.validate.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u8d26\u53f7\u767b\u5f55\u53c2\u6570")
public class LoginPwdValidate {
    @NotNull(message="username\u53c2\u6570\u7f3a\u5931")
    @NotEmpty(message="\u8d26\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u767b\u5f55\u8d26\u53f7", required=true)
    private @NotNull(message="username\u53c2\u6570\u7f3a\u5931") @NotEmpty(message="\u8d26\u53f7\u4e0d\u80fd\u4e3a\u7a7a") String account;
    @ApiModelProperty(value="\u767b\u5f55\u5bc6\u7801")
    private String password;
    @ApiModelProperty(value="\u9a8c\u8bc1\u7801\u767b\u5f55")
    private String code;
    @ApiModelProperty(value="\u7ec8\u7aef")
    private Integer scene;
    @ApiModelProperty(value="\u7ec8\u7aef")
    private Integer terminal;

    @NotNull(message="username\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="username\u53c2\u6570\u7f3a\u5931") String getAccount() {
        return this.account;
    }

    public String getPassword() {
        return this.password;
    }

    public String getCode() {
        return this.code;
    }

    public Integer getScene() {
        return this.scene;
    }

    public Integer getTerminal() {
        return this.terminal;
    }

    public void setAccount(@NotNull(message="username\u53c2\u6570\u7f3a\u5931") @NotNull(message="username\u53c2\u6570\u7f3a\u5931") String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setScene(Integer scene) {
        this.scene = scene;
    }

    public void setTerminal(Integer terminal) {
        this.terminal = terminal;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LoginPwdValidate)) {
            return false;
        }
        LoginPwdValidate other = (LoginPwdValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$scene = this.getScene();
        Integer other$scene = other.getScene();
        if (this$scene == null ? other$scene != null : !((Object)this$scene).equals(other$scene)) {
            return false;
        }
        Integer this$terminal = this.getTerminal();
        Integer other$terminal = other.getTerminal();
        if (this$terminal == null ? other$terminal != null : !((Object)this$terminal).equals(other$terminal)) {
            return false;
        }
        String this$account = this.getAccount();
        String other$account = other.getAccount();
        if (this$account == null ? other$account != null : !this$account.equals(other$account)) {
            return false;
        }
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        return !(this$code == null ? other$code != null : !this$code.equals(other$code));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LoginPwdValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $scene = this.getScene();
        result = result * 59 + ($scene == null ? 43 : ((Object)$scene).hashCode());
        Integer $terminal = this.getTerminal();
        result = result * 59 + ($terminal == null ? 43 : ((Object)$terminal).hashCode());
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        return result;
    }

    public String toString() {
        return "LoginPwdValidate(account=" + this.getAccount() + ", password=" + this.getPassword() + ", code=" + this.getCode() + ", scene=" + this.getScene() + ", terminal=" + this.getTerminal() + ")";
    }
}

