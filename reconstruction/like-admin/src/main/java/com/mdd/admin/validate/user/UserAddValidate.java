/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.user.UserAddValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  javax.validation.constraints.Pattern
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u7528\u6237\u65b0\u589e\u53c2\u6570")
public class UserAddValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    @NotEmpty(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    @Pattern(regexp="^[1][3-9][0-9]{9}$", message="\u624b\u673a\u53f7\u683c\u5f0f\u4e0d\u6b63\u786e")
    @ApiModelProperty(value="\u624b\u673a\u53f7", required=true)
    private @NotNull(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @NotEmpty(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @Pattern(regexp="^[1][3-9][0-9]{9}$", message="\u624b\u673a\u53f7\u683c\u5f0f\u4e0d\u6b63\u786e") String mobile;
    @NotNull(message="\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a")
    @NotEmpty(message="\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=6, max=20, message="\u5bc6\u7801\u5fc5\u987b\u57286~20\u4e2a\u5b57\u7b26\u5185")
    @ApiModelProperty(value="\u5bc6\u7801", required=true)
    private @NotNull(message="\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a") @NotEmpty(message="\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=6, max=20, message="\u5bc6\u7801\u5fc5\u987b\u57286~20\u4e2a\u5b57\u7b26\u5185") String password;
    @ApiModelProperty(value="\u6635\u79f0")
    private String nickname;

    @NotNull(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a") String getMobile() {
        return this.mobile;
    }

    @NotNull(message="\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a") String getPassword() {
        return this.password;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setMobile(@NotNull(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u624b\u673a\u53f7\u4e0d\u80fd\u4e3a\u7a7a") String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(@NotNull(message="\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a") String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserAddValidate)) {
            return false;
        }
        UserAddValidate other = (UserAddValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        if (this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile)) {
            return false;
        }
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        String this$nickname = this.getNickname();
        String other$nickname = other.getNickname();
        return !(this$nickname == null ? other$nickname != null : !this$nickname.equals(other$nickname));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserAddValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $nickname = this.getNickname();
        result = result * 59 + ($nickname == null ? 43 : $nickname.hashCode());
        return result;
    }

    public String toString() {
        return "UserAddValidate(mobile=" + this.getMobile() + ", password=" + this.getPassword() + ", nickname=" + this.getNickname() + ")";
    }
}

