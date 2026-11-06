/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.leave.LeaveListedVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.leave;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u8bf7\u5047\u5217\u8868\u54cd\u5e94\u5b9e\u4f53")
public class LeaveListedVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u7533\u8bf7\u4eba\u7c7b\u578b:1=\u5b66\u751f,2=\u6559\u7ec3")
    private Integer applicantType;
    @ApiModelProperty(value="\u7533\u8bf7\u4eba\u7c7b\u578b\u63cf\u8ff0")
    private String applicantTypeDesc;
    @ApiModelProperty(value="\u7533\u8bf7\u4eba\u59d3\u540d")
    private String applicantName;
    @ApiModelProperty(value="\u73ed\u7ea7\u540d\u79f0")
    private String className;
    @ApiModelProperty(value="\u8bf7\u5047\u5f00\u59cb\u65f6\u95f4")
    private String startTime;
    @ApiModelProperty(value="\u8bf7\u5047\u7ed3\u675f\u65f6\u95f4")
    private String endTime;
    @ApiModelProperty(value="\u8bf7\u5047\u539f\u56e0\u6807\u7b7e")
    private String reason;
    @ApiModelProperty(value="\u8bf7\u5047\u539f\u56e0\u8be6\u60c5")
    private String detail;
    @ApiModelProperty(value="\u72b6\u6001:0=\u5f85\u5ba1\u6279,1=\u5df2\u6279\u51c6,2=\u5df2\u62d2\u7edd")
    private Integer status;
    @ApiModelProperty(value="\u72b6\u6001\u63cf\u8ff0")
    private String statusDesc;
    @ApiModelProperty(value="\u5ba1\u6279\u4eba\u59d3\u540d")
    private String auditName;
    @ApiModelProperty(value="\u5ba1\u6279\u5907\u6ce8")
    private String auditRemark;
    @ApiModelProperty(value="\u63d0\u4ea4\u65f6\u95f4")
    private String createTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getApplicantType() {
        return this.applicantType;
    }

    public String getApplicantTypeDesc() {
        return this.applicantTypeDesc;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setApplicantType(Integer applicantType) {
        this.applicantType = applicantType;
    }

    public void setApplicantTypeDesc(String applicantTypeDesc) {
        this.applicantTypeDesc = applicantTypeDesc;
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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LeaveListedVo)) {
            return false;
        }
        LeaveListedVo other = (LeaveListedVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$applicantType = this.getApplicantType();
        Integer other$applicantType = other.getApplicantType();
        if (this$applicantType == null ? other$applicantType != null : !((Object)this$applicantType).equals(other$applicantType)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$applicantTypeDesc = this.getApplicantTypeDesc();
        String other$applicantTypeDesc = other.getApplicantTypeDesc();
        if (this$applicantTypeDesc == null ? other$applicantTypeDesc != null : !this$applicantTypeDesc.equals(other$applicantTypeDesc)) {
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
        return !(this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LeaveListedVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $applicantType = this.getApplicantType();
        result = result * 59 + ($applicantType == null ? 43 : ((Object)$applicantType).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $applicantTypeDesc = this.getApplicantTypeDesc();
        result = result * 59 + ($applicantTypeDesc == null ? 43 : $applicantTypeDesc.hashCode());
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
        return result;
    }

    public String toString() {
        return "LeaveListedVo(id=" + this.getId() + ", applicantType=" + this.getApplicantType() + ", applicantTypeDesc=" + this.getApplicantTypeDesc() + ", applicantName=" + this.getApplicantName() + ", className=" + this.getClassName() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ", reason=" + this.getReason() + ", detail=" + this.getDetail() + ", status=" + this.getStatus() + ", statusDesc=" + this.getStatusDesc() + ", auditName=" + this.getAuditName() + ", auditRemark=" + this.getAuditRemark() + ", createTime=" + this.getCreateTime() + ")";
    }
}

