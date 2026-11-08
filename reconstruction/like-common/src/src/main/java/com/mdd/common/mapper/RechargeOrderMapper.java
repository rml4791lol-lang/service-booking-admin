/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 */
package com.mdd.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.core.basics.IBaseMapper;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.ToolUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface RechargeOrderMapper
extends IBaseMapper<RechargeOrder> {

    @Update("UPDATE la_recharge_order SET lesson_count = lesson_count - 1, update_time = #{now} "
            + "WHERE id = #{id} AND lesson_count > 0 AND pay_status = 1 AND refund_status = 0 "
            + "AND delete_time IS NULL")
    int consumeLessonCount(@Param("id") Integer id, @Param("now") Long now);

    default public String randMakeOrderSn(String field) {
        String sn;
        RechargeOrder snModel;
        String date = TimeUtils.timestampToDate(System.currentTimeMillis() / 1000L, "yyyyMMddHHmmss");
        do {
            sn = date + ToolUtils.randomInt(12);
        } while ((snModel = (RechargeOrder)this.selectOne(new QueryWrapper<RechargeOrder>().select(new String[]{"id"}).eq(field, (Object)sn).last("limit 1"))) != null);
        return sn;
    }
}
