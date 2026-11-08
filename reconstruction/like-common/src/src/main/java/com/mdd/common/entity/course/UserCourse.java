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

@TableName(value="la_user_course")
@ApiModel(value="\u7528\u6237\u8bfe\u7a0b\u62a5\u540d\u8bb0\u5f55")
public class UserCourse
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u7528\u6237ID")
    private Integer userId;
    @ApiModelProperty(value="\u5b66\u5458ID")
    private Integer studentId;
    @ApiModelProperty(value="\u8bfe\u7a0b\u73ed\u7ea7ID")
    private Integer courseClassId;
    @ApiModelProperty(value="\u5173\u8054\u8ba2\u5355ID")
    private Integer orderId;
    @ApiModelProperty(value="\u8d2d\u4e70\u7684\u603b\u8bfe\u65f6")
    private Integer totalLessons;
    @ApiModelProperty(value="\u5df2\u6d88\u8bfe\u65f6")
    private Integer usedLessons;
    @ApiModelProperty(value="\u5269\u4f59\u8bfe\u65f6")
    private Integer remainLessons;
    @ApiModelProperty(value="\u72b6\u6001: 0=\u5b66\u4e60\u4e2d, 1=\u5df2\u5b8c\u6210")
    private Integer status;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Long updateTime;
    @ApiModelProperty(value="\u5220\u9664\u65f6\u95f4")
    private Long deleteTime;

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

    public Integer getOrderId() {
        return this.orderId;
    }

    public Integer getTotalLessons() {
        return this.totalLessons;
    }

    public Integer getUsedLessons() {
        return this.usedLessons;
    }

    public Integer getRemainLessons() {
        return this.remainLessons;
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

    public Long getDeleteTime() {
        return this.deleteTime;
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

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setTotalLessons(Integer totalLessons) {
        this.totalLessons = totalLessons;
    }

    public void setUsedLessons(Integer usedLessons) {
        this.usedLessons = usedLessons;
    }

    public void setRemainLessons(Integer remainLessons) {
        this.remainLessons = remainLessons;
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

    public void setDeleteTime(Long deleteTime) {
        this.deleteTime = deleteTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserCourse)) {
            return false;
        }
        UserCourse other = (UserCourse)o;
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
        Integer this$orderId = this.getOrderId();
        Integer other$orderId = other.getOrderId();
        if (this$orderId == null ? other$orderId != null : !((Object)this$orderId).equals(other$orderId)) {
            return false;
        }
        Integer this$totalLessons = this.getTotalLessons();
        Integer other$totalLessons = other.getTotalLessons();
        if (this$totalLessons == null ? other$totalLessons != null : !((Object)this$totalLessons).equals(other$totalLessons)) {
            return false;
        }
        Integer this$usedLessons = this.getUsedLessons();
        Integer other$usedLessons = other.getUsedLessons();
        if (this$usedLessons == null ? other$usedLessons != null : !((Object)this$usedLessons).equals(other$usedLessons)) {
            return false;
        }
        Integer this$remainLessons = this.getRemainLessons();
        Integer other$remainLessons = other.getRemainLessons();
        if (this$remainLessons == null ? other$remainLessons != null : !((Object)this$remainLessons).equals(other$remainLessons)) {
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
        Long this$deleteTime = this.getDeleteTime();
        Long other$deleteTime = other.getDeleteTime();
        return !(this$deleteTime == null ? other$deleteTime != null : !((Object)this$deleteTime).equals(other$deleteTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserCourse;
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
        Integer $orderId = this.getOrderId();
        result = result * 59 + ($orderId == null ? 43 : ((Object)$orderId).hashCode());
        Integer $totalLessons = this.getTotalLessons();
        result = result * 59 + ($totalLessons == null ? 43 : ((Object)$totalLessons).hashCode());
        Integer $usedLessons = this.getUsedLessons();
        result = result * 59 + ($usedLessons == null ? 43 : ((Object)$usedLessons).hashCode());
        Integer $remainLessons = this.getRemainLessons();
        result = result * 59 + ($remainLessons == null ? 43 : ((Object)$remainLessons).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        return result;
    }

    public String toString() {
        return "UserCourse(id=" + this.getId() + ", userId=" + this.getUserId() + ", studentId=" + this.getStudentId() + ", courseClassId=" + this.getCourseClassId() + ", orderId=" + this.getOrderId() + ", totalLessons=" + this.getTotalLessons() + ", usedLessons=" + this.getUsedLessons() + ", remainLessons=" + this.getRemainLessons() + ", status=" + this.getStatus() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ")";
    }
}

