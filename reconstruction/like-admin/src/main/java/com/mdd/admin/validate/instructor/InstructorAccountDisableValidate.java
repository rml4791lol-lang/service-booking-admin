package com.mdd.admin.validate.instructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(value = "教练账号停用参数")
public class InstructorAccountDisableValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id参数缺失")
    @ApiModelProperty(value = "教练ID", required = true)
    private Integer id;

    @Size(max = 200, message = "停用原因不能超过200个字符")
    @ApiModelProperty(value = "停用原因，可空")
    private String reason;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
