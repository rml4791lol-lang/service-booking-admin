package com.mdd.front.validate.instructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 教练资质提交审核参数（#49）。
 *
 * <p>支持一次提交多张：页面既可单张提交，也可把全部草稿一起提交。
 * 只允许提交本人名下、状态为草稿或已驳回的资质。</p>
 */
@ApiModel(value = "教练资质提交审核参数")
public class InstructorCertSubmitValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "请选择要提交的资质")
    @Size(max = 20, message = "一次最多提交20张资质")
    @ApiModelProperty(value = "资质ID列表")
    private List<@NotNull(message = "资质ID不能为空") Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
