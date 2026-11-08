package com.mdd.admin.vo.region;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 小区列表/详情 Vo。字段口径对齐小程序 pages/areas mock：
 * name/address/lat/lng/cover/tags/distance/sort/status。
 */
@ApiModel(value = "小区Vo")
public class AreaListedVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "小区ID")
    private Integer id;
    @ApiModelProperty(value = "所属社区ID")
    private Integer communityId;
    @ApiModelProperty(value = "所属社区名称")
    private String communityName;
    @ApiModelProperty(value = "所属街道ID")
    private Integer regionId;
    @ApiModelProperty(value = "所属街道名称")
    private String streetName;
    @ApiModelProperty(value = "所属区县ID")
    private Integer districtId;
    @ApiModelProperty(value = "所属区县名称")
    private String districtName;
    @ApiModelProperty(value = "小区名称")
    private String name;
    @ApiModelProperty(value = "详细地址")
    private String address;
    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;
    @ApiModelProperty(value = "经度")
    private BigDecimal lng;
    @ApiModelProperty(value = "封面图")
    private String cover;
    @ApiModelProperty(value = "标签数组（对齐前端 mock）")
    private List<String> tags = new ArrayList<>();
    @ApiModelProperty(value = "标签文本（逗号分隔，后台编辑用）")
    private String tagsText;
    @ApiModelProperty(value = "展示距离文案（对齐前端 mock）")
    private String distance;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "状态: 1=启用 0=停用")
    private Integer status;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "绑定班级数")
    private Integer classCount;
    @ApiModelProperty(value = "创建时间")
    private String createTime;

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

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTagsText() {
        return tagsText;
    }

    public void setTagsText(String tagsText) {
        this.tagsText = tagsText;
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

    public Integer getClassCount() {
        return classCount;
    }

    public void setClassCount(Integer classCount) {
        this.classCount = classCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
