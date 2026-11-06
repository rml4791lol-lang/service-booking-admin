package com.mdd.front.service;

import java.util.List;
import java.util.Map;

/**
 * 小程序课程/教练公开只读目录（#22）。
 *
 * <p>数据源：{@code la_course_class}（课程模板）、{@code ys_course_lesson}（场次）、
 * {@code la_course_booking}（预约/余位）、{@code la_instructor}（教练）、
 * {@code la_sports_category}（运动类目）。字段口径对齐 {@code services/db.js} 的 mock 契约。</p>
 */
public interface ICourseCatalogService {

    /**
     * 课程列表。支持小区/社区/街道/分区/类目/年龄/关键字筛选与分页。
     */
    List<Map<String, Object>> courses(Integer areaId, Integer communityId, Integer streetId, Integer districtId,
                                      Integer categoryId, Integer age, String keyword,
                                      Integer pageNo, Integer pageSize, String lat, String lng);

    /**
     * 课程详情：课程模板 + 教练 + 场地 + 可约场次 + 同小区相关课程。
     */
    Map<String, Object> courseDetail(Integer id);

    /**
     * 某课程的全部场次（含历史/已结课），按日期与时间排序。
     */
    List<Map<String, Object>> sessions(Integer courseId);

    /**
     * 首页快捷场次：未开始、今天及以后、仍有剩余名额，按日期时间升序。
     */
    List<Map<String, Object>> quickSessions(Integer limit);

    /**
     * 单场次详情。
     */
    Map<String, Object> session(Integer lessonId);

    /**
     * 教练列表（公开资料），支持小区/类目/关键字筛选。
     */
    List<Map<String, Object>> coaches(Integer areaId, Integer categoryId, String keyword);

    /**
     * 教练详情：公开资料 + 可授课程。
     */
    Map<String, Object> coachDetail(Integer id);
}
