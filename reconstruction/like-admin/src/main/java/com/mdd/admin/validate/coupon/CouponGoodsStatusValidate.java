package com.mdd.admin.validate.coupon;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 次数券商品上下架（#45 / T28.9）。
 *
 * <p>status：1=上架（小程序券商城可见），0=下架（小程序不展示，历史券仍可使用）。</p>
 */
public class CouponGoodsStatusValidate implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "商品ID不能为空")
    private Integer id;

    @NotNull(message = "上下架状态不能为空")
    @Min(value = 0, message = "上下架状态只能是 0（下架）或 1（上架）")
    @Max(value = 1, message = "上下架状态只能是 0（下架）或 1（上架）")
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
