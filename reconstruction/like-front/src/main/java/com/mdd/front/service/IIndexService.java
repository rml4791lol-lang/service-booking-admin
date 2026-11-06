/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.service.IIndexService
 */
package com.mdd.front.service;

import java.util.List;
import java.util.Map;

public interface IIndexService {
    public Map<String, Object> index();

    public Map<String, Object> decorate(Integer var1);

    public Map<String, Object> config();

    public Map<String, String> policy(String var1);

    public List<String> hotSearch();

    public Map<String, String> weather(String var1, String var2);
}

