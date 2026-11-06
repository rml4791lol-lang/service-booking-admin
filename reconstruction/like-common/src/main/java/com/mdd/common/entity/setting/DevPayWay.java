/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.setting;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u652f\u4ed8\u65b9\u5f0f\u5b9e\u4f53")
public class DevPayWay
implements Serializable {
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u652f\u4ed8\u914d\u7f6eID")
    private Integer payConfigId;
    @ApiModelProperty(value="\u573a\u666f\u7f16\u7801: [1=\u5fae\u4fe1\u5c0f\u7a0b\u5e8f, 2=\u5fae\u4fe1\u516c\u4f17\u53f7, 3=H5, 4=PC, 5=APP]")
    private Integer scene;
    @ApiModelProperty(value="\u9ed8\u8ba4\u652f\u4ed8: [0=\u5426\u7684, 1=\u662f\u7684]")
    private Integer isDefault;
    @ApiModelProperty(value="\u65b9\u5f0f\u72b6\u6001: [0=\u5173\u95ed, 1=\u5f00\u542f]")
    private Integer status;

    public Integer getId() {
        return this.id;
    }

    public Integer getPayConfigId() {
        return this.payConfigId;
    }

    public Integer getScene() {
        return this.scene;
    }

    public Integer getIsDefault() {
        return this.isDefault;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPayConfigId(Integer payConfigId) {
        this.payConfigId = payConfigId;
    }

    public void setScene(Integer scene) {
        this.scene = scene;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DevPayWay)) {
            return false;
        }
        DevPayWay other = (DevPayWay)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$payConfigId = this.getPayConfigId();
        Integer other$payConfigId = other.getPayConfigId();
        if (this$payConfigId == null ? other$payConfigId != null : !((Object)this$payConfigId).equals(other$payConfigId)) {
            return false;
        }
        Integer this$scene = this.getScene();
        Integer other$scene = other.getScene();
        if (this$scene == null ? other$scene != null : !((Object)this$scene).equals(other$scene)) {
            return false;
        }
        Integer this$isDefault = this.getIsDefault();
        Integer other$isDefault = other.getIsDefault();
        if (this$isDefault == null ? other$isDefault != null : !((Object)this$isDefault).equals(other$isDefault)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        return !(this$status == null ? other$status != null : !((Object)this$status).equals(other$status));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DevPayWay;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $payConfigId = this.getPayConfigId();
        result = result * 59 + ($payConfigId == null ? 43 : ((Object)$payConfigId).hashCode());
        Integer $scene = this.getScene();
        result = result * 59 + ($scene == null ? 43 : ((Object)$scene).hashCode());
        Integer $isDefault = this.getIsDefault();
        result = result * 59 + ($isDefault == null ? 43 : ((Object)$isDefault).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        return result;
    }

    public String toString() {
        return "DevPayWay(id=" + this.getId() + ", payConfigId=" + this.getPayConfigId() + ", scene=" + this.getScene() + ", isDefault=" + this.getIsDefault() + ", status=" + this.getStatus() + ")";
    }
}

