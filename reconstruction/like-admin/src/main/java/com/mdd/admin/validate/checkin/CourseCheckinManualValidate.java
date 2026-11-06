package com.mdd.admin.validate.checkin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 后台人工补核销参数（T30.4 / #37）。原因必填，至少选择一名学员。
 */
@ApiModel(value = "人工补核销参数")
public class CourseCheckinManualValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "场次不存在")
    @ApiModelProperty(value = "场次ID", required = true)
    private Integer sessionId;

    @NotEmpty(message = "请至少选择一名待核销学员")
    @ApiModelProperty(value = "学员ID列表", required = true)
    private List<Integer> studentIds;

    @NotBlank(message = "补核销原因不能为空")
    @ApiModelProperty(value = "补核销原因（必填）", required = true)
    private String reason;

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public List<Integer> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Integer> studentIds) {
        this.studentIds = studentIds;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
