package com.mdd.admin.validate.activity;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 后台新增活动（#186）。
 *
 * <p>必填：标题、活动日期、时段、地点、费用、名额、陪同上限、报名截止、上下架；
 * 封面/介绍/标签/须知可空。费用为每人费用，报名金额 = fee ×（学员数 + 陪同数）。</p>
 */
public class ActivityCreateValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "活动标题不能为空")
    private String title;
    /** 封面图（相对路径或绝对 URL，可空） */
    private String cover;
    @NotBlank(message = "活动日期不能为空")
    private String activityDate;
    @NotBlank(message = "活动时段不能为空")
    private String timeSlot;
    @NotBlank(message = "活动地点不能为空")
    private String place;
    /** 小区ID（0=不关联） */
    private Integer areaId;
    @NotNull(message = "活动费用不能为空")
    @DecimalMin(value = "0", message = "活动费用不能小于0")
    private BigDecimal fee;
    @NotNull(message = "名额上限不能为空")
    @Min(value = 1, message = "名额上限不能小于1")
    private Integer capacity;
    @NotNull(message = "陪同人数上限不能为空")
    @Min(value = 0, message = "陪同人数上限不能小于0")
    private Integer companionLimit;
    @NotBlank(message = "报名截止日期不能为空")
    private String deadline;
    private String description;
    private String tags;
    private String notice;
    @NotNull(message = "上下架状态不能为空")
    @Min(value = 0, message = "上下架状态只能是 0 或 1")
    @Max(value = 1, message = "上下架状态只能是 0 或 1")
    private Integer status;
    /** 排序，小在前 */
    private Integer sort = 0;

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
}
