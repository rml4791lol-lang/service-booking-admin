package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.InstructorLeave;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseLessonLog;
import com.mdd.common.entity.region.Area;
import com.mdd.common.entity.student.Student;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorLeaveMapper;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseLessonLogMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.region.AreaMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.service.CourseAttendanceService;
import com.mdd.common.util.ClassScheduleUtil;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.front.service.ICoachCheckinService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
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
 * 教练核销闭环实现（T29.6 / #26）。
 *
 * <p>数据源：</p>
 * <ul>
 *   <li>{@code ys_course_lesson}：场次、教练、状态（0=待上课，1=已结课，2=已取消）、
 *       教练签到；本票用 {@code status=1} 作为「场次已核销」状态标记；</li>
 *   <li>{@code la_course_booking}：预约状态（1=已预约，3=已取消，10=已上课）；</li>
 *   <li>{@code la_coupon_instance} / {@code la_coupon_flow}：逐人扣券与只增流水；</li>
 *   <li>{@code la_course_consume_log}：消课流水（每人一条，供报表/分账沿用）；</li>
 *   <li>{@code la_course_lesson_log}：课堂照片与点评，家长端 #25 记录页从这里读取。</li>
 * </ul>
 *
 * <p>幂等：提交时对场次行加 {@code SELECT ... FOR UPDATE}，事务内先校验
 * {@code status=1} 已核销则直接拒绝；提交成功后场次置 1，重复提交不会再扣券、
 * 不会重复写流水与记录。单实例另有 {@code synchronized} 兜底。</p>
 *
 * <p>无券口径：到场学员没有可用次数券（无券 / 次数用完 / 不适用）时**不阻断核销**，
 * 该学员仍记已上课并生成记录，本次不扣券，在响应 {@code warnings} 中逐人提示；
 * 教练可提示家长购券或转客服（#37 补扣）。</p>
 */
@Service
public class CoachCheckinServiceImpl implements ICoachCheckinService {

    private static final int BOOKING_CANCELLED = 3;
    private static final int BOOKING_ATTENDED = 10;
    private static final int LESSON_FINISHED = 1;
    private static final int LESSON_CANCELLED = 2;

    private static final int DEFAULT_PAGE_SIZE = 50;
    private static final int MAX_PAGE_SIZE = 100;
    private static final int MAX_PHOTOS = 9;

    private static final String[] WEEK_CN = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    private static final String[] STUDENT_COLORS = new String[]{"#FF6B35", "#3B7CFF", "#1FA971", "#B980F0", "#F2B705"};

    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CourseBookingMapper courseBookingMapper;
    @Resource
    CourseLessonLogMapper courseLessonLogMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    InstructorLeaveMapper instructorLeaveMapper;
    @Resource
    AreaMapper areaMapper;
    @Resource
    CourseAttendanceService courseAttendanceService;

    // ------------------------------------------------------------------
    // 场次列表 / 详情 / 名单
    // ------------------------------------------------------------------

    @Override
    public List<Map<String, Object>> sessions(Integer instructorId, String date, Integer pageNo, Integer pageSize) {
        if (instructorId == null || instructorId <= 0) {
            return Collections.emptyList();
        }
        String from = LocalDate.now().minusDays(30).toString();
        String to = LocalDate.now().plusDays(30).toString();
        QueryWrapper<CourseLesson> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("status", LESSON_CANCELLED);
        if (StringUtils.isNotBlank(date)) {
            queryWrapper.eq("lesson_date", date.trim());
        } else {
            queryWrapper.ge("lesson_date", from).le("lesson_date", to);
        }
        applyCoachScope(queryWrapper, instructorId);
        queryWrapper.orderByAsc("lesson_date").orderByAsc("time_slot").orderByAsc("id");

        List<CourseLesson> lessons = courseLessonMapper.selectList(queryWrapper);
        if (lessons.isEmpty()) {
            return Collections.emptyList();
        }
        SessionContext ctx = buildContext(lessons);
        List<Map<String, Object>> items = new ArrayList<>();
        for (CourseLesson lesson : lessons) {
            CourseClass courseClass = ctx.classes.get(lesson.getClassId());
            if (courseClass == null || !canAccess(instructorId, lesson, courseClass)) {
                continue;
            }
            items.add(toSession(lesson, courseClass, ctx, false));
        }
        return slice(items, pageNo, pageSize);
    }

