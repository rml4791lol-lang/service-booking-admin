/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IFinanceRefundService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.finance.FinanceRefundSearchValidate
 *  com.mdd.admin.vo.finance.FinanceRefundListVo
 *  com.mdd.admin.vo.finance.FinanceRefundLogVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.finance.FinanceRefundSearchValidate;
import com.mdd.admin.vo.finance.FinanceRefundListVo;
import com.mdd.admin.vo.finance.FinanceRefundLogVo;
import com.mdd.common.core.PageResult;
import java.util.List;
import java.util.Map;

public interface IFinanceRefundService {
    public PageResult<FinanceRefundListVo> list(PageValidate var1, FinanceRefundSearchValidate var2);

    public List<FinanceRefundLogVo> log(Integer var1);

    public Map<String, Object> stat();
}

