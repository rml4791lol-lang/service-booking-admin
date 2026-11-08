/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.setting.SettingLoginController
 *  com.mdd.admin.service.ISettingLoginService
 *  com.mdd.admin.validate.setting.SettingLoginValidate
 *  com.mdd.admin.vo.setting.SettingLoginVo
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
import com.mdd.admin.service.ISettingLoginService;
import com.mdd.admin.validate.setting.SettingLoginValidate;
import com.mdd.admin.vo.setting.SettingLoginVo;
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
@RequestMapping(value={"/adminapi/setting.user.user"})
@Api(tags={"\u914d\u7f6e\u7528\u6237\u767b\u5f55"})
public class SettingLoginController {
    @Resource
    ISettingLoginService iSettingLoginService;

    @GetMapping(value={"/getRegisterConfig"})
    @ApiOperation(value="\u767b\u5f55\u8bbe\u7f6e\u8be6\u60c5")
    public AjaxResult<SettingLoginVo> getRegisterConfig() {
        SettingLoginVo vo = this.iSettingLoginService.getRegisterConfig();
        return AjaxResult.success(vo);
    }

    @Log(title="\u767b\u5f55\u8bbe\u7f6e\u7f16\u8f91")
    @PostMapping(value={"/setRegisterConfig"})
    @ApiOperation(value="\u767b\u5f55\u8bbe\u7f6e\u7f16\u8f91")
    public AjaxResult<Object> setRegisterConfig(@Validated @RequestBody SettingLoginValidate loginValidate) {
        this.iSettingLoginService.setRegisterConfig(loginValidate);
        return AjaxResult.success();
    }
}

