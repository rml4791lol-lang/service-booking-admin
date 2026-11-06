package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseLessonLog;
import com.mdd.common.entity.region.Area;
import com.mdd.common.entity.student.Student;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseLessonLogMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.region.AreaMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.util.ClassScheduleUtil;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.front.service.IBookingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 小程序预约与上课记录实现（#25）。
 *
 * <p>数据源：</p>
 * <ul>
 *   <li>{@code la_course_booking}：预约/上课（status 1=已预约，3=已取消，10=已上课）；</li>
 *   <li>{@code ys_course_lesson}：场次日期/时段/教练/状态；</li>
 *   <li>{@code la_course_class}：课程名称/封面/场地/最大人数；</li>
 *   <li>{@code la_course_lesson_log}：课堂照片（多图逗号分隔）与教练点评；</li>
 *   <li>{@code la_student}：学员，按 {@code user_id} 隔离。</li>
 * </ul>
 *
 * <p>名额口径与 #22 完全一致：余位 = {@code la_course_class.max_students} -
 * {@code la_course_booking.status <> 3} 的预约数（按 {@code lesson_id} 统计）。
 * 报名截止 = 场次当天 23:59，因此日期早于今天即视为截止。创建预约只写
 * {@code la_course_booking}，不写消课流水、不扣券。</p>
 *
 * <p>并发：名额校验与写入放在同一个 {@code synchronized} 临界区里，且不包事务，
 * 让插入在释放锁前自动提交（与 #23 学员上限口径一致，单实例部署有效）。
 * 多实例部署时需要数据库唯一约束或分布式锁，见接口文档「已知限制」。</p>
 */
@Service
public class BookingServiceImpl implements IBookingService {

    /** la_course_booking.status：1=已预约，3=已取消，10=已上课。 */
    private static final int BOOKING_BOOKED = 1;
    private static final int BOOKING_CANCELLED = 3;
    private static final int BOOKING_ATTENDED = 10;

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 50;
    private static final int[] LIVE_CLASS_STATUS = new int[]{1, 2};

    private static final String[] WEEK_CN = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    @Resource
    CourseBookingMapper courseBookingMapper;
    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CourseLessonLogMapper courseLessonLogMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    AreaMapper areaMapper;
    @Resource
    com.mdd.common.mapper.course.UserCourseMapper userCourseMapper;
    @Resource
    com.mdd.common.service.CouponEligibility couponEligibility;

    // ------------------------------------------------------------------
    // 预约创建
    // ------------------------------------------------------------------

