/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.excel.annotation.ExcelProperty
 *  com.alibaba.excel.annotation.write.style.ColumnWidth
 *  com.mdd.admin.vo.finance.LessonProfitShareExportVo
 *  io.swagger.annotations.ApiModel
 */
package com.mdd.admin.vo.finance;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u5bfc\u51fa\u5206\u8d26\u8bb0\u5f55Vo")
public class LessonProfitShareExportVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u65e5\u671f"})
    @ColumnWidth(value=12)
    private String lessonDate;
    @ExcelProperty(value={"\u65f6\u95f4"})
    @ColumnWidth(value=16)
    private String lessonTime;
    @ExcelProperty(value={"\u8bfe\u7a0b\u5730\u5740"})
    @ColumnWidth(value=24)
    private String venue;
    @ExcelProperty(value={"\u4ee3\u7406\u4eba"})
    @ColumnWidth(value=16)
    private String agentName;
    @ExcelProperty(value={"\u4ee3\u7406\u5206\u8d26"})
    @ColumnWidth(value=14)
    private BigDecimal agentAmount;
    @ExcelProperty(value={"\u793e\u533a"})
    @ColumnWidth(value=16)
    private String communityName;
    @ExcelProperty(value={"\u793e\u533a\u5206\u8d26"})
    @ColumnWidth(value=14)
    private BigDecimal communityAmount;
    @ExcelProperty(value={"\u8bfe\u7a0b"})
    @ColumnWidth(value=24)
    private String className;
    @ExcelProperty(value={"\u4efb\u8bfe\u6559\u7ec3"})
    @ColumnWidth(value=16)
    private String instructorName;
    @ExcelProperty(value={"\u6559\u7ec3\u5206\u8d26"})
    @ColumnWidth(value=14)
    private BigDecimal instructorAmount;
    @ExcelProperty(value={"\u8bfe\u65f6\u91d1\u989d"})
    @ColumnWidth(value=14)
    private BigDecimal lessonAmount;
    @ExcelProperty(value={"\u5165\u8d26\u91d1\u989d"})
    @ColumnWidth(value=14)
    private BigDecimal platformAmount;
    @ExcelProperty(value={"\u72b6\u6001"})
    @ColumnWidth(value=12)
    private String statusLabel;

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

    public String getStatusLabel() {
        return this.statusLabel;
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

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LessonProfitShareExportVo)) {
            return false;
        }
        LessonProfitShareExportVo other = (LessonProfitShareExportVo)o;
        if (!other.canEqual((Object)this)) {
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
        return other instanceof LessonProfitShareExportVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
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
        return "LessonProfitShareExportVo(lessonDate=" + this.getLessonDate() + ", lessonTime=" + this.getLessonTime() + ", venue=" + this.getVenue() + ", agentName=" + this.getAgentName() + ", agentAmount=" + this.getAgentAmount() + ", communityName=" + this.getCommunityName() + ", communityAmount=" + this.getCommunityAmount() + ", className=" + this.getClassName() + ", instructorName=" + this.getInstructorName() + ", instructorAmount=" + this.getInstructorAmount() + ", lessonAmount=" + this.getLessonAmount() + ", platformAmount=" + this.getPlatformAmount() + ", statusLabel=" + this.getStatusLabel() + ")";
    }
}

