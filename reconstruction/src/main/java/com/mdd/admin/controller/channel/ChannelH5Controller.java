/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.channel.ChannelH5Controller
 *  com.mdd.admin.service.IChannelH5ConfigService
 *  com.mdd.admin.validate.channel.ChannelH5Validate
 *  com.mdd.admin.vo.channel.ChannelH5Vo
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
import com.mdd.admin.service.IChannelH5ConfigService;
import com.mdd.admin.validate.channel.ChannelH5Validate;
import com.mdd.admin.vo.channel.ChannelH5Vo;
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
@RequestMapping(value={"/adminapi/channel.web_page_setting"})
@Api(tags={"\u79fb\u52a8\u6e20\u9053\u8bbe\u7f6e"})
public class ChannelH5Controller {
    @Resource
    IChannelH5ConfigService iChannelH5ConfigService;

    @GetMapping(value={"/getConfig"})
    @ApiOperation(value="H5\u6e20\u9053\u8bbe\u7f6e\u8be6\u60c5")
    public AjaxResult<ChannelH5Vo> getConfig() {
        ChannelH5Vo vo = this.iChannelH5ConfigService.getConfig();
        return AjaxResult.success(vo);
    }

    @Log(title="H5\u6e20\u9053\u8bbe\u7f6e\u4fdd\u5b58")
    @PostMapping(value={"/setConfig"})
    @ApiOperation(value="H5\u6e20\u9053\u8bbe\u7f6e\u4fdd\u5b58")
    public AjaxResult<Object> setConfig(@Validated @RequestBody ChannelH5Validate channelH5Validate) {
        this.iChannelH5ConfigService.setConfig(channelH5Validate);
        return AjaxResult.success();
    }
}

