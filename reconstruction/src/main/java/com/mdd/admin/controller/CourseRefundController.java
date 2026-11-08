/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.admin.aop.Log
 *  com.mdd.admin.controller.CourseRefundController
 *  com.mdd.admin.service.ICourseRefundService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.refund.CourseRefundAuditValidate
 *  com.mdd.admin.validate.refund.CourseRefundSearchValidate
 *  com.mdd.admin.vo.refund.CourseRefundListedVo
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

import cn.dev33.satoken.stp.StpUtil;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.ICourseRefundService;
import com.mdd.admin.service.IRefundTaskService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.refund.CourseRefundAuditValidate;
import com.mdd.admin.validate.refund.CourseRefundSearchValidate;
import com.mdd.admin.validate.refund.RefundTaskRetryValidate;
import com.mdd.admin.validate.refund.RefundTaskSearchValidate;
import com.mdd.admin.vo.refund.CourseRefundListedVo;
import com.mdd.admin.vo.refund.RefundTaskListVo;
import com.mdd.admin.vo.refund.RefundTaskLogVo;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.LoginException;
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
@RequestMapping(value={"/adminapi/course_refund"})
@Api(tags={"\u9000\u8bfe\u7533\u8bf7\u7ba1\u7406"})
public class CourseRefundController {
    @Resource
    ICourseRefundService iCourseRefundService;
    @Resource
    IRefundTaskService iRefundTaskService;

    @GetMapping(value={"/list"})
    @ApiOperation(value="\u9000\u8bfe\u7533\u8bf7\u5217\u8868")
    public AjaxResult<PageResult<CourseRefundListedVo>> list(@Validated PageValidate pageValidate, @Validated CourseRefundSearchValidate searchValidate) {
        PageResult list = this.iCourseRefundService.list(pageValidate, searchValidate);
        return AjaxResult.success(list);
    }

    @Log(title="\u5ba1\u6279\u9000\u8bfe\u7533\u8bf7")
    @PostMapping(value={"/audit"})
    @ApiOperation(value="\u5ba1\u6279\u9000\u8bfe\u7533\u8bf7")
    public AjaxResult<Object> audit(@Validated @RequestBody CourseRefundAuditValidate auditValidate) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        this.iCourseRefundService.audit(auditValidate, adminId);
        return AjaxResult.success();
    }

    /**
     * 退款任务列表（#60）：路由没有独立菜单项，沿用退课审批/查询权限做人工校验；
     * 与 LikeAdminInterceptor 一致，超级管理员（admin id=1）放行。
     */
    @NotPower
    @GetMapping(value={"/task/list"})
    @ApiOperation(value="退款任务列表")
    public AjaxResult<PageResult<RefundTaskListVo>> taskList(@Validated PageValidate pageValidate, @Validated RefundTaskSearchValidate searchValidate) {
        this.checkRefundReadPermission();
        PageResult<RefundTaskListVo> result = this.iRefundTaskService.list(pageValidate, searchValidate);
        return AjaxResult.success(result);
    }

    @NotPower
    @GetMapping(value={"/task/log"})
    @ApiOperation(value="退款任务流水")
    public AjaxResult<List<RefundTaskLogVo>> taskLog(@Validated @IDMust @RequestParam(value="task_id") Integer taskId) {
        this.checkRefundReadPermission();
        return AjaxResult.success(this.iRefundTaskService.logs(taskId));
    }

    @Log(title="重试退款任务")
    @NotPower
    @PostMapping(value={"/task/retry"})
    @ApiOperation(value="重试退款任务")
    public AjaxResult<Object> taskRetry(@Validated @RequestBody RefundTaskRetryValidate retryValidate) {
        this.checkRefundRetryPermission();
        this.iRefundTaskService.retry(retryValidate.getId(), LikeAdminThreadLocal.getAdminId());
        return AjaxResult.success();
    }

    private void checkRefundReadPermission() {
        this.checkAnyPermission("course_refund/audit", "course_refund/list");
    }

    private void checkRefundRetryPermission() {
        this.checkAnyPermission("course_refund/audit", "course_refund/task/retry");
    }

    private void checkAnyPermission(String... permissions) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        if (adminId != null && adminId == 1) {
            return;
        }
        boolean allowed = false;
        try {
            for (String permission : permissions) {
                if (StpUtil.hasPermission(permission)) {
                    allowed = true;
                    break;
                }
            }
        } catch (Exception e) {
            allowed = false;
        }
        if (!allowed) {
            throw new LoginException(ErrorEnum.NO_PERMISSION.getCode(), ErrorEnum.NO_PERMISSION.getMsg());
        }
    }
}
