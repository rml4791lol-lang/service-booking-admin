/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u6559\u7ec3\u5fae\u4fe1\u6388\u6743\u5b9e\u4f53")
public class InstructorAuth
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u6559\u7ec3ID")
    private Integer instructorId;
    @ApiModelProperty(value="\u5fae\u4fe1openid")
    private String openid;
    @ApiModelProperty(value="\u5fae\u4fe1unionid")
    private String unionid;
    @ApiModelProperty(value="\u5ba2\u6237\u7aef\u7c7b\u578b\uff1a1-\u5fae\u4fe1\u5c0f\u7a0b\u5e8f\uff1b2-\u5fae\u4fe1\u516c\u4f17\u53f7")
    private Integer terminal;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Long updateTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getInstructorId() {
        return this.instructorId;
    }

    public String getOpenid() {
        return this.openid;
    }

    public String getUnionid() {
        return this.unionid;
    }

    public Integer getTerminal() {
        return this.terminal;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public void setTerminal(Integer terminal) {
        this.terminal = terminal;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InstructorAuth)) {
            return false;
        }
        InstructorAuth other = (InstructorAuth)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$instructorId = this.getInstructorId();
        Integer other$instructorId = other.getInstructorId();
        if (this$instructorId == null ? other$instructorId != null : !((Object)this$instructorId).equals(other$instructorId)) {
            return false;
        }
        Integer this$terminal = this.getTerminal();
        Integer other$terminal = other.getTerminal();
        if (this$terminal == null ? other$terminal != null : !((Object)this$terminal).equals(other$terminal)) {
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
        String this$openid = this.getOpenid();
        String other$openid = other.getOpenid();
        if (this$openid == null ? other$openid != null : !this$openid.equals(other$openid)) {
            return false;
        }
        String this$unionid = this.getUnionid();
        String other$unionid = other.getUnionid();
        return !(this$unionid == null ? other$unionid != null : !this$unionid.equals(other$unionid));
    }

    protected boolean canEqual(Object other) {
        return other instanceof InstructorAuth;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $instructorId = this.getInstructorId();
        result = result * 59 + ($instructorId == null ? 43 : ((Object)$instructorId).hashCode());
        Integer $terminal = this.getTerminal();
        result = result * 59 + ($terminal == null ? 43 : ((Object)$terminal).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $openid = this.getOpenid();
        result = result * 59 + ($openid == null ? 43 : $openid.hashCode());
        String $unionid = this.getUnionid();
        result = result * 59 + ($unionid == null ? 43 : $unionid.hashCode());
        return result;
    }

    public String toString() {
        return "InstructorAuth(id=" + this.getId() + ", instructorId=" + this.getInstructorId() + ", openid=" + this.getOpenid() + ", unionid=" + this.getUnionid() + ", terminal=" + this.getTerminal() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

