package com.mdd.common.entity.photo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 课堂照片授权台账（la_photo_authorization，#214 P0）。
 *
 * <p>按学员维护当前授权状态：一个学员一行（uk_student_id）。家长端授权/撤回时
 * 写 parent + la_user.id；教练核销只读，不写本表（教练操作由核销记录/审计承担）。
 * 授权时 {@code authorizedAt} 记当前时间并清空 {@code revokedAt}；撤回时
 * {@code revokedAt} 记当前时间，保留最近一次授权时间。</p>
 */
@TableName("la_photo_authorization")
public class PhotoAuthorization implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /** 家长账号ID（la_user.id） */
    private Integer userId;
    /** 学员ID（la_student.id） */
    private Integer studentId;
    /** 当前授权状态：0未授权 1已授权 */
    private Integer authorized;
    /** 最近一次授权时间（秒） */
    private Integer authorizedAt;
    /** 最近一次撤回时间（秒） */
    private Integer revokedAt;
    /** 操作人类型：parent/coach/admin */
    private String operatorType;
    /** 操作人ID */
    private Integer operatorId;
    /** 来源：miniprogram/coach_checkin/admin */
    private String source;
    /** 创建时间（秒） */
    private Integer createTime;
    /** 更新时间（秒） */
    private Integer updateTime;

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

    public Integer getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Integer authorized) {
        this.authorized = authorized;
    }

    public Integer getAuthorizedAt() {
        return authorizedAt;
    }

    public void setAuthorizedAt(Integer authorizedAt) {
        this.authorizedAt = authorizedAt;
    }

    public Integer getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(Integer revokedAt) {
        this.revokedAt = revokedAt;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }
}
