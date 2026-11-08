/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ICouponTemplateService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.coupon.CouponTemplateCreateValidate
 *  com.mdd.admin.vo.coupon.CouponTemplateListedVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.coupon.CouponTemplateCreateValidate;
import com.mdd.admin.vo.coupon.CouponTemplateListedVo;
import com.mdd.common.core.PageResult;
import java.util.List;

public interface ICouponTemplateService {
    public PageResult<CouponTemplateListedVo> list(PageValidate var1, String var2, Integer var3);

    public List<CouponTemplateListedVo> all();

    public void add(CouponTemplateCreateValidate var1, Integer var2);

    public void edit(CouponTemplateCreateValidate var1);

    public void del(Integer var1);
}

