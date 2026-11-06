package com.mdd.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.coupon.CouponFlow;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.mapper.coupon.CouponFlowMapper;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 核销扣券公共能力（T30.4 / #37）。
 *
 * <p>教练端核销（#26）与后台人工补核销共用同一套「逐人自动匹配一张可用次数券、
 * 扣 1 次、追加只增流水」的口径，避免两条链路各写一份导致次数/考勤不一致。</p>
 *
 * <p>规则与小程序 mock / 金样保持一致：</p>
 * <ul>
 *   <li>券状态有效、剩余次数 &gt; 0、在有效期内；</li>
 *   <li>scopeType=private 只认私教课；group 只认团课；trial 不认私教课；</li>
 *   <li>courseIds / areaIds 非空时做白名单匹配；</li>
 *   <li>传了 studentId 时必须与持券学员一致（未绑定学员的券也参与匹配）；</li>
 *   <li>多张可用券按「到期最早优先，同到期按 id 升序」。</li>
 * </ul>
 *
 * <p>扣减前 {@code SELECT ... FOR UPDATE} 重新加锁并复核，必须由外层事务调用；
 * 异常随外层事务整体回滚。无可用券返回 {@code deducted=false} 与中文原因，
 * 由调用方按「不阻断核销」处理。</p>
 */
@Service
public class CouponCheckinService {

    private static final int STATUS_VALID = 1;
    private static final int STATUS_USED = 2;
    private static final int FLOW_CONSUME = 2;

    @Resource
    CouponInstanceMapper couponInstanceMapper;
    @Resource
    CouponFlowMapper couponFlowMapper;
    @Resource
    CourseClassMapper courseClassMapper;

    /**
     * 核销扣券：按学员自动匹配一张可用券，扣 1 次并追加核销流水。
     *
     * @param operatorType 流水操作人类型（coach=教练核销，admin=后台人工补核销）
     * @param operatorId   操作人 ID（教练 ID / 管理员 ID）
     * @param remark       流水备注（后台补核销会写明原因）
     * @return {deducted, couponId, title, remaining, reason}
     */
    public Map<String, Object> deductForCheckin(Integer userId, Integer studentId, Integer courseClassId,
                                                String ref, String operatorType, Integer operatorId,
                                                String remark) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("deducted", false);
        result.put("couponId", "");
        result.put("title", "");
        result.put("remaining", 0);
        result.put("reason", "");
        if (userId == null || userId <= 0 || studentId == null || studentId <= 0
                || courseClassId == null || courseClassId <= 0) {
            result.put("reason", "核销参数不完整");
            return result;
        }
        CourseClass course = courseClassMapper.selectById(courseClassId);
        if (course == null) {
            result.put("reason", "课程不存在");
            return result;
        }

