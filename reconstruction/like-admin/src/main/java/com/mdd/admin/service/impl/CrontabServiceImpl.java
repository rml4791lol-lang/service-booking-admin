/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.config.quartz.QuartzUtils
 *  com.mdd.admin.service.ICrontabService
 *  com.mdd.admin.service.impl.CrontabServiceImpl
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.crontab.CrontabCreateValidate
 *  com.mdd.admin.validate.crontab.CrontabUpdateValidate
 *  com.mdd.admin.vo.CrontabDetailVo
 *  com.mdd.admin.vo.CrontabListedVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.DevCrontab
 *  com.mdd.common.mapper.DevCrontabMapper
 *  javax.annotation.PostConstruct
 *  javax.annotation.Resource
 *  org.quartz.Scheduler
 *  org.quartz.SchedulerException
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.Assert
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.config.quartz.CronUtils;
import com.mdd.admin.config.quartz.QuartzUtils;
import com.mdd.admin.config.quartz.TaskConstants;
import com.mdd.admin.service.ICrontabService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.crontab.CrontabCreateValidate;
import com.mdd.admin.validate.crontab.CrontabUpdateValidate;
import com.mdd.admin.vo.CrontabDetailVo;
import com.mdd.admin.vo.CrontabListedVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.DevCrontab;
import com.mdd.common.mapper.DevCrontabMapper;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class CrontabServiceImpl
implements ICrontabService {
    private static final Logger log = LoggerFactory.getLogger(CrontabServiceImpl.class);
    @Resource
    Scheduler scheduler;
    @Resource
    DevCrontabMapper crontabMapper;

    @PostConstruct
    public void init() throws SchedulerException {
        this.scheduler.clear();
        List<DevCrontab> jobs = this.crontabMapper.selectList(new QueryWrapper<DevCrontab>().isNull("delete_time"));
        long now = System.currentTimeMillis();
        for (DevCrontab crontab : jobs) {
            QuartzUtils.createScheduleJob((Scheduler)this.scheduler, (DevCrontab)crontab);
            if (TaskConstants.STATUS_STOP.equals(crontab.getStatus())) {
                log.info("定时任务已停止，本次不调度: id={} name={}", crontab.getId(), crontab.getName());
                continue;
            }
            if (TaskConstants.STATUS_FAIL.equals(crontab.getStatus())) {
                log.warn("定时任务上次执行失败，本次继续调度等待自愈: id={} name={} error={}",
                        crontab.getId(), crontab.getName(), crontab.getError());
            }
            this.warnIfMissed(crontab, now);
        }
    }

    /** 漏跑告警（2026-11-07 定时任务审计）：上次执行后若已错过计划点，启动时先告警。 */
    private void warnIfMissed(DevCrontab crontab, long nowMillis) {
        Long lastTime = crontab.getLastTime();
        if (lastTime == null || lastTime.longValue() <= 0L) {
            log.warn("定时任务从未执行过（last_time 为空），请确认是否漏跑: id={} name={} expression={}",
                    crontab.getId(), crontab.getName(), crontab.getExpression());
            return;
        }
        Date missed = CronUtils.missedFireTime(crontab.getExpression(), lastTime, nowMillis);
        if (missed != null) {
            long overdueMinutes = Math.max(0L, (nowMillis - missed.getTime()) / 60000L);
            log.warn("定时任务疑似漏跑: id={} name={} last_time={} 最近应执行={} 已逾期{}分钟 expression={}",
                    crontab.getId(), crontab.getName(), lastTime, missed, overdueMinutes,
                    crontab.getExpression());
        }
    }

    public PageResult<CrontabListedVo> list(PageValidate pageValidate) {
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        IPage<DevCrontab> iPage = this.crontabMapper.selectPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), new QueryWrapper<DevCrontab>().isNull("delete_time").orderByDesc("id"));
        LinkedList<CrontabListedVo> list = new LinkedList<CrontabListedVo>();
        for (DevCrontab crontab : iPage.getRecords()) {
            CrontabListedVo vo = new CrontabListedVo();
            BeanUtils.copyProperties((Object)crontab, (Object)vo);
            vo.setTypeDesc("\u5b9a\u65f6\u4efb\u52a1");
            vo.setStatusDesc(vo.getStatus() == null ? "\u672a\u8fd0\u884c" : (vo.getStatus().equals(1) ? "\u8fd0\u884c" : (vo.getStatus().equals(2) ? "\u505c\u6b62" : "\u9519\u8bef")));
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public CrontabDetailVo detail(Integer id) {
        DevCrontab crontab = (DevCrontab)this.crontabMapper.selectOne(new QueryWrapper<DevCrontab>().eq("id", (Object)id).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)crontab, (String)"\u6570\u636e\u4e0d\u5b58\u5728!");
        CrontabDetailVo vo = new CrontabDetailVo();
        BeanUtils.copyProperties((Object)crontab, (Object)vo);
        return vo;
    }

    @Transactional
    public void add(CrontabCreateValidate createValidate) throws SchedulerException {
        DevCrontab crontab = new DevCrontab();
        crontab.setName(createValidate.getName());
        crontab.setType(createValidate.getType());
        crontab.setCommand(createValidate.getCommand());
        crontab.setExpression(createValidate.getExpression());
        crontab.setStatus(createValidate.getStatus());
        crontab.setRemark(createValidate.getRemark());
        crontab.setParams(createValidate.getParams());
        crontab.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        crontab.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.crontabMapper.insert(crontab);
        if (createValidate.getStatus() != null && createValidate.getStatus().equals(1)) {
            QuartzUtils.createScheduleJob((Scheduler)this.scheduler, (DevCrontab)crontab);
        }
    }

    @Transactional
    public void edit(CrontabUpdateValidate updateValidate) throws SchedulerException {
        DevCrontab crontab = (DevCrontab)this.crontabMapper.selectOne(new QueryWrapper<DevCrontab>().eq("id", (Object)updateValidate.getId()).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)crontab, (String)"\u6570\u636e\u4e0d\u5b58\u5728!");
        crontab.setName(updateValidate.getName());
        crontab.setCommand(updateValidate.getCommand());
        crontab.setType(updateValidate.getType());
        crontab.setExpression(updateValidate.getExpression());
        crontab.setStatus(updateValidate.getStatus());
        crontab.setRemark(updateValidate.getRemark());
        crontab.setParams(updateValidate.getParams());
        crontab.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.crontabMapper.updateById(crontab);
        if (updateValidate.getStatus() != null && updateValidate.getStatus().equals(1)) {
            QuartzUtils.createScheduleJob((Scheduler)this.scheduler, (DevCrontab)crontab);
        } else {
            this.scheduler.deleteJob(QuartzUtils.getJobKey((Integer)crontab.getId(), (String)crontab.getType().toString()));
        }
    }

    @Transactional
    public void del(Integer id) throws SchedulerException {
        DevCrontab crontab = (DevCrontab)this.crontabMapper.selectOne(new QueryWrapper<DevCrontab>().eq("id", (Object)id).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)crontab, (String)"\u6570\u636e\u4e0d\u5b58\u5728!");
        crontab.setDeleteTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.crontabMapper.updateById(crontab);
        this.scheduler.deleteJob(QuartzUtils.getJobKey((Integer)crontab.getId(), (String)crontab.getType().toString()));
    }
}

