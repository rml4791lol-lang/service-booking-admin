/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.leave.LeaveAuditValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.admin.validate.leave;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u8bf7\u5047\u5ba1\u6279\u53c2\u6570")
public class LeaveAuditValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="id\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u8bf7\u5047\u8bb0\u5f55ID", required=true)
    private @NotNull(message="id\u4e0d\u80fd\u4e3a\u7a7a") Integer id;
    @NotNull(message="\u5ba1\u6838\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u72b6\u6001:1=\u901a\u8fc7,2=\u62d2\u7edd", required=true)
    private @NotNull(message="\u5ba1\u6838\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a") Integer status;
    @ApiModelProperty(value="\u5ba1\u6279\u5907\u6ce8")
    private String auditRemark;

    @NotNull(message="id\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="id\u4e0d\u80fd\u4e3a\u7a7a") Integer getId() {
        return this.id;
    }

    @NotNull(message="\u5ba1\u6838\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u5ba1\u6838\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a") Integer getStatus() {
        return this.status;
    }

    public String getAuditRemark() {
        return this.auditRemark;
    }

    public void setId(@NotNull(message="id\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="id\u4e0d\u80fd\u4e3a\u7a7a") Integer id) {
        this.id = id;
    }

    public void setStatus(@NotNull(message="\u5ba1\u6838\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u5ba1\u6838\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a") Integer status) {
        this.status = status;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LeaveAuditValidate)) {
            return false;
        }
        LeaveAuditValidate other = (LeaveAuditValidate)o;
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
        String this$auditRemark = this.getAuditRemark();
        String other$auditRemark = other.getAuditRemark();
        return !(this$auditRemark == null ? other$auditRemark != null : !this$auditRemark.equals(other$auditRemark));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LeaveAuditValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $auditRemark = this.getAuditRemark();
        result = result * 59 + ($auditRemark == null ? 43 : $auditRemark.hashCode());
        return result;
    }

    public String toString() {
        return "LeaveAuditValidate(id=" + this.getId() + ", status=" + this.getStatus() + ", auditRemark=" + this.getAuditRemark() + ")";
    }
}

