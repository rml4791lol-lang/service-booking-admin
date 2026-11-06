package com.mdd.front.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 小程序「我的订单」统一读模型（#63）。
 *
 * <p>券/活动/课程订单共用一张 {@code la_recharge_order}，前端不再拼四个接口反查名称/状态：
 * 后端一次性返回订单类型、标题、支付/退款状态与客服口径说明。字段保持只增，既有
 * 缴费记录接口不受影响。</p>
 */
@ApiModel(value = "我的订单列表Vo")
public class OrderListVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单ID")
    private Integer id;

    @ApiModelProperty(value = "订单号（对账/客服查询用）")
    private String orderSn;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "下单时间")
    private String createTime;

    @ApiModelProperty(value = "支付状态：0=待支付，1=已支付，4=已取消")
    private Integer payStatus;

    @ApiModelProperty(value = "退款状态：0=未退款，1=已退款")
    private Integer refundStatus;

    @ApiModelProperty(value = "#60 退款任务状态：0=待执行，1=执行中，2=成功，3=失败")
    private Integer refundTaskStatus;

    @ApiModelProperty(value = "#60 退款任务状态文案（无任务为 null）")
    private String refundTaskStatusText;

    @ApiModelProperty(value = "订单类型：coupon/activity/course/other")
    private String type;

    @ApiModelProperty(value = "订单类型文案")
    private String typeText;

    @ApiModelProperty(value = "商品标题（券商品/活动/课程）")
    private String title;

    @ApiModelProperty(value = "券商品ID")
    private Integer couponGoodsId;

    @ApiModelProperty(value = "活动ID")
    private Integer activityId;

    @ApiModelProperty(value = "课程班级ID")
    private Integer courseClassId;

    @ApiModelProperty(value = "活动报名人数")
    private Integer activityCount;

    @ApiModelProperty(value = "展示状态：pending/paid/refunded/cancelled")
    private String status;

    @ApiModelProperty(value = "展示状态文案：待支付/已支付/已退款/已取消")
    private String statusText;

    @ApiModelProperty(value = "状态说明（客服口径）")
    private String statusHint;

    @ApiModelProperty(value = "#110 券退款入口：refund=可申请原路退，service=联系客服，空=不展示")
    private String refundMode;

    @ApiModelProperty(value = "#110 券退款入口文案")
    private String refundHint;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Integer getRefundTaskStatus() {
        return refundTaskStatus;
    }

    public void setRefundTaskStatus(Integer refundTaskStatus) {
        this.refundTaskStatus = refundTaskStatus;
    }

    public String getRefundTaskStatusText() {
        return refundTaskStatusText;
    }

    public void setRefundTaskStatusText(String refundTaskStatusText) {
        this.refundTaskStatusText = refundTaskStatusText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCouponGoodsId() {
        return couponGoodsId;
    }

    public void setCouponGoodsId(Integer couponGoodsId) {
        this.couponGoodsId = couponGoodsId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getCourseClassId() {
        return courseClassId;
    }

    public void setCourseClassId(Integer courseClassId) {
        this.courseClassId = courseClassId;
    }

    public Integer getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(Integer activityCount) {
        this.activityCount = activityCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getStatusHint() {
        return statusHint;
    }

    public void setStatusHint(String statusHint) {
        this.statusHint = statusHint;
    }

    public String getRefundMode() {
        return refundMode;
    }

    public void setRefundMode(String refundMode) {
        this.refundMode = refundMode;
    }

    public String getRefundHint() {
        return refundHint;
    }

    public void setRefundHint(String refundHint) {
        this.refundHint = refundHint;
    }
}
