/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.service.ISettingDictTypeService
 *  com.mdd.admin.service.impl.SettingDictTypeServiceImpl
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.setting.DictTypeCreateValidate
 *  com.mdd.admin.validate.setting.DictTypeUpdateValidate
 *  com.mdd.admin.vo.setting.SettingDictTypeVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.setting.DictType
 *  com.mdd.common.mapper.setting.DictTypeMapper
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
import com.mdd.admin.service.ISettingDictTypeService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.setting.DictTypeCreateValidate;
import com.mdd.admin.validate.setting.DictTypeUpdateValidate;
import com.mdd.admin.vo.setting.SettingDictTypeVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.setting.DictType;
import com.mdd.common.mapper.setting.DictTypeMapper;
import com.mdd.common.util.TimeUtils;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class SettingDictTypeServiceImpl
implements ISettingDictTypeService {
    @Resource
    DictTypeMapper dictTypeMapper;

    public List<SettingDictTypeVo> all() {
        QueryWrapper<DictType> queryWrapper = new QueryWrapper<DictType>();
        queryWrapper.select(new String[]{"id,name,type,remark,status,create_time,update_time"});
        queryWrapper.isNull("delete_time");
        queryWrapper.orderByDesc("id");
        List<DictType> dictTypeList = this.dictTypeMapper.selectList((Wrapper)queryWrapper);
        LinkedList<SettingDictTypeVo> list = new LinkedList<SettingDictTypeVo>();
        for (DictType dictType : dictTypeList) {
            SettingDictTypeVo vo = new SettingDictTypeVo();
            BeanUtils.copyProperties((Object)dictType, (Object)vo);
            vo.setCreateTime(TimeUtils.timestampToDate((Long)dictType.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)dictType.getUpdateTime()));
            list.add(vo);
        }
        return list;
    }

    public PageResult<SettingDictTypeVo> list(PageValidate pageValidate, Map<String, String> params) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        QueryWrapper<DictType> queryWrapper = new QueryWrapper<DictType>();
        queryWrapper.select(new String[]{"id,name,type,remark,status,create_time,update_time"});
        queryWrapper.isNull("delete_time");
        queryWrapper.orderByDesc("id");
        this.dictTypeMapper.setSearch(queryWrapper, params, new String[]{"like:name@name:str", "like:type@type:str", "=:status@status:int"});
        IPage<DictType> iPage = this.dictTypeMapper.selectPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), (Wrapper)queryWrapper);
        LinkedList<SettingDictTypeVo> list = new LinkedList<SettingDictTypeVo>();
        for (DictType dictType : iPage.getRecords()) {
            SettingDictTypeVo vo = new SettingDictTypeVo();
            BeanUtils.copyProperties((Object)dictType, (Object)vo);
            vo.setCreateTime(TimeUtils.timestampToDate((Long)dictType.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)dictType.getUpdateTime()));
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public SettingDictTypeVo detail(Integer id) {
        DictType dictType = (DictType)this.dictTypeMapper.selectOne(new QueryWrapper<DictType>().select(new String[]{"id,name,type,remark,status,create_time,update_time"}).eq("id", (Object)id).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)dictType, (String)"\u5b57\u5178\u7c7b\u578b\u4e0d\u5b58\u5728\uff01", (Object[])new Object[0]);
        SettingDictTypeVo vo = new SettingDictTypeVo();
        BeanUtils.copyProperties((Object)dictType, (Object)vo);
        vo.setCreateTime(TimeUtils.timestampToDate((Long)dictType.getCreateTime()));
        vo.setUpdateTime(TimeUtils.timestampToDate((Long)dictType.getUpdateTime()));
        return vo;
    }

    public void add(DictTypeCreateValidate createValidate) {
        Assert.isNull(this.dictTypeMapper.selectOne(new QueryWrapper<DictType>().select(new String[]{"id"}).eq("name", (Object)createValidate.getName()).isNull("delete_time").last("limit 1")), (String)"\u5b57\u5178\u540d\u79f0\u5df2\u5b58\u5728\uff01", (Object[])new Object[0]);
        Assert.isNull(this.dictTypeMapper.selectOne(new QueryWrapper<DictType>().select(new String[]{"id"}).eq("type", (Object)createValidate.getType()).isNull("delete_time").last("limit 1")), (String)"\u5b57\u5178\u7c7b\u578b\u5df2\u5b58\u5728\uff01", (Object[])new Object[0]);
        DictType model = new DictType();
        model.setName(createValidate.getName());
        model.setType(createValidate.getType());
        model.setRemark(createValidate.getRemark());
        model.setStatus(createValidate.getStatus());
        model.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.dictTypeMapper.insert(model);
    }

    public void edit(DictTypeUpdateValidate updateValidate) {
        DictType model = (DictType)this.dictTypeMapper.selectOne(new QueryWrapper<DictType>().eq("id", (Object)updateValidate.getId()).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)model, (String)"\u5b57\u5178\u7c7b\u578b\u4e0d\u5b58\u5728\uff01", (Object[])new Object[0]);
        Assert.isNull(this.dictTypeMapper.selectOne(new QueryWrapper<DictType>().ne("id", (Object)updateValidate.getId()).eq("name", (Object)updateValidate.getName()).isNull("delete_time").last("limit 1")), (String)"\u5b57\u5178\u7c7b\u578b\u5df2\u5b58\u5728\uff01", (Object[])new Object[0]);
        Assert.isNull(this.dictTypeMapper.selectOne(new QueryWrapper<DictType>().ne("id", (Object)updateValidate.getId()).eq("type", (Object)updateValidate.getType()).isNull("delete_time").last("limit 1")), (String)"\u5b57\u5178\u7c7b\u578b\u5df2\u5b58\u5728\uff01", (Object[])new Object[0]);
        model.setName(updateValidate.getName());
        model.setType(updateValidate.getType());
        model.setRemark(updateValidate.getRemark());
        model.setStatus(updateValidate.getStatus());
        model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.dictTypeMapper.updateById(model);
    }

    public void del(Integer id) {
        DictType model = new DictType();
        model.setId(id);
        model.setDeleteTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.dictTypeMapper.updateById(model);
    }
}

