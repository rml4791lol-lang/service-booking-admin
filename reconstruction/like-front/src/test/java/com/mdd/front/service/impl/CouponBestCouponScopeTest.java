package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.student.Student;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.service.CouponCheckinService;
import com.mdd.common.service.CouponEligibility;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * #205 US11：bestCoupon 的学员范围必须包含账号级券。
 *
 * <p>本仓库离线 Maven 仓库没有 junit / surefire provider，按仓库惯例写成
 * 「带 main 的纯 Java 断言器」，由 tools/coupon/run-best-coupon-scope-test.sh 执行。</p>
 *
 * <p>验证的是 {@link CouponServiceImpl#bestCoupon} 发出的查询语义（公共方法 seam）：
 * 传了 studentId 时，候选券必须同时包含该学员的券、student_id IS NULL、student_id=0
 * 三种；与 CouponCheckinService 的核销口径一致。Mapper 用动态代理注入，不连数据库。</p>
 */
public final class CouponBestCouponScopeTest {

    private static int passed;
    private static int failed;
    private static String lastStudentSql = "";
    private static Map<String, Object> lastParams = Collections.emptyMap();

    public static void main(String[] args) throws Exception {
        studentScopeIncludesAccountLevelCoupons();
        accountLevelScopeIsNotFiltered();

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void studentScopeIncludesAccountLevelCoupons() throws Exception {
        System.out.println("[studentId>0：候选 = 该学员 + NULL + 0]");
        List<CouponInstance> candidates = new ArrayList<>();
        candidates.add(accountLevelCoupon());
        CouponServiceImpl service = serviceWith(candidates);

        Map<String, Object> best = service.bestCoupon(7001, "5001", null, "8001");
        check("账号级券能作为 bestCoupon 返回", best != null && "9001".equals(best.get("id")),
                String.valueOf(best));
        check("查询包含 student_id IS NULL（账号级）",
                lastStudentSql.contains("student_id IS NULL"), lastStudentSql);
        check("student_id 条件共 3 支（学员 / NULL / 0）",
                count(lastStudentSql, "student_id") == 3, lastStudentSql);
        check("查询参数包含该学员 id",
                lastParams.containsValue(8001) || String.valueOf(lastParams).contains("8001"),
                String.valueOf(lastParams));
    }

    private static void accountLevelScopeIsNotFiltered() throws Exception {
        System.out.println("[studentId<=0：不按学员过滤]");
        List<CouponInstance> candidates = new ArrayList<>();
        candidates.add(accountLevelCoupon());
        CouponServiceImpl service = serviceWith(candidates);

        service.bestCoupon(7001, "5001", null, null);
        check("未传 studentId 时不出现 student_id 过滤",
                !lastStudentSql.contains("student_id"), lastStudentSql);

        service.bestCoupon(7001, "5001", null, "0");
        check("studentId=0 时不出现 student_id 过滤",
                !lastStudentSql.contains("student_id"), lastStudentSql);
    }

    // ---- 夹具 ----------------------------------------------------------------

    private static CouponInstance accountLevelCoupon() {
        CouponInstance instance = new CouponInstance();
        instance.setId(9001);
        instance.setSn("T-BESTCOUPON-9001");
        instance.setUserId(7001);
        instance.setStudentId(0);
        instance.setGoodsId(1);
        instance.setTitle("团课单次券");
        instance.setTotal(1);
        instance.setRemaining(1);
        instance.setStatus(1);
        instance.setScope("团课通用");
        instance.setScopeType("all");
        instance.setCourseIds("");
        instance.setAreaIds("");
        instance.setCreateTime(1793865600L);
        return instance;
    }

    @SuppressWarnings("unchecked")
    private static CouponServiceImpl serviceWith(List<CouponInstance> candidates) throws Exception {
        CouponServiceImpl service = new CouponServiceImpl();
        CourseClass course = new CourseClass();
        course.setId(5001);
        course.setName("团课 A");
        course.setClassType(0);
        course.setAreaId(0);

        set(service, "courseClassMapper", proxy(CourseClassMapper.class, (self, method, args) -> {
            if ("selectById".equals(method.getName())) {
                return course;
            }
            return emptyListFor(method);
        }));
        set(service, "couponInstanceMapper", proxy(CouponInstanceMapper.class, (self, method, args) -> {
            if ("selectList".equals(method.getName())) {
                QueryWrapper<CouponInstance> wrapper = (QueryWrapper<CouponInstance>) args[0];
                lastStudentSql = wrapper.getSqlSegment();
                lastParams = wrapper.getParamNameValuePairs();
                return candidates;
            }
            if ("selectById".equals(method.getName())) {
                return candidates.isEmpty() ? null : candidates.get(0);
            }
            return emptyListFor(method);
        }));
        set(service, "studentMapper", proxy(StudentMapper.class, (self, method, args) -> {
            if ("selectBatchIds".equals(method.getName())) {
                List<Student> students = new ArrayList<>();
                return students;
            }
            return emptyListFor(method);
        }));
        set(service, "couponCheckinService", checkinServiceWith(candidates));
        return service;
    }

    /**
     * #221 起 CouponCheckinService.checkApplies 委托 CouponEligibility（纯逻辑，无需 Mapper），
     * 夹具同步注入，断言与查询语义均不变。
     */
    private static CouponCheckinService checkinServiceWith(List<CouponInstance> candidates) throws Exception {
        CouponCheckinService checkinService = new CouponCheckinService();
        set(checkinService, "couponEligibility", new CouponEligibility());
        return checkinService;
    }

    private static Object emptyListFor(Method method) {
        return Collections.emptyList();
    }

    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
    }

    private static void set(Object target, String field, Object value) throws Exception {
        Class<?> type = target instanceof CouponServiceImpl ? CouponServiceImpl.class : target.getClass();
        Field declared = type.getDeclaredField(field);
        declared.setAccessible(true);
        declared.set(target, value);
    }

    private static int count(String text, String needle) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(needle, index)) >= 0) {
            count += 1;
            index += needle.length();
        }
        return count;
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
