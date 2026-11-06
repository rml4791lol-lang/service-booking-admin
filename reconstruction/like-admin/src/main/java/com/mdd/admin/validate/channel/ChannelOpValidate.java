/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.channel.ChannelOpValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.channel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u5f00\u53d1\u5e73\u53f0\u6e20\u9053\u53c2\u6570")
public class ChannelOpValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="appId")
    private String appId;
    @ApiModelProperty(value="appSecret")
    private String appSecret;

    public String getAppId() {
        return this.appId;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChannelOpValidate)) {
            return false;
        }
        ChannelOpValidate other = (ChannelOpValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$appId = this.getAppId();
        String other$appId = other.getAppId();
        if (this$appId == null ? other$appId != null : !this$appId.equals(other$appId)) {
            return false;
        }
        String this$appSecret = this.getAppSecret();
        String other$appSecret = other.getAppSecret();
        return !(this$appSecret == null ? other$appSecret != null : !this$appSecret.equals(other$appSecret));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ChannelOpValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $appId = this.getAppId();
        result = result * 59 + ($appId == null ? 43 : $appId.hashCode());
        String $appSecret = this.getAppSecret();
        result = result * 59 + ($appSecret == null ? 43 : $appSecret.hashCode());
        return result;
    }

    public String toString() {
        return "ChannelOpValidate(appId=" + this.getAppId() + ", appSecret=" + this.getAppSecret() + ")";
    }
}

