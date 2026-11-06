package com.mdd.common.entity.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 活动主档（la_activity，T29.7 / #27）。
 *
 * <p>费用口径：{@code fee} 是每人费用；报名金额 = fee × 参与学员人数。</p>
 */
@TableName("la_activity")
public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /** 活动标题 */
    private String title;
    /** 封面图 */
    private String cover;
    /** 活动日期 yyyy-MM-dd */
    private String activityDate;
    /** 活动时段 HH:mm-HH:mm */
    private String timeSlot;
    /** 活动地点（展示文案） */
    private String place;
    /** 小区ID（0=不关联） */
    private Integer areaId;
    /** 每人费用 */
    private BigDecimal fee;
    /** 名额上限（人） */
    private Integer capacity;
    /** 陪同人员数上限（0=不开放） */
    private Integer companionLimit;
    /** 已占名额（含待支付预占） */
    private Integer joined;
    /** 报名截止日期 yyyy-MM-dd */
    private String deadline;
    /** 活动介绍（前端 desc） */
    private String description;
    /** 标签，逗号分隔 */
    private String tags;
    /** 报名须知 */
    private String notice;
    /** 上下架：0=下架，1=上架 */
    private Integer status;
    /** 排序，小在前 */
    private Integer sort;
    private Long createTime;
    private Long updateTime;
    private Long deleteTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCover() { return cover; }
    public void setCover(String cover) { this.cover = cover; }
    public String getActivityDate() { return activityDate; }
    public void setActivityDate(String activityDate) { this.activityDate = activityDate; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }
    public Integer getAreaId() { return areaId; }
    public void setAreaId(Integer areaId) { this.areaId = areaId; }
    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public Integer getCompanionLimit() { return companionLimit; }
    public void setCompanionLimit(Integer companionLimit) { this.companionLimit = companionLimit; }
    public Integer getJoined() { return joined; }
    public void setJoined(Integer joined) { this.joined = joined; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getNotice() { return notice; }
    public void setNotice(String notice) { this.notice = notice; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
    public Long getDeleteTime() { return deleteTime; }
    public void setDeleteTime(Long deleteTime) { this.deleteTime = deleteTime; }
}
