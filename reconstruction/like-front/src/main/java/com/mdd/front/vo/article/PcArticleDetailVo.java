/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.mdd.front.vo.article.PcArticleDetailVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.vo.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="PC\u6587\u7ae0\u8be6\u60c5Vo")
public class PcArticleDetailVo
implements Serializable {
    @ApiModelProperty(value="\u6587\u7ae0ID")
    private Integer id;
    @ApiModelProperty(value="\u6458\u8981")
    @JsonProperty(value="abstract")
    private String abstractField;
    @ApiModelProperty(value="\u6587\u7ae0\u4f5c\u8005")
    private String author;
    @ApiModelProperty(value="\u5206\u7c7b\u540d\u79f0")
    private String cateName;
    @ApiModelProperty(value="\u5206\u7c7bID")
    private Integer cid;
    @ApiModelProperty(value="\u6d4f\u89c8\u6570\u91cf")
    private Integer click;
    @ApiModelProperty(value="\u6587\u7ae0\u6807\u9898")
    private String title;
    @ApiModelProperty(value="\u662f\u5426\u6536\u85cf")
    private Boolean collect;
    @ApiModelProperty(value="\u6587\u7ae0\u5185\u5bb9")
    private String content;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private String updateTime;
    @ApiModelProperty(value="\u7b80\u4ecb")
    private String desc;
    @ApiModelProperty(value="\u6587\u7ae0\u5c01\u9762")
    private String image;
    @ApiModelProperty(value="\u662f\u5426\u663e\u793a: [0=\u5426, 1=\u662f]")
    private Integer isShow;
    @ApiModelProperty(value="\u6392\u5e8f\u7f16\u53f7")
    private Integer sort;
    @ApiModelProperty(value="\u4e0a\u4e00\u9875")
    private Object last;
    @ApiModelProperty(value="\u4e0b\u4e00\u9875")
    private Object next;
    @ApiModelProperty(value="\u6700\u65b0\u63a8\u8350")
    @JsonProperty(value="new")
    private Object news;

    public Integer getId() {
        return this.id;
    }

    public String getAbstractField() {
        return this.abstractField;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getCateName() {
        return this.cateName;
    }

    public Integer getCid() {
        return this.cid;
    }

    public Integer getClick() {
        return this.click;
    }

    public String getTitle() {
        return this.title;
    }

    public Boolean getCollect() {
        return this.collect;
    }

    public String getContent() {
        return this.content;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getImage() {
        return this.image;
    }

    public Integer getIsShow() {
        return this.isShow;
    }

    public Integer getSort() {
        return this.sort;
    }

    public Object getLast() {
        return this.last;
    }

    public Object getNext() {
        return this.next;
    }

    public Object getNews() {
        return this.news;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty(value="abstract")
    public void setAbstractField(String abstractField) {
        this.abstractField = abstractField;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public void setClick(Integer click) {
        this.click = click;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCollect(Boolean collect) {
        this.collect = collect;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setLast(Object last) {
        this.last = last;
    }

    public void setNext(Object next) {
        this.next = next;
    }

    @JsonProperty(value="new")
    public void setNews(Object news) {
        this.news = news;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PcArticleDetailVo)) {
            return false;
        }
        PcArticleDetailVo other = (PcArticleDetailVo)o;
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
        Boolean this$collect = this.getCollect();
        Boolean other$collect = other.getCollect();
        if (this$collect == null ? other$collect != null : !((Object)this$collect).equals(other$collect)) {
            return false;
        }
        Integer this$isShow = this.getIsShow();
        Integer other$isShow = other.getIsShow();
        if (this$isShow == null ? other$isShow != null : !((Object)this$isShow).equals(other$isShow)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        String this$abstractField = this.getAbstractField();
        String other$abstractField = other.getAbstractField();
        if (this$abstractField == null ? other$abstractField != null : !this$abstractField.equals(other$abstractField)) {
            return false;
        }
        String this$author = this.getAuthor();
        String other$author = other.getAuthor();
        if (this$author == null ? other$author != null : !this$author.equals(other$author)) {
            return false;
        }
        String this$cateName = this.getCateName();
        String other$cateName = other.getCateName();
        if (this$cateName == null ? other$cateName != null : !this$cateName.equals(other$cateName)) {
            return false;
        }
        String this$title = this.getTitle();
        String other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
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
        if (this$updateTime == null ? other$updateTime != null : !this$updateTime.equals(other$updateTime)) {
            return false;
        }
        String this$desc = this.getDesc();
        String other$desc = other.getDesc();
        if (this$desc == null ? other$desc != null : !this$desc.equals(other$desc)) {
            return false;
        }
        String this$image = this.getImage();
        String other$image = other.getImage();
        if (this$image == null ? other$image != null : !this$image.equals(other$image)) {
            return false;
        }
        Object this$last = this.getLast();
        Object other$last = other.getLast();
        if (this$last == null ? other$last != null : !this$last.equals(other$last)) {
            return false;
        }
        Object this$next = this.getNext();
        Object other$next = other.getNext();
        if (this$next == null ? other$next != null : !this$next.equals(other$next)) {
            return false;
        }
        Object this$news = this.getNews();
        Object other$news = other.getNews();
        return !(this$news == null ? other$news != null : !this$news.equals(other$news));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PcArticleDetailVo;
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
        Boolean $collect = this.getCollect();
        result = result * 59 + ($collect == null ? 43 : ((Object)$collect).hashCode());
        Integer $isShow = this.getIsShow();
        result = result * 59 + ($isShow == null ? 43 : ((Object)$isShow).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        String $abstractField = this.getAbstractField();
        result = result * 59 + ($abstractField == null ? 43 : $abstractField.hashCode());
        String $author = this.getAuthor();
        result = result * 59 + ($author == null ? 43 : $author.hashCode());
        String $cateName = this.getCateName();
        result = result * 59 + ($cateName == null ? 43 : $cateName.hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        String $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        String $desc = this.getDesc();
        result = result * 59 + ($desc == null ? 43 : $desc.hashCode());
        String $image = this.getImage();
        result = result * 59 + ($image == null ? 43 : $image.hashCode());
        Object $last = this.getLast();
        result = result * 59 + ($last == null ? 43 : $last.hashCode());
        Object $next = this.getNext();
        result = result * 59 + ($next == null ? 43 : $next.hashCode());
        Object $news = this.getNews();
        result = result * 59 + ($news == null ? 43 : $news.hashCode());
        return result;
    }

    public String toString() {
        return "PcArticleDetailVo(id=" + this.getId() + ", abstractField=" + this.getAbstractField() + ", author=" + this.getAuthor() + ", cateName=" + this.getCateName() + ", cid=" + this.getCid() + ", click=" + this.getClick() + ", title=" + this.getTitle() + ", collect=" + this.getCollect() + ", content=" + this.getContent() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", desc=" + this.getDesc() + ", image=" + this.getImage() + ", isShow=" + this.getIsShow() + ", sort=" + this.getSort() + ", last=" + this.getLast() + ", next=" + this.getNext() + ", news=" + this.getNews() + ")";
    }
}

