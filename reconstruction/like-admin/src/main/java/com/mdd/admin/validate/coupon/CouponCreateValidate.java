package com.mdd.admin.validate.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "优惠券创建参数")
public class CouponCreateValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "券名称不能为空")
    @Size(max = 100, message = "券名称最长100位")
    @ApiModelProperty(value = "券名称")
    private String name;

    @NotNull(message = "抵扣金额不能为空")
    @DecimalMin(value = "0.01", message = "抵扣金额必须大于0")
    @ApiModelProperty(value = "抵扣金额")
    private BigDecimal amount;

    @DecimalMin(value = "0.00", message = "最低消费金额不能小于0")
    @ApiModelProperty(value = "最低消费金额(0=无门槛)")
    private BigDecimal minAmount;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式不正确")
    @ApiModelProperty(value = "绑定手机号")
    private String mobile;

    @NotNull(message = "生效时间不能为空")
    @ApiModelProperty(value = "生效时间(时间戳)")
    private Long startTime;

    @NotNull(message = "过期时间不能为空")
    @ApiModelProperty(value = "过期时间(时间戳)")
    private Long expireTime;

    @Size(max = 255, message = "备注最长255位")
    @ApiModelProperty(value = "备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
