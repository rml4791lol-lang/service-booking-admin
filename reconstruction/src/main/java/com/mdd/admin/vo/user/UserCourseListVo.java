package com.mdd.admin.vo.user;

import java.io.Serializable;

/**
 * 老课包（la_user_course）列表行。
 *
 * <p>#222 起课时以消课流水为唯一事实：used/remain 为流水派生值（含调整流水增量），
 * fieldUsed/fieldRemain 为 la_user_course 账面字段，两者不一致时 fieldMismatch=true
 * 由前端标注；systemUsed/systemRemain 保留同一派生口径（兼容旧前端）。</p>
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
    private Integer fieldUsedLessons;
    private Integer fieldRemainLessons;
    private Boolean fieldMismatch;
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

    public Integer getFieldUsedLessons() {
        return fieldUsedLessons;
    }

    public void setFieldUsedLessons(Integer fieldUsedLessons) {
        this.fieldUsedLessons = fieldUsedLessons;
    }

    public Integer getFieldRemainLessons() {
        return fieldRemainLessons;
    }

    public void setFieldRemainLessons(Integer fieldRemainLessons) {
        this.fieldRemainLessons = fieldRemainLessons;
    }

    public Boolean getFieldMismatch() {
        return fieldMismatch;
    }

    public void setFieldMismatch(Boolean fieldMismatch) {
        this.fieldMismatch = fieldMismatch;
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