    @Override
    public Map<String, Object> session(Integer instructorId, Integer sessionId) {
        if (instructorId == null || instructorId <= 0 || sessionId == null || sessionId <= 0) {
            return null;
        }
        CourseLesson lesson = courseLessonMapper.selectById(sessionId);
        if (lesson == null) {
            return null;
        }
        CourseClass courseClass = courseClassMapper.selectById(lesson.getClassId());
        if (courseClass == null || (courseClass.getIsDelete() != null && courseClass.getIsDelete() == 1)
                || !canAccess(instructorId, lesson, courseClass)) {
            return null;
        }
        SessionContext ctx = buildContext(Collections.singletonList(lesson));
        return toSession(lesson, courseClass, ctx, true);
    }

    @Override
    public List<Map<String, Object>> roster(Integer instructorId, Integer sessionId) {
        if (instructorId == null || instructorId <= 0 || sessionId == null || sessionId <= 0) {
            return Collections.emptyList();
        }
        CourseLesson lesson = courseLessonMapper.selectById(sessionId);
        if (lesson == null) {
            return Collections.emptyList();
        }
        CourseClass courseClass = courseClassMapper.selectById(lesson.getClassId());
        if (courseClass == null || (courseClass.getIsDelete() != null && courseClass.getIsDelete() == 1)
                || !canAccess(instructorId, lesson, courseClass)) {
            return Collections.emptyList();
        }
        SessionContext ctx = buildContext(Collections.singletonList(lesson));
        return rosterItems(lesson, ctx);
    }

