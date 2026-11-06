/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.vo.article.PcArticleCenterVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.vo.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="PC\u8d44\u8baf\u4e2d\u5fc3Vo")
public class PcArticleCenterVo
implements Serializable {
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u6587\u7ae0")
    private Object article;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Object getArticle() {
        return this.article;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArticle(Object article) {
        this.article = article;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PcArticleCenterVo)) {
            return false;
        }
        PcArticleCenterVo other = (PcArticleCenterVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        Object this$article = this.getArticle();
        Object other$article = other.getArticle();
        return !(this$article == null ? other$article != null : !this$article.equals(other$article));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PcArticleCenterVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        Object $article = this.getArticle();
        result = result * 59 + ($article == null ? 43 : $article.hashCode());
        return result;
    }

    public String toString() {
        return "PcArticleCenterVo(id=" + this.getId() + ", name=" + this.getName() + ", article=" + this.getArticle() + ")";
    }
}

