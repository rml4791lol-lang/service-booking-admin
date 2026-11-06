/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.CourseLesson
 *  com.mdd.common.entity.course.CourseLessonLog
 *  com.mdd.common.mapper.InstructorLeaveMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.CourseLessonLogMapper
 *  com.mdd.common.mapper.course.CourseLessonMapper
 *  com.mdd.front.service.IInstructorService
 *  com.mdd.front.task.LessonAutoCompleteTask
 *  javax.annotation.Resource
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.scheduling.annotation.Scheduled
 */
package com.mdd.front.task;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.CourseLessonLog;
import com.mdd.common.mapper.InstructorLeaveMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseLessonLogMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.front.service.IInstructorService;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import com.mdd.common.entity.InstructorLeave;

public class LessonAutoCompleteTask {
    private static final Logger log = LoggerFactory.getLogger(LessonAutoCompleteTask.class);
    @Resource
    private CourseLessonMapper courseLessonMapper;
    @Resource
    private CourseClassMapper courseClassMapper;
    @Resource
    private CourseLessonLogMapper courseLessonLogMapper;
    @Resource
    private IInstructorService instructorService;
    @Resource
    private InstructorLeaveMapper instructorLeaveMapper;

    @Scheduled(cron="0 */5 * * * ?")
    public void autoCompleteLessons() {
        log.info("\u6267\u884c\u5b9a\u65f6\u4efb\u52a1\uff1a\u626b\u63cf\u7ed3\u675f\u8d8530\u5206\u949f\u4e14\u672a\u7ed3\u8bfe\u7684\u8bfe\u8282...");
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String yesterday = LocalDate.now().minusDays(1L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        QueryWrapper<CourseLesson> lessonQw = new QueryWrapper<CourseLesson>();
        lessonQw.in("lesson_date", new Object[]{yesterday, today}).eq("status", (Object)0);
        List<CourseLesson> lessons = this.courseLessonMapper.selectList((Wrapper)lessonQw);
        if (lessons == null || lessons.isEmpty()) {
            return;
        }
        LocalTime now = LocalTime.now();
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        for (CourseLesson lesson : lessons) {
            String timeSlot = lesson.getTimeSlot();
            if (timeSlot == null || !timeSlot.contains("-")) continue;
            try {
                int diffMinutes;
                String endTimeStr = timeSlot.split("-")[1].trim();
                LocalTime endTime = LocalTime.parse(endTimeStr);
                boolean isOverdue = false;
                if (lesson.getLessonDate().equals(yesterday)) {
                    isOverdue = true;
                } else if (lesson.getLessonDate().equals(currentDate) && (diffMinutes = (now.getHour() - endTime.getHour()) * 60 + (now.getMinute() - endTime.getMinute())) >= 30) {
                    isOverdue = true;
                }
                if (!isOverdue) continue;
                this.processAutoComplete(lesson);
            }
            catch (DateTimeParseException e) {
                log.error("\u89e3\u6790\u8bfe\u8282\u4e0b\u8bfe\u65f6\u95f4\u5931\u8d25: lessonId={}, timeSlot={}", (Object)lesson.getId(), (Object)timeSlot);
            }
            catch (Exception e) {
                log.error("\u81ea\u52a8\u7ed3\u8bfe\u5f02\u5e38: lessonId={}, error={}", (Object)lesson.getId(), (Object)e.getMessage());
            }
        }
    }

    private void processAutoComplete(CourseLesson lesson) {
        CourseClass cls = (CourseClass)this.courseClassMapper.selectById((Serializable)lesson.getClassId());
        if (cls == null || cls.getInstructorId() == null) {
            return;
        }
        if (cls.getStatus() == null || cls.getStatus() != 1) {
            return;
        }
        QueryWrapper<InstructorLeave> leaveQw = new QueryWrapper<InstructorLeave>();
        long now = System.currentTimeMillis() / 1000L;
        leaveQw.eq("instructor_id", (Object)cls.getInstructorId()).eq("status", (Object)1).isNull("delete_time").le("start_time", (Object)now).ge("end_time", (Object)now).last("limit 1");
        if (this.instructorLeaveMapper.selectOne((Wrapper)leaveQw) != null) {
            log.info("\u6559\u7ec3\u5904\u4e8e\u8bf7\u5047\u72b6\u6001\uff0c\u8df3\u8fc7\u81ea\u52a8\u7ed3\u8bfe: classId={}, lessonNo={}, instructorId={}", new Object[]{lesson.getClassId(), lesson.getLessonNo(), cls.getInstructorId()});
            return;
        }
        if (lesson.getLessonNo() == null || lesson.getLessonNo() <= 0) {
            log.warn("\u8bfe\u8282 lessonNo \u65e0\u6548\uff0c\u8df3\u8fc7\u81ea\u52a8\u7ed3\u8bfe: lessonId={}, classId={}, lessonNo={}", new Object[]{lesson.getId(), lesson.getClassId(), lesson.getLessonNo()});
            return;
        }
        CourseLessonLog existLog = (CourseLessonLog)this.courseLessonLogMapper.selectOne(new QueryWrapper<CourseLessonLog>().eq("course_class_id", (Object)lesson.getClassId()).eq("lesson_no", (Object)lesson.getLessonNo()).isNull("delete_time").last("limit 1"));
        if (existLog == null) {
            log.info("\u89e6\u53d1\u81ea\u52a8\u7ed3\u8bfe: classId={}, lessonNo={}", (Object)lesson.getClassId(), (Object)lesson.getLessonNo());
            this.instructorService.completeLesson(cls.getInstructorId(), cls.getId(), lesson.getLessonDate(), lesson.getId(), lesson.getLessonNo(), "\u7cfb\u7edf\u9ed8\u8ba4\u7ed3\u8bfe\uff0c\u6559\u7ec3\u672a\u6309\u65f6\u586b\u5199\u603b\u7ed3\u3002", "");
        }
    }
}

