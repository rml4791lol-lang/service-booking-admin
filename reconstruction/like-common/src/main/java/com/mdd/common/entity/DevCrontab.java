/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableField
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u8ba1\u5212\u4efb\u52a1\u5b9e\u4f53")
public class DevCrontab
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u4efb\u52a1\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u7c7b\u578b 1-\u5b9a\u65f6\u4efb\u52a1")
    private Integer type;
    @ApiModelProperty(value="\u662f\u5426\u7cfb\u7edf\u4efb\u52a1 0-\u5426 1-\u662f")
    @TableField(value="`system`")
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
    @ApiModelProperty(value="\u9519\u8bef\u4fe1\u606f")
    private String error;
    @ApiModelProperty(value="\u7ed3\u675f\u65f6\u95f4")
    private Long lastTime;
    @ApiModelProperty(value="\u6700\u5927\u6267\u884c\u65f6\u957f")
    private Long time;
    @ApiModelProperty(value="\u6700\u5927\u6267\u884c\u65f6\u957f")
    private Long maxTime;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long createTime;
    @ApiModelProperty(value="\u66f4\u65b0\u65f6\u95f4")
    private Long updateTime;
    @ApiModelProperty(value="\u5220\u9664\u65f6\u95f4")
    private Long deleteTime;

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

    public String getError() {
        return this.error;
    }

    public Long getLastTime() {
        return this.lastTime;
    }

    public Long getTime() {
        return this.time;
    }

    public Long getMaxTime() {
        return this.maxTime;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public Long getDeleteTime() {
        return this.deleteTime;
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

    public void setError(String error) {
        this.error = error;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setMaxTime(Long maxTime) {
        this.maxTime = maxTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleteTime(Long deleteTime) {
        this.deleteTime = deleteTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DevCrontab)) {
            return false;
        }
        DevCrontab other = (DevCrontab)o;
        if (!other.canEqual(this)) {
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
        Long this$lastTime = this.getLastTime();
        Long other$lastTime = other.getLastTime();
        if (this$lastTime == null ? other$lastTime != null : !((Object)this$lastTime).equals(other$lastTime)) {
            return false;
        }
        Long this$time = this.getTime();
        Long other$time = other.getTime();
        if (this$time == null ? other$time != null : !((Object)this$time).equals(other$time)) {
            return false;
        }
        Long this$maxTime = this.getMaxTime();
        Long other$maxTime = other.getMaxTime();
        if (this$maxTime == null ? other$maxTime != null : !((Object)this$maxTime).equals(other$maxTime)) {
            return false;
        }
        Long this$createTime = this.getCreateTime();
        Long other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Long this$updateTime = this.getUpdateTime();
        Long other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        Long this$deleteTime = this.getDeleteTime();
        Long other$deleteTime = other.getDeleteTime();
        if (this$deleteTime == null ? other$deleteTime != null : !((Object)this$deleteTime).equals(other$deleteTime)) {
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
        if (this$expression == null ? other$expression != null : !this$expression.equals(other$expression)) {
            return false;
        }
        String this$error = this.getError();
        String other$error = other.getError();
        return !(this$error == null ? other$error != null : !this$error.equals(other$error));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DevCrontab;
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
        Long $lastTime = this.getLastTime();
        result = result * 59 + ($lastTime == null ? 43 : ((Object)$lastTime).hashCode());
        Long $time = this.getTime();
        result = result * 59 + ($time == null ? 43 : ((Object)$time).hashCode());
        Long $maxTime = this.getMaxTime();
        result = result * 59 + ($maxTime == null ? 43 : ((Object)$maxTime).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Long $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        Long $deleteTime = this.getDeleteTime();
        result = result * 59 + ($deleteTime == null ? 43 : ((Object)$deleteTime).hashCode());
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
        String $error = this.getError();
        result = result * 59 + ($error == null ? 43 : $error.hashCode());
        return result;
    }

    public String toString() {
        return "DevCrontab(id=" + this.getId() + ", name=" + this.getName() + ", type=" + this.getType() + ", system=" + this.getSystem() + ", remark=" + this.getRemark() + ", command=" + this.getCommand() + ", status=" + this.getStatus() + ", params=" + this.getParams() + ", expression=" + this.getExpression() + ", error=" + this.getError() + ", lastTime=" + this.getLastTime() + ", time=" + this.getTime() + ", maxTime=" + this.getMaxTime() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", deleteTime=" + this.getDeleteTime() + ")";
    }
}

