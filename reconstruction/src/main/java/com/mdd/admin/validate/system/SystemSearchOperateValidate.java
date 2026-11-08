/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.system.SystemSearchOperateValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u7cfb\u7edf\u64cd\u4f5c\u65e5\u5fd7\u641c\u7d22\u53c2\u6570")
public class SystemSearchOperateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u6807\u9898")
    private String action;
    @ApiModelProperty(value="\u8d26\u53f7")
    private String username;
    @ApiModelProperty(value="IP")
    private String ip;
    @ApiModelProperty(value="\u7c7b\u578b")
    private String type;
    @ApiModelProperty(value="\u8def\u7531")
    private String url;
    @ApiModelProperty(value="\u5f00\u59cb\u65f6\u95f4")
    private String start_time;
    @ApiModelProperty(value="\u7ed3\u675f\u65f6\u95f4")
    private String end_time;

    public String getAction() {
        return this.action;
    }

    public String getUsername() {
        return this.username;
    }

    public String getIp() {
        return this.ip;
    }

    public String getType() {
        return this.type;
    }

    public String getUrl() {
        return this.url;
    }

    public String getStart_time() {
        return this.start_time;
    }

    public String getEnd_time() {
        return this.end_time;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemSearchOperateValidate)) {
            return false;
        }
        SystemSearchOperateValidate other = (SystemSearchOperateValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$action = this.getAction();
        String other$action = other.getAction();
        if (this$action == null ? other$action != null : !this$action.equals(other$action)) {
            return false;
        }
        String this$username = this.getUsername();
        String other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) {
            return false;
        }
        String this$ip = this.getIp();
        String other$ip = other.getIp();
        if (this$ip == null ? other$ip != null : !this$ip.equals(other$ip)) {
            return false;
        }
        String this$type = this.getType();
        String other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) {
            return false;
        }
        String this$url = this.getUrl();
        String other$url = other.getUrl();
        if (this$url == null ? other$url != null : !this$url.equals(other$url)) {
            return false;
        }
        String this$start_time = this.getStart_time();
        String other$start_time = other.getStart_time();
        if (this$start_time == null ? other$start_time != null : !this$start_time.equals(other$start_time)) {
            return false;
        }
        String this$end_time = this.getEnd_time();
        String other$end_time = other.getEnd_time();
        return !(this$end_time == null ? other$end_time != null : !this$end_time.equals(other$end_time));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SystemSearchOperateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $action = this.getAction();
        result = result * 59 + ($action == null ? 43 : $action.hashCode());
        String $username = this.getUsername();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        String $ip = this.getIp();
        result = result * 59 + ($ip == null ? 43 : $ip.hashCode());
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        String $url = this.getUrl();
        result = result * 59 + ($url == null ? 43 : $url.hashCode());
        String $start_time = this.getStart_time();
        result = result * 59 + ($start_time == null ? 43 : $start_time.hashCode());
        String $end_time = this.getEnd_time();
        result = result * 59 + ($end_time == null ? 43 : $end_time.hashCode());
        return result;
    }

    public String toString() {
        return "SystemSearchOperateValidate(action=" + this.getAction() + ", username=" + this.getUsername() + ", ip=" + this.getIp() + ", type=" + this.getType() + ", url=" + this.getUrl() + ", start_time=" + this.getStart_time() + ", end_time=" + this.getEnd_time() + ")";
    }
}

