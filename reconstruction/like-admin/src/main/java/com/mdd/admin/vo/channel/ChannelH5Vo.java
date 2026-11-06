/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.channel.ChannelH5Vo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.channel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="H5\u6e20\u9053Vo")
public class ChannelH5Vo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u662f\u5426\u5173\u95ed")
    private Integer status;
    @ApiModelProperty(value="\u5173\u95ed\u7c7b\u578b")
    private Integer pageStatus;
    @ApiModelProperty(value="\u5173\u95ed\u8bbf\u95ee")
    private String url;
    @ApiModelProperty(value="\u6587\u4ef6\u9875\u9762\u5730\u5740")
    private String pageUrl;

    public Integer getStatus() {
        return this.status;
    }

    public Integer getPageStatus() {
        return this.pageStatus;
    }

    public String getUrl() {
        return this.url;
    }

    public String getPageUrl() {
        return this.pageUrl;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setPageStatus(Integer pageStatus) {
        this.pageStatus = pageStatus;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChannelH5Vo)) {
            return false;
        }
        ChannelH5Vo other = (ChannelH5Vo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Integer this$pageStatus = this.getPageStatus();
        Integer other$pageStatus = other.getPageStatus();
        if (this$pageStatus == null ? other$pageStatus != null : !((Object)this$pageStatus).equals(other$pageStatus)) {
            return false;
        }
        String this$url = this.getUrl();
        String other$url = other.getUrl();
        if (this$url == null ? other$url != null : !this$url.equals(other$url)) {
            return false;
        }
        String this$pageUrl = this.getPageUrl();
        String other$pageUrl = other.getPageUrl();
        return !(this$pageUrl == null ? other$pageUrl != null : !this$pageUrl.equals(other$pageUrl));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ChannelH5Vo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $pageStatus = this.getPageStatus();
        result = result * 59 + ($pageStatus == null ? 43 : ((Object)$pageStatus).hashCode());
        String $url = this.getUrl();
        result = result * 59 + ($url == null ? 43 : $url.hashCode());
        String $pageUrl = this.getPageUrl();
        result = result * 59 + ($pageUrl == null ? 43 : $pageUrl.hashCode());
        return result;
    }

    public String toString() {
        return "ChannelH5Vo(status=" + this.getStatus() + ", pageStatus=" + this.getPageStatus() + ", url=" + this.getUrl() + ", pageUrl=" + this.getPageUrl() + ")";
    }
}

