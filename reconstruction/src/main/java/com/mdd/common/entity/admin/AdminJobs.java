/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u5c97\u4f4d\u5173\u8054\u8868\u5b9e\u4f53")
public class AdminJobs
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u7ba1\u7406\u5458id")
    private Integer adminId;
    @ApiModelProperty(value="\u5c97\u4f4did")
    private Integer jobsId;

    public Integer getAdminId() {
        return this.adminId;
    }

    public Integer getJobsId() {
        return this.jobsId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public void setJobsId(Integer jobsId) {
        this.jobsId = jobsId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AdminJobs)) {
            return false;
        }
        AdminJobs other = (AdminJobs)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$adminId = this.getAdminId();
        Integer other$adminId = other.getAdminId();
        if (this$adminId == null ? other$adminId != null : !((Object)this$adminId).equals(other$adminId)) {
            return false;
        }
        Integer this$jobsId = this.getJobsId();
        Integer other$jobsId = other.getJobsId();
        return !(this$jobsId == null ? other$jobsId != null : !((Object)this$jobsId).equals(other$jobsId));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AdminJobs;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $adminId = this.getAdminId();
        result = result * 59 + ($adminId == null ? 43 : ((Object)$adminId).hashCode());
        Integer $jobsId = this.getJobsId();
        result = result * 59 + ($jobsId == null ? 43 : ((Object)$jobsId).hashCode());
        return result;
    }

    public String toString() {
        return "AdminJobs(adminId=" + this.getAdminId() + ", jobsId=" + this.getJobsId() + ")";
    }
}

