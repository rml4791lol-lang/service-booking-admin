/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.admin.controller.instructor.InstructorLeaveController
 *  com.mdd.admin.service.IInstructorLeaveService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.instructor.InstructorLeaveAuditValidate
 *  com.mdd.admin.validate.instructor.InstructorLeaveSearchValidate
 *  com.mdd.admin.vo.instructor.InstructorLeaveListVo
 *  com.mdd.common.core.AjaxResult
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.InstructorLeave
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.CourseLesson
 *  com.mdd.common.entity.course.CourseMakeup
 *  com.mdd.common.entity.instructor.InstructorAccountLog
 *  com.mdd.common.mapper.InstructorLeaveMapper
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.CourseLessonMapper
 *  com.mdd.common.mapper.course.CourseMakeupMapper
 *  com.mdd.common.mapper.instructor.InstructorAccountLogMapper
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  javax.annotation.Resource
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.validation.annotation.Validated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.mdd.admin.controller.instructor;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.aop.Log;
import com.mdd.admin.service.IInstructorLeaveService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.instructor.InstructorLeaveAuditValidate;
import com.mdd.admin.validate.instructor.InstructorLeaveSearchValidate;
import com.mdd.admin.vo.instructor.InstructorLeaveListVo;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.InstructorLeave;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseMakeup;
import com.mdd.common.entity.instructor.InstructorAccountLog;
import com.mdd.common.mapper.InstructorLeaveMapper;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.CourseMakeupMapper;
import com.mdd.common.mapper.instructor.InstructorAccountLogMapper;
import com.mdd.common.util.ParamUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/adminapi/instructor/leave"})
@Api(tags={"\u6559\u7ec3\u8bf7\u5047\u7ba1\u7406"})
public class InstructorLeaveController {
    @Resource
    IInstructorLeaveService instructorLeaveService;
    @Resource
    InstructorLeaveMapper instructorLeaveMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    CourseMakeupMapper courseMakeupMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    InstructorAccountLogMapper instructorAccountLogMapper;

    @GetMapping(value={"/list"})
    @ApiOperation(value="\u83b7\u53d6\u8bf7\u5047\u5217\u8868")
    public AjaxResult<PageResult<InstructorLeaveListVo>> list(@Validated PageValidate pageValidate, @Validated InstructorLeaveSearchValidate searchValidate) {
        PageResult list = this.instructorLeaveService.list(pageValidate, searchValidate);
        return AjaxResult.success(list);
    }

    @Log(title="\u6559\u7ec3\u8bf7\u5047\u5ba1\u6838")
    @PostMapping(value={"/audit"})
    @ApiOperation(value="\u8bf7\u5047\u5ba1\u6279")
    public AjaxResult<Object> audit(@Validated @RequestBody InstructorLeaveAuditValidate auditValidate) {
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        this.instructorLeaveService.audit(auditValidate, adminId);
        return AjaxResult.success();
    }

