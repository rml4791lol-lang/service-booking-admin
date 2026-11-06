package com.mdd.front.service;

import java.util.List;
import java.util.Map;

/**
 * 小程序预约与上课记录（#25）。
 *
 * <p>口径：</p>
 * <ul>
 *   <li>预约只占名额、不扣券：创建时写 {@code la_course_booking.status=1}，
 *       不写消课流水、不扣任何券（券模型属 #24）；</li>
 *   <li>服务端校验名额、报名截止、重复预约、已满/已结束/无效场次，错误用
 *       {@code OperateException} 返回明确文案；</li>
 *   <li>我的预约/上课记录按登录家长账号隔离；记录详情（含课堂照片）只给本人，
 *       其他账号返回 {@code null}；</li>
 *   <li>字段口径对齐 {@code services/db.js} 的 mock 契约：
 *       {@code getBookings/getNextBooking/createBooking/getRecords/getRecord}。</li>
 * </ul>
 */
public interface IBookingService {

    /**
     * 创建预约（只占名额不扣券）。
     *
     * @param userId    登录家长
     * @param sessionId 场次 ID（{@code ys_course_lesson.id}）
     * @param studentId 学员 ID（必须是当前家长名下有效学员）
     * @param couponId  前端兼容字段：当前不校验、不扣减
     * @return {@code {ok:true, booking:{...}, couponDeducted:false}}
     */
    Map<String, Object> createBooking(Integer userId, Integer sessionId, Integer studentId, String couponId);

    /**
     * 我的预约：当前账号下状态为“已预约”（未上课/未取消）的记录。
     *
     * @param studentId 可选，按学员筛选（非本人学员返回空列表）
     * @param pageNo    页码，从 1 开始
     * @param pageSize  每页条数，默认 20，最大 50
     */
    List<Map<String, Object>> bookings(Integer userId, Integer studentId, Integer pageNo, Integer pageSize);

    /**
     * 下一节课：最近一场今天或以后、未上课且场次未取消的预约，没有时返回 {@code null}。
     */
    Map<String, Object> nextBooking(Integer userId);

    /**
     * 上课记录：当前账号下已上课（签到，{@code status=10}）的记录，含课堂照片与教练点评。
     *
     * @param studentId 可选，按学员筛选（非本人学员返回空列表）
     */
    List<Map<String, Object>> records(Integer userId, Integer studentId, Integer pageNo, Integer pageSize);

    /**
     * 记录详情：只返回当前账号自己的记录，其他账号/不存在统一返回 {@code null}。
     */
    Map<String, Object> record(Integer userId, String recordId);
}
