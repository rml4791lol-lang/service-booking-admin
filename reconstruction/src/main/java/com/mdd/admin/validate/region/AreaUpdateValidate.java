package com.mdd.admin.validate.region;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "小区更新参数")
public class AreaUpdateValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "小区ID", required = true)
    private Integer id;

    @NotNull(message = "所属社区不能为空")
    @ApiModelProperty(value = "所属社区ID", required = true)
    private Integer communityId;

    @NotBlank(message = "小区名称不能为空")
    @Size(max = 64, message = "小区名称不能超过64个字符")
    @ApiModelProperty(value = "小区名称", required = true)
    private String name;

    @Size(max = 255, message = "地址不能超过255个字符")
    @ApiModelProperty(value = "详细地址")
    private String address;

    @DecimalMin(value = "-90", message = "纬度范围不正确")
    @DecimalMax(value = "90", message = "纬度范围不正确")
    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    @DecimalMin(value = "-180", message = "经度范围不正确")
    @DecimalMax(value = "180", message = "经度范围不正确")
    @ApiModelProperty(value = "经度")
    private BigDecimal lng;

    @Size(max = 255, message = "封面地址不能超过255个字符")
    @ApiModelProperty(value = "封面图")
    private String cover;

    @Size(max = 255, message = "标签不能超过255个字符")
    @ApiModelProperty(value = "标签（逗号分隔）")
    private String tags;

    @Size(max = 32, message = "距离文案不能超过32个字符")
    @ApiModelProperty(value = "距离文案")
    private String distance;

    @Min(value = 0, message = "排序不能小于0")
    @Max(value = 65535, message = "排序不能大于65535")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @Min(value = 0, message = "状态只能是0或1")
    @Max(value = 1, message = "状态只能是0或1")
    @ApiModelProperty(value = "状态: 1=启用 0=停用")
    private Integer status;

    @Size(max = 255, message = "备注不能超过255个字符")
    @ApiModelProperty(value = "备注")
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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
