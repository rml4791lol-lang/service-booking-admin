/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.validate.course.CourseClassValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.validate.course;

import com.mdd.front.validate.common.PageValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="\u73ed\u7ea7\u5217\u8868\u53c2\u6570")
public class CourseClassValidate
extends PageValidate {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u7eac\u5ea6")
    private String lat;
    @ApiModelProperty(value="\u7ecf\u5ea6")
    private String lng;
    @ApiModelProperty(value="\u641c\u7d22\u5173\u952e\u8bcd")
    private String keyword;

    public String getLat() {
        return this.lat;
    }

    public String getLng() {
        return this.lng;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String toString() {
        return "CourseClassValidate(lat=" + this.getLat() + ", lng=" + this.getLng() + ", keyword=" + this.getKeyword() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CourseClassValidate)) {
            return false;
        }
        CourseClassValidate other = (CourseClassValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        String this$lat = this.getLat();
        String other$lat = other.getLat();
        if (this$lat == null ? other$lat != null : !this$lat.equals(other$lat)) {
            return false;
        }
        String this$lng = this.getLng();
        String other$lng = other.getLng();
        if (this$lng == null ? other$lng != null : !this$lng.equals(other$lng)) {
            return false;
        }
        String this$keyword = this.getKeyword();
        String other$keyword = other.getKeyword();
        return !(this$keyword == null ? other$keyword != null : !this$keyword.equals(other$keyword));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseClassValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        String $lat = this.getLat();
        result = result * 59 + ($lat == null ? 43 : $lat.hashCode());
        String $lng = this.getLng();
        result = result * 59 + ($lng == null ? 43 : $lng.hashCode());
        String $keyword = this.getKeyword();
        result = result * 59 + ($keyword == null ? 43 : $keyword.hashCode());
        return result;
    }
}

