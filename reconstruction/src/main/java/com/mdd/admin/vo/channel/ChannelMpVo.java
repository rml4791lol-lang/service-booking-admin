/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.channel.ChannelMpVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.channel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u5c0f\u7a0b\u5e8f\u6e20\u9053Vo")
public class ChannelMpVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u5c0f\u7a0b\u5e8f\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u539f\u59cbID")
    private String originalId;
    @ApiModelProperty(value="appId")
    private String appId;
    @ApiModelProperty(value="appSecret")
    private String appSecret;
    @ApiModelProperty(value="\u4e8c\u7ef4\u7801")
    private String qrCode;
    @ApiModelProperty(value="request\u5408\u6cd5\u57df\u540d")
    private String requestDomain;
    @ApiModelProperty(value="socket\u5408\u6cd5\u57df\u540d")
    private String socketDomain;
    @ApiModelProperty(value="uploadFile\u5408\u6cd5\u57df\u540d")
    private String uploadFileDomain;
    @ApiModelProperty(value="downloadFile\u5408\u6cd5\u57df\u540d")
    private String downloadFileDomain;
    @ApiModelProperty(value="udp\u5408\u6cd5\u57df\u540d")
    private String udpDomain;
    @ApiModelProperty(value="tcp\u5408\u6cd5\u57df\u540d")
    private String tcpDomain;
    @ApiModelProperty(value="\u4e1a\u52a1\u57df\u540d")
    private String businessDomain;

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
        if (!(o instanceof ChannelMpVo)) {
            return false;
        }
        ChannelMpVo other = (ChannelMpVo)o;
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
        if (this$qrCode == null ? other$qrCode != null : !this$qrCode.equals(other$qrCode)) {
            return false;
        }
        String this$requestDomain = this.getRequestDomain();
        String other$requestDomain = other.getRequestDomain();
        if (this$requestDomain == null ? other$requestDomain != null : !this$requestDomain.equals(other$requestDomain)) {
            return false;
        }
        String this$socketDomain = this.getSocketDomain();
        String other$socketDomain = other.getSocketDomain();
        if (this$socketDomain == null ? other$socketDomain != null : !this$socketDomain.equals(other$socketDomain)) {
            return false;
        }
        String this$uploadFileDomain = this.getUploadFileDomain();
        String other$uploadFileDomain = other.getUploadFileDomain();
        if (this$uploadFileDomain == null ? other$uploadFileDomain != null : !this$uploadFileDomain.equals(other$uploadFileDomain)) {
            return false;
        }
        String this$downloadFileDomain = this.getDownloadFileDomain();
        String other$downloadFileDomain = other.getDownloadFileDomain();
        if (this$downloadFileDomain == null ? other$downloadFileDomain != null : !this$downloadFileDomain.equals(other$downloadFileDomain)) {
            return false;
        }
        String this$udpDomain = this.getUdpDomain();
        String other$udpDomain = other.getUdpDomain();
        if (this$udpDomain == null ? other$udpDomain != null : !this$udpDomain.equals(other$udpDomain)) {
            return false;
        }
        String this$tcpDomain = this.getTcpDomain();
        String other$tcpDomain = other.getTcpDomain();
        if (this$tcpDomain == null ? other$tcpDomain != null : !this$tcpDomain.equals(other$tcpDomain)) {
            return false;
        }
        String this$businessDomain = this.getBusinessDomain();
        String other$businessDomain = other.getBusinessDomain();
        return !(this$businessDomain == null ? other$businessDomain != null : !this$businessDomain.equals(other$businessDomain));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ChannelMpVo;
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
        String $requestDomain = this.getRequestDomain();
        result = result * 59 + ($requestDomain == null ? 43 : $requestDomain.hashCode());
        String $socketDomain = this.getSocketDomain();
        result = result * 59 + ($socketDomain == null ? 43 : $socketDomain.hashCode());
        String $uploadFileDomain = this.getUploadFileDomain();
        result = result * 59 + ($uploadFileDomain == null ? 43 : $uploadFileDomain.hashCode());
        String $downloadFileDomain = this.getDownloadFileDomain();
        result = result * 59 + ($downloadFileDomain == null ? 43 : $downloadFileDomain.hashCode());
        String $udpDomain = this.getUdpDomain();
        result = result * 59 + ($udpDomain == null ? 43 : $udpDomain.hashCode());
        String $tcpDomain = this.getTcpDomain();
        result = result * 59 + ($tcpDomain == null ? 43 : $tcpDomain.hashCode());
        String $businessDomain = this.getBusinessDomain();
        result = result * 59 + ($businessDomain == null ? 43 : $businessDomain.hashCode());
        return result;
    }

    public String toString() {
        return "ChannelMpVo(name=" + this.getName() + ", originalId=" + this.getOriginalId() + ", appId=" + this.getAppId() + ", appSecret=" + this.getAppSecret() + ", qrCode=" + this.getQrCode() + ", requestDomain=" + this.getRequestDomain() + ", socketDomain=" + this.getSocketDomain() + ", uploadFileDomain=" + this.getUploadFileDomain() + ", downloadFileDomain=" + this.getDownloadFileDomain() + ", udpDomain=" + this.getUdpDomain() + ", tcpDomain=" + this.getTcpDomain() + ", businessDomain=" + this.getBusinessDomain() + ")";
    }
}

