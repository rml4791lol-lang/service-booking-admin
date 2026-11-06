/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.front.vo.pay.PayStatusVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.vo.pay;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u652f\u4ed8\u72b6\u6001Vo")
public class PayStatusVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u8ba2\u5355\u4fe1\u606f")
    private JSONObject order;
    @ApiModelProperty(value="\u652f\u4ed8\u72b6\u6001: [0=\u5f85\u652f\u4ed8, 1=\u5df2\u652f\u4ed8]")
    private Integer payStatus;
    @ApiModelProperty(value="\u8ba2\u5355ID")
    private Integer orderId;

    public JSONObject getOrder() {
        return this.order;
    }

    public Integer getPayStatus() {
        return this.payStatus;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrder(JSONObject order) {
        this.order = order;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PayStatusVo)) {
            return false;
        }
        PayStatusVo other = (PayStatusVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$payStatus = this.getPayStatus();
        Integer other$payStatus = other.getPayStatus();
        if (this$payStatus == null ? other$payStatus != null : !((Object)this$payStatus).equals(other$payStatus)) {
            return false;
        }
        Integer this$orderId = this.getOrderId();
        Integer other$orderId = other.getOrderId();
        if (this$orderId == null ? other$orderId != null : !((Object)this$orderId).equals(other$orderId)) {
            return false;
        }
        JSONObject this$order = this.getOrder();
        JSONObject other$order = other.getOrder();
        return !(this$order == null ? other$order != null : !this$order.equals(other$order));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PayStatusVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $payStatus = this.getPayStatus();
        result = result * 59 + ($payStatus == null ? 43 : ((Object)$payStatus).hashCode());
        Integer $orderId = this.getOrderId();
        result = result * 59 + ($orderId == null ? 43 : ((Object)$orderId).hashCode());
        JSONObject $order = this.getOrder();
        result = result * 59 + ($order == null ? 43 : $order.hashCode());
        return result;
    }

    public String toString() {
        return "PayStatusVo(order=" + this.getOrder() + ", payStatus=" + this.getPayStatus() + ", orderId=" + this.getOrderId() + ")";
    }
}

