/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.instructor.InstructorLeaveSearchValidate
 */
package com.mdd.admin.validate.instructor;

public class InstructorLeaveSearchValidate {
    private String name;
    private String phone;
    private Integer status;

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InstructorLeaveSearchValidate)) {
            return false;
        }
        InstructorLeaveSearchValidate other = (InstructorLeaveSearchValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$phone = this.getPhone();
        String other$phone = other.getPhone();
        return !(this$phone == null ? other$phone != null : !this$phone.equals(other$phone));
    }

    protected boolean canEqual(Object other) {
        return other instanceof InstructorLeaveSearchValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $phone = this.getPhone();
        result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
        return result;
    }

    public String toString() {
        return "InstructorLeaveSearchValidate(name=" + this.getName() + ", phone=" + this.getPhone() + ", status=" + this.getStatus() + ")";
    }
}

