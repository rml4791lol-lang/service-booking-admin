/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.finance.FinanceWalletListVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.finance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u4f59\u989d\u8bb0\u5f55\u5217\u8868Vo")
public class FinanceWalletListVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u8bb0\u5f55ID")
    private Integer id;
    @ApiModelProperty(value="\u7528\u6237\u7f16\u53f7")
    private String sn;
    private String account;
    private String mobile;
    private Integer action;
    @ApiModelProperty(value="u\u5c4f\u5e55\u4f30\u8ba1\u5934\u76ae\u5c51")
    private String avatar;
    @ApiModelProperty(value="\u7528\u6237\u6635\u79f0")
    private String nickname;
    @ApiModelProperty(value="\u53d8\u52a8\u91d1\u989d")
    private BigDecimal changeAmount;
    @ApiModelProperty(value="\u5269\u4f59\u91d1\u989d")
    private BigDecimal leftAmount;
    @ApiModelProperty(value="\u53d8\u52a8\u7c7b\u578b")
    private String changeType;
    private String changeTypeDesc;
    @ApiModelProperty(value="\u6765\u6e90\u5355\u53f7")
    private String sourceSn;
    @ApiModelProperty(value="\u8bb0\u5f55\u65f6\u95f4")
    private String createTime;

    public Integer getId() {
        return this.id;
    }

    public String getSn() {
        return this.sn;
    }

    public String getAccount() {
        return this.account;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Integer getAction() {
        return this.action;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getNickname() {
        return this.nickname;
    }

    public BigDecimal getChangeAmount() {
        return this.changeAmount;
    }

    public BigDecimal getLeftAmount() {
        return this.leftAmount;
    }

    public String getChangeType() {
        return this.changeType;
    }

    public String getChangeTypeDesc() {
        return this.changeTypeDesc;
    }

    public String getSourceSn() {
        return this.sourceSn;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public void setLeftAmount(BigDecimal leftAmount) {
        this.leftAmount = leftAmount;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public void setChangeTypeDesc(String changeTypeDesc) {
        this.changeTypeDesc = changeTypeDesc;
    }

    public void setSourceSn(String sourceSn) {
        this.sourceSn = sourceSn;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FinanceWalletListVo)) {
            return false;
        }
        FinanceWalletListVo other = (FinanceWalletListVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$action = this.getAction();
        Integer other$action = other.getAction();
        if (this$action == null ? other$action != null : !((Object)this$action).equals(other$action)) {
            return false;
        }
        String this$sn = this.getSn();
        String other$sn = other.getSn();
        if (this$sn == null ? other$sn != null : !this$sn.equals(other$sn)) {
            return false;
        }
        String this$account = this.getAccount();
        String other$account = other.getAccount();
        if (this$account == null ? other$account != null : !this$account.equals(other$account)) {
            return false;
        }
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        if (this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
            return false;
        }
        String this$nickname = this.getNickname();
        String other$nickname = other.getNickname();
        if (this$nickname == null ? other$nickname != null : !this$nickname.equals(other$nickname)) {
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
        String this$changeType = this.getChangeType();
        String other$changeType = other.getChangeType();
        if (this$changeType == null ? other$changeType != null : !this$changeType.equals(other$changeType)) {
            return false;
        }
        String this$changeTypeDesc = this.getChangeTypeDesc();
        String other$changeTypeDesc = other.getChangeTypeDesc();
        if (this$changeTypeDesc == null ? other$changeTypeDesc != null : !this$changeTypeDesc.equals(other$changeTypeDesc)) {
            return false;
        }
        String this$sourceSn = this.getSourceSn();
        String other$sourceSn = other.getSourceSn();
        if (this$sourceSn == null ? other$sourceSn != null : !this$sourceSn.equals(other$sourceSn)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof FinanceWalletListVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $action = this.getAction();
        result = result * 59 + ($action == null ? 43 : ((Object)$action).hashCode());
        String $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : $sn.hashCode());
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $nickname = this.getNickname();
        result = result * 59 + ($nickname == null ? 43 : $nickname.hashCode());
        BigDecimal $changeAmount = this.getChangeAmount();
        result = result * 59 + ($changeAmount == null ? 43 : ((Object)$changeAmount).hashCode());
        BigDecimal $leftAmount = this.getLeftAmount();
        result = result * 59 + ($leftAmount == null ? 43 : ((Object)$leftAmount).hashCode());
        String $changeType = this.getChangeType();
        result = result * 59 + ($changeType == null ? 43 : $changeType.hashCode());
        String $changeTypeDesc = this.getChangeTypeDesc();
        result = result * 59 + ($changeTypeDesc == null ? 43 : $changeTypeDesc.hashCode());
        String $sourceSn = this.getSourceSn();
        result = result * 59 + ($sourceSn == null ? 43 : $sourceSn.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        return result;
    }

    public String toString() {
        return "FinanceWalletListVo(id=" + this.getId() + ", sn=" + this.getSn() + ", account=" + this.getAccount() + ", mobile=" + this.getMobile() + ", action=" + this.getAction() + ", avatar=" + this.getAvatar() + ", nickname=" + this.getNickname() + ", changeAmount=" + this.getChangeAmount() + ", leftAmount=" + this.getLeftAmount() + ", changeType=" + this.getChangeType() + ", changeTypeDesc=" + this.getChangeTypeDesc() + ", sourceSn=" + this.getSourceSn() + ", createTime=" + this.getCreateTime() + ")";
    }
}

