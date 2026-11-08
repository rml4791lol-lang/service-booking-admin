/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request$GoodsDetail
 *  com.google.gson.annotations.SerializedName
 */
package com.mdd.common.plugin.wechat.request;

import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class RefundRequestV3
implements Serializable {
    private static final long serialVersionUID = -1L;
    private String transactionId;
    private String outTradeNo;
    @SerializedName(value="out_refund_no")
    private String outRefundNo;
    private Integer refundAmount;
    private Integer totalAmount;
    private String reason;
    private String notifyUrl;
    private String currency;
    private List<WxPayRefundV3Request.GoodsDetail> goodsDetails;
    private String subMchid;

    public String getTransactionId() {
        return this.transactionId;
    }

    public String getOutTradeNo() {
        return this.outTradeNo;
    }

    public String getOutRefundNo() {
        return this.outRefundNo;
    }

    public Integer getRefundAmount() {
        return this.refundAmount;
    }

    public Integer getTotalAmount() {
        return this.totalAmount;
    }

    public String getReason() {
        return this.reason;
    }

    public String getNotifyUrl() {
        return this.notifyUrl;
    }

    public String getCurrency() {
        return this.currency;
    }

    public List<WxPayRefundV3Request.GoodsDetail> getGoodsDetails() {
        return this.goodsDetails;
    }

    public String getSubMchid() {
        return this.subMchid;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public void setRefundAmount(Integer refundAmount) {
        this.refundAmount = refundAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setGoodsDetails(List<WxPayRefundV3Request.GoodsDetail> goodsDetails) {
        this.goodsDetails = goodsDetails;
    }

    public void setSubMchid(String subMchid) {
        this.subMchid = subMchid;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RefundRequestV3)) {
            return false;
        }
        RefundRequestV3 other = (RefundRequestV3)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$refundAmount = this.getRefundAmount();
        Integer other$refundAmount = other.getRefundAmount();
        if (this$refundAmount == null ? other$refundAmount != null : !((Object)this$refundAmount).equals(other$refundAmount)) {
            return false;
        }
        Integer this$totalAmount = this.getTotalAmount();
        Integer other$totalAmount = other.getTotalAmount();
        if (this$totalAmount == null ? other$totalAmount != null : !((Object)this$totalAmount).equals(other$totalAmount)) {
            return false;
        }
        String this$transactionId = this.getTransactionId();
        String other$transactionId = other.getTransactionId();
        if (this$transactionId == null ? other$transactionId != null : !this$transactionId.equals(other$transactionId)) {
            return false;
        }
        String this$outTradeNo = this.getOutTradeNo();
        String other$outTradeNo = other.getOutTradeNo();
        if (this$outTradeNo == null ? other$outTradeNo != null : !this$outTradeNo.equals(other$outTradeNo)) {
            return false;
        }
        String this$outRefundNo = this.getOutRefundNo();
        String other$outRefundNo = other.getOutRefundNo();
        if (this$outRefundNo == null ? other$outRefundNo != null : !this$outRefundNo.equals(other$outRefundNo)) {
            return false;
        }
        String this$reason = this.getReason();
        String other$reason = other.getReason();
        if (this$reason == null ? other$reason != null : !this$reason.equals(other$reason)) {
            return false;
        }
        String this$notifyUrl = this.getNotifyUrl();
        String other$notifyUrl = other.getNotifyUrl();
        if (this$notifyUrl == null ? other$notifyUrl != null : !this$notifyUrl.equals(other$notifyUrl)) {
            return false;
        }
        String this$currency = this.getCurrency();
        String other$currency = other.getCurrency();
        if (this$currency == null ? other$currency != null : !this$currency.equals(other$currency)) {
            return false;
        }
        List<WxPayRefundV3Request.GoodsDetail> this$goodsDetails = this.getGoodsDetails();
        List<WxPayRefundV3Request.GoodsDetail> other$goodsDetails = other.getGoodsDetails();
        if (this$goodsDetails == null ? other$goodsDetails != null : !((Object)this$goodsDetails).equals(other$goodsDetails)) {
            return false;
        }
        String this$subMchid = this.getSubMchid();
        String other$subMchid = other.getSubMchid();
        return !(this$subMchid == null ? other$subMchid != null : !this$subMchid.equals(other$subMchid));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RefundRequestV3;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $refundAmount = this.getRefundAmount();
        result = result * 59 + ($refundAmount == null ? 43 : ((Object)$refundAmount).hashCode());
        Integer $totalAmount = this.getTotalAmount();
        result = result * 59 + ($totalAmount == null ? 43 : ((Object)$totalAmount).hashCode());
        String $transactionId = this.getTransactionId();
        result = result * 59 + ($transactionId == null ? 43 : $transactionId.hashCode());
        String $outTradeNo = this.getOutTradeNo();
        result = result * 59 + ($outTradeNo == null ? 43 : $outTradeNo.hashCode());
        String $outRefundNo = this.getOutRefundNo();
        result = result * 59 + ($outRefundNo == null ? 43 : $outRefundNo.hashCode());
        String $reason = this.getReason();
        result = result * 59 + ($reason == null ? 43 : $reason.hashCode());
        String $notifyUrl = this.getNotifyUrl();
        result = result * 59 + ($notifyUrl == null ? 43 : $notifyUrl.hashCode());
        String $currency = this.getCurrency();
        result = result * 59 + ($currency == null ? 43 : $currency.hashCode());
        List<WxPayRefundV3Request.GoodsDetail> $goodsDetails = this.getGoodsDetails();
        result = result * 59 + ($goodsDetails == null ? 43 : ((Object)$goodsDetails).hashCode());
        String $subMchid = this.getSubMchid();
        result = result * 59 + ($subMchid == null ? 43 : $subMchid.hashCode());
        return result;
    }

    public String toString() {
        return "RefundRequestV3(transactionId=" + this.getTransactionId() + ", outTradeNo=" + this.getOutTradeNo() + ", outRefundNo=" + this.getOutRefundNo() + ", refundAmount=" + this.getRefundAmount() + ", totalAmount=" + this.getTotalAmount() + ", reason=" + this.getReason() + ", notifyUrl=" + this.getNotifyUrl() + ", currency=" + this.getCurrency() + ", goodsDetails=" + this.getGoodsDetails() + ", subMchid=" + this.getSubMchid() + ")";
    }
}

