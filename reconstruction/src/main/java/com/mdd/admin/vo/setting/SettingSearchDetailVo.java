/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.setting.SettingSearchDetailVo
 *  com.mdd.common.entity.setting.HotSearch
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.setting;

import com.mdd.common.entity.setting.HotSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value="\u70ed\u95e8\u641c\u7d22\u8be6\u60c5Vo")
public class SettingSearchDetailVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u662f\u5426\u5f00\u8d77\u70ed\u95e8\u641c\u7d22")
    private Integer status;
    @ApiModelProperty(value="\u70ed\u95e8\u641c\u7d22\u5173\u952e\u8bcd")
    private List<HotSearch> data;

    public Integer getStatus() {
        return this.status;
    }

    public List<HotSearch> getData() {
        return this.data;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setData(List<HotSearch> data) {
        this.data = data;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingSearchDetailVo)) {
            return false;
        }
        SettingSearchDetailVo other = (SettingSearchDetailVo)o;
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
        return other instanceof SettingSearchDetailVo;
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
        return "SettingSearchDetailVo(status=" + this.getStatus() + ", data=" + this.getData() + ")";
    }
}

