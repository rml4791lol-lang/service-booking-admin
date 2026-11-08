/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.config.quartz.CronUtils
 *  org.quartz.CronExpression
 */
package com.mdd.admin.config.quartz;

import java.text.ParseException;
import java.util.Date;
import org.quartz.CronExpression;

public class CronUtils {
    public static boolean isValid(String cronExpression) {
        return CronExpression.isValidExpression((String)cronExpression);
    }

    public static String invalidMessage(String cronExpression) {
        try {
            new CronExpression(cronExpression);
            return null;
        }
        catch (ParseException pe) {
            return pe.getMessage();
        }
    }

    public static Date nextExecution(String cronExpression) {
        try {
            CronExpression cron = new CronExpression(cronExpression);
            return cron.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
        }
        catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 漏跑判定（2026-11-07 定时任务审计）。
     *
     * @param cronExpression 任务 cron 表达式
     * @param lastTimeSeconds la_dev_crontab.last_time（秒）；空/0 表示从未执行
     * @param nowMillis 当前时间（毫秒）
     * @return 上次执行之后本应发生、且已过期的最近一次计划时间；未漏跑/无法判断返回 null
     */
    public static Date missedFireTime(String cronExpression, Long lastTimeSeconds, long nowMillis) {
        if (cronExpression == null || lastTimeSeconds == null || lastTimeSeconds <= 0) {
            return null;
        }
        try {
            CronExpression cron = new CronExpression(cronExpression);
            Date next = cron.getNextValidTimeAfter(new Date(lastTimeSeconds * 1000L));
            return next != null && next.getTime() < nowMillis ? next : null;
        }
        catch (ParseException e) {
            return null;
        }
    }
}

