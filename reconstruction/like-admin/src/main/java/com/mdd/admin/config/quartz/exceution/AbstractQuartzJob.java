/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.config.quartz.TaskConstants
 *  com.mdd.admin.config.quartz.exceution.AbstractQuartzJob
 *  com.mdd.common.entity.DevCrontab
 *  com.mdd.common.mapper.DevCrontabMapper
 *  com.mdd.common.util.SpringUtils
 *  com.mdd.common.util.StringUtils
 *  org.quartz.Job
 *  org.quartz.JobExecutionContext
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.BeanUtils
 */
package com.mdd.admin.config.quartz.exceution;

import com.mdd.admin.config.quartz.TaskConstants;
import com.mdd.common.entity.DevCrontab;
import com.mdd.common.mapper.DevCrontabMapper;
import com.mdd.common.util.SpringUtils;
import com.mdd.common.util.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public abstract class AbstractQuartzJob
implements Job {
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal();

    public void execute(JobExecutionContext context) {
        DevCrontab crontab = new DevCrontab();
        BeanUtils.copyProperties((Object)context.getMergedJobDataMap().get((Object)"TASK_PROPERTIES"), (Object)crontab);
        try {
            this.before();
            this.doExecute(context, crontab);
            this.after(crontab, null);
        }
        catch (Exception e) {
            log.error("\u4efb\u52a1\u6267\u884c\u5f02\u5e38\uff1a", (Throwable)e);
            this.after(crontab, e);
        }
    }

    protected void before() {
        threadLocal.set(System.currentTimeMillis());
    }

    protected void after(DevCrontab crontab, Exception e) {
        long startTime = (Long)threadLocal.get();
        long endTime = System.currentTimeMillis();
        threadLocal.remove();
        crontab.setError("");
        crontab.setLastTime(Long.valueOf(endTime / 1000L));
        crontab.setMaxTime(Long.valueOf(endTime - startTime));
        crontab.setTime(Long.valueOf(endTime - startTime));
        if (StringUtils.isNotNull(e)) {
            crontab.setError(e.getMessage());
            crontab.setStatus(TaskConstants.STATUS_FAIL);
        }
        ((DevCrontabMapper)SpringUtils.getBean(DevCrontabMapper.class)).updateById(crontab);
    }

    protected abstract void doExecute(JobExecutionContext var1, DevCrontab var2) throws Exception;
}