    // ------------------------------------------------------------------
    // 提交核销
    // ------------------------------------------------------------------

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Map<String, Object> submit(Integer instructorId, Integer sessionId,
                                                   List<Integer> attendedStudentIds, List<String> photos,
                                                   String comment) {
        if (instructorId == null || instructorId <= 0) {
            throw new OperateException("请先登录教练账号");
        }
        if (sessionId == null || sessionId <= 0) {
            throw new OperateException("场次不存在");
        }
        // 请假拦截（#170 能力对齐）：教练审批中的请假期间不允许核销，与老接口
        // /instructor/attendance/* 的拦截口径一致。
        long nowSeconds = System.currentTimeMillis() / 1000L;
        Long activeLeave = instructorLeaveMapper.selectCount(new QueryWrapper<InstructorLeave>()
                .eq("instructor_id", instructorId)
                .eq("status", 1)
                .isNull("delete_time")
                .le("start_time", nowSeconds)
                .ge("end_time", nowSeconds));
        if (activeLeave != null && activeLeave > 0) {
            throw new OperateException("您当前处于请假状态，无法点名");
        }
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
        if (!canAccess(instructorId, lesson, courseClass)) {
            throw new OperateException("只能核销本人负责的课程");
        }
        if (lesson.getStatus() != null && lesson.getStatus() == LESSON_CANCELLED) {
            throw new OperateException("该场次已取消，不能核销");
        }
        if (lesson.getStatus() != null && lesson.getStatus() == LESSON_FINISHED) {
            throw new OperateException("该场次已核销，不能重复提交");
        }
        if (attendedStudentIds == null || attendedStudentIds.isEmpty()) {
            throw new OperateException("请至少勾选一名到场学员");
        }
        List<String> cleanPhotos = normalizePhotos(photos);
        if (cleanPhotos.isEmpty()) {
            throw new OperateException("请至少上传一张课程结束照片");
        }
        String cleanComment = comment == null ? "" : comment.trim();
        if (cleanComment.length() > 200) {
            throw new OperateException("课堂点评不能超过 200 字");
        }

        // 名单：只认本场次有效预约（status<>3），先整体校验再写，避免部分扣券。
        List<CourseBooking> bookings = courseBookingMapper.selectList(new QueryWrapper<CourseBooking>()
                .eq("lesson_id", lesson.getId())
                .ne("status", BOOKING_CANCELLED)
                .orderByAsc("id"));
        Map<Integer, CourseBooking> bookingMap = new LinkedHashMap<>();
        Set<Integer> studentIds = new LinkedHashSet<>();
        for (CourseBooking booking : bookings) {
            if (booking.getStudentId() == null || booking.getStudentId() <= 0) {
                continue;
            }
            bookingMap.put(booking.getStudentId(), booking);
            studentIds.add(booking.getStudentId());
        }
        Set<Integer> attended = new LinkedHashSet<>();
        for (Integer studentId : attendedStudentIds) {
            if (studentId == null || studentId <= 0) {
                throw new OperateException("到场学员参数错误");
            }
            if (!studentIds.contains(studentId)) {
                throw new OperateException("该学员不在本场次预约名单中");
            }
            attended.add(studentId);
        }
        if (attended.isEmpty()) {
            throw new OperateException("请至少勾选一名到场学员");
        }
        Map<Integer, Instructor> instructors = new HashMap<>();
        Integer coachId = effectiveCoachId(lesson, courseClass);
        if (coachId != null && coachId > 0) {
            Instructor instructor = instructorMapper.selectById(coachId);
            if (instructor != null) {
                instructors.put(instructor.getId(), instructor);
            }
        }
        String time = resolveTime(lesson, courseClass);
        String ref = trimRef(courseClass.getName() + " " + lesson.getLessonDate() + " " + time
                + " 第" + (lesson.getLessonNo() == null ? 0 : lesson.getLessonNo()) + "节");

        long nowMillis = System.currentTimeMillis();
        int now = (int) (nowMillis / 1000L);
        // 与后台人工补核销共用同一份逐人扣券/考勤写入逻辑（#37 抽取）。
        Map<String, Object> attendance = courseAttendanceService.markAttended(
                lesson, courseClass, new ArrayList<>(bookingMap.values()), attended,
                "coach", instructorId, "教练核销扣减", 0, ref);
        @SuppressWarnings("unchecked")
        List<String> warnings = (List<String>) attendance.get("warnings");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> deductions = (List<Map<String, Object>>) attendance.get("deductions");

        // 场次置已完成：这是场次级幂等的状态标记。
        lesson.setStatus(LESSON_FINISHED);
        lesson.setCoachCheckIn(1);
        lesson.setUpdateTime(now);
        courseLessonMapper.updateById(lesson);

        // 课堂照片/点评：家长端 #25 记录页按 班级+课节 读取。
        CourseLessonLog lessonLog = courseLessonLogMapper.selectOne(new QueryWrapper<CourseLessonLog>()
                .eq("course_class_id", courseClass.getId())
                .eq("lesson_no", lesson.getLessonNo() == null ? 0 : lesson.getLessonNo())
                .isNull("delete_time")
                .orderByDesc("id")
                .last("limit 1"));
        String images = String.join(",", cleanPhotos);
        if (lessonLog == null) {
            lessonLog = new CourseLessonLog();
            lessonLog.setCourseClassId(courseClass.getId());
            lessonLog.setInstructorId(instructorId);
            lessonLog.setLessonNo(lesson.getLessonNo() == null ? 0 : lesson.getLessonNo());
            lessonLog.setSummary(cleanComment);
            lessonLog.setImages(images);
            lessonLog.setCreateTime(now);
            lessonLog.setUpdateTime(now);
            courseLessonLogMapper.insert(lessonLog);
        } else {
            lessonLog.setInstructorId(instructorId);
            lessonLog.setImages(images);
            if (StringUtils.isNotBlank(cleanComment)) {
                lessonLog.setSummary(cleanComment);
            }
            lessonLog.setUpdateTime(now);
            courseLessonLogMapper.updateById(lessonLog);
        }

        Map<String, Object> checkin = new LinkedHashMap<>();
        checkin.put("id", String.valueOf(lesson.getId()));
        checkin.put("sessionId", String.valueOf(lesson.getId()));
        checkin.put("courseId", String.valueOf(courseClass.getId()));
        checkin.put("coachId", String.valueOf(instructorId));
        checkin.put("attendedStudentIds", textList(attended));
        checkin.put("photos", absoluteList(cleanPhotos));
        checkin.put("comment", cleanComment);
        checkin.put("time", formatTime(lesson.getUpdateTime()));
        Instructor operator = instructors.get(instructorId);
        String operatorName = operator == null ? courseClass.getInstructorName() : operator.getName();
        checkin.put("operator", StringUtils.isBlank(operatorName) ? "教练本人" : operatorName + "（教练本人）");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("ok", true);
        result.put("count", attended.size());
        result.put("attendedStudentIds", textList(attended));
        result.put("photos", absoluteList(cleanPhotos));
        result.put("checkin", checkin);
        result.put("warnings", warnings);
        result.put("deductions", deductions);
        return result;
    }

