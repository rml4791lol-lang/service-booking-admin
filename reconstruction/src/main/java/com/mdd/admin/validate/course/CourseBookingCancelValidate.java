package com.mdd.admin.validate.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 后台取消预约参数（#213 C1）。
 *
 * <p>按预约记录 ID 取消课节名单里的单条预约；remark 选填，仅用于操作日志留痕
 * （la_course_booking 无备注列，不落库）。</p>
 */
@ApiModel(value = "后台取消预约参数")
public class CourseBookingCancelValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "预约记录ID不能为空")
    @Min(value = 1, message = "预约记录ID不正确")
    @ApiModelProperty(value = "预约记录ID（la_course_booking.id）")
    private Integer id;

    @Size(max = 200, message = "取消原因最长200位")
    @ApiModelProperty(value = "取消原因（可选，仅记录操作日志）")
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
