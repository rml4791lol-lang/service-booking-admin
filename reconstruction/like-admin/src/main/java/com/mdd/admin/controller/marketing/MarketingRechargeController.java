/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.controller.marketing.MarketingRechargeController
 *  com.mdd.admin.service.IMarketingRechargeService
 *  com.mdd.admin.validate.marketing.MarketingRechargeValidate
 *  com.mdd.admin.vo.marketing.MarketingRechargeVo
 *  com.mdd.common.core.AjaxResult
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiModelProperty
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.marketing;

import com.mdd.admin.service.IMarketingRechargeService;
import com.mdd.admin.validate.marketing.MarketingRechargeValidate;
import com.mdd.admin.vo.marketing.MarketingRechargeVo;
import com.mdd.common.core.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"adminapi/recharge/recharge", "adminapi/recharge.recharge"})
@Api(value="\u8425\u9500\u5145\u503c\u7ba1\u7406")
public class MarketingRechargeController {
    @Resource
    IMarketingRechargeService iMarketingRechargeService;

    @GetMapping(value={"/getConfig"})
    @ApiModelProperty(value="\u5145\u503c\u914d\u7f6e\u8be6\u60c5")
    public AjaxResult<MarketingRechargeVo> detail() {
        MarketingRechargeVo vo = this.iMarketingRechargeService.detail();
        return AjaxResult.success(vo);
    }

    @PostMapping(value={"/setConfig"})
    @ApiModelProperty(value="\u5145\u503c\u914d\u7f6e\u4fdd\u5b58")
    public AjaxResult<Object> save(@Validated @RequestBody MarketingRechargeValidate rechargeValidate) {
        this.iMarketingRechargeService.save(rechargeValidate);
        return AjaxResult.success();
    }
}

