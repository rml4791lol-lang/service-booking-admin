/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.channel.ChannelRpValidate
 *  com.mdd.common.validator.annotation.IntegerContains
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.admin.validate.channel;

import com.mdd.common.validator.annotation.IntegerContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u516c\u4f17\u53f7\u9ed8\u8ba4\u56de\u590d\u53c2\u6570")
public class ChannelRpValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    @NotNull(message="name\u53c2\u6570\u7f3a\u5931")
    @NotEmpty(message="\u89c4\u5219\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u540d\u79f0", required=true)
    private @NotNull(message="name\u53c2\u6570\u7f3a\u5931") @NotEmpty(message="\u89c4\u5219\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a") String name;
    @NotNull(message="content\u53c2\u6570\u7f3a\u5931")
    @NotEmpty(message="\u56de\u590d\u5185\u5bb9\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u56de\u590d\u5185\u5bb9", required=true)
    private @NotNull(message="content\u53c2\u6570\u7f3a\u5931") @NotEmpty(message="\u56de\u590d\u5185\u5bb9\u4e0d\u80fd\u4e3a\u7a7a") String content;
    @NotNull(message="contentType\u53c2\u6570\u7f3a\u5931")
    @IntegerContains(values={1, 2}, message="contentType\u503c\u4e0d\u7b26\u5408\u89c4\u8303")
    @ApiModelProperty(value="\u5185\u5bb9\u7c7b\u578b", required=true)
    private @NotNull(message="contentType\u53c2\u6570\u7f3a\u5931") Integer contentType;
    @NotNull(message="replyType\u53c2\u6570\u7f3a\u5931")
    @IntegerContains(values={1, 2, 3}, message="replyType\u503c\u4e0d\u7b26\u5408\u89c4\u8303")
    @ApiModelProperty(value="\u56de\u590d\u8bcd\u7c7b\u578b", required=true)
    private @NotNull(message="replyType\u53c2\u6570\u7f3a\u5931") Integer replyType;
    @NotNull(message="status\u53c2\u6570\u7f3a\u5931")
    @IntegerContains(values={0, 1}, message="\u72b6\u6001\u9009\u62e9\u5f02\u5e38")
    @ApiModelProperty(value="\u72b6\u6001", required=true)
    private @NotNull(message="status\u53c2\u6570\u7f3a\u5931") Integer status;
    @ApiModelProperty(value="\u5173\u952e\u8bcd", required=true)
    private String keyword;
    @ApiModelProperty(value="\u6a21\u5f0f", required=true)
    private Integer matchingType;
    @NotNull(message="sort\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u6392\u5e8f", required=true)
    private @NotNull(message="sort\u53c2\u6570\u7f3a\u5931") Integer sort;

    public Integer getId() {
        return this.id;
    }

    @NotNull(message="name\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String getName() {
        return this.name;
    }

    @NotNull(message="content\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="content\u53c2\u6570\u7f3a\u5931") String getContent() {
        return this.content;
    }

    @NotNull(message="contentType\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="contentType\u53c2\u6570\u7f3a\u5931") Integer getContentType() {
        return this.contentType;
    }

    @NotNull(message="replyType\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="replyType\u53c2\u6570\u7f3a\u5931") Integer getReplyType() {
        return this.replyType;
    }

    @NotNull(message="status\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="status\u53c2\u6570\u7f3a\u5931") Integer getStatus() {
        return this.status;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public Integer getMatchingType() {
        return this.matchingType;
    }

    @NotNull(message="sort\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="sort\u53c2\u6570\u7f3a\u5931") Integer getSort() {
        return this.sort;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(@NotNull(message="name\u53c2\u6570\u7f3a\u5931") @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String name) {
        this.name = name;
    }

    public void setContent(@NotNull(message="content\u53c2\u6570\u7f3a\u5931") @NotNull(message="content\u53c2\u6570\u7f3a\u5931") String content) {
        this.content = content;
    }

    public void setContentType(@NotNull(message="contentType\u53c2\u6570\u7f3a\u5931") @NotNull(message="contentType\u53c2\u6570\u7f3a\u5931") Integer contentType) {
        this.contentType = contentType;
    }

    public void setReplyType(@NotNull(message="replyType\u53c2\u6570\u7f3a\u5931") @NotNull(message="replyType\u53c2\u6570\u7f3a\u5931") Integer replyType) {
        this.replyType = replyType;
    }

    public void setStatus(@NotNull(message="status\u53c2\u6570\u7f3a\u5931") @NotNull(message="status\u53c2\u6570\u7f3a\u5931") Integer status) {
        this.status = status;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setMatchingType(Integer matchingType) {
        this.matchingType = matchingType;
    }

    public void setSort(@NotNull(message="sort\u53c2\u6570\u7f3a\u5931") @NotNull(message="sort\u53c2\u6570\u7f3a\u5931") Integer sort) {
        this.sort = sort;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChannelRpValidate)) {
            return false;
        }
        ChannelRpValidate other = (ChannelRpValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$contentType = this.getContentType();
        Integer other$contentType = other.getContentType();
        if (this$contentType == null ? other$contentType != null : !((Object)this$contentType).equals(other$contentType)) {
            return false;
        }
        Integer this$replyType = this.getReplyType();
        Integer other$replyType = other.getReplyType();
        if (this$replyType == null ? other$replyType != null : !((Object)this$replyType).equals(other$replyType)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Integer this$matchingType = this.getMatchingType();
        Integer other$matchingType = other.getMatchingType();
        if (this$matchingType == null ? other$matchingType != null : !((Object)this$matchingType).equals(other$matchingType)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
            return false;
        }
        String this$keyword = this.getKeyword();
        String other$keyword = other.getKeyword();
        return !(this$keyword == null ? other$keyword != null : !this$keyword.equals(other$keyword));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ChannelRpValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $contentType = this.getContentType();
        result = result * 59 + ($contentType == null ? 43 : ((Object)$contentType).hashCode());
        Integer $replyType = this.getReplyType();
        result = result * 59 + ($replyType == null ? 43 : ((Object)$replyType).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $matchingType = this.getMatchingType();
        result = result * 59 + ($matchingType == null ? 43 : ((Object)$matchingType).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        String $keyword = this.getKeyword();
        result = result * 59 + ($keyword == null ? 43 : $keyword.hashCode());
        return result;
    }

    public String toString() {
        return "ChannelRpValidate(id=" + this.getId() + ", name=" + this.getName() + ", content=" + this.getContent() + ", contentType=" + this.getContentType() + ", replyType=" + this.getReplyType() + ", status=" + this.getStatus() + ", keyword=" + this.getKeyword() + ", matchingType=" + this.getMatchingType() + ", sort=" + this.getSort() + ")";
    }
}

