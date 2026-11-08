/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.agent.AgentListedVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.agent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u4ee3\u7406\u5217\u8868Vo")
public class AgentListedVo
implements Serializable {
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u4ee3\u7406\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u540e\u53f0\u7ba1\u7406\u5458ID")
    private Integer adminId;
    @ApiModelProperty(value="\u8054\u7cfb\u7535\u8bdd")
    private String mobile;
    @ApiModelProperty(value="\u5206\u6210\u6bd4\u4f8b")
    private BigDecimal commissionRate;
    @ApiModelProperty(value="\u63d0\u73b0\u94f6\u884c\u540d\u79f0")
    private String bankName;
    @ApiModelProperty(value="\u63d0\u73b0\u94f6\u884c\u5361\u53f7")
    private String bankCard;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getAdminId() {
        return this.adminId;
    }

    public String getMobile() {
        return this.mobile;
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

    public String getCreateTime() {
        return this.createTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AgentListedVo)) {
            return false;
        }
        AgentListedVo other = (AgentListedVo)o;
        if (!other.canEqual((Object)this)) {
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
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AgentListedVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $adminId = this.getAdminId();
        result = result * 59 + ($adminId == null ? 43 : ((Object)$adminId).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        BigDecimal $commissionRate = this.getCommissionRate();
        result = result * 59 + ($commissionRate == null ? 43 : ((Object)$commissionRate).hashCode());
        String $bankName = this.getBankName();
        result = result * 59 + ($bankName == null ? 43 : $bankName.hashCode());
        String $bankCard = this.getBankCard();
        result = result * 59 + ($bankCard == null ? 43 : $bankCard.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        return result;
    }

    public String toString() {
        return "AgentListedVo(id=" + this.getId() + ", name=" + this.getName() + ", adminId=" + this.getAdminId() + ", mobile=" + this.getMobile() + ", commissionRate=" + this.getCommissionRate() + ", bankName=" + this.getBankName() + ", bankCard=" + this.getBankCard() + ", createTime=" + this.getCreateTime() + ")";
    }
}

