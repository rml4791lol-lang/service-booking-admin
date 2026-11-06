/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.validate.commons.PageValidate
 *  io.swagger.annotations.ApiModel
 *  javax.validation.constraints.DecimalMax
 *  javax.validation.constraints.DecimalMin
 */
package com.mdd.admin.validate.commons;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@ApiModel(value="\u5206\u9875\u53c2\u6570")
public class PageValidate
implements Serializable {
    private static final long serialVersionUID = 1L;
    @DecimalMin(value="1", message="pageNo\u53c2\u6570\u5fc5\u987b\u5927\u4e8e0\u7684\u6570\u5b57")
    public @DecimalMin(value="1", message="pageNo\u53c2\u6570\u5fc5\u987b\u5927\u4e8e0\u7684\u6570\u5b57") Integer page_no = 1;
    @DecimalMin(value="1", message="pageSize\u53c2\u6570\u5fc5\u987b\u662f\u5927\u4e8e0\u7684\u6570\u5b57")
    @DecimalMax(value="60", message="pageSize\u53c2\u6570\u5fc5\u987b\u662f\u5c0f\u4e8e60\u7684\u6570\u5b57")
    private @DecimalMin(value="1", message="pageSize\u53c2\u6570\u5fc5\u987b\u662f\u5927\u4e8e0\u7684\u6570\u5b57") @DecimalMax(value="60", message="pageSize\u53c2\u6570\u5fc5\u987b\u662f\u5c0f\u4e8e60\u7684\u6570\u5b57") Integer page_size = 20;

    public Integer getPage_no() {
        return this.page_no;
    }

    public Integer getPage_size() {
        return this.page_size;
    }

    public void setPage_no(Integer page_no) {
        this.page_no = page_no;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PageValidate)) {
            return false;
        }
        PageValidate other = (PageValidate)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$page_no = this.getPage_no();
        Integer other$page_no = other.getPage_no();
        if (this$page_no == null ? other$page_no != null : !((Object)this$page_no).equals(other$page_no)) {
            return false;
        }
        Integer this$page_size = this.getPage_size();
        Integer other$page_size = other.getPage_size();
        return !(this$page_size == null ? other$page_size != null : !((Object)this$page_size).equals(other$page_size));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PageValidate;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $page_no = this.getPage_no();
        result = result * 59 + ($page_no == null ? 43 : ((Object)$page_no).hashCode());
        Integer $page_size = this.getPage_size();
        result = result * 59 + ($page_size == null ? 43 : ((Object)$page_size).hashCode());
        return result;
    }

    public String toString() {
        return "PageValidate(page_no=" + this.getPage_no() + ", page_size=" + this.getPage_size() + ")";
    }
}

