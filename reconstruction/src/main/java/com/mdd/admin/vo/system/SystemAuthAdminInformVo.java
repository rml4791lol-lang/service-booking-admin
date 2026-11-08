/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.system.SystemAuthAdminInformVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u7ba1\u7406\u5458\u4fe1\u606fVo")
public class SystemAuthAdminInformVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u4e3b\u952e")
    private Integer id;
    @ApiModelProperty(value="\u90e8\u95e8ID")
    private Integer deptId;
    @ApiModelProperty(value="\u5c97\u4f4dID")
    private Integer postId;
    @ApiModelProperty(value="\u8d26\u53f7")
    private String username;
    @ApiModelProperty(value="\u6635\u79f0")
    private String nickname;
    @ApiModelProperty(value="\u5934\u50cf")
    private String avatar;
    @ApiModelProperty(value="\u591a\u7aef\u767b\u5f55: [0=\u5426, 1=\u662f]")
    private Integer isMultipoint;
    @ApiModelProperty(value="\u662f\u5426\u7981\u7528: [0=\u5426, 1=\u662f]")
    private Integer isDisable;
    @ApiModelProperty(value="\u6700\u540e\u767b\u5f55IP")
    private String lastLoginIp;
    @ApiModelProperty(value="\u6700\u540e\u767b\u5f55\u65f6\u95f4")
    private String lastLoginTime;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private String updateTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getDeptId() {
        return this.deptId;
    }

    public Integer getPostId() {
        return this.postId;
    }

    public String getUsername() {
        return this.username;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public Integer getIsMultipoint() {
        return this.isMultipoint;
    }

    public Integer getIsDisable() {
        return this.isDisable;
    }

    public String getLastLoginIp() {
        return this.lastLoginIp;
    }

    public String getLastLoginTime() {
        return this.lastLoginTime;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setIsMultipoint(Integer isMultipoint) {
        this.isMultipoint = isMultipoint;
    }

    public void setIsDisable(Integer isDisable) {
        this.isDisable = isDisable;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemAuthAdminInformVo)) {
            return false;
        }
        SystemAuthAdminInformVo other = (SystemAuthAdminInformVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$deptId = this.getDeptId();
        Integer other$deptId = other.getDeptId();
        if (this$deptId == null ? other$deptId != null : !((Object)this$deptId).equals(other$deptId)) {
            return false;
        }
        Integer this$postId = this.getPostId();
        Integer other$postId = other.getPostId();
        if (this$postId == null ? other$postId != null : !((Object)this$postId).equals(other$postId)) {
            return false;
        }
        Integer this$isMultipoint = this.getIsMultipoint();
        Integer other$isMultipoint = other.getIsMultipoint();
        if (this$isMultipoint == null ? other$isMultipoint != null : !((Object)this$isMultipoint).equals(other$isMultipoint)) {
            return false;
        }
        Integer this$isDisable = this.getIsDisable();
        Integer other$isDisable = other.getIsDisable();
        if (this$isDisable == null ? other$isDisable != null : !((Object)this$isDisable).equals(other$isDisable)) {
            return false;
        }
        String this$username = this.getUsername();
        String other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) {
            return false;
        }
        String this$nickname = this.getNickname();
        String other$nickname = other.getNickname();
        if (this$nickname == null ? other$nickname != null : !this$nickname.equals(other$nickname)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
            return false;
        }
        String this$lastLoginIp = this.getLastLoginIp();
        String other$lastLoginIp = other.getLastLoginIp();
        if (this$lastLoginIp == null ? other$lastLoginIp != null : !this$lastLoginIp.equals(other$lastLoginIp)) {
            return false;
        }
        String this$lastLoginTime = this.getLastLoginTime();
        String other$lastLoginTime = other.getLastLoginTime();
        if (this$lastLoginTime == null ? other$lastLoginTime != null : !this$lastLoginTime.equals(other$lastLoginTime)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime)) {
            return false;
        }
        String this$updateTime = this.getUpdateTime();
        String other$updateTime = other.getUpdateTime();
        return !(this$updateTime == null ? other$updateTime != null : !this$updateTime.equals(other$updateTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SystemAuthAdminInformVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $deptId = this.getDeptId();
        result = result * 59 + ($deptId == null ? 43 : ((Object)$deptId).hashCode());
        Integer $postId = this.getPostId();
        result = result * 59 + ($postId == null ? 43 : ((Object)$postId).hashCode());
        Integer $isMultipoint = this.getIsMultipoint();
        result = result * 59 + ($isMultipoint == null ? 43 : ((Object)$isMultipoint).hashCode());
        Integer $isDisable = this.getIsDisable();
        result = result * 59 + ($isDisable == null ? 43 : ((Object)$isDisable).hashCode());
        String $username = this.getUsername();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        String $nickname = this.getNickname();
        result = result * 59 + ($nickname == null ? 43 : $nickname.hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $lastLoginIp = this.getLastLoginIp();
        result = result * 59 + ($lastLoginIp == null ? 43 : $lastLoginIp.hashCode());
        String $lastLoginTime = this.getLastLoginTime();
        result = result * 59 + ($lastLoginTime == null ? 43 : $lastLoginTime.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        String $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        return result;
    }

    public String toString() {
        return "SystemAuthAdminInformVo(id=" + this.getId() + ", deptId=" + this.getDeptId() + ", postId=" + this.getPostId() + ", username=" + this.getUsername() + ", nickname=" + this.getNickname() + ", avatar=" + this.getAvatar() + ", isMultipoint=" + this.getIsMultipoint() + ", isDisable=" + this.getIsDisable() + ", lastLoginIp=" + this.getLastLoginIp() + ", lastLoginTime=" + this.getLastLoginTime() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

