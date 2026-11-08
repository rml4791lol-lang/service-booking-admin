package com.mdd.admin.validate.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 后台发券撤回参数（#213 C2）。
 *
 * <p>按券号定位单张未核销的券实例并置为已作废（status=4），同时写一条作废流水。</p>
 */
@ApiModel(value = "后台发券撤回参数")
public class CouponRevokeValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "请输入券号")
    @Size(max = 40, message = "券号格式不正确")
    @ApiModelProperty(value = "券号（la_coupon_instance.sn）")
    private String sn;

    @Size(max = 200, message = "撤回原因最长200位")
    @ApiModelProperty(value = "撤回原因（可选）")
    private String remark;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
