/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.mapper.notice.NoticeRecordMapper
 *  com.mdd.common.validator.annotation.IDMust
 *  com.mdd.front.controller.IndexController
 *  com.mdd.front.service.IIndexService
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.front.controller;

import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.mapper.notice.NoticeRecordMapper;
import com.mdd.common.validator.annotation.IDMust;
import com.mdd.front.service.IIndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/index"})
@Api(tags={"\u4e3b\u9875\u7ba1\u7406"})
public class IndexController {
    @Resource
    NoticeRecordMapper noticeRecordMapper;
    @Resource
    IIndexService iIndexService;

    @NotLogin
    @GetMapping(value={"/index"})
    @ApiOperation(value="\u9996\u9875\u6570\u636e")
    public AjaxResult<Map<String, Object>> index() {
        Map detail = this.iIndexService.index();
        return AjaxResult.success(detail);
    }

    @NotLogin
    @GetMapping(value={"/decorate"})
    @ApiOperation(value="\u88c5\u4fee\u6570\u636e")
    public AjaxResult<Map<String, Object>> decorate(@Validated @IDMust @RequestParam(value="id") Integer id) {
        Map detail = this.iIndexService.decorate(id);
        return AjaxResult.success(detail);
    }

    @NotLogin
    @GetMapping(value={"/config"})
    @ApiOperation(value="\u516c\u5171\u914d\u7f6e")
    public AjaxResult<Map<String, Object>> config() {
        Map map = this.iIndexService.config();
        return AjaxResult.success(map);
    }

    @NotLogin
    @GetMapping(value={"/policy"})
    @ApiOperation(value="\u653f\u7b56\u534f\u8bae")
    public AjaxResult<Map<String, String>> policy(@RequestParam String type) {
        Map map = this.iIndexService.policy(type);
        return AjaxResult.success(map);
    }

    @NotLogin
    @GetMapping(value={"/hotSearch"})
    @ApiOperation(value="\u70ed\u95e8\u641c\u7d22")
    public AjaxResult<List<String>> hotSearch() {
        List list = this.iIndexService.hotSearch();
        return AjaxResult.success(list);
    }

    @NotLogin
    @GetMapping(value={"/weather"})
    @ApiOperation(value="\u83b7\u53d6\u5b9e\u65f6\u5929\u6c14")
    public AjaxResult<Map<String, String>> weather(@RequestParam(value="lat", defaultValue="") String lat, @RequestParam(value="lng", defaultValue="") String lng) {
        Map map = this.iIndexService.weather(lat, lng);
        return AjaxResult.success(map);
    }
}

