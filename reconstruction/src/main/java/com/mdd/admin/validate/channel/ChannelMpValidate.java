/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.channel.ChannelMpValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.channel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u5c0f\u7a0b\u5e8f\u6e20\u9053\u53c2\u6570")
public class ChannelMpValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @Length(max=100, message="\u5c0f\u7a0b\u5e8f\u540d\u79f0\u4e0d\u80fd\u8d85100\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u5c0f\u7a0b\u5e8f\u540d\u79f0")
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
    // 以下域名由渠道页（admin-src views/channel/weapp.vue）提交/回显；此前 DTO 缺字段导致静默丢弃（task-7）
    @Length(max=300, message="\u8bf7\u6c42\u57df\u540d\u4e0d\u80fd\u8d85300\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u8bf7\u6c42\u57df\u540d")
    private String requestDomain = "";
    @Length(max=300, message="\u8bf7\u6c42\u57df\u540d\u4e0d\u80fd\u8d85300\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="socket \u57df\u540d")
    private String socketDomain = "";
    @Length(max=300, message="\u57df\u540d\u4e0d\u80fd\u8d85300\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u4e0a\u4f20\u6587\u4ef6\u57df\u540d")
    private String uploadFileDomain = "";
    @Length(max=300, message="\u57df\u540d\u4e0d\u80fd\u8d85300\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u4e0b\u8f7d\u6587\u4ef6\u57df\u540d")
    private String downloadFileDomain = "";
    @Length(max=300, message="\u57df\u540d\u4e0d\u80fd\u8d85300\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="UDP \u57df\u540d")
    private String udpDomain = "";
    @Length(max=300, message="\u57df\u540d\u4e0d\u80fd\u8d85300\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="TCP \u57df\u540d")
    private String tcpDomain = "";
    @Length(max=300, message="\u4e1a\u52a1\u57df\u540d\u4e0d\u80fd\u8d85300\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u4e1a\u52a1\u57df\u540d")
    private String businessDomain = "";

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


    public String getRequestDomain() {
        return this.requestDomain;
    }

    public String getSocketDomain() {
        return this.socketDomain;
    }

    public String getUploadFileDomain() {
        return this.uploadFileDomain;
    }

    public String getDownloadFileDomain() {
        return this.downloadFileDomain;
    }

    public String getUdpDomain() {
        return this.udpDomain;
    }

    public String getTcpDomain() {
        return this.tcpDomain;
    }

    public String getBusinessDomain() {
        return this.businessDomain;
    }

    public void setRequestDomain(String requestDomain) {
        this.requestDomain = requestDomain;
    }

    public void setSocketDomain(String socketDomain) {
        this.socketDomain = socketDomain;
    }

    public void setUploadFileDomain(String uploadFileDomain) {
        this.uploadFileDomain = uploadFileDomain;
    }

    public void setDownloadFileDomain(String downloadFileDomain) {
        this.downloadFileDomain = downloadFileDomain;
    }

    public void setUdpDomain(String udpDomain) {
        this.udpDomain = udpDomain;
    }

    public void setTcpDomain(String tcpDomain) {
        this.tcpDomain = tcpDomain;
    }

    public void setBusinessDomain(String businessDomain) {
        this.businessDomain = businessDomain;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChannelMpValidate)) {
            return false;
        }
        ChannelMpValidate other = (ChannelMpValidate)o;
        if (!other.canEqual((Object)this)) {
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
        return !(this$qrCode == null ? other$qrCode != null : !this$qrCode.equals(other$qrCode));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ChannelMpValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
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
        return result;
    }

    public String toString() {
        return "ChannelMpValidate(name=" + this.getName() + ", originalId=" + this.getOriginalId() + ", appId=" + this.getAppId() + ", appSecret=" + this.getAppSecret() + ", qrCode=" + this.getQrCode() + ")";
    }
}

