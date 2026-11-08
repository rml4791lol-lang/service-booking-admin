/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.setting.SettingSiteStatisticsValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u7ad9\u70b9\u7edf\u8ba1\u8bbe\u7f6e\u53c2\u6570")
public class SettingSiteStatisticsValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="clarityCode")
    private String clarityCode;

    public String getClarityCode() {
        return this.clarityCode;
    }

    public void setClarityCode(String clarityCode) {
        this.clarityCode = clarityCode;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingSiteStatisticsValidate)) {
            return false;
        }
        SettingSiteStatisticsValidate other = (SettingSiteStatisticsValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$clarityCode = this.getClarityCode();
        String other$clarityCode = other.getClarityCode();
        return !(this$clarityCode == null ? other$clarityCode != null : !this$clarityCode.equals(other$clarityCode));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingSiteStatisticsValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $clarityCode = this.getClarityCode();
        result = result * 59 + ($clarityCode == null ? 43 : $clarityCode.hashCode());
        return result;
    }

    public String toString() {
        return "SettingSiteStatisticsValidate(clarityCode=" + this.getClarityCode() + ")";
    }
}

