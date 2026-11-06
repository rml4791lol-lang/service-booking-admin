/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.setting.SettingSearchObjectVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u70ed\u95e8\u641c\u7d22\u5bf9\u8c61Vo")
public class SettingSearchObjectVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u5173\u952e\u8bcd")
    private String name;
    @ApiModelProperty(value="\u6392\u5e8f")
    private Integer sort;

    public String getName() {
        return this.name;
    }

    public Integer getSort() {
        return this.sort;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingSearchObjectVo)) {
            return false;
        }
        SettingSearchObjectVo other = (SettingSearchObjectVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        return !(this$name == null ? other$name != null : !this$name.equals(other$name));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingSearchObjectVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "SettingSearchObjectVo(name=" + this.getName() + ", sort=" + this.getSort() + ")";
    }
}