    @Override
    public synchronized Map<String, Object> createBooking(Integer userId, Integer sessionId, Integer studentId, String couponId) {
        if (sessionId == null || sessionId <= 0) {
            throw new OperateException("场次不存在");
        }
        if (studentId == null || studentId <= 0) {
            throw new OperateException("请选择学员");
        }

        CourseLesson lesson = courseLessonMapper.selectById(sessionId);
        if (lesson == null) {
            throw new OperateException("场次不存在");
        }
        validateLessonBookable(lesson);

        CourseClass courseClass = lesson.getClassId() == null ? null : courseClassMapper.selectById(lesson.getClassId());
        if (!isLiveClass(courseClass)) {
            throw new OperateException("课程不存在或已下架");
        }

        // 名额：余位 = max_students - 有效预约数（status<>3），与 #22 展示口径一致。
        int capacity = courseClass.getMaxStudents() == null ? 0 : courseClass.getMaxStudents();
        Long booked = courseBookingMapper.selectCount(new QueryWrapper<CourseBooking>()
                .eq("lesson_id", lesson.getId())
                .ne("status", BOOKING_CANCELLED));
        long activeBooked = booked == null ? 0L : booked;
        if (capacity <= 0 || activeBooked >= capacity) {
            throw new OperateException("该场次已约满");
        }

        // 学员必须属于当前登录家长，且未停用（软删）。
        Student student = studentMapper.selectOne(new QueryWrapper<Student>()
                .eq("id", studentId)
                .eq("user_id", userId)
                .isNull("delete_time")
                .last("limit 1"));
        if (student == null) {
            throw new OperateException("学员不存在或已停用");
        }

        // 课前券约束（甲方 2026-11-05）：一张券只能对应一节课，避免「一张券预约多节课」。
        // 可用凭证 = 有效次数券（学员级 + 账号级）+ 存量课包剩余课时（老客户过渡期不阻断）；
        // 已占用凭证 = 该学员当前有效预约数（status=1）。核销扣券后凭证减少，即无法再占用新场次。
        ensureBookingCredential(userId, studentId);

        // 重复预约：同一家长、同一学员、同一场次只能有一条有效预约；
        // 已取消（status=3）的历史行复用，不重复插入。
        CourseBooking existing = courseBookingMapper.selectOne(new QueryWrapper<CourseBooking>()
                .eq("user_id", userId)
                .eq("lesson_id", lesson.getId())
                .eq("student_id", studentId)
                .orderByDesc("id")
                .last("limit 1"));
        if (existing != null && !isCancelled(existing)) {
            throw new OperateException("该学员已预约本场次");
        }

        long now = System.currentTimeMillis() / 1000L;
        CourseBooking booking;
        if (existing != null) {
            existing.setCourseClassId(lesson.getClassId());
            existing.setLessonId(lesson.getId());
            existing.setLessonNo(lesson.getLessonNo());
            existing.setLessonDate(lesson.getLessonDate());
            existing.setStatus(BOOKING_BOOKED);
            existing.setUpdateTime(now);
            courseBookingMapper.updateById(existing);
            booking = existing;
        } else {
            booking = new CourseBooking();
            booking.setUserId(userId);
            booking.setStudentId(studentId);
            booking.setCourseClassId(lesson.getClassId());
            booking.setLessonId(lesson.getId());
            booking.setLessonNo(lesson.getLessonNo());
            booking.setLessonDate(lesson.getLessonDate());
            booking.setStatus(BOOKING_BOOKED);
            booking.setCreateTime(now);
            booking.setUpdateTime(now);
            courseBookingMapper.insert(booking);
        }

        List<Map<String, Object>> items = buildItems(Collections.singletonList(booking), false);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("ok", true);
        result.put("booking", items.isEmpty() ? null : items.get(0));
        // 本票只占名额：couponId 仅为前端兼容字段，不校验、不扣减（券模型属 #24）。
        result.put("couponDeducted", false);
        return result;
    }

    /**
     * 预约凭证校验（甲方 2026-11-05）：一张券只能预约一节课。
     *
     * <p>口径：可用凭证 = 有效次数券（该学员券 + 账号级券，未生效/已过期/用完不计）
     * + 存量课包剩余课时（老客户过渡期，避免未转券前无法约课）；
     * 已占用 = 该学员当前有效预约数（status=1）。凭证不足以覆盖「已占用 + 本次」时拒绝。</p>
     *
     * <p>#221 起券计数改调 {@link CouponEligibility#countUsable}（唯一判定模块，
     * 与核销选券 / 后台发券 / 小程序 mock 同一份规则）；这是凭证计数口径，
     * 不判课程范围（范围口径在核销侧判定）。</p>
     */
    private void ensureBookingCredential(Integer userId, Integer studentId) {
        long now = System.currentTimeMillis() / 1000L;
        Long active = courseBookingMapper.selectCount(new QueryWrapper<CourseBooking>()
                .eq("user_id", userId)
                .eq("student_id", studentId)
                .eq("status", BOOKING_BOOKED));
        long occupied = active == null ? 0L : active;

        long couponCount = couponEligibility.countUsable(userId, studentId, now);

        long legacyRemain = 0L;
        List<com.mdd.common.entity.course.UserCourse> legacy = userCourseMapper.selectList(
                new QueryWrapper<com.mdd.common.entity.course.UserCourse>()
                        .eq("user_id", userId)
                        .eq("student_id", studentId)
                        .eq("status", 0)
                        .isNull("delete_time"));
        for (com.mdd.common.entity.course.UserCourse uc : legacy) {
            if (uc.getRemainLessons() != null && uc.getRemainLessons() > 0) {
                legacyRemain += uc.getRemainLessons();
            }
        }

        if (couponCount + legacyRemain <= occupied) {
            throw new OperateException("该学员暂无可用课程券，请先购买课程券后再预约");
        }
    }

