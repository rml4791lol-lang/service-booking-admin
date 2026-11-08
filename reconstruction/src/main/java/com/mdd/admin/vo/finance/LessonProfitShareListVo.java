/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.finance.LessonProfitShareListVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.finance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u8bfe\u8282\u5206\u8d26\u660e\u7ec6Vo")
public class LessonProfitShareListVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u8bfe\u8282ID")
    private Integer lessonId;
    @ApiModelProperty(value="\u65e5\u671f")
    private String lessonDate;
    @ApiModelProperty(value="\u65f6\u95f4")
    private String lessonTime;
    @ApiModelProperty(value="\u8bfe\u7a0b\u5730\u5740")
    private String venue;
    @ApiModelProperty(value="\u4ee3\u7406\u4eba")
    private String agentName;
    @ApiModelProperty(value="\u4ee3\u7406\u5206\u8d26\u91d1\u989d")
    private BigDecimal agentAmount;
    @ApiModelProperty(value="\u793e\u533a")
    private String communityName;
    @ApiModelProperty(value="\u793e\u533a\u5206\u8d26\u91d1\u989d")
    private BigDecimal communityAmount;
    @ApiModelProperty(value="\u8bfe\u7a0b")
    private String className;
    @ApiModelProperty(value="\u4efb\u8bfe\u6559\u7ec3")
    private String instructorName;
    @ApiModelProperty(value="\u6559\u7ec3\u5206\u8d26\u91d1\u989d")
    private BigDecimal instructorAmount;
    @ApiModelProperty(value="\u8bfe\u65f6\u91d1\u989d")
    private BigDecimal lessonAmount;
    @ApiModelProperty(value="\u5165\u8d26\u91d1\u989d")
    private BigDecimal platformAmount;
    @ApiModelProperty(value="\u72b6\u6001: 1=\u5df2\u7ed3\u7b97 0=\u672a\u7ed3\u7b97")
    private Integer status;
    @ApiModelProperty(value="\u72b6\u6001\u6587\u6848")
    private String statusLabel;

    public Integer getLessonId() {
        return this.lessonId;
    }

    public String getLessonDate() {
        return this.lessonDate;
    }

    public String getLessonTime() {
        return this.lessonTime;
    }

    public String getVenue() {
        return this.venue;
    }

    public String getAgentName() {
        return this.agentName;
    }

    public BigDecimal getAgentAmount() {
        return this.agentAmount;
    }

    public String getCommunityName() {
        return this.communityName;
    }

    public BigDecimal getCommunityAmount() {
        return this.communityAmount;
    }

    public String getClassName() {
        return this.className;
    }

    public String getInstructorName() {
        return this.instructorName;
    }

    public BigDecimal getInstructorAmount() {
        return this.instructorAmount;
    }

    public BigDecimal getLessonAmount() {
        return this.lessonAmount;
    }

    public BigDecimal getPlatformAmount() {
        return this.platformAmount;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getStatusLabel() {
        return this.statusLabel;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public void setLessonDate(String lessonDate) {
        this.lessonDate = lessonDate;
    }

    public void setLessonTime(String lessonTime) {
        this.lessonTime = lessonTime;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setAgentAmount(BigDecimal agentAmount) {
        this.agentAmount = agentAmount;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public void setCommunityAmount(BigDecimal communityAmount) {
        this.communityAmount = communityAmount;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public void setInstructorAmount(BigDecimal instructorAmount) {
        this.instructorAmount = instructorAmount;
    }

    public void setLessonAmount(BigDecimal lessonAmount) {
        this.lessonAmount = lessonAmount;
    }

    public void setPlatformAmount(BigDecimal platformAmount) {
        this.platformAmount = platformAmount;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LessonProfitShareListVo)) {
            return false;
        }
        LessonProfitShareListVo other = (LessonProfitShareListVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$lessonId = this.getLessonId();
        Integer other$lessonId = other.getLessonId();
        if (this$lessonId == null ? other$lessonId != null : !((Object)this$lessonId).equals(other$lessonId)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$lessonDate = this.getLessonDate();
        String other$lessonDate = other.getLessonDate();
        if (this$lessonDate == null ? other$lessonDate != null : !this$lessonDate.equals(other$lessonDate)) {
            return false;
        }
        String this$lessonTime = this.getLessonTime();
        String other$lessonTime = other.getLessonTime();
        if (this$lessonTime == null ? other$lessonTime != null : !this$lessonTime.equals(other$lessonTime)) {
            return false;
        }
        String this$venue = this.getVenue();
        String other$venue = other.getVenue();
        if (this$venue == null ? other$venue != null : !this$venue.equals(other$venue)) {
            return false;
        }
        String this$agentName = this.getAgentName();
        String other$agentName = other.getAgentName();
        if (this$agentName == null ? other$agentName != null : !this$agentName.equals(other$agentName)) {
            return false;
        }
        BigDecimal this$agentAmount = this.getAgentAmount();
        BigDecimal other$agentAmount = other.getAgentAmount();
        if (this$agentAmount == null ? other$agentAmount != null : !((Object)this$agentAmount).equals(other$agentAmount)) {
            return false;
        }
        String this$communityName = this.getCommunityName();
        String other$communityName = other.getCommunityName();
        if (this$communityName == null ? other$communityName != null : !this$communityName.equals(other$communityName)) {
            return false;
        }
        BigDecimal this$communityAmount = this.getCommunityAmount();
        BigDecimal other$communityAmount = other.getCommunityAmount();
        if (this$communityAmount == null ? other$communityAmount != null : !((Object)this$communityAmount).equals(other$communityAmount)) {
            return false;
        }
        String this$className = this.getClassName();
        String other$className = other.getClassName();
        if (this$className == null ? other$className != null : !this$className.equals(other$className)) {
            return false;
        }
        String this$instructorName = this.getInstructorName();
        String other$instructorName = other.getInstructorName();
        if (this$instructorName == null ? other$instructorName != null : !this$instructorName.equals(other$instructorName)) {
            return false;
        }
        BigDecimal this$instructorAmount = this.getInstructorAmount();
        BigDecimal other$instructorAmount = other.getInstructorAmount();
        if (this$instructorAmount == null ? other$instructorAmount != null : !((Object)this$instructorAmount).equals(other$instructorAmount)) {
            return false;
        }
        BigDecimal this$lessonAmount = this.getLessonAmount();
        BigDecimal other$lessonAmount = other.getLessonAmount();
        if (this$lessonAmount == null ? other$lessonAmount != null : !((Object)this$lessonAmount).equals(other$lessonAmount)) {
            return false;
        }
        BigDecimal this$platformAmount = this.getPlatformAmount();
        BigDecimal other$platformAmount = other.getPlatformAmount();
        if (this$platformAmount == null ? other$platformAmount != null : !((Object)this$platformAmount).equals(other$platformAmount)) {
            return false;
        }
        String this$statusLabel = this.getStatusLabel();
        String other$statusLabel = other.getStatusLabel();
        return !(this$statusLabel == null ? other$statusLabel != null : !this$statusLabel.equals(other$statusLabel));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LessonProfitShareListVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $lessonId = this.getLessonId();
        result = result * 59 + ($lessonId == null ? 43 : ((Object)$lessonId).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $lessonDate = this.getLessonDate();
        result = result * 59 + ($lessonDate == null ? 43 : $lessonDate.hashCode());
        String $lessonTime = this.getLessonTime();
        result = result * 59 + ($lessonTime == null ? 43 : $lessonTime.hashCode());
        String $venue = this.getVenue();
        result = result * 59 + ($venue == null ? 43 : $venue.hashCode());
        String $agentName = this.getAgentName();
        result = result * 59 + ($agentName == null ? 43 : $agentName.hashCode());
        BigDecimal $agentAmount = this.getAgentAmount();
        result = result * 59 + ($agentAmount == null ? 43 : ((Object)$agentAmount).hashCode());
        String $communityName = this.getCommunityName();
        result = result * 59 + ($communityName == null ? 43 : $communityName.hashCode());
        BigDecimal $communityAmount = this.getCommunityAmount();
        result = result * 59 + ($communityAmount == null ? 43 : ((Object)$communityAmount).hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $instructorName = this.getInstructorName();
        result = result * 59 + ($instructorName == null ? 43 : $instructorName.hashCode());
        BigDecimal $instructorAmount = this.getInstructorAmount();
        result = result * 59 + ($instructorAmount == null ? 43 : ((Object)$instructorAmount).hashCode());
        BigDecimal $lessonAmount = this.getLessonAmount();
        result = result * 59 + ($lessonAmount == null ? 43 : ((Object)$lessonAmount).hashCode());
        BigDecimal $platformAmount = this.getPlatformAmount();
        result = result * 59 + ($platformAmount == null ? 43 : ((Object)$platformAmount).hashCode());
        String $statusLabel = this.getStatusLabel();
        result = result * 59 + ($statusLabel == null ? 43 : $statusLabel.hashCode());
        return result;
    }

    public String toString() {
        return "LessonProfitShareListVo(lessonId=" + this.getLessonId() + ", lessonDate=" + this.getLessonDate() + ", lessonTime=" + this.getLessonTime() + ", venue=" + this.getVenue() + ", agentName=" + this.getAgentName() + ", agentAmount=" + this.getAgentAmount() + ", communityName=" + this.getCommunityName() + ", communityAmount=" + this.getCommunityAmount() + ", className=" + this.getClassName() + ", instructorName=" + this.getInstructorName() + ", instructorAmount=" + this.getInstructorAmount() + ", lessonAmount=" + this.getLessonAmount() + ", platformAmount=" + this.getPlatformAmount() + ", status=" + this.getStatus() + ", statusLabel=" + this.getStatusLabel() + ")";
    }
}

