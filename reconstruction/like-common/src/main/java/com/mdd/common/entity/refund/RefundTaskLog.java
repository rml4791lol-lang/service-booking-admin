package com.mdd.common.entity.refund;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 退款任务执行流水（#60）：每次执行/重试追加一行，不更新历史行，便于对账回溯。
 */
@TableName(value = "la_refund_task_log")
@ApiModel(value = "退款任务执行流水")
public class RefundTaskLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 流水状态：处理中 */
    public static final int STATUS_PROCESSING = 0;
    /** 流水状态：成功 */
    public static final int STATUS_SUCCESS = 1;
    /** 流水状态：失败 */
    public static final int STATUS_FAILED = 2;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "流水ID")
    private Integer id;

    @ApiModelProperty(value = "退款任务ID")
    private Integer taskId;

    @ApiModelProperty(value = "第几次执行")
    private Integer attemptNo;

    @ApiModelProperty(value = "动作：create/execute/retry/requeue")
    private String action;

    @ApiModelProperty(value = "流水状态：0=处理中，1=成功，2=失败")
    private Integer status;

    @ApiModelProperty(value = "微信退款单号")
    private String outRefundNo;

    @ApiModelProperty(value = "结果/错误说明")
    private String message;

    @ApiModelProperty(value = "执行耗时（毫秒）")
    private Integer costMs;

    @ApiModelProperty(value = "创建时间戳")
    private Long createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getAttemptNo() {
        return attemptNo;
    }

    public void setAttemptNo(Integer attemptNo) {
        this.attemptNo = attemptNo;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCostMs() {
        return costMs;
    }

    public void setCostMs(Integer costMs) {
        this.costMs = costMs;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
