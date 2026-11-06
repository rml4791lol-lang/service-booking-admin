/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.RechargeOrder
 *  com.mdd.common.entity.course.CourseAttendance
 *  com.mdd.common.entity.course.CourseBooking
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.CourseConsumeLog
 *  com.mdd.common.entity.course.CourseLesson
 *  com.mdd.common.entity.course.CourseManualLesson
 *  com.mdd.common.entity.course.UserCourse
 *  com.mdd.common.entity.student.Student
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.RechargeOrderMapper
 *  com.mdd.common.mapper.course.CourseAttendanceMapper
 *  com.mdd.common.mapper.course.CourseBookingMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.CourseConsumeLogMapper
 *  com.mdd.common.mapper.course.CourseLessonMapper
 *  com.mdd.common.mapper.course.CourseManualLessonMapper
 *  com.mdd.common.mapper.course.UserCourseMapper
 *  com.mdd.common.mapper.student.StudentMapper
 *  com.mdd.common.util.ClassScheduleUtil
 *  com.mdd.common.util.CourseConsumeLogUtil
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.common.util.UserCourseBalanceUtil
 *  com.mdd.common.util.UserCourseBalanceUtil$Snapshot
 *  com.mdd.common.util.UserCourseEligibilityUtil
 *  com.mdd.front.service.IUserCourseService
 *  com.mdd.front.service.impl.UserCourseServiceImpl
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.course.CourseAttendance;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseConsumeLog;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseManualLesson;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.student.Student;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.course.CourseAttendanceMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseConsumeLogMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.CourseManualLessonMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.util.ClassScheduleUtil;
import com.mdd.common.util.CourseConsumeLogUtil;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.service.CourseConsumeEffectiveService;
import com.mdd.common.util.CourseDerivedBalance;
import com.mdd.common.util.UserCourseBalanceUtil;
import com.mdd.common.util.UserCourseEligibilityUtil;
import com.mdd.front.service.IUserCourseService;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserCourseServiceImpl
implements IUserCourseService {
    @Resource
    UserCourseMapper userCourseMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    CourseBookingMapper courseBookingMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    CourseConsumeLogMapper courseConsumeLogMapper;
    @Resource
    CourseConsumeEffectiveService courseConsumeEffectiveService;
    @Resource
    CourseAttendanceMapper courseAttendanceMapper;
    @Resource
    CourseManualLessonMapper courseManualLessonMapper;
    @Resource
    StudentMapper studentMapper;

    public PageResult<Object> myList(Integer userId, Integer pageNo, Integer pageSize) {
        IPage<UserCourse> page = this.userCourseMapper.selectPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).ne("status", (Object)2).and(qw -> qw.eq("status", (Object)0).or().gt("remain_lessons", (Object)0)).isNull("delete_time").orderByDesc("id"));
        LinkedList lists = new LinkedList();
        for (UserCourse userCourse : page.getRecords()) {
            CourseClass courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)userCourse.getCourseClassId());
            if (courseClass == null) continue;
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", userCourse.getId());
            item.put("courseClassId", userCourse.getCourseClassId());
            item.put("orderId", userCourse.getOrderId());
            item.put("status", userCourse.getStatus());
            item.put("createTime", TimeUtils.timestampToDate((Long)userCourse.getCreateTime()));
            item.put("courseName", courseClass.getName());
            item.put("instructorName", courseClass.getInstructorName());
            item.put("venue", courseClass.getVenue());
            item.put("image", UrlUtils.toAbsoluteUrl((String)courseClass.getImage()));
            item.put("weekDays", courseClass.getWeekDays());
            item.put("timeSlot", courseClass.getTimeSlot());
            item.put("scheduleMode", courseClass.getScheduleMode() != null ? courseClass.getScheduleMode() : 0);
            item.put("scheduleRules", courseClass.getScheduleRules());
            item.put("totalLessons", courseClass.getTotalLessons());
            item.put("studentCount", courseClass.getStudentCount());
            item.put("classType", courseClass.getClassType());
            item.put("courseStatus", courseClass.getStatus());
            item.put("startDate", courseClass.getStartDate());
            item.put("endDate", courseClass.getEndDate());
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            CourseLesson todayLesson = (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>().eq("class_id", (Object)userCourse.getCourseClassId()).eq("lesson_date", (Object)today).last("limit 1"));
            item.put("todayLessonStatus", todayLesson != null ? todayLesson.getStatus() : null);
            item.put("todayLessonTimeSlot", ClassScheduleUtil.resolveLessonTimeSlot((CourseLesson)todayLesson, (CourseClass)courseClass));
            lists.add(item);
        }
        return PageResult.iPageHandle((Long)page.getTotal(), (Long)page.getCurrent(), (Long)page.getSize(), lists);
    }

    public Map<String, Object> checkPurchased(Integer userId, Integer courseClassId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        Map<String, Object> stats = this.courseStats(userId, courseClassId);
        int remainLessons = stats.get("remainLessons") instanceof Number ? ((Number)stats.get("remainLessons")).intValue() : 0;
        boolean hasPurchasedHistory = Boolean.TRUE.equals(stats.get("hasPersonalLessons"));
        result.put("purchased", hasPurchasedHistory && remainLessons > 0);
        result.put("hasPurchasedHistory", hasPurchasedHistory);
        result.put("remainLessons", remainLessons);
        return result;
    }

    public Map<String, Object> stats(Integer userId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        List<UserCourse> enrollments = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).isNull("delete_time"));
        this.applyEffectiveRefundStatuses(enrollments);
        // #222：流水派生（含调整流水增量），不再走 UserCourseBalanceUtil 的库里回填兜底
        Map<Integer, CourseDerivedBalance> derivedMap = this.courseConsumeEffectiveService.deriveBalanceForUser(userId, enrollments);
        long activeClasses = 0L;
        int completedLessons = 0;
        int remainLessons = 0;
        for (UserCourse uc : enrollments) {
            CourseDerivedBalance balance = derivedMap.getOrDefault(uc.getId(),
                    CourseDerivedBalance.derive(uc.getTotalLessons(), 0, 0, uc.getStatus()));
            if (balance.getStatus() == 2) continue;
            if (balance.getStatus() == 0) {
                ++activeClasses;
            }
            completedLessons += balance.getUsedLessons();
            remainLessons += balance.getRemainLessons();
        }
        result.put("activeClasses", activeClasses);
        result.put("completedLessons", completedLessons);
        result.put("remainLessons", remainLessons);
        return result;
    }

    public Map<String, Object> courseStats(Integer userId, Integer courseClassId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        int purchasedLessons = 0;
        int usedLessons = 0;
        int remainLessons = 0;
        if (userId != null && userId > 0 && courseClassId != null && courseClassId > 0) {
            List<UserCourse> enrollments = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).eq("course_class_id", (Object)courseClassId).isNull("delete_time").orderByAsc("id"));
            this.applyEffectiveRefundStatuses(enrollments);
            // #222：流水派生（含调整流水增量）
            Map<Integer, CourseDerivedBalance> derivedMap = this.courseConsumeEffectiveService.deriveBalanceForUser(userId, enrollments);
            for (UserCourse enrollment : enrollments) {
                CourseDerivedBalance balance = derivedMap.getOrDefault(enrollment.getId(),
                        CourseDerivedBalance.derive(enrollment.getTotalLessons(), 0, 0, enrollment.getStatus()));
                if (balance.getStatus() == 2) continue;
                purchasedLessons += balance.getTotalLessons();
                usedLessons += balance.getUsedLessons();
                remainLessons += balance.getRemainLessons();
            }
        }
        result.put("purchasedLessons", purchasedLessons);
        result.put("usedLessons", usedLessons);
        result.put("remainLessons", remainLessons);
        result.put("hasPersonalLessons", purchasedLessons > 0);
        return result;
    }

    private void applyEffectiveRefundStatuses(List<UserCourse> enrollments) {
        List<Integer> orderIds = enrollments.stream().map(UserCourse::getOrderId).filter(orderId -> orderId != null && orderId > 0).distinct().collect(Collectors.toList());
        if (orderIds.isEmpty()) {
            return;
        }
        Map<Integer, RechargeOrder> orderMap = this.rechargeOrderMapper.selectBatchIds(orderIds).stream().collect(Collectors.toMap(RechargeOrder::getId, order -> order));
        for (UserCourse enrollment : enrollments) {
            RechargeOrder order2 = orderMap.get(enrollment.getOrderId());
            if (order2 == null) continue;
            enrollment.setStatus(Integer.valueOf(UserCourseEligibilityUtil.effectiveEnrollmentStatus((Integer)enrollment.getStatus(), (Integer)order2.getRefundStatus())));
        }
    }

    private List<CourseConsumeLog> loadEffectiveConsumeLogs(Integer userId, Set<Integer> classIds) {
        List<CourseConsumeLog> logs;
        QueryWrapper<CourseConsumeLog> logQw = new QueryWrapper<CourseConsumeLog>().eq("user_id", (Object)userId);
        if (classIds != null) {
            if (classIds.isEmpty()) {
                return Collections.emptyList();
            }
            logQw.in("course_class_id", classIds);
        }
        if ((logs = this.courseConsumeLogMapper.selectList((Wrapper)logQw)).isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<CourseConsumeLog> effective = new ArrayList<CourseConsumeLog>();
        for (CourseConsumeLog log : logs) {
            CourseLesson lesson = null;
            String lessonDate = "";
            if (log.getLessonNo() != null && log.getLessonNo() > 0 && (lesson = (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>().eq("class_id", (Object)log.getCourseClassId()).eq("lesson_no", (Object)log.getLessonNo()).last("limit 1"))) != null) {
                lessonDate = lesson.getLessonDate() != null ? lesson.getLessonDate() : "";
            }
            CourseBooking booking = null;
            CourseAttendance attendance = null;
            if (!lessonDate.isEmpty()) {
                QueryWrapper<CourseBooking> bookingQuery = new QueryWrapper<CourseBooking>().eq("course_class_id", (Object)log.getCourseClassId()).eq("user_id", (Object)userId).eq("lesson_date", (Object)lessonDate);
                if (log.getStudentId() != null && log.getStudentId() > 0) {
                    bookingQuery.eq("student_id", (Object)log.getStudentId());
                }
                booking = (CourseBooking)this.courseBookingMapper.selectOne((Wrapper)bookingQuery.last("limit 1"));
                attendance = (CourseAttendance)this.courseAttendanceMapper.selectOne(new QueryWrapper<CourseAttendance>().eq("class_id", (Object)log.getCourseClassId()).eq("user_id", (Object)userId).eq("lesson_date", (Object)lessonDate).last("limit 1"));
            }
            if (!CourseConsumeLogUtil.isEffectiveLessonRecord((CourseConsumeLog)log, lesson, booking, attendance)) continue;
            effective.add(log);
        }
        return effective;
    }

    private Set<String> loadNonRefundedClassStudentKeys(Integer userId) {
        List<UserCourse> enrollments = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).and(w -> w.isNull("status").or().ne("status", (Object)2)).isNull("delete_time"));
        HashSet<String> keys = new HashSet<String>();
        for (UserCourse uc : enrollments) {
            if (uc.getCourseClassId() == null || uc.getCourseClassId() <= 0) continue;
            keys.add(CourseConsumeLogUtil.classStudentKey((Integer)uc.getCourseClassId(), (Integer)uc.getStudentId()));
        }
        return keys;
    }

    private CourseLesson resolveBookingLesson(CourseBooking booking) {
        if (booking == null) {
            return null;
        }
        if (booking.getLessonId() != null && booking.getLessonId() > 0) {
            return (CourseLesson)this.courseLessonMapper.selectById((Serializable)booking.getLessonId());
        }
        if (booking.getCourseClassId() != null && booking.getLessonDate() != null) {
            return (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>().eq("class_id", (Object)booking.getCourseClassId()).eq("lesson_date", (Object)booking.getLessonDate()).last("limit 1"));
        }
        return null;
    }

    private boolean isLessonEnded(String lessonDate, String timeSlot) {
        if (lessonDate == null || timeSlot == null || !timeSlot.contains("-")) {
            return false;
        }
        try {
            String endTimeStr = timeSlot.split("-")[1].trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date endDateTime = sdf.parse(lessonDate + " " + endTimeStr);
            return endDateTime != null && new Date().after(endDateTime);
        }
        catch (Exception e) {
            return false;
        }
    }

    public List<Map<String, Object>> completedLessonList(Integer userId, String keyword, Integer pageNo, Integer pageSize) {
        Set<String> countableKeys = this.loadNonRefundedClassStudentKeys(userId);
        if (countableKeys.isEmpty()) {
            return new LinkedList<Map<String, Object>>();
        }
        List<CourseConsumeLog> allEffective = this.loadEffectiveConsumeLogs(userId, null);
        ArrayList<CourseConsumeLog> effectiveLogs = new ArrayList<CourseConsumeLog>();
        for (CourseConsumeLog log : allEffective) {
            String key = CourseConsumeLogUtil.classStudentKey((Integer)log.getCourseClassId(), (Integer)log.getStudentId());
            if (countableKeys.contains(key)) {
                effectiveLogs.add(log);
                continue;
            }
            if (CourseConsumeLogUtil.normalizeStudentId((Integer)log.getStudentId()) != 0 || log.getCourseClassId() == null || !countableKeys.stream().anyMatch(k -> k.startsWith(log.getCourseClassId() + ":"))) continue;
            effectiveLogs.add(log);
        }
        if (effectiveLogs.isEmpty()) {
            return new LinkedList<Map<String, Object>>();
        }
        effectiveLogs.sort((a, b) -> {
            long ta = a.getCreateTime() != null ? a.getCreateTime() : 0L;
            long tb = b.getCreateTime() != null ? b.getCreateTime() : 0L;
            return Long.compare(tb, ta);
        });
        List<Integer> classIds = effectiveLogs.stream().map(CourseConsumeLog::getCourseClassId).filter(id -> id != null && id > 0).distinct().collect(Collectors.toList());
        Map<Integer, CourseClass> classMap = this.getCourseClassMap(classIds);
        LinkedList allItems = new LinkedList();
        for (CourseConsumeLog log : effectiveLogs) {
            CourseManualLesson manual;
            CourseClass courseClass = (CourseClass)classMap.get(log.getCourseClassId());
            if (courseClass == null) continue;
            String lessonDate = "";
            String timeSlot = "";
            if (log.getLessonNo() != null && log.getLessonNo() > 0) {
                CourseLesson lesson = (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>().eq("class_id", (Object)log.getCourseClassId()).eq("lesson_no", (Object)log.getLessonNo()).last("limit 1"));
                if (lesson != null) {
                    lessonDate = lesson.getLessonDate() != null ? lesson.getLessonDate() : "";
                    timeSlot = lesson.getTimeSlot() != null ? lesson.getTimeSlot() : "";
                }
            } else if (log.getSourceType() != null && log.getSourceType() == 1 && log.getManualId() != null && (manual = (CourseManualLesson)this.courseManualLessonMapper.selectById((Serializable)log.getManualId())) != null) {
                lessonDate = manual.getLessonDate() != null ? manual.getLessonDate() : "";
                String string = timeSlot = manual.getTimeSlot() != null ? manual.getTimeSlot() : "";
            }
            if (timeSlot.isEmpty()) {
                String string = timeSlot = courseClass.getTimeSlot() != null ? courseClass.getTimeSlot() : "";
            }
            if (keyword != null && !keyword.isEmpty()) {
                boolean match;
                String lowerKey = keyword.toLowerCase();
                boolean bl = match = courseClass.getName() != null && courseClass.getName().toLowerCase().contains(lowerKey) || courseClass.getInstructorName() != null && courseClass.getInstructorName().toLowerCase().contains(lowerKey) || courseClass.getVenue() != null && courseClass.getVenue().toLowerCase().contains(lowerKey);
                if (!match) continue;
            }
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("bookingId", log.getId());
            item.put("lessonId", log.getLessonNo());
            item.put("classId", log.getCourseClassId());
            item.put("className", courseClass.getName());
            item.put("image", UrlUtils.toAbsoluteUrl((String)courseClass.getImage()));
            item.put("instructorName", courseClass.getInstructorName());
            item.put("venue", courseClass.getVenue());
            item.put("lessonNo", log.getLessonNo() != null && log.getLessonNo() > 0 ? log.getLessonNo() : 0);
            item.put("lessonDate", lessonDate);
            item.put("timeSlot", timeSlot);
            item.put("status", 10);
            item.put("sourceType", log.getSourceType() != null ? log.getSourceType() : 0);
            allItems.add(item);
        }
        int from = (pageNo - 1) * pageSize;
        if (from >= allItems.size()) {
            return new LinkedList<Map<String, Object>>();
        }
        int to = Math.min(from + pageSize, allItems.size());
        return allItems.subList(from, to);
    }

    public List<Map<String, Object>> todayLessons(Integer userId) {
        List<UserCourse> userCourses = this.getActiveUserCourses(userId);
        if (userCourses.isEmpty()) {
            return new LinkedList<Map<String, Object>>();
        }
        List<Integer> classIds = userCourses.stream().map(UserCourse::getCourseClassId).distinct().collect(Collectors.toList());
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        List<CourseLesson> lessons = this.courseLessonMapper.selectList(new QueryWrapper<CourseLesson>().in("class_id", classIds).eq("lesson_date", (Object)today).eq("status", (Object)0).orderByAsc("time_slot").orderByAsc("id"));
        return this.buildLessonList(userId, classIds, lessons, 0);
    }

    public List<Map<String, Object>> upcomingLessons(Integer userId) {
        List<UserCourse> userCourses = this.getActiveUserCourses(userId);
        if (userCourses.isEmpty()) {
            return new LinkedList<Map<String, Object>>();
        }
        List<Integer> classIds = userCourses.stream().map(UserCourse::getCourseClassId).distinct().collect(Collectors.toList());
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        List<CourseLesson> lessons = this.courseLessonMapper.selectList(new QueryWrapper<CourseLesson>().in("class_id", classIds).ge("lesson_date", (Object)today).eq("status", (Object)0).orderByAsc("lesson_date").orderByAsc("time_slot").orderByAsc("id"));
        return this.buildLessonList(userId, classIds, lessons, 3);
    }

    @Transactional
    public void bookLesson(Integer userId, Integer lessonId, Integer studentId) {
        int remain;
        CourseLesson lesson = (CourseLesson)this.courseLessonMapper.selectById((Serializable)lessonId);
        if (lesson == null) {
            throw new OperateException("\u8bfe\u8282\u4e0d\u5b58\u5728");
        }
        if (lesson.getStatus() == null || lesson.getStatus() != 0) {
            throw new OperateException("\u5f53\u524d\u8bfe\u65f6\u4e0d\u53ef\u9884\u7ea6");
        }
        // 旧预约接口（/api/user_course/booking）与 #25 新接口共用名额口径：
        // 余位 = max_students - 未取消预约数；超过名额必须拒绝，否则可绕过上限超卖。
        CourseClass courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)lesson.getClassId());
        if (courseClass == null
                || (courseClass.getIsDelete() != null && courseClass.getIsDelete() == 1)
                || (courseClass.getIsShow() != null && courseClass.getIsShow() == 0)) {
            throw new OperateException("\u8be5\u8bfe\u7a0b\u4e0d\u5b58\u5728\u6216\u5df2\u4e0b\u67b6");
        }
        String lessonDate = lesson.getLessonDate();
        if (lessonDate == null || lessonDate.length() < 10
                || lessonDate.compareTo(java.time.LocalDate.now().toString()) < 0) {
            throw new OperateException("\u8be5\u573a\u6b21\u5df2\u622a\u6b62\u62a5\u540d");
        }
        int capacity = courseClass.getMaxStudents() == null ? 0 : courseClass.getMaxStudents();
        Long booked = this.courseBookingMapper.selectCount(new QueryWrapper<CourseBooking>()
                .eq("lesson_id", (Object)lessonId).ne("status", (Object)3));
        if (capacity <= 0 || (booked != null && booked >= capacity)) {
            throw new OperateException("\u8be5\u573a\u6b21\u5df2\u7ea6\u6ee1");
        }
        UserCourse userCourse = this.resolveUserCourseForStudent(userId, lesson.getClassId(), studentId, true);
        if (userCourse == null) {
            throw new OperateException("\u60a8\u672a\u62a5\u540d\u8be5\u8bfe\u7a0b\uff0c\u65e0\u6cd5\u9884\u7ea6");
        }
        Integer targetStudentId = this.studentKey(userCourse.getStudentId());
        int n = remain = userCourse.getRemainLessons() != null ? userCourse.getRemainLessons() : 0;
        if (remain <= 0) {
            throw new OperateException("\u5269\u4f59\u8bfe\u65f6\u4e0d\u8db3\uff0c\u8bf7\u5148\u8d2d\u4e70\u8bfe\u65f6");
        }
        CourseBooking existingBooking = (CourseBooking)this.courseBookingMapper.selectOne(new QueryWrapper<CourseBooking>().eq("user_id", (Object)userId).eq("lesson_id", (Object)lessonId).eq("student_id", (Object)targetStudentId).last("limit 1"));
        if (existingBooking != null && existingBooking.getStatus() != null && existingBooking.getStatus() != 3) {
            throw new OperateException("\u8be5\u5b66\u5458\u5df2\u9884\u7ea6\u8be5\u8bfe\u8282\uff0c\u65e0\u9700\u91cd\u590d\u9884\u7ea6");
        }
        if (existingBooking != null) {
            existingBooking.setStudentId(targetStudentId);
            existingBooking.setCourseClassId(lesson.getClassId());
            existingBooking.setLessonNo(lesson.getLessonNo());
            existingBooking.setLessonDate(lesson.getLessonDate());
            existingBooking.setStatus(Integer.valueOf(1));
            existingBooking.setUpdateTime(Long.valueOf(this.currentTimestamp()));
            this.courseBookingMapper.updateById(existingBooking);
        } else {
            CourseBooking booking = new CourseBooking();
            booking.setUserId(userId);
            booking.setStudentId(targetStudentId);
            booking.setCourseClassId(lesson.getClassId());
            booking.setLessonId(lessonId);
            booking.setLessonNo(lesson.getLessonNo());
            booking.setLessonDate(lesson.getLessonDate());
            booking.setStatus(Integer.valueOf(1));
            booking.setCreateTime(Long.valueOf(this.currentTimestamp()));
            booking.setUpdateTime(Long.valueOf(this.currentTimestamp()));
            this.courseBookingMapper.insert(booking);
        }
    }

    @Transactional
    public void cancelBooking(Integer userId, Integer lessonId, Integer studentId) {
        RechargeOrder rechargeOrder;
        CourseConsumeLog consumeLog;
        CourseLesson lesson = (CourseLesson)this.courseLessonMapper.selectById((Serializable)lessonId);
        if (lesson == null) {
            throw new OperateException("\u8bfe\u8282\u4e0d\u5b58\u5728");
        }
        CourseBooking booking = this.resolveBookingForStudent(userId, lessonId, studentId);
        if (booking == null) {
            throw new OperateException("\u672a\u627e\u5230\u53ef\u53d6\u6d88\u7684\u9884\u7ea6\u8bb0\u5f55");
        }
        QueryWrapper<CourseBooking> bookingQuery = new QueryWrapper<CourseBooking>().eq("user_id", (Object)userId).eq("lesson_id", (Object)lessonId).ne("status", (Object)3);
        if (booking.getStudentId() != null && booking.getStudentId() > 0) {
            bookingQuery.eq("student_id", (Object)booking.getStudentId());
        } else {
            bookingQuery.eq("student_id", (Object)0);
        }
        booking = (CourseBooking)this.courseBookingMapper.selectOne((Wrapper)bookingQuery.last("limit 1"));
        if (booking == null) {
            throw new OperateException("\u672a\u627e\u5230\u53ef\u53d6\u6d88\u7684\u9884\u7ea6\u8bb0\u5f55");
        }
        if (!this.canCancelBooking(lesson)) {
            throw new OperateException("\u5f00\u8bfe\u524d1\u5c0f\u65f6\u5185\u4e0d\u53ef\u53d6\u6d88\u9884\u7ea6");
        }
        Long now = this.currentTimestamp();
        booking.setStatus(Integer.valueOf(3));
        booking.setUpdateTime(now);
        this.courseBookingMapper.updateById(booking);
        QueryWrapper<CourseConsumeLog> consumeLogQuery = new QueryWrapper<CourseConsumeLog>().eq("user_id", (Object)userId).eq("course_class_id", (Object)lesson.getClassId()).eq("lesson_no", (Object)lesson.getLessonNo());
        if (booking.getStudentId() != null && booking.getStudentId() > 0) {
            consumeLogQuery.eq("student_id", (Object)booking.getStudentId());
        }
        if ((consumeLog = (CourseConsumeLog)this.courseConsumeLogMapper.selectOne((Wrapper)consumeLogQuery.last("limit 1"))) == null) {
            return;
        }
        this.courseConsumeLogMapper.deleteById((Serializable)consumeLog.getId());
        UserCourse userCourse = this.resolveUserCourseForStudent(userId, lesson.getClassId(), booking.getStudentId(), false);
        if (userCourse != null) {
            int usedLessons = userCourse.getUsedLessons() != null ? userCourse.getUsedLessons() : 0;
            int remainLessons = userCourse.getRemainLessons() != null ? userCourse.getRemainLessons() : 0;
            userCourse.setUsedLessons(Integer.valueOf(Math.max(usedLessons - 1, 0)));
            userCourse.setRemainLessons(Integer.valueOf(remainLessons + 1));
            userCourse.setStatus(Integer.valueOf(UserCourseBalanceUtil.statusAfterLessonReturned((Integer)userCourse.getStatus(), (Integer)userCourse.getRemainLessons())));
            userCourse.setUpdateTime(now);
            this.userCourseMapper.updateById(userCourse);
        }
        if ((rechargeOrder = this.getRechargeOrderForUserCourse(userId, lesson.getClassId(), userCourse)) != null) {
            rechargeOrder.setLessonCount(Integer.valueOf((rechargeOrder.getLessonCount() != null ? rechargeOrder.getLessonCount() : 0) + 1));
            rechargeOrder.setUpdateTime(now);
            this.rechargeOrderMapper.updateById(rechargeOrder);
        }
    }

    private List<UserCourse> getActiveUserCourses(Integer userId) {
        return this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).ne("status", (Object)2).and(qw -> qw.eq("status", (Object)0).or().gt("remain_lessons", (Object)0)).isNull("delete_time").notInSql("order_id", "select id from la_recharge_order where refund_status = 1").orderByAsc("id"));
    }

    private List<UserCourse> getActiveUserCourses(Integer userId, Integer courseClassId) {
        List<UserCourse> userCourses = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).eq("course_class_id", (Object)courseClassId).ne("status", (Object)2).and(qw -> qw.eq("status", (Object)0).or().gt("remain_lessons", (Object)0)).isNull("delete_time").notInSql("order_id", "select id from la_recharge_order where refund_status = 1").orderByAsc("id"));
        this.fillStudentIdFromOrder(userCourses);
        return userCourses;
    }

    private UserCourse getActiveUserCourse(Integer userId, Integer courseClassId) {
        List<UserCourse> userCourses = this.getActiveUserCourses(userId, courseClassId);
        return userCourses.isEmpty() ? null : (UserCourse)userCourses.get(0);
    }

    private UserCourse resolveUserCourseForStudent(Integer userId, Integer courseClassId, Integer studentId, boolean requireChoice) {
        List<UserCourse> userCourses = this.getActiveUserCourses(userId, courseClassId);
        if (userCourses.isEmpty()) {
            return null;
        }
        if (studentId != null && studentId > 0) {
            for (UserCourse userCourse : userCourses) {
                if (!studentId.equals(userCourse.getStudentId())) continue;
                return userCourse;
            }
            throw new OperateException("\u6240\u9009\u5b66\u5458\u672a\u62a5\u540d\u8be5\u8bfe\u7a0b\uff0c\u65e0\u6cd5\u9884\u7ea6");
        }
        HashSet<Integer> studentIds = new HashSet<Integer>();
        for (UserCourse userCourse : userCourses) {
            if (userCourse.getStudentId() == null || userCourse.getStudentId() <= 0) continue;
            studentIds.add(userCourse.getStudentId());
        }
        if (requireChoice && studentIds.size() > 1) {
            throw new OperateException("\u8bf7\u9009\u62e9\u9700\u8981\u9884\u7ea6\u7684\u5b66\u5458");
        }
        return (UserCourse)userCourses.get(0);
    }

    private CourseBooking resolveBookingForStudent(Integer userId, Integer lessonId, Integer studentId) {
        QueryWrapper<CourseBooking> queryWrapper = new QueryWrapper<CourseBooking>().eq("user_id", (Object)userId).eq("lesson_id", (Object)lessonId).ne("status", (Object)3).orderByDesc("id");
        if (studentId != null && studentId > 0) {
            queryWrapper.eq("student_id", (Object)studentId);
            queryWrapper.last("limit 1");
            return (CourseBooking)this.courseBookingMapper.selectOne((Wrapper)queryWrapper);
        }
        List<CourseBooking> bookings = this.courseBookingMapper.selectList((Wrapper)queryWrapper);
        if (bookings.size() > 1) {
            throw new OperateException("\u8bf7\u9009\u62e9\u9700\u8981\u53d6\u6d88\u9884\u7ea6\u7684\u5b66\u5458");
        }
        return bookings.isEmpty() ? null : (CourseBooking)bookings.get(0);
    }

    private RechargeOrder getRechargeOrderForUserCourse(Integer userId, Integer courseClassId, UserCourse userCourse) {
        if (userCourse != null && userCourse.getOrderId() != null && userCourse.getOrderId() > 0) {
            RechargeOrder rechargeOrder = (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)userCourse.getOrderId());
            if (rechargeOrder != null && UserCourseEligibilityUtil.isActiveOrder((Integer)rechargeOrder.getPayStatus(), (Integer)rechargeOrder.getRefundStatus(), (Long)rechargeOrder.getDeleteTime())) {
                return rechargeOrder;
            }
            return null;
        }
        QueryWrapper<RechargeOrder> orderQuery = new QueryWrapper<RechargeOrder>().eq("user_id", (Object)userId).eq("course_class_id", (Object)courseClassId).eq("pay_status", (Object)1).eq("refund_status", (Object)0).isNull("delete_time").orderByAsc("id");
        if (userCourse != null && userCourse.getStudentId() != null && userCourse.getStudentId() > 0) {
            orderQuery.eq("student_id", (Object)userCourse.getStudentId());
        }
        return (RechargeOrder)this.rechargeOrderMapper.selectOne((Wrapper)orderQuery.last("limit 1"));
    }

    private Map<Integer, CourseClass> getCourseClassMap(List<Integer> classIds) {
        LinkedHashMap<Integer, CourseClass> classMap = new LinkedHashMap<Integer, CourseClass>();
        for (Integer classId : classIds) {
            CourseClass courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)classId);
            if (courseClass == null) continue;
            classMap.put(classId, courseClass);
        }
        return classMap;
    }

    private Map<Integer, List<CourseBooking>> getBookingMap(Integer userId, List<Integer> lessonIds) {
        LinkedHashMap<Integer, List<CourseBooking>> bookingMap = new LinkedHashMap<Integer, List<CourseBooking>>();
        if (lessonIds == null || lessonIds.isEmpty()) {
            return bookingMap;
        }
        List<CourseBooking> bookings = this.courseBookingMapper.selectList(new QueryWrapper<CourseBooking>().eq("user_id", (Object)userId).in("lesson_id", lessonIds).ne("status", (Object)3).orderByDesc("id"));
        for (CourseBooking booking : bookings) {
            bookingMap.computeIfAbsent(booking.getLessonId(), key -> new LinkedList()).add(booking);
        }
        return bookingMap;
    }

    private Map<Integer, List<UserCourse>> getUserCourseMap(Integer userId, List<Integer> classIds) {
        LinkedHashMap<Integer, List<UserCourse>> userCourseMap = new LinkedHashMap<Integer, List<UserCourse>>();
        if (classIds == null || classIds.isEmpty()) {
            return userCourseMap;
        }
        List<UserCourse> userCourses = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).in("course_class_id", classIds).ne("status", (Object)2).and(qw -> qw.eq("status", (Object)0).or().gt("remain_lessons", (Object)0)).isNull("delete_time").notInSql("order_id", "select id from la_recharge_order where refund_status = 1").orderByAsc("id"));
        this.fillStudentIdFromOrder(userCourses);
        LinkedHashMap<String, UserCourse> dedupeMap = new LinkedHashMap<String, UserCourse>();
        for (UserCourse userCourse : userCourses) {
            String key2 = userCourse.getCourseClassId() + ":" + this.studentKey(userCourse.getStudentId());
            dedupeMap.putIfAbsent(key2, userCourse);
        }
        for (UserCourse userCourse : dedupeMap.values()) {
            userCourseMap.computeIfAbsent(userCourse.getCourseClassId(), key -> new LinkedList()).add(userCourse);
        }
        return userCourseMap;
    }

    private void fillStudentIdFromOrder(List<UserCourse> userCourses) {
        if (userCourses == null || userCourses.isEmpty()) {
            return;
        }
        for (UserCourse userCourse : userCourses) {
            if (userCourse.getStudentId() != null && userCourse.getStudentId() > 0) continue;
            if (userCourse.getOrderId() == null || userCourse.getOrderId() <= 0) {
                userCourse.setStudentId(Integer.valueOf(0));
                continue;
            }
            RechargeOrder order = (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)userCourse.getOrderId());
            if (order != null && order.getStudentId() != null && order.getStudentId() > 0) {
                userCourse.setStudentId(order.getStudentId());
                continue;
            }
            userCourse.setStudentId(Integer.valueOf(0));
        }
    }

    private List<Map<String, Object>> buildLessonList(Integer userId, List<Integer> classIds, List<CourseLesson> lessons, int limit) {
        if (lessons == null || lessons.isEmpty()) {
            return new LinkedList<Map<String, Object>>();
        }
        Map<Integer, CourseClass> classMap = this.getCourseClassMap(classIds);
        Map<Integer, List<UserCourse>> userCourseMap = this.getUserCourseMap(userId, classIds);
        Map<Integer, List<CourseBooking>> bookingMap = this.getBookingMap(userId, lessons.stream().map(CourseLesson::getId).collect(Collectors.toList()));
        LinkedList<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
        for (CourseLesson lesson : lessons) {
            CourseClass courseClass = (CourseClass)classMap.get(lesson.getClassId());
            if (courseClass == null) continue;
            result.add(this.buildLessonItem(lesson, courseClass, (List)userCourseMap.get(lesson.getClassId()), (List)bookingMap.get(lesson.getId())));
            if (limit <= 0 || result.size() < limit) continue;
            break;
        }
        return result;
    }

    private Map<String, Object> buildLessonItem(CourseLesson lesson, CourseClass courseClass, List<UserCourse> userCourses, List<CourseBooking> bookings) {
        List<Map<String, Object>> students = this.buildLessonStudentOptions(userCourses, bookings, lesson);
        CourseBooking booking = bookings != null && !bookings.isEmpty() ? bookings.get(0) : null;
        boolean allBooked = !students.isEmpty() && students.stream().allMatch(student -> Boolean.TRUE.equals(student.get("booked")));
        LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
        item.put("lessonId", lesson.getId());
        item.put("classId", lesson.getClassId());
        item.put("className", courseClass.getName());
        item.put("lessonNo", lesson.getLessonNo());
        item.put("lessonType", lesson.getLessonType() != null ? lesson.getLessonType() : 0);
        item.put("lessonTypeLabel", ClassScheduleUtil.isTempLesson((CourseLesson)lesson) ? "\u4e34\u65f6\u52a0\u8bfe" : "\u6b63\u8bfe");
        item.put("lessonDate", lesson.getLessonDate());
        item.put("timeSlot", ClassScheduleUtil.resolveLessonTimeSlot((CourseLesson)lesson, (CourseClass)courseClass));
        item.put("instructorName", courseClass.getInstructorName());
        item.put("venue", ClassScheduleUtil.resolveLessonVenue((CourseLesson)lesson, (CourseClass)courseClass));
        item.put("booked", allBooked);
        item.put("bookingId", booking != null ? booking.getId() : null);
        item.put("canCancel", booking != null && this.canCancelBooking(lesson));
        item.put("students", students);
        return item;
    }

    private List<Map<String, Object>> buildLessonStudentOptions(List<UserCourse> userCourses, List<CourseBooking> bookings, CourseLesson lesson) {
        LinkedList<Map<String, Object>> students = new LinkedList<Map<String, Object>>();
        if (userCourses == null || userCourses.isEmpty()) {
            return students;
        }
        HashMap<Integer, CourseBooking> bookingMap = new HashMap<Integer, CourseBooking>();
        if (bookings != null) {
            for (CourseBooking booking : bookings) {
                bookingMap.putIfAbsent(this.studentKey(booking.getStudentId()), booking);
            }
        }
        for (UserCourse userCourse : userCourses) {
            Integer key = this.studentKey(userCourse.getStudentId());
            CourseBooking booking = (CourseBooking)bookingMap.get(key);
            Student student = userCourse.getStudentId() != null && userCourse.getStudentId() > 0 ? (Student)this.studentMapper.selectById((Serializable)userCourse.getStudentId()) : null;
            int remainLessons = userCourse.getRemainLessons() != null ? userCourse.getRemainLessons() : 0;
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("studentId", key);
            item.put("studentName", student != null && student.getName() != null && !student.getName().isEmpty() ? student.getName() : "\u5b66\u5458");
            item.put("studentAvatar", UrlUtils.toAbsoluteUrl((String)(student != null ? student.getAvatar() : "")));
            item.put("remainLessons", remainLessons);
            item.put("booked", booking != null);
            item.put("bookingId", booking != null ? booking.getId() : null);
            item.put("canBook", booking == null && remainLessons > 0);
            item.put("canCancel", booking != null && this.canCancelBooking(lesson));
            students.add(item);
        }
        return students;
    }

    private Integer studentKey(Integer studentId) {
        return studentId != null && studentId > 0 ? studentId : 0;
    }

    private boolean canCancelBooking(CourseLesson lesson) {
        Long lessonStartTimestamp = this.getLessonStartTimestamp(lesson);
        return lessonStartTimestamp != null && this.currentTimestamp() < lessonStartTimestamp - 3600L;
    }

    private Long getLessonStartTimestamp(CourseLesson lesson) {
        if (lesson == null || lesson.getLessonDate() == null) {
            return null;
        }
        CourseClass courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)lesson.getClassId());
        String timeSlot = ClassScheduleUtil.resolveLessonTimeSlot((CourseLesson)lesson, (CourseClass)courseClass);
        if (timeSlot.isEmpty() || !timeSlot.contains("-")) {
            return null;
        }
        try {
            String startTime = timeSlot.split("-")[0].trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date startDateTime = sdf.parse(lesson.getLessonDate() + " " + startTime);
            return startDateTime != null ? Long.valueOf(startDateTime.getTime() / 1000L) : null;
        }
        catch (Exception e) {
            return null;
        }
    }

    private long currentTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }
}

