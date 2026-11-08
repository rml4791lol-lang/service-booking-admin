/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONArray
 *  com.mdd.admin.controller.system.DeptController
 *  com.mdd.admin.service.system.IDeptService
 *  com.mdd.admin.validate.commons.IdValidate
 *  com.mdd.admin.validate.system.DeptCreateValidate
 *  com.mdd.admin.validate.system.DeptSearchValidate
 *  com.mdd.admin.validate.system.DeptUpdateValidate
 *  com.mdd.admin.vo.system.DeptVo
 *  com.mdd.common.aop.NotPower
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
package com.mdd.admin.controller.system;

import com.alibaba.fastjson2.JSONArray;
import com.mdd.admin.service.system.IDeptService;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.system.DeptCreateValidate;
import com.mdd.admin.validate.system.DeptSearchValidate;
import com.mdd.admin.validate.system.DeptUpdateValidate;
import com.mdd.admin.vo.system.DeptVo;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
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
@RequestMapping(value={"/adminapi/dept.dept"})
@Api(tags={"\u7cfb\u7edf\u90e8\u95e8\u7ba1\u7406"})
public class DeptController {
    @Resource
    IDeptService deptService;

    @NotPower
    @GetMapping(value={"/all"})
    @ApiOperation(value="\u90e8\u95e8\u6240\u6709")
    public AjaxResult<JSONArray> all() {
        JSONArray list = this.deptService.all();
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u90e8\u95e8\u5217\u8868")
    public AjaxResult<JSONArray> list(@Validated DeptSearchValidate searchValidate) {
        JSONArray list = this.deptService.list(searchValidate);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u90e8\u95e8\u8be6\u60c5")
    public AjaxResult<DeptVo> detail(@Validated @IDMust @RequestParam(value="id") Integer id) {
        DeptVo vo = this.deptService.detail(id);
        return AjaxResult.success(vo);
    }

    @PostMapping(value={"/add"})
    @ApiOperation(value="\u90e8\u95e8\u65b0\u589e")
    public AjaxResult<Object> add(@Validated @RequestBody DeptCreateValidate createValidate) {
        this.deptService.add(createValidate);
        return AjaxResult.success();
    }

    @PostMapping(value={"/edit"})
    @ApiOperation(value="\u90e8\u95e8\u7f16\u8f91")
    public AjaxResult<Object> edit(@Validated @RequestBody DeptUpdateValidate updateValidate) {
        this.deptService.edit(updateValidate);
        return AjaxResult.success();
    }

    @PostMapping(value={"/delete"})
    @ApiOperation(value="\u90e8\u95e8\u5220\u9664")
    public AjaxResult<Object> del(@Validated @RequestBody IdValidate idValidate) {
        this.deptService.del(idValidate.getId());
        return AjaxResult.success();
    }
}

