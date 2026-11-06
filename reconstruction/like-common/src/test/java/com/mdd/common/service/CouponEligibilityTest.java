package com.mdd.common.service;

import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * #221：CouponEligibility 唯一券可用性判定 —— 规则 seam 断言器。
 *
 * <p>本仓库离线 Maven 仓库没有 junit / surefire provider，按仓库惯例写成
 * 「带 main 的纯 Java 断言器」，由 tools/coupon/run-coupon-eligibility-test.sh 执行。</p>
 *
 * <p>验证公共 seam（{@link CouponEligibility#checkApplies} / {@link CouponEligibility#checkUsable}
 * / {@link CouponEligibility#evaluate} / {@link CouponEligibility#countUsable}）：
 * 未生效 / 过期 / 用完 / 状态 / 学员不符 / 范围不符（班型、课程白名单、小区白名单）各给出
 * 同一口径的中文原因；course 为空时跳过范围判定（预约凭证计数口径）；候选按到期最早优先。
 * 期望值全部来自独立事实（规则文案字面量与已知边界），不重算实现。Mapper 用动态代理注入，
 * 不连数据库。</p>
 */
public final class CouponEligibilityTest {

    /** 固定「当前时间」：所有夹具相对它构造，避免依赖真实时钟。 */
    private static final long NOW = 1800000000L;

    private static int passed;
    private static int failed;

    public static void main(String[] args) throws Exception {
        ownStateReasons();
        boundarySeconds();
        scopeReasons();
        courseNullSkipsScope();
        courseTypeTable();
        evaluatePartitionsAndOrders();
        countUsable();

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    // ---- 券自身状态（预约凭证 / 核销 / 后台三口径共享的判定） ------------------

    private static void ownStateReasons() throws Exception {
        System.out.println("[券自身状态 → 固定原因]");
        CouponEligibility eligibility = eligibility();

        check("未生效 → 券尚未生效",
                CouponEligibility.REASON_NOT_STARTED.equals("券尚未生效")
                        && "券尚未生效".equals(eligibility.checkUsable(instance(1800001000L, NOW + 86400L), 0, NOW)),
                "");
        check("过期 → 券已过期",
                CouponEligibility.REASON_EXPIRED.equals("券已过期")
                        && "券已过期".equals(eligibility.checkUsable(instance(0, NOW - 1L), 0, NOW)),
                "");
        check("用完 → 券次数已用完",
                CouponEligibility.REASON_EXHAUSTED.equals("券次数已用完")
                        && "券次数已用完".equals(eligibility.checkUsable(
                        withRemaining(instance(0, NOW + 86400L), 0), 0, NOW)),
                "");
        check("状态非有效 → 券状态不可用",
                "券状态不可用".equals(eligibility.checkUsable(
                        withStatus(instance(0, NOW + 86400L), 2), 0, NOW)),
                "");
        check("学员不符 → 券不属于该学员",
                CouponEligibility.REASON_STUDENT_MISMATCH.equals("券不属于该学员")
                        && "券不属于该学员".equals(eligibility.checkUsable(
                        withStudent(instance(0, NOW + 86400L), 9002), 9001, NOW)),
                "");
        check("有效券 → null（可用）",
                eligibility.checkUsable(instance(0, NOW + 86400L), 0, NOW) == null,
                "");
    }

    private static void boundarySeconds() throws Exception {
        System.out.println("[边界秒：start=now / expire=now 均可用]");
        CouponEligibility eligibility = eligibility();
        check("start_time == now 可用（仅 start > now 才算未生效）",
                eligibility.checkApplies(instance(NOW, NOW + 86400L), course("团课 A", 0, 0), 0, NOW) == null,
                "");
        check("expire_time == now 可用（仅 expire < now 才算过期）",
                eligibility.checkApplies(instance(0, NOW), course("团课 A", 0, 0), 0, NOW) == null,
                "");
    }

    // ---- 课程范围（核销 / mock / 后台配置共享的判定） --------------------------

    private static void scopeReasons() throws Exception {
        System.out.println("[课程范围 → 固定原因]");
        CouponEligibility eligibility = eligibility();

        check("private 券 × 团课 → 该券仅限私教课使用",
                "该券仅限私教课使用".equals(eligibility.checkApplies(
                        withScope(instance(0, NOW + 86400L), CouponEligibility.SCOPE_PRIVATE),
                        course("少儿篮球启蒙班", 0, 0), 0, NOW)),
                "");
        check("group 券 × 私教课 → 该券仅限团课使用",
                "该券仅限团课使用".equals(eligibility.checkApplies(
                        withScope(instance(0, NOW + 86400L), CouponEligibility.SCOPE_GROUP),
                        course("私教一对一", 0, 0), 0, NOW)),
                "");
        check("trial 券 × 私教课 → 体验券不能用于私教课",
                "体验券不能用于私教课".equals(eligibility.checkApplies(
                        withScope(instance(0, NOW + 86400L), CouponEligibility.SCOPE_TRIAL),
                        course("私教一对一", 0, 0), 0, NOW)),
                "");
        check("trial 券 × 团课 → 可用（体验不限定团课）",
                eligibility.checkApplies(
                        withScope(instance(0, NOW + 86400L), CouponEligibility.SCOPE_TRIAL),
                        course("少儿篮球启蒙班", 0, 0), 0, NOW) == null,
                "");
        check("课程白名单不含本课程 → 该券不适用于本课程",
                "该券不适用于本课程".equals(eligibility.checkApplies(
                        withCourseIds(instance(0, NOW + 86400L), "11,12"),
                        course("少儿篮球启蒙班", 0, 21), 0, NOW)),
                "");
        check("小区白名单不含本小区 → 该券不适用于本小区",
                "该券不适用于本小区".equals(eligibility.checkApplies(
                        withAreaIds(instance(0, NOW + 86400L), "31"),
                        course("少儿篮球启蒙班", 0, 21), 0, NOW)),
                "");
        check("scope 缺省按 all 处理",
                eligibility.checkApplies(withScope(instance(0, NOW + 86400L), null),
                        course("少儿篮球启蒙班", 0, 21), 0, NOW) == null,
                "");
    }

    private static void courseNullSkipsScope() throws Exception {
        System.out.println("[course 为空：跳过范围判定（预约凭证计数口径）]");
        CouponEligibility eligibility = eligibility();
        CouponInstance privateCoupon = withScope(instance(0, NOW + 86400L), CouponEligibility.SCOPE_PRIVATE);
        check("私教券在无课程上下文时仍计入凭证",
                eligibility.checkApplies(privateCoupon, null, 0, NOW) == null,
                "");
        check("checkUsable 与 course=null 的 checkApplies 同判",
                eligibility.checkUsable(privateCoupon, 0, NOW) == null,
                "");
        check("过期券在无课程上下文时仍被拒",
                "券已过期".equals(eligibility.checkApplies(instance(0, NOW - 1L), null, 0, NOW)),
                "");
    }

    private static void courseTypeTable() throws Exception {
        System.out.println("[courseType 判定表（核销 / 后台配置共享）]");
        CouponEligibility eligibility = eligibility();
        check("名称含一对一 → private",
                CouponEligibility.SCOPE_PRIVATE.equals(eligibility.courseType(course("少儿一对一体能", 0, 0))),
                "");
        check("名称含私教 → private",
                CouponEligibility.SCOPE_PRIVATE.equals(eligibility.courseType(course("私教体能课", 0, 0))),
                "");
        check("class_type=1 → trial",
                CouponEligibility.SCOPE_TRIAL.equals(eligibility.courseType(course("体验班", 1, 0))),
                "");
        check("其余 → group",
                CouponEligibility.SCOPE_GROUP.equals(eligibility.courseType(course("少儿篮球启蒙班", 0, 0))),
                "");
    }

    // ---- evaluate：可用/不可用分区 + 候选顺序 ----------------------------------

    private static void evaluatePartitionsAndOrders() throws Exception {
        System.out.println("[evaluate：可用/不可用分区 + 到期最早优先]");
        List<CouponInstance> fixtures = new ArrayList<>();
        // 到期最早的有效券（应排第一且可用）
        fixtures.add(withScope(withExpire(instance(0, NOW + 100L), 8801), CouponEligibility.SCOPE_GROUP));
        // 到期更晚的有效券
        fixtures.add(withScope(withExpire(instance(0, NOW + 90000L), 8802), CouponEligibility.SCOPE_GROUP));
        // 过期券
        fixtures.add(withExpire(instance(NOW - 10L, NOW - 86400L), 8803));
        // 用完券
        fixtures.add(withExpire(withRemaining(instance(0, NOW + 86400L), 0), 8804));

        CouponEligibility eligibility = eligibility(fixtures);
        CouponEligibility.Evaluation eval = eligibility.evaluate(7001, 8001,
                course("少儿篮球启蒙班", 0, 21), NOW);

        check("可用券 2 张", eval.getEligible().size() == 2, String.valueOf(eval.getEligible().size()));
        check("可用券按到期最早优先", eval.getEligible().get(0).getId() == 8801
                && eval.getEligible().get(1).getId() == 8802, eligibleIds(eval));
        check("不可用券 2 张", eval.getIneligible().size() == 2,
                String.valueOf(eval.getIneligible().size()));
        check("过期券原因 = 券已过期",
                "券已过期".equals(firstIneligibleReason(eval, 8803)), ineligibleText(eval));
        check("用完券原因 = 券次数已用完",
                "券次数已用完".equals(firstIneligibleReason(eval, 8804)), ineligibleText(eval));
        check("firstReason 取候选顺序第一个不可用原因",
                "券已过期".equals(eval.firstReason()), String.valueOf(eval.firstReason()));
        check("hasCandidates=true", eval.hasCandidates(), "");
    }

    private static void countUsable() throws Exception {
        System.out.println("[countUsable：预约凭证计数口径]");
        List<CouponInstance> fixtures = new ArrayList<>();
        fixtures.add(withScope(instance(0, NOW + 86400L), CouponEligibility.SCOPE_PRIVATE)); // 范围券也计入
        fixtures.add(instance(0, NOW - 86400L));                                            // 过期不计入
        CouponEligibility eligibility = eligibility(fixtures);
        check("私教券在无课程上下文时计入凭证 → count=1",
                eligibility.countUsable(7001, 8001, NOW) == 1L,
                String.valueOf(eligibility.countUsable(7001, 8001, NOW)));
    }

    // ---- 夹具 ------------------------------------------------------------------

    private static CouponInstance instance(long startTime, long expireTime) {
        CouponInstance instance = new CouponInstance();
        instance.setId(8803);
        instance.setUserId(7001);
        instance.setStudentId(0);
        instance.setTitle("团课单次券");
        instance.setTotal(1);
        instance.setRemaining(1);
        instance.setStatus(CouponEligibility.STATUS_VALID);
        instance.setScopeType(CouponEligibility.SCOPE_GROUP);
        instance.setCourseIds("");
        instance.setAreaIds("");
        instance.setStartTime(startTime == 0 ? null : startTime);
        instance.setExpireTime(expireTime);
        return instance;
    }

    private static CouponInstance withRemaining(CouponInstance instance, int remaining) {
        instance.setRemaining(remaining);
        return instance;
    }

    private static CouponInstance withStatus(CouponInstance instance, int status) {
        instance.setStatus(status);
        return instance;
    }

    private static CouponInstance withStudent(CouponInstance instance, int studentId) {
        instance.setStudentId(studentId);
        return instance;
    }

    private static CouponInstance withScope(CouponInstance instance, String scopeType) {
        instance.setScopeType(scopeType);
        return instance;
    }

    private static CouponInstance withCourseIds(CouponInstance instance, String courseIds) {
        instance.setCourseIds(courseIds);
        return instance;
    }

    private static CouponInstance withAreaIds(CouponInstance instance, String areaIds) {
        instance.setAreaIds(areaIds);
        return instance;
    }

    private static CouponInstance withExpire(CouponInstance instance, int id) {
        instance.setId(id);
        return instance;
    }

    private static CourseClass course(String name, Integer classType, Integer areaId) {
        CourseClass course = new CourseClass();
        course.setId(5001);
        course.setName(name);
        course.setClassType(classType);
        course.setAreaId(areaId);
        return course;
    }

    private static CouponEligibility eligibility() throws Exception {
        return eligibility(new ArrayList<>());
    }

    private static CouponEligibility eligibility(List<CouponInstance> fixtures) throws Exception {
        CouponEligibility eligibility = new CouponEligibility();
        set(eligibility, "couponInstanceMapper", proxy(CouponInstanceMapper.class, (self, method, args) -> {
            if ("selectList".equals(method.getName())) {
                return fixtures;
            }
            return emptyListFor(method);
        }));
        return eligibility;
    }

    private static String eligibleIds(CouponEligibility.Evaluation eval) {
        StringBuilder text = new StringBuilder();
        for (CouponInstance instance : eval.getEligible()) {
            text.append(instance.getId()).append(",");
        }
        return text.toString();
    }

    private static String firstIneligibleReason(CouponEligibility.Evaluation eval, int couponId) {
        return eval.getIneligible().get(couponId);
    }

    private static String ineligibleText(CouponEligibility.Evaluation eval) {
        return eval.getIneligible().toString();
    }

    private static Object emptyListFor(Method method) {
        if (method.getReturnType() == Long.TYPE || method.getReturnType() == Long.class) {
            return 0L;
        }
        return Collections.emptyList();
    }

    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
    }

    private static void set(Object target, String field, Object value) throws Exception {
        Field declared = CouponEligibility.class.getDeclaredField(field);
        declared.setAccessible(true);
        declared.set(target, value);
    }

    private static void check(String name, boolean ok, String detail) {
        if (ok) {
            passed += 1;
            System.out.println("  ✓ " + name + (detail.isEmpty() ? "" : "（" + detail + "）"));
        } else {
            failed += 1;
            System.out.println("  ✗ " + name + (detail.isEmpty() ? "" : "（" + detail + "）"));
        }
    }
}
