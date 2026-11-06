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

@TableName(value="ys_course_lesson")
@ApiModel(value="\u8bfe\u8282\u9884\u751f\u6210\u8868")
public class CourseLesson
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="\u4e3b\u952eID")
    private Integer id;
    @ApiModelProperty(value="\u73ed\u7ea7ID")
    private Integer classId;
    @ApiModelProperty(value="\u7b2c\u51e0\u8282\u8bfe")
    private Integer lessonNo;
    @ApiModelProperty(value="\u8bfe\u8282\u7c7b\u578b: 0=\u6b63\u8bfe 1=\u4e34\u65f6\u52a0\u8bfe")
    private Integer lessonType;
    @ApiModelProperty(value="\u4e0a\u8bfe\u65e5\u671f yyyy-MM-dd")
    private String lessonDate;
    @ApiModelProperty(value="\u5468\u51e0(1-7)")
    private Integer weekDay;
    @ApiModelProperty(value="\u4e0a\u8bfe\u65f6\u6bb5")
    private String timeSlot;
    @ApiModelProperty(value="\u4e0a\u8bfe\u5730\u70b9")
    private String venue;
    @ApiModelProperty(value="\u7eac\u5ea6")
    private String latitude;
    @ApiModelProperty(value="\u7ecf\u5ea6")
    private String longitude;
    @ApiModelProperty(value="\u5907\u6ce8")
    private String remark;
    @ApiModelProperty(value="\u521b\u5efa\u7ba1\u7406\u5458ID")
    private Integer operatorId;
    @ApiModelProperty(value="\u521b\u5efa\u4eba")
    private String operatorName;
    @ApiModelProperty(value="\u590d\u5236\u6765\u6e90\u8bfe\u8282ID")
    private Integer sourceLessonId;
    @ApiModelProperty(value="\u6388\u8bfe\u6559\u7ec3ID")
    private Integer coachId;
    @ApiModelProperty(value="\u6559\u7ec3\u7b7e\u5230: 0=\u672a\u7b7e\u5230, 1=\u5df2\u7b7e\u5230")
    private Integer coachCheckIn;
    @ApiModelProperty(value="\u72b6\u6001: 0=\u5f85\u4e0a\u8bfe, 1=\u5df2\u7ed3\u8bfe, 2=\u5df2\u53d6\u6d88")
    private Integer status;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Integer createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Integer updateTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getClassId() {
        return this.classId;
    }

    public Integer getLessonNo() {
        return this.lessonNo;
    }

    public Integer getLessonType() {
        return this.lessonType;
    }

    public String getLessonDate() {
        return this.lessonDate;
    }

    public Integer getWeekDay() {
        return this.weekDay;
    }

    public String getTimeSlot() {
        return this.timeSlot;
    }

    public String getVenue() {
        return this.venue;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
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

    public Integer getSourceLessonId() {
        return this.sourceLessonId;
    }

    public Integer getCoachId() {
        return this.coachId;
    }

    public Integer getCoachCheckIn() {
        return this.coachCheckIn;
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

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public void setLessonNo(Integer lessonNo) {
        this.lessonNo = lessonNo;
    }

    public void setLessonType(Integer lessonType) {
        this.lessonType = lessonType;
    }

    public void setLessonDate(String lessonDate) {
        this.lessonDate = lessonDate;
    }

    public void setWeekDay(Integer weekDay) {
        this.weekDay = weekDay;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public void setSourceLessonId(Integer sourceLessonId) {
        this.sourceLessonId = sourceLessonId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public void setCoachCheckIn(Integer coachCheckIn) {
        this.coachCheckIn = coachCheckIn;
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
        if (!(o instanceof CourseLesson)) {
            return false;
        }
        CourseLesson other = (CourseLesson)o;
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
        Integer this$lessonNo = this.getLessonNo();
        Integer other$lessonNo = other.getLessonNo();
        if (this$lessonNo == null ? other$lessonNo != null : !((Object)this$lessonNo).equals(other$lessonNo)) {
            return false;
        }
        Integer this$lessonType = this.getLessonType();
        Integer other$lessonType = other.getLessonType();
        if (this$lessonType == null ? other$lessonType != null : !((Object)this$lessonType).equals(other$lessonType)) {
            return false;
        }
        Integer this$weekDay = this.getWeekDay();
        Integer other$weekDay = other.getWeekDay();
        if (this$weekDay == null ? other$weekDay != null : !((Object)this$weekDay).equals(other$weekDay)) {
            return false;
        }
        Integer this$operatorId = this.getOperatorId();
        Integer other$operatorId = other.getOperatorId();
        if (this$operatorId == null ? other$operatorId != null : !((Object)this$operatorId).equals(other$operatorId)) {
            return false;
        }
        Integer this$sourceLessonId = this.getSourceLessonId();
        Integer other$sourceLessonId = other.getSourceLessonId();
        if (this$sourceLessonId == null ? other$sourceLessonId != null : !((Object)this$sourceLessonId).equals(other$sourceLessonId)) {
            return false;
        }
        Integer this$coachId = this.getCoachId();
        Integer other$coachId = other.getCoachId();
        if (this$coachId == null ? other$coachId != null : !((Object)this$coachId).equals(other$coachId)) {
            return false;
        }
        Integer this$coachCheckIn = this.getCoachCheckIn();
        Integer other$coachCheckIn = other.getCoachCheckIn();
        if (this$coachCheckIn == null ? other$coachCheckIn != null : !((Object)this$coachCheckIn).equals(other$coachCheckIn)) {
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
        String this$venue = this.getVenue();
        String other$venue = other.getVenue();
        if (this$venue == null ? other$venue != null : !this$venue.equals(other$venue)) {
            return false;
        }
        String this$latitude = this.getLatitude();
        String other$latitude = other.getLatitude();
        if (this$latitude == null ? other$latitude != null : !this$latitude.equals(other$latitude)) {
            return false;
        }
        String this$longitude = this.getLongitude();
        String other$longitude = other.getLongitude();
        if (this$longitude == null ? other$longitude != null : !this$longitude.equals(other$longitude)) {
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
        return other instanceof CourseLesson;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $classId = this.getClassId();
        result = result * 59 + ($classId == null ? 43 : ((Object)$classId).hashCode());
        Integer $lessonNo = this.getLessonNo();
        result = result * 59 + ($lessonNo == null ? 43 : ((Object)$lessonNo).hashCode());
        Integer $lessonType = this.getLessonType();
        result = result * 59 + ($lessonType == null ? 43 : ((Object)$lessonType).hashCode());
        Integer $weekDay = this.getWeekDay();
        result = result * 59 + ($weekDay == null ? 43 : ((Object)$weekDay).hashCode());
        Integer $operatorId = this.getOperatorId();
        result = result * 59 + ($operatorId == null ? 43 : ((Object)$operatorId).hashCode());
        Integer $sourceLessonId = this.getSourceLessonId();
        result = result * 59 + ($sourceLessonId == null ? 43 : ((Object)$sourceLessonId).hashCode());
        Integer $coachId = this.getCoachId();
        result = result * 59 + ($coachId == null ? 43 : ((Object)$coachId).hashCode());
        Integer $coachCheckIn = this.getCoachCheckIn();
        result = result * 59 + ($coachCheckIn == null ? 43 : ((Object)$coachCheckIn).hashCode());
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
        String $venue = this.getVenue();
        result = result * 59 + ($venue == null ? 43 : $venue.hashCode());
        String $latitude = this.getLatitude();
        result = result * 59 + ($latitude == null ? 43 : $latitude.hashCode());
        String $longitude = this.getLongitude();
        result = result * 59 + ($longitude == null ? 43 : $longitude.hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        String $operatorName = this.getOperatorName();
        result = result * 59 + ($operatorName == null ? 43 : $operatorName.hashCode());
        return result;
    }

    public String toString() {
        return "CourseLesson(id=" + this.getId() + ", classId=" + this.getClassId() + ", lessonNo=" + this.getLessonNo() + ", lessonType=" + this.getLessonType() + ", lessonDate=" + this.getLessonDate() + ", weekDay=" + this.getWeekDay() + ", timeSlot=" + this.getTimeSlot() + ", venue=" + this.getVenue() + ", latitude=" + this.getLatitude() + ", longitude=" + this.getLongitude() + ", remark=" + this.getRemark() + ", operatorId=" + this.getOperatorId() + ", operatorName=" + this.getOperatorName() + ", sourceLessonId=" + this.getSourceLessonId() + ", coachId=" + this.getCoachId() + ", coachCheckIn=" + this.getCoachCheckIn() + ", status=" + this.getStatus() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