    // ------------------------------------------------------------------
    // 教练上课记录
    // ------------------------------------------------------------------

    @Override
    public List<Map<String, Object>> records(Integer instructorId, Integer pageNo, Integer pageSize) {
        if (instructorId == null || instructorId <= 0) {
            return Collections.emptyList();
        }
        List<Integer> classIds = new ArrayList<>();
        List<CourseClass> classes = courseClassMapper.selectList(new QueryWrapper<CourseClass>()
                .eq("instructor_id", instructorId)
                .eq("is_delete", 0));
        for (CourseClass courseClass : classes) {
            classIds.add(courseClass.getId());
        }
        QueryWrapper<CourseLesson> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", LESSON_FINISHED);
        queryWrapper.and(wrapper -> {
            wrapper.eq("coach_id", instructorId);
            if (!classIds.isEmpty()) {
                wrapper.or(inner -> inner.eq("coach_id", 0).in("class_id", classIds));
            }
        });
        queryWrapper.orderByDesc("lesson_date").orderByDesc("time_slot").orderByDesc("id");
        List<CourseLesson> lessons = courseLessonMapper.selectList(queryWrapper);
        if (lessons.isEmpty()) {
            return Collections.emptyList();
        }
        SessionContext ctx = buildContext(lessons);
        List<Map<String, Object>> items = new ArrayList<>();
        for (CourseLesson lesson : lessons) {
            CourseClass courseClass = ctx.classes.get(lesson.getClassId());
            if (courseClass == null || !canAccess(instructorId, lesson, courseClass)) {
                continue;
            }
            Instructor instructor = ctx.instructors.get(effectiveCoachId(lesson, courseClass));
            String coachName = instructor == null ? nvl(courseClass.getInstructorName()) : nvl(instructor.getName());
            String coachAvatar = instructor == null ? "" : UrlUtils.toAbsoluteUrl(instructor.getAvatar());
            CourseLessonLog log = ctx.logs.get(lessonKey(lesson.getClassId(), lesson.getLessonNo()));
            List<String> photos = log == null ? Collections.emptyList() : splitPhotos(log.getImages());
            String comment = log == null ? "" : nvl(log.getSummary());
            String time = resolveTime(lesson, courseClass);
            for (CourseBooking booking : ctx.bookings.getOrDefault(lesson.getId(), Collections.emptyList())) {
                if (booking.getStatus() == null || booking.getStatus() != BOOKING_ATTENDED) {
                    continue;
                }
                Student student = ctx.students.get(booking.getStudentId());
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", String.valueOf(booking.getId()));
                item.put("sessionId", String.valueOf(lesson.getId()));
                item.put("courseId", String.valueOf(courseClass.getId()));
                item.put("studentId", String.valueOf(booking.getStudentId()));
                item.put("coachId", String.valueOf(effectiveCoachId(lesson, courseClass)));
                item.put("date", nvl(lesson.getLessonDate()));
                item.put("time", time);
                item.put("lessonNo", lesson.getLessonNo() == null ? 0 : lesson.getLessonNo());
                item.put("attended", true);
                item.put("couponTimes", 1);
                item.put("status", "done");
                item.put("photos", photos);
                item.put("comment", comment);
                item.put("coachName", coachName);
                item.put("coachAvatar", coachAvatar);
                item.put("courseTitle", nvl(courseClass.getName()));
                item.put("cover", UrlUtils.toAbsoluteUrl(courseClass.getImage()));
                item.put("studentName", student == null ? "学员" : nvl(student.getName()));
                item.put("dayLabel", dayLabel(lesson.getLessonDate()));
                item.put("shortDate", shortDate(lesson.getLessonDate()));
                item.put("weekday", weekday(lesson.getLessonDate()));
                item.put("createdAt", formatTime(lesson.getUpdateTime()));
                items.add(item);
            }
        }
        return slice(items, pageNo, pageSize);
    }

