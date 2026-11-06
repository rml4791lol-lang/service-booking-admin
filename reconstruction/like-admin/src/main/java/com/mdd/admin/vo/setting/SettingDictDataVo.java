/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.setting.SettingDictDataVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u5b57\u5178\u6570\u636eVo")
public class SettingDictDataVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u4e3b\u952e")
    private Integer id;
    @ApiModelProperty(value="\u7c7b\u578b")
    private Integer typeId;
    private String typeValue;
    @ApiModelProperty(value="\u952e")
    private String name;
    @ApiModelProperty(value="\u503c")
    private String value;
    @ApiModelProperty(value="\u5907\u6ce8")
    private String remark;
    @ApiModelProperty(value="\u6392\u5e8f")
    private Integer sort;
    @ApiModelProperty(value="\u662f\u5426\u505c\u7528")
    private Integer status;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private String updateTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getTypeId() {
        return this.typeId;
    }

    public String getTypeValue() {
        return this.typeValue;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String getRemark() {
        return this.remark;
    }

    public Integer getSort() {
        return this.sort;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingDictDataVo)) {
            return false;
        }
        SettingDictDataVo other = (SettingDictDataVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$typeId = this.getTypeId();
        Integer other$typeId = other.getTypeId();
        if (this$typeId == null ? other$typeId != null : !((Object)this$typeId).equals(other$typeId)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$typeValue = this.getTypeValue();
        String other$typeValue = other.getTypeValue();
        if (this$typeValue == null ? other$typeValue != null : !this$typeValue.equals(other$typeValue)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$value = this.getValue();
        String other$value = other.getValue();
        if (this$value == null ? other$value != null : !this$value.equals(other$value)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        if (this$remark == null ? other$remark != null : !this$remark.equals(other$remark)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime)) {
            return false;
        }
        String this$updateTime = this.getUpdateTime();
        String other$updateTime = other.getUpdateTime();
        return !(this$updateTime == null ? other$updateTime != null : !this$updateTime.equals(other$updateTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingDictDataVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $typeId = this.getTypeId();
        result = result * 59 + ($typeId == null ? 43 : ((Object)$typeId).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $typeValue = this.getTypeValue();
        result = result * 59 + ($typeValue == null ? 43 : $typeValue.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $value = this.getValue();
        result = result * 59 + ($value == null ? 43 : $value.hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        String $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        return result;
    }

    public String toString() {
        return "SettingDictDataVo(id=" + this.getId() + ", typeId=" + this.getTypeId() + ", typeValue=" + this.getTypeValue() + ", name=" + this.getName() + ", value=" + this.getValue() + ", remark=" + this.getRemark() + ", sort=" + this.getSort() + ", status=" + this.getStatus() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

