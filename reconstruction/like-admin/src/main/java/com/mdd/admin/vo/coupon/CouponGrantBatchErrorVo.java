package com.mdd.admin.vo.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 券批量发放失败行（#批量导入）。
 *
 * <p>row 为数据行序号（不含 CSV 表头，从 1 开始），mobile 回传原始手机号，
 * reason 为可直接展示的业务原因，前端按「第 N 行（手机号）：原因」拼接。</p>
 */
@ApiModel(value = "券批量发放失败行")
public class CouponGrantBatchErrorVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据行序号（不含表头，从1开始）")
    private Integer row;

    @ApiModelProperty(value = "原始手机号")
    private String mobile;

    @ApiModelProperty(value = "失败原因")
    private String reason;

    public CouponGrantBatchErrorVo() {
    }

    public CouponGrantBatchErrorVo(Integer row, String mobile, String reason) {
        this.row = row;
        this.mobile = mobile;
        this.reason = reason;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
