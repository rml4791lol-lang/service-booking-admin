/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.course.SportsCategoryCreateValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u8fd0\u52a8\u9879\u76ee\u5206\u7c7b\u521b\u5efa\u53c2\u6570")
public class SportsCategoryCreateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u9879\u76ee\u540d\u79f0")
    private @NotNull(message="\u9879\u76ee\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a") String name;
    @ApiModelProperty(value="\u7236\u7ea7ID(0\u6216NULL=\u6839)")
    @Min(value=0, message="\u7236\u7ea7ID\u4e0d\u5408\u6cd5")
    private Integer parentId = 0;
    @ApiModelProperty(value="\u56fe\u6807")
    @Length(max=255, message="\u56fe\u6807\u5730\u5740\u8fc7\u957f")
    private String icon;
    @ApiModelProperty(value="\u6392\u5e8f")
    @Min(value=0, message="\u6392\u5e8f\u4e0d\u80fd\u4e3a\u8d1f\u6570")
    @Max(value=65535, message="\u6392\u5e8f\u8fc7\u5927")
    private Integer sort = 0;
    @ApiModelProperty(value="\u662f\u5426\u663e\u793a: 0=\u9690\u85cf, 1=\u663e\u793a")
    @Min(value=0, message="\u663e\u793a\u72b6\u6001\u4e0d\u5408\u6cd5")
    @Max(value=1, message="\u663e\u793a\u72b6\u6001\u4e0d\u5408\u6cd5")
    private Integer isShow = 1;

    @NotNull(message="\u9879\u76ee\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u9879\u76ee\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a") String getName() {
        return this.name;
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public String getIcon() {
        return this.icon;
    }

    public Integer getSort() {
        return this.sort;
    }

    public Integer getIsShow() {
        return this.isShow;
    }

    public void setName(@NotNull(message="\u9879\u76ee\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u9879\u76ee\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a") String name) {
        this.name = name;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SportsCategoryCreateValidate)) {
            return false;
        }
        SportsCategoryCreateValidate other = (SportsCategoryCreateValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        Integer this$isShow = this.getIsShow();
        Integer other$isShow = other.getIsShow();
        if (this$isShow == null ? other$isShow != null : !((Object)this$isShow).equals(other$isShow)) {
            return false;
        }
        Integer this$parentId = this.getParentId();
        Integer other$parentId = other.getParentId();
        if (this$parentId == null ? other$parentId != null : !((Object)this$parentId).equals(other$parentId)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$icon = this.getIcon();
        String other$icon = other.getIcon();
        return !(this$icon == null ? other$icon != null : !this$icon.equals(other$icon));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SportsCategoryCreateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        Integer $isShow = this.getIsShow();
        result = result * 59 + ($isShow == null ? 43 : ((Object)$isShow).hashCode());
        Integer $parentId = this.getParentId();
        result = result * 59 + ($parentId == null ? 43 : ((Object)$parentId).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $icon = this.getIcon();
        result = result * 59 + ($icon == null ? 43 : $icon.hashCode());
        return result;
    }

    public String toString() {
        return "SportsCategoryCreateValidate(name=" + this.getName() + ", parentId=" + this.getParentId() + ", icon=" + this.getIcon() + ", sort=" + this.getSort() + ", isShow=" + this.getIsShow() + ")";
    }
}

