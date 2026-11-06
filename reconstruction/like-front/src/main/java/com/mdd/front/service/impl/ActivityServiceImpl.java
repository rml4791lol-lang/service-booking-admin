package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.activity.Activity;
import com.mdd.common.entity.activity.ActivityBooking;
import com.mdd.common.entity.activity.ActivityEnrollAlert;
import com.mdd.common.entity.activity.ActivitySignup;
import com.mdd.common.entity.region.Area;
import com.mdd.common.entity.student.Student;
import com.mdd.common.enums.AccountLogEnum;
import com.mdd.common.enums.PaymentEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.activity.ActivityEnrollAlertMapper;
import com.mdd.common.mapper.activity.ActivityBookingMapper;
import com.mdd.common.mapper.activity.ActivityMapper;
import com.mdd.common.mapper.activity.ActivitySignupMapper;
import com.mdd.common.mapper.log.UserAccountLogMapper;
import com.mdd.common.mapper.region.AreaMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.service.ActivitySignupCancelService;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.front.service.IActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 活动报名实现（T29.7 / #27）。
 *
 * <p>核心口径：</p>
 * <ul>
 *   <li><b>按总人头计费</b>：订单金额 = 活动 fee ×（参与学员人数 + 陪同人数），
 *       学员一人一行 {@code la_activity_signup}，一次报名一行
 *       {@code la_activity_booking}（#186）；</li>
 *   <li><b>占名额</b>：下单时用 {@code UPDATE ... joined + n <= capacity} 原子预占
 *       总人头，并发下不会超卖；未支付订单 30 分钟后按批次整批惰性释放；</li>
 *   <li><b>支付回调为准</b>：mock 回调与真实微信回调共用
 *       {@code PayService#handlePaidNotify("activity", sn)}，只有回调成功才把报名明细
 *       从待支付改为已支付；重复回调幂等；</li>
 *   <li><b>已付款但满员（#61）</b>：名额被超时释放后回调成功而活动已满时，默认策略为
 *       订单生效 + 允许超员（{@code joined > capacity}），同时生成后台待处理告警；</li>
 *   <li>同一学员同一活动只能有一条有效报名（唯一键 activity_id + student_id）。</li>
 * </ul>
 */
@Service
public class ActivityServiceImpl implements IActivityService {

    private static final Logger log = LoggerFactory.getLogger(ActivityServiceImpl.class);

    /** 未支付订单占名额的释放时限，与后台 OrderAutoCancelJob 的 30 分钟一致。 */
    private static final long HOLD_TTL_SECONDS = 1800L;
    private static final int WECHAT_PAY_DESCRIPTION_MAX_BYTES = 127;

    private static final String[] WEEK_LABELS = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private static final DateTimeFormatter SHORT_DATE = DateTimeFormatter.ofPattern("MM.dd");

    /**
     * 本地 mock 支付回调开关，与优惠券购买/ PayController 共用同一配置项。
     * 打开时 signup 返回 mockPay=true，前端继续走 /api/activity/mockNotify；
     * 生产默认 false，前端改走 /api/pay/prepay + wx.requestPayment，支付结果以回调为准。
     */
    @Value("${like.pay.mock-callback-enabled:false}")
    private boolean mockCallbackEnabled;

    @Resource
    ActivityMapper activityMapper;
    @Resource
    ActivitySignupMapper activitySignupMapper;
    @Resource
    ActivityBookingMapper activityBookingMapper;
    @Resource
    ActivityEnrollAlertMapper activityEnrollAlertMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    AreaMapper areaMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    UserAccountLogMapper userAccountLogMapper;
    @Resource
    ActivitySignupCancelService activitySignupCancelService;

    // ---- 列表 / 详情 -------------------------------------------------------

    @Override
    public List<Map<String, Object>> list(Integer userId) {
        releaseExpiredHolds(null);
        String today = LocalDate.now().toString();
        List<Activity> activities = activityMapper.selectList(new QueryWrapper<Activity>()
                .eq("status", 1)
                .isNull("delete_time")
                .ge("activity_date", today)
                .orderByAsc("activity_date")
                .orderByAsc("sort")
                .orderByAsc("id"));
        if (activities.isEmpty()) {
            return new ArrayList<>();
        }
        Set<Integer> signedIds = signedActivityIds(userId,
                activities.stream().map(Activity::getId).collect(Collectors.toList()));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Activity activity : activities) {
            result.add(activityItem(activity, signedIds.contains(activity.getId()), false, false));
        }
        return result;
    }

    @Override
    public Map<String, Object> detail(Integer userId, Integer activityId) {
        if (activityId == null || activityId <= 0) {
            return null;
        }
        releaseExpiredHolds(activityId);
        Activity activity = activityMapper.selectOne(new QueryWrapper<Activity>()
                .eq("id", activityId)
                .eq("status", 1)
                .isNull("delete_time")
                .last("limit 1"));
        if (activity == null) {
            return null;
        }
        boolean signed = signedActivityIds(userId, Collections.singletonList(activityId))
                .contains(activityId);
        boolean refunding = signed && activitySignupCancelService.isRefunding(userId, activityId);
        return activityItem(activity, signed, true, refunding);
    }

    private Set<Integer> signedActivityIds(Integer userId, List<Integer> activityIds) {
        if (userId == null || userId <= 0 || activityIds == null || activityIds.isEmpty()) {
            return new HashSet<>();
        }
        List<ActivitySignup> signups = activitySignupMapper.selectList(new QueryWrapper<ActivitySignup>()
                .eq("user_id", userId)
                .eq("status", ActivitySignup.STATUS_PAID)
                .in("activity_id", activityIds));
        Set<Integer> ids = new HashSet<>();
        for (ActivitySignup signup : signups) {
            ids.add(signup.getActivityId());
        }
        return ids;
    }

    private Map<String, Object> activityItem(Activity activity, boolean signed, boolean withAreaName,
                                             boolean refunding) {
        int capacity = activity.getCapacity() == null ? 0 : activity.getCapacity();
        int joined = activity.getJoined() == null ? 0 : activity.getJoined();
        int remaining = Math.max(capacity - joined, 0);
        String date = nvl(activity.getActivityDate());

        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", String.valueOf(activity.getId()));
        item.put("title", nvl(activity.getTitle()));
        item.put("cover", coverUrl(activity.getCover()));
        item.put("date", date);
        item.put("time", nvl(activity.getTimeSlot()));
        item.put("place", nvl(activity.getPlace()));
        item.put("areaId", activity.getAreaId() == null ? 0 : activity.getAreaId());
        item.put("fee", activity.getFee() == null ? BigDecimal.ZERO : activity.getFee());
        item.put("capacity", capacity);
        item.put("companionLimit", activity.getCompanionLimit() == null ? 0 : activity.getCompanionLimit());
        item.put("joined", joined);
        item.put("remaining", remaining);
        item.put("deadline", nvl(activity.getDeadline()));
        item.put("desc", nvl(activity.getDescription()));
        item.put("tags", splitTags(activity.getTags()));
        item.put("notice", nvl(activity.getNotice()));
        item.put("dayLabel", dayLabel(date));
        item.put("shortDate", shortDate(date));
        item.put("weekday", weekday(date));
        item.put("signed", signed);
        item.put("refunding", refunding);
        item.put("cancelable", signed && !refunding && !activitySignupCancelService.isStarted(activity));
        if (withAreaName) {
            item.put("areaName", areaName(activity.getAreaId()));
        }
        return item;
    }

    // ---- 报名下单 ----------------------------------------------------------

    @Override
    @Transactional
    public Map<String, Object> signup(Integer userId, Integer terminal, Integer activityId,
                                      List<Integer> studentIds, Integer companionCount) {
        if (userId == null || userId <= 0) {
            throw new OperateException("请先登录");
        }
        if (activityId == null || activityId <= 0) {
            throw new OperateException("活动不存在");
        }
        int companions = companionCount == null ? 0 : companionCount;
        if (companions < 0) {
            throw new OperateException("陪同人数不正确");
        }
        List<Integer> ids = normalizeStudentIds(studentIds);
        if (ids.isEmpty()) {
            throw new OperateException("请选择参与学员");
        }

        Activity activity = activityMapper.selectOne(new QueryWrapper<Activity>()
                .eq("id", activityId)
                .eq("status", 1)
                .isNull("delete_time")
                .last("limit 1"));
        if (activity == null) {
            throw new OperateException("活动不存在或已下架");
        }
        releaseExpiredHolds(activityId);
        assertOpenWindow(activity);

        int companionLimit = activity.getCompanionLimit() == null ? 0 : activity.getCompanionLimit();
        if (companions > companionLimit) {
            throw new OperateException("该活动最多可带 " + companionLimit + " 名陪同人员");
        }

        List<Student> students = requireStudents(userId, ids);
        Map<Integer, Student> studentMap = new HashMap<>();
        for (Student student : students) {
            studentMap.put(student.getId(), student);
        }

        // 已有报名：已支付/待支付都拒绝；已取消（超时释放）可复用原行重新下单。
        List<ActivitySignup> existing = activitySignupMapper.selectList(new QueryWrapper<ActivitySignup>()
                .eq("activity_id", activityId)
                .in("student_id", ids));
        Map<Integer, ActivitySignup> reuseMap = new HashMap<>();
        for (ActivitySignup signup : existing) {
            if (signup.getStatus() != null && signup.getStatus() == ActivitySignup.STATUS_CANCELLED) {
                reuseMap.put(signup.getStudentId(), signup);
                continue;
            }
            Student student = studentMap.get(signup.getStudentId());
            String name = student == null ? String.valueOf(signup.getStudentId()) : student.getName();
            throw new OperateException(signup.getStatus() != null && signup.getStatus() == ActivitySignup.STATUS_PAID
                    ? "学员 " + name + " 已报名该活动" : "学员 " + name + " 的报名正在处理中");
        }

        int count = ids.size();
        int totalHeads = count + companions;
        reserveSeats(activityId, totalHeads);
        long now = TimeUtils.timestamp();

        BigDecimal fee = activity.getFee() == null ? BigDecimal.ZERO : activity.getFee();
        BigDecimal amount = fee.multiply(BigDecimal.valueOf(totalHeads));

        // 免费活动：不生成支付订单，直接完成报名（名额已按总人头占）。
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            ActivityBooking booking = saveBooking(activityId, userId, 0, count, companions,
                    ActivityBooking.STATUS_PAID, now);
            for (Integer studentId : ids) {
                saveSignup(activityId, userId, studentId, null, booking.getId(),
                        ActivitySignup.STATUS_PAID, reuseMap.get(studentId), now);
            }
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("activityId", activityId);
            result.put("orderId", null);
            result.put("orderSn", "");
            result.put("amount", BigDecimal.ZERO);
            result.put("count", count);
            result.put("companionCount", companions);
            result.put("totalHeads", totalHeads);
            result.put("free", true);
            result.put("mockPay", false);
            result.put("remaining", remaining(activityId));
            return result;
        }

        RechargeOrder order = new RechargeOrder();
        order.setUserId(userId);
        order.setStudentId(ids.get(0));
        order.setSn(rechargeOrderMapper.randMakeOrderSn("sn"));
        order.setPayWay(PaymentEnum.WX_PAY.getCode());
        order.setPayStatus(PaymentEnum.UN_PAID.getCode());
        order.setRefundStatus(0);
        order.setOrderAmount(amount);
        order.setOrderTerminal(terminal == null ? 1 : terminal);
        order.setCourseClassId(0);
        order.setLessonCount(0);
        order.setCouponGoodsId(0);
        order.setActivityId(activityId);
        order.setActivityStudentIds(ids.stream().map(String::valueOf).collect(Collectors.joining(",")));
        order.setActivityCount(count);
        order.setCreateTime(now);
        order.setUpdateTime(now);
        rechargeOrderMapper.insert(order);

        ActivityBooking booking = saveBooking(activityId, userId, order.getId(), count, companions,
                ActivityBooking.STATUS_PENDING, now);
        for (Integer studentId : ids) {
            saveSignup(activityId, userId, studentId, order.getId(), booking.getId(),
                    ActivitySignup.STATUS_PENDING, reuseMap.get(studentId), now);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("activityId", activityId);
        result.put("orderId", order.getId());
        result.put("orderSn", order.getSn());
        result.put("amount", amount);
        result.put("count", count);
        result.put("companionCount", companions);
        result.put("totalHeads", totalHeads);
        result.put("free", false);
        result.put("mockPay", mockCallbackEnabled);
        result.put("remaining", remaining(activityId));
        return result;
    }

    @Override
    public Map<String, Object> cancel(Integer userId, Integer activityId) {
        return activitySignupCancelService.cancelForUser(userId, activityId);
    }

    private List<Integer> normalizeStudentIds(List<Integer> studentIds) {
        if (studentIds == null) {
            return new ArrayList<>();
        }
        LinkedHashSet<Integer> ids = new LinkedHashSet<>();
        for (Integer studentId : studentIds) {
            if (studentId == null || studentId <= 0) {
                throw new OperateException("参与学员参数异常");
            }
            if (!ids.add(studentId)) {
                throw new OperateException("参与学员重复");
            }
        }
        return new ArrayList<>(ids);
    }

    private List<Student> requireStudents(Integer userId, List<Integer> ids) {
        List<Student> students = studentMapper.selectList(new QueryWrapper<Student>()
                .in("id", ids)
                .eq("user_id", userId)
                .isNull("delete_time"));
        if (students.size() != ids.size()) {
            throw new OperateException("学员不存在或不属于当前账号");
        }
        Map<Integer, Student> map = new HashMap<>();
        for (Student student : students) {
            map.put(student.getId(), student);
        }
        for (Integer id : ids) {
            if (!map.containsKey(id)) {
                throw new OperateException("学员不存在或不属于当前账号");
            }
        }
        return students;
    }

    private ActivityBooking saveBooking(Integer activityId, Integer userId, Integer orderId,
                                        int studentCount, int companionCount, int status, long now) {
        ActivityBooking booking = new ActivityBooking();
        booking.setActivityId(activityId);
        booking.setUserId(userId);
        booking.setOrderId(orderId == null ? 0 : orderId);
        booking.setStudentCount(studentCount);
        booking.setCompanionCount(companionCount);
        booking.setStatus(status);
        booking.setCreateTime(now);
        booking.setUpdateTime(now);
        activityBookingMapper.insert(booking);
        return booking;
    }

    private void saveSignup(Integer activityId, Integer userId, Integer studentId, Integer orderId,
                            Integer bookingId, int status, ActivitySignup reuse, long now) {
        if (reuse != null) {
            reuse.setUserId(userId);
            reuse.setOrderId(orderId == null ? 0 : orderId);
            reuse.setBookingId(bookingId == null ? 0 : bookingId);
            reuse.setStatus(status);
            reuse.setUpdateTime(now);
            reuse.setCreateTime(now);
            activitySignupMapper.updateById(reuse);
            return;
        }
        ActivitySignup signup = new ActivitySignup();
        signup.setActivityId(activityId);
        signup.setUserId(userId);
        signup.setStudentId(studentId);
        signup.setOrderId(orderId == null ? 0 : orderId);
        signup.setBookingId(bookingId == null ? 0 : bookingId);
        signup.setStatus(status);
        signup.setCreateTime(now);
        signup.setUpdateTime(now);
        activitySignupMapper.insert(signup);
    }

    private void assertOpenWindow(Activity activity) {
        LocalDate today = LocalDate.now();
        LocalDate activityDate = parseDate(activity.getActivityDate());
        if (activityDate != null && activityDate.isBefore(today)) {
            throw new OperateException("活动已结束");
        }
        LocalDate deadline = parseDate(activity.getDeadline());
        if (deadline != null && deadline.isBefore(today)) {
            throw new OperateException("报名已截止");
        }
    }

    /**
     * 原子预占名额：只有 joined + n <= capacity 才成功，返回受影响行数。
     */
    private void reserveSeats(Integer activityId, int count) {
        if (!tryReserveSeats(activityId, count)) {
            throw new OperateException("剩余名额不足");
        }
    }

    /**
     * 尝试原子预占名额；返回 false 表示当前名额不足（不抛异常，供支付回调走超员补偿）。
     */
    private boolean tryReserveSeats(Integer activityId, int count) {
        if (count <= 0) {
            return true;
        }
        long now = TimeUtils.timestamp();
        int updated = activityMapper.update(null, new UpdateWrapper<Activity>()
                .setSql("joined = joined + " + count)
                .set("update_time", now)
                .eq("id", activityId)
                .eq("status", 1)
                .isNull("delete_time")
                .apply("joined + {0} <= capacity", count));
        return updated > 0;
    }

    /**
     * #61 超员生效：名额已满时仍把已付款的报名计入 joined（允许 joined > capacity）。
     * 只由支付回调在「名额已被释放且无法重新预占」时调用。
     *
     * <p>与预占不同，超员是兜底展示，不能把名额记到已下架/已删除的活动上，
     * 否则后台活动列表会出现幽灵占位；更新不到时返回 false，由调用方按实际原因落告警。</p>
     */
    private boolean increaseSeats(Integer activityId, int count) {
        if (count <= 0) {
            return true;
        }
        int updated = activityMapper.update(null, new UpdateWrapper<Activity>()
                .setSql("joined = joined + " + count)
                .set("update_time", TimeUtils.timestamp())
                .eq("id", activityId)
                .eq("status", 1)
                .isNull("delete_time"));
        return updated > 0;
    }

    /**
     * #61 落后台待处理告警；唯一键 order_id 保证重复回调/并发只生成一条。
     */
    private ActivityEnrollAlert createOverbookAlert(RechargeOrder order, Integer activityId,
                                                    List<ActivitySignup> signups, int released,
                                                    boolean seatsIncreased, long now) {
        ActivityEnrollAlert exists = activityEnrollAlertMapper.selectOne(new QueryWrapper<ActivityEnrollAlert>()
                .eq("order_id", order.getId())
                .last("limit 1"));
        if (exists != null) {
            return exists;
        }
        Activity activity = activityMapper.selectById(activityId);
        int capacity = activity == null || activity.getCapacity() == null ? 0 : activity.getCapacity();
        int joined = activity == null || activity.getJoined() == null ? 0 : activity.getJoined();
        // 只有真正把名额加回去时才存在「超员」；活动下架/删除时 joined 未变，
        // 告警原因必须按实际情况写，不能一律写「已满员」误导运营。
        int joinedBefore = seatsIncreased ? Math.max(joined - released, 0) : joined;
        String reason;
        boolean activityActive = activity != null && activity.getDeleteTime() == null
                && activity.getStatus() != null && activity.getStatus() == 1;
        if (seatsIncreased) {
            reason = "名额释放后支付：回调时活动已满员（容量 " + capacity + "，已占 " + joinedBefore
                    + "），订单生效并允许超员（当前已占 " + joined + "），待运营处理";
        } else if (!activityActive) {
            reason = "名额释放后支付：活动已下架或已删除，未增加名额（容量 " + capacity
                    + "，当前已占 " + joined + "），订单已支付待运营退款";
        } else {
            reason = "名额释放后支付：活动存在但名额更新失败，未增加名额（容量 " + capacity
                    + "，当前已占 " + joined + "），订单已支付待运营处理";
        }
        ActivityEnrollAlert alert = new ActivityEnrollAlert();
        alert.setActivityId(activityId);
        alert.setOrderId(order.getId());
        alert.setUserId(order.getUserId());
        alert.setOrderSn(nvl(order.getSn()));
        alert.setStudentIds(nvl(order.getActivityStudentIds()));
        alert.setStudentCount(order.getActivityCount() == null ? signups.size() : order.getActivityCount());
        alert.setAmount(order.getOrderAmount() == null ? BigDecimal.ZERO : order.getOrderAmount());
        alert.setPayWay(order.getPayWay() == null ? 0 : order.getPayWay());
        alert.setType(ActivityEnrollAlert.TYPE_OVERBOOKED_AFTER_RELEASE);
        alert.setStatus(ActivityEnrollAlert.STATUS_PENDING);
        alert.setReason(reason);
        alert.setRemark("");
        alert.setHandlerId(0);
        alert.setHandlerName("");
        alert.setHandleTime(0L);
        alert.setCreateTime(now);
        alert.setUpdateTime(now);
        try {
            activityEnrollAlertMapper.insert(alert);
            return alert;
        } catch (DuplicateKeyException e) {
            // 并发回调兜底：复用已存在的告警，不重复生成。
            return activityEnrollAlertMapper.selectOne(new QueryWrapper<ActivityEnrollAlert>()
                    .eq("order_id", order.getId())
                    .last("limit 1"));
        }
    }

    private void releaseSeats(Integer activityId, int count) {
        if (count <= 0) {
            return;
        }
        activityMapper.update(null, new UpdateWrapper<Activity>()
                .setSql("joined = GREATEST(joined - " + count + ", 0)")
                .set("update_time", TimeUtils.timestamp())
                .eq("id", activityId));
    }

    private Integer remaining(Integer activityId) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            return 0;
        }
        int capacity = activity.getCapacity() == null ? 0 : activity.getCapacity();
        int joined = activity.getJoined() == null ? 0 : activity.getJoined();
        return Math.max(capacity - joined, 0);
    }

    /**
     * 惰性释放超时未支付订单占用的名额（30 分钟）。
     *
     * <p>#186 口径：同一订单（同一报名批次）的 N 学员 + C 陪同必须整批释放一次，
     * 不能按学员行逐行释放 1，否则陪同名额会泄漏、活动长期显示已满。
     * 历史数据（{@code booking_id=0}）保持旧口径逐行释放 1。</p>
     */
    private void releaseExpiredHolds(Integer activityId) {
        try {
            long cutoff = TimeUtils.timestamp() - HOLD_TTL_SECONDS;
            QueryWrapper<ActivitySignup> query = new QueryWrapper<ActivitySignup>()
                    .eq("status", ActivitySignup.STATUS_PENDING)
                    .le("create_time", cutoff);
            if (activityId != null && activityId > 0) {
                query.eq("activity_id", activityId);
            }
            query.orderByAsc("id").last("limit 100");
            List<ActivitySignup> stale = activitySignupMapper.selectList(query);
            if (stale.isEmpty()) {
                return;
            }

            Map<Integer, List<ActivitySignup>> batches = new LinkedHashMap<>();
            List<ActivitySignup> legacy = new ArrayList<>();
            for (ActivitySignup signup : stale) {
                int bookingId = signup.getBookingId() == null ? 0 : signup.getBookingId();
                if (bookingId > 0) {
                    batches.computeIfAbsent(bookingId, key -> new ArrayList<>()).add(signup);
                } else {
                    legacy.add(signup);
                }
            }
            for (Map.Entry<Integer, List<ActivitySignup>> entry : batches.entrySet()) {
                releaseExpiredBatch(entry.getKey(), entry.getValue());
            }
            for (ActivitySignup signup : legacy) {
                releaseExpiredLegacy(signup);
            }
        } catch (Exception e) {
            // 名额释放是兜底逻辑，失败不能影响列表/详情读取。
            log.warn("活动未支付名额释放失败: {}", e.getMessage());
        }
    }

    /** 批次整批释放：booking PENDING→CANCELLED 抢到后，按 student+companion 释放一次。 */
    private void releaseExpiredBatch(Integer bookingId, List<ActivitySignup> signups) {
        ActivityBooking booking = activityBookingMapper.selectById(bookingId);
        if (booking == null) {
            // 历史异常数据：批次行缺失时退回逐行释放，至少不把名额永久卡死。
            for (ActivitySignup signup : signups) {
                releaseExpiredLegacy(signup);
            }
            return;
        }
        RechargeOrder order = findOrder(booking.getOrderId(), signups);
        long now = TimeUtils.timestamp();
        if (isPaidOrder(order)) {
            // 已支付但明细未推进（回调部分失败）：按已支付修复，不重复占/放名额。
            // 批次已被超时释放为 CANCELLED 时不能把明细提前置为已支付，否则支付回调
            // 看不到 cancelled 行、整批重占会被跳过，陪同名额永久缺失；交给回调处理。
            ActivityBooking current = activityBookingMapper.selectById(bookingId);
            if (current != null && current.getStatus() != null
                    && current.getStatus() == ActivityBooking.STATUS_CANCELLED) {
                return;
            }
            activityBookingMapper.update(null, new UpdateWrapper<ActivityBooking>()
                    .set("status", ActivityBooking.STATUS_PAID)
                    .set("update_time", now)
                    .eq("id", bookingId)
                    .eq("status", ActivityBooking.STATUS_PENDING));
            for (ActivitySignup signup : signups) {
                activitySignupMapper.update(null, new UpdateWrapper<ActivitySignup>()
                        .set("status", ActivitySignup.STATUS_PAID)
                        .set("update_time", now)
                        .eq("id", signup.getId())
                        .eq("status", ActivitySignup.STATUS_PENDING));
            }
            return;
        }

        int claimed = activityBookingMapper.update(null, new UpdateWrapper<ActivityBooking>()
                .set("status", ActivityBooking.STATUS_CANCELLED)
                .set("update_time", now)
                .eq("id", bookingId)
                .eq("status", ActivityBooking.STATUS_PENDING));
        ActivityBooking fresh = claimed > 0 ? booking : activityBookingMapper.selectById(bookingId);
        if (claimed > 0) {
            releaseSeats(booking.getActivityId(), headsOf(booking));
        } else if (fresh != null && fresh.getStatus() != null
                && fresh.getStatus() == ActivityBooking.STATUS_PAID) {
            // 批次已被支付回调推进为已支付：不再释放名额，明细按已支付修复。
            for (ActivitySignup signup : signups) {
                activitySignupMapper.update(null, new UpdateWrapper<ActivitySignup>()
                        .set("status", ActivitySignup.STATUS_PAID)
                        .set("update_time", now)
                        .eq("id", signup.getId())
                        .eq("status", ActivitySignup.STATUS_PENDING));
            }
            return;
        } else {
            // 上一次已释放成功但明细未全部落库（或批次行缺失）：只补取消，不能再扣一次名额。
            cancelPendingSignups(signups, now);
            markOrderCancelled(order, now);
            return;
        }
        cancelPendingSignups(signups, now);
        markOrderCancelled(order, now);
    }

    private void releaseExpiredLegacy(ActivitySignup signup) {
        RechargeOrder order = signup.getOrderId() == null || signup.getOrderId() <= 0
                ? null : rechargeOrderMapper.selectById(signup.getOrderId());
        long now = TimeUtils.timestamp();
        if (isPaidOrder(order)) {
            activitySignupMapper.update(null, new UpdateWrapper<ActivitySignup>()
                    .set("status", ActivitySignup.STATUS_PAID)
                    .set("update_time", now)
                    .eq("id", signup.getId())
                    .eq("status", ActivitySignup.STATUS_PENDING));
            return;
        }
        int released = activitySignupMapper.update(null, new UpdateWrapper<ActivitySignup>()
                .set("status", ActivitySignup.STATUS_CANCELLED)
                .set("update_time", now)
                .eq("id", signup.getId())
                .eq("status", ActivitySignup.STATUS_PENDING));
        if (released <= 0) {
            return;
        }
        releaseSeats(signup.getActivityId(), 1);
        markOrderCancelled(order, now);
    }

    private void cancelPendingSignups(List<ActivitySignup> signups, long now) {
        for (ActivitySignup signup : signups) {
            activitySignupMapper.update(null, new UpdateWrapper<ActivitySignup>()
                    .set("status", ActivitySignup.STATUS_CANCELLED)
                    .set("update_time", now)
                    .eq("id", signup.getId())
                    .eq("status", ActivitySignup.STATUS_PENDING));
        }
    }

    private RechargeOrder findOrder(Integer orderId, List<ActivitySignup> signups) {
        Integer id = orderId == null ? 0 : orderId;
        if (id <= 0 && signups != null) {
            for (ActivitySignup signup : signups) {
                if (signup.getOrderId() != null && signup.getOrderId() > 0) {
                    id = signup.getOrderId();
                    break;
                }
            }
        }
        return id <= 0 ? null : rechargeOrderMapper.selectById(id);
    }

    private boolean isPaidOrder(RechargeOrder order) {
        return order != null && order.getPayStatus() != null
                && order.getPayStatus() == PaymentEnum.OK_PAID.getCode();
    }

    /** 未支付订单在释放名额时同步关单（pay_status=4），条件更新保证幂等。 */
    private void markOrderCancelled(RechargeOrder order, long now) {
        if (order == null || order.getPayStatus() == null
                || order.getPayStatus() != PaymentEnum.UN_PAID.getCode()) {
            return;
        }
        rechargeOrderMapper.update(null, new UpdateWrapper<RechargeOrder>()
                .set("pay_status", 4)
                .set("update_time", now)
                .eq("id", order.getId())
                .eq("pay_status", PaymentEnum.UN_PAID.getCode()));
    }

    private int headsOf(ActivityBooking booking) {
        int students = booking.getStudentCount() == null ? 0 : booking.getStudentCount();
        int companions = booking.getCompanionCount() == null ? 0 : booking.getCompanionCount();
        return students + companions;
    }

    // ---- 支付回调 ----------------------------------------------------------

    @Override
    public RechargeOrder requireMockableOrder(Integer userId, Integer orderId) {
        if (orderId == null || orderId <= 0) {
            throw new OperateException("订单不存在");
        }
        RechargeOrder order = rechargeOrderMapper.selectById(orderId);
        if (order == null || order.getDeleteTime() != null
                || userId == null || !userId.equals(order.getUserId())) {
            throw new OperateException("订单不存在");
        }
        if (order.getActivityId() == null || order.getActivityId() <= 0) {
            throw new OperateException("订单不是活动订单");
        }
        return order;
    }

    @Override
    @Transactional
    public void issueForPaidOrder(String outTradeNo, String transactionId) {
        if (StringUtils.isEmpty(outTradeNo)) {
            log.warn("活动支付回调缺少订单号，忽略");
            return;
        }
        RechargeOrder order = rechargeOrderMapper.selectOne(new QueryWrapper<RechargeOrder>()
                .eq("sn", outTradeNo)
                .isNull("delete_time")
                .last("limit 1"));
        if (order == null) {
            log.warn("活动支付回调订单不存在: {}", outTradeNo);
            return;
        }
        Integer activityId = order.getActivityId();
        if (activityId == null || activityId <= 0) {
            log.warn("非活动订单收到活动回调，忽略: {}", outTradeNo);
            return;
        }
        // 已退款订单的重复/迟到回调不能再推进报名：退款已取消批次并释放名额，
        // 若继续按 #61 重占，会把已取消的报名复活并二次占用名额。
        // la_recharge_order.refund_status：0=未退款，1=退款成功（见 FinanceRechargerServiceImpl）。
        if (order.getRefundStatus() != null && order.getRefundStatus() != 0) {
            log.warn("活动订单已退款，忽略支付回调: orderSn={} refundStatus={}",
                    outTradeNo, order.getRefundStatus());
            return;
        }
        List<ActivitySignup> signups = activitySignupMapper.selectList(new QueryWrapper<ActivitySignup>()
                .eq("order_id", order.getId()));
        if (signups.isEmpty()) {
            log.warn("活动订单没有报名明细，忽略: {}", outTradeNo);
            return;
        }

        long now = TimeUtils.timestamp();
        // 并发/重复回调幂等：先条件更新抢占“未支付→已支付”，只有抢到的回调才扣余额，
        // 避免同一笔活动报名被重复通知时重复扣减钱包。
        // refund_status=0 必须进 WHERE：与上面的退款预检形成原子防线，回调与退款并发时
        // 即使预检读到未退款，退款已落库则这里抢不到，不能把已退款订单推进为已支付。
        int claimed = rechargeOrderMapper.update(null, new UpdateWrapper<RechargeOrder>()
                .set("pay_status", PaymentEnum.OK_PAID.getCode())
                .set("pay_time", now)
                .set("transaction_id", transactionId)
                .set("update_time", now)
                .eq("id", order.getId())
                .ne("pay_status", PaymentEnum.OK_PAID.getCode())
                .eq("refund_status", 0));
        if (claimed > 0) {
            if (order.getPayWay() != null && order.getPayWay() == PaymentEnum.WALLET_PAY.getCode()) {
                deductWallet(order, now);
            }
            order.setPayStatus(PaymentEnum.OK_PAID.getCode());
            order.setPayTime(now);
            order.setUpdateTime(now);
            if (!StringUtils.isEmpty(transactionId)) {
                order.setTransactionId(transactionId);
            }
        }

        // #61 默认策略：名额已被超时释放时优先重新预占；若此刻活动已满，
        // 不再整单回滚（否则用户已付款却报名失败），改为订单生效 + 允许超员，
        // 同时落一条后台待处理告警，由运营跟进（标记已处理/发起退款）。
        // #186：按报名批次整批重占（student_count + companion_count），不能按学员行
        // 只加回学员数，否则陪同名额永久缺失。
        Map<Integer, List<ActivitySignup>> batches = new LinkedHashMap<>();
        List<ActivitySignup> legacy = new ArrayList<>();
        for (ActivitySignup signup : signups) {
            int bookingId = signup.getBookingId() == null ? 0 : signup.getBookingId();
            if (bookingId > 0) {
                batches.computeIfAbsent(bookingId, key -> new ArrayList<>()).add(signup);
            } else {
                legacy.add(signup);
            }
        }

        int released = 0;
        int legacyReleased = 0;
        boolean overbooked = false;
        boolean seatsIncreased = false;
        for (Map.Entry<Integer, List<ActivitySignup>> entry : batches.entrySet()) {
            ActivityBooking booking = activityBookingMapper.selectById(entry.getKey());
            List<ActivitySignup> rows = entry.getValue();
            if (booking == null) {
                legacy.addAll(rows);
                continue;
            }
            boolean hasCancelled = false;
            for (ActivitySignup signup : rows) {
                if (signup.getStatus() != null && signup.getStatus() == ActivitySignup.STATUS_CANCELLED) {
                    hasCancelled = true;
                }
            }
            // 先抢占批次状态：重复/并发回调只有把批次从非已支付推到已支付的这一次才重占名额。
            int bookingClaimed = activityBookingMapper.update(null, new UpdateWrapper<ActivityBooking>()
                    .set("status", ActivityBooking.STATUS_PAID)
                    .set("update_time", now)
                    .eq("id", booking.getId())
                    .ne("status", ActivityBooking.STATUS_PAID));
            for (ActivitySignup signup : rows) {
                if (signup.getStatus() != null && signup.getStatus() == ActivitySignup.STATUS_PAID) {
                    continue;
                }
                activitySignupMapper.update(null, new UpdateWrapper<ActivitySignup>()
                        .set("status", ActivitySignup.STATUS_PAID)
                        .set("update_time", now)
                        .eq("id", signup.getId())
                        .eq("status", signup.getStatus() == null
                                ? ActivitySignup.STATUS_PENDING : signup.getStatus()));
            }
            if (hasCancelled && bookingClaimed > 0) {
                int heads = headsOf(booking);
                if (!tryReserveSeats(activityId, heads)) {
                    seatsIncreased = increaseSeats(activityId, heads) || seatsIncreased;
                    overbooked = true;
                }
                released += heads;
            }
        }

        // 历史数据（booking_id=0）保持逐行口径，避免老订单无法补偿。
        for (ActivitySignup signup : legacy) {
            if (signup.getStatus() != null && signup.getStatus() == ActivitySignup.STATUS_PAID) {
                continue;
            }
            boolean wasCancelled = signup.getStatus() != null
                    && signup.getStatus() == ActivitySignup.STATUS_CANCELLED;
            int transitioned = activitySignupMapper.update(null, new UpdateWrapper<ActivitySignup>()
                    .set("status", ActivitySignup.STATUS_PAID)
                    .set("update_time", now)
                    .eq("id", signup.getId())
                    .eq("status", wasCancelled ? ActivitySignup.STATUS_CANCELLED
                            : ActivitySignup.STATUS_PENDING));
            if (transitioned > 0 && wasCancelled) {
                released += 1;
                legacyReleased += 1;
            }
        }
        if (legacyReleased > 0 && !tryReserveSeats(activityId, legacyReleased)) {
            seatsIncreased = increaseSeats(activityId, legacyReleased) || seatsIncreased;
            overbooked = true;
        }
        if (overbooked) {
            ActivityEnrollAlert alert = createOverbookAlert(order, activityId, signups, released,
                    seatsIncreased, now);
            if (seatsIncreased) {
                log.warn("活动报名已付款但满员，按超员生效并生成待处理告警: orderSn={} activityId={} students={} released={} alertId={}",
                        outTradeNo, activityId, signups.size(), released,
                        alert == null ? 0 : alert.getId());
            } else {
                log.warn("活动报名已付款但活动已下架/删除，未增加名额并生成待处理告警: orderSn={} activityId={} students={} released={} alertId={}",
                        outTradeNo, activityId, signups.size(), released,
                        alert == null ? 0 : alert.getId());
            }
        } else {
            log.info("活动支付成功: orderSn={} activityId={} students={} claimed={}",
                    outTradeNo, activityId, signups.size(), claimed);
        }
    }

    private void deductWallet(RechargeOrder order, long now) {
        // #59：余额判断与扣减由条件 UPDATE 原子完成，并发下不会把余额扣成负数或覆盖他人扣减。
        int deducted = userMapper.deductMoney(order.getUserId(), order.getOrderAmount(), now);
        if (deducted <= 0) {
            if (userMapper.selectById(order.getUserId()) == null) {
                throw new OperateException("用户不存在");
            }
            throw new OperateException("余额不足，请先充值");
        }
        userAccountLogMapper.add(order.getUserId(), AccountLogEnum.UM_DEC_ACTIVITY_PAY.getCode(),
                order.getOrderAmount(), order.getId(), order.getSn(), "活动报名支付扣款", null);
    }

    @Override
    public String payDescription(RechargeOrder order) {
        String title = "活动报名";
        if (order != null && order.getActivityId() != null && order.getActivityId() > 0) {
            Activity activity = activityMapper.selectById(order.getActivityId());
            if (activity != null && StringUtils.isNotEmpty(activity.getTitle())) {
                title = activity.getTitle().trim() + "-活动报名";
            }
        }
        return truncateUtf8ByBytes(title, WECHAT_PAY_DESCRIPTION_MAX_BYTES);
    }

    private String truncateUtf8ByBytes(String text, int maxBytes) {
        if (StringUtils.isEmpty(text) || maxBytes <= 0
                || text.getBytes(StandardCharsets.UTF_8).length <= maxBytes) {
            return text;
        }
        for (int end = text.length(); end > 0; --end) {
            String candidate = text.substring(0, end);
            if (candidate.getBytes(StandardCharsets.UTF_8).length <= maxBytes) {
                return candidate;
            }
        }
        return "";
    }

    // ---- 展示工具 ----------------------------------------------------------

    private String coverUrl(String cover) {
        if (StringUtils.isEmpty(cover)) {
            return "";
        }
        if (cover.startsWith("http://") || cover.startsWith("https://") || cover.startsWith("/")) {
            return cover;
        }
        return UrlUtils.toAbsoluteUrl(cover);
    }

    private String areaName(Integer areaId) {
        if (areaId == null || areaId <= 0) {
            return "";
        }
        Area area = areaMapper.selectById(areaId);
        return area == null ? "" : nvl(area.getName());
    }

    private List<String> splitTags(String tags) {
        List<String> result = new ArrayList<>();
        if (StringUtils.isEmpty(tags)) {
            return result;
        }
        for (String tag : tags.split(",")) {
            String text = tag.trim();
            if (!text.isEmpty()) {
                result.add(text);
            }
        }
        return result;
    }

    private String dayLabel(String dateText) {
        LocalDate date = parseDate(dateText);
        if (date == null) {
            return "";
        }
        long days = ChronoUnit.DAYS.between(LocalDate.now(), date);
        if (days == 0) {
            return "今天";
        }
        if (days == 1) {
            return "明天";
        }
        if (days == 2) {
            return "后天";
        }
        return weekday(dateText);
    }

    private String shortDate(String dateText) {
        LocalDate date = parseDate(dateText);
        return date == null ? "" : date.format(SHORT_DATE);
    }

    private String weekday(String dateText) {
        LocalDate date = parseDate(dateText);
        if (date == null) {
            return "";
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return WEEK_LABELS[dayOfWeek.getValue() - 1];
    }

    private LocalDate parseDate(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        try {
            return LocalDate.parse(text.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private String nvl(String text) {
        return text == null ? "" : text;
    }
}
