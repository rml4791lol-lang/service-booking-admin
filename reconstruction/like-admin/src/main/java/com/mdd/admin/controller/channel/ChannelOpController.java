/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.controller.channel.ChannelOpController
 *  com.mdd.admin.service.IChannelOpService
 *  com.mdd.admin.validate.channel.ChannelOpValidate
 *  com.mdd.admin.vo.channel.ChannelOpVo
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

import com.mdd.admin.service.IChannelOpService;
import com.mdd.admin.validate.channel.ChannelOpValidate;
import com.mdd.admin.vo.channel.ChannelOpVo;
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
@RequestMapping(value={"/adminapi/channel.open_setting/"})
@Api(tags={"\u5fae\u4fe1\u5f00\u653e\u6e20\u9053"})
public class ChannelOpController {
    @Resource
    IChannelOpService iChannelOpService;

    @GetMapping(value={"/getConfig"})
    @ApiOperation(value="\u5f00\u653e\u5e73\u53f0\u8bbe\u7f6e\u8be6\u60c5")
    public AjaxResult<Object> getConfig() {
        ChannelOpVo vo = this.iChannelOpService.getConfig();
        return AjaxResult.success(vo);
    }

    @PostMapping(value={"/setConfig"})
    @ApiOperation(value="\u5f00\u653e\u5e73\u53f0\u8bbe\u7f6e\u4fdd\u5b58")
    public AjaxResult<Object> save(@Validated @RequestBody ChannelOpValidate opValidate) {
        this.iChannelOpService.setConfig(opValidate);
        return AjaxResult.success();
    }
}

