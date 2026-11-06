/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.setting.SettingUserController
 *  com.mdd.admin.service.ISettingUserService
 *  com.mdd.admin.validate.setting.SettingUserValidate
 *  com.mdd.admin.vo.setting.SettingUserVo
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
import com.mdd.admin.service.ISettingUserService;
import com.mdd.admin.validate.setting.SettingUserValidate;
import com.mdd.admin.vo.setting.SettingUserVo;
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
@Api(tags={"\u914d\u7f6e\u7528\u6237\u53c2\u6570"})
public class SettingUserController {
    @Resource
    ISettingUserService iSettingUserService;

    @GetMapping(value={"/getConfig"})
    @ApiOperation(value="\u7528\u6237\u8bbe\u7f6e\u8be6\u60c5")
    public AjaxResult<SettingUserVo> detail() {
        SettingUserVo vo = this.iSettingUserService.getConfig();
        return AjaxResult.success(vo);
    }

    @Log(title="\u7528\u6237\u8bbe\u7f6e\u7f16\u8f91")
    @PostMapping(value={"/setConfig"})
    @ApiOperation(value="\u7528\u6237\u8bbe\u7f6e\u7f16\u8f91")
    public AjaxResult<Object> setConfig(@Validated @RequestBody SettingUserValidate userValidate) {
        this.iSettingUserService.setConfig(userValidate);
        return AjaxResult.success();
    }
}

