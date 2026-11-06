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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 核销扣券公共能力（T30.4 / #37）。
 *
 * <p>教练端核销（#26）与后台人工补核销共用同一套「逐人自动匹配一张可用次数券、
 * 扣 1 次、追加只增流水」的口径，避免两条链路各写一份导致次数/考勤不一致。</p>
 *
 * <p>券可用性判定自 #221 起收敛到 {@link CouponEligibility}（唯一实现）：
 * 候选查询、单券判定、不可用原因全部走该模块，本服务只保留核销动作
 * （FOR UPDATE 加锁复核、扣次数、写流水）。</p>
 *
 * <p>扣减前 {@code SELECT ... FOR UPDATE} 重新加锁并复核，必须由外层事务调用；
 * 异常随外层事务整体回滚。无可用券返回 {@code deducted=false} 与中文原因，
 * 由调用方按「不阻断核销」处理。</p>
 */
@Service
public class CouponCheckinService {

    private static final int STATUS_VALID = CouponEligibility.STATUS_VALID;
    /** 券实例状态：2=已用完（核销扣至 0 后落库值）。 */
    private static final int STATUS_USED = 2;
    private static final int FLOW_CONSUME = 2;

    @Resource
    CouponInstanceMapper couponInstanceMapper;
    @Resource
    CouponFlowMapper couponFlowMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CouponEligibility couponEligibility;

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
        // 可用性判定统一走 CouponEligibility（#221）：学员本人 + 账号级券参与匹配，
        // 到期最早优先；不可用原因（未生效/过期/用完/范围不符/学员不符）由其给出。
        CouponEligibility.Evaluation evaluation = couponEligibility.evaluate(userId, studentId, course, now);
        String firstReason = evaluation.firstReason();
        for (CouponInstance candidate : evaluation.getEligible()) {
            CouponInstance locked = couponInstanceMapper.selectOne(new QueryWrapper<CouponInstance>()
                    .eq("id", candidate.getId())
                    .last("limit 1 for update"));
            if (locked == null) {
                continue;
            }
            String reason = couponEligibility.checkApplies(locked, course, studentId, now);
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
                : (evaluation.hasCandidates() ? CouponEligibility.REASON_EXHAUSTED : "无可用次数券"));
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
        CouponEligibility.Evaluation evaluation = couponEligibility.evaluate(userId, studentId, course, now);
        if (!evaluation.getEligible().isEmpty()) {
            CouponInstance instance = evaluation.getEligible().get(0);
            result.put("available", true);
            result.put("couponId", String.valueOf(instance.getId()));
            result.put("title", instance.getTitle() == null ? "" : instance.getTitle());
            result.put("remaining", instance.getRemaining() == null ? 0 : instance.getRemaining());
            result.put("reason", "");
            return result;
        }
        result.put("reason", evaluation.firstReason() != null ? evaluation.firstReason()
                : (evaluation.hasCandidates() ? CouponEligibility.REASON_EXHAUSTED : "无可用次数券"));
        return result;
    }

    /**
     * 券适用规则。返回 null 表示适用，否则返回不可用原因。
     *
     * <p>#221 起实现收敛到 {@link CouponEligibility#checkApplies}，保留本方法
     * 维持既有调用方（券详情 bestCoupon / applies 等）的签名不变。</p>
     */
    public String checkApplies(CouponInstance instance, CourseClass course, Integer studentId, long now) {
        return couponEligibility.checkApplies(instance, course, studentId, now);
    }

    /**
     * 课程类型：私教 / 体验 / 团课（#221 起实现收敛到 {@link CouponEligibility#courseType}）。
     */
    public String courseType(CourseClass course) {
        return couponEligibility.courseType(course);
    }

    private long now() {
        return System.currentTimeMillis() / 1000L;
    }
}