    // ------------------------------------------------------------------
    // 组装
    // ------------------------------------------------------------------

    private Map<String, Object> toSession(CourseLesson lesson, CourseClass courseClass,
                                          SessionContext ctx, boolean withRoster) {
        Integer coachId = effectiveCoachId(lesson, courseClass);
        Instructor instructor = ctx.instructors.get(coachId);
        Area area = ctx.areas.get(courseClass.getAreaId());
        int capacity = courseClass.getMaxStudents() == null ? 0 : courseClass.getMaxStudents();
        int booked = ctx.bookedCounts.getOrDefault(lesson.getId(), 0);
        int remaining = Math.max(capacity - booked, 0);
        String time = resolveTime(lesson, courseClass);

        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", String.valueOf(lesson.getId()));
        item.put("courseId", String.valueOf(courseClass.getId()));
        item.put("areaId", idText(courseClass.getAreaId()));
        item.put("coachId", idText(coachId));
        item.put("date", nvl(lesson.getLessonDate()));
        item.put("time", time);
        item.put("capacity", capacity);
        item.put("booked", booked);
        item.put("attendCount", booked);
        item.put("deadline", nvl(lesson.getLessonDate()));
        item.put("deadlineAt", nvl(lesson.getLessonDate()) + " 23:59");
        item.put("status", statusText(lesson.getStatus()));
        item.put("courseTitle", nvl(courseClass.getName()));
        item.put("cover", UrlUtils.toAbsoluteUrl(courseClass.getImage()));
        item.put("price", courseClass.getFeePerLesson() != null
                && courseClass.getFeePerLesson().signum() > 0
                ? courseClass.getFeePerLesson()
                : courseClass.getTotalFee());
        item.put("duration", 60);
        item.put("ageMin", courseClass.getAgeMin());
        item.put("ageMax", courseClass.getAgeMax());
        item.put("areaName", area == null ? "" : nvl(area.getName()));
        item.put("venue", ClassScheduleUtil.resolveLessonVenue(lesson, courseClass));
        item.put("coachName", instructor == null ? nvl(courseClass.getInstructorName()) : nvl(instructor.getName()));
        item.put("coachAvatar", instructor == null ? "" : UrlUtils.toAbsoluteUrl(instructor.getAvatar()));
        item.put("remaining", remaining);
        item.put("full", remaining <= 0);
        item.put("dayLabel", dayLabel(lesson.getLessonDate()));
        item.put("shortDate", shortDate(lesson.getLessonDate()));
        item.put("weekday", weekday(lesson.getLessonDate()));
        item.put("lessonNo", lesson.getLessonNo() == null ? 0 : lesson.getLessonNo());
        item.put("lessonType", lesson.getLessonType() == null ? 0 : lesson.getLessonType());
        item.put("weekDay", lesson.getWeekDay() == null ? 0 : lesson.getWeekDay());
        item.put("checkin", buildCheckin(lesson, courseClass, ctx, instructor));
        if (withRoster) {
            item.put("roster", rosterItems(lesson, ctx));
            Map<String, Object> course = new LinkedHashMap<>();
            course.put("id", String.valueOf(courseClass.getId()));
            course.put("title", nvl(courseClass.getName()));
            course.put("cover", UrlUtils.toAbsoluteUrl(courseClass.getImage()));
            course.put("summary", nvl(courseClass.getSummary()));
            course.put("areaName", area == null ? "" : nvl(area.getName()));
            course.put("venue", ClassScheduleUtil.resolveLessonVenue(lesson, courseClass));
            course.put("price", item.get("price"));
            course.put("duration", 60);
            course.put("ageMin", courseClass.getAgeMin());
            course.put("ageMax", courseClass.getAgeMax());
            course.put("coachName", item.get("coachName"));
            item.put("course", course);
        }
        return item;
    }

