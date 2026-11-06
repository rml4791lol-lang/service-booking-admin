package com.mdd.admin.vo.finance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "分账数据按维度汇总Vo")
public class SettlementAggregateVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "汇总维度：community / course / coach")
    private String dimension;
    @ApiModelProperty(value = "维度键")
    private String key;
    @ApiModelProperty(value = "维度ID")
    private Integer id;
    @ApiModelProperty(value = "维度名称")
    private String name;
    @ApiModelProperty(value = "分账项数")
    private Integer itemCount;
    @ApiModelProperty(value = "订单数")
    private Integer orderCount;
    @ApiModelProperty(value = "订单金额")
    private BigDecimal paidOrderAmount;
    @ApiModelProperty(value = "课时费/计费基数")
    private BigDecimal settlementBaseAmount;
    @ApiModelProperty(value = "社区分成")
    private BigDecimal communityShareAmount;
    @ApiModelProperty(value = "代理分成")
    private BigDecimal agentShareAmount;
    @ApiModelProperty(value = "教练分成")
    private BigDecimal instructorShareAmount;
    @ApiModelProperty(value = "平台留存")
    private BigDecimal platformShareAmount;
    @ApiModelProperty(value = "分成合计")
    private BigDecimal shareTotalAmount;
    @ApiModelProperty(value = "READY 项数")
    private Integer readyItemCount;
    @ApiModelProperty(value = "HOLD 项数")
    private Integer holdItemCount;

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
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

    public Integer getReadyItemCount() {
        return readyItemCount;
    }

    public void setReadyItemCount(Integer readyItemCount) {
        this.readyItemCount = readyItemCount;
    }

    public Integer getHoldItemCount() {
        return holdItemCount;
    }

    public void setHoldItemCount(Integer holdItemCount) {
        this.holdItemCount = holdItemCount;
    }
}
