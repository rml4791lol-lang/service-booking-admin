/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.refund.CourseRefundAuditValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.admin.validate.refund;

import com.mdd.common.validator.annotation.IntegerContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u9000\u8bfe\u5ba1\u6279\u53c2\u6570")
public class CourseRefundAuditValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="id\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u9000\u8bfe\u7533\u8bf7ID", required=true)
    private @NotNull(message="id\u4e0d\u80fd\u4e3a\u7a7a") Integer id;
    @NotNull(message="\u5ba1\u6838\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a")
    @IntegerContains(values={1, 2}, message="\u5ba1\u6838\u72b6\u6001\u53d6\u503c\u4e0d\u5408\u6cd5(1=\u901a\u8fc7,2=\u62d2\u7edd)")
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
        if (!(o instanceof CourseRefundAuditValidate)) {
            return false;
        }
        CourseRefundAuditValidate other = (CourseRefundAuditValidate)o;
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
        return other instanceof CourseRefundAuditValidate;
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
        return "CourseRefundAuditValidate(id=" + this.getId() + ", status=" + this.getStatus() + ", auditRemark=" + this.getAuditRemark() + ")";
    }
}

