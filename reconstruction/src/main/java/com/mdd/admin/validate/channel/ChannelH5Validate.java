/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.channel.ChannelH5Validate
 *  com.mdd.common.validator.annotation.IntegerContains
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.channel;

import com.mdd.common.validator.annotation.IntegerContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="H5\u6e20\u9053\u53c2\u6570")
public class ChannelH5Validate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="\u6e20\u9053\u5f00\u5173\u53c2\u6570\u7f3a\u5931")
    @IntegerContains(values={0, 1}, message="\u6e20\u9053\u5f00\u5173\u4e0d\u662f\u5408\u6cd5\u503c")
    @ApiModelProperty(value="\u6e20\u9053\u5f00\u5173", required=true)
    private @NotNull(message="\u6e20\u9053\u5f00\u5173\u53c2\u6570\u7f3a\u5931") Integer status;
    @NotNull(message="pageStatus\u53c2\u6570\u7f3a\u5931")
    @IntegerContains(values={0, 1}, message="\u9875\u9762\u72b6\u6001\u4e0d\u662f\u5408\u6cd5\u503c")
    @ApiModelProperty(value="\u72b6\u6001", required=true)
    private @NotNull(message="pageStatus\u53c2\u6570\u7f3a\u5931") Integer pageStatus;
    @Length(max=500, message="url\u4e0d\u80fd\u8d85500\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u5173\u95ed\u94fe\u63a5")
    private @Length(max=500, message="url\u4e0d\u80fd\u8d85500\u4e2a\u5b57\u7b26") String url;
    @Length(max=500, message="pageUrl\u4e0d\u80fd\u8d85500\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u81ea\u5b9a\u4e49\u94fe\u63a5")
    private @Length(max=500, message="pageUrl\u4e0d\u80fd\u8d85500\u4e2a\u5b57\u7b26") String pageUrl;

    @NotNull(message="\u6e20\u9053\u5f00\u5173\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="\u6e20\u9053\u5f00\u5173\u53c2\u6570\u7f3a\u5931") Integer getStatus() {
        return this.status;
    }

    @NotNull(message="pageStatus\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="pageStatus\u53c2\u6570\u7f3a\u5931") Integer getPageStatus() {
        return this.pageStatus;
    }

    public String getUrl() {
        return this.url;
    }

    public String getPageUrl() {
        return this.pageUrl;
    }

    public void setStatus(@NotNull(message="\u6e20\u9053\u5f00\u5173\u53c2\u6570\u7f3a\u5931") @NotNull(message="\u6e20\u9053\u5f00\u5173\u53c2\u6570\u7f3a\u5931") Integer status) {
        this.status = status;
    }

    public void setPageStatus(@NotNull(message="pageStatus\u53c2\u6570\u7f3a\u5931") @NotNull(message="pageStatus\u53c2\u6570\u7f3a\u5931") Integer pageStatus) {
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
        if (!(o instanceof ChannelH5Validate)) {
            return false;
        }
        ChannelH5Validate other = (ChannelH5Validate)o;
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
        return other instanceof ChannelH5Validate;
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
        return "ChannelH5Validate(status=" + this.getStatus() + ", pageStatus=" + this.getPageStatus() + ", url=" + this.getUrl() + ", pageUrl=" + this.getPageUrl() + ")";
    }
}

