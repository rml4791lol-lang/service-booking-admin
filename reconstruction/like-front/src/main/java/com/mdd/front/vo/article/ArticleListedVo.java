/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.vo.article.ArticleListedVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.vo.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u6587\u7ae0\u5217\u8868Vo")
public class ArticleListedVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u6587\u7ae0ID")
    private Integer id;
    @ApiModelProperty(value="\u5206\u7c7bID")
    private Integer cid;
    @ApiModelProperty(value="\u6587\u7ae0\u6807\u9898")
    private String title;
    @ApiModelProperty(value="\u6587\u7ae0\u5c01\u9762")
    private String image;
    @ApiModelProperty(value="\u6587\u7ae0\u7b80\u4ecb")
    private String desc;
    @ApiModelProperty(value="\u6d4f\u89c8\u6570\u91cf")
    private Integer click;
    @ApiModelProperty(value="\u662f\u5426\u6536\u85cf")
    private Boolean collect;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;

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

    public String getDesc() {
        return this.desc;
    }

    public Integer getClick() {
        return this.click;
    }

    public Boolean getCollect() {
        return this.collect;
    }

    public String getCreateTime() {
        return this.createTime;
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

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setClick(Integer click) {
        this.click = click;
    }

    public void setCollect(Boolean collect) {
        this.collect = collect;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArticleListedVo)) {
            return false;
        }
        ArticleListedVo other = (ArticleListedVo)o;
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
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ArticleListedVo;
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
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $image = this.getImage();
        result = result * 59 + ($image == null ? 43 : $image.hashCode());
        String $desc = this.getDesc();
        result = result * 59 + ($desc == null ? 43 : $desc.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        return result;
    }

    public String toString() {
        return "ArticleListedVo(id=" + this.getId() + ", cid=" + this.getCid() + ", title=" + this.getTitle() + ", image=" + this.getImage() + ", desc=" + this.getDesc() + ", click=" + this.getClick() + ", collect=" + this.getCollect() + ", createTime=" + this.getCreateTime() + ")";
    }
}

