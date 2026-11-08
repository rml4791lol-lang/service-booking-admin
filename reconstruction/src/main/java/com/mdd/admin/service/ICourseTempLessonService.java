/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ICourseTempLessonService
 */
package com.mdd.admin.service;

import java.util.List;
import java.util.Map;

public interface ICourseTempLessonService {
    public List<Map<String, Object>> listByClass(Integer var1);

    public Map<String, Object> detail(Integer var1);

    public void create(Integer var1, Map<String, Object> var2);

    public void cancel(Integer var1, Integer var2);
}

