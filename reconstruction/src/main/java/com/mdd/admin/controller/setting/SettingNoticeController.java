/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.setting.SettingNoticeController
 *  com.mdd.admin.service.ISettingNoticeService
 *  com.mdd.admin.vo.setting.SettingNoticeDetailVo
 *  com.mdd.common.core.AjaxResult
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

import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.ISettingNoticeService;
import com.mdd.admin.vo.setting.SettingNoticeDetailVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/notice.notice"})
@Api(tags={"\u914d\u7f6e\u6d88\u606f\u901a\u77e5"})
public class SettingNoticeController {
    @Resource
    ISettingNoticeService iSettingNoticeService;

    @GetMapping(value={"/settingLists"})
    @ApiOperation(value="\u901a\u77e5\u8bbe\u7f6e\u5217\u8868")
    public AjaxResult<JSONObject> list(@RequestParam Integer recipient) {
        List list = this.iSettingNoticeService.list(recipient);
        JSONObject result = new JSONObject();
        result.put("lists", list);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u901a\u77e5\u8bbe\u7f6e\u8be6\u60c5")
    public AjaxResult<SettingNoticeDetailVo> detail(@Validated @IDMust @RequestParam(value="id") Integer id) {
        SettingNoticeDetailVo vo = this.iSettingNoticeService.detail(id);
        return AjaxResult.success(vo);
    }

    @Log(title="\u901a\u77e5\u8bbe\u7f6e\u7f16\u8f91")
    @PostMapping(value={"/set"})
    @ApiOperation(value="\u901a\u77e5\u8bbe\u7f6e\u7f16\u8f91")
    public AjaxResult<Object> save(@RequestBody JSONObject params) {
        this.iSettingNoticeService.save(params);
        return AjaxResult.success();
    }
}

