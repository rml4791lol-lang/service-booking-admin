package com.mdd.admin.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 余额批量调整失败行（#批量导入）。
 *
 * <p>row 为数据行序号（不含 CSV 表头，从 1 开始），mobile 回传原始手机号，
 * reason 为可直接展示的业务原因。</p>
 */
@ApiModel(value = "余额批量调整失败行")
public class UserWalletBatchErrorVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据行序号（不含表头，从1开始）")
    private Integer row;

    @ApiModelProperty(value = "原始手机号")
    private String mobile;

    @ApiModelProperty(value = "失败原因")
    private String reason;

    public UserWalletBatchErrorVo() {
    }

    public UserWalletBatchErrorVo(Integer row, String mobile, String reason) {
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
