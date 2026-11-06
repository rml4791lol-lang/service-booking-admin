package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.coupon.CouponInstance;
import com.mdd.common.entity.region.Area;
import com.mdd.common.entity.student.Student;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseLessonLogMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.coupon.CouponInstanceMapper;
import com.mdd.common.mapper.region.AreaMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.service.CouponCheckinService;
import com.mdd.common.service.CouponEligibility;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * #221 三口径一致断言器：同一输入在「预约凭证 / 核销选券 / 唯一判定模块」结果一致。
 *
 * <p>本仓库离线 Maven 仓库没有 junit / surefire provider，按仓库惯例写成
 * 「带 main 的纯 Java 断言器」，由 tools/coupon/run-coupon-eligibility-consistency-test.sh 执行。</p>
 *
 * <p>三口径的 seam：</p>
 * <ul>
 *   <li>①预约凭证：{@link BookingServiceImpl#createBooking}（凭证计数口径，
 *       经 BookingServiceImpl → CouponEligibility.countUsable）；</li>
 *   <li>②核销选券：{@link CouponCheckinService#previewForCheckin}（课程范围口径，
 *       经 CouponCheckinService → CouponEligibility.evaluate）；</li>
 *   <li>③唯一判定：{@link CouponEligibility} 本身（checkApplies / countUsable）。</li>
 * </ul>
 *
 * <p>一致含义：同一张券的「券自身状态」结论（未生效/过期/用完/学员不符）与原因文案
 * 在三处完全一致；课程范围只在核销口径判定（预约凭证计数按设计不判范围，见场景 5 注释）。
 * 后台发券口径的一致断言见 like-admin 侧 CouponEligibilityGrantConsistencyTest。
 * Mapper 用动态代理注入，不连数据库。</p>
 */
public final class CouponEligibilityConsistencyTest {

    private static final int USER_ID = 7001;
    private static final int STUDENT_ID = 8001;
    private static final int OTHER_STUDENT_ID = 8002;
    private static final int CLASS_ID = 5001;
    private static final int LESSON_ID = 6001;
    private static final int AREA_ID = 21;
    private static final String GROUP_COURSE_NAME = "少儿篮球启蒙班";

    private static int passed;
    private static int failed;

    public static void main(String[] args) throws Exception {
        validCouponIsUsableAtAllThree();
        noCouponRejectedAtAllThree();
        expiredCouponSameReason();
        notStartedCouponSameReason();
        studentMismatchSameReason();
        scopeOnlyCheckinRejects();
        exhaustedCouponSameReason();

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    // ---- 场景 ------------------------------------------------------------------

    private static void validCouponIsUsableAtAllThree() throws Exception {
        System.out.println("[场景1 有券：可约 ↔ 可核销 ↔ 判定可用，且是同一张券]");
        CouponInstance coupon = coupon(CouponEligibility.SCOPE_GROUP, 0, 9001);
        coupon.getExpireTime();

        BookingServiceImpl booking = bookingService(coupon);
        Map<String, Object> result = booking.createBooking(USER_ID, LESSON_ID, STUDENT_ID, null);
        check("①预约：createBooking 成功", Boolean.TRUE.equals(result.get("ok")), String.valueOf(result.get("ok")));

        CouponCheckinService checkin = checkinService(coupon);
        Map<String, Object> preview = checkin.previewForCheckin(USER_ID, STUDENT_ID, CLASS_ID);
        check("②核销：previewForCheckin 命中",
                Boolean.TRUE.equals(preview.get("available")), String.valueOf(preview));
        check("两口径命中同一张券", String.valueOf(preview.get("couponId")).equals("9001"),
                String.valueOf(preview.get("couponId")));

        CouponEligibility eligibility = eligibility(coupon);
        CouponEligibility.Evaluation eval = eligibility.evaluate(USER_ID, STUDENT_ID, groupCourse(), now());
        check("③判定：可用列表恰为该券", eval.getEligible().size() == 1
                && eval.getEligible().get(0).getId() == 9001, String.valueOf(eval.getEligible().size()));
        check("③判定：countUsable=1（预约凭证计数）",
                eligibility.countUsable(USER_ID, STUDENT_ID, now()) == 1L,
                String.valueOf(eligibility.countUsable(USER_ID, STUDENT_ID, now())));
    }

    private static void noCouponRejectedAtAllThree() throws Exception {
        System.out.println("[场景2 无券：预约拒绝 ↔ 核销无券 ↔ 判定为空]");
        BookingServiceImpl booking = bookingService();
        try {
            booking.createBooking(USER_ID, LESSON_ID, STUDENT_ID, null);
            check("①预约：应拒绝", false, "未抛异常");
        } catch (OperateException e) {
            check("①预约：无券拒绝",
                    "该学员暂无可用课程券，请先购买课程券后再预约".equals(e.getMsg()),
                    String.valueOf(e.getMsg()));
        }

        CouponCheckinService checkin = checkinService();
        Map<String, Object> preview = checkin.previewForCheckin(USER_ID, STUDENT_ID, CLASS_ID);
        check("②核销：无可用券", !Boolean.TRUE.equals(preview.get("available")), String.valueOf(preview));
        check("②核销：原因=无可用次数券", "无可用次数券".equals(preview.get("reason")),
                String.valueOf(preview.get("reason")));

        CouponEligibility eligibility = eligibility();
        check("③判定：countUsable=0", eligibility.countUsable(USER_ID, STUDENT_ID, now()) == 0L, "");
    }

    private static void expiredCouponSameReason() throws Exception {
        System.out.println("[场景3 过期券：两口径同判不可用且原因一致]");
        CouponInstance coupon = coupon(CouponEligibility.SCOPE_GROUP, 0, 9003);
        coupon.setExpireTime(now() - 1L);
        consistencyRejected(coupon, CouponEligibility.REASON_EXPIRED);
    }

    private static void notStartedCouponSameReason() throws Exception {
        System.out.println("[场景4 未生效券：两口径同判不可用且原因一致]");
        CouponInstance coupon = coupon(CouponEligibility.SCOPE_GROUP, 0, 9004);
        coupon.setStartTime(now() + 3600L);
        consistencyRejected(coupon, CouponEligibility.REASON_NOT_STARTED);
    }

    private static void studentMismatchSameReason() throws Exception {
        System.out.println("[场景5 学员不符：两口径同判不可用且原因一致]");
        CouponInstance coupon = coupon(CouponEligibility.SCOPE_GROUP, OTHER_STUDENT_ID, 9005);
        consistencyRejected(coupon, CouponEligibility.REASON_STUDENT_MISMATCH);
    }

    /**
     * 场景6 范围不符：核销口径拒绝（该券仅限私教课使用）；预约凭证计数口径按设计
     * 不判课程范围（凭证 = 「这张券还能占一节课」，与课程无关），因此该券仍计入凭证。
     * 本断言固化这一设计差异：两口径对券自身状态的结论一致，范围差异仅出现在核销侧。
     */
    private static void scopeOnlyCheckinRejects() throws Exception {
        System.out.println("[场景6 范围不符：核销拒绝范围，预约凭证计数不判范围（设计差异）]");
        CouponInstance coupon = coupon(CouponEligibility.SCOPE_PRIVATE, 0, 9006);

        CouponCheckinService checkin = checkinService(coupon);
        Map<String, Object> preview = checkin.previewForCheckin(USER_ID, STUDENT_ID, CLASS_ID);
        check("②核销：范围不符拒绝",
                !Boolean.TRUE.equals(preview.get("available"))
                        && CouponEligibility.REASON_PRIVATE_ONLY.equals(preview.get("reason")),
                String.valueOf(preview.get("reason")));

        CouponEligibility eligibility = eligibility(coupon);
        check("③判定：核销口径同因",
                CouponEligibility.REASON_PRIVATE_ONLY.equals(
                        eligibility.checkApplies(coupon, groupCourse(), STUDENT_ID, now())),
                "");
        check("①预约凭证：该券仍计入凭证（不判范围）",
                eligibility.countUsable(USER_ID, STUDENT_ID, now()) == 1L,
                String.valueOf(eligibility.countUsable(USER_ID, STUDENT_ID, now())));
        try {
            bookingService(coupon).createBooking(USER_ID, LESSON_ID, STUDENT_ID, null);
            check("①预约：有凭证可约（口径设计如此）", true, "");
        } catch (OperateException e) {
            check("①预约：有凭证可约（口径设计如此）", false, e.getMessage());
        }
    }

    private static void exhaustedCouponSameReason() throws Exception {
        System.out.println("[场景7 用完券：两口径同判不可用且原因一致]");
        CouponInstance coupon = coupon(CouponEligibility.SCOPE_GROUP, 0, 9007);
        coupon.setRemaining(0);
        consistencyRejected(coupon, CouponEligibility.REASON_EXHAUSTED);
    }

    /**
     * 不可用券的三口径一致性骨架：
     * ①预约拒绝（凭证计数为 0）↔ ②核销拒绝且原因一致 ↔ ③判定原因一致。
     */
    private static void consistencyRejected(CouponInstance coupon, String expectReason) throws Exception {
        BookingServiceImpl booking = bookingService(coupon);
        try {
            booking.createBooking(USER_ID, LESSON_ID, STUDENT_ID, null);
            check("①预约：应拒绝", false, "未抛异常");
        } catch (OperateException e) {
            check("①预约：无可用凭证拒绝", true, e.getMessage());
        }

        CouponCheckinService checkin = checkinService(coupon);
        Map<String, Object> preview = checkin.previewForCheckin(USER_ID, STUDENT_ID, CLASS_ID);
        check("②核销：不可用", !Boolean.TRUE.equals(preview.get("available")), String.valueOf(preview));
        check("②核销：原因=" + expectReason, expectReason.equals(preview.get("reason")),
                String.valueOf(preview.get("reason")));

        CouponEligibility eligibility = eligibility(coupon);
        String reason = eligibility.checkApplies(coupon, groupCourse(), STUDENT_ID, now());
        check("③判定：原因一致", expectReason.equals(reason), String.valueOf(reason));
        check("③判定：countUsable=0（预约凭证计数）",
                eligibility.countUsable(USER_ID, STUDENT_ID, now()) == 0L,
                String.valueOf(eligibility.countUsable(USER_ID, STUDENT_ID, now())));
    }

    // ---- 夹具 ------------------------------------------------------------------

    private static long now() {
        return System.currentTimeMillis() / 1000L;
    }

    private static CouponInstance coupon(String scopeType, Integer studentId, int id) {
        CouponInstance instance = new CouponInstance();
        instance.setId(id);
        instance.setUserId(USER_ID);
        instance.setStudentId(studentId);
        instance.setTitle("团课单次券");
        instance.setTotal(1);
        instance.setRemaining(1);
        instance.setStatus(CouponEligibility.STATUS_VALID);
        instance.setScopeType(scopeType);
        instance.setCourseIds("");
        instance.setAreaIds("");
        instance.setExpireTime(now() + 86400L);
        return instance;
    }

    private static CourseClass groupCourse() {
        CourseClass course = new CourseClass();
        course.setId(CLASS_ID);
        course.setName(GROUP_COURSE_NAME);
        course.setClassType(0);
        course.setAreaId(AREA_ID);
        course.setStatus(1);
        course.setMaxStudents(10);
        course.setIsDelete(0);
        course.setIsShow(1);
        return course;
    }

    private static CourseLesson lesson() {
        CourseLesson lesson = new CourseLesson();
        lesson.setId(LESSON_ID);
        lesson.setClassId(CLASS_ID);
        lesson.setStatus(0);
        lesson.setLessonDate(LocalDate.now().plusDays(1).toString());
        return lesson;
    }

    private static Student student() {
        Student student = new Student();
        student.setId(STUDENT_ID);
        student.setUserId(USER_ID);
        student.setName("测试学员");
        return student;
    }

    /** 预约口径：BookingServiceImpl 全链路（代理 Mapper，不连库）。 */
    private static BookingServiceImpl bookingService(CouponInstance... fixtures) throws Exception {
        BookingServiceImpl service = new BookingServiceImpl();
        CourseClass course = groupCourse();
        CourseLesson lesson = lesson();
        Student student = student();

        set(service, "courseLessonMapper", proxy(CourseLessonMapper.class, (self, method, args1) -> {
            if ("selectById".equals(method.getName())) {
                return lesson;
            }
            return emptyListFor(method);
        }));
        set(service, "courseClassMapper", proxy(CourseClassMapper.class, (self, method, args1) -> {
            if ("selectById".equals(method.getName())) {
                return course;
            }
            return emptyListFor(method);
        }));
        set(service, "courseBookingMapper", proxy(CourseBookingMapper.class, (self, method, args1) -> {
            String name = method.getName();
            if ("selectCount".equals(name)) {
                return 0L;
            }
            if ("selectOne".equals(name)) {
                return null;
            }
            if ("insert".equals(name)) {
                return 1;
            }
            return emptyListFor(method);
        }));
        set(service, "studentMapper", proxy(StudentMapper.class, (self, method, args1) -> {
            if ("selectOne".equals(method.getName())) {
                return student;
            }
            return emptyListFor(method);
        }));
        set(service, "areaMapper", proxy(AreaMapper.class, (self, method, args1) ->
                emptyListFor(method)));
        set(service, "instructorMapper", proxy(InstructorMapper.class, (self, method, args1) ->
                emptyListFor(method)));
        set(service, "courseLessonLogMapper", proxy(CourseLessonLogMapper.class, (self, method, args1) ->
                emptyListFor(method)));
        set(service, "userCourseMapper", proxy(UserCourseMapper.class, (self, method, args1) ->
                emptyListFor(method)));
        set(service, "couponEligibility", eligibility(fixtures));
        return service;
    }

    /** 核销口径：CouponCheckinService（代理 Mapper，不连库）。 */
    private static CouponCheckinService checkinService(CouponInstance... fixtures) throws Exception {
        CouponCheckinService service = new CouponCheckinService();
        CourseClass course = groupCourse();
        set(service, "courseClassMapper", proxy(CourseClassMapper.class, (self, method, args1) -> {
            if ("selectById".equals(method.getName())) {
                return course;
            }
            return emptyListFor(method);
        }));
        set(service, "couponInstanceMapper", proxy(CouponInstanceMapper.class, (self, method, args1) -> {
            if ("selectOne".equals(method.getName())) {
                // FOR UPDATE 复核：返回加锁后的同一行
                return fixtures.length > 0 ? fixtures[0] : null;
            }
            return emptyListFor(method);
        }));
        set(service, "couponEligibility", eligibility(fixtures));
        return service;
    }

    private static CouponEligibility eligibility(CouponInstance... fixtures) {
        CouponEligibility eligibility = new CouponEligibility();
        List<CouponInstance> list = new ArrayList<>();
        Collections.addAll(list, fixtures);
        setQuietly(eligibility, "couponInstanceMapper", proxy(CouponInstanceMapper.class, (self, method, args1) -> {
            if ("selectList".equals(method.getName())) {
                return list;
            }
            return emptyListFor(method);
        }));
        return eligibility;
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

    private static void set(Object target, String field, Object value) throws Exception {
        Field declared = target.getClass().getDeclaredField(field);
        declared.setAccessible(true);
        declared.set(target, value);
    }

    private static void setQuietly(Object target, String field, Object value) {
        try {
            set(target, field, value);
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
