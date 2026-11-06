package com.mdd.admin.validate.instructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@ApiModel(value = "教练账号启用参数")
public class InstructorAccountEnableValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id参数缺失")
    @ApiModelProperty(value = "教练ID", required = true)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