    /**
     * 场次可约校验：状态、日期、报名截止。
     *
     * <p>报名截止 = 场次当天 23:59（与 #22 一致），日期早于今天即已截止；
     * status=1 视为已结束、status=2 视为已取消。</p>
     */
    private void validateLessonBookable(CourseLesson lesson) {
        Integer status = lesson.getStatus();
        if (status != null && status == 1) {
            throw new OperateException("该场次已结束");
        }
        if (status != null && status == 2) {
            throw new OperateException("该场次已取消");
        }
        if (status == null || status != 0) {
            throw new OperateException("该场次不可预约");
        }
        if (StringUtils.isBlank(lesson.getLessonDate()) || lesson.getLessonDate().length() < 10) {
            throw new OperateException("场次日期异常，无法预约");
        }
        if (lesson.getLessonDate().compareTo(LocalDate.now().toString()) < 0) {
            throw new OperateException("该场次已截止报名");
        }
    }

    private boolean isLiveClass(CourseClass courseClass) {
        if (courseClass == null) {
            return false;
        }
        if (courseClass.getIsDelete() != null && courseClass.getIsDelete() == 1) {
            return false;
        }
        if (courseClass.getIsShow() != null && courseClass.getIsShow() == 0) {
            return false;
        }
        if (courseClass.getStatus() == null) {
            return false;
        }
        for (int status : LIVE_CLASS_STATUS) {
            if (status == courseClass.getStatus()) {
                return true;
            }
        }
        return false;
    }

    private boolean isCancelled(CourseBooking booking) {
        return booking.getStatus() != null && booking.getStatus() == BOOKING_CANCELLED;
    }

    // ------------------------------------------------------------------
    // 我的预约 / 下一节课
    // ------------------------------------------------------------------

    @Override
    public List<Map<String, Object>> bookings(Integer userId, Integer studentId, Integer pageNo, Integer pageSize) {
        if (!ownsStudentWhenFiltered(userId, studentId)) {
            return Collections.emptyList();
        }
        QueryWrapper<CourseBooking> query = new QueryWrapper<CourseBooking>()
                .eq("user_id", userId)
                .eq("status", BOOKING_BOOKED)
                .orderByDesc("lesson_date")
                .orderByDesc("id");
        if (studentId != null && studentId > 0) {
            query.eq("student_id", studentId);
        }
        List<Map<String, Object>> items = buildItems(courseBookingMapper.selectList(query), false);
        sortBookings(items);
        return page(items, pageNo, pageSize);
    }

    @Override
    public Map<String, Object> nextBooking(Integer userId) {
        String today = LocalDate.now().toString();
        List<CourseBooking> bookings = courseBookingMapper.selectList(new QueryWrapper<CourseBooking>()
                .eq("user_id", userId)
                .eq("status", BOOKING_BOOKED)
                .ge("lesson_date", today)
                .orderByAsc("lesson_date")
                .orderByAsc("lesson_id"));
        List<Map<String, Object>> items = buildItems(bookings, false);
        items.sort((a, b) -> {
            int byDate = text(a.get("date")).compareTo(text(b.get("date")));
            if (byDate != 0) {
                return byDate;
            }
            int byTime = text(a.get("time")).compareTo(text(b.get("time")));
            if (byTime != 0) {
                return byTime;
            }
            return Long.compare(number(a.get("id")), number(b.get("id")));
        });
        return items.isEmpty() ? null : items.get(0);
    }

