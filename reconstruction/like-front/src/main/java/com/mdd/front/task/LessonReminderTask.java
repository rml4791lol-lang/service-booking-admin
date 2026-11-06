/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.common.entity.Message
 *  com.mdd.common.entity.course.CourseLesson
 *  com.mdd.common.entity.course.UserCourse
 *  com.mdd.common.mapper.MessageMapper
 *  com.mdd.common.mapper.course.CourseLessonMapper
 *  com.mdd.common.mapper.course.UserCourseMapper
 *  com.mdd.front.task.LessonReminderTask
 *  javax.annotation.Resource
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.scheduling.annotation.Scheduled
 */
package com.mdd.front.task;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.Message;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.mapper.MessageMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class LessonReminderTask {
    private static final Logger log = LoggerFactory.getLogger(LessonReminderTask.class);
    @Resource
    private CourseLessonMapper courseLessonMapper;
    @Resource
    private UserCourseMapper userCourseMapper;
    @Resource
    private MessageMapper messageMapper;

    @Scheduled(cron="0 */5 * * * ?")
    public void remindUpcomingLessons() {
        log.info("\u6267\u884c\u5b9a\u65f6\u4efb\u52a1\uff1a\u626b\u63cf\u5373\u5c06\u4e0a\u8bfe\u7684\u8bfe\u8282...");
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        QueryWrapper<CourseLesson> lessonQw = new QueryWrapper<CourseLesson>();
        lessonQw.eq("lesson_date", (Object)today).eq("status", (Object)0).isNull("delete_time");
        List<CourseLesson> lessons = this.courseLessonMapper.selectList((Wrapper)lessonQw);
        if (lessons == null || lessons.isEmpty()) {
            return;
        }
        LocalTime now = LocalTime.now();
        for (CourseLesson lesson : lessons) {
            String timeSlot = lesson.getTimeSlot();
            if (timeSlot == null || !timeSlot.contains("-")) continue;
            try {
                String startTimeStr = timeSlot.split("-")[0].trim();
                LocalTime startTime = LocalTime.parse(startTimeStr);
                int diffMinutes = (startTime.getHour() - now.getHour()) * 60 + (startTime.getMinute() - now.getMinute());
                if (diffMinutes <= 0 || diffMinutes > 30) continue;
                this.sendReminderForLesson(lesson, startTimeStr);
            }
            catch (DateTimeParseException e) {
                log.error("\u89e3\u6790\u8bfe\u8282\u65f6\u95f4\u5931\u8d25: lessonId={}, timeSlot={}", (Object)lesson.getId(), (Object)timeSlot);
            }
        }
    }

    private void sendReminderForLesson(CourseLesson lesson, String startTimeStr) {
        QueryWrapper<UserCourse> ucQw = new QueryWrapper<UserCourse>();
        ucQw.eq("course_class_id", (Object)lesson.getClassId()).isNull("delete_time");
        List<UserCourse> userCourses = this.userCourseMapper.selectList((Wrapper)ucQw);
        for (UserCourse uc : userCourses) {
            String extraInfo = "{\"lessonId\":" + lesson.getId() + "}";
            QueryWrapper<Message> msgQw = new QueryWrapper<Message>();
            msgQw.eq("type", (Object)1).eq("receiver_id", (Object)uc.getUserId()).eq("receiver_type", (Object)"student").like("extra", (Object)("\"lessonId\":" + lesson.getId()));
            Long count = this.messageMapper.selectCount((Wrapper)msgQw);
            if (count != null && count > 0L) continue;
            String title = "\u4e0a\u8bfe\u63d0\u9192";
            String content = "\u60a8\u597d\uff01\u60a8\u7684\u8bfe\u7a0b\u5373\u5c06\u4e8e\u4eca\u5929 " + startTimeStr + " \u5f00\u59cb\uff0c\u8bf7\u63d0\u524d\u505a\u597d\u4e0a\u8bfe\u51c6\u5907\u3002";
            Message msg = new Message();
            msg.setType(Integer.valueOf(1));
            msg.setTitle(title);
            msg.setContent(content);
            msg.setSenderId(Integer.valueOf(0));
            msg.setSenderType("system");
            msg.setSenderName("\u7cfb\u7edf");
            msg.setReceiverId(uc.getUserId());
            msg.setReceiverType("student");
            msg.setIsRead(Integer.valueOf(0));
            msg.setExtra(extraInfo);
            msg.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            msg.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.messageMapper.insert(msg);
            log.info("\u5df2\u53d1\u9001\u4e0a\u8bfe\u63d0\u9192: lessonId={}, userId={}", (Object)lesson.getId(), (Object)uc.getUserId());
        }
    }
}

