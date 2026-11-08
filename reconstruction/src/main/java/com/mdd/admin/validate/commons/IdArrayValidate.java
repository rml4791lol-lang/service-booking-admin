/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.commons.IdArrayValidate
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
public class IdArrayValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="ids\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="ID\u6570\u7ec4", required=true)
    private @NotNull(message="ids\u53c2\u6570\u7f3a\u5931") List<Integer> id;

    @NotNull(message="ids\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="ids\u53c2\u6570\u7f3a\u5931") List<Integer> getId() {
        return this.id;
    }

    public void setId(@NotNull(message="ids\u53c2\u6570\u7f3a\u5931") @NotNull(message="ids\u53c2\u6570\u7f3a\u5931") List<Integer> id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof IdArrayValidate)) {
            return false;
        }
        IdArrayValidate other = (IdArrayValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        List this$id = this.getId();
        List other$id = other.getId();
        return !(this$id == null ? other$id != null : !((Object)this$id).equals(other$id));
    }

    protected boolean canEqual(Object other) {
        return other instanceof IdArrayValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        return result;
    }

    public String toString() {
        return "IdArrayValidate(id=" + this.getId() + ")";
    }
}

