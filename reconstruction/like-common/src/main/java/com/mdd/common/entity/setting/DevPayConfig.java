/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableField
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.setting;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u652f\u4ed8\u914d\u7f6e\u5b9e\u4f53")
public class DevPayConfig
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u6a21\u7248\u540d\u79f0")
    private String name;
    @TableField(exist=false)
    @ApiModelProperty(value="\u663e\u793a\u540d\u79f0")
    private String payWayName;
    @ApiModelProperty(value="\u6e20\u9053\u56fe\u6807")
    private String icon;
    @ApiModelProperty(value="\u652f\u4ed8\u65b9\u5f0f: [1=\u4f59\u989d\u652f\u4ed8, 2=\u5fae\u4fe1\u652f\u4ed8, 3=\u652f\u4ed8\u5b9d\u652f\u4ed8]")
    private Integer payWay;
    @ApiModelProperty(value="\u6392\u5e8f\u7f16\u53f7")
    private Integer sort;
    @ApiModelProperty(value="\u5907\u6ce8\u4fe1\u606f")
    private String remark;
    @ApiModelProperty(value="\u914d\u7f6e\u53c2\u6570")
    private Object config;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getPayWayName() {
        return this.payWayName;
    }

    public String getIcon() {
        return this.icon;
    }

    public Integer getPayWay() {
        return this.payWay;
    }

    public Integer getSort() {
        return this.sort;
    }

    public String getRemark() {
        return this.remark;
    }

    public Object getConfig() {
        return this.config;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPayWayName(String payWayName) {
        this.payWayName = payWayName;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setConfig(Object config) {
        this.config = config;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DevPayConfig)) {
            return false;
        }
        DevPayConfig other = (DevPayConfig)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$payWay = this.getPayWay();
        Integer other$payWay = other.getPayWay();
        if (this$payWay == null ? other$payWay != null : !((Object)this$payWay).equals(other$payWay)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$payWayName = this.getPayWayName();
        String other$payWayName = other.getPayWayName();
        if (this$payWayName == null ? other$payWayName != null : !this$payWayName.equals(other$payWayName)) {
            return false;
        }
        String this$icon = this.getIcon();
        String other$icon = other.getIcon();
        if (this$icon == null ? other$icon != null : !this$icon.equals(other$icon)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        if (this$remark == null ? other$remark != null : !this$remark.equals(other$remark)) {
            return false;
        }
        Object this$config = this.getConfig();
        Object other$config = other.getConfig();
        return !(this$config == null ? other$config != null : !this$config.equals(other$config));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DevPayConfig;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $payWay = this.getPayWay();
        result = result * 59 + ($payWay == null ? 43 : ((Object)$payWay).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $payWayName = this.getPayWayName();
        result = result * 59 + ($payWayName == null ? 43 : $payWayName.hashCode());
        String $icon = this.getIcon();
        result = result * 59 + ($icon == null ? 43 : $icon.hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        Object $config = this.getConfig();
        result = result * 59 + ($config == null ? 43 : $config.hashCode());
        return result;
    }

    public String toString() {
        return "DevPayConfig(id=" + this.getId() + ", name=" + this.getName() + ", payWayName=" + this.getPayWayName() + ", icon=" + this.getIcon() + ", payWay=" + this.getPayWay() + ", sort=" + this.getSort() + ", remark=" + this.getRemark() + ", config=" + this.getConfig() + ")";
    }
}

