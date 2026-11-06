/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.decorate.DecoratePageValidate
 *  com.mdd.common.validator.annotation.IDMust
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.admin.validate.decorate;

import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u9875\u9762\u88c5\u4fee\u53c2\u6570")
public class DecoratePageValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @IDMust(message="id\u53c2\u6570\u5fc5\u4f20\u4e14\u9700\u5927\u4e8e0")
    @ApiModelProperty(value="id", required=true)
    private Integer id;
    private String name;
    private Integer type;
    @NotNull(message="pageData\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u88c5\u4fee\u6570\u636e", required=true)
    private @NotNull(message="pageData\u53c2\u6570\u7f3a\u5931") String data;
    @NotNull(message="pageMeta")
    @ApiModelProperty(value="\u88c5\u4fee\u6570\u636e", required=true)
    private @NotNull(message="pageMeta") String meta;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getType() {
        return this.type;
    }

    @NotNull(message="pageData\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="pageData\u53c2\u6570\u7f3a\u5931") String getData() {
        return this.data;
    }

    @NotNull(message="pageMeta")
    public @NotNull(message="pageMeta") String getMeta() {
        return this.meta;
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

    public void setData(@NotNull(message="pageData\u53c2\u6570\u7f3a\u5931") @NotNull(message="pageData\u53c2\u6570\u7f3a\u5931") String data) {
        this.data = data;
    }

    public void setMeta(@NotNull(message="pageMeta") @NotNull(message="pageMeta") String meta) {
        this.meta = meta;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DecoratePageValidate)) {
            return false;
        }
        DecoratePageValidate other = (DecoratePageValidate)o;
        if (!other.canEqual((Object)this)) {
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
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$data = this.getData();
        String other$data = other.getData();
        if (this$data == null ? other$data != null : !this$data.equals(other$data)) {
            return false;
        }
        String this$meta = this.getMeta();
        String other$meta = other.getMeta();
        return !(this$meta == null ? other$meta != null : !this$meta.equals(other$meta));
    }

    protected boolean canEqual(Object other) {
        return other instanceof DecoratePageValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $type = this.getType();
        result = result * 59 + ($type == null ? 43 : ((Object)$type).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        String $meta = this.getMeta();
        result = result * 59 + ($meta == null ? 43 : $meta.hashCode());
        return result;
    }

    public String toString() {
        return "DecoratePageValidate(id=" + this.getId() + ", name=" + this.getName() + ", type=" + this.getType() + ", data=" + this.getData() + ", meta=" + this.getMeta() + ")";
    }
}

