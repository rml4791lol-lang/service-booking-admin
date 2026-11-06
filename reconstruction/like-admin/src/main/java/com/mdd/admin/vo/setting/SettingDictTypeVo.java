/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.setting.SettingDictTypeVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u5b57\u5178\u7c7b\u578bVo")
public class SettingDictTypeVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u4e3b\u952e")
    private Integer id;
    @ApiModelProperty(value="\u5b57\u5178\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u5b57\u5178\u7c7b\u578b")
    private String type;
    @ApiModelProperty(value="\u5b57\u5178\u5907\u6ce8")
    private String remark;
    @ApiModelProperty(value="\u5b57\u5178\u72b6\u6001")
    private Integer status;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private String updateTime;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getRemark() {
        return this.remark;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        if (!(o instanceof SettingDictTypeVo)) {
            return false;
        }
        SettingDictTypeVo other = (SettingDictTypeVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$type = this.getType();
        String other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) {
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
        return other instanceof SettingDictTypeVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        String $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        return result;
    }

    public String toString() {
        return "SettingDictTypeVo(id=" + this.getId() + ", name=" + this.getName() + ", type=" + this.getType() + ", remark=" + this.getRemark() + ", status=" + this.getStatus() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

