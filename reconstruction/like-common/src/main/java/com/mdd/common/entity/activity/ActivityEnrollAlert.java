package com.mdd.common.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 活动报名超员待处理告警（#61）。
 *
 * <p>支付回调遇到名额已被超时释放且活动已满时，默认策略为「订单生效 + 允许超员」，
 * 同时落一条本表记录供后台跟进；运营可标记已处理或发起退款。唯一键 {@code order_id}
 * 保证重复回调不会重复生成告警。</p>
 */
@TableName("la_activity_enroll_alert")
public class ActivityEnrollAlert implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 异常类型：名额释放后支付已满员（超员生效） */
    public static final int TYPE_OVERBOOKED_AFTER_RELEASE = 1;
    /** 待处理 */
    public static final int STATUS_PENDING = 0;
    /** 已处理（保留报名） */
    public static final int STATUS_HANDLED = 1;
    /** 已退款（报名取消、名额释放） */
    public static final int STATUS_REFUNDED = 2;
    /** 处理中（仅事务内短暂存在，用于并发抢占） */
    public static final int STATUS_PROCESSING = 3;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer activityId;
    private Integer orderId;
    private Integer userId;
    private String orderSn;
    private String studentIds;
    private Integer studentCount;
    private BigDecimal amount;
    private Integer payWay;
    private Integer type;
    private Integer status;
    private String reason;
    private String remark;
    private Integer handlerId;
    private String handlerName;
    private Long handleTime;
    private Long createTime;
    private Long updateTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getActivityId() { return activityId; }
    public void setActivityId(Integer activityId) { this.activityId = activityId; }
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getOrderSn() { return orderSn; }
    public void setOrderSn(String orderSn) { this.orderSn = orderSn; }
    public String getStudentIds() { return studentIds; }
    public void setStudentIds(String studentIds) { this.studentIds = studentIds; }
    public Integer getStudentCount() { return studentCount; }
    public void setStudentCount(Integer studentCount) { this.studentCount = studentCount; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Integer getPayWay() { return payWay; }
    public void setPayWay(Integer payWay) { this.payWay = payWay; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
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
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
}
