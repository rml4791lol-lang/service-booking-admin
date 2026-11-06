/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.util.RedisUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.YmlUtils
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.cache.TokenLoginCache
 */
package com.mdd.front.cache;

import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.YmlUtils;
import com.mdd.front.LikeFrontThreadLocal;

/*
 * Exception performing whole class analysis ignored.
 */
public class TokenLoginCache {
    private static final String KEY = "login:front:token:";

    public static Integer get() {
        Integer terminal = LikeFrontThreadLocal.getTerminal();
        String token = LikeFrontThreadLocal.getToken();
        String key = "login:front:token:" + terminal + ":" + token;
        Object o = RedisUtils.get((String)key);
        if (StringUtils.isNull(o)) {
            return 0;
        }
        return Integer.parseInt(o.toString());
    }

    public static void set(String token, Object id) {
        Integer terminal = LikeFrontThreadLocal.getTerminal();
        String key = "login:front:token:" + terminal + ":" + token;
        RedisUtils.set((String)key, (Object)id, (long)timeout());
    }

    public static void set(String token, Object id, String terminal) {
        String key = "login:front:token:" + terminal + ":" + token;
        RedisUtils.set((String)key, (Object)id, (long)timeout());
    }

    /**
     * 与 la_user_session.expire_time 保持同一口径：
     * 优先 like.login.timeout（老系统显式配置），否则回退 sa-token.timeout（默认 30 天）。
     */
    private static int timeout() {
        String value = YmlUtils.get((String)"like.login.timeout");
        if (StringUtils.isBlank(value)) {
            value = YmlUtils.get((String)"sa-token.timeout");
        }
        if (StringUtils.isBlank(value)) {
            return 7200;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 7200;
        }
    }

    public static void del() {
        Integer terminal = LikeFrontThreadLocal.getTerminal();
        String token = LikeFrontThreadLocal.getToken();
        TokenLoginCache.del((Integer)terminal, (String)token);
    }

    public static void del(Integer terminal, String token) {
        String key = "login:front:token:" + terminal + ":" + token;
        RedisUtils.del((String[])new String[]{key});
    }
}

