/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.alibaba.fastjson2.JSONWriter$Feature
 *  com.mdd.admin.service.ISettingSmsService
 *  com.mdd.admin.service.impl.SettingSmsServiceImpl
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.YmlUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.mdd.admin.service.ISettingSmsService;
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
public class SettingSmsServiceImpl
implements ISettingSmsService {
    public List<Map<String, Object>> getConfig() {
        String engine = ConfigUtils.get((String)"sms", (String)"engine", (String)"ali");
        LinkedList<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
        LinkedHashMap<String, Object> aliyun = new LinkedHashMap<String, Object>();
        aliyun.put("name", "\u963f\u91cc\u4e91\u77ed\u4fe1");
        aliyun.put("type", "ali");
        aliyun.put("status", engine.equalsIgnoreCase("ali") ? 1 : 0);
        list.add(aliyun);
        LinkedHashMap<String, Object> tencent = new LinkedHashMap<String, Object>();
        tencent.put("name", "\u817e\u8baf\u4e91\u77ed\u4fe1");
        tencent.put("type", "tencent");
        tencent.put("status", engine.equalsIgnoreCase("tencent") ? 1 : 0);
        list.add(tencent);
        return list;
    }

    public Map<String, Object> detail(String type) {
        String env = YmlUtils.get((String)"like.production");
        boolean envStatus = StringUtils.isNotNull(env) && env.equals("true");
        String engine = ConfigUtils.get((String)"sms", (String)"engine", (String)"ali");
        Map config = ConfigUtils.getMap((String)"sms", (String)type);
        config = StringUtils.isNotNull(config) ? config : Collections.emptyMap();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", config.getOrDefault("name", ""));
        map.put("status", engine.equalsIgnoreCase(type) ? 1 : 0);
        map.put("type", type);
        map.put("sign", config.getOrDefault("sign", ""));
        switch (type) {
            case "ali": {
                map.put("app_key", envStatus ? "******" : config.getOrDefault("app_key", ""));
                map.put("secret_key", envStatus ? "******" : config.getOrDefault("secret_key", ""));
                break;
            }
            case "tencent": {
                map.put("app_id", envStatus ? "******" : config.getOrDefault("app_id", ""));
                map.put("secret_id", envStatus ? "******" : config.getOrDefault("secret_id", ""));
                map.put("secret_key", envStatus ? "******" : config.getOrDefault("secret_key", ""));
                break;
            }
        }
        return map;
    }

    public void setConfig(JSONObject params) {
        String type = params.getString("type");
        ConfigUtils.set((String)"sms", (String)type, (String)params.toJSONString(new JSONWriter.Feature[0]));
        String engine = ConfigUtils.get((String)"sms", (String)"engine", (String)"");
        Integer status = ParamUtils.intOrDefault(params.getString("status"), 0, "状态");
        if (status == 1) {
            ConfigUtils.set((String)"sms", (String)"engine", (String)type);
        } else if (engine.equals(type) && status == 0) {
            ConfigUtils.set((String)"sms", (String)"engine", (String)"");
        }
    }
}

