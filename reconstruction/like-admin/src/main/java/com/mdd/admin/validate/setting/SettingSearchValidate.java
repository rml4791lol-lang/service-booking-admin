/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.setting.SettingSearchValidate
 *  com.mdd.admin.vo.setting.SettingSearchObjectVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.setting;

import com.mdd.admin.vo.setting.SettingSearchObjectVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value="\u70ed\u95e8\u641c\u7d22\u8bbe\u7f6e\u53c2\u6570")
public class SettingSearchValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u662f\u5426\u5f00\u542f\u70ed\u95e8\u641c\u7d22")
    private Integer status;
    @ApiModelProperty(value="\u5173\u952e\u8bcd\u5217\u8868")
    private List<SettingSearchObjectVo> data;

    public Integer getStatus() {
        return this.status;
    }

    public List<SettingSearchObjectVo> getData() {
        return this.data;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setData(List<SettingSearchObjectVo> data) {
        this.data = data;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingSearchValidate)) {
            return false;
        }
        SettingSearchValidate other = (SettingSearchValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        List this$data = this.getData();
        List other$data = other.getData();
        return !(this$data == null ? other$data != null : !((Object)this$data).equals(other$data));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingSearchValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        List $data = this.getData();
        result = result * 59 + ($data == null ? 43 : ((Object)$data).hashCode());
        return result;
    }

    public String toString() {
        return "SettingSearchValidate(status=" + this.getStatus() + ", data=" + this.getData() + ")";
    }
}

