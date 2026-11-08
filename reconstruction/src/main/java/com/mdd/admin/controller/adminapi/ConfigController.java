/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.controller.adminapi.ConfigController
 *  com.mdd.admin.service.IIndexService
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.core.AjaxResult
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.adminapi;

import com.mdd.admin.service.IIndexService;
import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"adminapi/config"})
@Api(tags={"\u4e3b\u9875\u6570\u636e\u7ba1\u7406"})
public class ConfigController {
    private static final Logger log = LoggerFactory.getLogger(ConfigController.class);
    @Resource
    IIndexService iIndexService;

    @NotLogin
    @GetMapping(value={"/getConfig"})
    @ApiOperation(value="\u516c\u5171\u914d\u7f6e")
    public AjaxResult<Map<String, Object>> getConfig() {
        Map map = this.iIndexService.config();
        return AjaxResult.success(map);
    }
}

