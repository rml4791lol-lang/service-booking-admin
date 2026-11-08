/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.course.CourseClassCreateValidate
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.NotNull
 */
package com.mdd.admin.validate.course;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CourseClassCreateValidate
implements Serializable {
    @NotBlank(message="\u73ed\u7ea7\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a")
    private @NotBlank(message="\u73ed\u7ea7\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a") String name;
    @Size(max=255, message="课程简介不能超过255个字符")
    private String summary;
    private String introHtml;
    @Size(max=2000, message="介绍多图路径总长度不能超过2000个字符")
    private String introImages;
    @NotNull(message="\u9879\u76ee\u5206\u7c7b\u4e0d\u80fd\u4e3a\u7a7a")
    private @NotNull(message="\u9879\u76ee\u5206\u7c7b\u4e0d\u80fd\u4e3a\u7a7a") Integer categoryId;
    @NotNull(message="\u6559\u7ec3\u4e0d\u80fd\u4e3a\u7a7a")
    private @NotNull(message="\u6559\u7ec3\u4e0d\u80fd\u4e3a\u7a7a") Integer instructorId;
    private String venue;
    private String latitude;
    private String longitude;
    private String image;
    private Integer classType;
    private String startDate;
    private String endDate;
    private String weekDays;
    private String timeSlot;
    private Integer scheduleMode;
    private String scheduleRules;
    private Integer totalLessons;
    private BigDecimal feePerLesson;
    private BigDecimal totalFee;
    private Integer maxStudents;
    private Integer communityId;
    private Integer areaId;
    private Integer agentId;
    private Integer status;
    private Integer isShow;
    /** 适龄下限（岁，NULL=未维护，#29 课程适龄区间）。 */
    @Min(value=0, message="适龄下限不能小于0岁")
    @Max(value=18, message="适龄下限不能大于18岁")
    private Integer ageMin;
    /** 适龄上限（岁，NULL=未维护）。 */
    @Min(value=0, message="适龄上限不能小于0岁")
    @Max(value=18, message="适龄上限不能大于18岁")
    private Integer ageMax;
    /** 课程难度/等级（空=回退类目名）。 */
    @Size(max=20, message="课程等级不能超过20个字符")
    private String level;

    public String getName() {
        return this.name;
    }

    public String getSummary() {
        return this.summary;
    }

    public String getIntroHtml() {
        return this.introHtml;
    }

    public String getIntroImages() {
        return this.introImages;
    }

    @NotNull(message="\u9879\u76ee\u5206\u7c7b\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u9879\u76ee\u5206\u7c7b\u4e0d\u80fd\u4e3a\u7a7a") Integer getCategoryId() {
        return this.categoryId;
    }

    @NotNull(message="\u6559\u7ec3\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u6559\u7ec3\u4e0d\u80fd\u4e3a\u7a7a") Integer getInstructorId() {
        return this.instructorId;
    }

    public String getVenue() {
        return this.venue;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public String getImage() {
        return this.image;
    }

    public Integer getClassType() {
        return this.classType;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public String getWeekDays() {
        return this.weekDays;
    }

    public String getTimeSlot() {
        return this.timeSlot;
    }

    public Integer getScheduleMode() {
        return this.scheduleMode;
    }

    public String getScheduleRules() {
        return this.scheduleRules;
    }

    public Integer getTotalLessons() {
        return this.totalLessons;
    }

    public BigDecimal getFeePerLesson() {
        return this.feePerLesson;
    }

    public BigDecimal getTotalFee() {
        return this.totalFee;
    }

    public Integer getMaxStudents() {
        return this.maxStudents;
    }

    public Integer getCommunityId() {
        return this.communityId;
    }

    public Integer getAreaId() {
        return this.areaId;
    }

    public Integer getAgentId() {
        return this.agentId;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Integer getIsShow() {
        return this.isShow;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setIntroHtml(String introHtml) {
        this.introHtml = introHtml;
    }

    public void setIntroImages(String introImages) {
        this.introImages = introImages;
    }

    public void setCategoryId(@NotNull(message="\u9879\u76ee\u5206\u7c7b\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u9879\u76ee\u5206\u7c7b\u4e0d\u80fd\u4e3a\u7a7a") Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setInstructorId(@NotNull(message="\u6559\u7ec3\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u6559\u7ec3\u4e0d\u80fd\u4e3a\u7a7a") Integer instructorId) {
        this.instructorId = instructorId;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setClassType(Integer classType) {
        this.classType = classType;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setScheduleMode(Integer scheduleMode) {
        this.scheduleMode = scheduleMode;
    }

    public void setScheduleRules(String scheduleRules) {
        this.scheduleRules = scheduleRules;
    }

    public void setTotalLessons(Integer totalLessons) {
        this.totalLessons = totalLessons;
    }

    public void setFeePerLesson(BigDecimal feePerLesson) {
        this.feePerLesson = feePerLesson;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }


    public Integer getAgeMin() {
        return this.ageMin;
    }

    public void setAgeMin(Integer ageMin) {
        this.ageMin = ageMin;
    }

    public Integer getAgeMax() {
        return this.ageMax;
    }

    public void setAgeMax(Integer ageMax) {
        this.ageMax = ageMax;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CourseClassCreateValidate)) {
            return false;
        }
        CourseClassCreateValidate other = (CourseClassCreateValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$categoryId = this.getCategoryId();
        Integer other$categoryId = other.getCategoryId();
        if (this$categoryId == null ? other$categoryId != null : !((Object)this$categoryId).equals(other$categoryId)) {
            return false;
        }
        Integer this$instructorId = this.getInstructorId();
        Integer other$instructorId = other.getInstructorId();
        if (this$instructorId == null ? other$instructorId != null : !((Object)this$instructorId).equals(other$instructorId)) {
            return false;
        }
        Integer this$classType = this.getClassType();
        Integer other$classType = other.getClassType();
        if (this$classType == null ? other$classType != null : !((Object)this$classType).equals(other$classType)) {
            return false;
        }
        Integer this$scheduleMode = this.getScheduleMode();
        Integer other$scheduleMode = other.getScheduleMode();
        if (this$scheduleMode == null ? other$scheduleMode != null : !((Object)this$scheduleMode).equals(other$scheduleMode)) {
            return false;
        }
        Integer this$totalLessons = this.getTotalLessons();
        Integer other$totalLessons = other.getTotalLessons();
        if (this$totalLessons == null ? other$totalLessons != null : !((Object)this$totalLessons).equals(other$totalLessons)) {
            return false;
        }
        Integer this$maxStudents = this.getMaxStudents();
        Integer other$maxStudents = other.getMaxStudents();
        if (this$maxStudents == null ? other$maxStudents != null : !((Object)this$maxStudents).equals(other$maxStudents)) {
            return false;
        }
        Integer this$communityId = this.getCommunityId();
        Integer other$communityId = other.getCommunityId();
        if (this$communityId == null ? other$communityId != null : !((Object)this$communityId).equals(other$communityId)) {
            return false;
        }
        Integer this$agentId = this.getAgentId();
        Integer other$agentId = other.getAgentId();
        if (this$agentId == null ? other$agentId != null : !((Object)this$agentId).equals(other$agentId)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$venue = this.getVenue();
        String other$venue = other.getVenue();
        if (this$venue == null ? other$venue != null : !this$venue.equals(other$venue)) {
            return false;
        }
        String this$latitude = this.getLatitude();
        String other$latitude = other.getLatitude();
        if (this$latitude == null ? other$latitude != null : !this$latitude.equals(other$latitude)) {
            return false;
        }
        String this$longitude = this.getLongitude();
        String other$longitude = other.getLongitude();
        if (this$longitude == null ? other$longitude != null : !this$longitude.equals(other$longitude)) {
            return false;
        }
        String this$image = this.getImage();
        String other$image = other.getImage();
        if (this$image == null ? other$image != null : !this$image.equals(other$image)) {
            return false;
        }
        String this$startDate = this.getStartDate();
        String other$startDate = other.getStartDate();
        if (this$startDate == null ? other$startDate != null : !this$startDate.equals(other$startDate)) {
            return false;
        }
        String this$endDate = this.getEndDate();
        String other$endDate = other.getEndDate();
        if (this$endDate == null ? other$endDate != null : !this$endDate.equals(other$endDate)) {
            return false;
        }
        String this$weekDays = this.getWeekDays();
        String other$weekDays = other.getWeekDays();
        if (this$weekDays == null ? other$weekDays != null : !this$weekDays.equals(other$weekDays)) {
            return false;
        }
        String this$timeSlot = this.getTimeSlot();
        String other$timeSlot = other.getTimeSlot();
        if (this$timeSlot == null ? other$timeSlot != null : !this$timeSlot.equals(other$timeSlot)) {
            return false;
        }
        String this$scheduleRules = this.getScheduleRules();
        String other$scheduleRules = other.getScheduleRules();
        if (this$scheduleRules == null ? other$scheduleRules != null : !this$scheduleRules.equals(other$scheduleRules)) {
            return false;
        }
        BigDecimal this$feePerLesson = this.getFeePerLesson();
        BigDecimal other$feePerLesson = other.getFeePerLesson();
        if (this$feePerLesson == null ? other$feePerLesson != null : !((Object)this$feePerLesson).equals(other$feePerLesson)) {
            return false;
        }
        BigDecimal this$totalFee = this.getTotalFee();
        BigDecimal other$totalFee = other.getTotalFee();
        return !(this$totalFee == null ? other$totalFee != null : !((Object)this$totalFee).equals(other$totalFee));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseClassCreateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $categoryId = this.getCategoryId();
        result = result * 59 + ($categoryId == null ? 43 : ((Object)$categoryId).hashCode());
        Integer $instructorId = this.getInstructorId();
        result = result * 59 + ($instructorId == null ? 43 : ((Object)$instructorId).hashCode());
        Integer $classType = this.getClassType();
        result = result * 59 + ($classType == null ? 43 : ((Object)$classType).hashCode());
        Integer $scheduleMode = this.getScheduleMode();
        result = result * 59 + ($scheduleMode == null ? 43 : ((Object)$scheduleMode).hashCode());
        Integer $totalLessons = this.getTotalLessons();
        result = result * 59 + ($totalLessons == null ? 43 : ((Object)$totalLessons).hashCode());
        Integer $maxStudents = this.getMaxStudents();
        result = result * 59 + ($maxStudents == null ? 43 : ((Object)$maxStudents).hashCode());
        Integer $communityId = this.getCommunityId();
        result = result * 59 + ($communityId == null ? 43 : ((Object)$communityId).hashCode());
        Integer $agentId = this.getAgentId();
        result = result * 59 + ($agentId == null ? 43 : ((Object)$agentId).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $venue = this.getVenue();
        result = result * 59 + ($venue == null ? 43 : $venue.hashCode());
        String $latitude = this.getLatitude();
        result = result * 59 + ($latitude == null ? 43 : $latitude.hashCode());
        String $longitude = this.getLongitude();
        result = result * 59 + ($longitude == null ? 43 : $longitude.hashCode());
        String $image = this.getImage();
        result = result * 59 + ($image == null ? 43 : $image.hashCode());
        String $startDate = this.getStartDate();
        result = result * 59 + ($startDate == null ? 43 : $startDate.hashCode());
        String $endDate = this.getEndDate();
        result = result * 59 + ($endDate == null ? 43 : $endDate.hashCode());
        String $weekDays = this.getWeekDays();
        result = result * 59 + ($weekDays == null ? 43 : $weekDays.hashCode());
        String $timeSlot = this.getTimeSlot();
        result = result * 59 + ($timeSlot == null ? 43 : $timeSlot.hashCode());
        String $scheduleRules = this.getScheduleRules();
        result = result * 59 + ($scheduleRules == null ? 43 : $scheduleRules.hashCode());
        BigDecimal $feePerLesson = this.getFeePerLesson();
        result = result * 59 + ($feePerLesson == null ? 43 : ((Object)$feePerLesson).hashCode());
        BigDecimal $totalFee = this.getTotalFee();
        result = result * 59 + ($totalFee == null ? 43 : ((Object)$totalFee).hashCode());
        return result;
    }

    public String toString() {
        return "CourseClassCreateValidate(name=" + this.getName() + ", categoryId=" + this.getCategoryId() + ", instructorId=" + this.getInstructorId() + ", venue=" + this.getVenue() + ", latitude=" + this.getLatitude() + ", longitude=" + this.getLongitude() + ", image=" + this.getImage() + ", classType=" + this.getClassType() + ", startDate=" + this.getStartDate() + ", endDate=" + this.getEndDate() + ", weekDays=" + this.getWeekDays() + ", timeSlot=" + this.getTimeSlot() + ", scheduleMode=" + this.getScheduleMode() + ", scheduleRules=" + this.getScheduleRules() + ", totalLessons=" + this.getTotalLessons() + ", feePerLesson=" + this.getFeePerLesson() + ", totalFee=" + this.getTotalFee() + ", maxStudents=" + this.getMaxStudents() + ", communityId=" + this.getCommunityId() + ", agentId=" + this.getAgentId() + ", status=" + this.getStatus() + ")";
    }
}

