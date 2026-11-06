/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.binarywang.wx.miniapp.api.WxMaService
 *  cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage
 *  cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage$MsgData
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.common.entity.Message
 *  com.mdd.common.entity.course.CourseBooking
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.CourseLesson
 *  com.mdd.common.entity.course.UserCourse
 *  com.mdd.common.entity.student.Student
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.entity.user.UserAuth
 *  com.mdd.common.mapper.MessageMapper
 *  com.mdd.common.mapper.course.CourseBookingMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.CourseConsumeLogMapper
 *  com.mdd.common.mapper.course.CourseLessonMapper
 *  com.mdd.common.mapper.course.UserCourseMapper
 *  com.mdd.common.mapper.student.StudentMapper
 *  com.mdd.common.mapper.user.UserAuthMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.plugin.wechat.WxMnpDriver
 *  com.mdd.common.util.ClassScheduleUtil
 *  com.mdd.common.util.UserCourseBalanceUtil
 *  com.mdd.front.task.LessonCancelTask
 *  javax.annotation.Resource
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.scheduling.annotation.Scheduled
 *  org.springframework.stereotype.Component
 */
package com.mdd.front.task;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.Message;
import com.mdd.common.entity.course.CourseBooking;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.course.UserCourse;
import com.mdd.common.entity.student.Student;
import com.mdd.common.entity.user.User;
import com.mdd.common.entity.user.UserAuth;
import com.mdd.common.mapper.MessageMapper;
import com.mdd.common.mapper.course.CourseBookingMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseConsumeLogMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.course.UserCourseMapper;
import com.mdd.common.mapper.student.StudentMapper;
import com.mdd.common.mapper.user.UserAuthMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.plugin.wechat.WxMnpDriver;
import com.mdd.common.util.ClassScheduleUtil;
import com.mdd.common.util.UserCourseBalanceUtil;
import com.mdd.front.config.SubscribeMessageConfig;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.mdd.common.entity.course.CourseConsumeLog;

@Component
public class LessonCancelTask {
    private static final Logger log = LoggerFactory.getLogger(LessonCancelTask.class);
    @Resource
    private SubscribeMessageConfig subscribeMessageConfig;
    @Resource
    private CourseLessonMapper courseLessonMapper;
    @Resource
    private CourseClassMapper courseClassMapper;
    @Resource
    private CourseBookingMapper courseBookingMapper;
    @Resource
    private UserCourseMapper userCourseMapper;
    @Resource
    private CourseConsumeLogMapper courseConsumeLogMapper;
    @Resource
    private UserAuthMapper userAuthMapper;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private StudentMapper studentMapper;

    @Scheduled(cron="0 */10 * * * ?")
    public void checkMinStudents() {
        log.info("\u3010\u6700\u4f4e\u5f00\u8bfe\u68c0\u67e5\u3011\u5f00\u59cb\u626b\u63cf\u4eca\u65e5\u8bfe\u8282...");
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalTime now = LocalTime.now();
        List<CourseLesson> todayLessons = this.courseLessonMapper.selectList(new QueryWrapper<CourseLesson>().eq("lesson_date", (Object)today).eq("status", (Object)0).and(w -> w.isNull("lesson_type").or().eq("lesson_type", (Object)0)));
        if (todayLessons == null || todayLessons.isEmpty()) {
            return;
        }
        for (CourseLesson lesson : todayLessons) {
            try {
                this.processLesson(lesson, today, now);
            }
            catch (Exception e) {
                log.error("\u3010\u6700\u4f4e\u5f00\u8bfe\u68c0\u67e5\u3011\u5904\u7406\u8bfe\u8282\u5931\u8d25: lessonId={}, error={}", new Object[]{lesson.getId(), e.getMessage(), e});
            }
        }
    }

