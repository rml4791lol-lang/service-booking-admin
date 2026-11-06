/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.RechargeController
 *  com.mdd.front.service.IRechargeService
 *  com.mdd.front.validate.RechargeValidate
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.vo.RechargeConfigVo
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

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.IRechargeService;
import com.mdd.front.validate.RechargeValidate;
import com.mdd.front.validate.common.PageValidate;
import com.mdd.front.vo.RechargeConfigVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/recharge"})
@Api(tags={"\u5145\u503c\u7ba1\u7406"})
public class RechargeController {
    @Resource
    IRechargeService iRechargeService;

    @GetMapping(value={"/config"})
    @ApiOperation(value="\u5145\u503c\u914d\u7f6e")
    public AjaxResult<Object> config() {
        Integer userId = LikeFrontThreadLocal.getUserId();
        RechargeConfigVo vo = this.iRechargeService.config(userId);
        return AjaxResult.success(vo);
    }

    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u7f34\u8d39\u8bb0\u5f55")
    public AjaxResult<Object> lists(@Validated PageValidate pageValidate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        PageResult list = this.iRechargeService.record(userId, pageValidate);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/orderList"})
    @ApiOperation(value="\u6211\u7684\u8ba2\u5355\uff08\u5206\u9875+\u5238/\u6d3b\u52a8\u9000\u6b3e\u72b6\u6001\uff09")
    public AjaxResult<Object> orderList(@Validated PageValidate pageValidate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        PageResult list = this.iRechargeService.orderList(userId, pageValidate);
        return AjaxResult.success(list);
    }

    @PostMapping(value={"/recharge"})
    @ApiOperation(value="\u5145\u503c\u4e0b\u5355")
    public AjaxResult<Object> recharge(@Validated @RequestBody RechargeValidate rechargeValidate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        Integer terminal = LikeFrontThreadLocal.getTerminal();
        Map result = this.iRechargeService.placeOrder(userId, terminal, rechargeValidate);
        return AjaxResult.success(result);
    }
}

