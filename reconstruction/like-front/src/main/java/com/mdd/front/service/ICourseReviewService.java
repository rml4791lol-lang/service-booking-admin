/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.service.ICourseReviewService
 */
package com.mdd.front.service;

import java.util.List;
import java.util.Map;

public interface ICourseReviewService {
    public void submit(Integer var1, Map<String, Object> var2);

    public List<Map<String, Object>> list(Integer var1);

    public Map<String, Object> instructorReviewSummary(Integer var1, Integer var2, Integer var3, String var4);

    public void interact(Integer var1, Integer var2);

    public void reply(Integer var1, Integer var2, String var3);
}

