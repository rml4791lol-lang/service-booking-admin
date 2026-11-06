package com.mdd.admin.vo.activity;

import io.swagger.annotations.ApiModel;

/**
 * 活动报名告警中的报名明细（#61 / #7 复审）。
 * 字段与详情接口原 JSON key 一致：id/studentId/studentName/status/statusText。
 */
@ApiModel(value = "活动报名超员告警报名明细")
public class ActivityEnrollAlertSignupVo {

    private Integer id = 0;
    private Integer studentId = 0;
    private String studentName = "";
    private Integer status = 0;
    private String statusText = "";

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
}
