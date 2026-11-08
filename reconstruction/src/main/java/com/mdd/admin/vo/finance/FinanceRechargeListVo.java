/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.finance.FinanceRechargeListVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.finance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u7f34\u8d39\u8bb0\u5f55\u5217\u8868Vo")
public class FinanceRechargeListVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u7528\u6237\u6635\u79f0")
    private String nickname;
    @ApiModelProperty(value="\u7528\u6237\u5934\u50cf")
    private String avatar;
    @ApiModelProperty(value="\u8d26\u53f7")
    private String account;
    @ApiModelProperty(value="\u624b\u673a\u53f7")
    private String mobile;
    @ApiModelProperty(value="\u8bfe\u7a0b\u540d\u79f0")
    private String courseName;
    @ApiModelProperty(value="\u6559\u7ec3\u540d\u79f0")
    private String instructorName;
    @ApiModelProperty(value="\u5bf9\u8d26\u51ed\u8bc1")
    private String voucher;
    @ApiModelProperty(value="\u5bf9\u8d26\u72b6\u6001: [0=\u672a\u5bf9\u8d26, 1=\u5df2\u5bf9\u8d26]")
    private Integer isAudit;
    @ApiModelProperty(value="\u8ba2\u5355\u7f16\u53f7")
    private String sn;
    @ApiModelProperty(value="\u652f\u4ed8\u65b9\u5f0f: [2=\u5fae\u4fe1\u652f\u4ed8, 3=\u652f\u4ed8\u5b9d\u652f\u4ed8]")
    private String payWay;
    @ApiModelProperty(value="\u652f\u4ed8\u72b6\u6001: [0=\u5f85\u652f\u4ed8, 1=\u5df2\u652f\u4ed8]")
    private Integer payStatus;
    @ApiModelProperty(value="\u9000\u6b3e\u72b6\u6001: [0=\u672a\u9000\u6b3e , 1=\u5df2\u9000\u6b3e]")
    private Integer refundStatus;
    @ApiModelProperty(value="\u652f\u4ed8\u72b6\u6001: [0=\u5f85\u652f\u4ed8, 1=\u5df2\u652f\u4ed8]")
    private String payStatusText;
    @ApiModelProperty(value="\u652f\u4ed8\u65b9\u5f0f")
    private String payWayText;
    @ApiModelProperty(value="\u652f\u4ed8\u91d1\u989d")
    private BigDecimal orderAmount;
    @ApiModelProperty(value="\u652f\u4ed8\u65f6\u95f4")
    private String payTime;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;
    @ApiModelProperty(value="\u4f7f\u7528\u7684\u62b5\u6263\u5238ID")
    private Integer couponId;
    @ApiModelProperty(value="\u62b5\u6263\u91d1\u989d")
    private BigDecimal couponAmount;
    @ApiModelProperty(value="\u62b5\u6263\u5238\u540d\u79f0")
    private String couponName;
    @ApiModelProperty(value="\u5b66\u5458\u59d3\u540d")
    private String studentName;
    @ApiModelProperty(value="\u5b66\u5458\u5934\u50cf")
    private String studentAvatar;

    public Integer getId() {
        return this.id;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getAccount() {
        return this.account;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public String getInstructorName() {
        return this.instructorName;
    }

    public String getVoucher() {
        return this.voucher;
    }

    public Integer getIsAudit() {
        return this.isAudit;
    }

    public String getSn() {
        return this.sn;
    }

    public String getPayWay() {
        return this.payWay;
    }

    public Integer getPayStatus() {
        return this.payStatus;
    }

    public Integer getRefundStatus() {
        return this.refundStatus;
    }

    public String getPayStatusText() {
        return this.payStatusText;
    }

    public String getPayWayText() {
        return this.payWayText;
    }

    public BigDecimal getOrderAmount() {
        return this.orderAmount;
    }

    public String getPayTime() {
        return this.payTime;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public Integer getCouponId() {
        return this.couponId;
    }

    public BigDecimal getCouponAmount() {
        return this.couponAmount;
    }

    public String getCouponName() {
        return this.couponName;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public String getStudentAvatar() {
        return this.studentAvatar;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public void setIsAudit(Integer isAudit) {
        this.isAudit = isAudit;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public void setPayStatusText(String payStatusText) {
        this.payStatusText = payStatusText;
    }

    public void setPayWayText(String payWayText) {
        this.payWayText = payWayText;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStudentAvatar(String studentAvatar) {
        this.studentAvatar = studentAvatar;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FinanceRechargeListVo)) {
            return false;
        }
        FinanceRechargeListVo other = (FinanceRechargeListVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$isAudit = this.getIsAudit();
        Integer other$isAudit = other.getIsAudit();
        if (this$isAudit == null ? other$isAudit != null : !((Object)this$isAudit).equals(other$isAudit)) {
            return false;
        }
        Integer this$payStatus = this.getPayStatus();
        Integer other$payStatus = other.getPayStatus();
        if (this$payStatus == null ? other$payStatus != null : !((Object)this$payStatus).equals(other$payStatus)) {
            return false;
        }
        Integer this$refundStatus = this.getRefundStatus();
        Integer other$refundStatus = other.getRefundStatus();
        if (this$refundStatus == null ? other$refundStatus != null : !((Object)this$refundStatus).equals(other$refundStatus)) {
            return false;
        }
        Integer this$couponId = this.getCouponId();
        Integer other$couponId = other.getCouponId();
        if (this$couponId == null ? other$couponId != null : !((Object)this$couponId).equals(other$couponId)) {
            return false;
        }
        String this$nickname = this.getNickname();
        String other$nickname = other.getNickname();
        if (this$nickname == null ? other$nickname != null : !this$nickname.equals(other$nickname)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
            return false;
        }
        String this$account = this.getAccount();
        String other$account = other.getAccount();
        if (this$account == null ? other$account != null : !this$account.equals(other$account)) {
            return false;
        }
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        if (this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile)) {
            return false;
        }
        String this$courseName = this.getCourseName();
        String other$courseName = other.getCourseName();
        if (this$courseName == null ? other$courseName != null : !this$courseName.equals(other$courseName)) {
            return false;
        }
        String this$instructorName = this.getInstructorName();
        String other$instructorName = other.getInstructorName();
        if (this$instructorName == null ? other$instructorName != null : !this$instructorName.equals(other$instructorName)) {
            return false;
        }
        String this$voucher = this.getVoucher();
        String other$voucher = other.getVoucher();
        if (this$voucher == null ? other$voucher != null : !this$voucher.equals(other$voucher)) {
            return false;
        }
        String this$sn = this.getSn();
        String other$sn = other.getSn();
        if (this$sn == null ? other$sn != null : !this$sn.equals(other$sn)) {
            return false;
        }
        String this$payWay = this.getPayWay();
        String other$payWay = other.getPayWay();
        if (this$payWay == null ? other$payWay != null : !this$payWay.equals(other$payWay)) {
            return false;
        }
        String this$payStatusText = this.getPayStatusText();
        String other$payStatusText = other.getPayStatusText();
        if (this$payStatusText == null ? other$payStatusText != null : !this$payStatusText.equals(other$payStatusText)) {
            return false;
        }
        String this$payWayText = this.getPayWayText();
        String other$payWayText = other.getPayWayText();
        if (this$payWayText == null ? other$payWayText != null : !this$payWayText.equals(other$payWayText)) {
            return false;
        }
        BigDecimal this$orderAmount = this.getOrderAmount();
        BigDecimal other$orderAmount = other.getOrderAmount();
        if (this$orderAmount == null ? other$orderAmount != null : !((Object)this$orderAmount).equals(other$orderAmount)) {
            return false;
        }
        String this$payTime = this.getPayTime();
        String other$payTime = other.getPayTime();
        if (this$payTime == null ? other$payTime != null : !this$payTime.equals(other$payTime)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime)) {
            return false;
        }
        BigDecimal this$couponAmount = this.getCouponAmount();
        BigDecimal other$couponAmount = other.getCouponAmount();
        if (this$couponAmount == null ? other$couponAmount != null : !((Object)this$couponAmount).equals(other$couponAmount)) {
            return false;
        }
        String this$couponName = this.getCouponName();
        String other$couponName = other.getCouponName();
        if (this$couponName == null ? other$couponName != null : !this$couponName.equals(other$couponName)) {
            return false;
        }
        String this$studentName = this.getStudentName();
        String other$studentName = other.getStudentName();
        if (this$studentName == null ? other$studentName != null : !this$studentName.equals(other$studentName)) {
            return false;
        }
        String this$studentAvatar = this.getStudentAvatar();
        String other$studentAvatar = other.getStudentAvatar();
        return !(this$studentAvatar == null ? other$studentAvatar != null : !this$studentAvatar.equals(other$studentAvatar));
    }

    protected boolean canEqual(Object other) {
        return other instanceof FinanceRechargeListVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $isAudit = this.getIsAudit();
        result = result * 59 + ($isAudit == null ? 43 : ((Object)$isAudit).hashCode());
        Integer $payStatus = this.getPayStatus();
        result = result * 59 + ($payStatus == null ? 43 : ((Object)$payStatus).hashCode());
        Integer $refundStatus = this.getRefundStatus();
        result = result * 59 + ($refundStatus == null ? 43 : ((Object)$refundStatus).hashCode());
        Integer $couponId = this.getCouponId();
        result = result * 59 + ($couponId == null ? 43 : ((Object)$couponId).hashCode());
        String $nickname = this.getNickname();
        result = result * 59 + ($nickname == null ? 43 : $nickname.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        String $courseName = this.getCourseName();
        result = result * 59 + ($courseName == null ? 43 : $courseName.hashCode());
        String $instructorName = this.getInstructorName();
        result = result * 59 + ($instructorName == null ? 43 : $instructorName.hashCode());
        String $voucher = this.getVoucher();
        result = result * 59 + ($voucher == null ? 43 : $voucher.hashCode());
        String $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : $sn.hashCode());
        String $payWay = this.getPayWay();
        result = result * 59 + ($payWay == null ? 43 : $payWay.hashCode());
        String $payStatusText = this.getPayStatusText();
        result = result * 59 + ($payStatusText == null ? 43 : $payStatusText.hashCode());
        String $payWayText = this.getPayWayText();
        result = result * 59 + ($payWayText == null ? 43 : $payWayText.hashCode());
        BigDecimal $orderAmount = this.getOrderAmount();
        result = result * 59 + ($orderAmount == null ? 43 : ((Object)$orderAmount).hashCode());
        String $payTime = this.getPayTime();
        result = result * 59 + ($payTime == null ? 43 : $payTime.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        BigDecimal $couponAmount = this.getCouponAmount();
        result = result * 59 + ($couponAmount == null ? 43 : ((Object)$couponAmount).hashCode());
        String $couponName = this.getCouponName();
        result = result * 59 + ($couponName == null ? 43 : $couponName.hashCode());
        String $studentName = this.getStudentName();
        result = result * 59 + ($studentName == null ? 43 : $studentName.hashCode());
        String $studentAvatar = this.getStudentAvatar();
        result = result * 59 + ($studentAvatar == null ? 43 : $studentAvatar.hashCode());
        return result;
    }

    public String toString() {
        return "FinanceRechargeListVo(id=" + this.getId() + ", nickname=" + this.getNickname() + ", avatar=" + this.getAvatar() + ", account=" + this.getAccount() + ", mobile=" + this.getMobile() + ", courseName=" + this.getCourseName() + ", instructorName=" + this.getInstructorName() + ", voucher=" + this.getVoucher() + ", isAudit=" + this.getIsAudit() + ", sn=" + this.getSn() + ", payWay=" + this.getPayWay() + ", payStatus=" + this.getPayStatus() + ", refundStatus=" + this.getRefundStatus() + ", payStatusText=" + this.getPayStatusText() + ", payWayText=" + this.getPayWayText() + ", orderAmount=" + this.getOrderAmount() + ", payTime=" + this.getPayTime() + ", createTime=" + this.getCreateTime() + ", couponId=" + this.getCouponId() + ", couponAmount=" + this.getCouponAmount() + ", couponName=" + this.getCouponName() + ", studentName=" + this.getStudentName() + ", studentAvatar=" + this.getStudentAvatar() + ")";
    }
}

