package com.mdd.admin.validate.coupon;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 次数券商品排序调整（#45 / T28.9）。
 *
 * <p>sort 越小越靠前，与小程序券商城 /api/coupon/products 的排序一致。</p>
 */
public class CouponGoodsSortValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "商品ID不能为空")
    private Integer id;

    @NotNull(message = "排序值不能为空")
    @Min(value = 0, message = "排序值不能小于0")
    @Max(value = 9999, message = "排序值不能超过9999")
    private Integer sort;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
