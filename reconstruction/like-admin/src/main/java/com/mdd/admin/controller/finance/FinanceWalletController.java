/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.controller.finance.FinanceWalletController
 *  com.mdd.admin.service.IFinanceWalletService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.finance.FinanceWalletSearchValidate
 *  com.mdd.common.aop.NotPower
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.finance;

import com.mdd.admin.service.IFinanceWalletService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.finance.FinanceWalletSearchValidate;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/finance.account_log"})
@Api(value="\u4f59\u989d\u660e\u7ec6\u7ba1\u7406")
public class FinanceWalletController {
    @Resource
    IFinanceWalletService iFinanceWalletService;

    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u8bb0\u5f55\u5217\u8868")
    public AjaxResult<Object> list(@Validated PageValidate pageValidate, @Validated FinanceWalletSearchValidate searchValidate) {
        PageResult list = this.iFinanceWalletService.list(pageValidate, searchValidate);
        return AjaxResult.success(list);
    }

    @NotPower
    @GetMapping(value={"/getUmChangeType"})
    @ApiOperation(value="\u8bb0\u5f55\u5217\u8868")
    public AjaxResult<Map<Integer, String>> getUmChangeType() {
        return AjaxResult.success(this.iFinanceWalletService.getUmChangeType());
    }
}

