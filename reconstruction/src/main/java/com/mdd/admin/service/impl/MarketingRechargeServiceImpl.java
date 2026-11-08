/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IMarketingRechargeService
 *  com.mdd.admin.service.impl.MarketingRechargeServiceImpl
 *  com.mdd.admin.validate.marketing.MarketingRechargeValidate
 *  com.mdd.admin.vo.marketing.MarketingRechargeVo
 *  com.mdd.common.util.ConfigUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.mdd.admin.service.IMarketingRechargeService;
import com.mdd.admin.validate.marketing.MarketingRechargeValidate;
import com.mdd.admin.vo.marketing.MarketingRechargeVo;
import com.mdd.common.util.ConfigUtils;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class MarketingRechargeServiceImpl
implements IMarketingRechargeService {
    public MarketingRechargeVo detail() {
        Map<String, String> config = ConfigUtils.get((String)"recharge");
        MarketingRechargeVo vo = new MarketingRechargeVo();
        vo.setStatus(Integer.valueOf(Integer.parseInt(config.getOrDefault("status", "0"))));
        vo.setMinAmount(new BigDecimal(config.getOrDefault("min_amount", "0")));
        return vo;
    }

    public void save(MarketingRechargeValidate rechargeValidate) {
        ConfigUtils.set((String)"recharge", (String)"status", (String)rechargeValidate.getStatus().toString());
        ConfigUtils.set((String)"recharge", (String)"min_amount", (String)rechargeValidate.getMinAmount().toString());
    }
}

