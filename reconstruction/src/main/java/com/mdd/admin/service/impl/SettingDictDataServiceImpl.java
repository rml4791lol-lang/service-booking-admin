/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.service.ISettingDictDataService
 *  com.mdd.admin.service.impl.SettingDictDataServiceImpl
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.setting.DictDataCreateValidate
 *  com.mdd.admin.validate.setting.DictDataUpdateValidate
 *  com.mdd.admin.vo.setting.SettingDictDataVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.setting.DictData
 *  com.mdd.common.entity.setting.DictType
 *  com.mdd.common.mapper.setting.DictDataMapper
 *  com.mdd.common.mapper.setting.DictTypeMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.ISettingDictDataService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.setting.DictDataCreateValidate;
import com.mdd.admin.validate.setting.DictDataUpdateValidate;
import com.mdd.admin.vo.setting.SettingDictDataVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.setting.DictData;
import com.mdd.common.entity.setting.DictType;
import com.mdd.common.mapper.setting.DictDataMapper;
import com.mdd.common.mapper.setting.DictTypeMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class SettingDictDataServiceImpl
implements ISettingDictDataService {
    @Resource
    DictDataMapper dictDataMapper;
    @Resource
    DictTypeMapper dictTypeMapper;

    public List<SettingDictDataVo> all(Map<String, String> params) {
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<DictData>();
        queryWrapper.select(new String[]{"id,type_id,name,value,type_value,remark,sort,status,create_time,update_time"});
        queryWrapper.isNull("delete_time");
        if (StringUtils.isNotEmpty((String)params.get("dictType"))) {
            queryWrapper.eq("type_value", (Object)params.get("dictType"));
        }
        queryWrapper.orderByDesc("sort", "id");
        this.dictDataMapper.setSearch(queryWrapper, params, new String[]{"like:name:str", "like:value:str", "=:status:int"});
        List<DictData> dictDataList = this.dictDataMapper.selectList((Wrapper)queryWrapper);
        LinkedList<SettingDictDataVo> list = new LinkedList<SettingDictDataVo>();
        for (DictData dictData : dictDataList) {
            SettingDictDataVo vo = new SettingDictDataVo();
            BeanUtils.copyProperties((Object)dictData, (Object)vo);
            vo.setCreateTime(TimeUtils.timestampToDate((Long)dictData.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)dictData.getUpdateTime()));
            list.add(vo);
        }
        return list;
    }

    public PageResult<SettingDictDataVo> list(PageValidate pageValidate, Map<String, String> params) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<DictData>();
        queryWrapper.select(new String[]{"id,type_id,name,type_value,value,remark,sort,status,create_time,update_time"});
        queryWrapper.isNull("delete_time");
        queryWrapper.orderByDesc("id");
        if (StringUtils.isNotEmpty((String)params.get("type_id"))) {
            DictType dictType = (DictType)this.dictTypeMapper.selectOne(new QueryWrapper<DictType>().isNull("delete_time").eq("id", (Object)params.get("type_id")).last("limit 1"));
            Assert.notNull((Object)dictType, (String)"\u8be5\u5b57\u5178\u7c7b\u578b\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
            queryWrapper.eq("type_id", (Object)dictType.getId());
        }
        this.dictDataMapper.setSearch(queryWrapper, params, new String[]{"like:name:str", "like:value:str", "=:status:int"});
        IPage<DictData> iPage = this.dictDataMapper.selectPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), (Wrapper)queryWrapper);
        LinkedList<SettingDictDataVo> list = new LinkedList<SettingDictDataVo>();
        for (DictData dictData : iPage.getRecords()) {
            SettingDictDataVo vo = new SettingDictDataVo();
            BeanUtils.copyProperties((Object)dictData, (Object)vo);
            vo.setCreateTime(TimeUtils.timestampToDate((Long)dictData.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)dictData.getUpdateTime()));
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public SettingDictDataVo detail(Integer id) {
        DictData dictData = (DictData)this.dictDataMapper.selectOne(new QueryWrapper<DictData>().select(new String[]{"id,type_id,name,value,remark,sort,status,create_time,update_time"}).eq("id", (Object)id).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)dictData, (String)"\u5b57\u5178\u6570\u636e\u4e0d\u5b58\u5728\uff01", (Object[])new Object[0]);
        SettingDictDataVo vo = new SettingDictDataVo();
        BeanUtils.copyProperties((Object)dictData, (Object)vo);
        vo.setCreateTime(TimeUtils.timestampToDate((Long)dictData.getCreateTime()));
        vo.setUpdateTime(TimeUtils.timestampToDate((Long)dictData.getUpdateTime()));
        return vo;
    }

    public void add(DictDataCreateValidate createValidate) {
        Assert.isNull(this.dictDataMapper.selectOne(new QueryWrapper<DictData>().select(new String[]{"id"}).eq("name", (Object)createValidate.getName()).isNull("delete_time").last("limit 1")), (String)"\u5b57\u5178\u6570\u636e\u5df2\u5b58\u5728\uff01", (Object[])new Object[0]);
        DictData model = new DictData();
        model.setTypeId(createValidate.getTypeId());
        model.setName(createValidate.getName());
        model.setValue(createValidate.getValue());
        model.setTypeValue(createValidate.getTypeValue());
        model.setRemark(createValidate.getRemark());
        model.setSort(createValidate.getSort());
        model.setStatus(createValidate.getStatus());
        model.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.dictDataMapper.insert(model);
    }

    public void edit(DictDataUpdateValidate updateValidate) {
        DictData model = (DictData)this.dictDataMapper.selectOne(new QueryWrapper<DictData>().eq("id", (Object)updateValidate.getId()).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)model, (String)"\u5b57\u5178\u6570\u636e\u4e0d\u5b58\u5728\uff01", (Object[])new Object[0]);
        Assert.isNull(this.dictDataMapper.selectOne(new QueryWrapper<DictData>().select(new String[]{"id"}).ne("id", (Object)updateValidate.getId()).eq("name", (Object)updateValidate.getName()).isNull("delete_time").last("limit 1")), (String)"\u5b57\u5178\u6570\u636e\u5df2\u5b58\u5728\uff01", (Object[])new Object[0]);
        model.setName(updateValidate.getName());
        model.setValue(updateValidate.getValue());
        model.setTypeValue(updateValidate.getTypeValue());
        model.setRemark(updateValidate.getRemark());
        model.setSort(updateValidate.getSort());
        model.setStatus(updateValidate.getStatus());
        model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.dictDataMapper.updateById(model);
    }

    public void del(List<Integer> ids) {
        for (Integer id : ids) {
            DictData model = new DictData();
            model.setId(id);
            model.setDeleteTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.dictDataMapper.updateById(model);
        }
    }
}

