/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  com.mdd.front.LikeFrontThreadLocal
 *  com.mdd.front.controller.LeaveController
 *  com.mdd.front.service.ILeaveService
 *  com.mdd.front.validate.common.PageValidate
 *  com.mdd.front.validate.leave.LeaveApplyValidate
 *  com.mdd.front.validate.leave.LeaveAuditValidate
 *  com.mdd.front.vo.leave.LeaveListVo
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
package com.mdd.front.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.front.LikeFrontThreadLocal;
import com.mdd.front.service.ILeaveService;
import com.mdd.front.validate.common.PageValidate;
import com.mdd.front.validate.leave.LeaveApplyValidate;
import com.mdd.front.validate.leave.LeaveAuditValidate;
import com.mdd.front.vo.leave.LeaveListVo;
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
@RequestMapping(value={"/api/leave"})
@Api(tags={"\u8bf7\u5047\u63a5\u53e3"})
public class LeaveController {
    @Resource
    ILeaveService iLeaveService;

    @PostMapping(value={"/apply"})
    @ApiOperation(value="\u63d0\u4ea4\u8bf7\u5047\u7533\u8bf7")
    public AjaxResult<Object> apply(@Validated @RequestBody LeaveApplyValidate validate) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        boolean isInstructor = validate.getApplicantType() != null && validate.getApplicantType() == 2;
        this.iLeaveService.apply(validate, userId, isInstructor);
        return AjaxResult.success();
    }

    @GetMapping(value={"/myLeaves"})
    @ApiOperation(value="\u6211\u7684\u8bf7\u5047\u8bb0\u5f55(\u5b66\u751f\u7aef)")
    public AjaxResult<PageResult<LeaveListVo>> myLeaves(@Validated PageValidate pageValidate, @RequestParam(value="status", required=false) Integer status) {
        Integer userId = LikeFrontThreadLocal.getUserId();
        PageResult list = this.iLeaveService.myLeaves(pageValidate, userId, status);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/classLeaves"})
    @ApiOperation(value="\u73ed\u7ea7\u8bf7\u5047\u8bb0\u5f55(\u6559\u7ec3\u7aef\u67e5\u770b\u5b66\u5458\u8bf7\u5047)")
    public AjaxResult<PageResult<LeaveListVo>> classLeaves(@Validated PageValidate pageValidate, @RequestParam(value="classId", required=false) Integer classId, @RequestParam(value="status", required=false) Integer status) {
        Integer instructorUserId = LikeFrontThreadLocal.getUserId();
        PageResult list = this.iLeaveService.classLeaves(pageValidate, instructorUserId, classId, status);
        return AjaxResult.success(list);
    }

    @PostMapping(value={"/audit"})
    @ApiOperation(value="\u6559\u7ec3\u5ba1\u6279\u8bf7\u5047")
    public AjaxResult<Object> audit(@Validated @RequestBody LeaveAuditValidate auditValidate) {
        Integer instructorUserId = LikeFrontThreadLocal.getUserId();
        this.iLeaveService.audit(auditValidate, instructorUserId);
        return AjaxResult.success();
    }
}

