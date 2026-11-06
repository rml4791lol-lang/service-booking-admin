/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.admin.service.ICourseManualConsumeService
 *  com.mdd.admin.service.IUserCourseAccountService
 *  com.mdd.admin.service.impl.CourseManualConsumeServiceImpl
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.RechargeOrder
 *  com.mdd.common.entity.admin.Admin
 *  com.mdd.common.entity.agent.Agent
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.CourseConsumeLog
 *  com.mdd.common.entity.course.CourseManualConsume
 *  com.mdd.common.entity.course.CourseManualLesson
 *  com.mdd.common.entity.course.UserCourse
 *  com.mdd.common.entity.instructor.InstructorAccountLog
 *  com.mdd.common.entity.student.Student
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.RechargeOrderMapper
 *  com.mdd.common.mapper.admin.AdminMapper
 *  com.mdd.common.mapper.agent.AgentMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.CourseConsumeLogMapper
 *  com.mdd.common.mapper.course.CourseManualConsumeMapper
 *  com.mdd.common.mapper.course.CourseManualLessonMapper
 *  com.mdd.common.mapper.course.UserCourseMapper
 *  com.mdd.common.mapper.instructor.InstructorAccountLogMapper
 *  com.mdd.common.mapper.student.StudentMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.service.LessonCommissionService
 *  com.mdd.common.util.ClassScheduleUtil
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.UserCourseBalanceUtil
 *  com.mdd.common.util.UserCourseBalanceUtil$Snapshot
 *  com.mdd.common.util.UserCourseEligibilityUtil
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.service.ICourseManualConsumeService;
import com.mdd.admin.service.IUserCourseAccountService;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.admin.Admin;
import com.mdd.common.entity.agent.Agent;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseConsumeLog;
import com.mdd.common.entity.course.CourseManualConsume;
import com.mdd.common.entity.course.CourseManualLesson;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.instructor.InstructorAccountLog;
import com.mdd.common.entity.student.Student;
import com.mdd.common.entity.user.User;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.mapper.agent.AgentMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseConsumeLogMapper;
import com.mdd.common.mapper.course.CourseManualConsumeMapper;
import com.mdd.common.mapper.course.CourseManualLessonMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.instructor.InstructorAccountLogMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.service.LessonCommissionService;
import com.mdd.common.settlement.SettlementSigner;
import com.mdd.common.util.ClassScheduleUtil;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.UserCourseBalanceUtil;
import com.mdd.common.util.UserCourseEligibilityUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseManualConsumeServiceImpl
implements ICourseManualConsumeService {
    @Resource
    CourseManualLessonMapper courseManualLessonMapper;
    @Resource
    CourseManualConsumeMapper courseManualConsumeMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CourseConsumeLogMapper courseConsumeLogMapper;
    @Resource
    UserCourseMapper userCourseMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    InstructorAccountLogMapper instructorAccountLogMapper;
    @Resource
    AdminMapper adminMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    LessonCommissionService lessonCommissionService;
    @Resource
    AgentMapper agentMapper;
    @Resource
    IUserCourseAccountService userCourseAccountService;

    public List<Map<String, Object>> listByClass(Integer classId) {
        CourseClass courseClass = this.getAccessibleClass(classId);
        List<CourseManualLesson> records = this.courseManualLessonMapper.selectList(new QueryWrapper<CourseManualLesson>().eq("class_id", (Object)classId).orderByDesc("id"));
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CourseManualLesson item : records) {
            LinkedHashMap<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("id", item.getId());
            row.put("classId", item.getClassId());
            row.put("className", courseClass.getName());
            row.put("lessonDate", item.getLessonDate());
            row.put("timeSlot", item.getTimeSlot());
            row.put("instructorId", item.getInstructorId());
            row.put("instructorName", item.getInstructorName());
            row.put("defaultInstructorId", item.getDefaultInstructorId());
            row.put("isSubstitute", item.getDefaultInstructorId() != null && item.getInstructorId() != null && !item.getDefaultInstructorId().equals(item.getInstructorId()));
            row.put("venue", item.getVenue());
            row.put("feeAmount", item.getFeeAmount());
            row.put("studentCount", item.getStudentCount());
            row.put("remark", item.getRemark());
            row.put("operatorName", item.getOperatorName());
            row.put("createTime", this.formatTime(item.getCreateTime()));
            list.add(row);
        }
        return list;
    }

    public Map<String, Object> detail(Integer id) {
        CourseManualLesson manual = (CourseManualLesson)this.courseManualLessonMapper.selectById((Serializable)id);
        if (manual == null) {
            throw new OperateException("\u8bb0\u5f55\u4e0d\u5b58\u5728");
        }
        CourseClass courseClass = this.getAccessibleClass(manual.getClassId());
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("id", manual.getId());
        result.put("classId", manual.getClassId());
        result.put("className", courseClass.getName());
        result.put("lessonDate", manual.getLessonDate());
        result.put("timeSlot", manual.getTimeSlot());
        result.put("instructorId", manual.getInstructorId());
        result.put("instructorName", manual.getInstructorName());
        result.put("defaultInstructorId", manual.getDefaultInstructorId());
        result.put("venue", manual.getVenue());
        result.put("feeAmount", manual.getFeeAmount());
        result.put("studentCount", manual.getStudentCount());
        result.put("remark", manual.getRemark());
        result.put("operatorName", manual.getOperatorName());
        result.put("createTime", this.formatTime(manual.getCreateTime()));
        List<CourseManualConsume> details = this.courseManualConsumeMapper.selectList(new QueryWrapper<CourseManualConsume>().eq("manual_id", (Object)id));
        ArrayList students = new ArrayList();
        for (CourseManualConsume detail : details) {
            Student student;
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("userId", detail.getUserId());
            item.put("studentId", detail.getStudentId());
            User user = (User)this.userMapper.selectById((Serializable)detail.getUserId());
            if (user != null) {
                item.put("nickname", user.getNickname());
                item.put("mobile", user.getMobile());
            }
            if (detail.getStudentId() != null && detail.getStudentId() > 0 && (student = (Student)this.studentMapper.selectById((Serializable)detail.getStudentId())) != null) {
                item.put("studentName", student.getName());
            }
            students.add(item);
        }
        result.put("students", students);
        return result;
    }

    @Transactional(rollbackFor={Exception.class})
    public void submit(Integer adminId, Map<String, Object> params) {
        Integer classId = this.parseInt(params.get("classId"));
        String lessonDate = params.get("lessonDate") != null ? params.get("lessonDate").toString().trim() : "";
        String timeSlot = params.get("timeSlot") != null ? params.get("timeSlot").toString().trim() : "";
        Integer instructorId = this.parseInt(params.get("instructorId"));
        String venue = params.get("venue") != null ? params.get("venue").toString().trim() : "";
        BigDecimal feeAmount = this.parseAmount(params.get("feeAmount"));
        String remark = params.get("remark") != null ? params.get("remark").toString().trim() : "";
        List<Map<String, Integer>> studentSelections = this.parseStudentSelections(params);
        studentSelections.sort(Comparator
                .comparingInt((Map<String, Integer> item) -> item.getOrDefault("userCourseId", 0))
                .thenComparingInt(item -> item.getOrDefault("userId", 0))
                .thenComparingInt(item -> item.getOrDefault("studentId", 0)));
        if (classId <= 0) {
            throw new OperateException("\u73ed\u7ea7\u53c2\u6570\u9519\u8bef");
        }
        if (lessonDate.isEmpty()) {
            throw new OperateException("\u8bf7\u9009\u62e9\u4e0a\u8bfe\u65e5\u671f");
        }
        if (timeSlot.isEmpty()) {
            throw new OperateException("\u8bf7\u586b\u5199\u4e0a\u8bfe\u65f6\u6bb5");
        }
        if (instructorId <= 0) {
            throw new OperateException("\u8bf7\u9009\u62e9\u5b9e\u9645\u4e0a\u8bfe\u6559\u7ec3");
        }
        if (venue.isEmpty()) {
            throw new OperateException("\u8bf7\u586b\u5199\u4e0a\u8bfe\u5730\u70b9");
        }
        CourseClass courseClass = this.getAccessibleClass(classId);
        if (courseClass.getStatus() == null || courseClass.getStatus() != 1) {
            throw new OperateException("\u4ec5\u8fdb\u884c\u4e2d\u7684\u73ed\u7ea7\u53ef\u53d1\u8d77\u4eba\u5de5\u6263\u8bfe");
        }
        if (ClassScheduleUtil.validateTimeSlot((String)(timeSlot = this.normalizeManualTimeSlot(timeSlot, lessonDate, courseClass))) != null) {
            throw new OperateException("\u4e0a\u8bfe\u65f6\u6bb5\u683c\u5f0f\u9519\u8bef\uff0c\u8bf7\u9009\u62e9\u5b8c\u6574\u7684\u5f00\u59cb\u548c\u7ed3\u675f\u65f6\u95f4");
        }
        Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)instructorId);
        if (instructor == null || instructor.getDeleteTime() != null) {
            throw new OperateException("\u6240\u9009\u6559\u7ec3\u4e0d\u5b58\u5728");
        }
        if (feeAmount == null || feeAmount.compareTo(BigDecimal.ZERO) <= 0) {
            BigDecimal bigDecimal = feeAmount = courseClass.getFeePerLesson() != null ? courseClass.getFeePerLesson() : BigDecimal.ZERO;
        }
        if (feeAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new OperateException("\u7ed3\u8d39\u91d1\u989d\u5fc5\u987b\u5927\u4e8e0");
        }
        Admin admin = (Admin)this.adminMapper.selectById((Serializable)adminId);
        int now = (int)(System.currentTimeMillis() / 1000L);
        CourseManualLesson manual = new CourseManualLesson();
        manual.setClassId(classId);
        manual.setLessonDate(lessonDate);
        manual.setTimeSlot(timeSlot);
        manual.setInstructorId(instructorId);
        manual.setInstructorName(instructor.getName() != null ? instructor.getName() : "");
        manual.setDefaultInstructorId(Integer.valueOf(courseClass.getInstructorId() != null ? courseClass.getInstructorId() : 0));
        manual.setVenue(venue);
        manual.setFeeAmount(feeAmount);
        manual.setStudentCount(Integer.valueOf(studentSelections.size()));
        manual.setRemark(remark);
        manual.setOperatorId(Integer.valueOf(adminId != null ? adminId : 0));
        manual.setOperatorName(admin != null ? admin.getName() : "\u7ba1\u7406\u5458");
        manual.setRequestKey(buildManualRequestKey(classId, lessonDate, timeSlot, studentSelections));
        manual.setStatus(Integer.valueOf(1));
        manual.setCreateTime(Integer.valueOf(now));
        manual.setUpdateTime(Integer.valueOf(now));
        try {
            this.courseManualLessonMapper.insert(manual);
        } catch (DuplicateKeyException duplicate) {
            throw new OperateException("\u8be5\u73ed\u7ea7\u540c\u65e5\u540c\u65f6\u6bb5\u7684\u5b66\u5458\u5df2\u624b\u5de5\u6263\u8bfe\uff0c\u8bf7\u52ff\u91cd\u590d\u63d0\u4ea4");
        }
        for (Map selection : studentSelections) {
            Integer userCourseId;
            Integer studentId;
            Integer userId = (Integer)selection.get("userId");
            UserCourse userCourse = this.resolveConsumableUserCourse(userId, classId, studentId = (Integer)selection.get("studentId"), userCourseId = (Integer)selection.get("userCourseId"));
            if (userCourse == null) {
                throw new OperateException("\u6240\u9009\u5b66\u5458\u6ca1\u6709\u53ef\u6263\u9664\u7684\u6709\u6548\u8bfe\u65f6");
            }
            Integer consumeLogId = this.consumeOneStudent(userCourse, courseClass, manual.getId(), now);
            CourseManualConsume detail = new CourseManualConsume();
            detail.setManualId(manual.getId());
            detail.setUserId(userId);
            detail.setStudentId(userCourse.getStudentId());
            detail.setUserCourseId(userCourse.getId());
            detail.setConsumeLogId(consumeLogId);
            detail.setCreateTime(Integer.valueOf(now));
            this.courseManualConsumeMapper.insert(detail);
        }
        this.settleInstructorFee(instructor, courseClass, manual, feeAmount, now);
    }

    private String normalizeManualTimeSlot(String timeSlot, String lessonDate, CourseClass courseClass) {
        String value;
        String string = value = timeSlot != null ? timeSlot.trim() : "";
        if (StringUtils.isEmpty((String)value)) {
            return "";
        }
        if (ClassScheduleUtil.validateTimeSlot((String)value) == null) {
            return value;
        }
        Integer weekDay = this.parseLessonWeekDay(lessonDate);
        String ruleSlot = this.resolveScheduleRuleTimeSlot(courseClass, weekDay);
        if (!StringUtils.isEmpty((String)ruleSlot)) {
            return ruleSlot;
        }
        String inputSummarySlot = this.resolveSummaryTimeSlot(value, weekDay);
        if (!StringUtils.isEmpty((String)inputSummarySlot)) {
            return inputSummarySlot;
        }
        if (courseClass != null) {
            String classTimeSlot;
            String classSummarySlot = this.resolveSummaryTimeSlot(courseClass.getTimeSlot(), weekDay);
            if (!StringUtils.isEmpty((String)classSummarySlot)) {
                return classSummarySlot;
            }
            String string2 = classTimeSlot = courseClass.getTimeSlot() != null ? courseClass.getTimeSlot().trim() : "";
            if (ClassScheduleUtil.validateTimeSlot((String)classTimeSlot) == null) {
                return classTimeSlot;
            }
        }
        throw new OperateException("\u4e0a\u8bfe\u65f6\u6bb5\u683c\u5f0f\u9519\u8bef\uff0c\u8bf7\u9009\u62e9\u5b8c\u6574\u7684\u5f00\u59cb\u548c\u7ed3\u675f\u65f6\u95f4");
    }

    private String resolveScheduleRuleTimeSlot(CourseClass courseClass, Integer weekDay) {
        if (courseClass == null || weekDay == null || StringUtils.isEmpty((String)courseClass.getScheduleRules())) {
            return "";
        }
        try {
            String slot = (String)ClassScheduleUtil.toTimeSlotMap((String)courseClass.getScheduleRules()).get(weekDay);
            if (!StringUtils.isEmpty((String)slot) && ClassScheduleUtil.validateTimeSlot((String)slot) == null) {
                return slot.trim();
            }
        }
        catch (Exception ignored) {
            return "";
        }
        return "";
    }

    private String resolveSummaryTimeSlot(String summary, Integer weekDay) {
        if (StringUtils.isEmpty((String)summary) || weekDay == null) {
            return "";
        }
        String[] parts = summary.split("[,\uff0c]");
        boolean hasWeekPrefix = false;
        for (String part : parts) {
            String text = part != null ? part.trim() : "";
            String slot = this.stripWeekPrefix(text, weekDay);
            if (slot == null) {
                if (this.stripAnyWeekPrefix(text) == null) continue;
                hasWeekPrefix = true;
                continue;
            }
            hasWeekPrefix = true;
            if (ClassScheduleUtil.validateTimeSlot((String)slot) != null) continue;
            return slot.trim();
        }
        if (hasWeekPrefix) {
            return "";
        }
        for (String part : parts) {
            String slot;
            String string = slot = part != null ? part.trim() : "";
            if (ClassScheduleUtil.validateTimeSlot((String)slot) != null) continue;
            return slot;
        }
        return "";
    }

    private String stripWeekPrefix(String text, Integer weekDay) {
        if (StringUtils.isEmpty((String)text) || weekDay == null) {
            return null;
        }
        for (String label : this.getWeekLabels(weekDay)) {
            if (!text.startsWith(label)) continue;
            return text.substring(label.length()).trim();
        }
        return null;
    }

    private String stripAnyWeekPrefix(String text) {
        if (StringUtils.isEmpty((String)text)) {
            return null;
        }
        for (int weekDay = 1; weekDay <= 7; ++weekDay) {
            String slot = this.stripWeekPrefix(text, Integer.valueOf(weekDay));
            if (slot == null) continue;
            return slot;
        }
        return null;
    }

    private String[] getWeekLabels(Integer weekDay) {
        switch (weekDay) {
            case 1: {
                return new String[]{"\u5468\u4e00", "\u661f\u671f\u4e00"};
            }
            case 2: {
                return new String[]{"\u5468\u4e8c", "\u661f\u671f\u4e8c"};
            }
            case 3: {
                return new String[]{"\u5468\u4e09", "\u661f\u671f\u4e09"};
            }
            case 4: {
                return new String[]{"\u5468\u56db", "\u661f\u671f\u56db"};
            }
            case 5: {
                return new String[]{"\u5468\u4e94", "\u661f\u671f\u4e94"};
            }
            case 6: {
                return new String[]{"\u5468\u516d", "\u661f\u671f\u516d"};
            }
            case 7: {
                return new String[]{"\u5468\u65e5", "\u5468\u5929", "\u661f\u671f\u65e5", "\u661f\u671f\u5929"};
            }
        }
        return new String[0];
    }

    private Integer parseLessonWeekDay(String lessonDate) {
        try {
            return LocalDate.parse(lessonDate).getDayOfWeek().getValue();
        }
        catch (DateTimeParseException e) {
            throw new OperateException("\u4e0a\u8bfe\u65e5\u671f\u683c\u5f0f\u9519\u8bef");
        }
    }

    private List<Map<String, Integer>> parseStudentSelections(Map<String, Object> params) {
        Object rawUserIds;
        Integer userId;
        LinkedHashMap uniqueSelections = new LinkedHashMap();
        Object rawSelections = params.get("studentSelections");
        if (rawSelections instanceof List) {
            for (Object item : (List)rawSelections) {
                if (!(item instanceof Map)) continue;
                Map selection = (Map)item;
                userId = this.parseInt(selection.get("userId"));
                Integer studentId = this.parseInt(selection.get("studentId"));
                Integer userCourseId = this.parseInt(selection.get("userCourseId"));
                if (userId <= 0) continue;
                LinkedHashMap<String, Integer> value = new LinkedHashMap<String, Integer>();
                value.put("userId", userId);
                value.put("studentId", studentId);
                value.put("userCourseId", userCourseId);
                String key = userCourseId > 0 ? "uc:" + userCourseId : userId + ":" + Math.max(studentId, 0);
                uniqueSelections.putIfAbsent(key, value);
            }
        }
        if (uniqueSelections.isEmpty() && (rawUserIds = params.get("studentUserIds")) instanceof List) {
            for (Object value : (List)rawUserIds) {
                userId = this.parseInt(value);
                if (userId <= 0) continue;
                LinkedHashMap<String, Integer> selection = new LinkedHashMap<String, Integer>();
                selection.put("userId", userId);
                selection.put("studentId", 0);
                selection.put("userCourseId", 0);
                uniqueSelections.putIfAbsent(userId + ":0", selection);
            }
        }
        if (uniqueSelections.isEmpty()) {
            throw new OperateException("\u8bf7\u81f3\u5c11\u9009\u62e9\u4e00\u540d\u5b66\u5458");
        }
        return new ArrayList<Map<String, Integer>>(uniqueSelections.values());
    }

    private UserCourse resolveConsumableUserCourse(Integer userId, Integer classId, Integer studentId, Integer userCourseId) {
        List<UserCourse> candidates = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).eq("course_class_id", (Object)classId).ne("status", (Object)2).isNull("delete_time").orderByAsc("id"));
        if (candidates.isEmpty()) {
            return null;
        }
        ArrayList<UserCourse> consumable = new ArrayList<UserCourse>();
        for (UserCourse candidate : candidates) {
            candidate = this.userCourseMapper.selectByIdForUpdate(candidate.getId());
            if (candidate == null || (candidate.getStatus() != null && candidate.getStatus() == 2)
                    || candidate.getDeleteTime() != null) {
                continue;
            }
            RechargeOrder order = null;
            if (candidate.getOrderId() != null && candidate.getOrderId() > 0 && ((order = (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)candidate.getOrderId())) == null || !UserCourseEligibilityUtil.isActiveOrder((Integer)order.getPayStatus(), (Integer)order.getRefundStatus(), (Long)order.getDeleteTime()))) continue;
            Integer resolvedStudentId = candidate.getStudentId();
            if ((resolvedStudentId == null || resolvedStudentId <= 0) && order != null && order.getStudentId() != null && order.getStudentId() > 0) {
                resolvedStudentId = order.getStudentId();
                candidate.setStudentId(resolvedStudentId);
            }
            int effectiveUsed = this.userCourseAccountService.countEffectiveUsedLessonsForEnrollment(candidate);
            UserCourseBalanceUtil.Snapshot balance = UserCourseBalanceUtil.resolve((Integer)candidate.getTotalLessons(), (Integer)candidate.getUsedLessons(), (Integer)candidate.getRemainLessons(), (Integer)effectiveUsed, (Integer)candidate.getStatus());
            if (balance.getRemainLessons() <= 0) continue;
            this.syncEnrollmentBalance(candidate, balance);
            consumable.add(candidate);
        }
        if (consumable.isEmpty()) {
            return null;
        }
        if (userCourseId != null && userCourseId > 0) {
            for (UserCourse candidate : consumable) {
                if (!userCourseId.equals(candidate.getId())) continue;
                return candidate;
            }
        }
        if (studentId != null && studentId > 0) {
            for (UserCourse candidate : consumable) {
                if (!studentId.equals(candidate.getStudentId())) continue;
                return candidate;
            }
            return null;
        }
        if (!UserCourseEligibilityUtil.canResolveWithoutStudent((int)((int)consumable.stream().map(uc -> uc.getStudentId() != null && uc.getStudentId() > 0 ? uc.getStudentId() : 0).distinct().count()))) {
            throw new OperateException("\u8be5\u5bb6\u957f\u6709\u591a\u4e2a\u5b66\u5458\uff0c\u8bf7\u9009\u62e9\u5177\u4f53\u5b66\u5458\u540e\u6263\u8bfe");
        }
        return (UserCourse)consumable.get(0);
    }

    private void syncEnrollmentBalance(UserCourse uc, UserCourseBalanceUtil.Snapshot balance) {
        boolean dirty = false;
        if (uc.getRemainLessons() == null || uc.getRemainLessons().intValue() != balance.getRemainLessons()) {
            uc.setRemainLessons(Integer.valueOf(balance.getRemainLessons()));
            dirty = true;
        }
        if (uc.getUsedLessons() == null || uc.getUsedLessons().intValue() != balance.getUsedLessons()) {
            uc.setUsedLessons(Integer.valueOf(balance.getUsedLessons()));
            dirty = true;
        }
        if ((uc.getTotalLessons() == null || uc.getTotalLessons().intValue() != balance.getTotalLessons()) && balance.getTotalLessons() > 0) {
            uc.setTotalLessons(Integer.valueOf(balance.getTotalLessons()));
            dirty = true;
        }
        if (uc.getStatus() == null || uc.getStatus().intValue() != balance.getStatus()) {
            uc.setStatus(Integer.valueOf(balance.getStatus()));
            dirty = true;
        }
        if (dirty) {
            uc.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.userCourseMapper.updateById(uc);
        }
    }

    private Integer consumeOneStudent(UserCourse uc, CourseClass cls, Integer manualId, int now) {
        Integer remain = uc.getRemainLessons() != null ? uc.getRemainLessons() : 0;
        Integer used = uc.getUsedLessons() != null ? uc.getUsedLessons() : 0;
        Integer total = uc.getTotalLessons() != null ? uc.getTotalLessons() : 0;
        if (remain == 0 && used == 0 && total > 0) {
            remain = total;
        }
        if (total == 0 && cls.getTotalLessons() != null) {
            total = cls.getTotalLessons();
            if (remain == 0 && used == 0) {
                remain = total;
            }
            uc.setTotalLessons(total);
        }
        if (remain <= 0 && total > used) {
            remain = total - used;
            uc.setRemainLessons(remain);
            if (uc.getStatus() != null && uc.getStatus() == 1) {
                uc.setStatus(Integer.valueOf(0));
            }
        }
        if (remain <= 0) {
            throw new OperateException("\u5b66\u5458\u5269\u4f59\u8bfe\u65f6\u4e0d\u8db3\uff0c\u65e0\u6cd5\u6263\u8bfe");
        }
        int changed = this.userCourseMapper.consumeOneLesson(uc.getId(), Long.valueOf(now));
        if (changed != 1) {
            throw new OperateException("\u5b66\u5458\u5269\u4f59\u8bfe\u65f6\u4e0d\u8db3\u6216\u62a5\u540d\u5df2\u9000\u8bfe\uff0c\u65e0\u6cd5\u6263\u8bfe");
        }
        UserCourse consumed = this.userCourseMapper.selectByIdForUpdate(uc.getId());
        if (consumed == null) {
            throw new OperateException("\u62a5\u540d\u8bb0\u5f55\u4e0d\u5b58\u5728");
        }
        consumed.setStatus(Integer.valueOf(consumed.getRemainLessons() != null && consumed.getRemainLessons() <= 0 ? 1 : 0));
        consumed.setUpdateTime(Long.valueOf(now));
        this.userCourseMapper.updateById(consumed);
        uc.setRemainLessons(consumed.getRemainLessons());
        uc.setUsedLessons(consumed.getUsedLessons());
        uc.setStatus(consumed.getStatus());
        uc.setUpdateTime(Long.valueOf(now));
        CourseConsumeLog consumeLog = new CourseConsumeLog();
        consumeLog.setUserId(uc.getUserId());
        consumeLog.setStudentId(Integer.valueOf(uc.getStudentId() != null && uc.getStudentId() > 0 ? uc.getStudentId() : 0));
        consumeLog.setUserCourseId(uc.getId());
        consumeLog.setCourseClassId(uc.getCourseClassId());
        consumeLog.setLessonNo(Integer.valueOf(0));
        consumeLog.setSourceType(Integer.valueOf(1));
        consumeLog.setManualId(manualId);
        consumeLog.setConsumeKey("manual:" + manualId + ":" + uc.getId());
        consumeLog.setCreateTime(Long.valueOf(now));
        this.courseConsumeLogMapper.insert(consumeLog);
        this.lessonCommissionService.recordForLessonConsume(uc, cls);
        RechargeOrder rechargeOrder = null;
        if (uc.getOrderId() != null && uc.getOrderId() > 0) {
            RechargeOrder linkedOrder = (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)uc.getOrderId());
            if (linkedOrder != null && UserCourseEligibilityUtil.isActiveOrder((Integer)linkedOrder.getPayStatus(), (Integer)linkedOrder.getRefundStatus(), (Long)linkedOrder.getDeleteTime())) {
                rechargeOrder = linkedOrder;
            }
        } else {
            QueryWrapper<RechargeOrder> orderQuery = new QueryWrapper<RechargeOrder>().eq("user_id", (Object)uc.getUserId()).eq("course_class_id", (Object)uc.getCourseClassId()).eq("pay_status", (Object)1).eq("refund_status", (Object)0).gt("lesson_count", (Object)0).isNull("delete_time").orderByAsc("id");
            if (uc.getStudentId() != null && uc.getStudentId() > 0) {
                orderQuery.eq("student_id", (Object)uc.getStudentId());
            }
            rechargeOrder = (RechargeOrder)this.rechargeOrderMapper.selectOne((Wrapper)orderQuery.last("limit 1"));
        }
        if (rechargeOrder != null && rechargeOrder.getLessonCount() != null && rechargeOrder.getLessonCount() > 0) {
            this.rechargeOrderMapper.consumeLessonCount(rechargeOrder.getId(), Long.valueOf(now));
        }
        return consumeLog.getId();
    }

    private void settleInstructorFee(Instructor instructor, CourseClass courseClass, CourseManualLesson manual, BigDecimal feeAmount, int now) {
        int changed = this.instructorMapper.addManualLessonSettlement(instructor.getId(), feeAmount);
        if (changed != 1) {
            throw new OperateException("\u6559\u7ec3\u4e0d\u5b58\u5728\u6216\u5df2\u5220\u9664\uff0c\u7ed3\u8d39\u5931\u8d25");
        }
        Instructor settled = this.instructorMapper.selectById(instructor.getId());
        BigDecimal afterBalance = settled.getBalance() != null ? settled.getBalance() : BigDecimal.ZERO;
        BigDecimal beforeBalance = afterBalance.subtract(feeAmount);
        InstructorAccountLog accountLog = new InstructorAccountLog();
        accountLog.setInstructorId(instructor.getId());
        accountLog.setType(Integer.valueOf(1));
        accountLog.setAmount(feeAmount);
        accountLog.setBeforeBalance(beforeBalance);
        accountLog.setAfterBalance(afterBalance);
        accountLog.setRelationId(manual.getId());
        accountLog.setSettlementKey("manual:" + manual.getId());
        accountLog.setRemark("\u4eba\u5de5\u52a0\u8bfe: " + courseClass.getName() + " " + manual.getLessonDate() + " " + manual.getTimeSlot());
        accountLog.setCreateTime(Long.valueOf(now));
        this.instructorAccountLogMapper.insert(accountLog);
    }

    private String buildManualRequestKey(Integer classId, String lessonDate, String timeSlot,
                                         List<Map<String, Integer>> selections) {
        List<String> identities = new ArrayList<>();
        for (Map<String, Integer> selection : selections) {
            int userCourseId = selection.getOrDefault("userCourseId", 0);
            if (userCourseId > 0) {
                identities.add("uc:" + userCourseId);
            } else {
                identities.add("u:" + selection.getOrDefault("userId", 0)
                        + ":s:" + selection.getOrDefault("studentId", 0));
            }
        }
        identities.sort(String::compareTo);
        return SettlementSigner.sha256Hex(classId + "|" + lessonDate + "|" + timeSlot
                + "|" + String.join(",", identities));
    }

    private CourseClass getAccessibleClass(Integer classId) {
        Agent agent;
        CourseClass courseClass = (CourseClass)this.courseClassMapper.selectOne(new QueryWrapper<CourseClass>().eq("id", (Object)classId).eq("is_delete", (Object)0).last("limit 1"));
        if (courseClass == null) {
            throw new OperateException("\u73ed\u7ea7\u4e0d\u5b58\u5728");
        }
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        if (adminId != null && (agent = (Agent)this.agentMapper.selectOne(new QueryWrapper<Agent>().eq("admin_id", (Object)adminId).isNull("delete_time").last("limit 1"))) != null && courseClass.getAgentId() != null && !courseClass.getAgentId().equals(agent.getId())) {
            throw new OperateException("\u65e0\u6743\u64cd\u4f5c\u8be5\u73ed\u7ea7");
        }
        return courseClass;
    }

    private Integer parseInt(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        String text = value.toString().trim();
        if (text.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new com.mdd.common.exception.OperateException("参数格式错误，必须为整数");
        }
    }

    private BigDecimal parseAmount(Object value) {
        if (value == null || StringUtils.isEmpty((String)value.toString())) {
            return null;
        }
        return new BigDecimal(value.toString());
    }

    private String formatTime(Integer timestamp) {
        if (timestamp == null || timestamp <= 0) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp.longValue() * 1000L));
    }
}
