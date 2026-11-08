package com.mdd.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * #221 券可用性唯一判定模块。
 *
 * <p>「券是否可用」原先在预约凭证（like-front BookingServiceImpl.ensureBookingCredential）、
 * 核销选券（CouponCheckinService 查询 + checkApplies）、后台发券（CouponGoodsServiceImpl
 * 商品/范围校验）、小程序 mock（services/db.js couponApplies）各写一份；本模块收敛为唯一实现，
 * 四个调用点全部改引这里（mock 为 JS，按注释声明镜像本模块）。</p>
 *
 * <p>输入（userId、studentId、courseClass 可空、now），输出可用券列表与不可用原因：</p>
 * <ul>
 *   <li>{@link #checkUsable}：券自身状态 + 学员归属（未生效/过期/用完/状态/不属于学员），
 *       不依赖课程上下文 —— 预约凭证计数口径（一张券对应一节课的凭证，不判课程范围）；</li>
 *   <li>{@link #checkApplies}：checkUsable + 课程范围判定（班型 / 课程白名单 / 小区白名单）
 *       —— 核销选券与券详情口径；course 传 null 时退化为 checkUsable；</li>
 *   <li>{@link #evaluate}：一次性给出候选券的可用/不可用分区（核销选券、预检共用）；</li>
 *   <li>{@link #countUsable}：预约凭证计数（课程范围不敏感）。</li>
 * </ul>
 *
 * <p>规则与小程序 mock / 金样保持一致：</p>
 * <ul>
 *   <li>券状态有效、剩余次数 &gt; 0、在有效期内（仅 start &gt; now 算未生效，仅 expire &lt; now 算过期）；</li>
 *   <li>scopeType=private 只认私教课；group 只认团课；trial 不认私教课；</li>
 *   <li>courseIds / areaIds 非空时做白名单匹配；</li>
 *   <li>传了 studentId 时必须与持券学员一致（未绑定学员的券也参与匹配）；</li>
 *   <li>候选券按「到期最早优先，同到期按 id 升序」。</li>
 * </ul>
 *
 * <p>常量（{@link #SCOPE_ALL} 等）是范围口径的唯一来源：后台发券直接引用，
 * 小程序 mock db.js 镜像注释声明；改动这里即全链路生效。</p>
 */
@Service
public class CouponEligibility {

    /** 券实例状态：1=有效（发券侧落库同值）。 */
    public static final int STATUS_VALID = 1;

    /** 适用范围：全部课程。 */
    public static final String SCOPE_ALL = "all";
    /** 适用范围：仅团课。 */
    public static final String SCOPE_GROUP = "group";
    /** 适用范围：仅私教课。 */
    public static final String SCOPE_PRIVATE = "private";
    /** 适用范围：体验课（不认私教课）。 */
    public static final String SCOPE_TRIAL = "trial";

    /** 不可用原因：尚未生效。 */
    public static final String REASON_NOT_STARTED = "券尚未生效";
    /** 不可用原因：已过期。 */
    public static final String REASON_EXPIRED = "券已过期";
    /** 不可用原因：次数用完。 */
    public static final String REASON_EXHAUSTED = "券次数已用完";
    /** 不可用原因：状态非有效。 */
    public static final String REASON_STATUS_INVALID = "券状态不可用";
    /** 不可用原因：不属于该学员。 */
    public static final String REASON_STUDENT_MISMATCH = "券不属于该学员";
    /** 不可用原因：范围不符（私教券用于非私教课）。 */
    public static final String REASON_PRIVATE_ONLY = "该券仅限私教课使用";
    /** 不可用原因：范围不符（团课券用于非团课）。 */
    public static final String REASON_GROUP_ONLY = "该券仅限团课使用";
    /** 不可用原因：范围不符（体验券用于私教课）。 */
    public static final String REASON_TRIAL_NOT_PRIVATE = "体验券不能用于私教课";
    /** 不可用原因：范围不符（课程白名单）。 */
    public static final String REASON_COURSE_NOT_IN_SCOPE = "该券不适用于本课程";
    /** 不可用原因：范围不符（小区白名单）。 */
    public static final String REASON_AREA_NOT_IN_SCOPE = "该券不适用于本小区";
    /** 不可用原因：入参缺失（券或课程实体不存在）。 */
    public static final String REASON_MISSING = "券或课程不存在";

    @Resource
    CouponInstanceMapper couponInstanceMapper;

    /**
     * 券自身 + 学员归属判定（无课程上下文）。返回 null 表示可用，否则返回不可用原因。
     *
     * <p>预约凭证计数口径：只关心「这张券还能不能算一节课的凭证」，
     * 不判课程范围（范围口径见 {@link #checkApplies}）。</p>
     */
    public String checkUsable(CouponInstance instance, Integer studentId, long now) {
        if (instance == null) {
            return REASON_MISSING;
        }
        if (instance.getStatus() == null || instance.getStatus() != STATUS_VALID) {
            return REASON_STATUS_INVALID;
        }
        if (instance.getRemaining() == null || instance.getRemaining() <= 0) {
            return REASON_EXHAUSTED;
        }
        if (instance.getStartTime() != null && instance.getStartTime() > now) {
            return REASON_NOT_STARTED;
        }
        if (instance.getExpireTime() != null && instance.getExpireTime() < now) {
            return REASON_EXPIRED;
        }
        if (instance.getStudentId() != null && instance.getStudentId() > 0
                && studentId != null && studentId > 0
                && !instance.getStudentId().equals(studentId)) {
            return REASON_STUDENT_MISMATCH;
        }
        return null;
    }

    /**
     * 券适用规则（完整口径）。返回 null 表示适用，否则返回不可用原因。
     *
     * <p>course 传 null 表示无课程上下文（预约凭证计数），此时跳过课程范围判定，
     * 等价于 {@link #checkUsable}。</p>
     */
    public String checkApplies(CouponInstance instance, CourseClass course, Integer studentId, long now) {
        String ownReason = checkUsable(instance, studentId, now);
        if (ownReason != null) {
            return ownReason;
        }
        if (course == null) {
            return null;
        }

        String courseType = courseType(course);
        String scopeType = instance.getScopeType() == null ? SCOPE_ALL : instance.getScopeType();
        if (SCOPE_PRIVATE.equals(scopeType) && !SCOPE_PRIVATE.equals(courseType)) {
            return REASON_PRIVATE_ONLY;
        }
        if (SCOPE_GROUP.equals(scopeType) && !SCOPE_GROUP.equals(courseType)) {
            return REASON_GROUP_ONLY;
        }
        if (SCOPE_TRIAL.equals(scopeType) && SCOPE_PRIVATE.equals(courseType)) {
            return REASON_TRIAL_NOT_PRIVATE;
        }

        List<Integer> courseIds = parseIds(instance.getCourseIds());
        if (!courseIds.isEmpty() && !courseIds.contains(course.getId())) {
            return REASON_COURSE_NOT_IN_SCOPE;
        }
        List<Integer> areaIds = parseIds(instance.getAreaIds());
        if (!areaIds.isEmpty() && (course.getAreaId() == null || !areaIds.contains(course.getAreaId()))) {
            return REASON_AREA_NOT_IN_SCOPE;
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
            return SCOPE_PRIVATE;
        }
        if (course.getClassType() != null && course.getClassType() == 1) {
            return SCOPE_TRIAL;
        }
        return SCOPE_GROUP;
    }

    /**
     * 候选券查询（预约凭证 / 核销选券共用）：该学员的券 + 账号级券（student_id NULL / 0），
     * 到期最早优先，同到期按 id 升序。
     */
    public List<CouponInstance> listCandidates(Integer userId, Integer studentId) {
        return couponInstanceMapper.selectList(new QueryWrapper<CouponInstance>()
                .eq("user_id", userId)
                .and(wrapper -> wrapper.eq("student_id", studentId)
                        .or().isNull("student_id")
                        .or().eq("student_id", 0))
                .orderByAsc("expire_time")
                .orderByAsc("id"));
    }

    /**
     * 一次性判定：候选券分区为可用 / 不可用（含原因）。
     *
     * @param course   课程上下文，可空（null 时退化为凭证口径，不判范围）
     * @param now      判定时间戳（秒）
     * @return {@link Evaluation}，候选为空时 eligible/ineligible 均为空
     */
    public Evaluation evaluate(Integer userId, Integer studentId, CourseClass course, long now) {
        List<CouponInstance> candidates = listCandidates(userId, studentId);
        List<CouponInstance> eligible = new ArrayList<>();
        Map<Integer, String> ineligible = new LinkedHashMap<>();
        for (CouponInstance candidate : candidates) {
            String reason = checkApplies(candidate, course, studentId, now);
            if (reason == null) {
                eligible.add(candidate);
            } else {
                ineligible.put(candidate.getId(), reason);
            }
        }
        return new Evaluation(candidates, eligible, ineligible);
    }

    /**
     * 预约凭证计数：该学员 + 账号级可用次数券张数（不判课程范围）。
     * 供 BookingServiceImpl 凭证校验使用（老客户存量课包仍由调用方单独累计）。
     */
    public long countUsable(Integer userId, Integer studentId, long now) {
        long count = 0L;
        for (CouponInstance candidate : listCandidates(userId, studentId)) {
            if (checkUsable(candidate, studentId, now) == null) {
                count += 1;
            }
        }
        return count;
    }

    /**
     * 判定结果：候选顺序保持「到期最早优先」。
     * {@code firstReason()} 取候选顺序里第一个不可用原因，供核销兜底文案使用。
     */
    public static final class Evaluation {

        private final List<CouponInstance> candidates;
        private final List<CouponInstance> eligible;
        private final Map<Integer, String> ineligible;

        Evaluation(List<CouponInstance> candidates, List<CouponInstance> eligible,
                   Map<Integer, String> ineligible) {
            this.candidates = candidates == null ? Collections.<CouponInstance>emptyList() : candidates;
            this.eligible = eligible == null ? Collections.<CouponInstance>emptyList() : eligible;
            this.ineligible = ineligible == null
                    ? Collections.<Integer, String>emptyMap() : ineligible;
        }

        /** 全部候选券（含不可用），按到期最早优先排序。 */
        public List<CouponInstance> getCandidates() {
            return candidates;
        }

        /** 可用券列表（候选顺序）。 */
        public List<CouponInstance> getEligible() {
            return eligible;
        }

        /** 不可用券：couponId → 原因（候选顺序）。 */
        public Map<Integer, String> getIneligible() {
            return ineligible;
        }

        /** 候选顺序里第一个不可用原因；全部可用返回 null。 */
        public String firstReason() {
            for (String reason : ineligible.values()) {
                return reason;
            }
            return null;
        }

        /** 是否存在候选券（含不可用的）。 */
        public boolean hasCandidates() {
            return !candidates.isEmpty();
        }
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
}
