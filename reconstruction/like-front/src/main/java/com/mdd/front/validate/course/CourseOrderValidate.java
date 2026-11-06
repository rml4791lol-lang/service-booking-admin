/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.course.CourseOrderValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.front.validate.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u8bfe\u7a0b\u4e0b\u5355\u53c2\u6570")
public class CourseOrderValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="courseId\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u8bfe\u7a0bID", required=true)
    private @NotNull(message="courseId\u4e0d\u80fd\u4e3a\u7a7a") Integer courseId;
    @ApiModelProperty(value="\u62b5\u6263\u5238ID")
    private Integer couponId;
    @NotNull(message="payWay\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u652f\u4ed8\u65b9\u5f0f: 1=\u4f59\u989d,2=\u5fae\u4fe1,3=\u652f\u4ed8\u5b9d", required=true)
    private @NotNull(message="payWay\u4e0d\u80fd\u4e3a\u7a7a") Integer payWay;
    @NotNull(message="\u8bf7\u9009\u62e9\u62a5\u540d\u5b66\u5458")
    @ApiModelProperty(value="\u5b66\u5458ID", required=true)
    private @NotNull(message="\u8bf7\u9009\u62e9\u62a5\u540d\u5b66\u5458") Integer studentId;
    @NotNull(message="lessonCount\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u8d2d\u4e70\u8bfe\u65f6\u6570", required=true)
    private @NotNull(message="lessonCount\u4e0d\u80fd\u4e3a\u7a7a") Integer lessonCount;

    @NotNull(message="courseId\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="courseId\u4e0d\u80fd\u4e3a\u7a7a") Integer getCourseId() {
        return this.courseId;
    }

    public Integer getCouponId() {
        return this.couponId;
    }

    @NotNull(message="payWay\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="payWay\u4e0d\u80fd\u4e3a\u7a7a") Integer getPayWay() {
        return this.payWay;
    }

    @NotNull(message="\u8bf7\u9009\u62e9\u62a5\u540d\u5b66\u5458")
    public @NotNull(message="\u8bf7\u9009\u62e9\u62a5\u540d\u5b66\u5458") Integer getStudentId() {
        return this.studentId;
    }

    @NotNull(message="lessonCount\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="lessonCount\u4e0d\u80fd\u4e3a\u7a7a") Integer getLessonCount() {
        return this.lessonCount;
    }

    public void setCourseId(@NotNull(message="courseId\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="courseId\u4e0d\u80fd\u4e3a\u7a7a") Integer courseId) {
        this.courseId = courseId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public void setPayWay(@NotNull(message="payWay\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="payWay\u4e0d\u80fd\u4e3a\u7a7a") Integer payWay) {
        this.payWay = payWay;
    }

    public void setStudentId(@NotNull(message="\u8bf7\u9009\u62e9\u62a5\u540d\u5b66\u5458") @NotNull(message="\u8bf7\u9009\u62e9\u62a5\u540d\u5b66\u5458") Integer studentId) {
        this.studentId = studentId;
    }

    public void setLessonCount(@NotNull(message="lessonCount\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="lessonCount\u4e0d\u80fd\u4e3a\u7a7a") Integer lessonCount) {
        this.lessonCount = lessonCount;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CourseOrderValidate)) {
            return false;
        }
        CourseOrderValidate other = (CourseOrderValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$courseId = this.getCourseId();
        Integer other$courseId = other.getCourseId();
        if (this$courseId == null ? other$courseId != null : !((Object)this$courseId).equals(other$courseId)) {
            return false;
        }
        Integer this$couponId = this.getCouponId();
        Integer other$couponId = other.getCouponId();
        if (this$couponId == null ? other$couponId != null : !((Object)this$couponId).equals(other$couponId)) {
            return false;
        }
        Integer this$payWay = this.getPayWay();
        Integer other$payWay = other.getPayWay();
        if (this$payWay == null ? other$payWay != null : !((Object)this$payWay).equals(other$payWay)) {
            return false;
        }
        Integer this$studentId = this.getStudentId();
        Integer other$studentId = other.getStudentId();
        if (this$studentId == null ? other$studentId != null : !((Object)this$studentId).equals(other$studentId)) {
            return false;
        }
        Integer this$lessonCount = this.getLessonCount();
        Integer other$lessonCount = other.getLessonCount();
        return !(this$lessonCount == null ? other$lessonCount != null : !((Object)this$lessonCount).equals(other$lessonCount));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseOrderValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $courseId = this.getCourseId();
        result = result * 59 + ($courseId == null ? 43 : ((Object)$courseId).hashCode());
        Integer $couponId = this.getCouponId();
        result = result * 59 + ($couponId == null ? 43 : ((Object)$couponId).hashCode());
        Integer $payWay = this.getPayWay();
        result = result * 59 + ($payWay == null ? 43 : ((Object)$payWay).hashCode());
        Integer $studentId = this.getStudentId();
        result = result * 59 + ($studentId == null ? 43 : ((Object)$studentId).hashCode());
        Integer $lessonCount = this.getLessonCount();
        result = result * 59 + ($lessonCount == null ? 43 : ((Object)$lessonCount).hashCode());
        return result;
    }

    public String toString() {
        return "CourseOrderValidate(courseId=" + this.getCourseId() + ", couponId=" + this.getCouponId() + ", payWay=" + this.getPayWay() + ", studentId=" + this.getStudentId() + ", lessonCount=" + this.getLessonCount() + ")";
    }
}

