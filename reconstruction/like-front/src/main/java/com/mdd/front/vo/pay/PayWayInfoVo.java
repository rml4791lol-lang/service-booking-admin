/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.vo.pay.PayWayInfoVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.vo.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u652f\u4ed8\u65b9\u5f0f\u4fe1\u606fVo")
public class PayWayInfoVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u56fe\u6807")
    private String icon;
    @ApiModelProperty(value="\u662f\u5426\u9ed8\u8ba4: [0=\u5426, 1=\u662f]")
    private Integer isDefault;
    private String extra;
    private Integer payWay;
    private String remark;
    private Integer sort;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getIcon() {
        return this.icon;
    }

    public Integer getIsDefault() {
        return this.isDefault;
    }

    public String getExtra() {
        return this.extra;
    }

    public Integer getPayWay() {
        return this.payWay;
    }

    public String getRemark() {
        return this.remark;
    }

    public Integer getSort() {
        return this.sort;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PayWayInfoVo)) {
            return false;
        }
        PayWayInfoVo other = (PayWayInfoVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$isDefault = this.getIsDefault();
        Integer other$isDefault = other.getIsDefault();
        if (this$isDefault == null ? other$isDefault != null : !((Object)this$isDefault).equals(other$isDefault)) {
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
        String this$icon = this.getIcon();
        String other$icon = other.getIcon();
        if (this$icon == null ? other$icon != null : !this$icon.equals(other$icon)) {
            return false;
        }
        String this$extra = this.getExtra();
        String other$extra = other.getExtra();
        if (this$extra == null ? other$extra != null : !this$extra.equals(other$extra)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        return !(this$remark == null ? other$remark != null : !this$remark.equals(other$remark));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PayWayInfoVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $isDefault = this.getIsDefault();
        result = result * 59 + ($isDefault == null ? 43 : ((Object)$isDefault).hashCode());
        Integer $payWay = this.getPayWay();
        result = result * 59 + ($payWay == null ? 43 : ((Object)$payWay).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $icon = this.getIcon();
        result = result * 59 + ($icon == null ? 43 : $icon.hashCode());
        String $extra = this.getExtra();
        result = result * 59 + ($extra == null ? 43 : $extra.hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        return result;
    }

    public String toString() {
        return "PayWayInfoVo(id=" + this.getId() + ", name=" + this.getName() + ", icon=" + this.getIcon() + ", isDefault=" + this.getIsDefault() + ", extra=" + this.getExtra() + ", payWay=" + this.getPayWay() + ", remark=" + this.getRemark() + ", sort=" + this.getSort() + ")";
    }
}

