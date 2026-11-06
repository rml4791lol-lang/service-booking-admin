/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value="\u6559\u7ec3\u5b9e\u4f53")
public class Instructor
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u5bf9\u5e94\u767b\u5f55\u8d26\u53f7ID(la_user.id)")
    private Integer userId;
    @ApiModelProperty(value="\u6559\u7ec3\u59d3\u540d")
    private String name;
    @ApiModelProperty(value="\u5934\u50cf")
    private String avatar;
    @ApiModelProperty(value="\u8054\u7cfb\u7535\u8bdd")
    private String phone;
    @ApiModelProperty(value="\u5bc6\u7801")
    private String password;
    @ApiModelProperty(value="\u6027\u522b:1=\u7537,2=\u5973")
    private Integer gender;
    @ApiModelProperty(value="\u4e13\u4e1a\u9886\u57df")
    private String specialty;
    @ApiModelProperty(value="\u4ece\u4e1a\u5e74\u9650")
    private Integer experienceYears;
    @ApiModelProperty(value="\u4e2a\u4eba\u7b80\u4ecb")
    private String introduction;
    @ApiModelProperty(value="\u8d44\u8d28\u8bc1\u4e66\u56fe\u7247")
    private String certImages;
    @ApiModelProperty(value="\u6559\u7ec3\u8d44\u8d28(\u5982\uff1a\u56fd\u5bb6\u4e00\u7ea7\u6559\u7ec3\u5458)")
    private String certification;
    @ApiModelProperty(value="\u53ef\u6388\u8bfe\u7a0b(\u8fd0\u52a8\u9879\u76ee\u5206\u7c7bID,\u9017\u53f7\u5206\u9694)")
    private String teachableCourses;
    @ApiModelProperty(value="\u670d\u52a1\u533a\u57df(\u793e\u533aID,\u9017\u53f7\u5206\u9694)")
    private String serviceAreas;
    @ApiModelProperty(value="\u8d1f\u8d23\u73ed\u7ea7\u6570")
    private Integer classCount;
    @ApiModelProperty(value="\u8bfe\u916c\uff08\u5143/\u8bfe\u65f6\uff09")
    private BigDecimal hourlyRate;
    @ApiModelProperty(value="\u8bc4\u5206\uff080-5\uff09")
    private BigDecimal ratingScore;
    @ApiModelProperty(value="\u5f53\u524d\u53ef\u63d0\u73b0\u4f59\u989d")
    private BigDecimal balance;
    @ApiModelProperty(value="\u7d2f\u8ba1\u5df2\u63d0\u73b0\u603b\u989d")
    private BigDecimal totalWithdrawn;
    @ApiModelProperty(value="\u72b6\u6001:0=\u505c\u7528,1=\u6b63\u5e38,2=\u5f85\u9a8c\u8bc1")
    private Integer status;
    @ApiModelProperty(value="\u8eab\u4efd\u8bc1\u53f7\u7801")
    private String idCard;
    @ApiModelProperty(value="\u6392\u5e8f")
    private Integer sort;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Date createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Date updateTime;
    @ApiModelProperty(value="\u5220\u9664\u65f6\u95f4")
    private Date deleteTime;
    @ApiModelProperty(value="\u9996\u6b21\u767b\u5f55\u662f\u5426\u5fc5\u987b\u4fee\u6539\u521d\u59cb\u5bc6\u7801:0=\u5426,1=\u662f")
    private Integer mustChangePassword;
    @ApiModelProperty(value="\u6559\u7ec3\u8d26\u53f7\u5f00\u901a\u65f6\u95f4")
    private Date accountOpenedTime;
    @ApiModelProperty(value="\u767b\u5f55\u5bc6\u7801\u6700\u540e\u66f4\u65b0\u65f6\u95f4")
    private Date passwordUpdatedTime;
    @ApiModelProperty(value="\u6559\u7ec3\u7aef\u6700\u540e\u767b\u5f55\u65f6\u95f4")
    private Date lastLoginTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getPassword() {
        return this.password;
    }

    public Integer getGender() {
        return this.gender;
    }

    public String getSpecialty() {
        return this.specialty;
    }

    public Integer getExperienceYears() {
        return this.experienceYears;
    }

    public String getIntroduction() {
        return this.introduction;
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

    public BigDecimal getBalance() {
        return this.balance;
    }

    public BigDecimal getTotalWithdrawn() {
        return this.totalWithdrawn;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getIdCard() {
        return this.idCard;
    }

    public Integer getSort() {
        return this.sort;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public Date getDeleteTime() {
        return this.deleteTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setTotalWithdrawn(BigDecimal totalWithdrawn) {
        this.totalWithdrawn = totalWithdrawn;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Integer getMustChangePassword() {
        return this.mustChangePassword;
    }

    public Date getAccountOpenedTime() {
        return this.accountOpenedTime;
    }

    public Date getPasswordUpdatedTime() {
        return this.passwordUpdatedTime;
    }

    public Date getLastLoginTime() {
        return this.lastLoginTime;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setMustChangePassword(Integer mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    public void setAccountOpenedTime(Date accountOpenedTime) {
        this.accountOpenedTime = accountOpenedTime;
    }

    public void setPasswordUpdatedTime(Date passwordUpdatedTime) {
        this.passwordUpdatedTime = passwordUpdatedTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Instructor)) {
            return false;
        }
        Instructor other = (Instructor)o;
        if (!other.canEqual(this)) {
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
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
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
        if (this$ratingScore == null ? other$ratingScore != null : !((Object)this$ratingScore).equals(other$ratingScore)) {
            return false;
        }
        BigDecimal this$balance = this.getBalance();
        BigDecimal other$balance = other.getBalance();
        if (this$balance == null ? other$balance != null : !((Object)this$balance).equals(other$balance)) {
            return false;
        }
        BigDecimal this$totalWithdrawn = this.getTotalWithdrawn();
        BigDecimal other$totalWithdrawn = other.getTotalWithdrawn();
        if (this$totalWithdrawn == null ? other$totalWithdrawn != null : !((Object)this$totalWithdrawn).equals(other$totalWithdrawn)) {
            return false;
        }
        String this$idCard = this.getIdCard();
        String other$idCard = other.getIdCard();
        if (this$idCard == null ? other$idCard != null : !this$idCard.equals(other$idCard)) {
            return false;
        }
        Date this$createTime = this.getCreateTime();
        Date other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Date this$updateTime = this.getUpdateTime();
        Date other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        Date this$deleteTime = this.getDeleteTime();
        Date other$deleteTime = other.getDeleteTime();
        return !(this$deleteTime == null ? other$deleteTime != null : !((Object)this$deleteTime).equals(other$deleteTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Instructor;
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
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $phone = this.getPhone();
        result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $specialty = this.getSpecialty();
        result = result * 59 + ($specialty == null ? 43 : $specialty.hashCode());
        String $introduction = this.getIntroduction();
        result = result * 59 + ($introduction == null ? 43 : $introduction.hashCode());
        String $certImages = this.getCertImages();
        result = result * 59 + ($certImages == null ? 43 : $certImages.hashCode());
        String $certification = this.getCertification();
        result = result * 59 + ($certification == null ? 43 : $certification.hashCode());
        BigDecimal $hourlyRate = this.getHourlyRate();
        result = result * 59 + ($hourlyRate == null ? 43 : ((Object)$hourlyRate).hashCode());
        BigDecimal $ratingScore = this.getRatingScore();
        result = result * 59 + ($ratingScore == null ? 43 : ((Object)$ratingScore).hashCode());
        BigDecimal $balance = this.getBalance();
        result = result * 59 + ($balance == null ? 43 : ((Object)$balance).hashCode());
        BigDecimal $totalWithdrawn = this.getTotalWithdrawn();
        result = result * 59 + ($totalWithdrawn == null ? 43 : ((Object)$totalWithdrawn).hashCode());
        String $idCard = this.getIdCard();
        result = result * 59 + ($idCard == null ? 43 : $idCard.hashCode());
        Date $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Date $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Date $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        return result;
    }

    public String toString() {
        return "Instructor(id=" + this.getId() + ", name=" + this.getName() + ", avatar=" + this.getAvatar() + ", phone=" + this.getPhone() + ", password=" + this.getPassword() + ", gender=" + this.getGender() + ", specialty=" + this.getSpecialty() + ", experienceYears=" + this.getExperienceYears() + ", introduction=" + this.getIntroduction() + ", certImages=" + this.getCertImages() + ", certification=" + this.getCertification() + ", classCount=" + this.getClassCount() + ", hourlyRate=" + this.getHourlyRate() + ", ratingScore=" + this.getRatingScore() + ", balance=" + this.getBalance() + ", totalWithdrawn=" + this.getTotalWithdrawn() + ", status=" + this.getStatus() + ", idCard=" + this.getIdCard() + ", sort=" + this.getSort() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ")";
    }
}

