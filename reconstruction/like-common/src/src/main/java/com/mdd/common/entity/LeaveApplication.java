/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u8bf7\u5047\u7533\u8bf7\u5b9e\u4f53")
public class LeaveApplication
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u7533\u8bf7\u4eba\u7528\u6237ID(\u5b66\u751f)")
    private Integer userId;
    @ApiModelProperty(value="\u5173\u8054\u5b66\u5458ID")
    private Integer studentId;
    @ApiModelProperty(value="\u7533\u8bf7\u4eba\u6559\u7ec3ID(\u6559\u7ec3\u8bf7\u5047\u65f6\u586b)")
    private Integer instructorId;
    @ApiModelProperty(value="\u7533\u8bf7\u4eba\u7c7b\u578b:1=\u5b66\u751f,2=\u6559\u7ec3")
    private Integer applicantType;
    @ApiModelProperty(value="\u5173\u8054\u73ed\u7ea7ID")
    private Integer classId;
    @ApiModelProperty(value="\u73ed\u7ea7\u540d\u79f0(\u5197\u4f59)")
    private String className;
    @ApiModelProperty(value="\u7533\u8bf7\u4eba\u59d3\u540d(\u5197\u4f59)")
    private String applicantName;
    @ApiModelProperty(value="\u8bf7\u5047\u5f00\u59cb\u65f6\u95f4(\u65f6\u95f4\u6233)")
    private Long startTime;
    @ApiModelProperty(value="\u8bf7\u5047\u7ed3\u675f\u65f6\u95f4(\u65f6\u95f4\u6233)")
    private Long endTime;
    @ApiModelProperty(value="\u8bf7\u5047\u539f\u56e0\u6807\u7b7e")
    private String reason;
    @ApiModelProperty(value="\u8be6\u7ec6\u8bf4\u660e")
    private String detail;
    @ApiModelProperty(value="\u72b6\u6001:0=\u5f85\u5ba1\u6279,1=\u5df2\u6279\u51c6,2=\u5df2\u62d2\u7edd")
    private Integer status;
    @ApiModelProperty(value="\u5ba1\u6279\u4ebaID")
    private Integer auditId;
    @ApiModelProperty(value="\u5ba1\u6279\u4eba\u59d3\u540d")
    private String auditName;
    @ApiModelProperty(value="\u5ba1\u6279\u65f6\u95f4")
    private Long auditTime;
    @ApiModelProperty(value="\u5ba1\u6279\u5907\u6ce8")
    private String auditRemark;
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

    public Integer getInstructorId() {
        return this.instructorId;
    }

    public Integer getApplicantType() {
        return this.applicantType;
    }

    public Integer getClassId() {
        return this.classId;
    }

    public String getClassName() {
        return this.className;
    }

    public String getApplicantName() {
        return this.applicantName;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public Long getEndTime() {
        return this.endTime;
    }

    public String getReason() {
        return this.reason;
    }

    public String getDetail() {
        return this.detail;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Integer getAuditId() {
        return this.auditId;
    }

    public String getAuditName() {
        return this.auditName;
    }

    public Long getAuditTime() {
        return this.auditTime;
    }

    public String getAuditRemark() {
        return this.auditRemark;
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

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public void setApplicantType(Integer applicantType) {
        this.applicantType = applicantType;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public void setAuditTime(Long auditTime) {
        this.auditTime = auditTime;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
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
        if (!(o instanceof LeaveApplication)) {
            return false;
        }
        LeaveApplication other = (LeaveApplication)o;
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
        Integer this$instructorId = this.getInstructorId();
        Integer other$instructorId = other.getInstructorId();
        if (this$instructorId == null ? other$instructorId != null : !((Object)this$instructorId).equals(other$instructorId)) {
            return false;
        }
        Integer this$applicantType = this.getApplicantType();
        Integer other$applicantType = other.getApplicantType();
        if (this$applicantType == null ? other$applicantType != null : !((Object)this$applicantType).equals(other$applicantType)) {
            return false;
        }
        Integer this$classId = this.getClassId();
        Integer other$classId = other.getClassId();
        if (this$classId == null ? other$classId != null : !((Object)this$classId).equals(other$classId)) {
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
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Integer this$auditId = this.getAuditId();
        Integer other$auditId = other.getAuditId();
        if (this$auditId == null ? other$auditId != null : !((Object)this$auditId).equals(other$auditId)) {
            return false;
        }
        Long this$auditTime = this.getAuditTime();
        Long other$auditTime = other.getAuditTime();
        if (this$auditTime == null ? other$auditTime != null : !((Object)this$auditTime).equals(other$auditTime)) {
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
        if (this$deleteTime == null ? other$deleteTime != null : !((Object)this$deleteTime).equals(other$deleteTime)) {
            return false;
        }
        String this$className = this.getClassName();
        String other$className = other.getClassName();
        if (this$className == null ? other$className != null : !this$className.equals(other$className)) {
            return false;
        }
        String this$applicantName = this.getApplicantName();
        String other$applicantName = other.getApplicantName();
        if (this$applicantName == null ? other$applicantName != null : !this$applicantName.equals(other$applicantName)) {
            return false;
        }
        String this$reason = this.getReason();
        String other$reason = other.getReason();
        if (this$reason == null ? other$reason != null : !this$reason.equals(other$reason)) {
            return false;
        }
        String this$detail = this.getDetail();
        String other$detail = other.getDetail();
        if (this$detail == null ? other$detail != null : !this$detail.equals(other$detail)) {
            return false;
        }
        String this$auditName = this.getAuditName();
        String other$auditName = other.getAuditName();
        if (this$auditName == null ? other$auditName != null : !this$auditName.equals(other$auditName)) {
            return false;
        }
        String this$auditRemark = this.getAuditRemark();
        String other$auditRemark = other.getAuditRemark();
        return !(this$auditRemark == null ? other$auditRemark != null : !this$auditRemark.equals(other$auditRemark));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LeaveApplication;
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
        Integer $instructorId = this.getInstructorId();
        result = result * 59 + ($instructorId == null ? 43 : ((Object)$instructorId).hashCode());
        Integer $applicantType = this.getApplicantType();
        result = result * 59 + ($applicantType == null ? 43 : ((Object)$applicantType).hashCode());
        Integer $classId = this.getClassId();
        result = result * 59 + ($classId == null ? 43 : ((Object)$classId).hashCode());
        Long $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : ((Object)$startTime).hashCode());
        Long $endTime = this.getEndTime();
        result = result * 59 + ($endTime == null ? 43 : ((Object)$endTime).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $auditId = this.getAuditId();
        result = result * 59 + ($auditId == null ? 43 : ((Object)$auditId).hashCode());
        Long $auditTime = this.getAuditTime();
        result = result * 59 + ($auditTime == null ? 43 : ((Object)$auditTime).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $applicantName = this.getApplicantName();
        result = result * 59 + ($applicantName == null ? 43 : $applicantName.hashCode());
        String $reason = this.getReason();
        result = result * 59 + ($reason == null ? 43 : $reason.hashCode());
        String $detail = this.getDetail();
        result = result * 59 + ($detail == null ? 43 : $detail.hashCode());
        String $auditName = this.getAuditName();
        result = result * 59 + ($auditName == null ? 43 : $auditName.hashCode());
        String $auditRemark = this.getAuditRemark();
        result = result * 59 + ($auditRemark == null ? 43 : $auditRemark.hashCode());
        return result;
    }

    public String toString() {
        return "LeaveApplication(id=" + this.getId() + ", userId=" + this.getUserId() + ", studentId=" + this.getStudentId() + ", instructorId=" + this.getInstructorId() + ", applicantType=" + this.getApplicantType() + ", classId=" + this.getClassId() + ", className=" + this.getClassName() + ", applicantName=" + this.getApplicantName() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", reason=" + this.getReason() + ", detail=" + this.getDetail() + ", status=" + this.getStatus() + ", auditId=" + this.getAuditId() + ", auditName=" + this.getAuditName() + ", auditTime=" + this.getAuditTime() + ", auditRemark=" + this.getAuditRemark() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ")";
    }
}

