/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.channel.ChannelMpController
 *  com.mdd.admin.service.IChannelMpConfigService
 *  com.mdd.admin.validate.channel.ChannelMpValidate
 *  com.mdd.admin.vo.channel.ChannelMpVo
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
import com.mdd.admin.service.IChannelMpConfigService;
import com.mdd.admin.validate.channel.ChannelMpValidate;
import com.mdd.admin.vo.channel.ChannelMpVo;
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
@RequestMapping(value={"/adminapi/channel.mnp_settings"})
@Api(tags={"\u5fae\u4fe1\u7a0b\u5e8f\u6e20\u9053"})
public class ChannelMpController {
    @Resource
    IChannelMpConfigService iChannelMpConfigService;

    @GetMapping(value={"/getConfig"})
    @ApiOperation(value="\u5fae\u4fe1\u7a0b\u5e8f\u6e20\u9053\u8bbe\u7f6e\u8be6\u60c5")
    public AjaxResult<ChannelMpVo> getConfig() {
        ChannelMpVo vo = this.iChannelMpConfigService.getConfig();
        return AjaxResult.success(vo);
    }

    @Log(title="\u5fae\u4fe1\u5c0f\u7a0b\u5e8f\u6e20\u9053\u8bbe\u7f6e\u4fdd\u5b58")
    @PostMapping(value={"/setConfig"})
    @ApiOperation(value="\u5fae\u4fe1\u7a0b\u5e8f\u6e20\u9053\u8bbe\u7f6e\u4fdd\u5b58")
    public AjaxResult<Object> setConfig(@Validated @RequestBody ChannelMpValidate channelMpValidate) {
        this.iChannelMpConfigService.setConfig(channelMpValidate);
        return AjaxResult.success();
    }
}

