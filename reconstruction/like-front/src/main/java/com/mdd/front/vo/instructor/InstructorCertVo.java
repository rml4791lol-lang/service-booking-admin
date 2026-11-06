package com.mdd.front.vo.instructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 教练端资质证件输出（#49）。
 *
 * <p>status 使用页面可直接判断的字符串：draft/pending/approved/rejected；
 * statusCode 保留数据库数值，方便后续扩展。front 输出为绝对地址。</p>
 */
@ApiModel(value = "教练端资质证件")
public class InstructorCertVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资质ID")
    private Long id;

    @ApiModelProperty(value = "教练ID")
    private Integer instructorId;

    @ApiModelProperty(value = "证书类型")
    private String type;

    @ApiModelProperty(value = "证书名称")
    private String name;

    @ApiModelProperty(value = "证书编号")
    private String no;

    @ApiModelProperty(value = "发证机构")
    private String org;

    @ApiModelProperty(value = "有效期起 yyyy-MM-dd")
    private String expireStart;

    @ApiModelProperty(value = "有效期止 yyyy-MM-dd")
    private String expire;

    @ApiModelProperty(value = "证书照片绝对地址")
    private String front;

    @ApiModelProperty(value = "状态字符串:draft/pending/approved/rejected")
    private String status;

    @ApiModelProperty(value = "状态数值:0/1/2/3")
    private Integer statusCode;

    @ApiModelProperty(value = "状态文案")
    private String statusText;

    @ApiModelProperty(value = "审核意见")
    private String auditRemark;

    @ApiModelProperty(value = "审核人")
    private String auditAdminName;

    @ApiModelProperty(value = "审核时间")
    private String auditTime;

    @ApiModelProperty(value = "提交审核时间")
    private String submitTime;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "是否可编辑:草稿/已驳回")
    private Boolean editable;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
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

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }
}
