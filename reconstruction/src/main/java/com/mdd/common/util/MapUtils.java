/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSON
 *  com.alibaba.fastjson2.JSONObject
 *  com.alibaba.fastjson2.JSONWriter$Feature
 *  com.google.gson.reflect.TypeToken
 */
package com.mdd.common.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    public static Map<String, String> jsonToMap(String json) {
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return (Map)JSON.parseObject((String)json, (Type)type);
    }

    public static Map<String, Object> jsonToMapAsObj(String json) {
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        return (Map)JSON.parseObject((String)json, (Type)type);
    }

    public static Map<String, String> objectToMap(Object object) {
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return (Map)JSON.parseObject((String)JSONObject.toJSONString((Object)object, (JSONWriter.Feature[])new JSONWriter.Feature[0]), (Type)type);
    }

    public static Map<String, Object> mergeMapByObj(Map<String, Object> map, Map<String, Object> map1) {
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.putAll(map);
        map2.putAll(map1);
        return map2;
    }

    public static Map<String, String> mergeMapByStr(Map<String, String> map, Map<String, String> map1) {
        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.putAll(map);
        map2.putAll(map1);
        return map2;
    }
}

