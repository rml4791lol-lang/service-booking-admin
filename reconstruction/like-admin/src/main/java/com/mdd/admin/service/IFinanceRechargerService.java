/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.service.IFinanceRechargerService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.finance.FinanceRechargeSearchValidate
 *  com.mdd.admin.vo.finance.FinanceRechargeListVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.finance.FinanceRechargeSearchValidate;
import com.mdd.admin.vo.finance.FinanceRechargeListVo;
import com.mdd.common.core.PageResult;
import java.math.BigDecimal;
import java.util.Map;

public interface IFinanceRechargerService {
    public PageResult<FinanceRechargeListVo> list(PageValidate var1, FinanceRechargeSearchValidate var2);

    public void refund(Integer var1, Integer var2);

    /**
     * 按指定金额退款（#61 课程退课按「实付 - 已消耗课时费」退回，不能按整单金额）。
     * refundAmount 为 null 时等同整单退款。
     */
    public void refund(Integer var1, Integer var2, BigDecimal var3);

    public void refundAgain(Integer var1, Integer var2);

    public JSONObject getExportData(PageValidate var1, FinanceRechargeSearchValidate var2);

    public Map<String, Object> export(FinanceRechargeSearchValidate var1);

    public Map<String, Object> stats();

    public void edit(Integer var1, Integer var2, Integer var3, Integer var4, String var5, Integer var6);
}

