/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.mdd.admin.vo.system.SystemMenuListedVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u7cfb\u7edf\u83dc\u5355Vo")
public class SystemMenuListedVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u4e3b\u952e")
    private Integer id;
    @ApiModelProperty(value="\u4e0a\u7ea7\u83dc\u5355")
    private Integer pid;
    @ApiModelProperty(value="\u6743\u9650\u7c7b\u578b: [M=\u76ee\u5f55, C=\u83dc\u5355, A=\u6309\u94ae]")
    @JsonProperty(value="menuType")
    private String type;
    @ApiModelProperty(value="\u83dc\u5355\u540d\u79f0")
    @JsonProperty(value="menuName")
    private String name;
    @ApiModelProperty(value="\u83dc\u5355\u56fe\u6807")
    @JsonProperty(value="menuIcon")
    private String icon;
    @ApiModelProperty(value="\u83dc\u5355\u6392\u5e8f")
    @JsonProperty(value="menuSort")
    private Integer sort;
    @ApiModelProperty(value="\u6743\u9650\u6807\u8bc6")
    private String perms;
    @ApiModelProperty(value="\u8def\u7531\u5730\u5740")
    private String paths;
    @ApiModelProperty(value="\u524d\u7aef\u7ec4\u4ef6")
    private String component;
    @ApiModelProperty(value="\u9009\u4e2d\u8def\u5f84")
    private String selected;
    @ApiModelProperty(value="\u8def\u7531\u53c2\u6570")
    private String params;
    @ApiModelProperty(value="\u662f\u5426\u7f13\u5b58: [0=\u5426, 1=\u662f]")
    private Integer isCache;
    @ApiModelProperty(value="\u662f\u5426\u663e\u793a: [0=\u5426, 1=\u662f]")
    private Integer isShow;
    @ApiModelProperty(value="\u662f\u5426\u7981\u7528: [0=\u5426, 1=\u662f]")
    private Integer isDisable;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private String updateTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getPid() {
        return this.pid;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getIcon() {
        return this.icon;
    }

    public Integer getSort() {
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

    public Integer getIsCache() {
        return this.isCache;
    }

    public Integer getIsShow() {
        return this.isShow;
    }

    public Integer getIsDisable() {
        return this.isDisable;
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

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    @JsonProperty(value="menuType")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty(value="menuName")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty(value="menuIcon")
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @JsonProperty(value="menuSort")
    public void setSort(Integer sort) {
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

    public void setIsCache(Integer isCache) {
        this.isCache = isCache;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public void setIsDisable(Integer isDisable) {
        this.isDisable = isDisable;
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
        if (!(o instanceof SystemMenuListedVo)) {
            return false;
        }
        SystemMenuListedVo other = (SystemMenuListedVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
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
        if (this$params == null ? other$params != null : !this$params.equals(other$params)) {
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
        return other instanceof SystemMenuListedVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
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
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        String $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        return result;
    }

    public String toString() {
        return "SystemMenuListedVo(id=" + this.getId() + ", pid=" + this.getPid() + ", type=" + this.getType() + ", name=" + this.getName() + ", icon=" + this.getIcon() + ", sort=" + this.getSort() + ", perms=" + this.getPerms() + ", paths=" + this.getPaths() + ", component=" + this.getComponent() + ", selected=" + this.getSelected() + ", params=" + this.getParams() + ", isCache=" + this.getIsCache() + ", isShow=" + this.getIsShow() + ", isDisable=" + this.getIsDisable() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