    private Map<String, Object> buildCheckin(CourseLesson lesson, CourseClass courseClass,
                                             SessionContext ctx, Instructor instructor) {
        if (lesson.getStatus() == null || lesson.getStatus() != LESSON_FINISHED) {
            return null;
        }
        List<String> attended = new ArrayList<>();
        for (CourseBooking booking : ctx.bookings.getOrDefault(lesson.getId(), Collections.emptyList())) {
            if (booking.getStatus() != null && booking.getStatus() == BOOKING_ATTENDED
                    && booking.getStudentId() != null) {
                attended.add(String.valueOf(booking.getStudentId()));
            }
        }
        CourseLessonLog log = ctx.logs.get(lessonKey(lesson.getClassId(), lesson.getLessonNo()));
        List<String> photos = log == null ? new ArrayList<>() : splitPhotos(log.getImages());
        String operatorName = instructor == null ? nvl(courseClass.getInstructorName()) : nvl(instructor.getName());
        Map<String, Object> checkin = new LinkedHashMap<>();
        checkin.put("id", String.valueOf(lesson.getId()));
        checkin.put("sessionId", String.valueOf(lesson.getId()));
        checkin.put("courseId", String.valueOf(courseClass.getId()));
        checkin.put("coachId", idText(effectiveCoachId(lesson, courseClass)));
        checkin.put("attendedStudentIds", attended);
        checkin.put("photos", photos);
        checkin.put("comment", log == null ? "" : nvl(log.getSummary()));
        checkin.put("time", formatTime(lesson.getUpdateTime()));
        checkin.put("operator", StringUtils.isBlank(operatorName) ? "教练本人（历史核销）" : operatorName + "（教练本人）");
        return checkin;
    }

    private List<Map<String, Object>> rosterItems(CourseLesson lesson, SessionContext ctx) {
        List<Map<String, Object>> items = new ArrayList<>();
        int index = 0;
        for (CourseBooking booking : ctx.bookings.getOrDefault(lesson.getId(), Collections.emptyList())) {
            if (booking.getStatus() != null && booking.getStatus() == BOOKING_CANCELLED) {
                continue;
            }
            Student student = ctx.students.get(booking.getStudentId());
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("bookingId", String.valueOf(booking.getId()));
            item.put("studentId", String.valueOf(booking.getStudentId()));
            item.put("name", student == null ? "学员" : nvl(student.getName()));
            item.put("gender", genderText(student == null ? null : student.getGender()));
            item.put("age", student == null || student.getAge() == null ? "" : student.getAge());
            item.put("color", STUDENT_COLORS[index % STUDENT_COLORS.length]);
            item.put("status", booking.getStatus() != null && booking.getStatus() == BOOKING_ATTENDED
                    ? "attended" : "booked");
            item.put("statusCode", booking.getStatus() == null ? 1 : booking.getStatus());
            item.put("avatar", student == null ? "" : UrlUtils.toAbsoluteUrl(student.getAvatar()));
            items.add(item);
            index++;
        }
        return items;
    }

    // ------------------------------------------------------------------
    // 上下文与工具
    // ------------------------------------------------------------------

    private static class SessionContext {
        Map<Integer, CourseClass> classes = new HashMap<>();
        Map<Integer, Instructor> instructors = new HashMap<>();
        Map<Integer, Area> areas = new HashMap<>();
        Map<Integer, Student> students = new HashMap<>();
        Map<Integer, Integer> bookedCounts = new HashMap<>();
        Map<Integer, List<CourseBooking>> bookings = new HashMap<>();
        Map<String, CourseLessonLog> logs = new HashMap<>();
    }

