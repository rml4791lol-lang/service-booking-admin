package com.mdd.admin.vo.region;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 小区下拉项（课程班级绑定小区使用）。
 */
@ApiModel(value = "小区下拉Vo")
public class AreaOptionVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "小区ID")
    private Integer id;
    @ApiModelProperty(value = "小区名称")
    private String name;
    @ApiModelProperty(value = "所属社区ID")
    private Integer communityId;
    @ApiModelProperty(value = "所属社区名称")
    private String communityName;
    @ApiModelProperty(value = "所属街道名称")
    private String streetName;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;
    @ApiModelProperty(value = "经度")
    private BigDecimal lng;
    @ApiModelProperty(value = "状态")
    private Integer status;

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

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
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

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
