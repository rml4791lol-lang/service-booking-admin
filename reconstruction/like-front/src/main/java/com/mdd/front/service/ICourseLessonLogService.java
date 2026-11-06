/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.service.ICourseLessonLogService
 */
package com.mdd.front.service;

import java.util.List;
import java.util.Map;

public interface ICourseLessonLogService {
    public void submitSummary(Map<String, Object> var1, Integer var2);

    public List<Map<String, Object>> listSummary(Integer var1, Integer var2);

    public Map<String, Object> listSummaryPaged(Integer var1, Integer var2, Integer var3, Integer var4);

    public int calcLessonNo(Integer var1);
}

