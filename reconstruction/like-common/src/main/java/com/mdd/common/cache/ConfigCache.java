/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSON
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 */
package com.mdd.common.cache;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.Config;
import com.mdd.common.mapper.ConfigMapper;
import com.mdd.common.util.MapUtils;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.SpringUtils;
import com.mdd.common.util.StringUtils;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigCache {
    private static final String cacheKey = "config";

    public static void set() {
        ConfigMapper model = SpringUtils.getBean(ConfigMapper.class);
        List<Config> configs = (List<Config>) model.selectList(new QueryWrapper<Config>().select(new String[]{"id", "type", "name", "value"}));
        List<String> typeList = configs.stream().map(Config::getType).distinct().collect(Collectors.toList());
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> subMap = new LinkedHashMap<String, String>();
        for (String typeItem : typeList) {
            subMap.clear();
            for (Config configItem : configs) {
                if (!configItem.getType().equals(typeItem)) continue;
                subMap.put(configItem.getName(), configItem.getValue());
            }
            map.put(typeItem, JSON.toJSONString(subMap));
        }
        RedisUtils.set(cacheKey, JSON.toJSONString(map));
    }

    public static String get(String type, String name) {
        Map<String, String> configData;
        Map<String, String> configType;
        Object configs = RedisUtils.get(cacheKey);
        if (!StringUtils.isNull(configs) && !StringUtils.isEmpty(configs.toString()) && !StringUtils.isEmpty(configType = MapUtils.jsonToMap(configs.toString())) && StringUtils.isNotNull(configData = MapUtils.jsonToMap(configType.get(type)))) {
            return configData.get(name);
        }
        return null;
    }

    public static Map<String, String> get(String type) {
        Map<String, String> cacheConfigMap;
        Object cacheConfigObj;
        Map<String, Object> cacheConfig = ConfigCache.getConfigCache();
        if (!cacheConfig.isEmpty() && (cacheConfigObj = cacheConfig.get(type)) != null && !(cacheConfigMap = MapUtils.jsonToMap(cacheConfigObj.toString())).isEmpty()) {
            return cacheConfigMap;
        }
        return new LinkedHashMap<String, String>();
    }

    public static Map<String, Object> getConfigCache() {
        Object config = RedisUtils.get(cacheKey);
        if (!StringUtils.isNull(config) && !StringUtils.isEmpty(config.toString())) {
            return MapUtils.jsonToMapAsObj(config.toString());
        }
        return new LinkedHashMap<String, Object>();
    }

    public static void clear() {
        RedisUtils.del(cacheKey);
    }
}

