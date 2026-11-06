package com.mdd.admin.validate.stats;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 数据统计查询参数（T36 #106，统计链公共骨架）。
 *
 * <p>时间三种写法，优先级：{@code month} &gt; {@code startDate/endDate} &gt; 默认本月。
 * 日期均为 {@code yyyy-MM-dd}，含首尾两天；月份为 {@code yyyy-MM}。</p>
 *
 * <p>维度筛选项由 #107（社区）/ #108（小区）/ #109（老板视角）继续复用：
 * order/lesson 明细下钻已带 {@code communityId}/{@code areaId}/{@code courseClassId}，
 * 后续维度只需传入对应 id，不要另起一份明细 SQL。</p>
 */
@ApiModel(value = "数据统计查询参数")
public class StatsSearchValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "月份 yyyy-MM（优先级最高；缺省本月）")
    private String month;

    @ApiModelProperty(value = "开始日期 yyyy-MM-dd（含）")
    private String startDate;

    @ApiModelProperty(value = "结束日期 yyyy-MM-dd（含）")
    private String endDate;

    @ApiModelProperty(value = "教练ID（订单/课节明细、教练维度下钻）")
    private Integer instructorId;

    @ApiModelProperty(value = "教练ID（教练月营业额下钻，兼容 #102 命名；0=未分配教练）")
    private Integer coachId;

    @ApiModelProperty(value = "社区ID（0=未归属；口径 COALESCE(NULLIF(class.community_id,0), area.community_id, 0)）")
    private Integer communityId;

    @ApiModelProperty(value = "小区ID（0=未归属）")
    private Integer areaId;

    @ApiModelProperty(value = "课程班级/课程包ID（la_course_class.id）")
    private Integer courseClassId;

    @ApiModelProperty(value = "关键词：订单号/学员/课程班级（订单、课节明细）")
    private String keyword;

    @ApiModelProperty(value = "是否返回上期对比：1=是（默认 1）")
    private Integer compare;

    @ApiModelProperty(value = "导出文件名（不含扩展名）")
    private String file_name;

    @ApiModelProperty(value = "老板视角排序字段（后端白名单）：revenue=营业额(默认) / orderCount=订单数 / attends=客流")
    private String orderBy;

    @ApiModelProperty(value = "老板视角返回行数：默认 20，0=全部，最大 200（超限按 200 处理）")
    private Integer limit;

    @ApiModelProperty(value = "1=导出（老板视角 4 个查询与查询共用同一 URI/权限，导出返回 code=2 + url）")
    private Integer export;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getCourseClassId() {
        return courseClassId;
    }

    public void setCourseClassId(Integer courseClassId) {
        this.courseClassId = courseClassId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getCompare() {
        return compare;
    }

    public void setCompare(Integer compare) {
        this.compare = compare;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getExport() {
        return export;
    }

    public void setExport(Integer export) {
        this.export = export;
    }
}
