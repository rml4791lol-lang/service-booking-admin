/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.setting.SettingAgreementValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u653f\u7b56\u534f\u8bae\u8bbe\u7f6e\u53c2\u6570")
public class SettingAgreementValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u670d\u52a1\u534f\u8bae\u6807\u9898")
    private String serviceTitle;
    @ApiModelProperty(value="\u670d\u52a1\u534f\u8bae\u5185\u5bb9")
    private String serviceContent;
    @ApiModelProperty(value="\u670d\u52a1\u534f\u8bae\u6807\u9898")
    private String privacyTitle;
    @ApiModelProperty(value="\u670d\u52a1\u534f\u8bae\u5185\u5bb9")
    private String privacyContent;

    public String getServiceTitle() {
        return this.serviceTitle;
    }

    public String getServiceContent() {
        return this.serviceContent;
    }

    public String getPrivacyTitle() {
        return this.privacyTitle;
    }

    public String getPrivacyContent() {
        return this.privacyContent;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public void setServiceContent(String serviceContent) {
        this.serviceContent = serviceContent;
    }

    public void setPrivacyTitle(String privacyTitle) {
        this.privacyTitle = privacyTitle;
    }

    public void setPrivacyContent(String privacyContent) {
        this.privacyContent = privacyContent;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingAgreementValidate)) {
            return false;
        }
        SettingAgreementValidate other = (SettingAgreementValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$serviceTitle = this.getServiceTitle();
        String other$serviceTitle = other.getServiceTitle();
        if (this$serviceTitle == null ? other$serviceTitle != null : !this$serviceTitle.equals(other$serviceTitle)) {
            return false;
        }
        String this$serviceContent = this.getServiceContent();
        String other$serviceContent = other.getServiceContent();
        if (this$serviceContent == null ? other$serviceContent != null : !this$serviceContent.equals(other$serviceContent)) {
            return false;
        }
        String this$privacyTitle = this.getPrivacyTitle();
        String other$privacyTitle = other.getPrivacyTitle();
        if (this$privacyTitle == null ? other$privacyTitle != null : !this$privacyTitle.equals(other$privacyTitle)) {
            return false;
        }
        String this$privacyContent = this.getPrivacyContent();
        String other$privacyContent = other.getPrivacyContent();
        return !(this$privacyContent == null ? other$privacyContent != null : !this$privacyContent.equals(other$privacyContent));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingAgreementValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $serviceTitle = this.getServiceTitle();
        result = result * 59 + ($serviceTitle == null ? 43 : $serviceTitle.hashCode());
        String $serviceContent = this.getServiceContent();
        result = result * 59 + ($serviceContent == null ? 43 : $serviceContent.hashCode());
        String $privacyTitle = this.getPrivacyTitle();
        result = result * 59 + ($privacyTitle == null ? 43 : $privacyTitle.hashCode());
        String $privacyContent = this.getPrivacyContent();
        result = result * 59 + ($privacyContent == null ? 43 : $privacyContent.hashCode());
        return result;
    }

    public String toString() {
        return "SettingAgreementValidate(serviceTitle=" + this.getServiceTitle() + ", serviceContent=" + this.getServiceContent() + ", privacyTitle=" + this.getPrivacyTitle() + ", privacyContent=" + this.getPrivacyContent() + ")";
    }
}

