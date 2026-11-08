/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableField
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u6587\u7ae0\u5b9e\u4f53")
public class Article
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u5206\u7c7b")
    private Integer cid;
    @ApiModelProperty(value="\u6807\u9898")
    private String title;
    @ApiModelProperty(value="\u7b80\u4ecb")
    @TableField(value="`desc`")
    private String desc;
    @ApiModelProperty(value="\u6458\u8981")
    @TableField(value="abstract")
    private String abstractField;
    @ApiModelProperty(value="\u5c01\u9762")
    private String image;
    @ApiModelProperty(value="\u5185\u5bb9")
    private String content;
    @ApiModelProperty(value="\u4f5c\u8005")
    private String author;
    @ApiModelProperty(value="\u865a\u62df\u6d4f\u89c8\u91cf")
    private Integer clickVirtual;
    @ApiModelProperty(value="\u5b9e\u9645\u6d4f\u89c8\u91cf")
    private Integer clickActual;
    @ApiModelProperty(value="\u6392\u5e8f")
    private Integer sort;
    @ApiModelProperty(value="\u662f\u5426\u663e\u793a: [0=\u5426, 1=\u662f]")
    private Integer isShow;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Long updateTime;
    @ApiModelProperty(value="\u5220\u9664\u65f6\u95f4")
    private Long deleteTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getCid() {
        return this.cid;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getAbstractField() {
        return this.abstractField;
    }

    public String getImage() {
        return this.image;
    }

    public String getContent() {
        return this.content;
    }

    public String getAuthor() {
        return this.author;
    }

    public Integer getClickVirtual() {
        return this.clickVirtual;
    }

    public Integer getClickActual() {
        return this.clickActual;
    }

    public Integer getSort() {
        return this.sort;
    }

    public Integer getIsShow() {
        return this.isShow;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public Long getDeleteTime() {
        return this.deleteTime;
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

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setAbstractField(String abstractField) {
        this.abstractField = abstractField;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setClickVirtual(Integer clickVirtual) {
        this.clickVirtual = clickVirtual;
    }

    public void setClickActual(Integer clickActual) {
        this.clickActual = clickActual;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleteTime(Long deleteTime) {
        this.deleteTime = deleteTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        Article other = (Article)o;
        if (!other.canEqual(this)) {
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
        Long this$createTime = this.getCreateTime();
        Long other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Long this$updateTime = this.getUpdateTime();
        Long other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        Long this$deleteTime = this.getDeleteTime();
        Long other$deleteTime = other.getDeleteTime();
        if (this$deleteTime == null ? other$deleteTime != null : !((Object)this$deleteTime).equals(other$deleteTime)) {
            return false;
        }
        String this$title = this.getTitle();
        String other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
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
        String this$image = this.getImage();
        String other$image = other.getImage();
        if (this$image == null ? other$image != null : !this$image.equals(other$image)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
            return false;
        }
        String this$author = this.getAuthor();
        String other$author = other.getAuthor();
        return !(this$author == null ? other$author != null : !this$author.equals(other$author));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Article;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $cid = this.getCid();
        result = result * 59 + ($cid == null ? 43 : ((Object)$cid).hashCode());
        Integer $clickVirtual = this.getClickVirtual();
        result = result * 59 + ($clickVirtual == null ? 43 : ((Object)$clickVirtual).hashCode());
        Integer $clickActual = this.getClickActual();
        result = result * 59 + ($clickActual == null ? 43 : ((Object)$clickActual).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        Integer $isShow = this.getIsShow();
        result = result * 59 + ($isShow == null ? 43 : ((Object)$isShow).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $desc = this.getDesc();
        result = result * 59 + ($desc == null ? 43 : $desc.hashCode());
        String $abstractField = this.getAbstractField();
        result = result * 59 + ($abstractField == null ? 43 : $abstractField.hashCode());
        String $image = this.getImage();
        result = result * 59 + ($image == null ? 43 : $image.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        String $author = this.getAuthor();
        result = result * 59 + ($author == null ? 43 : $author.hashCode());
        return result;
    }

    public String toString() {
        return "Article(id=" + this.getId() + ", cid=" + this.getCid() + ", title=" + this.getTitle() + ", desc=" + this.getDesc() + ", abstractField=" + this.getAbstractField() + ", image=" + this.getImage() + ", content=" + this.getContent() + ", author=" + this.getAuthor() + ", clickVirtual=" + this.getClickVirtual() + ", clickActual=" + this.getClickActual() + ", sort=" + this.getSort() + ", isShow=" + this.getIsShow() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ")";
    }
}