    private void processLesson(CourseLesson lesson, String today, LocalTime now) {
        int minStudents;
        CourseClass cls = (CourseClass)this.courseClassMapper.selectById((Serializable)lesson.getClassId());
        String timeSlot = ClassScheduleUtil.resolveLessonTimeSlot((CourseLesson)lesson, (CourseClass)cls);
        if (timeSlot.isEmpty() || !timeSlot.contains("-")) {
            return;
        }
        String startTimeStr = timeSlot.split("-")[0].trim();
        LocalTime startTime = LocalTime.parse(startTimeStr);
        int diffMinutes = (startTime.getHour() - now.getHour()) * 60 + (startTime.getMinute() - now.getMinute());
        if (diffMinutes < 0 || diffMinutes > 60) {
            return;
        }
        if (cls == null) {
            return;
        }
        int n = minStudents = cls.getMaxStudents() != null ? cls.getMaxStudents() : 0;
        if (minStudents <= 0) {
            return;
        }
        Long bookingCount = this.courseBookingMapper.selectCount(new QueryWrapper<CourseBooking>().eq("course_class_id", (Object)lesson.getClassId()).eq("lesson_date", (Object)today).eq("status", (Object)1));
        long actualCount = bookingCount != null ? bookingCount : 0L;
        log.info("\u3010\u6700\u4f4e\u5f00\u8bfe\u68c0\u67e5\u3011\u73ed\u7ea7={}, \u8bfe\u8282={}, \u9884\u7ea6\u4eba\u6570={}, \u6700\u4f4e\u8981\u6c42={}", new Object[]{cls.getName(), lesson.getLessonNo(), actualCount, minStudents});
        if (actualCount >= (long)minStudents) {
            return;
        }
        log.info("\u3010\u6700\u4f4e\u5f00\u8bfe\u68c0\u67e5\u3011\u4eba\u6570\u4e0d\u8db3\uff0c\u53d6\u6d88\u8bfe\u8282: classId={}, lessonId={}", (Object)lesson.getClassId(), (Object)lesson.getId());
        lesson.setStatus(Integer.valueOf(2));
        lesson.setUpdateTime(Integer.valueOf((int)(System.currentTimeMillis() / 1000L)));
        this.courseLessonMapper.updateById(lesson);
        List<CourseBooking> bookings = this.courseBookingMapper.selectList(new QueryWrapper<CourseBooking>().eq("course_class_id", (Object)lesson.getClassId()).eq("lesson_date", (Object)today).eq("status", (Object)1));
        String venue = cls.getVenue() != null ? cls.getVenue() : "";
        String coachName = cls.getInstructorName() != null ? cls.getInstructorName() : "";
        for (CourseBooking booking : bookings) {
            User user;
            Student stu;
            booking.setStatus(Integer.valueOf(3));
            booking.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.courseBookingMapper.updateById(booking);
            QueryWrapper<CourseConsumeLog> consumeLogQuery = new QueryWrapper<CourseConsumeLog>().eq("user_id", (Object)booking.getUserId()).eq("course_class_id", (Object)booking.getCourseClassId()).eq("lesson_no", (Object)lesson.getLessonNo());
            if (booking.getStudentId() != null && booking.getStudentId() > 0) {
                consumeLogQuery.eq("student_id", (Object)booking.getStudentId());
            } else {
                consumeLogQuery.eq("student_id", (Object)0);
            }
            int deleted = this.courseConsumeLogMapper.delete((Wrapper)consumeLogQuery);
            if (deleted > 0) {
                UserCourse userCourse;
                QueryWrapper<UserCourse> userCourseQuery = new QueryWrapper<UserCourse>().eq("user_id", (Object)booking.getUserId()).eq("course_class_id", (Object)booking.getCourseClassId()).isNull("delete_time");
                if (booking.getStudentId() != null && booking.getStudentId() > 0) {
                    userCourseQuery.eq("student_id", (Object)booking.getStudentId());
                }
                if ((userCourse = (UserCourse)this.userCourseMapper.selectOne((Wrapper)userCourseQuery.last("limit 1"))) != null) {
                    int remain = userCourse.getRemainLessons() != null ? userCourse.getRemainLessons() : 0;
                    int used = userCourse.getUsedLessons() != null ? userCourse.getUsedLessons() : 0;
                    userCourse.setRemainLessons(Integer.valueOf(remain + 1));
                    userCourse.setUsedLessons(Integer.valueOf(Math.max(used - 1, 0)));
                    userCourse.setStatus(Integer.valueOf(UserCourseBalanceUtil.statusAfterLessonReturned((Integer)userCourse.getStatus(), (Integer)userCourse.getRemainLessons())));
                    userCourse.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
                    this.userCourseMapper.updateById(userCourse);
                    log.info("\u3010\u6700\u4f4e\u5f00\u8bfe\u68c0\u67e5\u3011\u5df2\u9000\u8fd8\u8bfe\u65f6: userId={}, remain={}->{}", new Object[]{booking.getUserId(), remain, remain + 1});
                }
            }
            String studentName = "";
            if (booking.getStudentId() != null && booking.getStudentId() > 0 && (stu = (Student)this.studentMapper.selectById((Serializable)booking.getStudentId())) != null && stu.getName() != null) {
                studentName = stu.getName();
            }
            if (studentName.isEmpty() && (user = (User)this.userMapper.selectById((Serializable)booking.getUserId())) != null) {
                studentName = user.getNickname() != null ? user.getNickname() : "";
            }
            this.sendCancelNotification(booking.getUserId(), studentName, cls.getName(), venue, coachName);
            Message msg = new Message();
            msg.setType(Integer.valueOf(3));
            msg.setTitle("\u8bfe\u7a0b\u53d6\u6d88\u901a\u77e5");
            msg.setContent("\u60a8\u9884\u7ea6\u7684\u300c" + cls.getName() + "\u300d\u7b2c" + lesson.getLessonNo() + "\u8282\u8bfe\uff08" + today + " " + timeSlot + "\uff09\u56e0\u62a5\u540d\u4eba\u6570\u4e0d\u8db3\u5df2\u53d6\u6d88" + (deleted > 0 ? "\uff0c\u8bfe\u65f6\u5df2\u81ea\u52a8\u9000\u8fd8\u3002" : "\u3002"));
            msg.setSenderId(Integer.valueOf(0));
            msg.setSenderType("system");
            msg.setSenderName("\u7cfb\u7edf");
            msg.setReceiverId(booking.getUserId());
            msg.setReceiverType("student");
            msg.setIsRead(Integer.valueOf(0));
            msg.setExtra("{\"lessonCancel\":" + lesson.getId() + "}");
            msg.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            msg.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.messageMapper.insert(msg);
        }
    }