    // ------------------------------------------------------------------
    // 上课记录 / 记录详情
    // ------------------------------------------------------------------

    @Override
    public List<Map<String, Object>> records(Integer userId, Integer studentId, Integer pageNo, Integer pageSize) {
        if (!ownsStudentWhenFiltered(userId, studentId)) {
            return Collections.emptyList();
        }
        QueryWrapper<CourseBooking> query = new QueryWrapper<CourseBooking>()
                .eq("user_id", userId)
                .eq("status", BOOKING_ATTENDED)
                .orderByDesc("lesson_date")
                .orderByDesc("id");
        if (studentId != null && studentId > 0) {
            query.eq("student_id", studentId);
        }
        List<Map<String, Object>> items = buildItems(courseBookingMapper.selectList(query), true);
        items.sort((a, b) -> {
            int byDate = text(b.get("date")).compareTo(text(a.get("date")));
            if (byDate != 0) {
                return byDate;
            }
            int byTime = text(b.get("time")).compareTo(text(a.get("time")));
            if (byTime != 0) {
                return byTime;
            }
            return Long.compare(number(b.get("id")), number(a.get("id")));
        });
        return page(items, pageNo, pageSize);
    }

    @Override
    public Map<String, Object> record(Integer userId, String recordId) {
        Integer id = parseInt(recordId);
        if (id == null || id <= 0) {
            return null;
        }
        // 只按当前账号查询：其他账号的记录等同于不存在，不暴露是否存在。
        CourseBooking booking = courseBookingMapper.selectOne(new QueryWrapper<CourseBooking>()
                .eq("id", id)
                .eq("user_id", userId)
                .eq("status", BOOKING_ATTENDED)
                .last("limit 1"));
        if (booking == null) {
            return null;
        }
        List<Map<String, Object>> items = buildItems(Collections.singletonList(booking), true);
        return items.isEmpty() ? null : items.get(0);
    }

    private boolean ownsStudentWhenFiltered(Integer userId, Integer studentId) {
        if (studentId == null || studentId <= 0) {
            return true;
        }
        Long count = studentMapper.selectCount(new QueryWrapper<Student>()
                .eq("id", studentId)
                .eq("user_id", userId));
        return count != null && count > 0L;
    }

    // ------------------------------------------------------------------
    // 组装（批量查询，避免逐条 N+1）
    // ------------------------------------------------------------------

