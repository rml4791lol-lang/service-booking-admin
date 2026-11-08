/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.system.SystemCacheController
 *  com.mdd.admin.service.system.ISystemCacheService
 *  com.mdd.common.core.AjaxResult
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.system;

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.system.ISystemCacheService;
import com.mdd.common.core.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/setting.system.cache"})
@Api(tags={"\u7cfb\u7edf\u7f13\u5b58\u7ba1\u7406"})
public class SystemCacheController {
    @Resource
    ISystemCacheService iSystemCacheService;

    @Log(title="\u6e05\u9664\u7cfb\u7edf\u7f13\u5b58")
    @PostMapping(value={"/clear"})
    @ApiOperation(value="\u6e05\u9664\u7cfb\u7edf\u7f13\u5b58")
    public AjaxResult<Object> clear() {
        this.iSystemCacheService.clear();
        return AjaxResult.success();
    }
}

