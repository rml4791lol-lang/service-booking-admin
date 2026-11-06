package com.mdd.front.service;

import java.util.List;
import java.util.Map;

/**
 * 教练核销闭环（T29.6 / #26）。
 *
 * <p>数据范围只按当前登录教练（拦截器注入的 {@code la_instructor.id}）过滤：
 * 场次归属 = {@code ys_course_lesson.coach_id}；为空时回退
 * {@code la_course_class.instructor_id}（与 #22 场次教练口径一致）。</p>
 */
public interface ICoachCheckinService {

    /**
     * 我负责的场次列表。
     *
     * @param date     可选，yyyy-MM-dd；传了就只查当天
     * @param pageNo   默认 1
     * @param pageSize 默认 50，最大 100
     */
    List<Map<String, Object>> sessions(Integer instructorId, String date, Integer pageNo, Integer pageSize);

    /**
     * 场次详情（含预约名单、核销结果）；不存在或非本人场次返回 null。
     */
    Map<String, Object> session(Integer instructorId, Integer sessionId);

    /**
     * 点名名单：该场次全部有效预约（status&lt;&gt;3），非本人场次返回空数组。
     */
    List<Map<String, Object>> roster(Integer instructorId, Integer sessionId);

    /**
     * 提交核销。
     *
     * <p>校验顺序：场次存在 → 本人负责 → 未取消/未核销 → 至少一名到场 → 至少一张照片
     * → 到场学员都在预约名单内。通过后逐人扣券（无券不阻断，见返回 warnings）、
     * 预约置已上课、写消课流水与课后记录、场次置已完成。</p>
     */
    Map<String, Object> submit(Integer instructorId, Integer sessionId, List<Integer> attendedStudentIds,
                               List<String> photos, String comment);

    /**
     * 我的上课记录（已核销场次的到场学员，每人一条），日期倒序。
     */
    List<Map<String, Object>> records(Integer instructorId, Integer pageNo, Integer pageSize);
}
