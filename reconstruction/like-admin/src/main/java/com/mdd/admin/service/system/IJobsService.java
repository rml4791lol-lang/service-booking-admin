/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.service.system.IJobsService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.system.JobsCreateValidate
 *  com.mdd.admin.validate.system.JobsSearchValidate
 *  com.mdd.admin.validate.system.JobsUpdateValidate
 *  com.mdd.admin.vo.system.JobsVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service.system;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.system.JobsCreateValidate;
import com.mdd.admin.validate.system.JobsSearchValidate;
import com.mdd.admin.validate.system.JobsUpdateValidate;
import com.mdd.admin.vo.system.JobsVo;
import com.mdd.common.core.PageResult;
import java.util.List;
import java.util.Map;

public interface IJobsService {
    public List<JobsVo> all();

    public PageResult<JobsVo> list(PageValidate var1, JobsSearchValidate var2);

    public JobsVo detail(Integer var1);

    public void add(JobsCreateValidate var1);

    public void edit(JobsUpdateValidate var1);

    public void del(Integer var1);

    public JSONObject getExportData(PageValidate var1, JobsSearchValidate var2);

    public Map<String, Object> export(JobsSearchValidate var1);
}

