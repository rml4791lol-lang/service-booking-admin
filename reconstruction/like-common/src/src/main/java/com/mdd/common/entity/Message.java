/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 */
package com.mdd.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

public class Message
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    private Integer id;
    private Integer type;
    private String title;
    private String content;
    private Integer senderId;
    private String senderType;
    private String senderName;
    private Integer receiverId;
    private String receiverType;
    private Integer isRead;
    private String extra;
    private Long createTime;
    private Long updateTime;
    private Long deleteTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getType() {
        return this.type;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public Integer getSenderId() {
        return this.senderId;
    }

    public String getSenderType() {
        return this.senderType;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public Integer getReceiverId() {
        return this.receiverId;
    }

    public String getReceiverType() {
        return this.receiverType;
    }

    public Integer getIsRead() {
        return this.isRead;
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

    public void setType(Integer type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
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
        if (!(o instanceof Message)) {
            return false;
        }
        Message other = (Message)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$type = this.getType();
        Integer other$type = other.getType();
        if (this$type == null ? other$type != null : !((Object)this$type).equals(other$type)) {
            return false;
        }
        Integer this$senderId = this.getSenderId();
        Integer other$senderId = other.getSenderId();
        if (this$senderId == null ? other$senderId != null : !((Object)this$senderId).equals(other$senderId)) {
            return false;
        }
        Integer this$receiverId = this.getReceiverId();
        Integer other$receiverId = other.getReceiverId();
        if (this$receiverId == null ? other$receiverId != null : !((Object)this$receiverId).equals(other$receiverId)) {
            return false;
        }
        Integer this$isRead = this.getIsRead();
        Integer other$isRead = other.getIsRead();
        if (this$isRead == null ? other$isRead != null : !((Object)this$isRead).equals(other$isRead)) {
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
        String this$senderType = this.getSenderType();
        String other$senderType = other.getSenderType();
        if (this$senderType == null ? other$senderType != null : !this$senderType.equals(other$senderType)) {
            return false;
        }
        String this$senderName = this.getSenderName();
        String other$senderName = other.getSenderName();
        if (this$senderName == null ? other$senderName != null : !this$senderName.equals(other$senderName)) {
            return false;
        }
        String this$receiverType = this.getReceiverType();
        String other$receiverType = other.getReceiverType();
        if (this$receiverType == null ? other$receiverType != null : !this$receiverType.equals(other$receiverType)) {
            return false;
        }
        String this$extra = this.getExtra();
        String other$extra = other.getExtra();
        return !(this$extra == null ? other$extra != null : !this$extra.equals(other$extra));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Message;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)$type).hashCode());
        Integer $senderId = this.getSenderId();
        result = result * 59 + ($senderId == null ? 43 : ((Object)$senderId).hashCode());
        Integer $receiverId = this.getReceiverId();
        result = result * 59 + ($receiverId == null ? 43 : ((Object)$receiverId).hashCode());
        Integer $isRead = this.getIsRead();
        result = result * 59 + ($isRead == null ? 43 : ((Object)$isRead).hashCode());
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
        String $senderType = this.getSenderType();
        result = result * 59 + ($senderType == null ? 43 : $senderType.hashCode());
        String $senderName = this.getSenderName();
        result = result * 59 + ($senderName == null ? 43 : $senderName.hashCode());
        String $receiverType = this.getReceiverType();
        result = result * 59 + ($receiverType == null ? 43 : $receiverType.hashCode());
        String $extra = this.getExtra();
        result = result * 59 + ($extra == null ? 43 : $extra.hashCode());
        return result;
    }

    public String toString() {
        return "Message(id=" + this.getId() + ", type=" + this.getType() + ", title=" + this.getTitle() + ", content=" + this.getContent() + ", senderId=" + this.getSenderId() + ", senderType=" + this.getSenderType() + ", senderName=" + this.getSenderName() + ", receiverId=" + this.getReceiverId() + ", receiverType=" + this.getReceiverType() + ", isRead=" + this.getIsRead() + ", extra=" + this.getExtra() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ")";
    }
}

