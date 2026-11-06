/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.PageResult
 *  com.mdd.front.service.IRechargeService
 *  com.mdd.front.validate.RechargeValidate
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.vo.RechargeConfigVo
 *  com.mdd.front.vo.RechargeRecordVo
 */
package com.mdd.front.service;

import com.mdd.common.core.PageResult;
import com.mdd.front.validate.RechargeValidate;
import com.mdd.front.validate.common.PageValidate;
import com.mdd.front.vo.OrderListVo;
import com.mdd.front.vo.RechargeConfigVo;
import com.mdd.front.vo.RechargeRecordVo;
import java.util.Map;

public interface IRechargeService {
    public RechargeConfigVo config(Integer var1);

    public PageResult<RechargeRecordVo> record(Integer var1, PageValidate var2);

    /** 小程序「我的订单」统一读模型：分页 + 券/活动/课程退款状态（#63）。 */
    public PageResult<OrderListVo> orderList(Integer var1, PageValidate var2);

    public Map<String, Object> placeOrder(Integer var1, Integer var2, RechargeValidate var3);

    public void updatePayOrderStatusToPaid(String var1, String var2);
}

