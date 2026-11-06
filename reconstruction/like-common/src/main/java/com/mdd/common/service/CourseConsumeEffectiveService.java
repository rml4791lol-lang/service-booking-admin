package com.mdd.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.course.CourseAttendance;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseConsumeLog;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.mapper.course.CourseAttendanceMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseConsumeLogMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.util.CourseConsumeLogUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 「实际已消课时」唯一口径（按课节核销流水重算）。
 *
 * <p>为什么不能直接读 {@code la_user_course.used_lessons}：老核销链路（点名/签退）
 * 只写 {@code la_course_consume_log}，没有回写 used_lessons / remain_lessons，
 * 于是上完课的课包在库里仍是 used=0，退款时会按 0 节全退（资金风险）。</p>
 *
 * <p>口径与后台「老课包账户」、小程序「我的课程」一致：</p>
 * <ol>
 *     <li>取该用户全部消课流水，按 {@link CourseConsumeLogUtil#isEffectiveLessonRecord}
 *         过滤掉取消课节 / 已取消预约 / 非到课的脏流水；</li>
 *     <li>同一用户同一班级可能存在多个课包（多次报名），按报名时间 FIFO 把流水分配到
 *         各课包；带 user_course_id 的流水优先归到对应课包。</li>
 * </ol>
 *
 * <p>退款申请与审核都通过本服务重算，保证「申请时算出的金额」不是最终依据。</p>
 */
@Service
public class CourseConsumeEffectiveService {

    @Resource
    UserCourseMapper userCourseMapper;
    @Resource
    CourseConsumeLogMapper courseConsumeLogMapper;
    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    CourseBookingMapper courseBookingMapper;
    @Resource
    CourseAttendanceMapper courseAttendanceMapper;

    /** 某条报名记录（课包）按消课流水重算出的已用课时。 */
    public int countEffectiveUsedLessonsForEnrollment(UserCourse enrollment) {
        if (enrollment == null || enrollment.getUserId() == null || enrollment.getCourseClassId() == null) {
            return 0;
        }
        List<UserCourse> peers = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>()
                .eq("user_id", enrollment.getUserId())
                .eq("course_class_id", enrollment.getCourseClassId())
                .isNull("delete_time"));
        Map<Integer, Integer> allocated = this.allocateUsedLessonsForUser(enrollment.getUserId(), peers);
        return allocated.getOrDefault(enrollment.getId(), 0);
    }

    /**
     * 用户维度：把有效消课流水 FIFO 分配到同一班级的多个课包。
     * （与后台老课包账户列表共用，避免两套实现漂移。）
     */
    public Map<Integer, Integer> allocateUsedLessonsForUser(Integer userId, List<UserCourse> enrollments) {
        LinkedHashMap<Integer, Integer> usedByUserCourseId = new LinkedHashMap<Integer, Integer>();
        if (userId == null || enrollments == null || enrollments.isEmpty()) {
            return usedByUserCourseId;
        }
        List<CourseConsumeLog> allLogs = this.courseConsumeLogMapper.selectList(
                new QueryWrapper<CourseConsumeLog>().eq("user_id", userId));
        List<CourseConsumeLog> effectiveLogs = this.filterEffectiveLogs(allLogs, userId);
        Map<Integer, List<UserCourse>> groups = enrollments.stream()
                .filter(uc -> uc.getCourseClassId() != null && uc.getCourseClassId() > 0)
                .collect(Collectors.groupingBy(UserCourse::getCourseClassId, LinkedHashMap::new, Collectors.toList()));
        for (Map.Entry<Integer, List<UserCourse>> entry : groups.entrySet()) {
            List<CourseConsumeLog> groupLogs = new ArrayList<CourseConsumeLog>();
            for (CourseConsumeLog log : effectiveLogs) {
                if (log.getCourseClassId() == null || !log.getCourseClassId().equals(entry.getKey())) {
                    continue;
                }
                groupLogs.add(log);
            }
            usedByUserCourseId.putAll(CourseConsumeLogUtil.allocateUsedByEnrollmentFifo(entry.getValue(), groupLogs));
        }
        return usedByUserCourseId;
    }

    private List<CourseConsumeLog> filterEffectiveLogs(List<CourseConsumeLog> logs, Integer userId) {
        ArrayList<CourseConsumeLog> effective = new ArrayList<CourseConsumeLog>();
        if (logs == null) {
            return effective;
        }
        for (CourseConsumeLog log : logs) {
            CourseLesson lesson = null;
            String lessonDate = "";
            if (log.getLessonNo() != null && log.getLessonNo() > 0) {
                lesson = this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>()
                        .eq("class_id", log.getCourseClassId())
                        .eq("lesson_no", log.getLessonNo())
                        .last("limit 1"));
                if (lesson != null) {
                    lessonDate = lesson.getLessonDate() != null ? lesson.getLessonDate() : "";
                }
            }
            CourseBooking booking = null;
            CourseAttendance attendance = null;
            if (!lessonDate.isEmpty()) {
                QueryWrapper<CourseBooking> bookingQuery = new QueryWrapper<CourseBooking>()
                        .eq("course_class_id", log.getCourseClassId())
                        .eq("user_id", userId)
                        .eq("lesson_date", lessonDate);
                if (log.getStudentId() != null && log.getStudentId() > 0) {
                    bookingQuery.eq("student_id", log.getStudentId());
                }
                booking = this.courseBookingMapper.selectOne(bookingQuery.last("limit 1"));
                attendance = this.courseAttendanceMapper.selectOne(new QueryWrapper<CourseAttendance>()
                        .eq("class_id", log.getCourseClassId())
                        .eq("user_id", userId)
                        .eq("lesson_date", lessonDate)
                        .last("limit 1"));
            }
            if (!CourseConsumeLogUtil.isEffectiveLessonRecord(log, lesson, booking, attendance)) {
                continue;
            }
            effective.add(log);
        }
        return effective;
    }
}
