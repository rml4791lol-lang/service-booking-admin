/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.community.CommunityEarningLogVo
 */
package com.mdd.admin.vo.community;

import java.io.Serializable;
import java.math.BigDecimal;

public class CommunityEarningLogVo
implements Serializable {
    private Integer id;
    private Integer courseClassId;
    private String courseName;
    private BigDecimal orderAmount;
    private BigDecimal commissionRate;
    private BigDecimal earningAmount;
    private String createTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getCourseClassId() {
        return this.courseClassId;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public BigDecimal getOrderAmount() {
        return this.orderAmount;
    }

    public BigDecimal getCommissionRate() {
        return this.commissionRate;
    }

    public BigDecimal getEarningAmount() {
        return this.earningAmount;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCourseClassId(Integer courseClassId) {
        this.courseClassId = courseClassId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public void setEarningAmount(BigDecimal earningAmount) {
        this.earningAmount = earningAmount;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommunityEarningLogVo)) {
            return false;
        }
        CommunityEarningLogVo other = (CommunityEarningLogVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$courseClassId = this.getCourseClassId();
        Integer other$courseClassId = other.getCourseClassId();
        if (this$courseClassId == null ? other$courseClassId != null : !((Object)this$courseClassId).equals(other$courseClassId)) {
            return false;
        }
        String this$courseName = this.getCourseName();
        String other$courseName = other.getCourseName();
        if (this$courseName == null ? other$courseName != null : !this$courseName.equals(other$courseName)) {
            return false;
        }
        BigDecimal this$orderAmount = this.getOrderAmount();
        BigDecimal other$orderAmount = other.getOrderAmount();
        if (this$orderAmount == null ? other$orderAmount != null : !((Object)this$orderAmount).equals(other$orderAmount)) {
            return false;
        }
        BigDecimal this$commissionRate = this.getCommissionRate();
        BigDecimal other$commissionRate = other.getCommissionRate();
        if (this$commissionRate == null ? other$commissionRate != null : !((Object)this$commissionRate).equals(other$commissionRate)) {
            return false;
        }
        BigDecimal this$earningAmount = this.getEarningAmount();
        BigDecimal other$earningAmount = other.getEarningAmount();
        if (this$earningAmount == null ? other$earningAmount != null : !((Object)this$earningAmount).equals(other$earningAmount)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CommunityEarningLogVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $courseClassId = this.getCourseClassId();
        result = result * 59 + ($courseClassId == null ? 43 : ((Object)$courseClassId).hashCode());
        String $courseName = this.getCourseName();
        result = result * 59 + ($courseName == null ? 43 : $courseName.hashCode());
        BigDecimal $orderAmount = this.getOrderAmount();
        result = result * 59 + ($orderAmount == null ? 43 : ((Object)$orderAmount).hashCode());
        BigDecimal $commissionRate = this.getCommissionRate();
        result = result * 59 + ($commissionRate == null ? 43 : ((Object)$commissionRate).hashCode());
        BigDecimal $earningAmount = this.getEarningAmount();
        result = result * 59 + ($earningAmount == null ? 43 : ((Object)$earningAmount).hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        return result;
    }

    public String toString() {
        return "CommunityEarningLogVo(id=" + this.getId() + ", courseClassId=" + this.getCourseClassId() + ", courseName=" + this.getCourseName() + ", orderAmount=" + this.getOrderAmount() + ", commissionRate=" + this.getCommissionRate() + ", earningAmount=" + this.getEarningAmount() + ", createTime=" + this.getCreateTime() + ")";
    }
}

