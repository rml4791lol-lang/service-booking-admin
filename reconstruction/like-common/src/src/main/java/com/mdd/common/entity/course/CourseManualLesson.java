/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.annotation.TableName
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.course;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@TableName(value="la_course_manual_lesson")
@ApiModel(value="\u4eba\u5de5\u52a0\u8bfe\u6263\u8bfe\u4e3b\u8bb0\u5f55")
public class CourseManualLesson
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value="\u73ed\u7ea7ID")
    private Integer classId;
    @ApiModelProperty(value="\u5b9e\u9645\u4e0a\u8bfe\u65e5\u671f")
    private String lessonDate;
    @ApiModelProperty(value="\u4e0a\u8bfe\u65f6\u6bb5")
    private String timeSlot;
    @ApiModelProperty(value="\u5b9e\u9645\u4e0a\u8bfe\u6559\u7ec3ID")
    private Integer instructorId;
    @ApiModelProperty(value="\u5b9e\u9645\u4e0a\u8bfe\u6559\u7ec3\u59d3\u540d")
    private String instructorName;
    @ApiModelProperty(value="\u73ed\u7ea7\u9ed8\u8ba4\u6559\u7ec3ID")
    private Integer defaultInstructorId;
    @ApiModelProperty(value="\u4e0a\u8bfe\u5730\u70b9")
    private String venue;
    @ApiModelProperty(value="\u6559\u7ec3\u7ed3\u8d39\u91d1\u989d")
    private BigDecimal feeAmount;
    @ApiModelProperty(value="\u6263\u8bfe\u5b66\u5458\u6570")
    private Integer studentCount;
    @ApiModelProperty(value="\u5907\u6ce8")
    private String remark;
    @ApiModelProperty(value="\u64cd\u4f5c\u7ba1\u7406\u5458ID")
    private Integer operatorId;
    @ApiModelProperty(value="\u64cd\u4f5c\u7ba1\u7406\u5458\u59d3\u540d")
    private String operatorName;
    @ApiModelProperty(value="\u5e42\u7b49\u8bf7\u6c42\u952e")
    private String requestKey;
    @ApiModelProperty(value="\u72b6\u6001:1=\u5df2\u5b8c\u6210")
    private Integer status;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Integer createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Integer updateTime;

    public Integer getId() {
        return this.id;
    }

    public String getRequestKey() {
        return this.requestKey;
    }

    public Integer getClassId() {
        return this.classId;
    }

    public String getLessonDate() {
        return this.lessonDate;
    }

    public String getTimeSlot() {
        return this.timeSlot;
    }

    public Integer getInstructorId() {
        return this.instructorId;
    }

    public String getInstructorName() {
        return this.instructorName;
    }

    public Integer getDefaultInstructorId() {
        return this.defaultInstructorId;
    }

    public String getVenue() {
        return this.venue;
    }

    public BigDecimal getFeeAmount() {
        return this.feeAmount;
    }

    public Integer getStudentCount() {
        return this.studentCount;
    }

    public String getRemark() {
        return this.remark;
    }

    public Integer getOperatorId() {
        return this.operatorId;
    }

    public String getOperatorName() {
        return this.operatorName;
    }


    public Integer getStatus() {
        return this.status;
    }

    public Integer getCreateTime() {
        return this.createTime;
    }

    public Integer getUpdateTime() {
        return this.updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public void setLessonDate(String lessonDate) {
        this.lessonDate = lessonDate;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public void setDefaultInstructorId(Integer defaultInstructorId) {
        this.defaultInstructorId = defaultInstructorId;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CourseManualLesson)) {
            return false;
        }
        CourseManualLesson other = (CourseManualLesson)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$classId = this.getClassId();
        Integer other$classId = other.getClassId();
        if (this$classId == null ? other$classId != null : !((Object)this$classId).equals(other$classId)) {
            return false;
        }
        Integer this$instructorId = this.getInstructorId();
        Integer other$instructorId = other.getInstructorId();
        if (this$instructorId == null ? other$instructorId != null : !((Object)this$instructorId).equals(other$instructorId)) {
            return false;
        }
        Integer this$defaultInstructorId = this.getDefaultInstructorId();
        Integer other$defaultInstructorId = other.getDefaultInstructorId();
        if (this$defaultInstructorId == null ? other$defaultInstructorId != null : !((Object)this$defaultInstructorId).equals(other$defaultInstructorId)) {
            return false;
        }
        Integer this$studentCount = this.getStudentCount();
        Integer other$studentCount = other.getStudentCount();
        if (this$studentCount == null ? other$studentCount != null : !((Object)this$studentCount).equals(other$studentCount)) {
            return false;
        }
        Integer this$operatorId = this.getOperatorId();
        Integer other$operatorId = other.getOperatorId();
        if (this$operatorId == null ? other$operatorId != null : !((Object)this$operatorId).equals(other$operatorId)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Integer this$createTime = this.getCreateTime();
        Integer other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Integer this$updateTime = this.getUpdateTime();
        Integer other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        String this$lessonDate = this.getLessonDate();
        String other$lessonDate = other.getLessonDate();
        if (this$lessonDate == null ? other$lessonDate != null : !this$lessonDate.equals(other$lessonDate)) {
            return false;
        }
        String this$timeSlot = this.getTimeSlot();
        String other$timeSlot = other.getTimeSlot();
        if (this$timeSlot == null ? other$timeSlot != null : !this$timeSlot.equals(other$timeSlot)) {
            return false;
        }
        String this$instructorName = this.getInstructorName();
        String other$instructorName = other.getInstructorName();
        if (this$instructorName == null ? other$instructorName != null : !this$instructorName.equals(other$instructorName)) {
            return false;
        }
        String this$venue = this.getVenue();
        String other$venue = other.getVenue();
        if (this$venue == null ? other$venue != null : !this$venue.equals(other$venue)) {
            return false;
        }
        BigDecimal this$feeAmount = this.getFeeAmount();
        BigDecimal other$feeAmount = other.getFeeAmount();
        if (this$feeAmount == null ? other$feeAmount != null : !((Object)this$feeAmount).equals(other$feeAmount)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        if (this$remark == null ? other$remark != null : !this$remark.equals(other$remark)) {
            return false;
        }
        String this$operatorName = this.getOperatorName();
        String other$operatorName = other.getOperatorName();
        return !(this$operatorName == null ? other$operatorName != null : !this$operatorName.equals(other$operatorName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseManualLesson;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $classId = this.getClassId();
        result = result * 59 + ($classId == null ? 43 : ((Object)$classId).hashCode());
        Integer $instructorId = this.getInstructorId();
        result = result * 59 + ($instructorId == null ? 43 : ((Object)$instructorId).hashCode());
        Integer $defaultInstructorId = this.getDefaultInstructorId();
        result = result * 59 + ($defaultInstructorId == null ? 43 : ((Object)$defaultInstructorId).hashCode());
        Integer $studentCount = this.getStudentCount();
        result = result * 59 + ($studentCount == null ? 43 : ((Object)$studentCount).hashCode());
        Integer $operatorId = this.getOperatorId();
        result = result * 59 + ($operatorId == null ? 43 : ((Object)$operatorId).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Integer $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $lessonDate = this.getLessonDate();
        result = result * 59 + ($lessonDate == null ? 43 : $lessonDate.hashCode());
        String $timeSlot = this.getTimeSlot();
        result = result * 59 + ($timeSlot == null ? 43 : $timeSlot.hashCode());
        String $instructorName = this.getInstructorName();
        result = result * 59 + ($instructorName == null ? 43 : $instructorName.hashCode());
        String $venue = this.getVenue();
        result = result * 59 + ($venue == null ? 43 : $venue.hashCode());
        BigDecimal $feeAmount = this.getFeeAmount();
        result = result * 59 + ($feeAmount == null ? 43 : ((Object)$feeAmount).hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        String $operatorName = this.getOperatorName();
        result = result * 59 + ($operatorName == null ? 43 : $operatorName.hashCode());
        return result;
    }

    public String toString() {
        return "CourseManualLesson(id=" + this.getId() + ", classId=" + this.getClassId() + ", lessonDate=" + this.getLessonDate() + ", timeSlot=" + this.getTimeSlot() + ", instructorId=" + this.getInstructorId() + ", instructorName=" + this.getInstructorName() + ", defaultInstructorId=" + this.getDefaultInstructorId() + ", venue=" + this.getVenue() + ", feeAmount=" + this.getFeeAmount() + ", studentCount=" + this.getStudentCount() + ", remark=" + this.getRemark() + ", operatorId=" + this.getOperatorId() + ", operatorName=" + this.getOperatorName() + ", status=" + this.getStatus() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}
