/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSON
 *  com.alibaba.fastjson2.JSONArray
 *  com.alibaba.fastjson2.JSONObject
 */
package com.mdd.common.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mdd.common.entity.course.CourseClass;
import com.mdd.common.entity.course.CourseLesson;
import com.mdd.common.util.StringUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClassScheduleUtil {
    public static final int MODE_SINGLE = 0;
    public static final int MODE_MULTI = 1;
    private static final String[] WEEK_LABELS = new String[]{"", "\u5468\u4e00", "\u5468\u4e8c", "\u5468\u4e09", "\u5468\u56db", "\u5468\u4e94", "\u5468\u516d", "\u5468\u65e5"};
    private static final int TIME_SLOT_SUMMARY_MAX_LEN = 500;

    public static List<ScheduleRule> parseRules(String scheduleRules) {
        ArrayList<ScheduleRule> list = new ArrayList<ScheduleRule>();
        if (StringUtils.isEmpty(scheduleRules)) {
            return list;
        }
        JSONArray arr = JSON.parseArray((String)scheduleRules);
        if (arr == null) {
            return list;
        }
        for (int i = 0; i < arr.size(); ++i) {
            JSONObject obj = arr.getJSONObject(i);
            if (obj == null) continue;
            Integer weekDay = obj.getInteger("weekDay");
            String timeSlot = obj.getString("timeSlot");
            if (weekDay == null || weekDay < 1 || weekDay > 7 || StringUtils.isEmpty(timeSlot)) continue;
            list.add(new ScheduleRule(weekDay, timeSlot.trim()));
        }
        list.sort(Comparator.comparing(ScheduleRule::getWeekDay));
        return list;
    }

    public static String toJson(List<ScheduleRule> rules) {
        if (rules == null || rules.isEmpty()) {
            return "";
        }
        ArrayList arr = new ArrayList();
        for (ScheduleRule rule : rules) {
            LinkedHashMap<String, Object> m = new LinkedHashMap<String, Object>();
            m.put("weekDay", rule.getWeekDay());
            m.put("timeSlot", rule.getTimeSlot());
            arr.add(m);
        }
        return JSON.toJSONString(arr);
    }

    public static Map<Integer, String> toTimeSlotMap(String scheduleRules) {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (ScheduleRule rule : ClassScheduleUtil.parseRules(scheduleRules)) {
            map.put(rule.getWeekDay(), rule.getTimeSlot());
        }
        return map;
    }

    public static String buildWeekDays(List<ScheduleRule> rules) {
        if (rules == null || rules.isEmpty()) {
            return "";
        }
        ArrayList<String> days = new ArrayList<String>();
        for (ScheduleRule rule : rules) {
            days.add(String.valueOf(rule.getWeekDay()));
        }
        return String.join((CharSequence)",", days);
    }

    public static String buildTimeSlotSummary(List<ScheduleRule> rules) {
        if (rules == null || rules.isEmpty()) {
            return "";
        }
        ArrayList<String> parts = new ArrayList<String>();
        for (ScheduleRule rule : rules) {
            int wd = rule.getWeekDay();
            String label = wd >= 1 && wd <= 7 ? WEEK_LABELS[wd] : "\u5468" + wd;
            parts.add(label + rule.getTimeSlot());
        }
        return String.join((CharSequence)",", parts);
    }

    public static String trimTimeSlotSummary(String summary) {
        if (summary == null) {
            return "";
        }
        if (summary.length() <= 500) {
            return summary;
        }
        return summary.substring(0, 497) + "...";
    }

    public static String validateRules(List<ScheduleRule> rules) {
        if (rules == null || rules.isEmpty()) {
            return "\u8bf7\u81f3\u5c11\u914d\u7f6e\u4e00\u4e2a\u4e0a\u8bfe\u65f6\u6bb5";
        }
        HashSet<Integer> seen = new HashSet<Integer>();
        for (ScheduleRule rule : rules) {
            if (rule.getWeekDay() == null || rule.getWeekDay() < 1 || rule.getWeekDay() > 7) {
                return "\u4e0a\u8bfe\u661f\u671f\u65e0\u6548";
            }
            if (!seen.add(rule.getWeekDay())) {
                return "\u6bcf\u4e2a\u4e0a\u8bfe\u65e5\u53ea\u80fd\u914d\u7f6e\u4e00\u4e2a\u65f6\u6bb5";
            }
            String err = ClassScheduleUtil.validateTimeSlot(rule.getTimeSlot());
            if (err == null) continue;
            return err;
        }
        return null;
    }

    public static String validateTimeSlot(String timeSlot) {
        if (StringUtils.isEmpty(timeSlot) || !timeSlot.contains("-")) {
            return "\u4e0a\u8bfe\u65f6\u6bb5\u683c\u5f0f\u65e0\u6548";
        }
        String[] parts = timeSlot.split("-");
        if (parts.length != 2) {
            return "\u4e0a\u8bfe\u65f6\u6bb5\u683c\u5f0f\u65e0\u6548";
        }
        int start = ClassScheduleUtil.parseMinutes(parts[0].trim());
        int end = ClassScheduleUtil.parseMinutes(parts[1].trim());
        if (start < 0 || end < 0) {
            return "\u4e0a\u8bfe\u65f6\u6bb5\u683c\u5f0f\u65e0\u6548";
        }
        if (start >= end) {
            return "\u5f00\u59cb\u65f6\u95f4\u5fc5\u987b\u65e9\u4e8e\u7ed3\u675f\u65f6\u95f4";
        }
        return null;
    }

    public static String resolveLessonTimeSlot(CourseLesson lesson, CourseClass cls) {
        String slot;
        Integer mode;
        if (lesson != null && !StringUtils.isEmpty(lesson.getTimeSlot()) && ClassScheduleUtil.validateTimeSlot(lesson.getTimeSlot()) == null) {
            return lesson.getTimeSlot();
        }
        if (lesson != null && lesson.getWeekDay() != null && cls != null && (mode = Integer.valueOf(cls.getScheduleMode() != null ? cls.getScheduleMode() : 0)) == 1 && !StringUtils.isEmpty(cls.getScheduleRules()) && !StringUtils.isEmpty(slot = ClassScheduleUtil.toTimeSlotMap(cls.getScheduleRules()).get(lesson.getWeekDay())) && ClassScheduleUtil.validateTimeSlot(slot) == null) {
            return slot;
        }
        if (cls != null && !StringUtils.isEmpty(cls.getTimeSlot()) && ClassScheduleUtil.validateTimeSlot(cls.getTimeSlot()) == null) {
            return cls.getTimeSlot();
        }
        return "";
    }

    public static String resolveLessonVenue(CourseLesson lesson, CourseClass cls) {
        if (lesson != null && !StringUtils.isEmpty(lesson.getVenue())) {
            return lesson.getVenue();
        }
        if (cls != null && !StringUtils.isEmpty(cls.getVenue())) {
            return cls.getVenue();
        }
        return "";
    }

    public static boolean isTempLesson(CourseLesson lesson) {
        return lesson != null && lesson.getLessonType() != null && lesson.getLessonType() == 1;
    }

    private static int parseMinutes(String time) {
        if (StringUtils.isEmpty(time) || !time.contains(":")) {
            return -1;
        }
        String[] hm = time.split(":");
        if (hm.length != 2) {
            return -1;
        }
        try {
            return Integer.parseInt(hm[0]) * 60 + Integer.parseInt(hm[1]);
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void applySchedule(CourseClassScheduleInput input) {
        Integer mode = input.getScheduleMode() != null ? input.getScheduleMode() : 0;
        if (mode == 1) {
            List<ScheduleRule> rules = ClassScheduleUtil.parseRules(input.getScheduleRules());
            String err = ClassScheduleUtil.validateRules(rules);
            if (err != null) {
                throw new IllegalArgumentException(err);
            }
            input.setScheduleMode(1);
            input.setScheduleRules(ClassScheduleUtil.toJson(rules));
            input.setWeekDays(ClassScheduleUtil.buildWeekDays(rules));
            input.setTimeSlot(ClassScheduleUtil.trimTimeSlotSummary(ClassScheduleUtil.buildTimeSlotSummary(rules)));
        } else {
            String err;
            input.setScheduleMode(0);
            input.setScheduleRules(null);
            if (!StringUtils.isEmpty(input.getTimeSlot()) && (err = ClassScheduleUtil.validateTimeSlot(input.getTimeSlot())) != null) {
                throw new IllegalArgumentException(err);
            }
        }
    }

    public static class ScheduleRule {
        private Integer weekDay;
        private String timeSlot;

        public ScheduleRule() {
        }

        public ScheduleRule(Integer weekDay, String timeSlot) {
            this.weekDay = weekDay;
            this.timeSlot = timeSlot;
        }

        public Integer getWeekDay() {
            return this.weekDay;
        }

        public void setWeekDay(Integer weekDay) {
            this.weekDay = weekDay;
        }

        public String getTimeSlot() {
            return this.timeSlot;
        }

        public void setTimeSlot(String timeSlot) {
            this.timeSlot = timeSlot;
        }
    }

    public static class CourseClassScheduleInput {
        private Integer scheduleMode;
        private String scheduleRules;
        private String weekDays;
        private String timeSlot;

        public Integer getScheduleMode() {
            return this.scheduleMode;
        }

        public void setScheduleMode(Integer scheduleMode) {
            this.scheduleMode = scheduleMode;
        }

        public String getScheduleRules() {
            return this.scheduleRules;
        }

        public void setScheduleRules(String scheduleRules) {
            this.scheduleRules = scheduleRules;
        }

        public String getWeekDays() {
            return this.weekDays;
        }

        public void setWeekDays(String weekDays) {
            this.weekDays = weekDays;
        }

        public String getTimeSlot() {
            return this.timeSlot;
        }

        public void setTimeSlot(String timeSlot) {
            this.timeSlot = timeSlot;
        }
    }
}

