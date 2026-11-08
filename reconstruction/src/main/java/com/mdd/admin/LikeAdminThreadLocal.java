/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.common.util.ListUtils
 */
package com.mdd.admin;

import com.mdd.common.util.ListUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/*
 * Exception performing whole class analysis ignored.
 */
public class LikeAdminThreadLocal {
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
        map.put(key, val);
        MY_LOCAL.set(map);
    }

    public static Object get(String key) {
        return ((Map)MY_LOCAL.get()).getOrDefault(key, "");
    }

    public static Integer getAdminId() {
        String adminId = LikeAdminThreadLocal.get((String)"adminId").toString();
        if (adminId.equals("")) {
            return 0;
        }
        return Integer.parseInt(adminId);
    }

    public static List<Integer> getRoleIds() {
        String roleIds = LikeAdminThreadLocal.get((String)"roleIds").toString();
        if (roleIds.equals("") || roleIds.equals("0")) {
            return Collections.emptyList();
        }
        return ListUtils.stringToListAsInt((String)roleIds, (String)",");
    }

    public static void remove() {
        MY_LOCAL.remove();
    }
}

