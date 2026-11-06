package com.mdd.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.activity.Activity;
import com.mdd.common.entity.activity.ActivityBooking;
import com.mdd.common.entity.activity.ActivitySignup;
import com.mdd.common.entity.course.CourseRefund;
import com.mdd.common.entity.user.User;
import com.mdd.common.enums.PaymentEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.activity.ActivityBookingMapper;
import com.mdd.common.mapper.activity.ActivityMapper;
import com.mdd.common.mapper.activity.ActivitySignupMapper;
import com.mdd.common.mapper.course.CourseRefundMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 活动报名取消（审计 #213 A2/C3）。
 *
 * <p>家长端与后台共用同一套取消/退款申请口径：</p>
 * <ul>
 *   <li><b>活动开始后不可取消</b>：以 {@code activity_date} 与今天比较，早于今天即已开始；</li>
 *   <li><b>免费报名</b>：报名行置已取消（3），批次置已取消（3），按批次整批释放
 *       {@code student_count + companion_count} 个名额；重复调用不再释放；</li>
 *   <li><b>付费报名</b>：只写一条待审核的 {@code la_course_refund}（{@code course_class_id=0}，
 *       {@code class_name='活动退款-...'}），报名保持已支付、名额保持占用，审核通过后由
 *       {@code CourseRefundServiceImpl} 走既有退款通道并调用本服务释放名额；</li>
 *   <li>同一订单至多一条待审核退款申请；重复取消返回 {@code refunding}，不重复写单。</li>
 * </ul>
 */
@Service
public class ActivitySignupCancelService {

    private static final Logger log = LoggerFactory.getLogger(ActivitySignupCancelService.class);

    public static final String MODE_CANCELLED = "cancelled";
    public static final String MODE_REFUNDING = "refunding";

    private static final int REFUND_PENDING = 0;
    private static final int STATUS_CANCELLED = ActivitySignup.STATUS_CANCELLED;
    private static final int STATUS_PAID = ActivitySignup.STATUS_PAID;
    private static final int CLASS_NAME_MAX = 100;
    private static final int REASON_MAX = 50;
    private static final int DETAIL_MAX = 500;

    @Resource
    ActivityMapper activityMapper;
    @Resource
    ActivitySignupMapper activitySignupMapper;
    @Resource
    ActivityBookingMapper activityBookingMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    CourseRefundMapper courseRefundMapper;
    @Resource
    UserMapper userMapper;

