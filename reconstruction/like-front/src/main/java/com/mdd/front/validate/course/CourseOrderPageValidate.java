/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.validate.course.CourseOrderPageValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.validate.course;

import com.mdd.front.validate.common.PageValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="\u8bfe\u7a0b\u8ba2\u5355\u5206\u9875\u53c2\u6570")
public class CourseOrderPageValidate
extends PageValidate {
    @ApiModelProperty(value="\u72b6\u6001: pending=\u5f85\u652f\u4ed8, paid=\u5df2\u652f\u4ed8, completed=\u5df2\u5b8c\u6210, cancelled=\u5df2\u53d6\u6d88 (\u975e\u5fc5\u586b: \u7a7a\u8868\u793a\u5168\u90e8)")
    private String status;
    @ApiModelProperty(value="\u641c\u7d22\u5173\u952e\u5b57: \u8ba2\u5355\u53f7/\u8bfe\u7a0b\u540d\u79f0\u7b49")
    private String keyword;

    public String getStatus() {
        return this.status;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String toString() {
        return "CourseOrderPageValidate(status=" + this.getStatus() + ", keyword=" + this.getKeyword() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CourseOrderPageValidate)) {
            return false;
        }
        CourseOrderPageValidate other = (CourseOrderPageValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$keyword = this.getKeyword();
        String other$keyword = other.getKeyword();
        return !(this$keyword == null ? other$keyword != null : !this$keyword.equals(other$keyword));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseOrderPageValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $keyword = this.getKeyword();
        result = result * 59 + ($keyword == null ? 43 : $keyword.hashCode());
        return result;
    }
}

