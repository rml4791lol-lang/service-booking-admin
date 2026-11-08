/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.channel.ChannelOaVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.channel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u516c\u4f17\u53f7\u6e20\u9053Vo")
public class ChannelOaVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u516c\u4f17\u53f7\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u539f\u59cbID")
    private String originalId;
    @ApiModelProperty(value="appId")
    private String appId;
    @ApiModelProperty(value="appSecret")
    private String appSecret;
    @ApiModelProperty(value="\u4e8c\u7ef4\u7801")
    private String qrCode;
    @ApiModelProperty(value="URL")
    private String url;
    @ApiModelProperty(value="Token")
    private String token;
    @ApiModelProperty(value="EncodingAESKey")
    private String encodingAesKey;
    @ApiModelProperty(value="\u6d88\u606f\u52a0\u5bc6\u65b9\u5f0f")
    private Integer encryptionType;
    @ApiModelProperty(value="\u4e1a\u52a1\u57df\u540d")
    private String businessDomain;
    @ApiModelProperty(value="JS\u63a5\u53e3\u5b89\u5168\u57df\u540d")
    private String jsSecureDomain;
    @ApiModelProperty(value="\u7f51\u9875\u6388\u6743\u57df\u540d")
    private String webAuthDomain;

    public String getName() {
        return this.name;
    }

    public String getOriginalId() {
        return this.originalId;
    }

    public String getAppId() {
        return this.appId;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public String getQrCode() {
        return this.qrCode;
    }

    public String getUrl() {
        return this.url;
    }

    public String getToken() {
        return this.token;
    }

    public String getEncodingAesKey() {
        return this.encodingAesKey;
    }

    public Integer getEncryptionType() {
        return this.encryptionType;
    }

    public String getBusinessDomain() {
        return this.businessDomain;
    }

    public String getJsSecureDomain() {
        return this.jsSecureDomain;
    }

    public String getWebAuthDomain() {
        return this.webAuthDomain;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEncodingAesKey(String encodingAesKey) {
        this.encodingAesKey = encodingAesKey;
    }

    public void setEncryptionType(Integer encryptionType) {
        this.encryptionType = encryptionType;
    }

    public void setBusinessDomain(String businessDomain) {
        this.businessDomain = businessDomain;
    }

    public void setJsSecureDomain(String jsSecureDomain) {
        this.jsSecureDomain = jsSecureDomain;
    }

    public void setWebAuthDomain(String webAuthDomain) {
        this.webAuthDomain = webAuthDomain;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChannelOaVo)) {
            return false;
        }
        ChannelOaVo other = (ChannelOaVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$encryptionType = this.getEncryptionType();
        Integer other$encryptionType = other.getEncryptionType();
        if (this$encryptionType == null ? other$encryptionType != null : !((Object)this$encryptionType).equals(other$encryptionType)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$originalId = this.getOriginalId();
        String other$originalId = other.getOriginalId();
        if (this$originalId == null ? other$originalId != null : !this$originalId.equals(other$originalId)) {
            return false;
        }
        String this$appId = this.getAppId();
        String other$appId = other.getAppId();
        if (this$appId == null ? other$appId != null : !this$appId.equals(other$appId)) {
            return false;
        }
        String this$appSecret = this.getAppSecret();
        String other$appSecret = other.getAppSecret();
        if (this$appSecret == null ? other$appSecret != null : !this$appSecret.equals(other$appSecret)) {
            return false;
        }
        String this$qrCode = this.getQrCode();
        String other$qrCode = other.getQrCode();
        if (this$qrCode == null ? other$qrCode != null : !this$qrCode.equals(other$qrCode)) {
            return false;
        }
        String this$url = this.getUrl();
        String other$url = other.getUrl();
        if (this$url == null ? other$url != null : !this$url.equals(other$url)) {
            return false;
        }
        String this$token = this.getToken();
        String other$token = other.getToken();
        if (this$token == null ? other$token != null : !this$token.equals(other$token)) {
            return false;
        }
        String this$encodingAesKey = this.getEncodingAesKey();
        String other$encodingAesKey = other.getEncodingAesKey();
        if (this$encodingAesKey == null ? other$encodingAesKey != null : !this$encodingAesKey.equals(other$encodingAesKey)) {
            return false;
        }
        String this$businessDomain = this.getBusinessDomain();
        String other$businessDomain = other.getBusinessDomain();
        if (this$businessDomain == null ? other$businessDomain != null : !this$businessDomain.equals(other$businessDomain)) {
            return false;
        }
        String this$jsSecureDomain = this.getJsSecureDomain();
        String other$jsSecureDomain = other.getJsSecureDomain();
        if (this$jsSecureDomain == null ? other$jsSecureDomain != null : !this$jsSecureDomain.equals(other$jsSecureDomain)) {
            return false;
        }
        String this$webAuthDomain = this.getWebAuthDomain();
        String other$webAuthDomain = other.getWebAuthDomain();
        return !(this$webAuthDomain == null ? other$webAuthDomain != null : !this$webAuthDomain.equals(other$webAuthDomain));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ChannelOaVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $encryptionType = this.getEncryptionType();
        result = result * 59 + ($encryptionType == null ? 43 : ((Object)$encryptionType).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $originalId = this.getOriginalId();
        result = result * 59 + ($originalId == null ? 43 : $originalId.hashCode());
        String $appId = this.getAppId();
        result = result * 59 + ($appId == null ? 43 : $appId.hashCode());
        String $appSecret = this.getAppSecret();
        result = result * 59 + ($appSecret == null ? 43 : $appSecret.hashCode());
        String $qrCode = this.getQrCode();
        result = result * 59 + ($qrCode == null ? 43 : $qrCode.hashCode());
        String $url = this.getUrl();
        result = result * 59 + ($url == null ? 43 : $url.hashCode());
        String $token = this.getToken();
        result = result * 59 + ($token == null ? 43 : $token.hashCode());
        String $encodingAesKey = this.getEncodingAesKey();
        result = result * 59 + ($encodingAesKey == null ? 43 : $encodingAesKey.hashCode());
        String $businessDomain = this.getBusinessDomain();
        result = result * 59 + ($businessDomain == null ? 43 : $businessDomain.hashCode());
        String $jsSecureDomain = this.getJsSecureDomain();
        result = result * 59 + ($jsSecureDomain == null ? 43 : $jsSecureDomain.hashCode());
        String $webAuthDomain = this.getWebAuthDomain();
        result = result * 59 + ($webAuthDomain == null ? 43 : $webAuthDomain.hashCode());
        return result;
    }

    public String toString() {
        return "ChannelOaVo(name=" + this.getName() + ", originalId=" + this.getOriginalId() + ", appId=" + this.getAppId() + ", appSecret=" + this.getAppSecret() + ", qrCode=" + this.getQrCode() + ", url=" + this.getUrl() + ", token=" + this.getToken() + ", encodingAesKey=" + this.getEncodingAesKey() + ", encryptionType=" + this.getEncryptionType() + ", businessDomain=" + this.getBusinessDomain() + ", jsSecureDomain=" + this.getJsSecureDomain() + ", webAuthDomain=" + this.getWebAuthDomain() + ")";
    }
}