    /**
     * @param records true=上课记录（含照片/点评），false=我的预约
     */
    private List<Map<String, Object>> buildItems(List<CourseBooking> bookings, boolean records) {
        if (bookings == null || bookings.isEmpty()) {
            return new ArrayList<>();
        }
        Set<Integer> studentIds = new LinkedHashSet<>();
        Set<Integer> classIds = new LinkedHashSet<>();
        Set<Integer> lessonIds = new LinkedHashSet<>();
        for (CourseBooking booking : bookings) {
            if (booking.getStudentId() != null && booking.getStudentId() > 0) {
                studentIds.add(booking.getStudentId());
            }
            if (booking.getCourseClassId() != null && booking.getCourseClassId() > 0) {
                classIds.add(booking.getCourseClassId());
            }
            if (booking.getLessonId() != null && booking.getLessonId() > 0) {
                lessonIds.add(booking.getLessonId());
            }
        }

        Map<Integer, Student> studentMap = new HashMap<>();
        if (!studentIds.isEmpty()) {
            for (Student student : studentMapper.selectBatchIds(studentIds)) {
                studentMap.put(student.getId(), student);
            }
        }
        Map<Integer, CourseClass> classMap = new HashMap<>();
        if (!classIds.isEmpty()) {
            for (CourseClass courseClass : courseClassMapper.selectBatchIds(classIds)) {
                classMap.put(courseClass.getId(), courseClass);
            }
        }
        Map<Integer, CourseLesson> lessonById = new HashMap<>();
        if (!lessonIds.isEmpty()) {
            for (CourseLesson lesson : courseLessonMapper.selectBatchIds(lessonIds)) {
                lessonById.put(lesson.getId(), lesson);
            }
        }
        // 老数据（教练空降点名）可能只有 class_id + lesson_date，没有 lesson_id，这里按班级+日期回退。
        Map<String, CourseLesson> lessonByClassDate = new HashMap<>();
        Set<String> fallbackClassDates = new LinkedHashSet<>();
        for (CourseBooking booking : bookings) {
            if (booking.getCourseClassId() == null || booking.getCourseClassId() <= 0
                    || StringUtils.isBlank(booking.getLessonDate())) {
                continue;
            }
            boolean found = booking.getLessonId() != null && booking.getLessonId() > 0
                    && lessonById.containsKey(booking.getLessonId());
            if (!found) {
                fallbackClassDates.add(dateKey(booking.getCourseClassId(), booking.getLessonDate()));
            }
        }
        if (!fallbackClassDates.isEmpty()) {
            Set<Integer> fallbackClassIds = new LinkedHashSet<>();
            Set<String> fallbackDates = new LinkedHashSet<>();
            for (String key : fallbackClassDates) {
                int split = key.indexOf('|');
                fallbackClassIds.add(parseInt(key.substring(0, split)));
                fallbackDates.add(key.substring(split + 1));
            }
            List<CourseLesson> fallbackLessons = courseLessonMapper.selectList(new QueryWrapper<CourseLesson>()
                    .in("class_id", fallbackClassIds)
                    .in("lesson_date", fallbackDates));
            for (CourseLesson lesson : fallbackLessons) {
                String key = dateKey(lesson.getClassId(), lesson.getLessonDate());
                CourseLesson exist = lessonByClassDate.get(key);
                if (exist == null || (exist.getId() != null && lesson.getId() != null && lesson.getId() > exist.getId())) {
                    lessonByClassDate.put(key, lesson);
                }
            }
        }

        Map<Integer, CourseLesson> resolved = new HashMap<>();
        Set<Integer> coachIds = new LinkedHashSet<>();
        Set<Integer> areaIds = new LinkedHashSet<>();
        Set<Integer> logClassIds = new LinkedHashSet<>();
        Set<Integer> lessonNos = new LinkedHashSet<>();
        for (CourseBooking booking : bookings) {
            CourseLesson lesson = resolveLesson(booking, lessonById, lessonByClassDate);
            resolved.put(booking.getId(), lesson);
            CourseClass courseClass = classMap.get(booking.getCourseClassId());
            Integer coachId = lesson != null && lesson.getCoachId() != null && lesson.getCoachId() > 0
                    ? lesson.getCoachId()
                    : (courseClass == null ? null : courseClass.getInstructorId());
            if (coachId != null && coachId > 0) {
                coachIds.add(coachId);
            }
            if (courseClass != null && courseClass.getAreaId() != null && courseClass.getAreaId() > 0) {
                areaIds.add(courseClass.getAreaId());
            }
            if (records) {
                Integer lessonNo = lessonNo(booking, lesson);
                if (lessonNo != null && lessonNo > 0 && booking.getCourseClassId() != null) {
                    logClassIds.add(booking.getCourseClassId());
                    lessonNos.add(lessonNo);
                }
            }
        }

        Map<Integer, Instructor> instructorMap = new HashMap<>();
        if (!coachIds.isEmpty()) {
            for (Instructor instructor : instructorMapper.selectBatchIds(coachIds)) {
                instructorMap.put(instructor.getId(), instructor);
            }
        }
        Map<Integer, Area> areaMap = new HashMap<>();
        if (!areaIds.isEmpty()) {
            for (Area area : areaMapper.selectBatchIds(areaIds)) {
                areaMap.put(area.getId(), area);
            }
        }
        Map<String, CourseLessonLog> lessonLogMap = new HashMap<>();
        if (records && !logClassIds.isEmpty() && !lessonNos.isEmpty()) {
            List<CourseLessonLog> logs = courseLessonLogMapper.selectList(new QueryWrapper<CourseLessonLog>()
                    .in("course_class_id", logClassIds)
                    .in("lesson_no", lessonNos)
                    .isNull("delete_time")
                    .orderByDesc("id"));
            for (CourseLessonLog log : logs) {
                String key = lessonKey(log.getCourseClassId(), log.getLessonNo());
                if (!lessonLogMap.containsKey(key)) {
                    lessonLogMap.put(key, log);
                }
            }
        }

        String today = LocalDate.now().toString();
        List<Map<String, Object>> items = new ArrayList<>();
        for (CourseBooking booking : bookings) {
            CourseLesson lesson = resolved.get(booking.getId());
            CourseClass courseClass = classMap.get(booking.getCourseClassId());
            Student student = studentMap.get(booking.getStudentId());
            Integer lessonNo = lessonNo(booking, lesson);
            Integer coachId = lesson != null && lesson.getCoachId() != null && lesson.getCoachId() > 0
                    ? lesson.getCoachId()
                    : (courseClass == null ? null : courseClass.getInstructorId());
            Instructor instructor = coachId == null ? null : instructorMap.get(coachId);
            Area area = courseClass == null || courseClass.getAreaId() == null ? null : areaMap.get(courseClass.getAreaId());
            String date = lesson != null && !StringUtils.isBlank(lesson.getLessonDate())
                    ? lesson.getLessonDate()
                    : text(booking.getLessonDate());
            String time = ClassScheduleUtil.resolveLessonTimeSlot(lesson, courseClass);
            String courseTitle = courseClass == null ? "" : text(courseClass.getName());

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", String.valueOf(booking.getId()));
            item.put("sessionId", String.valueOf(lesson != null && lesson.getId() != null
                    ? lesson.getId()
                    : (booking.getLessonId() == null ? 0 : booking.getLessonId())));
            item.put("courseId", booking.getCourseClassId() == null ? "" : String.valueOf(booking.getCourseClassId()));
            item.put("studentId", booking.getStudentId() == null ? "" : String.valueOf(booking.getStudentId()));
            item.put("coachId", coachId == null ? "" : String.valueOf(coachId));
            item.put("couponId", "");
            item.put("date", date);
            item.put("time", time);
            item.put("dayLabel", dayLabel(date));
            item.put("shortDate", shortDate(date));
            item.put("weekday", weekday(date));
            item.put("courseTitle", courseTitle);
            item.put("cover", courseClass == null ? "" : UrlUtils.toAbsoluteUrl(courseClass.getImage()));
            item.put("areaName", area == null ? "" : text(area.getName()));
            item.put("studentName", student == null ? "" : text(student.getName()));
            item.put("coachName", instructor == null ? (courseClass == null ? "" : text(courseClass.getInstructorName()))
                    : text(instructor.getName()));
            item.put("coachAvatar", instructor == null ? "" : UrlUtils.toAbsoluteUrl(instructor.getAvatar()));
            item.put("lessonNo", lessonNo == null ? 0 : lessonNo);
            item.put("upcoming", !date.isEmpty() && date.compareTo(today) >= 0);
            item.put("expired", !date.isEmpty() && date.compareTo(today) < 0);

            if (records) {
                item.put("status", "done");
                item.put("statusCode", BOOKING_ATTENDED);
                item.put("attended", true);
                // 沿用 mock 契约（页面写死“扣 1 次”）；券模型接入后改为实际扣减次数。
                item.put("couponTimes", 1);
                CourseLessonLog log = lessonLogMap.get(lessonKey(booking.getCourseClassId(), lessonNo));
                item.put("photos", splitImages(log == null ? "" : log.getImages()));
                item.put("comment", log == null ? "" : text(log.getSummary()));
                item.put("createdAt", formatDateTime(booking.getCreateTime()));
            } else {
                item.put("status", statusText(booking.getStatus()));
                item.put("statusCode", booking.getStatus());
                item.put("attended", false);
                item.put("createdAt", formatDateTime(booking.getCreateTime()));
            }
            items.add(item);
        }
        return items;
    }

