/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.config.quartz.CronUtils
 *  com.mdd.admin.config.quartz.QuartzUtils
 *  com.mdd.admin.config.quartz.TaskConstants
 *  com.mdd.admin.config.quartz.exceution.QuartzJobExecution
 *  com.mdd.common.entity.DevCrontab
 *  com.mdd.common.util.StringUtils
 *  org.quartz.CronScheduleBuilder
 *  org.quartz.CronTrigger
 *  org.quartz.Job
 *  org.quartz.JobBuilder
 *  org.quartz.JobDetail
 *  org.quartz.JobKey
 *  org.quartz.ScheduleBuilder
 *  org.quartz.Scheduler
 *  org.quartz.SchedulerException
 *  org.quartz.Trigger
 *  org.quartz.TriggerBuilder
 *  org.quartz.TriggerKey
 */
package com.mdd.admin.config.quartz;

import com.mdd.admin.config.quartz.CronUtils;
import com.mdd.admin.config.quartz.TaskConstants;
import com.mdd.admin.config.quartz.exceution.QuartzJobExecution;
import com.mdd.common.entity.DevCrontab;
import com.mdd.common.util.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

/*
 * Exception performing whole class analysis ignored.
 */
public class QuartzUtils {
    private static Class<? extends Job> getQuartzJobClass(DevCrontab crontab) {
        return QuartzJobExecution.class;
    }

    public static JobKey getJobKey(Integer jobId, String jobGroup) {
        return JobKey.jobKey((String)("TASK_CLASS_NAME" + jobId), (String)jobGroup);
    }

    public static TriggerKey getTriggerKey(Integer jobId, String jobGroup) {
        return TriggerKey.triggerKey((String)("TASK_PROPERTIES" + jobId), (String)jobGroup);
    }

    public static void createScheduleJob(Scheduler scheduler, DevCrontab job) throws SchedulerException {
        Integer jobId = job.getId();
        String jobGroup = job.getType().toString();
        String expression = job.getExpression();
        Class jobClass = QuartzUtils.getQuartzJobClass((DevCrontab)job);
        JobDetail jobDetail = JobBuilder.newJob((Class)jobClass).withIdentity(QuartzUtils.getJobKey((Integer)jobId, (String)jobGroup)).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule((String)expression);
        cronScheduleBuilder = cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
        jobDetail.getJobDataMap().put("TASK_PROPERTIES", (Object)job);
        CronTrigger trigger = (CronTrigger)TriggerBuilder.newTrigger().withIdentity(QuartzUtils.getTriggerKey((Integer)jobId, (String)jobGroup)).withSchedule((ScheduleBuilder)cronScheduleBuilder).build();
        if (scheduler.checkExists(QuartzUtils.getJobKey((Integer)jobId, (String)jobGroup))) {
            scheduler.deleteJob(QuartzUtils.getJobKey((Integer)jobId, (String)jobGroup));
        }
        if (StringUtils.isNotNull(CronUtils.nextExecution((String)job.getExpression()))) {
            scheduler.scheduleJob(jobDetail, (Trigger)trigger);
        }
        if (!job.getStatus().equals(TaskConstants.STATUS_RUN)) {
            scheduler.pauseJob(QuartzUtils.getJobKey((Integer)jobId, (String)jobGroup));
        }
    }
}

