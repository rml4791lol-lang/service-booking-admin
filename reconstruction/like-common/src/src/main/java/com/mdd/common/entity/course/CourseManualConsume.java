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

@TableName(value="la_course_manual_consume")
@ApiModel(value="\u4eba\u5de5\u6263\u8bfe\u660e\u7ec6")
public class CourseManualConsume
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value="\u4eba\u5de5\u6263\u8bfe\u8bb0\u5f55ID")
    private Integer manualId;
    @ApiModelProperty(value="\u7528\u6237ID")
    private Integer userId;
    @ApiModelProperty(value="\u5b66\u5458ID")
    private Integer studentId;
    @ApiModelProperty(value="\u7528\u6237\u8bfe\u7a0b\u62a5\u540dID")
    private Integer userCourseId;
    @ApiModelProperty(value="\u8bfe\u6d88\u6d41\u6c34ID")
    private Integer consumeLogId;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Integer createTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getManualId() {
        return this.manualId;
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

    public Integer getConsumeLogId() {
        return this.consumeLogId;
    }

    public Integer getCreateTime() {
        return this.createTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setManualId(Integer manualId) {
        this.manualId = manualId;
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

    public void setConsumeLogId(Integer consumeLogId) {
        this.consumeLogId = consumeLogId;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CourseManualConsume)) {
            return false;
        }
        CourseManualConsume other = (CourseManualConsume)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$manualId = this.getManualId();
        Integer other$manualId = other.getManualId();
        if (this$manualId == null ? other$manualId != null : !((Object)this$manualId).equals(other$manualId)) {
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
        Integer this$consumeLogId = this.getConsumeLogId();
        Integer other$consumeLogId = other.getConsumeLogId();
        if (this$consumeLogId == null ? other$consumeLogId != null : !((Object)this$consumeLogId).equals(other$consumeLogId)) {
            return false;
        }
        Integer this$createTime = this.getCreateTime();
        Integer other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseManualConsume;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $manualId = this.getManualId();
        result = result * 59 + ($manualId == null ? 43 : ((Object)$manualId).hashCode());
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $studentId = this.getStudentId();
        result = result * 59 + ($studentId == null ? 43 : ((Object)$studentId).hashCode());
        Integer $userCourseId = this.getUserCourseId();
        result = result * 59 + ($userCourseId == null ? 43 : ((Object)$userCourseId).hashCode());
        Integer $consumeLogId = this.getConsumeLogId();
        result = result * 59 + ($consumeLogId == null ? 43 : ((Object)$consumeLogId).hashCode());
        Integer $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        return result;
    }

    public String toString() {
        return "CourseManualConsume(id=" + this.getId() + ", manualId=" + this.getManualId() + ", userId=" + this.getUserId() + ", studentId=" + this.getStudentId() + ", userCourseId=" + this.getUserCourseId() + ", consumeLogId=" + this.getConsumeLogId() + ", createTime=" + this.getCreateTime() + ")";
    }
}

