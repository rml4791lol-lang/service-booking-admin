/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.setting.SettingPaymentMethodVo
 */
package com.mdd.admin.vo.setting;

import java.io.Serializable;

public class SettingPaymentMethodVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String payWayName;
    private Integer payConfigId;
    private String icon;
    private Integer scene;
    private Integer status;
    private Integer isDefault;

    public Integer getId() {
        return this.id;
    }

    public String getPayWayName() {
        return this.payWayName;
    }

    public Integer getPayConfigId() {
        return this.payConfigId;
    }

    public String getIcon() {
        return this.icon;
    }

    public Integer getScene() {
        return this.scene;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Integer getIsDefault() {
        return this.isDefault;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPayWayName(String payWayName) {
        this.payWayName = payWayName;
    }

    public void setPayConfigId(Integer payConfigId) {
        this.payConfigId = payConfigId;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setScene(Integer scene) {
        this.scene = scene;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingPaymentMethodVo)) {
            return false;
        }
        SettingPaymentMethodVo other = (SettingPaymentMethodVo)o;
        if (!other.canEqual((Object)this)) {
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
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Integer this$isDefault = this.getIsDefault();
        Integer other$isDefault = other.getIsDefault();
        if (this$isDefault == null ? other$isDefault != null : !((Object)this$isDefault).equals(other$isDefault)) {
            return false;
        }
        String this$payWayName = this.getPayWayName();
        String other$payWayName = other.getPayWayName();
        if (this$payWayName == null ? other$payWayName != null : !this$payWayName.equals(other$payWayName)) {
            return false;
        }
        String this$icon = this.getIcon();
        String other$icon = other.getIcon();
        return !(this$icon == null ? other$icon != null : !this$icon.equals(other$icon));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingPaymentMethodVo;
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
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $isDefault = this.getIsDefault();
        result = result * 59 + ($isDefault == null ? 43 : ((Object)$isDefault).hashCode());
        String $payWayName = this.getPayWayName();
        result = result * 59 + ($payWayName == null ? 43 : $payWayName.hashCode());
        String $icon = this.getIcon();
        result = result * 59 + ($icon == null ? 43 : $icon.hashCode());
        return result;
    }

    public String toString() {
        return "SettingPaymentMethodVo(id=" + this.getId() + ", payWayName=" + this.getPayWayName() + ", payConfigId=" + this.getPayConfigId() + ", icon=" + this.getIcon() + ", scene=" + this.getScene() + ", status=" + this.getStatus() + ", isDefault=" + this.getIsDefault() + ")";
    }
}

