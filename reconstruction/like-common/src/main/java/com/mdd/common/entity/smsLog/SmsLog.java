/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.smsLog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u77ed\u4fe1\u8bb0\u5f55\u5b9e\u4f53")
public class SmsLog
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="\u4e3b\u952e")
    private Integer id;
    @ApiModelProperty(value="\u573a\u666f")
    private Integer sceneId;
    @ApiModelProperty(value="\u624b\u673a\u53f7\u7801")
    private String mobile;
    @ApiModelProperty(value="\u53d1\u9001\u5185\u5bb9")
    private String content;
    @ApiModelProperty(value="\u53d1\u9001\u5173\u952e\u5b57\uff08\u6ce8\u518c\u3001\u627e\u56de\u5bc6\u7801\uff09")
    private String code;
    @ApiModelProperty(value="\u662f\u5426\u5df2\u9a8c\u8bc1\uff1b0-\u5426\uff1b1-\u662f")
    private Integer isVerify;
    @ApiModelProperty(value="\u9a8c\u8bc1\u6b21\u6570")
    private Integer checkNum;
    @ApiModelProperty(value="\u53d1\u9001\u72b6\u6001\uff1a0-\u53d1\u9001\u4e2d\uff1b1-\u53d1\u9001\u6210\u529f\uff1b2-\u53d1\u9001\u5931\u8d25")
    private Integer sendStatus;
    @ApiModelProperty(value="\u53d1\u9001\u65f6\u95f4")
    private Long sendTime;
    @ApiModelProperty(value="\u77ed\u4fe1\u7ed3\u679c")
    private String results;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Long updateTime;
    @ApiModelProperty(value="\u5220\u9664\u65f6\u95f4")
    private Long deleteTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getSceneId() {
        return this.sceneId;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getContent() {
        return this.content;
    }

    public String getCode() {
        return this.code;
    }

    public Integer getIsVerify() {
        return this.isVerify;
    }

    public Integer getCheckNum() {
        return this.checkNum;
    }

    public Integer getSendStatus() {
        return this.sendStatus;
    }

    public Long getSendTime() {
        return this.sendTime;
    }

    public String getResults() {
        return this.results;
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

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setIsVerify(Integer isVerify) {
        this.isVerify = isVerify;
    }

    public void setCheckNum(Integer checkNum) {
        this.checkNum = checkNum;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public void setResults(String results) {
        this.results = results;
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
        if (!(o instanceof SmsLog)) {
            return false;
        }
        SmsLog other = (SmsLog)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$sceneId = this.getSceneId();
        Integer other$sceneId = other.getSceneId();
        if (this$sceneId == null ? other$sceneId != null : !((Object)this$sceneId).equals(other$sceneId)) {
            return false;
        }
        Integer this$isVerify = this.getIsVerify();
        Integer other$isVerify = other.getIsVerify();
        if (this$isVerify == null ? other$isVerify != null : !((Object)this$isVerify).equals(other$isVerify)) {
            return false;
        }
        Integer this$checkNum = this.getCheckNum();
        Integer other$checkNum = other.getCheckNum();
        if (this$checkNum == null ? other$checkNum != null : !((Object)this$checkNum).equals(other$checkNum)) {
            return false;
        }
        Integer this$sendStatus = this.getSendStatus();
        Integer other$sendStatus = other.getSendStatus();
        if (this$sendStatus == null ? other$sendStatus != null : !((Object)this$sendStatus).equals(other$sendStatus)) {
            return false;
        }
        Long this$sendTime = this.getSendTime();
        Long other$sendTime = other.getSendTime();
        if (this$sendTime == null ? other$sendTime != null : !((Object)this$sendTime).equals(other$sendTime)) {
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
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        if (this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) {
            return false;
        }
        String this$results = this.getResults();
        String other$results = other.getResults();
        return !(this$results == null ? other$results != null : !this$results.equals(other$results));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SmsLog;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $sceneId = this.getSceneId();
        result = result * 59 + ($sceneId == null ? 43 : ((Object)$sceneId).hashCode());
        Integer $isVerify = this.getIsVerify();
        result = result * 59 + ($isVerify == null ? 43 : ((Object)$isVerify).hashCode());
        Integer $checkNum = this.getCheckNum();
        result = result * 59 + ($checkNum == null ? 43 : ((Object)$checkNum).hashCode());
        Integer $sendStatus = this.getSendStatus();
        result = result * 59 + ($sendStatus == null ? 43 : ((Object)$sendStatus).hashCode());
        Long $sendTime = this.getSendTime();
        result = result * 59 + ($sendTime == null ? 43 : ((Object)$sendTime).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        String $results = this.getResults();
        result = result * 59 + ($results == null ? 43 : $results.hashCode());
        return result;
    }

    public String toString() {
        return "SmsLog(id=" + this.getId() + ", sceneId=" + this.getSceneId() + ", mobile=" + this.getMobile() + ", content=" + this.getContent() + ", code=" + this.getCode() + ", isVerify=" + this.getIsVerify() + ", checkNum=" + this.getCheckNum() + ", sendStatus=" + this.getSendStatus() + ", sendTime=" + this.getSendTime() + ", results=" + this.getResults() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ")";
    }
}

