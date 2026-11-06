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
 *  com.mdd.common.entity.RechargeOrder
 *  com.mdd.common.entity.course.CourseClass
 *  com.mdd.common.entity.course.CourseLesson
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.entity.user.UserAuth
 *  com.mdd.common.mapper.MessageMapper
 *  com.mdd.common.mapper.RechargeOrderMapper
 *  com.mdd.common.mapper.course.CourseClassMapper
 *  com.mdd.common.mapper.course.CourseLessonMapper
 *  com.mdd.common.mapper.user.UserAuthMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.plugin.wechat.WxMnpDriver
 *  com.mdd.common.util.ClassScheduleUtil
 *  com.mdd.front.task.BookingReminderTask
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
import com.mdd.common.entity.RechargeOrder;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.entity.user.User;
import com.mdd.common.entity.user.UserAuth;
import com.mdd.common.mapper.MessageMapper;
import com.mdd.common.mapper.RechargeOrderMapper;
import com.mdd.common.mapper.course.CourseClassMapper;
import com.mdd.common.mapper.course.CourseLessonMapper;
import com.mdd.common.mapper.user.UserAuthMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.plugin.wechat.WxMnpDriver;
import com.mdd.common.util.ClassScheduleUtil;
import com.mdd.common.util.SensitiveUtils;
import com.mdd.front.config.SubscribeMessageConfig;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BookingReminderTask {
    private static final Logger log = LoggerFactory.getLogger(BookingReminderTask.class);
    @Resource
    private SubscribeMessageConfig subscribeMessageConfig;
    @Resource
    private RechargeOrderMapper rechargeOrderMapper;
    @Resource
    private CourseLessonMapper courseLessonMapper;
    @Resource
    private CourseClassMapper courseClassMapper;
    @Resource
    private UserAuthMapper userAuthMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private MessageMapper messageMapper;

    @Scheduled(cron="0 */5 * * * ?")
    public void remindPurchasedLessons() {
        if (!this.subscribeMessageConfig.hasLessonReminderTemplate()) {
            return;
        }
        CourseClass cc;
        ArrayList<Integer> classIds;
        List<CourseLesson> todayLessons;
        log.info("\u3010\u8bfe\u524d\u63d0\u9192\u3011\u5f00\u59cb\u626b\u63cf\u5df2\u8d2d\u8bfe\u7a0b\u7684\u4eca\u65e5\u8bfe\u8282...");
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalTime now = LocalTime.now();
        List<RechargeOrder> paidOrders = this.rechargeOrderMapper.selectList(new QueryWrapper<RechargeOrder>().eq("pay_status", (Object)1).gt("course_class_id", (Object)0).isNull("delete_time"));
        if (paidOrders == null || paidOrders.isEmpty()) {
            return;
        }
        LinkedHashMap<Integer, List<Integer>> classUserMap = new LinkedHashMap<Integer, List<Integer>>();
        HashSet<Integer> allUserIds = new HashSet<Integer>();
        for (RechargeOrder order : paidOrders) {
            classUserMap.computeIfAbsent(order.getCourseClassId(), k -> new ArrayList());
            if (classUserMap.get(order.getCourseClassId()).contains(order.getUserId())) continue;
            classUserMap.get(order.getCourseClassId()).add(order.getUserId());
            allUserIds.add(order.getUserId());
        }
        HashMap<Integer, User> userMap = new HashMap<Integer, User>();
        if (!allUserIds.isEmpty()) {
            List<User> userList = this.userMapper.selectBatchIds(allUserIds);
            for (User u : userList) {
                userMap.put(u.getId(), u);
            }
        }
        if ((todayLessons = this.courseLessonMapper.selectList(new QueryWrapper<CourseLesson>().in("class_id", classIds = new ArrayList(classUserMap.keySet())).eq("lesson_date", (Object)today).eq("status", (Object)0).orderByAsc("time_slot"))) == null || todayLessons.isEmpty()) {
            return;
        }
        LinkedHashMap<Integer, CourseClass> classMap = new LinkedHashMap<Integer, CourseClass>();
        for (Integer cid : classIds) {
            if (classMap.containsKey(cid) || (cc = (CourseClass)this.courseClassMapper.selectById((Serializable)cid)) == null) continue;
            classMap.put(cid, cc);
        }
        for (CourseLesson lesson : todayLessons) {
            String timeSlot = ClassScheduleUtil.resolveLessonTimeSlot((CourseLesson)lesson, (CourseClass)(cc = (CourseClass)classMap.get(lesson.getClassId())));
            if (timeSlot.isEmpty() || !timeSlot.contains("-")) continue;
            try {
                String venue;
                String startTimeStr = timeSlot.split("-")[0].trim();
                LocalTime startTime = LocalTime.parse(startTimeStr);
                int diffMinutes = (startTime.getHour() - now.getHour()) * 60 + (startTime.getMinute() - now.getMinute());
                if (diffMinutes < 120 || diffMinutes > 180) {
                    log.info("\u3010\u8bfe\u524d\u63d0\u9192\u3011\u8bfe\u7a0b\u3010{}\u3011\u5f53\u524d\u8bfe\u8282\u65f6\u95f4\u5dee\u3010{}\u3011\uff0c\u4e0d\u7b26\u54082-3\u5c0f\u65f6\u8981\u6c42\uff0c\u8df3\u8fc7", (Object)lesson.getId(), (Object)diffMinutes);
                    continue;
                }
                String className = cc != null ? cc.getName() : "\u8bfe\u7a0b";
                String string = venue = cc != null && cc.getVenue() != null ? cc.getVenue() : "";
                List<Integer> userIds = classUserMap.get(lesson.getClassId());
                if (userIds == null || userIds.isEmpty()) continue;
                for (Integer userId : userIds) {
                    boolean isSuccess;
                    UserAuth userAuth;
                    String extraKey = "\"lessonReminder\":" + lesson.getId();
                    Long sentCount = this.messageMapper.selectCount(new QueryWrapper<Message>().eq("type", (Object)3).eq("receiver_id", (Object)userId).eq("receiver_type", (Object)"student").like("extra", (Object)extraKey));
                    if (sentCount != null && sentCount > 0L || (userAuth = (UserAuth)this.userAuthMapper.selectOne(new QueryWrapper<UserAuth>().eq("user_id", (Object)userId).eq("terminal", (Object)1).last("limit 1"))) == null || userAuth.getOpenid() == null || userAuth.getOpenid().isEmpty()) continue;
                    String studentName = "\u5b66\u5458";
                    User user = (User)userMap.get(userId);
                    if (user != null) {
                        if (user.getRealName() != null && !user.getRealName().isEmpty()) {
                            studentName = user.getRealName();
                        } else if (user.getNickname() != null && !user.getNickname().isEmpty()) {
                            studentName = user.getNickname();
                        }
                    }
                    if (!(isSuccess = this.sendWxSubscribeMessage(userAuth.getOpenid(), studentName, className, timeSlot, venue, today))) continue;
                    Message msg = new Message();
                    msg.setType(Integer.valueOf(3));
                    msg.setTitle("\u9884\u7ea6\u63d0\u9192");
                    msg.setContent("\u60a8\u8d2d\u4e70\u7684\u300c" + className + "\u300d\u5c06\u4e8e\u4eca\u5929 " + startTimeStr + " \u5f00\u8bfe\uff0c\u8bf7\u505a\u597d\u51c6\u5907\u3002");
                    msg.setSenderId(Integer.valueOf(0));
                    msg.setSenderType("system");
                    msg.setSenderName("\u7cfb\u7edf");
                    msg.setReceiverId(userId);
                    msg.setReceiverType("student");
                    msg.setIsRead(Integer.valueOf(0));
                    msg.setExtra("{\"lessonReminder\":" + lesson.getId() + "}");
                    msg.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
                    msg.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
                    this.messageMapper.insert(msg);
                    log.info("\u3010\u9884\u7ea6\u63d0\u9192\u3011\u5df2\u53d1\u4fe1\u5e76\u5df2\u8bb0\u5f55\u9632\u91cd: userId={}, lessonId={}, class={}", new Object[]{userId, lesson.getId(), className});
                }
            }
            catch (Exception e) {
                log.error("\u3010\u9884\u7ea6\u63d0\u9192\u3011\u5904\u7406\u8bfe\u8282\u5931\u8d25: lessonId={}, error={}", new Object[]{lesson.getId(), e.getMessage(), e});
            }
        }
    }

    private boolean sendWxSubscribeMessage(String openId, String studentName, String className, String timeSlot, String venue, String lessonDate) {
        try {
            String venueTrunc;
            WxMaService wxMaService = WxMnpDriver.mnp();
            String startTime = timeSlot;
            if (timeSlot != null && timeSlot.contains("-")) {
                startTime = timeSlot.split("-")[0].trim();
            }
            ArrayList<WxMaSubscribeMessage.MsgData> dataList = new ArrayList<WxMaSubscribeMessage.MsgData>();
            dataList.add(new WxMaSubscribeMessage.MsgData("thing1", studentName != null ? studentName : "\u5b66\u5458"));
            dataList.add(new WxMaSubscribeMessage.MsgData("thing2", className));
            dataList.add(new WxMaSubscribeMessage.MsgData("time3", startTime));
            String string = venueTrunc = venue != null ? venue : "";
            if (venueTrunc.length() > 20) {
                venueTrunc = venueTrunc.substring(0, 14) + "..";
            }
            dataList.add(new WxMaSubscribeMessage.MsgData("thing4", venueTrunc));
            dataList.add(new WxMaSubscribeMessage.MsgData("thing10", "\u8bf7\u60a8\u4e8e\u8bfe\u7a0b\u5f00\u59cb\u524d\u4e00\u5c0f\u65f6\u5230\u5c0f\u7a0b\u5e8f\u9884\u7ea6"));
            WxMaSubscribeMessage message = WxMaSubscribeMessage.builder()
                    .toUser(openId)
                    .templateId(this.subscribeMessageConfig.getLessonReminderTemplateId())
                    .page(this.subscribeMessageConfig.getPage())
                    .data(dataList)
                    .build();
            wxMaService.getMsgService().sendSubscribeMsg(message);
            return true;
        }
        catch (Exception e) {
            log.error("【课前提醒】微信订阅消息发送失败: openId={}, error={}",
                    SensitiveUtils.maskOpenid((String) openId), e.getMessage());
            return false;
        }
    }
}
