/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.file.FileMoveValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.admin.validate.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u9644\u4ef6\u79fb\u52a8\u53c2\u6570")
public class FileMoveValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="ids\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u9644\u4ef6ID", required=true)
    private @NotNull(message="ids\u53c2\u6570\u7f3a\u5931") List<Integer> ids;
    @NotNull(message="cid\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u7c7b\u76eeID", required=true)
    private @NotNull(message="cid\u53c2\u6570\u7f3a\u5931") Integer cid;

    @NotNull(message="ids\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="ids\u53c2\u6570\u7f3a\u5931") List<Integer> getIds() {
        return this.ids;
    }

    @NotNull(message="cid\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="cid\u53c2\u6570\u7f3a\u5931") Integer getCid() {
        return this.cid;
    }

    public void setIds(@NotNull(message="ids\u53c2\u6570\u7f3a\u5931") @NotNull(message="ids\u53c2\u6570\u7f3a\u5931") List<Integer> ids) {
        this.ids = ids;
    }

    public void setCid(@NotNull(message="cid\u53c2\u6570\u7f3a\u5931") @NotNull(message="cid\u53c2\u6570\u7f3a\u5931") Integer cid) {
        this.cid = cid;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FileMoveValidate)) {
            return false;
        }
        FileMoveValidate other = (FileMoveValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$cid = this.getCid();
        Integer other$cid = other.getCid();
        if (this$cid == null ? other$cid != null : !((Object)this$cid).equals(other$cid)) {
            return false;
        }
        List this$ids = this.getIds();
        List other$ids = other.getIds();
        return !(this$ids == null ? other$ids != null : !((Object)this$ids).equals(other$ids));
    }

    protected boolean canEqual(Object other) {
        return other instanceof FileMoveValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $cid = this.getCid();
        result = result * 59 + ($cid == null ? 43 : ((Object)$cid).hashCode());
        List $ids = this.getIds();
        result = result * 59 + ($ids == null ? 43 : ((Object)$ids).hashCode());
        return result;
    }

    public String toString() {
        return "FileMoveValidate(ids=" + this.getIds() + ", cid=" + this.getCid() + ")";
    }
}

