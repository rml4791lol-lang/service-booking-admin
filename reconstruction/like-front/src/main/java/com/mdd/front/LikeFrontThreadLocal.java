/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.util.StringUtils
 *  com.mdd.front.LikeFrontThreadLocal
 */
package com.mdd.front;

import com.mdd.common.util.StringUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/*
 * Exception performing whole class analysis ignored.
 */
public class LikeFrontThreadLocal {
    private static final ThreadLocal<Map<String, Object>> MY_LOCAL = new ThreadLocal();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void put(String key, Object val) {
        ConcurrentSkipListMap<String, Object> map = (ConcurrentSkipListMap<String, Object>)MY_LOCAL.get();
        if (map == null) {
            ThreadLocal threadLocal = MY_LOCAL;
            synchronized (threadLocal) {
                map = new ConcurrentSkipListMap<String, Object>();
            }
        }
        if (StringUtils.isNull(val)) {
            return;
        }
        map.put(key, val);
        MY_LOCAL.set(map);
    }

    public static Object get(String key) {
        Map map = (Map)MY_LOCAL.get();
        if (map == null) {
            return null;
        }
        return map.getOrDefault(key, "");
    }

    public static Integer getUserId() {
        Object adminId = LikeFrontThreadLocal.get((String)"userId");
        if (adminId == null || adminId.toString().equals("")) {
            return 0;
        }
        return Integer.parseInt(adminId.toString());
    }

    public static Integer getTerminal() {
        Object adminId = LikeFrontThreadLocal.get((String)"terminal");
        if (adminId == null || adminId.toString().equals("")) {
            return 0;
        }
        return Integer.parseInt(adminId.toString());
    }

    public static String getToken() {
        Object token = LikeFrontThreadLocal.get((String)"token");
        if (token == null || token.toString().equals("")) {
            return "";
        }
        return token.toString();
    }

    public static void remove() {
        MY_LOCAL.remove();
    }
}

