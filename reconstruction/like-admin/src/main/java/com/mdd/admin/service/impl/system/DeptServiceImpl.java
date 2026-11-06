/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONArray
 *  com.alibaba.fastjson2.JSONReader$Feature
 *  com.alibaba.fastjson2.JSONWriter$Feature
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.mdd.admin.service.impl.system.DeptServiceImpl
 *  com.mdd.admin.service.system.IDeptService
 *  com.mdd.admin.validate.system.DeptCreateValidate
 *  com.mdd.admin.validate.system.DeptSearchValidate
 *  com.mdd.admin.validate.system.DeptUpdateValidate
 *  com.mdd.admin.vo.system.DeptVo
 *  com.mdd.common.entity.admin.Dept
 *  com.mdd.common.mapper.admin.AdminMapper
 *  com.mdd.common.mapper.admin.DeptMapper
 *  com.mdd.common.util.ListUtils
 *  com.mdd.common.util.TimeUtils
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl.system;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mdd.admin.service.system.IDeptService;
import com.mdd.admin.validate.system.DeptCreateValidate;
import com.mdd.admin.validate.system.DeptSearchValidate;
import com.mdd.admin.validate.system.DeptUpdateValidate;
import com.mdd.admin.vo.system.DeptVo;
import com.mdd.common.entity.admin.Dept;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.mapper.admin.DeptMapper;
import com.mdd.common.util.ListUtils;
import com.mdd.common.util.TimeUtils;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.mdd.common.entity.admin.Admin;

