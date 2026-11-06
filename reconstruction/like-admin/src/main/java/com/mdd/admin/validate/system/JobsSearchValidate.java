/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.system.JobsSearchValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u7cfb\u7edf\u5c97\u4f4d\u641c\u7d22\u53c2\u6570")
public class JobsSearchValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u7f16\u7801")
    private String code;
    @ApiModelProperty(value="\u5c97\u4f4d\u540d\u79f0")
    private String name;
    @ApiModelProperty(value="\u662f\u5426\u505c\u7528")
    private Integer status;
    @ApiModelProperty(value="\u5bfc\u51fa\u4fe1\u606f")
    private Integer export;
    @ApiModelProperty(value="file_name")
    private String file_name;
    @ApiModelProperty(value="page_start")
    private Integer page_start;
    @ApiModelProperty(value="page_end")
    private Integer page_end;
    @ApiModelProperty(value="page_size")
    private Integer page_size;
    @ApiModelProperty(value="page_type")
    private Integer page_type;

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Integer getExport() {
        return this.export;
    }

    public String getFile_name() {
        return this.file_name;
    }

    public Integer getPage_start() {
        return this.page_start;
    }

    public Integer getPage_end() {
        return this.page_end;
    }

    public Integer getPage_size() {
        return this.page_size;
    }

    public Integer getPage_type() {
        return this.page_type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setExport(Integer export) {
        this.export = export;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public void setPage_start(Integer page_start) {
        this.page_start = page_start;
    }

    public void setPage_end(Integer page_end) {
        this.page_end = page_end;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    public void setPage_type(Integer page_type) {
        this.page_type = page_type;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof JobsSearchValidate)) {
            return false;
        }
        JobsSearchValidate other = (JobsSearchValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Integer this$export = this.getExport();
        Integer other$export = other.getExport();
        if (this$export == null ? other$export != null : !((Object)this$export).equals(other$export)) {
            return false;
        }
        Integer this$page_start = this.getPage_start();
        Integer other$page_start = other.getPage_start();
        if (this$page_start == null ? other$page_start != null : !((Object)this$page_start).equals(other$page_start)) {
            return false;
        }
        Integer this$page_end = this.getPage_end();
        Integer other$page_end = other.getPage_end();
        if (this$page_end == null ? other$page_end != null : !((Object)this$page_end).equals(other$page_end)) {
            return false;
        }
        Integer this$page_size = this.getPage_size();
        Integer other$page_size = other.getPage_size();
        if (this$page_size == null ? other$page_size != null : !((Object)this$page_size).equals(other$page_size)) {
            return false;
        }
        Integer this$page_type = this.getPage_type();
        Integer other$page_type = other.getPage_type();
        if (this$page_type == null ? other$page_type != null : !((Object)this$page_type).equals(other$page_type)) {
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
        String this$file_name = this.getFile_name();
        String other$file_name = other.getFile_name();
        return !(this$file_name == null ? other$file_name != null : !this$file_name.equals(other$file_name));
    }

    protected boolean canEqual(Object other) {
        return other instanceof JobsSearchValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $export = this.getExport();
        result = result * 59 + ($export == null ? 43 : ((Object)$export).hashCode());
        Integer $page_start = this.getPage_start();
        result = result * 59 + ($page_start == null ? 43 : ((Object)$page_start).hashCode());
        Integer $page_end = this.getPage_end();
        result = result * 59 + ($page_end == null ? 43 : ((Object)$page_end).hashCode());
        Integer $page_size = this.getPage_size();
        result = result * 59 + ($page_size == null ? 43 : ((Object)$page_size).hashCode());
        Integer $page_type = this.getPage_type();
        result = result * 59 + ($page_type == null ? 43 : ((Object)$page_type).hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $file_name = this.getFile_name();
        result = result * 59 + ($file_name == null ? 43 : $file_name.hashCode());
        return result;
    }

    public String toString() {
        return "JobsSearchValidate(code=" + this.getCode() + ", name=" + this.getName() + ", status=" + this.getStatus() + ", export=" + this.getExport() + ", file_name=" + this.getFile_name() + ", page_start=" + this.getPage_start() + ", page_end=" + this.getPage_end() + ", page_size=" + this.getPage_size() + ", page_type=" + this.getPage_type() + ")";
    }
}

