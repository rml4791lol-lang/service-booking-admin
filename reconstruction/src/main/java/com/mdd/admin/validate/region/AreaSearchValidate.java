package com.mdd.admin.validate.region;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "小区搜索参数")
public class AreaSearchValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关键词（小区名称/地址）")
    private String keyword;
    @ApiModelProperty(value = "所属社区ID")
    private Integer communityId;
    @ApiModelProperty(value = "所属街道ID")
    private Integer regionId;
    @ApiModelProperty(value = "状态: 1=启用 0=停用")
    private Integer status;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
