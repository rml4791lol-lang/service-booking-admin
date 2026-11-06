package com.mdd.admin.validate.community;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "社区新增参数")
public class CommunityCreateValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "社区名称不能为空")
    @Size(max = 64, message = "社区名称不能超过64个字符")
    @ApiModelProperty(value = "社区名称", required = true)
    private String name;

    @NotEmpty(message = "联系电话不能为空")
    @Size(max = 20, message = "联系电话不能超过20个字符")
    @ApiModelProperty(value = "联系电话", required = true)
    private String mobile;

    @NotNull(message = "分成比例不能为空")
    @DecimalMin(value = "0.0", inclusive = true, message = "分成比例不能小于0")
    @DecimalMax(value = "1.0", inclusive = true, message = "分成比例不能大于1")
    @ApiModelProperty(value = "分成比例", required = true)
    private BigDecimal commissionRate;

    @ApiModelProperty(value = "提现银行名称")
    private String bankName;
    @ApiModelProperty(value = "提现银行卡号")
    private String bankCard;

    @NotEmpty(message = "后台登录密码不能为空")
    @ApiModelProperty(value = "登录密码(社区主体账号)", required = true)
    private String password;

    @ApiModelProperty(value = "所属街道ID（la_region.level=3）")
    private Integer regionId;
    @Size(max = 255, message = "地址不能超过255个字符")
    @ApiModelProperty(value = "详细地址")
    private String address;

    @DecimalMin(value = "-90", message = "纬度范围不正确")
    @DecimalMax(value = "90", message = "纬度范围不正确")
    @ApiModelProperty(value = "纬度")
    private BigDecimal latitude;

    @DecimalMin(value = "-180", message = "经度范围不正确")
    @DecimalMax(value = "180", message = "经度范围不正确")
    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
}
