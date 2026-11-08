/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.file.FileCateValidate
 *  com.mdd.common.validator.annotation.IntegerContains
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.Min
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.file;

import com.mdd.common.validator.annotation.IntegerContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u9644\u4ef6\u5206\u7c7b\u53c2\u6570")
public class FileCateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="\u7f3a\u5c11pid\u53c2\u6570")
    @Min(value=0L, message="pid\u53c2\u6570\u5fc5\u987b\u4e3a\u6570\u5b57")
    @ApiModelProperty(value="\u4e0a\u7ea7ID", required=true)
    private @NotNull(message="\u7f3a\u5c11pid\u53c2\u6570") @Min(value=0L, message="pid\u53c2\u6570\u5fc5\u987b\u4e3a\u6570\u5b57") Integer pid;
    @NotNull(message="\u7f3a\u5c11type\u53c2\u6570")
    @IntegerContains(values={10, 20, 30}, message="type\u4e0d\u5728\u5408\u6cd5\u503c\u5185")
    @ApiModelProperty(value="\u9644\u4ef6\u7c7b\u578b", required=true)
    private @NotNull(message="\u7f3a\u5c11type\u53c2\u6570") Integer type;
    @NotEmpty(message="\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=1, max=30, message="\u540d\u79f0\u4e0d\u80fd\u5927\u4e8e30\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u9644\u4ef6\u540d\u79f0", required=true)
    private @NotEmpty(message="\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=1, max=30, message="\u540d\u79f0\u4e0d\u80fd\u5927\u4e8e30\u4e2a\u5b57\u7b26") String name;

    @NotNull(message="\u7f3a\u5c11pid\u53c2\u6570")
    public @NotNull(message="\u7f3a\u5c11pid\u53c2\u6570") Integer getPid() {
        return this.pid;
    }

    @NotNull(message="\u7f3a\u5c11type\u53c2\u6570")
    public @NotNull(message="\u7f3a\u5c11type\u53c2\u6570") Integer getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public void setPid(@NotNull(message="\u7f3a\u5c11pid\u53c2\u6570") @NotNull(message="\u7f3a\u5c11pid\u53c2\u6570") Integer pid) {
        this.pid = pid;
    }

    public void setType(@NotNull(message="\u7f3a\u5c11type\u53c2\u6570") @NotNull(message="\u7f3a\u5c11type\u53c2\u6570") Integer type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FileCateValidate)) {
            return false;
        }
        FileCateValidate other = (FileCateValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$pid = this.getPid();
        Integer other$pid = other.getPid();
        if (this$pid == null ? other$pid != null : !((Object)this$pid).equals(other$pid)) {
            return false;
        }
        Integer this$type = this.getType();
        Integer other$type = other.getType();
        if (this$type == null ? other$type != null : !((Object)this$type).equals(other$type)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        return !(this$name == null ? other$name != null : !this$name.equals(other$name));
    }

    protected boolean canEqual(Object other) {
        return other instanceof FileCateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $pid = this.getPid();
        result = result * 59 + ($pid == null ? 43 : ((Object)$pid).hashCode());
        Integer $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)$type).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "FileCateValidate(pid=" + this.getPid() + ", type=" + this.getType() + ", name=" + this.getName() + ")";
    }
}

