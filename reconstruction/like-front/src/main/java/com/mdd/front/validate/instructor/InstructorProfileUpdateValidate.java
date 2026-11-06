/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.instructor.InstructorProfileUpdateValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.NotNull
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.front.validate.instructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u6559\u7ec3\u8d44\u6599\u66f4\u65b0\u53c2\u6570")
public class InstructorProfileUpdateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank(message="\u6559\u7ec3\u59d3\u540d\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(max=50, message="\u6559\u7ec3\u59d3\u540d\u4e0d\u80fd\u8d85\u8fc750\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u6559\u7ec3\u59d3\u540d")
    private @NotBlank(message="\u6559\u7ec3\u59d3\u540d\u4e0d\u80fd\u4e3a\u7a7a") @Length(max=50, message="\u6559\u7ec3\u59d3\u540d\u4e0d\u80fd\u8d85\u8fc750\u4e2a\u5b57\u7b26") String name;
    @ApiModelProperty(value="\u5934\u50cf")
    private String avatar;
    @NotNull(message="\u6027\u522b\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u6027\u522b:1=\u7537,2=\u5973")
    private @NotNull(message="\u6027\u522b\u4e0d\u80fd\u4e3a\u7a7a") Integer gender;
    @NotBlank(message="\u4e13\u4e1a\u9886\u57df\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(max=100, message="\u4e13\u4e1a\u9886\u57df\u4e0d\u80fd\u8d85\u8fc7100\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u4e13\u4e1a\u9886\u57df")
    private @NotBlank(message="\u4e13\u4e1a\u9886\u57df\u4e0d\u80fd\u4e3a\u7a7a") @Length(max=100, message="\u4e13\u4e1a\u9886\u57df\u4e0d\u80fd\u8d85\u8fc7100\u4e2a\u5b57\u7b26") String specialty;
    @NotNull(message="\u4ece\u4e1a\u5e74\u9650\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u4ece\u4e1a\u5e74\u9650")
    private @NotNull(message="\u4ece\u4e1a\u5e74\u9650\u4e0d\u80fd\u4e3a\u7a7a") Integer experienceYears;
    @Length(max=500, message="\u4e2a\u4eba\u7b80\u4ecb\u4e0d\u80fd\u8d85\u8fc7500\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u4e2a\u4eba\u7b80\u4ecb")
    private @Length(max=500, message="\u4e2a\u4eba\u7b80\u4ecb\u4e0d\u80fd\u8d85\u8fc7500\u4e2a\u5b57\u7b26") String introduction;
    @Length(max=200, message="\u6559\u7ec3\u8d44\u8d28\u4e0d\u80fd\u8d85\u8fc7200\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u6559\u7ec3\u8d44\u8d28(\u591a\u4e2a\u9017\u53f7\u5206\u9694)")
    private @Length(max=200, message="\u6559\u7ec3\u8d44\u8d28\u4e0d\u80fd\u8d85\u8fc7200\u4e2a\u5b57\u7b26") String certification;
    @ApiModelProperty(value="\u8d44\u8d28\u8bc1\u4e66\u56fe\u7247(\u9017\u53f7\u5206\u9694)")
    private String certImages;
    @Length(max=18, message="\u8eab\u4efd\u8bc1\u53f7\u683c\u5f0f\u4e0d\u6b63\u786e")
    @ApiModelProperty(value="\u8eab\u4efd\u8bc1\u53f7\u7801")
    private @Length(max=18, message="\u8eab\u4efd\u8bc1\u53f7\u683c\u5f0f\u4e0d\u6b63\u786e") String idCard;

    public String getName() {
        return this.name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    @NotNull(message="\u6027\u522b\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u6027\u522b\u4e0d\u80fd\u4e3a\u7a7a") Integer getGender() {
        return this.gender;
    }

    public String getSpecialty() {
        return this.specialty;
    }

    @NotNull(message="\u4ece\u4e1a\u5e74\u9650\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u4ece\u4e1a\u5e74\u9650\u4e0d\u80fd\u4e3a\u7a7a") Integer getExperienceYears() {
        return this.experienceYears;
    }

    public String getIntroduction() {
        return this.introduction;
    }

    public String getCertification() {
        return this.certification;
    }

    public String getCertImages() {
        return this.certImages;
    }

    public String getIdCard() {
        return this.idCard;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setGender(@NotNull(message="\u6027\u522b\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u6027\u522b\u4e0d\u80fd\u4e3a\u7a7a") Integer gender) {
        this.gender = gender;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setExperienceYears(@NotNull(message="\u4ece\u4e1a\u5e74\u9650\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u4ece\u4e1a\u5e74\u9650\u4e0d\u80fd\u4e3a\u7a7a") Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public void setCertImages(String certImages) {
        this.certImages = certImages;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InstructorProfileUpdateValidate)) {
            return false;
        }
        InstructorProfileUpdateValidate other = (InstructorProfileUpdateValidate)o;
        if (!other.canEqual((Object)this)) {
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
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
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
        String this$certification = this.getCertification();
        String other$certification = other.getCertification();
        if (this$certification == null ? other$certification != null : !this$certification.equals(other$certification)) {
            return false;
        }
        String this$certImages = this.getCertImages();
        String other$certImages = other.getCertImages();
        if (this$certImages == null ? other$certImages != null : !this$certImages.equals(other$certImages)) {
            return false;
        }
        String this$idCard = this.getIdCard();
        String other$idCard = other.getIdCard();
        return !(this$idCard == null ? other$idCard != null : !this$idCard.equals(other$idCard));
    }

    protected boolean canEqual(Object other) {
        return other instanceof InstructorProfileUpdateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $gender = this.getGender();
        result = result * 59 + ($gender == null ? 43 : ((Object)$gender).hashCode());
        Integer $experienceYears = this.getExperienceYears();
        result = result * 59 + ($experienceYears == null ? 43 : ((Object)$experienceYears).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $specialty = this.getSpecialty();
        result = result * 59 + ($specialty == null ? 43 : $specialty.hashCode());
        String $introduction = this.getIntroduction();
        result = result * 59 + ($introduction == null ? 43 : $introduction.hashCode());
        String $certification = this.getCertification();
        result = result * 59 + ($certification == null ? 43 : $certification.hashCode());
        String $certImages = this.getCertImages();
        result = result * 59 + ($certImages == null ? 43 : $certImages.hashCode());
        String $idCard = this.getIdCard();
        result = result * 59 + ($idCard == null ? 43 : $idCard.hashCode());
        return result;
    }

    public String toString() {
        return "InstructorProfileUpdateValidate(name=" + this.getName() + ", avatar=" + this.getAvatar() + ", gender=" + this.getGender() + ", specialty=" + this.getSpecialty() + ", experienceYears=" + this.getExperienceYears() + ", introduction=" + this.getIntroduction() + ", certification=" + this.getCertification() + ", certImages=" + this.getCertImages() + ", idCard=" + this.getIdCard() + ")";
    }
}

