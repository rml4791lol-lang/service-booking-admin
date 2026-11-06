package com.mdd.common.entity.instructor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

/**
 * 教练账号操作审计。
 *
 * <p>只记录“谁在什么时候对哪个教练账号做了什么”，不记录任何明文口令、
 * 口令摘要或初始密码。开通/重置/停用/启用/教练自助改密都会写入本表。</p>
 */
@ApiModel(value = "教练账号操作审计实体")
@TableName("la_instructor_account_audit")
public class InstructorAccountAudit implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "教练ID")
    private Integer instructorId;

    @ApiModelProperty(value = "登录账号ID")
    private Integer userId;

    @ApiModelProperty(value = "动作:OPEN/RESET/DISABLE/ENABLE/CHANGE_PASSWORD")
    private String action;

    @ApiModelProperty(value = "来源:ADMIN/COACH/SYSTEM")
    private String source;

    @ApiModelProperty(value = "操作人ID")
    private Integer operatorId;

    @ApiModelProperty(value = "操作人名称")
    private String operatorName;

    @ApiModelProperty(value = "操作IP")
    private String ip;

    @ApiModelProperty(value = "审计说明，不含口令")
    private String detail;

    @ApiModelProperty(value = "结果:1=成功,0=失败")
    private Integer result;

    @ApiModelProperty(value = "操作时间")
    private Date createTime;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
