/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.mdd.admin.validate.article.ArticleUpdateValidate
 *  com.mdd.common.validator.annotation.IDMust
 *  com.mdd.common.validator.annotation.IntegerContains
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.DecimalMin
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mdd.common.validator.annotation.IDMust;
import com.mdd.common.validator.annotation.IntegerContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u6587\u7ae0\u66f4\u65b0\u53c2\u6570")
public class ArticleUpdateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @IDMust(message="id\u53c2\u6570\u5fc5\u4f20\u4e14\u9700\u5927\u4e8e0")
    @ApiModelProperty(value="id", required=true)
    private Integer id;
    @IDMust(message="cid\u53c2\u6570\u5fc5\u4f20\u4e14\u9700\u5927\u4e8e0")
    @ApiModelProperty(value="\u5206\u7c7bID", required=true)
    private Integer cid;
    @NotEmpty(message="\u6587\u7ae0\u6807\u9898\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=1, max=200, message="\u6587\u7ae0\u6807\u9898\u4e0d\u80fd\u5927\u4e8e200\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u6587\u7ae0\u6807\u9898", required=true)
    private @NotEmpty(message="\u6587\u7ae0\u6807\u9898\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=1, max=200, message="\u6587\u7ae0\u6807\u9898\u4e0d\u80fd\u5927\u4e8e200\u4e2a\u5b57\u7b26") String title;
    @Length(max=200, message="\u7b80\u4ecb\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u6587\u7ae0\u7b80\u4ecb")
    @JsonProperty(value="abstract")
    private @Length(max=200, message="\u7b80\u4ecb\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26") String abstractFied = "";
    @Length(max=200, message="\u56fe\u7247\u94fe\u63a5\u8fc7\u957f\u4e0d\u80fd\u8d85200\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u5c01\u9762\u56fe\u7247")
    private @Length(max=200, message="\u56fe\u7247\u94fe\u63a5\u8fc7\u957f\u4e0d\u80fd\u8d85200\u4e2a\u5b57\u7b26") String image = "";
    @Length(max=32, message="\u4f5c\u8005\u540d\u79f0\u4e0d\u80fd\u8d8532\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u4f5c\u8005\u59d3\u540d")
    private @Length(max=32, message="\u4f5c\u8005\u540d\u79f0\u4e0d\u80fd\u8d8532\u4e2a\u5b57\u7b26") String author = "";
    @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    @DecimalMin(value="0", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5c11\u4e8e0")
    @ApiModelProperty(value="\u6392\u5e8f", required=true)
    private @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @DecimalMin(value="0", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5c11\u4e8e0") Integer sort;
    @NotNull(message="\u7f3a\u5c11isShow\u53c2\u6570")
    @IntegerContains(values={0, 1}, message="isShow\u4e0d\u662f\u5408\u6cd5\u503c")
    @ApiModelProperty(value="\u662f\u5426\u663e\u793a", required=true)
    private @NotNull(message="\u7f3a\u5c11isShow\u53c2\u6570") Integer isShow;
    @ApiModelProperty(value="\u6587\u7ae0\u5185\u5bb9")
    private String content = "";
    @ApiModelProperty(value="\u6587\u7ae0\u63cf\u8ff0")
    private String desc = "";
    @DecimalMin(value="0", message="\u521d\u59cb\u6d4f\u89c8\u91cf\u4e0d\u80fd\u5c11\u4e8e0")
    @ApiModelProperty(value="\u521d\u59cb\u6d4f\u89c8\u91cf")
    private @DecimalMin(value="0", message="\u521d\u59cb\u6d4f\u89c8\u91cf\u4e0d\u80fd\u5c11\u4e8e0") Integer click_virtual = 0;

    public Integer getId() {
        return this.id;
    }

    public Integer getCid() {
        return this.cid;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAbstractFied() {
        return this.abstractFied;
    }

    public String getImage() {
        return this.image;
    }

    public String getAuthor() {
        return this.author;
    }

    @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") Integer getSort() {
        return this.sort;
    }

    @NotNull(message="\u7f3a\u5c11isShow\u53c2\u6570")
    public @NotNull(message="\u7f3a\u5c11isShow\u53c2\u6570") Integer getIsShow() {
        return this.isShow;
    }

    public String getContent() {
        return this.content;
    }

    public String getDesc() {
        return this.desc;
    }

    public Integer getClick_virtual() {
        return this.click_virtual;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty(value="abstract")
    public void setAbstractFied(String abstractFied) {
        this.abstractFied = abstractFied;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setSort(@NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") Integer sort) {
        this.sort = sort;
    }

    public void setIsShow(@NotNull(message="\u7f3a\u5c11isShow\u53c2\u6570") @NotNull(message="\u7f3a\u5c11isShow\u53c2\u6570") Integer isShow) {
        this.isShow = isShow;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setClick_virtual(Integer click_virtual) {
        this.click_virtual = click_virtual;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArticleUpdateValidate)) {
            return false;
        }
        ArticleUpdateValidate other = (ArticleUpdateValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$cid = this.getCid();
        Integer other$cid = other.getCid();
        if (this$cid == null ? other$cid != null : !((Object)this$cid).equals(other$cid)) {
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
        Integer this$click_virtual = this.getClick_virtual();
        Integer other$click_virtual = other.getClick_virtual();
        if (this$click_virtual == null ? other$click_virtual != null : !((Object)this$click_virtual).equals(other$click_virtual)) {
            return false;
        }
        String this$title = this.getTitle();
        String other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
            return false;
        }
        String this$abstractFied = this.getAbstractFied();
        String other$abstractFied = other.getAbstractFied();
        if (this$abstractFied == null ? other$abstractFied != null : !this$abstractFied.equals(other$abstractFied)) {
            return false;
        }
        String this$image = this.getImage();
        String other$image = other.getImage();
        if (this$image == null ? other$image != null : !this$image.equals(other$image)) {
            return false;
        }
        String this$author = this.getAuthor();
        String other$author = other.getAuthor();
        if (this$author == null ? other$author != null : !this$author.equals(other$author)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
            return false;
        }
        String this$desc = this.getDesc();
        String other$desc = other.getDesc();
        return !(this$desc == null ? other$desc != null : !this$desc.equals(other$desc));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ArticleUpdateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $cid = this.getCid();
        result = result * 59 + ($cid == null ? 43 : ((Object)$cid).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        Integer $isShow = this.getIsShow();
        result = result * 59 + ($isShow == null ? 43 : ((Object)$isShow).hashCode());
        Integer $click_virtual = this.getClick_virtual();
        result = result * 59 + ($click_virtual == null ? 43 : ((Object)$click_virtual).hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $abstractFied = this.getAbstractFied();
        result = result * 59 + ($abstractFied == null ? 43 : $abstractFied.hashCode());
        String $image = this.getImage();
        result = result * 59 + ($image == null ? 43 : $image.hashCode());
        String $author = this.getAuthor();
        result = result * 59 + ($author == null ? 43 : $author.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        String $desc = this.getDesc();
        result = result * 59 + ($desc == null ? 43 : $desc.hashCode());
        return result;
    }

    public String toString() {
        return "ArticleUpdateValidate(id=" + this.getId() + ", cid=" + this.getCid() + ", title=" + this.getTitle() + ", abstractFied=" + this.getAbstractFied() + ", image=" + this.getImage() + ", author=" + this.getAuthor() + ", sort=" + this.getSort() + ", isShow=" + this.getIsShow() + ", content=" + this.getContent() + ", desc=" + this.getDesc() + ", click_virtual=" + this.getClick_virtual() + ")";
    }
}

