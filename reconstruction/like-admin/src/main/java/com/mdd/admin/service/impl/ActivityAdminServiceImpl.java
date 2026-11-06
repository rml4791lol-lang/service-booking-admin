package com.mdd.admin.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.mapper.ActivityRosterQueryMapper;
import com.mdd.admin.service.IActivityAdminService;
import com.mdd.admin.service.IFinanceRechargerService;
import com.mdd.admin.validate.activity.ActivityCreateValidate;
import com.mdd.admin.validate.activity.ActivityUpdateValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.activity.EnrollmentExportVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.activity.Activity;
import com.mdd.common.entity.activity.ActivityBooking;
import com.mdd.common.entity.activity.ActivityEnrollAlert;
import com.mdd.common.entity.activity.ActivitySignup;
import com.mdd.common.entity.activity.ClassApplyGroup;
import com.mdd.common.entity.activity.EnrollLink;
import com.mdd.common.entity.activity.Enrollment;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.admin.Admin;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseRefund;
import com.mdd.common.entity.region.Area;
import com.mdd.common.entity.region.Region;
import com.mdd.common.entity.student.Student;
import com.mdd.common.entity.user.User;
import com.mdd.common.enums.PaymentEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.activity.ActivityEnrollAlertMapper;
import com.mdd.common.mapper.activity.ActivityBookingMapper;
import com.mdd.common.mapper.activity.ActivityMapper;
import com.mdd.common.mapper.activity.ActivitySignupMapper;
import com.mdd.common.mapper.activity.ClassApplyGroupMapper;
import com.mdd.common.mapper.activity.ClassApplyMapper;
import com.mdd.common.mapper.activity.EnrollLinkMapper;
import com.mdd.common.mapper.activity.EnrollmentMapper;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.CourseRefundMapper;
import com.mdd.common.mapper.region.AreaMapper;
import com.mdd.common.mapper.region.RegionMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.admin.vo.activity.ActivityEnrollAlertDetailVo;
import com.mdd.admin.export.ExportFileService;
import com.mdd.admin.vo.activity.ActivityEnrollAlertListVo;
import com.mdd.admin.vo.activity.ActivityEnrollAlertSignupVo;
import com.mdd.admin.vo.activity.ActivitySignupExportVo;
import com.mdd.common.util.ClassScheduleUtil;
import com.mdd.common.service.ActivitySignupCancelService;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.YmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 活动报名链接 / 报名名单 / 开班申请后台实现（T29.7 #27、T30.6 #38）。
 *
 * <p>链接有效性口径：{@code status=1} 且（{@code expire_time=0} 长期有效或未到期）。
 * 名单去重口径保持 #27：同场次 + 手机号 + 学员名，数据库唯一键兜底并发。</p>
 */
@Service
public class ActivityAdminServiceImpl implements IActivityAdminService {

    private static final Logger log = LoggerFactory.getLogger(ActivityAdminServiceImpl.class);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter FILENAME_FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final int OPTIONS_LIMIT = 300;
    private static final int DEFAULT_EXPIRE_DAYS = 30;
    private static final int MAX_EXPIRE_DAYS = 365;
    private static final int EXPORT_LIMIT = 10000;
    private static final String DEDUPE_RULE = "同场次 + 手机号 + 学员名";
    private static final String SIGNUP_DEDUPE_RULE = "同一活动 + 同一学员（一人一行）";

    @Resource
    ExportFileService exportFileService;

    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    AreaMapper areaMapper;
    @Resource
    RegionMapper regionMapper;
    @Resource
    EnrollLinkMapper enrollLinkMapper;
    @Resource
    EnrollmentMapper enrollmentMapper;
    @Resource
    ClassApplyGroupMapper classApplyGroupMapper;
    @Resource
    ClassApplyMapper classApplyMapper;
    @Resource
    ActivityMapper activityMapper;
    @Resource
    ActivitySignupMapper activitySignupMapper;
    @Resource
    ActivityBookingMapper activityBookingMapper;
    @Resource
    ActivityEnrollAlertMapper activityEnrollAlertMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    AdminMapper adminMapper;
    @Resource
    IFinanceRechargerService iFinanceRechargerService;
    @Resource
    ActivityRosterQueryMapper activityRosterQueryMapper;
    @Resource
    CourseRefundMapper courseRefundMapper;
    @Resource
    ActivitySignupCancelService activitySignupCancelService;

    // ---- 报名链接 ----------------------------------------------------------

