/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.controller.IndexController
 *  com.mdd.admin.service.IIndexService
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
package com.mdd.admin.controller;

import com.mdd.admin.service.IIndexService;
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
@RequestMapping(value={"/adminapi/workbench"})
@Api(tags={"\u4e3b\u9875\u6570\u636e\u7ba1\u7406"})
public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);
    @Resource
    IIndexService iIndexService;

    @GetMapping(value={"/index"})
    @ApiOperation(value="\u63a7\u5236\u53f0")
    public AjaxResult<Map<String, Object>> index() {
        Map map = this.iIndexService.index();
        return AjaxResult.success(map);
    }

    @GetMapping(value={"/topStats"})
    @ApiOperation(value="\u5de5\u4f5c\u53f0\u9876\u90e8\u7edf\u8ba1")
    public AjaxResult<Map<String, Object>> topStats() {
        Map map = this.iIndexService.topStats();
        return AjaxResult.success(map);
    }
}

