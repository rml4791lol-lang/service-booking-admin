package com.mdd.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseConsumeLog;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.student.Student;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseConsumeLogMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.student.StudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 核销考勤写入公共能力（T30.4 / #37）。
 *
 * <p>教练核销（#26）与后台人工补核销共用同一份「到场学员逐人扣券 + 预约置已上课 +
 * 消课流水」写入逻辑，保证两条链路的券次数、考勤状态与消课流水完全一致。</p>
 *
 * <p>幂等：调用方必须先对场次行 {@code SELECT ... FOR UPDATE} 串行化；本服务在同一
 * 事务内逐人判断 {@code la_course_booking.status}，已经是 10（已上课）的学员直接跳过，
 * 不重复扣券、不重复写流水。重复提交因此只会返回 skipped，不会产生新的扣减。</p>
 *
 * <p>无券口径（与 #26 一致）：没有可用次数券不阻断考勤，该学员仍记已上课并写消课流水，
 * 在 {@code warnings} 中逐人返回原因，本次不扣券。</p>
 */
@Service
public class CourseAttendanceService {

    public static final int BOOKING_ATTENDED = 10;
    public static final int BOOKING_CANCELLED = 3;

    @Resource
    CourseBookingMapper courseBookingMapper;
    @Resource
    CourseConsumeLogMapper courseConsumeLogMapper;
    @Resource
    UserCourseMapper userCourseMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    CouponCheckinService couponCheckinService;

    /**
     * 将指定学员标记为已上课，逐人扣券并写消课流水。
     *
     * @param lesson             场次（调用方已加行锁）
     * @param courseClass        课程
     * @param bookings           本场次全部有效预约（调用方已按 id 升序读取）
     * @param attendedStudentIds 本次到场的学员 ID（只处理其中确属本场次预约的）
     * @param operatorType       扣券流水操作人类型（coach/admin）
     * @param operatorId         操作人 ID
     * @param couponRemark       扣券流水备注
     * @param sourceType         消课流水来源：0=正常核销，2=后台人工补核销
     * @param ref                扣券流水业务引用（课程 + 日期 + 时段 + 第 N 节）
     * @return {attendedStudentIds, changedStudentIds, deductions, warnings, skipped}
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> markAttended(CourseLesson lesson, CourseClass courseClass,
                                            List<CourseBooking> bookings, Set<Integer> attendedStudentIds,
                                            String operatorType, Integer operatorId, String couponRemark,
                                            int sourceType, String ref) {
        Set<Integer> attended = attendedStudentIds == null
                ? new LinkedHashSet<>() : new LinkedHashSet<>(attendedStudentIds);
        Map<Integer, Student> studentMap = new LinkedHashMap<>();
        if (!attended.isEmpty()) {
            List<Student> students = studentMapper.selectBatchIds(attended);
            if (students != null) {
                for (Student student : students) {
                    studentMap.put(student.getId(), student);
                }
            }
        }

        long nowMillis = System.currentTimeMillis();
        int now = (int) (nowMillis / 1000L);
        List<String> warnings = new ArrayList<>();
        List<Map<String, Object>> deductions = new ArrayList<>();
        List<Map<String, Object>> skipped = new ArrayList<>();
        List<Map<String, Object>> undeducted = new ArrayList<>();
        List<String> changedStudentIds = new ArrayList<>();
        List<CourseBooking> ordered = new ArrayList<>();
        if (bookings != null) {
            ordered.addAll(bookings);
        }
        ordered.sort((left, right) -> {
            Integer leftId = left.getId() == null ? 0 : left.getId();
            Integer rightId = right.getId() == null ? 0 : right.getId();
            return Integer.compare(leftId, rightId);
        });

        for (CourseBooking booking : ordered) {
            Integer studentId = booking.getStudentId();
            if (studentId == null || studentId <= 0 || !attended.contains(studentId)) {
                continue;
            }
            if (booking.getStatus() != null && booking.getStatus() == BOOKING_ATTENDED) {
                // 已点名/已补核销：幂等跳过，不再扣券、不再写消课流水。
                skipped.add(skippedItem(studentId, studentMap, "已核销"));
                continue;
            }
            Student student = studentMap.get(studentId);
            String studentName = student == null || student.getName() == null || student.getName().trim().isEmpty()
                    ? "学员" : student.getName();
            Map<String, Object> deduction = couponCheckinService.deductForCheckin(
                    booking.getUserId(), studentId, courseClass.getId(), ref,
                    operatorType, operatorId, couponRemark);
            boolean deducted = Boolean.TRUE.equals(deduction.get("deducted"));
            if (deducted) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("studentId", String.valueOf(studentId));
                item.put("studentName", studentName);
                item.put("couponId", deduction.get("couponId"));
                item.put("title", deduction.get("title"));
                item.put("remaining", deduction.get("remaining"));
                deductions.add(item);
            } else {
                warnings.add("学员" + studentName + "：" + deduction.get("reason") + "，本次未扣券");
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("studentId", String.valueOf(studentId));
                item.put("studentName", studentName);
                item.put("reason", deduction.get("reason"));
                undeducted.add(item);
            }
            booking.setStatus(BOOKING_ATTENDED);
            booking.setUpdateTime((long) now);
            courseBookingMapper.updateById(booking);
            changedStudentIds.add(String.valueOf(studentId));

            CourseConsumeLog consumeLog = new CourseConsumeLog();
            consumeLog.setUserId(booking.getUserId());
            consumeLog.setStudentId(studentId);
            consumeLog.setUserCourseId(resolveUserCourseId(booking));
            consumeLog.setCourseClassId(courseClass.getId());
            consumeLog.setLessonNo(lesson.getLessonNo() == null ? 0 : lesson.getLessonNo());
            consumeLog.setSourceType(sourceType);
            consumeLog.setManualId(0);
            consumeLog.setCreateTime(nowMillis / 1000L);
            courseConsumeLogMapper.insert(consumeLog);
        }

        List<String> attendedText = new ArrayList<>();
        for (Integer studentId : attended) {
            attendedText.add(String.valueOf(studentId));
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("attendedStudentIds", attendedText);
        result.put("changedStudentIds", changedStudentIds);
        result.put("deductions", deductions);
        result.put("warnings", warnings);
        result.put("undeducted", undeducted);
        result.put("skipped", skipped);
        return result;
    }

    private Map<String, Object> skippedItem(Integer studentId, Map<Integer, Student> studentMap, String reason) {
        Student student = studentMap.get(studentId);
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("studentId", String.valueOf(studentId));
        item.put("studentName", student == null || student.getName() == null || student.getName().trim().isEmpty()
                ? "学员" : student.getName());
        item.put("reason", reason);
        return item;
    }

    private Integer resolveUserCourseId(CourseBooking booking) {
        if (booking.getUserId() == null || booking.getUserId() <= 0
                || booking.getStudentId() == null || booking.getStudentId() <= 0) {
            return 0;
        }
        UserCourse userCourse = userCourseMapper.selectOne(new QueryWrapper<UserCourse>()
                .eq("user_id", booking.getUserId())
                .eq("course_class_id", booking.getCourseClassId())
                .eq("student_id", booking.getStudentId())
                .isNull("delete_time")
                .orderByDesc("id")
                .last("limit 1"));
        // 新券模型不依赖 la_user_course；这里只做旧报名记录关联，不改其余额。
        return userCourse == null ? 0 : userCourse.getId();
    }
}
