package com.mdd.admin.vo.community;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 社区下拉项（小区表单、课程筛选使用）。
 */
@ApiModel(value = "社区下拉Vo")
public class CommunityOptionVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "社区ID")
    private Integer id;
    @ApiModelProperty(value = "社区名称")
    private String name;
    @ApiModelProperty(value = "联系电话")
    private String mobile;
    @ApiModelProperty(value = "所属街道ID")
    private Integer regionId;
    @ApiModelProperty(value = "所属街道名称")
    private String streetName;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "纬度")
    private BigDecimal latitude;
    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

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
}
