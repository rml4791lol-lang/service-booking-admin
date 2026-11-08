/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.file.FileRenameValidate
 *  com.mdd.common.validator.annotation.IDMust
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotEmpty
 *  org.hibernate.validator.constraints.Length
 */
package com.mdd.admin.validate.file;

import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

@ApiModel(value="\u9644\u4ef6\u91cd\u547d\u540d\u53c2\u6570")
public class FileRenameValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @IDMust(message="id\u53c2\u6570\u5fc5\u4f20\u4e14\u9700\u5927\u4e8e0")
    @ApiModelProperty(value="\u9644\u4ef6ID", required=true)
    private Integer id;
    @NotEmpty(message="\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a")
    @Length(min=1, max=30, message="\u540d\u79f0\u4e0d\u80fd\u5927\u4e8e30\u4e2a\u5b57\u7b26")
    @ApiModelProperty(value="\u9644\u4ef6\u540d\u79f0", required=true)
    private @NotEmpty(message="\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a") @Length(min=1, max=30, message="\u540d\u79f0\u4e0d\u80fd\u5927\u4e8e30\u4e2a\u5b57\u7b26") String name;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FileRenameValidate)) {
            return false;
        }
        FileRenameValidate other = (FileRenameValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        return !(this$name == null ? other$name != null : !this$name.equals(other$name));
    }

    protected boolean canEqual(Object other) {
        return other instanceof FileRenameValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "FileRenameValidate(id=" + this.getId() + ", name=" + this.getName() + ")";
    }
}

