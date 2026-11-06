package com.mdd.admin.vo.region;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 街道下拉项（后台社区表单、小区筛选使用）。
 */
@ApiModel(value = "街道下拉Vo")
public class RegionStreetVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "街道ID")
    private Integer id;
    @ApiModelProperty(value = "城市ID")
    private Integer cityId;
    @ApiModelProperty(value = "城市名称")
    private String cityName;
    @ApiModelProperty(value = "区县ID")
    private Integer districtId;
    @ApiModelProperty(value = "区县名称")
    private String districtName;
    @ApiModelProperty(value = "街道/镇名称")
    private String name;
    @ApiModelProperty(value = "完整名称")
    private String fullName;
    @ApiModelProperty(value = "排序")
    private Integer sort;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
