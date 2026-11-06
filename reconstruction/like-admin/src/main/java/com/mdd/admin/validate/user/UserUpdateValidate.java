/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.user.UserUpdateValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.admin.validate.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value="\u7528\u6237\u66f4\u65b0\u53c2\u6570")
public class UserUpdateValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="id")
    private Integer id;
    @ApiModelProperty(value="\u5b57\u6bb5")
    private String field;
    @ApiModelProperty(value="\u503c")
    private String value;

    public Integer getId() {
        return this.id;
    }

    public String getField() {
        return this.field;
    }

    public String getValue() {
        return this.value;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setValue(String value) {
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
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
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
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $field = this.getField();
        result = result * 59 + ($field == null ? 43 : $field.hashCode());
        String $value = this.getValue();
        result = result * 59 + ($value == null ? 43 : $value.hashCode());
        return result;
    }

    public String toString() {
        return "UserUpdateValidate(id=" + this.getId() + ", field=" + this.getField() + ", value=" + this.getValue() + ")";
    }
}

