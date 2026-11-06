/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.controller.decorate.DecorateDataController
 *  com.mdd.admin.service.IDecorateDataService
 *  com.mdd.admin.vo.decorate.DecorateDataArticleVo
 *  com.mdd.common.aop.NotPower
 *  com.mdd.common.core.AjaxResult
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.decorate;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.service.IDecorateDataService;
import com.mdd.admin.vo.decorate.DecorateDataArticleVo;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/decorate.data"})
@Api(tags={"\u88c5\u4fee\u6570\u636e\u7ba1\u7406"})
public class DecorateDataController {
    @Resource
    IDecorateDataService iDecorateDataService;

    @NotPower
    @GetMapping(value={"/article"})
    @ApiOperation(value="\u83b7\u53d6\u6587\u7ae0\u6570\u636e")
    public AjaxResult<List<DecorateDataArticleVo>> article(@RequestParam(defaultValue="10") Integer limit) {
        List list = this.iDecorateDataService.article(limit);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/pc"})
    @ApiOperation(value="\u83b7\u53d6pc\u88c5\u4fee\u6570\u636e")
    public AjaxResult<JSONObject> pc() {
        JSONObject pc = this.iDecorateDataService.pc();
        return AjaxResult.success(pc);
    }
}

