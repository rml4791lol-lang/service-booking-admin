package com.mdd.admin.validate.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "优惠券批量发放参数")
public class CouponIssueValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "请选择优惠券模板")
    @Min(value = 1, message = "请选择优惠券模板")
    @ApiModelProperty(value = "模板ID")
    private Integer templateId;

    @NotEmpty(message = "请选择发放用户")
    @Size(max = 200, message = "单次最多发放200人")
    @ApiModelProperty(value = "用户ID列表")
    private List<@NotNull(message = "用户ID不能为空") @Min(value = 1, message = "用户ID必须大于0") Integer> userIds;

    @Size(max = 64, message = "幂等键最长64位")
    @ApiModelProperty(value = "客户端幂等键，重复提交不会重复发券")
    private String requestId;

    @Size(max = 200, message = "备注最长200位")
    @ApiModelProperty(value = "发放备注（可选，留空使用模板名）")
    private String remark;

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
