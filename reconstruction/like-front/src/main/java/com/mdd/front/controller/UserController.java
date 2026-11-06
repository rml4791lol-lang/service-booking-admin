/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.aop.NotLogin
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.exception.OperateException
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.UserController
 *  com.mdd.front.service.IUserService
 *  com.mdd.front.validate.users.NewUserUpdateValidate
 *  com.mdd.front.validate.users.ResetPasswordValidate
 *  com.mdd.front.validate.users.UserBindWechatValidate
 *  com.mdd.front.validate.users.UserChangePwdValidate
 *  com.mdd.front.validate.users.UserForgetPwdValidate
 *  com.mdd.front.validate.users.UserPhoneBindValidate
 *  com.mdd.front.validate.users.UserPhoneMnpValidate
 *  com.mdd.front.validate.users.UserUpdateValidate
 *  com.mdd.front.vo.user.UserCenterVo
 *  com.mdd.front.vo.user.UserInfoVo
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
package com.mdd.front.controller;

import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.exception.OperateException;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IUserService;
import com.mdd.front.validate.users.NewUserUpdateValidate;
import com.mdd.front.validate.users.ResetPasswordValidate;
import com.mdd.front.validate.users.UserBindWechatValidate;
import com.mdd.front.validate.users.UserChangePwdValidate;
import com.mdd.front.validate.users.UserForgetPwdValidate;
import com.mdd.front.validate.users.UserPhoneBindValidate;
import com.mdd.front.validate.users.UserPhoneMnpValidate;
import com.mdd.front.validate.users.UserProfileValidate;
import com.mdd.front.validate.users.UserUpdateValidate;
import com.mdd.front.vo.user.UserCenterVo;
import com.mdd.front.vo.user.UserInfoVo;
import com.mdd.front.vo.user.UserProfileVo;
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
@RequestMapping(value={"api/user"})
@Api(tags={"\u7528\u6237\u7ba1\u7406"})
public class UserController {
    @Resource
    IUserService iUserService;

    @GetMapping(value={"/center"})
    @ApiOperation(value="\u4e2a\u4eba\u4e2d\u5fc3")
    public AjaxResult<UserCenterVo> center() {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Integer terminal = LikeFrontThreadLocal.getTerminal();
        UserCenterVo vo = this.iUserService.center(userId, terminal);
        return AjaxResult.success(vo);
    }

    @GetMapping(value={"/info"})
    @ApiOperation(value="\u4e2a\u4eba\u4fe1\u606f")
    public AjaxResult<UserInfoVo> info() {
        Integer userId = LikeFrontThreadLocal.getUserId();
        UserInfoVo vo = this.iUserService.info(userId);
        return AjaxResult.success(vo);
    }

    @GetMapping(value={"/profile"})
    @ApiOperation(value="\u5bb6\u957f\u8d44\u6599")
    public AjaxResult<UserProfileVo> profile() {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(this.iUserService.profile(userId));
    }

    @PostMapping(value={"/profile"})
    @ApiOperation(value="\u4fdd\u5b58\u5bb6\u957f\u8d44\u6599")
    public AjaxResult<UserProfileVo> saveProfile(@Validated @RequestBody UserProfileValidate profileValidate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(this.iUserService.saveProfile(profileValidate, userId));
    }

    @PostMapping(value={"/setInfo"})
    @ApiOperation(value="\u7f16\u8f91\u4fe1\u606f")
    public AjaxResult<Object> setInfo(@Validated @RequestBody UserUpdateValidate updateValidate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.iUserService.setInfo(updateValidate, userId);
        return AjaxResult.success();
    }

    @PostMapping(value={"/changePassword"})
    @ApiOperation(value="\u4fee\u6539\u5bc6\u7801")
    public AjaxResult<Object> changePassword(@Validated @RequestBody UserChangePwdValidate passwordValidate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        if (!passwordValidate.getPassword().equals(passwordValidate.getPasswordConfirm())) {
            throw new OperateException("\u4e24\u6b21\u8f93\u5165\u7684\u5bc6\u7801\u4e0d\u4e00\u81f4");
        }
        this.iUserService.changePwd(passwordValidate.getPassword(), passwordValidate.getOldPassword(), userId);
        return AjaxResult.success();
    }

    @NotLogin
    @PostMapping(value={"/resetPassword"})
    @ApiOperation(value="\u91cd\u7f6e\u5bc6\u7801")
    public AjaxResult<Object> resetPassword(@Validated @RequestBody ResetPasswordValidate passwordValidate) {
        this.iUserService.resetPassword(passwordValidate);
        return AjaxResult.success();
    }

    @NotLogin
    @PostMapping(value={"/forgotPwd"})
    @ApiOperation(value="\u5fd8\u8bb0\u5bc6\u7801")
    public AjaxResult<Object> forgotPwd(@Validated @RequestBody UserForgetPwdValidate userForgetPwdValidate) {
        String password = userForgetPwdValidate.getPassword();
        String mobile = userForgetPwdValidate.getMobile();
        String code = userForgetPwdValidate.getCode();
        this.iUserService.forgotPwd(password, mobile, code);
        return AjaxResult.success();
    }

    @PostMapping(value={"/bindMobile"})
    @ApiOperation(value="\u7ed1\u5b9a\u624b\u673a")
    public AjaxResult<Object> bindMobile(@Validated @RequestBody UserPhoneBindValidate mobileValidate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.iUserService.bindMobile(mobileValidate, userId);
        return AjaxResult.success();
    }

    @PostMapping(value={"/getMobileByMnp"})
    @ApiOperation(value="\u5fae\u4fe1\u624b\u673a\u53f7")
    public AjaxResult<Object> mnpMobile(@Validated @RequestBody UserPhoneMnpValidate mobileValidate) {
        this.iUserService.mnpMobile(mobileValidate.getCode().trim());
        return AjaxResult.success();
    }

    @PostMapping(value={"/updateUser"})
    @ApiOperation(value="\u66f4\u65b0\u65b0\u7528\u6237\u4fe1\u606f")
    public AjaxResult<Object> updateData(@Validated @RequestBody NewUserUpdateValidate newUserUpdateValidate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.iUserService.updateNewUserInfo(newUserUpdateValidate, userId);
        return AjaxResult.success();
    }

    @PostMapping(value={"/bindMnp"})
    @ApiOperation(value="\u7ed1\u5b9a\u5c0f\u7a0b\u5e8f")
    public AjaxResult<Object> bindMnp(@Validated @RequestBody UserBindWechatValidate BindMnpValidate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.iUserService.bindMnp(BindMnpValidate, userId);
        return AjaxResult.success();
    }

    @PostMapping(value={"/bindOa"})
    @ApiOperation(value="\u7ed1\u5b9a\u5fae\u4fe1\u516c\u4f17\u53f7")
    public AjaxResult<Object> bindOa(@Validated @RequestBody UserBindWechatValidate BindOaValidate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.iUserService.bindOa(BindOaValidate, userId);
        return AjaxResult.success();
    }

    /**
     * 注销账号：body 传 {"confirm":true}（可选 reason 记录注销原因）。
     * 成功 {code:1}；存在在途资产时 {code:0,msg:原因}，账号不做任何变更。
     */
    @PostMapping(value={"/cancelAccount"})
    @ApiOperation(value="\u6ce8\u9500\u8d26\u53f7")
    public AjaxResult<Object> cancelAccount(@RequestBody(required=false) java.util.Map<String, Object> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.iUserService.cancelAccount(userId, params);
        return AjaxResult.success();
    }
}

