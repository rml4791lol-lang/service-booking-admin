/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.finance.FinanceRefundSearchValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.finance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u9000\u6b3e\u8bb0\u5f55\u641c\u7d22\u53c2\u6570")
public class FinanceRefundSearchValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u7b5b\u9009\u7c7b\u578b: -1=\u5168\u90e8, 0=\u9000\u6b3e\u4e2d, 1=\u9000\u6b3e\u6210\u529f, 2=\u9000\u6b3e\u5931\u8d25")
    private Integer refund_type;
    @ApiModelProperty(value="\u7528\u6237\u4fe1\u606f")
    private String user_info;
    @ApiModelProperty(value="\u9000\u6b3e\u5355\u53f7")
    private String sn;
    @ApiModelProperty(value="\u6765\u6e90\u5355\u53f7")
    private String order_sn;
    private Integer refund_status;
    @ApiModelProperty(value="\u5f00\u59cb\u65f6\u95f4")
    private String start_time;
    @ApiModelProperty(value="\u7ed3\u675f\u65f6\u95f4")
    private String end_time;

    public Integer getRefund_type() {
        return this.refund_type;
    }

    public String getUser_info() {
        return this.user_info;
    }

    public String getSn() {
        return this.sn;
    }

    public String getOrder_sn() {
        return this.order_sn;
    }

    public Integer getRefund_status() {
        return this.refund_status;
    }

    public String getStart_time() {
        return this.start_time;
    }

    public String getEnd_time() {
        return this.end_time;
    }

    public void setRefund_type(Integer refund_type) {
        this.refund_type = refund_type;
    }

    public void setUser_info(String user_info) {
        this.user_info = user_info;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public void setRefund_status(Integer refund_status) {
        this.refund_status = refund_status;
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
        if (!(o instanceof FinanceRefundSearchValidate)) {
            return false;
        }
        FinanceRefundSearchValidate other = (FinanceRefundSearchValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$refund_type = this.getRefund_type();
        Integer other$refund_type = other.getRefund_type();
        if (this$refund_type == null ? other$refund_type != null : !((Object)this$refund_type).equals(other$refund_type)) {
            return false;
        }
        Integer this$refund_status = this.getRefund_status();
        Integer other$refund_status = other.getRefund_status();
        if (this$refund_status == null ? other$refund_status != null : !((Object)this$refund_status).equals(other$refund_status)) {
            return false;
        }
        String this$user_info = this.getUser_info();
        String other$user_info = other.getUser_info();
        if (this$user_info == null ? other$user_info != null : !this$user_info.equals(other$user_info)) {
            return false;
        }
        String this$sn = this.getSn();
        String other$sn = other.getSn();
        if (this$sn == null ? other$sn != null : !this$sn.equals(other$sn)) {
            return false;
        }
        String this$order_sn = this.getOrder_sn();
        String other$order_sn = other.getOrder_sn();
        if (this$order_sn == null ? other$order_sn != null : !this$order_sn.equals(other$order_sn)) {
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
        return other instanceof FinanceRefundSearchValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $refund_type = this.getRefund_type();
        result = result * 59 + ($refund_type == null ? 43 : ((Object)$refund_type).hashCode());
        Integer $refund_status = this.getRefund_status();
        result = result * 59 + ($refund_status == null ? 43 : ((Object)$refund_status).hashCode());
        String $user_info = this.getUser_info();
        result = result * 59 + ($user_info == null ? 43 : $user_info.hashCode());
        String $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : $sn.hashCode());
        String $order_sn = this.getOrder_sn();
        result = result * 59 + ($order_sn == null ? 43 : $order_sn.hashCode());
        String $start_time = this.getStart_time();
        result = result * 59 + ($start_time == null ? 43 : $start_time.hashCode());
        String $end_time = this.getEnd_time();
        result = result * 59 + ($end_time == null ? 43 : $end_time.hashCode());
        return result;
    }

    public String toString() {
        return "FinanceRefundSearchValidate(refund_type=" + this.getRefund_type() + ", user_info=" + this.getUser_info() + ", sn=" + this.getSn() + ", order_sn=" + this.getOrder_sn() + ", refund_status=" + this.getRefund_status() + ", start_time=" + this.getStart_time() + ", end_time=" + this.getEnd_time() + ")";
    }
}

