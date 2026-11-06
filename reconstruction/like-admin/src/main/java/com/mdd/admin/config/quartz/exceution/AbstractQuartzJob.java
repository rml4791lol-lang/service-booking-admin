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

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mdd.admin.config.quartz.TaskConstants;
import com.mdd.common.entity.DevCrontab;
import com.mdd.common.mapper.DevCrontabMapper;
import com.mdd.common.util.SpringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * Quartz 任务基类：把每次执行结果写回 la_dev_crontab（last_time / time / max_time / error）。
 *
 * <p>状态语义（2026-11-07 定时任务审计）：1=运行、2=停止、3=错误。旧实现失败时写 2，
 * 与「管理员停止」同值，而启动重建触发器时 {@code status != 1} 一律暂停——一次瞬时失败
 * 加一次重启就会让任务永久静默停摆。现在失败写 3；启动时只有 2 才暂停，3 继续调度，
 * 该任务下一次成功执行后自动恢复为 1。成功执行不会把管理员刚停止的任务（2）拉起：
 * 状态恢复走 {@code WHERE status=3} 条件更新。</p>
 *
 * <p>另外错误信息按 la_dev_crontab.error varchar(256) 截断，避免错误串过长导致整条
 * 结果回写失败（连 last_time 都落不了库）。</p>
 */
public abstract class AbstractQuartzJob
implements Job {
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal();
    /** la_dev_crontab.error 列 varchar(256)，留余量避免写超长失败。 */
    private static final int ERROR_MAX_LENGTH = 240;

    public void execute(JobExecutionContext context) {
        DevCrontab crontab = new DevCrontab();
        BeanUtils.copyProperties((Object)context.getMergedJobDataMap().get((Object)"TASK_PROPERTIES"), (Object)crontab);
        try {
            this.before();
            this.doExecute(context, crontab);
            this.after(crontab, null);
        }
        catch (Exception e) {
            log.error("任务执行异常：", (Throwable)e);
            this.after(crontab, e);
        }
    }

    protected void before() {
        threadLocal.set(System.currentTimeMillis());
    }

    protected void after(DevCrontab crontab, Exception e) {
        Long start = (Long)threadLocal.get();
        long startTime = start == null ? System.currentTimeMillis() : start.longValue();
        long endTime = System.currentTimeMillis();
        threadLocal.remove();
        long cost = endTime - startTime;
        long maxCost = crontab.getMaxTime() != null && crontab.getMaxTime() > cost
                ? crontab.getMaxTime() : cost;
        String error = "";
        if (e != null) {
            error = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            if (error == null) {
                error = e.getClass().getSimpleName();
            }
            if (error.length() > ERROR_MAX_LENGTH) {
                error = error.substring(0, ERROR_MAX_LENGTH);
            }
        }
        long nowSeconds = endTime / 1000L;

        if (crontab.getId() == null) {
            log.warn("定时任务缺少 id，跳过执行结果落库: name={}", crontab.getName());
            return;
        }
        DevCrontabMapper mapper = (DevCrontabMapper)SpringUtils.getBean(DevCrontabMapper.class);
        UpdateWrapper<DevCrontab> metrics = new UpdateWrapper<DevCrontab>()
                .eq("id", crontab.getId())
                .set("error", error)
                .set("last_time", nowSeconds)
                .set("time", cost)
                .set("max_time", maxCost);
        mapper.update(null, metrics);
        if (e != null) {
            // 失败写 3=错误；管理员刚停止(2)的任务不回写成错误，避免重启后被拉起。
            mapper.update(null, new UpdateWrapper<DevCrontab>()
                    .eq("id", crontab.getId())
                    .ne("status", TaskConstants.STATUS_STOP)
                    .set("status", TaskConstants.STATUS_FAIL));
        } else {
            // 仅当库中仍是「错误」时恢复运行；管理员停止(2)不会被内存旧状态覆盖。
            mapper.update(null, new UpdateWrapper<DevCrontab>()
                    .eq("id", crontab.getId())
                    .eq("status", TaskConstants.STATUS_FAIL)
                    .set("status", TaskConstants.STATUS_RUN));
        }

        crontab.setError(error);
        crontab.setLastTime(nowSeconds);
        crontab.setTime(cost);
        crontab.setMaxTime(maxCost);
        if (e != null) {
            crontab.setStatus(TaskConstants.STATUS_FAIL);
        } else {
            crontab.setStatus(TaskConstants.STATUS_RUN);
        }
    }

    protected abstract void doExecute(JobExecutionContext var1, DevCrontab var2) throws Exception;
}
