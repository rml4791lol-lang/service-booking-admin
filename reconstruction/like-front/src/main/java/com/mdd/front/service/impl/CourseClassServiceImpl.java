/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.RechargeOrder
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.CourseConsumeLog
 *  com.mdd.common.entity.course.CourseLesson
 *  com.mdd.common.entity.course.CourseRefund
 *  com.mdd.common.entity.course.CourseReview
 *  com.mdd.common.entity.course.UserCourse
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.RechargeOrderMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.CourseConsumeLogMapper
 *  com.mdd.common.mapper.course.CourseLessonMapper
 *  com.mdd.common.mapper.course.CourseRefundMapper
 *  com.mdd.common.mapper.course.CourseReviewMapper
 *  com.mdd.common.mapper.course.UserCourseMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.util.ClassScheduleUtil
 *  com.mdd.common.util.ClassScheduleUtil$ScheduleRule
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.common.util.UserCourseEligibilityUtil
 *  com.mdd.front.service.ICourseClassService
 *  com.mdd.front.service.ICourseLessonLogService
 *  com.mdd.front.service.IUserCourseService
 *  com.mdd.front.service.impl.CourseClassServiceImpl
 *  com.mdd.front.validate.course.CourseClassValidate
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseConsumeLog;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseRefund;
import com.mdd.common.entity.course.CourseReview;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.user.User;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseConsumeLogMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.CourseRefundMapper;
import com.mdd.common.mapper.course.CourseReviewMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.service.CourseConsumeEffectiveService;
import com.mdd.common.util.ClassScheduleUtil;
import com.mdd.common.util.CourseRefundAmountUtil;
import com.mdd.common.util.HtmlSanitizer;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.UserCourseEligibilityUtil;
import com.mdd.front.service.ICourseClassService;
import com.mdd.front.service.ICourseLessonLogService;
import com.mdd.front.service.IUserCourseService;
import com.mdd.common.util.ParamUtils;
import com.mdd.front.validate.course.CourseClassValidate;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseClassServiceImpl
implements ICourseClassService {
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    CourseReviewMapper courseReviewMapper;
    @Resource
    ICourseLessonLogService iCourseLessonLogService;
    @Resource
    UserCourseMapper userCourseMapper;
    @Resource
    CourseConsumeLogMapper courseConsumeLogMapper;
    @Resource
    CourseRefundMapper courseRefundMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    RechargeOrderMapper rechargeOrderMapper;
    @Resource
    IUserCourseService iUserCourseService;
    @Resource
    CourseConsumeEffectiveService courseConsumeEffectiveService;

    public List<Map<String, Object>> list(CourseClassValidate courseClassValidate) {
        Integer page = courseClassValidate.getPage_no();
        Integer limit = courseClassValidate.getPage_size();
        int offset = (page - 1) * limit;
        List<CourseClass> list = this.courseClassMapper.selectList(new QueryWrapper<CourseClass>().in("status", new Object[]{1, 2}).eq("is_delete", (Object)0).eq("is_show", (Object)1).and(courseClassValidate.getKeyword() != null && !courseClassValidate.getKeyword().isEmpty(), w -> w.like("name", (Object)courseClassValidate.getKeyword()).or().like("venue", (Object)courseClassValidate.getKeyword()).or().like("instructor_name", (Object)courseClassValidate.getKeyword())).orderByDesc("id").last("limit " + offset + "," + limit));
        LinkedList<Map<String, Object>> res = new LinkedList<Map<String, Object>>();
        for (CourseClass courseClass : list) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("id", courseClass.getId());
            map.put("name", courseClass.getName());
            map.put("summary", courseClass.getSummary() == null ? "" : courseClass.getSummary());
            map.put("instructor", courseClass.getInstructorName());
            map.put("venue", courseClass.getVenue());
            map.put("time", courseClass.getTimeSlot());
            map.put("weekDays", courseClass.getWeekDays());
            map.put("scheduleMode", courseClass.getScheduleMode() != null ? courseClass.getScheduleMode() : 0);
            map.put("scheduleRules", courseClass.getScheduleRules());
            map.put("price", courseClass.getTotalFee());
            map.put("image", UrlUtils.toAbsoluteUrl((String)courseClass.getImage()));
            map.put("classType", courseClass.getClassType());
            map.put("lat", courseClass.getLatitude());
            map.put("lng", courseClass.getLongitude());
            String distance = "\u672a\u77e5";
            double distMeters = Double.MAX_VALUE;
            if (courseClassValidate.getLat() != null && courseClassValidate.getLng() != null && courseClass.getLatitude() != null && courseClass.getLongitude() != null) {
                try {
                    double dist;
                    double lat1 = Double.parseDouble(courseClassValidate.getLat());
                    double lng1 = Double.parseDouble(courseClassValidate.getLng());
                    double lat2 = Double.parseDouble(courseClass.getLatitude());
                    double lng2 = Double.parseDouble(courseClass.getLongitude());
                    distMeters = dist = this.getDistance(lat1, lng1, lat2, lng2);
                    distance = dist < 1000.0 ? Math.round(dist) + "m" : String.format("%.1fkm", dist / 1000.0);
                }
                catch (Exception e) {
                    distance = "\u8ddd\u79bb\u8ba1\u7b97\u5931\u8d25";
                }
            }
            map.put("distance", distance);
            map.put("_distMeters", distMeters);
            res.add(map);
        }
        res.sort((a, b) -> Double.compare((Double)a.getOrDefault("_distMeters", Double.MAX_VALUE), (Double)b.getOrDefault("_distMeters", Double.MAX_VALUE)));
        for (Map map : res) {
            map.remove("_distMeters");
        }
        return res;
    }

    private double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double R = 6378137.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2.0) * Math.sin(dLng / 2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return R * c;
    }

    public Map<String, Object> detail(Integer id, Integer userId) {
        CourseReview myReview;
        Instructor instructor;
        List rules;
        CourseClass item = (CourseClass)this.courseClassMapper.selectById((Serializable)id);
        // 已删除或已下架的课程不在小程序详情露出（#44）。下架只影响展示，不影响历史订单/报名查询。
        if (item == null || item.getIsDelete() == 1 || item.getIsShow() == null || item.getIsShow() != 1) {
            return null;
        }
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("id", item.getId());
        map.put("name", item.getName());
        map.put("summary", item.getSummary() == null ? "" : item.getSummary());
        map.put("introHtml", HtmlSanitizer.sanitizeCourseIntro(item.getIntroHtml()));
        map.put("introImages", this.toAbsoluteImageList(item.getIntroImages()));
        map.put("isShow", item.getIsShow());
        map.put("image", UrlUtils.toAbsoluteUrl((String)item.getImage()));
        map.put("classType", item.getClassType());
        map.put("venue", item.getVenue());
        map.put("latitude", item.getLatitude());
        map.put("longitude", item.getLongitude());
        map.put("weekDays", item.getWeekDays());
        map.put("timeSlot", item.getTimeSlot());
        map.put("scheduleMode", item.getScheduleMode() != null ? item.getScheduleMode() : 0);
        map.put("scheduleRules", item.getScheduleRules());
        map.put("totalLessons", item.getTotalLessons());
        map.put("feePerLesson", item.getFeePerLesson());
        if (item.getStartDate() != null) {
            map.put("startDate", TimeUtils.timestampToDay((Long)item.getStartDate().longValue()));
        }
        map.put("status", item.getStatus());
        int duration = 0;
        String durationSlot = item.getTimeSlot();
        if (item.getScheduleMode() != null && item.getScheduleMode() == 1 && !(rules = ClassScheduleUtil.parseRules((String)item.getScheduleRules())).isEmpty()) {
            durationSlot = ((ClassScheduleUtil.ScheduleRule)rules.get(0)).getTimeSlot();
        }
        if (durationSlot != null && durationSlot.contains("-") && ClassScheduleUtil.validateTimeSlot((String)durationSlot) == null) {
            String[] times = durationSlot.split("-");
            try {
                String[] start = times[0].trim().split(":");
                String[] end = times[1].trim().split(":");
                int startMins = Integer.parseInt(start[0]) * 60 + Integer.parseInt(start[1]);
                int endMins = Integer.parseInt(end[0]) * 60 + Integer.parseInt(end[1]);
                duration = endMins - startMins;
                if (duration < 0) {
                    duration += 1440;
                }
            }
            catch (Exception start) {
                // empty catch block
            }
        }
        map.put("duration", duration);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = sdf.format(new Date());
        Long totalLessonCount = this.courseLessonMapper.selectCount(new QueryWrapper<CourseLesson>().eq("class_id", (Object)item.getId()));
        int totalLessonsFromTable = totalLessonCount != null ? totalLessonCount.intValue() : 0;
        CourseLesson currentLesson = (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>().eq("class_id", (Object)item.getId()).le("lesson_date", (Object)todayStr).orderByDesc("lesson_no").last("limit 1"));
        int currentLessonNo = currentLesson != null ? currentLesson.getLessonNo() : 0;
        int nextLessonTime = 0;
        String nextLessonTimeSlot = "";
        CourseLesson nextLesson = (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>().eq("class_id", (Object)item.getId()).gt("lesson_date", (Object)todayStr).eq("status", (Object)0).orderByAsc("lesson_no").last("limit 1"));
        if (nextLesson != null) {
            try {
                nextLessonTime = (int)(sdf.parse(nextLesson.getLessonDate()).getTime() / 1000L);
            }
            catch (Exception exception) {
                // empty catch block
            }
            nextLessonTimeSlot = ClassScheduleUtil.resolveLessonTimeSlot((CourseLesson)nextLesson, (CourseClass)item);
        }
        CourseLesson todayLesson = (CourseLesson)this.courseLessonMapper.selectOne(new QueryWrapper<CourseLesson>().eq("class_id", (Object)item.getId()).eq("lesson_date", (Object)todayStr).last("limit 1"));
        String todayLessonTimeSlot = ClassScheduleUtil.resolveLessonTimeSlot((CourseLesson)todayLesson, (CourseClass)item);
        map.put("lessonNo", currentLessonNo);
        map.put("nextLessonTime", nextLessonTime);
        map.put("nextLessonTimeSlot", nextLessonTimeSlot);
        map.put("todayLessonTimeSlot", todayLessonTimeSlot);
        map.put("todayLessonStatus", todayLesson != null ? todayLesson.getStatus() : null);
        if (totalLessonsFromTable > 0) {
            map.put("totalLessons", totalLessonsFromTable);
        }
        map.putAll(this.iUserCourseService.courseStats(userId, item.getId()));
        BigDecimal totalFee = item.getTotalFee() != null ? item.getTotalFee() : BigDecimal.ZERO;
        BigDecimal discount = item.getDiscountPrice() != null ? item.getDiscountPrice() : BigDecimal.ZERO;
        BigDecimal originalPrice = totalFee.add(discount);
        map.put("originalPrice", originalPrice);
        map.put("payAmount", totalFee);
        map.put("discount", discount);
        LinkedHashMap<String, Object> coach = new LinkedHashMap<String, Object>();
        if (item.getInstructorId() != null && (instructor = (Instructor)this.instructorMapper.selectById((Serializable)item.getInstructorId())) != null) {
            coach.put("id", instructor.getId());
            coach.put("name", instructor.getName());
            coach.put("avatar", UrlUtils.toAbsoluteUrl((String)instructor.getAvatar()));
            coach.put("desc", instructor.getIntroduction());
            coach.put("specialty", instructor.getSpecialty());
            coach.put("experienceYears", instructor.getExperienceYears());
            coach.put("rating", instructor.getRatingScore());
        }
        if (coach.isEmpty()) {
            coach.put("name", item.getInstructorName());
            coach.put("avatar", "");
            coach.put("desc", "");
            coach.put("rating", 0);
        }
        map.put("coach", coach);
        if (userId != null && currentLessonNo > 0 && (myReview = (CourseReview)this.courseReviewMapper.selectOne(new QueryWrapper<CourseReview>().eq("user_id", (Object)userId).eq("course_class_id", (Object)id).eq("lesson_no", (Object)currentLessonNo).isNull("delete_time").last("limit 1"))) != null) {
            LinkedHashMap<String, Object> reviewMap = new LinkedHashMap<String, Object>();
            reviewMap.put("lessonNo", myReview.getLessonNo());
            reviewMap.put("ratingTeaching", myReview.getRatingTeaching());
            reviewMap.put("ratingAtmosphere", myReview.getRatingAtmosphere());
            reviewMap.put("ratingAttitude", myReview.getRatingAttitude());
            reviewMap.put("content", myReview.getContent());
            reviewMap.put("images", myReview.getImages() != null && !myReview.getImages().isEmpty() ? Arrays.stream(myReview.getImages().split(",")).map(s -> UrlUtils.toAbsoluteUrl((String)s.trim())).collect(Collectors.toList()) : new ArrayList());
            map.put("myReview", reviewMap);
        }
        if (userId != null) {
            Long pendingRefund = this.courseRefundMapper.selectCount(new QueryWrapper<CourseRefund>().eq("user_id", (Object)userId).eq("course_class_id", (Object)id).eq("status", (Object)0).isNull("delete_time"));
            map.put("refundPending", pendingRefund != null && pendingRefund > 0L);
        } else {
            map.put("refundPending", false);
        }
        return map;
    }

    /**
     * 后台维护的介绍多图（相对路径，逗号分隔）转小程序可直接渲染的绝对地址。
     */
    private List<String> toAbsoluteImageList(String introImages) {
        ArrayList<String> urls = new ArrayList<>();
        if (introImages == null || introImages.trim().isEmpty()) {
            return urls;
        }
        for (String path : introImages.split(",")) {
            String item = path.trim();
            if (!item.isEmpty()) {
                urls.add(UrlUtils.toAbsoluteUrl((String)item));
            }
        }
        return urls;
    }

    @Transactional(rollbackFor={Exception.class})
    public void consume(Integer userId, Map<String, Object> params) {
        CourseClass cls;
        UserCourse userCourse;
        if (userId == null) {
            throw new OperateException("\u7528\u6237\u672a\u767b\u5f55");
        }
        Integer courseClassId = ParamUtils.intOrDefault(params.get("courseClassId"), 0, "\u8bfe\u7a0bID");
        Integer lessonNo = ParamUtils.intOrDefault(params.get("lessonNo"), 0, "\u8bfe\u6b21");
        Integer studentId = ParamUtils.intOrDefault(params.get("studentId"), 0, "\u5b66\u5458ID");
        if (courseClassId <= 0 || lessonNo <= 0) {
            throw new OperateException("\u53c2\u6570\u9519\u8bef\uff1a\u8bfe\u7a0bID\u548c\u8bfe\u6b21\u5fc5\u987b\u4e3a\u6b63\u6574\u6570");
        }
        List<UserCourse> candidates = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).eq("course_class_id", (Object)courseClassId).ne("status", (Object)2).isNull("delete_time").orderByAsc("id"));
        ArrayList<UserCourse> consumableCourses = new ArrayList<UserCourse>();
        HashSet<Integer> candidateStudentIds = new HashSet<Integer>();
        for (UserCourse candidate : candidates) {
            int total;
            RechargeOrder linkedOrder = this.resolveActiveOrder(candidate, userId, courseClassId);
            if (linkedOrder == null) continue;
            Integer resolvedStudentId = candidate.getStudentId();
            if (resolvedStudentId == null || resolvedStudentId <= 0) {
                resolvedStudentId = linkedOrder.getStudentId();
            }
            resolvedStudentId = resolvedStudentId != null && resolvedStudentId > 0 ? resolvedStudentId : 0;
            if (studentId > 0 && !studentId.equals(resolvedStudentId)) continue;
            int remaining = candidate.getRemainLessons() != null ? candidate.getRemainLessons() : 0;
            int used = candidate.getUsedLessons() != null ? candidate.getUsedLessons() : 0;
            int n = total = candidate.getTotalLessons() != null ? candidate.getTotalLessons() : 0;
            if (remaining <= 0 && (remaining != 0 || used != 0 || total <= 0)) continue;
            candidate.setStudentId(resolvedStudentId);
            consumableCourses.add(candidate);
            candidateStudentIds.add(resolvedStudentId);
        }
        if (studentId <= 0 && !UserCourseEligibilityUtil.canResolveWithoutStudent((int)candidateStudentIds.size())) {
            throw new OperateException("\u8be5\u5bb6\u957f\u6709\u591a\u4e2a\u5b66\u5458\uff0c\u8bf7\u9009\u62e9\u5177\u4f53\u5b66\u5458\u540e\u6263\u8bfe");
        }
        UserCourse userCourse2 = userCourse = consumableCourses.isEmpty() ? null : (UserCourse)consumableCourses.get(0);
        if (userCourse == null) {
            throw new OperateException("\u672a\u627e\u5230\u8be5\u5b66\u5458\u53ef\u6263\u8bfe\u7684\u6709\u6548\u62a5\u540d\u8bb0\u5f55");
        }
        Integer remainLessons = userCourse.getRemainLessons() != null ? userCourse.getRemainLessons() : 0;
        Integer usedLessons = userCourse.getUsedLessons() != null ? userCourse.getUsedLessons() : 0;
        Integer totalLessons = userCourse.getTotalLessons() != null ? userCourse.getTotalLessons() : 0;
        if (totalLessons == 0 && (cls = (CourseClass)this.courseClassMapper.selectById((Serializable)courseClassId)) != null && cls.getTotalLessons() != null) {
            totalLessons = cls.getTotalLessons();
            userCourse.setTotalLessons(totalLessons);
        }
        if (remainLessons == 0 && usedLessons == 0 && totalLessons > 0) {
            remainLessons = totalLessons;
            userCourse.setRemainLessons(remainLessons);
        }
        if (remainLessons <= 0) {
            throw new OperateException("\u60a8\u7684\u8bfe\u65f6\u5df2\u7528\u5c3d\uff0c\u65e0\u6cd5\u6838\u9500");
        }
        Integer studentIdValue = userCourse.getStudentId() != null && userCourse.getStudentId() > 0 ? userCourse.getStudentId() : 0;
        QueryWrapper<CourseConsumeLog> consumeLogQuery = new QueryWrapper<CourseConsumeLog>().eq("user_id", (Object)userId).eq("course_class_id", (Object)courseClassId).eq("lesson_no", (Object)lessonNo).eq("student_id", (Object)studentIdValue);
        CourseConsumeLog existingLog = (CourseConsumeLog)this.courseConsumeLogMapper.selectOne((Wrapper)consumeLogQuery.last("limit 1"));
        if (existingLog != null) {
            throw new OperateException("\u672c\u8bfe\u6b21\u60a8\u5df2\u786e\u8ba4\u5b8c\u6bd5\uff0c\u8bf7\u52ff\u91cd\u590d\u64cd\u4f5c");
        }
        userCourse.setUsedLessons(Integer.valueOf(usedLessons + 1));
        userCourse.setRemainLessons(Integer.valueOf(remainLessons - 1));
        if (userCourse.getRemainLessons() <= 0) {
            userCourse.setStatus(Integer.valueOf(1));
        }
        userCourse.setUpdateTime(TimeUtils.timestamp());
        this.userCourseMapper.updateById(userCourse);
        CourseConsumeLog log = new CourseConsumeLog();
        log.setUserId(userId);
        log.setStudentId(studentIdValue);
        log.setUserCourseId(userCourse.getId());
        log.setCourseClassId(courseClassId);
        log.setLessonNo(lessonNo);
        log.setCreateTime(TimeUtils.timestamp());
        this.courseConsumeLogMapper.insert(log);
    }

    private RechargeOrder resolveActiveOrder(UserCourse userCourse, Integer userId, Integer courseClassId) {
        List activeOrders;
        if (userCourse.getOrderId() != null && userCourse.getOrderId() > 0) {
            RechargeOrder order = (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)userCourse.getOrderId());
            if (!(order != null && userId.equals(order.getUserId()) && courseClassId.equals(order.getCourseClassId()) && UserCourseEligibilityUtil.isActiveOrder((Integer)order.getPayStatus(), (Integer)order.getRefundStatus(), (Long)order.getDeleteTime()))) {
                return null;
            }
            return order;
        }
        QueryWrapper<RechargeOrder> orderQuery = new QueryWrapper<RechargeOrder>().eq("user_id", (Object)userId).eq("course_class_id", (Object)courseClassId).eq("pay_status", (Object)1).eq("refund_status", (Object)0).isNull("delete_time").orderByAsc("id");
        if (userCourse.getStudentId() != null && userCourse.getStudentId() > 0) {
            orderQuery.eq("student_id", (Object)userCourse.getStudentId());
        }
        return (activeOrders = this.rechargeOrderMapper.selectList((Wrapper)orderQuery.last("limit 2"))).size() == 1 ? (RechargeOrder)activeOrders.get(0) : null;
    }

    public void applyRefund(Integer userId, Map<String, Object> params) {
        if (userId == null) {
            throw new OperateException("\u7528\u6237\u672a\u767b\u5f55");
        }
        Integer courseClassId = ParamUtils.intOrDefault(params.get("courseClassId"), 0, "\u8bfe\u7a0bID");
        Integer orderId = ParamUtils.intOrDefault(params.get("orderId"), 0, "\u8ba2\u5355ID");
        String reason = params.get("reason") == null ? "" : String.valueOf(params.get("reason")).trim();
        String detail = params.get("detail") == null ? "" : String.valueOf(params.get("detail"));
        if (courseClassId <= 0) {
            throw new OperateException("\u53c2\u6570\u9519\u8bef");
        }
        if (reason.isEmpty()) {
            throw new OperateException("\u8bf7\u586b\u5199\u9000\u8bfe\u539f\u56e0");
        }
        // #123：la_course_refund.reason varchar(50) / detail varchar(500)，超长会以
        // MysqlDataTruncation 冒到全局处理器变成 500「系统繁忙」。与券退款同口径提前拦截。
        com.mdd.front.util.RefundTextLimits.validate(reason, detail);
        RechargeOrder order = this.resolveRefundOrder(userId, courseClassId, orderId);
        UserCourse userCourse = (UserCourse)this.userCourseMapper.selectOne(new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).eq("course_class_id", (Object)courseClassId).eq("order_id", (Object)order.getId()).ne("status", (Object)2).isNull("delete_time").last("limit 1"));
        if (userCourse == null) {
            throw new OperateException("\u60a8\u5e76\u672a\u62a5\u540d\u8be5\u8bfe\u7a0b");
        }
        int remainLessons = userCourse.getRemainLessons() != null ? userCourse.getRemainLessons() : 0;
        if (remainLessons <= 0) {
            throw new OperateException("\u60a8\u7684\u8bfe\u65f6\u5df2\u7528\u5c3d\uff0c\u65e0\u6cd5\u7533\u8bf7\u9000\u8bfe");
        }
        CourseRefund existing = (CourseRefund)this.courseRefundMapper.selectOne(new QueryWrapper<CourseRefund>().eq("user_id", (Object)userId).eq("course_class_id", (Object)courseClassId).eq("order_id", (Object)order.getId()).eq("status", (Object)0).isNull("delete_time").last("limit 1"));
        if (existing != null) {
            throw new OperateException("\u60a8\u5df2\u6709\u4e00\u6761\u5f85\u5ba1\u6279\u7684\u9000\u8bfe\u7533\u8bf7\uff0c\u8bf7\u52ff\u91cd\u590d\u63d0\u4ea4");
        }
        CourseClass courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)courseClassId);
        String className = courseClass != null ? courseClass.getName() : "";
        BigDecimal payAmount = order != null && order.getOrderAmount() != null
                ? order.getOrderAmount() : BigDecimal.ZERO;
        int totalLessons = userCourse.getTotalLessons() != null && userCourse.getTotalLessons() > 0
                ? userCourse.getTotalLessons()
                : (courseClass != null && courseClass.getTotalLessons() != null && courseClass.getTotalLessons() > 0
                        ? courseClass.getTotalLessons()
                        : (order.getLessonCount() != null ? order.getLessonCount() : 0));
        // 退课金额按「实际消课流水」重算已用课时：老核销链路可能只写流水、不回写 used_lessons，
        // 直接读 used_lessons 会让上完课的课包仍按 0 节全退（资金风险）。
        // 口径与「我的课程」effectiveUsedLessons 一致；审核通过时后台会用同一口径再算一次。
        // #222：与展示/审核同口径——流水 FIFO + 调整流水增量
        int effectiveUsedLessons = this.courseConsumeEffectiveService.countDerivedUsedLessonsForEnrollment(userCourse);
        CourseRefundAmountUtil.Result refundCalc = CourseRefundAmountUtil.resolve(
                payAmount,
                CourseRefundAmountUtil.resolveLessonFee(
                        courseClass != null ? courseClass.getFeePerLesson() : null,
                        courseClass != null ? courseClass.getTotalFee() : null,
                        totalLessons),
                totalLessons,
                effectiveUsedLessons);
        CourseRefund refund = new CourseRefund();
        refund.setUserId(userId);
        refund.setCourseClassId(courseClassId);
        refund.setOrderId(order.getId());
        refund.setClassName(className);
        User user = (User)this.userMapper.selectById((Serializable)userId);
        refund.setUserName(user != null && user.getNickname() != null ? user.getNickname() : "");
        refund.setReason(reason);
        refund.setDetail(detail);
        refund.setPayAmount(payAmount);
        refund.setRefundAmount(refundCalc.getRefundAmount());
        refund.setTotalLessons(Integer.valueOf(refundCalc.getTotalLessons()));
        refund.setUsedLessons(Integer.valueOf(refundCalc.getUsedLessons()));
        refund.setStatus(Integer.valueOf(0));
        refund.setCreateTime(TimeUtils.timestamp());
        refund.setUpdateTime(TimeUtils.timestamp());
        try {
            this.courseRefundMapper.insert(refund);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            // #152：pending 校验是 select-then-insert，并发双申请由 V023 uk_pending_order
            // （同单至多一笔待审批）在库层拦下；这里转业务错误，不暴露 SQL 细节。
            throw new OperateException("\u60a8\u5df2\u6709\u4e00\u6761\u5f85\u5ba1\u6279\u7684\u9000\u8bfe\u7533\u8bf7\uff0c\u8bf7\u52ff\u91cd\u590d\u63d0\u4ea4");
        }
    }

    private RechargeOrder resolveRefundOrder(Integer userId, Integer courseClassId, Integer requestedOrderId) {
        if (requestedOrderId != null && requestedOrderId > 0) {
            RechargeOrder order = (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)requestedOrderId);
            if (!(order != null && userId.equals(order.getUserId()) && courseClassId.equals(order.getCourseClassId()) && UserCourseEligibilityUtil.isActiveOrder((Integer)order.getPayStatus(), (Integer)order.getRefundStatus(), (Long)order.getDeleteTime()))) {
                throw new OperateException("\u6240\u9009\u8bfe\u7a0b\u8ba2\u5355\u4e0d\u5b58\u5728\u6216\u4e0d\u53ef\u9000\u8bfe");
            }
            return order;
        }
        List<UserCourse> enrollments = this.userCourseMapper.selectList(new QueryWrapper<UserCourse>().eq("user_id", (Object)userId).eq("course_class_id", (Object)courseClassId).ne("status", (Object)2).gt("remain_lessons", (Object)0).isNull("delete_time").orderByAsc("id"));
        LinkedHashMap<Integer, RechargeOrder> activeOrders = new LinkedHashMap<Integer, RechargeOrder>();
        for (UserCourse enrollment : enrollments) {
            RechargeOrder order;
            if (enrollment.getOrderId() == null || enrollment.getOrderId() <= 0 || (order = (RechargeOrder)this.rechargeOrderMapper.selectById((Serializable)enrollment.getOrderId())) == null || !UserCourseEligibilityUtil.isActiveOrder((Integer)order.getPayStatus(), (Integer)order.getRefundStatus(), (Long)order.getDeleteTime())) continue;
            activeOrders.putIfAbsent(order.getId(), order);
        }
        if (activeOrders.size() != 1) {
            throw new OperateException("\u8be5\u8bfe\u7a0b\u5b58\u5728\u591a\u4e2a\u62a5\u540d\u5b66\u5458\uff0c\u8bf7\u4ece\u8ba2\u5355\u8bb0\u5f55\u9009\u62e9\u9700\u8981\u9000\u8bfe\u7684\u5b66\u5458");
        }
        return (RechargeOrder)activeOrders.values().iterator().next();
    }
}
