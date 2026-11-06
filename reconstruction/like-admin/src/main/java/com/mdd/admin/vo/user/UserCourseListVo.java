package com.mdd.admin.vo.user;

import java.io.Serializable;

/**
 * 老课包（la_user_course）列表行。
 *
 * <p>total/used/remain 为账面值（调整接口直接写这三列，转化券以剩余课时为准）；
 * systemUsed/systemRemain 为按消课流水重算的「学员详情口径」，供核对差异，
 * 不参与调整入参。</p>
 */
public class UserCourseListVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer userId;
    private Integer studentId;
    private String studentName;
    private String mobile;
    private Integer courseClassId;
    private String className;
    private Integer totalLessons;
    private Integer usedLessons;
    private Integer remainLessons;
    private Integer systemUsedLessons;
    private Integer systemRemainLessons;
    private Integer status;
    private String statusText;
    private String createTime;
    private String updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getCourseClassId() {
        return courseClassId;
    }

    public void setCourseClassId(Integer courseClassId) {
        this.courseClassId = courseClassId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(Integer totalLessons) {
        this.totalLessons = totalLessons;
    }

    public Integer getUsedLessons() {
        return usedLessons;
    }

    public void setUsedLessons(Integer usedLessons) {
        this.usedLessons = usedLessons;
    }

    public Integer getRemainLessons() {
        return remainLessons;
    }

    public void setRemainLessons(Integer remainLessons) {
        this.remainLessons = remainLessons;
    }

    public Integer getSystemUsedLessons() {
        return systemUsedLessons;
    }

    public void setSystemUsedLessons(Integer systemUsedLessons) {
        this.systemUsedLessons = systemUsedLessons;
    }

    public Integer getSystemRemainLessons() {
        return systemRemainLessons;
    }

    public void setSystemRemainLessons(Integer systemRemainLessons) {
        this.systemRemainLessons = systemRemainLessons;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
