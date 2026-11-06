package com.mdd.admin.service;

import com.mdd.admin.validate.checkin.CourseCheckinAuditSearchValidate;
import com.mdd.admin.validate.checkin.CourseCheckinManualValidate;
import com.mdd.admin.validate.checkin.CourseCheckinSearchValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.common.core.PageResult;

import java.util.Map;

/**
 * 后台待核销与人工补核销（T30.4 / #37）。
 */
public interface ICourseCheckinService {

    /** 待核销场次列表：社区/课程/日期/状态筛选。 */
    PageResult<Map<String, Object>> list(PageValidate pageValidate, CourseCheckinSearchValidate searchValidate);

    /** 场次详情：场次信息、学员名单、券可用预览、审计记录。 */
    Map<String, Object> detail(Integer sessionId);

    /** 页面筛选下拉：课程 + 社区。 */
    Map<String, Object> options();

    /** 人工补核销：原因必填、逐人扣券/考勤、幂等、写审计。 */
    Map<String, Object> manual(Integer adminId, CourseCheckinManualValidate manualValidate);

    /** 补核销审计查询：按场次/学员/操作日期筛选。 */
    PageResult<Map<String, Object>> audits(PageValidate pageValidate,
                                           CourseCheckinAuditSearchValidate searchValidate);
}
