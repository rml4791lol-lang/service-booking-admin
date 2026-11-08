package com.mdd.admin.validate.activity;

import javax.validation.constraints.NotNull;

/**
 * 后台编辑活动（#186）：字段校验同新增，额外要求 id。
 */
public class ActivityUpdateValidate extends ActivityCreateValidate {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "活动ID不能为空")
    private Integer id;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
}
