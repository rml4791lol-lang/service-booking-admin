package com.mdd.common.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 活动报名批次（la_activity_booking，V025 / #186）：一次报名一行。
 *
 * <p>一个批次 = N 名学员 + C 名陪同，名额按 {@code studentCount + companionCount}
 * 整体占位/释放；{@code la_activity_signup} 仍一人一行，通过 {@code bookingId} 归属批次。</p>
 */
@TableName("la_activity_booking")
public class ActivityBooking implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 状态：待支付（已占名额） */
    public static final int STATUS_PENDING = 0;
    /** 状态：已支付 */
    public static final int STATUS_PAID = 1;
    /** 状态：已取消（超时释放/退款释放） */
    public static final int STATUS_CANCELLED = 3;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer activityId;
    /** 报名家长账号ID */
    private Integer userId;
    /** 支付订单ID（0=免费报名） */
    private Integer orderId;
    private Integer studentCount;
    private Integer companionCount;
    private Integer status;
    private Long createTime;
    private Long updateTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getActivityId() { return activityId; }
    public void setActivityId(Integer activityId) { this.activityId = activityId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public Integer getStudentCount() { return studentCount; }
    public void setStudentCount(Integer studentCount) { this.studentCount = studentCount; }
    public Integer getCompanionCount() { return companionCount; }
    public void setCompanionCount(Integer companionCount) { this.companionCount = companionCount; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
}
