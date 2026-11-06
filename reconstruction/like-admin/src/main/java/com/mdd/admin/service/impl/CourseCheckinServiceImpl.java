package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.service.ICourseCheckinService;
import com.mdd.admin.validate.checkin.CourseCheckinAuditSearchValidate;
import com.mdd.admin.validate.checkin.CourseCheckinManualValidate;
import com.mdd.admin.validate.checkin.CourseCheckinSearchValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.admin.Admin;
import com.mdd.common.entity.community.Community;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseCheckinAudit;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseLessonLog;
import com.mdd.common.entity.region.Area;
import com.mdd.common.entity.student.Student;
import com.mdd.common.entity.user.User;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.mapper.community.CommunityMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseCheckinAuditMapper;
import com.mdd.common.mapper.course.CourseLessonLogMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.region.AreaMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.service.CourseAttendanceService;
import com.mdd.common.service.CouponCheckinService;
import com.mdd.common.util.ClassScheduleUtil;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.UrlUtils;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 后台待核销与人工补核销实现（T30.4 / #37）。
 *
 * <p>待核销 = {@code ys_course_lesson.status=0} 的场次；已部分核销（存在预约
 * status=10 但场次仍未完成）单独展示为「部分核销」。人工补核销复用
 * {@link CourseAttendanceService}，与教练端 #26 共用逐人扣券、预约置已上课、
 * 消课流水写入逻辑；原因必填，实际发生状态变更才写审计。</p>
 */
@Service
public class CourseCheckinServiceImpl implements ICourseCheckinService {

    private static final int LESSON_FINISHED = 1;
    private static final int LESSON_CANCELLED = 2;
    private static final int BOOKING_ATTENDED = 10;
    private static final int BOOKING_CANCELLED = 3;
    private static final int MANUAL_SOURCE_TYPE = 2;
    private static final int MAX_REASON_LENGTH = 500;
    private static final String[] WEEK_CN = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CourseBookingMapper courseBookingMapper;
    @Resource
    CourseLessonLogMapper courseLessonLogMapper;
    @Resource
    CourseCheckinAuditMapper courseCheckinAuditMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    AreaMapper areaMapper;
    @Resource
    CommunityMapper communityMapper;
    @Resource
    AdminMapper adminMapper;
    @Resource
    CourseAttendanceService courseAttendanceService;
    @Resource
    CouponCheckinService couponCheckinService;

    // ------------------------------------------------------------------
    // 列表
    // ------------------------------------------------------------------

