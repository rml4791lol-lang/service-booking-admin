/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.excel.annotation.ExcelProperty
 *  com.alibaba.excel.annotation.write.style.ColumnWidth
 *  com.mdd.admin.vo.instructor.InstructorExportVo
 *  io.swagger.annotations.ApiModel
 */
package com.mdd.admin.vo.instructor;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u6559\u7ec3\u5bfc\u51faVo")
public class InstructorExportVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"ID"})
    @ColumnWidth(value=10)
    private Integer id;
    @ExcelProperty(value={"\u6559\u7ec3\u59d3\u540d"})
    @ColumnWidth(value=20)
    private String name;
    @ExcelProperty(value={"\u8054\u7cfb\u7535\u8bdd"})
    @ColumnWidth(value=20)
    private String phone;
    @ExcelProperty(value={"\u4e13\u4e1a\u9886\u57df"})
    @ColumnWidth(value=20)
    private String specialty;
    @ExcelProperty(value={"\u4ece\u4e1a\u5e74\u9650"})
    @ColumnWidth(value=15)
    private Integer experienceYears;
    @ExcelProperty(value={"\u8d1f\u8d23\u73ed\u7ea7"})
    @ColumnWidth(value=15)
    private Integer classCount;
    @ExcelProperty(value={"\u8bfe\u916c(\u5143/\u8bfe\u65f6)"})
    @ColumnWidth(value=15)
    private BigDecimal hourlyRate;
    @ExcelProperty(value={"\u8bc4\u5206"})
    @ColumnWidth(value=10)
    private BigDecimal ratingScore;
    @ExcelProperty(value={"\u72b6\u6001"})
    @ColumnWidth(value=15)
    private String statusDesc;
    @ExcelProperty(value={"\u521b\u5efa\u65f6\u95f4"})
    @ColumnWidth(value=25)
    private String createTime;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getSpecialty() {
        return this.specialty;
    }

    public Integer getExperienceYears() {
        return this.experienceYears;
    }

    public Integer getClassCount() {
        return this.classCount;
    }

    public BigDecimal getHourlyRate() {
        return this.hourlyRate;
    }

    public BigDecimal getRatingScore() {
        return this.ratingScore;
    }

    public String getStatusDesc() {
        return this.statusDesc;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public void setClassCount(Integer classCount) {
        this.classCount = classCount;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setRatingScore(BigDecimal ratingScore) {
        this.ratingScore = ratingScore;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InstructorExportVo)) {
            return false;
        }
        InstructorExportVo other = (InstructorExportVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$experienceYears = this.getExperienceYears();
        Integer other$experienceYears = other.getExperienceYears();
        if (this$experienceYears == null ? other$experienceYears != null : !((Object)this$experienceYears).equals(other$experienceYears)) {
            return false;
        }
        Integer this$classCount = this.getClassCount();
        Integer other$classCount = other.getClassCount();
        if (this$classCount == null ? other$classCount != null : !((Object)this$classCount).equals(other$classCount)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$phone = this.getPhone();
        String other$phone = other.getPhone();
        if (this$phone == null ? other$phone != null : !this$phone.equals(other$phone)) {
            return false;
        }
        String this$specialty = this.getSpecialty();
        String other$specialty = other.getSpecialty();
        if (this$specialty == null ? other$specialty != null : !this$specialty.equals(other$specialty)) {
            return false;
        }
        BigDecimal this$hourlyRate = this.getHourlyRate();
        BigDecimal other$hourlyRate = other.getHourlyRate();
        if (this$hourlyRate == null ? other$hourlyRate != null : !((Object)this$hourlyRate).equals(other$hourlyRate)) {
            return false;
        }
        BigDecimal this$ratingScore = this.getRatingScore();
        BigDecimal other$ratingScore = other.getRatingScore();
        if (this$ratingScore == null ? other$ratingScore != null : !((Object)this$ratingScore).equals(other$ratingScore)) {
            return false;
        }
        String this$statusDesc = this.getStatusDesc();
        String other$statusDesc = other.getStatusDesc();
        if (this$statusDesc == null ? other$statusDesc != null : !this$statusDesc.equals(other$statusDesc)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof InstructorExportVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $experienceYears = this.getExperienceYears();
        result = result * 59 + ($experienceYears == null ? 43 : ((Object)$experienceYears).hashCode());
        Integer $classCount = this.getClassCount();
        result = result * 59 + ($classCount == null ? 43 : ((Object)$classCount).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $phone = this.getPhone();
        result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
        String $specialty = this.getSpecialty();
        result = result * 59 + ($specialty == null ? 43 : $specialty.hashCode());
        BigDecimal $hourlyRate = this.getHourlyRate();
        result = result * 59 + ($hourlyRate == null ? 43 : ((Object)$hourlyRate).hashCode());
        BigDecimal $ratingScore = this.getRatingScore();
        result = result * 59 + ($ratingScore == null ? 43 : ((Object)$ratingScore).hashCode());
        String $statusDesc = this.getStatusDesc();
        result = result * 59 + ($statusDesc == null ? 43 : $statusDesc.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        return result;
    }

    public String toString() {
        return "InstructorExportVo(id=" + this.getId() + ", name=" + this.getName() + ", phone=" + this.getPhone() + ", specialty=" + this.getSpecialty() + ", experienceYears=" + this.getExperienceYears() + ", classCount=" + this.getClassCount() + ", hourlyRate=" + this.getHourlyRate() + ", ratingScore=" + this.getRatingScore() + ", statusDesc=" + this.getStatusDesc() + ", createTime=" + this.getCreateTime() + ")";
    }
}

