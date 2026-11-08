package com.mdd.admin.vo.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 活动报名超员告警列表响应（#61 / #7 复审）。
 *
 * <p>字段与原 {@code Map<String,Object>} 的 JSON key 完全一致，后台页面契约不变。</p>
 */
@ApiModel(value = "活动报名超员告警列表响应实体")
public class ActivityEnrollAlertListVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "告警ID")
    private Integer id = 0;
    @ApiModelProperty(value = "异常类型")
    private Integer type = 0;
    @ApiModelProperty(value = "异常类型文案")
    private String typeText = "";
    @ApiModelProperty(value = "状态：0=待处理,1=已处理,2=已退款")
    private Integer status = 0;
    @ApiModelProperty(value = "状态文案")
    private String statusText = "";
    @ApiModelProperty(value = "活动ID")
    private Integer activityId = 0;
    @ApiModelProperty(value = "活动标题")
    private String activityTitle = "";
    @ApiModelProperty(value = "活动日期")
    private String activityDate = "";
    @ApiModelProperty(value = "活动时段")
    private String activityTime = "";
    @ApiModelProperty(value = "活动地点")
    private String activityPlace = "";
    @ApiModelProperty(value = "活动容量")
    private Integer capacity = 0;
    @ApiModelProperty(value = "已占名额")
    private Integer joined = 0;
    @ApiModelProperty(value = "订单ID")
    private Integer orderId = 0;
    @ApiModelProperty(value = "订单号")
    private String orderSn = "";
    @ApiModelProperty(value = "订单支付状态")
    private Integer orderPayStatus = 0;
    @ApiModelProperty(value = "订单支付状态文案")
    private String orderPayStatusText = "";
    @ApiModelProperty(value = "订单退款状态")
    private Integer orderRefundStatus = 0;
    @ApiModelProperty(value = "告警金额")
    private BigDecimal amount = BigDecimal.ZERO;
    @ApiModelProperty(value = "支付方式")
    private Integer payWay = 0;
    @ApiModelProperty(value = "支付方式文案")
    private String payWayText = "";
    @ApiModelProperty(value = "用户ID")
    private Integer userId = 0;
    @ApiModelProperty(value = "用户姓名")
    private String userName = "";
    @ApiModelProperty(value = "用户手机号")
    private String userMobile = "";
    @ApiModelProperty(value = "学员ID列表，逗号分隔")
    private String studentIds = "";
    @ApiModelProperty(value = "学员姓名列表")
    private String studentNames = "";
    @ApiModelProperty(value = "学员人数")
    private Integer studentCount = 0;
    @ApiModelProperty(value = "异常原因")
    private String reason = "";
    @ApiModelProperty(value = "处理备注")
    private String remark = "";
    @ApiModelProperty(value = "处理人ID")
    private Integer handlerId = 0;
    @ApiModelProperty(value = "处理人姓名")
    private String handlerName = "";
    @ApiModelProperty(value = "处理时间戳")
    private Long handleTime = 0L;
    @ApiModelProperty(value = "处理时间")
    private String handledAt = "";
    @ApiModelProperty(value = "创建时间")
    private String createdAt = "";
    @ApiModelProperty(value = "更新时间")
    private String updatedAt = "";

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public String getTypeText() { return typeText; }
    public void setTypeText(String typeText) { this.typeText = typeText; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
    public Integer getActivityId() { return activityId; }
    public void setActivityId(Integer activityId) { this.activityId = activityId; }
    public String getActivityTitle() { return activityTitle; }
    public void setActivityTitle(String activityTitle) { this.activityTitle = activityTitle; }
    public String getActivityDate() { return activityDate; }
    public void setActivityDate(String activityDate) { this.activityDate = activityDate; }
    public String getActivityTime() { return activityTime; }
    public void setActivityTime(String activityTime) { this.activityTime = activityTime; }
    public String getActivityPlace() { return activityPlace; }
    public void setActivityPlace(String activityPlace) { this.activityPlace = activityPlace; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public Integer getJoined() { return joined; }
    public void setJoined(Integer joined) { this.joined = joined; }
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public String getOrderSn() { return orderSn; }
    public void setOrderSn(String orderSn) { this.orderSn = orderSn; }
    public Integer getOrderPayStatus() { return orderPayStatus; }
    public void setOrderPayStatus(Integer orderPayStatus) { this.orderPayStatus = orderPayStatus; }
    public String getOrderPayStatusText() { return orderPayStatusText; }
    public void setOrderPayStatusText(String orderPayStatusText) { this.orderPayStatusText = orderPayStatusText; }
    public Integer getOrderRefundStatus() { return orderRefundStatus; }
    public void setOrderRefundStatus(Integer orderRefundStatus) { this.orderRefundStatus = orderRefundStatus; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Integer getPayWay() { return payWay; }
    public void setPayWay(Integer payWay) { this.payWay = payWay; }
    public String getPayWayText() { return payWayText; }
    public void setPayWayText(String payWayText) { this.payWayText = payWayText; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserMobile() { return userMobile; }
    public void setUserMobile(String userMobile) { this.userMobile = userMobile; }
    public String getStudentIds() { return studentIds; }
    public void setStudentIds(String studentIds) { this.studentIds = studentIds; }
    public String getStudentNames() { return studentNames; }
    public void setStudentNames(String studentNames) { this.studentNames = studentNames; }
    public Integer getStudentCount() { return studentCount; }
    public void setStudentCount(Integer studentCount) { this.studentCount = studentCount; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getHandlerId() { return handlerId; }
    public void setHandlerId(Integer handlerId) { this.handlerId = handlerId; }
    public String getHandlerName() { return handlerName; }
    public void setHandlerName(String handlerName) { this.handlerName = handlerName; }
    public Long getHandleTime() { return handleTime; }
    public void setHandleTime(Long handleTime) { this.handleTime = handleTime; }
    public String getHandledAt() { return handledAt; }
    public void setHandledAt(String handledAt) { this.handledAt = handledAt; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
