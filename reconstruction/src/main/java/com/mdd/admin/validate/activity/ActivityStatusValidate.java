package com.mdd.admin.validate.activity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 后台活动上下架（#186）：status 只接受 0=下架、1=上架。
 */
public class ActivityStatusValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "活动ID不能为空")
    private Integer id;

    @NotNull(message = "上下架状态不能为空")
    @Min(value = 0, message = "上下架状态只能是 0 或 1")
    @Max(value = 1, message = "上下架状态只能是 0 或 1")
    private Integer status;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
