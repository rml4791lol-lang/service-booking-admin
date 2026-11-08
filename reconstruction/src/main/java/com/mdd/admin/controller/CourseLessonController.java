/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.controller.CourseLessonController
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.course.CourseBooking
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.CourseConsumeLog
 *  com.mdd.common.entity.course.CourseLesson
 *  com.mdd.common.entity.course.CourseLessonLog
 *  com.mdd.common.entity.course.UserCourse
 *  com.mdd.common.entity.student.Student
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.course.CourseBookingMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.CourseConsumeLogMapper
 *  com.mdd.common.mapper.course.CourseLessonLogMapper
 *  com.mdd.common.mapper.course.CourseLessonMapper
 *  com.mdd.common.mapper.course.UserCourseMapper
 *  com.mdd.common.mapper.student.StudentMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.util.UrlUtils
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.aop.Log;
import com.mdd.admin.validate.course.CourseBookingCancelValidate;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseConsumeLog;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseLessonLog;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.student.Student;
import com.mdd.common.entity.user.User;
import com.mdd.common.util.PageUtils;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseConsumeLogMapper;
import com.mdd.common.mapper.course.CourseLessonLogMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.UrlUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/course_lesson"})
@Api(tags={"\u8bfe\u7a0b\u8bfe\u65f6\u7ba1\u7406"})
public class CourseLessonController {
    @Resource
    private CourseLessonLogMapper courseLessonLogMapper;
    @Resource
    private InstructorMapper instructorMapper;
    @Resource
    private CourseClassMapper courseClassMapper;
    @Resource
    private CourseLessonMapper courseLessonMapper;
    @Resource
    private CourseBookingMapper courseBookingMapper;
    @Resource
    private CourseConsumeLogMapper courseConsumeLogMapper;
    @Resource
    private UserCourseMapper userCourseMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private StudentMapper studentMapper;

