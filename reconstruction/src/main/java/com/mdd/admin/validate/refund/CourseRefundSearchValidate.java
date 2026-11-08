/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.refund.CourseRefundSearchValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u9000\u8bfe\u7533\u8bf7\u5217\u8868\u67e5\u8be2\u53c2\u6570")
public class CourseRefundSearchValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u72b6\u6001:0=\u5f85\u5ba1\u6279,1=\u5df2\u901a\u8fc7,2=\u5df2\u62d2\u7edd")
    private Integer status;
    @ApiModelProperty(value="\u7528\u6237\u59d3\u540d")
    private String userName;
    @ApiModelProperty(value="\u9000\u6b3e\u6765\u6e90:course=\u8bfe\u7a0b,coupon=\u8bfe\u7a0b\u5238\uff08#110 T12\uff09")
    private String source;

    public Integer getStatus() {
        return this.status;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CourseRefundSearchValidate)) {
            return false;
        }
        CourseRefundSearchValidate other = (CourseRefundSearchValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$userName = this.getUserName();
        String other$userName = other.getUserName();
        return !(this$userName == null ? other$userName != null : !this$userName.equals(other$userName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseRefundSearchValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        return result;
    }

    public String toString() {
        return "CourseRefundSearchValidate(status=" + this.getStatus() + ", userName=" + this.getUserName() + ")";
    }
}

