/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.annotation.TableName
 */
package com.mdd.common.entity.community;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

@TableName(value="la_community_agent")
public class CommunityAgent
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    private Integer id;
    private Integer communityId;
    private Integer agentId;
    private Long createTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getCommunityId() {
        return this.communityId;
    }

    public Integer getAgentId() {
        return this.agentId;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommunityAgent)) {
            return false;
        }
        CommunityAgent other = (CommunityAgent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$communityId = this.getCommunityId();
        Integer other$communityId = other.getCommunityId();
        if (this$communityId == null ? other$communityId != null : !((Object)this$communityId).equals(other$communityId)) {
            return false;
        }
        Integer this$agentId = this.getAgentId();
        Integer other$agentId = other.getAgentId();
        if (this$agentId == null ? other$agentId != null : !((Object)this$agentId).equals(other$agentId)) {
            return false;
        }
        Long this$createTime = this.getCreateTime();
        Long other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CommunityAgent;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $communityId = this.getCommunityId();
        result = result * 59 + ($communityId == null ? 43 : ((Object)$communityId).hashCode());
        Integer $agentId = this.getAgentId();
        result = result * 59 + ($agentId == null ? 43 : ((Object)$agentId).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        return result;
    }

    public String toString() {
        return "CommunityAgent(id=" + this.getId() + ", communityId=" + this.getCommunityId() + ", agentId=" + this.getAgentId() + ", createTime=" + this.getCreateTime() + ")";
    }
}

