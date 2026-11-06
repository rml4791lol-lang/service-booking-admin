/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.vo.course.CourseOrderStatsVo
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.mdd.front.vo.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value="\u8bfe\u7a0b\u8ba2\u5355\u7edf\u8ba1Vo")
public class CourseOrderStatsVo
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="\u5168\u90e8\u8ba2\u5355\u6570")
    private Long total;
    @ApiModelProperty(value="\u5f85\u652f\u4ed8\u8ba2\u5355\u6570")
    private Long pending;
    @ApiModelProperty(value="\u603b\u6d88\u8d39\u91d1\u989d")
    private BigDecimal totalSpent;

    public Long getTotal() {
        return this.total;
    }

    public Long getPending() {
        return this.pending;
    }

    public BigDecimal getTotalSpent() {
        return this.totalSpent;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public void setPending(Long pending) {
        this.pending = pending;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CourseOrderStatsVo)) {
            return false;
        }
        CourseOrderStatsVo other = (CourseOrderStatsVo)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Long this$total = this.getTotal();
        Long other$total = other.getTotal();
        if (this$total == null ? other$total != null : !((Object)this$total).equals(other$total)) {
            return false;
        }
        Long this$pending = this.getPending();
        Long other$pending = other.getPending();
        if (this$pending == null ? other$pending != null : !((Object)this$pending).equals(other$pending)) {
            return false;
        }
        BigDecimal this$totalSpent = this.getTotalSpent();
        BigDecimal other$totalSpent = other.getTotalSpent();
        return !(this$totalSpent == null ? other$totalSpent != null : !((Object)this$totalSpent).equals(other$totalSpent));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CourseOrderStatsVo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $total = this.getTotal();
        result = result * 59 + ($total == null ? 43 : ((Object)$total).hashCode());
        Long $pending = this.getPending();
        result = result * 59 + ($pending == null ? 43 : ((Object)$pending).hashCode());
        BigDecimal $totalSpent = this.getTotalSpent();
        result = result * 59 + ($totalSpent == null ? 43 : ((Object)$totalSpent).hashCode());
        return result;
    }

    public String toString() {
        return "CourseOrderStatsVo(total=" + this.getTotal() + ", pending=" + this.getPending() + ", totalSpent=" + this.getTotalSpent() + ")";
    }
}

