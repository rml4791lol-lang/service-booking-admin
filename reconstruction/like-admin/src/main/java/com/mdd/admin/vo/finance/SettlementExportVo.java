package com.mdd.admin.vo.finance;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "分账数据导出Vo")
public class SettlementExportVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "期间")
    @ExcelProperty(value = {"期间"})
    @ColumnWidth(24)
    private String period;

    @ApiModelProperty(value = "分账项ID")
    @ExcelProperty(value = {"分账项ID"})
    @ColumnWidth(34)
    private String itemId;

    @ApiModelProperty(value = "状态")
    @ExcelProperty(value = {"状态"})
    @ColumnWidth(10)
    private String status;

    @ApiModelProperty(value = "社区")
    @ExcelProperty(value = {"社区"})
    @ColumnWidth(20)
    private String communityName;

    @ApiModelProperty(value = "代理")
    @ExcelProperty(value = {"代理"})
    @ColumnWidth(16)
    private String agentName;

    @ApiModelProperty(value = "课程")
    @ExcelProperty(value = {"课程"})
    @ColumnWidth(28)
    private String courseClassName;

    @ApiModelProperty(value = "教练")
    @ExcelProperty(value = {"教练"})
    @ColumnWidth(16)
    private String instructorName;

    @ApiModelProperty(value = "订单数")
    @ExcelProperty(value = {"订单数"})
    @ColumnWidth(10)
    private Integer orderCount;

    @ApiModelProperty(value = "订单金额")
    @ExcelProperty(value = {"订单金额"})
    @ColumnWidth(14)
    private BigDecimal paidOrderAmount;

    @ApiModelProperty(value = "购买课时")
    @ExcelProperty(value = {"购买课时"})
    @ColumnWidth(10)
    private Integer purchasedLessons;

    @ApiModelProperty(value = "课时费/计费基数")
    @ExcelProperty(value = {"课时费/计费基数"})
    @ColumnWidth(16)
    private BigDecimal settlementBaseAmount;

    @ApiModelProperty(value = "社区分成")
    @ExcelProperty(value = {"社区分成"})
    @ColumnWidth(12)
    private BigDecimal communityShareAmount;

    @ApiModelProperty(value = "代理分成")
    @ExcelProperty(value = {"代理分成"})
    @ColumnWidth(12)
    private BigDecimal agentShareAmount;

    @ApiModelProperty(value = "教练分成")
    @ExcelProperty(value = {"教练分成"})
    @ColumnWidth(12)
    private BigDecimal instructorShareAmount;

    @ApiModelProperty(value = "平台留存")
    @ExcelProperty(value = {"平台留存"})
    @ColumnWidth(12)
    private BigDecimal platformShareAmount;

    @ApiModelProperty(value = "分成合计")
    @ExcelProperty(value = {"分成合计"})
    @ColumnWidth(12)
    private BigDecimal shareTotalAmount;

    @ApiModelProperty(value = "未形成分账的订单金额")
    @ExcelProperty(value = {"未形成分账的订单金额"})
    @ColumnWidth(20)
    private BigDecimal unsettledOrderAmount;

    @ApiModelProperty(value = "教练分成明细")
    @ExcelProperty(value = {"教练分成明细"})
    @ColumnWidth(36)
    private String coachShareDetail;

    @ApiModelProperty(value = "数据质量")
    @ExcelProperty(value = {"数据质量"})
    @ColumnWidth(30)
    private String dataQuality;

    @ApiModelProperty(value = "数据来源")
    @ExcelProperty(value = {"数据来源"})
    @ColumnWidth(40)
    private String sourceTypes;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getCourseClassName() {
        return courseClassName;
    }

    public void setCourseClassName(String courseClassName) {
        this.courseClassName = courseClassName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getPaidOrderAmount() {
        return paidOrderAmount;
    }

    public void setPaidOrderAmount(BigDecimal paidOrderAmount) {
        this.paidOrderAmount = paidOrderAmount;
    }

    public Integer getPurchasedLessons() {
        return purchasedLessons;
    }

    public void setPurchasedLessons(Integer purchasedLessons) {
        this.purchasedLessons = purchasedLessons;
    }

    public BigDecimal getSettlementBaseAmount() {
        return settlementBaseAmount;
    }

    public void setSettlementBaseAmount(BigDecimal settlementBaseAmount) {
        this.settlementBaseAmount = settlementBaseAmount;
    }

    public BigDecimal getCommunityShareAmount() {
        return communityShareAmount;
    }

    public void setCommunityShareAmount(BigDecimal communityShareAmount) {
        this.communityShareAmount = communityShareAmount;
    }

    public BigDecimal getAgentShareAmount() {
        return agentShareAmount;
    }

    public void setAgentShareAmount(BigDecimal agentShareAmount) {
        this.agentShareAmount = agentShareAmount;
    }

    public BigDecimal getInstructorShareAmount() {
        return instructorShareAmount;
    }

    public void setInstructorShareAmount(BigDecimal instructorShareAmount) {
        this.instructorShareAmount = instructorShareAmount;
    }

    public BigDecimal getPlatformShareAmount() {
        return platformShareAmount;
    }

    public void setPlatformShareAmount(BigDecimal platformShareAmount) {
        this.platformShareAmount = platformShareAmount;
    }

    public BigDecimal getShareTotalAmount() {
        return shareTotalAmount;
    }

    public void setShareTotalAmount(BigDecimal shareTotalAmount) {
        this.shareTotalAmount = shareTotalAmount;
    }

    public BigDecimal getUnsettledOrderAmount() {
        return unsettledOrderAmount;
    }

    public void setUnsettledOrderAmount(BigDecimal unsettledOrderAmount) {
        this.unsettledOrderAmount = unsettledOrderAmount;
    }

    public String getCoachShareDetail() {
        return coachShareDetail;
    }

    public void setCoachShareDetail(String coachShareDetail) {
        this.coachShareDetail = coachShareDetail;
    }

    public String getDataQuality() {
        return dataQuality;
    }

    public void setDataQuality(String dataQuality) {
        this.dataQuality = dataQuality;
    }

    public String getSourceTypes() {
        return sourceTypes;
    }

    public void setSourceTypes(String sourceTypes) {
        this.sourceTypes = sourceTypes;
    }
}
