/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ICrontabService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.crontab.CrontabCreateValidate
 *  com.mdd.admin.validate.crontab.CrontabUpdateValidate
 *  com.mdd.admin.vo.CrontabDetailVo
 *  com.mdd.admin.vo.CrontabListedVo
 *  com.mdd.common.core.PageResult
 *  org.quartz.SchedulerException
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.crontab.CrontabCreateValidate;
import com.mdd.admin.validate.crontab.CrontabUpdateValidate;
import com.mdd.admin.vo.CrontabDetailVo;
import com.mdd.admin.vo.CrontabListedVo;
import com.mdd.common.core.PageResult;
import org.quartz.SchedulerException;

public interface ICrontabService {
    public PageResult<CrontabListedVo> list(PageValidate var1);

    public CrontabDetailVo detail(Integer var1);

    public void add(CrontabCreateValidate var1) throws SchedulerException;

    public void edit(CrontabUpdateValidate var1) throws SchedulerException;

    public void del(Integer var1) throws SchedulerException;
}

