/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.excel.EasyExcel
 *  com.alibaba.excel.support.ExcelTypeEnum
 *  com.alibaba.excel.write.builder.ExcelWriterBuilder
 *  com.alibaba.fastjson2.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.service.impl.system.JobsServiceImpl
 *  com.mdd.admin.service.system.IJobsService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.system.JobsCreateValidate
 *  com.mdd.admin.validate.system.JobsSearchValidate
 *  com.mdd.admin.validate.system.JobsUpdateValidate
 *  com.mdd.admin.vo.system.JobsExportVo
 *  com.mdd.admin.vo.system.JobsVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.admin.Jobs
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.admin.AdminMapper
 *  com.mdd.common.mapper.admin.JobsMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.ToolUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.common.util.YmlUtils
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl.system;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.system.IJobsService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.system.JobsCreateValidate;
import com.mdd.admin.validate.system.JobsSearchValidate;
import com.mdd.admin.validate.system.JobsUpdateValidate;
import com.mdd.admin.vo.system.JobsExportVo;
import com.mdd.admin.vo.system.JobsVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.admin.Jobs;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.mapper.admin.JobsMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.YmlUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.mdd.common.entity.admin.Admin;

@Service
public class JobsServiceImpl
implements IJobsService {
    @Resource
    JobsMapper jobsMapper;
    @Resource
    AdminMapper adminMapper;

    public List<JobsVo> all() {
        List<Jobs> jobs = this.jobsMapper.selectList(new QueryWrapper<Jobs>().isNull("delete_time").orderByDesc(Arrays.asList("sort", "id")));
        ArrayList<JobsVo> adminVoArrayList = new ArrayList<JobsVo>();
        for (Jobs job : jobs) {
            JobsVo vo = new JobsVo();
            BeanUtils.copyProperties((Object)job, (Object)vo);
            vo.setCreateTime(TimeUtils.timestampToDate((Long)job.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)job.getUpdateTime()));
            adminVoArrayList.add(vo);
        }
        return adminVoArrayList;
    }

    public PageResult<JobsVo> list(PageValidate pageValidate, JobsSearchValidate searchValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        QueryWrapper<Jobs> queryWrapper = new QueryWrapper<Jobs>();
        queryWrapper.select(Jobs.class, info -> !info.getColumn().equals("delete_time")).isNull("delete_time").orderByDesc(Arrays.asList("sort", "id"));
        this.jobsMapper.setSearch(queryWrapper, (Object)searchValidate, new String[]{"like:code:str", "like:name:str", "=:status@status:int"});
        IPage<Jobs> iPage = this.jobsMapper.selectPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), (Wrapper)queryWrapper);
        ArrayList<JobsVo> list = new ArrayList<JobsVo>();
        for (Jobs job : iPage.getRecords()) {
            JobsVo vo = new JobsVo();
            BeanUtils.copyProperties((Object)job, (Object)vo);
            vo.setStatusDesc(job.getStatus() != null && job.getStatus() == 1 ? "\u6b63\u5e38" : "\u505c\u7528");
            vo.setCreateTime(TimeUtils.timestampToDate((Long)job.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)job.getUpdateTime()));
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public JobsVo detail(Integer id) {
        Jobs systemAuthPost = (Jobs)this.jobsMapper.selectOne(new QueryWrapper<Jobs>().select(Jobs.class, info -> !info.getColumn().equals("delete_time")).eq("id", (Object)id).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)systemAuthPost, (String)"\u5c97\u4f4d\u4e0d\u5b58\u5728", (Object[])new Object[0]);
        JobsVo vo = new JobsVo();
        BeanUtils.copyProperties((Object)systemAuthPost, (Object)vo);
        vo.setCreateTime(TimeUtils.timestampToDate((Long)systemAuthPost.getCreateTime()));
        vo.setUpdateTime(TimeUtils.timestampToDate((Long)systemAuthPost.getUpdateTime()));
        return vo;
    }

    public void add(JobsCreateValidate createValidate) {
        Assert.isNull(this.jobsMapper.selectOne(new QueryWrapper<Jobs>().select(new String[]{"id,code,name"}).nested(wq -> wq.eq("code", (Object)createValidate.getCode()).or().eq("name", (Object)createValidate.getName())).isNull("delete_time").last("limit 1")), (String)"\u8be5\u5c97\u4f4d\u5df2\u5b58\u5728", (Object[])new Object[0]);
        Jobs model = new Jobs();
        model.setCode(createValidate.getCode());
        model.setName(createValidate.getName());
        model.setSort(createValidate.getSort());
        model.setRemark(createValidate.getRemark());
        model.setStatus(createValidate.getStatus());
        model.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.jobsMapper.insert(model);
    }

    public void edit(JobsUpdateValidate updateValidate) {
        Jobs model = (Jobs)this.jobsMapper.selectOne(new QueryWrapper<Jobs>().select(Jobs.class, info -> !info.getColumn().equals("delete_time")).eq("id", (Object)updateValidate.getId()).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)model, (String)"\u5c97\u4f4d\u4e0d\u5b58\u5728", (Object[])new Object[0]);
        Assert.isNull(this.jobsMapper.selectOne(new QueryWrapper<Jobs>().select(new String[]{"id,code,name"}).ne("id", (Object)updateValidate.getId()).nested(wq -> wq.eq("code", (Object)updateValidate.getCode()).or().eq("name", (Object)updateValidate.getName())).isNull("delete_time").last("limit 1")), (String)"\u8be5\u5c97\u4f4d\u5df2\u5b58\u5728", (Object[])new Object[0]);
        model.setCode(updateValidate.getCode());
        model.setName(updateValidate.getName());
        model.setSort(updateValidate.getSort());
        model.setRemark(updateValidate.getRemark());
        model.setStatus(updateValidate.getStatus());
        model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.jobsMapper.updateById(model);
    }

    public void del(Integer id) {
        Jobs model = (Jobs)this.jobsMapper.selectOne(new QueryWrapper<Jobs>().select(new String[]{"id,code,name"}).eq("id", (Object)id).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)model, (String)"\u5c97\u4f4d\u4e0d\u5b58\u5728", (Object[])new Object[0]);
        List<Admin> admins = this.adminMapper.getByJobs(id);
        Assert.isTrue((boolean)admins.isEmpty(), (String)"\u8be5\u5c97\u4f4d\u5df2\u88ab\u7ba1\u7406\u5458\u4f7f\u7528,\u8bf7\u5148\u79fb\u9664", (Object[])new Object[0]);
        model.setDeleteTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.jobsMapper.updateById(model);
    }

    public JSONObject getExportData(PageValidate pageValidate, JobsSearchValidate searchValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        PageResult userVoPageResult = this.list(pageValidate, searchValidate);
        JSONObject ret = ToolUtils.getExportData((Long)userVoPageResult.getCount(), (Integer)limit, (Integer)searchValidate.getPage_start(), (Integer)searchValidate.getPage_end(), (String)"\u5c97\u4f4d\u8bb0\u5f55\u5217\u8868");
        return ret;
    }

    public String export(JobsSearchValidate searchValidate) {
        PageValidate pageValidate = new PageValidate();
        if (StringUtils.isNotNull(searchValidate.getPage_start())) {
            pageValidate.setPage_no(searchValidate.getPage_start());
        } else {
            pageValidate.setPage_no(Integer.valueOf(1));
        }
        if (StringUtils.isNotNull(searchValidate.getPage_end()) && StringUtils.isNotNull(searchValidate.getPage_size())) {
            pageValidate.setPage_size(Integer.valueOf(searchValidate.getPage_end() * searchValidate.getPage_size()));
        } else {
            pageValidate.setPage_size(Integer.valueOf(20));
        }
        Boolean isAll = StringUtils.isNull(searchValidate.getPage_type()) || searchValidate.getPage_type().equals(0);
        List excellist = this.getExcellist(isAll.booleanValue(), pageValidate, searchValidate);
        String fileName = ToolUtils.safeExportFileName(searchValidate.getFile_name());
        String folderPath = "/excel/export/" + TimeUtils.timestampToDay((Long)(System.currentTimeMillis() / 1000L)) + "/";
        String path = folderPath + fileName + ".xlsx";
        String filePath = YmlUtils.get((String)"like.upload-directory") + path;
        File folder = new File(YmlUtils.get((String)"like.upload-directory") + folderPath);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new OperateException("\u521b\u5efa\u6587\u4ef6\u5939\u5931\u8d25");
        }
        ((ExcelWriterBuilder)EasyExcel.write((String)filePath).head(JobsExportVo.class)).excelType(ExcelTypeEnum.XLSX).sheet("\u7528\u6237\u8bb0\u5f55").doWrite((Collection)excellist);
        return UrlUtils.toAdminAbsoluteUrl((String)path);
    }

    private List<JobsExportVo> getExcellist(boolean isAll, PageValidate pageValidate, JobsSearchValidate searchValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        QueryWrapper<Jobs> queryWrapper = new QueryWrapper<Jobs>();
        queryWrapper.select(Jobs.class, info -> !info.getColumn().equals("delete_time")).isNull("delete_time").orderByDesc(Arrays.asList("sort", "id"));
        this.jobsMapper.setSearch(queryWrapper, (Object)searchValidate, new String[]{"like:code:str", "like:name:str", "=:status@status:int"});
        List<Jobs> jobsList = new ArrayList();
        if (isAll) {
            jobsList = this.jobsMapper.selectList((Wrapper)queryWrapper);
        } else {
            IPage<Jobs> iPage = this.jobsMapper.selectPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), (Wrapper)queryWrapper);
            jobsList = iPage.getRecords();
        }
        ArrayList<JobsExportVo> list = new ArrayList<JobsExportVo>();
        for (Jobs job : jobsList) {
            JobsExportVo vo = new JobsExportVo();
            BeanUtils.copyProperties((Object)job, (Object)vo);
            vo.setStatusDesc(job.getStatus() != null && job.getStatus() == 1 ? "\u6b63\u5e38" : "\u505c\u7528");
            vo.setCreateTime(TimeUtils.timestampToDate((Long)job.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)job.getUpdateTime()));
            list.add(vo);
        }
        return list;
    }
}

