package com.mdd.admin.validate.instructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@ApiModel(value = "教练账号开通参数")
public class InstructorAccountOpenValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id参数缺失")
    @ApiModelProperty(value = "教练ID", required = true)
    private Integer id;

    /**
     * 不传时由后端按配置或随机生成；只通过本次响应返回一次，不落日志。
     */
    @ApiModelProperty(value = "初始密码，可空")
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
