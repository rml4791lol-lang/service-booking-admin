/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.PaymentValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.front.validate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u9884\u652f\u4ed8\u8ba2\u5355\u53c2\u6570")
public class PaymentValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u652f\u4ed8\u573a\u666f: [recharge=\u5145\u503c,order=\u666e\u901a\u8ba2\u5355]")
    private String scene;
    @NotNull(message="payWay\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u652f\u4ed8\u65b9\u5f0f: [1=\u4f59\u989d\u652f\u4ed8,2=\u5fae\u4fe1\u652f\u4ed8,3=\u652f\u4ed8\u5b9d\u652f\u4ed8]")
    private @NotNull(message="payWay\u53c2\u6570\u7f3a\u5931") Integer payWay;
    @NotNull(message="orderId\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u8ba2\u5355ID")
    private @NotNull(message="orderId\u53c2\u6570\u7f3a\u5931") Integer orderId;
    @ApiModelProperty(value="\u91cd\u5b9a\u5411\u94fe\u63a5: H5\u7aef\u9700\u8981")
    private String redirect;
    @ApiModelProperty(value="\u7528\u6237ID", notes="\u8be5\u53c2\u6570\u65e0\u9700\u4f20\u9012")
    private Integer userId;
    @ApiModelProperty(value="\u8ba2\u5355\u7c7b\u578b", notes="\u8be5\u53c2\u6570\u65e0\u9700\u4f20\u9012")
    private String attach;
    @ApiModelProperty(value="\u8ba2\u5355\u7f16\u53f7", notes="\u8be5\u53c2\u6570\u65e0\u9700\u4f20\u9012")
    private String outTradeNo;
    @ApiModelProperty(value="\u8ba2\u5355\u91d1\u989d", notes="\u8be5\u53c2\u6570\u65e0\u9700\u4f20\u9012")
    private BigDecimal orderAmount;
    @ApiModelProperty(value="\u7528\u6237\u63cf\u8ff0", notes="\u8be5\u53c2\u6570\u65e0\u9700\u4f20\u9012")
    private String description;
    @ApiModelProperty(value="Terminal")
    private Integer Terminal;
    @ApiModelProperty(value="\u5fae\u4fe1\u6388\u6743code")
    private String code;

    public String getScene() {
        return this.scene;
    }

    @NotNull(message="payWay\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="payWay\u53c2\u6570\u7f3a\u5931") Integer getPayWay() {
        return this.payWay;
    }

    @NotNull(message="orderId\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="orderId\u53c2\u6570\u7f3a\u5931") Integer getOrderId() {
        return this.orderId;
    }

    public String getRedirect() {
        return this.redirect;
    }

    public Integer getUserId() {
        return this.userId;
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

    public Integer getTerminal() {
        return this.Terminal;
    }

    public String getCode() {
        return this.code;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public void setPayWay(@NotNull(message="payWay\u53c2\u6570\u7f3a\u5931") @NotNull(message="payWay\u53c2\u6570\u7f3a\u5931") Integer payWay) {
        this.payWay = payWay;
    }

    public void setOrderId(@NotNull(message="orderId\u53c2\u6570\u7f3a\u5931") @NotNull(message="orderId\u53c2\u6570\u7f3a\u5931") Integer orderId) {
        this.orderId = orderId;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public void setTerminal(Integer Terminal) {
        this.Terminal = Terminal;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PaymentValidate)) {
            return false;
        }
        PaymentValidate other = (PaymentValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$payWay = this.getPayWay();
        Integer other$payWay = other.getPayWay();
        if (this$payWay == null ? other$payWay != null : !((Object)this$payWay).equals(other$payWay)) {
            return false;
        }
        Integer this$orderId = this.getOrderId();
        Integer other$orderId = other.getOrderId();
        if (this$orderId == null ? other$orderId != null : !((Object)this$orderId).equals(other$orderId)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$Terminal = this.getTerminal();
        Integer other$Terminal = other.getTerminal();
        if (this$Terminal == null ? other$Terminal != null : !((Object)this$Terminal).equals(other$Terminal)) {
            return false;
        }
        String this$scene = this.getScene();
        String other$scene = other.getScene();
        if (this$scene == null ? other$scene != null : !this$scene.equals(other$scene)) {
            return false;
        }
        String this$redirect = this.getRedirect();
        String other$redirect = other.getRedirect();
        if (this$redirect == null ? other$redirect != null : !this$redirect.equals(other$redirect)) {
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
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        return !(this$code == null ? other$code != null : !this$code.equals(other$code));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PaymentValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $payWay = this.getPayWay();
        result = result * 59 + ($payWay == null ? 43 : ((Object)$payWay).hashCode());
        Integer $orderId = this.getOrderId();
        result = result * 59 + ($orderId == null ? 43 : ((Object)$orderId).hashCode());
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $Terminal = this.getTerminal();
        result = result * 59 + ($Terminal == null ? 43 : ((Object)$Terminal).hashCode());
        String $scene = this.getScene();
        result = result * 59 + ($scene == null ? 43 : $scene.hashCode());
        String $redirect = this.getRedirect();
        result = result * 59 + ($redirect == null ? 43 : $redirect.hashCode());
        String $attach = this.getAttach();
        result = result * 59 + ($attach == null ? 43 : $attach.hashCode());
        String $outTradeNo = this.getOutTradeNo();
        result = result * 59 + ($outTradeNo == null ? 43 : $outTradeNo.hashCode());
        BigDecimal $orderAmount = this.getOrderAmount();
        result = result * 59 + ($orderAmount == null ? 43 : ((Object)$orderAmount).hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        return result;
    }

    public String toString() {
        return "PaymentValidate(scene=" + this.getScene() + ", payWay=" + this.getPayWay() + ", orderId=" + this.getOrderId() + ", redirect=" + this.getRedirect() + ", userId=" + this.getUserId() + ", attach=" + this.getAttach() + ", outTradeNo=" + this.getOutTradeNo() + ", orderAmount=" + this.getOrderAmount() + ", description=" + this.getDescription() + ", Terminal=" + this.getTerminal() + ", code=" + this.getCode() + ")";
    }
}

