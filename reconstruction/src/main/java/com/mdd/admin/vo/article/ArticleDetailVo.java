/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.TableField
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.mdd.admin.vo.article.ArticleDetailVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.article;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u6587\u7ae0\u8be6\u60c5Vo")
public class ArticleDetailVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u4e3b\u952e")
    private Integer id;
    @ApiModelProperty(value="\u5206\u7c7b")
    private Integer cid;
    @ApiModelProperty(value="\u6807\u9898")
    private String title;
    @ApiModelProperty(value="\u56fe\u7247")
    private String image;
    @ApiModelProperty(value="\u4f5c\u8005")
    private String author;
    @ApiModelProperty(value="\u7b80\u4ecb")
    @TableField(value="`desc`")
    private String desc;
    @ApiModelProperty(value="\u6458\u8981")
    @JsonProperty(value="abstract")
    private String abstractField;
    @ApiModelProperty(value="\u8bbf\u95ee")
    private Integer click;
    @ApiModelProperty(value="\u5185\u5bb9")
    private String content;
    @ApiModelProperty(value="\u6392\u5e8f")
    private Integer sort;
    @ApiModelProperty(value="\u865a\u62df\u6d4f\u89c8\u91cf")
    private Integer clickVirtual;
    @ApiModelProperty(value="\u5b9e\u9645\u6d4f\u89c8\u91cf")
    private Integer clickActual;
    @ApiModelProperty(value="\u662f\u5426\u663e\u793a")
    private Integer isShow;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private String updateTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getCid() {
        return this.cid;
    }

    public String getTitle() {
        return this.title;
    }

    public String getImage() {
        return this.image;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getAbstractField() {
        return this.abstractField;
    }

    public Integer getClick() {
        return this.click;
    }

    public String getContent() {
        return this.content;
    }

    public Integer getSort() {
        return this.sort;
    }

    public Integer getClickVirtual() {
        return this.clickVirtual;
    }

    public Integer getClickActual() {
        return this.clickActual;
    }

    public Integer getIsShow() {
        return this.isShow;
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

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @JsonProperty(value="abstract")
    public void setAbstractField(String abstractField) {
        this.abstractField = abstractField;
    }

    public void setClick(Integer click) {
        this.click = click;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setClickVirtual(Integer clickVirtual) {
        this.clickVirtual = clickVirtual;
    }

    public void setClickActual(Integer clickActual) {
        this.clickActual = clickActual;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
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
        if (!(o instanceof ArticleDetailVo)) {
            return false;
        }
        ArticleDetailVo other = (ArticleDetailVo)o;
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
        Integer this$click = this.getClick();
        Integer other$click = other.getClick();
        if (this$click == null ? other$click != null : !((Object)this$click).equals(other$click)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        Integer this$clickVirtual = this.getClickVirtual();
        Integer other$clickVirtual = other.getClickVirtual();
        if (this$clickVirtual == null ? other$clickVirtual != null : !((Object)this$clickVirtual).equals(other$clickVirtual)) {
            return false;
        }
        Integer this$clickActual = this.getClickActual();
        Integer other$clickActual = other.getClickActual();
        if (this$clickActual == null ? other$clickActual != null : !((Object)this$clickActual).equals(other$clickActual)) {
            return false;
        }
        Integer this$isShow = this.getIsShow();
        Integer other$isShow = other.getIsShow();
        if (this$isShow == null ? other$isShow != null : !((Object)this$isShow).equals(other$isShow)) {
            return false;
        }
        String this$title = this.getTitle();
        String other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
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
        String this$desc = this.getDesc();
        String other$desc = other.getDesc();
        if (this$desc == null ? other$desc != null : !this$desc.equals(other$desc)) {
            return false;
        }
        String this$abstractField = this.getAbstractField();
        String other$abstractField = other.getAbstractField();
        if (this$abstractField == null ? other$abstractField != null : !this$abstractField.equals(other$abstractField)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
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
        return other instanceof ArticleDetailVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $cid = this.getCid();
        result = result * 59 + ($cid == null ? 43 : ((Object)$cid).hashCode());
        Integer $click = this.getClick();
        result = result * 59 + ($click == null ? 43 : ((Object)$click).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        Integer $clickVirtual = this.getClickVirtual();
        result = result * 59 + ($clickVirtual == null ? 43 : ((Object)$clickVirtual).hashCode());
        Integer $clickActual = this.getClickActual();
        result = result * 59 + ($clickActual == null ? 43 : ((Object)$clickActual).hashCode());
        Integer $isShow = this.getIsShow();
        result = result * 59 + ($isShow == null ? 43 : ((Object)$isShow).hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $image = this.getImage();
        result = result * 59 + ($image == null ? 43 : $image.hashCode());
        String $author = this.getAuthor();
        result = result * 59 + ($author == null ? 43 : $author.hashCode());
        String $desc = this.getDesc();
        result = result * 59 + ($desc == null ? 43 : $desc.hashCode());
        String $abstractField = this.getAbstractField();
        result = result * 59 + ($abstractField == null ? 43 : $abstractField.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        String $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        return result;
    }

    public String toString() {
        return "ArticleDetailVo(id=" + this.getId() + ", cid=" + this.getCid() + ", title=" + this.getTitle() + ", image=" + this.getImage() + ", author=" + this.getAuthor() + ", desc=" + this.getDesc() + ", abstractField=" + this.getAbstractField() + ", click=" + this.getClick() + ", content=" + this.getContent() + ", sort=" + this.getSort() + ", clickVirtual=" + this.getClickVirtual() + ", clickActual=" + this.getClickActual() + ", isShow=" + this.getIsShow() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

