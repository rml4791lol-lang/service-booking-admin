/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.decorate.DecorateTabbarController
 *  com.mdd.admin.service.IDecorateTabbarService
 *  com.mdd.admin.validate.decorate.DecorateTabsValidate
 *  com.mdd.admin.vo.decorate.DecorateTabbarVo
 *  com.mdd.common.core.AjaxResult
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.decorate;

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.IDecorateTabbarService;
import com.mdd.admin.validate.decorate.DecorateTabsValidate;
import com.mdd.admin.vo.decorate.DecorateTabbarVo;
import com.mdd.common.core.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/decorate.tabbar"})
@Api(tags={"\u88c5\u4fee\u5bfc\u822a\u7ba1\u7406"})
public class DecorateTabbarController {
    @Resource
    IDecorateTabbarService iDecorateTabbarService;

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u5e95\u90e8\u5bfc\u822a\u8be6\u60c5")
    public AjaxResult<DecorateTabbarVo> detail() {
        DecorateTabbarVo vo = this.iDecorateTabbarService.detail();
        return AjaxResult.success(vo);
    }

    @Log(title="\u5e95\u90e8\u5bfc\u822a\u7f16\u8f91")
    @PostMapping(value={"/save"})
    @ApiOperation(value="\u5e95\u90e8\u5bfc\u822a\u7f16\u8f91")
    public AjaxResult<Object> save(@Validated @RequestBody DecorateTabsValidate tabsValidate) {
        this.iDecorateTabbarService.save(tabsValidate);
        return AjaxResult.success();
    }
}

