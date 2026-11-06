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
}

