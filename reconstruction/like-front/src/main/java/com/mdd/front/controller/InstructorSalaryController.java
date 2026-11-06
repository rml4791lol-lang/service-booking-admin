/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.InstructorSalaryController
 *  com.mdd.front.service.IInstructorSalaryService
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.front.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IInstructorSalaryService;
import com.mdd.common.util.ParamUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.math.BigDecimal;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/instructor/salary"})
@Api(tags={"\u6559\u7ec3\u85aa\u8d44\u7ed3\u7b97"})
public class InstructorSalaryController {
    @Resource
    IInstructorSalaryService iInstructorSalaryService;

    @GetMapping(value={"/class_income"})
    @ApiOperation(value="\u73ed\u7ea7\u6536\u5165\u660e\u7ec6(\u9488\u5bf9\u85aa\u8d44\u7ed3\u7b97\u6a21\u5757)")
    public AjaxResult<Object> classIncome(@RequestParam(required=false) String month) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        if (instructorId == null || instructorId <= 0) {
            return AjaxResult.failed((String)"\u8bf7\u5148\u767b\u5f55\u6559\u7ec3\u8d26\u53f7");
        }
        Map data = this.iInstructorSalaryService.classIncomeDetail(instructorId, month);
        return AjaxResult.success(data);
    }

    @GetMapping(value={"/income_summary"})
    @ApiOperation(value="\u672c\u6708\u6536\u5165\u603b\u6c47")
    public AjaxResult<Object> incomeSummary(@RequestParam(required=false) String month) {
        Integer instructorId = LikeFrontThreadLocal.getUserId();
        if (instructorId == null || instructorId <= 0) {
            return AjaxResult.failed((String)"\u8bf7\u5148\u767b\u5f55\u6559\u7ec3\u8d26\u53f7");
        }
        return AjaxResult.success(this.iInstructorSalaryService.incomeSummary(instructorId, month));
    }

    @GetMapping(value={"/overview"})
    @ApiOperation(value="\u85aa\u8d44\u603b\u89c8")
    public AjaxResult<Object> overview() {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(this.iInstructorSalaryService.overview(userId));
    }

    @GetMapping(value={"/history"})
    @ApiOperation(value="\u6536\u5165\u660e\u7ec6\u6d41\u6c34")
    public AjaxResult<Object> history(@RequestParam(value="page_no", defaultValue="1") Integer pageNo, @RequestParam(value="page_size", defaultValue="20") Integer pageSize, @RequestParam(value="type", required=false) Integer type) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(this.iInstructorSalaryService.history(userId, pageNo, pageSize, type));
    }

    @GetMapping(value={"/bank"})
    @ApiOperation(value="\u67e5\u8be2\u94f6\u884c\u5361")
    public AjaxResult<Object> getBankInfo() {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(this.iInstructorSalaryService.getBankInfo(userId));
    }

    @PostMapping(value={"/bank"})
    @ApiOperation(value="\u4fdd\u5b58\u94f6\u884c\u5361")
    public AjaxResult<Object> saveBankInfo(@RequestBody Map<String, String> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        this.iInstructorSalaryService.saveBankInfo(userId, params);
        return AjaxResult.success();
    }

    @PostMapping(value={"/applyWithdrawal"})
    @ApiOperation(value="\u7533\u8bf7\u63d0\u73b0")
    public AjaxResult<Object> applyWithdrawal(@RequestBody Map<String, Object> params) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        BigDecimal amount = ParamUtils.requiredDecimal(params.get("amount"), "\u63d0\u73b0\u91d1\u989d");
        this.iInstructorSalaryService.applyWithdrawal(userId, amount);
        return AjaxResult.success();
    }

    @GetMapping(value={"/withdrawals"})
    @ApiOperation(value="\u63d0\u73b0\u8bb0\u5f55\u5217\u8868")
    public AjaxResult<Object> withdrawals(@RequestParam(value="page_no", defaultValue="1") Integer pageNo, @RequestParam(value="page_size", defaultValue="20") Integer pageSize) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        return AjaxResult.success(this.iInstructorSalaryService.withdrawalList(userId, pageNo, pageSize));
    }
}

