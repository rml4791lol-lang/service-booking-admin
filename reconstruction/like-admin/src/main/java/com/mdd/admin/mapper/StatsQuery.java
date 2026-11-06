package com.mdd.admin.mapper;

/**
 * 数据统计 SQL 查询参数（T36 #106，统计链公共骨架）。
 *
 * <p>只承载已归一化的条件：时间已换算为 epoch 秒，keyword 已转小写并加 {@code %} 通配符，
 * 分页用 offset/limit 下推数据库。统计口径本身仅在 {@code StatsQueryMapper.xml}，
 * 由 #106 建立、#107/#108/#109 追加维度时复用。</p>
 */
public class StatsQuery {

    private Long startEpoch;
    private Long endEpoch;
    private Long previousStartEpoch;
    private Long previousEndEpoch;
    private String keyword;
    private Integer instructorId;
    private Integer communityId;
    private Integer areaId;
    private Integer courseClassId;
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

    public Long getPreviousStartEpoch() {
        return previousStartEpoch;
    }

    public void setPreviousStartEpoch(Long previousStartEpoch) {
        this.previousStartEpoch = previousStartEpoch;
    }

    public Long getPreviousEndEpoch() {
        return previousEndEpoch;
    }

    public void setPreviousEndEpoch(Long previousEndEpoch) {
        this.previousEndEpoch = previousEndEpoch;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
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
