/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;

@ApiModel(value="\u5145\u503c\u8ba2\u5355\u5b9e\u4f53\u7c7b")
public class RechargeOrder {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u7528\u6237ID")
    private Integer userId;
    @ApiModelProperty(value="\u5b66\u5458ID")
    private Integer studentId;
    @ApiModelProperty(value="\u8ba2\u5355\u7f16\u53f7")
    private String sn;
    @ApiModelProperty(value="\u652f\u4ed8\u7f16\u53f7")
    private String paySn;
    @ApiModelProperty(value="\u652f\u4ed8\u65b9\u5f0f: [2=\u5fae\u4fe1\u652f\u4ed8, 3=\u652f\u4ed8\u5b9d\u652f\u4ed8]")
    private Integer payWay;
    @ApiModelProperty(value="\u652f\u4ed8\u72b6\u6001: [0=\u5f85\u652f\u4ed8, 1=\u5df2\u652f\u4ed8]")
    private Integer payStatus;
    @ApiModelProperty(value="\u652f\u4ed8\u65f6\u95f4")
    private Long payTime;
    @ApiModelProperty(value="\u5145\u503c\u91d1\u989d")
    private BigDecimal orderAmount;
    @ApiModelProperty(value="\u4f7f\u7528\u7684\u62b5\u6263\u5238ID(0=\u672a\u4f7f\u7528)")
    private Integer couponId;
    @ApiModelProperty(value="\u62b5\u6263\u91d1\u989d")
    private BigDecimal couponAmount;
    @ApiModelProperty(value="\u4e0b\u5355\u7ec8\u7aef")
    private Integer orderTerminal;
    @ApiModelProperty(value="\u4ea4\u6613\u6d41\u6c34")
    private String transactionId;
    @ApiModelProperty(value="\u9000\u6b3e\u72b6\u6001: [0=\u672a\u9000\u6b3e , 1=\u5df2\u9000\u6b3e]")
    private Integer refundStatus;
    @ApiModelProperty(value="\u9000\u6b3e\u4ea4\u6613\u6d41\u6c34\u53f7")
    private String refundTransactionId;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Long updateTime;
    @ApiModelProperty(value="\u5220\u9664\u65f6\u95f4")
    private Long deleteTime;
    @ApiModelProperty(value="\u8bfe\u7a0b\u73ed\u7ea7ID")
    private Integer courseClassId;
    @ApiModelProperty(value="券商品ID（0=非券订单，T29.4）")
    private Integer couponGoodsId;
    @ApiModelProperty(value="\u8d2d\u4e70\u8bfe\u65f6\u6570")
    private Integer lessonCount;
    @ApiModelProperty(value="活动ID（0=非活动订单，T29.7）")
    private Integer activityId;
    @ApiModelProperty(value="活动报名学员ID，逗号分隔（T29.7）")
    private String activityStudentIds;
    @ApiModelProperty(value="活动报名学员人数（T29.7）")
    private Integer activityCount;
    @ApiModelProperty(value="\u652f\u4ed8\u51ed\u8bc1")
    private String voucher;
    @ApiModelProperty(value="\u5bf9\u8d26\u72b6\u6001")
    private Integer isAudit;

    public Integer getCourseClassId() {
        return this.courseClassId;
    }

    public void setCourseClassId(Integer courseClassId) {
        this.courseClassId = courseClassId;
    }

    public Integer getCouponGoodsId() {
        return this.couponGoodsId;
    }

    public void setCouponGoodsId(Integer couponGoodsId) {
        this.couponGoodsId = couponGoodsId;
    }

