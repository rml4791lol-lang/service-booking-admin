/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.vo.RechargeRecordVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u7f34\u8d39\u8bb0\u5f55Vo")
public class RechargeRecordVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u91d1\u989d")
    private BigDecimal orderAmount;
    @ApiModelProperty(value="\u63cf\u8ff0")
    private String tips;
    @ApiModelProperty(value="\u64cd\u4f5c")
    private Integer action;
    @ApiModelProperty(value="\u65f6\u95f4")
    private String createTime;
    @ApiModelProperty(value="\u8ba2\u5355\u53f7")
    private String orderSn;
    @ApiModelProperty(value="\u652f\u4ed8\u72b6\u6001: [0=\u5f85\u652f\u4ed8, 1=\u5df2\u652f\u4ed8, 4=\u5df2\u53d6\u6d88]")
    private Integer payStatus;
    @ApiModelProperty(value="\u9000\u6b3e\u72b6\u6001: [0=\u672a\u9000\u6b3e, 1=\u5df2\u9000\u6b3e]")
    private Integer refundStatus;
    @ApiModelProperty(value="\u5c55\u793a\u72b6\u6001: pending/paid/refunded/cancelled")
    private String status;
    @ApiModelProperty(value="\u5c55\u793a\u72b6\u6001\u6587\u6848")
    private String statusText;

    public Integer getId() {
        return this.id;
    }

    public BigDecimal getOrderAmount() {
        return this.orderAmount;
    }

    public String getTips() {
        return this.tips;
    }

    public Integer getAction() {
        return this.action;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderSn() {
        return this.orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Integer getPayStatus() {
        return this.payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getRefundStatus() {
        return this.refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusText() {
        return this.statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RechargeRecordVo)) {
            return false;
        }
        RechargeRecordVo other = (RechargeRecordVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$action = this.getAction();
        Integer other$action = other.getAction();
        if (this$action == null ? other$action != null : !((Object)this$action).equals(other$action)) {
            return false;
        }
        BigDecimal this$orderAmount = this.getOrderAmount();
        BigDecimal other$orderAmount = other.getOrderAmount();
        if (this$orderAmount == null ? other$orderAmount != null : !((Object)this$orderAmount).equals(other$orderAmount)) {
            return false;
        }
        String this$tips = this.getTips();
        String other$tips = other.getTips();
        if (this$tips == null ? other$tips != null : !this$tips.equals(other$tips)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RechargeRecordVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $action = this.getAction();
        result = result * 59 + ($action == null ? 43 : ((Object)$action).hashCode());
        BigDecimal $orderAmount = this.getOrderAmount();
        result = result * 59 + ($orderAmount == null ? 43 : ((Object)$orderAmount).hashCode());
        String $tips = this.getTips();
        result = result * 59 + ($tips == null ? 43 : $tips.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        return result;
    }

    public String toString() {
        return "RechargeRecordVo(id=" + this.getId() + ", orderAmount=" + this.getOrderAmount() + ", tips=" + this.getTips() + ", action=" + this.getAction() + ", createTime=" + this.getCreateTime() + ")";
    }
}

