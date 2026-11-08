/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.admin.service.ICourseTempLessonService
 *  com.mdd.admin.service.impl.CourseTempLessonServiceImpl
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.admin.Admin
 *  com.mdd.common.entity.agent.Agent
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.CourseLesson
 *  com.mdd.common.entity.course.CourseTempLesson
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.admin.AdminMapper
 *  com.mdd.common.mapper.agent.AgentMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.CourseLessonMapper
 *  com.mdd.common.mapper.course.CourseTempLessonMapper
 *  com.mdd.common.util.ClassScheduleUtil
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.service.ICourseTempLessonService;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.admin.Admin;
import com.mdd.common.entity.agent.Agent;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseTempLesson;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.mapper.agent.AgentMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.CourseTempLessonMapper;
import com.mdd.common.util.ClassScheduleUtil;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseTempLessonServiceImpl
implements ICourseTempLessonService {
    private static final int LESSON_TYPE_TEMP = 1;
    private static final int STATUS_PENDING = 0;
    private static final int STATUS_COMPLETED = 1;
    private static final int STATUS_CANCELLED = 2;
    @Resource
    CourseTempLessonMapper courseTempLessonMapper;
    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    AdminMapper adminMapper;
    @Resource
    AgentMapper agentMapper;

    public List<Map<String, Object>> listByClass(Integer classId) {
        CourseClass courseClass = this.getAccessibleClass(classId);
        List<CourseTempLesson> records = this.courseTempLessonMapper.selectList(new QueryWrapper<CourseTempLesson>().eq("class_id", (Object)classId).orderByDesc("id"));
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CourseTempLesson item : records) {
            list.add(this.buildListItem(item, courseClass));
        }
        return list;
    }

    public Map<String, Object> detail(Integer id) {
        CourseTempLesson temp = (CourseTempLesson)this.courseTempLessonMapper.selectById((Serializable)id);
        if (temp == null) {
            throw new OperateException("\u8bb0\u5f55\u4e0d\u5b58\u5728");
        }
        CourseClass courseClass = this.getAccessibleClass(temp.getClassId());
        Map<String, Object> result = this.buildListItem(temp, courseClass);
        result.put("remark", temp.getRemark());
        result.put("lessonId", temp.getLessonId());
        result.put("lessonNo", temp.getLessonNo());
        return result;
    }

    @Transactional(rollbackFor={Exception.class})
    public void create(Integer adminId, Map<String, Object> params) {
        Integer classId = this.parseInt(params.get("classId"));
        String lessonDate = params.get("lessonDate") != null ? params.get("lessonDate").toString().trim() : "";
        String timeSlot = params.get("timeSlot") != null ? params.get("timeSlot").toString().trim() : "";
        Integer instructorId = this.parseInt(params.get("instructorId"));
        String venue = params.get("venue") != null ? params.get("venue").toString().trim() : "";
        String latitude = params.get("latitude") != null ? params.get("latitude").toString().trim() : "";
        String longitude = params.get("longitude") != null ? params.get("longitude").toString().trim() : "";
        String remark = params.get("remark") != null ? params.get("remark").toString().trim() : "";
        Integer sourceId = this.parseInt(params.get("sourceId"));
        if (classId <= 0) {
            throw new OperateException("\u73ed\u7ea7\u53c2\u6570\u9519\u8bef");
        }
        if (lessonDate.isEmpty()) {
            throw new OperateException("\u8bf7\u9009\u62e9\u4e0a\u8bfe\u65e5\u671f");
        }
        if (timeSlot.isEmpty()) {
            throw new OperateException("\u8bf7\u586b\u5199\u4e0a\u8bfe\u65f6\u6bb5");
        }
        if (ClassScheduleUtil.validateTimeSlot((String)timeSlot) != null) {
            throw new OperateException("\u4e0a\u8bfe\u65f6\u6bb5\u683c\u5f0f\u9519\u8bef\uff0c\u8bf7\u9009\u62e9\u5b8c\u6574\u7684\u5f00\u59cb\u548c\u7ed3\u675f\u65f6\u95f4");
        }
        if (instructorId <= 0) {
            throw new OperateException("\u8bf7\u9009\u62e9\u6388\u8bfe\u6559\u7ec3");
        }
        if (venue.isEmpty()) {
            throw new OperateException("\u8bf7\u586b\u5199\u4e0a\u8bfe\u5730\u70b9");
        }
        if (latitude.isEmpty() || longitude.isEmpty()) {
            throw new OperateException("\u8bf7\u5728\u5730\u56fe\u4e0a\u9009\u62e9\u4e0a\u8bfe\u5730\u70b9");
        }
        this.validateLessonDate(lessonDate);
        // 同一班级的课号分配必须串行；否则并发 max(lesson_no)+1 会选中同一个课号。
        if (this.courseClassMapper.selectByIdForUpdate(classId) == null) {
            throw new OperateException("\u73ed\u7ea7\u4e0d\u5b58\u5728");
        }
        CourseClass courseClass = this.getAccessibleClass(classId);
        if (courseClass.getStatus() == null || courseClass.getStatus() != 1) {
            throw new OperateException("\u4ec5\u8fdb\u884c\u4e2d\u7684\u73ed\u7ea7\u53ef\u53d1\u8d77\u4e34\u65f6\u52a0\u8bfe");
        }
        Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)instructorId);
        if (instructor == null || instructor.getDeleteTime() != null) {
            throw new OperateException("\u6240\u9009\u6559\u7ec3\u4e0d\u5b58\u5728");
        }
        Admin admin = (Admin)this.adminMapper.selectById((Serializable)adminId);
        int now = (int)(System.currentTimeMillis() / 1000L);
        int lessonNo = this.resolveNextLessonNo(classId);
        int weekDay = this.resolveWeekDay(lessonDate);
        CourseLesson lesson = new CourseLesson();
        lesson.setClassId(classId);
        lesson.setLessonNo(Integer.valueOf(lessonNo));
        lesson.setLessonType(Integer.valueOf(1));
        lesson.setLessonDate(lessonDate);
        lesson.setWeekDay(Integer.valueOf(weekDay));
        lesson.setTimeSlot(timeSlot);
        lesson.setVenue(venue);
        lesson.setLatitude(latitude);
        lesson.setLongitude(longitude);
        lesson.setRemark(remark);
        lesson.setOperatorId(Integer.valueOf(adminId != null ? adminId : 0));
        lesson.setOperatorName(admin != null ? admin.getName() : "\u7ba1\u7406\u5458");
        lesson.setSourceLessonId(Integer.valueOf(0));
        lesson.setCoachId(instructorId);
        lesson.setCoachCheckIn(Integer.valueOf(0));
        lesson.setStatus(Integer.valueOf(0));
        lesson.setCreateTime(Integer.valueOf(now));
        lesson.setUpdateTime(Integer.valueOf(now));
        this.courseLessonMapper.insert(lesson);
        CourseTempLesson temp = new CourseTempLesson();
        temp.setClassId(classId);
        temp.setLessonId(lesson.getId());
        temp.setLessonNo(Integer.valueOf(lessonNo));
        temp.setLessonDate(lessonDate);
        temp.setTimeSlot(timeSlot);
        temp.setVenue(venue);
        temp.setLatitude(latitude);
        temp.setLongitude(longitude);
        temp.setInstructorId(instructorId);
        temp.setInstructorName(instructor.getName() != null ? instructor.getName() : "");
        temp.setDefaultInstructorId(Integer.valueOf(courseClass.getInstructorId() != null ? courseClass.getInstructorId() : 0));
        temp.setRemark(remark);
        temp.setSourceId(Integer.valueOf(sourceId > 0 ? sourceId : 0));
        temp.setOperatorId(Integer.valueOf(adminId != null ? adminId : 0));
        temp.setOperatorName(admin != null ? admin.getName() : "\u7ba1\u7406\u5458");
        temp.setActiveKey(classId + ":" + lessonDate + ":" + timeSlot);
        temp.setStatus(Integer.valueOf(0));
        temp.setCreateTime(Integer.valueOf(now));
        temp.setUpdateTime(Integer.valueOf(now));
        try {
            this.courseTempLessonMapper.insert(temp);
        } catch (DuplicateKeyException duplicate) {
            throw new OperateException("\u8be5\u73ed\u7ea7\u540c\u65e5\u540c\u65f6\u6bb5\u5df2\u6709\u4e34\u65f6\u52a0\u8bfe\uff0c\u8bf7\u52ff\u91cd\u590d\u63d0\u4ea4");
        }
    }

    @Transactional(rollbackFor={Exception.class})
    public void cancel(Integer adminId, Integer id) {
        CourseLesson lesson;
        CourseTempLesson temp = (CourseTempLesson)this.courseTempLessonMapper.selectById((Serializable)id);
        if (temp == null) {
            throw new OperateException("\u8bb0\u5f55\u4e0d\u5b58\u5728");
        }
        this.getAccessibleClass(temp.getClassId());
        if (temp.getStatus() == null || temp.getStatus() != 0) {
            throw new OperateException("\u4ec5\u5f85\u4e0a\u8bfe\u7684\u4e34\u65f6\u52a0\u8bfe\u53ef\u53d6\u6d88");
        }
        int now = (int)(System.currentTimeMillis() / 1000L);
        temp.setStatus(Integer.valueOf(2));
        temp.setActiveKey(null);
        temp.setUpdateTime(Integer.valueOf(now));
        this.courseTempLessonMapper.updateById(temp);
        if (temp.getLessonId() != null && temp.getLessonId() > 0 && (lesson = (CourseLesson)this.courseLessonMapper.selectById((Serializable)temp.getLessonId())) != null && lesson.getStatus() != null && lesson.getStatus() == 0) {
            lesson.setStatus(Integer.valueOf(2));
            lesson.setUpdateTime(Integer.valueOf(now));
            this.courseLessonMapper.updateById(lesson);
        }
    }

    private Map<String, Object> buildListItem(CourseTempLesson item, CourseClass courseClass) {
        LinkedHashMap<String, Object> row = new LinkedHashMap<String, Object>();
        row.put("id", item.getId());
        row.put("classId", item.getClassId());
        row.put("className", courseClass.getName());
        row.put("lessonId", item.getLessonId());
        row.put("lessonNo", item.getLessonNo());
        row.put("lessonDate", item.getLessonDate());
        row.put("timeSlot", item.getTimeSlot());
        row.put("instructorId", item.getInstructorId());
        row.put("instructorName", item.getInstructorName());
        row.put("defaultInstructorId", item.getDefaultInstructorId());
        row.put("isSubstitute", item.getDefaultInstructorId() != null && item.getInstructorId() != null && !item.getDefaultInstructorId().equals(item.getInstructorId()));
        row.put("venue", item.getVenue());
        row.put("latitude", item.getLatitude());
        row.put("longitude", item.getLongitude());
        row.put("remark", item.getRemark());
        row.put("status", item.getStatus());
        row.put("statusText", this.resolveStatusText(item.getStatus()));
        row.put("operatorName", item.getOperatorName());
        row.put("createTime", this.formatTime(item.getCreateTime()));
        return row;
    }

    private String resolveStatusText(Integer status) {
        if (status == null) {
            return "\u672a\u77e5";
        }
        if (status == 0) {
            return "\u5f85\u4e0a\u8bfe";
        }
        if (status == 1) {
            return "\u5df2\u7ed3\u8bfe";
        }
        if (status == 2) {
            return "\u5df2\u53d6\u6d88";
        }
        return "\u672a\u77e5";
    }

    private int resolveNextLessonNo(Integer classId) {
        CourseLesson maxLesson = (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>().eq("class_id", (Object)classId).orderByDesc("lesson_no").last("limit 1"));
        if (maxLesson == null || maxLesson.getLessonNo() == null) {
            return 1;
        }
        return maxLesson.getLessonNo() + 1;
    }

    private int resolveWeekDay(String lessonDate) {
        try {
            LocalDate date = LocalDate.parse(lessonDate, DateTimeFormatter.ISO_LOCAL_DATE);
            return date.getDayOfWeek().getValue();
        }
        catch (DateTimeParseException e) {
            return 0;
        }
    }

    private void validateLessonDate(String lessonDate) {
        try {
            LocalDate.parse(lessonDate, DateTimeFormatter.ISO_LOCAL_DATE);
        }
        catch (DateTimeParseException e) {
            throw new OperateException("\u4e0a\u8bfe\u65e5\u671f\u683c\u5f0f\u9519\u8bef");
        }
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

    private String formatTime(Integer timestamp) {
        if (timestamp == null || timestamp <= 0) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp.longValue() * 1000L));
    }
}
