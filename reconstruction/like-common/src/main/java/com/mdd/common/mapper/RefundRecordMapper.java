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
import com.mdd.common.entity.RefundRecord;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.ToolUtils;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefundRecordMapper
extends IBaseMapper<RefundRecord> {
    default public String randMakeOrderSn(String field) {
        String sn;
        RefundRecord snModel;
        String date = TimeUtils.timestampToDate(System.currentTimeMillis() / 1000L, "yyyyMMddHHmmss");
        do {
            sn = date + ToolUtils.randomInt(12);
        } while ((snModel = (RefundRecord)this.selectOne(new QueryWrapper<RefundRecord>().select(new String[]{"id"}).eq(field, sn).last("limit 1"))) != null);
        return sn;
    }
}

