/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.util;

import com.mdd.common.entity.course.CourseAttendance;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseConsumeLog;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.UserCourse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CourseConsumeLogUtil {
    public static final int BOOKING_STATUS_CHECKED_IN = 10;
    public static final int BOOKING_STATUS_CANCELLED = 3;
    public static final int LESSON_STATUS_CANCELLED = 2;
    public static final int ATTENDANCE_ARRIVED = 1;

    private CourseConsumeLogUtil() {
    }

    public static boolean isEffectiveLessonRecord(CourseConsumeLog log, CourseLesson lesson, CourseBooking booking, CourseAttendance attendance) {
        if (log == null) {
            return false;
        }
        if (log.getSourceType() != null && log.getSourceType() == 1) {
            return true;
        }
        if (lesson != null && lesson.getStatus() != null && lesson.getStatus() == 2) {
            return false;
        }
        if (booking != null) {
            if (log.getStudentId() != null && log.getStudentId() > 0 && booking.getStudentId() != null && booking.getStudentId() > 0 && !log.getStudentId().equals(booking.getStudentId())) {
                return false;
            }
            if (booking.getStatus() != null && booking.getStatus() == 3) {
                return false;
            }
            if (booking.getStatus() != null && booking.getStatus() == 10) {
                return true;
            }
        }
        return attendance != null && attendance.getStatus() != null && attendance.getStatus() == 1;
    }

    public static int normalizeStudentId(Integer studentId) {
        return studentId != null && studentId > 0 ? studentId : 0;
    }

    public static String classStudentKey(Integer classId, Integer studentId) {
        return classId + ":" + CourseConsumeLogUtil.normalizeStudentId(studentId);
    }

    public static boolean matchesEnrollmentStudent(CourseConsumeLog log, Integer enrollmentStudentId) {
        if (log == null) {
            return false;
        }
        return CourseConsumeLogUtil.normalizeStudentId(log.getStudentId()) == CourseConsumeLogUtil.normalizeStudentId(enrollmentStudentId);
    }

    public static List<CourseConsumeLog> filterByEnrollmentStudent(List<CourseConsumeLog> logs, Integer studentId) {
        if (logs == null || logs.isEmpty()) {
            return logs == null ? new ArrayList() : logs;
        }
        int targetStudentId = CourseConsumeLogUtil.normalizeStudentId(studentId);
        ArrayList<CourseConsumeLog> result = new ArrayList<CourseConsumeLog>();
        for (CourseConsumeLog log : logs) {
            if (CourseConsumeLogUtil.normalizeStudentId(log.getStudentId()) != targetStudentId) continue;
            result.add(log);
        }
        return result;
    }

    public static Map<String, Long> groupEffectiveCountByClassStudent(List<CourseConsumeLog> effectiveLogs) {
        if (effectiveLogs == null || effectiveLogs.isEmpty()) {
            return Collections.emptyMap();
        }
        return effectiveLogs.stream().collect(Collectors.groupingBy(log -> CourseConsumeLogUtil.classStudentKey(log.getCourseClassId(), log.getStudentId()), Collectors.counting()));
    }

    public static Map<Integer, Integer> allocateUsedByEnrollmentFifo(List<UserCourse> enrollments, List<CourseConsumeLog> matchingEffectiveLogs) {
        LinkedHashMap<Integer, Integer> result = new LinkedHashMap<Integer, Integer>();
        if (enrollments == null || enrollments.isEmpty()) {
            return result;
        }
        List<UserCourse> allOrderedEnrollments = enrollments.stream().sorted(Comparator.comparing((UserCourse uc) -> uc.getCreateTime() != null ? uc.getCreateTime() : 0L).thenComparing(uc -> uc.getId() != null ? uc.getId() : 0)).collect(Collectors.toList());
        List<UserCourse> orderedEnrollments = allOrderedEnrollments.stream().filter(uc -> uc.getStatus() == null || uc.getStatus() != 2).collect(Collectors.toList());
        Map<Integer, UserCourse> enrollmentMap = orderedEnrollments.stream().filter(uc -> uc.getId() != null).collect(Collectors.toMap(UserCourse::getId, uc -> uc));
        for (UserCourse enrollment : orderedEnrollments) {
            if (enrollment.getId() == null) continue;
            result.put(enrollment.getId(), 0);
        }
        List<CourseConsumeLog> orderedLogs = matchingEffectiveLogs == null ? Collections.emptyList() : matchingEffectiveLogs.stream().sorted(Comparator.comparing((CourseConsumeLog log) -> log.getCreateTime() != null ? log.getCreateTime() : 0L).thenComparing(log -> log.getId() != null ? log.getId() : 0)).collect(Collectors.toList());
        ArrayList<CourseConsumeLog> legacyLogs = new ArrayList<CourseConsumeLog>();
        for (CourseConsumeLog log2 : orderedLogs) {
            int capacity;
            Integer linkedEnrollmentId = log2.getUserCourseId();
            if (linkedEnrollmentId == null || linkedEnrollmentId <= 0) {
                legacyLogs.add(log2);
                continue;
            }
            UserCourse linkedEnrollment = enrollmentMap.get(linkedEnrollmentId);
            if (linkedEnrollment == null) continue;
            int n = capacity = linkedEnrollment.getTotalLessons() != null ? Math.max(linkedEnrollment.getTotalLessons(), 0) : 0;
            int used = result.getOrDefault(linkedEnrollmentId, 0);
            if (used >= capacity) continue;
            result.put(linkedEnrollmentId, used + 1);
        }
        for (CourseConsumeLog log2 : legacyLogs) {
            int logStudentId = CourseConsumeLogUtil.normalizeStudentId(log2.getStudentId());
            if (CourseConsumeLogUtil.hasRefundedConflict(allOrderedEnrollments, logStudentId)) continue;
            UserCourse target = CourseConsumeLogUtil.findAvailableEnrollment(orderedEnrollments, result, logStudentId, false);
            if (target == null && logStudentId > 0) {
                target = CourseConsumeLogUtil.findAvailableEnrollment(orderedEnrollments, result, logStudentId, true);
            }
            if (target == null || target.getId() == null) continue;
            int used = result.getOrDefault(target.getId(), 0);
            result.put(target.getId(), used + 1);
        }
        return result;
    }

    private static boolean hasRefundedConflict(List<UserCourse> enrollments, int logStudentId) {
        for (UserCourse enrollment : enrollments) {
            if (enrollment.getStatus() == null || enrollment.getStatus() != 2) continue;
            int refundedStudentId = CourseConsumeLogUtil.normalizeStudentId(enrollment.getStudentId());
            if (logStudentId != 0 && refundedStudentId != 0 && refundedStudentId != logStudentId) continue;
            return true;
        }
        return false;
    }

    private static UserCourse findAvailableEnrollment(List<UserCourse> enrollments, Map<Integer, Integer> allocated, int logStudentId, boolean legacyEnrollmentOnly) {
        for (UserCourse enrollment : enrollments) {
            int capacity;
            if (enrollment.getId() == null) continue;
            int enrollmentStudentId = CourseConsumeLogUtil.normalizeStudentId(enrollment.getStudentId());
            if (logStudentId > 0 && (legacyEnrollmentOnly && enrollmentStudentId != 0 || !legacyEnrollmentOnly && enrollmentStudentId != logStudentId)) continue;
            int n = capacity = enrollment.getTotalLessons() != null ? Math.max(enrollment.getTotalLessons(), 0) : 0;
            if (allocated.getOrDefault(enrollment.getId(), 0) >= capacity) continue;
            return enrollment;
        }
        return null;
    }
}

