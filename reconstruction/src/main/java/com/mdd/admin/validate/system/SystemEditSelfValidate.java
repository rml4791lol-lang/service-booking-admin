/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.system.SystemEditSelfValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotEmpty
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u7cfb\u7edf\u7ba1\u7406\u5458\u81ea\u66f4\u65b0\u53c2\u6570")
public class SystemEditSelfValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotEmpty(message="\u6635\u79f0\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=2, max=30, message="\u6635\u79f0\u5fc5\u987b\u57282~30\u4e2a\u5b57\u7b26\u5185")
    @ApiModelProperty(value="\u7528\u6237\u6635\u79f0")
    private @NotEmpty(message="\u6635\u79f0\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=2, max=30, message="\u6635\u79f0\u5fc5\u987b\u57282~30\u4e2a\u5b57\u7b26\u5185") String account;
    @NotEmpty(message="\u6635\u79f0\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=2, max=30, message="\u6635\u79f0\u5fc5\u987b\u57282~30\u4e2a\u5b57\u7b26\u5185")
    @ApiModelProperty(value="\u7528\u6237\u6635\u79f0")
    private @NotEmpty(message="\u6635\u79f0\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=2, max=30, message="\u6635\u79f0\u5fc5\u987b\u57282~30\u4e2a\u5b57\u7b26\u5185") String name;
    @Length(max=200, message="\u5934\u50cf\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26")
    private @Length(max=200, message="\u5934\u50cf\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26") String avatar = "";
    @Length(max=64, message="\u5bc6\u7801\u5fc5\u987b\u57286~32\u4e2a\u5b57\u7b26\u5185")
    private @Length(max=64, message="\u5bc6\u7801\u5fc5\u987b\u57286~32\u4e2a\u5b57\u7b26\u5185") String password = "";
    @Length(max=64, message="\u5f53\u524d\u5bc6\u7801\u9519\u8bef")
    private @Length(max=64, message="\u5f53\u524d\u5bc6\u7801\u9519\u8bef") String password_confirm = "";
    @Length(max=64, message="\u65e7\u5bc6\u7801\u9519\u8bef")
    private @Length(max=64, message="\u65e7\u5bc6\u7801\u9519\u8bef") String password_old;

    public String getAccount() {
        return this.account;
    }

    public String getName() {
        return this.name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPassword_confirm() {
        return this.password_confirm;
    }

    public String getPassword_old() {
        return this.password_old;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPassword_confirm(String password_confirm) {
        this.password_confirm = password_confirm;
    }

    public void setPassword_old(String password_old) {
        this.password_old = password_old;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemEditSelfValidate)) {
            return false;
        }
        SystemEditSelfValidate other = (SystemEditSelfValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$account = this.getAccount();
        String other$account = other.getAccount();
        if (this$account == null ? other$account != null : !this$account.equals(other$account)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
            return false;
        }
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        String this$password_confirm = this.getPassword_confirm();
        String other$password_confirm = other.getPassword_confirm();
        if (this$password_confirm == null ? other$password_confirm != null : !this$password_confirm.equals(other$password_confirm)) {
            return false;
        }
        String this$password_old = this.getPassword_old();
        String other$password_old = other.getPassword_old();
        return !(this$password_old == null ? other$password_old != null : !this$password_old.equals(other$password_old));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SystemEditSelfValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $password_confirm = this.getPassword_confirm();
        result = result * 59 + ($password_confirm == null ? 43 : $password_confirm.hashCode());
        String $password_old = this.getPassword_old();
        result = result * 59 + ($password_old == null ? 43 : $password_old.hashCode());
        return result;
    }

    public String toString() {
        return "SystemEditSelfValidate(account=" + this.getAccount() + ", name=" + this.getName() + ", avatar=" + this.getAvatar() + ", password=" + this.getPassword() + ", password_confirm=" + this.getPassword_confirm() + ", password_old=" + this.getPassword_old() + ")";
    }
}

