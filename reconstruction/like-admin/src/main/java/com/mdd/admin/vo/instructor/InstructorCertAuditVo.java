package com.mdd.admin.vo.instructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 后台教练资质审核留痕输出（#49）。
 */
@ApiModel(value = "后台教练资质审核留痕")
public class InstructorCertAuditVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "留痕ID")
    private Long id;

    @ApiModelProperty(value = "资质ID")
    private Long certId;

    @ApiModelProperty(value = "证书名称快照")
    private String certName;

    @ApiModelProperty(value = "动作:SUBMIT/APPROVE/REJECT")
    private String action;

    @ApiModelProperty(value = "动作文案")
    private String actionLabel;

    @ApiModelProperty(value = "来源:COACH/ADMIN/SYSTEM")
    private String source;

    @ApiModelProperty(value = "来源文案")
    private String sourceLabel;

    @ApiModelProperty(value = "操作人ID")
    private Integer operatorId;

    @ApiModelProperty(value = "操作人名称")
    private String operatorName;

    @ApiModelProperty(value = "审核意见/提交说明")
    private String remark;

    @ApiModelProperty(value = "变更前状态")
    private Integer statusBefore;

    @ApiModelProperty(value = "变更后状态")
    private Integer statusAfter;

    @ApiModelProperty(value = "操作时间")
    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCertId() {
        return certId;
    }

    public void setCertId(Long certId) {
        this.certId = certId;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionLabel() {
        return actionLabel;
    }

    public void setActionLabel(String actionLabel) {
        this.actionLabel = actionLabel;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceLabel() {
        return sourceLabel;
    }

    public void setSourceLabel(String sourceLabel) {
        this.sourceLabel = sourceLabel;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatusBefore() {
        return statusBefore;
    }

    public void setStatusBefore(Integer statusBefore) {
        this.statusBefore = statusBefore;
    }

    public Integer getStatusAfter() {
        return statusAfter;
    }

    public void setStatusAfter(Integer statusAfter) {
        this.statusAfter = statusAfter;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
