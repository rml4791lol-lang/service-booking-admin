/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IIndexService
 */
package com.mdd.admin.service;

import java.util.Map;

public interface IIndexService {
    public Map<String, Object> index();

    public Map<String, Object> config();

    public Map<String, Object> topStats();
}

