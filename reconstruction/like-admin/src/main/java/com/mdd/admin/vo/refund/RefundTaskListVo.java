package com.mdd.admin.vo.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "退款任务列表响应实体")
public class RefundTaskListVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务ID")
    private Integer id;

    @ApiModelProperty(value = "任务编号")
    private String taskNo;

    @ApiModelProperty(value = "退课申请ID")
    private Integer refundId;

    @ApiModelProperty(value = "原订单ID")
    private Integer orderId;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "支付方式")
    private Integer payWay;

    @ApiModelProperty(value = "原商户订单号")
    private String outTradeNo;

    @ApiModelProperty(value = "微信退款单号")
    private String outRefundNo;

    @ApiModelProperty(value = "微信支付交易号")
    private String transactionId;

    @ApiModelProperty(value = "订单总额（元）")
    private String totalFee;

    @ApiModelProperty(value = "退款金额（元）")
    private String refundFee;

    @ApiModelProperty(value = "任务状态")
    private Integer status;

    @ApiModelProperty(value = "任务状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "失败重试次数")
    private Integer retryCount;

    @ApiModelProperty(value = "自动重试上限")
    private Integer maxRetry;

    @ApiModelProperty(value = "下次自动重试时间")
    private String nextRetryTime;

    @ApiModelProperty(value = "最后失败原因")
    private String lastError;

    @ApiModelProperty(value = "执行结果说明")
    private String resultMsg;

    @ApiModelProperty(value = "本次执行开始时间")
    private String executeTime;

    @ApiModelProperty(value = "任务结束时间")
    private String finishTime;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

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

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
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

    public String getNextRetryTime() {
        return nextRetryTime;
    }

    public void setNextRetryTime(String nextRetryTime) {
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

    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
