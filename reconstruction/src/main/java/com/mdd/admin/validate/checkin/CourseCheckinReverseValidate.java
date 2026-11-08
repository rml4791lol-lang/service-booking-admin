package com.mdd.admin.validate.checkin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 后台核销撤销参数（#213 B1/C4）。原因必填；studentIds 为空表示撤销整场。
 */
@ApiModel(value = "核销撤销参数")
public class CourseCheckinReverseValidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "场次不存在")
    @ApiModelProperty(value = "场次ID（ys_course_lesson.id）", required = true)
    private Integer lessonId;

    @ApiModelProperty(value = "限定学员ID列表；为空=撤销本场全部已核销学员")
    private List<Integer> studentIds;

    @NotBlank(message = "撤销原因不能为空")
    @ApiModelProperty(value = "撤销原因（必填）", required = true)
    private String reason;

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
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
