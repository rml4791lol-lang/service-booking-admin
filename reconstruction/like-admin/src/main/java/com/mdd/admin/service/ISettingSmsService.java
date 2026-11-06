/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.service.ISettingSmsService
 */
package com.mdd.admin.service;

import com.alibaba.fastjson2.JSONObject;
import java.util.List;
import java.util.Map;

public interface ISettingSmsService {
    public List<Map<String, Object>> getConfig();

    public Map<String, Object> detail(String var1);

    public void setConfig(JSONObject var1);
}

