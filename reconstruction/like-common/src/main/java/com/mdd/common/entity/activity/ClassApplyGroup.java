package com.mdd.common.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 开班申请聚合组（la_class_apply_group）：同一街道 + 课程类型只有一行，joined 合并计数。
 */
@TableName("la_class_apply_group")
public class ClassApplyGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_FOLLOWING = "following";
    public static final String STATUS_CLOSED = "closed";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer streetId;
    private String courseType;
    private Integer joined;
    private Integer target;
    private String status;
    private Long createTime;
    private Long updateTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getStreetId() { return streetId; }
    public void setStreetId(Integer streetId) { this.streetId = streetId; }
    public String getCourseType() { return courseType; }
    public void setCourseType(String courseType) { this.courseType = courseType; }
    public Integer getJoined() { return joined; }
    public void setJoined(Integer joined) { this.joined = joined; }
    public Integer getTarget() { return target; }
    public void setTarget(Integer target) { this.target = target; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
}
