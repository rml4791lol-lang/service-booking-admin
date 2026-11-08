/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u516c\u4f17\u53f7\u56de\u590d\u5b9e\u4f53")
public class OfficialAccountReply
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u89c4\u5219\u540d")
    private String name;
    @ApiModelProperty(value="\u5173\u952e\u8bcd")
    private String keyword;
    @ApiModelProperty(value="\u56de\u590d\u7c7b\u578b: [1=\u5173\u6ce8\u56de\u590d 2=\u5173\u952e\u5b57\u56de\u590d, 3=\u9ed8\u8ba4\u56de\u590d]")
    private Integer replyType;
    @ApiModelProperty(value="\u5339\u914d\u65b9\u5f0f: [1=\u5168\u5339\u914d, 2=\u6a21\u7cca\u5339\u914d]")
    private Integer matchingType;
    @ApiModelProperty(value="\u5185\u5bb9\u7c7b\u578b: [1=\u6587\u672c]")
    private Integer contentType;
    @ApiModelProperty(value="\u542f\u52a8\u72b6\u6001: [1=\u542f\u52a8, 0=\u5173\u95ed]")
    private Integer status;
    @ApiModelProperty(value="\u56de\u590d\u5185\u5bb9")
    private String content;
    @ApiModelProperty(value="\u6392\u5e8f\u7f16\u53f7")
    private Integer sort;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Long updateTime;
    @ApiModelProperty(value="\u5220\u9664\u65f6\u95f4")
    private Long deleteTime;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public Integer getReplyType() {
        return this.replyType;
    }

    public Integer getMatchingType() {
        return this.matchingType;
    }

    public Integer getContentType() {
        return this.contentType;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getContent() {
        return this.content;
    }

    public Integer getSort() {
        return this.sort;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public Long getDeleteTime() {
        return this.deleteTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setReplyType(Integer replyType) {
        this.replyType = replyType;
    }

    public void setMatchingType(Integer matchingType) {
        this.matchingType = matchingType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleteTime(Long deleteTime) {
        this.deleteTime = deleteTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OfficialAccountReply)) {
            return false;
        }
        OfficialAccountReply other = (OfficialAccountReply)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$replyType = this.getReplyType();
        Integer other$replyType = other.getReplyType();
        if (this$replyType == null ? other$replyType != null : !((Object)this$replyType).equals(other$replyType)) {
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
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        Long this$createTime = this.getCreateTime();
        Long other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Long this$updateTime = this.getUpdateTime();
        Long other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        Long this$deleteTime = this.getDeleteTime();
        Long other$deleteTime = other.getDeleteTime();
        if (this$deleteTime == null ? other$deleteTime != null : !((Object)this$deleteTime).equals(other$deleteTime)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$keyword = this.getKeyword();
        String other$keyword = other.getKeyword();
        if (this$keyword == null ? other$keyword != null : !this$keyword.equals(other$keyword)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        return !(this$content == null ? other$content != null : !this$content.equals(other$content));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OfficialAccountReply;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $replyType = this.getReplyType();
        result = result * 59 + ($replyType == null ? 43 : ((Object)$replyType).hashCode());
        Integer $matchingType = this.getMatchingType();
        result = result * 59 + ($matchingType == null ? 43 : ((Object)$matchingType).hashCode());
        Integer $contentType = this.getContentType();
        result = result * 59 + ($contentType == null ? 43 : ((Object)$contentType).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $keyword = this.getKeyword();
        result = result * 59 + ($keyword == null ? 43 : $keyword.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        return result;
    }

    public String toString() {
        return "OfficialAccountReply(id=" + this.getId() + ", name=" + this.getName() + ", keyword=" + this.getKeyword() + ", replyType=" + this.getReplyType() + ", matchingType=" + this.getMatchingType() + ", contentType=" + this.getContentType() + ", status=" + this.getStatus() + ", content=" + this.getContent() + ", sort=" + this.getSort() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ")";
    }
}

