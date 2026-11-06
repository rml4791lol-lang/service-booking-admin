/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.vo.pay.PayWayInfoVo
 *  com.mdd.front.vo.pay.PayWayListVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.vo.pay;

import com.mdd.front.vo.pay.PayWayInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ApiModel(value="\u652f\u4ed8\u65b9\u5f0f\u5217\u8868Vo")
public class PayWayListVo
implements Serializable {
    @ApiModelProperty(value="\u8ba2\u5355\u91d1\u989d")
    private BigDecimal orderAmount;
    @ApiModelProperty(value="\u65b9\u5f0f\u5217\u8868")
    private List<PayWayInfoVo> lists;

    public BigDecimal getOrderAmount() {
        return this.orderAmount;
    }

    public List<PayWayInfoVo> getLists() {
        return this.lists;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setLists(List<PayWayInfoVo> lists) {
        this.lists = lists;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PayWayListVo)) {
            return false;
        }
        PayWayListVo other = (PayWayListVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        BigDecimal this$orderAmount = this.getOrderAmount();
        BigDecimal other$orderAmount = other.getOrderAmount();
        if (this$orderAmount == null ? other$orderAmount != null : !((Object)this$orderAmount).equals(other$orderAmount)) {
            return false;
        }
        List this$lists = this.getLists();
        List other$lists = other.getLists();
        return !(this$lists == null ? other$lists != null : !((Object)this$lists).equals(other$lists));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PayWayListVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        BigDecimal $orderAmount = this.getOrderAmount();
        result = result * 59 + ($orderAmount == null ? 43 : ((Object)$orderAmount).hashCode());
        List $lists = this.getLists();
        result = result * 59 + ($lists == null ? 43 : ((Object)$lists).hashCode());
        return result;
    }

    public String toString() {
        return "PayWayListVo(orderAmount=" + this.getOrderAmount() + ", lists=" + this.getLists() + ")";
    }
}

