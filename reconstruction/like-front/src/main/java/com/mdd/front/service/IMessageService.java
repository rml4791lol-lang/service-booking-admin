/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.service.IMessageService
 */
package com.mdd.front.service;

import java.util.Map;

public interface IMessageService {
    public void send(Integer var1, String var2, Map<String, Object> var3);

    public Integer unreadCount(Integer var1);

    public Map<String, Object> list(Integer var1, Integer var2, Integer var3);

    public void read(Integer var1, Integer var2);
}

