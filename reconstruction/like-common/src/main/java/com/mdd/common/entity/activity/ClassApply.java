package com.mdd.common.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 开班申请明细（la_class_apply）：一次提交一行，关联聚合组。
 */
@TableName("la_class_apply")
public class ClassApply implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer groupId;
    private Integer userId;
    private Integer studentId;
    private Integer areaId;
    private String areaName;
    private String expectTime;
    private String phone;
    private String note;
    private String status;
    private Long createTime;
    private Long updateTime;
    private Long deleteTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getGroupId() { return groupId; }
    public void setGroupId(Integer groupId) { this.groupId = groupId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }
    public Integer getAreaId() { return areaId; }
    public void setAreaId(Integer areaId) { this.areaId = areaId; }
    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }
    public String getExpectTime() { return expectTime; }
    public void setExpectTime(String expectTime) { this.expectTime = expectTime; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
    public Long getDeleteTime() { return deleteTime; }
    public void setDeleteTime(Long deleteTime) { this.deleteTime = deleteTime; }
}
