/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.vo.leave.LeaveListVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.vo.leave;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u8bf7\u5047\u660e\u7ec6\u54cd\u5e94\u5b9e\u4f53(\u5ba2\u6237\u7aef)")
public class LeaveListVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u7533\u8bf7\u4eba\u540d\u79f0")
    private String applicantName;
    @ApiModelProperty(value="\u73ed\u7ea7\u540d\u79f0")
    private String className;
    @ApiModelProperty(value="\u8bf7\u5047\u5f00\u59cb\u65f6\u95f4")
    private String startTime;
    @ApiModelProperty(value="\u8bf7\u5047\u7ed3\u675f\u65f6\u95f4")
    private String endTime;
    @ApiModelProperty(value="\u8bf7\u5047\u539f\u56e0\u6807\u7b7e")
    private String reason;
    @ApiModelProperty(value="\u8bf7\u5047\u8be6\u7ec6\u8bf4\u660e")
    private String detail;
    @ApiModelProperty(value="\u72b6\u6001:0=\u5f85\u5ba1\u6279,1=\u5df2\u6279\u51c6,2=\u5df2\u62d2\u7edd")
    private Integer status;
    @ApiModelProperty(value="\u72b6\u6001\u63cf\u8ff0")
    private String statusDesc;
    @ApiModelProperty(value="\u5ba1\u6279\u4eba\u540d\u79f0")
    private String auditName;
    @ApiModelProperty(value="\u5ba1\u6279\u5907\u6ce8")
    private String auditRemark;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;
    @ApiModelProperty(value="\u73ed\u7ea7ID")
    private Integer classId;
    @ApiModelProperty(value="\u7528\u6237ID")
    private Integer userId;
    @ApiModelProperty(value="\u5269\u4f59\u8bfe\u65f6")
    private Integer remainLessons;
    @ApiModelProperty(value="\u7528\u6237\u5934\u50cf")
    private String avatar;
    @ApiModelProperty(value="\u5b66\u5458ID")
    private Integer studentId;
    @ApiModelProperty(value="\u5b66\u5458\u6635\u79f0")
    private String studentName;

    public Integer getId() {
        return this.id;
    }

    public String getApplicantName() {
        return this.applicantName;
    }

    public String getClassName() {
        return this.className;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
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

    public String getStatusDesc() {
        return this.statusDesc;
    }

    public String getAuditName() {
        return this.auditName;
    }

    public String getAuditRemark() {
        return this.auditRemark;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public Integer getClassId() {
        return this.classId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getRemainLessons() {
        return this.remainLessons;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public Integer getStudentId() {
        return this.studentId;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
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

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setRemainLessons(Integer remainLessons) {
        this.remainLessons = remainLessons;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LeaveListVo)) {
            return false;
        }
        LeaveListVo other = (LeaveListVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Integer this$classId = this.getClassId();
        Integer other$classId = other.getClassId();
        if (this$classId == null ? other$classId != null : !((Object)this$classId).equals(other$classId)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$remainLessons = this.getRemainLessons();
        Integer other$remainLessons = other.getRemainLessons();
        if (this$remainLessons == null ? other$remainLessons != null : !((Object)this$remainLessons).equals(other$remainLessons)) {
            return false;
        }
        Integer this$studentId = this.getStudentId();
        Integer other$studentId = other.getStudentId();
        if (this$studentId == null ? other$studentId != null : !((Object)this$studentId).equals(other$studentId)) {
            return false;
        }
        String this$applicantName = this.getApplicantName();
        String other$applicantName = other.getApplicantName();
        if (this$applicantName == null ? other$applicantName != null : !this$applicantName.equals(other$applicantName)) {
            return false;
        }
        String this$className = this.getClassName();
        String other$className = other.getClassName();
        if (this$className == null ? other$className != null : !this$className.equals(other$className)) {
            return false;
        }
        String this$startTime = this.getStartTime();
        String other$startTime = other.getStartTime();
        if (this$startTime == null ? other$startTime != null : !this$startTime.equals(other$startTime)) {
            return false;
        }
        String this$endTime = this.getEndTime();
        String other$endTime = other.getEndTime();
        if (this$endTime == null ? other$endTime != null : !this$endTime.equals(other$endTime)) {
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
        String this$statusDesc = this.getStatusDesc();
        String other$statusDesc = other.getStatusDesc();
        if (this$statusDesc == null ? other$statusDesc != null : !this$statusDesc.equals(other$statusDesc)) {
            return false;
        }
        String this$auditName = this.getAuditName();
        String other$auditName = other.getAuditName();
        if (this$auditName == null ? other$auditName != null : !this$auditName.equals(other$auditName)) {
            return false;
        }
        String this$auditRemark = this.getAuditRemark();
        String other$auditRemark = other.getAuditRemark();
        if (this$auditRemark == null ? other$auditRemark != null : !this$auditRemark.equals(other$auditRemark)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
            return false;
        }
        String this$studentName = this.getStudentName();
        String other$studentName = other.getStudentName();
        return !(this$studentName == null ? other$studentName != null : !this$studentName.equals(other$studentName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LeaveListVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $classId = this.getClassId();
        result = result * 59 + ($classId == null ? 43 : ((Object)$classId).hashCode());
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $remainLessons = this.getRemainLessons();
        result = result * 59 + ($remainLessons == null ? 43 : ((Object)$remainLessons).hashCode());
        Integer $studentId = this.getStudentId();
        result = result * 59 + ($studentId == null ? 43 : ((Object)$studentId).hashCode());
        String $applicantName = this.getApplicantName();
        result = result * 59 + ($applicantName == null ? 43 : $applicantName.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : $startTime.hashCode());
        String $endTime = this.getEndTime();
        result = result * 59 + ($endTime == null ? 43 : $endTime.hashCode());
        String $reason = this.getReason();
        result = result * 59 + ($reason == null ? 43 : $reason.hashCode());
        String $detail = this.getDetail();
        result = result * 59 + ($detail == null ? 43 : $detail.hashCode());
        String $statusDesc = this.getStatusDesc();
        result = result * 59 + ($statusDesc == null ? 43 : $statusDesc.hashCode());
        String $auditName = this.getAuditName();
        result = result * 59 + ($auditName == null ? 43 : $auditName.hashCode());
        String $auditRemark = this.getAuditRemark();
        result = result * 59 + ($auditRemark == null ? 43 : $auditRemark.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $studentName = this.getStudentName();
        result = result * 59 + ($studentName == null ? 43 : $studentName.hashCode());
        return result;
    }

    public String toString() {
        return "LeaveListVo(id=" + this.getId() + ", applicantName=" + this.getApplicantName() + ", className=" + this.getClassName() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", reason=" + this.getReason() + ", detail=" + this.getDetail() + ", status=" + this.getStatus() + ", statusDesc=" + this.getStatusDesc() + ", auditName=" + this.getAuditName() + ", auditRemark=" + this.getAuditRemark() + ", createTime=" + this.getCreateTime() + ", classId=" + this.getClassId() + ", userId=" + this.getUserId() + ", remainLessons=" + this.getRemainLessons() + ", avatar=" + this.getAvatar() + ", studentId=" + this.getStudentId() + ", studentName=" + this.getStudentName() + ")";
    }
}

