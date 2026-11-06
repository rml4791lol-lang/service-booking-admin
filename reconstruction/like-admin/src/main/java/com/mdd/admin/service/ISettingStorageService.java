/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ISettingStorageService
 */
package com.mdd.admin.service;

import java.util.List;
import java.util.Map;

public interface ISettingStorageService {
    public List<Map<String, Object>> list();

    public Map<String, Object> detail(String var1);

    public void setup(Map<String, String> var1);

    public void change(String var1, Integer var2);
}

