/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.finance.FinanceRefundListVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.finance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u9000\u6b3e\u8bb0\u5f55\u5217\u8868Vo")
public class FinanceRefundListVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u8bb0\u5f55ID")
    private Integer id;
    @ApiModelProperty(value="\u9000\u6b3e\u7f16\u53f7")
    private String sn;
    @ApiModelProperty(value="\u7528\u6237\u5934\u50cf")
    private String avatar;
    @ApiModelProperty(value="\u7528\u6237\u6635\u79f0")
    private String nickname;
    @ApiModelProperty(value="\u6765\u6e90\u5355\u53f7SN")
    private String orderSn;
    @ApiModelProperty(value="\u603b\u5e94\u4ed8\u6b3e\u91d1\u989d")
    private BigDecimal orderAmount;
    @ApiModelProperty(value="\u672c\u6b21\u9000\u6b3e\u91d1\u989d")
    private BigDecimal refundAmount;
    @ApiModelProperty(value="\u9000\u6b3e\u7c7b\u578b: 1=\u540e\u53f0\u9000\u6b3e")
    private Integer refundType;
    @ApiModelProperty(value="\u9000\u6b3e\u72b6\u6001: 0=\u9000\u6b3e\u4e2d, 1=\u9000\u6b3e\u6210\u529f, 2=\u9000\u6b3e\u5931\u8d25")
    private Integer refundStatus;
    private Integer refundWay;
    @ApiModelProperty(value="\u9000\u6b3e\u7c7b\u578b\u63cf\u8ff0")
    private String refundTypeText;
    @ApiModelProperty(value="\u9000\u6b3e\u72b6\u6001\u63cf\u8ff0")
    private String refundStatusText;
    private String refundWayText;
    @ApiModelProperty(value="\u8bb0\u5f55\u65f6\u95f4")
    private String createTime;

    public Integer getId() {
        return this.id;
    }

    public String getSn() {
        return this.sn;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getOrderSn() {
        return this.orderSn;
    }

    public BigDecimal getOrderAmount() {
        return this.orderAmount;
    }

    public BigDecimal getRefundAmount() {
        return this.refundAmount;
    }

    public Integer getRefundType() {
        return this.refundType;
    }

    public Integer getRefundStatus() {
        return this.refundStatus;
    }

    public Integer getRefundWay() {
        return this.refundWay;
    }

    public String getRefundTypeText() {
        return this.refundTypeText;
    }

    public String getRefundStatusText() {
        return this.refundStatusText;
    }

    public String getRefundWayText() {
        return this.refundWayText;
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

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public void setRefundType(Integer refundType) {
        this.refundType = refundType;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public void setRefundWay(Integer refundWay) {
        this.refundWay = refundWay;
    }

    public void setRefundTypeText(String refundTypeText) {
        this.refundTypeText = refundTypeText;
    }

    public void setRefundStatusText(String refundStatusText) {
        this.refundStatusText = refundStatusText;
    }

    public void setRefundWayText(String refundWayText) {
        this.refundWayText = refundWayText;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FinanceRefundListVo)) {
            return false;
        }
        FinanceRefundListVo other = (FinanceRefundListVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
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
        Integer this$refundWay = this.getRefundWay();
        Integer other$refundWay = other.getRefundWay();
        if (this$refundWay == null ? other$refundWay != null : !((Object)this$refundWay).equals(other$refundWay)) {
            return false;
        }
        String this$sn = this.getSn();
        String other$sn = other.getSn();
        if (this$sn == null ? other$sn != null : !this$sn.equals(other$sn)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
            return false;
        }
        String this$nickname = this.getNickname();
        String other$nickname = other.getNickname();
        if (this$nickname == null ? other$nickname != null : !this$nickname.equals(other$nickname)) {
            return false;
        }
        String this$orderSn = this.getOrderSn();
        String other$orderSn = other.getOrderSn();
        if (this$orderSn == null ? other$orderSn != null : !this$orderSn.equals(other$orderSn)) {
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
        String this$refundTypeText = this.getRefundTypeText();
        String other$refundTypeText = other.getRefundTypeText();
        if (this$refundTypeText == null ? other$refundTypeText != null : !this$refundTypeText.equals(other$refundTypeText)) {
            return false;
        }
        String this$refundStatusText = this.getRefundStatusText();
        String other$refundStatusText = other.getRefundStatusText();
        if (this$refundStatusText == null ? other$refundStatusText != null : !this$refundStatusText.equals(other$refundStatusText)) {
            return false;
        }
        String this$refundWayText = this.getRefundWayText();
        String other$refundWayText = other.getRefundWayText();
        if (this$refundWayText == null ? other$refundWayText != null : !this$refundWayText.equals(other$refundWayText)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof FinanceRefundListVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $refundType = this.getRefundType();
        result = result * 59 + ($refundType == null ? 43 : ((Object)$refundType).hashCode());
        Integer $refundStatus = this.getRefundStatus();
        result = result * 59 + ($refundStatus == null ? 43 : ((Object)$refundStatus).hashCode());
        Integer $refundWay = this.getRefundWay();
        result = result * 59 + ($refundWay == null ? 43 : ((Object)$refundWay).hashCode());
        String $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : $sn.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $nickname = this.getNickname();
        result = result * 59 + ($nickname == null ? 43 : $nickname.hashCode());
        String $orderSn = this.getOrderSn();
        result = result * 59 + ($orderSn == null ? 43 : $orderSn.hashCode());
        BigDecimal $orderAmount = this.getOrderAmount();
        result = result * 59 + ($orderAmount == null ? 43 : ((Object)$orderAmount).hashCode());
        BigDecimal $refundAmount = this.getRefundAmount();
        result = result * 59 + ($refundAmount == null ? 43 : ((Object)$refundAmount).hashCode());
        String $refundTypeText = this.getRefundTypeText();
        result = result * 59 + ($refundTypeText == null ? 43 : $refundTypeText.hashCode());
        String $refundStatusText = this.getRefundStatusText();
        result = result * 59 + ($refundStatusText == null ? 43 : $refundStatusText.hashCode());
        String $refundWayText = this.getRefundWayText();
        result = result * 59 + ($refundWayText == null ? 43 : $refundWayText.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        return result;
    }

    public String toString() {
        return "FinanceRefundListVo(id=" + this.getId() + ", sn=" + this.getSn() + ", avatar=" + this.getAvatar() + ", nickname=" + this.getNickname() + ", orderSn=" + this.getOrderSn() + ", orderAmount=" + this.getOrderAmount() + ", refundAmount=" + this.getRefundAmount() + ", refundType=" + this.getRefundType() + ", refundStatus=" + this.getRefundStatus() + ", refundWay=" + this.getRefundWay() + ", refundTypeText=" + this.getRefundTypeText() + ", refundStatusText=" + this.getRefundStatusText() + ", refundWayText=" + this.getRefundWayText() + ", createTime=" + this.getCreateTime() + ")";
    }
}

