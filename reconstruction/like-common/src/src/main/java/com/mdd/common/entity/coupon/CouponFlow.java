package com.mdd.common.entity.coupon;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 次数券流水（la_coupon_flow）：发放/扣减/补发/作废/退回，只追加、不可删除。
 */
@TableName("la_coupon_flow")
public class CouponFlow implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 流水ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /** 券实例ID */
    private Integer couponId;
    /** 用户ID */
    private Integer userId;
    /** 学员ID */
    private Integer studentId;
    /** 类型：1=发放，2=核销扣减，3=补发，4=作废，5=退回 */
    private Integer type;
    /** 变动次数（正数增加，负数扣减） */
    private Integer times;
    /** 变动后余额 */
    private Integer balance;
    /** 关联订单ID */
    private Integer orderId;
    /** 关联订单号 */
    private String orderSn;
    /** 关联业务（课程/场次/原因） */
    private String ref;
    /** 操作人类型 */
    private String operatorType;
    /** 操作人ID */
    private Integer operatorId;
    /** 备注 */
    private String remark;
    /** 创建时间 */
    private Long createTime;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCouponId() {
        return this.couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTimes() {
        return this.times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getBalance() {
        return this.balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderSn() {
        return this.orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getRef() {
        return this.ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getOperatorType() {
        return this.operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Integer getOperatorId() {
        return this.operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
