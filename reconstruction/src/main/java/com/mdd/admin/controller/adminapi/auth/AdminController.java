/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.admin.controller.adminapi.auth.AdminController
 *  com.mdd.admin.service.admin.IAdminRoleService
 *  com.mdd.admin.service.admin.IAdminService
 *  com.mdd.admin.validate.system.SystemEditSelfValidate
 *  com.mdd.admin.vo.auth.AdminMySelfVo
 *  com.mdd.common.aop.NotPower
 *  com.mdd.common.core.AjaxResult
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.adminapi.auth;

import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.service.admin.IAdminRoleService;
import com.mdd.admin.service.admin.IAdminService;
import com.mdd.admin.validate.system.SystemEditSelfValidate;
import com.mdd.admin.vo.auth.AdminMySelfVo;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"adminapi/auth/admin", "adminapi/auth.admin"})
@Api(tags={"\u7ba1\u7406\u5458\u8be6\u60c5\u7ba1\u7406"})
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    @Resource
    IAdminService iAdminService;
    @Resource
    IAdminRoleService iAdminRoleService;
    @Resource
    IAdminService iSystemAuthAdminService;

    @NotPower
    @GetMapping(value={"/mySelf"})
    @ApiOperation(value="\u83b7\u53d6\u5f53\u524d\u7ba1\u7406\u5458\u4fe1\u606f")
    public AjaxResult<AdminMySelfVo> mySelf() {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        List roleIds = this.iAdminRoleService.getRoleIdAttr(adminId);
        AdminMySelfVo mySelf = this.iAdminService.mySelf(LikeAdminThreadLocal.getAdminId(), roleIds);
        return AjaxResult.success(mySelf);
    }

    /**
     * 编辑当前登录管理员自己的资料（#58）：只允许操作本人（adminId 取自登录态），
     * 因此不需要按钮权限；此前漏加 @NotPower 导致普通角色固定 403（路由权限串
     * auth.admin/editSelf 没有对应菜单项，无法授权）。
     */
    @NotPower
    @PostMapping(value={"/editSelf"})
    @ApiOperation(value="\u7f16\u8f91\u5f53\u524d\u7ba1\u7406\u5458\u4fe1\u606f")
    public AjaxResult<Object> editSelf(@Validated @RequestBody SystemEditSelfValidate upInfoValidate) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        this.iAdminService.editSelf(upInfoValidate, adminId);
        return AjaxResult.success();
    }
}

