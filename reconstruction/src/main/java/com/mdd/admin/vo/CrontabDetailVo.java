/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.vo.CrontabDetailVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u8ba1\u5212\u4efb\u52a1\u8be6\u60c5Vo")
public class CrontabDetailVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u4efb\u52a1ID")
    private Integer id;
    @ApiModelProperty(value="\u4efb\u52a1\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u7c7b\u578b 1-\u5b9a\u65f6\u4efb\u52a1")
    private Integer type;
    @ApiModelProperty(value="\u662f\u5426\u7cfb\u7edf\u4efb\u52a1 0-\u5426 1-\u662f")
    private Integer system;
    @ApiModelProperty(value="\u5907\u6ce8\u4fe1\u606f")
    private String remark;
    @ApiModelProperty(value="\u6267\u884c\u547d\u4ee4")
    private String command;
    @ApiModelProperty(value=" \u6267\u884c\u72b6\u6001: 1=\u6b63\u5728\u8fd0\u884c, 2=\u4efb\u52a1\u505c\u6b62, 3=\u53d1\u751f\u9519\u8bef")
    private Integer status;
    @ApiModelProperty(value=" \u53c2\u6570")
    private String params;
    @ApiModelProperty(value="\u6267\u884c\u89c4\u5219")
    private String expression;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getType() {
        return this.type;
    }

    public Integer getSystem() {
        return this.system;
    }

    public String getRemark() {
        return this.remark;
    }

    public String getCommand() {
        return this.command;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getParams() {
        return this.params;
    }

    public String getExpression() {
        return this.expression;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setSystem(Integer system) {
        this.system = system;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CrontabDetailVo)) {
            return false;
        }
        CrontabDetailVo other = (CrontabDetailVo)o;
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
        Integer this$system = this.getSystem();
        Integer other$system = other.getSystem();
        if (this$system == null ? other$system != null : !((Object)this$system).equals(other$system)) {
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
        String this$remark = this.getRemark();
        String other$remark = other.getRemark();
        if (this$remark == null ? other$remark != null : !this$remark.equals(other$remark)) {
            return false;
        }
        String this$command = this.getCommand();
        String other$command = other.getCommand();
        if (this$command == null ? other$command != null : !this$command.equals(other$command)) {
            return false;
        }
        String this$params = this.getParams();
        String other$params = other.getParams();
        if (this$params == null ? other$params != null : !this$params.equals(other$params)) {
            return false;
        }
        String this$expression = this.getExpression();
        String other$expression = other.getExpression();
        return !(this$expression == null ? other$expression != null : !this$expression.equals(other$expression));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CrontabDetailVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)$type).hashCode());
        Integer $system = this.getSystem();
        result = result * 59 + ($system == null ? 43 : ((Object)$system).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $remark = this.getRemark();
        result = result * 59 + ($remark == null ? 43 : $remark.hashCode());
        String $command = this.getCommand();
        result = result * 59 + ($command == null ? 43 : $command.hashCode());
        String $params = this.getParams();
        result = result * 59 + ($params == null ? 43 : $params.hashCode());
        String $expression = this.getExpression();
        result = result * 59 + ($expression == null ? 43 : $expression.hashCode());
        return result;
    }

    public String toString() {
        return "CrontabDetailVo(id=" + this.getId() + ", name=" + this.getName() + ", type=" + this.getType() + ", system=" + this.getSystem() + ", remark=" + this.getRemark() + ", command=" + this.getCommand() + ", status=" + this.getStatus() + ", params=" + this.getParams() + ", expression=" + this.getExpression() + ")";
    }
}

