/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.annotation.TableName
 */
package com.mdd.common.entity.course;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

@TableName(value="la_course_booking")
public class CourseBooking
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer studentId;
    private Integer courseClassId;
    private Integer lessonId;
    private Integer lessonNo;
    private String lessonDate;
    private Integer status;
    private Long createTime;
    private Long updateTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getStudentId() {
        return this.studentId;
    }

    public Integer getCourseClassId() {
        return this.courseClassId;
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

    public Integer getStatus() {
        return this.status;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public Long getUpdateTime() {
        return this.updateTime;
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

    public void setCourseClassId(Integer courseClassId) {
        this.courseClassId = courseClassId;
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

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CourseBooking)) {
            return false;
        }
        CourseBooking other = (CourseBooking)o;
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
        Integer this$courseClassId = this.getCourseClassId();
        Integer other$courseClassId = other.getCourseClassId();
        if (this$courseClassId == null ? other$courseClassId != null : !((Object)this$courseClassId).equals(other$courseClassId)) {
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
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Long this$createTime = this.getCreateTime();
        Long other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Long this$updateTime = this.getUpdateTime();
        Long other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        String this$lessonDate = this.getLessonDate();
        String other$lessonDate = other.getLessonDate();
        return !(this$lessonDate == null ? other$lessonDate != null : !this$lessonDate.equals(other$lessonDate));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseBooking;
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
        Integer $courseClassId = this.getCourseClassId();
        result = result * 59 + ($courseClassId == null ? 43 : ((Object)$courseClassId).hashCode());
        Integer $lessonId = this.getLessonId();
        result = result * 59 + ($lessonId == null ? 43 : ((Object)$lessonId).hashCode());
        Integer $lessonNo = this.getLessonNo();
        result = result * 59 + ($lessonNo == null ? 43 : ((Object)$lessonNo).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $lessonDate = this.getLessonDate();
        result = result * 59 + ($lessonDate == null ? 43 : $lessonDate.hashCode());
        return result;
    }

    public String toString() {
        return "CourseBooking(id=" + this.getId() + ", userId=" + this.getUserId() + ", studentId=" + this.getStudentId() + ", courseClassId=" + this.getCourseClassId() + ", lessonId=" + this.getLessonId() + ", lessonNo=" + this.getLessonNo() + ", lessonDate=" + this.getLessonDate() + ", status=" + this.getStatus() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

