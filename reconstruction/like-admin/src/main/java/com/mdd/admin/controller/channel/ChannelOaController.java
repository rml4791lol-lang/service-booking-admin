/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.channel.ChannelOaController
 *  com.mdd.admin.service.IChannelOaConfigService
 *  com.mdd.admin.validate.channel.ChannelOaValidate
 *  com.mdd.admin.vo.channel.ChannelOaVo
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
package com.mdd.admin.controller.channel;

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.IChannelOaConfigService;
import com.mdd.admin.validate.channel.ChannelOaValidate;
import com.mdd.admin.vo.channel.ChannelOaVo;
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
@RequestMapping(value={"/adminapi/channel.official_account_setting"})
@Api(tags={"\u516c\u4f17\u53f7\u6e20\u9053\u8bbe\u7f6e"})
public class ChannelOaController {
    @Resource
    IChannelOaConfigService iChannelOaConfigService;

    @GetMapping(value={"/getConfig"})
    @ApiOperation(value="\u516c\u4f17\u53f7\u6e20\u9053\u8bbe\u7f6e\u8be6\u60c5")
    public AjaxResult<ChannelOaVo> getConfig() {
        ChannelOaVo vo = this.iChannelOaConfigService.getConfig();
        return AjaxResult.success(vo);
    }

    @Log(title="\u516c\u4f17\u53f7\u6e20\u9053\u8bbe\u7f6e\u4fdd\u5b58")
    @PostMapping(value={"/setConfig"})
    @ApiOperation(value="\u516c\u4f17\u53f7\u6e20\u9053\u8bbe\u7f6e\u4fdd\u5b58")
    public AjaxResult<Object> setConfig(@Validated @RequestBody ChannelOaValidate channelOaValidate) {
        this.iChannelOaConfigService.setConfig(channelOaValidate);
        return AjaxResult.success();
    }
}

