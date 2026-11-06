/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.setting.SettingWebsiteController
 *  com.mdd.admin.service.ISettingCopyrightService
 *  com.mdd.admin.service.ISettingProtocolService
 *  com.mdd.admin.service.ISettingWebsiteService
 *  com.mdd.admin.validate.setting.SettingAgreementValidate
 *  com.mdd.admin.validate.setting.SettingCopyrightValidate
 *  com.mdd.admin.validate.setting.SettingSiteStatisticsValidate
 *  com.mdd.admin.validate.setting.SettingWebsiteValidate
 *  com.mdd.admin.vo.setting.SettingAgreementVo
 *  com.mdd.admin.vo.setting.SettingCopyrightVo
 *  com.mdd.admin.vo.setting.SettingSiteStatisticsVo
 *  com.mdd.admin.vo.setting.SettingWebsiteVo
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.util.ConfigUtils
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

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.ISettingCopyrightService;
import com.mdd.admin.service.ISettingProtocolService;
import com.mdd.admin.service.ISettingWebsiteService;
import com.mdd.admin.validate.setting.SettingAgreementValidate;
import com.mdd.admin.validate.setting.SettingCopyrightValidate;
import com.mdd.admin.validate.setting.SettingSiteStatisticsValidate;
import com.mdd.admin.validate.setting.SettingWebsiteValidate;
import com.mdd.admin.vo.setting.SettingAgreementVo;
import com.mdd.admin.vo.setting.SettingCopyrightVo;
import com.mdd.admin.vo.setting.SettingSiteStatisticsVo;
import com.mdd.admin.vo.setting.SettingWebsiteVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.util.ConfigUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"adminapi/setting.web.web_setting"})
@Api(tags={"\u914d\u7f6e\u7f51\u7ad9\u4fe1\u606f"})
public class SettingWebsiteController {
    @Resource
    ISettingWebsiteService iSettingWebsiteService;
    @Resource
    ISettingCopyrightService iSettingCopyrightService;
    @Resource
    ISettingProtocolService iSettingProtocolService;

    @GetMapping(value={"/getWebsite"})
    @ApiOperation(value="\u7f51\u7ad9\u914d\u7f6e\u4fe1\u606f")
    public AjaxResult<SettingWebsiteVo> getWebsite() {
        SettingWebsiteVo detail = this.iSettingWebsiteService.getWebsite();
        return AjaxResult.success(detail);
    }

    @Log(title="\u7f51\u7ad9\u914d\u7f6e\u7f16\u8f91")
    @PostMapping(value={"/setWebsite"})
    @ApiOperation(value="\u7f51\u7ad9\u914d\u7f6e\u7f16\u8f91")
    public AjaxResult<Object> setWebsite(@Validated @RequestBody SettingWebsiteValidate websiteValidate) {
        this.iSettingWebsiteService.setWebsite(websiteValidate);
        return AjaxResult.success();
    }

    @GetMapping(value={"/getCopyright"})
    @ApiOperation(value="\u7f51\u7ad9\u7248\u6743\u4fe1\u606f")
    public AjaxResult<List<SettingCopyrightVo>> getCopyright() {
        List list = this.iSettingCopyrightService.getCopyright();
        return AjaxResult.success(list);
    }

    @Log(title="\u7f51\u7ad9\u7248\u6743\u7f16\u8f91")
    @PostMapping(value={"/setCopyright"})
    @ApiOperation(value="\u7f51\u7ad9\u7248\u6743\u7f16\u8f91")
    public AjaxResult<Object> setCopyright(@Validated @RequestBody SettingCopyrightValidate copyrightValidate) {
        this.iSettingCopyrightService.setCopyright(copyrightValidate);
        return AjaxResult.success();
    }

    @GetMapping(value={"/getAgreement"})
    @ApiOperation(value="\u653f\u7b56\u534f\u8bae\u4fe1\u606f")
    public AjaxResult<SettingAgreementVo> getAgreement() {
        SettingAgreementVo detail = this.iSettingProtocolService.getAgreement();
        return AjaxResult.success(detail);
    }

    @Log(title="\u653f\u7b56\u534f\u8bae\u7f16\u8f91")
    @PostMapping(value={"/setAgreement"})
    @ApiOperation(value="\u653f\u7b56\u534f\u8bae\u7f16\u8f91")
    public AjaxResult<Object> setAgreement(@Validated @RequestBody SettingAgreementValidate protocolValidate) {
        this.iSettingProtocolService.setAgreement(protocolValidate);
        return AjaxResult.success();
    }

    @GetMapping(value={"/getSiteStatistics"})
    @ApiOperation(value="\u83b7\u53d6\u7ad9\u70b9\u7edf\u8ba1\u914d\u7f6e")
    public AjaxResult<SettingSiteStatisticsVo> getSiteStatistics() {
        SettingSiteStatisticsVo vo = new SettingSiteStatisticsVo();
        vo.setClarityCode(ConfigUtils.get((String)"siteStatistics", (String)"clarity_code", (String)""));
        return AjaxResult.success(vo);
    }

    @Log(title="\u7ad9\u70b9\u7edf\u8ba1\u914d\u7f6e")
    @PostMapping(value={"/setSiteStatistics"})
    @ApiOperation(value="\u7ad9\u70b9\u7edf\u8ba1\u914d\u7f6e")
    public AjaxResult<Object> setSiteStatistics(@Validated @RequestBody SettingSiteStatisticsValidate settingSiteStatisticsValidate) {
        ConfigUtils.set((String)"siteStatistics", (String)"clarity_code", (String)settingSiteStatisticsValidate.getClarityCode());
        return AjaxResult.success();
    }
}

