package com.mdd.admin.validate.coupon;

import javax.validation.constraints.NotNull;

/**
 * 次数券商品编辑校验（#45 / T28.9）。
 *
 * <p>沿用新增校验的字段规则，额外要求 id；商品编码 code 必须与库中一致，
 * 服务层会拒绝修改编码，避免数据种子按 code 幂等时重复插入。</p>
 */
public class CouponGoodsUpdateValidate extends CouponGoodsCreateValidate {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "商品ID不能为空")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
