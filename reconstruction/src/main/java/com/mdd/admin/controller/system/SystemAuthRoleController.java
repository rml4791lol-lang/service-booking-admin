/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.system.SystemAuthRoleController
 *  com.mdd.admin.service.system.ISystemRoleService
 *  com.mdd.admin.validate.commons.IdValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.system.SystemRoleCreateValidate
 *  com.mdd.admin.validate.system.SystemRoleUpdateValidate
 *  com.mdd.admin.vo.system.SystemAuthRoleVo
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
package com.mdd.admin.controller.system;

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.system.ISystemRoleService;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.system.SystemRoleCreateValidate;
import com.mdd.admin.validate.system.SystemRoleUpdateValidate;
import com.mdd.admin.vo.system.SystemAuthRoleVo;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
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
@RequestMapping(value={"/adminapi/auth.role"})
@Api(tags={"\u7cfb\u7edf\u89d2\u8272\u7ba1\u7406"})
public class SystemAuthRoleController {
    @Resource
    ISystemRoleService iSystemAuthRoleService;

    @NotPower
    @GetMapping(value={"/all"})
    @ApiOperation(value="\u6240\u6709\u89d2\u8272")
    public AjaxResult<List<SystemAuthRoleVo>> all() {
        List list = this.iSystemAuthRoleService.all();
        return AjaxResult.success(list);
    }

    @Log(title="\u89d2\u8272\u5217\u8868")
    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u89d2\u8272\u5217\u8868")
    public AjaxResult<PageResult<SystemAuthRoleVo>> list(@Validated PageValidate pageValidate) {
        PageResult list = this.iSystemAuthRoleService.list(pageValidate);
        return AjaxResult.success(list);
    }

    @Log(title="\u89d2\u8272\u8be6\u60c5")
    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u89d2\u8272\u8be6\u60c5")
    public AjaxResult<SystemAuthRoleVo> detail(@Validated @IDMust @RequestParam(value="id") Integer id) {
        SystemAuthRoleVo vo = this.iSystemAuthRoleService.detail(id);
        return AjaxResult.success(vo);
    }

    @Log(title="\u89d2\u8272\u65b0\u589e")
    @PostMapping(value={"/add"})
    @ApiOperation(value="\u89d2\u8272\u65b0\u589e")
    public AjaxResult<Object> add(@Validated @RequestBody SystemRoleCreateValidate createValidate) {
        this.iSystemAuthRoleService.add(createValidate);
        return AjaxResult.success();
    }

    @Log(title="\u89d2\u8272\u7f16\u8f91")
    @PostMapping(value={"/edit"})
    @ApiOperation(value="\u89d2\u8272\u7f16\u8f91")
    public AjaxResult<Object> edit(@Validated @RequestBody SystemRoleUpdateValidate updateValidate) {
        this.iSystemAuthRoleService.edit(updateValidate);
        return AjaxResult.success();
    }

    @Log(title="\u89d2\u8272\u5220\u9664")
    @PostMapping(value={"/delete"})
    @ApiOperation(value="\u89d2\u8272\u5220\u9664")
    public AjaxResult<Object> del(@Validated @RequestBody IdValidate idValidate) {
        this.iSystemAuthRoleService.del(idValidate.getId());
        return AjaxResult.success();
    }
}

