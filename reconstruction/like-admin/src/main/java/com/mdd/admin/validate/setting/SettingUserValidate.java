/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.setting.SettingUserValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u7528\u6237\u8bbe\u7f6e\u53c2\u6570")
public class SettingUserValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u9ed8\u8ba4\u5934\u50cf")
    private String defaultAvatar = "";

    public String getDefaultAvatar() {
        return this.defaultAvatar;
    }

    public void setDefaultAvatar(String defaultAvatar) {
        this.defaultAvatar = defaultAvatar;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingUserValidate)) {
            return false;
        }
        SettingUserValidate other = (SettingUserValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$defaultAvatar = this.getDefaultAvatar();
        String other$defaultAvatar = other.getDefaultAvatar();
        return !(this$defaultAvatar == null ? other$defaultAvatar != null : !this$defaultAvatar.equals(other$defaultAvatar));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingUserValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $defaultAvatar = this.getDefaultAvatar();
        result = result * 59 + ($defaultAvatar == null ? 43 : $defaultAvatar.hashCode());
        return result;
    }

    public String toString() {
        return "SettingUserValidate(defaultAvatar=" + this.getDefaultAvatar() + ")";
    }
}

