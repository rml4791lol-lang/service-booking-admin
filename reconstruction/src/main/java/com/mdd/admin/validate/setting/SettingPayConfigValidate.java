/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.validate.setting.SettingPayConfigValidate
 *  com.mdd.common.validator.annotation.IDMust
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.admin.validate.setting;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u652f\u4ed8\u6e20\u9053\u8bbe\u7f6e\u53c2\u6570")
public class SettingPayConfigValidate {
    @IDMust(message="id\u53c2\u6570\u5fc5\u4f20\u4e14\u9700\u5927\u4e8e0")
    @ApiModelProperty(value="ID", required=true)
    private Integer id;
    @NotNull(message="name\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u6a21\u7248\u540d\u79f0", required=true)
    private @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String name;
    @ApiModelProperty(value="\u6a21\u7248\u540d\u79f0")
    private String icon;
    @ApiModelProperty(value="\u6392\u5e8f\u7f16\u53f7")
    private Integer sort;
    @ApiModelProperty(value="\u5907\u6ce8\u4fe1\u606f")
    private String remark;
    /** 支付方式（1=余额 / 2=微信 / 3=支付宝）：编辑页据此渲染对应参数表单；保存时以库内记录为准，不回写。 */
    @ApiModelProperty(value="\u652f\u4ed8\u65b9\u5f0f\uff08\u7f16\u8f91\u9875\u56de\u663e\u7528\uff0c\u4fdd\u5b58\u65f6\u5ffd\u7565\uff09")
    private Integer pay_way;
    @ApiModelProperty(value="domain")
    private String domain;
    @ApiModelProperty(value="\u914d\u7f6e\u53c2\u6570")
    private JSONObject config;

    public Integer getId() {
        return this.id;
    }

    @NotNull(message="name\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String getName() {
        return this.name;
    }

    public String getIcon() {
        return this.icon;
    }

    public Integer getSort() {
        return this.sort;
    }

    public String getRemark() {
        return this.remark;
    }


    public Integer getPay_way() {
        return this.pay_way;
    }

    public void setPay_way(Integer pay_way) {
        this.pay_way = pay_way;
    }

    public String getDomain() {
        return this.domain;
    }

    public JSONObject getConfig() {
        return this.config;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(@NotNull(message="name\u53c2\u6570\u7f3a\u5931") @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingPayConfigValidate)) {
            return false;
        }
        SettingPayConfigValidate other = (SettingPayConfigValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$icon = this.getIcon();
        String other$icon = other.getIcon();
        if (this$icon == null ? other$icon != null : !this$icon.equals(other$icon)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        if (this$remark == null ? other$remark != null : !this$remark.equals(other$remark)) {
            return false;
        }
        String this$domain = this.getDomain();
        String other$domain = other.getDomain();
        if (this$domain == null ? other$domain != null : !this$domain.equals(other$domain)) {
            return false;
        }
        JSONObject this$config = this.getConfig();
        JSONObject other$config = other.getConfig();
        return !(this$config == null ? other$config != null : !this$config.equals(other$config));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingPayConfigValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $icon = this.getIcon();
        result = result * 59 + ($icon == null ? 43 : $icon.hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        String $domain = this.getDomain();
        result = result * 59 + ($domain == null ? 43 : $domain.hashCode());
        JSONObject $config = this.getConfig();
        result = result * 59 + ($config == null ? 43 : $config.hashCode());
        return result;
    }

    public String toString() {
        return "SettingPayConfigValidate(id=" + this.getId() + ", name=" + this.getName() + ", icon=" + this.getIcon() + ", sort=" + this.getSort() + ", remark=" + this.getRemark() + ", domain=" + this.getDomain() + ", config=" + this.getConfig() + ")";
    }
}

