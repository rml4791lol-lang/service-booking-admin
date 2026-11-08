/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.commons.IdsValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.admin.validate.commons;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;

@ApiModel(value="IDS\u53c2\u6570")
public class IdsValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="ids\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="ID\u6570\u7ec4", required=true)
    private @NotNull(message="ids\u53c2\u6570\u7f3a\u5931") List<Integer> ids;

    @NotNull(message="ids\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="ids\u53c2\u6570\u7f3a\u5931") List<Integer> getIds() {
        return this.ids;
    }

    public void setIds(@NotNull(message="ids\u53c2\u6570\u7f3a\u5931") @NotNull(message="ids\u53c2\u6570\u7f3a\u5931") List<Integer> ids) {
        this.ids = ids;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof IdsValidate)) {
            return false;
        }
        IdsValidate other = (IdsValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        List this$ids = this.getIds();
        List other$ids = other.getIds();
        return !(this$ids == null ? other$ids != null : !((Object)this$ids).equals(other$ids));
    }

    protected boolean canEqual(Object other) {
        return other instanceof IdsValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List $ids = this.getIds();
        result = result * 59 + ($ids == null ? 43 : ((Object)$ids).hashCode());
        return result;
    }

    public String toString() {
        return "IdsValidate(ids=" + this.getIds() + ")";
    }
}

