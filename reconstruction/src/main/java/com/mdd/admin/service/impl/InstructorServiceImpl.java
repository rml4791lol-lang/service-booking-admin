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
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.service.IInstructorService
 *  com.mdd.admin.service.impl.InstructorServiceImpl
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.instructor.InstructorCreateValidate
 *  com.mdd.admin.validate.instructor.InstructorSearchValidate
 *  com.mdd.admin.validate.instructor.InstructorUpdateValidate
 *  com.mdd.admin.vo.instructor.InstructorDetailVo
 *  com.mdd.admin.vo.instructor.InstructorExportVo
 *  com.mdd.admin.vo.instructor.InstructorListedVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.Instructor
 *  com.mdd.common.entity.user.User
 *  com.mdd.common.exception.OperateException
 *  com.mdd.common.mapper.InstructorMapper
 *  com.mdd.common.mapper.user.UserMapper
 *  com.mdd.common.util.MessageUtil
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.ToolUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.common.util.YmlUtils
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 *  org.springframework.util.Assert
 */
package com.mdd.admin.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.IInstructorService;
import com.mdd.admin.export.ExportFileService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.instructor.InstructorCreateValidate;
import com.mdd.admin.validate.instructor.InstructorSearchValidate;
import com.mdd.admin.validate.instructor.InstructorUpdateValidate;
import com.mdd.admin.vo.instructor.InstructorDetailVo;
import com.mdd.admin.vo.instructor.InstructorExportVo;
import com.mdd.admin.vo.instructor.InstructorListedVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.Instructor;
import com.mdd.common.entity.community.Community;
import com.mdd.common.entity.course.SportsCategory;
import com.mdd.common.entity.user.User;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.InstructorMapper;
import com.mdd.common.mapper.community.CommunityMapper;
import com.mdd.common.mapper.course.SportsCategoryMapper;
import com.mdd.common.mapper.user.UserMapper;
import com.mdd.common.util.MessageUtil;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.YmlUtils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class InstructorServiceImpl
implements IInstructorService {
    @Resource
    InstructorMapper instructorMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    SportsCategoryMapper sportsCategoryMapper;
    @Resource
    CommunityMapper communityMapper;
    @Resource
    com.mdd.admin.service.IInstructorAccountService instructorAccountService;
    @Resource
    ExportFileService exportFileService;

    public PageResult<InstructorListedVo> list(PageValidate pageValidate, InstructorSearchValidate searchValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        QueryWrapper<Instructor> queryWrapper = new QueryWrapper<Instructor>();
        queryWrapper.orderByDesc(Arrays.asList("sort", "id"));
        this.instructorMapper.setSearch(queryWrapper, (Object)searchValidate, new String[]{"like:name:str", "=:phone:str", "=:specialty:str", "=:status:int"});
        IPage<Instructor> iPage = this.instructorMapper.selectPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), (Wrapper)queryWrapper);
        LinkedList<InstructorListedVo> list = new LinkedList<InstructorListedVo>();
        for (Instructor item : iPage.getRecords()) {
            InstructorListedVo vo = new InstructorListedVo();
            BeanUtils.copyProperties((Object)item, (Object)vo);
            vo.setAvatar(UrlUtils.toAdminAbsoluteUrl((String)item.getAvatar()));
            vo.setCreateTime(item.getCreateTime() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.getCreateTime()) : "");
            vo.setUpdateTime(item.getUpdateTime() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.getUpdateTime()) : "");
            com.mdd.admin.vo.instructor.InstructorAccountVo account = this.instructorAccountService.status(item.getId());
            vo.setUserId(account.getUserId());
            vo.setAccount(account.getAccount());
            vo.setAccountLinked(account.getAccountLinked());
            vo.setHasPassword(account.getHasPassword());
            vo.setMustChangePassword(account.getMustChangePassword());
            vo.setAccountStatus(account.getAccountStatus());
            vo.setAccountStatusLabel(account.getAccountStatusLabel());
            vo.setLastLoginTime(account.getLastLoginTime());
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public List<Map<String, Object>> listAllForSelect() {
        List<Instructor> instructors = this.instructorMapper.selectList(new QueryWrapper<Instructor>().isNull("delete_time").orderByDesc(Arrays.asList("sort", "id")));
        LinkedList<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
        for (Instructor item : instructors) {
            LinkedHashMap<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("id", item.getId());
            row.put("name", item.getName());
            row.put("phone", item.getPhone());
            row.put("status", item.getStatus());
            row.put("statusLabel", this.getStatusLabel(item.getStatus()));
            row.put("hourlyRate", item.getHourlyRate());
            list.add(row);
        }
        return list;
    }

    public Map<String, Object> options() {
        LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
        LinkedList<Map<String, Object>> categories = new LinkedList<Map<String, Object>>();
        List<SportsCategory> categoryList = this.sportsCategoryMapper.selectList(
                new QueryWrapper<SportsCategory>().eq("is_show", 1).orderByAsc(Arrays.asList("sort", "id")));
        for (SportsCategory item : categoryList) {
            LinkedHashMap<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("id", item.getId());
            row.put("name", item.getName());
            row.put("parentId", item.getParentId());
            categories.add(row);
        }
        LinkedList<Map<String, Object>> communities = new LinkedList<Map<String, Object>>();
        List<Community> communityList = this.communityMapper.selectList(
                new QueryWrapper<Community>().isNull("delete_time").orderByAsc("id"));
        for (Community item : communityList) {
            LinkedHashMap<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("id", item.getId());
            row.put("name", item.getName());
            communities.add(row);
        }
        data.put("categories", categories);
        data.put("communities", communities);
        return data;
    }

    private String getStatusLabel(Integer status) {
        if (status == null) {
            return "\u672a\u77e5";
        }
        switch (status) {
            case 1: {
                return "\u6b63\u5e38";
            }
            case 2: {
                return "\u5f85\u9a8c\u8bc1";
            }
        }
        return "\u5df2\u505c\u7528";
    }

    public InstructorDetailVo detail(Integer id) {
        Instructor model = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>().eq("id", (Object)id).last("limit 1"));
        Assert.notNull((Object)model, (String)"\u6570\u636e\u4e0d\u5b58\u5728");
        InstructorDetailVo vo = new InstructorDetailVo();
        BeanUtils.copyProperties((Object)model, (Object)vo);
        vo.setAvatar(UrlUtils.toAdminAbsoluteUrl((String)model.getAvatar()));
        vo.setCertImages(this.mapCertImages(model.getCertImages(), true));
        return vo;
    }

    /**
     * 证书图片入库统一存相对路径，详情统一输出可直接访问的绝对地址。
     */
    private String mapCertImages(String certImages, boolean absolute) {
        if (certImages == null || certImages.trim().isEmpty()) {
            return "";
        }
        LinkedList<String> urls = new LinkedList<String>();
        for (String item : certImages.split(",")) {
            String url = item.trim();
            if (url.isEmpty()) {
                continue;
            }
            urls.add(absolute ? UrlUtils.toAdminAbsoluteUrl(url) : UrlUtils.toRelativeUrl(url));
        }
        return String.join(",", urls);
    }

    public void add(InstructorCreateValidate createValidate) {
        Instructor model = new Instructor();
        model.setName(createValidate.getName());
        model.setPassword(ToolUtils.makePassword((String)createValidate.getPassword()));
        model.setAvatar(UrlUtils.toRelativeUrl((String)createValidate.getAvatar()));
        model.setPhone(createValidate.getPhone());
        model.setGender(createValidate.getGender());
        model.setSpecialty(createValidate.getSpecialty());
        model.setExperienceYears(createValidate.getExperienceYears());
        model.setIntroduction(createValidate.getIntroduction());
        model.setIdCard(createValidate.getIdCard());
        model.setCertImages(this.mapCertImages(createValidate.getCertImages(), false));
        model.setCertification(createValidate.getCertification());
        model.setTeachableCourses(createValidate.getTeachableCourses());
        model.setServiceAreas(createValidate.getServiceAreas());
        model.setClassCount(createValidate.getClassCount());
        model.setHourlyRate(createValidate.getHourlyRate());
        model.setRatingScore(createValidate.getRatingScore());
        model.setStatus(createValidate.getStatus());
        model.setSort(createValidate.getSort());
        model.setCreateTime(new Date());
        model.setUpdateTime(new Date());
        this.instructorMapper.insert(model);
    }

    public void edit(InstructorUpdateValidate updateValidate) {
        User user;
        Instructor model = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>().eq("id", (Object)updateValidate.getId()).last("limit 1"));
        Assert.notNull((Object)model, (String)"\u6570\u636e\u4e0d\u5b58\u5728!");
        Integer oldStatus = model.getStatus();
        model.setId(updateValidate.getId());
        model.setName(updateValidate.getName());
        if (StringUtils.isNotNull(updateValidate.getPassword()) && StringUtils.isNotEmpty((String)updateValidate.getPassword())) {
            model.setPassword(ToolUtils.makePassword((String)updateValidate.getPassword()));
        }
        model.setAvatar(UrlUtils.toRelativeUrl((String)updateValidate.getAvatar()));
        model.setPhone(updateValidate.getPhone());
        model.setGender(updateValidate.getGender());
        model.setSpecialty(updateValidate.getSpecialty());
        model.setExperienceYears(updateValidate.getExperienceYears());
        model.setIntroduction(updateValidate.getIntroduction());
        model.setIdCard(updateValidate.getIdCard());
        model.setCertImages(this.mapCertImages(updateValidate.getCertImages(), false));
        model.setCertification(updateValidate.getCertification());
        model.setTeachableCourses(updateValidate.getTeachableCourses());
        model.setServiceAreas(updateValidate.getServiceAreas());
        model.setClassCount(updateValidate.getClassCount());
        model.setHourlyRate(updateValidate.getHourlyRate());
        model.setRatingScore(updateValidate.getRatingScore());
        model.setStatus(updateValidate.getStatus());
        model.setSort(updateValidate.getSort());
        model.setUpdateTime(new Date());
        this.instructorMapper.updateById(model);
        if (oldStatus != null && oldStatus == 2 && updateValidate.getStatus() != 2 && model.getPhone() != null && !model.getPhone().isEmpty() && (user = (User)this.userMapper.selectOne(new QueryWrapper<User>().eq("mobile", (Object)model.getPhone()).last("LIMIT 1"))) != null) {
            String title = updateValidate.getStatus() == 1 ? "\u6559\u7ec3\u7533\u8bf7\u5df2\u901a\u8fc7" : "\u6559\u7ec3\u7533\u8bf7\u5df2\u88ab\u9a73\u56de";
            String content = updateValidate.getStatus() == 1 ? "\u606d\u559c\u60a8\uff0c\u60a8\u7684\u6559\u7ec3\u5165\u9a7b\u7533\u8bf7\u5df2\u901a\u8fc7\u5ba1\u6838\uff01" : "\u5f88\u62b1\u6b49\uff0c\u60a8\u7684\u6559\u7ec3\u5165\u9a7b\u7533\u8bf7\u5df2\u88ab\u9a73\u56de\u3002";
            MessageUtil.send((int)4, (String)title, (String)content, (Integer)0, (String)"system", (String)"\u7cfb\u7edf\u901a\u77e5", (Integer)user.getId(), (String)"user");
        }
    }

    public void del(Integer id) {
        Instructor model = (Instructor)this.instructorMapper.selectOne(new QueryWrapper<Instructor>().eq("id", (Object)id).last("limit 1"));
        Assert.notNull((Object)model, (String)"\u6570\u636e\u4e0d\u5b58\u5728!");
        this.instructorMapper.delete(new QueryWrapper<Instructor>().eq("id", (Object)id));
    }

    public JSONObject getExportData(PageValidate pageValidate, InstructorSearchValidate searchValidate) {
        Integer limit = pageValidate.getPage_size();
        PageResult pageResult = this.list(pageValidate, searchValidate);
        return ToolUtils.getExportData((Long)pageResult.getCount(), (Integer)limit, (Integer)searchValidate.getPage_start(), (Integer)searchValidate.getPage_end(), (String)"\u6559\u7ec3\u5217\u8868");
    }

    public Map<String, Object> export(InstructorSearchValidate searchValidate) {
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
        boolean isAll = StringUtils.isNull(searchValidate.getPage_type()) || searchValidate.getPage_type().equals(0);
        List exportList = this.getExcellist(isAll, pageValidate, searchValidate);
        String fileName = ToolUtils.safeExportFileName(searchValidate.getFile_name());
        File target = exportFileService.allocate(fileName);
        ((ExcelWriterBuilder)EasyExcel.write((String)target.getAbsolutePath()).head(InstructorExportVo.class)).excelType(ExcelTypeEnum.XLSX).sheet("\u6559\u7ec3\u8bb0\u5f55").doWrite((Collection)exportList);
        return exportFileService.issue(target, fileName + ".xlsx");
    }

    private List<InstructorExportVo> getExcellist(boolean isAll, PageValidate pageValidate, InstructorSearchValidate searchValidate) {
        List<Instructor> list;
        QueryWrapper<Instructor> queryWrapper = new QueryWrapper<Instructor>();
        queryWrapper.orderByDesc(Arrays.asList("sort", "id"));
        this.instructorMapper.setSearch(queryWrapper, (Object)searchValidate, new String[]{"like:name:str", "=:phone:str", "=:specialty:str"});
        if (isAll) {
            list = this.instructorMapper.selectList((Wrapper)queryWrapper);
        } else {
            IPage<Instructor> iPage = this.instructorMapper.selectPage((IPage)new Page((long)pageValidate.getPage_no().intValue(), (long)pageValidate.getPage_size().intValue()), (Wrapper)queryWrapper);
            list = iPage.getRecords();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ArrayList<InstructorExportVo> exportList = new ArrayList<InstructorExportVo>();
        for (Instructor item : list) {
            InstructorExportVo vo = new InstructorExportVo();
            BeanUtils.copyProperties((Object)item, (Object)vo);
            String statusDesc = "\u672a\u77e5";
            if (item.getStatus() != null) {
                switch (item.getStatus()) {
                    case 0: {
                        statusDesc = "\u5df2\u505c\u7528";
                        break;
                    }
                    case 1: {
                        statusDesc = "\u6b63\u5e38";
                        break;
                    }
                    case 2: {
                        statusDesc = "\u5f85\u5ba1\u6838";
                    }
                }
            }
            vo.setStatusDesc(statusDesc);
            vo.setCreateTime(item.getCreateTime() != null ? sdf.format(item.getCreateTime()) : "");
            exportList.add(vo);
        }
        return exportList;
    }
}

