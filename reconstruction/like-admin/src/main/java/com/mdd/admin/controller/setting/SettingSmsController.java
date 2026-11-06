/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.setting.SettingSmsController
 *  com.mdd.admin.service.ISettingSmsService
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
package com.mdd.admin.controller.setting;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.ISettingSmsService;
import com.mdd.common.core.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/notice.sms_config"})
@Api(tags={"\u914d\u7f6e\u77ed\u4fe1\u5f15\u64ce"})
public class SettingSmsController {
    @Resource
    ISettingSmsService iSettingSmsService;

    @GetMapping(value={"/getConfig"})
    @ApiOperation(value="\u77ed\u4fe1\u5f15\u64ce\u5217\u8868")
    public AjaxResult<List<Map<String, Object>>> getConfig() {
        List list = this.iSettingSmsService.getConfig();
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u77ed\u4fe1\u5f15\u64ce\u8be6\u60c5")
    public AjaxResult<Map<String, Object>> detail(String type) {
        Map map = this.iSettingSmsService.detail(type);
        return AjaxResult.success(map);
    }

    @Log(title="\u77ed\u4fe1\u5f15\u64ce\u7f16\u8f91")
    @PostMapping(value={"/setConfig"})
    @ApiOperation(value="\u77ed\u4fe1\u5f15\u64ce\u7f16\u8f91")
    public AjaxResult<Object> setConfig(@RequestBody JSONObject params) {
        this.iSettingSmsService.setConfig(params);
        return AjaxResult.success((String)"\u64cd\u4f5c\u6210\u529f");
    }
}

