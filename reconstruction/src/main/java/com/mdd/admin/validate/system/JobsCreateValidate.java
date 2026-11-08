/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.system.JobsCreateValidate
 *  com.mdd.common.validator.annotation.IntegerContains
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.DecimalMin
 *  javax.validation.constraints.NotNull
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.system;

import com.mdd.common.validator.annotation.IntegerContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u7cfb\u7edf\u5c97\u4f4d\u521b\u5efa\u53c2\u6570")
public class JobsCreateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="code\u53c2\u6570\u7f3a\u5931")
    @Length(min=1, max=30, message="\u5c97\u4f4d\u7f16\u7801\u5fc5\u987b\u57281~30\u4e2a\u5b57\u7b26\u5185")
    @ApiModelProperty(value="\u7f16\u7801", required=true)
    private @NotNull(message="code\u53c2\u6570\u7f3a\u5931") @Length(min=1, max=30, message="\u5c97\u4f4d\u7f16\u7801\u5fc5\u987b\u57281~30\u4e2a\u5b57\u7b26\u5185") String code;
    @NotNull(message="name\u53c2\u6570\u7f3a\u5931")
    @Length(min=1, max=30, message="\u5c97\u4f4d\u540d\u79f0\u5fc5\u987b\u57281~30\u4e2a\u5b57\u7b26\u5185")
    @ApiModelProperty(value="\u5c97\u4f4d\u540d\u79f0", required=true)
    private @NotNull(message="name\u53c2\u6570\u7f3a\u5931") @Length(min=1, max=30, message="\u5c97\u4f4d\u540d\u79f0\u5fc5\u987b\u57281~30\u4e2a\u5b57\u7b26\u5185") String name;
    @NotNull(message="\u8bf7\u9009\u62e9\u72b6\u6001")
    @IntegerContains(values={0, 1})
    @ApiModelProperty(value="\u662f\u5426\u505c\u7528", required=true)
    private @NotNull(message="\u8bf7\u9009\u62e9\u72b6\u6001") Integer status = 0;
    @Length(max=250, message="\u5c97\u4f4d\u5907\u6ce8\u4e0d\u80fd\u5927\u4e8e250\u4e2a\u5b57\u7b26\u5185")
    @ApiModelProperty(value="\u5907\u6ce8\u4fe1\u606f")
    private @Length(max=250, message="\u5c97\u4f4d\u5907\u6ce8\u4e0d\u80fd\u5927\u4e8e250\u4e2a\u5b57\u7b26\u5185") String remark = "";
    @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    @DecimalMin(value="0", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5c11\u4e8e0")
    @ApiModelProperty(value="\u6392\u5e8f", required=true)
    private @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @DecimalMin(value="0", message="\u6392\u5e8f\u53f7\u503c\u4e0d\u80fd\u5c11\u4e8e0") Integer sort = 0;

    @NotNull(message="code\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="code\u53c2\u6570\u7f3a\u5931") String getCode() {
        return this.code;
    }

    @NotNull(message="name\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String getName() {
        return this.name;
    }

    @NotNull(message="\u8bf7\u9009\u62e9\u72b6\u6001")
    public @NotNull(message="\u8bf7\u9009\u62e9\u72b6\u6001") Integer getStatus() {
        return this.status;
    }

    public String getRemark() {
        return this.remark;
    }

    @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") Integer getSort() {
        return this.sort;
    }

    public void setCode(@NotNull(message="code\u53c2\u6570\u7f3a\u5931") @NotNull(message="code\u53c2\u6570\u7f3a\u5931") String code) {
        this.code = code;
    }

    public void setName(@NotNull(message="name\u53c2\u6570\u7f3a\u5931") @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String name) {
        this.name = name;
    }

    public void setStatus(@NotNull(message="\u8bf7\u9009\u62e9\u72b6\u6001") @NotNull(message="\u8bf7\u9009\u62e9\u72b6\u6001") Integer status) {
        this.status = status;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSort(@NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") @NotNull(message="\u6392\u5e8f\u53f7\u4e0d\u80fd\u4e3a\u7a7a") Integer sort) {
        this.sort = sort;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof JobsCreateValidate)) {
            return false;
        }
        JobsCreateValidate other = (JobsCreateValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        return !(this$remark == null ? other$remark != null : !this$remark.equals(other$remark));
    }

    protected boolean canEqual(Object other) {
        return other instanceof JobsCreateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        return result;
    }

    public String toString() {
        return "JobsCreateValidate(code=" + this.getCode() + ", name=" + this.getName() + ", status=" + this.getStatus() + ", remark=" + this.getRemark() + ", sort=" + this.getSort() + ")";
    }
}

