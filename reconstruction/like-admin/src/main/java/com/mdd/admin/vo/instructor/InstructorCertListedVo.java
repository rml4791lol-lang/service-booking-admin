package com.mdd.admin.vo.instructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 后台教练资质列表/详情输出（#49）。
 */
@ApiModel(value = "后台教练资质")
public class InstructorCertListedVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资质ID")
    private Long id;

    @ApiModelProperty(value = "教练ID")
    private Integer instructorId;

    @ApiModelProperty(value = "教练姓名")
    private String instructorName;

    @ApiModelProperty(value = "教练手机号")
    private String instructorPhone;

    @ApiModelProperty(value = "证书类型")
    private String type;

    @ApiModelProperty(value = "证书名称")
    private String name;

    @ApiModelProperty(value = "证书编号")
    private String no;

    @ApiModelProperty(value = "发证机构")
    private String org;

    @ApiModelProperty(value = "有效期起")
    private String expireStart;

    @ApiModelProperty(value = "有效期止")
    private String expire;

    @ApiModelProperty(value = "证书照片绝对地址")
    private String front;

    @ApiModelProperty(value = "状态数值")
    private Integer status;

    @ApiModelProperty(value = "状态key")
    private String statusKey;

    @ApiModelProperty(value = "状态文案")
    private String statusLabel;

    @ApiModelProperty(value = "审核管理员ID")
    private Integer auditAdminId;

    @ApiModelProperty(value = "审核管理员名称")
    private String auditAdminName;

    @ApiModelProperty(value = "审核时间")
    private String auditTime;

    @ApiModelProperty(value = "审核意见")
    private String auditRemark;

    @ApiModelProperty(value = "提交审核时间")
    private String submitTime;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "是否可审核:仅待审核")
    private Boolean canAudit;

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

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorPhone() {
        return instructorPhone;
    }

    public void setInstructorPhone(String instructorPhone) {
        this.instructorPhone = instructorPhone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getExpireStart() {
        return expireStart;
    }

    public void setExpireStart(String expireStart) {
        this.expireStart = expireStart;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(String statusKey) {
        this.statusKey = statusKey;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public Integer getAuditAdminId() {
        return auditAdminId;
    }

    public void setAuditAdminId(Integer auditAdminId) {
        this.auditAdminId = auditAdminId;
    }

    public String getAuditAdminName() {
        return auditAdminName;
    }

    public void setAuditAdminName(String auditAdminName) {
        this.auditAdminName = auditAdminName;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getCanAudit() {
        return canAudit;
    }

    public void setCanAudit(Boolean canAudit) {
        this.canAudit = canAudit;
    }
}
