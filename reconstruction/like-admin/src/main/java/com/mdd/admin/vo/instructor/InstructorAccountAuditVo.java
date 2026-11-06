package com.mdd.admin.vo.instructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "教练账号审计Vo")
public class InstructorAccountAuditVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "审计ID")
    private Long id;
    @ApiModelProperty(value = "教练ID")
    private Integer instructorId;
    @ApiModelProperty(value = "登录账号ID")
    private Integer userId;
    @ApiModelProperty(value = "动作")
    private String action;
    @ApiModelProperty(value = "动作文案")
    private String actionLabel;
    @ApiModelProperty(value = "来源")
    private String source;
    @ApiModelProperty(value = "来源文案")
    private String sourceLabel;
    @ApiModelProperty(value = "操作人ID")
    private Integer operatorId;
    @ApiModelProperty(value = "操作人名称")
    private String operatorName;
    @ApiModelProperty(value = "操作IP")
    private String ip;
    @ApiModelProperty(value = "审计说明")
    private String detail;
    @ApiModelProperty(value = "结果:1=成功,0=失败")
    private Integer result;
    @ApiModelProperty(value = "操作时间")
    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
