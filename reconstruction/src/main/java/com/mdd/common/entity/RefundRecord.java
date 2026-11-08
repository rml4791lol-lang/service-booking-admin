/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u9000\u6b3e\u8bb0\u5f55\u5b9e\u4f53")
public class RefundRecord
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u9000\u6b3e\u7f16\u53f7")
    private String sn;
    @ApiModelProperty(value="\u5173\u8054\u7528\u6237ID")
    private Integer userId;
    @ApiModelProperty(value="\u6765\u6e90\u8ba2\u5355ID")
    private Integer orderId;
    private String refundKey;
    @ApiModelProperty(value="\u6765\u6e90\u5355\u53f7SN")
    private String orderSn;
    @ApiModelProperty(value="\u8ba2\u5355\u7c7b\u578b: [order=\u5546\u54c1\u8ba2\u5355, recharge=\u5145\u503c\u8ba2\u5355]")
    private String orderType;
    @ApiModelProperty(value="\u603b\u5e94\u4ed8\u6b3e\u91d1\u989d")
    private BigDecimal orderAmount;
    @ApiModelProperty(value="\u672c\u6b21\u9000\u6b3e\u91d1\u989d")
    private BigDecimal refundAmount;
    @ApiModelProperty(value="\u5e73\u53f0\u4ea4\u6613\u6d41\u6c34\u53f7")
    private String transactionId;
    @ApiModelProperty(value="\u9000\u6b3e\u65b9\u5f0f: 1=\u7ebf\u4e0a\u9000\u6b3e, 2=\u7ebf\u4e0b\u9000\u6b3e")
    private Integer refundWay;
    @ApiModelProperty(value="\u9000\u6b3e\u7c7b\u578b: 1=\u540e\u53f0\u9000\u6b3e")
    private Integer refundType;
    @ApiModelProperty(value="\u9000\u6b3e\u72b6\u6001: 0=\u9000\u6b3e\u4e2d, 1=\u9000\u6b3e\u6210\u529f, 2=\u9000\u6b3e\u5931\u8d25")
    private Integer refundStatus;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Long updateTime;

    public Integer getId() {
        return this.id;
    }

    public String getSn() {
        return this.sn;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public String getRefundKey() {
        return this.refundKey;
    }

    public String getOrderSn() {
        return this.orderSn;
    }

    public String getOrderType() {
        return this.orderType;
    }

    public BigDecimal getOrderAmount() {
        return this.orderAmount;
    }

    public BigDecimal getRefundAmount() {
        return this.refundAmount;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public Integer getRefundWay() {
        return this.refundWay;
    }

    public Integer getRefundType() {
        return this.refundType;
    }

    public Integer getRefundStatus() {
        return this.refundStatus;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setRefundKey(String refundKey) {
        this.refundKey = refundKey;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setRefundWay(Integer refundWay) {
        this.refundWay = refundWay;
    }

    public void setRefundType(Integer refundType) {
        this.refundType = refundType;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RefundRecord)) {
            return false;
        }
        RefundRecord other = (RefundRecord)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$orderId = this.getOrderId();
        Integer other$orderId = other.getOrderId();
        if (this$orderId == null ? other$orderId != null : !((Object)this$orderId).equals(other$orderId)) {
            return false;
        }
        Integer this$refundWay = this.getRefundWay();
        Integer other$refundWay = other.getRefundWay();
        if (this$refundWay == null ? other$refundWay != null : !((Object)this$refundWay).equals(other$refundWay)) {
            return false;
        }
        Integer this$refundType = this.getRefundType();
        Integer other$refundType = other.getRefundType();
        if (this$refundType == null ? other$refundType != null : !((Object)this$refundType).equals(other$refundType)) {
            return false;
        }
        Integer this$refundStatus = this.getRefundStatus();
        Integer other$refundStatus = other.getRefundStatus();
        if (this$refundStatus == null ? other$refundStatus != null : !((Object)this$refundStatus).equals(other$refundStatus)) {
            return false;
        }
        Long this$createTime = this.getCreateTime();
        Long other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Long this$updateTime = this.getUpdateTime();
        Long other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        String this$sn = this.getSn();
        String other$sn = other.getSn();
        if (this$sn == null ? other$sn != null : !this$sn.equals(other$sn)) {
            return false;
        }
        String this$orderSn = this.getOrderSn();
        String other$orderSn = other.getOrderSn();
        if (this$orderSn == null ? other$orderSn != null : !this$orderSn.equals(other$orderSn)) {
            return false;
        }
        String this$orderType = this.getOrderType();
        String other$orderType = other.getOrderType();
        if (this$orderType == null ? other$orderType != null : !this$orderType.equals(other$orderType)) {
            return false;
        }
        BigDecimal this$orderAmount = this.getOrderAmount();
        BigDecimal other$orderAmount = other.getOrderAmount();
        if (this$orderAmount == null ? other$orderAmount != null : !((Object)this$orderAmount).equals(other$orderAmount)) {
            return false;
        }
        BigDecimal this$refundAmount = this.getRefundAmount();
        BigDecimal other$refundAmount = other.getRefundAmount();
        if (this$refundAmount == null ? other$refundAmount != null : !((Object)this$refundAmount).equals(other$refundAmount)) {
            return false;
        }
        String this$transactionId = this.getTransactionId();
        String other$transactionId = other.getTransactionId();
        return !(this$transactionId == null ? other$transactionId != null : !this$transactionId.equals(other$transactionId));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RefundRecord;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $orderId = this.getOrderId();
        result = result * 59 + ($orderId == null ? 43 : ((Object)$orderId).hashCode());
        Integer $refundWay = this.getRefundWay();
        result = result * 59 + ($refundWay == null ? 43 : ((Object)$refundWay).hashCode());
        Integer $refundType = this.getRefundType();
        result = result * 59 + ($refundType == null ? 43 : ((Object)$refundType).hashCode());
        Integer $refundStatus = this.getRefundStatus();
        result = result * 59 + ($refundStatus == null ? 43 : ((Object)$refundStatus).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : $sn.hashCode());
        String $orderSn = this.getOrderSn();
        result = result * 59 + ($orderSn == null ? 43 : $orderSn.hashCode());
        String $orderType = this.getOrderType();
        result = result * 59 + ($orderType == null ? 43 : $orderType.hashCode());
        BigDecimal $orderAmount = this.getOrderAmount();
        result = result * 59 + ($orderAmount == null ? 43 : ((Object)$orderAmount).hashCode());
        BigDecimal $refundAmount = this.getRefundAmount();
        result = result * 59 + ($refundAmount == null ? 43 : ((Object)$refundAmount).hashCode());
        String $transactionId = this.getTransactionId();
        result = result * 59 + ($transactionId == null ? 43 : $transactionId.hashCode());
        return result;
    }

    public String toString() {
        return "RefundRecord(id=" + this.getId() + ", sn=" + this.getSn() + ", userId=" + this.getUserId() + ", orderId=" + this.getOrderId() + ", orderSn=" + this.getOrderSn() + ", orderType=" + this.getOrderType() + ", orderAmount=" + this.getOrderAmount() + ", refundAmount=" + this.getRefundAmount() + ", transactionId=" + this.getTransactionId() + ", refundWay=" + this.getRefundWay() + ", refundType=" + this.getRefundType() + ", refundStatus=" + this.getRefundStatus() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}
