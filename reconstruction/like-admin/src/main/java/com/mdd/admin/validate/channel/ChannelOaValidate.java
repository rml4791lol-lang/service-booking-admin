/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.channel.ChannelOaValidate
 *  com.mdd.common.validator.annotation.IntegerContains
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.channel;

import com.mdd.common.validator.annotation.IntegerContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u516c\u4f17\u53f7\u6e20\u9053\u53c2\u6570")
public class ChannelOaValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @Length(max=100, message="\u5c0f\u7a0b\u5e8f\u540d\u79f0\u4e0d\u80fd\u8d85100\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u516c\u4f17\u53f7\u540d\u79f0")
    private @Length(max=100, message="\u5c0f\u7a0b\u5e8f\u540d\u79f0\u4e0d\u80fd\u8d85100\u4e2a\u5b57\u7b26") String name = "";
    @Length(max=100, message="\u539f\u59cbID\u4e0d\u80fd\u8d85100\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u539f\u59cbID")
    private @Length(max=100, message="\u539f\u59cbID\u4e0d\u80fd\u8d85100\u4e2a\u5b57\u7b26") String originalId = "";
    @Length(max=100, message="appId\u4e0d\u80fd\u8d85100\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="appId")
    private @Length(max=100, message="appId\u4e0d\u80fd\u8d85100\u4e2a\u5b57\u7b26") String appId = "";
    @Length(max=200, message="appSecret\u4e0d\u80fd\u8d85100\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="appSecret")
    private @Length(max=200, message="appSecret\u4e0d\u80fd\u8d85100\u4e2a\u5b57\u7b26") String appSecret = "";
    @Length(max=300, message="\u5c0f\u7a0b\u5e8f\u7801\u4e0d\u80fd\u8d85300\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u4e8c\u7ef4\u7801")
    private @Length(max=300, message="\u5c0f\u7a0b\u5e8f\u7801\u4e0d\u80fd\u8d85300\u4e2a\u5b57\u7b26") String qrCode = "";
    @Length(max=300, message="URL\u4e0d\u80fd\u8d85300\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="URL")
    private @Length(max=300, message="URL\u4e0d\u80fd\u8d85300\u4e2a\u5b57\u7b26") String url = "";
    @Length(max=200, message="Token\u4e0d\u80fd\u8d85200\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="Token")
    private @Length(max=200, message="Token\u4e0d\u80fd\u8d85200\u4e2a\u5b57\u7b26") String token = "";
    @Length(max=43, message="EncodingAESKey\u4e0d\u80fd\u8d8543\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="EncodingAESKey")
    private @Length(max=43, message="EncodingAESKey\u4e0d\u80fd\u8d8543\u4e2a\u5b57\u7b26") String encodingAesKey = "";
    @IntegerContains(values={1, 2, 3}, message="encryptionType\u4e0d\u662f\u5408\u6cd5\u503c")
    @ApiModelProperty(value="\u6d88\u606f\u52a0\u5bc6\u65b9\u5f0f")
    private Integer encryptionType = 1;

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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChannelOaValidate)) {
            return false;
        }
        ChannelOaValidate other = (ChannelOaValidate)o;
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
        return !(this$encodingAesKey == null ? other$encodingAesKey != null : !this$encodingAesKey.equals(other$encodingAesKey));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ChannelOaValidate;
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
        return result;
    }

    public String toString() {
        return "ChannelOaValidate(name=" + this.getName() + ", originalId=" + this.getOriginalId() + ", appId=" + this.getAppId() + ", appSecret=" + this.getAppSecret() + ", qrCode=" + this.getQrCode() + ", url=" + this.getUrl() + ", token=" + this.getToken() + ", encodingAesKey=" + this.getEncodingAesKey() + ", encryptionType=" + this.getEncryptionType() + ")";
    }
}

