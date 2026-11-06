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

@ApiModel(value="\u7cfb\u7edf\u5c97\u4f4d\u5b9e\u4f53")
public class Dept
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u4e0a\u7ea7\u90e8\u95e8")
    private Integer pid;
    @ApiModelProperty(value="\u90e8\u95e8\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u8d1f\u8d23\u4eba\u540d")
    private String leader;
    @ApiModelProperty(value="\u8054\u7cfb\u7535\u8bdd")
    private String mobile;
    @ApiModelProperty(value="\u6392\u5e8f\u7f16\u53f7")
    private Integer sort;
    @ApiModelProperty(value="\u662f\u5426\u7981\u7528: [0=\u5426, 1=\u662f]")
    private Integer status;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Long updateTime;
    @ApiModelProperty(value="\u5220\u9664\u65f6\u95f4")
    private Long deleteTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getPid() {
        return this.pid;
    }

    public String getName() {
        return this.name;
    }

    public String getLeader() {
        return this.leader;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Integer getSort() {
        return this.sort;
    }

    public Integer getStatus() {
        return this.status;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Dept)) {
            return false;
        }
        Dept other = (Dept)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$pid = this.getPid();
        Integer other$pid = other.getPid();
        if (this$pid == null ? other$pid != null : !((Object)this$pid).equals(other$pid)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
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
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$leader = this.getLeader();
        String other$leader = other.getLeader();
        if (this$leader == null ? other$leader != null : !this$leader.equals(other$leader)) {
            return false;
        }
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        return !(this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Dept;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $pid = this.getPid();
        result = result * 59 + ($pid == null ? 43 : ((Object)$pid).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $leader = this.getLeader();
        result = result * 59 + ($leader == null ? 43 : $leader.hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        return result;
    }

    public String toString() {
        return "Dept(id=" + this.getId() + ", pid=" + this.getPid() + ", name=" + this.getName() + ", leader=" + this.getLeader() + ", mobile=" + this.getMobile() + ", sort=" + this.getSort() + ", status=" + this.getStatus() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ")";
    }
}