        long now = now();
        // 持券学员本人或未绑定学员的券都参与自动匹配（与 mock「未绑定或绑定该学员」一致）。
        List<CouponInstance> instances = couponInstanceMapper.selectList(new QueryWrapper<CouponInstance>()
                .eq("user_id", userId)
                .and(wrapper -> wrapper.eq("student_id", studentId)
                        .or().isNull("student_id")
                        .or().eq("student_id", 0))
                .orderByAsc("expire_time")
                .orderByAsc("id"));
        String firstReason = null;
        boolean hadCandidate = false;
        for (CouponInstance candidate : instances) {
            hadCandidate = true;
            CouponInstance locked = couponInstanceMapper.selectOne(new QueryWrapper<CouponInstance>()
                    .eq("id", candidate.getId())
                    .last("limit 1 for update"));
            if (locked == null) {
                continue;
            }
            String reason = checkApplies(locked, course, studentId, now);
            if (reason != null) {
                if (firstReason == null) {
                    firstReason = reason;
                }
                continue;
            }
            int before = locked.getRemaining() == null ? 0 : locked.getRemaining();
            int after = before - 1;
            locked.setRemaining(after);
            locked.setStatus(after <= 0 ? STATUS_USED : STATUS_VALID);
            locked.setUpdateTime(now);
            couponInstanceMapper.updateById(locked);

            CouponFlow flow = new CouponFlow();
            flow.setCouponId(locked.getId());
            flow.setUserId(userId);
            flow.setStudentId(studentId);
            flow.setType(FLOW_CONSUME);
            flow.setTimes(-1);
            flow.setBalance(after);
            flow.setOrderId(locked.getOrderId());
            flow.setOrderSn("");
            flow.setRef(ref == null ? "" : ref);
            flow.setOperatorType(operatorType == null || operatorType.trim().isEmpty() ? "system" : operatorType);
            flow.setOperatorId(operatorId == null ? 0 : operatorId);
            flow.setRemark(remark == null ? "" : remark);
            flow.setCreateTime(now);
            couponFlowMapper.insert(flow);

            result.put("deducted", true);
            result.put("couponId", String.valueOf(locked.getId()));
            result.put("title", locked.getTitle() == null ? "" : locked.getTitle());
            result.put("remaining", after);
            result.put("reason", "");
            return result;
        }
        result.put("reason", firstReason != null ? firstReason
                : (hadCandidate ? "券次数已用完" : "无可用次数券"));
        return result;
    }

    /**
     * 只读预览：不扣券，返回本学员本次核销会命中的券与次数，供后台补核销弹窗提示。
     * 返回 {available, couponId, title, remaining, reason}。
     */
    public Map<String, Object> previewForCheckin(Integer userId, Integer studentId, Integer courseClassId) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("available", false);
        result.put("couponId", "");
        result.put("title", "");
        result.put("remaining", 0);
        result.put("reason", "");
        if (userId == null || userId <= 0 || studentId == null || studentId <= 0
                || courseClassId == null || courseClassId <= 0) {
            result.put("reason", "核销参数不完整");
            return result;
        }
        CourseClass course = courseClassMapper.selectById(courseClassId);
        if (course == null) {
            result.put("reason", "课程不存在");
            return result;
        }
        long now = now();
        List<CouponInstance> instances = couponInstanceMapper.selectList(new QueryWrapper<CouponInstance>()
                .eq("user_id", userId)
                .and(wrapper -> wrapper.eq("student_id", studentId)
                        .or().isNull("student_id")
                        .or().eq("student_id", 0))
                .orderByAsc("expire_time")
                .orderByAsc("id"));
        String firstReason = null;
        boolean hadCandidate = false;
        for (CouponInstance instance : instances) {
            hadCandidate = true;
            String reason = checkApplies(instance, course, studentId, now);
            if (reason == null) {
                result.put("available", true);
                result.put("couponId", String.valueOf(instance.getId()));
                result.put("title", instance.getTitle() == null ? "" : instance.getTitle());
                result.put("remaining", instance.getRemaining() == null ? 0 : instance.getRemaining());
                result.put("reason", "");
                return result;
            }
            if (firstReason == null) {
                firstReason = reason;
            }
        }
        result.put("reason", firstReason != null ? firstReason
                : (hadCandidate ? "券次数已用完" : "无可用次数券"));
        return result;
    }

    /**
     * 券适用规则。返回 null 表示适用，否则返回不可用原因。
     */
    public String checkApplies(CouponInstance instance, CourseClass course, Integer studentId, long now) {
        if (instance == null || course == null) {
            return "券或课程不存在";
        }
        if (instance.getStatus() == null || instance.getStatus() != STATUS_VALID) {
            return "券状态不可用";
        }
        if (instance.getRemaining() == null || instance.getRemaining() <= 0) {
            return "券次数已用完";
        }
        if (instance.getStartTime() != null && instance.getStartTime() > now) {
            return "券尚未生效";
        }
        if (instance.getExpireTime() != null && instance.getExpireTime() < now) {
            return "券已过期";
        }
        if (instance.getStudentId() != null && instance.getStudentId() > 0
                && studentId != null && studentId > 0
                && !instance.getStudentId().equals(studentId)) {
            return "券不属于该学员";
        }

        String courseType = courseType(course);
        String scopeType = instance.getScopeType() == null ? "all" : instance.getScopeType();
        if ("private".equals(scopeType) && !"private".equals(courseType)) {
            return "该券仅限私教课使用";
        }
        if ("group".equals(scopeType) && !"group".equals(courseType)) {
            return "该券仅限团课使用";
        }
        if ("trial".equals(scopeType) && "private".equals(courseType)) {
            return "体验券不能用于私教课";
        }

        List<Integer> courseIds = parseIds(instance.getCourseIds());
        if (!courseIds.isEmpty() && !courseIds.contains(course.getId())) {
            return "该券不适用于本课程";
        }
        List<Integer> areaIds = parseIds(instance.getAreaIds());
        if (!areaIds.isEmpty() && (course.getAreaId() == null || !areaIds.contains(course.getAreaId()))) {
            return "该券不适用于本小区";
        }
        return null;
    }

    /**
     * 课程类型：私教 / 体验 / 团课。
     *
     * <p>现有 la_course_class 没有标准的班型字段：一对一/一对二/一对三/私教按名称识别，
     * class_type=1 为体验班，其余团课。券的适用判断只依赖这里，未来补字段时改一处即可。</p>
     */
    public String courseType(CourseClass course) {
        String name = course == null || course.getName() == null ? "" : course.getName();
        if (name.contains("一对一") || name.contains("一对二") || name.contains("一对三")
                || name.contains("私教")) {
            return "private";
        }
        if (course.getClassType() != null && course.getClassType() == 1) {
            return "trial";
        }
        return "group";
    }

    private List<Integer> parseIds(String text) {
        List<Integer> ids = new ArrayList<>();
        if (text == null || text.trim().isEmpty()) {
            return ids;
        }
        for (String part : text.split(",")) {
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

    private long now() {
        return System.currentTimeMillis() / 1000L;
    }
}
