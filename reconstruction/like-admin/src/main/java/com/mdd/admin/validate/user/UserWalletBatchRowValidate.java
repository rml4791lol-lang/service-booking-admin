package com.mdd.admin.validate.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 余额批量调整单行参数（CSV 由前端解析后提交 JSON 行数组）。
 *
 * <p>amount 正负表示增减：正数增加、负数扣减；remark 必填，用于流水留痕。</p>
 */
@ApiModel(value = "余额批量调整单行参数")
public class UserWalletBatchRowValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "家长手机号")
    private String mobile;

    @ApiModelProperty(value = "调整金额（正数增加，负数扣减）")
    private BigDecimal amount;

    @ApiModelProperty(value = "备注（必填）")
    private String remark;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