    public String getVoucher() {
        return this.voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public Integer getIsAudit() {
        return this.isAudit;
    }

    public void setIsAudit(Integer isAudit) {
        this.isAudit = isAudit;
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getStudentId() {
        return this.studentId;
    }

    public String getSn() {
        return this.sn;
    }

    public String getPaySn() {
        return this.paySn;
    }

    public Integer getPayWay() {
        return this.payWay;
    }

    public Integer getPayStatus() {
        return this.payStatus;
    }

    public Long getPayTime() {
        return this.payTime;
    }

    public BigDecimal getOrderAmount() {
        return this.orderAmount;
    }

    public Integer getCouponId() {
        return this.couponId;
    }

    public BigDecimal getCouponAmount() {
        return this.couponAmount;
    }

    public Integer getOrderTerminal() {
        return this.orderTerminal;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public Integer getRefundStatus() {
        return this.refundStatus;
    }

    public String getRefundTransactionId() {
        return this.refundTransactionId;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public Long getDeleteTime() {
        return this.deleteTime;
    }

    public Integer getLessonCount() {
        return this.lessonCount;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setPaySn(String paySn) {
        this.paySn = paySn;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public void setOrderTerminal(Integer orderTerminal) {
        this.orderTerminal = orderTerminal;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public void setRefundTransactionId(String refundTransactionId) {
        this.refundTransactionId = refundTransactionId;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleteTime(Long deleteTime) {
        this.deleteTime = deleteTime;
    }

    public void setLessonCount(Integer lessonCount) {
        this.lessonCount = lessonCount;
    }

    public Integer getActivityId() {
        return this.activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityStudentIds() {
        return this.activityStudentIds;
    }

    public void setActivityStudentIds(String activityStudentIds) {
        this.activityStudentIds = activityStudentIds;
    }

    public Integer getActivityCount() {
        return this.activityCount;
    }

    public void setActivityCount(Integer activityCount) {
        this.activityCount = activityCount;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RechargeOrder)) {
            return false;
        }
        RechargeOrder other = (RechargeOrder)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$studentId = this.getStudentId();
        Integer other$studentId = other.getStudentId();
        if (this$studentId == null ? other$studentId != null : !((Object)this$studentId).equals(other$studentId)) {
            return false;
        }
        Integer this$payWay = this.getPayWay();
        Integer other$payWay = other.getPayWay();
        if (this$payWay == null ? other$payWay != null : !((Object)this$payWay).equals(other$payWay)) {
            return false;
        }
        Integer this$payStatus = this.getPayStatus();
        Integer other$payStatus = other.getPayStatus();
        if (this$payStatus == null ? other$payStatus != null : !((Object)this$payStatus).equals(other$payStatus)) {
            return false;
        }
        Long this$payTime = this.getPayTime();
        Long other$payTime = other.getPayTime();
        if (this$payTime == null ? other$payTime != null : !((Object)this$payTime).equals(other$payTime)) {
            return false;
        }
        Integer this$couponId = this.getCouponId();
        Integer other$couponId = other.getCouponId();
        if (this$couponId == null ? other$couponId != null : !((Object)this$couponId).equals(other$couponId)) {
            return false;
        }
        Integer this$orderTerminal = this.getOrderTerminal();
        Integer other$orderTerminal = other.getOrderTerminal();
        if (this$orderTerminal == null ? other$orderTerminal != null : !((Object)this$orderTerminal).equals(other$orderTerminal)) {
            return false;
        }
        Integer this$refundStatus = this.getRefundStatus();
        Integer other$refundStatus = other.getRefundStatus();
        if (this$refundStatus == null ? other$refundStatus != null : !((Object)this$refundStatus).equals(other$refundStatus)) {
            return false;
        }
        Long this$createTime = this.getCreateTime();
        Long other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Long this$updateTime = this.getUpdateTime();
        Long other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        Long this$deleteTime = this.getDeleteTime();
        Long other$deleteTime = other.getDeleteTime();
        if (this$deleteTime == null ? other$deleteTime != null : !((Object)this$deleteTime).equals(other$deleteTime)) {
            return false;
        }
        Integer this$courseClassId = this.getCourseClassId();
        Integer other$courseClassId = other.getCourseClassId();
        if (this$courseClassId == null ? other$courseClassId != null : !((Object)this$courseClassId).equals(other$courseClassId)) {
            return false;
        }
        Integer this$couponGoodsId = this.getCouponGoodsId();
        Integer other$couponGoodsId = other.getCouponGoodsId();
        if (this$couponGoodsId == null ? other$couponGoodsId != null : !((Object)this$couponGoodsId).equals(other$couponGoodsId)) {
            return false;
        }
        Integer this$lessonCount = this.getLessonCount();
        Integer other$lessonCount = other.getLessonCount();
        if (this$lessonCount == null ? other$lessonCount != null : !((Object)this$lessonCount).equals(other$lessonCount)) {
            return false;
        }
        Integer this$isAudit = this.getIsAudit();
        Integer other$isAudit = other.getIsAudit();
        if (this$isAudit == null ? other$isAudit != null : !((Object)this$isAudit).equals(other$isAudit)) {
            return false;
        }
        String this$sn = this.getSn();
        String other$sn = other.getSn();
        if (this$sn == null ? other$sn != null : !this$sn.equals(other$sn)) {
            return false;
        }
        String this$paySn = this.getPaySn();
        String other$paySn = other.getPaySn();
        if (this$paySn == null ? other$paySn != null : !this$paySn.equals(other$paySn)) {
            return false;
        }
        BigDecimal this$orderAmount = this.getOrderAmount();
        BigDecimal other$orderAmount = other.getOrderAmount();
        if (this$orderAmount == null ? other$orderAmount != null : !((Object)this$orderAmount).equals(other$orderAmount)) {
            return false;
        }
        BigDecimal this$couponAmount = this.getCouponAmount();
        BigDecimal other$couponAmount = other.getCouponAmount();
        if (this$couponAmount == null ? other$couponAmount != null : !((Object)this$couponAmount).equals(other$couponAmount)) {
            return false;
        }
        String this$transactionId = this.getTransactionId();
        String other$transactionId = other.getTransactionId();
        if (this$transactionId == null ? other$transactionId != null : !this$transactionId.equals(other$transactionId)) {
            return false;
        }
        String this$refundTransactionId = this.getRefundTransactionId();
        String other$refundTransactionId = other.getRefundTransactionId();
        if (this$refundTransactionId == null ? other$refundTransactionId != null : !this$refundTransactionId.equals(other$refundTransactionId)) {
            return false;
        }
        String this$voucher = this.getVoucher();
        String other$voucher = other.getVoucher();
        return !(this$voucher == null ? other$voucher != null : !this$voucher.equals(other$voucher));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RechargeOrder;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $studentId = this.getStudentId();
        result = result * 59 + ($studentId == null ? 43 : ((Object)$studentId).hashCode());
        Integer $payWay = this.getPayWay();
        result = result * 59 + ($payWay == null ? 43 : ((Object)$payWay).hashCode());
        Integer $payStatus = this.getPayStatus();
        result = result * 59 + ($payStatus == null ? 43 : ((Object)$payStatus).hashCode());
        Long $payTime = this.getPayTime();
        result = result * 59 + ($payTime == null ? 43 : ((Object)$payTime).hashCode());
        Integer $couponId = this.getCouponId();
        result = result * 59 + ($couponId == null ? 43 : ((Object)$couponId).hashCode());
        Integer $orderTerminal = this.getOrderTerminal();
        result = result * 59 + ($orderTerminal == null ? 43 : ((Object)$orderTerminal).hashCode());
        Integer $refundStatus = this.getRefundStatus();
        result = result * 59 + ($refundStatus == null ? 43 : ((Object)$refundStatus).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        Integer $courseClassId = this.getCourseClassId();
        result = result * 59 + ($courseClassId == null ? 43 : ((Object)$courseClassId).hashCode());
        Integer $couponGoodsId = this.getCouponGoodsId();
        result = result * 59 + ($couponGoodsId == null ? 43 : ((Object)$couponGoodsId).hashCode());
        Integer $lessonCount = this.getLessonCount();
        result = result * 59 + ($lessonCount == null ? 43 : ((Object)$lessonCount).hashCode());
        Integer $isAudit = this.getIsAudit();
        result = result * 59 + ($isAudit == null ? 43 : ((Object)$isAudit).hashCode());
        String $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : $sn.hashCode());
        String $paySn = this.getPaySn();
        result = result * 59 + ($paySn == null ? 43 : $paySn.hashCode());
        BigDecimal $orderAmount = this.getOrderAmount();
        result = result * 59 + ($orderAmount == null ? 43 : ((Object)$orderAmount).hashCode());
        BigDecimal $couponAmount = this.getCouponAmount();
        result = result * 59 + ($couponAmount == null ? 43 : ((Object)$couponAmount).hashCode());
        String $transactionId = this.getTransactionId();
        result = result * 59 + ($transactionId == null ? 43 : $transactionId.hashCode());
        String $refundTransactionId = this.getRefundTransactionId();
        result = result * 59 + ($refundTransactionId == null ? 43 : $refundTransactionId.hashCode());
        String $voucher = this.getVoucher();
        result = result * 59 + ($voucher == null ? 43 : $voucher.hashCode());
        return result;
    }

    public String toString() {
        return "RechargeOrder(id=" + this.getId() + ", userId=" + this.getUserId() + ", studentId=" + this.getStudentId() + ", sn=" + this.getSn() + ", paySn=" + this.getPaySn() + ", payWay=" + this.getPayWay() + ", payStatus=" + this.getPayStatus() + ", payTime=" + this.getPayTime() + ", orderAmount=" + this.getOrderAmount() + ", couponId=" + this.getCouponId() + ", couponAmount=" + this.getCouponAmount() + ", orderTerminal=" + this.getOrderTerminal() + ", transactionId=" + this.getTransactionId() + ", refundStatus=" + this.getRefundStatus() + ", refundTransactionId=" + this.getRefundTransactionId() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ", courseClassId=" + this.getCourseClassId() + ", couponGoodsId=" + this.getCouponGoodsId() + ", lessonCount=" + this.getLessonCount() + ", activityId=" + this.getActivityId() + ", activityStudentIds=" + this.getActivityStudentIds() + ", activityCount=" + this.getActivityCount() + ", voucher=" + this.getVoucher() + ", isAudit=" + this.getIsAudit() + ")";
    }
}

