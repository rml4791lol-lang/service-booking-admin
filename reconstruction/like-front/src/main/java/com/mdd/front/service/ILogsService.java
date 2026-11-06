/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.PageResult
 *  com.mdd.front.service.ILogsService
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.vo.RechargeRecordVo
 */
package com.mdd.front.service;

import com.mdd.common.core.PageResult;
import com.mdd.front.validate.common.PageValidate;
import com.mdd.front.vo.RechargeRecordVo;

public interface ILogsService {
    public PageResult<RechargeRecordVo> userMoney(PageValidate var1, Integer var2, Integer var3);
}

