/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.config;

import java.util.HashMap;
import java.util.Map;

public class ProjectConfig {
    public static Map<String, Integer> lists = new HashMap<String, Integer>(){
        {
            this.put("pageSizeMax", 25000);
            this.put("pageSize", 25);
        }
    };
}

