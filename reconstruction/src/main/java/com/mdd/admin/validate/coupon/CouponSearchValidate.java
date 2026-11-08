package com.mdd.admin.validate.coupon;

import com.mdd.admin.validate.commons.PageValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * 券列表筛选。
 *
 * 学员维度同时支持 user_id（精确）与 mobile（模糊），与线上 la_coupon 的索引一致。
 */
@ApiModel(value = "优惠券搜索参数")
public class CouponSearchValidate extends PageValidate {
    private static final long serialVersionUID = 1L;

    @Size(max = 32, message = "手机号最长32位")
    @ApiModelProperty(value = "学员手机号（模糊）")
    private String mobile;

    @Min(value = 0, message = "状态只能是0/1/2")
    @Max(value = 2, message = "状态只能是0/1/2")
    @ApiModelProperty(value = "状态:0=未使用,1=已使用,2=已过期")
    private Integer status;

    @Size(max = 100, message = "券名称最长100位")
    @ApiModelProperty(value = "券名称（模糊）")
    private String name;

    @Size(max = 32, message = "券编号最长32位")
    @ApiModelProperty(value = "券编号（模糊）")
    private String sn;

    @Min(value = 1, message = "用户ID必须大于0")
    @ApiModelProperty(value = "学员用户ID")
    private Integer userId;

    @Min(value = 1, message = "模板ID必须大于0")
    @ApiModelProperty(value = "券模板ID")
    private Integer templateId;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }
}
