/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.vo.article.ArticleCollectVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.vo.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u6587\u7ae0\u6536\u85cfVo")
public class ArticleCollectVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u6536\u85cf\u4e3b\u952e")
    private Integer id;
    @ApiModelProperty(value="\u6587\u7ae0ID")
    private Integer articleId;
    @ApiModelProperty(value="\u6587\u7ae0\u6807\u9898")
    private String title;
    @ApiModelProperty(value="\u6587\u7ae0\u5c01\u9762")
    private String image;
    @ApiModelProperty(value="\u6587\u7ae0\u7b80\u4ecb")
    private String desc;
    @ApiModelProperty(value="\u662f\u5426\u663e\u793a:1-\u662f.0-\u5426")
    private Integer isShow;
    @ApiModelProperty(value="\u865a\u62df\u6d4f\u89c8\u91cf")
    private Integer clickVirtual;
    @ApiModelProperty(value="\u5b9e\u9645\u6d4f\u89c8\u91cf")
    private Integer clickActual;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Object createTime;
    @ApiModelProperty(value="\u6536\u96c6\u65f6\u95f4")
    private Object collectTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getArticleId() {
        return this.articleId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getImage() {
        return this.image;
    }

    public String getDesc() {
        return this.desc;
    }

    public Integer getIsShow() {
        return this.isShow;
    }

    public Integer getClickVirtual() {
        return this.clickVirtual;
    }

    public Integer getClickActual() {
        return this.clickActual;
    }

    public Object getCreateTime() {
        return this.createTime;
    }

    public Object getCollectTime() {
        return this.collectTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public void setClickVirtual(Integer clickVirtual) {
        this.clickVirtual = clickVirtual;
    }

    public void setClickActual(Integer clickActual) {
        this.clickActual = clickActual;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public void setCollectTime(Object collectTime) {
        this.collectTime = collectTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArticleCollectVo)) {
            return false;
        }
        ArticleCollectVo other = (ArticleCollectVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$articleId = this.getArticleId();
        Integer other$articleId = other.getArticleId();
        if (this$articleId == null ? other$articleId != null : !((Object)this$articleId).equals(other$articleId)) {
            return false;
        }
        Integer this$isShow = this.getIsShow();
        Integer other$isShow = other.getIsShow();
        if (this$isShow == null ? other$isShow != null : !((Object)this$isShow).equals(other$isShow)) {
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
        String this$desc = this.getDesc();
        String other$desc = other.getDesc();
        if (this$desc == null ? other$desc != null : !this$desc.equals(other$desc)) {
            return false;
        }
        Object this$createTime = this.getCreateTime();
        Object other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime)) {
            return false;
        }
        Object this$collectTime = this.getCollectTime();
        Object other$collectTime = other.getCollectTime();
        return !(this$collectTime == null ? other$collectTime != null : !this$collectTime.equals(other$collectTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ArticleCollectVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $articleId = this.getArticleId();
        result = result * 59 + ($articleId == null ? 43 : ((Object)$articleId).hashCode());
        Integer $isShow = this.getIsShow();
        result = result * 59 + ($isShow == null ? 43 : ((Object)$isShow).hashCode());
        Integer $clickVirtual = this.getClickVirtual();
        result = result * 59 + ($clickVirtual == null ? 43 : ((Object)$clickVirtual).hashCode());
        Integer $clickActual = this.getClickActual();
        result = result * 59 + ($clickActual == null ? 43 : ((Object)$clickActual).hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $image = this.getImage();
        result = result * 59 + ($image == null ? 43 : $image.hashCode());
        String $desc = this.getDesc();
        result = result * 59 + ($desc == null ? 43 : $desc.hashCode());
        Object $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        Object $collectTime = this.getCollectTime();
        result = result * 59 + ($collectTime == null ? 43 : $collectTime.hashCode());
        return result;
    }

    public String toString() {
        return "ArticleCollectVo(id=" + this.getId() + ", articleId=" + this.getArticleId() + ", title=" + this.getTitle() + ", image=" + this.getImage() + ", desc=" + this.getDesc() + ", isShow=" + this.getIsShow() + ", clickVirtual=" + this.getClickVirtual() + ", clickActual=" + this.getClickActual() + ", createTime=" + this.getCreateTime() + ", collectTime=" + this.getCollectTime() + ")";
    }
}

