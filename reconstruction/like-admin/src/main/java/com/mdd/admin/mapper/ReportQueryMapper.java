package com.mdd.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 报表 SQL 聚合/分页查询（#66）。
 *
 * <p>列表、计数与导出共用同一套 XML 口径，只是 LIMIT 不同：
 * 列表只取当前页，导出按批次取页，避免把全量数据读进内存。
 * 统计汇总同样由数据库聚合返回，不再通过遍历全量行计算。</p>
 */
@Mapper
public interface ReportQueryMapper {

    // 课程报表（真实收益口径的 SQL 聚合）
    long countCourse(@Param("q") ReportQuery query);

    List<Map<String, Object>> pageCourse(@Param("q") ReportQuery query);

    Map<String, Object> summaryCourse(@Param("q") ReportQuery query);

    // 教练月营业额（#102）
    long countCoachMonth(@Param("q") ReportQuery query);

    List<Map<String, Object>> pageCoachMonth(@Param("q") ReportQuery query);

    Map<String, Object> summaryCoachMonth(@Param("q") ReportQuery query);

    Map<String, Object> summaryCoachMonthCoupon(@Param("q") ReportQuery query);

    List<Map<String, Object>> pageCoachMonthLessons(@Param("q") ReportQuery query);

    List<Map<String, Object>> pageCoachMonthOrders(@Param("q") ReportQuery query);

    // 核销报表
    long countCheckin(@Param("q") ReportQuery query);

    List<Integer> pageCheckinIds(@Param("q") ReportQuery query);

    Map<String, Object> summaryCheckin(@Param("q") ReportQuery query);

    // 券流水报表
    long countCoupon(@Param("q") ReportQuery query);

    List<Integer> pageCouponIds(@Param("q") ReportQuery query);

    Map<String, Object> summaryCoupon(@Param("q") ReportQuery query);

    // 活动报名报表
    long countActivity(@Param("q") ReportQuery query);

    List<Integer> pageActivityIds(@Param("q") ReportQuery query);

    Map<String, Object> summaryActivity(@Param("q") ReportQuery query);

    // 开班申请报表
    long countClassApply(@Param("q") ReportQuery query);

    List<Integer> pageClassApplyIds(@Param("q") ReportQuery query);

    Map<String, Object> summaryClassApply(@Param("q") ReportQuery query);
}
