/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.vo.setting.SettingNoticeListedVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.setting;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u901a\u77e5\u8bbe\u7f6e\u5217\u8868Vo")
public class SettingNoticeListedVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u4e3b\u952e")
    private Integer id;
    @ApiModelProperty(value="\u901a\u77e5\u540d\u79f0")
    private String sceneName;
    @ApiModelProperty(value="\u901a\u77e5\u7c7b\u578b")
    private String type;
    @ApiModelProperty(value="\u901a\u77e5\u7c7b\u578b")
    private String typeDesc;
    @ApiModelProperty(value="\u901a\u77e5\u72b6\u6001")
    private String smsStatusDesc;
    @ApiModelProperty(value="\u901a\u77e5\u5bf9\u8c61")
    private JSONObject smsNotice;

    public Integer getId() {
        return this.id;
    }

    public String getSceneName() {
        return this.sceneName;
    }

    public String getType() {
        return this.type;
    }

    public String getTypeDesc() {
        return this.typeDesc;
    }

    public String getSmsStatusDesc() {
        return this.smsStatusDesc;
    }

    public JSONObject getSmsNotice() {
        return this.smsNotice;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public void setSmsStatusDesc(String smsStatusDesc) {
        this.smsStatusDesc = smsStatusDesc;
    }

    public void setSmsNotice(JSONObject smsNotice) {
        this.smsNotice = smsNotice;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingNoticeListedVo)) {
            return false;
        }
        SettingNoticeListedVo other = (SettingNoticeListedVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
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
        String this$typeDesc = this.getTypeDesc();
        String other$typeDesc = other.getTypeDesc();
        if (this$typeDesc == null ? other$typeDesc != null : !this$typeDesc.equals(other$typeDesc)) {
            return false;
        }
        String this$smsStatusDesc = this.getSmsStatusDesc();
        String other$smsStatusDesc = other.getSmsStatusDesc();
        if (this$smsStatusDesc == null ? other$smsStatusDesc != null : !this$smsStatusDesc.equals(other$smsStatusDesc)) {
            return false;
        }
        JSONObject this$smsNotice = this.getSmsNotice();
        JSONObject other$smsNotice = other.getSmsNotice();
        return !(this$smsNotice == null ? other$smsNotice != null : !this$smsNotice.equals(other$smsNotice));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingNoticeListedVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $sceneName = this.getSceneName();
        result = result * 59 + ($sceneName == null ? 43 : $sceneName.hashCode());
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        String $typeDesc = this.getTypeDesc();
        result = result * 59 + ($typeDesc == null ? 43 : $typeDesc.hashCode());
        String $smsStatusDesc = this.getSmsStatusDesc();
        result = result * 59 + ($smsStatusDesc == null ? 43 : $smsStatusDesc.hashCode());
        JSONObject $smsNotice = this.getSmsNotice();
        result = result * 59 + ($smsNotice == null ? 43 : $smsNotice.hashCode());
        return result;
    }

    public String toString() {
        return "SettingNoticeListedVo(id=" + this.getId() + ", sceneName=" + this.getSceneName() + ", type=" + this.getType() + ", typeDesc=" + this.getTypeDesc() + ", smsStatusDesc=" + this.getSmsStatusDesc() + ", smsNotice=" + this.getSmsNotice() + ")";
    }
}

