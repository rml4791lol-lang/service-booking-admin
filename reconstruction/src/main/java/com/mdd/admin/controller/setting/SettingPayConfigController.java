/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.controller.setting.SettingPayConfigController
 *  com.mdd.admin.service.ISettingPaymentService
 *  com.mdd.admin.validate.setting.SettingPayConfigValidate
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.entity.setting.DevPayConfig
 *  com.mdd.common.validator.annotation.IDMust
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.setting;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.service.ISettingPaymentService;
import com.mdd.admin.validate.setting.SettingPayConfigValidate;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.entity.setting.DevPayConfig;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.YmlUtils;
import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/setting.pay.pay_config"})
@Api(tags={"\u914d\u7f6e\u652f\u4ed8\u53c2\u6570"})
public class SettingPayConfigController {
    @Resource
    ISettingPaymentService iSettingPaymentService;

    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u652f\u4ed8\u914d\u7f6e\u5217\u8868")
    public AjaxResult<JSONObject> list() {
        JSONObject result = this.iSettingPaymentService.list();
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/getConfig"})
    @ApiOperation(value="\u652f\u4ed8\u914d\u7f6e\u8be6\u60c5")
    public AjaxResult<Object> getConfig(@Validated @IDMust @RequestParam(value="id") Integer id) {
        DevPayConfig vo = this.iSettingPaymentService.getConfig(id);
        // 编辑页（admin-src views/setting/pay/config/edit.vue）按 pay_way 选择微信/支付宝参数模板、
        // 按 domain 展示支付授权目录；实体字段为 payWay 且无 domain，直接返回会让参数表单不渲染、
        // 商户参数无法保存（task-7）。这里在响应层补齐两个回显字段，不改实体/表结构。
        JSONObject result = (JSONObject) JSON.toJSON(vo);
        result.put("pay_way", vo.getPayWay());
        result.put("domain", this.resolveFrontDomain());
        return AjaxResult.success(result);
    }

    /** 前台域名：优先 like.front-url（小程序端 H5 域名），回退 like.url；去掉结尾斜杠。 */
    private String resolveFrontDomain() {
        String domain = YmlUtils.get("like.front-url");
        if (StringUtils.isEmpty(domain)) {
            domain = YmlUtils.get("like.url");
        }
        if (StringUtils.isEmpty(domain)) {
            return "";
        }
        return domain.replaceAll("/+$", "");
    }

    @PostMapping(value={"/setConfig"})
    @ApiOperation(value="\u652f\u4ed8\u914d\u7f6e\u7f16\u8f91")
    public AjaxResult<Object> setConfig(@Validated @RequestBody SettingPayConfigValidate configValidate) {
        this.iSettingPaymentService.setConfig(configValidate);
        return AjaxResult.success();
    }
}

