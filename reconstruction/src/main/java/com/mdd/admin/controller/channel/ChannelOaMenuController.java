/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONArray
 *  com.mdd.admin.controller.channel.ChannelOaMenuController
 *  com.mdd.admin.service.IChannelOaMenusService
 *  com.mdd.common.core.AjaxResult
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.channel;

import com.alibaba.fastjson2.JSONArray;
import com.mdd.admin.service.IChannelOaMenusService;
import com.mdd.common.core.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/channel.official_account_menu"})
@Api(tags={"\u516c\u4f17\u53f7\u83dc\u5355\u7ba1\u7406"})
public class ChannelOaMenuController {
    @Resource
    IChannelOaMenusService iChannelOaMenusService;

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u83dc\u5355\u8be6\u60c5")
    public AjaxResult<JSONArray> detail() {
        JSONArray detail = this.iChannelOaMenusService.detail();
        return AjaxResult.success(detail);
    }

    @PostMapping(value={"/save"})
    @ApiOperation(value="\u4ec5\u662f\u4fdd\u5b58\u83dc\u5355")
    public AjaxResult<Object> save(@RequestBody List<Object> params) {
        this.iChannelOaMenusService.save(params, Boolean.valueOf(false));
        return AjaxResult.success();
    }

    @PostMapping(value={"/saveAndPublish"})
    @ApiOperation(value="saveAndPublish")
    public AjaxResult<Object> publish(@RequestBody List<Object> params) {
        this.iChannelOaMenusService.save(params, Boolean.valueOf(true));
        return AjaxResult.success();
    }
}

