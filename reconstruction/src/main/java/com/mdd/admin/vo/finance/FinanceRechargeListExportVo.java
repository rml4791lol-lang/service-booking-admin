/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.excel.annotation.ExcelProperty
 *  com.alibaba.excel.annotation.write.style.ColumnWidth
 *  com.mdd.admin.vo.finance.FinanceRechargeListExportVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.finance;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u5bfc\u51fa\u7f34\u8d39\u8bb0\u5f55\u5217\u8868Vo")
public class FinanceRechargeListExportVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="ID")
    @ExcelProperty(value={"ID"})
    @ColumnWidth(value=10)
    private Integer id;
    @ApiModelProperty(value="\u7528\u6237\u6635\u79f0")
    @ExcelProperty(value={"\u7528\u6237\u6635\u79f0"})
    @ColumnWidth(value=20)
    private String nickname;
    @ApiModelProperty(value="\u8d26\u53f7")
    @ExcelProperty(value={"\u8d26\u53f7"})
    @ColumnWidth(value=20)
    private String account;
    @ApiModelProperty(value="\u624b\u673a\u53f7")
    @ExcelProperty(value={"\u624b\u673a\u53f7"})
    @ColumnWidth(value=15)
    private String mobile;
    @ApiModelProperty(value="\u8bfe\u7a0b\u540d\u79f0")
    @ExcelProperty(value={"\u8bfe\u7a0b\u540d\u79f0"})
    @ColumnWidth(value=25)
    private String courseName;
    @ApiModelProperty(value="\u6559\u7ec3\u540d\u79f0")
    @ExcelProperty(value={"\u6559\u7ec3\u540d\u79f0"})
    @ColumnWidth(value=20)
    private String instructorName;
    @ApiModelProperty(value="\u8ba2\u5355\u7f16\u53f7")
    @ExcelProperty(value={"\u8ba2\u5355\u7f16\u53f7"})
    @ColumnWidth(value=25)
    private String sn;
    @ApiModelProperty(value="\u652f\u4ed8\u91d1\u989d")
    @ExcelProperty(value={"\u652f\u4ed8\u91d1\u989d"})
    @ColumnWidth(value=15)
    private BigDecimal orderAmount;
    @ApiModelProperty(value="\u652f\u4ed8\u65b9\u5f0f")
    @ExcelProperty(value={"\u652f\u4ed8\u65b9\u5f0f"})
    @ColumnWidth(value=15)
    private String payWayText;
    @ApiModelProperty(value="\u652f\u4ed8\u72b6\u6001")
    @ExcelProperty(value={"\u652f\u4ed8\u72b6\u6001"})
    @ColumnWidth(value=12)
    private String payStatusText;
    @ApiModelProperty(value="\u9000\u6b3e\u72b6\u6001")
    @ExcelProperty(value={"\u9000\u6b3e\u72b6\u6001"})
    @ColumnWidth(value=12)
    private Integer refundStatus;
    @ApiModelProperty(value="\u652f\u4ed8\u65f6\u95f4")
    @ExcelProperty(value={"\u652f\u4ed8\u65f6\u95f4"})
    @ColumnWidth(value=20)
    private String payTime;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    @ExcelProperty(value={"\u521b\u5efa\u65f6\u95f4"})
    @ColumnWidth(value=20)
    private String createTime;
    private String avatar;
    private String payWay;
    private Integer payStatus;

    public Integer getId() {
        return this.id;
    }

    public String getNickname() {
        return this.nickname;
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

    public String getSn() {
        return this.sn;
    }

    public BigDecimal getOrderAmount() {
        return this.orderAmount;
    }

    public String getPayWayText() {
        return this.payWayText;
    }

    public String getPayStatusText() {
        return this.payStatusText;
    }

    public Integer getRefundStatus() {
        return this.refundStatus;
    }

    public String getPayTime() {
        return this.payTime;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getPayWay() {
        return this.payWay;
    }

    public Integer getPayStatus() {
        return this.payStatus;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setPayWayText(String payWayText) {
        this.payWayText = payWayText;
    }

    public void setPayStatusText(String payStatusText) {
        this.payStatusText = payStatusText;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FinanceRechargeListExportVo)) {
            return false;
        }
        FinanceRechargeListExportVo other = (FinanceRechargeListExportVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$refundStatus = this.getRefundStatus();
        Integer other$refundStatus = other.getRefundStatus();
        if (this$refundStatus == null ? other$refundStatus != null : !((Object)this$refundStatus).equals(other$refundStatus)) {
            return false;
        }
        Integer this$payStatus = this.getPayStatus();
        Integer other$payStatus = other.getPayStatus();
        if (this$payStatus == null ? other$payStatus != null : !((Object)this$payStatus).equals(other$payStatus)) {
            return false;
        }
        String this$nickname = this.getNickname();
        String other$nickname = other.getNickname();
        if (this$nickname == null ? other$nickname != null : !this$nickname.equals(other$nickname)) {
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
        String this$sn = this.getSn();
        String other$sn = other.getSn();
        if (this$sn == null ? other$sn != null : !this$sn.equals(other$sn)) {
            return false;
        }
        BigDecimal this$orderAmount = this.getOrderAmount();
        BigDecimal other$orderAmount = other.getOrderAmount();
        if (this$orderAmount == null ? other$orderAmount != null : !((Object)this$orderAmount).equals(other$orderAmount)) {
            return false;
        }
        String this$payWayText = this.getPayWayText();
        String other$payWayText = other.getPayWayText();
        if (this$payWayText == null ? other$payWayText != null : !this$payWayText.equals(other$payWayText)) {
            return false;
        }
        String this$payStatusText = this.getPayStatusText();
        String other$payStatusText = other.getPayStatusText();
        if (this$payStatusText == null ? other$payStatusText != null : !this$payStatusText.equals(other$payStatusText)) {
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
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
            return false;
        }
        String this$payWay = this.getPayWay();
        String other$payWay = other.getPayWay();
        return !(this$payWay == null ? other$payWay != null : !this$payWay.equals(other$payWay));
    }

    protected boolean canEqual(Object other) {
        return other instanceof FinanceRechargeListExportVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $refundStatus = this.getRefundStatus();
        result = result * 59 + ($refundStatus == null ? 43 : ((Object)$refundStatus).hashCode());
        Integer $payStatus = this.getPayStatus();
        result = result * 59 + ($payStatus == null ? 43 : ((Object)$payStatus).hashCode());
        String $nickname = this.getNickname();
        result = result * 59 + ($nickname == null ? 43 : $nickname.hashCode());
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        String $courseName = this.getCourseName();
        result = result * 59 + ($courseName == null ? 43 : $courseName.hashCode());
        String $instructorName = this.getInstructorName();
        result = result * 59 + ($instructorName == null ? 43 : $instructorName.hashCode());
        String $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : $sn.hashCode());
        BigDecimal $orderAmount = this.getOrderAmount();
        result = result * 59 + ($orderAmount == null ? 43 : ((Object)$orderAmount).hashCode());
        String $payWayText = this.getPayWayText();
        result = result * 59 + ($payWayText == null ? 43 : $payWayText.hashCode());
        String $payStatusText = this.getPayStatusText();
        result = result * 59 + ($payStatusText == null ? 43 : $payStatusText.hashCode());
        String $payTime = this.getPayTime();
        result = result * 59 + ($payTime == null ? 43 : $payTime.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $payWay = this.getPayWay();
        result = result * 59 + ($payWay == null ? 43 : $payWay.hashCode());
        return result;
    }

    public String toString() {
        return "FinanceRechargeListExportVo(id=" + this.getId() + ", nickname=" + this.getNickname() + ", account=" + this.getAccount() + ", mobile=" + this.getMobile() + ", courseName=" + this.getCourseName() + ", instructorName=" + this.getInstructorName() + ", sn=" + this.getSn() + ", orderAmount=" + this.getOrderAmount() + ", payWayText=" + this.getPayWayText() + ", payStatusText=" + this.getPayStatusText() + ", refundStatus=" + this.getRefundStatus() + ", payTime=" + this.getPayTime() + ", createTime=" + this.getCreateTime() + ", avatar=" + this.getAvatar() + ", payWay=" + this.getPayWay() + ", payStatus=" + this.getPayStatus() + ")";
    }
}

