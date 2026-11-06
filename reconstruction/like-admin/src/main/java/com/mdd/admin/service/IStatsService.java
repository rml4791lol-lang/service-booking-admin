package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.stats.StatsSearchValidate;
import com.mdd.common.core.PageResult;

import java.util.Map;

/**
 * 数据统计（T36 #106，统计链公共骨架）。
 *
 * <p>一级：5 类对象数量 + 期间订单/营业额总览 + 环比；二级：营业额按日趋势；
 * 教练维度直接复用 #102 的教练月营业额实现（同一份 SQL，不复制第二份口径）；
 * 三级：订单/课节明细下钻，供 #107 社区 / #108 小区 / #109 老板视角复用。</p>
 *
 * <p>只展示真实营业额与工作量：不查会话访问统计，不做二次分配计算。</p>
 */
public interface IStatsService {

    /** 一级总览：对象存量数量 + 期间订单/营业额/上课/人次 + 环比。 */
    Map<String, Object> overview(StatsSearchValidate searchValidate);

    /** 二级：营业额按日序列（默认近 30 天，可传 startDate/endDate 或 month）。 */
    Map<String, Object> trend(StatsSearchValidate searchValidate);

    /** 教练维度列表（复用 #102 教练月营业额：订单数/上课数/服务人次/总营业额）。 */
    PageResult<Map<String, Object>> instructorList(PageValidate pageValidate,
                                                   StatsSearchValidate searchValidate);

    /** 教练维度导出（复用 #102 导出：EasyExcel + code=2 URL）。 */
    Map<String, Object> instructorExport(StatsSearchValidate searchValidate);

    /** 教练维度下钻（该教练区间内课节 + 订单明细）。 */
    Map<String, Object> instructorDetail(StatsSearchValidate searchValidate);

    /**
     * 社区维度列表（#107）：社区 / 订单数 / 客流（到场人次）/ 营业额。
     *
     * <p>分组键 {@code COALESCE(NULLIF(class.community_id,0), area.community_id, 0)}，
     * 归 0 的行展示为「未归属」；下钻复用 {@code stats/order/lists}、{@code stats/lesson/lists}。</p>
     */
    PageResult<Map<String, Object>> communityList(PageValidate pageValidate,
                                                 StatsSearchValidate searchValidate);

    /** 社区维度导出（EasyExcel + code=2 URL，与列表同一份聚合）。 */
    Map<String, Object> communityExport(StatsSearchValidate searchValidate);

    /**
     * 小区维度列表（#108）：小区 / 订单数 / 客流（到场人次）/ 营业额。
     *
     * <p>分组键 {@code COALESCE(class.area_id, 0)}，归 0 的行展示为「未归属」；
     * 与社区维度同构，下钻复用 {@code stats/order/lists}、{@code stats/lesson/lists} 的 {@code areaId}。</p>
     */
    PageResult<Map<String, Object>> areaList(PageValidate pageValidate,
                                            StatsSearchValidate searchValidate);

    /** 小区维度导出（EasyExcel + code=2 URL，与列表同一份聚合）。 */
    Map<String, Object> areaExport(StatsSearchValidate searchValidate);

    /** 三级：订单明细（可按教练/社区/小区/课程包筛选；#107/#108/#109 复用）。 */
    PageResult<Map<String, Object>> orderList(PageValidate pageValidate,
                                             StatsSearchValidate searchValidate);

    /** 三级：课节明细（消课流水聚合到 班级+课节）。 */
    PageResult<Map<String, Object>> lessonList(PageValidate pageValidate,
                                              StatsSearchValidate searchValidate);

    // ------------------------------------------------------------------
    // 老板视角（#109）：4 个查询 + 各自导出（导出与查询同 URI，export=1）
    // ------------------------------------------------------------------

    /** 片区覆盖：片区（level=2 分区）/ 覆盖社区数 / 订单数 / 营业额；社区按街道上卷，含「未归属」行。 */
    Map<String, Object> ownerRegion(StatsSearchValidate searchValidate);

    /** 片区覆盖导出（EasyExcel + code=2 URL，与查询同一份聚合、全量行）。 */
    Map<String, Object> ownerRegionExport(StatsSearchValidate searchValidate);

    /** 社区效果排行：营业额 DESC、并列看订单数；limit 默认 20（0=全部），orderBy 走白名单。 */
    Map<String, Object> ownerCommunityTop(StatsSearchValidate searchValidate);

    /** 社区效果排行导出（全量行）。 */
    Map<String, Object> ownerCommunityTopExport(StatsSearchValidate searchValidate);

    /** 教练效果排行：复用 #102 教练口径 + 「未分配教练」残差行（订单无有效班级时显式成行）。 */
    Map<String, Object> ownerInstructorTop(StatsSearchValidate searchValidate);

    /** 教练效果排行导出（全量行）。 */
    Map<String, Object> ownerInstructorTopExport(StatsSearchValidate searchValidate);

    /** 课程构成（上了什么课）：课程班级 / 类目 / 订单数 / 营业额，含「未归属」行。 */
    Map<String, Object> ownerCourseMix(StatsSearchValidate searchValidate);

    /** 课程构成导出（全量行）。 */
    Map<String, Object> ownerCourseMixExport(StatsSearchValidate searchValidate);

    /** 课程包销量：课程包 / 购买数 / 金额 / 报名数（列出全部有购买或订单的包，不硬编码包数量）。 */
    PageResult<Map<String, Object>> coursePackList(PageValidate pageValidate,
                                                   StatsSearchValidate searchValidate);

    /** 课程包销量导出（EasyExcel + code=2 URL）。 */
    Map<String, Object> coursePackExport(StatsSearchValidate searchValidate);
}
