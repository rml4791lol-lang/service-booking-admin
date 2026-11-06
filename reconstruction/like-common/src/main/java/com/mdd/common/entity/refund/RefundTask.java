package com.mdd.common.entity.refund;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 退款任务 outbox（#60）。
 *
 * 审核通过时在本地事务内落一条任务（refund_id 唯一），事务提交后异步调用微信退款；
 * 失败/超时可重试，out_refund_no 固定，微信侧按该单号幂等。
 */
@TableName(value = "la_refund_task")
@ApiModel(value = "退款任务 outbox")
public class RefundTask implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 状态：待执行 */
    public static final int STATUS_PENDING = 0;
    /** 状态：执行中 */
    public static final int STATUS_RUNNING = 1;
    /** 状态：成功（仅微信退款受理成功后） */
    public static final int STATUS_SUCCESS = 2;
    /** 状态：失败 */
    public static final int STATUS_FAILED = 3;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "任务ID")
    private Integer id;

    @ApiModelProperty(value = "本地任务编号")
    private String taskNo;

    @ApiModelProperty(value = "退课申请ID")
    private Integer refundId;

    @ApiModelProperty(value = "原支付订单ID")
    private Integer orderId;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "班级ID")
    private Integer courseClassId;

    @ApiModelProperty(value = "支付方式")
    private Integer payWay;

    @ApiModelProperty(value = "微信支付交易号")
    private String transactionId;

    @ApiModelProperty(value = "原商户订单号")
    private String outTradeNo;

    @ApiModelProperty(value = "微信退款单号（幂等键）")
    private String outRefundNo;

    @ApiModelProperty(value = "订单总额（分）")
    private Integer totalFee;

    @ApiModelProperty(value = "退款金额（分）")
    private Integer refundFee;

    @ApiModelProperty(value = "状态：0=待执行，1=执行中，2=成功，3=失败")
    private Integer status;

    @ApiModelProperty(value = "失败重试次数")
    private Integer retryCount;

    @ApiModelProperty(value = "自动重试上限")
    private Integer maxRetry;

    @ApiModelProperty(value = "下次自动重试时间戳（0=不自动重试）")
    private Long nextRetryTime;

    @ApiModelProperty(value = "最后一次失败原因")
    private String lastError;

    @ApiModelProperty(value = "执行结果说明")
    private String resultMsg;

    @ApiModelProperty(value = "本次执行开始时间戳")
    private Long executeTime;

    @ApiModelProperty(value = "任务结束时间戳")
    private Long finishTime;

    @ApiModelProperty(value = "创建时间戳")
    private Long createTime;

    @ApiModelProperty(value = "更新时间戳")
    private Long updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public Integer getRefundId() {
        return refundId;
    }

    public void setRefundId(Integer refundId) {
        this.refundId = refundId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCourseClassId() {
        return courseClassId;
    }

    public void setCourseClassId(Integer courseClassId) {
        this.courseClassId = courseClassId;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Integer refundFee) {
        this.refundFee = refundFee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(Integer maxRetry) {
        this.maxRetry = maxRetry;
    }

    public Long getNextRetryTime() {
        return nextRetryTime;
    }

    public void setNextRetryTime(Long nextRetryTime) {
        this.nextRetryTime = nextRetryTime;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
