package com.mdd.common.settlement;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 分账数据项：一个课程班级在一个分账期间内的聚合结果。
 *
 * <p>字段口径必须与 {@code docs/settlement/分账数据接口契约-v1.0.md} 保持一致。
 * 金额均使用 {@link BigDecimal}，禁止在业务代码中转换为 double。</p>
 */
public class SettlementItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 确定性数据项 ID：SI-CLS-{classId}-{periodStart}-{periodEnd}。 */
    private String itemId;
    /** READY=可上报；HOLD=数据异常，禁止上报；VOID=已作废。 */
    private String status = "READY";

    private Integer communityId = 0;
    private String communityName = "";
    private Integer agentId = 0;
    private String agentName = "";
    private Integer courseClassId = 0;
    private String courseClassName = "";
    private Integer instructorId = 0;
    private String instructorName = "";

    private Integer orderCount = 0;
    private BigDecimal paidOrderAmount = BigDecimal.ZERO;
    private Integer purchasedLessons = 0;
    private BigDecimal settlementBaseAmount = BigDecimal.ZERO;
    private BigDecimal communityShareAmount = BigDecimal.ZERO;
    private BigDecimal agentShareAmount = BigDecimal.ZERO;
    private BigDecimal instructorShareAmount = BigDecimal.ZERO;
    private BigDecimal platformShareAmount = BigDecimal.ZERO;
    private BigDecimal shareTotalAmount = BigDecimal.ZERO;
    private BigDecimal unsettledOrderAmount = BigDecimal.ZERO;

    private String currency = "CNY";
    private List<SettlementCoachShare> coachShares = new ArrayList<SettlementCoachShare>();
    private List<String> sourceTypes = new ArrayList<String>();
    private List<String> dataQuality = new ArrayList<String>();

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

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Integer getCourseClassId() {
        return courseClassId;
    }

    public void setCourseClassId(Integer courseClassId) {
        this.courseClassId = courseClassId;
    }

    public String getCourseClassName() {
        return courseClassName;
    }

    public void setCourseClassName(String courseClassName) {
        this.courseClassName = courseClassName;
    }

    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<SettlementCoachShare> getCoachShares() {
        return coachShares;
    }

    public void setCoachShares(List<SettlementCoachShare> coachShares) {
        this.coachShares = coachShares;
    }

    public List<String> getSourceTypes() {
        return sourceTypes;
    }

    public void setSourceTypes(List<String> sourceTypes) {
        this.sourceTypes = sourceTypes;
    }

    public List<String> getDataQuality() {
        return dataQuality;
    }

    public void setDataQuality(List<String> dataQuality) {
        this.dataQuality = dataQuality;
    }
}
