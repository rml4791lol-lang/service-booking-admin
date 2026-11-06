/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.LogsController
 *  com.mdd.front.service.ILogsService
 *  com.mdd.front.validate.common.PageValidate
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

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.ILogsService;
import com.mdd.front.validate.common.PageValidate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/logs"})
@Api(tags={"\u65e5\u5fd7\u7ba1\u7406"})
public class LogsController {
    @Resource
    ILogsService iLogsService;

    @GetMapping(value={"/userMoney"})
    @ApiOperation(value="\u7528\u6237\u91d1\u989d\u53d8\u52a8\u65e5\u5fd7")
    public AjaxResult<Object> userMoney(@Validated PageValidate pageValidate, @RequestParam(defaultValue="0") Integer type) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        PageResult list = this.iLogsService.userMoney(pageValidate, userId, type);
        return AjaxResult.success(list);
    }
}

