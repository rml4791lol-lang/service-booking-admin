package com.mdd.front.service;

import java.util.List;
import java.util.Map;

/**
 * 小程序地区五级只读：城市 -> 分区 -> 街道 -> 社区 -> 小区。
 *
 * 数据全部来自后台维护的 la_region / la_community / la_area / la_course_class，
 * 字段口径与小程序 services/db.js 的 mock 契约一致。
 */
public interface IRegionService {

    /**
     * 城市列表（level=1）。
     */
    List<Map<String, Object>> cities();

    /**
     * 分区列表（level=2），可按城市过滤。
     */
    List<Map<String, Object>> districts(Integer cityId);

    /**
     * 街道列表（level=3），可按分区过滤；areaCount 只统计可见小区。
     */
    List<Map<String, Object>> streets(Integer districtId);

    /**
     * 社区列表（la_community），可按街道过滤。
     */
    List<Map<String, Object>> communities(Integer streetId);

    /**
     * 小区列表。
     *
     * @param onlyLive    true 时只返回“有已上架课程且有可约场次”的小区
     * @param districtId  分区筛选
     * @param streetId    街道筛选
     * @param communityId 社区筛选
     * @param keyword     小区名称关键字
     */
    List<Map<String, Object>> areas(Boolean onlyLive, Integer districtId, Integer streetId,
                                    Integer communityId, String keyword);

    /**
     * 小区详情：小区基础信息 + 在售课程 + 授课教练。
     */
    Map<String, Object> areaDetail(Integer id);
}
