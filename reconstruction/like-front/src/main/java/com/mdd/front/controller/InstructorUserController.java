/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.front.controller.InstructorUserController
 *  com.mdd.front.service.IInstructorService
 *  com.mdd.front.validate.users.NewUserUpdateValidate
 *  com.mdd.front.validate.users.UserPhoneMnpValidate
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  javax.servlet.http.HttpServletRequest
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.front.controller;

import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.front.service.IInstructorService;
import com.mdd.front.validate.users.NewUserUpdateValidate;
import com.mdd.front.validate.users.UserPhoneMnpValidate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"api/instructorUser"})
@Api(tags={"\u6559\u7ec3\u7528\u6237\u7ba1\u7406"})
public class InstructorUserController {
    @Resource
    IInstructorService iInstructorService;

    @NotLogin
    @PostMapping(value={"/getMobileByMnp"})
    @ApiOperation(value="\u83b7\u53d6\u5fae\u4fe1\u624b\u673a\u53f7\u5e76\u7ed1\u5b9a\u6559\u7ec3")
    public AjaxResult<Object> mnpMobile(@Validated @RequestBody UserPhoneMnpValidate mobileValidate, HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            return AjaxResult.failed((String)"\u7f3a\u5c11token\u53c2\u6570");
        }
        this.iInstructorService.mnpMobile(mobileValidate.getCode().trim(), token);
        return AjaxResult.success();
    }

    @NotLogin
    @PostMapping(value={"/updateUser"})
    @ApiOperation(value="\u66f4\u65b0\u6559\u7ec3\u5fae\u4fe1\u4fe1\u606f")
    public AjaxResult<Object> updateData(@Validated @RequestBody NewUserUpdateValidate newUserUpdateValidate, HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            return AjaxResult.failed((String)"\u7f3a\u5c11token\u53c2\u6570");
        }
        this.iInstructorService.updateInstructorInfo(newUserUpdateValidate, token);
        return AjaxResult.success();
    }
}