    private void sendCancelNotification(Integer userId, String studentName, String className, String venue, String coachName) {
        if (!this.subscribeMessageConfig.hasLessonCancelTemplate()) {
            return;
        }
        try {
            String venueTrunc;
            UserAuth userAuth = (UserAuth)this.userAuthMapper.selectOne(new QueryWrapper<UserAuth>().eq("user_id", (Object)userId).eq("terminal", (Object)1).last("limit 1"));
            if (userAuth == null || userAuth.getOpenid() == null || userAuth.getOpenid().isEmpty()) {
                return;
            }
            WxMaService wxMaService = WxMnpDriver.mnp();
            ArrayList<WxMaSubscribeMessage.MsgData> dataList = new ArrayList<WxMaSubscribeMessage.MsgData>();
            dataList.add(new WxMaSubscribeMessage.MsgData("thing2", studentName));
            dataList.add(new WxMaSubscribeMessage.MsgData("thing3", className));
            dataList.add(new WxMaSubscribeMessage.MsgData("thing4", "\u62a5\u540d\u4eba\u6570\u4e0d\u8db3\uff0c\u8bfe\u65f6\u5df2\u53d6\u6d88"));
            String string = venueTrunc = venue != null ? venue : "";
            if (venueTrunc.length() > 20) {
                venueTrunc = venueTrunc.substring(0, 14) + "..";
            }
            dataList.add(new WxMaSubscribeMessage.MsgData("thing6", venueTrunc));
            dataList.add(new WxMaSubscribeMessage.MsgData("thing7", coachName != null ? coachName : ""));
            WxMaSubscribeMessage message = WxMaSubscribeMessage.builder()
                    .toUser(userAuth.getOpenid())
                    .templateId(this.subscribeMessageConfig.getLessonCancelTemplateId())
                    .page(this.subscribeMessageConfig.getPage())
                    .data(dataList)
                    .build();
            wxMaService.getMsgService().sendSubscribeMsg(message);
            log.info("\u3010\u6700\u4f4e\u5f00\u8bfe\u68c0\u67e5\u3011\u5df2\u53d1\u9001\u53d6\u6d88\u901a\u77e5: userId={}, class={}", (Object)userId, (Object)className);
        }
        catch (Exception e) {
            log.error("\u3010\u6700\u4f4e\u5f00\u8bfe\u68c0\u67e5\u3011\u53d1\u9001\u53d6\u6d88\u901a\u77e5\u5931\u8d25: userId={}, error={}", (Object)userId, (Object)e.getMessage());
        }
    }
}
