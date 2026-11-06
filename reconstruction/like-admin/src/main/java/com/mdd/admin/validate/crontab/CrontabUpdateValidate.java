/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.crontab.CrontabUpdateValidate
 *  com.mdd.common.validator.annotation.IDMust
 *  com.mdd.common.validator.annotation.IntegerContains
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.crontab;

import com.mdd.common.validator.annotation.IDMust;
import com.mdd.common.validator.annotation.IntegerContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u8ba1\u5212\u4efb\u52a1\u66f4\u65b0\u53c2\u6570")
public class CrontabUpdateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @IDMust(message="id\u53c2\u6570\u5fc5\u4f20\u4e14\u9700\u5927\u4e8e0")
    @ApiModelProperty(value="id", required=true)
    private Integer id;
    @NotNull(message="name\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u4efb\u52a1\u540d\u79f0", required=true)
    private @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String name;
    @NotNull(message="types\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u7c7b\u578b 1-\u5b9a\u65f6\u4efb\u52a1")
    private @NotNull(message="types\u53c2\u6570\u7f3a\u5931") Integer type;
    @NotNull(message="command\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u6267\u884c\u6307\u4ee4", required=true)
    private @NotNull(message="command\u53c2\u6570\u7f3a\u5931") String command;
    @NotNull(message="rules\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u6267\u884c\u89c4\u5219", required=true)
    private @NotNull(message="rules\u53c2\u6570\u7f3a\u5931") String expression;
    @Length(max=200, message="remark\u53c2\u6570\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u5907\u6ce8")
    private @Length(max=200, message="remark\u53c2\u6570\u4e0d\u80fd\u8d85\u51fa200\u4e2a\u5b57\u7b26") String remark;
    @NotNull(message="status\u53c2\u6570\u7f3a\u5931")
    @IntegerContains(values={1, 2, 3}, message="status\u53c2\u6570\u53d6\u503c\u5f02\u5e38")
    @ApiModelProperty(value="\u72b6\u6001")
    private @NotNull(message="status\u53c2\u6570\u7f3a\u5931") Integer status;
    @ApiModelProperty(value=" \u53c2\u6570")
    private String params;

    public Integer getId() {
        return this.id;
    }

    @NotNull(message="name\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String getName() {
        return this.name;
    }

    @NotNull(message="types\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="types\u53c2\u6570\u7f3a\u5931") Integer getType() {
        return this.type;
    }

    @NotNull(message="command\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="command\u53c2\u6570\u7f3a\u5931") String getCommand() {
        return this.command;
    }

    @NotNull(message="rules\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="rules\u53c2\u6570\u7f3a\u5931") String getExpression() {
        return this.expression;
    }

    public String getRemark() {
        return this.remark;
    }

    @NotNull(message="status\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="status\u53c2\u6570\u7f3a\u5931") Integer getStatus() {
        return this.status;
    }

    public String getParams() {
        return this.params;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(@NotNull(message="name\u53c2\u6570\u7f3a\u5931") @NotNull(message="name\u53c2\u6570\u7f3a\u5931") String name) {
        this.name = name;
    }

    public void setType(@NotNull(message="types\u53c2\u6570\u7f3a\u5931") @NotNull(message="types\u53c2\u6570\u7f3a\u5931") Integer type) {
        this.type = type;
    }

    public void setCommand(@NotNull(message="command\u53c2\u6570\u7f3a\u5931") @NotNull(message="command\u53c2\u6570\u7f3a\u5931") String command) {
        this.command = command;
    }

    public void setExpression(@NotNull(message="rules\u53c2\u6570\u7f3a\u5931") @NotNull(message="rules\u53c2\u6570\u7f3a\u5931") String expression) {
        this.expression = expression;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setStatus(@NotNull(message="status\u53c2\u6570\u7f3a\u5931") @NotNull(message="status\u53c2\u6570\u7f3a\u5931") Integer status) {
        this.status = status;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CrontabUpdateValidate)) {
            return false;
        }
        CrontabUpdateValidate other = (CrontabUpdateValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$type = this.getType();
        Integer other$type = other.getType();
        if (this$type == null ? other$type != null : !((Object)this$type).equals(other$type)) {
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
        String this$command = this.getCommand();
        String other$command = other.getCommand();
        if (this$command == null ? other$command != null : !this$command.equals(other$command)) {
            return false;
        }
        String this$expression = this.getExpression();
        String other$expression = other.getExpression();
        if (this$expression == null ? other$expression != null : !this$expression.equals(other$expression)) {
            return false;
        }
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        if (this$remark == null ? other$remark != null : !this$remark.equals(other$remark)) {
            return false;
        }
        String this$params = this.getParams();
        String other$params = other.getParams();
        return !(this$params == null ? other$params != null : !this$params.equals(other$params));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CrontabUpdateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)$type).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $command = this.getCommand();
        result = result * 59 + ($command == null ? 43 : $command.hashCode());
        String $expression = this.getExpression();
        result = result * 59 + ($expression == null ? 43 : $expression.hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        String $params = this.getParams();
        result = result * 59 + ($params == null ? 43 : $params.hashCode());
        return result;
    }

    public String toString() {
        return "CrontabUpdateValidate(id=" + this.getId() + ", name=" + this.getName() + ", type=" + this.getType() + ", command=" + this.getCommand() + ", expression=" + this.getExpression() + ", remark=" + this.getRemark() + ", status=" + this.getStatus() + ", params=" + this.getParams() + ")";
    }
}

