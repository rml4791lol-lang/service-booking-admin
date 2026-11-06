/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.users.UserBindWechatValidate
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.validation.constraints.NotNull
 */
package com.mdd.front.validate.users;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@ApiModel(value="\u7ed1\u5b9a\u5fae\u4fe1\u5c0f\u7a0b\u5e8f\u6216\u516c\u4f17\u53f7")
public class UserBindWechatValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message="code\u53c2\u6570\u7f3a\u5931")
    @ApiModelProperty(value="\u5fae\u4fe1code", required=true)
    private @NotNull(message="code\u53c2\u6570\u7f3a\u5931") String code;
    @ApiModelProperty(value="key")
    private String key;

    @NotNull(message="code\u53c2\u6570\u7f3a\u5931")
    public @NotNull(message="code\u53c2\u6570\u7f3a\u5931") String getCode() {
        return this.code;
    }

    public String getKey() {
        return this.key;
    }

    public void setCode(@NotNull(message="code\u53c2\u6570\u7f3a\u5931") @NotNull(message="code\u53c2\u6570\u7f3a\u5931") String code) {
        this.code = code;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserBindWechatValidate)) {
            return false;
        }
        UserBindWechatValidate other = (UserBindWechatValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) {
            return false;
        }
        String this$key = this.getKey();
        String other$key = other.getKey();
        return !(this$key == null ? other$key != null : !this$key.equals(other$key));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserBindWechatValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        String $key = this.getKey();
        result = result * 59 + ($key == null ? 43 : $key.hashCode());
        return result;
    }

    public String toString() {
        return "UserBindWechatValidate(code=" + this.getCode() + ", key=" + this.getKey() + ")";
    }
}

