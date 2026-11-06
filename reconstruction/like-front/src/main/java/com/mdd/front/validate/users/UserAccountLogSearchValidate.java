/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.validate.users.UserAccountLogSearchValidate
 *  io.swagger.annotations.ApiModel
 */
package com.mdd.front.validate.users;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;

@ApiModel(value="\u7528\u6237\u8d44\u91d1\u53d8\u66f4\u53c2\u6570")
public class UserAccountLogSearchValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer userId;
    private String type;
    private Integer action;

    public Integer getUserId() {
        return this.userId;
    }

    public String getType() {
        return this.type;
    }

    public Integer getAction() {
        return this.action;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserAccountLogSearchValidate)) {
            return false;
        }
        UserAccountLogSearchValidate other = (UserAccountLogSearchValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$userId = this.getUserId();
        Integer other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$action = this.getAction();
        Integer other$action = other.getAction();
        if (this$action == null ? other$action != null : !((Object)this$action).equals(other$action)) {
            return false;
        }
        String this$type = this.getType();
        String other$type = other.getType();
        return !(this$type == null ? other$type != null : !this$type.equals(other$type));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserAccountLogSearchValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $action = this.getAction();
        result = result * 59 + ($action == null ? 43 : ((Object)$action).hashCode());
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        return result;
    }

    public String toString() {
        return "UserAccountLogSearchValidate(userId=" + this.getUserId() + ", type=" + this.getType() + ", action=" + this.getAction() + ")";
    }
}

