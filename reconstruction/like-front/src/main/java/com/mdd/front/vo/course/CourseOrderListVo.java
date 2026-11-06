/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.vo.course.CourseOrderListVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.vo.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u8bfe\u7a0b\u8ba2\u5355\u5217\u8868Vo")
public class CourseOrderListVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u8ba2\u5355ID")
    private Integer id;
    @ApiModelProperty(value="\u8bfe\u7a0bID")
    private Integer courseId;
    @ApiModelProperty(value="\u8ba2\u5355\u53f7")
    private String orderNo;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;
    @ApiModelProperty(value="\u8bfe\u7a0b\u540d\u79f0")
    private String courseName;
    @ApiModelProperty(value="\u6559\u7ec3\u53ca\u573a\u9986")
    private String coachAndVenue;
    @ApiModelProperty(value="\u4e0a\u8bfe\u65f6\u95f4")
    private String classTime;
    @ApiModelProperty(value="\u6709\u6548\u671f")
    private String validPeriod;
    @ApiModelProperty(value="\u8ba2\u5355\u91d1\u989d")
    private BigDecimal amount;
    @ApiModelProperty(value="\u62a5\u540d\u5b66\u5458\u59d3\u540d")
    private String studentName;
    @ApiModelProperty(value="\u72b6\u6001: pending=\u5f85\u652f\u4ed8, paid=\u5df2\u652f\u4ed8, completed=\u5df2\u5b8c\u6210, cancelled=\u5df2\u53d6\u6d88")
    private String status;
    @ApiModelProperty(value="\u72b6\u6001\u6587\u6848")
    private String statusText;
    @ApiModelProperty(value="\u4e3b\u8981\u64cd\u4f5c\u6309\u94ae")
    private String primaryAction;
    @ApiModelProperty(value="\u6b21\u8981\u64cd\u4f5c\u6309\u94ae")
    private String secondaryAction;
    @ApiModelProperty(value="\u53d6\u6d88\u6587\u6848")
    private String cancelText;

    public Integer getId() {
        return this.id;
    }

    public Integer getCourseId() {
        return this.courseId;
    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public String getCoachAndVenue() {
        return this.coachAndVenue;
    }

    public String getClassTime() {
        return this.classTime;
    }

    public String getValidPeriod() {
        return this.validPeriod;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public String getStatus() {
        return this.status;
    }

    public String getStatusText() {
        return this.statusText;
    }

    public String getPrimaryAction() {
        return this.primaryAction;
    }

    public String getSecondaryAction() {
        return this.secondaryAction;
    }

    public String getCancelText() {
        return this.cancelText;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCoachAndVenue(String coachAndVenue) {
        this.coachAndVenue = coachAndVenue;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public void setValidPeriod(String validPeriod) {
        this.validPeriod = validPeriod;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public void setPrimaryAction(String primaryAction) {
        this.primaryAction = primaryAction;
    }

    public void setSecondaryAction(String secondaryAction) {
        this.secondaryAction = secondaryAction;
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CourseOrderListVo)) {
            return false;
        }
        CourseOrderListVo other = (CourseOrderListVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$courseId = this.getCourseId();
        Integer other$courseId = other.getCourseId();
        if (this$courseId == null ? other$courseId != null : !((Object)this$courseId).equals(other$courseId)) {
            return false;
        }
        String this$orderNo = this.getOrderNo();
        String other$orderNo = other.getOrderNo();
        if (this$orderNo == null ? other$orderNo != null : !this$orderNo.equals(other$orderNo)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime)) {
            return false;
        }
        String this$courseName = this.getCourseName();
        String other$courseName = other.getCourseName();
        if (this$courseName == null ? other$courseName != null : !this$courseName.equals(other$courseName)) {
            return false;
        }
        String this$coachAndVenue = this.getCoachAndVenue();
        String other$coachAndVenue = other.getCoachAndVenue();
        if (this$coachAndVenue == null ? other$coachAndVenue != null : !this$coachAndVenue.equals(other$coachAndVenue)) {
            return false;
        }
        String this$classTime = this.getClassTime();
        String other$classTime = other.getClassTime();
        if (this$classTime == null ? other$classTime != null : !this$classTime.equals(other$classTime)) {
            return false;
        }
        String this$validPeriod = this.getValidPeriod();
        String other$validPeriod = other.getValidPeriod();
        if (this$validPeriod == null ? other$validPeriod != null : !this$validPeriod.equals(other$validPeriod)) {
            return false;
        }
        BigDecimal this$amount = this.getAmount();
        BigDecimal other$amount = other.getAmount();
        if (this$amount == null ? other$amount != null : !((Object)this$amount).equals(other$amount)) {
            return false;
        }
        String this$studentName = this.getStudentName();
        String other$studentName = other.getStudentName();
        if (this$studentName == null ? other$studentName != null : !this$studentName.equals(other$studentName)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$statusText = this.getStatusText();
        String other$statusText = other.getStatusText();
        if (this$statusText == null ? other$statusText != null : !this$statusText.equals(other$statusText)) {
            return false;
        }
        String this$primaryAction = this.getPrimaryAction();
        String other$primaryAction = other.getPrimaryAction();
        if (this$primaryAction == null ? other$primaryAction != null : !this$primaryAction.equals(other$primaryAction)) {
            return false;
        }
        String this$secondaryAction = this.getSecondaryAction();
        String other$secondaryAction = other.getSecondaryAction();
        if (this$secondaryAction == null ? other$secondaryAction != null : !this$secondaryAction.equals(other$secondaryAction)) {
            return false;
        }
        String this$cancelText = this.getCancelText();
        String other$cancelText = other.getCancelText();
        return !(this$cancelText == null ? other$cancelText != null : !this$cancelText.equals(other$cancelText));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseOrderListVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $courseId = this.getCourseId();
        result = result * 59 + ($courseId == null ? 43 : ((Object)$courseId).hashCode());
        String $orderNo = this.getOrderNo();
        result = result * 59 + ($orderNo == null ? 43 : $orderNo.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        String $courseName = this.getCourseName();
        result = result * 59 + ($courseName == null ? 43 : $courseName.hashCode());
        String $coachAndVenue = this.getCoachAndVenue();
        result = result * 59 + ($coachAndVenue == null ? 43 : $coachAndVenue.hashCode());
        String $classTime = this.getClassTime();
        result = result * 59 + ($classTime == null ? 43 : $classTime.hashCode());
        String $validPeriod = this.getValidPeriod();
        result = result * 59 + ($validPeriod == null ? 43 : $validPeriod.hashCode());
        BigDecimal $amount = this.getAmount();
        result = result * 59 + ($amount == null ? 43 : ((Object)$amount).hashCode());
        String $studentName = this.getStudentName();
        result = result * 59 + ($studentName == null ? 43 : $studentName.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $statusText = this.getStatusText();
        result = result * 59 + ($statusText == null ? 43 : $statusText.hashCode());
        String $primaryAction = this.getPrimaryAction();
        result = result * 59 + ($primaryAction == null ? 43 : $primaryAction.hashCode());
        String $secondaryAction = this.getSecondaryAction();
        result = result * 59 + ($secondaryAction == null ? 43 : $secondaryAction.hashCode());
        String $cancelText = this.getCancelText();
        result = result * 59 + ($cancelText == null ? 43 : $cancelText.hashCode());
        return result;
    }

    public String toString() {
        return "CourseOrderListVo(id=" + this.getId() + ", courseId=" + this.getCourseId() + ", orderNo=" + this.getOrderNo() + ", createTime=" + this.getCreateTime() + ", courseName=" + this.getCourseName() + ", coachAndVenue=" + this.getCoachAndVenue() + ", classTime=" + this.getClassTime() + ", validPeriod=" + this.getValidPeriod() + ", amount=" + this.getAmount() + ", studentName=" + this.getStudentName() + ", status=" + this.getStatus() + ", statusText=" + this.getStatusText() + ", primaryAction=" + this.getPrimaryAction() + ", secondaryAction=" + this.getSecondaryAction() + ", cancelText=" + this.getCancelText() + ")";
    }
}