    @Override
    public List<Map<String, Object>> enrollOptions(String keyword) {
        String today = LocalDate.now().toString();
        List<CourseLesson> lessons = courseLessonMapper.selectList(new QueryWrapper<CourseLesson>()
                .eq("status", 0)
                .ge("lesson_date", today)
                .orderByAsc("lesson_date")
                .orderByAsc("time_slot")
                .orderByAsc("id")
                .last("limit " + OPTIONS_LIMIT));
        if (lessons.isEmpty()) {
            return new ArrayList<>();
        }
        Set<Integer> classIds = new HashSet<>();
        for (CourseLesson lesson : lessons) {
            classIds.add(lesson.getClassId());
        }
        Map<Integer, CourseClass> classes = new HashMap<>();
        Map<Integer, String> areaNames = new HashMap<>();
        for (CourseClass courseClass : courseClassMapper.selectBatchIds(classIds)) {
            if (courseClass.getIsDelete() != null && courseClass.getIsDelete() == 1) {
                continue;
            }
            classes.put(courseClass.getId(), courseClass);
        }
        Set<Integer> areaIds = new HashSet<>();
        Set<Integer> coachIds = new HashSet<>();
        for (CourseClass courseClass : classes.values()) {
            if (courseClass.getAreaId() != null && courseClass.getAreaId() > 0) {
                areaIds.add(courseClass.getAreaId());
            }
            if (courseClass.getInstructorId() != null && courseClass.getInstructorId() > 0) {
                coachIds.add(courseClass.getInstructorId());
            }
        }
        if (!areaIds.isEmpty()) {
            for (Area area : areaMapper.selectBatchIds(areaIds)) {
                areaNames.put(area.getId(), area.getName());
            }
        }
        Map<Integer, String> coachNames = new HashMap<>();
        if (!coachIds.isEmpty()) {
            for (Instructor instructor : instructorMapper.selectBatchIds(coachIds)) {
                coachNames.put(instructor.getId(), nvl(instructor.getName()));
            }
        }

        String kw = keyword == null ? "" : keyword.trim();
        List<Map<String, Object>> result = new ArrayList<>();
        for (CourseLesson lesson : lessons) {
            CourseClass courseClass = classes.get(lesson.getClassId());
            if (courseClass == null) {
                continue;
            }
            String title = nvl(courseClass.getName());
            String areaName = areaNames.getOrDefault(courseClass.getAreaId(), "");
            Integer coachId = lesson.getCoachId() != null && lesson.getCoachId() > 0
                    ? lesson.getCoachId() : courseClass.getInstructorId();
            String coachName = nvl(coachNames.get(coachId));
            if (coachName.isEmpty()) {
                coachName = nvl(courseClass.getInstructorName());
            }
            if (!kw.isEmpty() && !(title.contains(kw) || areaName.contains(kw) || coachName.contains(kw))) {
                continue;
            }
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", lesson.getId());
            item.put("courseId", courseClass.getId());
            item.put("courseTitle", title);
            item.put("lessonNo", lesson.getLessonNo());
            item.put("date", nvl(lesson.getLessonDate()));
            item.put("time", ClassScheduleUtil.resolveLessonTimeSlot(lesson, courseClass));
            item.put("areaName", areaName);
            item.put("venue", ClassScheduleUtil.resolveLessonVenue(lesson, courseClass));
            item.put("coachName", coachName);
            item.put("capacity", courseClass.getMaxStudents() == null ? 0 : courseClass.getMaxStudents());
            result.add(item);
        }
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> createEnrollLink(Integer adminId, Integer sessionId, Integer expireDays) {
        if (sessionId == null || sessionId <= 0) {
            throw new OperateException("请选择课程场次");
        }
        CourseLesson lesson = courseLessonMapper.selectById(sessionId);
        if (lesson == null) {
            throw new OperateException("课程场次不存在");
        }
        CourseClass courseClass = courseClassMapper.selectById(lesson.getClassId());
        if (courseClass == null) {
            throw new OperateException("课程不存在或已删除");
        }
        long now = TimeUtils.timestamp();
        EnrollLink existing = enrollLinkMapper.selectOne(new QueryWrapper<EnrollLink>()
                .eq("session_id", sessionId)
                .eq("status", 1)
                .and(wrapper -> wrapper.eq("expire_time", 0).or().gt("expire_time", now))
                .orderByDesc("id")
                .last("limit 1"));
        if (existing != null) {
            return linkResult(existing, lesson, courseClass, true);
        }

        int days = expireDays == null || expireDays <= 0 ? DEFAULT_EXPIRE_DAYS : expireDays;
        if (days > MAX_EXPIRE_DAYS) {
            days = MAX_EXPIRE_DAYS;
        }
        EnrollLink link = new EnrollLink();
        link.setToken(nextToken());
        link.setSessionId(sessionId);
        link.setCourseId(lesson.getClassId());
        link.setCreatorId(adminId == null ? 0 : adminId);
        link.setSource("admin");
        link.setExpireTime(now + days * 86400L);
        link.setStatus(1);
        link.setCreateTime(now);
        link.setUpdateTime(now);
        enrollLinkMapper.insert(link);
        return linkResult(link, lesson, courseClass, false);
    }

    @Override
    public PageResult<Map<String, Object>> enrollLinks(PageValidate pageValidate, Integer sessionId,
                                                       String status, String keyword) {
        long now = TimeUtils.timestamp();
        QueryWrapper<EnrollLink> query = new QueryWrapper<>();
        if (sessionId != null && sessionId > 0) {
            query.eq("session_id", sessionId);
        }
        String state = trimToEmpty(status);
        if ("0".equals(state)) {
            query.eq("status", 0);
        } else if ("1".equals(state)) {
            query.eq("status", 1)
                    .and(wrapper -> wrapper.eq("expire_time", 0).or().ge("expire_time", now));
        } else if ("2".equals(state)) {
            query.eq("status", 1).gt("expire_time", 0).lt("expire_time", now);
        }
        String kw = trimToEmpty(keyword);
        if (!kw.isEmpty()) {
            query.like("token", kw);
        }
        query.orderByDesc("id");
        IPage<EnrollLink> iPage = enrollLinkMapper.selectPage(
                new Page<>(pageValidate.getPage_no(), pageValidate.getPage_size()), query);

        Map<Integer, CourseLesson> lessons = mapLessonsByIds(sessionIdsOfLinks(iPage.getRecords()));
        Map<Integer, CourseClass> classes = mapClasses(lessons.values());
        Map<Integer, String> areaNames = mapAreaNames(classes.values());
        Map<Integer, Map<String, Object>> stats = linkSubmissionStats(iPage.getRecords());

        List<Map<String, Object>> list = new ArrayList<>();
        for (EnrollLink link : iPage.getRecords()) {
            CourseLesson lesson = lessons.get(link.getSessionId());
            CourseClass courseClass = lesson == null ? null : classes.get(lesson.getClassId());
            Map<String, Object> item = linkListItem(link, lesson, courseClass, areaNames, now);
            Map<String, Object> count = stats.get(link.getId());
            item.put("submissionCount", count == null ? 0 : count.get("total"));
            item.put("pendingCount", count == null ? 0 : count.get("pending"));
            item.put("confirmedCount", count == null ? 0 : count.get("confirmed"));
            item.put("rejectedCount", count == null ? 0 : count.get("rejected"));
            item.put("lastSubmitAt", count == null ? "" : count.get("lastSubmitAt"));
            list.add(item);
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list,
                linkSummary(sessionId, kw, now));
    }

    @Override
    public Map<String, Object> updateEnrollLinkStatus(Integer adminId, Integer id, Integer status) {
        EnrollLink link = requireLink(id);
        if (status == null || (status != 0 && status != 1)) {
            throw new OperateException("链接状态不合法");
        }
        long now = TimeUtils.timestamp();
        link.setStatus(status);
        link.setUpdateTime(now);
        enrollLinkMapper.updateById(link);
        return linkStatusResult(link, now);
    }

    @Override
    public Map<String, Object> updateEnrollLinkExpire(Integer adminId, Integer id, Integer expireDays) {
        EnrollLink link = requireLink(id);
        if (expireDays == null || expireDays < 0 || expireDays > MAX_EXPIRE_DAYS) {
            throw new OperateException("有效期需为 0（长期）或 1-" + MAX_EXPIRE_DAYS + " 天");
        }
        long now = TimeUtils.timestamp();
        link.setExpireTime(expireDays == 0 ? 0L : now + expireDays * 86400L);
        link.setUpdateTime(now);
        enrollLinkMapper.updateById(link);
        return linkStatusResult(link, now);
    }

    private EnrollLink requireLink(Integer id) {
        if (id == null || id <= 0) {
            throw new OperateException("请选择报名链接");
        }
        EnrollLink link = enrollLinkMapper.selectById(id);
        if (link == null) {
            throw new OperateException("报名链接不存在");
        }
        return link;
    }

    private Map<String, Object> linkResult(EnrollLink link, CourseLesson lesson, CourseClass courseClass,
                                           boolean reused) {
        Map<String, Object> result = new LinkedHashMap<>();
        long now = TimeUtils.timestamp();
        result.put("id", link.getId());
        result.put("token", link.getToken());
        result.put("sessionId", link.getSessionId());
        result.put("courseId", link.getCourseId());
        result.put("courseTitle", nvl(courseClass.getName()));
        result.put("date", nvl(lesson.getLessonDate()));
        result.put("time", ClassScheduleUtil.resolveLessonTimeSlot(lesson, courseClass));
        result.put("expireTime", link.getExpireTime());
        result.put("expireAt", formatTime(link.getExpireTime()));
        result.put("path", linkPath(link));
        result.put("status", link.getStatus());
        result.put("statusText", linkStatusText(link, now));
        result.put("valid", linkValid(link, now));
        result.put("reused", reused);
        return result;
    }

    private Map<String, Object> linkStatusResult(EnrollLink link, long now) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", link.getId());
        result.put("token", link.getToken());
        result.put("sessionId", link.getSessionId());
        result.put("status", link.getStatus());
        result.put("statusText", linkStatusText(link, now));
        result.put("valid", linkValid(link, now));
        result.put("expired", isExpired(link, now));
        result.put("expireTime", link.getExpireTime());
        result.put("expireAt", formatTime(link.getExpireTime()));
        result.put("path", linkPath(link));
        if (link.getStatus() != null && link.getStatus() == 1 && isExpired(link, now)) {
            result.put("notice", "链接当前已过期，请同时重设有效期后再发给家长");
        }
        return result;
    }

