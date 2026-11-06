/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.setting.SettingDictTypeController
 *  com.mdd.admin.service.ISettingDictTypeService
 *  com.mdd.admin.validate.commons.IdValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.setting.DictTypeCreateValidate
 *  com.mdd.admin.validate.setting.DictTypeUpdateValidate
 *  com.mdd.admin.vo.setting.SettingDictTypeVo
 *  com.mdd.common.aop.NotPower
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
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

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.ISettingDictTypeService;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.setting.DictTypeCreateValidate;
import com.mdd.admin.validate.setting.DictTypeUpdateValidate;
import com.mdd.admin.vo.setting.SettingDictTypeVo;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
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
@RequestMapping(value={"/adminapi/setting.dict.dict_type"})
@Api(tags={"\u914d\u7f6e\u5b57\u5178\u7c7b\u578b"})
public class SettingDictTypeController {
    @Resource
    ISettingDictTypeService iSettingDictTypeService;

    @NotPower
    @GetMapping(value={"/all"})
    @ApiOperation(value="\u5b57\u5178\u7c7b\u578b\u6240\u6709")
    public AjaxResult<List<SettingDictTypeVo>> all() {
        List list = this.iSettingDictTypeService.all();
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u5b57\u5178\u7c7b\u578b\u5217\u8868")
    public AjaxResult<PageResult<SettingDictTypeVo>> list(@Validated PageValidate pageValidate, @RequestParam Map<String, String> params) {
        PageResult list = this.iSettingDictTypeService.list(pageValidate, params);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u5b57\u5178\u7c7b\u578b\u8be6\u60c5")
    public AjaxResult<SettingDictTypeVo> detail(@Validated @IDMust @RequestParam(value="id") Integer id) {
        SettingDictTypeVo vo = this.iSettingDictTypeService.detail(id);
        return AjaxResult.success(vo);
    }

    @Log(title="\u5b57\u5178\u7c7b\u578b\u65b0\u589e")
    @PostMapping(value={"/add"})
    @ApiOperation(value="\u5b57\u5178\u7c7b\u578b\u65b0\u589e")
    public AjaxResult<Object> add(@Validated @RequestBody DictTypeCreateValidate createValidate) {
        this.iSettingDictTypeService.add(createValidate);
        return AjaxResult.success();
    }

    @Log(title="\u5b57\u5178\u7c7b\u578b\u7f16\u8f91")
    @PostMapping(value={"/edit"})
    @ApiOperation(value="\u5b57\u5178\u7c7b\u578b\u7f16\u8f91")
    public AjaxResult<Object> edit(@Validated @RequestBody DictTypeUpdateValidate updateValidate) {
        this.iSettingDictTypeService.edit(updateValidate);
        return AjaxResult.success();
    }

    @Log(title="\u5b57\u5178\u7c7b\u578b\u5220\u9664")
    @PostMapping(value={"/delete"})
    @ApiOperation(value="\u5b57\u5178\u7c7b\u578b\u5220\u9664")
    public AjaxResult<Object> del(@Validated @RequestBody IdValidate idValidate) {
        this.iSettingDictTypeService.del(idValidate.getId());
        return AjaxResult.success();
    }
}

