package com.mdd.admin.vo.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "优惠券详情Vo")
public class CouponDetailVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Integer id;
    @ApiModelProperty(value = "券模板ID(0=手工创建)")
    private Integer templateId;
    @ApiModelProperty(value = "券编号")
    private String sn;
    @ApiModelProperty(value = "券名称")
    private String name;
    @ApiModelProperty(value = "抵扣金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "最低消费金额")
    private BigDecimal minAmount;
    @ApiModelProperty(value = "绑定用户ID")
    private Integer userId;
    @ApiModelProperty(value = "学员昵称")
    private String nickname;
    @ApiModelProperty(value = "学员真实姓名")
    private String realName;
    @ApiModelProperty(value = "绑定手机号")
    private String mobile;
    @ApiModelProperty(value = "状态:0=未使用,1=已使用,2=已过期")
    private Integer status;
    @ApiModelProperty(value = "状态描述")
    private String statusDesc;
    @ApiModelProperty(value = "使用时的订单ID")
    private Integer usedOrderId;
    @ApiModelProperty(value = "使用时间")
    private String usedTime;
    @ApiModelProperty(value = "生效时间")
    private Long startTime;
    @ApiModelProperty(value = "过期时间")
    private Long expireTime;
    @ApiModelProperty(value = "发券管理员ID")
    private Integer adminId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public Integer getUsedOrderId() {
        return usedOrderId;
    }

    public void setUsedOrderId(Integer usedOrderId) {
        this.usedOrderId = usedOrderId;
    }

    public String getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(String usedTime) {
        this.usedTime = usedTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
