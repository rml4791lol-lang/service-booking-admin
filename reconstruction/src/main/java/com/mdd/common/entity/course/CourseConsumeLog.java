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

@TableName(value="la_course_consume_log")
@ApiModel(value="\u8bfe\u7a0b\u6838\u9500\u6d41\u6c34\u8868")
public class CourseConsumeLog
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u7528\u6237ID")
    private Integer userId;
    @ApiModelProperty(value="\u5b66\u5458ID")
    private Integer studentId;
    @ApiModelProperty(value="\u62a5\u540d\u8bb0\u5f55ID")
    private Integer userCourseId;
    @ApiModelProperty(value="\u8bfe\u7a0b\u73ed\u7ea7ID")
    private Integer courseClassId;
    @ApiModelProperty(value="\u6838\u9500\u7684\u8bfe\u8282")
    private Integer lessonNo;
    @ApiModelProperty(value="\u6765\u6e90:0=\u6b63\u5e38,1=\u4eba\u5de5\u6263\u8bfe")
    private Integer sourceType;
    @ApiModelProperty(value="\u4eba\u5de5\u6263\u8bfe\u8bb0\u5f55ID")
    private Integer manualId;
    private String consumeKey;
    @ApiModelProperty(value="\u6838\u9500\u65f6\u95f4")
    private Long createTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getStudentId() {
        return this.studentId;
    }

    public Integer getUserCourseId() {
        return this.userCourseId;
    }

    public Integer getCourseClassId() {
        return this.courseClassId;
    }

    public Integer getLessonNo() {
        return this.lessonNo;
    }

    public Integer getSourceType() {
        return this.sourceType;
    }

    public Integer getManualId() {
        return this.manualId;
    }

    public String getConsumeKey() {
        return this.consumeKey;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public void setUserCourseId(Integer userCourseId) {
        this.userCourseId = userCourseId;
    }

    public void setCourseClassId(Integer courseClassId) {
        this.courseClassId = courseClassId;
    }

    public void setLessonNo(Integer lessonNo) {
        this.lessonNo = lessonNo;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public void setManualId(Integer manualId) {
        this.manualId = manualId;
    }

    public void setConsumeKey(String consumeKey) {
        this.consumeKey = consumeKey;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CourseConsumeLog)) {
            return false;
        }
        CourseConsumeLog other = (CourseConsumeLog)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$studentId = this.getStudentId();
        Integer other$studentId = other.getStudentId();
        if (this$studentId == null ? other$studentId != null : !((Object)this$studentId).equals(other$studentId)) {
            return false;
        }
        Integer this$userCourseId = this.getUserCourseId();
        Integer other$userCourseId = other.getUserCourseId();
        if (this$userCourseId == null ? other$userCourseId != null : !((Object)this$userCourseId).equals(other$userCourseId)) {
            return false;
        }
        Integer this$courseClassId = this.getCourseClassId();
        Integer other$courseClassId = other.getCourseClassId();
        if (this$courseClassId == null ? other$courseClassId != null : !((Object)this$courseClassId).equals(other$courseClassId)) {
            return false;
        }
        Integer this$lessonNo = this.getLessonNo();
        Integer other$lessonNo = other.getLessonNo();
        if (this$lessonNo == null ? other$lessonNo != null : !((Object)this$lessonNo).equals(other$lessonNo)) {
            return false;
        }
        Integer this$sourceType = this.getSourceType();
        Integer other$sourceType = other.getSourceType();
        if (this$sourceType == null ? other$sourceType != null : !((Object)this$sourceType).equals(other$sourceType)) {
            return false;
        }
        Integer this$manualId = this.getManualId();
        Integer other$manualId = other.getManualId();
        if (this$manualId == null ? other$manualId != null : !((Object)this$manualId).equals(other$manualId)) {
            return false;
        }
        Long this$createTime = this.getCreateTime();
        Long other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseConsumeLog;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $studentId = this.getStudentId();
        result = result * 59 + ($studentId == null ? 43 : ((Object)$studentId).hashCode());
        Integer $userCourseId = this.getUserCourseId();
        result = result * 59 + ($userCourseId == null ? 43 : ((Object)$userCourseId).hashCode());
        Integer $courseClassId = this.getCourseClassId();
        result = result * 59 + ($courseClassId == null ? 43 : ((Object)$courseClassId).hashCode());
        Integer $lessonNo = this.getLessonNo();
        result = result * 59 + ($lessonNo == null ? 43 : ((Object)$lessonNo).hashCode());
        Integer $sourceType = this.getSourceType();
        result = result * 59 + ($sourceType == null ? 43 : ((Object)$sourceType).hashCode());
        Integer $manualId = this.getManualId();
        result = result * 59 + ($manualId == null ? 43 : ((Object)$manualId).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        return result;
    }

    public String toString() {
        return "CourseConsumeLog(id=" + this.getId() + ", userId=" + this.getUserId() + ", studentId=" + this.getStudentId() + ", userCourseId=" + this.getUserCourseId() + ", courseClassId=" + this.getCourseClassId() + ", lessonNo=" + this.getLessonNo() + ", sourceType=" + this.getSourceType() + ", manualId=" + this.getManualId() + ", createTime=" + this.getCreateTime() + ")";
    }
}
