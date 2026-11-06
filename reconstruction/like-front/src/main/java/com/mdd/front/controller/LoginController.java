/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.enums.ClientEnum
 *  com.mdd.common.util.StringUtils
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.LoginController
 *  com.mdd.front.service.ILoginService
 *  com.mdd.front.service.IUserService
 *  com.mdd.front.validate.login.LoginCodeValidate
 *  com.mdd.front.validate.login.LoginPwdValidate
 *  com.mdd.front.validate.login.LoginScanValidate
 *  com.mdd.front.validate.login.RegisterValidate
 *  com.mdd.front.validate.users.UserBindWechatValidate
 *  com.mdd.front.vo.login.LoginTokenVo
 *  com.mdd.front.vo.login.LoginUrlsVo
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  javax.servlet.http.HttpSession
 *  javax.validation.constraints.NotNull
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.front.controller;

import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.enums.ClientEnum;
import com.mdd.common.util.StringUtils;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.ILoginService;
import com.mdd.front.service.IUserService;
import com.mdd.front.validate.login.LoginCodeValidate;
import com.mdd.front.validate.login.LoginPwdValidate;
import com.mdd.front.validate.login.LoginScanValidate;
import com.mdd.front.validate.login.RegisterValidate;
import com.mdd.front.validate.users.UserBindWechatValidate;
import com.mdd.front.vo.login.LoginTokenVo;
import com.mdd.front.vo.login.LoginUrlsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/login"})
@Api(tags={"\u767b\u5f55\u7ba1\u7406"})
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    @Resource
    ILoginService iLoginService;
    @Resource
    IUserService iUserService;

    @NotLogin
    @PostMapping(value={"/register"})
    @ApiOperation(value="\u6ce8\u518c\u8d26\u53f7")
    public AjaxResult<Object> register(@Validated @RequestBody RegisterValidate registerValidate) {
        // \u4f18\u5148\u4f7f\u7528\u8bf7\u6c42\u663e\u5f0f\u4f20\u5165\u7684 terminal\uff0c\u672a\u4f20\u65f6\u624d\u56de\u9000\u5230\u8bf7\u6c42\u5934/\u4f1a\u8bdd\u89e3\u6790\u51fa\u7684\u7ec8\u7aef\u3002
        Integer terminal = StringUtils.isNull(registerValidate.getTerminal())
                ? LikeFrontThreadLocal.getTerminal() : registerValidate.getTerminal();
        String account = registerValidate.getAccount();
        String password = registerValidate.getPassword();
        String userType = registerValidate.getUserType();
        this.iLoginService.register(account, password, userType, terminal);
        return AjaxResult.success();
    }

    @NotLogin
    @PostMapping(value={"/account"})
    @ApiOperation(value="\u5b66\u5458\u7aef-\u8d26\u53f7\u767b\u5f55\uff08\u5bc6\u7801/\u9a8c\u8bc1\u7801\uff09")
    public AjaxResult<LoginTokenVo> account(@Validated @RequestBody LoginPwdValidate loginPwdValidate) {
        if (StringUtils.isNull(loginPwdValidate.getTerminal())) {
            loginPwdValidate.setTerminal(LikeFrontThreadLocal.getTerminal());
        }
        LoginTokenVo vo = this.iLoginService.accountLogin(loginPwdValidate);
        return AjaxResult.success(vo);
    }

    @NotLogin
    @PostMapping(value={"/instructorPassword"})
    @ApiOperation(value="\u6559\u7ec3\u7aef-\u5bc6\u7801\u767b\u5f55")
    public AjaxResult<LoginTokenVo> instructorPassword(@Validated @RequestBody LoginPwdValidate loginPwdValidate) {
        if (StringUtils.isNull(loginPwdValidate.getTerminal())) {
            loginPwdValidate.setTerminal(LikeFrontThreadLocal.getTerminal());
        }
        LoginTokenVo vo = this.iLoginService.instructorPasswordLogin(loginPwdValidate);
        return AjaxResult.success(vo);
    }

    @NotLogin
    @PostMapping(value={"/instructorMobile"})
    @ApiOperation(value="\u6559\u7ec3\u7aef-\u9a8c\u8bc1\u7801\u767b\u5f55")
    public AjaxResult<LoginTokenVo> instructorMobile(@Validated @RequestBody LoginPwdValidate loginPwdValidate) {
        if (StringUtils.isNull(loginPwdValidate.getTerminal())) {
            loginPwdValidate.setTerminal(LikeFrontThreadLocal.getTerminal());
        }
        LoginTokenVo vo = this.iLoginService.instructorMobileLogin(loginPwdValidate);
        return AjaxResult.success(vo);
    }

    @NotLogin
    @PostMapping(value={"/mnpLogin"})
    @ApiOperation(value="\u5fae\u4fe1\u767b\u5f55")
    public AjaxResult<LoginTokenVo> mnpLogin(@Validated @RequestBody LoginCodeValidate loginCodeValidate) {
        Integer terminal = ClientEnum.MNP.getCode();
        String code = loginCodeValidate.getCode();
        LoginTokenVo vo = this.iLoginService.mnpLogin(code, terminal);
        return AjaxResult.success(vo);
    }

    @NotLogin
    @PostMapping(value={"/instructorMnpLogin"})
    @ApiOperation(value="\u6559\u7ec3\u7aef\u5fae\u4fe1\u767b\u5f55")
    public AjaxResult<LoginTokenVo> instructorMnpLogin(@Validated @RequestBody LoginCodeValidate loginCodeValidate) {
        Integer terminal = ClientEnum.MNP.getCode();
        String code = loginCodeValidate.getCode();
        LoginTokenVo vo = this.iLoginService.instructorMnpLogin(code, terminal);
        return AjaxResult.success(vo);
    }

    @NotLogin
    @PostMapping(value={"/oaLogin"})
    @ApiOperation(value="\u516c\u4f17\u53f7\u767b\u5f55")
    public AjaxResult<LoginTokenVo> oaLogin(@Validated @RequestBody LoginCodeValidate loginCodeValidate) {
        Integer terminal = ClientEnum.OA.getCode();
        String code = loginCodeValidate.getCode();
        LoginTokenVo vo = this.iLoginService.officeLogin(code, terminal);
        return AjaxResult.success(vo);
    }

    @NotLogin
    @GetMapping(value={"/codeUrl"})
    @ApiOperation(value="\u516c\u4f17\u53f7\u94fe\u63a5")
    public AjaxResult<LoginUrlsVo> codeUrl(@Validated @NotNull @RequestParam(value="url") String url) {
        LoginUrlsVo vo = new LoginUrlsVo();
        vo.setUrl(this.iLoginService.oaCodeUrl(url));
        return AjaxResult.success(vo);
    }

    @NotLogin
    @GetMapping(value={"/getScanCode"})
    @ApiOperation(value="PC\u626b\u7801\u94fe\u63a5")
    public AjaxResult<LoginUrlsVo> scanCodeUrl(@Validated @NotNull @RequestParam(value="url") String url, HttpSession session) {
        String qrcodeUrl = this.iLoginService.scanCodeUrl(url, session);
        LoginUrlsVo vo = new LoginUrlsVo();
        vo.setUrl(qrcodeUrl);
        return AjaxResult.success(vo);
    }

    @NotLogin
    @PostMapping(value={"/scanLogin"})
    @ApiOperation(value="PC\u626b\u7801\u767b\u5f55")
    public AjaxResult<Object> scanLogin(@Validated @RequestBody LoginScanValidate loginScanValidate, HttpSession session) {
        Integer terminal = LikeFrontThreadLocal.getTerminal();
        String code = loginScanValidate.getCode();
        String state = loginScanValidate.getState();
        LoginTokenVo vo = this.iLoginService.scanLogin(code, state, terminal, session);
        return AjaxResult.success(vo);
    }

    @NotLogin
    @PostMapping(value={"/logout"})
    @ApiOperation(value="\u9000\u51fa\u767b\u5f55")
    public AjaxResult<Object> logout() {
        this.iLoginService.logout();
        return AjaxResult.success();
    }

    @PostMapping(value={"/oaAuthBind"})
    @ApiOperation(value="\u7ed1\u5b9a\u5fae\u4fe1\u516c\u4f17\u53f7")
    public AjaxResult<Object> oaAuthBind(@Validated @RequestBody UserBindWechatValidate BindOaValidate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.iUserService.bindOa(BindOaValidate, userId);
        return AjaxResult.success();
    }

    @PostMapping(value={"/mnpAuthBind"})
    @ApiOperation(value="\u7ed1\u5b9a\u5c0f\u7a0b\u5e8f")
    public AjaxResult<Object> mnpAuthBind(@Validated @RequestBody UserBindWechatValidate BindOaValidate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.iUserService.bindMnp(BindOaValidate, userId);
        return AjaxResult.success();
    }
}

