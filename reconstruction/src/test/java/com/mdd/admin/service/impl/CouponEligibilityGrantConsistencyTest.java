package com.mdd.admin.service.impl;

import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.coupon.CouponGoods;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.region.AreaMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.coupon.CouponGoodsMapper;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;
import com.mdd.common.service.CouponCheckinService;
import com.mdd.common.service.CouponEligibility;
import com.mdd.admin.validate.coupon.CouponGoodsCreateValidate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * #221 后台发券口径一致断言器：发券侧（商品配置 + 发放落库）与唯一判定模块同源。
 *
 * <p>本仓库离线 Maven 仓库没有 junit / surefire provider，按仓库惯例写成
 * 「带 main 的纯 Java 断言器」，由 tools/coupon/run-coupon-eligibility-grant-test.sh 执行。</p>
 *
 * <p>三口径中的「后台发券」seam：</p>
 * <ul>
 *   <li>商品配置：{@code CouponGoodsServiceImpl#add} 的范围校验（trial 不能限定私教课）
 *       —— 班型判定已委托 {@link CouponEligibility#courseType}，常量引用 SCOPE_*；</li>
 *   <li>发放落库：{@code buildGrantInstance} 产出的券实例（status=STATUS_VALID、
 *       remaining=1、scopeType 透传）在 {@link CouponEligibility#checkApplies} 与
 *       {@link CouponCheckinService#previewForCheckin}（核销口径）下结论一致。</li>
 * </ul>
 *
 * <p>Mapper 用动态代理注入，不连数据库；发券幂等键本身由 CouponGrantIdempotentKeyTest 覆盖。</p>
 */
public final class CouponEligibilityGrantConsistencyTest {

    private static final int USER_ID = 7001;
    private static final int STUDENT_ID = 0;
    private static final long NOW = System.currentTimeMillis() / 1000L;

    private static int passed;
    private static int failed;

    public static void main(String[] args) throws Exception {
        goodsScopeValidationSharesCourseType();
        grantedInstanceJudgedConsistently();

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    // ---- 场景 1：商品配置的范围校验与 CouponEligibility.courseType 同源 ----------

    private static void goodsScopeValidationSharesCourseType() throws Exception {
        System.out.println("[商品配置：trial 不能限定私教课（共享 courseType 判定）]");
        CouponGoodsServiceImpl service = grantService(new ArrayList<>(), privateCourse(), area());

        CouponEligibility eligibility = new CouponEligibility();
        check("共享判定：私教体验课 courseType=private",
                CouponEligibility.SCOPE_PRIVATE.equals(eligibility.courseType(privateCourse())), "");

        try {
            service.add(trialValidate("5001"));
            check("trial 商品限定私教课 → 应拒绝", false, "未抛异常");
        } catch (OperateException e) {
            check("trial 商品限定私教课 → 拒绝（体验券不能限定私教课程）",
                    String.valueOf(e.getMsg()).contains("体验券不能限定私教课程"), String.valueOf(e.getMsg()));
        }

        try {
            service.add(allValidateWithCourse("5001"));
            check("all 商品限定课程 → 应拒绝", false, "未抛异常");
        } catch (OperateException e) {
            check("all 商品限定课程 → 拒绝", String.valueOf(e.getMsg()).contains("不能指定限定课程"),
                    String.valueOf(e.getMsg()));
        }

        System.out.println("[商品配置：group 商品限定团课 → 落库 scopeType 引用同源常量]");
        List<CouponGoods> inserted = new ArrayList<>();
        CouponGoodsServiceImpl okService = grantService(inserted, privateCourse(), area());
        okService.add(groupValidate("5002"));
        check("group 商品 + 团课 → 创建成功且落库 1 条", inserted.size() == 1,
                String.valueOf(inserted.size()));
        check("落库 scopeType = CouponEligibility.SCOPE_GROUP",
                !inserted.isEmpty() && CouponEligibility.SCOPE_GROUP.equals(inserted.get(0).getScopeType()),
                inserted.isEmpty() ? "" : String.valueOf(inserted.get(0).getScopeType()));
    }

    private static void grantedInstanceJudgedConsistently() throws Exception {
        System.out.println("[发放落库：trial 券实例在唯一判定 / 核销口径下同判]");
        CouponGoodsServiceImpl service = grantService(new ArrayList<>(), privateCourse(), area());
        CouponGoods goods = trialGoods(null);

        CouponInstance granted = buildGrantInstance(service, goods);
        check("发券落库 status = CouponEligibility.STATUS_VALID",
                granted.getStatus() != null && granted.getStatus() == CouponEligibility.STATUS_VALID,
                String.valueOf(granted.getStatus()));
        check("发券落库 remaining=1", granted.getRemaining() != null && granted.getRemaining() == 1,
                String.valueOf(granted.getRemaining()));
        check("发券落库 scopeType 透传商品配置（同源常量）",
                CouponEligibility.SCOPE_TRIAL.equals(granted.getScopeType()),
                String.valueOf(granted.getScopeType()));

        CouponEligibility eligibility = new CouponEligibility();
        check("唯一判定：trial 券 × 私教课 → 体验券不能用于私教课",
                CouponEligibility.REASON_TRIAL_NOT_PRIVATE.equals(
                        eligibility.checkApplies(granted, privateCourse(), STUDENT_ID, NOW + 1)),
                "");
        check("唯一判定：trial 券 × 团课 → 可用",
                eligibility.checkApplies(granted, groupCourse(), STUDENT_ID, NOW + 1) == null, "");

        // 与核销口径联动：同一张后台发出的券，核销预检给出同一结论同一原因。
        CouponCheckinService checkin = checkinService(granted, privateCourse());
        Map<String, Object> preview = checkin.previewForCheckin(USER_ID, 8001, 5001);
        check("核销口径：同一张券被同因拒绝",
                !Boolean.TRUE.equals(preview.get("available"))
                        && CouponEligibility.REASON_TRIAL_NOT_PRIVATE.equals(preview.get("reason")),
                String.valueOf(preview.get("reason")));
    }

    // ---- 夹具 ------------------------------------------------------------------

    private static CouponGoodsCreateValidate baseValidate(String scopeType, String courseIds) {
        CouponGoodsCreateValidate validate = new CouponGoodsCreateValidate();
        validate.setCode("T-ELIGIBILITY-001");
        validate.setTitle("新人体验券");
        validate.setTimes(1);
        validate.setValidDays(30);
        validate.setScopeType(scopeType);
        validate.setScope("");
        validate.setCourseIds(courseIds == null ? "" : courseIds);
        validate.setAreaIds("");
        return validate;
    }

    private static CouponGoodsCreateValidate trialValidate(String courseIds) {
        return baseValidate(CouponEligibility.SCOPE_TRIAL, courseIds);
    }

    private static CouponGoodsCreateValidate allValidateWithCourse(String courseIds) {
        return baseValidate(CouponEligibility.SCOPE_ALL, courseIds);
    }

    private static CouponGoodsCreateValidate groupValidate(String courseIds) {
        return baseValidate(CouponEligibility.SCOPE_GROUP, courseIds);
    }

    private static CouponGoods trialGoods(String courseIds) {
        CouponGoods goods = new CouponGoods();
        goods.setId(45);
        goods.setTitle("新人体验券");
        goods.setCode("TRIAL-001");
        goods.setStatus(1);
        goods.setTimes(1);
        goods.setValidDays(30);
        goods.setScopeType(CouponEligibility.SCOPE_TRIAL);
        goods.setScope("体验课");
        goods.setCourseIds(courseIds == null ? "" : courseIds);
        goods.setAreaIds("");
        return goods;
    }

    private static CouponGoods allGoodsWithCourse(String courseIds) {
        CouponGoods goods = trialGoods(courseIds);
        goods.setScopeType(CouponEligibility.SCOPE_ALL);
        return goods;
    }

    private static CourseClass privateCourse() {
        CourseClass course = new CourseClass();
        course.setId(5001);
        course.setName("私教一对一");
        course.setClassType(0);
        course.setAreaId(21);
        course.setIsDelete(0);
        return course;
    }

    private static CourseClass groupCourse() {
        CourseClass course = new CourseClass();
        course.setId(5002);
        course.setName("少儿篮球启蒙班");
        course.setClassType(0);
        course.setAreaId(21);
        course.setIsDelete(0);
        return course;
    }

    private static com.mdd.common.entity.region.Area area() {
        com.mdd.common.entity.region.Area area = new com.mdd.common.entity.region.Area();
        area.setId(21);
        area.setName("测试小区");
        return area;
    }

    private static CouponGoodsServiceImpl grantService(List<CouponGoods> inserted,
                                                       CourseClass course,
                                                       com.mdd.common.entity.region.Area area) {
        CouponGoodsServiceImpl service = new CouponGoodsServiceImpl();
        setQuietly(service, "couponGoodsMapper", proxy(CouponGoodsMapper.class, (self, method, args1) -> {
            String name = method.getName();
            if ("selectCount".equals(name)) {
                return 0L;
            }
            if ("insert".equals(name)) {
                inserted.add((CouponGoods) args1[0]);
                return 1;
            }
            return emptyListFor(method);
        }));
        setQuietly(service, "courseClassMapper", proxy(CourseClassMapper.class, (self, method, args1) -> {
            if ("selectBatchIds".equals(method.getName())) {
                List<CourseClass> courses = new ArrayList<>();
                courses.add(course);
                courses.add(groupCourse());
                return courses;
            }
            return emptyListFor(method);
        }));
        setQuietly(service, "areaMapper", proxy(AreaMapper.class, (self, method, args1) -> {
            if ("selectBatchIds".equals(method.getName())) {
                List<com.mdd.common.entity.region.Area> areas = new ArrayList<>();
                areas.add(area);
                return areas;
            }
            return emptyListFor(method);
        }));
        setQuietly(service, "couponInstanceMapper", proxy(CouponInstanceMapper.class, (self, method, args1) -> {
            if ("randMakeCouponInstanceSn".equals(method.getName())) {
                return "SN-TEST-0001";
            }
            return emptyListFor(method);
        }));
        setQuietly(service, "couponEligibility", new CouponEligibility());
        return service;
    }

    private static CouponCheckinService checkinService(CouponInstance fixture, CourseClass course) {
        CouponCheckinService service = new CouponCheckinService();
        setQuietly(service, "courseClassMapper", proxy(CourseClassMapper.class, (self, method, args1) -> {
            if ("selectById".equals(method.getName())) {
                return course;
            }
            return emptyListFor(method);
        }));
        setQuietly(service, "couponEligibility", eligibility(fixture));
        return service;
    }

    private static CouponEligibility eligibility(CouponInstance fixture) {
        CouponEligibility eligibility = new CouponEligibility();
        List<CouponInstance> list = new ArrayList<>();
        list.add(fixture);
        setQuietly(eligibility, "couponInstanceMapper", proxy(CouponInstanceMapper.class, (self, method, args1) -> {
            if ("selectList".equals(method.getName())) {
                return list;
            }
            return emptyListFor(method);
        }));
        return eligibility;
    }

    /** 反射调用私有多参 buildGrantInstance（反射注入是本仓库断言器惯例）。 */
    private static CouponInstance buildGrantInstance(CouponGoodsServiceImpl service, CouponGoods goods)
            throws Exception {
        Method method = CouponGoodsServiceImpl.class.getDeclaredMethod("buildGrantInstance",
                Integer.class, int.class, CouponGoods.class, String.class, long.class);
        method.setAccessible(true);
        return (CouponInstance) method.invoke(service, USER_ID, STUDENT_ID, goods, "后台发放", NOW);
    }

    private static Object emptyListFor(Method method) {
        if (method.getReturnType() == Long.TYPE || method.getReturnType() == Long.class) {
            return 0L;
        }
        if (method.getReturnType() == Integer.TYPE || method.getReturnType() == Integer.class) {
            return 0;
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
    }

    private static void setQuietly(Object target, String field, Object value) {
        try {
            Field declared = target.getClass().getDeclaredField(field);
            declared.setAccessible(true);
            declared.set(target, value);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static void check(String name, boolean ok, String detail) {
        detail = detail == null ? "" : detail;
        if (ok) {
            passed += 1;
            System.out.println("  ✓ " + name + (detail.isEmpty() ? "" : "（" + detail + "）"));
        } else {
            failed += 1;
            System.out.println("  ✗ " + name + (detail.isEmpty() ? "" : "（" + detail + "）"));
        }
    }
}
