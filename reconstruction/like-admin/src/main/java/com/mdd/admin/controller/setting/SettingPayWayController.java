/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.controller.setting.SettingPayWayController
 *  com.mdd.admin.service.ISettingPaymentService
 *  com.mdd.admin.vo.setting.SettingPaymentMethodVo
 *  com.mdd.common.core.AjaxResult
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
package com.mdd.admin.controller.setting;

import com.mdd.admin.service.ISettingPaymentService;
import com.mdd.admin.vo.setting.SettingPaymentMethodVo;
import com.mdd.common.core.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/setting.pay.pay_way"})
@Api(tags={"\u914d\u7f6e\u652f\u4ed8\u53c2\u6570"})
public class SettingPayWayController {
    @Resource
    ISettingPaymentService iSettingPaymentService;

    @GetMapping(value={"/getPayWay"})
    @ApiOperation(value="\u652f\u4ed8\u65b9\u5f0f\u5217\u8868")
    public AjaxResult<HashMap> method() {
        HashMap list = this.iSettingPaymentService.getPayWay();
        return AjaxResult.success(list);
    }

    @PostMapping(value={"/setPayWay"})
    @ApiOperation(value="\u652f\u4ed8\u65b9\u5f0f\u7f16\u8f91")
    public AjaxResult<Object> setPayWay(@Validated @RequestBody HashMap<Integer, List<SettingPaymentMethodVo>> data) {
        this.iSettingPaymentService.setPayWay(data);
        return AjaxResult.success();
    }
}

