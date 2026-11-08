/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.finance.FinanceRefundLogVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.finance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u9000\u6b3e\u8bb0\u5f55\u65e5\u5fd7Vo")
public class FinanceRefundLogVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u6d41\u6c34\u53f7")
    private String sn;
    @ApiModelProperty(value="\u8ba2\u5355\u91d1\u989d")
    private BigDecimal orderAmount;
    @ApiModelProperty(value="\u9000\u6b3e\u91d1\u989d")
    private BigDecimal refundAmount;
    @ApiModelProperty(value="\u9000\u6b3e\u72b6\u6001: [0=\u9000\u6b3e\u4e2d, 1=\u9000\u6b3e\u6210\u529f, 2=\u9000\u6b3e\u5931\u8d25]")
    private Integer refundStatus;
    @ApiModelProperty(value="\u9000\u6b3e\u72b6\u6001\u63cf\u8ff0")
    private String refundStatusText;
    @ApiModelProperty(value="\u9000\u6b3e\u4fe1\u606f")
    private String refundMsg;
    @ApiModelProperty(value="\u64cd\u4f5c\u4eba")
    private String handler;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;

    public Integer getId() {
        return this.id;
    }

    public String getSn() {
        return this.sn;
    }

    public BigDecimal getOrderAmount() {
        return this.orderAmount;
    }

    public BigDecimal getRefundAmount() {
        return this.refundAmount;
    }

    public Integer getRefundStatus() {
        return this.refundStatus;
    }

    public String getRefundStatusText() {
        return this.refundStatusText;
    }

    public String getRefundMsg() {
        return this.refundMsg;
    }

    public String getHandler() {
        return this.handler;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public void setRefundStatusText(String refundStatusText) {
        this.refundStatusText = refundStatusText;
    }

    public void setRefundMsg(String refundMsg) {
        this.refundMsg = refundMsg;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FinanceRefundLogVo)) {
            return false;
        }
        FinanceRefundLogVo other = (FinanceRefundLogVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$refundStatus = this.getRefundStatus();
        Integer other$refundStatus = other.getRefundStatus();
        if (this$refundStatus == null ? other$refundStatus != null : !((Object)this$refundStatus).equals(other$refundStatus)) {
            return false;
        }
        String this$sn = this.getSn();
        String other$sn = other.getSn();
        if (this$sn == null ? other$sn != null : !this$sn.equals(other$sn)) {
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
        String this$refundStatusText = this.getRefundStatusText();
        String other$refundStatusText = other.getRefundStatusText();
        if (this$refundStatusText == null ? other$refundStatusText != null : !this$refundStatusText.equals(other$refundStatusText)) {
            return false;
        }
        String this$refundMsg = this.getRefundMsg();
        String other$refundMsg = other.getRefundMsg();
        if (this$refundMsg == null ? other$refundMsg != null : !this$refundMsg.equals(other$refundMsg)) {
            return false;
        }
        String this$handler = this.getHandler();
        String other$handler = other.getHandler();
        if (this$handler == null ? other$handler != null : !this$handler.equals(other$handler)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof FinanceRefundLogVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $refundStatus = this.getRefundStatus();
        result = result * 59 + ($refundStatus == null ? 43 : ((Object)$refundStatus).hashCode());
        String $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : $sn.hashCode());
        BigDecimal $orderAmount = this.getOrderAmount();
        result = result * 59 + ($orderAmount == null ? 43 : ((Object)$orderAmount).hashCode());
        BigDecimal $refundAmount = this.getRefundAmount();
        result = result * 59 + ($refundAmount == null ? 43 : ((Object)$refundAmount).hashCode());
        String $refundStatusText = this.getRefundStatusText();
        result = result * 59 + ($refundStatusText == null ? 43 : $refundStatusText.hashCode());
        String $refundMsg = this.getRefundMsg();
        result = result * 59 + ($refundMsg == null ? 43 : $refundMsg.hashCode());
        String $handler = this.getHandler();
        result = result * 59 + ($handler == null ? 43 : $handler.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        return result;
    }

    public String toString() {
        return "FinanceRefundLogVo(id=" + this.getId() + ", sn=" + this.getSn() + ", orderAmount=" + this.getOrderAmount() + ", refundAmount=" + this.getRefundAmount() + ", refundStatus=" + this.getRefundStatus() + ", refundStatusText=" + this.getRefundStatusText() + ", refundMsg=" + this.getRefundMsg() + ", handler=" + this.getHandler() + ", createTime=" + this.getCreateTime() + ")";
    }
}

