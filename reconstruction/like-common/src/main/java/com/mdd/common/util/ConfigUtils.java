/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 */
package com.mdd.common.util;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.cache.ConfigCache;
import com.mdd.common.entity.Config;
import com.mdd.common.entity.setting.DevPayConfig;
import com.mdd.common.enums.PaymentEnum;
import com.mdd.common.mapper.ConfigMapper;
import com.mdd.common.mapper.setting.DevPayConfigMapper;
import com.mdd.common.util.MapUtils;
import com.mdd.common.util.SpringUtils;
import com.mdd.common.util.StringUtils;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigUtils {
    public static Map<String, String> get(String type) {
        Map<String, String> cache = ConfigCache.get(type);
        if (!cache.isEmpty()) {
            return cache;
        }
        ConfigMapper model = SpringUtils.getBean(ConfigMapper.class);
        List<Config> configs = (List<Config>) model.selectList(new QueryWrapper<Config>().select(new String[]{"id", "type", "name", "value"}).eq("type", type));
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        for (Config config : configs) {
            map.put(config.getName(), config.getValue());
        }
        ConfigCache.set();
        return map;
    }

    public static String get(String type, String name) {
        String cache = ConfigCache.get(type, name);
        if (!StringUtils.isNull(cache) && !StringUtils.isEmpty(cache)) {
            return cache;
        }
        ConfigMapper model = SpringUtils.getBean(ConfigMapper.class);
        Config config = model.selectOne(new QueryWrapper<Config>().select(new String[]{"id", "type", "name", "value"}).eq("name", name).eq("type", type));
        ConfigCache.set();
        return config.getValue();
    }

    public static String get(String type, String name, String defaults) {
        String cache = ConfigCache.get(type, name);
        if (!StringUtils.isNull(cache) && !StringUtils.isEmpty(cache)) {
            return cache;
        }
        ConfigMapper model = SpringUtils.getBean(ConfigMapper.class);
        Config config = (Config)model.selectOne(new QueryWrapper<Config>().select(new String[]{"id", "type", "name", "value"}).eq("type", type).eq("name", (Object)name));
        if (config == null) {
            return defaults;
        }
        ConfigCache.set();
        return config.getValue();
    }

    public static Map<String, String> getMap(String type, String name) {
        String cache = ConfigCache.get(type, name);
        if (!StringUtils.isNull(cache) && !StringUtils.isEmpty(cache)) {
            cache = "[]".equals(cache) ? "{}" : cache;
            return MapUtils.jsonToMap(cache);
        }
        ConfigMapper model = SpringUtils.getBean(ConfigMapper.class);
        Config config = (Config)model.selectOne(new QueryWrapper<Config>().select(new String[]{"id", "type", "name", "value"}).eq("type", type).eq("name", (Object)name));
        if (config == null) {
            return null;
        }
        if (config.getValue().equals("") || config.getValue().equals("[]") || config.getValue().equals("{}")) {
            return new LinkedHashMap<String, String>();
        }
        ConfigCache.set();
        return MapUtils.jsonToMap(config.getValue());
    }

    public static void set(String type, String name, String val) {
        ConfigMapper model = SpringUtils.getBean(ConfigMapper.class);
        Config config = model.selectOne(new QueryWrapper<Config>().eq("type", type).eq("name", name));
        if (config != null) {
            config.setValue(val);
            config.setUpdateTime(System.currentTimeMillis() / 1000L);
            model.updateById(config);
        } else {
            Config systemConfig = new Config();
            systemConfig.setType(type);
            systemConfig.setName(name);
            systemConfig.setValue(val);
            systemConfig.setCreateTime(System.currentTimeMillis() / 1000L);
            systemConfig.setUpdateTime(System.currentTimeMillis() / 1000L);
            model.insert(systemConfig);
        }
        ConfigCache.set();
    }

    public static String getAliDevPay(String field) {
        DevPayConfigMapper model = SpringUtils.getBean(DevPayConfigMapper.class);
        DevPayConfig devPay = (DevPayConfig)model.selectOne(new QueryWrapper<DevPayConfig>().eq("pay_way", PaymentEnum.ALI_PAY.getCode()));
        if (StringUtils.isNull(devPay)) {
            return "";
        }
        JSONObject jsonObject = JSONObject.parseObject((String)devPay.getConfig().toString());
        return StringUtils.isNull(jsonObject.getString(field)) ? "" : jsonObject.getString(field);
    }
}

