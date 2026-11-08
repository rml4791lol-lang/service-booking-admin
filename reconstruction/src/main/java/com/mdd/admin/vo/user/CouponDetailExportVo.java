package com.mdd.admin.vo.user;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * #168 学员导出第二 sheet「券明细」：老抵扣券（la_coupon）与次数券（la_coupon_instance + la_coupon_goods）并列。
 *
 * <p>次数券金额口径：unit = goods.price / goods.times，
 * 已核销 = unit × (total - remaining)；未核销只计仍可用的剩余（status=1 有效），
 * status=2 已用完/3 已过期/4 已作废的未核销 = 0；均四舍五入 2 位。</p>
 *
 * <p>主 sheet 的已核销/未核销金额由本 sheet 按用户汇总得出，两 sheet 合计一致。</p>
 */
@ApiModel(value = "券明细导出Vo")
public class CouponDetailExportVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @ExcelProperty(value = {"用户ID"})
    @ColumnWidth(value = 20)
    private Integer userId;

    @ApiModelProperty(value = "家长手机号")
    @ExcelProperty(value = {"家长手机号"})
    @ColumnWidth(value = 20)
    private String mobile;

    @ApiModelProperty(value = "券类型：老抵扣券/次数券")
    @ExcelProperty(value = {"券类型"})
    @ColumnWidth(value = 20)
    private String couponType;

    @ApiModelProperty(value = "券名称")
    @ExcelProperty(value = {"券名称"})
    @ColumnWidth(value = 30)
    private String couponName;

    @ApiModelProperty(value = "总次数")
    @ExcelProperty(value = {"总次数"})
    @ColumnWidth(value = 20)
    private Integer totalTimes;

    @ApiModelProperty(value = "剩余次数")
    @ExcelProperty(value = {"剩余次数"})
    @ColumnWidth(value = 20)
    private Integer remainingTimes;

    @ApiModelProperty(value = "已核销金额(元)")
    @ExcelProperty(value = {"已核销金额(元)"})
    @ColumnWidth(value = 20)
    private BigDecimal usedAmount;

    @ApiModelProperty(value = "未核销金额(元)")
    @ExcelProperty(value = {"未核销金额(元)"})
    @ColumnWidth(value = 20)
    private BigDecimal unusedAmount;

    @ApiModelProperty(value = "状态")
    @ExcelProperty(value = {"状态"})
    @ColumnWidth(value = 20)
    private String status;

    @ApiModelProperty(value = "到期时间")
    @ExcelProperty(value = {"到期时间"})
    @ColumnWidth(value = 25)
    private String expireTime;

    @ApiModelProperty(value = "核销时间")
    @ExcelProperty(value = {"核销时间"})
    @ColumnWidth(value = 25)
    private String usedTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Integer getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Integer totalTimes) {
        this.totalTimes = totalTimes;
    }

    public Integer getRemainingTimes() {
        return remainingTimes;
    }

    public void setRemainingTimes(Integer remainingTimes) {
        this.remainingTimes = remainingTimes;
    }

    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    public BigDecimal getUnusedAmount() {
        return unusedAmount;
    }

    public void setUnusedAmount(BigDecimal unusedAmount) {
        this.unusedAmount = unusedAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(String usedTime) {
        this.usedTime = usedTime;
    }
}
