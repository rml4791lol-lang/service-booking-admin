/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.monitor.MonitorCacheController
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.util.StringUtils
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.data.redis.connection.RedisServerCommands
 *  org.springframework.data.redis.core.RedisTemplate
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.monitor;

import com.mdd.admin.aop.Log;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import javax.annotation.Resource;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.redis.core.RedisCallback;

@RestController
@RequestMapping(value={"api/monitor"})
@Api(tags={"\u76d1\u63a7\u7f13\u5b58\u7ba1\u7406"})
public class MonitorCacheController {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Log(title="\u7f13\u5b58\u76d1\u63a7")
    @GetMapping(value={"/cache"})
    @ApiOperation(value="\u7f13\u5b58\u76d1\u63a7")
    public AjaxResult<Object> info() {
        Properties info = (Properties)this.redisTemplate.execute((RedisCallback)RedisServerCommands::info);
        Properties commandStats = (Properties)this.redisTemplate.execute((RedisCallback)(connection -> connection.info("commandstats")));
        Object dbSize = this.redisTemplate.execute(RedisServerCommands::dbSize);
        if (commandStats == null) {
            return AjaxResult.failed((String)"\u83b7\u53d6\u5f02\u5e38");
        }
        HashMap<String, Object> result = new HashMap<String, Object>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);
        ArrayList pieList = new ArrayList();
        commandStats.stringPropertyNames().forEach(key -> {
            HashMap<String, String> data = new HashMap<String, String>(2);
            String property = commandStats.getProperty((String)key);
            data.put("name", StringUtils.removeStart((String)key, (String)"cmdstat_"));
            data.put("value", StringUtils.substringBetween((String)property, (String)"calls=", (String)",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return AjaxResult.success(result);
    }
}

