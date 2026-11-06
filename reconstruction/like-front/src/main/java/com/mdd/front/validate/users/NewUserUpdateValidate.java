/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.users.NewUserUpdateValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.front.validate.users;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u65b0\u7528\u6237\u66f4\u65b0\u4fe1\u606f\u53c2\u6570")
public class NewUserUpdateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="\u6635\u79f0\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u6635\u79f0", required=true)
    private @NotNull(message="\u6635\u79f0\u53c2\u6570\u7f3a\u5931") String nickname;
    @NotNull(message="\u5934\u50cf\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u5934\u50cf", required=true)
    private @NotNull(message="\u5934\u50cf\u53c2\u6570\u7f3a\u5931") String avatar;

    @NotNull(message="\u6635\u79f0\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="\u6635\u79f0\u53c2\u6570\u7f3a\u5931") String getNickname() {
        return this.nickname;
    }

    @NotNull(message="\u5934\u50cf\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="\u5934\u50cf\u53c2\u6570\u7f3a\u5931") String getAvatar() {
        return this.avatar;
    }

    public void setNickname(@NotNull(message="\u6635\u79f0\u53c2\u6570\u7f3a\u5931") @NotNull(message="\u6635\u79f0\u53c2\u6570\u7f3a\u5931") String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(@NotNull(message="\u5934\u50cf\u53c2\u6570\u7f3a\u5931") @NotNull(message="\u5934\u50cf\u53c2\u6570\u7f3a\u5931") String avatar) {
        this.avatar = avatar;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NewUserUpdateValidate)) {
            return false;
        }
        NewUserUpdateValidate other = (NewUserUpdateValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$nickname = this.getNickname();
        String other$nickname = other.getNickname();
        if (this$nickname == null ? other$nickname != null : !this$nickname.equals(other$nickname)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        return !(this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar));
    }

    protected boolean canEqual(Object other) {
        return other instanceof NewUserUpdateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $nickname = this.getNickname();
        result = result * 59 + ($nickname == null ? 43 : $nickname.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        return result;
    }

    public String toString() {
        return "NewUserUpdateValidate(nickname=" + this.getNickname() + ", avatar=" + this.getAvatar() + ")";
    }
}

