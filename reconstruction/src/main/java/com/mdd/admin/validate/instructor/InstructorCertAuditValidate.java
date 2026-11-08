package com.mdd.admin.validate.instructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 教练资质审核参数（#49）。
 *
 * <p>通过/驳回二选一；驳回原因必填，且会写入资质当前审核意见与审核留痕表。</p>
 */
@ApiModel(value = "教练资质审核参数")
public class InstructorCertAuditValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id参数缺失")
    @ApiModelProperty(value = "资质ID", required = true)
    private Long id;

    @NotBlank(message = "请选择审核结果")
    @Pattern(regexp = "^(approve|reject)$", message = "审核结果只能是approve或reject")
    @ApiModelProperty(value = "审核结果:approve/reject", required = true)
    private String action;

    @Size(max = 500, message = "审核意见不能超过500个字符")
    @ApiModelProperty(value = "审核意见，驳回时必填")
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
