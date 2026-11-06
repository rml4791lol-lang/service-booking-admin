/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.login.RegisterValidate
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
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u6ce8\u518c\u8d26\u53f7\u53c2\u6570")
public class RegisterValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="\u8d26\u53f7\u7f3a\u5931")
    @NotEmpty(message="\u8d26\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u767b\u5f55\u8d26\u53f7", required=true)
    private @NotNull(message="\u8d26\u53f7\u7f3a\u5931") @NotEmpty(message="\u8d26\u53f7\u4e0d\u80fd\u4e3a\u7a7a") String account;
    @NotNull(message="password\u53c2\u6570\u7f3a\u5931")
    @NotEmpty(message="\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=6, max=12, message="\u5bc6\u7801\u5fc5\u987b\u57286~12\u4e2a\u5b57\u7b26\u5185")
    @ApiModelProperty(value="\u767b\u5f55\u5bc6\u7801", required=true)
    private @NotNull(message="password\u53c2\u6570\u7f3a\u5931") @NotEmpty(message="\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=6, max=12, message="\u5bc6\u7801\u5fc5\u987b\u57286~12\u4e2a\u5b57\u7b26\u5185") String password;
    @ApiModelProperty(value="\u7528\u6237\u7c7b\u578b: student=\u5b66\u5458, instructor=\u6559\u7ec3", required=true)
    @NotNull(message="\u7528\u6237\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a")
    @Pattern(message="\u7528\u6237\u7c7b\u578b\u53ea\u80fd\u662fstudent\u6216instructor", regexp="^(student|instructor)$")
    private @NotNull(message="\u7528\u6237\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a") @Pattern(message="\u7528\u6237\u7c7b\u578b\u53ea\u80fd\u662fstudent\u6216instructor", regexp="^(student|instructor)$") String userType;
    @ApiModelProperty(value="\u7ec8\u7aef")
    private Integer terminal;

    @NotNull(message="\u8d26\u53f7\u7f3a\u5931")
    public @NotNull(message="\u8d26\u53f7\u7f3a\u5931") String getAccount() {
        return this.account;
    }

    @NotNull(message="password\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="password\u53c2\u6570\u7f3a\u5931") String getPassword() {
        return this.password;
    }

    @NotNull(message="\u7528\u6237\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u7528\u6237\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a") String getUserType() {
        return this.userType;
    }

    public Integer getTerminal() {
        return this.terminal;
    }

    public void setAccount(@NotNull(message="\u8d26\u53f7\u7f3a\u5931") @NotNull(message="\u8d26\u53f7\u7f3a\u5931") String account) {
        this.account = account;
    }

    public void setPassword(@NotNull(message="password\u53c2\u6570\u7f3a\u5931") @NotNull(message="password\u53c2\u6570\u7f3a\u5931") String password) {
        this.password = password;
    }

    public void setUserType(@NotNull(message="\u7528\u6237\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u7528\u6237\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a") String userType) {
        this.userType = userType;
    }

    public void setTerminal(Integer terminal) {
        this.terminal = terminal;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RegisterValidate)) {
            return false;
        }
        RegisterValidate other = (RegisterValidate)o;
        if (!other.canEqual((Object)this)) {
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
        String this$userType = this.getUserType();
        String other$userType = other.getUserType();
        return !(this$userType == null ? other$userType != null : !this$userType.equals(other$userType));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RegisterValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $terminal = this.getTerminal();
        result = result * 59 + ($terminal == null ? 43 : ((Object)$terminal).hashCode());
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $userType = this.getUserType();
        result = result * 59 + ($userType == null ? 43 : $userType.hashCode());
        return result;
    }

    public String toString() {
        return "RegisterValidate(account=" + this.getAccount() + ", password=" + this.getPassword() + ", userType=" + this.getUserType() + ", terminal=" + this.getTerminal() + ")";
    }
}

