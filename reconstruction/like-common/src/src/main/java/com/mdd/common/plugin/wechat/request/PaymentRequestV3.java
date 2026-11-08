/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.plugin.wechat.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaymentRequestV3
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer terminal;
    private String openId;
    private String attach;
    private String outTradeNo;
    private BigDecimal orderAmount;
    private String description;

    public Integer getTerminal() {
        return this.terminal;
    }

    public String getOpenId() {
        return this.openId;
    }

    public String getAttach() {
        return this.attach;
    }

    public String getOutTradeNo() {
        return this.outTradeNo;
    }

    public BigDecimal getOrderAmount() {
        return this.orderAmount;
    }

    public String getDescription() {
        return this.description;
    }

    public void setTerminal(Integer terminal) {
        this.terminal = terminal;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PaymentRequestV3)) {
            return false;
        }
        PaymentRequestV3 other = (PaymentRequestV3)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$terminal = this.getTerminal();
        Integer other$terminal = other.getTerminal();
        if (this$terminal == null ? other$terminal != null : !((Object)this$terminal).equals(other$terminal)) {
            return false;
        }
        String this$openId = this.getOpenId();
        String other$openId = other.getOpenId();
        if (this$openId == null ? other$openId != null : !this$openId.equals(other$openId)) {
            return false;
        }
        String this$attach = this.getAttach();
        String other$attach = other.getAttach();
        if (this$attach == null ? other$attach != null : !this$attach.equals(other$attach)) {
            return false;
        }
        String this$outTradeNo = this.getOutTradeNo();
        String other$outTradeNo = other.getOutTradeNo();
        if (this$outTradeNo == null ? other$outTradeNo != null : !this$outTradeNo.equals(other$outTradeNo)) {
            return false;
        }
        BigDecimal this$orderAmount = this.getOrderAmount();
        BigDecimal other$orderAmount = other.getOrderAmount();
        if (this$orderAmount == null ? other$orderAmount != null : !((Object)this$orderAmount).equals(other$orderAmount)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        return !(this$description == null ? other$description != null : !this$description.equals(other$description));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PaymentRequestV3;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $terminal = this.getTerminal();
        result = result * 59 + ($terminal == null ? 43 : ((Object)$terminal).hashCode());
        String $openId = this.getOpenId();
        result = result * 59 + ($openId == null ? 43 : $openId.hashCode());
        String $attach = this.getAttach();
        result = result * 59 + ($attach == null ? 43 : $attach.hashCode());
        String $outTradeNo = this.getOutTradeNo();
        result = result * 59 + ($outTradeNo == null ? 43 : $outTradeNo.hashCode());
        BigDecimal $orderAmount = this.getOrderAmount();
        result = result * 59 + ($orderAmount == null ? 43 : ((Object)$orderAmount).hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        return result;
    }

    public String toString() {
        return "PaymentRequestV3(terminal=" + this.getTerminal() + ", openId=" + this.getOpenId() + ", attach=" + this.getAttach() + ", outTradeNo=" + this.getOutTradeNo() + ", orderAmount=" + this.getOrderAmount() + ", description=" + this.getDescription() + ")";
    }
}

