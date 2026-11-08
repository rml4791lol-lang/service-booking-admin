/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.common.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u7cfb\u7edf\u767b\u5f55\u65e5\u5fd7\u5b9e\u4f53")
public class SystemLogLogin
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u7ba1\u7406\u5458ID")
    private Integer adminId;
    @ApiModelProperty(value="\u767b\u5f55\u8d26\u53f7")
    private String username;
    @ApiModelProperty(value="\u767b\u5f55IP")
    private String ip;
    @ApiModelProperty(value="\u64cd\u4f5c\u7cfb\u7edf")
    private String os;
    @ApiModelProperty(value="\u6d4f\u89c8\u5668")
    private String browser;
    @ApiModelProperty(value="\u64cd\u4f5c\u72b6\u6001: [1=\u6210\u529f, 0=\u5931\u8d25]")
    private Integer status;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long createTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getAdminId() {
        return this.adminId;
    }

    public String getUsername() {
        return this.username;
    }

    public String getIp() {
        return this.ip;
    }

    public String getOs() {
        return this.os;
    }

    public String getBrowser() {
        return this.browser;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SystemLogLogin)) {
            return false;
        }
        SystemLogLogin other = (SystemLogLogin)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$adminId = this.getAdminId();
        Integer other$adminId = other.getAdminId();
        if (this$adminId == null ? other$adminId != null : !((Object)this$adminId).equals(other$adminId)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Long this$createTime = this.getCreateTime();
        Long other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
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
        String this$os = this.getOs();
        String other$os = other.getOs();
        if (this$os == null ? other$os != null : !this$os.equals(other$os)) {
            return false;
        }
        String this$browser = this.getBrowser();
        String other$browser = other.getBrowser();
        return !(this$browser == null ? other$browser != null : !this$browser.equals(other$browser));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SystemLogLogin;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $adminId = this.getAdminId();
        result = result * 59 + ($adminId == null ? 43 : ((Object)$adminId).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        String $username = this.getUsername();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        String $ip = this.getIp();
        result = result * 59 + ($ip == null ? 43 : $ip.hashCode());
        String $os = this.getOs();
        result = result * 59 + ($os == null ? 43 : $os.hashCode());
        String $browser = this.getBrowser();
        result = result * 59 + ($browser == null ? 43 : $browser.hashCode());
        return result;
    }

    public String toString() {
        return "SystemLogLogin(id=" + this.getId() + ", adminId=" + this.getAdminId() + ", username=" + this.getUsername() + ", ip=" + this.getIp() + ", os=" + this.getOs() + ", browser=" + this.getBrowser() + ", status=" + this.getStatus() + ", createTime=" + this.getCreateTime() + ")";
    }
}

