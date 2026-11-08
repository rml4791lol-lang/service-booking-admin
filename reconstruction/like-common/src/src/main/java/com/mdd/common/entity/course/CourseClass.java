/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.course;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u73ed\u7ea7\u7ba1\u7406\u5b9e\u4f53")
public class CourseClass
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="\u4e3b\u952eID")
    private Integer id;
    @ApiModelProperty(value="\u73ed\u7ea7\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="课程简介/副标题")
    private String summary;
    @ApiModelProperty(value="课程介绍富文本(HTML，后台维护)")
    private String introHtml;
    @ApiModelProperty(value="课程介绍多图(相对路径，逗号分隔)")
    private String introImages;
    @ApiModelProperty(value="\u9879\u76eeID")
    private Integer categoryId;
    @ApiModelProperty(value="\u6559\u7ec3ID")
    private Integer instructorId;
    @ApiModelProperty(value="\u6559\u7ec3\u59d3\u540d(\u5197\u4f59)")
    private String instructorName;
    @ApiModelProperty(value="\u4ee3\u7406\u5546ID(\u5206\u9500)")
    private Integer agentId;
    @ApiModelProperty(value="\u793e\u533aID")
    private Integer communityId;
    @ApiModelProperty(value="\u5c0f\u533aID")
    private Integer areaId;
    @ApiModelProperty(value="适龄下限（岁，NULL=未维护）")
    private Integer ageMin;
    @ApiModelProperty(value="适龄上限（岁，NULL=未维护）")
    private Integer ageMax;
    @ApiModelProperty(value="课程难度/等级（空=回退运动类目名）")
    private String level;
    @ApiModelProperty(value="\u8bad\u7ec3\u573a\u9986")
    private String venue;
    @ApiModelProperty(value="\u5750\u6807\uff1alatitude")
    private String latitude;
    @ApiModelProperty(value="\u5750\u6807\uff1alongitude")
    private String longitude;
    @ApiModelProperty(value="\u8bfe\u7a0b\u56fe\u7247")
    private String image;
    @ApiModelProperty(value="\u73ed\u7ea7\u7c7b\u578b: 0=\u6b63\u5e38\u73ed, 1=\u4f53\u9a8c\u73ed")
    private Integer classType;
    @ApiModelProperty(value="\u5f00\u8bfe\u65e5\u671f")
    private Integer startDate;
    @ApiModelProperty(value="\u7ed3\u675f\u65e5\u671f")
    private Integer endDate;
    @ApiModelProperty(value="\u4e0a\u8bfe\u5468\u671f")
    private String weekDays;
    @ApiModelProperty(value="\u4e0a\u8bfe\u65f6\u95f4\u6bb5")
    private String timeSlot;
    @ApiModelProperty(value="\u6392\u8bfe\u6a21\u5f0f: 0=\u7edf\u4e00\u65f6\u6bb5, 1=\u591a\u65f6\u6bb5")
    private Integer scheduleMode;
    @ApiModelProperty(value="\u591a\u65f6\u6bb5\u89c4\u5219 JSON")
    private String scheduleRules;
    @ApiModelProperty(value="\u603b\u8bfe\u8282\u6570")
    private Integer totalLessons;
    @ApiModelProperty(value="\u8bfe\u65f6\u8d39")
    private BigDecimal feePerLesson;
    @ApiModelProperty(value="\u603b\u8d39\u7528")
    private BigDecimal totalFee;
    @ApiModelProperty(value="\u4f18\u60e0\u4ef7\u683c")
    private BigDecimal discountPrice;
    @ApiModelProperty(value="\u5b66\u5458\u4eba\u6570")
    private Integer studentCount;
    @ApiModelProperty(value="\u4eba\u6570\u4e0a\u9650")
    private Integer maxStudents;
    @ApiModelProperty(value="\u72b6\u6001: 0=\u5df2\u7ed3\u675f, 1=\u8fdb\u884c\u4e2d, 2=\u9884\u62a5\u540d")
    private Integer status;
    @ApiModelProperty(value="上架/显示状态: 1=上架(小程序可见), 0=下架")
    private Integer isShow;
    @ApiModelProperty(value="\u662f\u5426\u5220\u9664")
    private Integer isDelete;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Integer createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Integer updateTime;

    public Integer getId() {
        return this.id;
    }

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

    public Integer getCategoryId() {
        return this.categoryId;
    }

    public Integer getInstructorId() {
        return this.instructorId;
    }

    public String getInstructorName() {
        return this.instructorName;
    }

    public Integer getAgentId() {
        return this.agentId;
    }

    public Integer getCommunityId() {
        return this.communityId;
    }

    public Integer getAreaId() {
        return this.areaId;
    }

    public Integer getAgeMin() {
        return this.ageMin;
    }

    public Integer getAgeMax() {
        return this.ageMax;
    }

    public String getLevel() {
        return this.level;
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

    public Integer getStartDate() {
        return this.startDate;
    }

    public Integer getEndDate() {
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

    public BigDecimal getDiscountPrice() {
        return this.discountPrice;
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

    public Integer getIsDelete() {
        return this.isDelete;
    }

    public Integer getCreateTime() {
        return this.createTime;
    }

    public Integer getUpdateTime() {
        return this.updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public void setAgeMin(Integer ageMin) {
        this.ageMin = ageMin;
    }

    public void setAgeMax(Integer ageMax) {
        this.ageMax = ageMax;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Integer endDate) {
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

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
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

    public Integer getIsShow() {
        return this.isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CourseClass)) {
            return false;
        }
        CourseClass other = (CourseClass)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
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
        Integer this$agentId = this.getAgentId();
        Integer other$agentId = other.getAgentId();
        if (this$agentId == null ? other$agentId != null : !((Object)this$agentId).equals(other$agentId)) {
            return false;
        }
        Integer this$communityId = this.getCommunityId();
        Integer other$communityId = other.getCommunityId();
        if (this$communityId == null ? other$communityId != null : !((Object)this$communityId).equals(other$communityId)) {
            return false;
        }
        Integer this$classType = this.getClassType();
        Integer other$classType = other.getClassType();
        if (this$classType == null ? other$classType != null : !((Object)this$classType).equals(other$classType)) {
            return false;
        }
        Integer this$startDate = this.getStartDate();
        Integer other$startDate = other.getStartDate();
        if (this$startDate == null ? other$startDate != null : !((Object)this$startDate).equals(other$startDate)) {
            return false;
        }
        Integer this$endDate = this.getEndDate();
        Integer other$endDate = other.getEndDate();
        if (this$endDate == null ? other$endDate != null : !((Object)this$endDate).equals(other$endDate)) {
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
        Integer this$isDelete = this.getIsDelete();
        Integer other$isDelete = other.getIsDelete();
        if (this$isDelete == null ? other$isDelete != null : !((Object)this$isDelete).equals(other$isDelete)) {
            return false;
        }
        Integer this$createTime = this.getCreateTime();
        Integer other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Integer this$updateTime = this.getUpdateTime();
        Integer other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
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
        BigDecimal this$discountPrice = this.getDiscountPrice();
        BigDecimal other$discountPrice = other.getDiscountPrice();
        return !(this$discountPrice == null ? other$discountPrice != null : !((Object)this$discountPrice).equals(other$discountPrice));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseClass;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $categoryId = this.getCategoryId();
        result = result * 59 + ($categoryId == null ? 43 : ((Object)$categoryId).hashCode());
        Integer $instructorId = this.getInstructorId();
        result = result * 59 + ($instructorId == null ? 43 : ((Object)$instructorId).hashCode());
        Integer $agentId = this.getAgentId();
        result = result * 59 + ($agentId == null ? 43 : ((Object)$agentId).hashCode());
        Integer $communityId = this.getCommunityId();
        result = result * 59 + ($communityId == null ? 43 : ((Object)$communityId).hashCode());
        Integer $classType = this.getClassType();
        result = result * 59 + ($classType == null ? 43 : ((Object)$classType).hashCode());
        Integer $startDate = this.getStartDate();
        result = result * 59 + ($startDate == null ? 43 : ((Object)$startDate).hashCode());
        Integer $endDate = this.getEndDate();
        result = result * 59 + ($endDate == null ? 43 : ((Object)$endDate).hashCode());
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
        Integer $isDelete = this.getIsDelete();
        result = result * 59 + ($isDelete == null ? 43 : ((Object)$isDelete).hashCode());
        Integer $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Integer $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $instructorName = this.getInstructorName();
        result = result * 59 + ($instructorName == null ? 43 : $instructorName.hashCode());
        String $venue = this.getVenue();
        result = result * 59 + ($venue == null ? 43 : $venue.hashCode());
        String $latitude = this.getLatitude();
        result = result * 59 + ($latitude == null ? 43 : $latitude.hashCode());
        String $longitude = this.getLongitude();
        result = result * 59 + ($longitude == null ? 43 : $longitude.hashCode());
        String $image = this.getImage();
        result = result * 59 + ($image == null ? 43 : $image.hashCode());
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
        BigDecimal $discountPrice = this.getDiscountPrice();
        result = result * 59 + ($discountPrice == null ? 43 : ((Object)$discountPrice).hashCode());
        return result;
    }

    public String toString() {
        return "CourseClass(id=" + this.getId() + ", name=" + this.getName() + ", categoryId=" + this.getCategoryId() + ", instructorId=" + this.getInstructorId() + ", instructorName=" + this.getInstructorName() + ", agentId=" + this.getAgentId() + ", communityId=" + this.getCommunityId() + ", areaId=" + this.getAreaId() + ", ageMin=" + this.getAgeMin() + ", ageMax=" + this.getAgeMax() + ", level=" + this.getLevel() + ", venue=" + this.getVenue() + ", latitude=" + this.getLatitude() + ", longitude=" + this.getLongitude() + ", image=" + this.getImage() + ", classType=" + this.getClassType() + ", startDate=" + this.getStartDate() + ", endDate=" + this.getEndDate() + ", weekDays=" + this.getWeekDays() + ", timeSlot=" + this.getTimeSlot() + ", scheduleMode=" + this.getScheduleMode() + ", scheduleRules=" + this.getScheduleRules() + ", totalLessons=" + this.getTotalLessons() + ", feePerLesson=" + this.getFeePerLesson() + ", totalFee=" + this.getTotalFee() + ", discountPrice=" + this.getDiscountPrice() + ", studentCount=" + this.getStudentCount() + ", maxStudents=" + this.getMaxStudents() + ", status=" + this.getStatus() + ", isDelete=" + this.getIsDelete() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

