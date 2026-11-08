/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.controller.finance.FinanceRefundController
 *  com.mdd.admin.service.IFinanceRefundService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.finance.FinanceRefundSearchValidate
 *  com.mdd.common.aop.NotPower
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.validator.annotation.IDMust
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.finance;

import cn.dev33.satoken.stp.StpUtil;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.service.IFinanceRefundService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.finance.FinanceRefundSearchValidate;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.LoginException;
import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/finance.refund"})
@Api(value="\u9000\u6b3e\u8bb0\u5f55\u7ba1\u7406")
public class FinanceRefundController {
    @Resource
    IFinanceRefundService iFinanceRefundService;

    /**
     * 退款统计（#58）：报表口径来自 la_refund_record 汇总，属财务数据。
     * 路由 finance.refund/stat 没有独立菜单项、无法单独授权，因此保留 @NotPower 绕过
     * 路由级校验，改为要求「退款记录」按钮权限 finance.refund/record（页面入口权限）；
     * 超级管理员（admin id=1）放行，与 LikeAdminInterceptor 的约定一致。
     */
    @NotPower
    @GetMapping(value={"/stat"})
    @ApiOperation(value="\u9000\u8fd8\u7edf\u8ba1")
    public AjaxResult<Object> stat() {
        this.checkRecordPermission();
        return AjaxResult.success(this.iFinanceRefundService.stat());
    }

    private void checkRecordPermission() {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        if (adminId != null && adminId == 1) {
            return;
        }
        boolean allowed;
        try {
            allowed = StpUtil.hasPermission("finance.refund/record");
        } catch (Exception e) {
            allowed = false;
        }
        if (!allowed) {
            throw new LoginException(ErrorEnum.NO_PERMISSION.getCode(), ErrorEnum.NO_PERMISSION.getMsg());
        }
    }

    @GetMapping(value={"/record"})
    @ApiOperation(value="\u9000\u6b3e\u8bb0\u5f55")
    public AjaxResult<Object> list(@Validated PageValidate pageValidate, @Validated FinanceRefundSearchValidate searchValidate) {
        PageResult list = this.iFinanceRefundService.list(pageValidate, searchValidate);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/log"})
    @ApiOperation(value="\u9000\u6b3e\u65e5\u5fd7")
    public AjaxResult<Object> log(@Validated @IDMust @RequestParam(value="record_id") Integer id) {
        List list = this.iFinanceRefundService.log(id);
        return AjaxResult.success(list);
    }
}

