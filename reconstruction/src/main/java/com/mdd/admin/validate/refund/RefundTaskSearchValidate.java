package com.mdd.admin.validate.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "退款任务查询参数")
public class RefundTaskSearchValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "退课申请ID")
    private Integer refundId;

    @ApiModelProperty(value = "任务状态：0=待执行,1=执行中,2=成功,3=失败")
    private Integer status;

    @ApiModelProperty(value = "任务编号")
    private String taskNo;

    public Integer getRefundId() {
        return refundId;
    }

    public void setRefundId(Integer refundId) {
        this.refundId = refundId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }
}
