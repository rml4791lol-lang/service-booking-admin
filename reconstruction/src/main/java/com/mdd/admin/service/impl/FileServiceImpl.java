/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONArray
 *  com.alibaba.fastjson2.JSONObject
 *  com.alibaba.fastjson2.JSONReader$Feature
 *  com.alibaba.fastjson2.JSONWriter$Feature
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.service.IFileService
 *  com.mdd.admin.service.impl.FileServiceImpl
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.file.FileCateValidate
 *  com.mdd.admin.validate.file.FileSearchValidate
 *  com.mdd.admin.vo.album.FileCateVo
 *  com.mdd.admin.vo.album.FileVo
 *  com.mdd.common.config.GlobalConfig
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.file.File
 *  com.mdd.common.entity.file.FileCate
 *  com.mdd.common.mapper.album.FileCateMapper
 *  com.mdd.common.mapper.album.FileMapper
 *  com.mdd.common.plugin.storage.StorageDriver
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.ListUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.IFileService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.file.FileCateValidate;
import com.mdd.admin.validate.file.FileSearchValidate;
import com.mdd.admin.vo.album.FileCateVo;
import com.mdd.admin.vo.album.FileVo;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.file.File;
import com.mdd.common.entity.file.FileCate;
import com.mdd.common.mapper.album.FileCateMapper;
import com.mdd.common.mapper.album.FileMapper;
import com.mdd.common.plugin.storage.StorageDriver;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.ListUtils;
import com.mdd.common.util.ParamUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl
implements IFileService {
    @Resource
    FileMapper fileMapper;
    @Resource
    FileCateMapper fileCateMapper;

    public PageResult<FileVo> fileList(PageValidate pageValidate, FileSearchValidate searchValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        QueryWrapper<File> queryWrapper = new QueryWrapper<File>();
        queryWrapper.select(File.class, info -> !info.getColumn().equals("aid") && !info.getColumn().equals("uid") && !info.getColumn().equals("delete_time")).isNull("delete_time").orderByDesc("id");
        if (StringUtils.isNotNull(searchValidate.getCid())) {
            queryWrapper.eq("cid", (Object)searchValidate.getCid());
        }
        this.fileMapper.setSearch(queryWrapper, (Object)searchValidate, new String[]{"=:type:int", "like:name@name:str", "=:source:int"});
        IPage<File> iPage = this.fileMapper.selectPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), (Wrapper)queryWrapper);
        String engine = ConfigUtils.get((String)"storage", (String)"default", (String)"local");
        engine = engine.equals("") ? "local" : engine;
        ArrayList<FileVo> list = new ArrayList<FileVo>();
        for (File file : iPage.getRecords()) {
            FileVo vo = new FileVo();
            BeanUtils.copyProperties((Object)file, (Object)vo);
            vo.setUrl(UrlUtils.toAdminAbsoluteUrl((String)file.getUri()));
            if (engine.equals("local")) {
                vo.setUri(GlobalConfig.adminPublicPrefix + "/" + file.getUri());
            } else {
                vo.setUri(file.getUri());
            }
            vo.setCreateTime(TimeUtils.timestampToDate((Long)file.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)file.getUpdateTime()));
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public void fileRename(Integer id, String name) {
        File file = (File)this.fileMapper.selectOne(new QueryWrapper<File>().select(new String[]{"id", "name"}).eq("id", (Object)id).isNull("delete_time"));
        Assert.notNull((Object)file, (String)"\u6587\u4ef6\u4e22\u5931\uff01", (Object[])new Object[0]);
        file.setName(name);
        file.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.fileMapper.updateById(file);
    }

    public void fileMove(List<Integer> ids, Integer cid) {
        List<File> files = this.fileMapper.selectList(new QueryWrapper<File>().select(new String[]{"id", "name"}).in("id", ids).isNull("delete_time"));
        Assert.notNull((Object)files, (String)"\u6587\u4ef6\u4e22\u5931\uff01", (Object[])new Object[0]);
        if (cid > 0) {
            Assert.notNull((Object)this.fileCateMapper.selectOne(new QueryWrapper<FileCate>().eq("id", (Object)cid).isNull("delete_time")), (String)"\u7c7b\u76ee\u5df2\u4e0d\u5b58\u5728\uff01", (Object[])new Object[0]);
        }
        for (File file : files) {
            file.setCid(cid);
            file.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.fileMapper.updateById(file);
        }
    }

    public Integer fileAdd(Map<String, String> params) {
        String name = params.get("name");
        if (name == null) {
            name = "";
        }
        if (name.length() >= 100) {
            name = name.substring(0, 99);
        }
        File album = new File();
        album.setCid(ParamUtils.intOrDefault(params.get("cid"), 0, "文件分类ID"));
        album.setSourceId(ParamUtils.intOrDefault(params.get("uid"), 0, "上传用户ID"));
        album.setType(ParamUtils.intOrDefault(params.get("type"), 0, "文件类型"));
        album.setName(name);
        album.setUri(params.get("url"));
        album.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        album.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.fileMapper.insert(album);
        return album.getId();
    }

    public void fileDel(List<Integer> ids) {
        List<File> files = this.fileMapper.selectList(new QueryWrapper<File>().in("id", ids).isNull("delete_time"));
        Assert.notNull((Object)files, (String)"\u6587\u4ef6\u4e22\u5931\uff01", (Object[])new Object[0]);
        for (File file : files) {
            StorageDriver driver = new StorageDriver();
            driver.deleteFile(file.getUri());
            file.setDeleteTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.fileMapper.updateById(file);
        }
    }

    public JSONObject cateList(FileSearchValidate searchValidate) {
        QueryWrapper<FileCate> queryWrapper = new QueryWrapper<FileCate>();
        queryWrapper.select(FileCate.class, info -> !info.getColumn().equals("delete_time")).isNull("delete_time").orderByDesc("id");
        if (StringUtils.isNotNull(searchValidate.getType()) && searchValidate.getType() > 0) {
            queryWrapper.eq("type", (Object)searchValidate.getType());
        }
        if (StringUtils.isNotNull(searchValidate.getName()) && StringUtils.isNotEmpty((String)searchValidate.getName())) {
            queryWrapper.like("name", (Object)searchValidate.getName());
        }
        List<FileCate> fileCateList = this.fileCateMapper.selectList((Wrapper)queryWrapper);
        LinkedList<FileCateVo> lists = new LinkedList<FileCateVo>();
        for (FileCate fileCate : fileCateList) {
            FileCateVo vo = new FileCateVo();
            BeanUtils.copyProperties((Object)fileCate, (Object)vo);
            vo.setCreateTime(TimeUtils.timestampToDate((Long)fileCate.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)fileCate.getUpdateTime()));
            lists.add(vo);
        }
        JSONArray jsonArray = JSONArray.parseArray((String)JSONArray.toJSONString(lists, (JSONWriter.Feature[])new JSONWriter.Feature[0]), (JSONReader.Feature[])new JSONReader.Feature[0]);
        JSONObject result = new JSONObject();
        result.put("lists", ListUtils.listToTree((JSONArray)jsonArray, "id", "pid", "children"));
        return result;
    }

    public void cateAdd(FileCateValidate cateValidate) {
        FileCate albumCate = new FileCate();
        albumCate.setType(cateValidate.getType());
        albumCate.setPid(cateValidate.getPid());
        albumCate.setName(cateValidate.getName());
        albumCate.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        albumCate.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.fileCateMapper.insert(albumCate);
    }

    public void cateRename(Integer id, String name) {
        FileCate fileCate = (FileCate)this.fileCateMapper.selectOne(new QueryWrapper<FileCate>().select(new String[]{"id", "name"}).eq("id", (Object)id).isNull("delete_time"));
        Assert.notNull((Object)fileCate, (String)"\u5206\u7c7b\u5df2\u4e0d\u5b58\u5728\uff01", (Object[])new Object[0]);
        fileCate.setName(name);
        fileCate.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.fileCateMapper.updateById(fileCate);
    }

    public void cateDel(Integer id) {
        FileCate albumCate = (FileCate)this.fileCateMapper.selectOne(new QueryWrapper<FileCate>().select(new String[]{"id", "name"}).eq("id", (Object)id).isNull("delete_time"));
        Assert.notNull((Object)albumCate, (String)"\u5206\u7c7b\u5df2\u4e0d\u5b58\u5728\uff01", (Object[])new Object[0]);
        Assert.isNull(this.fileMapper.selectOne(new QueryWrapper<File>().select(new String[]{"id", "cid", "name"}).eq("cid", (Object)id).isNull("delete_time").last("limit 1")), (String)"\u5f53\u524d\u5206\u7c7b\u6b63\u88ab\u4f7f\u7528\u4e2d,\u4e0d\u80fd\u5220\u9664\uff01", (Object[])new Object[0]);
        albumCate.setDeleteTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.fileCateMapper.updateById(albumCate);
    }
}

