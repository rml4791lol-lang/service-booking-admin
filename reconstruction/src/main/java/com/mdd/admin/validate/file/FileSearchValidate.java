/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.file.FileSearchValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u9644\u4ef6\u641c\u7d22\u53c2\u6570")
public class FileSearchValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u5206\u7c7bID")
    private Integer cid;
    @ApiModelProperty(value="\u7c7b\u578b")
    private Integer type;
    @ApiModelProperty(value="\u6765\u6e90")
    private Integer source;
    @ApiModelProperty(value="\u5173\u952e\u8bcd")
    private String name;

    public Integer getCid() {
        return this.cid;
    }

    public Integer getType() {
        return this.type;
    }

    public Integer getSource() {
        return this.source;
    }

    public String getName() {
        return this.name;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FileSearchValidate)) {
            return false;
        }
        FileSearchValidate other = (FileSearchValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$cid = this.getCid();
        Integer other$cid = other.getCid();
        if (this$cid == null ? other$cid != null : !((Object)this$cid).equals(other$cid)) {
            return false;
        }
        Integer this$type = this.getType();
        Integer other$type = other.getType();
        if (this$type == null ? other$type != null : !((Object)this$type).equals(other$type)) {
            return false;
        }
        Integer this$source = this.getSource();
        Integer other$source = other.getSource();
        if (this$source == null ? other$source != null : !((Object)this$source).equals(other$source)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        return !(this$name == null ? other$name != null : !this$name.equals(other$name));
    }

    protected boolean canEqual(Object other) {
        return other instanceof FileSearchValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $cid = this.getCid();
        result = result * 59 + ($cid == null ? 43 : ((Object)$cid).hashCode());
        Integer $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)$type).hashCode());
        Integer $source = this.getSource();
        result = result * 59 + ($source == null ? 43 : ((Object)$source).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "FileSearchValidate(cid=" + this.getCid() + ", type=" + this.getType() + ", source=" + this.getSource() + ", name=" + this.getName() + ")";
    }
}

