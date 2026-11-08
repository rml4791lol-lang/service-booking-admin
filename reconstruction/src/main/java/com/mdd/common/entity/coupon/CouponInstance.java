package com.mdd.common.entity.coupon;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 次数券实例（la_coupon_instance）：一张券绑定一个学员，记录剩余次数与有效期。
 */
@TableName("la_coupon_instance")
public class CouponInstance implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 券实例ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /** 券编号 */
    private String sn;
    /** 持券用户ID */
    private Integer userId;
    /** 持券学员ID */
    private Integer studentId;
    /** 券商品ID */
    private Integer goodsId;
    /** 券名称快照 */
    private String title;
    /** 总次数 */
    private Integer total;
    /** 剩余次数 */
    private Integer remaining;
    /** 适用范围文案快照 */
    private String scope;
    /** 适用范围类型快照 */
    private String scopeType;
    /** 限定课程ID，逗号分隔；空=不限 */
    private String courseIds;
    /** 限定小区ID，逗号分隔；空=不限 */
    private String areaIds;
    /** 状态：1=有效，2=已用完，3=已过期，4=已作废 */
    private Integer status;
    /** 生效时间 */
    private Long startTime;
    /** 到期时间（NULL=长期有效） */
    private Long expireTime;
    /** 发放来源订单ID（NULL=后台发放/补发） */
    private Integer orderId;
    /** 同一订单内第几张券（0=旧数据/单张；券包按张发放时 1..N） */
    private Integer orderSeq;
    /** 来源：purchase/grant/compensate */
    private String source;
    /** 备注 */
    private String remark;
    /** 创建时间 */
    private Long createTime;
    /** 更新时间 */
    private Long updateTime;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
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

    public Integer getGoodsId() {
        return this.goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotal() {
        return this.total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getRemaining() {
        return this.remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScopeType() {
        return this.scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public String getCourseIds() {
        return this.courseIds;
    }

    public void setCourseIds(String courseIds) {
        this.courseIds = courseIds;
    }

    public String getAreaIds() {
        return this.areaIds;
    }

    public void setAreaIds(String areaIds) {
        this.areaIds = areaIds;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderSeq() {
        return this.orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
