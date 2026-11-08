/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IFinanceWalletService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.finance.FinanceWalletSearchValidate
 *  com.mdd.admin.vo.finance.FinanceWalletListVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.finance.FinanceWalletSearchValidate;
import com.mdd.admin.vo.finance.FinanceWalletListVo;
import com.mdd.common.core.PageResult;
import java.util.Map;

public interface IFinanceWalletService {
    public PageResult<FinanceWalletListVo> list(PageValidate var1, FinanceWalletSearchValidate var2);

    public Map<Integer, String> getUmChangeType();
}

