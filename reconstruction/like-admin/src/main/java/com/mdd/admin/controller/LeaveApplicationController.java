/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.LeaveApplicationController
 *  com.mdd.admin.service.ILeaveApplicationService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.leave.LeaveAuditValidate
 *  com.mdd.admin.validate.leave.LeaveSearchValidate
 *  com.mdd.admin.vo.leave.LeaveListedVo
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
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
package com.mdd.admin.controller;

import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.ILeaveApplicationService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.leave.LeaveAuditValidate;
import com.mdd.admin.validate.leave.LeaveSearchValidate;
import com.mdd.admin.vo.leave.LeaveListedVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/leave"})
@Api(tags={"\u8bf7\u5047\u7ba1\u7406"})
public class LeaveApplicationController {
    @Resource
    ILeaveApplicationService leaveApplicationService;

    @GetMapping(value={"/list"})
    @ApiOperation(value="\u8bf7\u5047\u5217\u8868")
    public AjaxResult<PageResult<LeaveListedVo>> list(@Validated PageValidate pageValidate, @Validated LeaveSearchValidate searchValidate) {
        PageResult list = this.leaveApplicationService.list(pageValidate, searchValidate);
        return AjaxResult.success(list);
    }

    @Log(title="\u5ba1\u6279\u8bf7\u5047")
    @PostMapping(value={"/audit"})
    @ApiOperation(value="\u5ba1\u6279\u8bf7\u5047")
    public AjaxResult<Object> audit(@Validated @RequestBody LeaveAuditValidate auditValidate) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        this.leaveApplicationService.audit(auditValidate, adminId);
        return AjaxResult.success();
    }
}

