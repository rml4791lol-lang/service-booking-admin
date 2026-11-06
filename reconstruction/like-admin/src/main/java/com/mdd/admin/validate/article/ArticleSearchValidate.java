/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.article.ArticleSearchValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u6587\u7ae0\u641c\u7d22\u53c2\u6570")
public class ArticleSearchValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u6587\u7ae0\u6807\u9898")
    private String title;
    @ApiModelProperty(value="\u6587\u7ae0\u5206\u7c7b")
    private Integer cid;
    @ApiModelProperty(value="\u662f\u5426\u663e\u793a")
    private Integer is_show;
    @ApiModelProperty(value="\u5f00\u59cb\u65f6\u95f4")
    private String startTime;
    @ApiModelProperty(value="\u7ed3\u675f\u65f6\u95f4")
    private String endTime;

    public String getTitle() {
        return this.title;
    }

    public Integer getCid() {
        return this.cid;
    }

    public Integer getIs_show() {
        return this.is_show;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public void setIs_show(Integer is_show) {
        this.is_show = is_show;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
        Integer this$is_show = this.getIs_show();
        Integer other$is_show = other.getIs_show();
        if (this$is_show == null ? other$is_show != null : !((Object)this$is_show).equals(other$is_show)) {
            return false;
        }
        String this$title = this.getTitle();
        String other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
            return false;
        }
        String this$startTime = this.getStartTime();
        String other$startTime = other.getStartTime();
        if (this$startTime == null ? other$startTime != null : !this$startTime.equals(other$startTime)) {
            return false;
        }
        String this$endTime = this.getEndTime();
        String other$endTime = other.getEndTime();
        return !(this$endTime == null ? other$endTime != null : !this$endTime.equals(other$endTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ArticleSearchValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $cid = this.getCid();
        result = result * 59 + ($cid == null ? 43 : ((Object)$cid).hashCode());
        Integer $is_show = this.getIs_show();
        result = result * 59 + ($is_show == null ? 43 : ((Object)$is_show).hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : $startTime.hashCode());
        String $endTime = this.getEndTime();
        result = result * 59 + ($endTime == null ? 43 : $endTime.hashCode());
        return result;
    }

    public String toString() {
        return "ArticleSearchValidate(title=" + this.getTitle() + ", cid=" + this.getCid() + ", is_show=" + this.getIs_show() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ")";
    }
}

