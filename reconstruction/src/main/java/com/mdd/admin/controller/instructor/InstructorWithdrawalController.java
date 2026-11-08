/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.instructor.InstructorWithdrawalController
 *  com.mdd.admin.service.instructor.IInstructorWithdrawalService
 *  com.mdd.admin.validate.instructor.InstructorWithdrawalAuditValidate
 *  com.mdd.admin.validate.instructor.InstructorWithdrawalSearchValidate
 *  com.mdd.admin.vo.instructor.InstructorWithdrawalListedVo
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  javax.annotation.Resource
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.instructor;

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.instructor.IInstructorWithdrawalService;
import com.mdd.admin.validate.instructor.InstructorWithdrawalAuditValidate;
import com.mdd.admin.validate.instructor.InstructorWithdrawalSearchValidate;
import com.mdd.admin.vo.instructor.InstructorWithdrawalListedVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
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
@RequestMapping(value={"/adminapi/instructor/withdrawal"})
public class InstructorWithdrawalController {
    @Resource
    private IInstructorWithdrawalService instructorWithdrawalService;

    @GetMapping(value={"/list"})
    public AjaxResult<PageResult<InstructorWithdrawalListedVo>> list(@Validated InstructorWithdrawalSearchValidate searchValidate, @RequestParam(defaultValue="1") Integer pageNo, @RequestParam(defaultValue="20") Integer pageSize) {
        PageResult list = this.instructorWithdrawalService.list(searchValidate, pageNo, pageSize);
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/stats"})
    public AjaxResult<Map<String, Object>> stats() {
        return AjaxResult.success(this.instructorWithdrawalService.stats());
    }

    @Log(title="\u6559\u7ec3\u63d0\u73b0\u5ba1\u6838")
    @PostMapping(value={"/audit"})
    public AjaxResult<Object> audit(@Validated @RequestBody InstructorWithdrawalAuditValidate auditValidate) {
        this.instructorWithdrawalService.audit(auditValidate);
        return AjaxResult.success();
    }
}

