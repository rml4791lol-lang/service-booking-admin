/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSON
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.mdd.admin.service.ISettingStorageService
 *  com.mdd.admin.service.impl.SettingStorageServiceImpl
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.YmlUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mdd.admin.service.ISettingStorageService;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.ParamUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.YmlUtils;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SettingStorageServiceImpl
implements ISettingStorageService {
    public List<Map<String, Object>> list() {
        String engine = ConfigUtils.get((String)"storage", (String)"default", (String)"local");
        LinkedList<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
        LinkedHashMap<String, Object> local = new LinkedHashMap<String, Object>();
        local.put("name", "\u672c\u5730\u5b58\u50a8");
        local.put("engine", "local");
        local.put("path", "\u5b58\u50a8\u5728\u672c\u5730\u670d\u52a1\u5668");
        local.put("status", engine.equals("local") ? 1 : 0);
        list.add(local);
        LinkedHashMap<String, Object> qiniu = new LinkedHashMap<String, Object>();
        qiniu.put("name", "\u4e03\u725b\u4e91\u5b58\u50a8");
        qiniu.put("engine", "qiniu");
        qiniu.put("path", "\u5b58\u50a8\u5728\u4e03\u725b\u4e91\uff0c\u8bf7\u524d\u5f80\u4e03\u725b\u4e91\u5f00\u901a\u5b58\u50a8\u670d\u52a1");
        qiniu.put("status", engine.equals("qiniu") ? 1 : 0);
        list.add(qiniu);
        LinkedHashMap<String, Object> aliyun = new LinkedHashMap<String, Object>();
        aliyun.put("name", "\u963f\u91cc\u4e91OSS");
        aliyun.put("engine", "aliyun");
        aliyun.put("path", "\u5b58\u50a8\u5728\u963f\u91cc\u4e91\uff0c\u8bf7\u524d\u5f80\u963f\u91cc\u4e91\u5f00\u901a\u5b58\u50a8\u670d\u52a1");
        aliyun.put("status", engine.equals("aliyun") ? 1 : 0);
        list.add(aliyun);
        LinkedHashMap<String, Object> qcloud = new LinkedHashMap<String, Object>();
        qcloud.put("name", "\u817e\u8baf\u4e91COS");
        qcloud.put("engine", "qcloud");
        qcloud.put("path", "\u5b58\u50a8\u5728\u817e\u8baf\u4e91\uff0c\u8bf7\u524d\u5f80\u817e\u8baf\u4e91\u5f00\u901a\u5b58\u50a8\u670d\u52a1");
        qcloud.put("status", engine.equals("qcloud") ? 1 : 0);
        list.add(qcloud);
        return list;
    }

    public Map<String, Object> detail(String engine) {
        String env = YmlUtils.get((String)"like.production");
        boolean envStatus = StringUtils.isNotNull(env) && env.equals("true");
        String defaultEngine = ConfigUtils.get((String)"storage", (String)"default", (String)"local");
        // engine 缺参/空串时回退当前默认引擎：原先 engine=null 会走到 engine.equals("local") 抛 NPE（task-7）
        String engineKey = StringUtils.isNotEmpty(engine) ? engine : defaultEngine;
        Map config = ConfigUtils.getMap((String)"storage", (String)engineKey);
        config = StringUtils.isNotNull(config) ? config : Collections.emptyMap();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", config.getOrDefault("name", ""));
        map.put("engine", engineKey);
        map.put("status", defaultEngine.equals(engineKey) ? 1 : 0);
        if (!"local".equals(engineKey)) {
            map.put("bucket", config.getOrDefault("bucket", ""));
            map.put("secret_key", envStatus ? "******" : config.getOrDefault("secret_key", ""));
            map.put("access_key", envStatus ? "******" : config.getOrDefault("access_key", ""));
            map.put("domain", config.getOrDefault("domain", ""));
            if ("qcloud".equals(engineKey)) {
                map.put("region", config.getOrDefault("region", ""));
            }
            if ("aliyun".equals(engineKey)) {
                map.put("endpoint", config.getOrDefault("endpoint", ""));
            }
        }
        return map;
    }

    public void setup(Map<String, String> params) {
        Assert.notNull((Object)params.get("engine"), (String)"engine\u53c2\u6570\u7f3a\u5931", (Object[])new Object[0]);
        Assert.notNull((Object)params.get("status"), (String)"status\u53c2\u6570\u7f3a\u5931", (Object[])new Object[0]);
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("name", "\u672c\u5730\u5b58\u50a8");
        if (!params.get("engine").equals("local")) {
            map.put("bucket", params.getOrDefault("bucket", ""));
            map.put("secret_key", params.getOrDefault("secret_key", ""));
            map.put("access_key", params.getOrDefault("access_key", ""));
            map.put("domain", params.getOrDefault("domain", ""));
            switch (params.get("engine")) {
                case "qcloud": {
                    map.put("name", "\u817e\u8baf\u4e91\u5b58\u50a8");
                    map.put("region", params.getOrDefault("region", ""));
                    break;
                }
                case "qiniu": {
                    map.put("name", "\u4e03\u725b\u4e91\u5b58\u50a8");
                    break;
                }
                case "aliyun": {
                    map.put("name", "\u963f\u91cc\u4e91\u5b58\u50a8");
                    map.put("endpoint", params.getOrDefault("endpoint", ""));
                }
            }
        }
        ConfigUtils.set((String)"storage", (String)params.get("engine"), (String)JSON.toJSONString(map));
        String engine = ConfigUtils.get((String)"storage", (String)"default", (String)"local");
        Integer status = ParamUtils.intOrDefault(params.get("status"), 0, "状态");
        if (status == 1) {
            ConfigUtils.set((String)"storage", (String)"default", (String)params.get("engine"));
        } else if (engine.equals(params.get("engine")) && status == 0) {
            ConfigUtils.set((String)"storage", (String)"default", (String)"");
        }
    }

    public void change(String alias, Integer status) {
        String engine = ConfigUtils.get((String)"storage", (String)"default", (String)"local");
        if (engine.equals(alias) && status == 0) {
            ConfigUtils.set((String)"storage", (String)"default", (String)"");
        } else {
            ConfigUtils.set((String)"storage", (String)"default", (String)alias);
        }
    }
}

