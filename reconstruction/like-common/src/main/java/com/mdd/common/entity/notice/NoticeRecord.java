/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableField
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.notice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u901a\u77e5\u8bb0\u5f55\u5b9e\u4f53")
public class NoticeRecord
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="\u4e3b\u952e")
    private Integer id;
    @ApiModelProperty(value="\u7528\u6237")
    private Integer userId;
    @ApiModelProperty(value="\u7f16\u7801")
    private String title;
    @ApiModelProperty(value="\u5185\u5bb9")
    private String content;
    @ApiModelProperty(value="\u573a\u666f")
    private Integer sceneId;
    @ApiModelProperty(value="\u5df2\u8bfb\u72b6\u6001: [0=\u672a\u8bfb, 1=\u5df2\u8bfb]")
    @TableField(value="`read`")
    private Integer read;
    @ApiModelProperty(value="\u901a\u77e5\u63a5\u6536\u5bf9\u8c61\u7c7b\u578b;1-\u4f1a\u5458;2-\u5546\u5bb6;3-\u5e73\u53f0;4-\u6e38\u5ba2(\u672a\u6ce8\u518c\u7528\u6237)")
    private Integer recipient;
    @ApiModelProperty(value="\u901a\u77e5\u53d1\u9001\u7c7b\u578b 1-\u7cfb\u7edf\u901a\u77e5 2-\u77ed\u4fe1\u901a\u77e5 3-\u5fae\u4fe1\u6a21\u677f 4-\u5fae\u4fe1\u5c0f\u7a0b\u5e8f")
    private Integer sendType;
    @ApiModelProperty(value="\u901a\u77e5\u7c7b\u578b 1-\u4e1a\u52a1\u901a\u77e5 2-\u9a8c\u8bc1\u7801")
    private Integer noticeType;
    @ApiModelProperty(value="\u5176\u4ed6")
    private String extra;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Long updateTime;
    @ApiModelProperty(value="\u5220\u9664\u65f6\u95f4")
    private Long deleteTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public Integer getSceneId() {
        return this.sceneId;
    }

    public Integer getRead() {
        return this.read;
    }

    public Integer getRecipient() {
        return this.recipient;
    }

    public Integer getSendType() {
        return this.sendType;
    }

    public Integer getNoticeType() {
        return this.noticeType;
    }

    public String getExtra() {
        return this.extra;
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

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public void setRead(Integer read) {
        this.read = read;
    }

    public void setRecipient(Integer recipient) {
        this.recipient = recipient;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }

    public void setExtra(String extra) {
        this.extra = extra;
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
        if (!(o instanceof NoticeRecord)) {
            return false;
        }
        NoticeRecord other = (NoticeRecord)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$sceneId = this.getSceneId();
        Integer other$sceneId = other.getSceneId();
        if (this$sceneId == null ? other$sceneId != null : !((Object)this$sceneId).equals(other$sceneId)) {
            return false;
        }
        Integer this$read = this.getRead();
        Integer other$read = other.getRead();
        if (this$read == null ? other$read != null : !((Object)this$read).equals(other$read)) {
            return false;
        }
        Integer this$recipient = this.getRecipient();
        Integer other$recipient = other.getRecipient();
        if (this$recipient == null ? other$recipient != null : !((Object)this$recipient).equals(other$recipient)) {
            return false;
        }
        Integer this$sendType = this.getSendType();
        Integer other$sendType = other.getSendType();
        if (this$sendType == null ? other$sendType != null : !((Object)this$sendType).equals(other$sendType)) {
            return false;
        }
        Integer this$noticeType = this.getNoticeType();
        Integer other$noticeType = other.getNoticeType();
        if (this$noticeType == null ? other$noticeType != null : !((Object)this$noticeType).equals(other$noticeType)) {
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
        String this$title = this.getTitle();
        String other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
            return false;
        }
        String this$extra = this.getExtra();
        String other$extra = other.getExtra();
        return !(this$extra == null ? other$extra != null : !this$extra.equals(other$extra));
    }

    protected boolean canEqual(Object other) {
        return other instanceof NoticeRecord;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $sceneId = this.getSceneId();
        result = result * 59 + ($sceneId == null ? 43 : ((Object)$sceneId).hashCode());
        Integer $read = this.getRead();
        result = result * 59 + ($read == null ? 43 : ((Object)$read).hashCode());
        Integer $recipient = this.getRecipient();
        result = result * 59 + ($recipient == null ? 43 : ((Object)$recipient).hashCode());
        Integer $sendType = this.getSendType();
        result = result * 59 + ($sendType == null ? 43 : ((Object)$sendType).hashCode());
        Integer $noticeType = this.getNoticeType();
        result = result * 59 + ($noticeType == null ? 43 : ((Object)$noticeType).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        String $extra = this.getExtra();
        result = result * 59 + ($extra == null ? 43 : $extra.hashCode());
        return result;
    }

    public String toString() {
        return "NoticeRecord(id=" + this.getId() + ", userId=" + this.getUserId() + ", title=" + this.getTitle() + ", content=" + this.getContent() + ", sceneId=" + this.getSceneId() + ", read=" + this.getRead() + ", recipient=" + this.getRecipient() + ", sendType=" + this.getSendType() + ", noticeType=" + this.getNoticeType() + ", extra=" + this.getExtra() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ")";
    }
}

