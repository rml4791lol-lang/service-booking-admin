/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.config.quartz.InvokeUtils
 *  com.mdd.admin.config.quartz.exceution.AbstractQuartzJob
 *  com.mdd.admin.config.quartz.exceution.QuartzDisExecution
 *  com.mdd.common.entity.DevCrontab
 *  org.quartz.DisallowConcurrentExecution
 *  org.quartz.JobExecutionContext
 */
package com.mdd.admin.config.quartz.exceution;

import com.mdd.admin.config.quartz.InvokeUtils;
import com.mdd.admin.config.quartz.exceution.AbstractQuartzJob;
import com.mdd.common.entity.DevCrontab;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

@DisallowConcurrentExecution
public class QuartzDisExecution
extends AbstractQuartzJob {
    protected void doExecute(JobExecutionContext context, DevCrontab crontab) throws Exception {
        InvokeUtils.invokeMethod((DevCrontab)crontab);
    }
}

