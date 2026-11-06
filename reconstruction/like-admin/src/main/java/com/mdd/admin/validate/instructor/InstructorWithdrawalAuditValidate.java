/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.instructor.InstructorWithdrawalAuditValidate
 *  javax.validation.constraints.NotNull
 */
package com.mdd.admin.validate.instructor;

import com.mdd.common.validator.annotation.IntegerContains;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

public class InstructorWithdrawalAuditValidate
implements Serializable {
    @NotNull(message="id\u53c2\u6570\u7f3a\u5931")
    private @NotNull(message="id\u53c2\u6570\u7f3a\u5931") Integer id;
    @NotNull(message="\u5ba1\u6838\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a(1=\u901a\u8fc7, 2=\u62d2\u7edd)")
    @IntegerContains(values={1, 2}, message="\u5ba1\u6838\u72b6\u6001\u53d6\u503c\u4e0d\u5408\u6cd5(1=\u901a\u8fc7, 2=\u62d2\u7edd)")
    private @NotNull(message="\u5ba1\u6838\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a(1=\u901a\u8fc7, 2=\u62d2\u7edd)") Integer status;
    private String auditRemark;

    @NotNull(message="id\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="id\u53c2\u6570\u7f3a\u5931") Integer getId() {
        return this.id;
    }

    @NotNull(message="\u5ba1\u6838\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a(1=\u901a\u8fc7, 2=\u62d2\u7edd)")
    public @NotNull(message="\u5ba1\u6838\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a(1=\u901a\u8fc7, 2=\u62d2\u7edd)") Integer getStatus() {
        return this.status;
    }

    public String getAuditRemark() {
        return this.auditRemark;
    }

    public void setId(@NotNull(message="id\u53c2\u6570\u7f3a\u5931") @NotNull(message="id\u53c2\u6570\u7f3a\u5931") Integer id) {
        this.id = id;
    }

    public void setStatus(@NotNull(message="\u5ba1\u6838\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a(1=\u901a\u8fc7, 2=\u62d2\u7edd)") @NotNull(message="\u5ba1\u6838\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a(1=\u901a\u8fc7, 2=\u62d2\u7edd)") Integer status) {
        this.status = status;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InstructorWithdrawalAuditValidate)) {
            return false;
        }
        InstructorWithdrawalAuditValidate other = (InstructorWithdrawalAuditValidate)o;
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
        return other instanceof InstructorWithdrawalAuditValidate;
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
        return "InstructorWithdrawalAuditValidate(id=" + this.getId() + ", status=" + this.getStatus() + ", auditRemark=" + this.getAuditRemark() + ")";
    }
}

