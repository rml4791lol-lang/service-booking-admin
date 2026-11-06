package com.mdd.admin.controller;

import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.ICourseCheckinService;
import com.mdd.admin.validate.checkin.CourseCheckinAuditSearchValidate;
import com.mdd.admin.validate.checkin.CourseCheckinManualValidate;
import com.mdd.admin.validate.checkin.CourseCheckinSearchValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 后台待核销与人工补核销（T30.4 / #37）。
 *
 * <p>接口权限按路由校验：{@code course_checkin/list|detail|options|manual|audit}，
 * 菜单/权限 seed 见 tools/ddl/migrations/20261023-add-pending-checkin-menu.sql。</p>
 */
@RestController
@RequestMapping(value = "/adminapi/course_checkin")
@Api(tags = "待核销与人工补核销")
public class CourseCheckinController {

    @Resource
    ICourseCheckinService iCourseCheckinService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "待核销场次列表")
    public AjaxResult<PageResult<Map<String, Object>>> list(@Validated PageValidate pageValidate,
                                                            @Validated CourseCheckinSearchValidate searchValidate) {
        return AjaxResult.success(iCourseCheckinService.list(pageValidate, searchValidate));
    }

    @GetMapping(value = "/detail")
    @ApiOperation(value = "场次详情（学员/券/审计）")
    public AjaxResult<Map<String, Object>> detail(@RequestParam(value = "sessionId") Integer sessionId) {
        return AjaxResult.success(iCourseCheckinService.detail(sessionId));
    }

    @GetMapping(value = "/options")
    @ApiOperation(value = "待核销筛选下拉")
    public AjaxResult<Map<String, Object>> options() {
        return AjaxResult.success(iCourseCheckinService.options());
    }

    @Log(title = "人工补核销")
    @PostMapping(value = "/manual")
    @ApiOperation(value = "人工补核销")
    public AjaxResult<Map<String, Object>> manual(
            @Validated @RequestBody CourseCheckinManualValidate manualValidate) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        return AjaxResult.success(iCourseCheckinService.manual(adminId, manualValidate));
    }

    @GetMapping(value = "/audit")
    @ApiOperation(value = "补核销审计查询")
    public AjaxResult<PageResult<Map<String, Object>>> audit(@Validated PageValidate pageValidate,
                                                             @Validated CourseCheckinAuditSearchValidate searchValidate) {
        return AjaxResult.success(iCourseCheckinService.audits(pageValidate, searchValidate));
    }
}
