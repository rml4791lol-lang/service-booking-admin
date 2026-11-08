/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.user.UserSearchValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u7528\u6237\u641c\u7d22\u53c2\u6570")
public class UserSearchValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u6e20\u9053")
    private Integer channel;
    @ApiModelProperty(value="\u5173\u952e\u8bcd")
    private String keyword;
    @ApiModelProperty(value="\u624b\u673a\u53f7")
    private String mobile;
    @ApiModelProperty(value="\u59d3\u540d\uff08\u5bb6\u957f\u771f\u5b9e\u59d3\u540d/\u6635\u79f0\u6216\u5b66\u5458\u59d3\u540d\uff09")
    private String name;
    @ApiModelProperty(value="\u6240\u5728\u73ed\u7ea7ID")
    private Integer classId;
    @ApiModelProperty(value="\u5f00\u59cb\u65f6\u95f4")
    private String create_time_start;
    @ApiModelProperty(value="\u7ed3\u675f\u65f6\u95f4")
    private String create_time_end;
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

    public Integer getChannel() {
        return this.channel;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getName() {
        return this.name;
    }

    public Integer getClassId() {
        return this.classId;
    }

    public String getCreate_time_start() {
        return this.create_time_start;
    }

    public String getCreate_time_end() {
        return this.create_time_end;
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

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public void setCreate_time_start(String create_time_start) {
        this.create_time_start = create_time_start;
    }

    public void setCreate_time_end(String create_time_end) {
        this.create_time_end = create_time_end;
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
        if (!(o instanceof UserSearchValidate)) {
            return false;
        }
        UserSearchValidate other = (UserSearchValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$channel = this.getChannel();
        Integer other$channel = other.getChannel();
        if (this$channel == null ? other$channel != null : !((Object)this$channel).equals(other$channel)) {
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
        String this$keyword = this.getKeyword();
        String other$keyword = other.getKeyword();
        if (this$keyword == null ? other$keyword != null : !this$keyword.equals(other$keyword)) {
            return false;
        }
        String this$mobile = this.getMobile();
        String other$mobile = other.getMobile();
        if (this$mobile == null ? other$mobile != null : !this$mobile.equals(other$mobile)) {
            return false;
        }
        String this$create_time_start = this.getCreate_time_start();
        String other$create_time_start = other.getCreate_time_start();
        if (this$create_time_start == null ? other$create_time_start != null : !this$create_time_start.equals(other$create_time_start)) {
            return false;
        }
        String this$create_time_end = this.getCreate_time_end();
        String other$create_time_end = other.getCreate_time_end();
        if (this$create_time_end == null ? other$create_time_end != null : !this$create_time_end.equals(other$create_time_end)) {
            return false;
        }
        String this$file_name = this.getFile_name();
        String other$file_name = other.getFile_name();
        return !(this$file_name == null ? other$file_name != null : !this$file_name.equals(other$file_name));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserSearchValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $channel = this.getChannel();
        result = result * 59 + ($channel == null ? 43 : ((Object)$channel).hashCode());
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
        String $keyword = this.getKeyword();
        result = result * 59 + ($keyword == null ? 43 : $keyword.hashCode());
        String $mobile = this.getMobile();
        result = result * 59 + ($mobile == null ? 43 : $mobile.hashCode());
        String $create_time_start = this.getCreate_time_start();
        result = result * 59 + ($create_time_start == null ? 43 : $create_time_start.hashCode());
        String $create_time_end = this.getCreate_time_end();
        result = result * 59 + ($create_time_end == null ? 43 : $create_time_end.hashCode());
        String $file_name = this.getFile_name();
        result = result * 59 + ($file_name == null ? 43 : $file_name.hashCode());
        return result;
    }

    public String toString() {
        return "UserSearchValidate(channel=" + this.getChannel() + ", keyword=" + this.getKeyword() + ", mobile=" + this.getMobile() + ", create_time_start=" + this.getCreate_time_start() + ", create_time_end=" + this.getCreate_time_end() + ", export=" + this.getExport() + ", file_name=" + this.getFile_name() + ", page_start=" + this.getPage_start() + ", page_end=" + this.getPage_end() + ", page_size=" + this.getPage_size() + ", page_type=" + this.getPage_type() + ")";
    }
}

