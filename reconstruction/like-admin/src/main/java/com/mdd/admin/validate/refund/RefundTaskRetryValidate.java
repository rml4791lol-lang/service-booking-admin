package com.mdd.admin.validate.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(value = "退款任务重试参数")
public class RefundTaskRetryValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "退课申请ID不能为空")
    @ApiModelProperty(value = "退课申请ID", required = true)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
