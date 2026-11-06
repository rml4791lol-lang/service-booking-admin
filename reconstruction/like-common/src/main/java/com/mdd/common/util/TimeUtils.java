/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.util;

import java.lang.management.ManagementFactory;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class TimeUtils {
    public static String timestampToDate(Long time) {
        if (time != null) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time * 1000L));
        }
        return "-";
    }

    public static String timestampToDate(String time) {
        if (time == null) {
            time = "0";
        }
        long longTime = Long.parseLong(time);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(longTime * 1000L));
    }

    public static String timestampToDate(Long time, String format) {
        return new SimpleDateFormat(format).format(new Date(time * 1000L));
    }

    public static String timestampToDate(String time, String format) {
        long longTime = Long.parseLong(time);
        return new SimpleDateFormat(format).format(new Date(longTime * 1000L));
    }

    public static Long dateToTimestamp(String date) {
        String dateTime = TimeUtils.formatDate(date);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime, new ParsePosition(0)).getTime() / 1000L;
    }

    public static String millisecondToDate(Long time) {
        Date date = new Date();
        date.setTime(time);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String millisecondToDate(String time) {
        Date date = new Date();
        date.setTime(Long.parseLong(time));
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String millisecondToDate(Long time, String format) {
        Date date = new Date();
        date.setTime(time);
        return new SimpleDateFormat(format).format(date);
    }

    public static String millisecondToDate(String time, String format) {
        Date date = new Date();
        date.setTime(Long.parseLong(time));
        return new SimpleDateFormat(format).format(date);
    }

    public static String formatDate(String dateStr) {
        dateStr = dateStr.trim();
        HashMap<String, String> dateRegFormat = new HashMap<String, String>();
        dateRegFormat.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D*$", "yyyy-MM-dd-HH-mm-ss");
        dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd-HH-mm");
        dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd-HH");
        dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd");
        dateRegFormat.put("^\\d{4}\\D+\\d{2}$", "yyyy-MM");
        dateRegFormat.put("^\\d{4}$", "yyyy");
        dateRegFormat.put("^\\d{14}$", "yyyyMMddHHmmss");
        dateRegFormat.put("^\\d{12}$", "yyyyMMddHHmm");
        dateRegFormat.put("^\\d{10}$", "yyyyMMddHH");
        dateRegFormat.put("^\\d{8}$", "yyyyMMdd");
        dateRegFormat.put("^\\d{6}$", "yyyyMM");
        dateRegFormat.put("^\\d{2}\\s*:\\s*\\d{2}\\s*:\\s*\\d{2}$", "yyyy-MM-dd-HH-mm-ss");
        dateRegFormat.put("^\\d{2}\\s*:\\s*\\d{2}$", "yyyy-MM-dd-HH-mm");
        dateRegFormat.put("^\\d{2}\\D+\\d{1,2}\\D+\\d{1,2}$", "yy-MM-dd");
        dateRegFormat.put("^\\d{1,2}\\D+\\d{1,2}$", "yyyy-dd-MM");
        dateRegFormat.put("^\\d{1,2}\\D+\\d{1,2}\\D+\\d{4}$", "dd-MM-yyyy");
        String curDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strSuccess = "";
        try {
            for (String key : dateRegFormat.keySet()) {
                if (!Pattern.compile(key).matcher(dateStr).matches()) continue;
                SimpleDateFormat formatter2 = new SimpleDateFormat((String)dateRegFormat.get(key));
                if (key.equals("^\\d{2}\\s*:\\s*\\d{2}\\s*:\\s*\\d{2}$") || key.equals("^\\d{2}\\s*:\\s*\\d{2}$")) {
                    dateStr = curDate + "-" + dateStr;
                } else if (key.equals("^\\d{1,2}\\D+\\d{1,2}$")) {
                    dateStr = curDate.substring(0, 4) + "-" + dateStr;
                }
                String dateReplace = dateStr.replaceAll("\\D+", "-");
                strSuccess = formatter1.format(formatter2.parse(dateReplace));
                break;
            }
            return strSuccess;
        }
        catch (Exception exception) {
            return "";
        }
    }

    public static Long timestamp() {
        return System.currentTimeMillis() / 1000L;
    }

    public static Date nowDate() {
        return new Date();
    }

    public static List<Long> today() {
        ArrayList<Long> list = new ArrayList<Long>();
        LocalDateTime startOfDay = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime zonedDateTimeStart = startOfDay.atZone(ZoneId.systemDefault());
        long startTimestamp = Date.from(zonedDateTimeStart.toInstant()).getTime() / 1000L;
        list.add(startTimestamp);
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(11, 23);
        todayEnd.set(12, 59);
        todayEnd.set(13, 59);
        todayEnd.set(14, 999);
        list.add(todayEnd.getTime().getTime() / 1000L);
        return list;
    }

    public static List<Long> yesterday() {
        List<Long> today = TimeUtils.today();
        ArrayList<Long> list = new ArrayList<Long>();
        list.add(today.get(0) - 86400L);
        list.add(today.get(1) - 86400L);
        return list;
    }

    public static List<Long> week() {
        ArrayList<Long> list = new ArrayList<Long>();
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(1), cal.get(2), cal.get(5), 0, 0, 0);
        cal.set(7, 2);
        list.add(cal.getTimeInMillis() / 1000L);
        list.add((Long)list.get(0) + 604800L - 1L);
        return list;
    }

    public static List<Long> lastWeek() {
        List<Long> week = TimeUtils.week();
        ArrayList<Long> list = new ArrayList<Long>();
        list.add(week.get(0) - 604800L);
        list.add(week.get(1) - 604800L);
        return list;
    }

    public static Long dayOfWeek() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        boolean isFirstSunday = cal.getFirstDayOfWeek() == 1;
        long week = cal.get(7);
        if (isFirstSunday) {
            week = week - 1L == 0L ? 7L : week - 1L;
        }
        return week;
    }

    public static List<Long> month() {
        ArrayList<Long> list = new ArrayList<Long>();
        Calendar calStart = Calendar.getInstance();
        calStart.set(calStart.get(1), calStart.get(2), calStart.get(5), 0, 0, 0);
        calStart.set(5, calStart.getActualMinimum(5));
        list.add(calStart.getTimeInMillis() / 1000L);
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(calEnd.get(1), calEnd.get(2), calEnd.get(5), 0, 0, 0);
        calEnd.set(5, calEnd.getActualMaximum(5));
        calEnd.set(11, 24);
        list.add(calEnd.getTimeInMillis() / 1000L - 1L);
        return list;
    }

    public static List<Long> lastMonth() {
        ArrayList<Long> list = new ArrayList<Long>();
        Calendar calStart = Calendar.getInstance();
        calStart.add(2, -1);
        calStart.set(5, 1);
        calStart.set(10, 0);
        calStart.set(12, 0);
        calStart.set(13, 0);
        calStart.set(14, 0);
        list.add(calStart.getTimeInMillis() / 1000L - 43200L);
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(calEnd.get(1), calEnd.get(2), calEnd.get(5), 0, 0, 0);
        calEnd.set(5, calEnd.getActualMinimum(5));
        list.add(calEnd.getTimeInMillis() / 1000L - 1L);
        return list;
    }

    public static List<Long> year() {
        ArrayList<Long> list = new ArrayList<Long>();
        Calendar calStart = Calendar.getInstance();
        calStart.add(1, 0);
        calStart.add(5, 0);
        calStart.add(2, 0);
        calStart.set(6, 1);
        calStart.set(11, 0);
        calStart.set(12, 0);
        calStart.set(13, 0);
        calStart.set(14, 0);
        list.add(calStart.getTimeInMillis() / 1000L);
        Calendar calEnd = Calendar.getInstance();
        int year = calEnd.get(1);
        calEnd.clear();
        calEnd.set(1, year);
        calEnd.set(11, 23);
        calEnd.set(12, 59);
        calEnd.set(13, 59);
        calEnd.set(14, 999);
        calEnd.roll(6, -1);
        list.add(calEnd.getTimeInMillis() / 1000L);
        return list;
    }

    public static List<Long> lastYear() {
        ArrayList<Long> list = new ArrayList<Long>();
        Calendar calStart = Calendar.getInstance();
        calStart.add(1, -1);
        calStart.set(11, 0);
        calStart.set(6, 1);
        calStart.set(12, 0);
        calStart.set(13, 0);
        calStart.set(14, 0);
        list.add(calStart.getTimeInMillis() / 1000L);
        Calendar calEnd = Calendar.getInstance();
        calEnd.add(1, -1);
        calEnd.set(2, calEnd.getActualMaximum(2));
        calEnd.set(5, calEnd.getActualMaximum(5));
        calEnd.set(11, 23);
        calEnd.set(12, 59);
        calEnd.set(13, 59);
        calEnd.set(14, 999);
        list.add(calEnd.getTimeInMillis() / 1000L);
        return list;
    }

    public static List<Long> dayToNow(int day) {
        List<Long> today = TimeUtils.today();
        ArrayList<Long> list = new ArrayList<Long>();
        list.add(today.get(0) - (long)day * 86400L);
        list.add(today.get(0) - 1L);
        return list;
    }

    public Long daysAgo(long day) {
        long currTime = System.currentTimeMillis() / 1000L;
        return currTime - day * 86400L;
    }

    public Long daysAfter(long day) {
        long currTime = System.currentTimeMillis() / 1000L;
        return currTime + day * 86400L;
    }

    public static Integer monthDay() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(5);
    }

    public static List<String> daysAgoDate(Integer day) {
        long time = TimeUtils.today().get(0);
        ArrayList<String> data = new ArrayList<String>();
        for (int i = 0; i < day; ++i) {
            if (i != 0) {
                time -= 86400L;
            }
            data.add(TimeUtils.timestampToDate(time, "yyyy-MM-dd"));
        }
        Collections.reverse(data);
        return data;
    }

    public static List<Long> daysAgoTime(Integer day) {
        long time = TimeUtils.today().get(0);
        ArrayList<Long> data = new ArrayList<Long>();
        for (int i = 0; i < day; ++i) {
            if (i != 0) {
                time -= 86400L;
            }
            data.add(time);
        }
        Collections.reverse(data);
        return data;
    }

    public static Date serverStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    public static String datePoor(Date endDate, Date nowDate) {
        long nd = 86400000L;
        long nh = 3600000L;
        long nm = 60000L;
        long diff = endDate.getTime() - nowDate.getTime();
        long day = diff / nd;
        long hour = diff % nd / nh;
        long min = diff % nd % nh / nm;
        return day + "\u5929" + hour + "\u5c0f\u65f6" + min + "\u5206\u949f";
    }

    public static String timestampToDay(Long time) {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(time * 1000L));
    }
}

