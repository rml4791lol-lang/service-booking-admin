package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.report.ReportSearchValidate;
import com.mdd.common.core.PageResult;

import java.util.Map;

/**
 * 后台数据报表（T30.5 / #40）：课程、核销、券流水、活动报名、开班申请、教练月营业额（#102）。
 *
 * <p>列表与导出复用同一份查询/口径实现：列表分页展示，导出输出同一条件下
 * 的全量行，保证页面与 Excel 行数、金额一致。</p>
 */
public interface IReportService {

    PageResult<Map<String, Object>> courseList(PageValidate pageValidate, ReportSearchValidate searchValidate);

    Map<String, Object> courseExport(ReportSearchValidate searchValidate);

    // 教练月营业额（#102）：按自然月统计教练维度营业额与工作量，可下钻课节/订单明细。
    PageResult<Map<String, Object>> coachMonthList(PageValidate pageValidate, ReportSearchValidate searchValidate);

    Map<String, Object> coachMonthExport(ReportSearchValidate searchValidate);

    Map<String, Object> coachMonthDetail(ReportSearchValidate searchValidate);

    PageResult<Map<String, Object>> checkinList(PageValidate pageValidate, ReportSearchValidate searchValidate);

    Map<String, Object> checkinExport(ReportSearchValidate searchValidate);

    PageResult<Map<String, Object>> couponList(PageValidate pageValidate, ReportSearchValidate searchValidate);

    Map<String, Object> couponExport(ReportSearchValidate searchValidate);

    PageResult<Map<String, Object>> activityList(PageValidate pageValidate, ReportSearchValidate searchValidate);

    Map<String, Object> activityExport(ReportSearchValidate searchValidate);

    PageResult<Map<String, Object>> classApplyList(PageValidate pageValidate, ReportSearchValidate searchValidate);

    Map<String, Object> classApplyExport(ReportSearchValidate searchValidate);
}