    private Map<String, Object> linkListItem(EnrollLink link, CourseLesson lesson, CourseClass courseClass,
                                             Map<Integer, String> areaNames, long now) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", link.getId());
        item.put("token", link.getToken());
        item.put("path", linkPath(link));
        item.put("sessionId", link.getSessionId());
        item.put("courseId", link.getCourseId());
        item.put("courseTitle", courseClass == null ? "" : nvl(courseClass.getName()));
        item.put("date", lesson == null ? "" : nvl(lesson.getLessonDate()));
        item.put("time", lesson == null || courseClass == null ? ""
                : ClassScheduleUtil.resolveLessonTimeSlot(lesson, courseClass));
        item.put("areaName", courseClass == null ? "" : areaNames.getOrDefault(courseClass.getAreaId(), ""));
        item.put("status", link.getStatus());
        item.put("statusText", linkStatusText(link, now));
        item.put("valid", linkValid(link, now));
        item.put("expired", isExpired(link, now));
        item.put("expireTime", link.getExpireTime());
        item.put("expireAt", formatTime(link.getExpireTime()));
        item.put("source", nvl(link.getSource()));
        item.put("creatorId", link.getCreatorId() == null ? 0 : link.getCreatorId());
        item.put("createdAt", formatTime(link.getCreateTime()));
        item.put("updatedAt", formatTime(link.getUpdateTime()));
        return item;
    }

    private Map<String, Object> linkSummary(Integer sessionId, String keyword, long now) {
        QueryWrapper<EnrollLink> base = new QueryWrapper<>();
        if (sessionId != null && sessionId > 0) {
            base.eq("session_id", sessionId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            base.like("token", keyword);
        }
        Long total = enrollLinkMapper.selectCount(base);
        // 单个 QueryWrapper 只能复用条件，统计口径单独构造，避免修改 base 的 SQL 片段。
        QueryWrapper<EnrollLink> disabledQuery = new QueryWrapper<>();
        if (sessionId != null && sessionId > 0) {
            disabledQuery.eq("session_id", sessionId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            disabledQuery.like("token", keyword);
        }
        disabledQuery.eq("status", 0);
        Long disabled = enrollLinkMapper.selectCount(disabledQuery);
        QueryWrapper<EnrollLink> activeQuery = new QueryWrapper<>();
        if (sessionId != null && sessionId > 0) {
            activeQuery.eq("session_id", sessionId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            activeQuery.like("token", keyword);
        }
        activeQuery.eq("status", 1)
                .and(wrapper -> wrapper.eq("expire_time", 0).or().ge("expire_time", now));
        Long active = enrollLinkMapper.selectCount(activeQuery);

        QueryWrapper<EnrollLink> expiredQuery = new QueryWrapper<>();
        if (sessionId != null && sessionId > 0) {
            expiredQuery.eq("session_id", sessionId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            expiredQuery.like("token", keyword);
        }
        expiredQuery.eq("status", 1).gt("expire_time", 0).lt("expire_time", now);
        Long expired = enrollLinkMapper.selectCount(expiredQuery);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("total", total == null ? 0 : total);
        summary.put("active", active == null ? 0 : active);
        summary.put("disabled", disabled == null ? 0 : disabled);
        summary.put("expired", expired == null ? 0 : expired);
        return summary;
    }

    private Map<Integer, Map<String, Object>> linkSubmissionStats(List<EnrollLink> links) {
        Map<Integer, Map<String, Object>> result = new HashMap<>();
        if (links == null || links.isEmpty()) {
            return result;
        }
        Set<Integer> ids = new HashSet<>();
        for (EnrollLink link : links) {
            ids.add(link.getId());
        }
        List<Map<String, Object>> rows = enrollmentMapper.selectMaps(new QueryWrapper<Enrollment>()
                .select("link_id",
                        "COUNT(*) AS total",
                        "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS pending",
                        "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS confirmed",
                        "SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) AS rejected",
                        "MAX(create_time) AS last_time")
                .in("link_id", ids)
                .groupBy("link_id"));
        if (rows == null) {
            return result;
        }
        for (Map<String, Object> row : rows) {
            Integer linkId = intValue(row.get("link_id"));
            if (linkId == null) {
                continue;
            }
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("total", longValue(row.get("total")));
            item.put("pending", longValue(row.get("pending")));
            item.put("confirmed", longValue(row.get("confirmed")));
            item.put("rejected", longValue(row.get("rejected")));
            item.put("lastSubmitAt", formatTime(longValue(row.get("last_time"))));
            result.put(linkId, item);
        }
        return result;
    }

    private String nextToken() {
        for (int i = 0; i < 10; i++) {
            String token = "EN" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                    + ThreadLocalRandom.current().nextInt(100000, 1000000);
            Long count = enrollLinkMapper.selectCount(new QueryWrapper<EnrollLink>().eq("token", token));
            if (count == null || count == 0) {
                return token;
            }
        }
        throw new OperateException("报名链接生成失败，请重试");
    }

    // ---- 报名名单 ----------------------------------------------------------

    @Override
    public PageResult<Map<String, Object>> enrollmentList(PageValidate pageValidate, Integer sessionId,
                                                          Integer linkId, String token, String keyword,
                                                          String phone, String studentName, Integer status,
                                                          String dateStart, String dateEnd) {
        QueryWrapper<Enrollment> query = enrollmentQuery(sessionId, linkId, token, keyword, phone,
                studentName, status, dateStart, dateEnd);
        query.orderByDesc("id");
        IPage<Enrollment> iPage = enrollmentMapper.selectPage(
                new Page<>(pageValidate.getPage_no(), pageValidate.getPage_size()), query);
        List<Map<String, Object>> list = buildEnrollmentRows(iPage.getRecords());
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list,
                enrollmentSummary(sessionId, linkId, token, keyword, phone, studentName, status,
                        dateStart, dateEnd));
    }

    @Override
    public Map<String, Object> exportEnrollments(Integer sessionId, Integer linkId, String token,
                                                 String keyword, String phone, String studentName,
                                                 Integer status, String dateStart, String dateEnd) {
        QueryWrapper<Enrollment> query = enrollmentQuery(sessionId, linkId, token, keyword, phone,
                studentName, status, dateStart, dateEnd);
        query.orderByDesc("id").last("limit " + EXPORT_LIMIT);
        List<Enrollment> records = enrollmentMapper.selectList(query);
        List<EnrollmentExportVo> rows = new ArrayList<>();
        for (Map<String, Object> item : buildEnrollmentRows(records)) {
            EnrollmentExportVo vo = new EnrollmentExportVo();
            vo.setCreatedAt(text(item.get("createdAt")));
            vo.setLinkId(intValue(item.get("linkId")));
            vo.setToken(text(item.get("token")));
            vo.setSessionId(intValue(item.get("sessionId")));
            vo.setCourseTitle(text(item.get("courseTitle")));
            vo.setDate(text(item.get("date")));
            vo.setTime(text(item.get("time")));
            vo.setAreaName(text(item.get("areaName")));
            vo.setParentName(text(item.get("parentName")));
            vo.setPhone(text(item.get("phone")));
            vo.setStudentName(text(item.get("studentName")));
            vo.setGender(text(item.get("gender")));
            vo.setAge(intValue(item.get("age")));
            vo.setStatusText(text(item.get("statusText")));
            vo.setNote(text(item.get("note")));
            rows.add(vo);
        }

        String fileName = "enrollment-roster-" + LocalDateTime.now().format(FILENAME_FMT)
                + "-" + ToolUtils.makeUUID().substring(0, 6) + ".xlsx";
        File target = exportFileService.allocate(fileName);
        EasyExcel.write(target.getAbsolutePath()).head(EnrollmentExportVo.class)
                .excelType(ExcelTypeEnum.XLSX).sheet("报名名单").doWrite(rows);

        Map<String, Object> result = new LinkedHashMap<>();
        result.putAll(exportFileService.issue(target, fileName));
        result.put("fileName", fileName);
        result.put("rowCount", rows.size());
        result.put("summary", enrollmentSummary(sessionId, linkId, token, keyword, phone,
                studentName, status, dateStart, dateEnd));
        return result;
    }

    // ---- 活动报名名单（含支付状态） ----------------------------------------

    @Override
    public PageResult<Map<String, Object>> activitySignupList(PageValidate pageValidate, Integer activityId,
                                                              Integer status, Integer payStatus,
                                                              String keyword, String dateStart,
                                                              String dateEnd) {
        int pageNo = pageValidate.getPage_no() == null || pageValidate.getPage_no() < 1
                ? 1 : pageValidate.getPage_no();
        int pageSize = pageValidate.getPage_size() == null || pageValidate.getPage_size() < 1
                ? 20 : pageValidate.getPage_size();
        Integer statusFilter = signupStatusFilter(status);
        Integer payFilter = payStatusFilter(payStatus);
        String like = likeKeyword(keyword);
        Long start = dateTimestamp(dateStart, false);
        Long end = dateTimestamp(dateEnd, true);

        long total = activityRosterQueryMapper.countSignup(activityId, statusFilter, payFilter,
                like, start, end);
        List<Map<String, Object>> records = activityRosterQueryMapper.pageSignup(activityId,
                statusFilter, payFilter, like, start, end, (pageNo - 1) * pageSize, pageSize);
        List<Map<String, Object>> list = new ArrayList<>();
        if (records != null) {
            for (Map<String, Object> record : records) {
                list.add(activitySignupItem(record));
            }
        }
        attachCancelableState(list);
        return PageResult.iPageHandle(total, (long) pageNo, (long) pageSize, list,
                activitySignupSummary(activityId, statusFilter, payFilter, like, start, end,
                        dateStart, dateEnd));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> cancelActivitySignup(Integer adminId, Integer signupId, String remark) {
        Map<String, Object> result = activitySignupCancelService.cancelForSignup(signupId, remark);
        log.info("后台取消活动报名: adminId={} signupId={} activityId={} mode={} refundId={}",
                adminId, signupId, result.get("activityId"), result.get("mode"), result.get("refundId"));
        return result;
    }

    /**
     * 报名名单按钮态：仅「未开始 + 已报名 + 无待审核退款」可点取消；
     * 退款审核中单独标记，前端不再提供第二次入口。
     */
    private void attachCancelableState(List<Map<String, Object>> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Set<Integer> orderIds = new HashSet<>();
        for (Map<String, Object> item : list) {
            Integer orderId = intValue(item.get("orderId"));
            if (orderId != null && orderId > 0) {
                orderIds.add(orderId);
            }
        }
        Set<Integer> pendingOrderIds = new HashSet<>();
        if (!orderIds.isEmpty()) {
            List<CourseRefund> pendings = courseRefundMapper.selectList(new QueryWrapper<CourseRefund>()
                    .select("order_id")
                    .eq("status", 0)
                    .isNull("delete_time")
                    .in("order_id", orderIds));
            for (CourseRefund refund : pendings) {
                if (refund.getOrderId() != null) {
                    pendingOrderIds.add(refund.getOrderId());
                }
            }
        }
        for (Map<String, Object> item : list) {
            Integer signupStatus = intValue(item.get("status"));
            Integer orderId = intValue(item.get("orderId"));
            boolean started = activityStarted(text(item.get("activityDate")));
            boolean refundPending = orderId != null && pendingOrderIds.contains(orderId);
            item.put("refundPending", refundPending);
            item.put("cancelable", signupStatus != null && signupStatus == ActivitySignup.STATUS_PAID
                    && !started && !refundPending);
        }
    }

    private boolean activityStarted(String activityDate) {
        if (activityDate == null || activityDate.trim().isEmpty()) {
            return false;
        }
        try {
            return LocalDate.parse(activityDate.trim()).isBefore(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Map<String, Object> exportActivitySignups(Integer activityId, Integer status,
                                                     Integer payStatus, String keyword,
                                                     String dateStart, String dateEnd) {
        Integer statusFilter = signupStatusFilter(status);
        Integer payFilter = payStatusFilter(payStatus);
        String like = likeKeyword(keyword);
        Long start = dateTimestamp(dateStart, false);
        Long end = dateTimestamp(dateEnd, true);
        List<Map<String, Object>> records = activityRosterQueryMapper.pageSignup(activityId,
                statusFilter, payFilter, like, start, end, 0, EXPORT_LIMIT);
        List<ActivitySignupExportVo> rows = new ArrayList<>();
        if (records != null) {
            for (Map<String, Object> record : records) {
                Map<String, Object> item = activitySignupItem(record);
                ActivitySignupExportVo vo = new ActivitySignupExportVo();
                vo.setCreatedAt(text(item.get("createdAt")));
                vo.setActivityTitle(text(item.get("activityTitle")));
                vo.setActivityDate(text(item.get("activityDate")));
                vo.setActivityTime(text(item.get("activityTime")));
                vo.setActivityPlace(text(item.get("activityPlace")));
                vo.setAreaName(text(item.get("areaName")));
                vo.setParentName(text(item.get("parentName")));
                vo.setParentMobile(text(item.get("parentMobile")));
                vo.setStudentName(text(item.get("studentName")));
                vo.setStatusText(text(item.get("statusText")));
                vo.setPayStatusText(text(item.get("payStatusText")));
                vo.setOrderSn(text(item.get("orderSn")));
                BigDecimal amount = item.get("orderAmount") instanceof BigDecimal
                        ? (BigDecimal) item.get("orderAmount") : BigDecimal.ZERO;
                vo.setOrderAmount(amount.toPlainString());
                vo.setPayTime(text(item.get("payTime")));
                rows.add(vo);
            }
        }

        String fileName = "activity-roster-" + LocalDateTime.now().format(FILENAME_FMT)
                + "-" + ToolUtils.makeUUID().substring(0, 6) + ".xlsx";
        File target = exportFileService.allocate(fileName);
        EasyExcel.write(target.getAbsolutePath()).head(ActivitySignupExportVo.class)
                .excelType(ExcelTypeEnum.XLSX).sheet("活动报名名单").doWrite(rows);

        Map<String, Object> result = new LinkedHashMap<>();
        result.putAll(exportFileService.issue(target, fileName));
        result.put("fileName", fileName);
        result.put("rowCount", rows.size());
        result.put("summary", activitySignupSummary(activityId, statusFilter, payFilter, like,
                start, end, dateStart, dateEnd));
        return result;
    }

    private Map<String, Object> activitySignupItem(Map<String, Object> row) {
        Integer rawPayStatus = intValue(row.get("payStatus"));
        Integer payStatus = rawPayStatus == null ? -1 : rawPayStatus;
        String realName = text(row.get("realName"));
        String nickname = text(row.get("nickname"));
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", intValue(row.get("id")));
        item.put("activityId", intValue(row.get("activityId")));
        item.put("activityTitle", text(row.get("activityTitle")));
        item.put("activityDate", text(row.get("activityDate")));
        item.put("activityTime", text(row.get("activityTime")));
        item.put("activityPlace", text(row.get("activityPlace")));
        item.put("areaName", text(row.get("areaName")));
        item.put("parentName", realName.isEmpty() ? nickname : realName);
        item.put("parentMobile", text(row.get("parentMobile")));
        item.put("studentId", intValue(row.get("studentId")));
        item.put("studentName", text(row.get("studentName")));
        item.put("status", intValue(row.get("status")));
        item.put("statusText", signupStatusText(intValue(row.get("status"))));
        item.put("orderId", intValue(row.get("orderId")) == null ? 0 : intValue(row.get("orderId")));
        item.put("orderSn", text(row.get("orderSn")));
        item.put("orderAmount", row.get("orderAmount") == null ? BigDecimal.ZERO : row.get("orderAmount"));
        item.put("payStatus", payStatus);
        item.put("payStatusText", payStatusText(payStatus));
        int payWay = intValue(row.get("payWay")) == null ? 0 : intValue(row.get("payWay"));
        item.put("payWay", payWay);
        item.put("payWayText", payWay > 0 ? PaymentEnum.getPayWayMsg(payWay) : "");
        item.put("refundStatus", intValue(row.get("refundStatus")) == null
                ? 0 : intValue(row.get("refundStatus")));
        item.put("payTime", formatTime(longValue(row.get("payTime"))));
        item.put("createdAt", formatTime(longValue(row.get("createTime"))));
        item.put("createTime", longValue(row.get("createTime")));
        return item;
    }

    private Map<String, Object> activitySignupSummary(Integer activityId, Integer status,
                                                      Integer payStatus, String keyword,
                                                      Long dateStart, Long dateEnd,
                                                      String dateStartText, String dateEndText) {
        Map<String, Object> agg = activityRosterQueryMapper.summarySignup(activityId, status,
                payStatus, keyword, dateStart, dateEnd);
        if (agg == null) {
            agg = new HashMap<>();
        }
        long pending = longValue(agg.get("pendingCount"));
        long cancelled = longValue(agg.get("cancelledCount"));
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalCount", longValue(agg.get("totalCount")));
        summary.put("uniqueCount", longValue(agg.get("uniqueCount")));
        summary.put("paidCount", longValue(agg.get("paidCount")));
        summary.put("unpaidCount", pending + cancelled);
        summary.put("pendingCount", pending);
        summary.put("cancelledCount", cancelled);
        summary.put("freeCount", longValue(agg.get("freeCount")));
        summary.put("paidAmount", agg.get("paidAmount") == null ? BigDecimal.ZERO : agg.get("paidAmount"));
        summary.put("dedupeRule", SIGNUP_DEDUPE_RULE);
        summary.put("dateStart", trimToEmpty(dateStartText));
        summary.put("dateEnd", trimToEmpty(dateEndText));
        return summary;
    }

    private Integer signupStatusFilter(Integer status) {
        if (status == null) {
            return null;
        }
        return status == 0 || status == 1 || status == 3 ? status : null;
    }

    private Integer payStatusFilter(Integer payStatus) {
        if (payStatus == null) {
            return null;
        }
        return payStatus == -1 || payStatus == 0 || payStatus == 1 || payStatus == 4
                ? payStatus : null;
    }

    private String likeKeyword(String keyword) {
        String text = trimToEmpty(keyword);
        return text.isEmpty() ? null : "%" + text + "%";
    }

    private String payStatusText(Integer payStatus) {
        if (payStatus == null || payStatus == -1) {
            return "免费";
        }
        if (payStatus == 1) {
            return "已支付";
        }
        if (payStatus == 4) {
            return "已取消";
        }
        return "待支付";
    }

    private QueryWrapper<Enrollment> enrollmentQuery(Integer sessionId, Integer linkId, String token,
                                                     String keyword, String phone, String studentName,
                                                     Integer status, String dateStart, String dateEnd) {
        QueryWrapper<Enrollment> query = new QueryWrapper<>();
        if (sessionId != null && sessionId > 0) {
            query.eq("session_id", sessionId);
        }
        if (linkId != null && linkId > 0) {
            query.eq("link_id", linkId);
        }
        String tokenText = trimToEmpty(token);
        if (!tokenText.isEmpty()) {
            query.eq("token", tokenText);
        }
        String phoneText = trimToEmpty(phone);
        if (!phoneText.isEmpty()) {
            query.like("phone", phoneText);
        }
        String studentText = trimToEmpty(studentName);
        if (!studentText.isEmpty()) {
            query.like("student_name", studentText);
        }
        if (status != null && status >= 0 && status <= 2) {
            query.eq("status", status);
        }
        String kw = trimToEmpty(keyword);
        if (!kw.isEmpty()) {
            query.and(wrapper -> wrapper.like("phone", kw)
                    .or().like("student_name", kw)
                    .or().like("parent_name", kw));
        }
        Long start = dateTimestamp(dateStart, false);
        if (start != null) {
            query.ge("create_time", start);
        }
        Long end = dateTimestamp(dateEnd, true);
        if (end != null) {
            query.lt("create_time", end);
        }
        return query;
    }

    private Long dateTimestamp(String date, boolean endOfDay) {
        String text = trimToEmpty(date);
        if (!text.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return null;
        }
        Long timestamp = TimeUtils.dateToTimestamp(text);
        if (timestamp == null || timestamp <= 0) {
            return null;
        }
        return endOfDay ? timestamp + 86400L : timestamp;
    }

    private Map<String, Object> enrollmentSummary(Integer sessionId, Integer linkId, String token,
                                                  String keyword, String phone, String studentName,
                                                  Integer status, String dateStart, String dateEnd) {
        QueryWrapper<Enrollment> query = enrollmentQuery(sessionId, linkId, token, keyword, phone,
                studentName, status, dateStart, dateEnd);
        Long total = enrollmentMapper.selectCount(query);
        QueryWrapper<Enrollment> aggQuery = enrollmentQuery(sessionId, linkId, token, keyword, phone,
                studentName, status, dateStart, dateEnd);
        List<Map<String, Object>> rows = enrollmentMapper.selectMaps(aggQuery.select(
                "COUNT(*) AS total_count",
                "COUNT(DISTINCT CONCAT_WS('|', session_id, phone, student_name)) AS unique_count",
                "COUNT(DISTINCT session_id) AS session_count",
                "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS pending_count",
                "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS confirmed_count",
                "SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) AS rejected_count"));
        Map<String, Object> agg = rows == null || rows.isEmpty() ? new HashMap<>() : rows.get(0);
        long totalCount = total == null ? 0 : total;
        long uniqueCount = longValue(agg.get("unique_count"));
        if (uniqueCount <= 0 && totalCount > 0) {
            // 兜底：聚合查询异常时按列表已去重口径处理，不把 0 条误报给前端。
            uniqueCount = totalCount;
        }
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalCount", totalCount);
        summary.put("uniqueCount", uniqueCount);
        summary.put("duplicateCount", Math.max(totalCount - uniqueCount, 0));
        summary.put("sessionCount", longValue(agg.get("session_count")));
        summary.put("pendingCount", longValue(agg.get("pending_count")));
        summary.put("confirmedCount", longValue(agg.get("confirmed_count")));
        summary.put("rejectedCount", longValue(agg.get("rejected_count")));
        summary.put("dedupeRule", DEDUPE_RULE);
        summary.put("dateStart", trimToEmpty(dateStart));
        summary.put("dateEnd", trimToEmpty(dateEnd));
        return summary;
    }

    private List<Map<String, Object>> buildEnrollmentRows(List<Enrollment> records) {
        Set<Integer> sessionIds = new HashSet<>();
        for (Enrollment enrollment : records) {
            sessionIds.add(enrollment.getSessionId());
        }
        Map<Integer, CourseLesson> lessons = mapLessonsByIds(sessionIds);
        Map<Integer, CourseClass> classes = mapClasses(lessons.values());
        Map<Integer, String> areaNames = mapAreaNames(classes.values());

        List<Map<String, Object>> list = new ArrayList<>();
        for (Enrollment enrollment : records) {
            CourseLesson lesson = lessons.get(enrollment.getSessionId());
            CourseClass courseClass = lesson == null ? null : classes.get(lesson.getClassId());
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", enrollment.getId());
            item.put("linkId", enrollment.getLinkId());
            item.put("token", nvl(enrollment.getToken()));
            item.put("linkPath", linkPath(enrollment.getToken(), enrollment.getSessionId()));
            item.put("sessionId", enrollment.getSessionId());
            item.put("courseId", enrollment.getCourseId());
            item.put("courseTitle", courseClass == null ? "" : nvl(courseClass.getName()));
            item.put("date", lesson == null ? "" : nvl(lesson.getLessonDate()));
            item.put("time", lesson == null || courseClass == null ? ""
                    : ClassScheduleUtil.resolveLessonTimeSlot(lesson, courseClass));
            item.put("areaName", courseClass == null ? "" : areaNames.getOrDefault(courseClass.getAreaId(), ""));
            item.put("parentName", nvl(enrollment.getParentName()));
            item.put("phone", nvl(enrollment.getPhone()));
            item.put("studentName", nvl(enrollment.getStudentName()));
            item.put("gender", nvl(enrollment.getGender()));
            item.put("age", enrollment.getAge());
            item.put("note", nvl(enrollment.getNote()));
            item.put("status", enrollment.getStatus());
            item.put("statusText", enrollmentStatusText(enrollment.getStatus()));
            item.put("dedupeKey", enrollment.getSessionId() + "|" + nvl(enrollment.getPhone())
                    + "|" + nvl(enrollment.getStudentName()));
            item.put("duplicate", false);
            item.put("createdAt", formatTime(enrollment.getCreateTime()));
            item.put("createTime", enrollment.getCreateTime());
            list.add(item);
        }
        return list;
    }

    // ---- 开班申请 ----------------------------------------------------------

    @Override
    public PageResult<Map<String, Object>> applyGroups(PageValidate pageValidate, Integer streetId,
                                                       String courseType, String status) {
        QueryWrapper<ClassApplyGroup> query = new QueryWrapper<>();
        if (streetId != null && streetId > 0) {
            query.eq("street_id", streetId);
        }
        String type = courseType == null ? "" : courseType.trim();
        if (!type.isEmpty()) {
            query.eq("course_type", type);
        }
        String state = status == null ? "" : status.trim();
        if (!state.isEmpty()) {
            query.eq("status", state);
        }
        query.orderByDesc("joined").orderByDesc("update_time").orderByDesc("id");
        IPage<ClassApplyGroup> iPage = classApplyGroupMapper.selectPage(
                new Page<>(pageValidate.getPage_no(), pageValidate.getPage_size()), query);

        Set<Integer> streetIds = new HashSet<>();
        for (ClassApplyGroup group : iPage.getRecords()) {
            streetIds.add(group.getStreetId());
        }
        Map<Integer, Region> regions = mapRegions(streetIds);

        List<Map<String, Object>> list = new ArrayList<>();
        for (ClassApplyGroup group : iPage.getRecords()) {
            Region street = regions.get(group.getStreetId());
            Region district = street == null ? null : regions.get(street.getParentId());
            int target = group.getTarget() == null ? 5 : group.getTarget();
            int joined = group.getJoined() == null ? 0 : group.getJoined();
            Long applyCount = classApplyMapper.selectCount(new QueryWrapper<com.mdd.common.entity.activity.ClassApply>()
                    .eq("group_id", group.getId())
                    .isNull("delete_time"));

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", group.getId());
            item.put("streetId", group.getStreetId());
            item.put("streetName", street == null ? "" : nvl(street.getName()));
            item.put("districtName", district == null ? "" : nvl(district.getName()));
            item.put("courseType", nvl(group.getCourseType()));
            item.put("joined", joined);
            item.put("target", target);
            item.put("need", Math.max(target - joined, 0));
            item.put("status", nvl(group.getStatus()));
            item.put("statusText", applyStatusText(group.getStatus()));
            item.put("applyCount", applyCount == null ? 0 : applyCount);
            item.put("createdAt", formatTime(group.getCreateTime()));
            item.put("updatedAt", formatTime(group.getUpdateTime()));
            list.add(item);
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    // ---- 活动报名超员待处理告警（#61） ------------------------------------

    @Override
    public PageResult<ActivityEnrollAlertListVo> enrollExceptions(PageValidate pageValidate, Integer status,
                                                                  Integer activityId, String keyword) {
        QueryWrapper<ActivityEnrollAlert> query = exceptionQuery(status, activityId, keyword);
        query.orderByDesc("id");
        IPage<ActivityEnrollAlert> iPage = activityEnrollAlertMapper.selectPage(
                new Page<>(pageValidate.getPage_no(), pageValidate.getPage_size()), query);
        List<ActivityEnrollAlertListVo> list = buildAlertRows(iPage.getRecords());
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list,
                exceptionSummary());
    }

    @Override
    public ActivityEnrollAlertDetailVo enrollExceptionDetail(Integer id) {
        ActivityEnrollAlert alert = requireAlert(id);
        ActivityEnrollAlertDetailVo detail = new ActivityEnrollAlertDetailVo();
        BeanUtils.copyProperties(buildAlertRows(java.util.Collections.singletonList(alert)).get(0), detail);
        List<ActivitySignup> signups = activitySignupMapper.selectList(new QueryWrapper<ActivitySignup>()
                .eq("order_id", alert.getOrderId())
                .orderByAsc("id"));
        Map<Integer, String> names = studentNames(alertStudentIds(alert));
        List<ActivityEnrollAlertSignupVo> rows = new ArrayList<>();
        for (ActivitySignup signup : signups) {
            ActivityEnrollAlertSignupVo item = new ActivityEnrollAlertSignupVo();
            item.setId(signup.getId());
            item.setStudentId(signup.getStudentId());
            item.setStudentName(names.getOrDefault(signup.getStudentId(), ""));
            item.setStatus(signup.getStatus());
            item.setStatusText(signupStatusText(signup.getStatus()));
            rows.add(item);
        }
        detail.setSignups(rows);
        return detail;
    }

    @Override
    public Map<String, Object> handleEnrollException(Integer adminId, Integer id, String remark) {
        ActivityEnrollAlert alert = requireAlert(id);
        if (alert.getStatus() == null || alert.getStatus() != ActivityEnrollAlert.STATUS_PENDING) {
            throw new OperateException(alert.getStatus() != null && alert.getStatus() == ActivityEnrollAlert.STATUS_REFUNDED
                    ? "该异常报名已退款，无需再标记处理" : "该异常报名已在处理或已处理");
        }
        long now = TimeUtils.timestamp();
        String adminName = adminName(adminId);
        String safeRemark = trimToMax(remark, 255);
        int claimed = activityEnrollAlertMapper.update(null,
                new UpdateWrapper<ActivityEnrollAlert>()
                        .set("status", ActivityEnrollAlert.STATUS_HANDLED)
                        .set("remark", safeRemark)
                        .set("handler_id", adminId == null ? 0 : adminId)
                        .set("handler_name", adminName)
                        .set("handle_time", now)
                        .set("update_time", now)
                        .eq("id", id)
                        .eq("status", ActivityEnrollAlert.STATUS_PENDING));
        if (claimed != 1) {
            throw new OperateException("该异常报名已在处理或已处理，请刷新后查看");
        }
        alert.setStatus(ActivityEnrollAlert.STATUS_HANDLED);
        alert.setRemark(safeRemark);
        alert.setHandlerId(adminId == null ? 0 : adminId);
        alert.setHandlerName(adminName);
        alert.setHandleTime(now);
        alert.setUpdateTime(now);
        log.info("活动超员告警标记已处理: alertId={} orderSn={} adminId={}",
                alert.getId(), alert.getOrderSn(), adminId);
        return alertResult(alert);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> refundEnrollException(Integer adminId, Integer id, String remark) {
        // 事务边界（#61）：订单退款落库、报名明细取消、活动名额释放、告警状态必须同生共死。
        // 微信支付退款是外部不可回滚操作：FinanceRechargerServiceImpl 使用 REQUIRES_NEW
        // 先独立提交退款记录/订单退款状态，再次进入本方法时 order.refund_status=1 会跳过
        // 外部退款，只补做本地取消/释放/告警（幂等补偿）；外部退款成功后即使本事务回滚，
        // 退款记录仍在，可凭 la_refund_record / 微信退款单号对账，不会重复退款。
        ActivityEnrollAlert alert = requireAlert(id);
        if (alert.getStatus() != null && alert.getStatus() == ActivityEnrollAlert.STATUS_REFUNDED) {
            throw new OperateException("该异常报名已退款，请勿重复操作");
        }
        if (alert.getStatus() == null || alert.getStatus() != ActivityEnrollAlert.STATUS_PENDING) {
            throw new OperateException("该异常报名已在处理或已处理，请刷新后查看");
        }
        long claimNow = TimeUtils.timestamp();
        int claimed = activityEnrollAlertMapper.update(null,
                new UpdateWrapper<ActivityEnrollAlert>()
                        .set("status", ActivityEnrollAlert.STATUS_PROCESSING)
                        .set("update_time", claimNow)
                        .eq("id", id)
                        .eq("status", ActivityEnrollAlert.STATUS_PENDING));
        if (claimed != 1) {
            throw new OperateException("该异常报名正在被其他操作处理，请刷新后查看");
        }
        RechargeOrder order = rechargeOrderMapper.selectById(alert.getOrderId());
        if (order == null || order.getDeleteTime() != null) {
            throw new OperateException("异常报名关联订单不存在");
        }
        if (order.getRefundStatus() == null || order.getRefundStatus() != 1) {
            if (order.getPayStatus() == null || order.getPayStatus() != PaymentEnum.OK_PAID.getCode()) {
                throw new OperateException("订单未支付，不能发起退款");
            }
            try {
                // 复用既有缴费退款通道（余额/微信/支付宝），不另造退款链路。
                iFinanceRechargerService.refund(order.getId(), adminId);
            } catch (OperateException e) {
                log.warn("活动超员告警发起退款失败: alertId={} orderSn={} msg={}",
                        alert.getId(), alert.getOrderSn(), e.getMessage());
                throw new OperateException("发起退款失败：" + e.getMessage());
            } catch (Exception e) {
                log.error("活动超员告警发起退款异常: alertId={} orderSn={}",
                        alert.getId(), alert.getOrderSn(), e);
                throw new OperateException("发起退款失败，请稍后重试或到缴费记录处理");
            }
        }
        // 退款成功（或已退款重试）：取消报名明细并释放名额，保证用户端不再显示已报名。
        // #186：按报名批次整批释放（学员 + 陪同），历史数据（booking_id=0）逐行释放 1。
        List<ActivitySignup> signups = activitySignupMapper.selectList(new QueryWrapper<ActivitySignup>()
                .eq("order_id", alert.getOrderId()));
        long now = TimeUtils.timestamp();
        Map<Integer, List<ActivitySignup>> batches = new LinkedHashMap<>();
        List<ActivitySignup> legacy = new ArrayList<>();
        for (ActivitySignup signup : signups) {
            int bookingId = signup.getBookingId() == null ? 0 : signup.getBookingId();
            if (bookingId > 0) {
                batches.computeIfAbsent(bookingId, key -> new ArrayList<>()).add(signup);
            } else {
                legacy.add(signup);
            }
        }
        int released = 0;
        for (Map.Entry<Integer, List<ActivitySignup>> entry : batches.entrySet()) {
            ActivityBooking booking = activityBookingMapper.selectById(entry.getKey());
            if (booking == null) {
                legacy.addAll(entry.getValue());
                continue;
            }
            int cancelledRows = 0;
            for (ActivitySignup signup : entry.getValue()) {
                if (signup.getStatus() != null && signup.getStatus() == ActivitySignup.STATUS_CANCELLED) {
                    continue;
                }
                int updated = activitySignupMapper.update(null,
                        new UpdateWrapper<ActivitySignup>()
                                .set("status", ActivitySignup.STATUS_CANCELLED)
                                .set("update_time", now)
                                .eq("id", signup.getId())
                                .ne("status", ActivitySignup.STATUS_CANCELLED));
                if (updated > 0) {
                    cancelledRows += 1;
                }
            }
            if (cancelledRows > 0) {
                int students = booking.getStudentCount() == null ? 0 : booking.getStudentCount();
                int companions = booking.getCompanionCount() == null ? 0 : booking.getCompanionCount();
                // 批次状态是名额释放的唯一闸门：只有把批次从非取消抢到取消的这一次
                // 才计入 released。与超时释放并发时，后到者更新 0 行，不会重复扣名额。
                int bookingClaimed = activityBookingMapper.update(null,
                        new UpdateWrapper<ActivityBooking>()
                                .set("status", ActivityBooking.STATUS_CANCELLED)
                                .set("update_time", now)
                                .eq("id", booking.getId())
                                .ne("status", ActivityBooking.STATUS_CANCELLED));
                if (bookingClaimed > 0) {
                    released += students + companions;
                }
            }
        }
        for (ActivitySignup signup : legacy) {
            if (signup.getStatus() != null && signup.getStatus() == ActivitySignup.STATUS_CANCELLED) {
                continue;
            }
            int updated = activitySignupMapper.update(null,
                    new UpdateWrapper<ActivitySignup>()
                            .set("status", ActivitySignup.STATUS_CANCELLED)
                            .set("update_time", now)
                            .eq("id", signup.getId())
                            .ne("status", ActivitySignup.STATUS_CANCELLED));
            if (updated > 0) {
                released += 1;
            }
        }
        // 只有回调时确实把名额加回去的告警才释放；活动下架/删除时 joined 未增加，
        // 再减一次会把原本正确的占位数扣少。原因串由 like-front 统一写入。
        boolean seatWasIncreased = alert.getReason() == null
                || !alert.getReason().contains("未增加名额");
        if (released > 0 && seatWasIncreased) {
            activityMapper.update(null,
                    new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<Activity>()
                            .setSql("joined = GREATEST(joined - " + released + ", 0)")
                            .set("update_time", now)
                            .eq("id", alert.getActivityId()));
        }
        alert.setStatus(ActivityEnrollAlert.STATUS_REFUNDED);
        alert.setRemark(trimToMax(remark, 255));
        alert.setHandlerId(adminId == null ? 0 : adminId);
        alert.setHandlerName(adminName(adminId));
        alert.setHandleTime(now);
        alert.setUpdateTime(now);
        activityEnrollAlertMapper.updateById(alert);
        log.info("活动超员告警已退款: alertId={} orderSn={} released={} seatWasIncreased={} adminId={}",
                alert.getId(), alert.getOrderSn(), released, seatWasIncreased, adminId);
        return alertResult(alert);
    }

    // ---- 活动管理 CRUD（#186） --------------------------------------------

    @Override
    public PageResult<Activity> activityList(PageValidate pageValidate, String keyword, Integer status) {
        QueryWrapper<Activity> query = new QueryWrapper<Activity>().isNull("delete_time");
        String kw = trimToEmpty(keyword);
        if (!kw.isEmpty()) {
            query.and(wrapper -> wrapper.like("title", kw).or().like("place", kw));
        }
        if (status != null && status >= 0) {
            query.eq("status", status);
        }
        query.orderByAsc("sort").orderByDesc("id");
        IPage<Activity> page = activityMapper.selectPage(
                new Page<>(pageValidate.getPage_no(), pageValidate.getPage_size()), query);
        return PageResult.iPageHandle(page);
    }

    @Override
    public Map<String, Object> activityDetail(Integer id) {
        return activityItem(requireActivity(id));
    }

    @Override
    public void addActivity(ActivityCreateValidate createValidate) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(createValidate, activity);
        normalizeActivity(activity);
        activity.setJoined(0);
        long now = TimeUtils.timestamp();
        activity.setCreateTime(now);
        activity.setUpdateTime(now);
        activityMapper.insert(activity);
    }

    @Override
    public void editActivity(ActivityUpdateValidate updateValidate) {
        requireActivity(updateValidate.getId());
        long now = TimeUtils.timestamp();
        int updated = activityMapper.update(null, new UpdateWrapper<Activity>()
                .set("title", trimToEmpty(updateValidate.getTitle()))
                .set("cover", nvl(updateValidate.getCover()))
                .set("activity_date", trimToEmpty(updateValidate.getActivityDate()))
                .set("time_slot", trimToEmpty(updateValidate.getTimeSlot()))
                .set("place", trimToEmpty(updateValidate.getPlace()))
                .set("area_id", updateValidate.getAreaId() == null ? 0 : updateValidate.getAreaId())
                .set("fee", updateValidate.getFee())
                .set("capacity", updateValidate.getCapacity())
                .set("companion_limit", updateValidate.getCompanionLimit())
                .set("deadline", trimToEmpty(updateValidate.getDeadline()))
                .set("description", nvl(updateValidate.getDescription()))
                .set("tags", nvl(updateValidate.getTags()))
                .set("notice", nvl(updateValidate.getNotice()))
                .set("status", updateValidate.getStatus())
                .set("sort", updateValidate.getSort() == null ? 0 : updateValidate.getSort())
                .set("update_time", now)
                .eq("id", updateValidate.getId())
                .isNull("delete_time"));
        if (updated <= 0) {
            throw new OperateException("活动不存在");
        }
    }

    @Override
    public void updateActivityStatus(Integer id, Integer status) {
        if (id == null || id <= 0) {
            throw new OperateException("活动不存在");
        }
        if (status == null || (status != 0 && status != 1)) {
            throw new OperateException("上下架状态只能是 0 或 1");
        }
        int updated = activityMapper.update(null, new UpdateWrapper<Activity>()
                .set("status", status)
                .set("update_time", TimeUtils.timestamp())
                .eq("id", id)
                .isNull("delete_time"));
        if (updated <= 0) {
            throw new OperateException("活动不存在");
        }
    }

    @Override
    public void deleteActivity(Integer id) {
        requireActivity(id);
        long paid = activitySignupMapper.selectCount(new QueryWrapper<ActivitySignup>()
                .eq("activity_id", id)
                .eq("status", ActivitySignup.STATUS_PAID));
        if (paid > 0) {
            throw new OperateException("已有报名，不能删除，请下架");
        }
        long now = TimeUtils.timestamp();
        int updated = activityMapper.update(null, new UpdateWrapper<Activity>()
                .set("delete_time", now)
                .set("update_time", now)
                .eq("id", id)
                .isNull("delete_time"));
        if (updated <= 0) {
            throw new OperateException("活动不存在");
        }
    }

    private Activity requireActivity(Integer id) {
        if (id == null || id <= 0) {
            throw new OperateException("活动不存在");
        }
        Activity activity = activityMapper.selectOne(new QueryWrapper<Activity>()
                .eq("id", id)
                .isNull("delete_time")
                .last("limit 1"));
        if (activity == null) {
            throw new OperateException("活动不存在");
        }
        return activity;
    }

    private void normalizeActivity(Activity activity) {
        activity.setTitle(trimToEmpty(activity.getTitle()));
        activity.setCover(nvl(activity.getCover()));
        activity.setActivityDate(trimToEmpty(activity.getActivityDate()));
        activity.setTimeSlot(trimToEmpty(activity.getTimeSlot()));
        activity.setPlace(trimToEmpty(activity.getPlace()));
        activity.setAreaId(activity.getAreaId() == null ? 0 : activity.getAreaId());
        activity.setDeadline(trimToEmpty(activity.getDeadline()));
        activity.setDescription(nvl(activity.getDescription()));
        activity.setTags(nvl(activity.getTags()));
        activity.setNotice(nvl(activity.getNotice()));
        activity.setStatus(activity.getStatus() == null ? 0 : activity.getStatus());
        activity.setSort(activity.getSort() == null ? 0 : activity.getSort());
    }

    private Map<String, Object> activityItem(Activity activity) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", activity.getId());
        item.put("title", nvl(activity.getTitle()));
        item.put("cover", nvl(activity.getCover()));
        item.put("activityDate", nvl(activity.getActivityDate()));
        item.put("timeSlot", nvl(activity.getTimeSlot()));
        item.put("place", nvl(activity.getPlace()));
        item.put("areaId", activity.getAreaId() == null ? 0 : activity.getAreaId());
        item.put("fee", activity.getFee() == null ? BigDecimal.ZERO : activity.getFee());
        item.put("capacity", activity.getCapacity() == null ? 0 : activity.getCapacity());
        item.put("joined", activity.getJoined() == null ? 0 : activity.getJoined());
        item.put("companionLimit",
                activity.getCompanionLimit() == null ? 0 : activity.getCompanionLimit());
        item.put("deadline", nvl(activity.getDeadline()));
        item.put("description", nvl(activity.getDescription()));
        item.put("tags", nvl(activity.getTags()));
        item.put("notice", nvl(activity.getNotice()));
        item.put("status", activity.getStatus() == null ? 0 : activity.getStatus());
        item.put("sort", activity.getSort() == null ? 0 : activity.getSort());
        item.put("createTime", activity.getCreateTime());
        item.put("updateTime", activity.getUpdateTime());
        return item;
    }

    private QueryWrapper<ActivityEnrollAlert> exceptionQuery(Integer status, Integer activityId, String keyword) {
        QueryWrapper<ActivityEnrollAlert> query = new QueryWrapper<>();
        if (status != null && status >= 0) {
            query.eq("status", status);
        }
        if (activityId != null && activityId > 0) {
            query.eq("activity_id", activityId);
        }
        String kw = trimToEmpty(keyword);
        if (!kw.isEmpty()) {
            query.and(wrapper -> wrapper.like("order_sn", kw).or().like("student_ids", kw));
        }
        return query;
    }

    private List<ActivityEnrollAlertListVo> buildAlertRows(List<ActivityEnrollAlert> alerts) {
        List<ActivityEnrollAlertListVo> rows = new ArrayList<>();
        if (alerts == null || alerts.isEmpty()) {
            return rows;
        }
        Set<Integer> activityIds = new HashSet<>();
        Set<Integer> orderIds = new HashSet<>();
        Set<Integer> userIds = new HashSet<>();
        Set<Integer> studentIds = new HashSet<>();
        for (ActivityEnrollAlert alert : alerts) {
            if (alert.getActivityId() != null && alert.getActivityId() > 0) {
                activityIds.add(alert.getActivityId());
            }
            if (alert.getOrderId() != null && alert.getOrderId() > 0) {
                orderIds.add(alert.getOrderId());
            }
            if (alert.getUserId() != null && alert.getUserId() > 0) {
                userIds.add(alert.getUserId());
            }
            studentIds.addAll(alertStudentIds(alert));
        }
        Map<Integer, Activity> activities = new HashMap<>();
        if (!activityIds.isEmpty()) {
            for (Activity activity : activityMapper.selectBatchIds(activityIds)) {
                activities.put(activity.getId(), activity);
            }
        }
        Map<Integer, RechargeOrder> orders = new HashMap<>();
        if (!orderIds.isEmpty()) {
            for (RechargeOrder order : rechargeOrderMapper.selectBatchIds(orderIds)) {
                orders.put(order.getId(), order);
            }
        }
        Map<Integer, User> users = new HashMap<>();
        if (!userIds.isEmpty()) {
            for (User user : userMapper.selectBatchIds(userIds)) {
                users.put(user.getId(), user);
            }
        }
        Map<Integer, String> names = studentNames(studentIds);
        for (ActivityEnrollAlert alert : alerts) {
            ActivityEnrollAlertListVo item = new ActivityEnrollAlertListVo();
            fillAlertRow(item, alert, activities.get(alert.getActivityId()),
                    orders.get(alert.getOrderId()), users.get(alert.getUserId()), names);
            rows.add(item);
        }
        return rows;
    }

    private void fillAlertRow(ActivityEnrollAlertListVo item, ActivityEnrollAlert alert,
                              Activity activity, RechargeOrder order, User user,
                              Map<Integer, String> names) {
        item.setId(alert.getId());
        item.setType(alert.getType());
        item.setTypeText(alert.getType() != null
                && alert.getType() == ActivityEnrollAlert.TYPE_OVERBOOKED_AFTER_RELEASE
                ? "满员后支付（超员生效）" : "异常报名");
        item.setStatus(alert.getStatus());
        item.setStatusText(alertStatusText(alert.getStatus()));
        item.setActivityId(alert.getActivityId());
        item.setActivityTitle(activity == null ? "" : nvl(activity.getTitle()));
        item.setActivityDate(activity == null ? "" : nvl(activity.getActivityDate()));
        item.setActivityTime(activity == null ? "" : nvl(activity.getTimeSlot()));
        item.setActivityPlace(activity == null ? "" : nvl(activity.getPlace()));
        item.setCapacity(activity == null || activity.getCapacity() == null ? 0 : activity.getCapacity());
        item.setJoined(activity == null || activity.getJoined() == null ? 0 : activity.getJoined());
        item.setOrderId(alert.getOrderId());
        item.setOrderSn(nvl(alert.getOrderSn()));
        item.setOrderPayStatus(order == null ? 0 : order.getPayStatus());
        item.setOrderPayStatusText(order == null ? "" : PaymentEnum.getPayStatusMsg(order.getPayStatus()));
        item.setOrderRefundStatus(order == null ? 0 : order.getRefundStatus());
        item.setAmount(alert.getAmount() == null ? BigDecimal.ZERO : alert.getAmount());
        item.setPayWay(alert.getPayWay());
        item.setPayWayText(alert.getPayWay() == null ? "" : PaymentEnum.getPayWayMsg(alert.getPayWay()));
        item.setUserId(alert.getUserId());
        item.setUserName(userName(user));
        item.setUserMobile(user == null ? "" : nvl(user.getMobile()));
        // 与原 Map 契约一致：null 也输出空串/0，避免前端字段缺失。
        item.setStudentIds(nvl(alert.getStudentIds()));
        item.setStudentNames(studentNamesText(alertStudentIds(alert), names));
        item.setStudentCount(alert.getStudentCount() == null ? 0 : alert.getStudentCount());
        item.setReason(nvl(alert.getReason()));
        item.setRemark(nvl(alert.getRemark()));
        item.setHandlerId(alert.getHandlerId() == null ? 0 : alert.getHandlerId());
        item.setHandlerName(nvl(alert.getHandlerName()));
        item.setHandleTime(alert.getHandleTime() == null ? 0 : alert.getHandleTime());
        item.setHandledAt(formatTime(alert.getHandleTime()));
        item.setCreatedAt(formatTime(alert.getCreateTime()));
        item.setUpdatedAt(formatTime(alert.getUpdateTime()));
    }

    private Map<String, Object> exceptionSummary() {
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("total", countAlerts(null));
        summary.put("pending", countAlerts(ActivityEnrollAlert.STATUS_PENDING));
        summary.put("handled", countAlerts(ActivityEnrollAlert.STATUS_HANDLED));
        summary.put("refunded", countAlerts(ActivityEnrollAlert.STATUS_REFUNDED));
        return summary;
    }

    private long countAlerts(Integer status) {
        QueryWrapper<ActivityEnrollAlert> query = new QueryWrapper<>();
        if (status != null) {
            query.eq("status", status);
        }
        Long count = activityEnrollAlertMapper.selectCount(query);
        return count == null ? 0 : count;
    }

    private ActivityEnrollAlert requireAlert(Integer id) {
        if (id == null || id <= 0) {
            throw new OperateException("请选择异常报名记录");
        }
        ActivityEnrollAlert alert = activityEnrollAlertMapper.selectById(id);
        if (alert == null) {
            throw new OperateException("异常报名记录不存在");
        }
        return alert;
    }

    private Map<String, Object> alertResult(ActivityEnrollAlert alert) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", alert.getId());
        result.put("orderId", alert.getOrderId());
        result.put("orderSn", alert.getOrderSn());
        result.put("status", alert.getStatus());
        result.put("statusText", alertStatusText(alert.getStatus()));
        result.put("handlerId", alert.getHandlerId());
        result.put("handlerName", alert.getHandlerName());
        result.put("handleTime", alert.getHandleTime());
        result.put("handledAt", formatTime(alert.getHandleTime()));
        result.put("remark", alert.getRemark());
        return result;
    }

    private String alertStatusText(Integer status) {
        if (status != null && status == ActivityEnrollAlert.STATUS_HANDLED) {
            return "已处理";
        }
        if (status != null && status == ActivityEnrollAlert.STATUS_REFUNDED) {
            return "已退款";
        }
        return "待处理";
    }

    private String signupStatusText(Integer status) {
        if (status != null && status == ActivitySignup.STATUS_PAID) {
            return "已支付";
        }
        if (status != null && status == ActivitySignup.STATUS_CANCELLED) {
            return "已取消";
        }
        return "待支付";
    }

    private List<Integer> alertStudentIds(ActivityEnrollAlert alert) {
        List<Integer> ids = new ArrayList<>();
        String text = trimToEmpty(alert.getStudentIds());
        if (text.isEmpty()) {
            return ids;
        }
        for (String part : text.split(",")) {
            String value = part.trim();
            if (value.isEmpty()) {
                continue;
            }
            try {
                ids.add(Integer.parseInt(value));
            } catch (NumberFormatException ignored) {
                // 忽略脏数据
            }
        }
        return ids;
    }

    private Map<Integer, String> studentNames(Collection<Integer> ids) {
        Map<Integer, String> result = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            return result;
        }
        for (Student student : studentMapper.selectBatchIds(ids)) {
            result.put(student.getId(), nvl(student.getName()));
        }
        return result;
    }

    private String studentNamesText(List<Integer> ids, Map<Integer, String> names) {
        List<String> namesList = new ArrayList<>();
        for (Integer id : ids) {
            String name = names.get(id);
            if (name != null && !name.isEmpty()) {
                namesList.add(name);
            }
        }
        return String.join("、", namesList);
    }

    private String userName(User user) {
        if (user == null) {
            return "";
        }
        if (!StringUtils.isEmpty(user.getRealName())) {
            return user.getRealName();
        }
        if (!StringUtils.isEmpty(user.getNickname())) {
            return user.getNickname();
        }
        return nvl(user.getAccount());
    }

    private String adminName(Integer adminId) {
        if (adminId == null || adminId <= 0) {
            return "";
        }
        Admin admin = adminMapper.selectById(adminId);
        return admin == null ? "" : nvl(admin.getName());
    }

    private String trimToMax(String value, int maxLength) {
        String text = value == null ? "" : value.trim();
        return text.length() > maxLength ? text.substring(0, maxLength) : text;
    }

    private String applyStatusText(String status) {
        if (ClassApplyGroup.STATUS_FOLLOWING.equals(status)) {
            return "客服跟进中";
        }
        if (ClassApplyGroup.STATUS_CLOSED.equals(status)) {
            return "已开班";
        }
        return "招募中";
    }

    // ---- 公共装配 ----------------------------------------------------------

    private Set<Integer> sessionIdsOfLinks(List<EnrollLink> links) {
        Set<Integer> ids = new HashSet<>();
        for (EnrollLink link : links) {
            ids.add(link.getSessionId());
        }
        return ids;
    }

    private Map<Integer, CourseLesson> mapLessonsByIds(Collection<Integer> sessionIds) {
        Map<Integer, CourseLesson> map = new HashMap<>();
        if (sessionIds == null || sessionIds.isEmpty()) {
            return map;
        }
        for (CourseLesson lesson : courseLessonMapper.selectBatchIds(sessionIds)) {
            map.put(lesson.getId(), lesson);
        }
        return map;
    }

    private Map<Integer, CourseClass> mapClasses(Iterable<CourseLesson> lessons) {
        Set<Integer> ids = new HashSet<>();
        for (CourseLesson lesson : lessons) {
            ids.add(lesson.getClassId());
        }
        Map<Integer, CourseClass> map = new HashMap<>();
        if (ids.isEmpty()) {
            return map;
        }
        for (CourseClass courseClass : courseClassMapper.selectBatchIds(ids)) {
            map.put(courseClass.getId(), courseClass);
        }
        return map;
    }

    private Map<Integer, String> mapAreaNames(Iterable<CourseClass> classes) {
        Set<Integer> ids = new HashSet<>();
        for (CourseClass courseClass : classes) {
            if (courseClass.getAreaId() != null && courseClass.getAreaId() > 0) {
                ids.add(courseClass.getAreaId());
            }
        }
        Map<Integer, String> map = new HashMap<>();
        if (ids.isEmpty()) {
            return map;
        }
        for (Area area : areaMapper.selectBatchIds(ids)) {
            map.put(area.getId(), area.getName());
        }
        return map;
    }

    private Map<Integer, Region> mapRegions(Set<Integer> streetIds) {
        Map<Integer, Region> map = new HashMap<>();
        if (streetIds == null || streetIds.isEmpty()) {
            return map;
        }
        List<Region> streets = regionMapper.selectBatchIds(streetIds);
        Set<Integer> parentIds = new HashSet<>();
        for (Region street : streets) {
            map.put(street.getId(), street);
            if (street.getParentId() != null && street.getParentId() > 0) {
                parentIds.add(street.getParentId());
            }
        }
        if (!parentIds.isEmpty()) {
            for (Region district : regionMapper.selectBatchIds(parentIds)) {
                map.put(district.getId(), district);
            }
        }
        return map;
    }

    private String linkPath(EnrollLink link) {
        return linkPath(link.getToken(), link.getSessionId());
    }

    private String linkPath(String token, Integer sessionId) {
        if (StringUtils.isEmpty(token)) {
            return "";
        }
        return "/pages/enroll/index?token=" + token + "&sessionId=" + sessionId;
    }

    private boolean isExpired(EnrollLink link, long now) {
        return link.getExpireTime() != null && link.getExpireTime() > 0 && link.getExpireTime() < now;
    }

    private boolean linkValid(EnrollLink link, long now) {
        return link.getStatus() != null && link.getStatus() == 1 && !isExpired(link, now);
    }

    private String linkStatusText(EnrollLink link, long now) {
        if (link.getStatus() == null || link.getStatus() == 0) {
            return "已停用";
        }
        if (isExpired(link, now)) {
            return "已过期";
        }
        return "有效";
    }

    private String enrollmentStatusText(Integer status) {
        if (status != null && status == 1) {
            return "已确认";
        }
        if (status != null && status == 2) {
            return "已拒绝";
        }
        return "待确认";
    }

    private String formatTime(Long timestamp) {
        if (timestamp == null || timestamp <= 0) {
            return "";
        }
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()).format(TIME_FMT);
    }

    private Long longValue(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (NumberFormatException ignored) {
            return 0L;
        }
    }

    private Integer intValue(Object value) {
        if (value == null) {
            return null;
        }
        try {
            String text = String.valueOf(value).trim();
            return text.isEmpty() ? null : Integer.parseInt(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private String text(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private String nvl(String text) {
        return StringUtils.isEmpty(text) ? "" : text;
    }
}
