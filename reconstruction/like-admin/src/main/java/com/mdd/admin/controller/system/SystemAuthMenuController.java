/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONArray
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.system.SystemAuthMenuController
 *  com.mdd.admin.service.system.ISystemMenuService
 *  com.mdd.admin.validate.commons.IdValidate
 *  com.mdd.admin.validate.system.SystemMenuCreateValidate
 *  com.mdd.admin.validate.system.SystemMenuUpdateValidate
 *  com.mdd.admin.vo.system.SystemAuthMenuVo
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
import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.system.ISystemMenuService;
import com.mdd.admin.validate.commons.IdValidate;
import com.mdd.admin.validate.system.SystemMenuCreateValidate;
import com.mdd.admin.validate.system.SystemMenuUpdateValidate;
import com.mdd.admin.vo.system.SystemAuthMenuVo;
import com.mdd.common.aop.NotPower;
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
@RequestMapping(value={"/adminapi/auth.menu"})
@Api(tags={"\u7cfb\u7edf\u83dc\u5355\u7ba1\u7406"})
public class SystemAuthMenuController {
    @Resource
    ISystemMenuService iSystemAuthMenuService;

    @NotPower
    @GetMapping(value={"/route"})
    @ApiOperation(value="\u83b7\u53d6\u83dc\u5355\u8def\u7531")
    public AjaxResult<JSONArray> route() {
        List roleIds = LikeAdminThreadLocal.getRoleIds();
        JSONArray lists = this.iSystemAuthMenuService.selectMenuByRoleId(roleIds);
        return AjaxResult.success(lists);
    }

    @NotPower
    @GetMapping(value={"/lists"})
    @ApiOperation(value="\u83b7\u53d6\u83dc\u5355\u5217\u8868")
    public AjaxResult<JSONObject> list() {
        JSONObject result = this.iSystemAuthMenuService.list();
        return AjaxResult.success(result);
    }

    @NotPower
    @GetMapping(value={"/systemMenuLists"})
    @ApiOperation(value="\u83b7\u53d6\u83dc\u5355\u5217\u8868")
    public AjaxResult<Object> systemMenuLists() {
        JSONArray result = this.iSystemAuthMenuService.systemMenuLists();
        return AjaxResult.success(result);
    }

    @NotPower
    @GetMapping(value={"/all"})
    @ApiOperation(value="\u83b7\u53d6\u83dc\u5355\u5217\u8868")
    public AjaxResult<JSONArray> all() {
        JSONArray result = this.iSystemAuthMenuService.all();
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u83b7\u53d6\u83dc\u5355\u8be6\u60c5")
    public AjaxResult<SystemAuthMenuVo> detail(@Validated @IDMust @RequestParam(value="id") Integer id) {
        SystemAuthMenuVo vo = this.iSystemAuthMenuService.detail(id);
        return AjaxResult.success(vo);
    }

    @Log(title="\u83dc\u5355\u65b0\u589e")
    @PostMapping(value={"/add"})
    @ApiOperation(value="\u65b0\u589e\u83dc\u5355")
    public AjaxResult<Object> add(@Validated @RequestBody SystemMenuCreateValidate createValidate) {
        this.iSystemAuthMenuService.add(createValidate);
        return AjaxResult.success();
    }

    @Log(title="\u83dc\u5355\u7f16\u8f91")
    @PostMapping(value={"/edit"})
    @ApiOperation(value="\u83dc\u5355\u7f16\u8f91")
    public AjaxResult<Object> edit(@Validated @RequestBody SystemMenuUpdateValidate updateValidate) {
        this.iSystemAuthMenuService.edit(updateValidate);
        return AjaxResult.success();
    }

    @Log(title="\u83dc\u5355\u5220\u9664")
    @PostMapping(value={"/delete"})
    @ApiOperation(value="\u83dc\u5355\u5220\u9664")
    public AjaxResult<Object> del(@Validated @RequestBody IdValidate idValidate) {
        this.iSystemAuthMenuService.del(idValidate.getId());
        return AjaxResult.success();
    }
}

