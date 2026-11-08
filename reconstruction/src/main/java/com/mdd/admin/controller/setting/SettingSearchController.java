/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.setting.SettingSearchController
 *  com.mdd.admin.service.ISettingSearchService
 *  com.mdd.admin.validate.setting.SettingSearchValidate
 *  com.mdd.admin.vo.setting.SettingSearchDetailVo
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
package com.mdd.admin.controller.setting;

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.ISettingSearchService;
import com.mdd.admin.validate.setting.SettingSearchValidate;
import com.mdd.admin.vo.setting.SettingSearchDetailVo;
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
@RequestMapping(value={"/adminapi/setting.hot_search"})
@Api(tags={"\u914d\u7f6e\u70ed\u95e8\u641c\u7d22"})
public class SettingSearchController {
    @Resource
    ISettingSearchService iSettingSearchService;

    @GetMapping(value={"/getConfig"})
    @ApiOperation(value="\u70ed\u95e8\u641c\u7d22\u8be6\u60c5")
    public AjaxResult<SettingSearchDetailVo> getConfig() {
        SettingSearchDetailVo vo = this.iSettingSearchService.getConfig();
        return AjaxResult.success(vo);
    }

    @Log(title="\u70ed\u95e8\u641c\u7d22\u7f16\u8f91")
    @PostMapping(value={"/setConfig"})
    @ApiOperation(value="\u70ed\u95e8\u641c\u7d22\u7f16\u8f91")
    public AjaxResult<Object> setConfig(@Validated @RequestBody SettingSearchValidate searchValidate) {
        this.iSettingSearchService.setConfig(searchValidate);
        return AjaxResult.success((String)"\u64cd\u4f5c\u6210\u529f");
    }
}

