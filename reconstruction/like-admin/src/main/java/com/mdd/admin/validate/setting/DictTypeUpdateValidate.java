/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.setting.DictTypeUpdateValidate
 *  com.mdd.common.validator.annotation.IDMust
 *  com.mdd.common.validator.annotation.IntegerContains
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.setting;

import com.mdd.common.validator.annotation.IDMust;
import com.mdd.common.validator.annotation.IntegerContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u5b57\u5178\u7c7b\u578b\u66f4\u65b0\u53c2\u6570")
public class DictTypeUpdateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @IDMust(message="id\u53c2\u6570\u5fc5\u4f20\u4e14\u9700\u5927\u4e8e0")
    @ApiModelProperty(value="ID", required=true)
    private Integer id;
    @NotNull(message="name\u53c2\u6570\u7f3a\u5931")
    @Length(max=200, message="\u540d\u79f0\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u540d\u79f0", required=true)
    private @NotNull(message="name\u53c2\u6570\u7f3a\u5931") @Length(max=200, message="\u540d\u79f0\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26") String name;
    @NotNull(message="type\u53c2\u6570\u7f3a\u5931")
    @Length(max=200, message="\u7c7b\u578b\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u7c7b\u578b", required=true)
    private @NotNull(message="type\u53c2\u6570\u7f3a\u5931") @Length(max=200, message="\u7c7b\u578b\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26") String type;
    @Length(max=200, message="\u5907\u6ce8\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u5907\u6ce8")
    private @Length(max=200, message="\u5907\u6ce8\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26") String remark;
    @NotNull(message="dictStatus\u53c2\u6570\u7f3a\u5931")
    @IntegerContains(values={0, 1}, message="dictStatus\u53c2\u6570\u4e0d\u5728\u5408\u6cd5\u503c\u5185")
    @ApiModelProperty(value="\u72b6\u6001", required=true)
    private @NotNull(message="dictStatus\u53c2\u6570\u7f3a\u5931") Integer status;

    public Integer getId() {
        return this.id;
    }

    @NotNull(message="name\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String getName() {
        return this.name;
    }

    @NotNull(message="type\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="type\u53c2\u6570\u7f3a\u5931") String getType() {
        return this.type;
    }

    public String getRemark() {
        return this.remark;
    }

    @NotNull(message="dictStatus\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="dictStatus\u53c2\u6570\u7f3a\u5931") Integer getStatus() {
        return this.status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(@NotNull(message="name\u53c2\u6570\u7f3a\u5931") @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String name) {
        this.name = name;
    }

    public void setType(@NotNull(message="type\u53c2\u6570\u7f3a\u5931") @NotNull(message="type\u53c2\u6570\u7f3a\u5931") String type) {
        this.type = type;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setStatus(@NotNull(message="dictStatus\u53c2\u6570\u7f3a\u5931") @NotNull(message="dictStatus\u53c2\u6570\u7f3a\u5931") Integer status) {
        this.status = status;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DictTypeUpdateValidate)) {
            return false;
        }
        DictTypeUpdateValidate other = (DictTypeUpdateValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$type = this.getType();
        String other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        return !(this$remark == null ? other$remark != null : !this$remark.equals(other$remark));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DictTypeUpdateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        return result;
    }

    public String toString() {
        return "DictTypeUpdateValidate(id=" + this.getId() + ", name=" + this.getName() + ", type=" + this.getType() + ", remark=" + this.getRemark() + ", status=" + this.getStatus() + ")";
    }
}

