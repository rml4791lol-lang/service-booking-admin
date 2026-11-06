/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.validator.annotation.StringContains
 *  com.mdd.front.validate.article.ArticleSearchValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.front.validate.article;

import com.mdd.common.validator.annotation.StringContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u6587\u7ae0\u641c\u7d22\u53c2\u6570")
public class ArticleSearchValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer cid;
    @Length(max=100, message="\u5173\u952e\u8bcd\u8fc7\u957f\u4e86")
    @ApiModelProperty(value="\u5173\u952e\u8bcd")
    private @Length(max=100, message="\u5173\u952e\u8bcd\u8fc7\u957f\u4e86") String keyword;
    @StringContains(values={"hot", "new", "default"})
    @ApiModelProperty(value="\u6392\u5e8f\u53f7")
    private String sort;

    public Integer getCid() {
        return this.cid;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public String getSort() {
        return this.sort;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArticleSearchValidate)) {
            return false;
        }
        ArticleSearchValidate other = (ArticleSearchValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$cid = this.getCid();
        Integer other$cid = other.getCid();
        if (this$cid == null ? other$cid != null : !((Object)this$cid).equals(other$cid)) {
            return false;
        }
        String this$keyword = this.getKeyword();
        String other$keyword = other.getKeyword();
        if (this$keyword == null ? other$keyword != null : !this$keyword.equals(other$keyword)) {
            return false;
        }
        String this$sort = this.getSort();
        String other$sort = other.getSort();
        return !(this$sort == null ? other$sort != null : !this$sort.equals(other$sort));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ArticleSearchValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $cid = this.getCid();
        result = result * 59 + ($cid == null ? 43 : ((Object)$cid).hashCode());
        String $keyword = this.getKeyword();
        result = result * 59 + ($keyword == null ? 43 : $keyword.hashCode());
        String $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : $sort.hashCode());
        return result;
    }

    public String toString() {
        return "ArticleSearchValidate(cid=" + this.getCid() + ", keyword=" + this.getKeyword() + ", sort=" + this.getSort() + ")";
    }
}