    private SessionContext buildContext(List<CourseLesson> lessons) {
        SessionContext ctx = new SessionContext();
        List<Integer> lessonIds = new ArrayList<>();
        Set<Integer> classIds = new LinkedHashSet<>();
        Set<Integer> lessonNos = new LinkedHashSet<>();
        for (CourseLesson lesson : lessons) {
            lessonIds.add(lesson.getId());
            classIds.add(lesson.getClassId());
            if (lesson.getLessonNo() != null) {
                lessonNos.add(lesson.getLessonNo());
            }
        }
        if (lessonIds.isEmpty()) {
            return ctx;
        }
        if (!classIds.isEmpty()) {
            for (CourseClass courseClass : courseClassMapper.selectBatchIds(classIds)) {
                ctx.classes.put(courseClass.getId(), courseClass);
            }
        }
        Set<Integer> areaIds = new HashSet<>();
        Set<Integer> coachIds = new HashSet<>();
        for (CourseClass courseClass : ctx.classes.values()) {
            if (courseClass.getAreaId() != null && courseClass.getAreaId() > 0) {
                areaIds.add(courseClass.getAreaId());
            }
        }
        for (CourseLesson lesson : lessons) {
            Integer coachId = effectiveCoachId(lesson, ctx.classes.get(lesson.getClassId()));
            if (coachId != null && coachId > 0) {
                coachIds.add(coachId);
            }
        }
        if (!areaIds.isEmpty()) {
            for (Area area : areaMapper.selectBatchIds(areaIds)) {
                ctx.areas.put(area.getId(), area);
            }
        }
        if (!coachIds.isEmpty()) {
            for (Instructor instructor : instructorMapper.selectBatchIds(coachIds)) {
                ctx.instructors.put(instructor.getId(), instructor);
            }
        }

        List<Map<String, Object>> rows = courseBookingMapper.selectMaps(new QueryWrapper<CourseBooking>()
                .select("lesson_id", "COUNT(*) AS booked_count")
                .ne("status", BOOKING_CANCELLED)
                .in("lesson_id", lessonIds)
                .groupBy("lesson_id"));
        if (rows != null) {
            for (Map<String, Object> row : rows) {
                Object lessonId = row.get("lesson_id");
                Object count = row.get("booked_count");
                if (lessonId instanceof Number && count instanceof Number) {
                    ctx.bookedCounts.put(((Number) lessonId).intValue(), ((Number) count).intValue());
                }
            }
        }

        Set<Integer> studentIds = new HashSet<>();
        List<CourseBooking> bookings = courseBookingMapper.selectList(new QueryWrapper<CourseBooking>()
                .in("lesson_id", lessonIds)
                .orderByAsc("id"));
        for (CourseBooking booking : bookings) {
            ctx.bookings.computeIfAbsent(booking.getLessonId(), key -> new ArrayList<>()).add(booking);
            if (booking.getStudentId() != null && booking.getStudentId() > 0) {
                studentIds.add(booking.getStudentId());
            }
        }
        if (!studentIds.isEmpty()) {
            for (Student student : studentMapper.selectBatchIds(studentIds)) {
                ctx.students.put(student.getId(), student);
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
                if (!ctx.logs.containsKey(key)) {
                    ctx.logs.put(key, log);
                }
            }
        }
        return ctx;
    }

    private void applyCoachScope(QueryWrapper<CourseLesson> queryWrapper, Integer instructorId) {
        List<CourseClass> classes = courseClassMapper.selectList(new QueryWrapper<CourseClass>()
                .select("id")
                .eq("instructor_id", instructorId)
                .eq("is_delete", 0));
        List<Integer> classIds = new ArrayList<>();
        for (CourseClass courseClass : classes) {
            classIds.add(courseClass.getId());
        }
        queryWrapper.and(wrapper -> {
            wrapper.eq("coach_id", instructorId);
            if (!classIds.isEmpty()) {
                wrapper.or(inner -> inner.eq("coach_id", 0).in("class_id", classIds));
            }
        });
    }

