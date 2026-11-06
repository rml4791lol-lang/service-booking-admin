/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.instructor.InstructorUpdateValidate
 *  com.mdd.common.validator.annotation.IDMust
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.instructor;

import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u6559\u7ec3\u66f4\u65b0\u53c2\u6570")
public class InstructorUpdateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @IDMust
    @NotNull(message="id\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u4e3b\u952eID")
    private @NotNull(message="id\u53c2\u6570\u7f3a\u5931") Integer id;
    @NotNull(message="name\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u6559\u7ec3\u59d3\u540d")
    private @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String name;
    @Length(min=6, max=20, message="\u5bc6\u7801\u5fc5\u987b\u57286~20\u4e2a\u5b57\u7b26\u5185")
    @ApiModelProperty(value="\u5bc6\u7801")
    private @Length(min=6, max=20, message="\u5bc6\u7801\u5fc5\u987b\u57286~20\u4e2a\u5b57\u7b26\u5185") String password;
    @ApiModelProperty(value="\u5934\u50cf")
    private String avatar;
    @NotNull(message="phone\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u8054\u7cfb\u7535\u8bdd")
    private @NotNull(message="phone\u53c2\u6570\u7f3a\u5931") String phone;
    @ApiModelProperty(value="\u6027\u522b")
    private Integer gender;
    @NotNull(message="specialty\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u4e13\u4e1a\u9886\u57df")
    private @NotNull(message="specialty\u53c2\u6570\u7f3a\u5931") String specialty;
    @NotNull(message="experienceYears\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u4ece\u4e1a\u5e74\u9650")
    private @NotNull(message="experienceYears\u53c2\u6570\u7f3a\u5931") Integer experienceYears;
    @ApiModelProperty(value="\u4e2a\u4eba\u7b80\u4ecb")
    private String introduction;
    @ApiModelProperty(value="\u8eab\u4efd\u8bc1\u53f7\u7801")
    private String idCard;
    @ApiModelProperty(value="\u8d44\u8d28\u8bc1\u4e66\u56fe\u7247")
    private String certImages;
    @ApiModelProperty(value="\u6559\u7ec3\u8d44\u8d28(\u591a\u4e2a\u9017\u53f7\u5206\u9694)")
    private String certification;
    @ApiModelProperty(value="\u53ef\u6388\u8bfe\u7a0b(\u8fd0\u52a8\u9879\u76ee\u5206\u7c7bID,\u9017\u53f7\u5206\u9694)")
    private String teachableCourses;
    @ApiModelProperty(value="\u670d\u52a1\u533a\u57df(\u793e\u533aID,\u9017\u53f7\u5206\u9694)")
    private String serviceAreas;
    @ApiModelProperty(value="\u8d1f\u8d23\u73ed\u7ea7")
    private Integer classCount;
    @ApiModelProperty(value="\u8bfe\u916c")
    private BigDecimal hourlyRate;
    @ApiModelProperty(value="\u8bc4\u5206")
    private BigDecimal ratingScore;
    @ApiModelProperty(value="\u72b6\u6001")
    private Integer status;
    @ApiModelProperty(value="\u6392\u5e8f")
    private Integer sort;

    @NotNull(message="id\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="id\u53c2\u6570\u7f3a\u5931") Integer getId() {
        return this.id;
    }

    @NotNull(message="name\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public String getAvatar() {
        return this.avatar;
    }

    @NotNull(message="phone\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="phone\u53c2\u6570\u7f3a\u5931") String getPhone() {
        return this.phone;
    }

    public Integer getGender() {
        return this.gender;
    }

    @NotNull(message="specialty\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="specialty\u53c2\u6570\u7f3a\u5931") String getSpecialty() {
        return this.specialty;
    }

    @NotNull(message="experienceYears\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="experienceYears\u53c2\u6570\u7f3a\u5931") Integer getExperienceYears() {
        return this.experienceYears;
    }

    public String getIntroduction() {
        return this.introduction;
    }

    public String getIdCard() {
        return this.idCard;
    }

    public String getCertImages() {
        return this.certImages;
    }

    public String getCertification() {
        return this.certification;
    }

    public String getTeachableCourses() {
        return this.teachableCourses;
    }

    public String getServiceAreas() {
        return this.serviceAreas;
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

    public Integer getStatus() {
        return this.status;
    }

    public Integer getSort() {
        return this.sort;
    }

    public void setId(@NotNull(message="id\u53c2\u6570\u7f3a\u5931") @NotNull(message="id\u53c2\u6570\u7f3a\u5931") Integer id) {
        this.id = id;
    }

    public void setName(@NotNull(message="name\u53c2\u6570\u7f3a\u5931") @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setPhone(@NotNull(message="phone\u53c2\u6570\u7f3a\u5931") @NotNull(message="phone\u53c2\u6570\u7f3a\u5931") String phone) {
        this.phone = phone;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setSpecialty(@NotNull(message="specialty\u53c2\u6570\u7f3a\u5931") @NotNull(message="specialty\u53c2\u6570\u7f3a\u5931") String specialty) {
        this.specialty = specialty;
    }

    public void setExperienceYears(@NotNull(message="experienceYears\u53c2\u6570\u7f3a\u5931") @NotNull(message="experienceYears\u53c2\u6570\u7f3a\u5931") Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setCertImages(String certImages) {
        this.certImages = certImages;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public void setTeachableCourses(String teachableCourses) {
        this.teachableCourses = teachableCourses;
    }

    public void setServiceAreas(String serviceAreas) {
        this.serviceAreas = serviceAreas;
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

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InstructorUpdateValidate)) {
            return false;
        }
        InstructorUpdateValidate other = (InstructorUpdateValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$gender = this.getGender();
        Integer other$gender = other.getGender();
        if (this$gender == null ? other$gender != null : !((Object)this$gender).equals(other$gender)) {
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
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
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
        String this$introduction = this.getIntroduction();
        String other$introduction = other.getIntroduction();
        if (this$introduction == null ? other$introduction != null : !this$introduction.equals(other$introduction)) {
            return false;
        }
        String this$idCard = this.getIdCard();
        String other$idCard = other.getIdCard();
        if (this$idCard == null ? other$idCard != null : !this$idCard.equals(other$idCard)) {
            return false;
        }
        String this$certImages = this.getCertImages();
        String other$certImages = other.getCertImages();
        if (this$certImages == null ? other$certImages != null : !this$certImages.equals(other$certImages)) {
            return false;
        }
        String this$certification = this.getCertification();
        String other$certification = other.getCertification();
        if (this$certification == null ? other$certification != null : !this$certification.equals(other$certification)) {
            return false;
        }
        BigDecimal this$hourlyRate = this.getHourlyRate();
        BigDecimal other$hourlyRate = other.getHourlyRate();
        if (this$hourlyRate == null ? other$hourlyRate != null : !((Object)this$hourlyRate).equals(other$hourlyRate)) {
            return false;
        }
        BigDecimal this$ratingScore = this.getRatingScore();
        BigDecimal other$ratingScore = other.getRatingScore();
        return !(this$ratingScore == null ? other$ratingScore != null : !((Object)this$ratingScore).equals(other$ratingScore));
    }

    protected boolean canEqual(Object other) {
        return other instanceof InstructorUpdateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $gender = this.getGender();
        result = result * 59 + ($gender == null ? 43 : ((Object)$gender).hashCode());
        Integer $experienceYears = this.getExperienceYears();
        result = result * 59 + ($experienceYears == null ? 43 : ((Object)$experienceYears).hashCode());
        Integer $classCount = this.getClassCount();
        result = result * 59 + ($classCount == null ? 43 : ((Object)$classCount).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $phone = this.getPhone();
        result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
        String $specialty = this.getSpecialty();
        result = result * 59 + ($specialty == null ? 43 : $specialty.hashCode());
        String $introduction = this.getIntroduction();
        result = result * 59 + ($introduction == null ? 43 : $introduction.hashCode());
        String $idCard = this.getIdCard();
        result = result * 59 + ($idCard == null ? 43 : $idCard.hashCode());
        String $certImages = this.getCertImages();
        result = result * 59 + ($certImages == null ? 43 : $certImages.hashCode());
        String $certification = this.getCertification();
        result = result * 59 + ($certification == null ? 43 : $certification.hashCode());
        BigDecimal $hourlyRate = this.getHourlyRate();
        result = result * 59 + ($hourlyRate == null ? 43 : ((Object)$hourlyRate).hashCode());
        BigDecimal $ratingScore = this.getRatingScore();
        result = result * 59 + ($ratingScore == null ? 43 : ((Object)$ratingScore).hashCode());
        return result;
    }

    public String toString() {
        return "InstructorUpdateValidate(id=" + this.getId() + ", name=" + this.getName() + ", password=" + this.getPassword() + ", avatar=" + this.getAvatar() + ", phone=" + this.getPhone() + ", gender=" + this.getGender() + ", specialty=" + this.getSpecialty() + ", experienceYears=" + this.getExperienceYears() + ", introduction=" + this.getIntroduction() + ", idCard=" + this.getIdCard() + ", certImages=" + this.getCertImages() + ", certification=" + this.getCertification() + ", classCount=" + this.getClassCount() + ", hourlyRate=" + this.getHourlyRate() + ", ratingScore=" + this.getRatingScore() + ", status=" + this.getStatus() + ", sort=" + this.getSort() + ")";
    }
}

