package com.mdd.admin.validate.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 券批量发放单行参数（CSV 由前端解析后提交 JSON 行数组）。
 *
 * <p>字段与单条发券 {@link CouponGrantValidate} 对齐：mobile 必填、
 * goodsId 必填、studentId 选填（空/0=账号级券）、quantity 1–99、remark 选填。
 * 逐行校验失败只影响该行，由服务层收集行号与原因。</p>
 */
@ApiModel(value = "券批量发放单行参数")
public class CouponGrantBatchRowValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "家长手机号")
    private String mobile;

    @ApiModelProperty(value = "券商品ID")
    private Integer goodsId;

    @ApiModelProperty(value = "学员ID（可选，0/空=账号级券）")
    private Integer studentId;

    @ApiModelProperty(value = "发放张数（1-99）")
    private Integer quantity;

    @ApiModelProperty(value = "发放备注（可选）")
    private String remark;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
