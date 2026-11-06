/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ICourseManualConsumeService
 */
package com.mdd.admin.service;

import java.util.List;
import java.util.Map;

public interface ICourseManualConsumeService {
    public List<Map<String, Object>> listByClass(Integer var1);

    public Map<String, Object> detail(Integer var1);

    public void submit(Integer var1, Map<String, Object> var2);
}

