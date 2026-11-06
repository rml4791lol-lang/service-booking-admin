/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.finance.FinanceWalletSearchValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.finance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u4f59\u989d\u8bb0\u5f55\u641c\u7d22\u53c2\u6570")
public class FinanceWalletSearchValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u5173\u952e\u8bcd")
    private String user_info;
    @ApiModelProperty(value="\u7c7b\u578b")
    private Integer change_type;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String start_time;
    @ApiModelProperty(value="\u7ed3\u675f\u65f6\u95f4")
    private String end_time;

    public String getUser_info() {
        return this.user_info;
    }

    public Integer getChange_type() {
        return this.change_type;
    }

    public String getStart_time() {
        return this.start_time;
    }

    public String getEnd_time() {
        return this.end_time;
    }

    public void setUser_info(String user_info) {
        this.user_info = user_info;
    }

    public void setChange_type(Integer change_type) {
        this.change_type = change_type;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FinanceWalletSearchValidate)) {
            return false;
        }
        FinanceWalletSearchValidate other = (FinanceWalletSearchValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$change_type = this.getChange_type();
        Integer other$change_type = other.getChange_type();
        if (this$change_type == null ? other$change_type != null : !((Object)this$change_type).equals(other$change_type)) {
            return false;
        }
        String this$user_info = this.getUser_info();
        String other$user_info = other.getUser_info();
        if (this$user_info == null ? other$user_info != null : !this$user_info.equals(other$user_info)) {
            return false;
        }
        String this$start_time = this.getStart_time();
        String other$start_time = other.getStart_time();
        if (this$start_time == null ? other$start_time != null : !this$start_time.equals(other$start_time)) {
            return false;
        }
        String this$end_time = this.getEnd_time();
        String other$end_time = other.getEnd_time();
        return !(this$end_time == null ? other$end_time != null : !this$end_time.equals(other$end_time));
    }

    protected boolean canEqual(Object other) {
        return other instanceof FinanceWalletSearchValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $change_type = this.getChange_type();
        result = result * 59 + ($change_type == null ? 43 : ((Object)$change_type).hashCode());
        String $user_info = this.getUser_info();
        result = result * 59 + ($user_info == null ? 43 : $user_info.hashCode());
        String $start_time = this.getStart_time();
        result = result * 59 + ($start_time == null ? 43 : $start_time.hashCode());
        String $end_time = this.getEnd_time();
        result = result * 59 + ($end_time == null ? 43 : $end_time.hashCode());
        return result;
    }

    public String toString() {
        return "FinanceWalletSearchValidate(user_info=" + this.getUser_info() + ", change_type=" + this.getChange_type() + ", start_time=" + this.getStart_time() + ", end_time=" + this.getEnd_time() + ")";
    }
}

