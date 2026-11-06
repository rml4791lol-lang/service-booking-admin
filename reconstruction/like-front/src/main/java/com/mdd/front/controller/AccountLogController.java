/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.AccountLogController
 *  com.mdd.front.service.IUserAccountLogService
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.validate.users.UserAccountLogSearchValidate
 *  com.mdd.front.vo.user.UserAccountListVo
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.front.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IUserAccountLogService;
import com.mdd.front.validate.common.PageValidate;
import com.mdd.front.validate.users.UserAccountLogSearchValidate;
import com.mdd.front.vo.user.UserAccountListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/account_log"})
@Api(tags={"\u7528\u6237\u8d44\u91d1\u53d8\u66f4\u7ba1\u7406"})
public class AccountLogController {
    @Resource
    IUserAccountLogService iUserAccountLogService;

    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u7528\u6237\u8d44\u91d1\u53d8\u66f4\u5217\u8868")
    public AjaxResult<PageResult<UserAccountListVo>> lists(@Validated PageValidate pageValidate, @Validated UserAccountLogSearchValidate searchValidate) {
        searchValidate.setUserId(LikeFrontThreadLocal.getUserId());
        PageResult list = this.iUserAccountLogService.lists(pageValidate, searchValidate);
        return AjaxResult.success(list);
    }
}

