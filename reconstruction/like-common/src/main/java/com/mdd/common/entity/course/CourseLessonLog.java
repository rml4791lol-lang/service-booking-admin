/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.course;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u8bfe\u7a0b\u8bfe\u540e\u603b\u7ed3\u65e5\u5fd7\u5b9e\u4f53")
public class CourseLessonLog
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="\u4e3b\u952eID")
    private Integer id;
    @ApiModelProperty(value="\u73ed\u7ea7ID")
    private Integer courseClassId;
    @ApiModelProperty(value="\u6559\u7ec3ID")
    private Integer instructorId;
    @ApiModelProperty(value="\u7b2c\u51e0\u8282\u8bfe")
    private Integer lessonNo;
    @ApiModelProperty(value="\u8bfe\u540e\u603b\u7ed3")
    private String summary;
    @ApiModelProperty(value="\u4e0a\u8bfe\u7167\u7247(\u591a\u56fe\u9017\u53f7\u5206\u9694)")
    private String images;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Integer createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Integer updateTime;
    @ApiModelProperty(value="\u5220\u9664\u65f6\u95f4")
    private Integer deleteTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getCourseClassId() {
        return this.courseClassId;
    }

    public Integer getInstructorId() {
        return this.instructorId;
    }

    public Integer getLessonNo() {
        return this.lessonNo;
    }

    public String getSummary() {
        return this.summary;
    }

    public String getImages() {
        return this.images;
    }

    public Integer getCreateTime() {
        return this.createTime;
    }

    public Integer getUpdateTime() {
        return this.updateTime;
    }

    public Integer getDeleteTime() {
        return this.deleteTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCourseClassId(Integer courseClassId) {
        this.courseClassId = courseClassId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public void setLessonNo(Integer lessonNo) {
        this.lessonNo = lessonNo;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleteTime(Integer deleteTime) {
        this.deleteTime = deleteTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CourseLessonLog)) {
            return false;
        }
        CourseLessonLog other = (CourseLessonLog)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$courseClassId = this.getCourseClassId();
        Integer other$courseClassId = other.getCourseClassId();
        if (this$courseClassId == null ? other$courseClassId != null : !((Object)this$courseClassId).equals(other$courseClassId)) {
            return false;
        }
        Integer this$instructorId = this.getInstructorId();
        Integer other$instructorId = other.getInstructorId();
        if (this$instructorId == null ? other$instructorId != null : !((Object)this$instructorId).equals(other$instructorId)) {
            return false;
        }
        Integer this$lessonNo = this.getLessonNo();
        Integer other$lessonNo = other.getLessonNo();
        if (this$lessonNo == null ? other$lessonNo != null : !((Object)this$lessonNo).equals(other$lessonNo)) {
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
        Integer this$deleteTime = this.getDeleteTime();
        Integer other$deleteTime = other.getDeleteTime();
        if (this$deleteTime == null ? other$deleteTime != null : !((Object)this$deleteTime).equals(other$deleteTime)) {
            return false;
        }
        String this$summary = this.getSummary();
        String other$summary = other.getSummary();
        if (this$summary == null ? other$summary != null : !this$summary.equals(other$summary)) {
            return false;
        }
        String this$images = this.getImages();
        String other$images = other.getImages();
        return !(this$images == null ? other$images != null : !this$images.equals(other$images));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseLessonLog;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $courseClassId = this.getCourseClassId();
        result = result * 59 + ($courseClassId == null ? 43 : ((Object)$courseClassId).hashCode());
        Integer $instructorId = this.getInstructorId();
        result = result * 59 + ($instructorId == null ? 43 : ((Object)$instructorId).hashCode());
        Integer $lessonNo = this.getLessonNo();
        result = result * 59 + ($lessonNo == null ? 43 : ((Object)$lessonNo).hashCode());
        Integer $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Integer $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Integer $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        String $summary = this.getSummary();
        result = result * 59 + ($summary == null ? 43 : $summary.hashCode());
        String $images = this.getImages();
        result = result * 59 + ($images == null ? 43 : $images.hashCode());
        return result;
    }

    public String toString() {
        return "CourseLessonLog(id=" + this.getId() + ", courseClassId=" + this.getCourseClassId() + ", instructorId=" + this.getInstructorId() + ", lessonNo=" + this.getLessonNo() + ", summary=" + this.getSummary() + ", images=" + this.getImages() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ")";
    }
}

