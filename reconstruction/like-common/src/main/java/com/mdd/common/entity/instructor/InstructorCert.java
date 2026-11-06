package com.mdd.common.entity.instructor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

/**
 * 教练资质证件（#49）。
 *
 * <p>一条资质一条记录，结构化保存证书类型/名称/编号/发证机构/有效期/照片，
 * 并记录草稿/待审核/已通过/已驳回状态与审核人、审核时间、审核意见。
 * 旧字段 {@code la_instructor.certification/cert_images} 只保留兼容展示，不再写入。</p>
 */
@ApiModel(value = "教练资质证件实体")
@TableName("la_instructor_cert")
public class InstructorCert implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 草稿：教练可继续编辑。 */
    public static final int STATUS_DRAFT = 0;
    /** 待审核：已提交，教练不可编辑，等待后台审核。 */
    public static final int STATUS_PENDING = 1;
    /** 已通过：审核通过，对外展示。 */
    public static final int STATUS_APPROVED = 2;
    /** 已驳回：审核拒绝，教练可修改后重新提交。 */
    public static final int STATUS_REJECTED = 3;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "教练ID(la_instructor.id)")
    private Integer instructorId;

    @ApiModelProperty(value = "证书类型")
    private String type;

    @ApiModelProperty(value = "证书名称")
    private String name;

    @ApiModelProperty(value = "证书编号")
    private String no;

    @ApiModelProperty(value = "发证机构")
    private String org;

    @ApiModelProperty(value = "有效期起")
    private Date expireStart;

    @ApiModelProperty(value = "有效期止")
    private Date expire;

    @ApiModelProperty(value = "证书照片(相对路径)")
    private String front;

    @ApiModelProperty(value = "状态:0=草稿,1=待审核,2=已通过,3=已驳回")
    private Integer status;

    @ApiModelProperty(value = "审核管理员ID")
    private Integer auditAdminId;

    @ApiModelProperty(value = "审核管理员名称快照")
    private String auditAdminName;

    @ApiModelProperty(value = "审核时间")
    private Date auditTime;

    @ApiModelProperty(value = "审核意见(驳回必填)")
    private String auditRemark;

    @ApiModelProperty(value = "最后提交审核时间")
    private Date submitTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "删除时间(软删)")
    private Date deleteTime;

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

    public Date getExpireStart() {
        return expireStart;
    }

    public void setExpireStart(Date expireStart) {
        this.expireStart = expireStart;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
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

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }
}
