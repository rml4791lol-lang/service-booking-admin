/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.system.SystemMenuCreateValidate
 *  com.mdd.common.validator.annotation.IntegerContains
 *  com.mdd.common.validator.annotation.StringContains
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.DecimalMin
 *  javax.validation.constraints.NotNull
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.system;

import com.mdd.common.validator.annotation.IntegerContains;
import com.mdd.common.validator.annotation.StringContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u7cfb\u7edf\u83dc\u5355\u521b\u5efa\u53c2\u6570")
public class SystemMenuCreateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="\u4e0a\u7ea7\u83dc\u5355\u4e0d\u80fd\u4e3a\u7a7a")
    @DecimalMin(value="0", message="\u4e0a\u7ea7\u83dc\u5355\u503c\u4e0d\u80fd\u5c11\u4e8e0")
    @ApiModelProperty(value="\u4e0a\u7ea7ID", required=true)
    private @NotNull(message="\u4e0a\u7ea7\u83dc\u5355\u4e0d\u80fd\u4e3a\u7a7a") @DecimalMin(value="0", message="\u4e0a\u7ea7\u83dc\u5355\u503c\u4e0d\u80fd\u5c11\u4e8e0") Integer pid;
    @NotNull(message="menuType\u53c2\u6570\u7f3a\u5931")
    @StringContains(values={"M", "C", "A"}, message="\u83dc\u5355\u7c7b\u578b\u4e0d\u662f\u5408\u6cd5\u503c(M,C,A)")
    @ApiModelProperty(value="\u83dc\u5355\u7c7b\u578b", required=true)
    private @NotNull(message="menuType\u53c2\u6570\u7f3a\u5931") String type;
    @NotNull(message="\u7f3a\u5c11\u53c2\u6570menuName")
    @Length(min=1, max=30, message="\u83dc\u5355\u540d\u79f0\u5fc5\u987b\u57281~30\u4e2a\u5b57\u7b26\u5185")
    @ApiModelProperty(value="\u83dc\u5355\u540d\u79f0", required=true)
    private @NotNull(message="\u7f3a\u5c11\u53c2\u6570menuName") @Length(min=1, max=30, message="\u83dc\u5355\u540d\u79f0\u5fc5\u987b\u57281~30\u4e2a\u5b57\u7b26\u5185") String name;
    @Length(max=100, message="\u56fe\u6807\u540d\u79f0\u4e0d\u80fd\u8d85\u8fc7100\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u83dc\u5355\u56fe\u6807")
    private @Length(max=100, message="\u56fe\u6807\u540d\u79f0\u4e0d\u80fd\u8d85\u8fc7100\u4e2a\u5b57\u7b26") String icon;
    @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    @DecimalMin(value="0", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5c11\u4e8e0")
    @ApiModelProperty(value="\u83dc\u5355\u6392\u5e8f")
    private @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @DecimalMin(value="0", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5c11\u4e8e0") Integer sort;
    @Length(max=100, message="\u6743\u9650\u5b57\u7b26\u4e0d\u80fd\u8d85\u8fc7100\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u6743\u9650\u5b57\u7b26")
    private @Length(max=100, message="\u6743\u9650\u5b57\u7b26\u4e0d\u80fd\u8d85\u8fc7100\u4e2a\u5b57\u7b26") String perms;
    @Length(max=200, message="\u8def\u7531\u5730\u5740\u4e0d\u80fd\u8d85\u8fc7200\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u8def\u7531\u5730\u5740")
    private @Length(max=200, message="\u8def\u7531\u5730\u5740\u4e0d\u80fd\u8d85\u8fc7200\u4e2a\u5b57\u7b26") String paths;
    @Length(max=200, message="\u524d\u7aef\u7ec4\u4ef6\u4e0d\u80fd\u8d85\u8fc7200\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u524d\u7aef\u7ec4\u4ef6")
    private @Length(max=200, message="\u524d\u7aef\u7ec4\u4ef6\u4e0d\u80fd\u8d85\u8fc7200\u4e2a\u5b57\u7b26") String component;
    @Length(max=200, message="\u9009\u4e2d\u83dc\u5355\u8def\u5f84\u4e0d\u80fd\u8d85\u8fc7200\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u9009\u4e2d\u83dc\u5355")
    private @Length(max=200, message="\u9009\u4e2d\u83dc\u5355\u8def\u5f84\u4e0d\u80fd\u8d85\u8fc7200\u4e2a\u5b57\u7b26") String selected;
    @Length(max=200, message="\u8def\u7531\u53c2\u6570\u4e0d\u80fd\u8d85\u8fc7200\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u8def\u7531\u53c2\u6570")
    private @Length(max=200, message="\u8def\u7531\u53c2\u6570\u4e0d\u80fd\u8d85\u8fc7200\u4e2a\u5b57\u7b26") String params;
    @NotNull(message="\u8bf7\u9009\u62e9\u7f13\u5b58\u72b6\u6001")
    @IntegerContains(values={0, 1})
    @ApiModelProperty(value="\u7f13\u5b58\u72b6\u6001", required=true)
    private @NotNull(message="\u8bf7\u9009\u62e9\u7f13\u5b58\u72b6\u6001") Integer isCache;
    @NotNull(message="\u8bf7\u9009\u62e9\u663e\u793a\u72b6\u6001")
    @IntegerContains(values={0, 1})
    @ApiModelProperty(value="\u663e\u793a\u72b6\u6001", required=true)
    private @NotNull(message="\u8bf7\u9009\u62e9\u663e\u793a\u72b6\u6001") Integer isShow;
    @NotNull(message="\u8bf7\u9009\u62e9\u83dc\u5355\u72b6\u6001")
    @IntegerContains(values={0, 1})
    @ApiModelProperty(value="\u83dc\u5355\u72b6\u6001", required=true)
    private @NotNull(message="\u8bf7\u9009\u62e9\u83dc\u5355\u72b6\u6001") Integer isDisable;

    @NotNull(message="\u4e0a\u7ea7\u83dc\u5355\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u4e0a\u7ea7\u83dc\u5355\u4e0d\u80fd\u4e3a\u7a7a") Integer getPid() {
        return this.pid;
    }

    @NotNull(message="menuType\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="menuType\u53c2\u6570\u7f3a\u5931") String getType() {
        return this.type;
    }

    @NotNull(message="\u7f3a\u5c11\u53c2\u6570menuName")
    public @NotNull(message="\u7f3a\u5c11\u53c2\u6570menuName") String getName() {
        return this.name;
    }

    public String getIcon() {
        return this.icon;
    }

    @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") Integer getSort() {
        return this.sort;
    }

    public String getPerms() {
        return this.perms;
    }

    public String getPaths() {
        return this.paths;
    }

    public String getComponent() {
        return this.component;
    }

    public String getSelected() {
        return this.selected;
    }

    public String getParams() {
        return this.params;
    }

    @NotNull(message="\u8bf7\u9009\u62e9\u7f13\u5b58\u72b6\u6001")
    public @NotNull(message="\u8bf7\u9009\u62e9\u7f13\u5b58\u72b6\u6001") Integer getIsCache() {
        return this.isCache;
    }

    @NotNull(message="\u8bf7\u9009\u62e9\u663e\u793a\u72b6\u6001")
    public @NotNull(message="\u8bf7\u9009\u62e9\u663e\u793a\u72b6\u6001") Integer getIsShow() {
        return this.isShow;
    }

    @NotNull(message="\u8bf7\u9009\u62e9\u83dc\u5355\u72b6\u6001")
    public @NotNull(message="\u8bf7\u9009\u62e9\u83dc\u5355\u72b6\u6001") Integer getIsDisable() {
        return this.isDisable;
    }

    public void setPid(@NotNull(message="\u4e0a\u7ea7\u83dc\u5355\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u4e0a\u7ea7\u83dc\u5355\u4e0d\u80fd\u4e3a\u7a7a") Integer pid) {
        this.pid = pid;
    }

    public void setType(@NotNull(message="menuType\u53c2\u6570\u7f3a\u5931") @NotNull(message="menuType\u53c2\u6570\u7f3a\u5931") String type) {
        this.type = type;
    }

    public void setName(@NotNull(message="\u7f3a\u5c11\u53c2\u6570menuName") @NotNull(message="\u7f3a\u5c11\u53c2\u6570menuName") String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setSort(@NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") Integer sort) {
        this.sort = sort;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public void setPaths(String paths) {
        this.paths = paths;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setIsCache(@NotNull(message="\u8bf7\u9009\u62e9\u7f13\u5b58\u72b6\u6001") @NotNull(message="\u8bf7\u9009\u62e9\u7f13\u5b58\u72b6\u6001") Integer isCache) {
        this.isCache = isCache;
    }

    public void setIsShow(@NotNull(message="\u8bf7\u9009\u62e9\u663e\u793a\u72b6\u6001") @NotNull(message="\u8bf7\u9009\u62e9\u663e\u793a\u72b6\u6001") Integer isShow) {
        this.isShow = isShow;
    }

    public void setIsDisable(@NotNull(message="\u8bf7\u9009\u62e9\u83dc\u5355\u72b6\u6001") @NotNull(message="\u8bf7\u9009\u62e9\u83dc\u5355\u72b6\u6001") Integer isDisable) {
        this.isDisable = isDisable;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemMenuCreateValidate)) {
            return false;
        }
        SystemMenuCreateValidate other = (SystemMenuCreateValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$pid = this.getPid();
        Integer other$pid = other.getPid();
        if (this$pid == null ? other$pid != null : !((Object)this$pid).equals(other$pid)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        Integer this$isCache = this.getIsCache();
        Integer other$isCache = other.getIsCache();
        if (this$isCache == null ? other$isCache != null : !((Object)this$isCache).equals(other$isCache)) {
            return false;
        }
        Integer this$isShow = this.getIsShow();
        Integer other$isShow = other.getIsShow();
        if (this$isShow == null ? other$isShow != null : !((Object)this$isShow).equals(other$isShow)) {
            return false;
        }
        Integer this$isDisable = this.getIsDisable();
        Integer other$isDisable = other.getIsDisable();
        if (this$isDisable == null ? other$isDisable != null : !((Object)this$isDisable).equals(other$isDisable)) {
            return false;
        }
        String this$type = this.getType();
        String other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) {
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
        String this$perms = this.getPerms();
        String other$perms = other.getPerms();
        if (this$perms == null ? other$perms != null : !this$perms.equals(other$perms)) {
            return false;
        }
        String this$paths = this.getPaths();
        String other$paths = other.getPaths();
        if (this$paths == null ? other$paths != null : !this$paths.equals(other$paths)) {
            return false;
        }
        String this$component = this.getComponent();
        String other$component = other.getComponent();
        if (this$component == null ? other$component != null : !this$component.equals(other$component)) {
            return false;
        }
        String this$selected = this.getSelected();
        String other$selected = other.getSelected();
        if (this$selected == null ? other$selected != null : !this$selected.equals(other$selected)) {
            return false;
        }
        String this$params = this.getParams();
        String other$params = other.getParams();
        return !(this$params == null ? other$params != null : !this$params.equals(other$params));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SystemMenuCreateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $pid = this.getPid();
        result = result * 59 + ($pid == null ? 43 : ((Object)$pid).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        Integer $isCache = this.getIsCache();
        result = result * 59 + ($isCache == null ? 43 : ((Object)$isCache).hashCode());
        Integer $isShow = this.getIsShow();
        result = result * 59 + ($isShow == null ? 43 : ((Object)$isShow).hashCode());
        Integer $isDisable = this.getIsDisable();
        result = result * 59 + ($isDisable == null ? 43 : ((Object)$isDisable).hashCode());
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $icon = this.getIcon();
        result = result * 59 + ($icon == null ? 43 : $icon.hashCode());
        String $perms = this.getPerms();
        result = result * 59 + ($perms == null ? 43 : $perms.hashCode());
        String $paths = this.getPaths();
        result = result * 59 + ($paths == null ? 43 : $paths.hashCode());
        String $component = this.getComponent();
        result = result * 59 + ($component == null ? 43 : $component.hashCode());
        String $selected = this.getSelected();
        result = result * 59 + ($selected == null ? 43 : $selected.hashCode());
        String $params = this.getParams();
        result = result * 59 + ($params == null ? 43 : $params.hashCode());
        return result;
    }

    public String toString() {
        return "SystemMenuCreateValidate(pid=" + this.getPid() + ", type=" + this.getType() + ", name=" + this.getName() + ", icon=" + this.getIcon() + ", sort=" + this.getSort() + ", perms=" + this.getPerms() + ", paths=" + this.getPaths() + ", component=" + this.getComponent() + ", selected=" + this.getSelected() + ", params=" + this.getParams() + ", isCache=" + this.getIsCache() + ", isShow=" + this.getIsShow() + ", isDisable=" + this.getIsDisable() + ")";
    }
}

