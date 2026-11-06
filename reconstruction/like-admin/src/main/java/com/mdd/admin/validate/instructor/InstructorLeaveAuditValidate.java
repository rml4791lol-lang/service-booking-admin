/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.instructor.InstructorLeaveAuditValidate
 *  com.mdd.common.validator.annotation.IDMust
 *  com.mdd.common.validator.annotation.IntegerContains
 *  javax.validation.constraints.NotNull
 */
package com.mdd.admin.validate.instructor;

import com.mdd.common.validator.annotation.IDMust;
import com.mdd.common.validator.annotation.IntegerContains;
import javax.validation.constraints.NotNull;

public class InstructorLeaveAuditValidate {
    @IDMust(message="id\u53c2\u6570\u5fc5\u4f20\u4e14\u9700\u5927\u4e8e0")
    private Integer id;
    @NotNull(message="status\u53c2\u6570\u5fc5\u4f20")
    @IntegerContains(values={1, 2}, message="status\u53c2\u6570\u53d6\u503c\u4e0d\u5408\u6cd5(1=\u6279\u51c6,2=\u62d2\u7edd)")
    private @NotNull(message="status\u53c2\u6570\u5fc5\u4f20") Integer status;
    private String remark;

    public Integer getId() {
        return this.id;
    }

    @NotNull(message="status\u53c2\u6570\u5fc5\u4f20")
    public @NotNull(message="status\u53c2\u6570\u5fc5\u4f20") Integer getStatus() {
        return this.status;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStatus(@NotNull(message="status\u53c2\u6570\u5fc5\u4f20") @NotNull(message="status\u53c2\u6570\u5fc5\u4f20") Integer status) {
        this.status = status;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InstructorLeaveAuditValidate)) {
            return false;
        }
        InstructorLeaveAuditValidate other = (InstructorLeaveAuditValidate)o;
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
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        return !(this$remark == null ? other$remark != null : !this$remark.equals(other$remark));
    }

    protected boolean canEqual(Object other) {
        return other instanceof InstructorLeaveAuditValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        return result;
    }

    public String toString() {
        return "InstructorLeaveAuditValidate(id=" + this.getId() + ", status=" + this.getStatus() + ", remark=" + this.getRemark() + ")";
    }
}

