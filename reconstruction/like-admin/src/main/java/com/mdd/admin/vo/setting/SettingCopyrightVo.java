/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.setting.SettingCopyrightVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u7248\u6743\u8bbe\u7f6eVo")
public class SettingCopyrightVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u540d\u79f0")
    private String key;
    @ApiModelProperty(value="\u94fe\u63a5")
    private String value;

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingCopyrightVo)) {
            return false;
        }
        SettingCopyrightVo other = (SettingCopyrightVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$key = this.getKey();
        String other$key = other.getKey();
        if (this$key == null ? other$key != null : !this$key.equals(other$key)) {
            return false;
        }
        String this$value = this.getValue();
        String other$value = other.getValue();
        return !(this$value == null ? other$value != null : !this$value.equals(other$value));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingCopyrightVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $key = this.getKey();
        result = result * 59 + ($key == null ? 43 : $key.hashCode());
        String $value = this.getValue();
        result = result * 59 + ($value == null ? 43 : $value.hashCode());
        return result;
    }

    public String toString() {
        return "SettingCopyrightVo(key=" + this.getKey() + ", value=" + this.getValue() + ")";
    }
}

