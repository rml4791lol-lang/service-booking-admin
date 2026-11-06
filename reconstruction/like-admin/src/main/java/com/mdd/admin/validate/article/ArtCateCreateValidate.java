/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.article.ArtCateCreateValidate
 *  com.mdd.common.validator.annotation.IntegerContains
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.DecimalMin
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.article;

import com.mdd.common.validator.annotation.IntegerContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u6587\u7ae0\u5206\u7c7b\u521b\u5efa\u53c2\u6570")
public class ArtCateCreateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotEmpty(message="\u5206\u7c7b\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=1, max=60, message="\u5206\u7c7b\u540d\u79f0\u4e0d\u80fd\u5927\u4e8e60\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u5206\u7c7b\u540d\u79f0", required=true)
    private @NotEmpty(message="\u5206\u7c7b\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=1, max=60, message="\u5206\u7c7b\u540d\u79f0\u4e0d\u80fd\u5927\u4e8e60\u4e2a\u5b57\u7b26") String name;
    @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    @DecimalMin(value="0", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5c11\u4e8e0")
    @ApiModelProperty(value="\u6392\u5e8f", required=true)
    private @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @DecimalMin(value="0", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5c11\u4e8e0") Integer sort;
    @NotNull(message="\u7f3a\u5c11isShow\u53c2\u6570")
    @IntegerContains(values={0, 1}, message="isShow\u4e0d\u662f\u5408\u6cd5\u503c")
    @ApiModelProperty(value="\u662f\u5426\u663e\u793a", required=true)
    private @NotNull(message="\u7f3a\u5c11isShow\u53c2\u6570") Integer isShow;

    public String getName() {
        return this.name;
    }

    @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") Integer getSort() {
        return this.sort;
    }

    @NotNull(message="\u7f3a\u5c11isShow\u53c2\u6570")
    public @NotNull(message="\u7f3a\u5c11isShow\u53c2\u6570") Integer getIsShow() {
        return this.isShow;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSort(@NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") Integer sort) {
        this.sort = sort;
    }

    public void setIsShow(@NotNull(message="\u7f3a\u5c11isShow\u53c2\u6570") @NotNull(message="\u7f3a\u5c11isShow\u53c2\u6570") Integer isShow) {
        this.isShow = isShow;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArtCateCreateValidate)) {
            return false;
        }
        ArtCateCreateValidate other = (ArtCateCreateValidate)o;
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
        String this$name = this.getName();
        String other$name = other.getName();
        return !(this$name == null ? other$name != null : !this$name.equals(other$name));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ArtCateCreateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        Integer $isShow = this.getIsShow();
        result = result * 59 + ($isShow == null ? 43 : ((Object)$isShow).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "ArtCateCreateValidate(name=" + this.getName() + ", sort=" + this.getSort() + ", isShow=" + this.getIsShow() + ")";
    }
}

