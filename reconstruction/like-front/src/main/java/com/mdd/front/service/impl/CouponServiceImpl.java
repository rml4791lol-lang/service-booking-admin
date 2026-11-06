package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.Coupon;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.coupon.CouponGoods;
import com.mdd.common.entity.coupon.CouponFlow;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.region.Area;
import com.mdd.common.entity.student.Student;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.CouponMapper;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.coupon.CouponFlowMapper;
import com.mdd.common.mapper.coupon.CouponGoodsMapper;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.region.AreaMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.service.CouponCheckinService;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.front.service.ICouponService;
import com.mdd.front.util.CouponRefundRules;
import com.mdd.front.vo.CouponVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 小程序券服务实现（T29.4）。
 *
 * <p>次数券口径：券商品（卖什么）→ 支付订单（la_recharge_order.coupon_goods_id）→
 * 支付回调发券（la_coupon_instance，order_id 唯一）→ 券流水（la_coupon_flow，只追加）。</p>
 *
 * <p>适用规则与 {@code services/db.js#couponApplies} 金样保持一致：</p>
 * <ul>
 *   <li>状态有效、剩余次数 &gt; 0、未过期；</li>
 *   <li>scopeType=private 只认私教课；group 只认团课；trial 不认私教课；</li>
 *   <li>courseIds / areaIds 非空时做白名单匹配；</li>
 *   <li>传了 studentId 时必须与持券学员一致。</li>
 * </ul>
 *
 * <p>私教/团课/体验的判断沿用现有课程数据：名字含「一对一/一对二/一对三/私教」视为私教，
 * classType=1 视为体验班，其余为团课。待后台补标准字段后可在此一处切换。</p>
 */
@Service
public class CouponServiceImpl implements ICouponService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MINUTE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /** 券实例状态：1=有效，2=已用完，3=已过期，4=已作废。 */
    private static final int STATUS_VALID = 1;
    private static final int STATUS_USED = 2;
    private static final int STATUS_EXPIRED = 3;
    private static final int STATUS_VOID = 4;

    /** 券流水类型：1=发放，2=核销扣减，3=补发，4=作废，5=退回。 */
    private static final int FLOW_GRANT = 1;
    private static final int FLOW_CONSUME = 2;

    @Resource
    CouponGoodsMapper couponGoodsMapper;
    @Resource
    CouponInstanceMapper couponInstanceMapper;
    @Resource
    CouponFlowMapper couponFlowMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    AreaMapper areaMapper;
    @Resource
    CouponCheckinService couponCheckinService;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;

    /** 券退款开关（#110 T12）：关闭时小程序不展示任何退款入口，接口侧同样拒绝。 */
    @Value("${like.coupon.refund-enabled:true}")
    private boolean couponRefundEnabled;

    // 老抵扣券
    @Resource
    CouponMapper couponMapper;

    // ---- 老抵扣券（保留） --------------------------------------------------

    @Override
    public List<CouponVo> legacyMyCoupons(Integer userId, Integer status) {
        QueryWrapper<Coupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.isNull("delete_time");
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("id");
        List<Coupon> coupons = couponMapper.selectList(queryWrapper);
        long now = now();
        List<CouponVo> list = new ArrayList<>();
        for (Coupon item : coupons) {
            if (item.getStatus() != null && item.getStatus() == 0
                    && item.getExpireTime() != null && item.getExpireTime() < now) {
                item.setStatus(2);
                item.setUpdateTime(now);
                couponMapper.updateById(item);
            }
            CouponVo vo = new CouponVo();
            vo.setId(item.getId());
            vo.setSn(item.getSn());
            vo.setName(item.getName());
            vo.setAmount(item.getAmount());
            vo.setMinAmount(item.getMinAmount());
            vo.setStatus(item.getStatus());
            vo.setStartTime(item.getStartTime() != null ? TimeUtils.timestampToDate(item.getStartTime()) : "");
            vo.setExpireTime(item.getExpireTime() != null ? TimeUtils.timestampToDate(item.getExpireTime()) : "");
            vo.setStatusDesc(legacyStatusDesc(item.getStatus()));
            list.add(vo);
        }
        return list;
    }

    @Override
    public List<CouponVo> legacyAvailableCoupons(Integer userId, BigDecimal orderAmount) {
        long now = now();
        QueryWrapper<Coupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("status", 0);
        queryWrapper.le("start_time", now);
        queryWrapper.ge("expire_time", now);
        queryWrapper.le("min_amount", orderAmount);
        queryWrapper.isNull("delete_time");
        queryWrapper.orderByDesc("amount");
        List<Coupon> coupons = couponMapper.selectList(queryWrapper);
        List<CouponVo> list = new ArrayList<>();
        for (Coupon item : coupons) {
            CouponVo vo = new CouponVo();
            vo.setId(item.getId());
            vo.setSn(item.getSn());
            vo.setName(item.getName());
            vo.setAmount(item.getAmount());
            vo.setMinAmount(item.getMinAmount());
            vo.setStatus(0);
            vo.setStatusDesc("可使用");
            vo.setStartTime(item.getStartTime() != null ? TimeUtils.timestampToDate(item.getStartTime()) : "");
            vo.setExpireTime(item.getExpireTime() != null ? TimeUtils.timestampToDate(item.getExpireTime()) : "");
            list.add(vo);
        }
        return list;
    }

    private String legacyStatusDesc(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 0:
                return "未使用";
            case 1:
                return "已使用";
            case 2:
                return "已过期";
            default:
                return "未知";
        }
    }

    // ---- 次数券：商品 ------------------------------------------------------

    @Override
    public List<Map<String, Object>> products() {
        QueryWrapper<CouponGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.isNull("delete_time");
        queryWrapper.orderByAsc("sort");
        queryWrapper.orderByAsc("id");
        List<CouponGoods> goodsList = couponGoodsMapper.selectList(queryWrapper);

        Set<Integer> courseIds = new HashSet<>();
        Set<Integer> areaIds = new HashSet<>();
        for (CouponGoods goods : goodsList) {
            courseIds.addAll(parseIds(goods.getCourseIds()));
            areaIds.addAll(parseIds(goods.getAreaIds()));
        }
        Map<Integer, String> courseNames = courseNames(courseIds);
        Map<Integer, String> areaNames = areaNames(areaIds);

        List<Map<String, Object>> result = new ArrayList<>();
        for (CouponGoods goods : goodsList) {
            result.add(productVo(goods, courseNames, areaNames));
        }
        return result;
    }

    private Map<String, Object> productVo(CouponGoods goods, Map<Integer, String> courseNames,
                                          Map<Integer, String> areaNames) {
        List<Integer> goodsCourseIds = parseIds(goods.getCourseIds());
        List<Integer> goodsAreaIds = parseIds(goods.getAreaIds());
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", String.valueOf(goods.getId()));
        item.put("code", goods.getCode());
        item.put("title", goods.getTitle());
        item.put("desc", goods.getDescription());
        item.put("price", goods.getPrice() == null ? BigDecimal.ZERO : goods.getPrice());
        item.put("times", goods.getTimes());
        item.put("validDays", goods.getValidDays());
        item.put("scope", goods.getScope());
        item.put("scopeType", goods.getScopeType());
        item.put("courseIds", textIds(goodsCourseIds));
        item.put("areaIds", textIds(goodsAreaIds));
        item.put("perUser", goods.getPerUser());
        item.put("perStudent", goods.getPerStudent());
        item.put("status", "on");
        item.put("tag", goods.getTag());
        item.put("sort", goods.getSort());
        item.put("scopeText", scopeText(goods.getScope(), goodsCourseIds, goodsAreaIds, courseNames, areaNames));
        item.put("limitText", limitText(goods));
        return item;
    }

    private String limitText(CouponGoods goods) {
        int perStudent = goods.getPerStudent() == null ? 0 : goods.getPerStudent();
        int perUser = goods.getPerUser() == null ? 0 : goods.getPerUser();
        if (perStudent > 0) {
            return "每名学员限领 " + perStudent + " 份";
        }
        if (perUser > 0) {
            return "每账号限购 " + perUser + " 份";
        }
        return "不限购";
    }

    // ---- 次数券：我的券 ----------------------------------------------------

    @Override
    public List<Map<String, Object>> myCoupons(Integer userId, Integer studentId) {
        if (studentId != null && studentId > 0) {
            requireStudent(userId, studentId);
        }
        QueryWrapper<CouponInstance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        if (studentId != null && studentId > 0) {
            queryWrapper.eq("student_id", studentId);
        }
        queryWrapper.orderByDesc("id");
        List<CouponInstance> instances = couponInstanceMapper.selectList(queryWrapper);

        long now = now();
        Set<Integer> courseIds = new HashSet<>();
        Set<Integer> areaIds = new HashSet<>();
        Set<Integer> studentIds = new HashSet<>();
        for (CouponInstance instance : instances) {
            courseIds.addAll(parseIds(instance.getCourseIds()));
            areaIds.addAll(parseIds(instance.getAreaIds()));
            if (instance.getStudentId() != null) {
                studentIds.add(instance.getStudentId());
            }
        }
        Map<Integer, String> courseNames = courseNames(courseIds);
        Map<Integer, String> areaNames = areaNames(areaIds);
        Map<Integer, String> studentNames = studentNames(studentIds);

        List<Map<String, Object>> result = new ArrayList<>();
        for (CouponInstance instance : instances) {
            refreshStatus(instance, now);
            result.add(couponVo(instance, studentNames.get(instance.getStudentId()),
                    courseNames, areaNames, now));
        }
        attachRefundInfo(result);
        return result;
    }

    /**
     * #110 T12：给券卡补退款入口口径（只读聚合，一次订单 + 一次实例计数 + 一次核销流水）。
     *
     * <p>未使用的单券（该订单只有 1 张券、无核销）→ refundMode=refund（显示「申请退款」）；
     * 已使用或 10 节课券包（一单多张）→ refundMode=service（显示「联系客服」）；
     * 开关关闭/订单查不到/未支付 → 空串（不展示入口）。</p>
     */
    private void attachRefundInfo(List<Map<String, Object>> cards) {
        Set<Integer> orderIds = new HashSet<>();
        for (Map<String, Object> card : cards) {
            Integer orderId = toInt(card.get("orderId"));
            if (orderId != null && orderId > 0) {
                orderIds.add(orderId);
            }
        }
        Map<Integer, RechargeOrder> orders = new HashMap<>();
        Map<Integer, Integer> counts = new HashMap<>();
        Set<Integer> consumedOrders = new HashSet<>();
        if (!orderIds.isEmpty()) {
            for (RechargeOrder order : rechargeOrderMapper.selectBatchIds(orderIds)) {
                orders.put(order.getId(), order);
            }
            for (CouponInstance instance : couponInstanceMapper.selectList(
                    new QueryWrapper<CouponInstance>().in("order_id", orderIds))) {
                Integer orderId = instance.getOrderId();
                counts.put(orderId, (counts.containsKey(orderId) ? counts.get(orderId) : 0) + 1);
            }
            for (CouponFlow flow : couponFlowMapper.selectList(new QueryWrapper<CouponFlow>()
                    .in("order_id", orderIds).eq("type", 2))) {
                consumedOrders.add(flow.getOrderId());
            }
        }
        for (Map<String, Object> card : cards) {
            Integer orderId = toInt(card.get("orderId"));
            RechargeOrder order = orderId == null ? null : orders.get(orderId);
            int count = orderId == null || !counts.containsKey(orderId) ? 0 : counts.get(orderId);
            Integer total = toInt(card.get("total"));
            Integer remaining = toInt(card.get("remaining"));
            Integer statusCode = toInt(card.get("statusCode"));
            boolean used = consumedOrders.contains(orderId)
                    || (total != null && remaining != null && remaining < total)
                    || (statusCode != null && (statusCode == 2 || statusCode == 4));
            card.put("orderCouponCount", count);
            if (!couponRefundEnabled || order == null) {
                card.put("refundMode", CouponRefundRules.MODE_NONE);
                card.put("refundHint", "");
                continue;
            }
            boolean pack = count > 1 || (total != null && total > 1);
            String mode = CouponRefundRules.mode(count, used, pack, order.getPayStatus(), order.getRefundStatus());
            card.put("refundMode", mode);
            card.put("refundHint", CouponRefundRules.hint(mode, count, used, pack));
        }
    }

    private static Integer toInt(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return Integer.valueOf(String.valueOf(value).trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Map<String, Object> couponVo(CouponInstance instance, String studentName,
                                         Map<Integer, String> courseNames,
                                         Map<Integer, String> areaNames, long now) {
        List<Integer> instanceCourseIds = parseIds(instance.getCourseIds());
        List<Integer> instanceAreaIds = parseIds(instance.getAreaIds());
        String expire = instance.getExpireTime() == null ? "长期有效" : dateText(instance.getExpireTime());
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", String.valueOf(instance.getId()));
        item.put("sn", instance.getSn());
        item.put("productId", String.valueOf(instance.getGoodsId()));
        item.put("goodsId", instance.getGoodsId());
        item.put("title", instance.getTitle());
        item.put("total", instance.getTotal());
        item.put("remaining", instance.getRemaining());
        item.put("scope", instance.getScope());
        item.put("scopeType", instance.getScopeType());
        item.put("courseIds", textIds(instanceCourseIds));
        item.put("areaIds", textIds(instanceAreaIds));
        // 前端做候选券初筛时需要与核销规则一致的生效时间；保留时间戳供精确比较，
        // startText 仅用于展示，不参与判断。
        item.put("startTime", instance.getStartTime());
        item.put("startText", minuteText(instance.getStartTime()));
        item.put("expire", expire);
        item.put("expireText", expire);
        item.put("status", statusText(instance.getStatus()));
        item.put("statusCode", instance.getStatus());
        item.put("statusDesc", statusDesc(instance.getStatus()));
        item.put("usable", usable(instance, now));
        item.put("studentId", String.valueOf(instance.getStudentId()));
        item.put("studentName", studentName == null ? "" : studentName);
        item.put("scopeText", scopeText(instance.getScope(), instanceCourseIds, instanceAreaIds,
                courseNames, areaNames));
        item.put("scopedCourseNames", namesOf(instanceCourseIds, courseNames));
        item.put("scopedAreaNames", namesOf(instanceAreaIds, areaNames));
        item.put("orderId", instance.getOrderId());
        item.put("source", instance.getSource());
        item.put("createTime", instance.getCreateTime());
        return item;
    }

    /** 惰性更新状态：已过期 → 3，次数用尽 → 2（状态字段允许更新，流水不允许）。 */
    private void refreshStatus(CouponInstance instance, long now) {
        Integer status = instance.getStatus();
        if (status == null || status != STATUS_VALID) {
            return;
        }
        int target = 0;
        if (instance.getExpireTime() != null && instance.getExpireTime() < now) {
            target = STATUS_EXPIRED;
        } else if (instance.getRemaining() == null || instance.getRemaining() <= 0) {
            target = STATUS_USED;
        }
        if (target > 0) {
            instance.setStatus(target);
            instance.setUpdateTime(now);
            couponInstanceMapper.updateById(instance);
        }
    }

    private boolean usable(CouponInstance instance, long now) {
        if (instance.getStatus() == null || instance.getStatus() != STATUS_VALID) {
            return false;
        }
        if (instance.getRemaining() == null || instance.getRemaining() <= 0) {
            return false;
        }
        return instance.getExpireTime() == null || instance.getExpireTime() >= now;
    }

    private String statusText(Integer status) {
        if (status == null) {
            return "expired";
        }
        switch (status) {
            case STATUS_VALID:
                return "valid";
            case STATUS_USED:
                return "used";
            case STATUS_VOID:
                return "void";
            case STATUS_EXPIRED:
            default:
                return "expired";
        }
    }

    private String statusDesc(Integer status) {
        if (status == null) {
            return "已失效";
        }
        switch (status) {
            case STATUS_VALID:
                return "可用";
            case STATUS_USED:
                return "已用完";
            case STATUS_VOID:
                return "已作废";
            case STATUS_EXPIRED:
            default:
                return "已过期";
        }
    }

    // ---- 次数券：流水 ------------------------------------------------------

    @Override
    public List<Map<String, Object>> flows(Integer userId, Integer studentId, Integer couponId) {
        if (studentId != null && studentId > 0) {
            requireStudent(userId, studentId);
        }
        QueryWrapper<CouponFlow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        if (studentId != null && studentId > 0) {
            queryWrapper.eq("student_id", studentId);
        }
        if (couponId != null && couponId > 0) {
            queryWrapper.eq("coupon_id", couponId);
        }
        queryWrapper.orderByAsc("id");
        List<CouponFlow> flowList = couponFlowMapper.selectList(queryWrapper);

        Set<Integer> couponIds = new HashSet<>();
        Set<Integer> studentIds = new HashSet<>();
        for (CouponFlow flow : flowList) {
            if (flow.getCouponId() != null) {
                couponIds.add(flow.getCouponId());
            }
            if (flow.getStudentId() != null) {
                studentIds.add(flow.getStudentId());
            }
        }
        Map<Integer, String> titles = couponTitles(couponIds);
        Map<Integer, String> studentNames = studentNames(studentIds);

        List<Map<String, Object>> result = new ArrayList<>();
        for (CouponFlow flow : flowList) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", String.valueOf(flow.getId()));
            item.put("couponId", String.valueOf(flow.getCouponId()));
            item.put("type", flowTypeText(flow.getType()));
            item.put("typeCode", flow.getType());
            item.put("times", flow.getTimes());
            item.put("balance", flow.getBalance());
            item.put("time", minuteText(flow.getCreateTime()));
            item.put("orderNo", flow.getOrderSn() == null ? "" : flow.getOrderSn());
            item.put("ref", flow.getRef() == null ? "" : flow.getRef());
            item.put("title", titles.getOrDefault(flow.getCouponId(), ""));
            item.put("studentId", String.valueOf(flow.getStudentId()));
            item.put("studentName", studentNames.getOrDefault(flow.getStudentId(), ""));
            item.put("remark", flow.getRemark() == null ? "" : flow.getRemark());
            result.add(item);
        }
        return result;
    }

    private String flowTypeText(Integer type) {
        if (type == null) {
            return "未知";
        }
        switch (type) {
            case FLOW_GRANT:
                return "发放";
            case 2:
                return "核销扣减";
            case 3:
                return "补发";
            case 4:
                return "作废";
            case 5:
                return "退回";
            default:
                return "未知";
        }
    }

    private Map<Integer, String> couponTitles(Set<Integer> ids) {
        Map<Integer, String> map = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            return map;
        }
        List<CouponInstance> instances = couponInstanceMapper.selectBatchIds(ids);
        for (CouponInstance instance : instances) {
            map.put(instance.getId(), instance.getTitle());
        }
        return map;
    }

    // ---- 次数券：适用校验 / 最优券 -----------------------------------------

    @Override
    public Map<String, Object> applies(Integer userId, String couponId, String courseId,
                                       String sessionId, String studentId) {
        Integer couponIdValue = toInt(couponId);
        Integer studentIdValue = toInt(studentId);
        CourseClass course = resolveCourse(toInt(courseId), toInt(sessionId));
        if (course == null) {
            return appliesResult(false, "课程不存在");
        }
        if (couponIdValue == null || couponIdValue <= 0) {
            return appliesResult(false, "券不存在");
        }
        CouponInstance instance = couponInstanceMapper.selectById(couponIdValue);
        if (instance == null || !userId.equals(instance.getUserId())) {
            return appliesResult(false, "券不存在或不属于当前账号");
        }
        String reason = checkApplies(instance, course, studentIdValue, now());
        return appliesResult(reason == null, reason == null ? "" : reason);
    }

    private Map<String, Object> appliesResult(boolean applies, String reason) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("applies", applies);
        result.put("reason", reason);
        return result;
    }

    /**
     * 券适用规则（与小程序 mock / 金样一致）。返回 null 表示适用，否则返回不可用原因。
     * 规则实现已抽取到 {@link CouponCheckinService}，教练核销与后台补核销共用。
     */
    private String checkApplies(CouponInstance instance, CourseClass course, Integer studentId, long now) {
        return couponCheckinService.checkApplies(instance, course, studentId, now);
    }

    @Override
    public Map<String, Object> bestCoupon(Integer userId, String courseId, String sessionId,
                                          String studentId) {
        CourseClass course = resolveCourse(toInt(courseId), toInt(sessionId));
        if (course == null) {
            return null;
        }
        Integer studentIdValue = toInt(studentId);
        long now = now();
        QueryWrapper<CouponInstance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("status", STATUS_VALID);
        queryWrapper.gt("remaining", 0);
        queryWrapper.and(wrapper -> wrapper.isNull("expire_time").or().ge("expire_time", now));
        if (studentIdValue != null && studentIdValue > 0) {
            // 与 CouponCheckinService 同口径：持券学员本人 + 账号级券（student_id NULL / 0）
            // 都参与匹配。账号级券可给名下任一学员使用，只查 student_id = 学员 会把它漏掉。
            queryWrapper.and(wrapper -> wrapper.eq("student_id", studentIdValue)
                    .or().isNull("student_id")
                    .or().eq("student_id", 0));
        }
        List<CouponInstance> instances = couponInstanceMapper.selectList(queryWrapper);

        List<CouponInstance> matched = new ArrayList<>();
        for (CouponInstance instance : instances) {
            if (checkApplies(instance, course, studentIdValue, now) == null) {
                matched.add(instance);
            }
        }
        if (matched.isEmpty()) {
            return null;
        }
        matched.sort(new Comparator<CouponInstance>() {
            @Override
            public int compare(CouponInstance left, CouponInstance right) {
                Long leftExpire = left.getExpireTime();
                Long rightExpire = right.getExpireTime();
                if (leftExpire == null && rightExpire == null) {
                    return Integer.compare(left.getId(), right.getId());
                }
                if (leftExpire == null) {
                    return 1;
                }
                if (rightExpire == null) {
                    return -1;
                }
                int compare = leftExpire.compareTo(rightExpire);
                return compare != 0 ? compare : Integer.compare(left.getId(), right.getId());
            }
        });

        CouponInstance best = matched.get(0);
        Set<Integer> courseIds = new HashSet<>(parseIds(best.getCourseIds()));
        Set<Integer> areaIds = new HashSet<>(parseIds(best.getAreaIds()));
        Set<Integer> studentIds = new HashSet<>();
        if (best.getStudentId() != null) {
            studentIds.add(best.getStudentId());
        }
        return couponVo(best, studentNames(studentIds).get(best.getStudentId()),
                courseNames(courseIds), areaNames(areaIds), now);
    }

    // ---- 次数券：核销扣减（#26） -------------------------------------------

    /**
     * 教练核销扣券：按学员自动匹配一张可用券，扣 1 次并追加核销流水。
     *
     * <p>候选券按“到期最早优先、同到期按 id 升序”排序；每次真正扣减前用
     * {@code SELECT ... FOR UPDATE} 重新加锁并复核适用规则，保证并发核销不超扣。
     * 必须由外层事务调用，异常时随外层事务回滚。</p>
     */
    @Override
    public Map<String, Object> deductForCheckin(Integer userId, Integer studentId, Integer courseClassId,
                                                String ref, Integer operatorId) {
        // 与后台人工补核销共用同一套扣券实现（#37 抽取）。
        return couponCheckinService.deductForCheckin(
                userId, studentId, courseClassId, ref, "coach", operatorId, "教练核销扣减");
    }

    /**
     * 课程类型：私教 / 体验 / 团课。
     *
     * <p>现有 la_course_class 没有标准的班型字段：一对一/一对二/一对三/私教按名称识别，
     * class_type=1 为体验班，其余团课。券的适用判断只依赖这里，未来补字段时改一处即可。</p>
     */
    private String courseType(CourseClass course) {
        return couponCheckinService.courseType(course);
    }

    private CourseClass resolveCourse(Integer courseId, Integer sessionId) {
        Integer targetId = courseId;
        if ((targetId == null || targetId <= 0) && sessionId != null && sessionId > 0) {
            CourseLesson lesson = courseLessonMapper.selectById(sessionId);
            if (lesson == null) {
                return null;
            }
            targetId = lesson.getClassId();
        }
        if (targetId == null || targetId <= 0) {
            return null;
        }
        return courseClassMapper.selectById(targetId);
    }

    // ---- 公共工具 ----------------------------------------------------------

    private Student requireStudent(Integer userId, Integer studentId) {
        Student student = studentMapper.selectById(studentId);
        if (student == null || student.getDeleteTime() != null
                || userId == null || !userId.equals(student.getUserId())) {
            throw new OperateException("学员不存在或不属于当前账号");
        }
        return student;
    }

    private Map<Integer, String> courseNames(Set<Integer> ids) {
        Map<Integer, String> map = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            return map;
        }
        List<CourseClass> classes = courseClassMapper.selectBatchIds(ids);
        for (CourseClass courseClass : classes) {
            map.put(courseClass.getId(), courseClass.getName());
        }
        return map;
    }

    private Map<Integer, String> areaNames(Set<Integer> ids) {
        Map<Integer, String> map = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            return map;
        }
        List<Area> areas = areaMapper.selectBatchIds(ids);
        for (Area area : areas) {
            map.put(area.getId(), area.getName());
        }
        return map;
    }

    private Map<Integer, String> studentNames(Set<Integer> ids) {
        Map<Integer, String> map = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            return map;
        }
        List<Student> students = studentMapper.selectBatchIds(ids);
        for (Student student : students) {
            map.put(student.getId(), student.getName());
        }
        return map;
    }

    private String scopeText(String scope, List<Integer> courseIds, List<Integer> areaIds,
                             Map<Integer, String> courseNames, Map<Integer, String> areaNames) {
        List<String> scopedCourses = namesOf(courseIds, courseNames);
        if (!scopedCourses.isEmpty()) {
            return "仅限 " + String.join("、", scopedCourses);
        }
        List<String> scopedAreas = namesOf(areaIds, areaNames);
        if (!scopedAreas.isEmpty()) {
            return "仅限 " + String.join("、", scopedAreas);
        }
        return scope == null ? "" : scope;
    }

    private List<String> namesOf(List<Integer> ids, Map<Integer, String> names) {
        List<String> list = new ArrayList<>();
        if (ids == null) {
            return list;
        }
        for (Integer id : ids) {
            String name = names.get(id);
            if (!StringUtils.isEmpty(name)) {
                list.add(name);
            }
        }
        return list;
    }

    private List<String> textIds(List<Integer> ids) {
        List<String> list = new ArrayList<>();
        if (ids != null) {
            for (Integer id : ids) {
                list.add(String.valueOf(id));
            }
        }
        return list;
    }

    private List<Integer> parseIds(String text) {
        List<Integer> ids = new ArrayList<>();
        if (text == null || text.trim().isEmpty()) {
            return ids;
        }
        String[] parts = text.split(",");
        for (String part : parts) {
            if (part == null || part.trim().isEmpty()) {
                continue;
            }
            try {
                int value = Integer.parseInt(part.trim());
                if (value > 0) {
                    ids.add(value);
                }
            } catch (NumberFormatException ignored) {
                // 后台脏数据跳过，不影响其他 ID
            }
        }
        return ids;
    }

    private Integer toInt(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private long now() {
        return System.currentTimeMillis() / 1000L;
    }

    private String dateText(Long timestamp) {
        if (timestamp == null) {
            return "";
        }
        LocalDate date = Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
        return date.format(DATE_FMT);
    }

    private String minuteText(Long timestamp) {
        if (timestamp == null || timestamp <= 0) {
            return "";
        }
        return Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault())
                .toLocalDateTime().format(MINUTE_FMT);
    }
}
