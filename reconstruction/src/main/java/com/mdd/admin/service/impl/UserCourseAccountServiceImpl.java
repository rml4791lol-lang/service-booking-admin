/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.admin.service.IUserCourseAccountService
 *  com.mdd.admin.service.impl.UserCourseAccountServiceImpl
 *  com.mdd.common.entity.RechargeOrder
 *  com.mdd.common.entity.course.CourseAttendance
 *  com.mdd.common.entity.course.CourseBooking
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.CourseConsumeLog
 *  com.mdd.common.entity.course.CourseLesson
 *  com.mdd.common.entity.course.CourseManualLesson
 *  com.mdd.common.entity.course.UserCourse
 *  com.mdd.common.entity.student.Student
 *  com.mdd.common.entity.user.User
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
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.util.CourseConsumeLogUtil
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.common.util.UserCourseBalanceUtil
 *  com.mdd.common.util.UserCourseBalanceUtil$Snapshot
 *  com.mdd.common.util.UserCourseEligibilityUtil
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.service.IUserCourseAccountService;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.course.CourseAttendance;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseConsumeLog;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseManualLesson;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.student.Student;
import com.mdd.common.entity.user.User;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.course.CourseAttendanceMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseConsumeLogMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.CourseManualLessonMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.service.CourseConsumeEffectiveService;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.CourseConsumeLogUtil;
import com.mdd.common.util.CourseDerivedBalance;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.UserCourseEligibilityUtil;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserCourseAccountServiceImpl
implements IUserCourseAccountService {
    @Resource
    UserMapper userMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    UserCourseMapper userCourseMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CourseConsumeLogMapper courseConsumeLogMapper;
    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    CourseManualLessonMapper courseManualLessonMapper;
    @Resource
    CourseBookingMapper courseBookingMapper;
    @Resource
    CourseAttendanceMapper courseAttendanceMapper;
    @Resource
    CourseConsumeEffectiveService courseConsumeEffectiveService;

    public Map<String, Object> getAccount(Integer userId) {
        User user = (User)this.userMapper.selectById((Serializable)userId);
        if (user == null || user.getDeleteTime() != null) {
            throw new OperateException("\u7528\u6237\u4e0d\u5b58\u5728");
        }
        List<UserCourse> enrollments = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).isNull("delete_time").orderByDesc("id"));
        this.applyEffectiveRefundStatuses(enrollments);
        Map<Integer, CourseClass> classMap = this.loadClassMap(enrollments);
        // #222：展示值以消课流水派生（含调整流水增量），不再走 UserCourseBalanceUtil 的库里回填兜底
        Map<Integer, CourseDerivedBalance> derivedMap = this.courseConsumeEffectiveService.deriveBalanceForUser(userId, enrollments);
        int sumTotal = 0;
        int sumUsed = 0;
        int sumRemain = 0;
        ArrayList courses = new ArrayList();
        for (UserCourse uc : enrollments) {
            Student student;
            CourseClass cls = (CourseClass)classMap.get(uc.getCourseClassId());
            if (cls == null || cls.getIsDelete() != null && cls.getIsDelete() == 1) continue;
            CourseDerivedBalance balance = derivedMap.get(uc.getId());
            if (balance == null) {
                balance = CourseDerivedBalance.derive(uc.getTotalLessons(), 0, 0, uc.getStatus());
            }
            int total = balance.getTotalLessons();
            int used = balance.getUsedLessons();
            int remain = balance.getRemainLessons();
            if (balance.getStatus() != 2) {
                sumTotal += total;
                sumUsed += used;
                sumRemain += remain;
            }
            // 派生值与账面字段不一致要标注（老链路未扣剩余/直接改库的历史漂移）
            int fieldUsed = nonNegativeLessons(uc.getUsedLessons());
            int fieldRemain = Math.min(nonNegativeLessons(uc.getRemainLessons()), total);
            boolean fieldMismatch = balance.getStatus() != 2 && (used != fieldUsed || remain != fieldRemain);
            LinkedHashMap<String, Object> courseItem = new LinkedHashMap<String, Object>();
            courseItem.put("userCourseId", uc.getId());
            courseItem.put("courseClassId", uc.getCourseClassId());
            courseItem.put("className", cls.getName());
            courseItem.put("instructorName", cls.getInstructorName());
            courseItem.put("totalLessons", total);
            courseItem.put("usedLessons", used);
            courseItem.put("remainLessons", remain);
            courseItem.put("fieldUsedLessons", fieldUsed);
            courseItem.put("fieldRemainLessons", fieldRemain);
            courseItem.put("fieldMismatch", fieldMismatch);
            courseItem.put("status", balance.getStatus());
            courseItem.put("statusText", this.resolveUserCourseStatusText(Integer.valueOf(balance.getStatus())));
            courseItem.put("enrollTime", uc.getCreateTime() != null ? TimeUtils.timestampToDate((Long)uc.getCreateTime()) : "");
            if (uc.getStudentId() != null && uc.getStudentId() > 0 && (student = (Student)this.studentMapper.selectOne(new QueryWrapper<Student>().eq("id", (Object)uc.getStudentId()).isNull("delete_time").last("limit 1"))) != null && StringUtils.isNotEmpty((String)student.getName())) {
                courseItem.put("studentName", student.getName());
            }
            courses.add(courseItem);
        }
        LinkedHashMap<String, Integer> summary = new LinkedHashMap<String, Integer>();
        summary.put("purchasedLessons", sumTotal);
        summary.put("usedLessons", sumUsed);
        summary.put("remainLessons", sumRemain);
        LinkedHashMap<String, Object> student = new LinkedHashMap<String, Object>();
        student.put("userId", userId);
        student.put("name", this.resolveDisplayName(userId, enrollments, user));
        student.put("phone", user.getMobile() != null ? user.getMobile() : "");
        student.put("nickname", user.getNickname());
        student.put("avatar", UrlUtils.toAdminAbsoluteUrl((String)user.getAvatar()));
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("student", student);
        result.put("summary", summary);
        result.put("courses", courses);
        return result;
    }

    private Map<Integer, Integer> allocateUsedLessonsForUser(Integer userId, List<UserCourse> enrollments) {
        // 口径唯一实现收敛到 like-common：退课/退款重算与本列表共用同一套 FIFO 分配。
        return this.courseConsumeEffectiveService.allocateUsedLessonsForUser(userId, enrollments);
    }

    private static int nonNegativeLessons(Integer value) {
        return value != null && value > 0 ? value : 0;
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

    public Map<String, Object> getLessonRecords(Integer userId, Integer courseClassId, Integer pageNo, Integer pageSize) {
        User user = (User)this.userMapper.selectById((Serializable)userId);
        if (user == null || user.getDeleteTime() != null) {
            throw new OperateException("\u7528\u6237\u4e0d\u5b58\u5728");
        }
        QueryWrapper<CourseConsumeLog> logQw = new QueryWrapper<CourseConsumeLog>();
        logQw.eq("user_id", (Object)userId);
        if (courseClassId != null && courseClassId > 0) {
            logQw.eq("course_class_id", (Object)courseClassId);
        }
        logQw.orderByDesc("create_time");
        List<CourseConsumeLog> allLogs = this.courseConsumeLogMapper.selectList((Wrapper)logQw);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ArrayList recordList = new ArrayList();
        for (CourseConsumeLog log : allLogs) {
            CourseManualLesson manual;
            CourseClass cls = (CourseClass)this.courseClassMapper.selectById((Serializable)log.getCourseClassId());
            String lessonDate = "";
            String timeSlot = "";
            CourseLesson lesson = null;
            if (log.getLessonNo() != null && log.getLessonNo() > 0) {
                lesson = (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>().eq("class_id", (Object)log.getCourseClassId()).eq("lesson_no", (Object)log.getLessonNo()).last("limit 1"));
                if (lesson != null) {
                    lessonDate = lesson.getLessonDate() != null ? lesson.getLessonDate() : "";
                    timeSlot = lesson.getTimeSlot() != null ? lesson.getTimeSlot() : "";
                }
            } else if (log.getSourceType() != null && log.getSourceType() == 1 && log.getManualId() != null && (manual = (CourseManualLesson)this.courseManualLessonMapper.selectById((Serializable)log.getManualId())) != null) {
                lessonDate = manual.getLessonDate() != null ? manual.getLessonDate() : "";
                timeSlot = manual.getTimeSlot() != null ? manual.getTimeSlot() : "";
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
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", log.getId());
            item.put("courseClassId", log.getCourseClassId());
            item.put("className", cls != null ? cls.getName() : "");
            item.put("instructorName", cls != null ? cls.getInstructorName() : "");
            item.put("lessonNo", log.getLessonNo());
            item.put("sourceType", log.getSourceType() != null ? log.getSourceType() : 0);
            item.put("sourceTypeText", log.getSourceType() != null && log.getSourceType() == 1 ? "\u4eba\u5de5\u6263\u8bfe" : "\u6b63\u5e38\u4e0a\u8bfe");
            item.put("consumeTime", log.getCreateTime() != null ? sdf.format(new Date(log.getCreateTime() * 1000L)) : "");
            item.put("lessonDate", lessonDate);
            item.put("timeSlot", timeSlot);
            if (attendance != null) {
                item.put("attendanceStatus", attendance.getStatus());
                item.put("attendanceStatusText", this.resolveAttendanceStatusText(attendance.getStatus()));
            }
            recordList.add(item);
        }
        int start = (pageNo - 1) * pageSize;
        int end = Math.min(start + pageSize, recordList.size());
        List pageList = start < recordList.size() ? recordList.subList(start, end) : new ArrayList();
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("total", recordList.size());
        result.put("list", pageList);
        return result;
    }

    public int countEffectiveUsedLessons(Integer userId, Integer courseClassId) {
        if (userId == null || userId <= 0 || courseClassId == null || courseClassId <= 0) {
            return 0;
        }
        List<CourseConsumeLog> logs = this.courseConsumeLogMapper.selectList(new QueryWrapper<CourseConsumeLog>().eq("user_id", (Object)userId).eq("course_class_id", (Object)courseClassId));
        return this.filterEffectiveLogs(logs, userId).size();
    }

    public int countEffectiveUsedLessons(Integer userId, Integer courseClassId, Integer studentId) {
        if (userId == null || userId <= 0 || courseClassId == null || courseClassId <= 0) {
            return 0;
        }
        List<UserCourse> enrollments = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).eq("course_class_id", (Object)courseClassId).isNull("delete_time"));
        if (enrollments.isEmpty()) {
            return 0;
        }
        Map<Integer, Integer> allocated = this.allocateUsedLessonsForUser(userId, enrollments);
        int targetStudentId = CourseConsumeLogUtil.normalizeStudentId((Integer)studentId);
        return enrollments.stream().filter(uc -> CourseConsumeLogUtil.normalizeStudentId((Integer)uc.getStudentId()) == targetStudentId).mapToInt(uc -> allocated.getOrDefault(uc.getId(), 0)).sum();
    }

    public int countEffectiveUsedLessonsForEnrollment(UserCourse enrollment) {
        if (enrollment == null || enrollment.getUserId() == null || enrollment.getCourseClassId() == null) {
            return 0;
        }
        List<UserCourse> peers = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().eq("user_id", (Object)enrollment.getUserId()).eq("course_class_id", (Object)enrollment.getCourseClassId()).isNull("delete_time"));
        Map<Integer, Integer> allocated = this.allocateUsedLessonsForUser(enrollment.getUserId(), peers);
        return allocated.getOrDefault(enrollment.getId(), 0);
    }

    public Map<Integer, Integer> countEffectiveUsedLessonsByUserIds(List<Integer> userIds) {
        LinkedHashMap<Integer, Integer> result = new LinkedHashMap<Integer, Integer>();
        if (userIds == null || userIds.isEmpty()) {
            return result;
        }
        List<Integer> distinctUserIds = userIds.stream().filter(id -> id != null && id > 0).distinct().collect(Collectors.toList());
        if (distinctUserIds.isEmpty()) {
            return result;
        }
        // FIFO 分配需要同一用户下的全部课包，不能只看当前分页行
        List<UserCourse> enrollments = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().in("user_id", distinctUserIds.toArray()).isNull("delete_time"));
        this.applyEffectiveRefundStatuses(enrollments);
        Map<Integer, List<UserCourse>> groups = enrollments.stream().filter(uc -> uc.getUserId() != null).collect(Collectors.groupingBy(UserCourse::getUserId, LinkedHashMap::new, Collectors.toList()));
        for (Map.Entry<Integer, List<UserCourse>> entry : groups.entrySet()) {
            result.putAll(this.allocateUsedLessonsForUser(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    private List<CourseConsumeLog> filterEffectiveLogs(List<CourseConsumeLog> logs, Integer userId) {
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

    private Map<Integer, CourseClass> loadClassMap(List<UserCourse> enrollments) {
        List classIds = enrollments.stream().map(UserCourse::getCourseClassId).filter(id -> id != null && id > 0).distinct().collect(Collectors.toList());
        if (classIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return this.courseClassMapper.selectList(new QueryWrapper<CourseClass>().in("id", classIds)).stream().collect(Collectors.toMap(CourseClass::getId, c -> c, (a, b) -> a));
    }

    private String resolveDisplayName(Integer userId, List<UserCourse> enrollments, User user) {
        for (UserCourse uc : enrollments) {
            Student stu;
            if (uc.getStudentId() == null || uc.getStudentId() <= 0 || (stu = (Student)this.studentMapper.selectOne(new QueryWrapper<Student>().eq("id", (Object)uc.getStudentId()).isNull("delete_time").last("limit 1"))) == null || !StringUtils.isNotEmpty((String)stu.getName())) continue;
            return stu.getName();
        }
        Student child = (Student)this.studentMapper.selectOne(new QueryWrapper<Student>().eq("user_id", (Object)userId).isNull("delete_time").orderByDesc("is_default").orderByAsc("id").last("limit 1"));
        if (child != null && StringUtils.isNotEmpty((String)child.getName())) {
            return child.getName();
        }
        if (StringUtils.isNotEmpty((String)user.getRealName())) {
            return user.getRealName();
        }
        if (StringUtils.isNotEmpty((String)user.getNickname())) {
            return user.getNickname();
        }
        return "\u7528\u6237" + userId;
    }

    private String resolveUserCourseStatusText(Integer status) {
        if (status == null || status == 0) {
            return "\u5b66\u4e60\u4e2d";
        }
        if (status == 1) {
            return "\u5df2\u5b8c\u6210";
        }
        if (status == 2) {
            return "\u5df2\u9000\u8bfe";
        }
        return "\u672a\u77e5";
    }

    private String resolveAttendanceStatusText(Integer status) {
        if (status == null) {
            return "\u672a\u77e5";
        }
        switch (status) {
            case 0: {
                return "\u672a\u7b7e\u5230";
            }
            case 1: {
                return "\u5df2\u5230\u8bfe";
            }
            case 2: {
                return "\u8bf7\u5047";
            }
            case 3: {
                return "\u7f3a\u52e4";
            }
        }
        return "\u672a\u77e5";
    }
}

