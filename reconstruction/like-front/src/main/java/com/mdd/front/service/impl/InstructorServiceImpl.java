/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.binarywang.wx.miniapp.api.WxMaService
 *  cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.InstructorAuth
 *  com.mdd.common.entity.InstructorLeave
 *  com.mdd.common.entity.Message
 *  com.mdd.common.entity.RechargeOrder
 *  com.mdd.common.entity.course.CourseAttendance
 *  com.mdd.common.entity.course.CourseBooking
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.CourseConsumeLog
 *  com.mdd.common.entity.course.CourseLesson
 *  com.mdd.common.entity.course.CourseLessonLog
 *  com.mdd.common.entity.course.CourseManualLesson
 *  com.mdd.common.entity.course.CourseTempLesson
 *  com.mdd.common.entity.course.UserCourse
 *  com.mdd.common.entity.instructor.InstructorAccountLog
 *  com.mdd.common.entity.student.Student
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.entity.user.UserSession
 *  com.mdd.common.enums.ClientEnum
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.InstructorAuthMapper
 *  com.mdd.common.mapper.InstructorLeaveMapper
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.LeaveApplicationMapper
 *  com.mdd.common.mapper.MessageMapper
 *  com.mdd.common.mapper.RechargeOrderMapper
 *  com.mdd.common.mapper.course.CourseAttendanceMapper
 *  com.mdd.common.mapper.course.CourseBookingMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.CourseConsumeLogMapper
 *  com.mdd.common.mapper.course.CourseLessonLogMapper
 *  com.mdd.common.mapper.course.CourseLessonMapper
 *  com.mdd.common.mapper.course.CourseManualLessonMapper
 *  com.mdd.common.mapper.course.CourseTempLessonMapper
 *  com.mdd.common.mapper.course.UserCourseMapper
 *  com.mdd.common.mapper.instructor.InstructorAccountLogMapper
 *  com.mdd.common.mapper.student.StudentMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.mapper.user.UserSessionMapper
 *  com.mdd.common.plugin.wechat.WxMnpDriver
 *  com.mdd.common.service.LessonCommissionService
 *  com.mdd.common.util.ClassScheduleUtil
 *  com.mdd.common.util.CourseConsumeLogUtil
 *  com.mdd.common.util.RedisUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.common.util.UserCourseEligibilityUtil
 *  com.mdd.common.util.YmlUtils
 *  com.mdd.front.cache.TokenLoginCache
 *  com.mdd.front.service.IInstructorService
 *  com.mdd.front.service.impl.InstructorServiceImpl
 *  com.mdd.front.validate.instructor.InstructorLeaveApplyValidate
 *  com.mdd.front.validate.instructor.InstructorProfileUpdateValidate
 *  com.mdd.front.validate.users.NewUserUpdateValidate
 *  javax.annotation.Resource
 *  me.chanjar.weixin.common.error.WxErrorException
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.front.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.InstructorAuth;
import com.mdd.common.entity.InstructorLeave;
import com.mdd.common.entity.Message;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.course.CourseAttendance;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseConsumeLog;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseLessonLog;
import com.mdd.common.entity.course.CourseManualLesson;
import com.mdd.common.entity.course.CourseTempLesson;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.instructor.InstructorAccountLog;
import com.mdd.common.entity.student.Student;
import com.mdd.common.entity.user.User;
import com.mdd.common.entity.user.UserSession;
import com.mdd.common.enums.ClientEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorAuthMapper;
import com.mdd.common.mapper.InstructorLeaveMapper;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.LeaveApplicationMapper;
import com.mdd.common.mapper.MessageMapper;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.course.CourseAttendanceMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseConsumeLogMapper;
import com.mdd.common.mapper.course.CourseLessonLogMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.CourseManualLessonMapper;
import com.mdd.common.mapper.course.CourseTempLessonMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.instructor.InstructorAccountLogMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.mapper.user.UserSessionMapper;
import com.mdd.common.plugin.wechat.WxMnpDriver;
import com.mdd.common.service.LessonCommissionService;
import com.mdd.common.util.ClassScheduleUtil;
import com.mdd.common.util.CourseConsumeLogUtil;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.UserCourseEligibilityUtil;
import com.mdd.common.util.YmlUtils;
import com.mdd.front.cache.TokenLoginCache;
import com.mdd.front.service.IInstructorService;
import com.mdd.front.validate.instructor.InstructorLeaveApplyValidate;
import com.mdd.front.validate.instructor.InstructorProfileUpdateValidate;
import com.mdd.front.validate.users.NewUserUpdateValidate;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mdd.common.entity.LeaveApplication;
import java.util.Iterator;

