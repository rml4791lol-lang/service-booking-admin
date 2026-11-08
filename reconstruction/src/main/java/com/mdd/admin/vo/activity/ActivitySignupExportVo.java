package com.mdd.admin.vo.activity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 活动报名名单导出（含支付状态）。
 *
 * <p>一条报名明细一行；支付状态口径：已支付 / 待支付 / 已取消 / 免费（无订单）。</p>
 */
@ApiModel(value = "活动报名名单导出Vo")
public class ActivitySignupExportVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报名时间")
    @ExcelProperty(value = {"报名时间"})
    @ColumnWidth(22)
    private String createdAt;

    @ApiModelProperty(value = "活动")
    @ExcelProperty(value = {"活动"})
    @ColumnWidth(28)
    private String activityTitle;

    @ApiModelProperty(value = "活动日期")
    @ExcelProperty(value = {"活动日期"})
    @ColumnWidth(14)
    private String activityDate;

    @ApiModelProperty(value = "时段")
    @ExcelProperty(value = {"时段"})
    @ColumnWidth(16)
    private String activityTime;

    @ApiModelProperty(value = "地点")
    @ExcelProperty(value = {"地点"})
    @ColumnWidth(24)
    private String activityPlace;

    @ApiModelProperty(value = "小区")
    @ExcelProperty(value = {"小区"})
    @ColumnWidth(18)
    private String areaName;

    @ApiModelProperty(value = "家长姓名")
    @ExcelProperty(value = {"家长姓名"})
    @ColumnWidth(14)
    private String parentName;

    @ApiModelProperty(value = "手机号")
    @ExcelProperty(value = {"手机号"})
    @ColumnWidth(16)
    private String parentMobile;

    @ApiModelProperty(value = "学员姓名")
    @ExcelProperty(value = {"学员姓名"})
    @ColumnWidth(14)
    private String studentName;

    @ApiModelProperty(value = "报名状态")
    @ExcelProperty(value = {"报名状态"})
    @ColumnWidth(12)
    private String statusText;

    @ApiModelProperty(value = "支付状态")
    @ExcelProperty(value = {"支付状态"})
    @ColumnWidth(12)
    private String payStatusText;

    @ApiModelProperty(value = "订单号")
    @ExcelProperty(value = {"订单号"})
    @ColumnWidth(26)
    private String orderSn;

    @ApiModelProperty(value = "订单金额")
    @ExcelProperty(value = {"订单金额"})
    @ColumnWidth(12)
    private String orderAmount;

    @ApiModelProperty(value = "支付时间")
    @ExcelProperty(value = {"支付时间"})
    @ColumnWidth(22)
    private String payTime;

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getActivityTitle() { return activityTitle; }
    public void setActivityTitle(String activityTitle) { this.activityTitle = activityTitle; }
    public String getActivityDate() { return activityDate; }
    public void setActivityDate(String activityDate) { this.activityDate = activityDate; }
    public String getActivityTime() { return activityTime; }
    public void setActivityTime(String activityTime) { this.activityTime = activityTime; }
    public String getActivityPlace() { return activityPlace; }
    public void setActivityPlace(String activityPlace) { this.activityPlace = activityPlace; }
    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }
    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }
    public String getParentMobile() { return parentMobile; }
    public void setParentMobile(String parentMobile) { this.parentMobile = parentMobile; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
    public String getPayStatusText() { return payStatusText; }
    public void setPayStatusText(String payStatusText) { this.payStatusText = payStatusText; }
    public String getOrderSn() { return orderSn; }
    public void setOrderSn(String orderSn) { this.orderSn = orderSn; }
    public String getOrderAmount() { return orderAmount; }
    public void setOrderAmount(String orderAmount) { this.orderAmount = orderAmount; }
    public String getPayTime() { return payTime; }
    public void setPayTime(String payTime) { this.payTime = payTime; }
}
