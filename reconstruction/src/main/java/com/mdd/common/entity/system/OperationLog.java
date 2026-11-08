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

@ApiModel(value="\u7cfb\u7edf\u64cd\u4f5c\u65e5\u5fd7\u5b9e\u4f53")
public class OperationLog
implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id", type=IdType.AUTO)
    @ApiModelProperty(value="ID")
    private Integer id;
    @ApiModelProperty(value="\u64cd\u4f5c\u4ebaID")
    private Integer adminId;
    private String adminName;
    private String account;
    @ApiModelProperty(value="\u64cd\u4f5c\u6807\u9898")
    private String action;
    @ApiModelProperty(value="\u8bf7\u6c42\u7c7b\u578b: GET/POST/PUT")
    private String type;
    @ApiModelProperty(value="\u8bf7\u6c42\u63a5\u53e3")
    private String url;
    @ApiModelProperty(value="\u8bf7\u6c42\u53c2\u6570")
    private String params;
    @ApiModelProperty(value="\u8bf7\u6c42\u7ed3\u679c")
    private String result;
    @ApiModelProperty(value="\u8bf7\u6c42IP")
    private String ip;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4")
    private Long createTime;

    public Integer getId() {
        return this.id;
    }

    public Integer getAdminId() {
        return this.adminId;
    }

    public String getAdminName() {
        return this.adminName;
    }

    public String getAccount() {
        return this.account;
    }

    public String getAction() {
        return this.action;
    }

    public String getType() {
        return this.type;
    }

    public String getUrl() {
        return this.url;
    }

    public String getParams() {
        return this.params;
    }

    public String getResult() {
        return this.result;
    }

    public String getIp() {
        return this.ip;
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

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OperationLog)) {
            return false;
        }
        OperationLog other = (OperationLog)o;
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
        Long this$createTime = this.getCreateTime();
        Long other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        String this$adminName = this.getAdminName();
        String other$adminName = other.getAdminName();
        if (this$adminName == null ? other$adminName != null : !this$adminName.equals(other$adminName)) {
            return false;
        }
        String this$account = this.getAccount();
        String other$account = other.getAccount();
        if (this$account == null ? other$account != null : !this$account.equals(other$account)) {
            return false;
        }
        String this$action = this.getAction();
        String other$action = other.getAction();
        if (this$action == null ? other$action != null : !this$action.equals(other$action)) {
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
        String this$params = this.getParams();
        String other$params = other.getParams();
        if (this$params == null ? other$params != null : !this$params.equals(other$params)) {
            return false;
        }
        String this$result = this.getResult();
        String other$result = other.getResult();
        if (this$result == null ? other$result != null : !this$result.equals(other$result)) {
            return false;
        }
        String this$ip = this.getIp();
        String other$ip = other.getIp();
        return !(this$ip == null ? other$ip != null : !this$ip.equals(other$ip));
    }

    protected boolean canEqual(Object other) {
        return other instanceof OperationLog;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $adminId = this.getAdminId();
        result = result * 59 + ($adminId == null ? 43 : ((Object)$adminId).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        String $adminName = this.getAdminName();
        result = result * 59 + ($adminName == null ? 43 : $adminName.hashCode());
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $action = this.getAction();
        result = result * 59 + ($action == null ? 43 : $action.hashCode());
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        String $url = this.getUrl();
        result = result * 59 + ($url == null ? 43 : $url.hashCode());
        String $params = this.getParams();
        result = result * 59 + ($params == null ? 43 : $params.hashCode());
        String $result = this.getResult();
        result = result * 59 + ($result == null ? 43 : $result.hashCode());
        String $ip = this.getIp();
        result = result * 59 + ($ip == null ? 43 : $ip.hashCode());
        return result;
    }

    public String toString() {
        return "OperationLog(id=" + this.getId() + ", adminId=" + this.getAdminId() + ", adminName=" + this.getAdminName() + ", account=" + this.getAccount() + ", action=" + this.getAction() + ", type=" + this.getType() + ", url=" + this.getUrl() + ", params=" + this.getParams() + ", result=" + this.getResult() + ", ip=" + this.getIp() + ", createTime=" + this.getCreateTime() + ")";
    }
}

