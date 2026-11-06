/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.channel.ChannelRpVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.channel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u516c\u4f17\u53f7\u56de\u590dVo")
public class ChannelRpVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="id")
    private Integer id;
    @ApiModelProperty(value="\u89c4\u5219\u540d")
    private String name;
    @ApiModelProperty(value="\u56de\u590d\u5185\u5bb9")
    private String content;
    @ApiModelProperty(value="\u5173\u952e\u8bcd")
    private String keyword;
    @ApiModelProperty(value="\u5339\u914d\u65b9\u5f0f: [1=\u5168\u5339\u914d, 2=\u6a21\u7cca\u5339\u914d]")
    private Integer matchingType;
    @ApiModelProperty(value="\u5185\u5bb9\u7c7b\u578b: [1=\u6587\u672c]")
    private Integer contentType;
    @ApiModelProperty(value="\u6392\u5e8f\u7f16\u53f7")
    private Integer sort;
    @ApiModelProperty(value="\u542f\u52a8\u72b6\u6001: [1=\u542f\u52a8, 0=\u5173\u95ed]")
    private Integer status;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getContent() {
        return this.content;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public Integer getMatchingType() {
        return this.matchingType;
    }

    public Integer getContentType() {
        return this.contentType;
    }

    public Integer getSort() {
        return this.sort;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setMatchingType(Integer matchingType) {
        this.matchingType = matchingType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChannelRpVo)) {
            return false;
        }
        ChannelRpVo other = (ChannelRpVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$matchingType = this.getMatchingType();
        Integer other$matchingType = other.getMatchingType();
        if (this$matchingType == null ? other$matchingType != null : !((Object)this$matchingType).equals(other$matchingType)) {
            return false;
        }
        Integer this$contentType = this.getContentType();
        Integer other$contentType = other.getContentType();
        if (this$contentType == null ? other$contentType != null : !((Object)this$contentType).equals(other$contentType)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
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
        return other instanceof ChannelRpVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $matchingType = this.getMatchingType();
        result = result * 59 + ($matchingType == null ? 43 : ((Object)$matchingType).hashCode());
        Integer $contentType = this.getContentType();
        result = result * 59 + ($contentType == null ? 43 : ((Object)$contentType).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        String $keyword = this.getKeyword();
        result = result * 59 + ($keyword == null ? 43 : $keyword.hashCode());
        return result;
    }

    public String toString() {
        return "ChannelRpVo(id=" + this.getId() + ", name=" + this.getName() + ", content=" + this.getContent() + ", keyword=" + this.getKeyword() + ", matchingType=" + this.getMatchingType() + ", contentType=" + this.getContentType() + ", sort=" + this.getSort() + ", status=" + this.getStatus() + ")";
    }
}

