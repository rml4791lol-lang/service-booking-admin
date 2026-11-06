/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.agent.AgentWithdrawalAuditValidate
 *  javax.validation.constraints.NotNull
 */
package com.mdd.admin.validate.agent;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

public class AgentWithdrawalAuditValidate
implements Serializable {
    @NotNull(message="\u8bb0\u5f55ID\u4e0d\u80fd\u4e3a\u7a7a")
    private @NotNull(message="\u8bb0\u5f55ID\u4e0d\u80fd\u4e3a\u7a7a") Integer id;
    @NotNull(message="\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a")
    private @NotNull(message="\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a") Integer status;
    private String auditRemark;

    @NotNull(message="\u8bb0\u5f55ID\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u8bb0\u5f55ID\u4e0d\u80fd\u4e3a\u7a7a") Integer getId() {
        return this.id;
    }

    @NotNull(message="\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a") Integer getStatus() {
        return this.status;
    }

    public String getAuditRemark() {
        return this.auditRemark;
    }

    public void setId(@NotNull(message="\u8bb0\u5f55ID\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u8bb0\u5f55ID\u4e0d\u80fd\u4e3a\u7a7a") Integer id) {
        this.id = id;
    }

    public void setStatus(@NotNull(message="\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a") Integer status) {
        this.status = status;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AgentWithdrawalAuditValidate)) {
            return false;
        }
        AgentWithdrawalAuditValidate other = (AgentWithdrawalAuditValidate)o;
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
        return other instanceof AgentWithdrawalAuditValidate;
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
        return "AgentWithdrawalAuditValidate(id=" + this.getId() + ", status=" + this.getStatus() + ", auditRemark=" + this.getAuditRemark() + ")";
    }
}

