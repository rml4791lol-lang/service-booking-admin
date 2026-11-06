/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.service.IInstructorSalaryService
 */
package com.mdd.front.service;

import java.math.BigDecimal;
import java.util.Map;

public interface IInstructorSalaryService {
    public Map<String, Object> classIncomeDetail(Integer var1, String var2);

    public Map<String, Object> incomeSummary(Integer var1, String var2);

    public Map<String, Object> overview(Integer var1);

    public Map<String, Object> history(Integer var1, Integer var2, Integer var3, Integer var4);

    public Map<String, Object> getBankInfo(Integer var1);

    public void saveBankInfo(Integer var1, Map<String, String> var2);

    public void applyWithdrawal(Integer var1, BigDecimal var2);

    public Map<String, Object> withdrawalList(Integer var1, Integer var2, Integer var3);
}

