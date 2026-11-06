/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.agent.AgentSearchValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.agent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u4ee3\u7406\u641c\u7d22\u53c2\u6570")
public class AgentSearchValidate
implements Serializable {
    @ApiModelProperty(value="\u4ee3\u7406\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u8054\u7cfb\u7535\u8bdd")
    private String mobile;
    @ApiModelProperty(value="\u6240\u5c5e\u793e\u533aID")
    private Integer communityId;

    public String getName() {
        return this.name;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Integer getCommunityId() {
        return this.communityId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AgentSearchValidate)) {
            return false;
        }
        AgentSearchValidate other = (AgentSearchValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$communityId = this.getCommunityId();
        Integer other$communityId = other.getCommunityId();
        if (this$communityId == null ? other$communityId != null : !((Object)this$communityId).equals(other$communityId)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        return !(this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile));
    }

    protected boolean canEqual(Object other) {
        return other instanceof AgentSearchValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $communityId = this.getCommunityId();
        result = result * 59 + ($communityId == null ? 43 : ((Object)$communityId).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        return result;
    }

    public String toString() {
        return "AgentSearchValidate(name=" + this.getName() + ", mobile=" + this.getMobile() + ", communityId=" + this.getCommunityId() + ")";
    }
}

