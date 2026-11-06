/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.service.IStudentService
 */
package com.mdd.front.service;

import java.util.List;
import java.util.Map;

public interface IStudentService {
    public List<Map<String, Object>> list(Integer var1);

    public Map<String, Object> getDefault(Integer var1);

    public Map<String, Object> add(Integer var1, Map<String, Object> var2);

    public void edit(Integer var1, Map<String, Object> var2);

    public Map<String, Object> del(Integer var1, Map<String, Object> var2);

    public void setDefault(Integer var1, Map<String, Object> var2);
}

