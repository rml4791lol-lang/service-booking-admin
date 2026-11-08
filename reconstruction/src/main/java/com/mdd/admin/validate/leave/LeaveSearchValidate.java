/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.leave.LeaveSearchValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.leave;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u8bf7\u5047\u7533\u8bf7\u5217\u8868\u67e5\u8be2\u53c2\u6570")
public class LeaveSearchValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u7533\u8bf7\u4eba\u7c7b\u578b:1=\u5b66\u751f,2=\u6559\u7ec3")
    private Integer applicantType;
    @ApiModelProperty(value="\u72b6\u6001:0=\u5f85\u5ba1\u6279,1=\u5df2\u6279\u51c6,2=\u5df2\u62d2\u7edd")
    private Integer status;
    @ApiModelProperty(value="\u73ed\u7ea7ID")
    private Integer classId;
    @ApiModelProperty(value="\u7533\u8bf7\u4eba\u540d\u79f0")
    private String applicantName;

    public Integer getApplicantType() {
        return this.applicantType;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Integer getClassId() {
        return this.classId;
    }

    public String getApplicantName() {
        return this.applicantName;
    }

    public void setApplicantType(Integer applicantType) {
        this.applicantType = applicantType;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LeaveSearchValidate)) {
            return false;
        }
        LeaveSearchValidate other = (LeaveSearchValidate)o;
        if (!other.canEqual((Object)this)) {
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
        Integer this$classId = this.getClassId();
        Integer other$classId = other.getClassId();
        if (this$classId == null ? other$classId != null : !((Object)this$classId).equals(other$classId)) {
            return false;
        }
        String this$applicantName = this.getApplicantName();
        String other$applicantName = other.getApplicantName();
        return !(this$applicantName == null ? other$applicantName != null : !this$applicantName.equals(other$applicantName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LeaveSearchValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $applicantType = this.getApplicantType();
        result = result * 59 + ($applicantType == null ? 43 : ((Object)$applicantType).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $classId = this.getClassId();
        result = result * 59 + ($classId == null ? 43 : ((Object)$classId).hashCode());
        String $applicantName = this.getApplicantName();
        result = result * 59 + ($applicantName == null ? 43 : $applicantName.hashCode());
        return result;
    }

    public String toString() {
        return "LeaveSearchValidate(applicantType=" + this.getApplicantType() + ", status=" + this.getStatus() + ", classId=" + this.getClassId() + ", applicantName=" + this.getApplicantName() + ")";
    }
}

