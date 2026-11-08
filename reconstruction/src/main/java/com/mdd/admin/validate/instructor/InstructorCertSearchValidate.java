package com.mdd.admin.validate.instructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 教练资质列表筛选（#49）。
 */
@ApiModel(value = "教练资质列表筛选参数")
public class InstructorCertSearchValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "状态:0=草稿,1=待审核,2=已通过,3=已驳回")
    private Integer status;

    @ApiModelProperty(value = "教练ID")
    private Integer instructorId;

    @ApiModelProperty(value = "证书类型")
    private String type;

    @ApiModelProperty(value = "关键词：证书名称/编号/机构")
    private String keyword;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
