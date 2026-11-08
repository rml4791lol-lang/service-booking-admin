/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.finance.LessonProfitShareSearchValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.finance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u5206\u8d26\u660e\u7ec6\u641c\u7d22\u53c2\u6570")
public class LessonProfitShareSearchValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u793e\u533aID(\u7ba1\u7406\u5458\u53ef\u9009)")
    private Integer communityId;
    @ApiModelProperty(value="\u8bfe\u7a0b/\u73ed\u7ea7\u540d\u79f0")
    private String className;
    @ApiModelProperty(value="\u4ee3\u7406\u4eba\u540d\u79f0")
    private String agentName;
    @ApiModelProperty(value="\u4e0a\u8bfe\u65e5\u671f\u8d77 yyyy-MM-dd")
    private String startDate;
    @ApiModelProperty(value="\u4e0a\u8bfe\u65e5\u671f\u6b62 yyyy-MM-dd")
    private String endDate;
    @ApiModelProperty(value="\u72b6\u6001: 1=\u5df2\u7ed3\u7b97 0=\u672a\u7ed3\u7b97")
    private Integer status;
    @ApiModelProperty(value="\u5bfc\u51fa\u7c7b\u578b")
    private Integer export;
    @ApiModelProperty(value="\u5bfc\u51fa\u6587\u4ef6\u540d")
    private String file_name;
    @ApiModelProperty(value="\u5bfc\u51fa\u5f00\u59cb\u9875")
    private Integer page_start;
    @ApiModelProperty(value="\u5bfc\u51fa\u7ed3\u675f\u9875")
    private Integer page_end;
    @ApiModelProperty(value="\u5bfc\u51fa\u6bcf\u9875\u6761\u6570")
    private Integer page_size;
    @ApiModelProperty(value="\u5bfc\u51fa\u8303\u56f4\u7c7b\u578b")
    private Integer page_type;

    public Integer getCommunityId() {
        return this.communityId;
    }

    public String getClassName() {
        return this.className;
    }

    public String getAgentName() {
        return this.agentName;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
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

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
        if (!(o instanceof LessonProfitShareSearchValidate)) {
            return false;
        }
        LessonProfitShareSearchValidate other = (LessonProfitShareSearchValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$communityId = this.getCommunityId();
        Integer other$communityId = other.getCommunityId();
        if (this$communityId == null ? other$communityId != null : !((Object)this$communityId).equals(other$communityId)) {
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
        String this$className = this.getClassName();
        String other$className = other.getClassName();
        if (this$className == null ? other$className != null : !this$className.equals(other$className)) {
            return false;
        }
        String this$agentName = this.getAgentName();
        String other$agentName = other.getAgentName();
        if (this$agentName == null ? other$agentName != null : !this$agentName.equals(other$agentName)) {
            return false;
        }
        String this$startDate = this.getStartDate();
        String other$startDate = other.getStartDate();
        if (this$startDate == null ? other$startDate != null : !this$startDate.equals(other$startDate)) {
            return false;
        }
        String this$endDate = this.getEndDate();
        String other$endDate = other.getEndDate();
        if (this$endDate == null ? other$endDate != null : !this$endDate.equals(other$endDate)) {
            return false;
        }
        String this$file_name = this.getFile_name();
        String other$file_name = other.getFile_name();
        return !(this$file_name == null ? other$file_name != null : !this$file_name.equals(other$file_name));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LessonProfitShareSearchValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $communityId = this.getCommunityId();
        result = result * 59 + ($communityId == null ? 43 : ((Object)$communityId).hashCode());
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
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $agentName = this.getAgentName();
        result = result * 59 + ($agentName == null ? 43 : $agentName.hashCode());
        String $startDate = this.getStartDate();
        result = result * 59 + ($startDate == null ? 43 : $startDate.hashCode());
        String $endDate = this.getEndDate();
        result = result * 59 + ($endDate == null ? 43 : $endDate.hashCode());
        String $file_name = this.getFile_name();
        result = result * 59 + ($file_name == null ? 43 : $file_name.hashCode());
        return result;
    }

    public String toString() {
        return "LessonProfitShareSearchValidate(communityId=" + this.getCommunityId() + ", className=" + this.getClassName() + ", agentName=" + this.getAgentName() + ", startDate=" + this.getStartDate() + ", endDate=" + this.getEndDate() + ", status=" + this.getStatus() + ", export=" + this.getExport() + ", file_name=" + this.getFile_name() + ", page_start=" + this.getPage_start() + ", page_end=" + this.getPage_end() + ", page_size=" + this.getPage_size() + ", page_type=" + this.getPage_type() + ")";
    }
}

