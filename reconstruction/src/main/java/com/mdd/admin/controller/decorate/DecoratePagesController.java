/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.decorate.DecoratePagesController
 *  com.mdd.admin.service.IDecoratePageService
 *  com.mdd.admin.validate.decorate.DecoratePageValidate
 *  com.mdd.admin.vo.decorate.DecoratePageVo
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.validator.annotation.IDMust
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.decorate;

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.IDecoratePageService;
import com.mdd.admin.validate.decorate.DecoratePageValidate;
import com.mdd.admin.vo.decorate.DecoratePageVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/decorate.page"})
@Api(tags={"\u88c5\u4fee\u9875\u9762\u7ba1\u7406"})
public class DecoratePagesController {
    @Resource
    IDecoratePageService iDecoratePageService;

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u9875\u9762\u88c5\u4fee\u8be6\u60c5")
    public AjaxResult<DecoratePageVo> detail(@Validated @IDMust @RequestParam(value="id") Integer id) {
        DecoratePageVo vo = this.iDecoratePageService.detail(id);
        return AjaxResult.success(vo);
    }

    @Log(title="\u9875\u9762\u88c5\u4fee\u4fdd\u5b58")
    @PostMapping(value={"/save"})
    @ApiOperation(value="\u9875\u9762\u88c5\u4fee\u4fdd\u5b58")
    public AjaxResult<Object> save(@RequestBody DecoratePageValidate decoratePageValidate) {
        this.iDecoratePageService.save(decoratePageValidate);
        return AjaxResult.success();
    }
}