    @GetMapping(value={"/makeupLessons"})
    @ApiOperation(value="\u67e5\u8be2\u8bf7\u5047\u671f\u95f4\u8bfe\u8282")
    public AjaxResult<Object> makeupLessons(@RequestParam(value="leaveId") Integer leaveId) {
        InstructorLeave leave = (InstructorLeave)this.instructorLeaveMapper.selectById((Serializable)leaveId);
        if (leave == null) {
            return AjaxResult.failed((String)"\u8bf7\u5047\u8bb0\u5f55\u4e0d\u5b58\u5728");
        }
        Integer instructorId = leave.getInstructorId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = sdf.format(new Date(leave.getStartTime() * 1000L));
        String endDate = sdf.format(new Date(leave.getEndTime() * 1000L));
        List<CourseClass> classes = this.courseClassMapper.selectList(new QueryWrapper<CourseClass>().eq("instructor_id", (Object)instructorId).eq("is_delete", (Object)0));
        if (classes.isEmpty()) {
            LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
            result.put("lessons", new ArrayList());
            result.put("instructorName", "");
            return AjaxResult.success(result);
        }
        ArrayList<Integer> classIds = new ArrayList<Integer>();
        HashMap<Integer, String> classNameMap = new HashMap<Integer, String>();
        for (CourseClass cls : classes) {
            classIds.add(cls.getId());
            classNameMap.put(cls.getId(), cls.getName());
        }
        List<CourseLesson> lessons = this.courseLessonMapper.selectList(new QueryWrapper<CourseLesson>().in("class_id", classIds).ge("lesson_date", (Object)startDate).le("lesson_date", (Object)endDate).orderByAsc("lesson_date", "lesson_no"));
        List<CourseMakeup> makeups = this.courseMakeupMapper.selectList(new QueryWrapper<CourseMakeup>().eq("leave_id", (Object)leaveId));
        HashMap<Integer, CourseMakeup> makeupMap = new HashMap<Integer, CourseMakeup>();
        for (CourseMakeup m : makeups) {
            makeupMap.put(m.getLessonId(), m);
        }
        String[] weekLabels = new String[]{"", "\u5468\u4e00", "\u5468\u4e8c", "\u5468\u4e09", "\u5468\u56db", "\u5468\u4e94", "\u5468\u516d", "\u5468\u65e5"};
        ArrayList list = new ArrayList();
        for (CourseLesson lesson : lessons) {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("lessonId", lesson.getId());
            item.put("classId", lesson.getClassId());
            item.put("className", classNameMap.getOrDefault(lesson.getClassId(), ""));
            item.put("lessonNo", lesson.getLessonNo());
            item.put("lessonDate", lesson.getLessonDate());
            item.put("weekDay", lesson.getWeekDay() != null && lesson.getWeekDay() >= 1 && lesson.getWeekDay() <= 7 ? weekLabels[lesson.getWeekDay()] : "");
            item.put("timeSlot", lesson.getTimeSlot());
            item.put("lessonStatus", lesson.getStatus());
            CourseMakeup makeup = (CourseMakeup)makeupMap.get(lesson.getId());
            if (makeup != null) {
                item.put("makeupId", makeup.getId());
                item.put("makeupDate", makeup.getMakeupDate());
                item.put("makeupTimeSlot", makeup.getMakeupTimeSlot());
                item.put("makeupStatus", makeup.getStatus());
            } else {
                item.put("makeupId", null);
                item.put("makeupDate", "");
                item.put("makeupTimeSlot", "");
                item.put("makeupStatus", -1);
            }
            list.add(item);
        }
        Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)instructorId);
        String instructorName = instructor != null ? instructor.getName() : "";
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("lessons", list);
        result.put("instructorName", instructorName);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        return AjaxResult.success(result);
    }

    @PostMapping(value={"/arrangeMakeup"})
    @ApiOperation(value="\u5b89\u6392\u8865\u8bfe\u65f6\u95f4")
    public AjaxResult<Object> arrangeMakeup(@RequestBody Map<String, Object> params) {
        Integer leaveId = ParamUtils.requiredInt(params.get("leaveId"), "请假记录ID");
        Integer lessonId = ParamUtils.requiredInt(params.get("lessonId"), "课节ID");
        String makeupDate = (String)params.get("makeupDate");
        String makeupTimeSlot = (String)params.get("makeupTimeSlot");
        InstructorLeave leave = (InstructorLeave)this.instructorLeaveMapper.selectById((Serializable)leaveId);
        if (leave == null) {
            return AjaxResult.failed((String)"\u8bf7\u5047\u8bb0\u5f55\u4e0d\u5b58\u5728");
        }
        CourseLesson lesson = (CourseLesson)this.courseLessonMapper.selectById((Serializable)lessonId);
        if (lesson == null) {
            return AjaxResult.failed((String)"\u8bfe\u8282\u4e0d\u5b58\u5728");
        }
        CourseMakeup exist = (CourseMakeup)this.courseMakeupMapper.selectOne(new QueryWrapper<CourseMakeup>().eq("lesson_id", (Object)lessonId).eq("leave_id", (Object)leaveId));
        int now = (int)(System.currentTimeMillis() / 1000L);
        if (exist != null) {
            exist.setMakeupDate(makeupDate);
            exist.setMakeupTimeSlot(makeupTimeSlot);
            exist.setUpdateTime(Integer.valueOf(now));
            this.courseMakeupMapper.updateById(exist);
        } else {
            CourseClass cls = (CourseClass)this.courseClassMapper.selectById((Serializable)lesson.getClassId());
            CourseMakeup makeup = new CourseMakeup();
            makeup.setLessonId(lessonId);
            makeup.setClassId(lesson.getClassId());
            makeup.setLeaveId(leaveId);
            makeup.setInstructorId(leave.getInstructorId());
            makeup.setLessonNo(lesson.getLessonNo());
            makeup.setOriginalDate(lesson.getLessonDate());
            makeup.setMakeupDate(makeupDate);
            makeup.setMakeupTimeSlot(makeupTimeSlot);
            makeup.setStatus(Integer.valueOf(0));
            makeup.setSettleAmount(cls != null ? cls.getFeePerLesson() : BigDecimal.ZERO);
            makeup.setCreateTime(Integer.valueOf(now));
            makeup.setUpdateTime(Integer.valueOf(now));
            this.courseMakeupMapper.insert(makeup);
        }
        return AjaxResult.success();
    }

    @PostMapping(value={"/makeupComplete"})
    @ApiOperation(value="\u5b8c\u6210\u8865\u8bfe\u7ed3\u7b97")
    @Transactional(rollbackFor={Exception.class})
    public AjaxResult<Object> makeupComplete(@RequestBody Map<String, Object> params) {
        Instructor inst;
        CourseClass cls;
        BigDecimal feePerLesson;
        Integer makeupId = ParamUtils.requiredInt(params.get("makeupId"), "补课记录ID");
        CourseMakeup makeup = (CourseMakeup)this.courseMakeupMapper.selectById((Serializable)makeupId);
        if (makeup == null) {
            return AjaxResult.failed((String)"\u8865\u8bfe\u8bb0\u5f55\u4e0d\u5b58\u5728");
        }
        if (makeup.getStatus() == 1) {
            return AjaxResult.failed((String)"\u8be5\u8bfe\u65f6\u5df2\u5b8c\u6210\u8865\u8bfe");
        }
        int now = (int)(System.currentTimeMillis() / 1000L);
        makeup.setStatus(Integer.valueOf(1));
        makeup.setUpdateTime(Integer.valueOf(now));
        this.courseMakeupMapper.updateById(makeup);
        CourseLesson lesson = (CourseLesson)this.courseLessonMapper.selectById((Serializable)makeup.getLessonId());
        if (lesson != null && lesson.getStatus() != 1) {
            lesson.setStatus(Integer.valueOf(1));
            lesson.setUpdateTime(Integer.valueOf(now));
            this.courseLessonMapper.updateById(lesson);
        }
        BigDecimal bigDecimal = feePerLesson = (cls = (CourseClass)this.courseClassMapper.selectById((Serializable)makeup.getClassId())) != null ? cls.getFeePerLesson() : BigDecimal.ZERO;
        if (feePerLesson != null && feePerLesson.compareTo(BigDecimal.ZERO) > 0 && (inst = (Instructor)this.instructorMapper.selectById((Serializable)makeup.getInstructorId())) != null) {
            BigDecimal beforeBalance = inst.getBalance() != null ? inst.getBalance() : BigDecimal.ZERO;
            BigDecimal afterBalance = beforeBalance.add(feePerLesson);
            inst.setBalance(afterBalance);
            this.instructorMapper.updateById(inst);
            InstructorAccountLog accountLog = new InstructorAccountLog();
            accountLog.setInstructorId(makeup.getInstructorId());
            accountLog.setType(Integer.valueOf(1));
            accountLog.setAmount(feePerLesson);
            accountLog.setBeforeBalance(beforeBalance);
            accountLog.setAfterBalance(afterBalance);
            accountLog.setRelationId(makeup.getId());
            accountLog.setRemark("\u8865\u8bfe\u7ed3\u7b97: " + (cls != null ? cls.getName() : "") + " \u7b2c" + makeup.getLessonNo() + "\u8282");
            accountLog.setCreateTime(Long.valueOf(now));
            this.instructorAccountLogMapper.insert(accountLog);
            makeup.setSettleAmount(feePerLesson);
            this.courseMakeupMapper.updateById(makeup);
        }
        return AjaxResult.success();
    }
}

