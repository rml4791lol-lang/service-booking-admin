/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.setting.SettingDictDataController
 *  com.mdd.admin.service.ISettingDictDataService
 *  com.mdd.admin.validate.commons.IdArrayValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.setting.DictDataCreateValidate
 *  com.mdd.admin.validate.setting.DictDataUpdateValidate
 *  com.mdd.admin.vo.setting.SettingDictDataVo
 *  com.mdd.common.aop.NotPower
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.util.StringUtils
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

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.ISettingDictDataService;
import com.mdd.admin.validate.commons.IdArrayValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.setting.DictDataCreateValidate;
import com.mdd.admin.validate.setting.DictDataUpdateValidate;
import com.mdd.admin.vo.setting.SettingDictDataVo;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.util.StringUtils;
import com.mdd.common.validator.annotation.IDMust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/setting.dict.dict_data"})
@Api(tags={"\u914d\u7f6e\u5b57\u5178\u6570\u636e"})
public class SettingDictDataController {
    @Resource
    ISettingDictDataService iSettingDictDataService;

    @NotPower
    @GetMapping(value={"/all"})
    @ApiOperation(value="\u5b57\u5178\u6570\u636e\u6240\u6709")
    public AjaxResult<List<SettingDictDataVo>> all(@RequestParam Map<String, String> params) {
        Assert.isFalse((boolean)StringUtils.isEmpty((String)params.get("type_id")), (String)"type_id\u7f3a\u5931", (Object[])new Object[0]);
        List list = this.iSettingDictDataService.all(params);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u5b57\u5178\u6570\u636e\u5217\u8868")
    public AjaxResult<PageResult<SettingDictDataVo>> list(@Validated PageValidate pageValidate, @RequestParam Map<String, String> params) {
        Assert.isFalse((boolean)StringUtils.isEmpty((String)params.get("type_id")), (String)"type_id\u7f3a\u5931", (Object[])new Object[0]);
        PageResult list = this.iSettingDictDataService.list(pageValidate, params);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u5b57\u5178\u6570\u636e\u8be6\u60c5")
    public AjaxResult<SettingDictDataVo> detail(@Validated @IDMust @RequestParam(value="id") Integer id) {
        SettingDictDataVo vo = this.iSettingDictDataService.detail(id);
        return AjaxResult.success(vo);
    }

    @Log(title="\u5b57\u5178\u6570\u636e\u65b0\u589e")
    @PostMapping(value={"/add"})
    @ApiOperation(value="\u5b57\u5178\u6570\u636e\u65b0\u589e")
    public AjaxResult<Object> add(@Validated @RequestBody DictDataCreateValidate createValidate) {
        this.iSettingDictDataService.add(createValidate);
        return AjaxResult.success((String)"\u64cd\u4f5c\u6210\u529f");
    }

    @Log(title="\u5b57\u5178\u6570\u636e\u7f16\u8f91")
    @PostMapping(value={"/edit"})
    @ApiOperation(value="\u5b57\u5178\u6570\u636e\u7f16\u8f91")
    public AjaxResult<Object> edit(@Validated @RequestBody DictDataUpdateValidate updateValidate) {
        this.iSettingDictDataService.edit(updateValidate);
        return AjaxResult.success((String)"\u64cd\u4f5c\u6210\u529f");
    }

    @Log(title="\u5b57\u5178\u6570\u636e\u5220\u9664")
    @PostMapping(value={"/delete"})
    @ApiOperation(value="\u5b57\u5178\u6570\u636e\u5220\u9664")
    public AjaxResult<Object> del(@Validated @RequestBody IdArrayValidate idArrayValidate) {
        this.iSettingDictDataService.del(idArrayValidate.getId());
        return AjaxResult.success((String)"\u64cd\u4f5c\u6210\u529f");
    }

    @NotPower
    @GetMapping(value={"/type"})
    @ApiOperation(value="\u6839\u636e\u7c7b\u578b\u83b7\u53d6\u5b57\u5178\u6570\u636e")
    public AjaxResult<List<SettingDictDataVo>> getByType(@RequestParam Map<String, String> params) {
        Assert.isFalse((boolean)StringUtils.isEmpty((String)params.get("dictType")), (String)"dictType\u7f3a\u5931", (Object[])new Object[0]);
        List list = this.iSettingDictDataService.all(params);
        return AjaxResult.success(list);
    }
}

