package com.mdd.admin.controller.report;

import com.mdd.admin.aop.Log;
import com.mdd.admin.service.IReportService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.report.ReportSearchValidate;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.enums.ErrorEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 后台数据报表（T30.5 / #40、#102）。
 *
 * <p>五类报表：课程、核销、券流水、活动报名、开班申请，加教练月营业额（#102）。
 * 列表与导出共用同一查询口径；导出返回 {@code code=2 + url}，由前端打开下载地址。
 * 课程报表与教练月营业额只展示真实营业额与工作量，不做二次分配计算。</p>
 *
 * <p>权限字符串与 URI 一一对应：{@code report/course/lists}、{@code report/course/export} …，
 * 菜单/按钮 seed 见 tools/ddl/migrations/20261024-add-report-menu.sql。</p>
 */
@RestController
@RequestMapping(value = "/adminapi/report")
@Api(tags = "数据报表与 Excel 导出")
public class ReportController {

    @Resource
    IReportService iReportService;

    // ------------------------------------------------------------------
    // 课程报表（真实收益口径：订单/课时/核销/营业额）
    // ------------------------------------------------------------------

    @GetMapping(value = "/course/lists")
    @ApiOperation(value = "课程报表列表（真实收益口径：订单/课时/核销/营业额）")
    public AjaxResult<PageResult<Map<String, Object>>> courseList(
            @Validated PageValidate pageValidate,
            @Validated ReportSearchValidate searchValidate) {
        return AjaxResult.success(iReportService.courseList(pageValidate, searchValidate));
    }

    @Log(title = "导出课程报表")
    @GetMapping(value = "/course/export")
    @ApiOperation(value = "导出课程报表（xlsx）")
    public AjaxResult<Object> courseExport(@Validated ReportSearchValidate searchValidate) {
        return exportResult(iReportService.courseExport(searchValidate));
    }

    // ------------------------------------------------------------------
    // 教练月营业额（#102）
    // ------------------------------------------------------------------

    @GetMapping(value = "/course/coach_month/lists")
    @ApiOperation(value = "教练月营业额列表（自然月：上课数/服务人次/总营业额）")
    public AjaxResult<PageResult<Map<String, Object>>> coachMonthList(
            @Validated PageValidate pageValidate,
            @Validated ReportSearchValidate searchValidate) {
        return AjaxResult.success(iReportService.coachMonthList(pageValidate, searchValidate));
    }

    @Log(title = "导出教练月营业额")
    @GetMapping(value = "/course/coach_month/export")
    @ApiOperation(value = "导出教练月营业额（xlsx）")
    public AjaxResult<Object> coachMonthExport(@Validated ReportSearchValidate searchValidate) {
        return exportResult(iReportService.coachMonthExport(searchValidate));
    }

    @GetMapping(value = "/course/coach_month/detail")
    @ApiOperation(value = "教练月营业额下钻（该教练当月课节/订单明细）")
    public AjaxResult<Map<String, Object>> coachMonthDetail(@Validated ReportSearchValidate searchValidate) {
        return AjaxResult.success(iReportService.coachMonthDetail(searchValidate));
    }

    // ------------------------------------------------------------------
    // 核销报表
    // ------------------------------------------------------------------

    @GetMapping(value = "/checkin/lists")
    @ApiOperation(value = "核销报表列表")
    public AjaxResult<PageResult<Map<String, Object>>> checkinList(
            @Validated PageValidate pageValidate,
            @Validated ReportSearchValidate searchValidate) {
        return AjaxResult.success(iReportService.checkinList(pageValidate, searchValidate));
    }

    @Log(title = "导出核销报表")
    @GetMapping(value = "/checkin/export")
    @ApiOperation(value = "导出核销报表（xlsx）")
    public AjaxResult<Object> checkinExport(@Validated ReportSearchValidate searchValidate) {
        return exportResult(iReportService.checkinExport(searchValidate));
    }

    // ------------------------------------------------------------------
    // 券流水报表
    // ------------------------------------------------------------------

    @GetMapping(value = "/coupon/lists")
    @ApiOperation(value = "券流水报表列表")
    public AjaxResult<PageResult<Map<String, Object>>> couponList(
            @Validated PageValidate pageValidate,
            @Validated ReportSearchValidate searchValidate) {
        return AjaxResult.success(iReportService.couponList(pageValidate, searchValidate));
    }

    @Log(title = "导出券流水报表")
    @GetMapping(value = "/coupon/export")
    @ApiOperation(value = "导出券流水报表（xlsx）")
    public AjaxResult<Object> couponExport(@Validated ReportSearchValidate searchValidate) {
        return exportResult(iReportService.couponExport(searchValidate));
    }

    // ------------------------------------------------------------------
    // 活动报名报表
    // ------------------------------------------------------------------

    @GetMapping(value = "/activity/lists")
    @ApiOperation(value = "活动报名报表列表")
    public AjaxResult<PageResult<Map<String, Object>>> activityList(
            @Validated PageValidate pageValidate,
            @Validated ReportSearchValidate searchValidate) {
        return AjaxResult.success(iReportService.activityList(pageValidate, searchValidate));
    }

    @Log(title = "导出活动报名报表")
    @GetMapping(value = "/activity/export")
    @ApiOperation(value = "导出活动报名报表（xlsx）")
    public AjaxResult<Object> activityExport(@Validated ReportSearchValidate searchValidate) {
        return exportResult(iReportService.activityExport(searchValidate));
    }

    // ------------------------------------------------------------------
    // 开班申请报表
    // ------------------------------------------------------------------

    @GetMapping(value = "/class_apply/lists")
    @ApiOperation(value = "开班申请报表列表")
    public AjaxResult<PageResult<Map<String, Object>>> classApplyList(
            @Validated PageValidate pageValidate,
            @Validated ReportSearchValidate searchValidate) {
        return AjaxResult.success(iReportService.classApplyList(pageValidate, searchValidate));
    }

    @Log(title = "导出开班申请报表")
    @GetMapping(value = "/class_apply/export")
    @ApiOperation(value = "导出开班申请报表（xlsx）")
    public AjaxResult<Object> classApplyExport(@Validated ReportSearchValidate searchValidate) {
        return exportResult(iReportService.classApplyExport(searchValidate));
    }

    /**
     * code=2：前端 request 拦截器会打开 data.url 下载；与现有缴费记录/分账导出保持一致。
     */
    private AjaxResult<Object> exportResult(Map<String, Object> data) {
        return AjaxResult.success(2, data, ErrorEnum.SHOW_MSG.getCode());
    }
}
