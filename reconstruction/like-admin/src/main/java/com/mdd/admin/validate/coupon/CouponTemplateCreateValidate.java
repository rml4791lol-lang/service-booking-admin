package com.mdd.admin.validate.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 券模板新增/编辑参数。
 *
 * 字段与线上 la_coupon_template 一一对应：
 * name/amount/min_amount/valid_days/start_time/expire_time/status/remark。
 */
@ApiModel(value = "优惠券模板创建/编辑参数")
public class CouponTemplateCreateValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID(编辑时传)")
    private Integer id;

    @NotBlank(message = "券名称不能为空")
    @Size(max = 100, message = "券名称最长100位")
    @ApiModelProperty(value = "券名称")
    private String name;

    @NotNull(message = "抵扣金额不能为空")
    @DecimalMin(value = "0.01", message = "抵扣金额必须大于0")
    @ApiModelProperty(value = "抵扣金额")
    private BigDecimal amount;

    @DecimalMin(value = "0.00", message = "满减门槛不能小于0")
    @ApiModelProperty(value = "满减门槛(0=无门槛)")
    private BigDecimal minAmount;

    @Min(value = 0, message = "有效天数不能小于0")
    @Max(value = 36500, message = "有效天数不能超过36500")
    @ApiModelProperty(value = "有效天数(从发放日起算,0=按固定时间)")
    private Integer validDays;

    @ApiModelProperty(value = "固定生效时间(时间戳,秒)")
    private Long startTime;

    @ApiModelProperty(value = "固定过期时间(时间戳,秒)")
    private Long expireTime;

    @Min(value = 0, message = "状态只能是0/1")
    @Max(value = 1, message = "状态只能是0/1")
    @ApiModelProperty(value = "状态:0=启用,1=停用")
    private Integer status;

    @Size(max = 500, message = "备注最长500位")
    @ApiModelProperty(value = "备注")
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getValidDays() {
        return validDays;
    }

    public void setValidDays(Integer validDays) {
        this.validDays = validDays;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
