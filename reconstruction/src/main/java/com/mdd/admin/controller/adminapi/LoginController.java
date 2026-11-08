/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.controller.adminapi.LoginController
 *  com.mdd.admin.service.system.ISystemLoginService
 *  com.mdd.admin.validate.system.SystemAdminLoginsValidate
 *  com.mdd.admin.vo.system.SystemCaptchaVo
 *  com.mdd.admin.vo.system.SystemLoginVo
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.aop.NotPower
 *  com.mdd.common.core.AjaxResult
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  javax.servlet.http.HttpServletRequest
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.adminapi;

import com.mdd.admin.service.system.ISystemLoginService;
import com.mdd.admin.validate.system.SystemAdminLoginsValidate;
import com.mdd.admin.vo.system.SystemCaptchaVo;
import com.mdd.admin.vo.system.SystemLoginVo;
import com.mdd.common.aop.NotLogin;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"adminapi/login"})
@Api(tags={"\u7cfb\u7edf\u767b\u5f55\u7ba1\u7406"})
public class LoginController {
    @Resource
    ISystemLoginService iSystemLoginService;

    @NotLogin
    @GetMapping(value={"/captcha"})
    @ApiOperation(value="\u53d6\u9a8c\u8bc1\u7801")
    public AjaxResult<SystemCaptchaVo> captcha() {
        SystemCaptchaVo vo = this.iSystemLoginService.captcha();
        return AjaxResult.success(vo);
    }

    @NotLogin
    @PostMapping(value={"/account"})
    @ApiOperation(value="\u767b\u5f55\u7cfb\u7edf")
    public AjaxResult<SystemLoginVo> account(@Validated @RequestBody SystemAdminLoginsValidate loginsValidate) {
        SystemLoginVo vo = this.iSystemLoginService.login(loginsValidate);
        return AjaxResult.success(vo);
    }

    @NotPower
    @PostMapping(value={"/logout"})
    @ApiOperation(value="\u9000\u51fa\u767b\u5f55")
    public AjaxResult<Object> logout(HttpServletRequest request) {
        this.iSystemLoginService.logout(request.getHeader("token"));
        return AjaxResult.success();
    }
}

