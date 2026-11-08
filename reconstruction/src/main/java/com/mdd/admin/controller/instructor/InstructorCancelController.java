/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.admin.controller.instructor.InstructorCancelController
 *  com.mdd.admin.service.IInstructorCancelService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.instructor.InstructorCancelAuditValidate
 *  com.mdd.admin.validate.instructor.InstructorCancelSearchValidate
 *  com.mdd.admin.vo.instructor.InstructorCancelListVo
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
package com.mdd.admin.controller.instructor;

import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.IInstructorCancelService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.instructor.InstructorCancelAuditValidate;
import com.mdd.admin.validate.instructor.InstructorCancelSearchValidate;
import com.mdd.admin.vo.instructor.InstructorCancelListVo;
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
@RequestMapping(value={"/adminapi/instructor/cancel"})
@Api(tags={"\u6559\u7ec3\u6ce8\u9500\u7ba1\u7406"})
public class InstructorCancelController {
    @Resource
    IInstructorCancelService instructorCancelService;

    @GetMapping(value={"/list"})
    @ApiOperation(value="\u83b7\u53d6\u6ce8\u9500\u7533\u8bf7\u5217\u8868")
    public AjaxResult<PageResult<InstructorCancelListVo>> list(@Validated PageValidate pageValidate, @Validated InstructorCancelSearchValidate searchValidate) {
        PageResult list = this.instructorCancelService.list(pageValidate, searchValidate);
        return AjaxResult.success(list);
    }

    @Log(title="\u6559\u7ec3\u6ce8\u9500\u5ba1\u6838")
    @PostMapping(value={"/audit"})
    @ApiOperation(value="\u6ce8\u9500\u5ba1\u6279")
    public AjaxResult<Object> audit(@Validated @RequestBody InstructorCancelAuditValidate auditValidate) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        this.instructorCancelService.audit(auditValidate, adminId);
        return AjaxResult.success();
    }
}

