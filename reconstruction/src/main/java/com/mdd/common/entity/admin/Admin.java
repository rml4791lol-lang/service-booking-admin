/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u7cfb\u7edf\u7ba1\u7406\u5458\u5b9e\u4f53")
public class Admin
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u662f\u5426\u8d85\u7ea7\u7ba1\u7406\u5458 0-\u5426 1-\u662f")
    private Integer root;
    @ApiModelProperty(value="\u7528\u6237\u8d26\u53f7")
    private String name;
    @ApiModelProperty(value="\u7528\u6237\u5934\u50cf")
    private String avatar;
    @ApiModelProperty(value="\u7528\u6237\u6635\u79f0")
    private String account;
    @ApiModelProperty(value="\u7528\u6237\u5bc6\u7801")
    private String password;
    @ApiModelProperty(value="\u591a\u7aef\u767b\u5f55: [0=\u5426, 1=\u662f]")
    private Integer multipointLogin;
    @ApiModelProperty(value="\u662f\u5426\u7981\u7528: [0=\u5426, 1=\u662f]")
    private Integer disable;
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
    @ApiModelProperty(value="firstLogin")
    private Integer firstLogin;

    public Integer getId() {
        return this.id;
    }

    public Integer getRoot() {
        return this.root;
    }

    public String getName() {
        return this.name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getAccount() {
        return this.account;
    }

    public String getPassword() {
        return this.password;
    }

    public Integer getMultipointLogin() {
        return this.multipointLogin;
    }

    public Integer getDisable() {
        return this.disable;
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

    public Integer getFirstLogin() {
        return this.firstLogin;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoot(Integer root) {
        this.root = root;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMultipointLogin(Integer multipointLogin) {
        this.multipointLogin = multipointLogin;
    }

    public void setDisable(Integer disable) {
        this.disable = disable;
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

    public void setFirstLogin(Integer firstLogin) {
        this.firstLogin = firstLogin;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Admin)) {
            return false;
        }
        Admin other = (Admin)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$root = this.getRoot();
        Integer other$root = other.getRoot();
        if (this$root == null ? other$root != null : !((Object)this$root).equals(other$root)) {
            return false;
        }
        Integer this$multipointLogin = this.getMultipointLogin();
        Integer other$multipointLogin = other.getMultipointLogin();
        if (this$multipointLogin == null ? other$multipointLogin != null : !((Object)this$multipointLogin).equals(other$multipointLogin)) {
            return false;
        }
        Integer this$disable = this.getDisable();
        Integer other$disable = other.getDisable();
        if (this$disable == null ? other$disable != null : !((Object)this$disable).equals(other$disable)) {
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
        Integer this$firstLogin = this.getFirstLogin();
        Integer other$firstLogin = other.getFirstLogin();
        if (this$firstLogin == null ? other$firstLogin != null : !((Object)this$firstLogin).equals(other$firstLogin)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
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
        String this$loginIp = this.getLoginIp();
        String other$loginIp = other.getLoginIp();
        return !(this$loginIp == null ? other$loginIp != null : !this$loginIp.equals(other$loginIp));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Admin;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $root = this.getRoot();
        result = result * 59 + ($root == null ? 43 : ((Object)$root).hashCode());
        Integer $multipointLogin = this.getMultipointLogin();
        result = result * 59 + ($multipointLogin == null ? 43 : ((Object)$multipointLogin).hashCode());
        Integer $disable = this.getDisable();
        result = result * 59 + ($disable == null ? 43 : ((Object)$disable).hashCode());
        Long $loginTime = this.getLoginTime();
        result = result * 59 + ($loginTime == null ? 43 : ((Object)$loginTime).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        Integer $firstLogin = this.getFirstLogin();
        result = result * 59 + ($firstLogin == null ? 43 : ((Object)$firstLogin).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $loginIp = this.getLoginIp();
        result = result * 59 + ($loginIp == null ? 43 : $loginIp.hashCode());
        return result;
    }

    public String toString() {
        return "Admin(id=" + this.getId() + ", root=" + this.getRoot() + ", name=" + this.getName() + ", avatar=" + this.getAvatar() + ", account=" + this.getAccount() + ", password=" + this.getPassword() + ", multipointLogin=" + this.getMultipointLogin() + ", disable=" + this.getDisable() + ", loginIp=" + this.getLoginIp() + ", loginTime=" + this.getLoginTime() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ", firstLogin=" + this.getFirstLogin() + ")";
    }
}