    @Override
    public PageResult<Map<String, Object>> list(PageValidate pageValidate,
                                                CourseCheckinSearchValidate searchValidate) {
        int pageNo = pageValidate.getPage_no() == null ? 1 : pageValidate.getPage_no();
        int pageSize = pageValidate.getPage_size() == null ? 20 : pageValidate.getPage_size();

        Set<Integer> classIds = null;
        if (searchValidate != null) {
            if (searchValidate.getCommunityId() != null && searchValidate.getCommunityId() > 0) {
                classIds = classIdsForCommunity(searchValidate.getCommunityId());
            }
            if (searchValidate.getCourseId() != null && searchValidate.getCourseId() > 0) {
                classIds = intersect(classIds, Collections.singleton(searchValidate.getCourseId()));
            }
            if (StringUtils.isNotBlank(searchValidate.getKeyword())) {
                classIds = intersect(classIds, classIdsByName(searchValidate.getKeyword().trim()));
            }
        }
        if (classIds != null && classIds.isEmpty()) {
            return PageResult.iPageHandle(0L, (long) pageNo, (long) pageSize, Collections.emptyList());
        }

        QueryWrapper<CourseLesson> queryWrapper = new QueryWrapper<>();
        if (classIds != null) {
            queryWrapper.in("class_id", classIds);
        }
        if (searchValidate != null) {
            String date = searchValidate.getDate();
            if (StringUtils.isNotBlank(date)) {
                if (parseDate(date.trim()) == null) {
                    throw new OperateException("日期格式不正确");
                }
                queryWrapper.eq("lesson_date", date.trim());
            }
            Integer status = searchValidate.getStatus();
            if (status != null) {
                if (status == 0) {
                    queryWrapper.eq("status", 0)
                            .exists("SELECT 1 FROM la_course_booking b0 WHERE b0.lesson_id = ys_course_lesson.id AND b0.status <> 3")
                            .notExists("SELECT 1 FROM la_course_booking b WHERE b.lesson_id = ys_course_lesson.id AND b.status = 10");
                } else if (status == 1) {
                    queryWrapper.eq("status", LESSON_FINISHED);
                } else if (status == 2) {
                    queryWrapper.eq("status", LESSON_CANCELLED);
                } else if (status == 3) {
                    queryWrapper.eq("status", 0)
                            .exists("SELECT 1 FROM la_course_booking b0 WHERE b0.lesson_id = ys_course_lesson.id AND b0.status <> 3")
                            .exists("SELECT 1 FROM la_course_booking b WHERE b.lesson_id = ys_course_lesson.id AND b.status = 10");
                }
            }
        }
        queryWrapper.orderByDesc("lesson_date").orderByDesc("time_slot").orderByDesc("id");

        IPage<CourseLesson> iPage = courseLessonMapper.selectPage(
                new Page<>(pageNo, pageSize), queryWrapper);
        List<CourseLesson> lessons = iPage.getRecords();
        if (lessons.isEmpty()) {
            return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(),
                    Collections.emptyList());
        }
        CheckinContext context = buildContext(lessons);
        List<Map<String, Object>> rows = new ArrayList<>();
        for (CourseLesson lesson : lessons) {
            CourseClass courseClass = context.classes.get(lesson.getClassId());
            rows.add(buildSession(lesson, courseClass, context));
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), rows);
    }

    // ------------------------------------------------------------------
    // 详情
    // ------------------------------------------------------------------

    @Override
    public Map<String, Object> detail(Integer sessionId) {
        if (sessionId == null || sessionId <= 0) {
            return null;
        }
        CourseLesson lesson = courseLessonMapper.selectById(sessionId);
        if (lesson == null) {
            return null;
        }
        CourseClass courseClass = courseClassMapper.selectById(lesson.getClassId());
        if (courseClass == null || (courseClass.getIsDelete() != null && courseClass.getIsDelete() == 1)) {
            return null;
        }
        CheckinContext context = buildContext(Collections.singletonList(lesson));
        Map<String, Object> session = buildSession(lesson, courseClass, context);
        List<CourseBooking> bookings = activeBookings(lesson.getId());
        Map<Integer, Student> students = studentsOf(bookings);
        Map<Integer, User> users = usersOf(bookings);
        List<Map<String, Object>> roster = new ArrayList<>();
        for (CourseBooking booking : bookings) {
            Student student = students.get(booking.getStudentId());
            User user = users.get(booking.getUserId());
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("bookingId", booking.getId());
            item.put("studentId", booking.getStudentId());
            item.put("studentName", studentName(student));
            item.put("gender", genderText(student == null ? null : student.getGender()));
            item.put("age", student == null || student.getAge() == null ? "" : student.getAge());
            item.put("userId", booking.getUserId());
            item.put("userName", userName(user));
            item.put("userMobile", user == null ? "" : nvl(user.getMobile()));
            item.put("status", booking.getStatus());
            item.put("statusText", bookingStatusText(booking.getStatus()));
            item.put("attended", booking.getStatus() != null && booking.getStatus() == BOOKING_ATTENDED);
            item.put("bookedAt", formatTime(booking.getCreateTime()));
            if (booking.getStatus() != null && booking.getStatus() == BOOKING_ATTENDED) {
                item.put("coupon", null);
            } else {
                item.put("coupon", couponCheckinService.previewForCheckin(
                        booking.getUserId(), booking.getStudentId(), courseClass.getId()));
            }
            roster.add(item);
        }
        session.put("roster", roster);
        boolean canViewAudit = canViewAudit();
        session.put("canViewAudit", canViewAudit);
        session.put("audits", canViewAudit ? auditRows(new QueryWrapper<CourseCheckinAudit>()
                .eq("session_id", sessionId)
                .orderByDesc("id")) : Collections.emptyList());
        CourseLessonLog log = context.logs.get(lessonKey(lesson.getClassId(), lesson.getLessonNo()));
        session.put("photos", log == null ? Collections.emptyList() : photoUrls(log.getImages()));
        session.put("photoCount", log == null ? 0 : countImages(log.getImages()));
        session.put("summary", log == null ? "" : nvl(log.getSummary()));
        return session;
    }

    // ------------------------------------------------------------------
    // 下拉
    // ------------------------------------------------------------------

    @Override
    public Map<String, Object> options() {
        List<CourseClass> classes = courseClassMapper.selectList(new QueryWrapper<CourseClass>()
                .eq("is_delete", 0)
                .orderByAsc("id"));
        Map<Integer, Community> communityMap = new HashMap<>();
        List<Community> communities = communityMapper.selectList(new QueryWrapper<Community>()
                .isNull("delete_time")
                .orderByAsc("id"));
        for (Community community : communities) {
            communityMap.put(community.getId(), community);
        }
        Map<Integer, Area> areaMap = new HashMap<>();
        for (Area area : areaMapper.selectList(new QueryWrapper<Area>().isNull("delete_time"))) {
            areaMap.put(area.getId(), area);
        }

        List<Map<String, Object>> courses = new ArrayList<>();
        for (CourseClass courseClass : classes) {
            Area area = courseClass.getAreaId() == null ? null : areaMap.get(courseClass.getAreaId());
            Integer communityId = courseClass.getCommunityId() != null && courseClass.getCommunityId() > 0
                    ? courseClass.getCommunityId()
                    : (area == null || area.getCommunityId() == null ? 0 : area.getCommunityId());
            Community community = communityMap.get(communityId);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", courseClass.getId());
            item.put("name", nvl(courseClass.getName()));
            item.put("communityId", communityId == null ? 0 : communityId);
            item.put("communityName", community == null ? "" : nvl(community.getName()));
            item.put("areaId", courseClass.getAreaId() == null ? 0 : courseClass.getAreaId());
            item.put("areaName", area == null ? "" : nvl(area.getName()));
            item.put("instructorName", nvl(courseClass.getInstructorName()));
            courses.add(item);
        }
        List<Map<String, Object>> communityOptions = new ArrayList<>();
        for (Community community : communities) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", community.getId());
            item.put("name", nvl(community.getName()));
            communityOptions.add(item);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("courses", courses);
        result.put("communities", communityOptions);
        return result;
    }

    // ------------------------------------------------------------------
    // 人工补核销
    // ------------------------------------------------------------------

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> manual(Integer adminId, CourseCheckinManualValidate manualValidate) {
        if (adminId == null || adminId <= 0) {
            throw new OperateException("请先登录管理员账号");
        }
        Integer sessionId = manualValidate.getSessionId();
        if (sessionId == null || sessionId <= 0) {
            throw new OperateException("场次不存在");
        }
        String reason = manualValidate.getReason() == null ? "" : manualValidate.getReason().trim();
        if (reason.isEmpty()) {
            throw new OperateException("补核销原因不能为空");
        }
        if (reason.length() > MAX_REASON_LENGTH) {
            throw new OperateException("补核销原因不能超过 " + MAX_REASON_LENGTH + " 字");
        }
        List<Integer> studentIdList = manualValidate.getStudentIds();
        if (studentIdList == null || studentIdList.isEmpty()) {
            throw new OperateException("请至少选择一名待核销学员");
        }
        Set<Integer> targetStudentIds = new LinkedHashSet<>();
        for (Integer studentId : studentIdList) {
            if (studentId == null || studentId <= 0) {
                throw new OperateException("学员参数错误");
            }
            targetStudentIds.add(studentId);
        }

        // 场次行锁：所有核销入口（教练/后台）都先锁场次，保证同一场次的补核销串行、幂等。
        CourseLesson lesson = courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>()
                .eq("id", sessionId)
                .last("limit 1 for update"));
        if (lesson == null) {
            throw new OperateException("场次不存在");
        }
        CourseClass courseClass = courseClassMapper.selectById(lesson.getClassId());
        if (courseClass == null || (courseClass.getIsDelete() != null && courseClass.getIsDelete() == 1)) {
            throw new OperateException("课程不存在或已下架");
        }
        if (lesson.getStatus() != null && lesson.getStatus() == LESSON_CANCELLED) {
            throw new OperateException("该场次已取消，不能补核销");
        }

        List<CourseBooking> bookings = activeBookings(sessionId);
        Map<Integer, CourseBooking> bookingMap = new LinkedHashMap<>();
        Map<Integer, Integer> beforeStatus = new LinkedHashMap<>();
        for (CourseBooking booking : bookings) {
            if (booking.getStudentId() != null && booking.getStudentId() > 0) {
                bookingMap.put(booking.getStudentId(), booking);
                beforeStatus.put(booking.getStudentId(), booking.getStatus() == null ? 0 : booking.getStatus());
            }
        }
        for (Integer studentId : targetStudentIds) {
            if (!bookingMap.containsKey(studentId)) {
                throw new OperateException("该学员不在本场次预约名单中");
            }
        }
        Map<Integer, Student> students = studentsOf(bookings);
        Admin admin = adminMapper.selectById(adminId);
        String operatorName = adminName(admin, adminId);
        String time = resolveTime(lesson, courseClass);
        String ref = trimRef(nvl(courseClass.getName()) + " " + nvl(lesson.getLessonDate()) + " " + time
                + " 第" + (lesson.getLessonNo() == null ? 0 : lesson.getLessonNo()) + "节");
        String couponRemark = trimRemark("人工补核销扣减：" + reason);

        long nowMillis = System.currentTimeMillis();
        int now = (int) (nowMillis / 1000L);
        Map<String, Object> attendance = courseAttendanceService.markAttended(
                lesson, courseClass, bookings, targetStudentIds,
                "admin", adminId, couponRemark, MANUAL_SOURCE_TYPE, ref);
        @SuppressWarnings("unchecked")
        List<String> changedStudentIds = (List<String>) attendance.get("changedStudentIds");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> deductions = (List<Map<String, Object>>) attendance.get("deductions");
        @SuppressWarnings("unchecked")
        List<String> warnings = (List<String>) attendance.get("warnings");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> skipped = (List<Map<String, Object>>) attendance.get("skipped");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> undeducted = (List<Map<String, Object>>) attendance.get("undeducted");
        @SuppressWarnings("unchecked")
        List<String> attendedIds = (List<String>) attendance.get("attendedStudentIds");

        Map<String, Map<String, Object>> deductionMap = new HashMap<>();
        for (Map<String, Object> deduction : deductions) {
            deductionMap.put(String.valueOf(deduction.get("studentId")), deduction);
        }
        Map<String, Map<String, Object>> undeductedMap = new HashMap<>();
        for (Map<String, Object> item : undeducted) {
            undeductedMap.put(String.valueOf(item.get("studentId")), item);
        }

        // 全部有效预约都已上课时才把场次置为已结课；部分补核销保持待核销并展示进度。
        boolean allAttended = true;
        for (CourseBooking booking : bookings) {
            if (booking.getStatus() == null || booking.getStatus() != BOOKING_ATTENDED) {
                allAttended = false;
                break;
            }
        }
        if (allAttended) {
            if (lesson.getStatus() == null || lesson.getStatus() != LESSON_FINISHED) {
                lesson.setStatus(LESSON_FINISHED);
                lesson.setUpdateTime(now);
                courseLessonMapper.updateById(lesson);
            }
        } else {
            lesson.setUpdateTime(now);
            courseLessonMapper.updateById(lesson);
        }

        List<Integer> auditIds = new ArrayList<>();
        for (String studentIdText : changedStudentIds) {
            Integer studentId = Integer.parseInt(studentIdText);
            CourseBooking booking = bookingMap.get(studentId);
            if (booking == null) {
                continue;
            }
            Student student = students.get(studentId);
            Map<String, Object> deduction = deductionMap.get(studentIdText);
            Map<String, Object> undeductedItem = undeductedMap.get(studentIdText);
            CourseCheckinAudit audit = new CourseCheckinAudit();
            audit.setSessionId(lesson.getId());
            audit.setClassId(courseClass.getId());
            audit.setLessonNo(lesson.getLessonNo() == null ? 0 : lesson.getLessonNo());
            audit.setBookingId(booking.getId());
            audit.setStudentId(studentId);
            audit.setStudentName(studentName(student));
            audit.setUserId(booking.getUserId());
            audit.setCourseName(nvl(courseClass.getName()));
            audit.setSessionDate(nvl(lesson.getLessonDate()));
            audit.setTimeSlot(time);
            audit.setActionType("manual_checkin");
            audit.setReason(reason);
            audit.setOperatorAdminId(adminId);
            audit.setOperatorName(operatorName);
            audit.setBeforeStatus(beforeStatus.getOrDefault(studentId, 1));
            audit.setAfterStatus(BOOKING_ATTENDED);
            if (deduction != null) {
                audit.setDeducted(1);
                audit.setCouponId(parseInt(deduction.get("couponId")));
                audit.setCouponTitle(nvl((String) deduction.get("title")));
                audit.setCouponRemaining(parseInt(deduction.get("remaining")));
                audit.setDetail("已扣券：" + nvl((String) deduction.get("title"))
                        + "，余额 " + parseInt(deduction.get("remaining")) + " 次");
            } else {
                audit.setDeducted(0);
                audit.setCouponId(0);
                audit.setCouponTitle("");
                audit.setCouponRemaining(0);
                String undeductedReason = undeductedItem == null ? "无可用次数券" : nvl((String) undeductedItem.get("reason"));
                audit.setDetail("未扣券：" + undeductedReason + "，考勤仍记为已上课");
            }
            audit.setCreateTime(now);
            courseCheckinAuditMapper.insert(audit);
            auditIds.add(audit.getId());
        }

        CheckinContext context = buildContext(Collections.singletonList(lesson));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("ok", true);
        result.put("idempotent", changedStudentIds.isEmpty());
        result.put("count", changedStudentIds.size());
        result.put("sessionId", String.valueOf(sessionId));
        result.put("reason", reason);
        result.put("attendedStudentIds", attendedIds == null ? Collections.emptyList() : attendedIds);
        result.put("changedStudentIds", changedStudentIds);
        result.put("deductions", deductions);
        result.put("warnings", warnings);
        result.put("undeducted", undeducted);
        result.put("skipped", skipped);
        result.put("auditIds", auditIds);
        result.put("session", buildSession(lesson, courseClass, context));
        return result;
    }

    // ------------------------------------------------------------------
    // 审计查询
    // ------------------------------------------------------------------

    @Override
    public PageResult<Map<String, Object>> audits(PageValidate pageValidate,
                                                  CourseCheckinAuditSearchValidate searchValidate) {
        int pageNo = pageValidate.getPage_no() == null ? 1 : pageValidate.getPage_no();
        int pageSize = pageValidate.getPage_size() == null ? 20 : pageValidate.getPage_size();
        QueryWrapper<CourseCheckinAudit> queryWrapper = new QueryWrapper<>();
        if (searchValidate != null) {
            if (searchValidate.getSessionId() != null && searchValidate.getSessionId() > 0) {
                queryWrapper.eq("session_id", searchValidate.getSessionId());
            }
            if (searchValidate.getStudentId() != null && searchValidate.getStudentId() > 0) {
                queryWrapper.eq("student_id", searchValidate.getStudentId());
            }
            if (StringUtils.isNotBlank(searchValidate.getDate())) {
                LocalDate date = parseDate(searchValidate.getDate().trim());
                if (date == null) {
                    throw new OperateException("日期格式不正确");
                }
                long start = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
                long end = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toEpochSecond() - 1;
                queryWrapper.ge("create_time", start).le("create_time", end);
            }
        }
        queryWrapper.orderByDesc("id");
        IPage<CourseCheckinAudit> iPage = courseCheckinAuditMapper.selectPage(
                new Page<>(pageNo, pageSize), queryWrapper);
        List<Map<String, Object>> rows = new ArrayList<>();
        for (CourseCheckinAudit audit : iPage.getRecords()) {
            rows.add(auditRow(audit));
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), rows);
    }

    // ------------------------------------------------------------------
    // 组装
    // ------------------------------------------------------------------

    private static class CheckinContext {
        Map<Integer, CourseClass> classes = new HashMap<>();
        Map<Integer, Instructor> instructors = new HashMap<>();
        Map<Integer, Area> areas = new HashMap<>();
        Map<Integer, Community> communities = new HashMap<>();
        Map<Integer, Integer> bookedCounts = new HashMap<>();
        Map<Integer, Integer> attendedCounts = new HashMap<>();
        Map<String, CourseLessonLog> logs = new HashMap<>();
        Map<Integer, CourseCheckinAudit> lastAudits = new HashMap<>();
    }

    private CheckinContext buildContext(List<CourseLesson> lessons) {
        CheckinContext context = new CheckinContext();
        if (lessons == null || lessons.isEmpty()) {
            return context;
        }
        Set<Integer> lessonIds = new LinkedHashSet<>();
        Set<Integer> classIds = new LinkedHashSet<>();
        Set<Integer> lessonNos = new LinkedHashSet<>();
        for (CourseLesson lesson : lessons) {
            if (lesson.getId() != null) {
                lessonIds.add(lesson.getId());
            }
            if (lesson.getClassId() != null) {
                classIds.add(lesson.getClassId());
            }
            if (lesson.getLessonNo() != null) {
                lessonNos.add(lesson.getLessonNo());
            }
        }
        if (!classIds.isEmpty()) {
            for (CourseClass courseClass : courseClassMapper.selectBatchIds(classIds)) {
                context.classes.put(courseClass.getId(), courseClass);
            }
        }
        Set<Integer> areaIds = new LinkedHashSet<>();
        Set<Integer> communityIds = new LinkedHashSet<>();
        for (CourseClass courseClass : context.classes.values()) {
            if (courseClass.getAreaId() != null && courseClass.getAreaId() > 0) {
                areaIds.add(courseClass.getAreaId());
            }
            if (courseClass.getCommunityId() != null && courseClass.getCommunityId() > 0) {
                communityIds.add(courseClass.getCommunityId());
            }
        }
        if (!areaIds.isEmpty()) {
            for (Area area : areaMapper.selectBatchIds(areaIds)) {
                context.areas.put(area.getId(), area);
                if (area.getCommunityId() != null && area.getCommunityId() > 0) {
                    communityIds.add(area.getCommunityId());
                }
            }
        }
        if (!communityIds.isEmpty()) {
            for (Community community : communityMapper.selectBatchIds(communityIds)) {
                context.communities.put(community.getId(), community);
            }
        }
        Set<Integer> coachIds = new LinkedHashSet<>();
        for (CourseLesson lesson : lessons) {
            Integer coachId = effectiveCoachId(lesson, context.classes.get(lesson.getClassId()));
            if (coachId != null && coachId > 0) {
                coachIds.add(coachId);
            }
        }
        if (!coachIds.isEmpty()) {
            for (Instructor instructor : instructorMapper.selectBatchIds(coachIds)) {
                context.instructors.put(instructor.getId(), instructor);
            }
        }

        List<Map<String, Object>> bookedRows = courseBookingMapper.selectMaps(new QueryWrapper<CourseBooking>()
                .select("lesson_id", "COUNT(*) AS booked_count")
                .ne("status", BOOKING_CANCELLED)
                .in("lesson_id", lessonIds)
                .groupBy("lesson_id"));
        for (Map<String, Object> row : bookedRows) {
            Integer lessonId = toInt(row.get("lesson_id"));
            Integer count = toInt(row.get("booked_count"));
            if (lessonId != null && count != null) {
                context.bookedCounts.put(lessonId, count);
            }
        }
        List<Map<String, Object>> attendedRows = courseBookingMapper.selectMaps(new QueryWrapper<CourseBooking>()
                .select("lesson_id", "COUNT(*) AS attended_count")
                .eq("status", BOOKING_ATTENDED)
                .in("lesson_id", lessonIds)
                .groupBy("lesson_id"));
        for (Map<String, Object> row : attendedRows) {
            Integer lessonId = toInt(row.get("lesson_id"));
            Integer count = toInt(row.get("attended_count"));
            if (lessonId != null && count != null) {
                context.attendedCounts.put(lessonId, count);
            }
        }

        if (!classIds.isEmpty() && !lessonNos.isEmpty()) {
            List<CourseLessonLog> logs = courseLessonLogMapper.selectList(new QueryWrapper<CourseLessonLog>()
                    .in("course_class_id", classIds)
                    .in("lesson_no", lessonNos)
                    .isNull("delete_time")
                    .orderByDesc("id"));
            for (CourseLessonLog log : logs) {
                String key = lessonKey(log.getCourseClassId(), log.getLessonNo());
                if (!context.logs.containsKey(key)) {
                    context.logs.put(key, log);
                }
            }
        }
        if (!lessonIds.isEmpty()) {
            List<CourseCheckinAudit> audits = courseCheckinAuditMapper.selectList(
                    new QueryWrapper<CourseCheckinAudit>()
                            .in("session_id", lessonIds)
                            .orderByDesc("id"));
            for (CourseCheckinAudit audit : audits) {
                if (!context.lastAudits.containsKey(audit.getSessionId())) {
                    context.lastAudits.put(audit.getSessionId(), audit);
                }
            }
        }
        return context;
    }

    private Map<String, Object> buildSession(CourseLesson lesson, CourseClass courseClass,
                                             CheckinContext context) {
        Integer classId = lesson.getClassId();
        Area area = courseClass == null || courseClass.getAreaId() == null
                ? null : context.areas.get(courseClass.getAreaId());
        Integer communityId = courseClass == null ? 0
                : (courseClass.getCommunityId() != null && courseClass.getCommunityId() > 0
                ? courseClass.getCommunityId()
                : (area == null || area.getCommunityId() == null ? 0 : area.getCommunityId()));
        Community community = context.communities.get(communityId);
        Integer coachId = effectiveCoachId(lesson, courseClass);
        Instructor instructor = context.instructors.get(coachId);
        String coachName = instructor == null
                ? (courseClass == null ? "" : nvl(courseClass.getInstructorName()))
                : nvl(instructor.getName());
        int booked = context.bookedCounts.getOrDefault(lesson.getId(), 0);
        int attended = context.attendedCounts.getOrDefault(lesson.getId(), 0);
        int pending = Math.max(booked - attended, 0);
        CourseLessonLog log = context.logs.get(lessonKey(classId, lesson.getLessonNo()));
        int photoCount = log == null ? 0 : countImages(log.getImages());
        String time = resolveTime(lesson, courseClass);
        String venue = lesson.getVenue() != null && !lesson.getVenue().trim().isEmpty()
                ? lesson.getVenue() : (courseClass == null ? "" : nvl(courseClass.getVenue()));

        int statusCode;
        String statusText;
        if (lesson.getStatus() != null && lesson.getStatus() == LESSON_CANCELLED) {
            statusCode = 2;
            statusText = "已取消";
        } else if (lesson.getStatus() != null && lesson.getStatus() == LESSON_FINISHED) {
            statusCode = 1;
            statusText = "已核销";
        } else if (attended > 0) {
            statusCode = 3;
            statusText = "部分核销";
        } else {
            statusCode = 0;
            statusText = "待核销";
        }
        CourseCheckinAudit lastAudit = context.lastAudits.get(lesson.getId());

        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", lesson.getId());
        item.put("sessionId", lesson.getId());
        item.put("classId", classId);
        item.put("courseId", classId);
        item.put("courseTitle", courseClass == null ? "" : nvl(courseClass.getName()));
        item.put("cover", courseClass == null ? "" : UrlUtils.toAdminAbsoluteUrl(courseClass.getImage()));
        item.put("date", nvl(lesson.getLessonDate()));
        item.put("weekday", weekday(lesson.getLessonDate()));
        item.put("time", time);
        item.put("timeSlot", time);
        item.put("lessonNo", lesson.getLessonNo() == null ? 0 : lesson.getLessonNo());
        item.put("venue", venue);
        item.put("communityId", communityId);
        item.put("communityName", community == null ? "" : nvl(community.getName()));
        item.put("areaId", courseClass == null || courseClass.getAreaId() == null ? 0 : courseClass.getAreaId());
        item.put("areaName", area == null ? "" : nvl(area.getName()));
        item.put("coachId", coachId == null ? 0 : coachId);
        item.put("coachName", coachName);
        item.put("capacity", courseClass == null || courseClass.getMaxStudents() == null ? 0 : courseClass.getMaxStudents());
        item.put("bookedCount", booked);
        item.put("attendedCount", attended);
        item.put("pendingCount", pending);
        item.put("photoCount", photoCount);
        item.put("hasPhotos", photoCount > 0 ? 1 : 0);
        item.put("status", statusCode);
        item.put("statusCode", statusCode);
        item.put("statusText", statusText);
        item.put("coachCheckIn", lesson.getCoachCheckIn() == null ? 0 : lesson.getCoachCheckIn());
        item.put("canManual", statusCode != 2 && pending > 0);
        item.put("updateTime", formatTime(lesson.getUpdateTime()));
        item.put("lastAudit", lastAudit == null ? null : auditRow(lastAudit));
        return item;
    }

    private List<CourseBooking> activeBookings(Integer sessionId) {
        return courseBookingMapper.selectList(new QueryWrapper<CourseBooking>()
                .eq("lesson_id", sessionId)
                .ne("status", BOOKING_CANCELLED)
                .orderByAsc("id"));
    }

    private Map<Integer, Student> studentsOf(List<CourseBooking> bookings) {
        Set<Integer> ids = new HashSet<>();
        for (CourseBooking booking : bookings) {
            if (booking.getStudentId() != null && booking.getStudentId() > 0) {
                ids.add(booking.getStudentId());
            }
        }
        Map<Integer, Student> map = new HashMap<>();
        if (ids.isEmpty()) {
            return map;
        }
        for (Student student : studentMapper.selectBatchIds(ids)) {
            map.put(student.getId(), student);
        }
        return map;
    }

    private Map<Integer, User> usersOf(List<CourseBooking> bookings) {
        Set<Integer> ids = new HashSet<>();
        for (CourseBooking booking : bookings) {
            if (booking.getUserId() != null && booking.getUserId() > 0) {
                ids.add(booking.getUserId());
            }
        }
        Map<Integer, User> map = new HashMap<>();
        if (ids.isEmpty()) {
            return map;
        }
        for (User user : userMapper.selectBatchIds(ids)) {
            map.put(user.getId(), user);
        }
        return map;
    }

    private List<Map<String, Object>> auditRows(QueryWrapper<CourseCheckinAudit> queryWrapper) {
        List<CourseCheckinAudit> audits = courseCheckinAuditMapper.selectList(queryWrapper);
        List<Map<String, Object>> rows = new ArrayList<>();
        for (CourseCheckinAudit audit : audits) {
            rows.add(auditRow(audit));
        }
        return rows;
    }

    private Map<String, Object> auditRow(CourseCheckinAudit audit) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", audit.getId());
        item.put("sessionId", audit.getSessionId());
        item.put("classId", audit.getClassId());
        item.put("lessonNo", audit.getLessonNo());
        item.put("bookingId", audit.getBookingId());
        item.put("studentId", audit.getStudentId());
        item.put("studentName", nvl(audit.getStudentName()));
        item.put("userId", audit.getUserId());
        item.put("courseName", nvl(audit.getCourseName()));
        item.put("sessionDate", nvl(audit.getSessionDate()));
        item.put("timeSlot", nvl(audit.getTimeSlot()));
        item.put("actionType", nvl(audit.getActionType()));
        item.put("reason", nvl(audit.getReason()));
        item.put("operatorAdminId", audit.getOperatorAdminId());
        item.put("operatorName", nvl(audit.getOperatorName()));
        item.put("beforeStatus", audit.getBeforeStatus());
        item.put("afterStatus", audit.getAfterStatus());
        item.put("deducted", audit.getDeducted() == null ? 0 : audit.getDeducted());
        item.put("deductedText", audit.getDeducted() != null && audit.getDeducted() == 1 ? "已扣券" : "未扣券");
        item.put("couponId", audit.getCouponId());
        item.put("couponTitle", nvl(audit.getCouponTitle()));
        item.put("couponRemaining", audit.getCouponRemaining());
        item.put("detail", nvl(audit.getDetail()));
        item.put("createTime", formatTime(audit.getCreateTime()));
        return item;
    }

    // ------------------------------------------------------------------
    // 工具
    // ------------------------------------------------------------------

    private Set<Integer> classIdsForCommunity(Integer communityId) {
        List<Area> areas = areaMapper.selectList(new QueryWrapper<Area>()
                .eq("community_id", communityId)
                .isNull("delete_time"));
        List<Integer> areaIds = new ArrayList<>();
        for (Area area : areas) {
            areaIds.add(area.getId());
        }
        QueryWrapper<CourseClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0);
        if (areaIds.isEmpty()) {
            queryWrapper.eq("community_id", communityId);
        } else {
            queryWrapper.and(wrapper -> wrapper.eq("community_id", communityId)
                    .or().in("area_id", areaIds));
        }
        return idsOf(courseClassMapper.selectList(queryWrapper));
    }

    private Set<Integer> classIdsByName(String keyword) {
        return idsOf(courseClassMapper.selectList(new QueryWrapper<CourseClass>()
                .eq("is_delete", 0)
                .like("name", keyword)));
    }

    private Set<Integer> idsOf(List<CourseClass> classes) {
        Set<Integer> ids = new LinkedHashSet<>();
        for (CourseClass courseClass : classes) {
            ids.add(courseClass.getId());
        }
        return ids;
    }

    private Set<Integer> intersect(Set<Integer> left, Set<Integer> right) {
        if (left == null) {
            return new LinkedHashSet<>(right);
        }
        Set<Integer> result = new LinkedHashSet<>(left);
        result.retainAll(right);
        return result;
    }

    private Integer effectiveCoachId(CourseLesson lesson, CourseClass courseClass) {
        if (lesson != null && lesson.getCoachId() != null && lesson.getCoachId() > 0) {
            return lesson.getCoachId();
        }
        return courseClass == null ? null : courseClass.getInstructorId();
    }

    private String resolveTime(CourseLesson lesson, CourseClass courseClass) {
        String time = ClassScheduleUtil.resolveLessonTimeSlot(lesson, courseClass);
        if (StringUtils.isBlank(time) && courseClass != null) {
            time = courseClass.getTimeSlot();
        }
        return nvl(time);
    }

    private String lessonKey(Integer classId, Integer lessonNo) {
        return String.valueOf(classId) + "-" + String.valueOf(lessonNo == null ? 0 : lessonNo);
    }

    private String studentName(Student student) {
        return student == null || StringUtils.isBlank(student.getName()) ? "学员" : student.getName();
    }

    private String userName(User user) {
        if (user == null) {
            return "";
        }
        if (StringUtils.isNotBlank(user.getRealName())) {
            return user.getRealName();
        }
        return nvl(user.getNickname());
    }

    private String adminName(Admin admin, Integer adminId) {
        if (admin == null) {
            return "管理员" + adminId;
        }
        if (StringUtils.isNotBlank(admin.getName())) {
            return admin.getName();
        }
        return "管理员" + adminId;
    }

    private String bookingStatusText(Integer status) {
        if (status == null) {
            return "未知";
        }
        if (status == BOOKING_ATTENDED) {
            return "已上课";
        }
        if (status == BOOKING_CANCELLED) {
            return "已取消";
        }
        return "已预约";
    }

    private String genderText(Integer gender) {
        if (gender == null) {
            return "";
        }
        if (gender == 1) {
            return "男";
        }
        if (gender == 2) {
            return "女";
        }
        return "";
    }

    private String weekday(String date) {
        LocalDate value = parseDate(date);
        if (value == null) {
            return "";
        }
        return WEEK_CN[value.getDayOfWeek().getValue() % 7];
    }

    private LocalDate parseDate(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        try {
            return LocalDate.parse(date.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private int countImages(String images) {
        if (StringUtils.isBlank(images)) {
            return 0;
        }
        int count = 0;
        for (String item : images.split(",")) {
            if (StringUtils.isNotBlank(item)) {
                count++;
            }
        }
        return count;
    }

    private List<String> photoUrls(String images) {
        List<String> urls = new ArrayList<>();
        if (StringUtils.isBlank(images)) {
            return urls;
        }
        for (String item : images.split(",")) {
            if (StringUtils.isNotBlank(item)) {
                urls.add(UrlUtils.toAdminAbsoluteUrl(item.trim()));
            }
        }
        return urls;
    }

    private String formatTime(Integer timestamp) {
        if (timestamp == null || timestamp <= 0) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(timestamp.longValue() * 1000L));
    }

    private String formatTime(Long timestamp) {
        return timestamp == null ? "" : formatTime(timestamp.intValue());
    }

    /**
     * 审计可见性：根管理员或拥有 course_checkin/audit 权限的角色。
     * 仅有详情权限的角色可以看到名单/券预览，但不下发审计记录。
     */
    private boolean canViewAudit() {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        if (adminId != null && adminId == 1) {
            return true;
        }
        try {
            return StpUtil.hasPermission("course_checkin/audit");
        } catch (Exception e) {
            return false;
        }
    }

    private String trimRef(String ref) {
        if (ref == null) {
            return "";
        }
        return ref.length() > 250 ? ref.substring(0, 250) : ref;
    }

    private String trimRemark(String remark) {
        if (remark == null) {
            return "";
        }
        return remark.length() > 250 ? remark.substring(0, 250) : remark;
    }

    private Integer parseInt(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value).trim());
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private Integer toInt(Object value) {
        return parseInt(value);
    }

    private String nvl(String text) {
        return text == null ? "" : text;
    }
}
