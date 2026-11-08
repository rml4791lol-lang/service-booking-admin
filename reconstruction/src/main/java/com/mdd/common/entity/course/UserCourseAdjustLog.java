package com.mdd.common.entity.course;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 课时调整流水（#222）：后台「调整课时」的事实记录。
 *
 * <p>一行 = 一次课时调整；before/after 为调整前后的账户值（used 为流水口径），
 * change_* 为增量（used 增量会被 {@link com.mdd.common.util.CourseDerivedBalance}
 * 并入派生已用，是「调整→流水→派生一致」的桥）。</p>
 */
@TableName(value = "la_user_course_adjust_log")
@ApiModel(value = "课时调整流水")
public class UserCourseAdjustLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "学员ID")
    private Integer studentId;

    @ApiModelProperty(value = "报名记录ID")
    private Integer userCourseId;

    @ApiModelProperty(value = "课程班级ID")
    private Integer courseClassId;

    @ApiModelProperty(value = "调整前总课时")
    private Integer beforeTotal;

    @ApiModelProperty(value = "调整前已用（流水口径）")
    private Integer beforeUsed;

    @ApiModelProperty(value = "调整前剩余")
    private Integer beforeRemain;

    @ApiModelProperty(value = "调整后总课时")
    private Integer afterTotal;

    @ApiModelProperty(value = "调整后已用（流水口径）")
    private Integer afterUsed;

    @ApiModelProperty(value = "调整后剩余")
    private Integer afterRemain;

    @ApiModelProperty(value = "总课时增量")
    private Integer changeTotal;

    @ApiModelProperty(value = "已用增量（并入派生）")
    private Integer changeUsed;

    @ApiModelProperty(value = "剩余增量")
    private Integer changeRemain;

    @ApiModelProperty(value = "调整原因")
    private String remark;

    @ApiModelProperty(value = "操作管理员ID")
    private Integer adminId;

    @ApiModelProperty(value = "调整时间")
    private Long createTime;

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

    public Integer getUserCourseId() {
        return userCourseId;
    }

    public void setUserCourseId(Integer userCourseId) {
        this.userCourseId = userCourseId;
    }

    public Integer getCourseClassId() {
        return courseClassId;
    }

    public void setCourseClassId(Integer courseClassId) {
        this.courseClassId = courseClassId;
    }

    public Integer getBeforeTotal() {
        return beforeTotal;
    }

    public void setBeforeTotal(Integer beforeTotal) {
        this.beforeTotal = beforeTotal;
    }

    public Integer getBeforeUsed() {
        return beforeUsed;
    }

    public void setBeforeUsed(Integer beforeUsed) {
        this.beforeUsed = beforeUsed;
    }

    public Integer getBeforeRemain() {
        return beforeRemain;
    }

    public void setBeforeRemain(Integer beforeRemain) {
        this.beforeRemain = beforeRemain;
    }

    public Integer getAfterTotal() {
        return afterTotal;
    }

    public void setAfterTotal(Integer afterTotal) {
        this.afterTotal = afterTotal;
    }

    public Integer getAfterUsed() {
        return afterUsed;
    }

    public void setAfterUsed(Integer afterUsed) {
        this.afterUsed = afterUsed;
    }

    public Integer getAfterRemain() {
        return afterRemain;
    }

    public void setAfterRemain(Integer afterRemain) {
        this.afterRemain = afterRemain;
    }

    public Integer getChangeTotal() {
        return changeTotal;
    }

    public void setChangeTotal(Integer changeTotal) {
        this.changeTotal = changeTotal;
    }

    public Integer getChangeUsed() {
        return changeUsed;
    }

    public void setChangeUsed(Integer changeUsed) {
        this.changeUsed = changeUsed;
    }

    public Integer getChangeRemain() {
        return changeRemain;
    }

    public void setChangeRemain(Integer changeRemain) {
        this.changeRemain = changeRemain;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
