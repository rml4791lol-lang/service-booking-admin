/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.annotation.TableName
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@TableName(value="la_instructor_application")
@ApiModel(value="\u6559\u7ec3\u7533\u8bf7\u5b9e\u4f53")
public class InstructorApplication
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Long id;
    @ApiModelProperty(value="\u7533\u8bf7\u4eba\u7528\u6237ID")
    private Integer userId;
    @ApiModelProperty(value="\u8eab\u4efd\u8bc1\u53f7")
    private String idCard;
    @ApiModelProperty(value="\u8d44\u8d28\u8bc1\u4e66\u56fe\u7247(\u591a\u5f20\u9017\u53f7\u5206\u9694)")
    private String certImages;
    @ApiModelProperty(value="\u6559\u5b66\u7ecf\u9a8c\u63cf\u8ff0")
    private String experience;
    @ApiModelProperty(value="\u7533\u8bf7\u72b6\u6001:0=\u5f85\u5ba1\u6838,1=\u5df2\u901a\u8fc7,2=\u5df2\u62d2\u7edd")
    private Integer applyStatus;
    @ApiModelProperty(value="\u5ba1\u6838\u5907\u6ce8")
    private String auditRemark;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Integer createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Integer updateTime;

    public Long getId() {
        return this.id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public String getIdCard() {
        return this.idCard;
    }

    public String getCertImages() {
        return this.certImages;
    }

    public String getExperience() {
        return this.experience;
    }

    public Integer getApplyStatus() {
        return this.applyStatus;
    }

    public String getAuditRemark() {
        return this.auditRemark;
    }

    public Integer getCreateTime() {
        return this.createTime;
    }

    public Integer getUpdateTime() {
        return this.updateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setCertImages(String certImages) {
        this.certImages = certImages;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
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
        if (!(o instanceof InstructorApplication)) {
            return false;
        }
        InstructorApplication other = (InstructorApplication)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$applyStatus = this.getApplyStatus();
        Integer other$applyStatus = other.getApplyStatus();
        if (this$applyStatus == null ? other$applyStatus != null : !((Object)this$applyStatus).equals(other$applyStatus)) {
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
        String this$experience = this.getExperience();
        String other$experience = other.getExperience();
        if (this$experience == null ? other$experience != null : !this$experience.equals(other$experience)) {
            return false;
        }
        String this$auditRemark = this.getAuditRemark();
        String other$auditRemark = other.getAuditRemark();
        return !(this$auditRemark == null ? other$auditRemark != null : !this$auditRemark.equals(other$auditRemark));
    }

    protected boolean canEqual(Object other) {
        return other instanceof InstructorApplication;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $applyStatus = this.getApplyStatus();
        result = result * 59 + ($applyStatus == null ? 43 : ((Object)$applyStatus).hashCode());
        Integer $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Integer $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $idCard = this.getIdCard();
        result = result * 59 + ($idCard == null ? 43 : $idCard.hashCode());
        String $certImages = this.getCertImages();
        result = result * 59 + ($certImages == null ? 43 : $certImages.hashCode());
        String $experience = this.getExperience();
        result = result * 59 + ($experience == null ? 43 : $experience.hashCode());
        String $auditRemark = this.getAuditRemark();
        result = result * 59 + ($auditRemark == null ? 43 : $auditRemark.hashCode());
        return result;
    }

    public String toString() {
        return "InstructorApplication(id=" + this.getId() + ", userId=" + this.getUserId() + ", idCard=" + this.getIdCard() + ", certImages=" + this.getCertImages() + ", experience=" + this.getExperience() + ", applyStatus=" + this.getApplyStatus() + ", auditRemark=" + this.getAuditRemark() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

