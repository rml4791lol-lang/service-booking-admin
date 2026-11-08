/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.notice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u901a\u77e5\u8bbe\u7f6e\u5b9e\u4f53")
public class NoticeSetting
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u573a\u666f\u7f16\u53f7")
    private Integer sceneId;
    @ApiModelProperty(value="\u573a\u666f\u540d\u79f0")
    private String sceneName;
    @ApiModelProperty(value="\u573a\u666f\u63cf\u8ff0")
    private String sceneDesc;
    @ApiModelProperty(value="\u63a5\u6536\u4eba\u5458: [1=\u7528\u6237, 2=\u5e73\u53f0]")
    private Integer recipient;
    @ApiModelProperty(value="\u901a\u77e5\u7c7b\u578b: [1=\u4e1a\u52a1, 2=\u9a8c\u8bc1\u7801]")
    private Integer type;
    @ApiModelProperty(value="\u7cfb\u7edf\u7684\u901a\u77e5\u8bbe\u7f6e")
    private String systemNotice;
    @ApiModelProperty(value="\u77ed\u4fe1\u7684\u901a\u77e5\u8bbe\u7f6e")
    private String smsNotice;
    @ApiModelProperty(value="\u516c\u4f17\u53f7\u901a\u77e5\u8bbe\u7f6e")
    private String oaNotice;
    @ApiModelProperty(value="\u5c0f\u7a0b\u5e8f\u901a\u77e5\u8bbe\u7f6e")
    private String mnpNotice;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Long updateTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getSceneId() {
        return this.sceneId;
    }

    public String getSceneName() {
        return this.sceneName;
    }

    public String getSceneDesc() {
        return this.sceneDesc;
    }

    public Integer getRecipient() {
        return this.recipient;
    }

    public Integer getType() {
        return this.type;
    }

    public String getSystemNotice() {
        return this.systemNotice;
    }

    public String getSmsNotice() {
        return this.smsNotice;
    }

    public String getOaNotice() {
        return this.oaNotice;
    }

    public String getMnpNotice() {
        return this.mnpNotice;
    }

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public void setSceneDesc(String sceneDesc) {
        this.sceneDesc = sceneDesc;
    }

    public void setRecipient(Integer recipient) {
        this.recipient = recipient;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setSystemNotice(String systemNotice) {
        this.systemNotice = systemNotice;
    }

    public void setSmsNotice(String smsNotice) {
        this.smsNotice = smsNotice;
    }

    public void setOaNotice(String oaNotice) {
        this.oaNotice = oaNotice;
    }

    public void setMnpNotice(String mnpNotice) {
        this.mnpNotice = mnpNotice;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NoticeSetting)) {
            return false;
        }
        NoticeSetting other = (NoticeSetting)o;
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
        Integer this$recipient = this.getRecipient();
        Integer other$recipient = other.getRecipient();
        if (this$recipient == null ? other$recipient != null : !((Object)this$recipient).equals(other$recipient)) {
            return false;
        }
        Integer this$type = this.getType();
        Integer other$type = other.getType();
        if (this$type == null ? other$type != null : !((Object)this$type).equals(other$type)) {
            return false;
        }
        Long this$updateTime = this.getUpdateTime();
        Long other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        String this$sceneName = this.getSceneName();
        String other$sceneName = other.getSceneName();
        if (this$sceneName == null ? other$sceneName != null : !this$sceneName.equals(other$sceneName)) {
            return false;
        }
        String this$sceneDesc = this.getSceneDesc();
        String other$sceneDesc = other.getSceneDesc();
        if (this$sceneDesc == null ? other$sceneDesc != null : !this$sceneDesc.equals(other$sceneDesc)) {
            return false;
        }
        String this$systemNotice = this.getSystemNotice();
        String other$systemNotice = other.getSystemNotice();
        if (this$systemNotice == null ? other$systemNotice != null : !this$systemNotice.equals(other$systemNotice)) {
            return false;
        }
        String this$smsNotice = this.getSmsNotice();
        String other$smsNotice = other.getSmsNotice();
        if (this$smsNotice == null ? other$smsNotice != null : !this$smsNotice.equals(other$smsNotice)) {
            return false;
        }
        String this$oaNotice = this.getOaNotice();
        String other$oaNotice = other.getOaNotice();
        if (this$oaNotice == null ? other$oaNotice != null : !this$oaNotice.equals(other$oaNotice)) {
            return false;
        }
        String this$mnpNotice = this.getMnpNotice();
        String other$mnpNotice = other.getMnpNotice();
        return !(this$mnpNotice == null ? other$mnpNotice != null : !this$mnpNotice.equals(other$mnpNotice));
    }

    protected boolean canEqual(Object other) {
        return other instanceof NoticeSetting;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $sceneId = this.getSceneId();
        result = result * 59 + ($sceneId == null ? 43 : ((Object)$sceneId).hashCode());
        Integer $recipient = this.getRecipient();
        result = result * 59 + ($recipient == null ? 43 : ((Object)$recipient).hashCode());
        Integer $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)$type).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $sceneName = this.getSceneName();
        result = result * 59 + ($sceneName == null ? 43 : $sceneName.hashCode());
        String $sceneDesc = this.getSceneDesc();
        result = result * 59 + ($sceneDesc == null ? 43 : $sceneDesc.hashCode());
        String $systemNotice = this.getSystemNotice();
        result = result * 59 + ($systemNotice == null ? 43 : $systemNotice.hashCode());
        String $smsNotice = this.getSmsNotice();
        result = result * 59 + ($smsNotice == null ? 43 : $smsNotice.hashCode());
        String $oaNotice = this.getOaNotice();
        result = result * 59 + ($oaNotice == null ? 43 : $oaNotice.hashCode());
        String $mnpNotice = this.getMnpNotice();
        result = result * 59 + ($mnpNotice == null ? 43 : $mnpNotice.hashCode());
        return result;
    }

    public String toString() {
        return "NoticeSetting(id=" + this.getId() + ", sceneId=" + this.getSceneId() + ", sceneName=" + this.getSceneName() + ", sceneDesc=" + this.getSceneDesc() + ", recipient=" + this.getRecipient() + ", type=" + this.getType() + ", systemNotice=" + this.getSystemNotice() + ", smsNotice=" + this.getSmsNotice() + ", oaNotice=" + this.getOaNotice() + ", mnpNotice=" + this.getMnpNotice() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

