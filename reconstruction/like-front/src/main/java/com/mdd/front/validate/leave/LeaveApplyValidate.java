/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.leave.LeaveApplyValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.front.validate.leave;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u8bf7\u5047\u7533\u8bf7\u53c2\u6570")
public class LeaveApplyValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="\u73ed\u7ea7ID\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u5173\u8054\u73ed\u7ea7ID", required=true)
    private @NotNull(message="\u73ed\u7ea7ID\u4e0d\u80fd\u4e3a\u7a7a") Integer classId;
    @ApiModelProperty(value="\u5173\u8054\u5b66\u5458ID")
    private Integer studentId;
    @NotNull(message="\u8bf7\u5047\u5f00\u59cb\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u8bf7\u5047\u5f00\u59cb\u65f6\u95f4(\u65f6\u95f4\u6233)", required=true)
    private @NotNull(message="\u8bf7\u5047\u5f00\u59cb\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a") Long startTime;
    @NotNull(message="\u8bf7\u5047\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u8bf7\u5047\u7ed3\u675f\u65f6\u95f4(\u65f6\u95f4\u6233)", required=true)
    private @NotNull(message="\u8bf7\u5047\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a") Long endTime;
    @NotNull(message="\u8bf7\u5047\u539f\u56e0\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u8bf7\u5047\u539f\u56e0\u6807\u7b7e", required=true)
    private @NotNull(message="\u8bf7\u5047\u539f\u56e0\u4e0d\u80fd\u4e3a\u7a7a") String reason;
    @ApiModelProperty(value="\u8be6\u7ec6\u8bf4\u660e")
    private String detail;
    @ApiModelProperty(value="\u8bf7\u5047\u4eba\u7c7b\u578b 1\u5b66\u751f 2\u6559\u7ec3 \uff08\u524d\u7aef\u9875\u9762\u53ef\u4ee5\u9009\u586b\uff09")
    private Integer applicantType;

    @NotNull(message="\u73ed\u7ea7ID\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u73ed\u7ea7ID\u4e0d\u80fd\u4e3a\u7a7a") Integer getClassId() {
        return this.classId;
    }

    public Integer getStudentId() {
        return this.studentId;
    }

    @NotNull(message="\u8bf7\u5047\u5f00\u59cb\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u8bf7\u5047\u5f00\u59cb\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a") Long getStartTime() {
        return this.startTime;
    }

    @NotNull(message="\u8bf7\u5047\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u8bf7\u5047\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a") Long getEndTime() {
        return this.endTime;
    }

    @NotNull(message="\u8bf7\u5047\u539f\u56e0\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u8bf7\u5047\u539f\u56e0\u4e0d\u80fd\u4e3a\u7a7a") String getReason() {
        return this.reason;
    }

    public String getDetail() {
        return this.detail;
    }

    public Integer getApplicantType() {
        return this.applicantType;
    }

    public void setClassId(@NotNull(message="\u73ed\u7ea7ID\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u73ed\u7ea7ID\u4e0d\u80fd\u4e3a\u7a7a") Integer classId) {
        this.classId = classId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public void setStartTime(@NotNull(message="\u8bf7\u5047\u5f00\u59cb\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u8bf7\u5047\u5f00\u59cb\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a") Long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(@NotNull(message="\u8bf7\u5047\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u8bf7\u5047\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a") Long endTime) {
        this.endTime = endTime;
    }

    public void setReason(@NotNull(message="\u8bf7\u5047\u539f\u56e0\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u8bf7\u5047\u539f\u56e0\u4e0d\u80fd\u4e3a\u7a7a") String reason) {
        this.reason = reason;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setApplicantType(Integer applicantType) {
        this.applicantType = applicantType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LeaveApplyValidate)) {
            return false;
        }
        LeaveApplyValidate other = (LeaveApplyValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$classId = this.getClassId();
        Integer other$classId = other.getClassId();
        if (this$classId == null ? other$classId != null : !((Object)this$classId).equals(other$classId)) {
            return false;
        }
        Integer this$studentId = this.getStudentId();
        Integer other$studentId = other.getStudentId();
        if (this$studentId == null ? other$studentId != null : !((Object)this$studentId).equals(other$studentId)) {
            return false;
        }
        Long this$startTime = this.getStartTime();
        Long other$startTime = other.getStartTime();
        if (this$startTime == null ? other$startTime != null : !((Object)this$startTime).equals(other$startTime)) {
            return false;
        }
        Long this$endTime = this.getEndTime();
        Long other$endTime = other.getEndTime();
        if (this$endTime == null ? other$endTime != null : !((Object)this$endTime).equals(other$endTime)) {
            return false;
        }
        Integer this$applicantType = this.getApplicantType();
        Integer other$applicantType = other.getApplicantType();
        if (this$applicantType == null ? other$applicantType != null : !((Object)this$applicantType).equals(other$applicantType)) {
            return false;
        }
        String this$reason = this.getReason();
        String other$reason = other.getReason();
        if (this$reason == null ? other$reason != null : !this$reason.equals(other$reason)) {
            return false;
        }
        String this$detail = this.getDetail();
        String other$detail = other.getDetail();
        return !(this$detail == null ? other$detail != null : !this$detail.equals(other$detail));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LeaveApplyValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $classId = this.getClassId();
        result = result * 59 + ($classId == null ? 43 : ((Object)$classId).hashCode());
        Integer $studentId = this.getStudentId();
        result = result * 59 + ($studentId == null ? 43 : ((Object)$studentId).hashCode());
        Long $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : ((Object)$startTime).hashCode());
        Long $endTime = this.getEndTime();
        result = result * 59 + ($endTime == null ? 43 : ((Object)$endTime).hashCode());
        Integer $applicantType = this.getApplicantType();
        result = result * 59 + ($applicantType == null ? 43 : ((Object)$applicantType).hashCode());
        String $reason = this.getReason();
        result = result * 59 + ($reason == null ? 43 : $reason.hashCode());
        String $detail = this.getDetail();
        result = result * 59 + ($detail == null ? 43 : $detail.hashCode());
        return result;
    }

    public String toString() {
        return "LeaveApplyValidate(classId=" + this.getClassId() + ", studentId=" + this.getStudentId() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", reason=" + this.getReason() + ", detail=" + this.getDetail() + ", applicantType=" + this.getApplicantType() + ")";
    }
}

