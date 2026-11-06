/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.CourseLesson
 *  com.mdd.common.entity.course.CourseLessonLog
 *  com.mdd.common.entity.course.CourseReview
 *  com.mdd.common.entity.instructor.InstructorAccountLog
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.CourseLessonLogMapper
 *  com.mdd.common.mapper.course.CourseLessonMapper
 *  com.mdd.common.mapper.course.CourseReviewMapper
 *  com.mdd.common.mapper.instructor.InstructorAccountLogMapper
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.front.service.ICourseLessonLogService
 *  com.mdd.front.service.impl.CourseLessonLogServiceImpl
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseLessonLog;
import com.mdd.common.entity.course.CourseReview;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.instructor.InstructorAccountLog;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseLessonLogMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.CourseReviewMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.instructor.InstructorAccountLogMapper;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.front.service.ICourseLessonLogService;
import com.mdd.common.util.ParamUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseLessonLogServiceImpl
implements ICourseLessonLogService {
    @Resource
    CourseLessonLogMapper courseLessonLogMapper;
    @Resource
    CourseClassMapper courseClassMapper;
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    CourseReviewMapper courseReviewMapper;
    @Resource
    CourseLessonMapper courseLessonMapper;
    @Resource
    InstructorAccountLogMapper instructorAccountLogMapper;
    @Resource
    UserCourseMapper userCourseMapper;

    @Transactional(rollbackFor={Exception.class})
    public void submitSummary(Map<String, Object> params, Integer userId) {
        Instructor inst;
        Integer lessonNo;
        Integer courseClassId = ParamUtils.requiredInt(params.get("courseClassId"), "\u8bfe\u7a0bID");
        String summary = params.get("summary") == null ? "" : String.valueOf(params.get("summary"));
        String images = params.get("images") == null ? "" : String.valueOf(params.get("images"));
        // 锁住班级行，序列化同一班级的总结提交，避免并发绕过“已总结”检查重复结算。
        CourseClass courseClass = (CourseClass)this.courseClassMapper.selectOne(
                new QueryWrapper<CourseClass>().eq("id", (Object)courseClassId).last("limit 1 FOR UPDATE"));
        if (courseClass == null) {
            throw new OperateException("\u8be5\u8bfe\u7a0b\u4e0d\u5b58\u5728");
        }
        Instructor instructor = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>().eq("user_id", (Object)userId).last("limit 1"));
        Integer n = lessonNo = ParamUtils.optionalInt(params.get("lessonNo"), "\u8bfe\u6b21");
        if (lessonNo == null) {
            throw new OperateException("\u7531\u4e8e\u6309\u56fa\u5b9a\u6392\u8bfe\u89c4\u5219\u6267\u884c\uff0c\u7f3a\u5c11\u5fc5\u586b\u7684\u8bfe\u6b21\u53f7(lessonNo)");
        }
        CourseLesson targetLesson = (CourseLesson)this.courseLessonMapper.selectOne(
                new QueryWrapper<CourseLesson>().eq("class_id", (Object)courseClassId)
                        .eq("lesson_no", (Object)lessonNo).last("limit 1"));
        if (targetLesson == null) {
            throw new OperateException("\u8be5\u73ed\u7ea7\u4e0d\u5b58\u5728\u5bf9\u5e94\u8bfe\u6b21");
        }
        if (targetLesson.getStatus() != null && targetLesson.getStatus() == 2) {
            throw new OperateException("\u8be5\u8bfe\u6b21\u5df2\u53d6\u6d88\uff0c\u4e0d\u80fd\u63d0\u4ea4\u8bfe\u540e\u603b\u7ed3");
        }
        boolean classCoach = instructor != null && instructor.getId().equals(courseClass.getInstructorId());
        boolean lessonCoach = instructor != null && targetLesson.getCoachId() != null
                && instructor.getId().equals(targetLesson.getCoachId());
        if (!classCoach && !lessonCoach) {
            throw new OperateException("\u4ec5\u8be5\u73ed\u6388\u8bfe\u6559\u7ec3\u53ef\u63d0\u4ea4\u8bfe\u540e\u603b\u7ed3");
        }
        Integer insId = instructor.getId();
        CourseLessonLog existLog = (CourseLessonLog)this.courseLessonLogMapper.selectOne(new QueryWrapper<CourseLessonLog>().eq("course_class_id", (Object)courseClassId).eq("lesson_no", (Object)lessonNo).last("limit 1"));
        if (existLog != null) {
            throw new OperateException("\u7b2c " + lessonNo + " \u8bfe\u65f6\u5df2\u88ab\u603b\u7ed3\u8fc7");
        }
        if (summary.length() > 500) {
            throw new OperateException("课后总结不能超过500字");
        }
        validateSummaryImages(images);
        CourseLessonLog log = new CourseLessonLog();
        log.setCourseClassId(courseClassId);
        log.setInstructorId(insId);
        log.setLessonNo(lessonNo);
        log.setSummary(summary);
        log.setImages(images);
        log.setCreateTime(Integer.valueOf(TimeUtils.timestamp().intValue()));
        log.setUpdateTime(Integer.valueOf(TimeUtils.timestamp().intValue()));
        this.courseLessonLogMapper.insert(log);
        BigDecimal feePerLesson = courseClass.getFeePerLesson();
        if (feePerLesson != null && feePerLesson.compareTo(BigDecimal.ZERO) > 0 && (inst = (Instructor)this.instructorMapper.selectById((Serializable)insId)) != null) {
            int updated = this.instructorMapper.addBalance(insId, feePerLesson);
            if (updated <= 0) {
                throw new OperateException("教练结算失败");
            }
            Instructor settled = (Instructor)this.instructorMapper.selectById((Serializable)insId);
            BigDecimal afterBalance = settled != null && settled.getBalance() != null
                    ? settled.getBalance() : feePerLesson;
            BigDecimal beforeBalance = afterBalance.subtract(feePerLesson);
            InstructorAccountLog accountLog = new InstructorAccountLog();
            accountLog.setInstructorId(insId);
            accountLog.setType(Integer.valueOf(1));
            accountLog.setAmount(feePerLesson);
            accountLog.setBeforeBalance(beforeBalance);
            accountLog.setAfterBalance(afterBalance);
            accountLog.setRelationId(log.getId());
            accountLog.setRemark("\u8bfe\u65f6\u6536\u5165: " + courseClass.getName() + " \u7b2c" + lessonNo + "\u8282");
            accountLog.setCreateTime(TimeUtils.timestamp());
            this.instructorAccountLogMapper.insert(accountLog);
        }
        if (targetLesson.getStatus() == null || targetLesson.getStatus() != 1) {
            targetLesson.setStatus(Integer.valueOf(1));
            targetLesson.setUpdateTime(Integer.valueOf(TimeUtils.timestamp().intValue()));
            this.courseLessonMapper.updateById(targetLesson);
        }
    }

    public List<Map<String, Object>> listSummary(Integer courseClassId, Integer userId) {
        CourseClass courseClass = (CourseClass)this.courseClassMapper.selectById((Serializable)courseClassId);
        if (courseClass == null) {
            return new ArrayList<Map<String, Object>>();
        }
        requireClassReadAccess(courseClass, userId);
        List<CourseLesson> lessons = this.courseLessonMapper.selectList(new QueryWrapper<CourseLesson>().eq("class_id", (Object)courseClassId).orderByAsc("lesson_no"));
        List<CourseLessonLog> logs = this.courseLessonLogMapper.selectList(new QueryWrapper<CourseLessonLog>().eq("course_class_id", (Object)courseClassId).isNull("delete_time"));
        Map<Integer, CourseLessonLog> logMap = logs.stream().collect(Collectors.toMap(CourseLessonLog::getLessonNo, v -> v, (v1, v2) -> v1));
        Map<Object, Object> reviewMap = new HashMap();
        if (userId != null) {
            List<CourseReview> reviews = this.courseReviewMapper.selectList(new QueryWrapper<CourseReview>().eq("course_class_id", (Object)courseClassId).eq("user_id", (Object)userId).isNull("delete_time"));
            reviewMap = reviews.stream().collect(Collectors.toMap(CourseReview::getLessonNo, v -> v, (v1, v2) -> v1));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = sdf.format(new Date());
        ArrayList<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
        for (CourseLesson lesson : lessons) {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("lessonNo", lesson.getLessonNo());
            item.put("date", lesson.getLessonDate());
            item.put("timeSlot", lesson.getTimeSlot());
            item.put("weekDay", lesson.getWeekDay());
            if (lesson.getStatus() != null && lesson.getStatus() == 2) {
                item.put("status", 2);
            } else {
                boolean isPast = false;
                if (lesson.getLessonDate() != null) {
                    if (lesson.getLessonDate().compareTo(todayStr) < 0) {
                        isPast = true;
                    } else if (lesson.getLessonDate().equals(todayStr) && lesson.getTimeSlot() != null && lesson.getTimeSlot().contains("-")) {
                        try {
                            String endTimeStr = lesson.getTimeSlot().split("-")[1].trim();
                            LocalTime endTime = LocalTime.parse(endTimeStr);
                            if (LocalTime.now().isAfter(endTime)) {
                                isPast = true;
                            }
                        }
                        catch (Exception endTimeStr) {
                            // empty catch block
                        }
                    }
                }
                item.put("status", lesson.getStatus() == 1 || isPast ? 1 : 0);
            }
            int lessonNo = lesson.getLessonNo();
            CourseLessonLog log = logMap.get(lessonNo);
            if (log != null) {
                item.put("hasLog", true);
                item.put("summary", log.getSummary());
                item.put("images", log.getImages() != null && !log.getImages().isEmpty() ? Arrays.stream(log.getImages().split(",")).map(s -> UrlUtils.toAbsoluteUrl((String)s.trim())).collect(Collectors.toList()) : new ArrayList());
                item.put("logCreateTime", TimeUtils.timestampToDate((Long)log.getCreateTime().longValue()));
                Instructor ins = (Instructor)this.instructorMapper.selectById((Serializable)log.getInstructorId());
                if (ins != null) {
                    item.put("instructorName", ins.getName());
                    item.put("instructorAvatar", UrlUtils.toAbsoluteUrl((String)ins.getAvatar()));
                }
            } else {
                item.put("hasLog", false);
            }
            CourseReview review = (CourseReview)reviewMap.get(lessonNo);
            item.put("hasReview", review != null);
            if (review != null) {
                item.put("reviewScore", review.getRatingTeaching());
            }
            res.add(item);
        }
        return res;
    }

    public Map<String, Object> listSummaryPaged(Integer courseClassId, Integer userId, Integer pageNo, Integer pageSize) {
        List<Map<String, Object>> allList = this.listSummary(courseClassId, userId);
        int total = allList.size();
        int fromIndex = (pageNo - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        List pageList = fromIndex >= total ? new ArrayList() : allList.subList(fromIndex, toIndex);
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("list", pageList);
        result.put("total", total);
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);
        result.put("totalPages", (int)Math.ceil((double)total / (double)pageSize.intValue()));
        return result;
    }

    public int calcLessonNo(Integer courseClassId) {
        Long count = this.courseLessonLogMapper.selectCount(new QueryWrapper<CourseLessonLog>().eq("course_class_id", (Object)courseClassId).isNull("delete_time"));
        return count != null ? count.intValue() : 0;
    }

    private void requireClassReadAccess(CourseClass courseClass, Integer userId) {
        if (userId == null) {
            throw new OperateException("请先登录");
        }
        Instructor instructor = this.instructorMapper.selectOne(
                new QueryWrapper<Instructor>().eq("user_id", (Object)userId).last("limit 1"));
        boolean coach = instructor != null && instructor.getId().equals(courseClass.getInstructorId());
        if (!coach && instructor != null) {
            Long assignedLessons = this.courseLessonMapper.selectCount(new QueryWrapper<CourseLesson>()
                    .eq("class_id", (Object)courseClass.getId())
                    .eq("coach_id", (Object)instructor.getId()));
            coach = assignedLessons != null && assignedLessons > 0;
        }
        if (coach) {
            return;
        }
        Long enrolled = this.userCourseMapper.selectCount(new QueryWrapper<UserCourse>()
                .eq("user_id", (Object)userId)
                .eq("course_class_id", (Object)courseClass.getId())
                .ne("status", (Object)2)
                .isNull("delete_time")
                .notInSql("order_id", "select id from la_recharge_order where refund_status = 1"));
        if (enrolled == null || enrolled <= 0) {
            throw new OperateException("\u65e0\u6743\u67e5\u770b\u8be5\u73ed\u7ea7\u8bfe\u540e\u603b\u7ed3");
        }
    }

    private void validateSummaryImages(String images) {
        if (images == null || images.trim().isEmpty()) {
            return;
        }
        String[] parts = images.split(",");
        if (parts.length > 9) {
            throw new OperateException("课堂照片最多9张");
        }
        for (String part : parts) {
            String path = part == null ? "" : part.trim();
            if (path.isEmpty() || path.length() > 500 || path.contains("..")
                    || path.matches("(?i)^(https?|javascript|data):")) {
                throw new OperateException("课堂照片地址不合法");
            }
            if (!(path.startsWith("image/") || path.startsWith("/api/uploads/")
                    || path.startsWith("uploads/"))) {
                throw new OperateException("课堂照片必须来自本站上传");
            }
        }
    }
}
