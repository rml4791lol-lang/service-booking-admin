/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.agent.AgentUpdateValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.DecimalMin
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.admin.validate.agent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u4ee3\u7406\u66f4\u65b0\u53c2\u6570")
public class AgentUpdateValidate
implements Serializable {
    @NotNull(message="id\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="id", required=true)
    private @NotNull(message="id\u4e0d\u80fd\u4e3a\u7a7a") Integer id;
    @NotEmpty(message="\u4ee3\u7406\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u4ee3\u7406\u540d\u79f0", required=true)
    private @NotEmpty(message="\u4ee3\u7406\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a") String name;
    @NotEmpty(message="\u8054\u7cfb\u7535\u8bdd\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u8054\u7cfb\u7535\u8bdd", required=true)
    private @NotEmpty(message="\u8054\u7cfb\u7535\u8bdd\u4e0d\u80fd\u4e3a\u7a7a") String mobile;
    @ApiModelProperty(value="\u4ee3\u7406\u533a\u57df(JSON)")
    private String areaPolygon;
    @NotNull(message="\u5206\u6210\u6bd4\u4f8b\u4e0d\u80fd\u4e3a\u7a7a")
    @DecimalMin(value="0.0", inclusive=true, message="\u5206\u6210\u6bd4\u4f8b\u4e0d\u80fd\u5c0f\u4e8e0")
    @ApiModelProperty(value="\u5206\u6210\u6bd4\u4f8b", required=true)
    private @NotNull(message="\u5206\u6210\u6bd4\u4f8b\u4e0d\u80fd\u4e3a\u7a7a") @DecimalMin(value="0.0", inclusive=true, message="\u5206\u6210\u6bd4\u4f8b\u4e0d\u80fd\u5c0f\u4e8e0") BigDecimal commissionRate;
    @ApiModelProperty(value="\u767b\u5f55\u5bc6\u7801")
    private String password;
    @ApiModelProperty(value="\u63d0\u73b0\u94f6\u884c\u540d\u79f0")
    private String bankName;
    @ApiModelProperty(value="\u63d0\u73b0\u94f6\u884c\u5361\u53f7")
    private String bankCard;

    @NotNull(message="id\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="id\u4e0d\u80fd\u4e3a\u7a7a") Integer getId() {
        return this.id;
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

    @NotNull(message="\u5206\u6210\u6bd4\u4f8b\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u5206\u6210\u6bd4\u4f8b\u4e0d\u80fd\u4e3a\u7a7a") BigDecimal getCommissionRate() {
        return this.commissionRate;
    }

    public String getPassword() {
        return this.password;
    }

    public String getBankName() {
        return this.bankName;
    }

    public String getBankCard() {
        return this.bankCard;
    }

    public void setId(@NotNull(message="id\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="id\u4e0d\u80fd\u4e3a\u7a7a") Integer id) {
        this.id = id;
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

    public void setCommissionRate(@NotNull(message="\u5206\u6210\u6bd4\u4f8b\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u5206\u6210\u6bd4\u4f8b\u4e0d\u80fd\u4e3a\u7a7a") BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AgentUpdateValidate)) {
            return false;
        }
        AgentUpdateValidate other = (AgentUpdateValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
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
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        String this$bankName = this.getBankName();
        String other$bankName = other.getBankName();
        if (this$bankName == null ? other$bankName != null : !this$bankName.equals(other$bankName)) {
            return false;
        }
        String this$bankCard = this.getBankCard();
        String other$bankCard = other.getBankCard();
        return !(this$bankCard == null ? other$bankCard != null : !this$bankCard.equals(other$bankCard));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AgentUpdateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        String $areaPolygon = this.getAreaPolygon();
        result = result * 59 + ($areaPolygon == null ? 43 : $areaPolygon.hashCode());
        BigDecimal $commissionRate = this.getCommissionRate();
        result = result * 59 + ($commissionRate == null ? 43 : ((Object)$commissionRate).hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $bankName = this.getBankName();
        result = result * 59 + ($bankName == null ? 43 : $bankName.hashCode());
        String $bankCard = this.getBankCard();
        result = result * 59 + ($bankCard == null ? 43 : $bankCard.hashCode());
        return result;
    }

    public String toString() {
        return "AgentUpdateValidate(id=" + this.getId() + ", name=" + this.getName() + ", mobile=" + this.getMobile() + ", areaPolygon=" + this.getAreaPolygon() + ", commissionRate=" + this.getCommissionRate() + ", password=" + this.getPassword() + ", bankName=" + this.getBankName() + ", bankCard=" + this.getBankCard() + ")";
    }
}

