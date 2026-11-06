/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.validator.annotation.StringContains
 *  com.mdd.front.validate.users.UserUpdateValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.front.validate.users;

import com.mdd.common.validator.annotation.StringContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u7528\u6237\u66f4\u65b0\u53c2\u6570")
public class UserUpdateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="field\u53c2\u6570\u7f3a\u5931")
    @StringContains(values={"avatar", "account", "nickname", "sex", "birthday"})
    @ApiModelProperty(value="\u64cd\u4f5c\u5b57\u6bb5", required=true, example="avatar,account,nickname,sex,birthday")
    private @NotNull(message="field\u53c2\u6570\u7f3a\u5931") String field;
    @NotNull(message="value\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u53d8\u66f4\u7684\u503c", required=true)
    private @NotNull(message="value\u53c2\u6570\u7f3a\u5931") String value;

    @NotNull(message="field\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="field\u53c2\u6570\u7f3a\u5931") String getField() {
        return this.field;
    }

    @NotNull(message="value\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="value\u53c2\u6570\u7f3a\u5931") String getValue() {
        return this.value;
    }

    public void setField(@NotNull(message="field\u53c2\u6570\u7f3a\u5931") @NotNull(message="field\u53c2\u6570\u7f3a\u5931") String field) {
        this.field = field;
    }

    public void setValue(@NotNull(message="value\u53c2\u6570\u7f3a\u5931") @NotNull(message="value\u53c2\u6570\u7f3a\u5931") String value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserUpdateValidate)) {
            return false;
        }
        UserUpdateValidate other = (UserUpdateValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$field = this.getField();
        String other$field = other.getField();
        if (this$field == null ? other$field != null : !this$field.equals(other$field)) {
            return false;
        }
        String this$value = this.getValue();
        String other$value = other.getValue();
        return !(this$value == null ? other$value != null : !this$value.equals(other$value));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserUpdateValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $field = this.getField();
        result = result * 59 + ($field == null ? 43 : $field.hashCode());
        String $value = this.getValue();
        result = result * 59 + ($value == null ? 43 : $value.hashCode());
        return result;
    }

    public String toString() {
        return "UserUpdateValidate(field=" + this.getField() + ", value=" + this.getValue() + ")";
    }
}

