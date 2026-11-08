package com.mdd.admin.validate.finance;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 后台编辑缴费订单参数（#77）。
 *
 * <p>历史前端（旧后台 recharge_detail）提交 snake_case（pay_way/pay_status/is_audit/coupon_id），
 * 新后台按 issue 约定提交 camelCase；两套字段名都接受，服务层统一校验。</p>
 */
@ApiModel(value = "编辑缴费订单参数")
public class FinanceRechargeEditValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "缴费订单ID", required = true)
    @NotNull(message = "订单ID不能为空")
    @Min(value = 1, message = "订单ID不合法")
    private Integer id;

    @ApiModelProperty(value = "支付方式: [1=余额支付, 2=微信支付, 3=支付宝支付, 4=线下支付]", required = true)
    @NotNull(message = "支付方式不能为空")
    @JsonAlias("pay_way")
    private Integer payWay;

    @ApiModelProperty(value = "支付状态: [0=未支付, 1=已支付, 4=已取消]", required = true)
    @NotNull(message = "支付状态不能为空")
    @JsonAlias("pay_status")
    private Integer payStatus;

    @ApiModelProperty(value = "对账状态: [0=未对账, 1=已对账]", required = true)
    @NotNull(message = "审核状态不能为空")
    @JsonAlias("is_audit")
    private Integer isAudit;

    @ApiModelProperty(value = "支付凭证（线下支付必填）")
    @Size(max = 100, message = "支付凭证地址最长100位")
    private String voucher;

    @ApiModelProperty(value = "抵扣券ID（0/空=不使用或不变更）")
    @Min(value = 0, message = "优惠券ID不合法")
    @JsonAlias("coupon_id")
    private Integer couponId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(Integer isAudit) {
        this.isAudit = isAudit;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    @Override
    public String toString() {
        return "FinanceRechargeEditValidate(id=" + id
                + ", payWay=" + payWay
                + ", payStatus=" + payStatus
                + ", isAudit=" + isAudit
                + ", voucher=" + voucher
                + ", couponId=" + couponId + ")";
    }
}
