package com.mdd.common.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 活动报名明细（la_activity_signup）：一人一行，支付成功前也占名额。
 */
@TableName("la_activity_signup")
public class ActivitySignup implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 状态：待支付 */
    public static final int STATUS_PENDING = 0;
    /** 状态：已支付 */
    public static final int STATUS_PAID = 1;
    /** 状态：已取消（超时释放） */
    public static final int STATUS_CANCELLED = 3;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer activityId;
    private Integer userId;
    private Integer studentId;
    private Integer orderId;
    /** 报名批次ID（la_activity_booking.id，0=历史数据） */
    private Integer bookingId;
    private Integer status;
    private Long createTime;
    private Long updateTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getActivityId() { return activityId; }
    public void setActivityId(Integer activityId) { this.activityId = activityId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
}
