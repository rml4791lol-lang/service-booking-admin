package com.mdd.admin.validate.user;

import java.io.Serializable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 老课包课时调整入参。
 *
 * <p>总课时 / 剩余课时均可选（至少填一项，由服务层校验）；备注必填，
 * 用于操作日志追溯调整原因。</p>
 */
public class UserCourseAdjustValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "缺少课包id参数")
    @Min(value = 1, message = "课包id必须大于0")
    private Integer id;

    @Min(value = 0, message = "总课时不能为负数")
    private Integer totalLessons;

    @Min(value = 0, message = "剩余课时不能为负数")
    private Integer remainLessons;

    @NotBlank(message = "请填写调整备注")
    @Size(max = 500, message = "调整备注最长500位")
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(Integer totalLessons) {
        this.totalLessons = totalLessons;
    }

    public Integer getRemainLessons() {
        return remainLessons;
    }

    public void setRemainLessons(Integer remainLessons) {
        this.remainLessons = remainLessons;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
