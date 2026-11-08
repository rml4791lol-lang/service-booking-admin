package com.mdd.admin.validate.community;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "社区搜索参数")
public class CommunitySearchValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "社区名称")
    private String name;
    @ApiModelProperty(value = "联系电话")
    private String mobile;
    @ApiModelProperty(value = "所属街道ID")
    private Integer regionId;

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
}
