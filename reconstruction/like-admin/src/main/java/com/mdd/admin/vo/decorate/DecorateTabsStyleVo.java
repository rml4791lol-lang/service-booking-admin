/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.decorate.DecorateTabsStyleVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.decorate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u88c5\u4fee\u5e95\u90e8\u5bfc\u822a\u6837\u5f0fVo")
public class DecorateTabsStyleVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u9ed8\u8ba4\u989c\u8272")
    private String defaultColor;
    @ApiModelProperty(value="\u9009\u4e2d\u989c\u8272")
    private String selectedColor;

    public String getDefaultColor() {
        return this.defaultColor;
    }

    public String getSelectedColor() {
        return this.selectedColor;
    }

    public void setDefaultColor(String defaultColor) {
        this.defaultColor = defaultColor;
    }

    public void setSelectedColor(String selectedColor) {
        this.selectedColor = selectedColor;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DecorateTabsStyleVo)) {
            return false;
        }
        DecorateTabsStyleVo other = (DecorateTabsStyleVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$defaultColor = this.getDefaultColor();
        String other$defaultColor = other.getDefaultColor();
        if (this$defaultColor == null ? other$defaultColor != null : !this$defaultColor.equals(other$defaultColor)) {
            return false;
        }
        String this$selectedColor = this.getSelectedColor();
        String other$selectedColor = other.getSelectedColor();
        return !(this$selectedColor == null ? other$selectedColor != null : !this$selectedColor.equals(other$selectedColor));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DecorateTabsStyleVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $defaultColor = this.getDefaultColor();
        result = result * 59 + ($defaultColor == null ? 43 : $defaultColor.hashCode());
        String $selectedColor = this.getSelectedColor();
        result = result * 59 + ($selectedColor == null ? 43 : $selectedColor.hashCode());
        return result;
    }

    public String toString() {
        return "DecorateTabsStyleVo(defaultColor=" + this.getDefaultColor() + ", selectedColor=" + this.getSelectedColor() + ")";
    }
}

