/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.front.controller.SearchController
 *  com.mdd.front.service.ISearchService
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.front.service.ISearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/search"})
@Api(tags={"\u641c\u7d22"})
public class SearchController {
    @Resource
    ISearchService iSearchService;

    @NotLogin
    @GetMapping(value={"/hotLists"})
    @ApiOperation(value="\u641c\u7d22\u5217\u8868")
    public AjaxResult<JSONObject> hotLists() {
        JSONObject result = this.iSearchService.hotLists();
        return AjaxResult.success(result);
    }
}

