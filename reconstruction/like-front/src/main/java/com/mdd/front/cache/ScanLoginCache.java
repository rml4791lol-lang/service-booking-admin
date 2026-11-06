/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.util.RedisUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.front.cache.ScanLoginCache
 */
package com.mdd.front.cache;

import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.StringUtils;

public class ScanLoginCache {
    private static final String KEY = "wechat:scan:login:";

    public static String get(String sessionId) {
        Object o = RedisUtils.get((String)(KEY + sessionId));
        if (StringUtils.isNull(o)) {
            return "";
        }
        RedisUtils.del((String[])new String[]{KEY + sessionId});
        return o.toString();
    }

    public static void set(String sessionId, String state) {
        RedisUtils.set((String)(KEY + sessionId), (Object)state, (long)600L);
    }
}

