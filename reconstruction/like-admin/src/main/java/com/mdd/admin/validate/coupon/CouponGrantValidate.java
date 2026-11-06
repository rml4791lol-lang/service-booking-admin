package com.mdd.admin.validate.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 后台发次数券参数（#206）。
 *
 * <p>按家长手机号定位账号，学员选填：留空或 0 发账号级券（名下任一学员可用），
 * 指定学员时券只对该学员生效。张数 1–99，每张券固定 total=remaining=1，
 * 与券商品 notes/次数无关（商品次数只用于小程序购买流程）。</p>
 */
@ApiModel(value = "后台发次数券参数")
public class CouponGrantValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "请选择券商品")
    @Min(value = 1, message = "请选择券商品")
    @ApiModelProperty(value = "券商品ID")
    private Integer goodsId;

    @NotBlank(message = "请输入家长手机号")
    @Pattern(regexp = "^1\\d{10}$", message = "请输入正确的 11 位手机号")
    @ApiModelProperty(value = "家长手机号（用于定位 la_user）")
    private String mobile;

    @Min(value = 0, message = "学员ID不能小于0")
    @ApiModelProperty(value = "学员ID（可选，0/空=账号级券）")
    private Integer studentId;

    @NotNull(message = "请输入发放张数")
    @Min(value = 1, message = "发放张数需在 1-99 之间")
    @Max(value = 99, message = "发放张数需在 1-99 之间")
    @ApiModelProperty(value = "发放张数（1-99）")
    private Integer quantity;

    @Size(max = 200, message = "备注最长200位")
    @ApiModelProperty(value = "发放备注（可选）")
    private String remark;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
