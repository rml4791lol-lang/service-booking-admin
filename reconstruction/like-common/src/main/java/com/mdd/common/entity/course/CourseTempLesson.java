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

@TableName(value="la_course_temp_lesson")
@ApiModel(value="\u4e34\u65f6\u52a0\u8bfe\u8bb0\u5f55")
public class CourseTempLesson
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value="\u73ed\u7ea7ID")
    private Integer classId;
    @ApiModelProperty(value="\u5173\u8054\u8bfe\u8282ID")
    private Integer lessonId;
    @ApiModelProperty(value="\u8bfe\u8282\u53f7")
    private Integer lessonNo;
    @ApiModelProperty(value="\u4e0a\u8bfe\u65e5\u671f")
    private String lessonDate;
    @ApiModelProperty(value="\u4e0a\u8bfe\u65f6\u6bb5")
    private String timeSlot;
    @ApiModelProperty(value="\u4e0a\u8bfe\u5730\u70b9")
    private String venue;
    @ApiModelProperty(value="\u7eac\u5ea6")
    private String latitude;
    @ApiModelProperty(value="\u7ecf\u5ea6")
    private String longitude;
    @ApiModelProperty(value="\u6388\u8bfe\u6559\u7ec3ID")
    private Integer instructorId;
    @ApiModelProperty(value="\u6388\u8bfe\u6559\u7ec3\u59d3\u540d")
    private String instructorName;
    @ApiModelProperty(value="\u73ed\u7ea7\u9ed8\u8ba4\u6559\u7ec3ID")
    private Integer defaultInstructorId;
    @ApiModelProperty(value="\u5907\u6ce8")
    private String remark;
    @ApiModelProperty(value="\u590d\u5236\u6765\u6e90ID")
    private Integer sourceId;
    @ApiModelProperty(value="\u64cd\u4f5c\u7ba1\u7406\u5458ID")
    private Integer operatorId;
    @ApiModelProperty(value="\u64cd\u4f5c\u7ba1\u7406\u5458\u59d3\u540d")
    private String operatorName;
    @ApiModelProperty(value="\u72b6\u6001: 0=\u5f85\u4e0a\u8bfe 1=\u5df2\u7ed3\u8bfe 2=\u5df2\u53d6\u6d88")
    private Integer status;
    private String activeKey;
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

    public Integer getLessonId() {
        return this.lessonId;
    }

    public Integer getLessonNo() {
        return this.lessonNo;
    }

    public String getLessonDate() {
        return this.lessonDate;
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

    public Integer getInstructorId() {
        return this.instructorId;
    }

    public String getInstructorName() {
        return this.instructorName;
    }

    public Integer getDefaultInstructorId() {
        return this.defaultInstructorId;
    }

    public String getRemark() {
        return this.remark;
    }

    public Integer getSourceId() {
        return this.sourceId;
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

    public String getActiveKey() {
        return this.activeKey;
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

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public void setLessonNo(Integer lessonNo) {
        this.lessonNo = lessonNo;
    }

    public void setLessonDate(String lessonDate) {
        this.lessonDate = lessonDate;
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

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public void setDefaultInstructorId(Integer defaultInstructorId) {
        this.defaultInstructorId = defaultInstructorId;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
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

    public void setActiveKey(String activeKey) {
        this.activeKey = activeKey;
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
        if (!(o instanceof CourseTempLesson)) {
            return false;
        }
        CourseTempLesson other = (CourseTempLesson)o;
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
        Integer this$lessonId = this.getLessonId();
        Integer other$lessonId = other.getLessonId();
        if (this$lessonId == null ? other$lessonId != null : !((Object)this$lessonId).equals(other$lessonId)) {
            return false;
        }
        Integer this$lessonNo = this.getLessonNo();
        Integer other$lessonNo = other.getLessonNo();
        if (this$lessonNo == null ? other$lessonNo != null : !((Object)this$lessonNo).equals(other$lessonNo)) {
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
        Integer this$sourceId = this.getSourceId();
        Integer other$sourceId = other.getSourceId();
        if (this$sourceId == null ? other$sourceId != null : !((Object)this$sourceId).equals(other$sourceId)) {
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
        String this$instructorName = this.getInstructorName();
        String other$instructorName = other.getInstructorName();
        if (this$instructorName == null ? other$instructorName != null : !this$instructorName.equals(other$instructorName)) {
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
        return other instanceof CourseTempLesson;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $classId = this.getClassId();
        result = result * 59 + ($classId == null ? 43 : ((Object)$classId).hashCode());
        Integer $lessonId = this.getLessonId();
        result = result * 59 + ($lessonId == null ? 43 : ((Object)$lessonId).hashCode());
        Integer $lessonNo = this.getLessonNo();
        result = result * 59 + ($lessonNo == null ? 43 : ((Object)$lessonNo).hashCode());
        Integer $instructorId = this.getInstructorId();
        result = result * 59 + ($instructorId == null ? 43 : ((Object)$instructorId).hashCode());
        Integer $defaultInstructorId = this.getDefaultInstructorId();
        result = result * 59 + ($defaultInstructorId == null ? 43 : ((Object)$defaultInstructorId).hashCode());
        Integer $sourceId = this.getSourceId();
        result = result * 59 + ($sourceId == null ? 43 : ((Object)$sourceId).hashCode());
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
        String $venue = this.getVenue();
        result = result * 59 + ($venue == null ? 43 : $venue.hashCode());
        String $latitude = this.getLatitude();
        result = result * 59 + ($latitude == null ? 43 : $latitude.hashCode());
        String $longitude = this.getLongitude();
        result = result * 59 + ($longitude == null ? 43 : $longitude.hashCode());
        String $instructorName = this.getInstructorName();
        result = result * 59 + ($instructorName == null ? 43 : $instructorName.hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        String $operatorName = this.getOperatorName();
        result = result * 59 + ($operatorName == null ? 43 : $operatorName.hashCode());
        return result;
    }

    public String toString() {
        return "CourseTempLesson(id=" + this.getId() + ", classId=" + this.getClassId() + ", lessonId=" + this.getLessonId() + ", lessonNo=" + this.getLessonNo() + ", lessonDate=" + this.getLessonDate() + ", timeSlot=" + this.getTimeSlot() + ", venue=" + this.getVenue() + ", latitude=" + this.getLatitude() + ", longitude=" + this.getLongitude() + ", instructorId=" + this.getInstructorId() + ", instructorName=" + this.getInstructorName() + ", defaultInstructorId=" + this.getDefaultInstructorId() + ", remark=" + this.getRemark() + ", sourceId=" + this.getSourceId() + ", operatorId=" + this.getOperatorId() + ", operatorName=" + this.getOperatorName() + ", status=" + this.getStatus() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}
