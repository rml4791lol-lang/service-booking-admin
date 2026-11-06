package com.mdd.admin.validate.checkin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 后台补核销审计查询参数（T30.4 / #37）。
 */
@ApiModel(value = "补核销审计查询参数")
public class CourseCheckinAuditSearchValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "场次ID")
    private Integer sessionId;
    @ApiModelProperty(value = "学员ID")
    private Integer studentId;
    @ApiModelProperty(value = "操作日期 yyyy-MM-dd")
    private String date;

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
