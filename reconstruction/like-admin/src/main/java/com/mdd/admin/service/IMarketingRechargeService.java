/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IMarketingRechargeService
 *  com.mdd.admin.validate.marketing.MarketingRechargeValidate
 *  com.mdd.admin.vo.marketing.MarketingRechargeVo
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.marketing.MarketingRechargeValidate;
import com.mdd.admin.vo.marketing.MarketingRechargeVo;

public interface IMarketingRechargeService {
    public MarketingRechargeVo detail();

    public void save(MarketingRechargeValidate var1);
}

