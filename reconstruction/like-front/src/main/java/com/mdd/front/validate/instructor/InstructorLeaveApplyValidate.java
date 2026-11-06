/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.instructor.InstructorLeaveApplyValidate
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.front.validate.instructor;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class InstructorLeaveApplyValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="\u5f00\u59cb\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a")
    private @NotNull(message="\u5f00\u59cb\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a") Long startTime;
    @NotNull(message="\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a")
    private @NotNull(message="\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a") Long endTime;
    @NotEmpty(message="\u8bf7\u5047\u539f\u56e0\u4e0d\u80fd\u4e3a\u7a7a")
    private @NotEmpty(message="\u8bf7\u5047\u539f\u56e0\u4e0d\u80fd\u4e3a\u7a7a") String reason;
    @NotEmpty(message="\u8be6\u7ec6\u8bf4\u660e\u4e0d\u80fd\u4e3a\u7a7a")
    private @NotEmpty(message="\u8be6\u7ec6\u8bf4\u660e\u4e0d\u80fd\u4e3a\u7a7a") String detail;

    @NotNull(message="\u5f00\u59cb\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u5f00\u59cb\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a") Long getStartTime() {
        return this.startTime;
    }

    @NotNull(message="\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a") Long getEndTime() {
        return this.endTime;
    }

    public String getReason() {
        return this.reason;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setStartTime(@NotNull(message="\u5f00\u59cb\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u5f00\u59cb\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a") Long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(@NotNull(message="\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a") Long endTime) {
        this.endTime = endTime;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InstructorLeaveApplyValidate)) {
            return false;
        }
        InstructorLeaveApplyValidate other = (InstructorLeaveApplyValidate)o;
        if (!other.canEqual((Object)this)) {
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
        return other instanceof InstructorLeaveApplyValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : ((Object)$startTime).hashCode());
        Long $endTime = this.getEndTime();
        result = result * 59 + ($endTime == null ? 43 : ((Object)$endTime).hashCode());
        String $reason = this.getReason();
        result = result * 59 + ($reason == null ? 43 : $reason.hashCode());
        String $detail = this.getDetail();
        result = result * 59 + ($detail == null ? 43 : $detail.hashCode());
        return result;
    }

    public String toString() {
        return "InstructorLeaveApplyValidate(startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", reason=" + this.getReason() + ", detail=" + this.getDetail() + ")";
    }
}

