/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.article.ArtCateSearchValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u6587\u7ae0\u5206\u7c7b\u641c\u7d22\u53c2\u6570")
public class ArtCateSearchValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u5206\u7c7b\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u662f\u5426\u663e\u793a")
    private Integer isShow;

    public String getName() {
        return this.name;
    }

    public Integer getIsShow() {
        return this.isShow;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArtCateSearchValidate)) {
            return false;
        }
        ArtCateSearchValidate other = (ArtCateSearchValidate)o;
        if (!other.canEqual((Object)this)) {
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
        return other instanceof ArtCateSearchValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $isShow = this.getIsShow();
        result = result * 59 + ($isShow == null ? 43 : ((Object)$isShow).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "ArtCateSearchValidate(name=" + this.getName() + ", isShow=" + this.getIsShow() + ")";
    }
}

