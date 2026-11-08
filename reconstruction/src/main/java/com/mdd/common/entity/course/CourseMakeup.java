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

@TableName(value="la_course_makeup")
@ApiModel(value="\u8865\u8bfe\u8bb0\u5f55\u8868")
public class CourseMakeup
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value="\u5173\u8054\u8bfe\u65f6ID")
    private Integer lessonId;
    @ApiModelProperty(value="\u73ed\u7ea7ID")
    private Integer classId;
    @ApiModelProperty(value="\u5173\u8054\u8bf7\u5047\u8bb0\u5f55ID")
    private Integer leaveId;
    @ApiModelProperty(value="\u6559\u7ec3ID")
    private Integer instructorId;
    @ApiModelProperty(value="\u539f\u8bfe\u65f6\u53f7")
    private Integer lessonNo;
    @ApiModelProperty(value="\u539f\u4e0a\u8bfe\u65e5\u671f")
    private String originalDate;
    @ApiModelProperty(value="\u8865\u8bfe\u65e5\u671f")
    private String makeupDate;
    @ApiModelProperty(value="\u8865\u8bfe\u65f6\u6bb5")
    private String makeupTimeSlot;
    @ApiModelProperty(value="0=\u5f85\u8865\u8bfe, 1=\u5df2\u8865\u8bfe")
    private Integer status;
    @ApiModelProperty(value="\u7ed3\u7b97\u91d1\u989d")
    private BigDecimal settleAmount;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Integer createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Integer updateTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getLessonId() {
        return this.lessonId;
    }

    public Integer getClassId() {
        return this.classId;
    }

    public Integer getLeaveId() {
        return this.leaveId;
    }

    public Integer getInstructorId() {
        return this.instructorId;
    }

    public Integer getLessonNo() {
        return this.lessonNo;
    }

    public String getOriginalDate() {
        return this.originalDate;
    }

    public String getMakeupDate() {
        return this.makeupDate;
    }

    public String getMakeupTimeSlot() {
        return this.makeupTimeSlot;
    }

    public Integer getStatus() {
        return this.status;
    }

    public BigDecimal getSettleAmount() {
        return this.settleAmount;
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

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public void setLeaveId(Integer leaveId) {
        this.leaveId = leaveId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public void setLessonNo(Integer lessonNo) {
        this.lessonNo = lessonNo;
    }

    public void setOriginalDate(String originalDate) {
        this.originalDate = originalDate;
    }

    public void setMakeupDate(String makeupDate) {
        this.makeupDate = makeupDate;
    }

    public void setMakeupTimeSlot(String makeupTimeSlot) {
        this.makeupTimeSlot = makeupTimeSlot;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setSettleAmount(BigDecimal settleAmount) {
        this.settleAmount = settleAmount;
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
        if (!(o instanceof CourseMakeup)) {
            return false;
        }
        CourseMakeup other = (CourseMakeup)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$lessonId = this.getLessonId();
        Integer other$lessonId = other.getLessonId();
        if (this$lessonId == null ? other$lessonId != null : !((Object)this$lessonId).equals(other$lessonId)) {
            return false;
        }
        Integer this$classId = this.getClassId();
        Integer other$classId = other.getClassId();
        if (this$classId == null ? other$classId != null : !((Object)this$classId).equals(other$classId)) {
            return false;
        }
        Integer this$leaveId = this.getLeaveId();
        Integer other$leaveId = other.getLeaveId();
        if (this$leaveId == null ? other$leaveId != null : !((Object)this$leaveId).equals(other$leaveId)) {
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
        String this$originalDate = this.getOriginalDate();
        String other$originalDate = other.getOriginalDate();
        if (this$originalDate == null ? other$originalDate != null : !this$originalDate.equals(other$originalDate)) {
            return false;
        }
        String this$makeupDate = this.getMakeupDate();
        String other$makeupDate = other.getMakeupDate();
        if (this$makeupDate == null ? other$makeupDate != null : !this$makeupDate.equals(other$makeupDate)) {
            return false;
        }
        String this$makeupTimeSlot = this.getMakeupTimeSlot();
        String other$makeupTimeSlot = other.getMakeupTimeSlot();
        if (this$makeupTimeSlot == null ? other$makeupTimeSlot != null : !this$makeupTimeSlot.equals(other$makeupTimeSlot)) {
            return false;
        }
        BigDecimal this$settleAmount = this.getSettleAmount();
        BigDecimal other$settleAmount = other.getSettleAmount();
        return !(this$settleAmount == null ? other$settleAmount != null : !((Object)this$settleAmount).equals(other$settleAmount));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseMakeup;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $lessonId = this.getLessonId();
        result = result * 59 + ($lessonId == null ? 43 : ((Object)$lessonId).hashCode());
        Integer $classId = this.getClassId();
        result = result * 59 + ($classId == null ? 43 : ((Object)$classId).hashCode());
        Integer $leaveId = this.getLeaveId();
        result = result * 59 + ($leaveId == null ? 43 : ((Object)$leaveId).hashCode());
        Integer $instructorId = this.getInstructorId();
        result = result * 59 + ($instructorId == null ? 43 : ((Object)$instructorId).hashCode());
        Integer $lessonNo = this.getLessonNo();
        result = result * 59 + ($lessonNo == null ? 43 : ((Object)$lessonNo).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Integer $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $originalDate = this.getOriginalDate();
        result = result * 59 + ($originalDate == null ? 43 : $originalDate.hashCode());
        String $makeupDate = this.getMakeupDate();
        result = result * 59 + ($makeupDate == null ? 43 : $makeupDate.hashCode());
        String $makeupTimeSlot = this.getMakeupTimeSlot();
        result = result * 59 + ($makeupTimeSlot == null ? 43 : $makeupTimeSlot.hashCode());
        BigDecimal $settleAmount = this.getSettleAmount();
        result = result * 59 + ($settleAmount == null ? 43 : ((Object)$settleAmount).hashCode());
        return result;
    }

    public String toString() {
        return "CourseMakeup(id=" + this.getId() + ", lessonId=" + this.getLessonId() + ", classId=" + this.getClassId() + ", leaveId=" + this.getLeaveId() + ", instructorId=" + this.getInstructorId() + ", lessonNo=" + this.getLessonNo() + ", originalDate=" + this.getOriginalDate() + ", makeupDate=" + this.getMakeupDate() + ", makeupTimeSlot=" + this.getMakeupTimeSlot() + ", status=" + this.getStatus() + ", settleAmount=" + this.getSettleAmount() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