@Service
public class InstructorServiceImpl
implements IInstructorService {
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    InstructorAuthMapper instructorAuthMapper;
    @Resource
    InstructorLeaveMapper instructorLeaveMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    UserCourseMapper userCourseMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    CourseAttendanceMapper courseAttendanceMapper;
    @Resource
    CourseConsumeLogMapper courseConsumeLogMapper;
    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    CourseLessonLogMapper courseLessonLogMapper;
    @Resource
    LeaveApplicationMapper leaveApplicationMapper;
    @Resource
    MessageMapper messageMapper;
    @Resource
    InstructorAccountLogMapper instructorAccountLogMapper;
    @Resource
    UserSessionMapper userSessionMapper;
    @Resource
    CourseBookingMapper courseBookingMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    LessonCommissionService lessonCommissionService;
    @Resource
    CourseManualLessonMapper courseManualLessonMapper;
    @Resource
    CourseTempLessonMapper courseTempLessonMapper;

    @Transactional
    public void mnpMobile(String code, String token) {
        String redisKey = "instructor:wxlogin:temp:" + token;
        Object cacheObj = RedisUtils.get((String)redisKey);
        if (StringUtils.isNull(cacheObj)) {
            throw new OperateException("\u83b7\u53d6\u767b\u5f55\u72b6\u6001\u5931\u8d25, \u8bf7\u91cd\u65b0\u767b\u5f55");
        }
        String[] parts = cacheObj.toString().split(",", 2);
        String openId = parts[0];
        String unionId = parts.length > 1 ? parts[1] : "0";
        try {
            WxMaService wxMaService = WxMnpDriver.mnp();
            WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getNewPhoneNoInfo(code);
            String mobile = phoneNoInfo.getPhoneNumber();
            Instructor instructor = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>().eq("phone", (Object)mobile).isNull("delete_time").last("limit 1"));
            if (StringUtils.isNull(instructor)) {
                throw new OperateException("\u8be5\u624b\u673a\u53f7\u5c1a\u672a\u6ce8\u518c\u4e3a\u6559\u7ec3\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458\u5728\u540e\u53f0\u6dfb\u52a0");
            }
            if (instructor.getStatus() == 0) {
                throw new OperateException("\u6559\u7ec3\u8d26\u53f7\u5df2\u88ab\u505c\u7528");
            }
            if (instructor.getStatus() == 2) {
                throw new OperateException("\u8fd8\u672a\u5ba1\u6838\uff0c\u65e0\u6cd5\u767b\u5f55");
            }
            InstructorAuth auth = (InstructorAuth)this.instructorAuthMapper.selectOne(new QueryWrapper<InstructorAuth>().eq("openid", (Object)openId).last("limit 1"));
            if (auth == null) {
                auth = new InstructorAuth();
                auth.setInstructorId(instructor.getId());
                auth.setOpenid(openId);
                auth.setUnionid(unionId);
                auth.setTerminal(Integer.valueOf(ClientEnum.MNP.getCode()));
                auth.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
                auth.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
                this.instructorAuthMapper.insert(auth);
            } else {
                auth.setInstructorId(instructor.getId());
                if (StringUtils.isNotEmpty((String)unionId) && !"0".equals(unionId)) {
                    auth.setUnionid(unionId);
                }
                auth.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
                this.instructorAuthMapper.updateById(auth);
            }
            int terminal = ClientEnum.MNP.getCode();
            Integer tokenUserId = instructor.getUserId() != null && instructor.getUserId() > 0
                    ? instructor.getUserId() : instructor.getId();
            TokenLoginCache.set((String)token, (Object)tokenUserId, (String)String.valueOf(terminal));
            this.userSessionMapper.delete(new QueryWrapper<UserSession>().eq("user_id", (Object)tokenUserId).eq("terminal", (Object)terminal));
            UserSession userSession = new UserSession();
            userSession.setUserId(tokenUserId);
            userSession.setToken(token);
            userSession.setTerminal(Integer.valueOf(terminal));
            userSession.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            int timeout = 7200;
            if (StringUtils.isNotNull(YmlUtils.get((String)"like.login.timeout"))) {
                timeout = Integer.parseInt(YmlUtils.get((String)"like.login.timeout"));
            }
            userSession.setExpireTime(Long.valueOf(System.currentTimeMillis() / 1000L + (long)timeout));
            this.userSessionMapper.insert(userSession);
            RedisUtils.del((String[])new String[]{redisKey});
        }
        catch (WxErrorException e) {
            throw new OperateException(e.getError().getErrorCode() + ", " + e.getError().getErrorMsg());
        }
    }

    public void updateInstructorInfo(NewUserUpdateValidate newUserUpdateValidate, String token) {
        String key = "login:front:token:" + ClientEnum.MNP.getCode() + ":" + token;
        Object instructorIdObj = RedisUtils.get((String)key);
        Integer instructorId = instructorIdObj != null ? Integer.parseInt(instructorIdObj.toString()) : 0;
        if (instructorId == 0) {
            throw new OperateException("\u7531\u4e8e\u5c1a\u672a\u7ed1\u5b9a\u624b\u673a\u53f7\u6216\u4ee4\u724c\u5931\u6548, \u65e0\u6cd5\u66f4\u65b0\u4fe1\u606f");
        }
        Instructor instructor = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>()
                .eq("user_id", (Object)instructorId).isNull("delete_time").last("limit 1"));
        if (instructor == null) {
            instructor = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>()
                    .eq("id", (Object)instructorId).isNull("delete_time").last("limit 1"));
        }
        Assert.notNull((Object)instructor, (String)"\u6559\u7ec3\u8d26\u53f7\u4e0d\u5b58\u5728", (Object[])new Object[0]);
        if (StringUtils.isNotEmpty((String)newUserUpdateValidate.getAvatar())) {
            instructor.setAvatar(newUserUpdateValidate.getAvatar());
        }
        if (StringUtils.isNotEmpty((String)newUserUpdateValidate.getNickname())) {
            instructor.setName(newUserUpdateValidate.getNickname());
        }
        instructor.setUpdateTime(new Date());
        this.instructorMapper.updateById(instructor);
    }

    public Map<String, Object> getHomeData(Integer instructorId) {
        Instructor instructor = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>().eq("id", (Object)instructorId).isNull("delete_time").last("limit 1"));
        if (instructor == null) {
            throw new OperateException("\u6559\u7ec3\u4fe1\u606f\u4e0d\u5b58\u5728");
        }
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, Object> coachInfo = new LinkedHashMap<String, Object>();
        coachInfo.put("id", instructor.getId());
        coachInfo.put("name", instructor.getName());
        coachInfo.put("avatar", UrlUtils.toAbsoluteUrl((String)instructor.getAvatar()));
        coachInfo.put("phone", instructor.getPhone());
        result.put("coachInfo", coachInfo);
        QueryWrapper<CourseClass> classQw = new QueryWrapper<CourseClass>();
        classQw.eq("instructor_id", (Object)instructor.getId());
        classQw.eq("is_delete", (Object)0);
        classQw.orderByDesc("id");
        List<CourseClass> classes = this.courseClassMapper.selectList((Wrapper)classQw);
        ArrayList classList = new ArrayList();
        for (CourseClass cls : classes) {
            LinkedHashMap<String, Object> m = new LinkedHashMap<String, Object>();
            m.put("id", cls.getId());
            m.put("name", cls.getName());
            m.put("image", UrlUtils.toAbsoluteUrl((String)cls.getImage()));
            m.put("venue", cls.getVenue());
            long realStudentCount = this.userCourseMapper.selectCount(new QueryWrapper<UserCourse>().eq("course_class_id", (Object)cls.getId()).isNull("delete_time"));
            m.put("studentCount", realStudentCount);
            m.put("weekDays", cls.getWeekDays());
            m.put("timeSlot", cls.getTimeSlot());
            m.put("scheduleMode", cls.getScheduleMode() != null ? cls.getScheduleMode() : 0);
            m.put("scheduleRules", cls.getScheduleRules());
            m.put("totalLessons", cls.getTotalLessons());
            m.put("status", cls.getStatus());
            m.put("statusText", this.getCourseClassStatusText(cls.getStatus()));
            classList.add(m);
        }
        result.put("myClasses", classList);
        ArrayList<Integer> classIds = new ArrayList<Integer>();
        for (CourseClass cls : classes) {
            classIds.add(cls.getId());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = sdf.format(new Date());
        ArrayList todayCourseList = new ArrayList();
        if (!classIds.isEmpty()) {
            List<CourseLesson> todayLessons = this.courseLessonMapper.selectList(new QueryWrapper<CourseLesson>().in("class_id", classIds).eq("lesson_date", (Object)todayStr).ne("status", (Object)2).orderByAsc("time_slot").orderByAsc("id"));
            LinkedHashMap<Integer, CourseClass> classMap = new LinkedHashMap<Integer, CourseClass>();
            for (CourseClass cls : classes) {
                classMap.put(cls.getId(), cls);
            }
            for (CourseLesson lesson : todayLessons) {
                Integer coachId;
                CourseClass cls = (CourseClass)classMap.get(lesson.getClassId());
                if (cls == null || ClassScheduleUtil.isTempLesson((CourseLesson)lesson) && ((coachId = lesson.getCoachId() != null && lesson.getCoachId() > 0 ? lesson.getCoachId() : cls.getInstructorId()) == null || !coachId.equals(instructor.getId()))) continue;
                String todayTimeSlot = ClassScheduleUtil.resolveLessonTimeSlot((CourseLesson)lesson, (CourseClass)cls);
                LinkedHashMap<String, Object> m = new LinkedHashMap<String, Object>();
                m.put("id", cls.getId());
                m.put("lessonId", lesson.getId());
                m.put("lessonNo", lesson.getLessonNo());
                m.put("lessonType", lesson.getLessonType() != null ? lesson.getLessonType() : 0);
                m.put("name", cls.getName());
                m.put("image", UrlUtils.toAbsoluteUrl((String)cls.getImage()));
                m.put("venue", ClassScheduleUtil.resolveLessonVenue((CourseLesson)lesson, (CourseClass)cls));
                m.put("startTime", todayTimeSlot);
                m.put("duration", 90);
                todayCourseList.add(m);
            }
        }
        result.put("todayCourses", todayCourseList);
        LinkedHashMap<String, Number> stats = new LinkedHashMap<String, Number>();
        stats.put("classCount", classes.size());
        stats.put("todayLessons", todayCourseList.size());
        long pendingLeave = 0L;
        if (!classIds.isEmpty()) {
            pendingLeave = this.leaveApplicationMapper.selectCount(new QueryWrapper<LeaveApplication>().in("class_id", classIds).eq("applicant_type", (Object)1).eq("status", (Object)0).isNull("delete_time"));
        }
        stats.put("pendingLeave", pendingLeave);
        long pendingCheckIn = 0L;
        if (!classIds.isEmpty()) {
            Long cnt = this.courseBookingMapper.selectCount(new QueryWrapper<CourseBooking>().in("course_class_id", classIds).eq("lesson_date", (Object)todayStr).eq("status", (Object)1));
            pendingCheckIn = cnt != null ? cnt : 0L;
        }
        stats.put("pendingCheckIn", pendingCheckIn);
        result.put("stats", stats);
        return result;
    }

    private String getCourseClassStatusText(Integer status) {
        if (status == null) {
            return "";
        }
        switch (status) {
            case 0: {
                return "\u5df2\u7ed3\u675f";
            }
            case 1: {
                return "\u8fdb\u884c\u4e2d";
            }
            case 2: {
                return "\u9884\u62a5\u540d";
            }
        }
        return "";
    }

    public Map<String, Object> getProfile(Integer instructorId) {
        Instructor instructor = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>().eq("id", (Object)instructorId).isNull("delete_time").last("limit 1"));
        if (instructor == null) {
            throw new OperateException("\u6559\u7ec3\u4fe1\u606f\u4e0d\u5b58\u5728");
        }
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("id", instructor.getId());
        result.put("name", instructor.getName());
        result.put("avatar", UrlUtils.toAbsoluteUrl((String)instructor.getAvatar()));
        result.put("phone", instructor.getPhone());
        result.put("gender", instructor.getGender());
        result.put("specialty", instructor.getSpecialty() != null ? instructor.getSpecialty() : "");
        result.put("experienceYears", instructor.getExperienceYears() != null ? instructor.getExperienceYears() : 0);
        result.put("introduction", instructor.getIntroduction() != null ? instructor.getIntroduction() : "");
        result.put("ratingScore", instructor.getRatingScore() != null ? instructor.getRatingScore() : Integer.valueOf(0));
        result.put("hourlyRate", instructor.getHourlyRate() != null ? instructor.getHourlyRate() : Integer.valueOf(0));
        long activeClasses = this.courseClassMapper.selectCount(new QueryWrapper<CourseClass>().eq("instructor_id", (Object)instructorId).eq("is_delete", (Object)0).eq("status", (Object)1));
        result.put("activeClasses", activeClasses);
        long totalLessons = this.courseLessonLogMapper.selectCount(new QueryWrapper<CourseLessonLog>().eq("instructor_id", (Object)instructorId).isNull("delete_time").inSql("course_class_id", "SELECT id FROM la_course_class"));
        result.put("totalLessons", totalLessons);
        String cert = instructor.getCertification();
        result.put("certification", cert != null && !cert.isEmpty() ? Arrays.asList(cert.split(",")) : new ArrayList());
        result.put("certificationText", cert != null ? cert : "");
        result.put("idCard", instructor.getIdCard() != null ? instructor.getIdCard() : "");
        String certImagesStr = instructor.getCertImages();
        if (certImagesStr != null && !certImagesStr.isEmpty()) {
            ArrayList<String> certImageList = new ArrayList<String>();
            for (String img : certImagesStr.split(",")) {
                if (img == null || img.trim().isEmpty()) continue;
                certImageList.add(UrlUtils.toAbsoluteUrl((String)img.trim()));
            }
            result.put("certImages", certImageList);
        } else {
            result.put("certImages", new ArrayList());
        }
        return result;
    }

    public void updateProfile(Integer instructorId, InstructorProfileUpdateValidate validate) {
        Instructor instructor = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>().eq("id", (Object)instructorId).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)instructor, (String)"\u6559\u7ec3\u4fe1\u606f\u4e0d\u5b58\u5728", (Object[])new Object[0]);
        if (instructor.getStatus() != null && instructor.getStatus() != 1) {
            throw new OperateException("\u5f53\u524d\u8d26\u53f7\u72b6\u6001\u4e0d\u53ef\u4fee\u6539\u8d44\u6599");
        }
        if (StringUtils.isNotEmpty((String)validate.getIdCard())) {
            String idCard = validate.getIdCard().trim();
            if (!idCard.matches("^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$")) {
                throw new OperateException("\u8eab\u4efd\u8bc1\u53f7\u683c\u5f0f\u4e0d\u6b63\u786e");
            }
            instructor.setIdCard(idCard);
        }
        instructor.setName(validate.getName().trim());
        if (StringUtils.isNotEmpty((String)validate.getAvatar())) {
            instructor.setAvatar(UrlUtils.toRelativeUrl((String)validate.getAvatar()));
        }
        instructor.setGender(validate.getGender());
        instructor.setSpecialty(validate.getSpecialty().trim());
        instructor.setExperienceYears(Integer.valueOf(Math.max(0, validate.getExperienceYears())));
        instructor.setIntroduction(validate.getIntroduction() != null ? validate.getIntroduction().trim() : "");
        instructor.setCertification(validate.getCertification() != null ? validate.getCertification().trim() : "");
        if (validate.getCertImages() != null) {
            ArrayList<String> relativeImages = new ArrayList<String>();
            for (String img : validate.getCertImages().split(",")) {
                if (img == null || img.trim().isEmpty()) continue;
                relativeImages.add(UrlUtils.toRelativeUrl((String)img.trim()));
            }
            instructor.setCertImages(String.join((CharSequence)",", relativeImages));
        }
        instructor.setUpdateTime(new Date());
        this.instructorMapper.updateById(instructor);
    }

    public Map<String, Object> getStudents(Integer instructorId, Integer pageNo, Integer pageSize, String keyword) {
        long absent;
        Instructor instructor = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>().eq("id", (Object)instructorId).isNull("delete_time").last("limit 1"));
        List<CourseClass> classes = this.courseClassMapper.selectList(new QueryWrapper<CourseClass>().eq("instructor_id", (Object)instructorId).eq("is_delete", (Object)0));
        if (classes.isEmpty()) {
            LinkedHashMap<String, Object> empty = new LinkedHashMap<String, Object>();
            empty.put("total", 0);
            empty.put("stats", emptyStudentStats());
            empty.put("list", new ArrayList());
            return empty;
        }
        List classIds = classes.stream().map(CourseClass::getId).collect(Collectors.toList());
        Map<Integer, String> classNameMap = classes.stream().collect(Collectors.toMap(CourseClass::getId, CourseClass::getName));
        List<UserCourse> userCourses = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().in("course_class_id", classIds).isNull("delete_time"));
        if (userCourses.isEmpty()) {
            LinkedHashMap<String, Object> empty = new LinkedHashMap<String, Object>();
            empty.put("total", 0);
            empty.put("stats", emptyStudentStats());
            empty.put("list", new ArrayList());
            return empty;
        }
        Set userIds = userCourses.stream().map(UserCourse::getUserId).collect(Collectors.toSet());
        List<User> users = this.userMapper.selectList(new QueryWrapper<User>().in("id", userIds).isNull("delete_time"));
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(User::getId, u -> u, (a, b) -> a));
        if (keyword != null && !keyword.trim().isEmpty()) {
            String kw = keyword.trim().toLowerCase();
            userMap.entrySet().removeIf(entry -> {
                User u = (User)entry.getValue();
                String nickname = u.getNickname() != null ? u.getNickname().toLowerCase() : "";
                String realName = u.getRealName() != null ? u.getRealName().toLowerCase() : "";
                String mobile = u.getMobile() != null ? u.getMobile() : "";
                String idStr = String.valueOf(u.getId());
                boolean match = nickname.contains(kw) || realName.contains(kw) || mobile.contains(kw) || idStr.contains(kw);
                return !match;
            });
        }
        ArrayList resultList = new ArrayList();
        for (Map.Entry<Integer, User> entry2 : userMap.entrySet()) {
            Integer userId = entry2.getKey();
            User user = entry2.getValue();
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            String displayName = null;
            List<UserCourse> myUcForStudent = userCourses.stream().filter(uc -> uc.getUserId().equals(userId)).collect(Collectors.toList());
            for (UserCourse ucItem : myUcForStudent) {
                Student stu;
                if (ucItem.getStudentId() == null || ucItem.getStudentId() <= 0 || (stu = (Student)this.studentMapper.selectOne(new QueryWrapper<Student>().eq("id", (Object)ucItem.getStudentId()).isNull("delete_time").last("limit 1"))) == null || stu.getName() == null || stu.getName().isEmpty()) continue;
                displayName = stu.getName();
                break;
            }
            if (displayName == null) {
                displayName = user.getRealName() != null && !user.getRealName().isEmpty() ? user.getRealName() : (user.getNickname() != null ? user.getNickname() : "\u672a\u77e5");
            }
            item.put("userId", userId);
            item.put("name", displayName);
            item.put("avatar", UrlUtils.toAbsoluteUrl((String)user.getAvatar()));
            item.put("phone", user.getMobile() != null ? user.getMobile() : "");
            List<UserCourse> myUc = userCourses.stream().filter(uc -> uc.getUserId().equals(userId) && classNameMap.containsKey(uc.getCourseClassId())).collect(Collectors.toList());
            List<UserCourse> countableUc = myUc.stream().filter(uc -> uc.getStatus() == null || uc.getStatus() != 2).collect(Collectors.toList());
            List<String> classNames = countableUc.stream().map(uc -> classNameMap.getOrDefault(uc.getCourseClassId(), "")).filter(n -> !n.isEmpty()).collect(Collectors.toList());
            item.put("enrolledClasses", String.join((CharSequence)"  ", classNames));
            int totalH = countableUc.stream().mapToInt(uc -> uc.getUsedLessons() != null ? uc.getUsedLessons() : 0).sum();
            int totalAll = countableUc.stream().mapToInt(uc -> uc.getTotalLessons() != null ? uc.getTotalLessons() : 0).sum();
            int remainH = countableUc.stream().mapToInt(uc -> {
                if (uc.getRemainLessons() != null) {
                    return uc.getRemainLessons();
                }
                int total = uc.getTotalLessons() != null ? uc.getTotalLessons() : 0;
                int used = uc.getUsedLessons() != null ? uc.getUsedLessons() : 0;
                return Math.max(total - used, 0);
            }).sum();
            item.put("totalHours", totalH);
            item.put("remainingHours", remainH);
            item.put("purchasedHours", totalAll);
            long fiveDaysAgoTs = System.currentTimeMillis() / 1000L - 432000L;
            boolean isActive = user.getLoginTime() != null && user.getLoginTime() > fiveDaysAgoTs;
            item.put("status", isActive ? "\u6d3b\u8dc3" : "\u4e0d\u6d3b\u8dc3");
            resultList.add(item);
        }
        long totalStudents = resultList.size();
        long fiveDaysAgo = System.currentTimeMillis() / 1000L - 432000L;
        long activeStudents = userMap.values().stream().filter(u -> u.getLoginTime() != null && u.getLoginTime() > fiveDaysAgo).count();
        List<CourseAttendance> atts = this.courseAttendanceMapper.selectList(new QueryWrapper<CourseAttendance>().in("class_id", classIds));
        long present = atts.stream().filter(a -> a.getStatus() != null && a.getStatus() == 1).count();
        long presentAbsentSum = present + (absent = atts.stream().filter(a -> a.getStatus() != null && a.getStatus() == 3).count());
        String attendanceRate = presentAbsentSum > 0L ? String.format("%d%%", (long)((double)present * 100.0 / (double)presentAbsentSum)) : "-";
        String averageRating = instructor != null && instructor.getRatingScore() != null ? String.valueOf(instructor.getRatingScore()) : "-";
        int start = (pageNo - 1) * pageSize;
        int end = Math.min(start + pageSize, resultList.size());
        List pageList = start < resultList.size() ? resultList.subList(start, end) : new ArrayList();
        LinkedHashMap<String, Object> statsMap = new LinkedHashMap<String, Object>();
        statsMap.put("totalStudents", totalStudents);
        statsMap.put("activeStudents", activeStudents);
        statsMap.put("attendanceRate", attendanceRate);
        statsMap.put("averageRating", averageRating);
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("total", totalStudents);
        result.put("stats", statsMap);
        result.put("list", pageList);
        return result;
    }

    public Map<String, Object> getSchedule(Integer instructorId, String dateStr, Integer pageNo, Integer pageSize) {
        String dateParam;
        String wd;
        Instructor instructor = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>().eq("id", (Object)instructorId).isNull("delete_time").last("limit 1"));
        if (instructor == null) {
            throw new OperateException("\u6559\u7ec3\u4fe1\u606f\u4e0d\u5b58\u5728");
        }
        QueryWrapper<CourseClass> classQw = new QueryWrapper<CourseClass>();
        classQw.eq("instructor_id", (Object)instructor.getId());
        classQw.eq("is_delete", (Object)0);
        classQw.orderByDesc("id");
        List<CourseClass> classes = this.courseClassMapper.selectList((Wrapper)classQw);
        ArrayList<Integer> classIds = new ArrayList<Integer>();
        LinkedHashMap<Integer, CourseClass> classMap = new LinkedHashMap<Integer, CourseClass>();
        for (CourseClass cls : classes) {
            classIds.add(cls.getId());
            classMap.put(cls.getId(), cls);
        }
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, Object> stats = new LinkedHashMap<String, Object>();
        stats.put("activeClasses", classes.size());
        long totalLessons = this.courseLessonLogMapper.selectCount(new QueryWrapper<CourseLessonLog>().eq("instructor_id", (Object)instructor.getId()).isNull("delete_time").inSql("course_class_id", "SELECT id FROM la_course_class"));
        stats.put("totalLessons", totalLessons);
        BigDecimal ratingScore = instructor.getRatingScore();
        stats.put("rating", ratingScore != null ? String.format("%.1f", ratingScore) : "0.0");
        result.put("stats", stats);
        Calendar cal = Calendar.getInstance();
        cal.set(7, 1);
        ArrayList<Object> weekCalendar = new ArrayList<Object>();
        String[] weekLabels = new String[]{"\u65e5", "\u4e00", "\u4e8c", "\u4e09", "\u56db", "\u4e94", "\u516d"};
        Calendar todayCal = Calendar.getInstance();
        int todayDate = todayCal.get(5);
        int todayMonth = todayCal.get(2);
        int todayYear = todayCal.get(1);
        SimpleDateFormat rangeSdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 7; ++i) {
            LinkedHashMap<String, Object> dayMap = new LinkedHashMap<String, Object>();
            dayMap.put("label", weekLabels[i]);
            dayMap.put("date", cal.get(5));
            boolean isToday = cal.get(5) == todayDate && cal.get(2) == todayMonth && cal.get(1) == todayYear;
            dayMap.put("isToday", isToday);
            int isoDay = i == 0 ? 7 : i;
            String calDateStr = rangeSdf.format(cal.getTime());
            boolean hasCourse = false;
            for (CourseClass cls : classes) {
                String endStr;
                String startStr;
                wd = cls.getWeekDays();
                if (wd == null || !wd.contains(String.valueOf(isoDay))) continue;
                boolean inRange = true;
                if (cls.getStartDate() != null && calDateStr.compareTo(startStr = rangeSdf.format(new Date((long)cls.getStartDate().intValue() * 1000L))) < 0) {
                    inRange = false;
                }
                if (cls.getEndDate() != null && calDateStr.compareTo(endStr = rangeSdf.format(new Date((long)cls.getEndDate().intValue() * 1000L))) > 0) {
                    inRange = false;
                }
                if (!inRange) continue;
                hasCourse = true;
                break;
            }
            if (!hasCourse && !classIds.isEmpty()) {
                Long lessonCount = this.courseLessonMapper.selectCount(new QueryWrapper<CourseLesson>().in("class_id", classIds).eq("lesson_date", (Object)calDateStr).ne("status", (Object)2));
                hasCourse = lessonCount != null && lessonCount > 0L;
            }
            dayMap.put("hasCourse", hasCourse);
            weekCalendar.add(dayMap);
            cal.add(5, 1);
        }
        result.put("weekDays", weekCalendar);
        ArrayList courseDateRanges = new ArrayList();
        for (CourseClass cls : classes) {
            LinkedHashMap<String, String> rangeMap = new LinkedHashMap<String, String>();
            rangeMap.put("weekDays", cls.getWeekDays());
            rangeMap.put("startDate", cls.getStartDate() != null ? rangeSdf.format(new Date((long)cls.getStartDate().intValue() * 1000L)) : "");
            rangeMap.put("endDate", cls.getEndDate() != null ? rangeSdf.format(new Date((long)cls.getEndDate().intValue() * 1000L)) : "");
            courseDateRanges.add(rangeMap);
        }
        result.put("courseDateRanges", courseDateRanges);
        Integer targetWeekDay = null;
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                String[] parts = dateStr.split("-");
                Calendar parsed = Calendar.getInstance();
                parsed.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]));
                int dow = parsed.get(7);
                targetWeekDay = dow == 1 ? 7 : dow - 1;
            }
            catch (Exception e) {
                targetWeekDay = null;
            }
        }
        ArrayList<Map> courseList = new ArrayList<Map>();
        String string = dateParam = dateStr != null && !dateStr.isEmpty() ? dateStr : new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (!classIds.isEmpty()) {
            List<CourseLesson> dayLessons = this.courseLessonMapper.selectList(new QueryWrapper<CourseLesson>().in("class_id", classIds).eq("lesson_date", (Object)dateParam).ne("status", (Object)2).orderByAsc("time_slot").orderByAsc("id"));
            for (CourseLesson lesson : dayLessons) {
                Integer coachId;
        CourseClass cls;
                cls = (CourseClass)classMap.get(lesson.getClassId());
                if (cls == null || (ClassScheduleUtil.isTempLesson((CourseLesson)lesson) ? (coachId = lesson.getCoachId() != null && lesson.getCoachId() > 0 ? lesson.getCoachId() : cls.getInstructorId()) == null || !coachId.equals(instructorId) : targetWeekDay != null && ((wd = cls.getWeekDays()) == null || !wd.contains(String.valueOf(targetWeekDay))))) continue;
                courseList.add(this.buildScheduleCourseEntry((CourseClass)cls, lesson, instructor, dateParam));
            }
        }
        ArrayList lessonDates = new ArrayList();
        if (!classIds.isEmpty()) {
            LinkedHashSet<String> dateSet = new LinkedHashSet<String>();
            List<CourseLesson> scheduledLessons = this.courseLessonMapper.selectList(new QueryWrapper<CourseLesson>().in("class_id", classIds).ne("status", (Object)2).select(new String[]{"lesson_date"}));
            for (CourseLesson scheduled : scheduledLessons) {
                if (scheduled.getLessonDate() == null || scheduled.getLessonDate().isEmpty()) continue;
                dateSet.add(scheduled.getLessonDate());
            }
            lessonDates.addAll(dateSet);
        }
        result.put("lessonDates", lessonDates);
        int fromIndex = (pageNo - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, courseList.size());
        List pagedList = fromIndex < courseList.size() ? courseList.subList(fromIndex, toIndex) : new ArrayList();
        result.put("courses", pagedList);
        result.put("count", courseList.size());
        return result;
    }

    private Map<String, Object> buildScheduleCourseEntry(CourseClass cls, CourseLesson lesson, Instructor instructor, String dateParam) {
        LinkedHashMap<String, Object> m = new LinkedHashMap<String, Object>();
        m.put("id", cls.getId());
        m.put("lessonId", lesson.getId());
        m.put("lessonNo", lesson.getLessonNo() != null ? lesson.getLessonNo() : 0);
        m.put("lessonType", lesson.getLessonType() != null ? lesson.getLessonType() : 0);
        m.put("lessonTypeLabel", ClassScheduleUtil.isTempLesson((CourseLesson)lesson) ? "\u4e34\u65f6\u52a0\u8bfe" : "\u6b63\u8bfe");
        String lessonTimeSlot = ClassScheduleUtil.resolveLessonTimeSlot((CourseLesson)lesson, (CourseClass)cls);
        m.put("time", lessonTimeSlot);
        m.put("name", cls.getName());
        m.put("image", UrlUtils.toAbsoluteUrl((String)cls.getImage()));
        m.put("venue", ClassScheduleUtil.resolveLessonVenue((CourseLesson)lesson, (CourseClass)cls));
        m.put("studentCount", cls.getStudentCount());
        String statusLabel = "\u672a\u5f00\u59cb";
        if (lesson.getStatus() != null && lesson.getStatus() == 2) {
            statusLabel = "\u5df2\u53d6\u6d88";
        } else if (lesson.getStatus() != null && lesson.getStatus() == 1) {
            statusLabel = "\u5df2\u7ed3\u8bfe";
        } else if (lessonTimeSlot != null && lessonTimeSlot.contains("-")) {
            try {
                String[] timeParts = lessonTimeSlot.split("-");
                LocalTime startT = LocalTime.parse(timeParts[0].trim());
                LocalTime endT = LocalTime.parse(timeParts[1].trim());
                LocalTime nowT = LocalTime.now();
                if (dateParam.equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {
                    if (!nowT.isBefore(startT) && nowT.isBefore(endT)) {
                        statusLabel = "\u8fdb\u884c\u4e2d";
                    } else if (!nowT.isBefore(endT)) {
                        statusLabel = "\u5df2\u7ed3\u675f";
                    }
                }
            }
            catch (Exception timeParts) {
                // empty catch block
            }
        }
        m.put("status", statusLabel);
        long totalBooked = this.courseBookingMapper.selectCount(new QueryWrapper<CourseBooking>().eq("course_class_id", (Object)cls.getId()).eq("lesson_date", (Object)dateParam).ne("status", (Object)3));
        long checkedIn = this.courseBookingMapper.selectCount(new QueryWrapper<CourseBooking>().eq("course_class_id", (Object)cls.getId()).eq("lesson_date", (Object)dateParam).eq("status", (Object)10));
        m.put("attendance", checkedIn + "/" + totalBooked + "\u7b7e\u5230");
        m.put("rating", (instructor.getRatingScore() != null ? instructor.getRatingScore() : "0.0") + "\u8bc4\u5206");
        boolean isCancelled = "\u5df2\u53d6\u6d88".equals(statusLabel);
        boolean coachChecked = lesson.getCoachCheckIn() != null && lesson.getCoachCheckIn() == 1;
        m.put("coachCheckIn", coachChecked ? 1 : 0);
        ArrayList buttons = new ArrayList();
        LinkedHashMap<String, String> b1 = new LinkedHashMap<String, String>();
        if (isCancelled) {
            b1.put("label", "\u7b7e\u5230");
            b1.put("bg", "#C0C0C0");
            b1.put("color", "#fff");
            b1.put("action", "none");
        } else if (coachChecked) {
            b1.put("label", "\u5df2\u7b7e\u5230");
            b1.put("bg", "#C0C0C0");
            b1.put("color", "#fff");
            b1.put("action", "none");
        } else {
            b1.put("label", "\u7b7e\u5230");
            b1.put("bg", "#32DA3B");
            b1.put("color", "#fff");
            b1.put("action", "coachCheckIn");
        }
        LinkedHashMap<String, String> b2 = new LinkedHashMap<String, String>();
        if (isCancelled) {
            b2.put("label", "\u70b9\u540d");
            b2.put("bg", "#C0C0C0");
            b2.put("color", "#fff");
            b2.put("action", "none");
        } else {
            b2.put("label", "\u70b9\u540d");
            b2.put("bg", "#1A466B");
            b2.put("color", "#fff");
            b2.put("action", "attendance");
        }
        buttons.add(b1);
        buttons.add(b2);
        m.put("buttons", buttons);
        return m;
    }

    private void validateLessonTime(CourseClass cls, CourseLesson lesson, int now, String dateStr, int mode) {
        block15: {
            String timeSlot = ClassScheduleUtil.resolveLessonTimeSlot((CourseLesson)lesson, (CourseClass)cls);
            if (timeSlot == null || timeSlot.isEmpty()) {
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String todayStr = sdf.format(new Date((long)now * 1000L));
            int dateCompare = todayStr.compareTo(dateStr);
            if (dateCompare < 0) {
                throw new OperateException("\u672a\u5230\u8bfe\u8282\u5b89\u6392\u65e5\u671f\uff0c\u65e0\u6cd5\u64cd\u4f5c");
            }
            if (dateCompare > 0 && mode != 3) {
                throw new OperateException("\u53ea\u80fd\u5728\u8bfe\u8282\u5b89\u6392\u7684\u5f53\u5929\u624d\u53ef\u4ee5\u64cd\u4f5c");
            }
            String[] parts = timeSlot.split("-");
            if (parts.length != 2) {
                return;
            }
            try {
                String[] startParts = parts[0].trim().split(":");
                String[] endParts = parts[1].trim().split(":");
                int startHour = Integer.parseInt(startParts[0]);
                int startMinute = Integer.parseInt(startParts[1]);
                int endHour = Integer.parseInt(endParts[0]);
                int endMinute = Integer.parseInt(endParts[1]);
                int startMinTotal = startHour * 60 + startMinute;
                int endMinTotal = endHour * 60 + endMinute;
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis((long)now * 1000L);
                int currentMinTotal = cal.get(11) * 60 + cal.get(12);
                if (mode == 1) {
                    if (currentMinTotal < startMinTotal - 30) {
                        throw new OperateException("\u672a\u5230\u7b7e\u5230\u65f6\u95f4\uff0c\u8bf7\u5728\u4e0a\u8bfe\u524d30\u5206\u949f\u5185\u6216\u4e0a\u8bfe\u671f\u95f4\u7b7e\u5230");
                    }
                    if (currentMinTotal > endMinTotal) {
                        throw new OperateException("\u8bfe\u7a0b\u5df2\u7ed3\u675f\uff0c\u65e0\u6cd5\u7b7e\u5230");
                    }
                } else if (mode == 2) {
                    if (currentMinTotal < startMinTotal) {
                        throw new OperateException("\u672a\u5230\u4e0a\u8bfe\u65f6\u95f4\uff0c\u65e0\u6cd5\u8fdb\u884c\u5b66\u5458\u70b9\u540d/\u7b7e\u5230");
                    }
                    if (currentMinTotal > endMinTotal) {
                        throw new OperateException("\u8bfe\u7a0b\u5df2\u7ed3\u675f\uff0c\u65e0\u6cd5\u8fdb\u884c\u5b66\u5458\u70b9\u540d/\u7b7e\u5230");
                    }
                } else if (mode == 3 && dateCompare == 0 && currentMinTotal <= endMinTotal) {
                    throw new OperateException("\u8bfe\u7a0b\u5c1a\u672a\u7ed3\u675f\uff0c\u65e0\u6cd5\u63d0\u4ea4\u8bfe\u5802\u603b\u7ed3");
                }
            }
            catch (Exception e) {
                if (!(e instanceof OperateException)) break block15;
                throw (OperateException)e;
            }
        }
    }

    public void coachCheckIn(Integer instructorId, Integer lessonId, Integer classId) {
        CourseLesson lesson = null;
        if (lessonId != null) {
            lesson = (CourseLesson)this.courseLessonMapper.selectById((Serializable)lessonId);
        } else if (classId != null) {
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            lesson = (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>().eq("class_id", (Object)classId).eq("lesson_date", (Object)today).last("limit 1"));
        }
        if (lesson == null) {
            throw new OperateException("\u672a\u627e\u5230\u8bfe\u8282\u4fe1\u606f");
        }
        CourseClass cls = (CourseClass)this.courseClassMapper.selectById((Serializable)lesson.getClassId());
        this.assertCoachOwnsClass(instructorId, lesson.getClassId());
        this.validateLessonTime(cls, lesson, (int)(System.currentTimeMillis() / 1000L), lesson.getLessonDate(), 1);
        lesson.setCoachCheckIn(Integer.valueOf(1));
        lesson.setUpdateTime(Integer.valueOf((int)(System.currentTimeMillis() / 1000L)));
        this.courseLessonMapper.updateById(lesson);
    }

    public Map<String, Object> getAttendanceData(Integer instructorId, Integer classId, String date, Integer lessonId) {
        CourseLessonLog existLog;
        CourseClass cls = (CourseClass)this.courseClassMapper.selectById((Serializable)classId);
        if (cls == null) {
            throw new OperateException("\u73ed\u7ea7\u4e0d\u5b58\u5728!");
        }
        this.assertCoachOwnsClass(instructorId, classId);
        if (date == null || date.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format(new Date());
        }
        CourseLesson todayLesson = null;
        if (lessonId != null && lessonId > 0 && (todayLesson = (CourseLesson)this.courseLessonMapper.selectById((Serializable)lessonId)) != null && !todayLesson.getClassId().equals(classId)) {
            todayLesson = null;
        }
        if (todayLesson == null) {
            todayLesson = (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>().eq("class_id", (Object)classId).eq("lesson_date", (Object)date).orderByAsc("lesson_type").orderByAsc("id").last("limit 1"));
        }
        int lessonNo = todayLesson != null ? todayLesson.getLessonNo() : 0;
        Integer currentLessonId = todayLesson != null ? todayLesson.getId() : null;
        List<UserCourse> allUc = this.getConsumableUserCourses(null, classId);
        List<CourseBooking> bookings = this.courseBookingMapper.selectList(new QueryWrapper<CourseBooking>().eq("course_class_id", (Object)classId).eq("lesson_date", (Object)date).orderByAsc("id"));
        if (lessonNo == 0 && !bookings.isEmpty()) {
            int n = lessonNo = ((CourseBooking)bookings.get(0)).getLessonNo() != null ? ((CourseBooking)bookings.get(0)).getLessonNo() : 0;
        }
        if (currentLessonId == null && !bookings.isEmpty()) {
            currentLessonId = ((CourseBooking)bookings.get(0)).getLessonId();
        }
        // #170①：名单主体=该课节预约学员（booking 驱动）；班级全量可消课账户降为第二分组
        // 「未预约（现场单候选）」，供旧客户端走现场补建（#170②保留）。到课统计只按预约口径。
        LinkedHashMap<Integer, CourseBooking> bookingByStudent = new LinkedHashMap<Integer, CourseBooking>();
        LinkedHashMap<Integer, CourseBooking> bookingByUser = new LinkedHashMap<Integer, CourseBooking>();
        for (CourseBooking b : bookings) {
            if (b.getStatus() != null && b.getStatus() == 3) continue;
            if (b.getStudentId() != null && b.getStudentId() > 0) {
                bookingByStudent.putIfAbsent(b.getStudentId(), b);
                continue;
            }
            if (b.getUserId() != null && b.getUserId() > 0) {
                bookingByUser.putIfAbsent(b.getUserId(), b);
            }
        }
        Set<Integer> rosterStudentIds = new LinkedHashSet<Integer>(bookingByStudent.keySet());
        ArrayList studentList = new ArrayList();
        int arrived = 0;
        for (CourseBooking booking : bookingByStudent.values()) {
            User user = (User)this.userMapper.selectById((Serializable)booking.getUserId());
            if (user == null) continue;
            Student stu = (Student)this.studentMapper.selectById((Serializable)booking.getStudentId());
            int status = booking.getStatus() != null ? booking.getStatus() : 1;
            if (status == 10) {
                ++arrived;
            }
            studentList.add(this.attendanceStudentRow(user, stu, booking.getStudentId(), booking, cls, status, true));
        }
        // 老数据形态：无 student_id 的跨学员历史单，按 userId 只读呈现（#170 不改历史）。
        for (CourseBooking booking : bookingByUser.values()) {
            User user = (User)this.userMapper.selectById((Serializable)booking.getUserId());
            if (user == null) continue;
            int status = booking.getStatus() != null ? booking.getStatus() : 1;
            if (status == 10) {
                ++arrived;
            }
            studentList.add(this.attendanceStudentRow(user, null, null, booking, cls, status, true));
        }
        // #170③：候选账户匹配不到本人预约时不再回退 userId 借他人预约，按「未预约」呈现。
        for (UserCourse uc : allUc) {
            if (uc.getStudentId() != null && uc.getStudentId() > 0 && rosterStudentIds.contains(uc.getStudentId())) continue;
            if ((uc.getStudentId() == null || uc.getStudentId() <= 0) && bookingByUser.containsKey(uc.getUserId())) continue;
            User user = (User)this.userMapper.selectById((Serializable)uc.getUserId());
            if (user == null) continue;
            Student stu = uc.getStudentId() != null && uc.getStudentId() > 0 ? (Student)this.studentMapper.selectById((Serializable)uc.getStudentId()) : null;
            studentList.add(this.attendanceStudentRow(user, stu, uc.getStudentId(), null, cls, 0, false));
        }
        LinkedHashMap<String, Object> classInfo = new LinkedHashMap<String, Object>();
        classInfo.put("name", cls.getName());
        classInfo.put("timeSlot", ClassScheduleUtil.resolveLessonTimeSlot((CourseLesson)todayLesson, (CourseClass)cls));
        classInfo.put("coachName", cls.getInstructorName());
        classInfo.put("image", UrlUtils.toAbsoluteUrl((String)(cls.getImage() != null ? cls.getImage() : "")));
        classInfo.put("date", date);
        classInfo.put("lessonNo", lessonNo);
        classInfo.put("lessonId", currentLessonId);
        classInfo.put("lessonType", todayLesson != null && todayLesson.getLessonType() != null ? todayLesson.getLessonType() : 0);
        classInfo.put("venue", ClassScheduleUtil.resolveLessonVenue((CourseLesson)todayLesson, (CourseClass)cls));
        LinkedHashMap<String, Integer> stats = new LinkedHashMap<String, Integer>();
        int expected = bookingByStudent.size() + bookingByUser.size();
        stats.put("expected", expected);
        stats.put("arrived", arrived);
        stats.put("absent", Math.max(expected - arrived, 0));
        LinkedHashMap<String, Object> lessonLog = new LinkedHashMap<String, Object>();
        boolean lessonCompleted = false;
        if (lessonNo > 0 && (existLog = (CourseLessonLog)this.courseLessonLogMapper.selectOne(new QueryWrapper<CourseLessonLog>().eq("course_class_id", (Object)classId).eq("lesson_no", (Object)lessonNo).isNull("delete_time").last("limit 1"))) != null) {
            lessonCompleted = true;
            lessonLog.put("summary", existLog.getSummary());
            String rawImages = existLog.getImages();
            if (rawImages != null && !rawImages.isEmpty()) {
                String[] parts = rawImages.split(",");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < parts.length; ++i) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    sb.append(UrlUtils.toAbsoluteUrl((String)parts[i].trim()));
                }
                lessonLog.put("images", sb.toString());
            } else {
                lessonLog.put("images", "");
            }
            lessonLog.put("createTime", existLog.getCreateTime());
        }
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("classInfo", classInfo);
        result.put("stats", stats);
        result.put("students", studentList);
        result.put("lessonCompleted", lessonCompleted);
        result.put("lessonLog", lessonLog);
        return result;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    /**
     * 老点名名单行组装（#170 收敛）。booked=true 表示该行来自本场预约（含老数据无
     * student_id 的跨学员历史单，只读呈现）；booked=false 表示班级可消课账户但无本场
     * 预约（现场单候选，#170② 保留现场补建）。不再按 userId 回退借他人预约（#170③）。
     */
    private LinkedHashMap<String, Object> attendanceStudentRow(User user, Student stu, Integer studentId,
                                                               CourseBooking booking, CourseClass cls,
                                                               int status, boolean booked) {
        LinkedHashMap<String, Object> s = new LinkedHashMap<String, Object>();
        s.put("userId", user.getId());
        s.put("bookingId", booking != null ? booking.getId() : null);
        s.put("studentId", studentId != null && studentId > 0 ? studentId : null);
        String studentName = stu != null && stu.getName() != null && !stu.getName().isEmpty() ? stu.getName() : null;
        if (studentName == null) {
            studentName = user.getNickname() != null ? user.getNickname() : "";
        }
        s.put("name", studentName);
        String studentAvatar = stu != null && stu.getAvatar() != null ? stu.getAvatar() : "";
        String userAvatar = user.getAvatar() != null ? user.getAvatar() : "";
        s.put("studentAvatar", UrlUtils.toAbsoluteUrl((String)studentAvatar));
        s.put("userAvatar", UrlUtils.toAbsoluteUrl((String)userAvatar));
        s.put("avatar", UrlUtils.toAbsoluteUrl((String)(!studentAvatar.isEmpty() ? studentAvatar : userAvatar)));
        s.put("className", cls.getName());
        s.put("status", status);
        String statusLabel = "\u70b9\u540d";
        if (status == 10) {
            statusLabel = "\u5df2\u70b9\u540d";
        } else if (status == 1) {
            statusLabel = "\u9884\u7ea6\u70b9\u540d";
        }
        if (!booked) {
            statusLabel = "\u672a\u9884\u7ea6";
        }
        s.put("statusLabel", statusLabel);
        s.put("booked", booked);
        return s;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Transactional(rollbackFor={Exception.class})
    public Map<String, Object> changeAttendanceStatus(Integer instructorId, Integer classId, Integer userId, Integer studentId, Integer status, String date, Integer lessonId, Integer targetLessonNo) {
        if (date == null || date.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format(new Date());
        }
        CourseClass cls = (CourseClass)this.courseClassMapper.selectById((Serializable)classId);
        this.assertCoachOwnsClass(instructorId, classId);
        int now = (int)(System.currentTimeMillis() / 1000L);
        CourseLesson todayLesson = (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>().eq("class_id", (Object)classId).eq("lesson_date", (Object)date).last("limit 1"));
        this.validateLessonTime(cls, todayLesson, now, date, 2);
        // #170③：studentId 缺省且该家长当天有多条预约时禁止任选（老实现 limit 1 会点到别人）。
        if (studentId == null || studentId <= 0) {
            Long sameDayBookings = this.courseBookingMapper.selectCount(new QueryWrapper<CourseBooking>().eq("course_class_id", (Object)classId).eq("user_id", (Object)userId).eq("lesson_date", (Object)date));
            if (sameDayBookings != null && sameDayBookings > 1L) {
                throw new OperateException("\u8be5\u5bb6\u957f\u5f53\u5929\u6709\u591a\u540d\u5b66\u5458\u9884\u7ea6\uff0c\u8bf7\u9009\u62e9\u5177\u4f53\u5b66\u5458\u540e\u70b9\u540d");
            }
        }
        QueryWrapper<CourseBooking> bookingQuery = new QueryWrapper<CourseBooking>().eq("course_class_id", (Object)classId).eq("user_id", (Object)userId).eq("lesson_date", (Object)date);
        if (studentId != null && studentId > 0) {
            bookingQuery.eq("student_id", (Object)studentId);
        }
        bookingQuery.orderByAsc("id");
        bookingQuery.last("limit 1");
        CourseBooking booking = (CourseBooking)this.courseBookingMapper.selectOne((Wrapper)bookingQuery);
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("classId", classId);
        result.put("userId", userId);
        result.put("date", date);
        result.put("status", status);
        boolean deducted = false;
        boolean onSite = false;
        int resolvedLessonNo = 0;
        if (booking == null) {
            // #170②：无预约现场补建保留（上门真实场景），但必须绑定实际核销的 studentId
            //（禁止再按 userId 兜底，#170③），响应显式标记 onSite=true 现场单。
            booking = new CourseBooking();
            booking.setUserId(userId);
            UserCourse uc = this.resolveConsumableUserCourse(userId, classId, studentId, true);
            if (uc == null) {
                throw new OperateException("\u6240\u9009\u5b66\u5458\u6ca1\u6709\u53ef\u6263\u9664\u7684\u6709\u6548\u8bfe\u65f6");
            }
            booking.setStudentId(studentId != null && studentId > 0 ? studentId : this.studentIdValue(uc.getStudentId()));
            booking.setCourseClassId(classId);
            booking.setLessonId(Integer.valueOf(lessonId != null ? lessonId : 0));
            resolvedLessonNo = this.resolveLessonNoForConsume(targetLessonNo, todayLesson, lessonId);
            booking.setLessonNo(Integer.valueOf(resolvedLessonNo));
            booking.setLessonDate(date);
            booking.setStatus(status);
            booking.setCreateTime(Long.valueOf(now));
            booking.setUpdateTime(Long.valueOf(now));
            this.courseBookingMapper.insert(booking);
            onSite = true;
            Integer remain = uc.getRemainLessons() != null ? uc.getRemainLessons() : 0;
            if (remain <= 0) throw new OperateException("\u8be5\u5b66\u5458\u5269\u4f59\u8bfe\u65f6\u4e0d\u8db3\uff0c\u65e0\u6cd5\u8fdb\u884c\u7a7a\u964d\u70b9\u540d");
            uc.setUsedLessons(Integer.valueOf((uc.getUsedLessons() != null ? uc.getUsedLessons() : 0) + 1));
            uc.setRemainLessons(Integer.valueOf(remain - 1));
            if (uc.getRemainLessons() <= 0) {
                uc.setStatus(Integer.valueOf(1));
            }
            uc.setUpdateTime(Long.valueOf(now));
            this.userCourseMapper.updateById(uc);
            CourseConsumeLog ccl = new CourseConsumeLog();
            ccl.setUserId(userId);
            ccl.setStudentId(this.studentIdValue(uc.getStudentId()));
            ccl.setUserCourseId(uc.getId());
            ccl.setCourseClassId(classId);
            ccl.setLessonNo(Integer.valueOf(resolvedLessonNo));
            ccl.setCreateTime(Long.valueOf(now));
            this.courseConsumeLogMapper.insert(ccl);
            this.lessonCommissionService.recordForLessonConsume(uc, cls);
            deducted = true;
            RechargeOrder ro = this.getRechargeOrderForUserCourse(uc, userId, classId);
            if (ro != null && ro.getLessonCount() != null && ro.getLessonCount() > 0) {
                ro.setLessonCount(Integer.valueOf(ro.getLessonCount() - 1));
                ro.setUpdateTime(Long.valueOf(now));
                this.rechargeOrderMapper.updateById(ro);
            }
        } else {
            CourseConsumeLog existing;
            Integer oldStatus = booking.getStatus();
            booking.setStatus(status);
            booking.setUpdateTime(Long.valueOf(now));
            this.courseBookingMapper.updateById(booking);
            if (status != null && status == 10 && (oldStatus == null || oldStatus != 10)) {
                resolvedLessonNo = this.resolveLessonNoForConsume(booking.getLessonNo(), todayLesson, booking.getLessonId());
                Integer targetStudentId = booking.getStudentId() != null && booking.getStudentId() > 0 ? booking.getStudentId() : studentId;
                UserCourse uc = this.resolveConsumableUserCourse(userId, classId, targetStudentId, true);
                if (uc != null && resolvedLessonNo > 0 && (existing = (CourseConsumeLog)this.courseConsumeLogMapper.selectOne((Wrapper)this.buildConsumeLogQuery(userId, classId, Integer.valueOf(resolvedLessonNo), uc.getStudentId()).last("limit 1"))) == null) {
                    Integer remain = uc.getRemainLessons() != null ? uc.getRemainLessons() : 0;
                    if (remain <= 0) throw new OperateException("\u8be5\u5b66\u5458\u5269\u4f59\u8bfe\u65f6\u4e0d\u8db3\uff0c\u65e0\u6cd5\u70b9\u540d");
                    uc.setUsedLessons(Integer.valueOf((uc.getUsedLessons() != null ? uc.getUsedLessons() : 0) + 1));
                    uc.setRemainLessons(Integer.valueOf(remain - 1));
                    if (uc.getRemainLessons() <= 0) {
                        uc.setStatus(Integer.valueOf(1));
                    }
                    uc.setUpdateTime(Long.valueOf(now));
                    this.userCourseMapper.updateById(uc);
                    CourseConsumeLog ccl = new CourseConsumeLog();
                    ccl.setUserId(userId);
                    ccl.setStudentId(this.studentIdValue(uc.getStudentId()));
                    ccl.setUserCourseId(uc.getId());
                    ccl.setCourseClassId(classId);
                    ccl.setLessonNo(Integer.valueOf(resolvedLessonNo));
                    ccl.setCreateTime(Long.valueOf(now));
                    this.courseConsumeLogMapper.insert(ccl);
                    this.lessonCommissionService.recordForLessonConsume(uc, cls);
                    deducted = true;
                    RechargeOrder ro = this.getRechargeOrderForUserCourse(uc, userId, classId);
                    if (ro != null && ro.getLessonCount() != null && ro.getLessonCount() > 0) {
                        ro.setLessonCount(Integer.valueOf(ro.getLessonCount() - 1));
                        ro.setUpdateTime(Long.valueOf(now));
                        this.rechargeOrderMapper.updateById(ro);
                    }
                }
            }
        }
        result.put("onSite", onSite);
        result.put("bookingId", booking.getId());
        result.put("studentId", booking.getStudentId() != null && booking.getStudentId() > 0 ? booking.getStudentId() : null);
        result.put("lessonNo", resolvedLessonNo);
        result.put("deducted", deducted);
        if (booking.getStudentId() != null && booking.getStudentId() > 0) {
            Student stu = (Student)this.studentMapper.selectById((Serializable)booking.getStudentId());
            result.put("studentName", stu != null && stu.getName() != null ? stu.getName() : "");
        } else {
            result.put("studentName", "");
        }
        return result;
    }

    @Transactional(rollbackFor={Exception.class})
    public void completeLesson(Integer instructorId, Integer classId, String date, Integer lessonId, Integer lessonNo, String summary, String images) {
        Instructor inst;
        CourseLesson lesson;
        Long existCount;
        CourseLesson lesson2;
        CourseClass cls;
        if (date == null || date.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format(new Date());
        }
        if ((cls = (CourseClass)this.courseClassMapper.selectById((Serializable)classId)) == null) {
            throw new OperateException("\u73ed\u7ea7\u4e0d\u5b58\u5728");
        }
        this.assertCoachOwnsClass(instructorId, classId);
        int now = (int)(System.currentTimeMillis() / 1000L);
        CourseLesson dateLesson = (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>().eq("class_id", (Object)classId).eq("lesson_date", (Object)date).last("limit 1"));
        this.validateLessonTime(cls, dateLesson, now, date, 3);
        if (lessonNo != null && lessonNo > 0) {
            if (lessonId == null && (lesson2 = (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>().eq("class_id", (Object)classId).eq("lesson_no", (Object)lessonNo).last("limit 1"))) != null) {
                lessonId = lesson2.getId();
            }
        } else {
            lesson2 = null;
            lesson2 = lessonId != null ? (CourseLesson)this.courseLessonMapper.selectById((Serializable)lessonId) : (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>().eq("class_id", (Object)classId).eq("lesson_date", (Object)date).last("limit 1"));
            if (lesson2 == null || lesson2.getLessonNo() == null || lesson2.getLessonNo() <= 0) {
                throw new OperateException("\u65e0\u6cd5\u83b7\u53d6\u6709\u6548\u7684\u8bfe\u65f6\u53f7\uff0c\u65e0\u6cd5\u63d0\u4ea4");
            }
            lessonNo = lesson2.getLessonNo();
            if (lessonId == null) {
                lessonId = lesson2.getId();
            }
        }
        if ((existCount = this.courseLessonLogMapper.selectCount(new QueryWrapper<CourseLessonLog>().eq("course_class_id", (Object)classId).eq("lesson_no", (Object)lessonNo))) != null && existCount > 0L) {
            throw new OperateException("\u7b2c" + lessonNo + "\u8282\u8bfe\u5802\u5c0f\u7ed3\u5df2\u63d0\u4ea4\uff0c\u8bf7\u52ff\u91cd\u590d\u63d0\u4ea4");
        }
        CourseLessonLog log = new CourseLessonLog();
        log.setCourseClassId(classId);
        log.setInstructorId(instructorId);
        log.setLessonNo(lessonNo);
        log.setSummary(summary != null ? summary : "");
        log.setImages(images != null ? images : "");
        log.setCreateTime(Integer.valueOf(now));
        log.setUpdateTime(Integer.valueOf(now));
        this.courseLessonLogMapper.insert(log);
        if (lessonId != null && (lesson = (CourseLesson)this.courseLessonMapper.selectById((Serializable)lessonId)) != null) {
            lesson.setStatus(Integer.valueOf(1));
            lesson.setUpdateTime(Integer.valueOf(now));
            this.courseLessonMapper.updateById(lesson);
            CourseTempLesson tempLesson = (CourseTempLesson)this.courseTempLessonMapper.selectOne(new QueryWrapper<CourseTempLesson>().eq("lesson_id", (Object)lessonId).last("limit 1"));
            if (tempLesson != null) {
                tempLesson.setStatus(Integer.valueOf(1));
                tempLesson.setUpdateTime(Integer.valueOf(now));
                this.courseTempLessonMapper.updateById(tempLesson);
            }
        }
        QueryWrapper<CourseAttendance> attQw = new QueryWrapper<CourseAttendance>();
        attQw.eq("class_id", (Object)classId);
        attQw.eq("lesson_date", (Object)date);
        attQw.ne("status", (Object)2);
        List<CourseAttendance> needConsumeAtts = this.courseAttendanceMapper.selectList((Wrapper)attQw);
        List<UserCourse> allUc = this.getConsumableUserCourses(null, classId);
        List<CourseAttendance> leaveAtts = this.courseAttendanceMapper.selectList(new QueryWrapper<CourseAttendance>().eq("class_id", (Object)classId).eq("lesson_date", (Object)date).eq("status", (Object)2));
        Set leaveUserIds = leaveAtts.stream().map(CourseAttendance::getUserId).collect(Collectors.toSet());
        List<CourseBooking> dayBookings = this.courseBookingMapper.selectList(new QueryWrapper<CourseBooking>().eq("course_class_id", (Object)classId).eq("lesson_date", (Object)date).eq("status", (Object)10));
        Set bookedStudentKeys = dayBookings.stream().filter(booking -> booking.getStudentId() != null && booking.getStudentId() > 0).map(booking -> this.userStudentKey(booking.getUserId(), booking.getStudentId())).collect(Collectors.toSet());
        Set bookedUserIds = dayBookings.stream().map(CourseBooking::getUserId).collect(Collectors.toSet());
        for (UserCourse uc : allUc) {
            Integer uid = uc.getUserId();
            if (leaveUserIds.contains(uid) || (uc.getStudentId() == null || uc.getStudentId() <= 0 ? !bookedUserIds.contains(uid) : !bookedStudentKeys.contains(this.userStudentKey(uid, uc.getStudentId())))) continue;
            CourseConsumeLog existingLog = (CourseConsumeLog)this.courseConsumeLogMapper.selectOne((Wrapper)this.buildConsumeLogQuery(uid, classId, lessonNo, uc.getStudentId()).last("limit 1"));
            if (existingLog == null) {
                Integer remain = uc.getRemainLessons() != null ? uc.getRemainLessons() : 0;
                Integer used = uc.getUsedLessons() != null ? uc.getUsedLessons() : 0;
                Integer total = uc.getTotalLessons() != null ? uc.getTotalLessons() : 0;
                if (remain == 0 && used == 0 && total > 0) {
                    remain = total;
                }
                if (total == 0 && cls != null && cls.getTotalLessons() != null) {
                    total = cls.getTotalLessons();
                    if (remain == 0 && used == 0) {
                        remain = total;
                    }
                    uc.setTotalLessons(total);
                }
                if (remain <= 0) continue;
                uc.setRemainLessons(Integer.valueOf(remain - 1));
                uc.setUsedLessons(Integer.valueOf(used + 1));
                if (uc.getRemainLessons() <= 0) {
                    uc.setStatus(Integer.valueOf(1));
                }
                uc.setUpdateTime(Long.valueOf(now));
                this.userCourseMapper.updateById(uc);
                CourseConsumeLog ccl = new CourseConsumeLog();
                ccl.setUserId(uid);
                ccl.setStudentId(this.studentIdValue(uc.getStudentId()));
                ccl.setUserCourseId(uc.getId());
                ccl.setCourseClassId(classId);
                ccl.setLessonNo(lessonNo);
                ccl.setCreateTime(Long.valueOf(now));
                this.courseConsumeLogMapper.insert(ccl);
                this.lessonCommissionService.recordForLessonConsume((UserCourse)uc, cls);
                RechargeOrder ro = this.getRechargeOrderForUserCourse((UserCourse)uc, uid, classId);
                if (ro == null || ro.getLessonCount() == null || ro.getLessonCount() <= 0) continue;
                ro.setLessonCount(Integer.valueOf(ro.getLessonCount() - 1));
                ro.setUpdateTime(Long.valueOf(now));
                this.rechargeOrderMapper.updateById(ro);
                continue;
            }
            if (uc.getRemainLessons() == null || uc.getRemainLessons() > 0 || uc.getStatus() != null && uc.getStatus() != 0) continue;
            uc.setStatus(Integer.valueOf(1));
            uc.setUpdateTime(Long.valueOf(now));
            this.userCourseMapper.updateById(uc);
        }
        List<CourseBooking> noShowBookings = this.courseBookingMapper.selectList(new QueryWrapper<CourseBooking>().eq("course_class_id", (Object)classId).eq("lesson_date", (Object)date).eq("status", (Object)1));
        for (CourseBooking noShow : noShowBookings) {
            UserCourse uc;
            int deleted = this.courseConsumeLogMapper.delete((Wrapper)this.buildConsumeLogQuery(noShow.getUserId(), classId, lessonNo, noShow.getStudentId()));
            if (deleted <= 0 || (uc = this.resolveRestorableUserCourse(noShow.getUserId(), classId, noShow.getStudentId())) == null) continue;
            int used = uc.getUsedLessons() != null ? uc.getUsedLessons() : 0;
            int remain = uc.getRemainLessons() != null ? uc.getRemainLessons() : 0;
            uc.setUsedLessons(Integer.valueOf(Math.max(used - 1, 0)));
            uc.setRemainLessons(Integer.valueOf(remain + 1));
            if (uc.getStatus() != null && uc.getStatus() == 1 && uc.getRemainLessons() > 0) {
                uc.setStatus(Integer.valueOf(0));
            }
            uc.setUpdateTime(Long.valueOf(now));
            this.userCourseMapper.updateById(uc);
            RechargeOrder ro = this.getRechargeOrderForUserCourse(uc, noShow.getUserId(), classId);
            if (ro == null) continue;
            ro.setLessonCount(Integer.valueOf((ro.getLessonCount() != null ? ro.getLessonCount() : 0) + 1));
            ro.setUpdateTime(Long.valueOf(now));
            this.rechargeOrderMapper.updateById(ro);
        }
        BigDecimal feePerLesson = cls.getFeePerLesson();
        if (feePerLesson != null && feePerLesson.compareTo(BigDecimal.ZERO) > 0 && (inst = (Instructor)this.instructorMapper.selectById((Serializable)instructorId)) != null) {
            BigDecimal beforeBalance = inst.getBalance() != null ? inst.getBalance() : BigDecimal.ZERO;
            BigDecimal afterBalance = beforeBalance.add(feePerLesson);
            inst.setBalance(afterBalance);
            this.instructorMapper.updateById(inst);
            InstructorAccountLog accountLog = new InstructorAccountLog();
            accountLog.setInstructorId(instructorId);
            accountLog.setType(Integer.valueOf(1));
            accountLog.setAmount(feePerLesson);
            accountLog.setBeforeBalance(beforeBalance);
            accountLog.setAfterBalance(afterBalance);
            accountLog.setRelationId(log.getId());
            accountLog.setRemark("\u8bfe\u65f6\u6536\u5165: " + cls.getName() + " \u7b2c" + lessonNo + "\u8282");
            accountLog.setCreateTime(Long.valueOf(now));
            this.instructorAccountLogMapper.insert(accountLog);
            Integer classCount = inst.getClassCount() != null ? inst.getClassCount() : 0;
            inst.setClassCount(Integer.valueOf(classCount + 1));
            this.instructorMapper.updateById(inst);
        }
        for (UserCourse uc : allUc) {
            if (leaveUserIds.contains(uc.getUserId())) continue;
            Message msg = new Message();
            msg.setType(Integer.valueOf(5));
            msg.setTitle("\u8bfe\u7a0b\u7ed3\u675f\u901a\u77e5");
            msg.setContent("\u60a8\u7684\u8bfe\u7a0b\u3010" + cls.getName() + "\u3011\u7b2c" + lessonNo + "\u8282\u5df2\u7ed3\u8bfe\uff0c\u4e14\u5df2\u6263\u96641\u4e2a\u5269\u4f59\u8bfe\u65f6\u3002");
            msg.setSenderId(Integer.valueOf(0));
            msg.setSenderType("system");
            msg.setSenderName("\u7cfb\u7edf");
            msg.setReceiverId(uc.getUserId());
            msg.setReceiverType("student");
            msg.setIsRead(Integer.valueOf(0));
            msg.setCreateTime(Long.valueOf(now));
            msg.setUpdateTime(Long.valueOf(now));
            this.messageMapper.insert(msg);
        }
    }

    public Map<String, Object> getReportData(Integer instructorId) {
        List<CourseClass> classes = this.courseClassMapper.selectList(new QueryWrapper<CourseClass>().eq("instructor_id", (Object)instructorId).isNotNull("name").eq("is_delete", (Object)0).orderByDesc("id"));
        ArrayList classList = new ArrayList();
        for (CourseClass cls : classes) {
            LinkedHashMap<String, Object> cmap = new LinkedHashMap<String, Object>();
            cmap.put("id", cls.getId());
            cmap.put("name", cls.getName() != null ? cls.getName() : "\u672a\u547d\u540d");
            long studentCount = this.userCourseMapper.selectCount(new QueryWrapper<UserCourse>().eq("course_class_id", (Object)cls.getId()).isNull("delete_time"));
            cmap.put("studentCount", studentCount);
            List<CourseAttendance> atts = this.courseAttendanceMapper.selectList(new QueryWrapper<CourseAttendance>().eq("class_id", (Object)cls.getId()));
            long present = atts.stream().filter(a -> a.getStatus() != null && a.getStatus() == 1).count();
            long absent = atts.stream().filter(a -> a.getStatus() != null && a.getStatus() == 3).count();
            long presentAbsentSum = present + absent;
            int rate = presentAbsentSum > 0L ? (int)((double)present * 100.0 / (double)presentAbsentSum) : 0;
            cmap.put("attendanceRate", rate);
            classList.add(cmap);
        }
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("classData", classList);
        return result;
    }

    public void applyLeave(Integer instructorId, InstructorLeaveApplyValidate validate) {
        if (validate.getStartTime() >= validate.getEndTime()) {
            throw new OperateException("\u7ed3\u675f\u65f6\u95f4\u5fc5\u987b\u665a\u4e8e\u5f00\u59cb\u65f6\u95f4");
        }
        InstructorLeave leave = new InstructorLeave();
        leave.setInstructorId(instructorId);
        leave.setStartTime(validate.getStartTime());
        leave.setEndTime(validate.getEndTime());
        leave.setReason(validate.getReason());
        leave.setDetail(validate.getDetail());
        leave.setStatus(Integer.valueOf(0));
        leave.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        leave.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.instructorLeaveMapper.insert(leave);
    }

    public boolean checkLeave(Integer instructorId, Long startTime, Long endTime) {
        if (startTime == null || endTime == null || startTime >= endTime) {
            return false;
        }
        QueryWrapper<InstructorLeave> qw = new QueryWrapper<InstructorLeave>();
        qw.eq("instructor_id", (Object)instructorId).ne("status", (Object)2).isNull("delete_time").and(wrapper -> wrapper.between("start_time", (Object)startTime, (Object)(endTime - 1L)).or().between("end_time", (Object)(startTime + 1L), (Object)endTime).or().and(w -> w.le("start_time", (Object)startTime).ge("end_time", (Object)endTime))).last("limit 1");
        return this.instructorLeaveMapper.selectCount((Wrapper)qw) > 0L;
    }

    public void cancelLeave(Integer instructorId) {
        long now = System.currentTimeMillis() / 1000L;
        QueryWrapper<InstructorLeave> qw = new QueryWrapper<InstructorLeave>();
        qw.eq("instructor_id", (Object)instructorId).ne("status", (Object)2).ne("status", (Object)4).isNull("delete_time").le("start_time", (Object)now).ge("end_time", (Object)now).last("limit 1");
        InstructorLeave leave = (InstructorLeave)this.instructorLeaveMapper.selectOne((Wrapper)qw);
        if (leave != null) {
            leave.setStatus(Integer.valueOf(4));
            leave.setDeleteTime(Long.valueOf(now));
            leave.setUpdateTime(Long.valueOf(now));
            this.instructorLeaveMapper.updateById(leave);
        }
    }

    public List<Map<String, Object>> completedLessonList(Integer instructorId, String keyword, Integer pageNo, Integer pageSize) {
        Object cc;
        List<CourseLessonLog> logs = this.courseLessonLogMapper.selectList(new QueryWrapper<CourseLessonLog>().eq("instructor_id", (Object)instructorId).isNull("delete_time").orderByDesc("create_time"));
        if (logs.isEmpty()) {
            return new ArrayList<Map<String, Object>>();
        }
        HashSet<Integer> classIdSet = new HashSet<Integer>();
        for (CourseLessonLog log : logs) {
            classIdSet.add(log.getCourseClassId());
        }
        LinkedHashMap<Integer, Object> classMap = new LinkedHashMap<Integer, Object>();
        for (Integer cid : classIdSet) {
            cc = (CourseClass)this.courseClassMapper.selectById((Serializable)cid);
            if (cc == null) continue;
            classMap.put(cid, cc);
        }
        LinkedHashMap<String, CourseLesson> lessonMap = new LinkedHashMap<String, CourseLesson>();
        if (!classIdSet.isEmpty()) {
            List<CourseLesson> allLessons = this.courseLessonMapper.selectList(new QueryWrapper<CourseLesson>().in("class_id", classIdSet));
            for (CourseLesson l : allLessons) {
                lessonMap.put(l.getClassId() + "_" + l.getLessonNo(), l);
            }
        }
        LinkedHashMap<Integer, Long> enrollCountMap = new LinkedHashMap<Integer, Long>();
        Iterator<Integer> ccIterator = classIdSet.iterator();
        while (ccIterator.hasNext()) {
            Integer cid = ccIterator.next();
            Long cnt = this.userCourseMapper.selectCount(new QueryWrapper<UserCourse>().eq("course_class_id", cid).isNull("delete_time"));
            enrollCountMap.put(cid, cnt != null ? cnt : 0L);
        }
        ArrayList allItems = new ArrayList();
        for (CourseLessonLog log : logs) {
            String lessonDate;
            CourseClass cc2 = (CourseClass)classMap.get(log.getCourseClassId());
            if (cc2 == null) continue;
            if (keyword != null && !keyword.isEmpty()) {
                boolean match;
                String lowerKey = keyword.toLowerCase();
                boolean bl = match = cc2.getName() != null && cc2.getName().toLowerCase().contains(lowerKey) || cc2.getVenue() != null && cc2.getVenue().toLowerCase().contains(lowerKey);
                if (!match) continue;
            }
            CourseLesson lesson = (CourseLesson)lessonMap.get(log.getCourseClassId() + "_" + log.getLessonNo());
            long expectedCount = enrollCountMap.getOrDefault(log.getCourseClassId(), 0L);
            long leaveCount = 0L;
            String string = lessonDate = lesson != null ? lesson.getLessonDate() : null;
            if (lessonDate != null) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date lessonDay = sdf.parse(lessonDate);
                    long dayStart = lessonDay.getTime() / 1000L;
                    long dayEnd = dayStart + 86400L;
                    Long lc = this.leaveApplicationMapper.selectCount(new QueryWrapper<LeaveApplication>().eq("class_id", (Object)log.getCourseClassId()).eq("applicant_type", (Object)1).eq("status", (Object)1).le("start_time", (Object)dayEnd).ge("end_time", (Object)dayStart).isNull("delete_time"));
                    leaveCount = lc != null ? lc : 0L;
                }
                catch (Exception sdf) {
                    // empty catch block
                }
            }
            long actualCount = Math.max(0L, expectedCount - leaveCount);
            Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)instructorId);
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("lessonId", lesson != null ? lesson.getId() : null);
            item.put("classId", log.getCourseClassId());
            item.put("className", cc2.getName());
            item.put("image", UrlUtils.toAbsoluteUrl((String)cc2.getImage()));
            item.put("instructorName", cc2.getInstructorName());
            item.put("venue", cc2.getVenue());
            item.put("lessonNo", log.getLessonNo());
            item.put("lessonDate", lessonDate != null ? lessonDate : "");
            item.put("timeSlot", lesson != null ? lesson.getTimeSlot() : cc2.getTimeSlot());
            item.put("weekDay", lesson != null ? lesson.getWeekDay() : "");
            item.put("expectedCount", expectedCount);
            item.put("actualCount", actualCount);
            item.put("rating", instructor != null && instructor.getRatingScore() != null ? instructor.getRatingScore() : Integer.valueOf(0));
            allItems.add(item);
        }
        int from = (pageNo - 1) * pageSize;
        if (from >= allItems.size()) {
            return new ArrayList<Map<String, Object>>();
        }
        int to = Math.min(from + pageSize, allItems.size());
        return allItems.subList(from, to);
    }

    public Map<String, Object> getStudentAccount(Integer instructorId, Integer userId) {
        List<Integer> classIds = this.getInstructorClassIds(instructorId);
        this.assertStudentUnderInstructor(classIds, userId);
        User user = (User)this.userMapper.selectById((Serializable)userId);
        if (user == null || user.getDeleteTime() != null) {
            throw new OperateException("\u5b66\u5458\u4e0d\u5b58\u5728");
        }
        Map<Integer, String> classNameMap = this.courseClassMapper.selectList(new QueryWrapper<CourseClass>().in("id", (Collection)classIds)).stream().collect(Collectors.toMap(CourseClass::getId, CourseClass::getName, (a, b) -> a));
        List<UserCourse> enrollments = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).in("course_class_id", (Collection)classIds).isNull("delete_time").orderByDesc("id"));
        String displayName = this.resolveStudentDisplayName(userId, enrollments, user);
        Map<Integer, Integer> usedByUserCourseId = this.allocateUsedLessonsFifo(userId, enrollments);
        ArrayList courses = new ArrayList();
        int sumTotal = 0;
        int sumUsed = 0;
        int sumRemain = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (UserCourse uc : enrollments) {
            int total = uc.getTotalLessons() != null ? uc.getTotalLessons() : 0;
            int used = usedByUserCourseId.getOrDefault(uc.getId(), 0);
            int remain = Math.max(total - used, 0);
            if (uc.getStatus() == null || uc.getStatus() != 2) {
                sumTotal += total;
                sumUsed += used;
                sumRemain += remain;
            }
            LinkedHashMap<String, Object> courseItem = new LinkedHashMap<String, Object>();
            courseItem.put("userCourseId", uc.getId());
            courseItem.put("courseClassId", uc.getCourseClassId());
            courseItem.put("className", classNameMap.getOrDefault(uc.getCourseClassId(), ""));
            courseItem.put("totalLessons", total);
            courseItem.put("usedLessons", used);
            courseItem.put("remainLessons", remain);
            courseItem.put("status", uc.getStatus());
            courseItem.put("statusText", this.resolveUserCourseStatusText(uc.getStatus()));
            courseItem.put("enrollTime", uc.getCreateTime() != null ? sdf.format(new Date(uc.getCreateTime() * 1000L)) : "");
            courses.add(courseItem);
        }
        LinkedHashMap<String, Integer> summary = new LinkedHashMap<String, Integer>();
        summary.put("purchasedLessons", sumTotal);
        summary.put("usedLessons", sumUsed);
        summary.put("remainLessons", sumRemain);
        LinkedHashMap<String, Object> student = new LinkedHashMap<String, Object>();
        student.put("userId", userId);
        student.put("name", displayName);
        student.put("phone", user.getMobile() != null ? user.getMobile() : "");
        student.put("avatar", UrlUtils.toAbsoluteUrl((String)user.getAvatar()));
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("student", student);
        result.put("summary", summary);
        result.put("courses", courses);
        return result;
    }

    public Map<String, Object> getStudentLessonRecords(Integer instructorId, Integer userId, Integer courseClassId, Integer pageNo, Integer pageSize) {
        List<Integer> classIds = this.getInstructorClassIds(instructorId);
        this.assertStudentUnderInstructor((List)classIds, userId);
        if (courseClassId != null && courseClassId > 0 && !classIds.contains(courseClassId)) {
            throw new OperateException("\u65e0\u6743\u67e5\u770b\u8be5\u73ed\u7ea7\u8bb0\u5f55");
        }
        List<Integer> queryClassIds = courseClassId != null && courseClassId > 0 ? Collections.singletonList(courseClassId) : classIds;
        Map<Integer, String> classNameMap = this.courseClassMapper.selectList(new QueryWrapper<CourseClass>().in("id", queryClassIds)).stream().collect(Collectors.toMap(CourseClass::getId, CourseClass::getName, (a, b) -> a));
        QueryWrapper<CourseConsumeLog> logQw = new QueryWrapper<CourseConsumeLog>();
        logQw.eq("user_id", (Object)userId).in("course_class_id", queryClassIds).orderByDesc("create_time");
        List<CourseConsumeLog> allLogs = this.courseConsumeLogMapper.selectList((Wrapper)logQw);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ArrayList recordList = new ArrayList();
        for (CourseConsumeLog log : allLogs) {
            CourseManualLesson manual;
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
            item.put("className", classNameMap.getOrDefault(log.getCourseClassId(), ""));
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

    private Map<Integer, Integer> allocateUsedLessonsFifo(Integer userId, List<UserCourse> enrollments) {
        LinkedHashMap<Integer, Integer> usedByUserCourseId = new LinkedHashMap<Integer, Integer>();
        if (enrollments == null || enrollments.isEmpty()) {
            return usedByUserCourseId;
        }
        List<CourseConsumeLog> allLogs = this.courseConsumeLogMapper.selectList(new QueryWrapper<CourseConsumeLog>().eq("user_id", (Object)userId));
        ArrayList<CourseConsumeLog> effective = new ArrayList<CourseConsumeLog>();
        for (CourseConsumeLog log : allLogs) {
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
        Map<Integer, List<UserCourse>> groups = enrollments.stream().filter(uc -> uc.getCourseClassId() != null && uc.getCourseClassId() > 0).collect(Collectors.groupingBy(UserCourse::getCourseClassId, LinkedHashMap::new, Collectors.toList()));
        for (Map.Entry<Integer, List<UserCourse>> entry : groups.entrySet()) {
            List<UserCourse> group = entry.getValue();
            Integer classId = entry.getKey();
            ArrayList<CourseConsumeLog> groupLogs = new ArrayList<CourseConsumeLog>();
            for (CourseConsumeLog log : effective) {
                if (log.getCourseClassId() == null || !log.getCourseClassId().equals(classId)) continue;
                groupLogs.add(log);
            }
            usedByUserCourseId.putAll(CourseConsumeLogUtil.allocateUsedByEnrollmentFifo(group, groupLogs));
        }
        return usedByUserCourseId;
    }

    private Map<String, Long> loadConsumeCountByClassStudent(Integer userId) {
        List<CourseConsumeLog> logs = this.courseConsumeLogMapper.selectList(new QueryWrapper<CourseConsumeLog>().eq("user_id", (Object)userId));
        if (logs.isEmpty()) {
            return Collections.emptyMap();
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
        return CourseConsumeLogUtil.groupEffectiveCountByClassStudent(effective);
    }

    private String userStudentKey(Integer userId, Integer studentId) {
        return String.valueOf(userId) + ":" + this.studentIdValue(studentId);
    }

    private Integer studentIdValue(Integer studentId) {
        return studentId != null && studentId > 0 ? studentId : 0;
    }

    private QueryWrapper<CourseConsumeLog> buildConsumeLogQuery(Integer userId, Integer classId, Integer lessonNo, Integer studentId) {
        QueryWrapper<CourseConsumeLog> queryWrapper = new QueryWrapper<CourseConsumeLog>().eq("user_id", (Object)userId).eq("course_class_id", (Object)classId).eq("lesson_no", (Object)lessonNo);
        if (studentId != null && studentId > 0) {
            queryWrapper.eq("student_id", (Object)studentId);
        } else {
            queryWrapper.eq("student_id", (Object)0);
        }
        return queryWrapper;
    }

    private List<UserCourse> getConsumableUserCourses(Integer userId, Integer classId) {
        return this.getEligibleUserCourses(userId, classId, true);
    }

    private List<UserCourse> getEligibleUserCourses(Integer userId, Integer classId, boolean requireRemaining) {
        QueryWrapper<UserCourse> queryWrapper = new QueryWrapper<UserCourse>().eq("course_class_id", (Object)classId).ne("status", (Object)2).isNull("delete_time").orderByAsc("id");
        if (userId != null && userId > 0) {
            queryWrapper.eq("user_id", (Object)userId);
        }
        if (requireRemaining) {
            queryWrapper.and(w -> w.gt("remain_lessons", (Object)0).or().apply("IFNULL(used_lessons,0) < IFNULL(total_lessons,0)", new Object[0]));
        }
        List<UserCourse> candidates = this.userCourseMapper.selectList((Wrapper)queryWrapper);
        LinkedHashMap<String, UserCourse> uniqueStudents = new LinkedHashMap<String, UserCourse>();
        for (UserCourse candidate : candidates) {
            Integer resolvedStudentId;
            this.normalizeConsumableRemain(candidate);
            RechargeOrder linkedOrder = null;
            if (candidate.getOrderId() != null && candidate.getOrderId() > 0) {
                boolean eligible;
                linkedOrder = (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)candidate.getOrderId());
                if (linkedOrder == null) continue;
                eligible = requireRemaining ? UserCourseEligibilityUtil.isConsumable((Integer)candidate.getStatus(), (Integer)candidate.getRemainLessons(), (Integer)linkedOrder.getPayStatus(), (Integer)linkedOrder.getRefundStatus(), (Long)linkedOrder.getDeleteTime()) : (eligible = (candidate.getStatus() == null || candidate.getStatus() != 2) && UserCourseEligibilityUtil.isActiveOrder((Integer)linkedOrder.getPayStatus(), (Integer)linkedOrder.getRefundStatus(), (Long)linkedOrder.getDeleteTime()));
                if (!eligible) {
                    continue;
                }
            } else if (candidate.getStatus() != null && candidate.getStatus() == 2 || requireRemaining && (candidate.getRemainLessons() == null || candidate.getRemainLessons() <= 0)) continue;
            if (((resolvedStudentId = candidate.getStudentId()) == null || resolvedStudentId <= 0) && linkedOrder != null) {
                resolvedStudentId = linkedOrder.getStudentId();
            }
            candidate.setStudentId(this.studentIdValue(resolvedStudentId));
            uniqueStudents.putIfAbsent(this.userStudentKey(candidate.getUserId(), candidate.getStudentId()), candidate);
        }
        return new ArrayList<UserCourse>(uniqueStudents.values());
    }

    private void normalizeConsumableRemain(UserCourse uc) {
        int total;
        if (uc == null) {
            return;
        }
        int remain = uc.getRemainLessons() != null ? uc.getRemainLessons() : 0;
        int used = uc.getUsedLessons() != null ? uc.getUsedLessons() : 0;
        int n = total = uc.getTotalLessons() != null ? uc.getTotalLessons() : 0;
        if (remain <= 0 && total > used) {
            uc.setRemainLessons(Integer.valueOf(total - used));
            if (uc.getStatus() != null && uc.getStatus() == 1) {
                uc.setStatus(Integer.valueOf(0));
            }
        }
    }

    private UserCourse resolveConsumableUserCourse(Integer userId, Integer classId, Integer studentId, boolean requireStudentChoice) {
        List<UserCourse> candidates = this.getConsumableUserCourses(userId, classId);
        if (studentId != null && studentId > 0) {
            for (UserCourse candidate : candidates) {
                if (!studentId.equals(candidate.getStudentId())) continue;
                return candidate;
            }
            return null;
        }
        Set studentIds = candidates.stream().map(UserCourse::getStudentId).map(arg_0 -> this.studentIdValue(arg_0)).collect(Collectors.toSet());
        if (requireStudentChoice && !UserCourseEligibilityUtil.canResolveWithoutStudent((int)studentIds.size())) {
            throw new OperateException("\u8be5\u5bb6\u957f\u6709\u591a\u4e2a\u5b66\u5458\uff0c\u8bf7\u9009\u62e9\u5177\u4f53\u5b66\u5458\u540e\u70b9\u540d");
        }
        return candidates.isEmpty() ? null : (UserCourse)candidates.get(0);
    }

    private UserCourse resolveRestorableUserCourse(Integer userId, Integer classId, Integer studentId) {
        List<UserCourse> candidates = this.getEligibleUserCourses(userId, classId, false);
        if (studentId != null && studentId > 0) {
            for (UserCourse candidate : candidates) {
                if (!studentId.equals(candidate.getStudentId())) continue;
                return candidate;
            }
            return null;
        }
        return candidates.size() == 1 ? (UserCourse)candidates.get(0) : null;
    }

    private RechargeOrder getRechargeOrderForUserCourse(UserCourse userCourse, Integer userId, Integer classId) {
        if (userCourse != null && userCourse.getOrderId() != null && userCourse.getOrderId() > 0) {
            RechargeOrder rechargeOrder = (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)userCourse.getOrderId());
            if (rechargeOrder != null && UserCourseEligibilityUtil.isActiveOrder((Integer)rechargeOrder.getPayStatus(), (Integer)rechargeOrder.getRefundStatus(), (Long)rechargeOrder.getDeleteTime())) {
                return rechargeOrder;
            }
            return null;
        }
        QueryWrapper<UserCourse> queryWrapper = new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).eq("course_class_id", (Object)classId).eq("pay_status", (Object)1).eq("refund_status", (Object)0).isNull("delete_time").orderByAsc("id");
        if (userCourse != null && userCourse.getStudentId() != null && userCourse.getStudentId() > 0) {
            queryWrapper.eq("student_id", (Object)userCourse.getStudentId());
        }
        return (RechargeOrder)this.rechargeOrderMapper.selectOne((Wrapper)queryWrapper.last("limit 1"));
    }

    /**
     * 教练端写操作/名单读取的班级归属校验：班级本人负责，或该教练在此班级有代课场次（ys_course_lesson.coach_id）。
     * 原实现只信任请求里的 classId，任何教练 token 都能操作别人班级的签到/点名/课堂总结。
     */
    private void assertCoachOwnsClass(Integer instructorId, Integer classId) {
        CourseClass cls = classId == null || classId <= 0
                ? null : (CourseClass)this.courseClassMapper.selectById((Serializable)classId);
        if (cls == null || (cls.getIsDelete() != null && cls.getIsDelete() == 1)) {
            throw new OperateException("\u73ed\u7ea7\u4e0d\u5b58\u5728");
        }
        if (instructorId != null && instructorId.equals(cls.getInstructorId())) {
            return;
        }
        Long substitute = this.courseLessonMapper.selectCount(new QueryWrapper<CourseLesson>()
                .eq("class_id", (Object)classId).eq("coach_id", (Object)instructorId));
        if (substitute == null || substitute == 0L) {
            throw new OperateException("\u65e0\u6743\u64cd\u4f5c\u8be5\u73ed\u7ea7\u8bfe\u7a0b");
        }
    }

    private List<Integer> getInstructorClassIds(Integer instructorId) {
        List<CourseClass> classes = this.courseClassMapper.selectList(new QueryWrapper<CourseClass>().eq("instructor_id", (Object)instructorId).eq("is_delete", (Object)0));
        return classes.stream().map(CourseClass::getId).collect(Collectors.toList());
    }

    private void assertStudentUnderInstructor(List<Integer> classIds, Integer userId) {
        if (classIds.isEmpty()) {
            throw new OperateException("\u6682\u65e0\u8d1f\u8d23\u7684\u73ed\u7ea7");
        }
        Long count = this.userCourseMapper.selectCount(new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).in("course_class_id", classIds).isNull("delete_time"));
        if (count == null || count == 0L) {
            throw new OperateException("\u8be5\u5b66\u5458\u4e0d\u5728\u60a8\u7684\u73ed\u7ea7\u4e2d");
        }
    }

    private int resolveLessonNoForConsume(Integer targetLessonNo, CourseLesson todayLesson, Integer lessonId) {
        CourseLesson lesson;
        if (targetLessonNo != null && targetLessonNo > 0) {
            return targetLessonNo;
        }
        if (todayLesson != null && todayLesson.getLessonNo() != null && todayLesson.getLessonNo() > 0) {
            return todayLesson.getLessonNo();
        }
        if (lessonId != null && lessonId > 0 && (lesson = (CourseLesson)this.courseLessonMapper.selectById((Serializable)lessonId)) != null && lesson.getLessonNo() != null && lesson.getLessonNo() > 0) {
            return lesson.getLessonNo();
        }
        return 0;
    }

    private String resolveStudentDisplayName(Integer userId, List<UserCourse> enrollments, User user) {
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

    /** 空班级场景的学员统计。用 Java 8 兼容写法（生产运行时为 temurin:8-jre，不能用 Map.of）。 */
    private Map<String, Object> emptyStudentStats() {
        Map<String, Object> stats = new HashMap<String, Object>();
        stats.put("totalStudents", 0);
        stats.put("activeStudents", 0);
        return stats;
    }
}

