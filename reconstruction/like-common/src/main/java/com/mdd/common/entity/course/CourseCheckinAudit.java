package com.mdd.common.entity.course;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 人工补核销审计（la_course_checkin_audit，T30.4 / #37）。
 *
 * <p>每次预约由未核销变为已上课（status=10）时写一条，保留操作人、时间、原因、
 * 场次/学员快照与扣券结果。重复提交被幂等跳过时不重复写。</p>
 */
@TableName("la_course_checkin_audit")
public class CourseCheckinAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer sessionId;
    private Integer classId;
    private Integer lessonNo;
    private Integer bookingId;
    private Integer studentId;
    private String studentName;
    private Integer userId;
    private String courseName;
    private String sessionDate;
    private String timeSlot;
    private String actionType;
    private String reason;
    private Integer operatorAdminId;
    private String operatorName;
    private Integer beforeStatus;
    private Integer afterStatus;
    private Integer deducted;
    private Integer couponId;
    private String couponTitle;
    private Integer couponRemaining;
    private String detail;
    private Integer createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getLessonNo() {
        return lessonNo;
    }

    public void setLessonNo(Integer lessonNo) {
        this.lessonNo = lessonNo;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getOperatorAdminId() {
        return operatorAdminId;
    }

    public void setOperatorAdminId(Integer operatorAdminId) {
        this.operatorAdminId = operatorAdminId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getBeforeStatus() {
        return beforeStatus;
    }

    public void setBeforeStatus(Integer beforeStatus) {
        this.beforeStatus = beforeStatus;
    }

    public Integer getAfterStatus() {
        return afterStatus;
    }

    public void setAfterStatus(Integer afterStatus) {
        this.afterStatus = afterStatus;
    }

    public Integer getDeducted() {
        return deducted;
    }

    public void setDeducted(Integer deducted) {
        this.deducted = deducted;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public Integer getCouponRemaining() {
        return couponRemaining;
    }

    public void setCouponRemaining(Integer couponRemaining) {
        this.couponRemaining = couponRemaining;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}
