/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.annotation.TableName
 */
package com.mdd.common.entity.agent;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

@TableName(value="la_agent")
public class Agent
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    private Integer id;
    private Integer adminId;
    private String name;
    private String mobile;
    private String areaPolygon;
    private BigDecimal commissionRate;
    private String bankName;
    private String bankCard;
    private Long createTime;
    private Long updateTime;
    private Long deleteTime;
    private BigDecimal balance;
    private BigDecimal totalEarnings;

    public Integer getId() {
        return this.id;
    }

    public Integer getAdminId() {
        return this.adminId;
    }

    public String getName() {
        return this.name;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getAreaPolygon() {
        return this.areaPolygon;
    }

    public BigDecimal getCommissionRate() {
        return this.commissionRate;
    }

    public String getBankName() {
        return this.bankName;
    }

    public String getBankCard() {
        return this.bankCard;
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

    public BigDecimal getBalance() {
        return this.balance;
    }

    public BigDecimal getTotalEarnings() {
        return this.totalEarnings;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAreaPolygon(String areaPolygon) {
        this.areaPolygon = areaPolygon;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
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

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setTotalEarnings(BigDecimal totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Agent)) {
            return false;
        }
        Agent other = (Agent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
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
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        if (this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile)) {
            return false;
        }
        String this$areaPolygon = this.getAreaPolygon();
        String other$areaPolygon = other.getAreaPolygon();
        if (this$areaPolygon == null ? other$areaPolygon != null : !this$areaPolygon.equals(other$areaPolygon)) {
            return false;
        }
        BigDecimal this$commissionRate = this.getCommissionRate();
        BigDecimal other$commissionRate = other.getCommissionRate();
        if (this$commissionRate == null ? other$commissionRate != null : !((Object)this$commissionRate).equals(other$commissionRate)) {
            return false;
        }
        String this$bankName = this.getBankName();
        String other$bankName = other.getBankName();
        if (this$bankName == null ? other$bankName != null : !this$bankName.equals(other$bankName)) {
            return false;
        }
        String this$bankCard = this.getBankCard();
        String other$bankCard = other.getBankCard();
        if (this$bankCard == null ? other$bankCard != null : !this$bankCard.equals(other$bankCard)) {
            return false;
        }
        BigDecimal this$balance = this.getBalance();
        BigDecimal other$balance = other.getBalance();
        if (this$balance == null ? other$balance != null : !((Object)this$balance).equals(other$balance)) {
            return false;
        }
        BigDecimal this$totalEarnings = this.getTotalEarnings();
        BigDecimal other$totalEarnings = other.getTotalEarnings();
        return !(this$totalEarnings == null ? other$totalEarnings != null : !((Object)this$totalEarnings).equals(other$totalEarnings));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Agent;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $adminId = this.getAdminId();
        result = result * 59 + ($adminId == null ? 43 : ((Object)$adminId).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        String $areaPolygon = this.getAreaPolygon();
        result = result * 59 + ($areaPolygon == null ? 43 : $areaPolygon.hashCode());
        BigDecimal $commissionRate = this.getCommissionRate();
        result = result * 59 + ($commissionRate == null ? 43 : ((Object)$commissionRate).hashCode());
        String $bankName = this.getBankName();
        result = result * 59 + ($bankName == null ? 43 : $bankName.hashCode());
        String $bankCard = this.getBankCard();
        result = result * 59 + ($bankCard == null ? 43 : $bankCard.hashCode());
        BigDecimal $balance = this.getBalance();
        result = result * 59 + ($balance == null ? 43 : ((Object)$balance).hashCode());
        BigDecimal $totalEarnings = this.getTotalEarnings();
        result = result * 59 + ($totalEarnings == null ? 43 : ((Object)$totalEarnings).hashCode());
        return result;
    }

    public String toString() {
        return "Agent(id=" + this.getId() + ", adminId=" + this.getAdminId() + ", name=" + this.getName() + ", mobile=" + this.getMobile() + ", areaPolygon=" + this.getAreaPolygon() + ", commissionRate=" + this.getCommissionRate() + ", bankName=" + this.getBankName() + ", bankCard=" + this.getBankCard() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ", balance=" + this.getBalance() + ", totalEarnings=" + this.getTotalEarnings() + ")";
    }
}

