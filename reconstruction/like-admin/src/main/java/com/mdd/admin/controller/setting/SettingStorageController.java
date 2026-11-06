/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.setting.SettingStorageController
 *  com.mdd.admin.service.ISettingStorageService
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

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.ISettingStorageService;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.util.ParamUtils;
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
@RequestMapping(value={"/adminapi/setting.storage"})
@Api(tags={"\u914d\u7f6e\u5b58\u50a8\u65b9\u5f0f"})
public class SettingStorageController {
    @Resource
    ISettingStorageService iSettingStorageService;

    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u5b58\u50a8\u5217\u8868")
    public AjaxResult<List<Map<String, Object>>> list() {
        List list = this.iSettingStorageService.list();
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u5b58\u50a8\u8be6\u60c5")
    public AjaxResult<Map<String, Object>> detail(String engine) {
        Map map = this.iSettingStorageService.detail(engine);
        return AjaxResult.success(map);
    }

    @Log(title="\u5b58\u50a8\u7f16\u8f91")
    @PostMapping(value={"/setup"})
    @ApiOperation(value="\u5b58\u50a8\u7f16\u8f91")
    public AjaxResult<Object> setup(@RequestBody Map<String, String> params) {
        this.iSettingStorageService.setup(params);
        return AjaxResult.success();
    }

    @Log(title="\u5b58\u50a8\u5207\u6362")
    @PostMapping(value={"/change"})
    @ApiOperation(value="\u5b58\u50a8\u5207\u6362")
    public AjaxResult<Object> change(@RequestBody Map<String, String> params) {
        Assert.notNull((Object)params.get("alias"), (String)"alias\u53c2\u6570\u7f3a\u5931", (Object[])new Object[0]);
        Assert.notNull((Object)params.get("status"), (String)"status\u53c2\u6570\u7f3a\u5931", (Object[])new Object[0]);
        String alias = params.get("alias");
        Integer status = ParamUtils.requiredInt(params.get("status"), "状态");
        this.iSettingStorageService.change(alias, status);
        return AjaxResult.success();
    }
}

