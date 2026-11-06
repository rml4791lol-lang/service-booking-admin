/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.cache.CaptchaCache
 *  com.mdd.common.util.IpUtils
 *  com.mdd.common.util.RedisUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.YmlUtils
 */
package com.mdd.admin.cache;

import com.mdd.common.util.IpUtils;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.YmlUtils;

public class CaptchaCache {
    public static String get(String uuid) {
        String ip = IpUtils.getIpAddress().replaceAll("\\.", "");
        String captchaKey = YmlUtils.get((String)"like.captcha.token") + ip + ":" + uuid;
        Object code = RedisUtils.get((String)captchaKey);
        if (StringUtils.isNull(code) || StringUtils.isEmpty((String)code.toString())) {
            return "";
        }
        RedisUtils.del((String[])new String[]{captchaKey});
        return code.toString();
    }

    public static void set(String code, String uuid) {
        String ip = IpUtils.getIpAddress().replaceAll("\\.", "");
        String verifyKey = YmlUtils.get((String)"like.captcha.token") + ip + ":" + uuid;
        long expireTime = Long.parseLong(YmlUtils.get((String)"like.captcha.expire"));
        RedisUtils.set((String)verifyKey, (Object)code.toLowerCase(), (long)expireTime);
    }
}

