/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.vo.user.UserAccountListVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u7528\u6237\u8d44\u91d1\u53d8\u66f4Vo")
public class UserAccountListVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u7528\u6237ID")
    private Integer id;
    @ApiModelProperty(value="\u53d8\u52a8\u7c7b\u578b")
    private Integer changeType;
    @ApiModelProperty(value="\u53d8\u52a8\u7c7b\u578b\u5b57\u7b26\u4e32")
    private String typeDesc;
    @ApiModelProperty(value="\u53d8\u52a8\u6570\u91cf")
    private BigDecimal changeAmount;
    @ApiModelProperty(value="\u53d8\u52a8\u6570\u91cf\u5b57\u7b26\u4e32")
    private String changeAmountDesc;
    @ApiModelProperty(value="\u53d8\u52a8\u7c7b\u578b: [1=\u589e\u52a0, 2=\u51cf\u5c11]")
    private Integer action;
    @ApiModelProperty(value="\u5907\u6ce8\u4fe1\u606f")
    private String remark;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private String createTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getChangeType() {
        return this.changeType;
    }

    public String getTypeDesc() {
        return this.typeDesc;
    }

    public BigDecimal getChangeAmount() {
        return this.changeAmount;
    }

    public String getChangeAmountDesc() {
        return this.changeAmountDesc;
    }

    public Integer getAction() {
        return this.action;
    }

    public String getRemark() {
        return this.remark;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public void setChangeAmountDesc(String changeAmountDesc) {
        this.changeAmountDesc = changeAmountDesc;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserAccountListVo)) {
            return false;
        }
        UserAccountListVo other = (UserAccountListVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$changeType = this.getChangeType();
        Integer other$changeType = other.getChangeType();
        if (this$changeType == null ? other$changeType != null : !((Object)this$changeType).equals(other$changeType)) {
            return false;
        }
        Integer this$action = this.getAction();
        Integer other$action = other.getAction();
        if (this$action == null ? other$action != null : !((Object)this$action).equals(other$action)) {
            return false;
        }
        String this$typeDesc = this.getTypeDesc();
        String other$typeDesc = other.getTypeDesc();
        if (this$typeDesc == null ? other$typeDesc != null : !this$typeDesc.equals(other$typeDesc)) {
            return false;
        }
        BigDecimal this$changeAmount = this.getChangeAmount();
        BigDecimal other$changeAmount = other.getChangeAmount();
        if (this$changeAmount == null ? other$changeAmount != null : !((Object)this$changeAmount).equals(other$changeAmount)) {
            return false;
        }
        String this$changeAmountDesc = this.getChangeAmountDesc();
        String other$changeAmountDesc = other.getChangeAmountDesc();
        if (this$changeAmountDesc == null ? other$changeAmountDesc != null : !this$changeAmountDesc.equals(other$changeAmountDesc)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        if (this$remark == null ? other$remark != null : !this$remark.equals(other$remark)) {
            return false;
        }
        String this$createTime = this.getCreateTime();
        String other$createTime = other.getCreateTime();
        return !(this$createTime == null ? other$createTime != null : !this$createTime.equals(other$createTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserAccountListVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $changeType = this.getChangeType();
        result = result * 59 + ($changeType == null ? 43 : ((Object)$changeType).hashCode());
        Integer $action = this.getAction();
        result = result * 59 + ($action == null ? 43 : ((Object)$action).hashCode());
        String $typeDesc = this.getTypeDesc();
        result = result * 59 + ($typeDesc == null ? 43 : $typeDesc.hashCode());
        BigDecimal $changeAmount = this.getChangeAmount();
        result = result * 59 + ($changeAmount == null ? 43 : ((Object)$changeAmount).hashCode());
        String $changeAmountDesc = this.getChangeAmountDesc();
        result = result * 59 + ($changeAmountDesc == null ? 43 : $changeAmountDesc.hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        String $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        return result;
    }

    public String toString() {
        return "UserAccountListVo(id=" + this.getId() + ", changeType=" + this.getChangeType() + ", typeDesc=" + this.getTypeDesc() + ", changeAmount=" + this.getChangeAmount() + ", changeAmountDesc=" + this.getChangeAmountDesc() + ", action=" + this.getAction() + ", remark=" + this.getRemark() + ", createTime=" + this.getCreateTime() + ")";
    }
}

