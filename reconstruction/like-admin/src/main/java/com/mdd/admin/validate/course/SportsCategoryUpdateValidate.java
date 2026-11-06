package com.mdd.admin.validate.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u8fd0\u52a8\u9879\u76ee\u5206\u7c7b\u7f16\u8f91\u53c2\u6570")
public class SportsCategoryUpdateValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message="\u5206\u7c7bID\u4e0d\u80fd\u4e3a\u7a7a")
    @ApiModelProperty(value="\u5206\u7c7bID")
    private Integer id;

    @ApiModelProperty(value="\u9879\u76ee\u540d\u79f0(\u4e0d\u4f20=\u4e0d\u4fee\u6539)")
    private String name;

    @Min(value=0, message="\u7236\u7ea7ID\u4e0d\u5408\u6cd5")
    @ApiModelProperty(value="\u7236\u7ea7ID(0=\u79fb\u5230\u6839\u7ea7,\u4e0d\u4f20=\u4e0d\u4fee\u6539)")
    private Integer parentId;

    @ApiModelProperty(value="\u56fe\u6807(\u4e0d\u4f20=\u4e0d\u4fee\u6539)")
    @Length(max=255, message="\u56fe\u6807\u5730\u5740\u8fc7\u957f")
    private String icon;

    @Min(value=0, message="\u6392\u5e8f\u4e0d\u80fd\u4e3a\u8d1f\u6570")
    @Max(value=65535, message="\u6392\u5e8f\u8fc7\u5927")
    @ApiModelProperty(value="\u6392\u5e8f(\u4e0d\u4f20=\u4e0d\u4fee\u6539)")
    private Integer sort;

    @Min(value=0, message="\u663e\u793a\u72b6\u6001\u4e0d\u5408\u6cd5")
    @Max(value=1, message="\u663e\u793a\u72b6\u6001\u4e0d\u5408\u6cd5")
    @ApiModelProperty(value="\u662f\u5426\u663e\u793a: 0=\u9690\u85cf, 1=\u663e\u793a(\u4e0d\u4f20=\u4e0d\u4fee\u6539)")
    private Integer isShow;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSort() {
        return this.sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getIsShow() {
        return this.isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }
}