@Service
public class DeptServiceImpl
implements IDeptService {
    @Resource
    DeptMapper deptMapper;
    @Resource
    AdminMapper adminMapper;

    public JSONArray all() {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<Dept>();
        queryWrapper.isNull("delete_time");
        queryWrapper.orderByDesc(Arrays.asList("sort", "id"));
        queryWrapper.select(Dept.class, info -> !info.getColumn().equals("delete_time"));
        List<Dept> systemAuthDeptList = this.deptMapper.selectList((Wrapper)queryWrapper);
        LinkedList<DeptVo> list = new LinkedList<DeptVo>();
        for (Dept systemAuthDept : systemAuthDeptList) {
            DeptVo vo = new DeptVo();
            BeanUtils.copyProperties((Object)systemAuthDept, (Object)vo);
            vo.setStatusDesc(systemAuthDept.getStatus() != null && systemAuthDept.getStatus() == 1 ? "\u6b63\u5e38" : "\u7981\u7528");
            vo.setCreateTime(TimeUtils.timestampToDate((Long)systemAuthDept.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)systemAuthDept.getUpdateTime()));
            list.add(vo);
        }
        JSONArray jsonArray = JSONArray.parseArray((String)JSONArray.toJSONString(list, (JSONWriter.Feature[])new JSONWriter.Feature[0]), (JSONReader.Feature[])new JSONReader.Feature[0]);
        return ListUtils.listToTree((JSONArray)jsonArray, (String)"id", (String)"pid", (String)"children");
    }

    public JSONArray list(DeptSearchValidate searchValidate) {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<Dept>();
        queryWrapper.isNull("delete_time");
        queryWrapper.orderByDesc(Arrays.asList("sort", "id"));
        queryWrapper.select(Dept.class, info -> !info.getColumn().equals("delete_time"));
        this.deptMapper.setSearch(queryWrapper, (Object)searchValidate, new String[]{"like:name:str", "=:status@status:int"});
        List<Dept> systemAuthDeptList = this.deptMapper.selectList((Wrapper)queryWrapper);
        LinkedList<DeptVo> list = new LinkedList<DeptVo>();
        for (Dept systemAuthDept : systemAuthDeptList) {
            DeptVo vo = new DeptVo();
            BeanUtils.copyProperties((Object)systemAuthDept, (Object)vo);
            vo.setStatusDesc(systemAuthDept.getStatus() != null && systemAuthDept.getStatus() == 1 ? "\u6b63\u5e38" : "\u7981\u7528");
            vo.setCreateTime(TimeUtils.timestampToDate((Long)systemAuthDept.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)systemAuthDept.getUpdateTime()));
            list.add(vo);
        }
        JSONArray jsonArray = JSONArray.parseArray((String)JSONArray.toJSONString(list, (JSONWriter.Feature[])new JSONWriter.Feature[0]), (JSONReader.Feature[])new JSONReader.Feature[0]);
        return ListUtils.listToTree((JSONArray)jsonArray, (String)"id", (String)"pid", (String)"children");
    }

    public DeptVo detail(Integer id) {
        Dept systemAuthDept = (Dept)this.deptMapper.selectOne(new QueryWrapper<Dept>().select(Dept.class, info -> !info.getColumn().equals("delete_time")).eq("id", (Object)id).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)systemAuthDept, (String)"\u90e8\u95e8\u5df2\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        DeptVo vo = new DeptVo();
        BeanUtils.copyProperties((Object)systemAuthDept, (Object)vo);
        vo.setCreateTime(TimeUtils.timestampToDate((Long)systemAuthDept.getCreateTime()));
        vo.setUpdateTime(TimeUtils.timestampToDate((Long)systemAuthDept.getUpdateTime()));
        return vo;
    }

    public void add(DeptCreateValidate createValidate) {
        if (createValidate.getPid().equals(0)) {
            Dept systemAuthDept = (Dept)this.deptMapper.selectOne(new QueryWrapper<Dept>().select(new String[]{"id,pid,name"}).eq("pid", (Object)0).isNull("delete_time").last("limit 1"));
            Assert.isNull(systemAuthDept, (String)"\u9876\u7ea7\u90e8\u95e8\u53ea\u5141\u8bb8\u6709\u4e00\u4e2a", (Object[])new Object[0]);
        }
        Dept model = new Dept();
        model.setPid(createValidate.getPid());
        model.setName(createValidate.getName());
        model.setLeader(createValidate.getLeader());
        model.setMobile(createValidate.getMobile());
        model.setSort(createValidate.getSort());
        model.setStatus(createValidate.getStatus());
        model.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.deptMapper.insert(model);
    }

    public void edit(DeptUpdateValidate updateValidate) {
        Dept model = (Dept)this.deptMapper.selectOne(new QueryWrapper<Dept>().select(Dept.class, info -> !info.getColumn().equals("delete_time")).eq("id", (Object)updateValidate.getId()).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)model, (String)"\u90e8\u95e8\u4e0d\u5b58\u5728", (Object[])new Object[0]);
        Assert.isFalse((model.getPid().equals(0) && updateValidate.getPid() > 0 ? 1 : 0) != 0, (String)"\u9876\u7ea7\u90e8\u95e8\u4e0d\u80fd\u4fee\u6539\u4e0a\u7ea7", (Object[])new Object[0]);
        Assert.isFalse((boolean)updateValidate.getId().equals(updateValidate.getPid()), (String)"\u4e0a\u7ea7\u90e8\u95e8\u4e0d\u80fd\u662f\u81ea\u5df1", (Object[])new Object[0]);
        model.setPid(updateValidate.getPid());
        model.setName(updateValidate.getName());
        model.setLeader(updateValidate.getLeader());
        model.setMobile(updateValidate.getMobile());
        model.setSort(updateValidate.getSort());
        model.setStatus(updateValidate.getStatus());
        model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.deptMapper.updateById(model);
    }

    public void del(Integer id) {
        Dept model = (Dept)this.deptMapper.selectOne(new QueryWrapper<Dept>().select(new String[]{"id,pid,name"}).eq("id", (Object)id).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)model, (String)"\u90e8\u95e8\u4e0d\u5b58\u5728", (Object[])new Object[0]);
        Assert.isFalse((model.getPid() == 0 ? 1 : 0) != 0, (String)"\u9876\u7ea7\u90e8\u95e8\u4e0d\u80fd\u5220\u9664", (Object[])new Object[0]);
        Dept pModel = (Dept)this.deptMapper.selectOne(new QueryWrapper<Dept>().select(new String[]{"id,pid,name"}).eq("pid", (Object)id).isNull("delete_time").last("limit 1"));
        Assert.isNull(pModel, (String)"\u8bf7\u5148\u5220\u9664\u5b50\u7ea7\u90e8\u95e8", (Object[])new Object[0]);
        List<Admin> admins = this.adminMapper.getByDept(id);
        Assert.isTrue((boolean)admins.isEmpty(), (String)"\u8be5\u90e8\u95e8\u5df2\u88ab\u7ba1\u7406\u5458\u4f7f\u7528,\u8bf7\u5148\u79fb\u9664", (Object[])new Object[0]);
        model.setDeleteTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.deptMapper.updateById(model);
    }
}

