package com.mdd.front.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.util.IpUtils;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IInstructorAccountService;
import com.mdd.front.validate.instructor.InstructorPasswordChangeValidate;
import com.mdd.front.vo.login.LoginTokenVo;
import com.mdd.front.vo.user.InstructorAccountInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 教练端账号自助（#31）。
 *
 * <p>首次登录拿到 mustChangePassword=true 的 token 后，拦截器只放行
 * /api/instructor/account/info 与 /api/instructor/account/changePassword。</p>
 */
@RestController
@RequestMapping(value = {"/api/instructor/account"})
@Api(tags = {"教练账号自助"})
public class InstructorAccountController {
    @Resource
    IInstructorAccountService instructorAccountService;

    @GetMapping({"/info"})
    @ApiOperation("教练账号信息")
    public AjaxResult<InstructorAccountInfoVo> info() {
        return AjaxResult.success(instructorAccountService.info(LikeFrontThreadLocal.getUserId()));
    }

    @PostMapping({"/changePassword"})
    @ApiOperation("教练修改登录密码")
    public AjaxResult<LoginTokenVo> changePassword(
            @Validated @RequestBody InstructorPasswordChangeValidate validate) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        LoginTokenVo tokenVo = instructorAccountService.changePassword(instructorId, validate, IpUtils.getIpAddress());
        return AjaxResult.success(tokenVo);
    }
}
