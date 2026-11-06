package com.mdd.admin.validate.checkin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 后台待核销列表查询参数（T30.4 / #37）。
 */
@ApiModel(value = "待核销列表查询参数")
public class CourseCheckinSearchValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "社区ID")
    private Integer communityId;
    @ApiModelProperty(value = "课程班级ID")
    private Integer courseId;
    @ApiModelProperty(value = "场次日期 yyyy-MM-dd")
    private String date;
    @ApiModelProperty(value = "状态:0=待核销,1=已核销,2=已取消,3=部分核销")
    private Integer status;
    @ApiModelProperty(value = "课程名称关键词")
    private String keyword;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
