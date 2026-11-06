package com.mdd.admin.vo.community;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "社区详情Vo")
public class CommunityDetailVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Integer id;
    @ApiModelProperty(value = "社区名称")
    private String name;
    @ApiModelProperty(value = "联系电话")
    private String mobile;
    @ApiModelProperty(value = "所属街道ID")
    private Integer regionId;
    @ApiModelProperty(value = "城市名称")
    private String cityName;
    @ApiModelProperty(value = "区县名称")
    private String districtName;
    @ApiModelProperty(value = "街道名称")
    private String streetName;
    @ApiModelProperty(value = "详细地址")
    private String address;
    @ApiModelProperty(value = "纬度")
    private BigDecimal latitude;
    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;
    @ApiModelProperty(value = "分成比例")
    private BigDecimal commissionRate;
    @ApiModelProperty(value = "提现银行名称")
    private String bankName;
    @ApiModelProperty(value = "提现银行卡号")
    private String bankCard;
    @ApiModelProperty(value = "当前可提现余额")
    private BigDecimal balance;
    @ApiModelProperty(value = "累计总收益")
    private BigDecimal totalEarnings;
    @ApiModelProperty(value = "小区数量")
    private Integer areaCount;
    @ApiModelProperty(value = "创建时间")
    private String createTime;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(BigDecimal totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public Integer getAreaCount() {
        return areaCount;
    }

    public void setAreaCount(Integer areaCount) {
        this.areaCount = areaCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
