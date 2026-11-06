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

@ApiModel(value="\u9000\u6b3e\u65e5\u5fd7\u5b9e\u4f53")
public class RefundLog
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u7f16\u53f7")
    private String sn;
    @ApiModelProperty(value="\u9000\u6b3e\u8bb0\u5f55ID")
    private Integer recordId;
    @ApiModelProperty(value="\u5173\u8054\u7528\u6237ID")
    private Integer userId;
    @ApiModelProperty(value="\u5904\u7406\u7ba1\u7406ID")
    private Integer handleId;
    @ApiModelProperty(value="\u603b\u5e94\u4ed8\u7684\u91d1\u989d")
    private BigDecimal orderAmount;
    @ApiModelProperty(value="\u672c\u6b21\u9000\u6b3e\u91d1\u989d")
    private BigDecimal refundAmount;
    @ApiModelProperty(value="\u9000\u6b3e\u72b6\u6001: [0=\u9000\u6b3e\u4e2d, 1=\u9000\u6b3e\u6210\u529f, 2=\u9000\u6b3e\u5931\u8d25]")
    private Integer refundStatus;
    @ApiModelProperty(value="\u9000\u6b3e\u4fe1\u606f")
    private String refundMsg;
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

    public Integer getRecordId() {
        return this.recordId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getHandleId() {
        return this.handleId;
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

    public String getRefundMsg() {
        return this.refundMsg;
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

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setHandleId(Integer handleId) {
        this.handleId = handleId;
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

    public void setRefundMsg(String refundMsg) {
        this.refundMsg = refundMsg;
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
        if (!(o instanceof RefundLog)) {
            return false;
        }
        RefundLog other = (RefundLog)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$recordId = this.getRecordId();
        Integer other$recordId = other.getRecordId();
        if (this$recordId == null ? other$recordId != null : !((Object)this$recordId).equals(other$recordId)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$handleId = this.getHandleId();
        Integer other$handleId = other.getHandleId();
        if (this$handleId == null ? other$handleId != null : !((Object)this$handleId).equals(other$handleId)) {
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
        String this$refundMsg = this.getRefundMsg();
        String other$refundMsg = other.getRefundMsg();
        return !(this$refundMsg == null ? other$refundMsg != null : !this$refundMsg.equals(other$refundMsg));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RefundLog;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $recordId = this.getRecordId();
        result = result * 59 + ($recordId == null ? 43 : ((Object)$recordId).hashCode());
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $handleId = this.getHandleId();
        result = result * 59 + ($handleId == null ? 43 : ((Object)$handleId).hashCode());
        Integer $refundStatus = this.getRefundStatus();
        result = result * 59 + ($refundStatus == null ? 43 : ((Object)$refundStatus).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : $sn.hashCode());
        BigDecimal $orderAmount = this.getOrderAmount();
        result = result * 59 + ($orderAmount == null ? 43 : ((Object)$orderAmount).hashCode());
        BigDecimal $refundAmount = this.getRefundAmount();
        result = result * 59 + ($refundAmount == null ? 43 : ((Object)$refundAmount).hashCode());
        String $refundMsg = this.getRefundMsg();
        result = result * 59 + ($refundMsg == null ? 43 : $refundMsg.hashCode());
        return result;
    }

    public String toString() {
        return "RefundLog(id=" + this.getId() + ", sn=" + this.getSn() + ", recordId=" + this.getRecordId() + ", userId=" + this.getUserId() + ", handleId=" + this.getHandleId() + ", orderAmount=" + this.getOrderAmount() + ", refundAmount=" + this.getRefundAmount() + ", refundStatus=" + this.getRefundStatus() + ", refundMsg=" + this.getRefundMsg() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

