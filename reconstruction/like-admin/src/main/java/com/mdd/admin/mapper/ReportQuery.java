package com.mdd.admin.mapper;

/**
 * 报表 SQL 分页查询参数（#66）。
 *
 * <p>只承载已经归一化的查询条件：时间已换算为 epoch 秒，keyword 已转小写并加
 * {@code %} 通配符，分页使用 offset/limit 下推数据库。报表口径本身仍在
 * {@code ReportServiceImpl} 的 SQL（mapper XML）中，和列表/导出共用。</p>
 */
public class ReportQuery {

    private Long startEpoch;
    private Long endEpoch;
    private String startDate;
    private String endDate;
    private String keyword;
    private Integer communityId;
    private Integer courseId;
    private Integer instructorId;
    private Integer sourceType;
    private Integer couponGoodsId;
    private Integer activityId;
    private Integer streetId;
    private Integer flowType;
    private Long userId;
    private String mobile;
    private Integer signupStatus;
    private String bookingStatus;
    private String courseType;
    private String groupStatus;
    private Integer matchNone;
    private Integer offset;
    private Integer limit;

    public Long getStartEpoch() {
        return startEpoch;
    }

    public void setStartEpoch(Long startEpoch) {
        this.startEpoch = startEpoch;
    }

    public Long getEndEpoch() {
        return endEpoch;
    }

    public void setEndEpoch(Long endEpoch) {
        this.endEpoch = endEpoch;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getCouponGoodsId() {
        return couponGoodsId;
    }

    public void setCouponGoodsId(Integer couponGoodsId) {
        this.couponGoodsId = couponGoodsId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getStreetId() {
        return streetId;
    }

    public void setStreetId(Integer streetId) {
        this.streetId = streetId;
    }

    public Integer getFlowType() {
        return flowType;
    }

    public void setFlowType(Integer flowType) {
        this.flowType = flowType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getSignupStatus() {
        return signupStatus;
    }

    public void setSignupStatus(Integer signupStatus) {
        this.signupStatus = signupStatus;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    public Integer getMatchNone() {
        return matchNone;
    }

    public void setMatchNone(Integer matchNone) {
        this.matchNone = matchNone;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
