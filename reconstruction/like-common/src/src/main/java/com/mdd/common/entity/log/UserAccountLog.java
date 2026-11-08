/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.log;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;

@ApiModel(value="\u8d26\u6237\u53d8\u52a8\u5b9e\u4f53")
public class UserAccountLog {
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="\u4e3b\u952e")
    private Integer id;
    @ApiModelProperty(value="\u6d41\u6c34\u53f7")
    private String sn;
    @ApiModelProperty(value="\u7528\u6237ID")
    private Integer userId;
    @ApiModelProperty(value="\u53d8\u52a8\u5bf9\u8c61")
    private Integer changeObject;
    @ApiModelProperty(value="\u5173\u8054\u5355\u53f7")
    private String sourceSn;
    @ApiModelProperty(value="\u53d8\u52a8\u7c7b\u578b")
    private Integer changeType;
    @ApiModelProperty(value="\u53d8\u52a8\u6570\u91cf")
    private BigDecimal changeAmount;
    @ApiModelProperty(value="\u53d8\u52a8\u540e\u6570\u91cf")
    private BigDecimal leftAmount;
    @ApiModelProperty(value="\u53d8\u52a8\u7c7b\u578b: [1=\u589e\u52a0, 2=\u51cf\u5c11]")
    private Integer action;
    @ApiModelProperty(value="\u5907\u6ce8\u4fe1\u606f")
    private String remark;
    @ApiModelProperty(value="\u9884\u7559\u5b57\u6bb5")
    private String extra;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Long updateTime;
    @ApiModelProperty(value="\u5220\u9664\u65f6\u95f4")
    private Long deleteTime;

    public Integer getId() {
        return this.id;
    }

    public String getSn() {
        return this.sn;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getChangeObject() {
        return this.changeObject;
    }

    public String getSourceSn() {
        return this.sourceSn;
    }

    public Integer getChangeType() {
        return this.changeType;
    }

    public BigDecimal getChangeAmount() {
        return this.changeAmount;
    }

    public BigDecimal getLeftAmount() {
        return this.leftAmount;
    }

    public Integer getAction() {
        return this.action;
    }

    public String getRemark() {
        return this.remark;
    }

    public String getExtra() {
        return this.extra;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public Long getDeleteTime() {
        return this.deleteTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setChangeObject(Integer changeObject) {
        this.changeObject = changeObject;
    }

    public void setSourceSn(String sourceSn) {
        this.sourceSn = sourceSn;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public void setLeftAmount(BigDecimal leftAmount) {
        this.leftAmount = leftAmount;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleteTime(Long deleteTime) {
        this.deleteTime = deleteTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserAccountLog)) {
            return false;
        }
        UserAccountLog other = (UserAccountLog)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$changeObject = this.getChangeObject();
        Integer other$changeObject = other.getChangeObject();
        if (this$changeObject == null ? other$changeObject != null : !((Object)this$changeObject).equals(other$changeObject)) {
            return false;
        }
        Integer this$changeType = this.getChangeType();
        Integer other$changeType = other.getChangeType();
        if (this$changeType == null ? other$changeType != null : !((Object)this$changeType).equals(other$changeType)) {
            return false;
        }
        Integer this$action = this.getAction();
        Integer other$action = other.getAction();
        if (this$action == null ? other$action != null : !((Object)this$action).equals(other$action)) {
            return false;
        }
        Long this$createTime = this.getCreateTime();
        Long other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Long this$updateTime = this.getUpdateTime();
        Long other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        Long this$deleteTime = this.getDeleteTime();
        Long other$deleteTime = other.getDeleteTime();
        if (this$deleteTime == null ? other$deleteTime != null : !((Object)this$deleteTime).equals(other$deleteTime)) {
            return false;
        }
        String this$sn = this.getSn();
        String other$sn = other.getSn();
        if (this$sn == null ? other$sn != null : !this$sn.equals(other$sn)) {
            return false;
        }
        String this$sourceSn = this.getSourceSn();
        String other$sourceSn = other.getSourceSn();
        if (this$sourceSn == null ? other$sourceSn != null : !this$sourceSn.equals(other$sourceSn)) {
            return false;
        }
        BigDecimal this$changeAmount = this.getChangeAmount();
        BigDecimal other$changeAmount = other.getChangeAmount();
        if (this$changeAmount == null ? other$changeAmount != null : !((Object)this$changeAmount).equals(other$changeAmount)) {
            return false;
        }
        BigDecimal this$leftAmount = this.getLeftAmount();
        BigDecimal other$leftAmount = other.getLeftAmount();
        if (this$leftAmount == null ? other$leftAmount != null : !((Object)this$leftAmount).equals(other$leftAmount)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        if (this$remark == null ? other$remark != null : !this$remark.equals(other$remark)) {
            return false;
        }
        String this$extra = this.getExtra();
        String other$extra = other.getExtra();
        return !(this$extra == null ? other$extra != null : !this$extra.equals(other$extra));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserAccountLog;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $changeObject = this.getChangeObject();
        result = result * 59 + ($changeObject == null ? 43 : ((Object)$changeObject).hashCode());
        Integer $changeType = this.getChangeType();
        result = result * 59 + ($changeType == null ? 43 : ((Object)$changeType).hashCode());
        Integer $action = this.getAction();
        result = result * 59 + ($action == null ? 43 : ((Object)$action).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        String $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : $sn.hashCode());
        String $sourceSn = this.getSourceSn();
        result = result * 59 + ($sourceSn == null ? 43 : $sourceSn.hashCode());
        BigDecimal $changeAmount = this.getChangeAmount();
        result = result * 59 + ($changeAmount == null ? 43 : ((Object)$changeAmount).hashCode());
        BigDecimal $leftAmount = this.getLeftAmount();
        result = result * 59 + ($leftAmount == null ? 43 : ((Object)$leftAmount).hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        String $extra = this.getExtra();
        result = result * 59 + ($extra == null ? 43 : $extra.hashCode());
        return result;
    }

    public String toString() {
        return "UserAccountLog(id=" + this.getId() + ", sn=" + this.getSn() + ", userId=" + this.getUserId() + ", changeObject=" + this.getChangeObject() + ", sourceSn=" + this.getSourceSn() + ", changeType=" + this.getChangeType() + ", changeAmount=" + this.getChangeAmount() + ", leftAmount=" + this.getLeftAmount() + ", action=" + this.getAction() + ", remark=" + this.getRemark() + ", extra=" + this.getExtra() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ")";
    }
}

