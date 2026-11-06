/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.setting.SettingCopyrightValidate
 *  com.mdd.admin.vo.setting.SettingCopyrightVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.setting;

import com.mdd.admin.vo.setting.SettingCopyrightVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value="\u7248\u6743\u4fe1\u606f\u8bbe\u7f6e\u53c2\u6570")
public class SettingCopyrightValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u7248\u6743\u5217\u8868")
    private List<SettingCopyrightVo> config;

    public List<SettingCopyrightVo> getConfig() {
        return this.config;
    }

    public void setConfig(List<SettingCopyrightVo> config) {
        this.config = config;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingCopyrightValidate)) {
            return false;
        }
        SettingCopyrightValidate other = (SettingCopyrightValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        List this$config = this.getConfig();
        List other$config = other.getConfig();
        return !(this$config == null ? other$config != null : !((Object)this$config).equals(other$config));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingCopyrightValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List $config = this.getConfig();
        result = result * 59 + ($config == null ? 43 : ((Object)$config).hashCode());
        return result;
    }

    public String toString() {
        return "SettingCopyrightValidate(config=" + this.getConfig() + ")";
    }
}

