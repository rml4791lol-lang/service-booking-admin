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

@ApiModel(value="\u7528\u6237\u4f1a\u8bdd\u5b9e\u4f53")
public class UserSession
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u7528\u6237ID")
    private Integer userId;
    @ApiModelProperty(value="\u5ba2\u6237\u7aef\u7c7b\u578b\uff1a1-\u5fae\u4fe1\u5c0f\u7a0b\u5e8f\uff1b2-\u5fae\u4fe1\u516c\u4f17\u53f7\uff1b3-\u624b\u673aH5\uff1b4-\u7535\u8111PC\uff1b5-\u82f9\u679cAPP\uff1b6-\u5b89\u5353APP")
    private Integer terminal;
    @ApiModelProperty(value="\u4ee4\u724c")
    private String token;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long updateTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Long expireTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Integer getTerminal() {
        return this.terminal;
    }

    public String getToken() {
        return this.token;
    }

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public Long getExpireTime() {
        return this.expireTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setTerminal(Integer terminal) {
        this.terminal = terminal;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserSession)) {
            return false;
        }
        UserSession other = (UserSession)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$terminal = this.getTerminal();
        Integer other$terminal = other.getTerminal();
        if (this$terminal == null ? other$terminal != null : !((Object)this$terminal).equals(other$terminal)) {
            return false;
        }
        Long this$updateTime = this.getUpdateTime();
        Long other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        Long this$expireTime = this.getExpireTime();
        Long other$expireTime = other.getExpireTime();
        if (this$expireTime == null ? other$expireTime != null : !((Object)this$expireTime).equals(other$expireTime)) {
            return false;
        }
        String this$token = this.getToken();
        String other$token = other.getToken();
        return !(this$token == null ? other$token != null : !this$token.equals(other$token));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserSession;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $terminal = this.getTerminal();
        result = result * 59 + ($terminal == null ? 43 : ((Object)$terminal).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $expireTime = this.getExpireTime();
        result = result * 59 + ($expireTime == null ? 43 : ((Object)$expireTime).hashCode());
        String $token = this.getToken();
        result = result * 59 + ($token == null ? 43 : $token.hashCode());
        return result;
    }

    public String toString() {
        return "UserSession(id=" + this.getId() + ", userId=" + this.getUserId() + ", terminal=" + this.getTerminal() + ", token=" + this.getToken() + ", updateTime=" + this.getUpdateTime() + ", expireTime=" + this.getExpireTime() + ")";
    }
}

