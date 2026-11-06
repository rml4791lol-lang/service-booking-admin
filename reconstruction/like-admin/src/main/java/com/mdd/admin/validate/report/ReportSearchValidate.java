package com.mdd.admin.validate.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 后台数据报表查询参数（T30.5 / #40）。
 *
 * <p>五类报表共用一套筛选字段，各报表只取自己支持的维度；未使用的字段忽略。
 * 日期均为 {@code yyyy-MM-dd}，含首尾两天。</p>
 */
@ApiModel(value = "数据报表查询参数")
public class ReportSearchValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "开始日期 yyyy-MM-dd（含）")
    private String startDate;

    @ApiModelProperty(value = "结束日期 yyyy-MM-dd（含）")
    private String endDate;

    @ApiModelProperty(value = "社区ID")
    private Integer communityId;

    @ApiModelProperty(value = "课程班级ID")
    private Integer courseId;

    @ApiModelProperty(value = "教练ID")
    private Integer instructorId;

    @ApiModelProperty(value = "活动ID")
    private Integer activityId;

    @ApiModelProperty(value = "券商品ID")
    private Integer couponGoodsId;

    @ApiModelProperty(value = "街道ID（la_region.id，level=3）")
    private Integer streetId;

    @ApiModelProperty(value = "课程类型（开班申请）")
    private String courseType;

    @ApiModelProperty(value = "状态：课程报表 READY/HOLD；核销 attended/booked/none；活动报名 0/1/3；开班申请 pending/following/closed")
    private String status;

    @ApiModelProperty(value = "核销来源：0=正常核销，1=历史人工，2=后台人工补核销")
    private Integer sourceType;

    @ApiModelProperty(value = "券流水类型：1=发放，2=核销扣减，3=补发，4=作废，5=退回")
    private Integer flowType;

    @ApiModelProperty(value = "关键词：学员/家长/手机号/课程/单号/活动标题等")
    private String keyword;

    @ApiModelProperty(value = "学员/家长用户ID（券流水按学员过滤，la_user.id，精确匹配）")
    private Long userId;

    @ApiModelProperty(value = "家长手机号（券流水按手机号过滤，支持模糊匹配）")
    private String mobile;

    @ApiModelProperty(value = "月份 yyyy-MM（教练月营业额，缺省本月）")
    private String month;

    @ApiModelProperty(value = "教练ID（教练月营业额下钻；0=未分配教练）")
    private Integer coachId;

    @ApiModelProperty(value = "导出文件名（不含扩展名）")
    private String file_name;

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

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getCouponGoodsId() {
        return couponGoodsId;
    }

    public void setCouponGoodsId(Integer couponGoodsId) {
        this.couponGoodsId = couponGoodsId;
    }

    public Integer getStreetId() {
        return streetId;
    }

    public void setStreetId(Integer streetId) {
        this.streetId = streetId;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getFlowType() {
        return flowType;
    }

    public void setFlowType(Integer flowType) {
        this.flowType = flowType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
