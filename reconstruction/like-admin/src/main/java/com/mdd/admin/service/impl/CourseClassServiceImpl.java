/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.admin.service.ICourseClassService
 *  com.mdd.admin.service.IUserCourseAccountService
 *  com.mdd.admin.service.impl.CourseClassServiceImpl
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.course.CourseClassCreateValidate
 *  com.mdd.admin.validate.course.CourseClassSearchValidate
 *  com.mdd.admin.validate.course.CourseClassUpdateValidate
 *  com.mdd.admin.vo.course.CourseClassListedVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.RechargeOrder
 *  com.mdd.common.entity.agent.Agent
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.CourseLesson
 *  com.mdd.common.entity.course.SportsCategory
 *  com.mdd.common.entity.course.UserCourse
 *  com.mdd.common.entity.student.Student
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.RechargeOrderMapper
 *  com.mdd.common.mapper.agent.AgentMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.CourseLessonMapper
 *  com.mdd.common.mapper.course.SportsCategoryMapper
 *  com.mdd.common.mapper.course.UserCourseMapper
 *  com.mdd.common.mapper.student.StudentMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.util.ClassScheduleUtil
 *  com.mdd.common.util.ClassScheduleUtil$CourseClassScheduleInput
 *  com.mdd.common.util.LessonScheduleUtil
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.common.util.UserCourseBalanceUtil
 *  com.mdd.common.util.UserCourseBalanceUtil$Snapshot
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.service.ICourseClassService;
import com.mdd.admin.service.IUserCourseAccountService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.course.CourseClassCreateValidate;
import com.mdd.admin.validate.course.CourseClassSearchValidate;
import com.mdd.admin.validate.course.CourseClassUpdateValidate;
import com.mdd.admin.vo.course.CourseClassListedVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.agent.Agent;
import com.mdd.common.entity.community.Community;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.SportsCategory;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.region.Area;
import com.mdd.common.entity.student.Student;
import com.mdd.common.entity.user.User;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.agent.AgentMapper;
import com.mdd.common.mapper.community.CommunityMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.SportsCategoryMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.region.AreaMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.ClassScheduleUtil;
import com.mdd.common.util.HtmlSanitizer;
import com.mdd.common.util.LessonScheduleUtil;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.UserCourseBalanceUtil;
import com.mdd.common.util.ParamUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class CourseClassServiceImpl
implements ICourseClassService {
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    SportsCategoryMapper sportsCategoryMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    CourseBookingMapper courseBookingMapper;
    @Resource
    UserCourseMapper userCourseMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    AgentMapper agentMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    CommunityMapper communityMapper;
    @Resource
    AreaMapper areaMapper;
    @Resource
    IUserCourseAccountService userCourseAccountService;

    public PageResult<CourseClassListedVo> list(PageValidate pageValidate, CourseClassSearchValidate searchValidate) {
        Agent agent;
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        QueryWrapper<Agent> queryWrapper = new QueryWrapper<Agent>();
        queryWrapper.eq("is_delete", (Object)0);
        queryWrapper.orderByDesc("id");
        Integer adminId = LikeAdminThreadLocal.getAdminId();
        if (adminId != null && (agent = (Agent)this.agentMapper.selectOne(new QueryWrapper<Agent>().eq("admin_id", (Object)adminId).isNull("delete_time").last("limit 1"))) != null) {
            queryWrapper.eq("agent_id", (Object)agent.getId());
        }
        if (searchValidate.getName() != null && !searchValidate.getName().equals("")) {
            queryWrapper.like("name", (Object)searchValidate.getName());
        }
        if (searchValidate.getCategoryId() != null) {
            queryWrapper.eq("category_id", (Object)searchValidate.getCategoryId());
        }
        if (searchValidate.getStatus() != null) {
            queryWrapper.eq("status", (Object)searchValidate.getStatus());
        }
        if (searchValidate.getIsShow() != null) {
            queryWrapper.eq("is_show", (Object)searchValidate.getIsShow());
        }
        if (searchValidate.getAreaId() != null && searchValidate.getAreaId() > 0) {
            queryWrapper.eq("area_id", (Object)searchValidate.getAreaId());
        }
        if (searchValidate.getCommunityId() != null && searchValidate.getCommunityId() > 0) {
            List<Integer> areaIds = this.areaIdsByCommunity(searchValidate.getCommunityId());
            if (areaIds.isEmpty()) {
                queryWrapper.eq("community_id", (Object)searchValidate.getCommunityId());
            } else {
                Integer communityId = searchValidate.getCommunityId();
                queryWrapper.and(w -> w.eq("community_id", (Object)communityId)
                        .or().in("area_id", areaIds));
            }
        }
        IPage<CourseClass> iPage = this.courseClassMapper.selectPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), (Wrapper)queryWrapper);
        Map<Integer, Community> communityMap = this.communityMap();
        Map<Integer, Area> areaMap = this.areaMap();
        // #66：分类/报名人数一次性批量查询，避免每行各查一次（N+1）。
        Set<Integer> categoryIds = new HashSet<Integer>();
        Set<Integer> courseClassIds = new HashSet<Integer>();
        for (CourseClass courseClass : iPage.getRecords()) {
            if (courseClass.getCategoryId() != null && courseClass.getCategoryId() > 0) {
                categoryIds.add(courseClass.getCategoryId());
            }
            courseClassIds.add(courseClass.getId());
        }
        Map<Integer, SportsCategory> categoryMap = new HashMap<Integer, SportsCategory>();
        if (!categoryIds.isEmpty()) {
            for (SportsCategory category : this.sportsCategoryMapper.selectBatchIds(categoryIds)) {
                categoryMap.put(category.getId(), category);
            }
        }
        Map<Integer, Integer> enrollCountMap = new HashMap<Integer, Integer>();
        if (!courseClassIds.isEmpty()) {
            List<Map<String, Object>> enrollCounts = this.userCourseMapper.selectMaps(new QueryWrapper<UserCourse>()
                    .select("course_class_id AS classId", "COUNT(*) AS total")
                    .in("course_class_id", courseClassIds)
                    .isNull("delete_time")
                    .groupBy("course_class_id"));
            for (Map<String, Object> row : enrollCounts) {
                Object classId = row.containsKey("classId") ? row.get("classId") : row.get("class_id");
                Object total = row.containsKey("total") ? row.get("total") : row.get("cnt");
                if (classId != null && total instanceof Number) {
                    enrollCountMap.put(((Number) classId).intValue(), ((Number) total).intValue());
                }
            }
        }
        ArrayList<CourseClassListedVo> list = new ArrayList<CourseClassListedVo>();
        for (CourseClass courseClass : iPage.getRecords()) {
            CourseClassListedVo vo = new CourseClassListedVo();
            BeanUtils.copyProperties((Object)courseClass, (Object)vo);
            SportsCategory category = categoryMap.get(courseClass.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
            Area area = courseClass.getAreaId() != null && courseClass.getAreaId() > 0 ? areaMap.get(courseClass.getAreaId()) : null;
            Integer effectiveCommunityId = courseClass.getCommunityId() != null && courseClass.getCommunityId() > 0
                    ? courseClass.getCommunityId()
                    : (area != null ? area.getCommunityId() : 0);
            vo.setAreaId(courseClass.getAreaId() == null ? 0 : courseClass.getAreaId());
            vo.setAreaName(area == null ? "" : area.getName());
            vo.setCommunityId(effectiveCommunityId == null ? 0 : effectiveCommunityId);
            Community community = communityMap.get(effectiveCommunityId);
            vo.setCommunityName(community == null ? "" : community.getName());
            if (courseClass.getStartDate() != null) {
                vo.setStartDate(TimeUtils.timestampToDay((Long)courseClass.getStartDate().longValue()));
            }
            if (courseClass.getCreateTime() != null) {
                vo.setCreateTime(TimeUtils.timestampToDate((Long)courseClass.getCreateTime().longValue()));
            }
            vo.setImage(UrlUtils.toAdminAbsoluteUrl((String)courseClass.getImage()));
            vo.setHasIntro(courseClass.getIntroHtml() != null && !courseClass.getIntroHtml().trim().isEmpty() ? 1 : 0);
            Integer enrollCount = enrollCountMap.get(courseClass.getId());
            vo.setStudentCount(Integer.valueOf(enrollCount != null ? enrollCount.intValue() : 0));
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public Map<String, Object> detail(Integer id) {
        CourseClass courseClass = (CourseClass)this.courseClassMapper.selectOne(new QueryWrapper<CourseClass>().eq("id", (Object)id).eq("is_delete", (Object)0).last("limit 1"));
        if (courseClass == null) {
            return null;
        }
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("id", courseClass.getId());
        map.put("name", courseClass.getName());
        map.put("summary", courseClass.getSummary() == null ? "" : courseClass.getSummary());
        map.put("introHtml", HtmlSanitizer.sanitizeCourseIntro(courseClass.getIntroHtml()));
        map.put("introImages", courseClass.getIntroImages() == null ? "" : courseClass.getIntroImages());
        map.put("introImageUrls", this.toAdminImageUrls(courseClass.getIntroImages()));
        map.put("isShow", courseClass.getIsShow() == null ? 1 : courseClass.getIsShow());
        map.put("categoryId", courseClass.getCategoryId());
        SportsCategory category = courseClass.getCategoryId() != null && courseClass.getCategoryId() > 0
                ? (SportsCategory)this.sportsCategoryMapper.selectById((Serializable)courseClass.getCategoryId()) : null;
        map.put("categoryName", category == null ? "" : category.getName());
        map.put("instructorId", courseClass.getInstructorId());
        Instructor instructor = courseClass.getInstructorId() != null && courseClass.getInstructorId() > 0
                ? (Instructor)this.instructorMapper.selectById((Serializable)courseClass.getInstructorId()) : null;
        map.put("instructorName", instructor != null ? instructor.getName()
                : (courseClass.getInstructorName() == null ? "" : courseClass.getInstructorName()));
        Area area = courseClass.getAreaId() != null && courseClass.getAreaId() > 0 ? this.areaMapper.selectById((Serializable)courseClass.getAreaId()) : null;
        Integer effectiveCommunityId = courseClass.getCommunityId() != null && courseClass.getCommunityId() > 0
                ? courseClass.getCommunityId()
                : (area != null ? area.getCommunityId() : 0);
        Community community = effectiveCommunityId != null && effectiveCommunityId > 0 ? this.communityMapper.selectById((Serializable)effectiveCommunityId) : null;
        map.put("communityId", effectiveCommunityId == null ? 0 : effectiveCommunityId);
        map.put("communityName", community == null ? "" : community.getName());
        map.put("areaId", courseClass.getAreaId() == null ? 0 : courseClass.getAreaId());
        map.put("areaName", area == null ? "" : area.getName());
        map.put("ageMin", courseClass.getAgeMin());
        map.put("ageMax", courseClass.getAgeMax());
        map.put("level", courseClass.getLevel() == null ? "" : courseClass.getLevel());
        map.put("agentId", courseClass.getAgentId());
        Agent agent = courseClass.getAgentId() != null && courseClass.getAgentId() > 0
                ? (Agent)this.agentMapper.selectById((Serializable)courseClass.getAgentId()) : null;
        map.put("agentName", agent == null ? "" : agent.getName());
        map.put("venue", courseClass.getVenue());
        map.put("latitude", courseClass.getLatitude());
        map.put("longitude", courseClass.getLongitude());
        if (courseClass.getLatitude() != null && courseClass.getLongitude() != null && !courseClass.getLatitude().isEmpty() && !courseClass.getLongitude().isEmpty()) {
            map.put("location", courseClass.getLongitude() + "," + courseClass.getLatitude());
        } else {
            map.put("location", "");
        }
        map.put("image", UrlUtils.toAdminAbsoluteUrl((String)courseClass.getImage()));
        // 原始相对路径：编辑回填时直接回传，避免把动态域名写入数据库
        map.put("imagePath", courseClass.getImage() == null ? "" : courseClass.getImage());
        map.put("classType", courseClass.getClassType());
        if (courseClass.getStartDate() != null) {
            map.put("startDate", TimeUtils.timestampToDay((Long)courseClass.getStartDate().longValue()));
        }
        if (courseClass.getEndDate() != null) {
            map.put("endDate", TimeUtils.timestampToDay((Long)courseClass.getEndDate().longValue()));
        }
        map.put("weekDays", courseClass.getWeekDays());
        map.put("timeSlot", courseClass.getTimeSlot());
        map.put("scheduleMode", courseClass.getScheduleMode() != null ? courseClass.getScheduleMode() : 0);
        map.put("scheduleRules", courseClass.getScheduleRules());
        map.put("totalLessons", courseClass.getTotalLessons());
        map.put("feePerLesson", courseClass.getFeePerLesson());
        map.put("totalFee", courseClass.getTotalFee());
        map.put("maxStudents", courseClass.getMaxStudents());
        map.put("status", courseClass.getStatus());
        Long enrolledCount = this.userCourseMapper.selectCount(
                new QueryWrapper<UserCourse>().eq("course_class_id", (Object)courseClass.getId()).isNull("delete_time")
        );
        map.put("studentCount", enrolledCount == null ? 0 : enrolledCount.intValue());
        return map;
    }

    public void add(CourseClassCreateValidate createValidate) {
        this.validateAgeRange(createValidate.getAgeMin(), createValidate.getAgeMax());
        ClassScheduleUtil.CourseClassScheduleInput scheduleInput = this.buildScheduleInput(createValidate.getScheduleMode(), createValidate.getScheduleRules(), createValidate.getWeekDays(), createValidate.getTimeSlot());
        try {
            ClassScheduleUtil.applySchedule((ClassScheduleUtil.CourseClassScheduleInput)scheduleInput);
        }
        catch (IllegalArgumentException e) {
            throw new OperateException(e.getMessage());
        }
        CourseClass courseClass = new CourseClass();
        BeanUtils.copyProperties((Object)createValidate, (Object)courseClass);
        courseClass.setLevel(this.normalizeLevel(createValidate.getLevel()));
        this.applyAreaForCreate(createValidate.getAreaId(), createValidate.getCommunityId(), courseClass);
        courseClass.setSummary(createValidate.getSummary() == null ? "" : createValidate.getSummary().trim());
        courseClass.setIntroHtml(HtmlSanitizer.sanitizeCourseIntro(createValidate.getIntroHtml()));
        courseClass.setIntroImages(this.normalizeIntroImages(createValidate.getIntroImages()));
        courseClass.setImage(UrlUtils.toRelativeUrl(createValidate.getImage()));
        courseClass.setIsShow(createValidate.getIsShow() != null && createValidate.getIsShow() == 0 ? 0 : 1);
        courseClass.setScheduleMode(scheduleInput.getScheduleMode());
        courseClass.setScheduleRules(scheduleInput.getScheduleRules());
        courseClass.setWeekDays(scheduleInput.getWeekDays());
        courseClass.setTimeSlot(scheduleInput.getTimeSlot());
        Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)createValidate.getInstructorId());
        if (instructor != null) {
            courseClass.setInstructorName(instructor.getName());
        }
        if (createValidate.getStartDate() != null && !createValidate.getStartDate().equals("")) {
            courseClass.setStartDate(Integer.valueOf(TimeUtils.dateToTimestamp((String)createValidate.getStartDate()).intValue()));
        }
        if (courseClass.getTotalLessons() == null || courseClass.getTotalLessons() <= 0) {
            courseClass.setTotalLessons(Integer.valueOf(300));
        }
        courseClass.setStudentCount(Integer.valueOf(0));
        courseClass.setIsDelete(Integer.valueOf(0));
        courseClass.setCreateTime(Integer.valueOf(TimeUtils.timestamp().intValue()));
        courseClass.setUpdateTime(Integer.valueOf(TimeUtils.timestamp().intValue()));
        this.courseClassMapper.insert(courseClass);
        this.generateLessons(courseClass);
    }

    public void edit(CourseClassUpdateValidate updateValidate) {
        this.validateAgeRange(updateValidate.getAgeMin(), updateValidate.getAgeMax());
        String newScheduleRules;
        CourseClass courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)updateValidate.getId());
        if (courseClass == null || courseClass.getIsDelete() == 1) {
            throw new OperateException("\u73ed\u7ea7\u5df2\u88ab\u5220\u9664");
        }
        String oldStartDate = courseClass.getStartDate() != null ? TimeUtils.timestampToDay((Long)courseClass.getStartDate().longValue()) : "";
        String newStartDate = updateValidate.getStartDate() == null ? "" : updateValidate.getStartDate();
        String oldWeekDays = courseClass.getWeekDays() == null ? "" : courseClass.getWeekDays();
        String newWeekDays = updateValidate.getWeekDays() == null ? "" : updateValidate.getWeekDays();
        Integer oldTotalLessons = courseClass.getTotalLessons() == null ? 0 : courseClass.getTotalLessons();
        Integer newTotalLessons = updateValidate.getTotalLessons() == null ? 0 : updateValidate.getTotalLessons();
        String oldTimeSlot = courseClass.getTimeSlot() == null ? "" : courseClass.getTimeSlot();
        String newTimeSlot = updateValidate.getTimeSlot() == null ? "" : updateValidate.getTimeSlot();
        Integer oldScheduleMode = courseClass.getScheduleMode() != null ? courseClass.getScheduleMode() : 0;
        Integer newScheduleMode = updateValidate.getScheduleMode() != null ? updateValidate.getScheduleMode() : 0;
        String oldScheduleRules = courseClass.getScheduleRules() == null ? "" : courseClass.getScheduleRules();
        String string = newScheduleRules = updateValidate.getScheduleRules() == null ? "" : updateValidate.getScheduleRules();
        if (!(Objects.equals(oldStartDate, newStartDate) && Objects.equals(oldWeekDays, newWeekDays) && Objects.equals(oldTotalLessons, newTotalLessons) && Objects.equals(oldScheduleMode, newScheduleMode))) {
            throw new OperateException("\u5f53\u524d\u4ec5\u652f\u6301\u4fee\u6539\u4e0a\u8bfe\u65f6\u95f4\uff0c\u5f00\u8bfe\u65e5\u671f\u3001\u4e0a\u8bfe\u5468\u671f\u3001\u603b\u8bfe\u8282\u6570\u6682\u4e0d\u5141\u8bb8\u7f16\u8f91\u3002");
        }
        ClassScheduleUtil.CourseClassScheduleInput scheduleInput = this.buildScheduleInput(newScheduleMode, newScheduleRules, newWeekDays, newTimeSlot);
        try {
            ClassScheduleUtil.applySchedule((ClassScheduleUtil.CourseClassScheduleInput)scheduleInput);
        }
        catch (IllegalArgumentException e) {
            throw new OperateException(e.getMessage());
        }
        BeanUtils.copyProperties((Object)updateValidate, (Object)courseClass);
        if (updateValidate.getLevel() != null) {
            courseClass.setLevel(this.normalizeLevel(updateValidate.getLevel()));
        }
        this.applyAreaForEdit(updateValidate.getAreaId(), updateValidate.getCommunityId(), courseClass);
        courseClass.setSummary(updateValidate.getSummary() == null ? "" : updateValidate.getSummary().trim());
        courseClass.setIntroHtml(HtmlSanitizer.sanitizeCourseIntro(updateValidate.getIntroHtml()));
        courseClass.setIntroImages(this.normalizeIntroImages(updateValidate.getIntroImages()));
        courseClass.setImage(UrlUtils.toRelativeUrl(updateValidate.getImage()));
        if (updateValidate.getIsShow() != null) {
            courseClass.setIsShow(updateValidate.getIsShow() == 0 ? 0 : 1);
        } else if (courseClass.getIsShow() == null) {
            courseClass.setIsShow(1);
        }
        courseClass.setScheduleMode(scheduleInput.getScheduleMode());
        courseClass.setScheduleRules(scheduleInput.getScheduleRules());
        courseClass.setWeekDays(scheduleInput.getWeekDays());
        courseClass.setTimeSlot(scheduleInput.getTimeSlot());
        Instructor instructor = (Instructor)this.instructorMapper.selectById((Serializable)updateValidate.getInstructorId());
        if (instructor != null) {
            courseClass.setInstructorName(instructor.getName());
        }
        if (updateValidate.getStartDate() != null && !updateValidate.getStartDate().equals("")) {
            courseClass.setStartDate(Integer.valueOf(TimeUtils.dateToTimestamp((String)updateValidate.getStartDate()).intValue()));
        }
        courseClass.setUpdateTime(Integer.valueOf(TimeUtils.timestamp().intValue()));
        this.courseClassMapper.updateById(courseClass);
        this.syncPendingLessonTimeSlots(courseClass, oldScheduleMode, oldTimeSlot, oldScheduleRules, scheduleInput.getScheduleMode(), scheduleInput.getTimeSlot(), scheduleInput.getScheduleRules());
    }

    public void del(Integer id) {
        if (id == null || id <= 0) {
            throw new OperateException("参数错误");
        }
        CourseClass courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)id);
        if (courseClass == null || (courseClass.getIsDelete() != null && courseClass.getIsDelete() == 1)) {
            throw new OperateException("课程不存在或已删除");
        }
        long enrollCount = this.countOrZero(this.userCourseMapper.selectCount(
                new QueryWrapper<UserCourse>().eq("course_class_id", (Object)id).isNull("delete_time")
        ));
        long orderCount = this.countOrZero(this.rechargeOrderMapper.selectCount(
                new QueryWrapper<RechargeOrder>().eq("course_class_id", (Object)id)
        ));
        long bookingCount = this.countOrZero(this.courseBookingMapper.selectCount(
                new QueryWrapper<CourseBooking>().eq("course_class_id", (Object)id)
        ));
        if (enrollCount + orderCount + bookingCount > 0) {
            throw new OperateException(
                    "该课程已有 " + enrollCount + " 条报名、 " + orderCount + " 笔订单、 " + bookingCount
                            + " 条预约记录，不能删除；请改为下架（下架后小程序不再展示）。"
            );
        }
        courseClass.setIsDelete(Integer.valueOf(1));
        courseClass.setUpdateTime(Integer.valueOf(TimeUtils.timestamp().intValue()));
        this.courseClassMapper.updateById(courseClass);
    }

    public void setShow(Integer id, Integer isShow) {
        if (id == null || id <= 0 || isShow == null || (isShow != 0 && isShow != 1)) {
            throw new OperateException("参数错误");
        }
        CourseClass courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)id);
        if (courseClass == null || (courseClass.getIsDelete() != null && courseClass.getIsDelete() == 1)) {
            throw new OperateException("课程不存在或已删除");
        }
        courseClass.setIsShow(isShow);
        courseClass.setUpdateTime(Integer.valueOf(TimeUtils.timestamp().intValue()));
        this.courseClassMapper.updateById(courseClass);
    }

    private long countOrZero(Long value) {
        return value == null ? 0L : value.longValue();
    }

    /**
     * 介绍多图归一化：去空白、去空项、去重后以英文逗号拼接。
     * 超过 la_course_class.intro_images 的 2000 字符上限时直接拒绝，避免截断丢图。
     */
    private String normalizeIntroImages(String introImages) {
        if (introImages == null || introImages.trim().isEmpty()) {
            return "";
        }
        java.util.LinkedHashSet<String> paths = new java.util.LinkedHashSet<>();
        for (String path : introImages.split(",")) {
            String item = path.trim();
            if (!item.isEmpty()) {
                // 前端 material-picker 提交的是绝对地址；入库统一转相对路径，避免域名漂移
                paths.add(UrlUtils.toRelativeUrl((String)item));
            }
        }
        String joined = String.join(",", paths);
        if (joined.length() > 2000) {
            throw new OperateException("介绍图片过多，请精简后再保存");
        }
        return joined;
    }

    private List<String> toAdminImageUrls(String introImages) {
        ArrayList<String> urls = new ArrayList<>();
        if (introImages == null || introImages.trim().isEmpty()) {
            return urls;
        }
        for (String path : introImages.split(",")) {
            String item = path.trim();
            if (!item.isEmpty()) {
                urls.add(UrlUtils.toAdminAbsoluteUrl((String)item));
            }
        }
        return urls;
    }

    private List<Integer> areaIdsByCommunity(Integer communityId) {
        List<Area> areas = this.areaMapper.selectList(
                new QueryWrapper<Area>().eq("community_id", communityId).isNull("delete_time")
        );
        List<Integer> ids = new ArrayList<>();
        for (Area area : areas) {
            ids.add(area.getId());
        }
        return ids;
    }

    private Map<Integer, Community> communityMap() {
        List<Community> communities = this.communityMapper.selectList(
                new QueryWrapper<Community>().isNull("delete_time")
        );
        Map<Integer, Community> map = new HashMap<>();
        for (Community community : communities) {
            map.put(community.getId(), community);
        }
        return map;
    }

    private Map<Integer, Area> areaMap() {
        List<Area> areas = this.areaMapper.selectList(
                new QueryWrapper<Area>().isNull("delete_time")
        );
        Map<Integer, Area> map = new HashMap<>();
        for (Area area : areas) {
            map.put(area.getId(), area);
        }
        return map;
    }

    private void applyAreaForCreate(Integer areaId, Integer communityId, CourseClass courseClass) {
        if (areaId == null || areaId <= 0) {
            courseClass.setAreaId(0);
            courseClass.setCommunityId(communityId == null ? 0 : communityId);
            return;
        }
        Area area = this.requireArea(areaId);
        courseClass.setAreaId(area.getId());
        courseClass.setCommunityId(this.resolveCommunityId(communityId, area));
    }

    private void applyAreaForEdit(Integer areaId, Integer communityId, CourseClass courseClass) {
        if (areaId == null) {
            return;
        }
        if (areaId <= 0) {
            courseClass.setAreaId(0);
            if (communityId != null) {
                courseClass.setCommunityId(communityId);
            }
            return;
        }
        Area area = this.requireArea(areaId);
        courseClass.setAreaId(area.getId());
        if (communityId != null) {
            courseClass.setCommunityId(this.resolveCommunityId(communityId, area));
        }
    }

    private Area requireArea(Integer areaId) {
        Area area = this.areaMapper.selectById((Serializable)areaId);
        if (area == null || area.getDeleteTime() != null) {
            throw new OperateException("\u5c0f\u533a\u4e0d\u5b58\u5728");
        }
        return area;
    }

    private Integer resolveCommunityId(Integer communityId, Area area) {
        Integer areaCommunityId = area.getCommunityId() == null ? 0 : area.getCommunityId();
        if (communityId == null || communityId <= 0) {
            return areaCommunityId;
        }
        if (areaCommunityId > 0 && !communityId.equals(areaCommunityId)) {
            throw new OperateException("\u5c0f\u533a\u4e0d\u5c5e\u4e8e\u6240\u9009\u793e\u533a");
        }
        return communityId;
    }

    public Map<String, Object> stats() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Long totalCount = this.courseClassMapper.selectCount(new QueryWrapper<CourseClass>().eq("is_delete", (Object)0));
        Long activeCount = this.courseClassMapper.selectCount(new QueryWrapper<CourseClass>().eq("is_delete", (Object)0).eq("status", (Object)1));
        Long advanceCount = this.courseClassMapper.selectCount(new QueryWrapper<CourseClass>().eq("is_delete", (Object)0).eq("status", (Object)2));
        Long finishedCount = this.courseClassMapper.selectCount(new QueryWrapper<CourseClass>().eq("is_delete", (Object)0).eq("status", (Object)0));
        map.put("totalCount", totalCount);
        map.put("activeCount", activeCount);
        map.put("advanceCount", advanceCount);
        map.put("finishedCount", finishedCount);
        return map;
    }

    public List<Map<String, Object>> enrolledStudents(Integer classId) {
        List<UserCourse> userCourses = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().eq("course_class_id", (Object)classId).isNull("delete_time").orderByDesc("id"));
        ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (UserCourse uc : userCourses) {
            Student student;
            Integer studentId;
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("id", uc.getId());
            item.put("userId", uc.getUserId());
            item.put("orderId", uc.getOrderId());
            RechargeOrder linkedOrder = uc.getOrderId() != null && uc.getOrderId() > 0 ? (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)uc.getOrderId()) : null;
            Integer enrollmentStatus = linkedOrder != null && linkedOrder.getRefundStatus() != null && linkedOrder.getRefundStatus() == 1 ? 2 : uc.getStatus();
            int effectiveUsedLessons = this.userCourseAccountService.countEffectiveUsedLessonsForEnrollment(uc);
            UserCourseBalanceUtil.Snapshot balance = UserCourseBalanceUtil.resolve((Integer)uc.getTotalLessons(), (Integer)uc.getUsedLessons(), (Integer)uc.getRemainLessons(), (Integer)effectiveUsedLessons, (Integer)enrollmentStatus);
            item.put("status", balance.getStatus());
            item.put("statusText", balance.getStatus() == 2 ? "\u5df2\u9000\u8bfe" : (balance.getStatus() == 1 ? "\u5df2\u5b8c\u6210" : "\u5b66\u4e60\u4e2d"));
            item.put("totalLessons", balance.getTotalLessons());
            item.put("usedLessons", balance.getUsedLessons());
            item.put("remainLessons", balance.getRemainLessons());
            item.put("canConsume", balance.getStatus() != 2 && balance.getRemainLessons() > 0);
            item.put("createTime", TimeUtils.timestampToDate((Long)uc.getCreateTime()));
            User user = (User)this.userMapper.selectById((Serializable)uc.getUserId());
            if (user != null) {
                item.put("nickname", user.getNickname());
                item.put("avatar", UrlUtils.toAdminAbsoluteUrl((String)user.getAvatar()));
                item.put("mobile", user.getMobile());
                item.put("realName", user.getRealName());
            }
            if (((studentId = uc.getStudentId()) == null || studentId <= 0) && linkedOrder != null) {
                studentId = linkedOrder.getStudentId();
            }
            item.put("studentId", studentId != null && studentId > 0 ? studentId : 0);
            if (studentId != null && studentId > 0 && (student = (Student)this.studentMapper.selectById((Serializable)studentId)) != null) {
                item.put("studentName", student.getName());
                item.put("studentAvatar", student.getAvatar() != null ? UrlUtils.toAdminAbsoluteUrl((String)student.getAvatar()) : "");
            }
            result.add(item);
        }
        return result;
    }

    private void generateLessons(CourseClass courseClass) {
        Integer totalLessons = courseClass.getTotalLessons() != null ? courseClass.getTotalLessons() : 300;
        Integer scheduleMode = courseClass.getScheduleMode() != null ? courseClass.getScheduleMode() : 0;
        List<Map<String, Object>> scheduleList = scheduleMode == 1 ? LessonScheduleUtil.generateSchedule((Integer)courseClass.getStartDate(), (Integer)totalLessons, (Map)ClassScheduleUtil.toTimeSlotMap((String)courseClass.getScheduleRules())) : LessonScheduleUtil.generateSchedule((Integer)courseClass.getStartDate(), (Integer)totalLessons, (String)courseClass.getWeekDays(), (String)courseClass.getTimeSlot());
        int now = TimeUtils.timestamp().intValue();
        for (Map sch : scheduleList) {
            CourseLesson lesson = new CourseLesson();
            lesson.setClassId(courseClass.getId());
            lesson.setLessonNo(ParamUtils.intOrDefault(sch.get("lessonNo"), 0, "课次序号"));
            lesson.setLessonDate(sch.get("date").toString());
            lesson.setWeekDay(ParamUtils.intOrDefault(sch.get("weekDay"), 0, "星期"));
            lesson.setTimeSlot(sch.get("timeSlot") != null ? sch.get("timeSlot").toString() : "");
            lesson.setCoachId(Integer.valueOf(courseClass.getInstructorId() != null ? courseClass.getInstructorId() : 0));
            lesson.setStatus(Integer.valueOf(0));
            lesson.setCreateTime(Integer.valueOf(now));
            lesson.setUpdateTime(Integer.valueOf(now));
            this.courseLessonMapper.insert(lesson);
        }
    }

    /** 适龄区间校验：两端均可空；都填时必须是合法的闭区间（数据库为 tinyint unsigned）。 */
    private void validateAgeRange(Integer ageMin, Integer ageMax) {
        if (ageMin != null && (ageMin < 0 || ageMin > 18)) {
            throw new OperateException("适龄下限需在 0-18 岁之间");
        }
        if (ageMax != null && (ageMax < 0 || ageMax > 18)) {
            throw new OperateException("适龄上限需在 0-18 岁之间");
        }
        if (ageMin != null && ageMax != null && ageMin > ageMax) {
            throw new OperateException("适龄下限不能大于上限");
        }
    }

    /** 课程等级：去空白；空串表示回退到运动类目名。 */
    private String normalizeLevel(String level) {
        if (level == null) {
            return "";
        }
        String trimmed = level.trim();
        return trimmed.length() > 20 ? trimmed.substring(0, 20) : trimmed;
    }

    private ClassScheduleUtil.CourseClassScheduleInput buildScheduleInput(Integer scheduleMode, String scheduleRules, String weekDays, String timeSlot) {
        ClassScheduleUtil.CourseClassScheduleInput input = new ClassScheduleUtil.CourseClassScheduleInput();
        input.setScheduleMode(scheduleMode);
        input.setScheduleRules(scheduleRules);
        input.setWeekDays(weekDays);
        input.setTimeSlot(timeSlot);
        return input;
    }

    private void syncPendingLessonTimeSlots(CourseClass courseClass, Integer oldScheduleMode, String oldTimeSlot, String oldScheduleRules, Integer newScheduleMode, String newTimeSlot, String newScheduleRules) {
        int now = TimeUtils.timestamp().intValue();
        if (Objects.equals(oldScheduleMode, newScheduleMode) && Objects.equals(oldTimeSlot, newTimeSlot) && Objects.equals(oldScheduleRules, newScheduleRules)) {
            return;
        }
        if (newScheduleMode != null && newScheduleMode == 1) {
            Map<Integer, String> newRuleMap = ClassScheduleUtil.toTimeSlotMap((String)newScheduleRules);
            Map<Integer, String> oldRuleMap = ClassScheduleUtil.toTimeSlotMap((String)oldScheduleRules);
            for (Map.Entry entry : newRuleMap.entrySet()) {
                if (Objects.equals(entry.getValue(), oldRuleMap.get(entry.getKey()))) continue;
                CourseLesson lessonUpdate = new CourseLesson();
                lessonUpdate.setTimeSlot((String)entry.getValue());
                lessonUpdate.setUpdateTime(Integer.valueOf(now));
                this.courseLessonMapper.update(lessonUpdate, new QueryWrapper<CourseLesson>().eq("class_id", (Object)courseClass.getId()).eq("week_day", entry.getKey()).eq("status", (Object)0));
            }
            return;
        }
        CourseLesson lessonUpdate = new CourseLesson();
        lessonUpdate.setTimeSlot(newTimeSlot);
        lessonUpdate.setUpdateTime(Integer.valueOf(now));
        this.courseLessonMapper.update(lessonUpdate, new QueryWrapper<CourseLesson>().eq("class_id", (Object)courseClass.getId()).eq("status", (Object)0));
    }
}
