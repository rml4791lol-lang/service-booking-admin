/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.system.SystemAdminUpInfoValidate
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
public class SystemAdminUpInfoValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotEmpty(message="\u6635\u79f0\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=2, max=30, message="\u6635\u79f0\u5fc5\u987b\u57282~30\u4e2a\u5b57\u7b26\u5185")
    @ApiModelProperty(value="\u7528\u6237\u6635\u79f0")
    private @NotEmpty(message="\u6635\u79f0\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=2, max=30, message="\u6635\u79f0\u5fc5\u987b\u57282~30\u4e2a\u5b57\u7b26\u5185") String nickname;
    @Length(max=64, message="\u5bc6\u7801\u5fc5\u987b\u57286~32\u4e2a\u5b57\u7b26\u5185")
    private @Length(max=64, message="\u5bc6\u7801\u5fc5\u987b\u57286~32\u4e2a\u5b57\u7b26\u5185") String password = "";
    @Length(max=64, message="\u5f53\u524d\u5bc6\u7801\u9519\u8bef")
    private @Length(max=64, message="\u5f53\u524d\u5bc6\u7801\u9519\u8bef") String currPassword = "";
    @Length(max=200, message="\u5934\u50cf\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26")
    private @Length(max=200, message="\u5934\u50cf\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26") String avatar = "";

    public String getNickname() {
        return this.nickname;
    }

    public String getPassword() {
        return this.password;
    }

    public String getCurrPassword() {
        return this.currPassword;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCurrPassword(String currPassword) {
        this.currPassword = currPassword;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemAdminUpInfoValidate)) {
            return false;
        }
        SystemAdminUpInfoValidate other = (SystemAdminUpInfoValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$nickname = this.getNickname();
        String other$nickname = other.getNickname();
        if (this$nickname == null ? other$nickname != null : !this$nickname.equals(other$nickname)) {
            return false;
        }
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        String this$currPassword = this.getCurrPassword();
        String other$currPassword = other.getCurrPassword();
        if (this$currPassword == null ? other$currPassword != null : !this$currPassword.equals(other$currPassword)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        return !(this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SystemAdminUpInfoValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $nickname = this.getNickname();
        result = result * 59 + ($nickname == null ? 43 : $nickname.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $currPassword = this.getCurrPassword();
        result = result * 59 + ($currPassword == null ? 43 : $currPassword.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        return result;
    }

    public String toString() {
        return "SystemAdminUpInfoValidate(nickname=" + this.getNickname() + ", password=" + this.getPassword() + ", currPassword=" + this.getCurrPassword() + ", avatar=" + this.getAvatar() + ")";
    }
}

