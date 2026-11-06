/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.refund.CourseRefundListedVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u9000\u8bfe\u7533\u8bf7\u5217\u8868\u54cd\u5e94\u5b9e\u4f53")
public class CourseRefundListedVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u7528\u6237\u59d3\u540d")
    private String userName;
    @ApiModelProperty(value="\u73ed\u7ea7\u540d\u79f0")
    private String className;
    @ApiModelProperty(value="\u9000\u8bfe\u539f\u56e0")
    private String reason;
    @ApiModelProperty(value="\u8be6\u7ec6\u8bf4\u660e")
    private String detail;
    @ApiModelProperty(value="\u5df2\u4ed8\u91d1\u989d")
    private String payAmount;
    @ApiModelProperty(value="\u5e94\u9000\u91d1\u989d")
    private String refundAmount;
    @ApiModelProperty(value="\u603b\u8bfe\u65f6")
    private Integer totalLessons;
    @ApiModelProperty(value="\u5df2\u6d88\u8bfe\u65f6")
    private Integer usedLessons;
    @ApiModelProperty(value="\u72b6\u6001")
    private Integer status;
    @ApiModelProperty(value="\u72b6\u6001\u63cf\u8ff0")
    private String statusDesc;
    @ApiModelProperty(value="\u5ba1\u6279\u4eba\u59d3\u540d")
    private String auditName;
    @ApiModelProperty(value="\u5ba1\u6279\u65f6\u95f4")
    private String auditTime;
    @ApiModelProperty(value="\u5ba1\u6279\u5907\u6ce8")
    private String auditRemark;
    @ApiModelProperty(value="\u63d0\u4ea4\u65f6\u95f4")
    private String createTime;
    @ApiModelProperty(value="退款任务ID")
    private Integer refundTaskId;
    @ApiModelProperty(value="退款任务状态:0=待执行,1=执行中,2=成功,3=失败")
    private Integer refundTaskStatus;
    @ApiModelProperty(value="退款任务状态描述")
    private String refundTaskStatusDesc;
    @ApiModelProperty(value="退款任务重试次数")
    private Integer refundRetryCount;
    @ApiModelProperty(value="退款任务最后错误")
    private String refundLastError;
    @ApiModelProperty(value="退款任务结果说明")
    private String refundTaskResultMsg;
    @ApiModelProperty(value="退款来源：course=课程, coupon=课程券（#110 T12）")
    private String source;
    @ApiModelProperty(value="退款来源文案：课程/券")
    private String sourceText;

    public Integer getId() {
        return this.id;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getClassName() {
        return this.className;
    }

    public String getReason() {
        return this.reason;
    }

    public String getDetail() {
        return this.detail;
    }

    public String getPayAmount() {
        return this.payAmount;
    }

    public String getRefundAmount() {
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

    public String getStatusDesc() {
        return this.statusDesc;
    }

    public String getAuditName() {
        return this.auditName;
    }

    public String getAuditTime() {
        return this.auditTime;
    }

    public String getAuditRemark() {
        return this.auditRemark;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public void setRefundAmount(String refundAmount) {
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

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getRefundTaskId() {
        return this.refundTaskId;
    }

    public void setRefundTaskId(Integer refundTaskId) {
        this.refundTaskId = refundTaskId;
    }

    public Integer getRefundTaskStatus() {
        return this.refundTaskStatus;
    }

    public void setRefundTaskStatus(Integer refundTaskStatus) {
        this.refundTaskStatus = refundTaskStatus;
    }

    public String getRefundTaskStatusDesc() {
        return this.refundTaskStatusDesc;
    }

    public void setRefundTaskStatusDesc(String refundTaskStatusDesc) {
        this.refundTaskStatusDesc = refundTaskStatusDesc;
    }

    public Integer getRefundRetryCount() {
        return this.refundRetryCount;
    }

    public void setRefundRetryCount(Integer refundRetryCount) {
        this.refundRetryCount = refundRetryCount;
    }

    public String getRefundLastError() {
        return this.refundLastError;
    }

    public void setRefundLastError(String refundLastError) {
        this.refundLastError = refundLastError;
    }

    public String getRefundTaskResultMsg() {
        return this.refundTaskResultMsg;
    }

    public void setRefundTaskResultMsg(String refundTaskResultMsg) {
        this.refundTaskResultMsg = refundTaskResultMsg;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CourseRefundListedVo)) {
            return false;
        }
        CourseRefundListedVo other = (CourseRefundListedVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
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
        String this$userName = this.getUserName();
        String other$userName = other.getUserName();
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) {
            return false;
        }
        String this$className = this.getClassName();
        String other$className = other.getClassName();
        if (this$className == null ? other$className != null : !this$className.equals(other$className)) {
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
        String this$payAmount = this.getPayAmount();
        String other$payAmount = other.getPayAmount();
        if (this$payAmount == null ? other$payAmount != null : !this$payAmount.equals(other$payAmount)) {
            return false;
        }
        String this$refundAmount = this.getRefundAmount();
        String other$refundAmount = other.getRefundAmount();
        if (this$refundAmount == null ? other$refundAmount != null : !this$refundAmount.equals(other$refundAmount)) {
            return false;
        }
        String this$statusDesc = this.getStatusDesc();
        String other$statusDesc = other.getStatusDesc();
        if (this$statusDesc == null ? other$statusDesc != null : !this$statusDesc.equals(other$statusDesc)) {
            return false;
        }
        String this$auditName = this.getAuditName();
        String other$auditName = other.getAuditName();
        if (this$auditName == null ? other$auditName != null : !this$auditName.equals(other$auditName)) {
            return false;
        }
        String this$auditTime = this.getAuditTime();
        String other$auditTime = other.getAuditTime();
        if (this$auditTime == null ? other$auditTime != null : !this$auditTime.equals(other$auditTime)) {
            return false;
        }
        String this$auditRemark = this.getAuditRemark();
        String other$auditRemark = other.getAuditRemark();
        if (this$auditRemark == null ? other$auditRemark != null : !this$auditRemark.equals(other$auditRemark)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseRefundListedVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $totalLessons = this.getTotalLessons();
        result = result * 59 + ($totalLessons == null ? 43 : ((Object)$totalLessons).hashCode());
        Integer $usedLessons = this.getUsedLessons();
        result = result * 59 + ($usedLessons == null ? 43 : ((Object)$usedLessons).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $reason = this.getReason();
        result = result * 59 + ($reason == null ? 43 : $reason.hashCode());
        String $detail = this.getDetail();
        result = result * 59 + ($detail == null ? 43 : $detail.hashCode());
        String $payAmount = this.getPayAmount();
        result = result * 59 + ($payAmount == null ? 43 : $payAmount.hashCode());
        String $refundAmount = this.getRefundAmount();
        result = result * 59 + ($refundAmount == null ? 43 : $refundAmount.hashCode());
        String $statusDesc = this.getStatusDesc();
        result = result * 59 + ($statusDesc == null ? 43 : $statusDesc.hashCode());
        String $auditName = this.getAuditName();
        result = result * 59 + ($auditName == null ? 43 : $auditName.hashCode());
        String $auditTime = this.getAuditTime();
        result = result * 59 + ($auditTime == null ? 43 : $auditTime.hashCode());
        String $auditRemark = this.getAuditRemark();
        result = result * 59 + ($auditRemark == null ? 43 : $auditRemark.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        return result;
    }

    public String toString() {
        return "CourseRefundListedVo(id=" + this.getId() + ", userName=" + this.getUserName() + ", className=" + this.getClassName() + ", reason=" + this.getReason() + ", detail=" + this.getDetail() + ", payAmount=" + this.getPayAmount() + ", refundAmount=" + this.getRefundAmount() + ", totalLessons=" + this.getTotalLessons() + ", usedLessons=" + this.getUsedLessons() + ", status=" + this.getStatus() + ", statusDesc=" + this.getStatusDesc() + ", auditName=" + this.getAuditName() + ", auditTime=" + this.getAuditTime() + ", auditRemark=" + this.getAuditRemark() + ", createTime=" + this.getCreateTime() + ")";
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceText() {
        return this.sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }
}
