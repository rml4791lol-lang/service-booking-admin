/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONArray
 *  com.mdd.admin.service.IChannelOaMenusService
 */
package com.mdd.admin.service;

import com.alibaba.fastjson2.JSONArray;
import java.util.List;

public interface IChannelOaMenusService {
    public JSONArray detail();

    public void save(List<Object> var1, Boolean var2);
}

