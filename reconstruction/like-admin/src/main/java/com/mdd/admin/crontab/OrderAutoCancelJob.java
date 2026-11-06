/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper
 *  com.mdd.admin.crontab.OrderAutoCancelJob
 *  com.mdd.common.mapper.RechargeOrderMapper
 *  com.mdd.common.util.TimeUtils
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Component
 */
package com.mdd.admin.crontab;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.util.TimeUtils;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.mdd.common.entity.RechargeOrder;

@Component(value="orderAutoCancelJob")
public class OrderAutoCancelJob {
    @Resource
    RechargeOrderMapper rechargeOrderMapper;

    public void execute() {
        long now = TimeUtils.timestamp();
        long expireTime = now - 1800L;
        long count = this.rechargeOrderMapper.selectCount(new QueryWrapper<RechargeOrder>().eq("pay_status", (Object)0).le("create_time", (Object)expireTime).isNull("delete_time"));
        if (count == 0L) {
            return;
        }
        int updated = this.rechargeOrderMapper.update(null, (Wrapper)((UpdateWrapper)((UpdateWrapper)((UpdateWrapper)((UpdateWrapper)new UpdateWrapper().set((Object)"pay_status", (Object)4)).set((Object)"update_time", (Object)now)).eq("pay_status", (Object)0)).le("create_time", (Object)expireTime)).isNull("delete_time"));
        System.out.println("\u672a\u652f\u4ed8\u8ba2\u5355\u81ea\u52a8\u53d6\u6d88\u4efb\u52a1\u6267\u884c\u5b8c\u6210\uff0c\u5171\u53d6\u6d88\u8ba2\u5355\u6570\u91cf\uff1a" + updated);
    }
}

