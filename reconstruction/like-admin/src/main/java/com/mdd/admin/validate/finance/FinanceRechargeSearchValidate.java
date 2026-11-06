/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.finance.FinanceRechargeSearchValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.finance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u5145\u503c\u8ba2\u5355\u641c\u7d22\u53c2\u6570")
public class FinanceRechargeSearchValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u8ba2\u5355\u7f16\u53f7")
    private String sn;
    @ApiModelProperty(value="\u5173\u952e\u8bcd")
    private String user_info;
    @ApiModelProperty(value="\u652f\u4ed8\u65b9\u5f0f")
    private Integer pay_way;
    @ApiModelProperty(value="\u652f\u4ed8\u72b6\u6001")
    private Integer pay_status;
    @ApiModelProperty(value="\u5f00\u59cb\u65f6\u95f4")
    private String start_time;
    @ApiModelProperty(value="\u7ed3\u675f\u65f6\u95f4")
    private String end_time;
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

    public String getSn() {
        return this.sn;
    }

    public String getUser_info() {
        return this.user_info;
    }

    public Integer getPay_way() {
        return this.pay_way;
    }

    public Integer getPay_status() {
        return this.pay_status;
    }

    public String getStart_time() {
        return this.start_time;
    }

    public String getEnd_time() {
        return this.end_time;
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

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setUser_info(String user_info) {
        this.user_info = user_info;
    }

    public void setPay_way(Integer pay_way) {
        this.pay_way = pay_way;
    }

    public void setPay_status(Integer pay_status) {
        this.pay_status = pay_status;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
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
        if (!(o instanceof FinanceRechargeSearchValidate)) {
            return false;
        }
        FinanceRechargeSearchValidate other = (FinanceRechargeSearchValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$pay_way = this.getPay_way();
        Integer other$pay_way = other.getPay_way();
        if (this$pay_way == null ? other$pay_way != null : !((Object)this$pay_way).equals(other$pay_way)) {
            return false;
        }
        Integer this$pay_status = this.getPay_status();
        Integer other$pay_status = other.getPay_status();
        if (this$pay_status == null ? other$pay_status != null : !((Object)this$pay_status).equals(other$pay_status)) {
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
        String this$sn = this.getSn();
        String other$sn = other.getSn();
        if (this$sn == null ? other$sn != null : !this$sn.equals(other$sn)) {
            return false;
        }
        String this$user_info = this.getUser_info();
        String other$user_info = other.getUser_info();
        if (this$user_info == null ? other$user_info != null : !this$user_info.equals(other$user_info)) {
            return false;
        }
        String this$start_time = this.getStart_time();
        String other$start_time = other.getStart_time();
        if (this$start_time == null ? other$start_time != null : !this$start_time.equals(other$start_time)) {
            return false;
        }
        String this$end_time = this.getEnd_time();
        String other$end_time = other.getEnd_time();
        if (this$end_time == null ? other$end_time != null : !this$end_time.equals(other$end_time)) {
            return false;
        }
        String this$file_name = this.getFile_name();
        String other$file_name = other.getFile_name();
        return !(this$file_name == null ? other$file_name != null : !this$file_name.equals(other$file_name));
    }

    protected boolean canEqual(Object other) {
        return other instanceof FinanceRechargeSearchValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $pay_way = this.getPay_way();
        result = result * 59 + ($pay_way == null ? 43 : ((Object)$pay_way).hashCode());
        Integer $pay_status = this.getPay_status();
        result = result * 59 + ($pay_status == null ? 43 : ((Object)$pay_status).hashCode());
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
        String $sn = this.getSn();
        result = result * 59 + ($sn == null ? 43 : $sn.hashCode());
        String $user_info = this.getUser_info();
        result = result * 59 + ($user_info == null ? 43 : $user_info.hashCode());
        String $start_time = this.getStart_time();
        result = result * 59 + ($start_time == null ? 43 : $start_time.hashCode());
        String $end_time = this.getEnd_time();
        result = result * 59 + ($end_time == null ? 43 : $end_time.hashCode());
        String $file_name = this.getFile_name();
        result = result * 59 + ($file_name == null ? 43 : $file_name.hashCode());
        return result;
    }

    public String toString() {
        return "FinanceRechargeSearchValidate(sn=" + this.getSn() + ", user_info=" + this.getUser_info() + ", pay_way=" + this.getPay_way() + ", pay_status=" + this.getPay_status() + ", start_time=" + this.getStart_time() + ", end_time=" + this.getEnd_time() + ", export=" + this.getExport() + ", file_name=" + this.getFile_name() + ", page_start=" + this.getPage_start() + ", page_end=" + this.getPage_end() + ", page_size=" + this.getPage_size() + ", page_type=" + this.getPage_type() + ")";
    }
}

