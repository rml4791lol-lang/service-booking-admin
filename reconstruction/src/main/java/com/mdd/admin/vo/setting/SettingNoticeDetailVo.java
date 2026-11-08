/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.vo.setting.SettingNoticeDetailVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.setting;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u901a\u77e5\u8bbe\u7f6e\u8be6\u60c5Vo")
public class SettingNoticeDetailVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="id")
    private Integer id;
    @ApiModelProperty(value="senceId")
    private Integer senceId;
    @ApiModelProperty(value="\u573a\u666f\u540d\u79f0")
    private String sceneName;
    @ApiModelProperty(value="\u901a\u77e5\u7c7b\u578b: [1=\u4e1a\u52a1, 2=\u9a8c\u8bc1]")
    private String type;
    @ApiModelProperty(value="\u573a\u666f\u63cf\u8ff0")
    private String sceneDesc;
    @ApiModelProperty(value="\u7cfb\u7edf\u7684\u901a\u77e5\u8bbe\u7f6e")
    private JSONObject systemNotice;
    @ApiModelProperty(value="\u516c\u4f17\u53f7\u901a\u77e5\u8bbe\u7f6e")
    private JSONObject oaNotice;
    @ApiModelProperty(value="\u5c0f\u7a0b\u5e8f\u901a\u77e5\u8bbe\u7f6e")
    private JSONObject mnpNotice;
    @ApiModelProperty(value="\u77ed\u4fe1\u7684\u901a\u77e5\u8bbe\u7f6e")
    private JSONObject smsNotice;

    public Integer getId() {
        return this.id;
    }

    public Integer getSenceId() {
        return this.senceId;
    }

    public String getSceneName() {
        return this.sceneName;
    }

    public String getType() {
        return this.type;
    }

    public String getSceneDesc() {
        return this.sceneDesc;
    }

    public JSONObject getSystemNotice() {
        return this.systemNotice;
    }

    public JSONObject getOaNotice() {
        return this.oaNotice;
    }

    public JSONObject getMnpNotice() {
        return this.mnpNotice;
    }

    public JSONObject getSmsNotice() {
        return this.smsNotice;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSenceId(Integer senceId) {
        this.senceId = senceId;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSceneDesc(String sceneDesc) {
        this.sceneDesc = sceneDesc;
    }

    public void setSystemNotice(JSONObject systemNotice) {
        this.systemNotice = systemNotice;
    }

    public void setOaNotice(JSONObject oaNotice) {
        this.oaNotice = oaNotice;
    }

    public void setMnpNotice(JSONObject mnpNotice) {
        this.mnpNotice = mnpNotice;
    }

    public void setSmsNotice(JSONObject smsNotice) {
        this.smsNotice = smsNotice;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingNoticeDetailVo)) {
            return false;
        }
        SettingNoticeDetailVo other = (SettingNoticeDetailVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$senceId = this.getSenceId();
        Integer other$senceId = other.getSenceId();
        if (this$senceId == null ? other$senceId != null : !((Object)this$senceId).equals(other$senceId)) {
            return false;
        }
        String this$sceneName = this.getSceneName();
        String other$sceneName = other.getSceneName();
        if (this$sceneName == null ? other$sceneName != null : !this$sceneName.equals(other$sceneName)) {
            return false;
        }
        String this$type = this.getType();
        String other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) {
            return false;
        }
        String this$sceneDesc = this.getSceneDesc();
        String other$sceneDesc = other.getSceneDesc();
        if (this$sceneDesc == null ? other$sceneDesc != null : !this$sceneDesc.equals(other$sceneDesc)) {
            return false;
        }
        JSONObject this$systemNotice = this.getSystemNotice();
        JSONObject other$systemNotice = other.getSystemNotice();
        if (this$systemNotice == null ? other$systemNotice != null : !this$systemNotice.equals(other$systemNotice)) {
            return false;
        }
        JSONObject this$oaNotice = this.getOaNotice();
        JSONObject other$oaNotice = other.getOaNotice();
        if (this$oaNotice == null ? other$oaNotice != null : !this$oaNotice.equals(other$oaNotice)) {
            return false;
        }
        JSONObject this$mnpNotice = this.getMnpNotice();
        JSONObject other$mnpNotice = other.getMnpNotice();
        if (this$mnpNotice == null ? other$mnpNotice != null : !this$mnpNotice.equals(other$mnpNotice)) {
            return false;
        }
        JSONObject this$smsNotice = this.getSmsNotice();
        JSONObject other$smsNotice = other.getSmsNotice();
        return !(this$smsNotice == null ? other$smsNotice != null : !this$smsNotice.equals(other$smsNotice));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingNoticeDetailVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $senceId = this.getSenceId();
        result = result * 59 + ($senceId == null ? 43 : ((Object)$senceId).hashCode());
        String $sceneName = this.getSceneName();
        result = result * 59 + ($sceneName == null ? 43 : $sceneName.hashCode());
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        String $sceneDesc = this.getSceneDesc();
        result = result * 59 + ($sceneDesc == null ? 43 : $sceneDesc.hashCode());
        JSONObject $systemNotice = this.getSystemNotice();
        result = result * 59 + ($systemNotice == null ? 43 : $systemNotice.hashCode());
        JSONObject $oaNotice = this.getOaNotice();
        result = result * 59 + ($oaNotice == null ? 43 : $oaNotice.hashCode());
        JSONObject $mnpNotice = this.getMnpNotice();
        result = result * 59 + ($mnpNotice == null ? 43 : $mnpNotice.hashCode());
        JSONObject $smsNotice = this.getSmsNotice();
        result = result * 59 + ($smsNotice == null ? 43 : $smsNotice.hashCode());
        return result;
    }

    public String toString() {
        return "SettingNoticeDetailVo(id=" + this.getId() + ", senceId=" + this.getSenceId() + ", sceneName=" + this.getSceneName() + ", type=" + this.getType() + ", sceneDesc=" + this.getSceneDesc() + ", systemNotice=" + this.getSystemNotice() + ", oaNotice=" + this.getOaNotice() + ", mnpNotice=" + this.getMnpNotice() + ", smsNotice=" + this.getSmsNotice() + ")";
    }
}