    /** 活动是否已开始（activity_date 早于今天；日期缺失视为未开始，与报名窗口口径一致）。 */
    public boolean isStarted(Activity activity) {
        if (activity == null || StringUtils.isEmpty(activity.getActivityDate())) {
            return false;
        }
        try {
            return LocalDate.parse(activity.getActivityDate().trim()).isBefore(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    /** 当前家长在该活动是否有待审核退款申请。 */
    public boolean isRefunding(Integer userId, Integer activityId) {
        if (userId == null || userId <= 0 || activityId == null || activityId <= 0) {
            return false;
        }
        List<RechargeOrder> orders = rechargeOrderMapper.selectList(new QueryWrapper<RechargeOrder>()
                .select("id")
                .eq("user_id", userId)
                .eq("activity_id", activityId)
                .eq("pay_status", PaymentEnum.OK_PAID.getCode())
                .isNull("delete_time"));
        if (orders.isEmpty()) {
            return false;
        }
        List<Integer> ids = new ArrayList<>();
        for (RechargeOrder order : orders) {
            ids.add(order.getId());
        }
        Long pending = courseRefundMapper.selectCount(new QueryWrapper<CourseRefund>()
                .eq("status", REFUND_PENDING)
                .isNull("delete_time")
                .in("order_id", ids));
        return pending != null && pending > 0;
    }

    /** 家长取消当前活动报名：免费直接取消，付费生成退款申请（幂等）。 */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> cancelForUser(Integer userId, Integer activityId) {
        if (userId == null || userId <= 0) {
            throw new OperateException("请先登录");
        }
        if (activityId == null || activityId <= 0) {
            throw new OperateException("活动不存在");
        }
        Activity activity = activityMapper.selectOne(new QueryWrapper<Activity>()
                .eq("id", activityId)
                .eq("status", 1)
                .isNull("delete_time")
                .last("limit 1"));
        if (activity == null) {
            throw new OperateException("活动不存在或已下架");
        }
        if (isStarted(activity)) {
            throw new OperateException("活动已开始，不可取消报名");
        }

        List<ActivitySignup> active = activitySignupMapper.selectList(new QueryWrapper<ActivitySignup>()
                .eq("user_id", userId)
                .eq("activity_id", activityId)
                .eq("status", STATUS_PAID)
                .orderByAsc("id"));
        if (active.isEmpty()) {
            Long any = activitySignupMapper.selectCount(new QueryWrapper<ActivitySignup>()
                    .eq("user_id", userId)
                    .eq("activity_id", activityId));
            if (any != null && any > 0) {
                return result(activityId, MODE_CANCELLED, null, Collections.<Integer>emptyList(),
                        0, 0, true);
            }
            throw new OperateException("未找到报名记录");
        }

        Map<Integer, RechargeOrder> orders = loadOrders(active);
        List<ActivitySignup> freeRows = new ArrayList<>();
        Set<Integer> refundOrderIds = new LinkedHashSet<>();
        for (ActivitySignup signup : active) {
            int orderId = signup.getOrderId() == null ? 0 : signup.getOrderId();
            if (orderId <= 0) {
                freeRows.add(signup);
                continue;
            }
            RechargeOrder order = orders.get(orderId);
            if (order == null || order.getDeleteTime() != null
                    || !userId.equals(order.getUserId()) || !activityId.equals(order.getActivityId())) {
                throw new OperateException("报名订单不存在，请联系客服");
            }
            int refundStatus = order.getRefundStatus() == null ? 0 : order.getRefundStatus();
            BigDecimal amount = order.getOrderAmount() == null ? BigDecimal.ZERO : order.getOrderAmount();
            boolean paid = order.getPayStatus() != null && order.getPayStatus() == PaymentEnum.OK_PAID.getCode();
            if (paid && refundStatus == 0 && amount.compareTo(BigDecimal.ZERO) > 0) {
                refundOrderIds.add(orderId);
            } else if (refundStatus != 0
                    || (order.getPayStatus() != null && order.getPayStatus() == PaymentEnum.CLOSED.getCode())
                    || amount.compareTo(BigDecimal.ZERO) <= 0) {
                // 已退款/已关闭/零元订单：没有待退资金，直接取消并释放名额（数据修复口径）。
                freeRows.add(signup);
            } else {
                throw new OperateException("报名订单状态异常，请联系客服");
            }
        }

        CancelStats stats = cancelSignups(freeRows);
        List<Integer> refundIds = new ArrayList<>();
        for (Integer orderId : refundOrderIds) {
            CourseRefund refund = createRefund(orders.get(orderId), "用户取消活动报名", "");
            refundIds.add(refund.getId());
        }
        String mode = refundIds.isEmpty() ? MODE_CANCELLED : MODE_REFUNDING;
        log.info("活动报名取消: userId={} activityId={} mode={} cancelledSignups={} released={} refundIds={}",
                userId, activityId, mode, stats.signupCount, stats.released, refundIds);
        return result(activityId, mode, refundIds.isEmpty() ? null : refundIds.get(0), refundIds,
                stats.signupCount, stats.released, false);
    }

    /** 后台按报名行取消；同一批次/订单整组处理，规则与家长端一致。 */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> cancelForSignup(Integer signupId, String remark) {
        if (signupId == null || signupId <= 0) {
            throw new OperateException("报名记录不存在");
        }
        ActivitySignup signup = activitySignupMapper.selectById(signupId);
        if (signup == null) {
            throw new OperateException("报名记录不存在");
        }
        Activity activity = activityMapper.selectById(signup.getActivityId());
        if (activity == null || activity.getDeleteTime() != null) {
            throw new OperateException("活动不存在");
        }
        if (isStarted(activity)) {
            throw new OperateException("活动已开始，不可取消报名");
        }
        if (signup.getStatus() != null && signup.getStatus() == STATUS_CANCELLED) {
            return result(signup.getActivityId(), MODE_CANCELLED, null, Collections.<Integer>emptyList(),
                    0, 0, true);
        }
        if (signup.getStatus() == null || signup.getStatus() != STATUS_PAID) {
            throw new OperateException("只有已报名状态可以取消");
        }

        List<ActivitySignup> group = signupGroup(signup);
        int orderId = signup.getOrderId() == null ? 0 : signup.getOrderId();
        int cancelledSignups;
        int released;
        int refundId = 0;
        String mode;
        if (orderId <= 0) {
            CancelStats stats = cancelSignups(group);
            cancelledSignups = stats.signupCount;
            released = stats.released;
            mode = MODE_CANCELLED;
        } else {
            RechargeOrder order = rechargeOrderMapper.selectById(orderId);
            if (order == null || order.getDeleteTime() != null
                    || !signup.getUserId().equals(order.getUserId())
                    || !signup.getActivityId().equals(order.getActivityId())) {
                throw new OperateException("报名订单不存在，请联系客服");
            }
            int refundStatus = order.getRefundStatus() == null ? 0 : order.getRefundStatus();
            BigDecimal amount = order.getOrderAmount() == null ? BigDecimal.ZERO : order.getOrderAmount();
            boolean paid = order.getPayStatus() != null && order.getPayStatus() == PaymentEnum.OK_PAID.getCode();
            if (paid && refundStatus == 0 && amount.compareTo(BigDecimal.ZERO) > 0) {
                CourseRefund refund = createRefund(order, "后台取消活动报名", trim(remark, DETAIL_MAX));
                refundId = refund.getId();
                cancelledSignups = 0;
                released = 0;
                mode = MODE_REFUNDING;
            } else if (refundStatus != 0
                    || (order.getPayStatus() != null && order.getPayStatus() == PaymentEnum.CLOSED.getCode())
                    || amount.compareTo(BigDecimal.ZERO) <= 0) {
                CancelStats stats = cancelSignups(group);
                cancelledSignups = stats.signupCount;
                released = stats.released;
                mode = MODE_CANCELLED;
            } else {
                throw new OperateException("报名订单状态异常，请联系客服");
            }
        }
        log.info("后台活动报名取消: signupId={} activityId={} mode={} cancelledSignups={} released={} refundId={}",
                signupId, signup.getActivityId(), mode, cancelledSignups, released, refundId);
        return result(signup.getActivityId(), mode, refundId > 0 ? refundId : null,
                refundId > 0 ? Collections.singletonList(refundId) : Collections.<Integer>emptyList(),
                cancelledSignups, released, false);
    }

    /**
     * 活动退款审核通过后的履约回滚：取消该订单全部报名行、批次置取消并按批次释放名额。
     * 幂等：批次状态是从非取消抢到取消的唯一闸门，重复调用不会二次释放。
     */
    @Transactional(rollbackFor = Exception.class)
    public int cancelOrderSignups(Integer orderId) {
        if (orderId == null || orderId <= 0) {
            return 0;
        }
        List<ActivitySignup> rows = activitySignupMapper.selectList(new QueryWrapper<ActivitySignup>()
                .eq("order_id", orderId)
                .orderByAsc("id"));
        if (rows.isEmpty()) {
            return 0;
        }
        CancelStats stats = cancelSignups(rows);
        if (stats.released > 0) {
            log.info("活动退款审核通过释放名额: orderId={} cancelledSignups={} released={}",
                    orderId, stats.signupCount, stats.released);
        }
        return stats.released;
    }

    /**
     * 超时未支付关单释放（OrderAutoCancelJob / #214）：只释放仍待支付的报名批次与明细。
     *
     * <p>与 {@link #cancelOrderSignups(Integer)} 的区别：本方法以
     * {@code booking.status=PENDING} 为唯一释放闸门，绝不取消/释放已支付批次；
     * 订单已支付但批次仍待支付的数据不一致场景留给支付回调对账，不能在这里把名额退掉。
     * 历史数据（{@code booking_id=0}）按旧口径逐行释放 1 个名额。</p>
     *
     * @return 实际释放的名额数（学员 + 陪同）
     */
    @Transactional(rollbackFor = Exception.class)
    public int releaseUnpaidOrder(Integer orderId) {
        if (orderId == null || orderId <= 0) {
            return 0;
        }
        long now = TimeUtils.timestamp();
        List<ActivityBooking> bookings = activityBookingMapper.selectList(new QueryWrapper<ActivityBooking>()
                .eq("order_id", orderId)
                .orderByAsc("id"));
        List<ActivitySignup> signups = activitySignupMapper.selectList(new QueryWrapper<ActivitySignup>()
                .eq("order_id", orderId)
                .orderByAsc("id"));

        Map<Integer, Integer> releaseByActivity = new HashMap<>();
        Map<Integer, ActivityBooking> bookingById = new LinkedHashMap<>();
        for (ActivityBooking booking : bookings) {
            bookingById.put(booking.getId(), booking);
            if (booking.getStatus() == null || booking.getStatus() != ActivityBooking.STATUS_PENDING) {
                // 已支付/已取消批次不释放；其明细也不在这里取消。
                continue;
            }
            int claimed = activityBookingMapper.update(null, new UpdateWrapper<ActivityBooking>()
                    .set("status", ActivityBooking.STATUS_CANCELLED)
                    .set("update_time", now)
                    .eq("id", booking.getId())
                    .eq("status", ActivityBooking.STATUS_PENDING));
            if (claimed <= 0) {
                continue;
            }
            int heads = (booking.getStudentCount() == null ? 0 : booking.getStudentCount())
                    + (booking.getCompanionCount() == null ? 0 : booking.getCompanionCount());
            if (heads > 0) {
                releaseByActivity.merge(booking.getActivityId(), heads, Integer::sum);
            }
        }

        for (ActivitySignup signup : signups) {
            int bookingId = signup.getBookingId() == null ? 0 : signup.getBookingId();
            if (bookingId > 0) {
                ActivityBooking booking = bookingById.get(bookingId);
                if (booking != null) {
                    if (booking.getStatus() != null && booking.getStatus() == ActivityBooking.STATUS_PENDING) {
                        cancelPendingRow(signup, now);
                    }
                    // 批次已支付/已取消：明细保持原状，交给支付回调/退款链路。
                    continue;
                }
                // 批次行缺失的历史异常数据：退回逐行释放，至少不把名额永久卡死。
                if (cancelPendingRow(signup, now) > 0) {
                    releaseByActivity.merge(signup.getActivityId(), 1, Integer::sum);
                }
                continue;
            }
            if (cancelPendingRow(signup, now) > 0) {
                releaseByActivity.merge(signup.getActivityId(), 1, Integer::sum);
            }
        }

        int released = 0;
        for (Map.Entry<Integer, Integer> entry : releaseByActivity.entrySet()) {
            activityMapper.update(null, new UpdateWrapper<Activity>()
                    .setSql("joined = GREATEST(joined - " + entry.getValue() + ", 0)")
                    .set("update_time", now)
                    .eq("id", entry.getKey()));
            released += entry.getValue();
        }
        if (released > 0) {
            log.info("超时未支付订单释放活动名额: orderId={} released={}", orderId, released);
        }
        return released;
    }

    private int cancelPendingRow(ActivitySignup signup, long now) {
        return activitySignupMapper.update(null, new UpdateWrapper<ActivitySignup>()
                .set("status", STATUS_CANCELLED)
                .set("update_time", now)
                .eq("id", signup.getId())
                .eq("status", ActivitySignup.STATUS_PENDING));
    }

    private List<ActivitySignup> signupGroup(ActivitySignup signup) {
        int bookingId = signup.getBookingId() == null ? 0 : signup.getBookingId();
        if (bookingId > 0) {
            return activitySignupMapper.selectList(new QueryWrapper<ActivitySignup>()
                    .eq("booking_id", bookingId)
                    .orderByAsc("id"));
        }
        int orderId = signup.getOrderId() == null ? 0 : signup.getOrderId();
        if (orderId > 0) {
            return activitySignupMapper.selectList(new QueryWrapper<ActivitySignup>()
                    .eq("order_id", orderId)
                    .orderByAsc("id"));
        }
        return Collections.singletonList(signup);
    }

    private Map<Integer, RechargeOrder> loadOrders(List<ActivitySignup> signups) {
        Set<Integer> ids = new LinkedHashSet<>();
        for (ActivitySignup signup : signups) {
            if (signup.getOrderId() != null && signup.getOrderId() > 0) {
                ids.add(signup.getOrderId());
            }
        }
        Map<Integer, RechargeOrder> map = new HashMap<>();
        if (ids.isEmpty()) {
            return map;
        }
        List<RechargeOrder> orders = rechargeOrderMapper.selectBatchIds(ids);
        for (RechargeOrder order : orders) {
            map.put(order.getId(), order);
        }
        return map;
    }

    private CourseRefund createRefund(RechargeOrder order, String reason, String detail) {
        CourseRefund existing = courseRefundMapper.selectOne(new QueryWrapper<CourseRefund>()
                .eq("order_id", order.getId())
                .eq("status", REFUND_PENDING)
                .isNull("delete_time")
                .last("limit 1"));
        if (existing != null) {
            return existing;
        }
        Activity activity = activityMapper.selectById(order.getActivityId());
        String title = activity == null || StringUtils.isEmpty(activity.getTitle())
                ? "活动" : activity.getTitle().trim();
        BigDecimal amount = order.getOrderAmount() == null ? BigDecimal.ZERO : order.getOrderAmount();
        User user = userMapper.selectById(order.getUserId());
        long now = TimeUtils.timestamp();

        CourseRefund refund = new CourseRefund();
        refund.setUserId(order.getUserId());
        // 活动退款复用 la_course_refund：course_class_id=0 + 固定前缀标记来源；
        // 审核侧以 order.activity_id>0 路由到活动履约回滚（不按课程/券处理）。
        refund.setCourseClassId(0);
        refund.setOrderId(order.getId());
        refund.setClassName(trim("活动退款-" + title, CLASS_NAME_MAX));
        refund.setUserName(user != null && user.getNickname() != null ? user.getNickname() : "");
        refund.setReason(trim(reason, REASON_MAX));
        refund.setDetail(trim(detail, DETAIL_MAX));
        refund.setPayAmount(amount);
        refund.setRefundAmount(amount);
        refund.setTotalLessons(0);
        refund.setUsedLessons(0);
        refund.setStatus(REFUND_PENDING);
        refund.setCreateTime(now);
        refund.setUpdateTime(now);
        try {
            courseRefundMapper.insert(refund);
            return refund;
        } catch (DuplicateKeyException e) {
            CourseRefund concurrent = courseRefundMapper.selectOne(new QueryWrapper<CourseRefund>()
                    .eq("order_id", order.getId())
                    .eq("status", REFUND_PENDING)
                    .isNull("delete_time")
                    .last("limit 1"));
            if (concurrent != null) {
                return concurrent;
            }
            throw new OperateException("退款申请正在处理，请稍后重试");
        }
    }

    /**
     * 取消报名行并释放名额。批次状态是释放闸门：只有把批次从非取消抢到取消的调用
     * 才计入释放，重复取消/退款与超时释放并发时不会二次扣减 joined。
     */
    private CancelStats cancelSignups(List<ActivitySignup> rows) {
        CancelStats stats = new CancelStats();
        if (rows == null || rows.isEmpty()) {
            return stats;
        }
        long now = TimeUtils.timestamp();
        Map<Integer, List<ActivitySignup>> batches = new LinkedHashMap<>();
        List<ActivitySignup> legacy = new ArrayList<>();
        for (ActivitySignup signup : rows) {
            int bookingId = signup.getBookingId() == null ? 0 : signup.getBookingId();
            if (bookingId > 0) {
                batches.computeIfAbsent(bookingId, key -> new ArrayList<>()).add(signup);
            } else {
                legacy.add(signup);
            }
        }
        Map<Integer, Integer> releaseByActivity = new HashMap<>();
        for (Map.Entry<Integer, List<ActivitySignup>> entry : batches.entrySet()) {
            ActivityBooking booking = activityBookingMapper.selectById(entry.getKey());
            if (booking == null) {
                legacy.addAll(entry.getValue());
                continue;
            }
            int claimed = activityBookingMapper.update(null, new UpdateWrapper<ActivityBooking>()
                    .set("status", ActivityBooking.STATUS_CANCELLED)
                    .set("update_time", now)
                    .eq("id", booking.getId())
                    .ne("status", ActivityBooking.STATUS_CANCELLED));
            if (claimed > 0) {
                int heads = (booking.getStudentCount() == null ? 0 : booking.getStudentCount())
                        + (booking.getCompanionCount() == null ? 0 : booking.getCompanionCount());
                if (heads > 0) {
                    releaseByActivity.merge(booking.getActivityId(), heads, Integer::sum);
                }
            }
            for (ActivitySignup signup : entry.getValue()) {
                stats.signupCount += cancelRow(signup, now);
            }
        }
        for (ActivitySignup signup : legacy) {
            if (cancelRow(signup, now) > 0) {
                stats.signupCount += 1;
                releaseByActivity.merge(signup.getActivityId(), 1, Integer::sum);
            }
        }
        for (Map.Entry<Integer, Integer> entry : releaseByActivity.entrySet()) {
            activityMapper.update(null, new UpdateWrapper<Activity>()
                    .setSql("joined = GREATEST(joined - " + entry.getValue() + ", 0)")
                    .set("update_time", now)
                    .eq("id", entry.getKey()));
            stats.released += entry.getValue();
        }
        return stats;
    }

    private int cancelRow(ActivitySignup signup, long now) {
        if (signup.getStatus() != null && signup.getStatus() == STATUS_CANCELLED) {
            return 0;
        }
        return activitySignupMapper.update(null, new UpdateWrapper<ActivitySignup>()
                .set("status", STATUS_CANCELLED)
                .set("update_time", now)
                .eq("id", signup.getId())
                .ne("status", STATUS_CANCELLED));
    }

    private Map<String, Object> result(Integer activityId, String mode, Integer refundId,
                                       List<Integer> refundIds, int cancelledSignups,
                                       int released, boolean already) {
        Activity activity = activityMapper.selectById(activityId);
        int capacity = activity == null || activity.getCapacity() == null ? 0 : activity.getCapacity();
        int joined = activity == null || activity.getJoined() == null ? 0 : activity.getJoined();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("activityId", activityId);
        result.put("mode", mode);
        result.put("refundId", refundId);
        result.put("refundIds", refundIds);
        result.put("cancelledSignups", cancelledSignups);
        result.put("released", released);
        result.put("remaining", Math.max(capacity - joined, 0));
        result.put("already", already);
        return result;
    }

    private String trim(String text, int max) {
        String value = text == null ? "" : text.trim();
        return value.length() <= max ? value : value.substring(0, max);
    }

    private static class CancelStats {
        int signupCount;
        int released;
    }
}