    @GetMapping(value={"/list"})
    @ApiOperation(value="\u8bfe\u5802\u603b\u7ed3\u5217\u8868")
    public AjaxResult<Object> list(@RequestParam(value="page_no", defaultValue="1") Integer pageNo, @RequestParam(value="page_size", defaultValue="20") Integer pageSize, @RequestParam(value="courseClassId", required=false) Integer courseClassId, @RequestParam(value="instructorId", required=false) Integer instructorId, @RequestParam(value="keyword", required=false) String keyword) {
        pageNo = PageUtils.pageNo(pageNo);
        pageSize = PageUtils.pageSize(pageSize);
        IPage<CourseLessonLog> page = new Page<CourseLessonLog>((long)pageNo.intValue(), (long)pageSize.intValue());
        QueryWrapper<CourseLessonLog> queryWrapper = new QueryWrapper<CourseLessonLog>();
        if (courseClassId != null && courseClassId > 0) {
            queryWrapper.eq("course_class_id", (Object)courseClassId);
        }
        if (instructorId != null && instructorId > 0) {
            queryWrapper.eq("instructor_id", (Object)instructorId);
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.like("summary", (Object)keyword.trim());
        }
        queryWrapper.orderByDesc("id");
        page = this.courseLessonLogMapper.selectPage((IPage)page, (Wrapper)queryWrapper);
        // 课节维度：按班级+课次补上课日期/时段/地点/状态，便于后台按课节核对总结。
        java.util.Set<Integer> logClassIds = new java.util.HashSet<Integer>();
        java.util.Set<Integer> logLessonNos = new java.util.HashSet<Integer>();
        for (CourseLessonLog log : page.getRecords()) {
            if (log.getCourseClassId() != null) {
                logClassIds.add(log.getCourseClassId());
            }
            if (log.getLessonNo() != null) {
                logLessonNos.add(log.getLessonNo());
            }
        }
        Map<String, CourseLesson> lessonMap = new java.util.HashMap<String, CourseLesson>();
        if (!logClassIds.isEmpty() && !logLessonNos.isEmpty()) {
            List<CourseLesson> lessons = this.courseLessonMapper.selectList(new QueryWrapper<CourseLesson>()
                    .in("class_id", logClassIds)
                    .in("lesson_no", logLessonNos));
            for (CourseLesson lesson : lessons) {
                lessonMap.put(lesson.getClassId() + "_" + lesson.getLessonNo(), lesson);
            }
        }
        // 任课教练名（#223）：按 coach_id 批量取，避免逐行查库。
        java.util.Set<Integer> coachIdSet = new java.util.HashSet<Integer>();
        for (CourseLessonLog log : page.getRecords()) {
            CourseLesson lessonItem = lessonMap.get(log.getCourseClassId() + "_" + log.getLessonNo());
            Integer rowCoachId = resolveLessonCoachId(lessonItem, log.getInstructorId());
            if (rowCoachId != null && rowCoachId > 0) {
                coachIdSet.add(rowCoachId);
            }
        }
        Map<Integer, String> coachNameMap = loadInstructorNames(coachIdSet);
        ArrayList list = new ArrayList();
        for (CourseLessonLog log : page.getRecords()) {
            Instructor instructor;
            CourseClass courseClass;
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", log.getId());
            item.put("courseClassId", log.getCourseClassId());
            String courseName = "";
            if (log.getCourseClassId() != null && (courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)log.getCourseClassId())) != null) {
                courseName = courseClass.getName();
            }
            item.put("courseName", courseName);
            item.put("lessonNo", log.getLessonNo());
            item.put("summary", log.getSummary());
            item.put("images", String.join((CharSequence)",", this.normalizeImages(log.getImages())));
            item.put("createTime", this.formatTimestamp(log.getCreateTime()));
            CourseLesson lesson = lessonMap.get(log.getCourseClassId() + "_" + log.getLessonNo());
            item.put("lessonDate", lesson != null && lesson.getLessonDate() != null ? lesson.getLessonDate() : "");
            item.put("timeSlot", lesson != null && lesson.getTimeSlot() != null ? lesson.getTimeSlot() : "");
            item.put("venue", lesson != null && lesson.getVenue() != null ? lesson.getVenue() : "");
            item.put("lessonStatus", lesson != null && lesson.getStatus() != null ? lesson.getStatus() : -1);
            item.put("coachCheckIn", lesson != null && lesson.getCoachCheckIn() != null ? lesson.getCoachCheckIn() : 0);
            String instructorName = "\u672a\u77e5";
            if (log.getInstructorId() != null && (instructor = (Instructor)this.instructorMapper.selectById((Serializable)log.getInstructorId())) != null) {
                instructorName = instructor.getName();
            }
            item.put("instructorName", instructorName);
            // 任课教练（#223）：coach_id 为任课教练唯一来源，历史行（无 lesson/coach_id=0）回退总结教练
            CourseLesson lessonForCoach = lessonMap.get(log.getCourseClassId() + "_" + log.getLessonNo());
            Integer rowCoachId = this.resolveLessonCoachId(lessonForCoach, log.getInstructorId());
            item.put("coachId", rowCoachId == null ? 0 : rowCoachId);
            item.put("coachName", rowCoachId != null && rowCoachId > 0 ? coachNameMap.get(rowCoachId) : "");
            list.add(item);
        }
        LinkedHashMap<String, Serializable> result = new LinkedHashMap<String, Serializable>();
        result.put("count", Long.valueOf(page.getTotal()));
        result.put("lists", list);
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/lessons"})
    @ApiOperation(value="\u8bfe\u65f6\u5217\u8868")
    public AjaxResult<Object> lessons(@RequestParam(value="classId") Integer classId, @RequestParam(value="page_no", defaultValue="1") Integer pageNo, @RequestParam(value="page_size", defaultValue="50") Integer pageSize, @RequestParam(value="status", required=false) Integer status) {
        pageNo = PageUtils.pageNo(pageNo);
        pageSize = PageUtils.pageSize(pageSize);
        IPage<CourseLesson> page = new Page<CourseLesson>((long)pageNo.intValue(), (long)pageSize.intValue());
        QueryWrapper<CourseLessonLog> queryWrapper = new QueryWrapper<CourseLessonLog>();
        queryWrapper.eq("class_id", (Object)classId);
        if (status != null) {
            queryWrapper.eq("status", (Object)status);
        }
        queryWrapper.orderByAsc("lesson_no");
        page = this.courseLessonMapper.selectPage((IPage)page, (Wrapper)queryWrapper);
        String className = "";
        CourseClass courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)classId);
        if (courseClass != null) {
            className = courseClass.getName();
        }
        // 任课教练（#223）：coach_id 唯一来源；defaultCoachId=开班教练，供前端打「默认」标签
        Integer classInstructorId = courseClass != null && courseClass.getInstructorId() != null ? courseClass.getInstructorId() : 0;
        java.util.Set<Integer> lessonCoachIds = new java.util.HashSet<Integer>();
        for (CourseLesson lesson : page.getRecords()) {
            if (lesson.getCoachId() != null && lesson.getCoachId() > 0) {
                lessonCoachIds.add(lesson.getCoachId());
            }
        }
        if (classInstructorId > 0) {
            lessonCoachIds.add(classInstructorId);
        }
        Map<Integer, String> lessonCoachNames = this.loadInstructorNames(lessonCoachIds);
        List<CourseLessonLog> logs = this.courseLessonLogMapper.selectList(new QueryWrapper<CourseLessonLog>().eq("course_class_id", (Object)classId).isNull("delete_time"));
        HashSet<Integer> loggedLessonNos = new HashSet<Integer>();
        for (CourseLessonLog log : logs) {
            loggedLessonNos.add(log.getLessonNo());
        }
        ArrayList list = new ArrayList();
        String[] statusLabels = new String[]{"\u5f85\u4e0a\u8bfe", "\u5df2\u7ed3\u8bfe", "\u5df2\u53d6\u6d88"};
        String[] weekLabels = new String[]{"", "\u5468\u4e00", "\u5468\u4e8c", "\u5468\u4e09", "\u5468\u56db", "\u5468\u4e94", "\u5468\u516d", "\u5468\u65e5"};
        for (CourseLesson lesson : page.getRecords()) {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", lesson.getId());
            item.put("lessonNo", lesson.getLessonNo());
            item.put("lessonType", lesson.getLessonType() != null ? lesson.getLessonType() : 0);
            item.put("lessonTypeLabel", lesson.getLessonType() != null && lesson.getLessonType() == 1 ? "\u4e34\u65f6\u52a0\u8bfe" : "\u6b63\u8bfe");
            item.put("lessonDate", lesson.getLessonDate());
            item.put("weekDay", lesson.getWeekDay() != null && lesson.getWeekDay() >= 1 && lesson.getWeekDay() <= 7 ? weekLabels[lesson.getWeekDay()] : "");
            item.put("timeSlot", lesson.getTimeSlot());
            item.put("venue", lesson.getVenue() != null ? lesson.getVenue() : "");
            item.put("latitude", lesson.getLatitude() != null ? lesson.getLatitude() : "");
            item.put("longitude", lesson.getLongitude() != null ? lesson.getLongitude() : "");
            item.put("remark", lesson.getRemark() != null ? lesson.getRemark() : "");
            item.put("status", lesson.getStatus());
            item.put("statusLabel", lesson.getStatus() != null && lesson.getStatus() >= 0 && lesson.getStatus() < statusLabels.length ? statusLabels[lesson.getStatus()] : "\u672a\u77e5");
            item.put("coachCheckIn", lesson.getCoachCheckIn());
            item.put("coachId", lesson.getCoachId() != null && lesson.getCoachId() > 0 ? lesson.getCoachId() : 0);
            item.put("defaultCoachId", classInstructorId);
            item.put("coachName", this.resolveCoachName(lesson.getCoachId(), classInstructorId, lessonCoachNames));
            item.put("hasLog", loggedLessonNos.contains(lesson.getLessonNo()));
            list.add(item);
        }
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("count", page.getTotal());
        result.put("lists", list);
        result.put("className", className);
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/detail"})
    @ApiOperation(value="\u8bfe\u65f6\u8be6\u60c5")
    public AjaxResult<Object> detail(@RequestParam(value="lessonId") Integer lessonId) {
        CourseLesson lesson = (CourseLesson)this.courseLessonMapper.selectById((Serializable)lessonId);
        if (lesson == null) {
            return AjaxResult.failed((String)"\u8bfe\u65f6\u4e0d\u5b58\u5728");
        }
        CourseClass courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)lesson.getClassId());
        Integer instructorId = lesson.getCoachId();
        if ((instructorId == null || instructorId <= 0) && courseClass != null) {
            instructorId = courseClass.getInstructorId();
        }
        Instructor instructor = instructorId != null && instructorId > 0 ? (Instructor)this.instructorMapper.selectById((Serializable)instructorId) : null;
        CourseLessonLog lessonLog = (CourseLessonLog)this.courseLessonLogMapper.selectOne(new QueryWrapper<CourseLessonLog>().eq("course_class_id", (Object)lesson.getClassId()).eq("lesson_no", (Object)lesson.getLessonNo()).isNull("delete_time").orderByDesc("id").last("limit 1"));
        List<CourseBooking> bookingEntities = this.loadLessonBookings(lesson);
        Map<String, Map<String, Object>> participantMap = this.buildParticipantMap(lesson, bookingEntities);
        ArrayList<Map> bookings = new ArrayList(participantMap.values());
        ArrayList<Map> attendees = new ArrayList<Map>();
        ArrayList rollCalls = new ArrayList();
        int bookedCount = 0;
        int attendedCount = 0;
        int cancelledCount = 0;
        for (Map item : bookings) {
            Integer bookingStatus = this.parseStatus(item.get("status"));
            if (bookingStatus == 3) {
                ++cancelledCount;
            } else {
                ++bookedCount;
            }
            if (bookingStatus == 10) {
                ++attendedCount;
                attendees.add(item);
            }
            LinkedHashMap<String, String> rollCallItem = new LinkedHashMap<String, String>(item);
            rollCallItem.put("rollCallStatus", bookingStatus == 10 ? "\u5df2\u70b9\u540d" : (bookingStatus == 3 ? "\u5df2\u53d6\u6d88" : "\u672a\u70b9\u540d"));
            rollCalls.add(rollCallItem);
        }
        LinkedHashMap<String, Object> lessonInfo = new LinkedHashMap<String, Object>();
        lessonInfo.put("lessonId", lesson.getId());
        lessonInfo.put("classId", lesson.getClassId());
        lessonInfo.put("className", courseClass != null ? courseClass.getName() : "");
        lessonInfo.put("lessonNo", lesson.getLessonNo());
        lessonInfo.put("lessonType", lesson.getLessonType() != null ? lesson.getLessonType() : 0);
        lessonInfo.put("lessonTypeLabel", lesson.getLessonType() != null && lesson.getLessonType() == 1 ? "\u4e34\u65f6\u52a0\u8bfe" : "\u6b63\u8bfe");
        lessonInfo.put("lessonDate", lesson.getLessonDate());
        lessonInfo.put("timeSlot", lesson.getTimeSlot());
        lessonInfo.put("weekDay", lesson.getWeekDay());
        lessonInfo.put("status", lesson.getStatus());
        lessonInfo.put("statusLabel", this.convertLessonStatus(lesson.getStatus()));
        lessonInfo.put("coachCheckIn", lesson.getCoachCheckIn());
        lessonInfo.put("coachCheckInLabel", lesson.getCoachCheckIn() != null && lesson.getCoachCheckIn() == 1 ? "\u5df2\u7b7e\u5230" : "\u672a\u7b7e\u5230");
        String venue = lesson.getVenue();
        if (venue == null || venue.isEmpty()) {
            venue = courseClass != null ? courseClass.getVenue() : "";
        }
        lessonInfo.put("venue", venue);
        lessonInfo.put("latitude", lesson.getLatitude() != null ? lesson.getLatitude() : "");
        lessonInfo.put("longitude", lesson.getLongitude() != null ? lesson.getLongitude() : "");
        lessonInfo.put("remark", lesson.getRemark() != null ? lesson.getRemark() : "");
        LinkedHashMap<String, Object> teacherInfo = new LinkedHashMap<String, Object>();
        teacherInfo.put("id", instructor != null ? instructor.getId() : instructorId);
        teacherInfo.put("name", instructor != null ? instructor.getName() : (courseClass != null ? courseClass.getInstructorName() : ""));
        teacherInfo.put("phone", instructor != null ? instructor.getPhone() : "");
        teacherInfo.put("avatar", instructor != null ? UrlUtils.toAdminAbsoluteUrl((String)instructor.getAvatar()) : "");
        teacherInfo.put("rating", instructor != null ? instructor.getRatingScore() : null);
        teacherInfo.put("specialty", instructor != null ? instructor.getSpecialty() : "");
        teacherInfo.put("coachCheckIn", lesson.getCoachCheckIn());
        teacherInfo.put("coachCheckInLabel", lesson.getCoachCheckIn() != null && lesson.getCoachCheckIn() == 1 ? "\u5df2\u7b7e\u5230" : "\u672a\u7b7e\u5230");
        LinkedHashMap<String, Object> lessonLogInfo = new LinkedHashMap<String, Object>();
        if (lessonLog != null) {
            lessonLogInfo.put("id", lessonLog.getId());
            lessonLogInfo.put("summary", lessonLog.getSummary());
            lessonLogInfo.put("images", this.normalizeImages(lessonLog.getImages()));
            lessonLogInfo.put("createTime", this.formatTimestamp(lessonLog.getCreateTime()));
        } else {
            lessonLogInfo.put("summary", "");
            lessonLogInfo.put("images", new ArrayList());
            lessonLogInfo.put("createTime", "");
        }
        LinkedHashMap<String, Integer> stats = new LinkedHashMap<String, Integer>();
        stats.put("bookedCount", bookedCount);
        stats.put("attendedCount", attendedCount);
        stats.put("notAttendedCount", Math.max(bookedCount - attendedCount, 0));
        stats.put("cancelledCount", cancelledCount);
        LinkedHashMap<String, Cloneable> result = new LinkedHashMap<String, Cloneable>();
        result.put("lessonInfo", lessonInfo);
        result.put("teacherInfo", teacherInfo);
        result.put("lessonLog", lessonLogInfo);
        result.put("stats", stats);
        result.put("bookings", bookings);
        result.put("attendees", attendees);
        result.put("rollCalls", rollCalls);
        result.put("healthRecords", new ArrayList());
        return AjaxResult.success(result);
    }

    /**
     * 后台取消预约（#213 C1）：课节名单里的单条预约置为已取消（status=3）。
     *
     * <p>口径：</p>
     * <ul>
     *   <li>只能取消 status=1（已预约）的记录；已上课（status=10）拒绝；</li>
     *   <li>已取消（status=3）幂等：直接返回成功，不重复改库；</li>
     *   <li>状态用条件更新（id + status=1）落 3，并发下不会覆盖已上课记录；</li>
     *   <li>名额是动态统计（status&lt;&gt;3），不修改课程表/消课流水；</li>
     *   <li>remark 仅随操作日志留痕（预约表无备注列）。</li>
     * </ul>
     */
    @PostMapping(value={"/cancelBooking"})
    @ApiOperation(value="\u540e\u53f0\u53d6\u6d88\u9884\u7ea6")
    @Log(title="后台取消预约")
    public AjaxResult<Object> cancelBooking(@Validated @RequestBody CourseBookingCancelValidate cancelValidate) {
        CourseBooking booking = (CourseBooking)this.courseBookingMapper.selectById((Serializable)cancelValidate.getId());
        if (booking == null) {
            throw new OperateException("\u9884\u7ea6\u8bb0\u5f55\u4e0d\u5b58\u5728");
        }
        int status = booking.getStatus() == null ? 0 : booking.getStatus().intValue();
        if (status == 10) {
            throw new OperateException("\u5b66\u5458\u5df2\u4e0a\u8bfe\uff0c\u4e0d\u80fd\u53d6\u6d88\u9884\u7ea6");
        }
        if (status == 3) {
            return AjaxResult.success(this.cancelBookingResult(booking.getId(), cancelValidate.getRemark()));
        }
        if (status != 1) {
            throw new OperateException("\u5f53\u524d\u9884\u7ea6\u72b6\u6001\u4e3a\u300c" + this.convertBookingStatus(Integer.valueOf(status)) + "\u300d\uff0c\u4e0d\u80fd\u53d6\u6d88");
        }

        long now = System.currentTimeMillis() / 1000L;
        int affected = this.courseBookingMapper.update(null, new UpdateWrapper<CourseBooking>()
                .eq("id", booking.getId())
                .eq("status", 1)
                .set("status", 3)
                .set("update_time", now));
        if (affected <= 0) {
            // 并发：家长/其他管理员先一步取消 → 幂等成功；先一步上课 → 拒绝。
            CourseBooking latest = (CourseBooking)this.courseBookingMapper.selectById((Serializable)booking.getId());
            if (latest != null && latest.getStatus() != null && latest.getStatus().intValue() == 3) {
                return AjaxResult.success(this.cancelBookingResult(booking.getId(), cancelValidate.getRemark()));
            }
            throw new OperateException("\u9884\u7ea6\u72b6\u6001\u5df2\u53d8\u5316\uff0c\u8bf7\u5237\u65b0\u540e\u91cd\u8bd5");
        }
        return AjaxResult.success(this.cancelBookingResult(booking.getId(), cancelValidate.getRemark()));
    }

    /**
     * 设置课节任课教练（#223）：ys_course_lesson.coach_id 是任课教练唯一来源，
     * 默认=开班教练；教练端课表/核销按 coach_id 过滤，改派后课次自动跟随任课教练。
     *
     * <p>口径：</p>
     * <ul>
     *   <li>课节必须存在；教练必须存在且未删除；</li>
     *   <li>教练须已开通登录账号（关联用户且已设密码），停用/禁用账号明确拒绝；</li>
     *   <li>落库为按 id 的条件 UPDATE，单条语句原子生效，重复设置同教练幂等成功；</li>
     *   <li>已开始/已核销课次不拦：保留改派能力，历史流水口径不变（结算按实际操作落账）。</li>
     * </ul>
     */
    @PostMapping(value={"/setCoach"})
    @ApiOperation(value="设置课节任课教练")
    @Log(title="设置课节任课教练")
    public AjaxResult<Object> setCoach(@RequestBody Map<String, Object> params) {
        Map<String, Object> body = params != null ? params : new java.util.HashMap<String, Object>();
        Integer lessonId = this.parseIdParam(body.get("lessonId"));
        Integer coachId = this.parseIdParam(body.get("coachId"));
        if (lessonId <= 0) {
            throw new OperateException("课节参数错误");
        }
        if (coachId <= 0) {
            throw new OperateException("教练参数错误");
        }
        CourseLesson lesson = (CourseLesson)this.courseLessonMapper.selectById((Serializable)lessonId);
        if (lesson == null) {
            throw new OperateException("课节不存在");
        }
        Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)coachId);
        if (instructor == null || instructor.getDeleteTime() != null) {
            throw new OperateException("所选教练不存在");
        }
        this.assertCoachAccountAvailable(instructor);
        long now = System.currentTimeMillis() / 1000L;
        int affected = this.courseLessonMapper.update(null, new UpdateWrapper<CourseLesson>()
                .eq("id", lessonId)
                .set("coach_id", coachId)
                .set("update_time", now));
        if (affected <= 0) {
            throw new OperateException("课节状态已变化，请刷新后重试");
        }
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("lessonId", lessonId);
        result.put("coachId", coachId);
        result.put("coachName", instructor.getName() != null ? instructor.getName() : "");
        result.put("lessonDate", lesson.getLessonDate());
        result.put("lessonNo", lesson.getLessonNo());
        return AjaxResult.success(result);
    }

    /** 教练须已开通登录账号（对齐教练账号管理口径）：未关联用户/未设密码=未开通，停用/禁用拒绝。 */
    private void assertCoachAccountAvailable(Instructor instructor) {
        if (instructor.getStatus() != null && instructor.getStatus() == 0) {
            throw new OperateException("教练账号已停用，不能设为任课教练");
        }
        User user = instructor.getUserId() != null && instructor.getUserId() > 0
                ? (User)this.userMapper.selectById((Serializable)instructor.getUserId()) : null;
        if (user == null || user.getDeleteTime() != null
                || instructor.getPassword() == null || instructor.getPassword().isEmpty()) {
            throw new OperateException("教练账号未开通，请先在「教练账号管理」开通后再设置");
        }
        if (user.getIsDisable() != null && user.getIsDisable() == 1) {
            throw new OperateException("教练登录账号已被禁用，不能设为任课教练");
        }
    }

    private Integer parseIdParam(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        try {
            return Integer.parseInt(value.toString().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /** 任课教练回退口径：lesson.coach_id>0 优先，否则回退到总结/开班教练 id（历史行）。 */
    private Integer resolveLessonCoachId(CourseLesson lesson, Integer fallbackCoachId) {
        if (lesson != null && lesson.getCoachId() != null && lesson.getCoachId() > 0) {
            return lesson.getCoachId();
        }
        return fallbackCoachId != null ? fallbackCoachId : 0;
    }

    private Map<Integer, String> loadInstructorNames(java.util.Set<Integer> instructorIds) {
        Map<Integer, String> nameMap = new java.util.HashMap<Integer, String>();
        if (instructorIds == null || instructorIds.isEmpty()) {
            return nameMap;
        }
        for (Instructor instructor : this.instructorMapper.selectBatchIds(instructorIds)) {
            nameMap.put(instructor.getId(), instructor.getName() != null ? instructor.getName() : "");
        }
        return nameMap;
    }

    private String resolveCoachName(Integer coachId, Integer defaultCoachId, Map<Integer, String> nameMap) {
        Integer effectiveId = coachId != null && coachId > 0 ? coachId : defaultCoachId;
        if (effectiveId == null || effectiveId <= 0) {
            return "";
        }
        String name = nameMap.get(effectiveId);
        return name != null ? name : "";
    }

    private LinkedHashMap<String, Object> cancelBookingResult(Integer bookingId, String remark) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("id", bookingId);
        result.put("status", 3);
        result.put("statusLabel", "\u5df2\u53d6\u6d88");
        result.put("remark", remark == null ? "" : remark.trim());
        return result;
    }

    private List<CourseBooking> loadLessonBookings(CourseLesson lesson) {
        List<CourseBooking> bookings = this.courseBookingMapper.selectList(new QueryWrapper<CourseBooking>().eq("course_class_id", (Object)lesson.getClassId()).eq("lesson_date", (Object)lesson.getLessonDate()).orderByAsc("id"));
        ArrayList<CourseBooking> result = new ArrayList<CourseBooking>();
        for (CourseBooking booking : bookings) {
            if (!this.isSameLessonBooking(booking, lesson)) continue;
            result.add(booking);
        }
        return result;
    }

    private boolean isSameLessonBooking(CourseBooking booking, CourseLesson lesson) {
        boolean hasLessonNo;
        boolean hasLessonId = booking.getLessonId() != null && booking.getLessonId() > 0;
        boolean bl = hasLessonNo = booking.getLessonNo() != null && booking.getLessonNo() > 0;
        if (!hasLessonId && !hasLessonNo) {
            return true;
        }
        if (hasLessonId && booking.getLessonId().equals(lesson.getId())) {
            return true;
        }
        return hasLessonNo && booking.getLessonNo().equals(lesson.getLessonNo());
    }

    private Map<String, Map<String, Object>> buildParticipantMap(CourseLesson lesson, List<CourseBooking> bookingEntities) {
        LinkedHashMap<String, Map<String, Object>> participantMap = new LinkedHashMap<String, Map<String, Object>>();
        for (CourseBooking booking : bookingEntities) {
            List<Integer> studentIds = this.resolveBookingStudentIds(booking, lesson.getClassId());
            if (studentIds.isEmpty()) {
                this.putParticipant(participantMap, this.buildParticipantItem(booking.getId(), null, booking.getUserId(), this.normalizeStudentId(booking.getStudentId()), lesson.getLessonDate(), lesson.getLessonNo(), booking.getStatus(), booking.getCreateTime(), booking.getUpdateTime()));
                continue;
            }
            for (Integer studentId : studentIds) {
                this.putParticipant(participantMap, this.buildParticipantItem(booking.getId(), null, booking.getUserId(), studentId, lesson.getLessonDate(), lesson.getLessonNo(), booking.getStatus(), booking.getCreateTime(), booking.getUpdateTime()));
            }
        }
        List<CourseConsumeLog> consumeLogs = this.courseConsumeLogMapper.selectList(new QueryWrapper<CourseConsumeLog>().eq("course_class_id", (Object)lesson.getClassId()).eq("lesson_no", (Object)lesson.getLessonNo()).orderByAsc("id"));
        for (CourseConsumeLog log : consumeLogs) {
            String key = this.participantKey(log.getUserId(), log.getStudentId());
            if (participantMap.containsKey(key)) {
                Map<String, Object> item = participantMap.get(key);
                if (this.parseStatus(item.get("status")) == 3) continue;
                item.put("consumeLogId", log.getId());
                item.put("status", 10);
                item.put("statusLabel", this.convertBookingStatus(Integer.valueOf(10)));
                item.put("updateTime", this.formatLongTimestamp(log.getCreateTime()));
                continue;
            }
            this.putParticipant(participantMap, this.buildParticipantItem(null, log.getId(), log.getUserId(), this.normalizeStudentId(log.getStudentId()), lesson.getLessonDate(), lesson.getLessonNo(), Integer.valueOf(10), log.getCreateTime(), log.getCreateTime()));
        }
        return participantMap;
    }

    private List<Integer> resolveBookingStudentIds(CourseBooking booking, Integer classId) {
        ArrayList<Integer> studentIds = new ArrayList<Integer>();
        Integer bookingStudentId = this.normalizeStudentId(booking.getStudentId());
        if (booking.getStatus() != null && booking.getStatus() == 3) {
            if (bookingStudentId > 0) {
                studentIds.add(bookingStudentId);
            }
            return studentIds;
        }
        List<Integer> enrolledStudentIds = this.loadEnrolledStudentIds(booking.getUserId(), classId);
        if (!enrolledStudentIds.isEmpty()) {
            studentIds.addAll(enrolledStudentIds);
            if (bookingStudentId > 0 && !studentIds.contains(bookingStudentId)) {
                studentIds.add(bookingStudentId);
            }
            return studentIds;
        }
        if (bookingStudentId > 0) {
            studentIds.add(bookingStudentId);
        }
        return studentIds;
    }

    private List<Integer> loadEnrolledStudentIds(Integer userId, Integer classId) {
        ArrayList<Integer> studentIds = new ArrayList<Integer>();
        if (userId == null || userId <= 0 || classId == null || classId <= 0) {
            return studentIds;
        }
        List<UserCourse> userCourses = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).eq("course_class_id", (Object)classId).isNull("delete_time").orderByAsc("id"));
        HashSet<Integer> seen = new HashSet<Integer>();
        for (UserCourse userCourse : userCourses) {
            Integer studentId = this.normalizeStudentId(userCourse.getStudentId());
            if (studentId <= 0 || !seen.add(studentId)) continue;
            studentIds.add(studentId);
        }
        return studentIds;
    }

    private void putParticipant(Map<String, Map<String, Object>> participantMap, Map<String, Object> item) {
        participantMap.put(this.participantKey((Integer)item.get("userId"), (Integer)item.get("studentId")), item);
    }

    private String participantKey(Integer userId, Integer studentId) {
        return String.valueOf(userId != null ? userId : 0) + ":" + this.normalizeStudentId(studentId);
    }

    private Integer normalizeStudentId(Integer studentId) {
        return studentId != null && studentId > 0 ? studentId : 0;
    }

    private Integer parseStatus(Object status) {
        if (status instanceof Number) {
            return ((Number)status).intValue();
        }
        return 0;
    }

    private Map<String, Object> buildParticipantItem(Integer bookingId, Integer consumeLogId, Integer userId, Integer studentId, String lessonDate, Integer lessonNo, Integer status, Long createTime, Long updateTime) {
        User user = userId != null && userId > 0 ? (User)this.userMapper.selectById((Serializable)userId) : null;
        Student student = studentId != null && studentId > 0 ? (Student)this.studentMapper.selectById((Serializable)studentId) : null;
        String displayName = "";
        if (student != null && student.getName() != null && !student.getName().isEmpty()) {
            displayName = student.getName();
        } else if (user != null && user.getNickname() != null) {
            displayName = user.getNickname();
        }
        LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
        item.put("id", bookingId);
        item.put("bookingId", bookingId);
        item.put("consumeLogId", consumeLogId);
        item.put("userId", userId);
        item.put("studentId", this.normalizeStudentId(studentId));
        item.put("displayName", displayName);
        item.put("studentName", student != null ? student.getName() : "");
        item.put("parentName", user != null ? user.getNickname() : "");
        item.put("realName", user != null ? user.getRealName() : "");
        item.put("mobile", user != null ? user.getMobile() : "");
        item.put("avatar", user != null ? UrlUtils.toAdminAbsoluteUrl((String)user.getAvatar()) : "");
        item.put("studentAvatar", student != null ? UrlUtils.toAdminAbsoluteUrl((String)student.getAvatar()) : "");
        item.put("lessonDate", lessonDate);
        item.put("lessonNo", lessonNo);
        item.put("status", status);
        item.put("statusLabel", this.convertBookingStatus(status));
        item.put("createTime", this.formatLongTimestamp(createTime));
        item.put("updateTime", this.formatLongTimestamp(updateTime));
        return item;
    }

    private List<String> normalizeImages(String images) {
        String[] imageArray;
        ArrayList<String> result = new ArrayList<String>();
        if (images == null || images.trim().isEmpty()) {
            return result;
        }
        for (String image : imageArray = images.split(",")) {
            if (image == null || image.trim().isEmpty()) continue;
            result.add(UrlUtils.toAdminAbsoluteUrl((String)image.trim()));
        }
        return result;
    }

    private String convertLessonStatus(Integer status) {
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

    private String convertBookingStatus(Integer status) {
        if (status == null) {
            return "\u672a\u77e5";
        }
        if (status == 10) {
            return "\u5df2\u70b9\u540d";
        }
        if (status == 3) {
            return "\u5df2\u53d6\u6d88";
        }
        if (status == 1) {
            return "\u5df2\u9884\u7ea6\u672a\u70b9\u540d";
        }
        if (status == 0) {
            return "\u73b0\u573a\u70b9\u540d";
        }
        return "\u672a\u77e5";
    }

    private String formatTimestamp(Integer timestamp) {
        if (timestamp == null || timestamp <= 0) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp.longValue() * 1000L));
    }

    private String formatLongTimestamp(Long timestamp) {
        if (timestamp == null || timestamp <= 0L) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp * 1000L));
    }
}

