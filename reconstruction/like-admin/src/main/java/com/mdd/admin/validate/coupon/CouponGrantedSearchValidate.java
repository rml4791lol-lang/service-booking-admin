package com.mdd.admin.validate.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 已发券查询条件（#213 C2）。
 *
 * <p>券号精确匹配、手机号精确匹配，二者至少填一个（由服务层校验）；
 * 同时填写时按 AND 组合。</p>
 */
@ApiModel(value = "已发券查询条件")
public class CouponGrantedSearchValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @Size(max = 40, message = "券号格式不正确")
    @ApiModelProperty(value = "券号（精确匹配）")
    private String sn;

    @Size(max = 20, message = "手机号格式不正确")
    @ApiModelProperty(value = "家长手机号（精确匹配）")
    private String mobile;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