    private boolean canAccess(Integer instructorId, CourseLesson lesson, CourseClass courseClass) {
        Integer coachId = effectiveCoachId(lesson, courseClass);
        return coachId != null && coachId > 0 && coachId.equals(instructorId);
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

    private List<String> normalizePhotos(List<String> photos) {
        if (photos == null || photos.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> clean = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (String photo : photos) {
            if (StringUtils.isBlank(photo)) {
                continue;
            }
            String value = normalizePhotoPath(photo);
            if (value == null) {
                throw new OperateException("照片地址无效，请重新上传");
            }
            if (value.length() > 500) {
                throw new OperateException("照片地址过长，请重新上传");
            }
            if (seen.add(value)) {
                clean.add(value);
            }
        }
        if (clean.size() > MAX_PHOTOS) {
            throw new OperateException("最多上传 " + MAX_PHOTOS + " 张照片");
        }
        return clean;
    }

    /**
     * 照片地址归一：绝对地址原样保留；本地上传返回的 {@code image/...} 直接落库
     * （由 UrlUtils 拼成 /api/uploads/...）；兼容误传的 /api/uploads、/uploads 前缀。
     */
    private String normalizePhotoPath(String photo) {
        if (photo == null) {
            return null;
        }
        String value = photo.trim();
        if (value.isEmpty()) {
            return null;
        }
        if (value.startsWith("http://") || value.startsWith("https://")) {
            return value.length() > 500 ? null : value;
        }
        while (value.startsWith("/")) {
            value = value.substring(1);
        }
        if (value.startsWith("api/uploads/")) {
            value = value.substring("api/uploads/".length());
        } else if (value.startsWith("uploads/")) {
            value = value.substring("uploads/".length());
        }
        if (!value.startsWith("image/") || value.contains("..") || value.length() > 500) {
            return null;
        }
        return value;
    }

    private List<String> splitPhotos(String images) {
        List<String> photos = new ArrayList<>();
        if (StringUtils.isBlank(images)) {
            return photos;
        }
        for (String item : Arrays.asList(images.split(","))) {
            if (StringUtils.isNotBlank(item)) {
                photos.add(UrlUtils.toAbsoluteUrl(item.trim()));
            }
        }
        return photos;
    }

    private List<String> absoluteList(List<String> photos) {
        List<String> list = new ArrayList<>();
        for (String photo : photos) {
            list.add(UrlUtils.toAbsoluteUrl(photo));
        }
        return list;
    }

    private List<String> textList(Set<Integer> ids) {
        List<String> list = new ArrayList<>();
        for (Integer id : ids) {
            list.add(String.valueOf(id));
        }
        return list;
    }

    private List<Map<String, Object>> slice(List<Map<String, Object>> items, Integer pageNo, Integer pageSize) {
        int page = pageNo == null || pageNo <= 0 ? 1 : pageNo;
        int size = pageSize == null || pageSize <= 0 ? DEFAULT_PAGE_SIZE : Math.min(pageSize, MAX_PAGE_SIZE);
        int from = (page - 1) * size;
        if (from >= items.size()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(items.subList(from, Math.min(from + size, items.size())));
    }

    private String lessonKey(Integer classId, Integer lessonNo) {
        return String.valueOf(classId) + "-" + String.valueOf(lessonNo);
    }

    private String trimRef(String ref) {
        if (ref == null) {
            return "";
        }
        return ref.length() > 250 ? ref.substring(0, 250) : ref;
    }

    private String statusText(Integer status) {
        if (status != null && status == LESSON_FINISHED) {
            return "finished";
        }
        if (status != null && status == LESSON_CANCELLED) {
            return "cancelled";
        }
        return "open";
    }

    private String dayLabel(String date) {
        LocalDate value = parseDate(date);
        if (value == null) {
            return "";
        }
        long diff = ChronoUnit.DAYS.between(LocalDate.now(), value);
        if (diff == 0) {
            return "今天";
        }
        if (diff == 1) {
            return "明天";
        }
        if (diff == 2) {
            return "后天";
        }
        return weekday(date);
    }

    private String shortDate(String date) {
        LocalDate value = parseDate(date);
        if (value == null) {
            return "";
        }
        return String.format("%02d.%02d", value.getMonthValue(), value.getDayOfMonth());
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
            return LocalDate.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    private String formatTime(Integer timestamp) {
        if (timestamp == null || timestamp <= 0) {
            return "";
        }
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
                .format(new Date(timestamp.longValue() * 1000L));
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

    private String idText(Integer id) {
        return id == null || id <= 0 ? "" : String.valueOf(id);
    }

    private String nvl(String text) {
        return text == null ? "" : text;
    }
}
