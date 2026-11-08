package com.mdd.common.entity.instructor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

/**
 * 教练资质审核留痕（#49）。
 *
 * <p>每次提交/通过/驳回写一条，保留证书名称与状态变更前后值；
 * 驳回后重新提交时，历史驳回原因仍可在审计记录中追溯。</p>
 */
@ApiModel(value = "教练资质审核留痕实体")
@TableName("la_instructor_cert_audit")
public class InstructorCertAudit implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "资质ID")
    private Long certId;

    @ApiModelProperty(value = "教练ID")
    private Integer instructorId;

    @ApiModelProperty(value = "证书名称快照")
    private String certName;

    @ApiModelProperty(value = "动作:SUBMIT/APPROVE/REJECT")
    private String action;

    @ApiModelProperty(value = "来源:COACH/ADMIN/SYSTEM")
    private String source;

    @ApiModelProperty(value = "操作人ID")
    private Integer operatorId;

    @ApiModelProperty(value = "操作人名称快照")
    private String operatorName;

    @ApiModelProperty(value = "操作IP")
    private String ip;

    @ApiModelProperty(value = "审核意见/提交说明")
    private String remark;

    @ApiModelProperty(value = "变更前状态")
    private Integer statusBefore;

    @ApiModelProperty(value = "变更后状态")
    private Integer statusAfter;

    @ApiModelProperty(value = "操作时间")
    private Date createTime;

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

    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