    private CourseLesson resolveLesson(CourseBooking booking, Map<Integer, CourseLesson> lessonById,
                                       Map<String, CourseLesson> lessonByClassDate) {
        if (booking.getLessonId() != null && booking.getLessonId() > 0) {
            CourseLesson lesson = lessonById.get(booking.getLessonId());
            if (lesson != null) {
                return lesson;
            }
        }
        if (booking.getCourseClassId() == null || StringUtils.isBlank(booking.getLessonDate())) {
            return null;
        }
        return lessonByClassDate.get(dateKey(booking.getCourseClassId(), booking.getLessonDate()));
    }

    private Integer lessonNo(CourseBooking booking, CourseLesson lesson) {
        if (booking.getLessonNo() != null && booking.getLessonNo() > 0) {
            return booking.getLessonNo();
        }
        return lesson == null ? null : lesson.getLessonNo();
    }

    /** 预约列表排序：今天及以后在前（日期升序），历史在后（日期倒序）。 */
    private void sortBookings(List<Map<String, Object>> items) {
        items.sort((a, b) -> {
            boolean upcomingA = Boolean.TRUE.equals(a.get("upcoming"));
            boolean upcomingB = Boolean.TRUE.equals(b.get("upcoming"));
            if (upcomingA != upcomingB) {
                return upcomingA ? -1 : 1;
            }
            int byDate = text(a.get("date")).compareTo(text(b.get("date")));
            if (byDate != 0) {
                byDate = upcomingA ? byDate : -byDate;
            }
            if (byDate != 0) {
                return byDate;
            }
            int byTime = text(a.get("time")).compareTo(text(b.get("time")));
            if (byTime != 0) {
                return upcomingA ? byTime : -byTime;
            }
            return Long.compare(number(a.get("id")), number(b.get("id")));
        });
    }

