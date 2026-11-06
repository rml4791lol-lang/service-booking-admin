/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.util;

import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LessonScheduleUtil {
    public static List<Map<String, Object>> generateSchedule(Integer startDate, Integer totalLessons, String weekDaysStr, String timeSlot) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (startDate == null || totalLessons == null || totalLessons <= 0 || StringUtils.isEmpty(weekDaysStr)) {
            return list;
        }
        HashSet<Integer> validDays = new HashSet<Integer>();
        if (weekDaysStr.contains("1") || weekDaysStr.contains("\u4e00")) {
            validDays.add(1);
        }
        if (weekDaysStr.contains("2") || weekDaysStr.contains("\u4e8c")) {
            validDays.add(2);
        }
        if (weekDaysStr.contains("3") || weekDaysStr.contains("\u4e09")) {
            validDays.add(3);
        }
        if (weekDaysStr.contains("4") || weekDaysStr.contains("\u56db")) {
            validDays.add(4);
        }
        if (weekDaysStr.contains("5") || weekDaysStr.contains("\u4e94")) {
            validDays.add(5);
        }
        if (weekDaysStr.contains("6") || weekDaysStr.contains("\u516d")) {
            validDays.add(6);
        }
        if (weekDaysStr.contains("7") || weekDaysStr.contains("\u65e5") || weekDaysStr.contains("\u5929")) {
            validDays.add(7);
        }
        if (validDays.isEmpty()) {
            return list;
        }
        Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis((long)startDate.intValue() * 1000L);
        startCal.set(11, 0);
        startCal.set(12, 0);
        startCal.set(13, 0);
        int lessonNo = 1;
        for (int dayCount = 0; lessonNo <= totalLessons && dayCount < 3000; ++dayCount) {
            int logicalDay;
            int dayOfWeek = startCal.get(7);
            int n = logicalDay = dayOfWeek == 1 ? 7 : dayOfWeek - 1;
            if (validDays.contains(logicalDay)) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("lessonNo", lessonNo++);
                map.put("date", TimeUtils.timestampToDate(startCal.getTimeInMillis() / 1000L, "yyyy-MM-dd"));
                map.put("timestamp", startCal.getTimeInMillis() / 1000L);
                map.put("timeSlot", timeSlot == null ? "" : timeSlot);
                map.put("weekDay", logicalDay);
                list.add(map);
            }
            startCal.add(5, 1);
        }
        return list;
    }

    public static List<Map<String, Object>> generateSchedule(Integer startDate, Integer totalLessons, Map<Integer, String> weekDayTimeSlots) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (startDate == null || totalLessons == null || totalLessons <= 0 || weekDayTimeSlots == null || weekDayTimeSlots.isEmpty()) {
            return list;
        }
        HashSet<Integer> validDays = new HashSet<Integer>(weekDayTimeSlots.keySet());
        if (validDays.isEmpty()) {
            return list;
        }
        Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis((long)startDate.intValue() * 1000L);
        startCal.set(11, 0);
        startCal.set(12, 0);
        startCal.set(13, 0);
        int lessonNo = 1;
        for (int dayCount = 0; lessonNo <= totalLessons && dayCount < 3000; ++dayCount) {
            int logicalDay;
            int dayOfWeek = startCal.get(7);
            int n = logicalDay = dayOfWeek == 1 ? 7 : dayOfWeek - 1;
            if (validDays.contains(logicalDay)) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("lessonNo", lessonNo++);
                map.put("date", TimeUtils.timestampToDate(startCal.getTimeInMillis() / 1000L, "yyyy-MM-dd"));
                map.put("timestamp", startCal.getTimeInMillis() / 1000L);
                String slot = weekDayTimeSlots.get(logicalDay);
                map.put("timeSlot", slot == null ? "" : slot);
                map.put("weekDay", logicalDay);
                list.add(map);
            }
            startCal.add(5, 1);
        }
        return list;
    }

    public static List<Map<String, Object>> generateSchedule(Integer startDate, Integer endDate, String weekDaysStr, String timeSlot, boolean legacy) {
        return LessonScheduleUtil.generateSchedule(startDate, 300, weekDaysStr, timeSlot);
    }
}

