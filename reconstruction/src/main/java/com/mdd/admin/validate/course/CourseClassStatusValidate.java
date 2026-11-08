package com.mdd.admin.validate.course;

import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 课程上下架（#44）。
 *
 * <p>isShow：1=上架（小程序列表/详情可见），0=下架（小程序不展示）。</p>
 */
public class CourseClassStatusValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message="课程ID不能为空")
    private Integer id;

    @NotNull(message="上架状态不能为空")
    @Min(value=0, message="上架状态只能是 0 或 1")
    @Max(value=1, message="上架状态只能是 0 或 1")
    private Integer isShow;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }
}
