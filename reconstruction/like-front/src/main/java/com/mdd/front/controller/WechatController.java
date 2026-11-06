/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.front.controller.WechatController
 *  com.mdd.front.service.IWechatService
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  javax.validation.constraints.NotEmpty
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.front.controller;

import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.front.service.IWechatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"api/wechat"})
@Api(tags={"\u5fae\u4fe1\u7ba1\u7406"})
public class WechatController {
    @Resource
    IWechatService iWechatService;

    @NotLogin
    @GetMapping(value={"/jsConfig"})
    @ApiOperation(value="\u5fae\u4fe1jsConfig")
    public AjaxResult<Object> jsConfig(@Validated @NotEmpty @RequestParam(value="url") String url) throws Exception {
        Map map = this.iWechatService.jsConfig(url);
        return AjaxResult.success(map);
    }
}

