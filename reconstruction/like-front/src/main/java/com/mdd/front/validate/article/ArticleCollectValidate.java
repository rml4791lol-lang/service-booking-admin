/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.validator.annotation.IDMust
 *  com.mdd.front.validate.article.ArticleCollectValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.front.validate.article;

import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u6587\u7ae0\u6536\u85cf\u53c2\u6570")
public class ArticleCollectValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="articleId\u53c2\u6570\u7f3a\u5931")
    @IDMust(message="articleId\u53c2\u6570\u5fc5\u987b\u5927\u4e8e0")
    @ApiModelProperty(value="\u6587\u7ae0ID", required=true)
    private @NotNull(message="articleId\u53c2\u6570\u7f3a\u5931") Integer id;

    @NotNull(message="articleId\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="articleId\u53c2\u6570\u7f3a\u5931") Integer getId() {
        return this.id;
    }

    public void setId(@NotNull(message="articleId\u53c2\u6570\u7f3a\u5931") @NotNull(message="articleId\u53c2\u6570\u7f3a\u5931") Integer id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArticleCollectValidate)) {
            return false;
        }
        ArticleCollectValidate other = (ArticleCollectValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        return !(this$id == null ? other$id != null : !((Object)this$id).equals(other$id));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ArticleCollectValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        return result;
    }

    public String toString() {
        return "ArticleCollectValidate(id=" + this.getId() + ")";
    }
}

