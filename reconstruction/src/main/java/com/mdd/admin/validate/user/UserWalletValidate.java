/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.user.UserWalletValidate
 *  com.mdd.common.validator.annotation.IntegerContains
 *  javax.validation.constraints.Min
 *  javax.validation.constraints.NotNull
 */
package com.mdd.admin.validate.user;

import com.mdd.common.validator.annotation.IntegerContains;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UserWalletValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="\u7f3a\u5c11\u7528\u6237id\u53c2\u6570")
    @Min(value=0L, message="\u7528\u6237id\u5fc5\u987b\u4e3a\u6570\u5b57")
    private @NotNull(message="\u7f3a\u5c11\u7528\u6237id\u53c2\u6570") @Min(value=0L, message="\u7528\u6237id\u5fc5\u987b\u4e3a\u6570\u5b57") Integer userId;
    @NotNull(message="\u8bf7\u8f93\u5165\u53d8\u52a8\u7c7b\u578b")
    @IntegerContains(values={2, 1}, message="\u53d8\u52a8\u7c7b\u578b\u9519\u8bef")
    private @NotNull(message="\u8bf7\u8f93\u5165\u53d8\u52a8\u7c7b\u578b") Integer action;
    @NotNull(message="\u8bf7\u8f93\u5165\u53d8\u52a8\u91d1\u989d")
    @DecimalMin(value="0.01", message="\u53d8\u52a8\u91d1\u989d\u5fc5\u987b\u5927\u4e8e0")
    @Digits(integer=10, fraction=2, message="\u53d8\u52a8\u91d1\u989d\u6700\u591a\u4e24\u4f4d\u5c0f\u6570")
    private @NotNull(message="\u8bf7\u8f93\u5165\u53d8\u52a8\u91d1\u989d") @DecimalMin(value="0.01", message="\u53d8\u52a8\u91d1\u989d\u5fc5\u987b\u5927\u4e8e0") @Digits(integer=10, fraction=2, message="\u53d8\u52a8\u91d1\u989d\u6700\u591a\u4e24\u4f4d\u5c0f\u6570") BigDecimal num;
    private String remark;

    @NotNull(message="\u7f3a\u5c11\u7528\u6237id\u53c2\u6570")
    public @NotNull(message="\u7f3a\u5c11\u7528\u6237id\u53c2\u6570") Integer getUserId() {
        return this.userId;
    }

    @NotNull(message="\u8bf7\u8f93\u5165\u53d8\u52a8\u7c7b\u578b")
    public @NotNull(message="\u8bf7\u8f93\u5165\u53d8\u52a8\u7c7b\u578b") Integer getAction() {
        return this.action;
    }

    @NotNull(message="\u8bf7\u8f93\u5165\u53d8\u52a8\u91d1\u989d")
    public @NotNull(message="\u8bf7\u8f93\u5165\u53d8\u52a8\u91d1\u989d") BigDecimal getNum() {
        return this.num;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setUserId(@NotNull(message="\u7f3a\u5c11\u7528\u6237id\u53c2\u6570") @NotNull(message="\u7f3a\u5c11\u7528\u6237id\u53c2\u6570") Integer userId) {
        this.userId = userId;
    }

    public void setAction(@NotNull(message="\u8bf7\u8f93\u5165\u53d8\u52a8\u7c7b\u578b") @NotNull(message="\u8bf7\u8f93\u5165\u53d8\u52a8\u7c7b\u578b") Integer action) {
        this.action = action;
    }

    public void setNum(@NotNull(message="\u8bf7\u8f93\u5165\u53d8\u52a8\u91d1\u989d") @NotNull(message="\u8bf7\u8f93\u5165\u53d8\u52a8\u91d1\u989d") BigDecimal num) {
        this.num = num;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserWalletValidate)) {
            return false;
        }
        UserWalletValidate other = (UserWalletValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$action = this.getAction();
        Integer other$action = other.getAction();
        if (this$action == null ? other$action != null : !((Object)this$action).equals(other$action)) {
            return false;
        }
        BigDecimal this$num = this.getNum();
        BigDecimal other$num = other.getNum();
        if (this$num == null ? other$num != null : !((Object)this$num).equals(other$num)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        return !(this$remark == null ? other$remark != null : !this$remark.equals(other$remark));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserWalletValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $action = this.getAction();
        result = result * 59 + ($action == null ? 43 : ((Object)$action).hashCode());
        BigDecimal $num = this.getNum();
        result = result * 59 + ($num == null ? 43 : ((Object)$num).hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        return result;
    }

    public String toString() {
        return "UserWalletValidate(userId=" + this.getUserId() + ", action=" + this.getAction() + ", num=" + this.getNum() + ", remark=" + this.getRemark() + ")";
    }
}

