/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.vo.user.UserCenterVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u4e2a\u4eba\u4e2d\u5fc3Vo")
public class UserCenterVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u7528\u6237ID")
    private Integer id;
    @ApiModelProperty(value="\u7528\u6237\u7f16\u53f7")
    private Integer sn;
    @ApiModelProperty(value="\u7528\u6237\u5934\u50cf")
    private String avatar;
    @ApiModelProperty(value="\u771f\u5b9e\u59d3\u540d")
    private String realName;
    @ApiModelProperty(value="\u7528\u6237\u6635\u79f0")
    private String nickname;
    @ApiModelProperty(value="\u7528\u6237\u8d26\u53f7")
    private String account;
    @ApiModelProperty(value="\u7528\u6237\u7535\u8bdd")
    private String mobile;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;
    @ApiModelProperty(value="\u662f\u5426\u4e3a\u65b0\u7528\u6237: [0=\u5426, 1=\u662f]")
    private Integer isNewUser;
    @ApiModelProperty(value="\u6709\u5bc6\u7801")
    private Boolean hasPassword;
    @ApiModelProperty(value="\u7528\u6237\u6027\u522b: [1=\u7537, 2=\u5973]")
    private String sex;
    @ApiModelProperty(value="\u7528\u6237\u94b1\u5305")
    private BigDecimal userMoney;
    @ApiModelProperty(value="\u662f\u5426\u7ed1\u5b9a\u5fae\u4fe1")
    private Boolean isAuth;

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

    public String getMobile() {
        return this.mobile;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public Integer getIsNewUser() {
        return this.isNewUser;
    }

    public Boolean getHasPassword() {
        return this.hasPassword;
    }

    public String getSex() {
        return this.sex;
    }

    public BigDecimal getUserMoney() {
        return this.userMoney;
    }

    public Boolean getIsAuth() {
        return this.isAuth;
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

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setIsNewUser(Integer isNewUser) {
        this.isNewUser = isNewUser;
    }

    public void setHasPassword(Boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setUserMoney(BigDecimal userMoney) {
        this.userMoney = userMoney;
    }

    public void setIsAuth(Boolean isAuth) {
        this.isAuth = isAuth;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserCenterVo)) {
            return false;
        }
        UserCenterVo other = (UserCenterVo)o;
        if (!other.canEqual((Object)this)) {
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
        Integer this$isNewUser = this.getIsNewUser();
        Integer other$isNewUser = other.getIsNewUser();
        if (this$isNewUser == null ? other$isNewUser != null : !((Object)this$isNewUser).equals(other$isNewUser)) {
            return false;
        }
        Boolean this$hasPassword = this.getHasPassword();
        Boolean other$hasPassword = other.getHasPassword();
        if (this$hasPassword == null ? other$hasPassword != null : !((Object)this$hasPassword).equals(other$hasPassword)) {
            return false;
        }
        Boolean this$isAuth = this.getIsAuth();
        Boolean other$isAuth = other.getIsAuth();
        if (this$isAuth == null ? other$isAuth != null : !((Object)this$isAuth).equals(other$isAuth)) {
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
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        if (this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime)) {
            return false;
        }
        String this$sex = this.getSex();
        String other$sex = other.getSex();
        if (this$sex == null ? other$sex != null : !this$sex.equals(other$sex)) {
            return false;
        }
        BigDecimal this$userMoney = this.getUserMoney();
        BigDecimal other$userMoney = other.getUserMoney();
        return !(this$userMoney == null ? other$userMoney != null : !((Object)this$userMoney).equals(other$userMoney));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserCenterVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : ((Object)$sn).hashCode());
        Integer $isNewUser = this.getIsNewUser();
        result = result * 59 + ($isNewUser == null ? 43 : ((Object)$isNewUser).hashCode());
        Boolean $hasPassword = this.getHasPassword();
        result = result * 59 + ($hasPassword == null ? 43 : ((Object)$hasPassword).hashCode());
        Boolean $isAuth = this.getIsAuth();
        result = result * 59 + ($isAuth == null ? 43 : ((Object)$isAuth).hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $realName = this.getRealName();
        result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
        String $nickname = this.getNickname();
        result = result * 59 + ($nickname == null ? 43 : $nickname.hashCode());
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        String $sex = this.getSex();
        result = result * 59 + ($sex == null ? 43 : $sex.hashCode());
        BigDecimal $userMoney = this.getUserMoney();
        result = result * 59 + ($userMoney == null ? 43 : ((Object)$userMoney).hashCode());
        return result;
    }

    public String toString() {
        return "UserCenterVo(id=" + this.getId() + ", sn=" + this.getSn() + ", avatar=" + this.getAvatar() + ", realName=" + this.getRealName() + ", nickname=" + this.getNickname() + ", account=" + this.getAccount() + ", mobile=" + this.getMobile() + ", createTime=" + this.getCreateTime() + ", isNewUser=" + this.getIsNewUser() + ", hasPassword=" + this.getHasPassword() + ", sex=" + this.getSex() + ", userMoney=" + this.getUserMoney() + ", isAuth=" + this.getIsAuth() + ")";
    }
}

