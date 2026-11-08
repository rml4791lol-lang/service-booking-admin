/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.system.SystemRoleCreateValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.DecimalMin
 *  javax.validation.constraints.NotNull
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u7cfb\u7edf\u89d2\u8272\u521b\u5efa\u53c2\u6570")
public class SystemRoleCreateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="\u7f3a\u5c11\u53c2\u6570name")
    @Length(min=1, max=16, message="\u89d2\u8272\u540d\u79f0\u5fc5\u987b\u57281~16\u4e2a\u5b57\u7b26\u5185")
    @ApiModelProperty(value="\u89d2\u8272\u540d\u79f0", required=true)
    private @NotNull(message="\u7f3a\u5c11\u53c2\u6570name") @Length(min=1, max=16, message="\u89d2\u8272\u540d\u79f0\u5fc5\u987b\u57281~16\u4e2a\u5b57\u7b26\u5185") String name;
    @Length(max=200, message="\u63cf\u8ff0\u4fe1\u606f\u4e0d\u80fd\u8d85\u8fc7200\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u63cf\u8ff0", required=true)
    private @Length(max=200, message="\u63cf\u8ff0\u4fe1\u606f\u4e0d\u80fd\u8d85\u8fc7200\u4e2a\u5b57\u7b26") String desc;
    @DecimalMin(value="0", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5c11\u4e8e0")
    @ApiModelProperty(value="\u6392\u5e8f", required=true)
    private @DecimalMin(value="0", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5c11\u4e8e0") Integer sort = 0;
    @ApiModelProperty(value="\u83dc\u5355\u6743\u9650")
    private List<Integer> menuId;

    @NotNull(message="\u7f3a\u5c11\u53c2\u6570name")
    public @NotNull(message="\u7f3a\u5c11\u53c2\u6570name") String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.desc;
    }

    public Integer getSort() {
        return this.sort;
    }

    public List<Integer> getMenuId() {
        return this.menuId;
    }

    public void setName(@NotNull(message="\u7f3a\u5c11\u53c2\u6570name") @NotNull(message="\u7f3a\u5c11\u53c2\u6570name") String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setMenuId(List<Integer> menuId) {
        this.menuId = menuId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemRoleCreateValidate)) {
            return false;
        }
        SystemRoleCreateValidate other = (SystemRoleCreateValidate)o;
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
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$desc = this.getDesc();
        String other$desc = other.getDesc();
        if (this$desc == null ? other$desc != null : !this$desc.equals(other$desc)) {
            return false;
        }
        List this$menuId = this.getMenuId();
        List other$menuId = other.getMenuId();
        return !(this$menuId == null ? other$menuId != null : !((Object)this$menuId).equals(other$menuId));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SystemRoleCreateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $desc = this.getDesc();
        result = result * 59 + ($desc == null ? 43 : $desc.hashCode());
        List $menuId = this.getMenuId();
        result = result * 59 + ($menuId == null ? 43 : ((Object)$menuId).hashCode());
        return result;
    }

    public String toString() {
        return "SystemRoleCreateValidate(name=" + this.getName() + ", desc=" + this.getDesc() + ", sort=" + this.getSort() + ", menuId=" + this.getMenuId() + ")";
    }
}

