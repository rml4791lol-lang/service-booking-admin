/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONArray
 *  com.mdd.admin.vo.setting.SettingLoginVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.setting;

import com.alibaba.fastjson2.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u767b\u5f55\u8bbe\u7f6eVo")
public class SettingLoginVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u767b\u5f55\u65b9\u5f0f")
    private JSONArray loginWay;
    @ApiModelProperty(value="\u5f3a\u5236\u7ed1\u5b9a\u624b\u673a")
    private Integer coerceMobile;
    @ApiModelProperty(value="\u662f\u5426\u5f00\u542f\u534f\u8bae")
    private Integer loginAgreement;
    @ApiModelProperty(value="\u7b2c\u4e09\u65b9\u7684\u767b\u5f55")
    private Integer thirdAuth;
    @ApiModelProperty(value="\u5fae\u4fe1\u767b\u5f55")
    private Integer wechatAuth;
    @ApiModelProperty(value="qq\u767b\u5f55")
    private Integer qqAuth;

    public JSONArray getLoginWay() {
        return this.loginWay;
    }

    public Integer getCoerceMobile() {
        return this.coerceMobile;
    }

    public Integer getLoginAgreement() {
        return this.loginAgreement;
    }

    public Integer getThirdAuth() {
        return this.thirdAuth;
    }

    public Integer getWechatAuth() {
        return this.wechatAuth;
    }

    public Integer getQqAuth() {
        return this.qqAuth;
    }

    public void setLoginWay(JSONArray loginWay) {
        this.loginWay = loginWay;
    }

    public void setCoerceMobile(Integer coerceMobile) {
        this.coerceMobile = coerceMobile;
    }

    public void setLoginAgreement(Integer loginAgreement) {
        this.loginAgreement = loginAgreement;
    }

    public void setThirdAuth(Integer thirdAuth) {
        this.thirdAuth = thirdAuth;
    }

    public void setWechatAuth(Integer wechatAuth) {
        this.wechatAuth = wechatAuth;
    }

    public void setQqAuth(Integer qqAuth) {
        this.qqAuth = qqAuth;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingLoginVo)) {
            return false;
        }
        SettingLoginVo other = (SettingLoginVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$coerceMobile = this.getCoerceMobile();
        Integer other$coerceMobile = other.getCoerceMobile();
        if (this$coerceMobile == null ? other$coerceMobile != null : !((Object)this$coerceMobile).equals(other$coerceMobile)) {
            return false;
        }
        Integer this$loginAgreement = this.getLoginAgreement();
        Integer other$loginAgreement = other.getLoginAgreement();
        if (this$loginAgreement == null ? other$loginAgreement != null : !((Object)this$loginAgreement).equals(other$loginAgreement)) {
            return false;
        }
        Integer this$thirdAuth = this.getThirdAuth();
        Integer other$thirdAuth = other.getThirdAuth();
        if (this$thirdAuth == null ? other$thirdAuth != null : !((Object)this$thirdAuth).equals(other$thirdAuth)) {
            return false;
        }
        Integer this$wechatAuth = this.getWechatAuth();
        Integer other$wechatAuth = other.getWechatAuth();
        if (this$wechatAuth == null ? other$wechatAuth != null : !((Object)this$wechatAuth).equals(other$wechatAuth)) {
            return false;
        }
        Integer this$qqAuth = this.getQqAuth();
        Integer other$qqAuth = other.getQqAuth();
        if (this$qqAuth == null ? other$qqAuth != null : !((Object)this$qqAuth).equals(other$qqAuth)) {
            return false;
        }
        JSONArray this$loginWay = this.getLoginWay();
        JSONArray other$loginWay = other.getLoginWay();
        return !(this$loginWay == null ? other$loginWay != null : !this$loginWay.equals(other$loginWay));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingLoginVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $coerceMobile = this.getCoerceMobile();
        result = result * 59 + ($coerceMobile == null ? 43 : ((Object)$coerceMobile).hashCode());
        Integer $loginAgreement = this.getLoginAgreement();
        result = result * 59 + ($loginAgreement == null ? 43 : ((Object)$loginAgreement).hashCode());
        Integer $thirdAuth = this.getThirdAuth();
        result = result * 59 + ($thirdAuth == null ? 43 : ((Object)$thirdAuth).hashCode());
        Integer $wechatAuth = this.getWechatAuth();
        result = result * 59 + ($wechatAuth == null ? 43 : ((Object)$wechatAuth).hashCode());
        Integer $qqAuth = this.getQqAuth();
        result = result * 59 + ($qqAuth == null ? 43 : ((Object)$qqAuth).hashCode());
        JSONArray $loginWay = this.getLoginWay();
        result = result * 59 + ($loginWay == null ? 43 : $loginWay.hashCode());
        return result;
    }

    public String toString() {
        return "SettingLoginVo(loginWay=" + this.getLoginWay() + ", coerceMobile=" + this.getCoerceMobile() + ", loginAgreement=" + this.getLoginAgreement() + ", thirdAuth=" + this.getThirdAuth() + ", wechatAuth=" + this.getWechatAuth() + ", qqAuth=" + this.getQqAuth() + ")";
    }
}

