package com.mdd.admin.validate.instructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@ApiModel(value = "教练账号重置密码参数")
public class InstructorAccountResetValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id参数缺失")
    @ApiModelProperty(value = "教练ID", required = true)
    private Integer id;

    @ApiModelProperty(value = "新的初始密码，可空")
    private String initialPassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInitialPassword() {
        return initialPassword;
    }

    public void setInitialPassword(String initialPassword) {
        this.initialPassword = initialPassword;
    }
}
