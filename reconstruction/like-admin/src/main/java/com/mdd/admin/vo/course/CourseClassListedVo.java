/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.course.CourseClassListedVo
 */
package com.mdd.admin.vo.course;

import java.io.Serializable;
import java.math.BigDecimal;

public class CourseClassListedVo
implements Serializable {
    private Integer id;
    private String name;
    private String summary;
    private String categoryName;
    private Integer agentId;
    private Integer communityId;
    private String communityName;
    private Integer areaId;
    private String areaName;
    private String instructorName;
    private String venue;
    private String startDate;
    private String endDate;
    private String weekDays;
    private String timeSlot;
    private Integer scheduleMode;
    private String scheduleRules;
    private Integer totalLessons;
    private BigDecimal feePerLesson;
    private BigDecimal totalFee;
    private Integer studentCount;
    private Integer maxStudents;
    private Integer status;
    private Integer isShow;
    private Integer hasIntro;
    private String createTime;
    private String image;
    /** 适龄下限（岁，NULL=未维护）。 */
    private Integer ageMin;
    /** 适龄上限（岁，NULL=未维护）。 */
    private Integer ageMax;
    /** 课程难度/等级（空=回退类目名）。 */
    private String level;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public Integer getAgentId() {
        return this.agentId;
    }

    public Integer getCommunityId() {
        return this.communityId;
    }

    public String getCommunityName() {
        return this.communityName;
    }

    public Integer getAreaId() {
        return this.areaId;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public String getInstructorName() {
        return this.instructorName;
    }

    public String getVenue() {
        return this.venue;
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

    public Integer getStudentCount() {
        return this.studentCount;
    }

    public Integer getMaxStudents() {
        return this.maxStudents;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Integer getIsShow() {
        return this.isShow;
    }

    public Integer getHasIntro() {
        return this.hasIntro;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public String getImage() {
        return this.image;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public void setVenue(String venue) {
        this.venue = venue;
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

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public void setHasIntro(Integer hasIntro) {
        this.hasIntro = hasIntro;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setImage(String image) {
        this.image = image;
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
        if (!(o instanceof CourseClassListedVo)) {
            return false;
        }
        CourseClassListedVo other = (CourseClassListedVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$agentId = this.getAgentId();
        Integer other$agentId = other.getAgentId();
        if (this$agentId == null ? other$agentId != null : !((Object)this$agentId).equals(other$agentId)) {
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
        Integer this$studentCount = this.getStudentCount();
        Integer other$studentCount = other.getStudentCount();
        if (this$studentCount == null ? other$studentCount != null : !((Object)this$studentCount).equals(other$studentCount)) {
            return false;
        }
        Integer this$maxStudents = this.getMaxStudents();
        Integer other$maxStudents = other.getMaxStudents();
        if (this$maxStudents == null ? other$maxStudents != null : !((Object)this$maxStudents).equals(other$maxStudents)) {
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
        String this$categoryName = this.getCategoryName();
        String other$categoryName = other.getCategoryName();
        if (this$categoryName == null ? other$categoryName != null : !this$categoryName.equals(other$categoryName)) {
            return false;
        }
        String this$instructorName = this.getInstructorName();
        String other$instructorName = other.getInstructorName();
        if (this$instructorName == null ? other$instructorName != null : !this$instructorName.equals(other$instructorName)) {
            return false;
        }
        String this$venue = this.getVenue();
        String other$venue = other.getVenue();
        if (this$venue == null ? other$venue != null : !this$venue.equals(other$venue)) {
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
        if (this$totalFee == null ? other$totalFee != null : !((Object)this$totalFee).equals(other$totalFee)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime)) {
            return false;
        }
        String this$image = this.getImage();
        String other$image = other.getImage();
        return !(this$image == null ? other$image != null : !this$image.equals(other$image));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseClassListedVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $agentId = this.getAgentId();
        result = result * 59 + ($agentId == null ? 43 : ((Object)$agentId).hashCode());
        Integer $scheduleMode = this.getScheduleMode();
        result = result * 59 + ($scheduleMode == null ? 43 : ((Object)$scheduleMode).hashCode());
        Integer $totalLessons = this.getTotalLessons();
        result = result * 59 + ($totalLessons == null ? 43 : ((Object)$totalLessons).hashCode());
        Integer $studentCount = this.getStudentCount();
        result = result * 59 + ($studentCount == null ? 43 : ((Object)$studentCount).hashCode());
        Integer $maxStudents = this.getMaxStudents();
        result = result * 59 + ($maxStudents == null ? 43 : ((Object)$maxStudents).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $categoryName = this.getCategoryName();
        result = result * 59 + ($categoryName == null ? 43 : $categoryName.hashCode());
        String $instructorName = this.getInstructorName();
        result = result * 59 + ($instructorName == null ? 43 : $instructorName.hashCode());
        String $venue = this.getVenue();
        result = result * 59 + ($venue == null ? 43 : $venue.hashCode());
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
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        String $image = this.getImage();
        result = result * 59 + ($image == null ? 43 : $image.hashCode());
        return result;
    }

    public String toString() {
        return "CourseClassListedVo(id=" + this.getId() + ", name=" + this.getName() + ", categoryName=" + this.getCategoryName() + ", agentId=" + this.getAgentId() + ", instructorName=" + this.getInstructorName() + ", venue=" + this.getVenue() + ", startDate=" + this.getStartDate() + ", endDate=" + this.getEndDate() + ", weekDays=" + this.getWeekDays() + ", timeSlot=" + this.getTimeSlot() + ", scheduleMode=" + this.getScheduleMode() + ", scheduleRules=" + this.getScheduleRules() + ", totalLessons=" + this.getTotalLessons() + ", feePerLesson=" + this.getFeePerLesson() + ", totalFee=" + this.getTotalFee() + ", studentCount=" + this.getStudentCount() + ", maxStudents=" + this.getMaxStudents() + ", status=" + this.getStatus() + ", createTime=" + this.getCreateTime() + ", image=" + this.getImage() + ")";
    }
}

