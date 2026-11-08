/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.annotation.TableName
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.course;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@TableName(value="la_course_refund")
@ApiModel(value="\u9000\u8bfe\u7533\u8bf7\u8868")
public class CourseRefund
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u7528\u6237ID")
    private Integer userId;
    @ApiModelProperty(value="\u73ed\u7ea7ID")
    private Integer courseClassId;
    @ApiModelProperty(value="\u539f\u8bfe\u7a0b\u8ba2\u5355ID")
    private Integer orderId;
    @ApiModelProperty(value="\u73ed\u7ea7\u540d\u79f0")
    private String className;
    @ApiModelProperty(value="\u7528\u6237\u59d3\u540d")
    private String userName;
    @ApiModelProperty(value="\u9000\u8bfe\u539f\u56e0")
    private String reason;
    @ApiModelProperty(value="\u8be6\u7ec6\u8bf4\u660e")
    private String detail;
    @ApiModelProperty(value="\u5df2\u4ed8\u91d1\u989d")
    private BigDecimal payAmount;
    @ApiModelProperty(value="\u5e94\u9000\u91d1\u989d")
    private BigDecimal refundAmount;
    @ApiModelProperty(value="\u603b\u8bfe\u65f6")
    private Integer totalLessons;
    @ApiModelProperty(value="\u5df2\u6d88\u8bfe\u65f6")
    private Integer usedLessons;
    @ApiModelProperty(value="\u72b6\u6001:0=\u5f85\u5ba1\u6279,1=\u5df2\u901a\u8fc7,2=\u5df2\u62d2\u7edd")
    private Integer status;
    @ApiModelProperty(value="\u5ba1\u6279\u4ebaID")
    private Integer auditId;
    @ApiModelProperty(value="\u5ba1\u6279\u4eba\u59d3\u540d")
    private String auditName;
    @ApiModelProperty(value="\u5ba1\u6279\u65f6\u95f4")
    private Long auditTime;
    @ApiModelProperty(value="\u5ba1\u6279\u5907\u6ce8")
    private String auditRemark;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Long updateTime;
    @ApiModelProperty(value="\u5220\u9664\u65f6\u95f4")
    private Long deleteTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getCourseClassId() {
        return this.courseClassId;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public String getClassName() {
        return this.className;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getReason() {
        return this.reason;
    }

    public String getDetail() {
        return this.detail;
    }

    public BigDecimal getPayAmount() {
        return this.payAmount;
    }

    public BigDecimal getRefundAmount() {
        return this.refundAmount;
    }

    public Integer getTotalLessons() {
        return this.totalLessons;
    }

    public Integer getUsedLessons() {
        return this.usedLessons;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Integer getAuditId() {
        return this.auditId;
    }

    public String getAuditName() {
        return this.auditName;
    }

    public Long getAuditTime() {
        return this.auditTime;
    }

    public String getAuditRemark() {
        return this.auditRemark;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setCourseClassId(Integer courseClassId) {
        this.courseClassId = courseClassId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public void setTotalLessons(Integer totalLessons) {
        this.totalLessons = totalLessons;
    }

    public void setUsedLessons(Integer usedLessons) {
        this.usedLessons = usedLessons;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public void setAuditTime(Long auditTime) {
        this.auditTime = auditTime;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CourseRefund)) {
            return false;
        }
        CourseRefund other = (CourseRefund)o;
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
        Integer this$courseClassId = this.getCourseClassId();
        Integer other$courseClassId = other.getCourseClassId();
        if (this$courseClassId == null ? other$courseClassId != null : !((Object)this$courseClassId).equals(other$courseClassId)) {
            return false;
        }
        Integer this$orderId = this.getOrderId();
        Integer other$orderId = other.getOrderId();
        if (this$orderId == null ? other$orderId != null : !((Object)this$orderId).equals(other$orderId)) {
            return false;
        }
        Integer this$totalLessons = this.getTotalLessons();
        Integer other$totalLessons = other.getTotalLessons();
        if (this$totalLessons == null ? other$totalLessons != null : !((Object)this$totalLessons).equals(other$totalLessons)) {
            return false;
        }
        Integer this$usedLessons = this.getUsedLessons();
        Integer other$usedLessons = other.getUsedLessons();
        if (this$usedLessons == null ? other$usedLessons != null : !((Object)this$usedLessons).equals(other$usedLessons)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Integer this$auditId = this.getAuditId();
        Integer other$auditId = other.getAuditId();
        if (this$auditId == null ? other$auditId != null : !((Object)this$auditId).equals(other$auditId)) {
            return false;
        }
        Long this$auditTime = this.getAuditTime();
        Long other$auditTime = other.getAuditTime();
        if (this$auditTime == null ? other$auditTime != null : !((Object)this$auditTime).equals(other$auditTime)) {
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
        String this$className = this.getClassName();
        String other$className = other.getClassName();
        if (this$className == null ? other$className != null : !this$className.equals(other$className)) {
            return false;
        }
        String this$userName = this.getUserName();
        String other$userName = other.getUserName();
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) {
            return false;
        }
        String this$reason = this.getReason();
        String other$reason = other.getReason();
        if (this$reason == null ? other$reason != null : !this$reason.equals(other$reason)) {
            return false;
        }
        String this$detail = this.getDetail();
        String other$detail = other.getDetail();
        if (this$detail == null ? other$detail != null : !this$detail.equals(other$detail)) {
            return false;
        }
        BigDecimal this$payAmount = this.getPayAmount();
        BigDecimal other$payAmount = other.getPayAmount();
        if (this$payAmount == null ? other$payAmount != null : !((Object)this$payAmount).equals(other$payAmount)) {
            return false;
        }
        BigDecimal this$refundAmount = this.getRefundAmount();
        BigDecimal other$refundAmount = other.getRefundAmount();
        if (this$refundAmount == null ? other$refundAmount != null : !((Object)this$refundAmount).equals(other$refundAmount)) {
            return false;
        }
        String this$auditName = this.getAuditName();
        String other$auditName = other.getAuditName();
        if (this$auditName == null ? other$auditName != null : !this$auditName.equals(other$auditName)) {
            return false;
        }
        String this$auditRemark = this.getAuditRemark();
        String other$auditRemark = other.getAuditRemark();
        return !(this$auditRemark == null ? other$auditRemark != null : !this$auditRemark.equals(other$auditRemark));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseRefund;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $courseClassId = this.getCourseClassId();
        result = result * 59 + ($courseClassId == null ? 43 : ((Object)$courseClassId).hashCode());
        Integer $orderId = this.getOrderId();
        result = result * 59 + ($orderId == null ? 43 : ((Object)$orderId).hashCode());
        Integer $totalLessons = this.getTotalLessons();
        result = result * 59 + ($totalLessons == null ? 43 : ((Object)$totalLessons).hashCode());
        Integer $usedLessons = this.getUsedLessons();
        result = result * 59 + ($usedLessons == null ? 43 : ((Object)$usedLessons).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $auditId = this.getAuditId();
        result = result * 59 + ($auditId == null ? 43 : ((Object)$auditId).hashCode());
        Long $auditTime = this.getAuditTime();
        result = result * 59 + ($auditTime == null ? 43 : ((Object)$auditTime).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        String $reason = this.getReason();
        result = result * 59 + ($reason == null ? 43 : $reason.hashCode());
        String $detail = this.getDetail();
        result = result * 59 + ($detail == null ? 43 : $detail.hashCode());
        BigDecimal $payAmount = this.getPayAmount();
        result = result * 59 + ($payAmount == null ? 43 : ((Object)$payAmount).hashCode());
        BigDecimal $refundAmount = this.getRefundAmount();
        result = result * 59 + ($refundAmount == null ? 43 : ((Object)$refundAmount).hashCode());
        String $auditName = this.getAuditName();
        result = result * 59 + ($auditName == null ? 43 : $auditName.hashCode());
        String $auditRemark = this.getAuditRemark();
        result = result * 59 + ($auditRemark == null ? 43 : $auditRemark.hashCode());
        return result;
    }

    public String toString() {
        return "CourseRefund(id=" + this.getId() + ", userId=" + this.getUserId() + ", courseClassId=" + this.getCourseClassId() + ", orderId=" + this.getOrderId() + ", className=" + this.getClassName() + ", userName=" + this.getUserName() + ", reason=" + this.getReason() + ", detail=" + this.getDetail() + ", payAmount=" + this.getPayAmount() + ", refundAmount=" + this.getRefundAmount() + ", totalLessons=" + this.getTotalLessons() + ", usedLessons=" + this.getUsedLessons() + ", status=" + this.getStatus() + ", auditId=" + this.getAuditId() + ", auditName=" + this.getAuditName() + ", auditTime=" + this.getAuditTime() + ", auditRemark=" + this.getAuditRemark() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ")";
    }
}

