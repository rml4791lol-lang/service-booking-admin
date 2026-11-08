/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u7528\u6237\u4fe1\u606f\u5b9e\u4f53")
public class User
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u7f16\u53f7")
    private Integer sn;
    @ApiModelProperty(value="\u7528\u6237\u5934\u50cf")
    private String avatar;
    @ApiModelProperty(value="\u771f\u5b9e\u59d3\u540d")
    private String realName;
    @ApiModelProperty(value="\u7528\u6237\u6635\u79f0")
    private String nickname;
    @ApiModelProperty(value="\u7528\u6237\u8d26\u53f7")
    private String account;
    @ApiModelProperty(value="\u7528\u6237\u5bc6\u7801")
    private String password;
    @ApiModelProperty(value="\u7528\u6237\u7535\u8bdd")
    private String mobile;
    @ApiModelProperty(value="\u5e38\u53bb\u5c0f\u533aID")
    private Integer areaId;
    @ApiModelProperty(value="\u5bb6\u957f\u5907\u6ce8")
    private String note;
    @ApiModelProperty(value="\u7528\u6237\u6027\u522b: [1=\u7537, 2=\u5973]")
    private Integer sex;
    @ApiModelProperty(value="\u51fa\u751f\u65e5\u671f")
    private String birthday;
    @ApiModelProperty(value="\u6ce8\u518c\u6e20\u9053")
    private Integer channel;
    @ApiModelProperty(value="\u662f\u5426\u7981\u7528: [0=\u5426, 1=\u662f]")
    private Integer isDisable;
    @ApiModelProperty(value="\u7528\u6237\u94b1\u5305")
    private BigDecimal userMoney;
    @ApiModelProperty(value="\u7d2f\u8ba1\u5145\u503c")
    private BigDecimal totalRechargeAmount;
    @ApiModelProperty(value="\u6700\u540e\u767b\u5f55IP")
    private String loginIp;
    @ApiModelProperty(value="\u6700\u540e\u767b\u5f55\u65f6\u95f4")
    private Long loginTime;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Long updateTime;
    @ApiModelProperty(value="\u5220\u9664\u65f6\u95f4")
    private Long deleteTime;
    @ApiModelProperty(value="\u662f\u5426\u4e3a\u65b0\u7528\u6237: [0=\u5426, 1=\u662f]")
    private Integer isNewUser;

    public Integer getId() {
        return this.id;
    }

    public Integer getSn() {
        return this.sn;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getRealName() {
        return this.realName;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getAccount() {
        return this.account;
    }

    public String getPassword() {
        return this.password;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Integer getAreaId() {
        return this.areaId;
    }

    public String getNote() {
        return this.note;
    }

    public Integer getSex() {
        return this.sex;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public Integer getChannel() {
        return this.channel;
    }

    public Integer getIsDisable() {
        return this.isDisable;
    }

    public BigDecimal getUserMoney() {
        return this.userMoney;
    }

    public BigDecimal getTotalRechargeAmount() {
        return this.totalRechargeAmount;
    }

    public String getLoginIp() {
        return this.loginIp;
    }

    public Long getLoginTime() {
        return this.loginTime;
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

    public Integer getIsNewUser() {
        return this.isNewUser;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public void setIsDisable(Integer isDisable) {
        this.isDisable = isDisable;
    }

    public void setUserMoney(BigDecimal userMoney) {
        this.userMoney = userMoney;
    }

    public void setTotalRechargeAmount(BigDecimal totalRechargeAmount) {
        this.totalRechargeAmount = totalRechargeAmount;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
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

    public void setIsNewUser(Integer isNewUser) {
        this.isNewUser = isNewUser;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User other = (User)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$sn = this.getSn();
        Integer other$sn = other.getSn();
        if (this$sn == null ? other$sn != null : !((Object)this$sn).equals(other$sn)) {
            return false;
        }
        Integer this$sex = this.getSex();
        Integer other$sex = other.getSex();
        if (this$sex == null ? other$sex != null : !((Object)this$sex).equals(other$sex)) {
            return false;
        }
        Integer this$channel = this.getChannel();
        Integer other$channel = other.getChannel();
        if (this$channel == null ? other$channel != null : !((Object)this$channel).equals(other$channel)) {
            return false;
        }
        Integer this$isDisable = this.getIsDisable();
        Integer other$isDisable = other.getIsDisable();
        if (this$isDisable == null ? other$isDisable != null : !((Object)this$isDisable).equals(other$isDisable)) {
            return false;
        }
        Long this$loginTime = this.getLoginTime();
        Long other$loginTime = other.getLoginTime();
        if (this$loginTime == null ? other$loginTime != null : !((Object)this$loginTime).equals(other$loginTime)) {
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
        Integer this$isNewUser = this.getIsNewUser();
        Integer other$isNewUser = other.getIsNewUser();
        if (this$isNewUser == null ? other$isNewUser != null : !((Object)this$isNewUser).equals(other$isNewUser)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
            return false;
        }
        String this$realName = this.getRealName();
        String other$realName = other.getRealName();
        if (this$realName == null ? other$realName != null : !this$realName.equals(other$realName)) {
            return false;
        }
        String this$nickname = this.getNickname();
        String other$nickname = other.getNickname();
        if (this$nickname == null ? other$nickname != null : !this$nickname.equals(other$nickname)) {
            return false;
        }
        String this$account = this.getAccount();
        String other$account = other.getAccount();
        if (this$account == null ? other$account != null : !this$account.equals(other$account)) {
            return false;
        }
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        if (this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile)) {
            return false;
        }
        String this$birthday = this.getBirthday();
        String other$birthday = other.getBirthday();
        if (this$birthday == null ? other$birthday != null : !this$birthday.equals(other$birthday)) {
            return false;
        }
        BigDecimal this$userMoney = this.getUserMoney();
        BigDecimal other$userMoney = other.getUserMoney();
        if (this$userMoney == null ? other$userMoney != null : !((Object)this$userMoney).equals(other$userMoney)) {
            return false;
        }
        BigDecimal this$totalRechargeAmount = this.getTotalRechargeAmount();
        BigDecimal other$totalRechargeAmount = other.getTotalRechargeAmount();
        if (this$totalRechargeAmount == null ? other$totalRechargeAmount != null : !((Object)this$totalRechargeAmount).equals(other$totalRechargeAmount)) {
            return false;
        }
        String this$loginIp = this.getLoginIp();
        String other$loginIp = other.getLoginIp();
        return !(this$loginIp == null ? other$loginIp != null : !this$loginIp.equals(other$loginIp));
    }

    protected boolean canEqual(Object other) {
        return other instanceof User;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : ((Object)$sn).hashCode());
        Integer $sex = this.getSex();
        result = result * 59 + ($sex == null ? 43 : ((Object)$sex).hashCode());
        Integer $channel = this.getChannel();
        result = result * 59 + ($channel == null ? 43 : ((Object)$channel).hashCode());
        Integer $isDisable = this.getIsDisable();
        result = result * 59 + ($isDisable == null ? 43 : ((Object)$isDisable).hashCode());
        Long $loginTime = this.getLoginTime();
        result = result * 59 + ($loginTime == null ? 43 : ((Object)$loginTime).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        Integer $isNewUser = this.getIsNewUser();
        result = result * 59 + ($isNewUser == null ? 43 : ((Object)$isNewUser).hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $realName = this.getRealName();
        result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
        String $nickname = this.getNickname();
        result = result * 59 + ($nickname == null ? 43 : $nickname.hashCode());
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        String $birthday = this.getBirthday();
        result = result * 59 + ($birthday == null ? 43 : $birthday.hashCode());
        BigDecimal $userMoney = this.getUserMoney();
        result = result * 59 + ($userMoney == null ? 43 : ((Object)$userMoney).hashCode());
        BigDecimal $totalRechargeAmount = this.getTotalRechargeAmount();
        result = result * 59 + ($totalRechargeAmount == null ? 43 : ((Object)$totalRechargeAmount).hashCode());
        String $loginIp = this.getLoginIp();
        result = result * 59 + ($loginIp == null ? 43 : $loginIp.hashCode());
        return result;
    }

    public String toString() {
        return "User(id=" + this.getId() + ", sn=" + this.getSn() + ", avatar=" + this.getAvatar() + ", realName=" + this.getRealName() + ", nickname=" + this.getNickname() + ", account=" + this.getAccount() + ", password=" + this.getPassword() + ", mobile=" + this.getMobile() + ", sex=" + this.getSex() + ", birthday=" + this.getBirthday() + ", channel=" + this.getChannel() + ", isDisable=" + this.getIsDisable() + ", userMoney=" + this.getUserMoney() + ", totalRechargeAmount=" + this.getTotalRechargeAmount() + ", loginIp=" + this.getLoginIp() + ", loginTime=" + this.getLoginTime() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ", isNewUser=" + this.getIsNewUser() + ")";
    }
}

