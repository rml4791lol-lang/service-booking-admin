/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.setting.DictDataCreateValidate
 *  com.mdd.common.validator.annotation.IDMust
 *  com.mdd.common.validator.annotation.IntegerContains
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.DecimalMin
 *  javax.validation.constraints.NotNull
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.setting;

import com.mdd.common.validator.annotation.IDMust;
import com.mdd.common.validator.annotation.IntegerContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u5b57\u5178\u6570\u636e\u521b\u5efa\u53c2\u6570")
public class DictDataCreateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @IDMust(message="typeId\u53c2\u6570\u5fc5\u4f20\u4e14\u9700\u5927\u4e8e0")
    @ApiModelProperty(value="\u7c7b\u578bID", required=true)
    private Integer typeId;
    private String typeValue;
    @NotNull(message="name\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u952e", required=true)
    private @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String name;
    @NotNull(message="value\u53c2\u6570\u7f3a\u5931")
    @Length(max=100, message="\u952e\u540d\u4e0d\u80fd\u8d85\u51fa100\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u503c", required=true)
    private @NotNull(message="value\u53c2\u6570\u7f3a\u5931") @Length(max=100, message="\u952e\u540d\u4e0d\u80fd\u8d85\u51fa100\u4e2a\u5b57\u7b26") String value;
    @Length(max=200, message="\u6570\u503c\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u5907\u6ce8")
    private @Length(max=200, message="\u6570\u503c\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26") String remark;
    @DecimalMin(value="0", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5c11\u4e8e0")
    @ApiModelProperty(value="\u6392\u5e8f")
    private @DecimalMin(value="0", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5c11\u4e8e0") Integer sort;
    @NotNull(message="status\u53c2\u6570\u7f3a\u5931")
    @IntegerContains(values={0, 1}, message="status\u53c2\u6570\u4e0d\u5728\u5408\u6cd5\u503c\u5185")
    @ApiModelProperty(value="\u72b6\u6001", required=true)
    private @NotNull(message="status\u53c2\u6570\u7f3a\u5931") Integer status;

    public Integer getTypeId() {
        return this.typeId;
    }

    public String getTypeValue() {
        return this.typeValue;
    }

    @NotNull(message="name\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String getName() {
        return this.name;
    }

    @NotNull(message="value\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="value\u53c2\u6570\u7f3a\u5931") String getValue() {
        return this.value;
    }

    public String getRemark() {
        return this.remark;
    }

    public Integer getSort() {
        return this.sort;
    }

    @NotNull(message="status\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="status\u53c2\u6570\u7f3a\u5931") Integer getStatus() {
        return this.status;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public void setName(@NotNull(message="name\u53c2\u6570\u7f3a\u5931") @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String name) {
        this.name = name;
    }

    public void setValue(@NotNull(message="value\u53c2\u6570\u7f3a\u5931") @NotNull(message="value\u53c2\u6570\u7f3a\u5931") String value) {
        this.value = value;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setStatus(@NotNull(message="status\u53c2\u6570\u7f3a\u5931") @NotNull(message="status\u53c2\u6570\u7f3a\u5931") Integer status) {
        this.status = status;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DictDataCreateValidate)) {
            return false;
        }
        DictDataCreateValidate other = (DictDataCreateValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$typeId = this.getTypeId();
        Integer other$typeId = other.getTypeId();
        if (this$typeId == null ? other$typeId != null : !((Object)this$typeId).equals(other$typeId)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$typeValue = this.getTypeValue();
        String other$typeValue = other.getTypeValue();
        if (this$typeValue == null ? other$typeValue != null : !this$typeValue.equals(other$typeValue)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$value = this.getValue();
        String other$value = other.getValue();
        if (this$value == null ? other$value != null : !this$value.equals(other$value)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        return !(this$remark == null ? other$remark != null : !this$remark.equals(other$remark));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DictDataCreateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $typeId = this.getTypeId();
        result = result * 59 + ($typeId == null ? 43 : ((Object)$typeId).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $typeValue = this.getTypeValue();
        result = result * 59 + ($typeValue == null ? 43 : $typeValue.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $value = this.getValue();
        result = result * 59 + ($value == null ? 43 : $value.hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        return result;
    }

    public String toString() {
        return "DictDataCreateValidate(typeId=" + this.getTypeId() + ", typeValue=" + this.getTypeValue() + ", name=" + this.getName() + ", value=" + this.getValue() + ", remark=" + this.getRemark() + ", sort=" + this.getSort() + ", status=" + this.getStatus() + ")";
    }
}

