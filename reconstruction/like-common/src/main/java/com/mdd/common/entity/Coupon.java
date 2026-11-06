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

@ApiModel(value="\u4f18\u60e0\u5238\u5b9e\u4f53")
public class Coupon
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u5173\u8054\u6a21\u677fID(0=\u624b\u52a8\u521b\u5efa)")
    private Integer templateId;
    @ApiModelProperty(value="\u5238\u7f16\u53f7")
    private String sn;
    @ApiModelProperty(value="\u5238\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u62b5\u6263\u91d1\u989d")
    private BigDecimal amount;
    @ApiModelProperty(value="\u6700\u4f4e\u6d88\u8d39\u91d1\u989d(0=\u65e0\u95e8\u69db)")
    private BigDecimal minAmount;
    @ApiModelProperty(value="\u7ed1\u5b9a\u7528\u6237ID")
    private Integer userId;
    @ApiModelProperty(value="\u7ed1\u5b9a\u624b\u673a\u53f7")
    private String mobile;
    @ApiModelProperty(value="\u72b6\u6001:0=\u672a\u4f7f\u7528,1=\u5df2\u4f7f\u7528,2=\u5df2\u8fc7\u671f")
    private Integer status;
    @ApiModelProperty(value="\u4f7f\u7528\u65f6\u7684\u8ba2\u5355ID")
    private Integer usedOrderId;
    @ApiModelProperty(value="\u4f7f\u7528\u65f6\u95f4")
    private Long usedTime;
    @ApiModelProperty(value="\u751f\u6548\u65f6\u95f4")
    private Long startTime;
    @ApiModelProperty(value="\u8fc7\u671f\u65f6\u95f4")
    private Long expireTime;
    @ApiModelProperty(value="\u53d1\u5238\u7ba1\u7406\u5458ID")
    private Integer adminId;
    @ApiModelProperty(value="\u5907\u6ce8")
    private String remark;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Long updateTime;
    @ApiModelProperty(value="\u5220\u9664\u65f6\u95f4")
    private Long deleteTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getTemplateId() {
        return this.templateId;
    }

    public String getSn() {
        return this.sn;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public BigDecimal getMinAmount() {
        return this.minAmount;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Integer getUsedOrderId() {
        return this.usedOrderId;
    }

    public Long getUsedTime() {
        return this.usedTime;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public Long getExpireTime() {
        return this.expireTime;
    }

    public Integer getAdminId() {
        return this.adminId;
    }

    public String getRemark() {
        return this.remark;
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

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setUsedOrderId(Integer usedOrderId) {
        this.usedOrderId = usedOrderId;
    }

    public void setUsedTime(Long usedTime) {
        this.usedTime = usedTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        if (!(o instanceof Coupon)) {
            return false;
        }
        Coupon other = (Coupon)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$templateId = this.getTemplateId();
        Integer other$templateId = other.getTemplateId();
        if (this$templateId == null ? other$templateId != null : !((Object)this$templateId).equals(other$templateId)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Integer this$usedOrderId = this.getUsedOrderId();
        Integer other$usedOrderId = other.getUsedOrderId();
        if (this$usedOrderId == null ? other$usedOrderId != null : !((Object)this$usedOrderId).equals(other$usedOrderId)) {
            return false;
        }
        Long this$usedTime = this.getUsedTime();
        Long other$usedTime = other.getUsedTime();
        if (this$usedTime == null ? other$usedTime != null : !((Object)this$usedTime).equals(other$usedTime)) {
            return false;
        }
        Long this$startTime = this.getStartTime();
        Long other$startTime = other.getStartTime();
        if (this$startTime == null ? other$startTime != null : !((Object)this$startTime).equals(other$startTime)) {
            return false;
        }
        Long this$expireTime = this.getExpireTime();
        Long other$expireTime = other.getExpireTime();
        if (this$expireTime == null ? other$expireTime != null : !((Object)this$expireTime).equals(other$expireTime)) {
            return false;
        }
        Integer this$adminId = this.getAdminId();
        Integer other$adminId = other.getAdminId();
        if (this$adminId == null ? other$adminId != null : !((Object)this$adminId).equals(other$adminId)) {
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
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        BigDecimal this$amount = this.getAmount();
        BigDecimal other$amount = other.getAmount();
        if (this$amount == null ? other$amount != null : !((Object)this$amount).equals(other$amount)) {
            return false;
        }
        BigDecimal this$minAmount = this.getMinAmount();
        BigDecimal other$minAmount = other.getMinAmount();
        if (this$minAmount == null ? other$minAmount != null : !((Object)this$minAmount).equals(other$minAmount)) {
            return false;
        }
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        if (this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        return !(this$remark == null ? other$remark != null : !this$remark.equals(other$remark));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Coupon;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $templateId = this.getTemplateId();
        result = result * 59 + ($templateId == null ? 43 : ((Object)$templateId).hashCode());
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $usedOrderId = this.getUsedOrderId();
        result = result * 59 + ($usedOrderId == null ? 43 : ((Object)$usedOrderId).hashCode());
        Long $usedTime = this.getUsedTime();
        result = result * 59 + ($usedTime == null ? 43 : ((Object)$usedTime).hashCode());
        Long $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : ((Object)$startTime).hashCode());
        Long $expireTime = this.getExpireTime();
        result = result * 59 + ($expireTime == null ? 43 : ((Object)$expireTime).hashCode());
        Integer $adminId = this.getAdminId();
        result = result * 59 + ($adminId == null ? 43 : ((Object)$adminId).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        String $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : $sn.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        BigDecimal $amount = this.getAmount();
        result = result * 59 + ($amount == null ? 43 : ((Object)$amount).hashCode());
        BigDecimal $minAmount = this.getMinAmount();
        result = result * 59 + ($minAmount == null ? 43 : ((Object)$minAmount).hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        return result;
    }

    public String toString() {
        return "Coupon(id=" + this.getId() + ", templateId=" + this.getTemplateId() + ", sn=" + this.getSn() + ", name=" + this.getName() + ", amount=" + this.getAmount() + ", minAmount=" + this.getMinAmount() + ", userId=" + this.getUserId() + ", mobile=" + this.getMobile() + ", status=" + this.getStatus() + ", usedOrderId=" + this.getUsedOrderId() + ", usedTime=" + this.getUsedTime() + ", startTime=" + this.getStartTime() + ", expireTime=" + this.getExpireTime() + ", adminId=" + this.getAdminId() + ", remark=" + this.getRemark() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ")";
    }
}