    private List<Map<String, Object>> page(List<Map<String, Object>> items, Integer pageNo, Integer pageSize) {
        int page = pageNo == null || pageNo < 1 ? 1 : pageNo;
        int size = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : Math.min(pageSize, MAX_PAGE_SIZE);
        int from = (page - 1) * size;
        if (from >= items.size()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(items.subList(from, Math.min(from + size, items.size())));
    }

    // ------------------------------------------------------------------
    // 工具
    // ------------------------------------------------------------------

    private String statusText(Integer status) {
        int value = status == null ? BOOKING_BOOKED : status;
        if (value == BOOKING_ATTENDED) {
            return "done";
        }
        if (value == BOOKING_CANCELLED) {
            return "cancelled";
        }
        return "booked";
    }

    private List<String> splitImages(String images) {
        List<String> photos = new ArrayList<>();
        if (StringUtils.isBlank(images)) {
            return photos;
        }
        Set<String> seen = new LinkedHashSet<>();
        for (String part : images.split(",")) {
            String url = part == null ? "" : part.trim();
            if (url.isEmpty()) {
                continue;
            }
            String absolute = UrlUtils.toAbsoluteUrl(url);
            if (!absolute.isEmpty() && seen.add(absolute)) {
                photos.add(absolute);
            }
        }
        return photos;
    }

    private String formatDateTime(Long timestamp) {
        if (timestamp == null || timestamp <= 0) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(timestamp * 1000L));
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

    private String dateKey(Integer classId, String lessonDate) {
        return String.valueOf(classId) + "|" + (lessonDate == null ? "" : lessonDate);
    }

    private String lessonKey(Integer classId, Integer lessonNo) {
        return String.valueOf(classId) + "|" + (lessonNo == null ? "" : lessonNo);
    }

    private Integer parseInt(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private long number(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        Integer parsed = parseInt(String.valueOf(value));
        return parsed == null ? 0L : parsed.longValue();
    }

    private String text(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
