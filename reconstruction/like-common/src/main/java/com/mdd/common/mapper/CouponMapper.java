/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  org.apache.ibatis.annotations.Mapper
 */
package com.mdd.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.core.basics.IBaseMapper;
import com.mdd.common.entity.Coupon;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.ToolUtils;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponMapper
extends IBaseMapper<Coupon> {
    default public String randMakeCouponSn() {
        String sn;
        Coupon snModel;
        String date = TimeUtils.timestampToDate(System.currentTimeMillis() / 1000L, "yyyyMMddHHmmss");
        do {
            sn = "CPN" + date + ToolUtils.randomInt(6);
        } while ((snModel = (Coupon)this.selectOne(new QueryWrapper<Coupon>().select(new String[]{"id"}).eq("sn", (Object)sn).last("limit 1"))) != null);
        return sn;
    }
}

