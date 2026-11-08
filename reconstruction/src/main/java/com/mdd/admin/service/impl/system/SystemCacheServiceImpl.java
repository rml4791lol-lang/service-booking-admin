/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.impl.system.SystemCacheServiceImpl
 *  com.mdd.admin.service.system.ISystemCacheService
 *  com.mdd.common.cache.ConfigCache
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl.system;

import com.mdd.admin.service.system.ISystemCacheService;
import com.mdd.common.cache.ConfigCache;
import org.springframework.stereotype.Service;

@Service
public class SystemCacheServiceImpl
implements ISystemCacheService {
    public void clear() {
        ConfigCache.clear();
    }
}

