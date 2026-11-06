/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.service.admin.IAdminRoleService
 *  com.mdd.admin.service.impl.system.SystemRoleServiceImpl
 *  com.mdd.admin.service.system.ISystemRoleMenuService
 *  com.mdd.admin.service.system.ISystemRoleService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.system.SystemRoleCreateValidate
 *  com.mdd.admin.validate.system.SystemRoleUpdateValidate
 *  com.mdd.admin.vo.system.SystemAuthRoleVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.admin.Admin
 *  com.mdd.common.entity.system.SystemRole
 *  com.mdd.common.mapper.admin.AdminMapper
 *  com.mdd.common.mapper.system.SystemRoleMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.validation.annotation.Validated
 */
package com.mdd.admin.service.impl.system;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.admin.IAdminRoleService;
import com.mdd.admin.service.system.ISystemRoleMenuService;
import com.mdd.admin.service.system.ISystemRoleService;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.system.SystemRoleCreateValidate;
import com.mdd.admin.validate.system.SystemRoleUpdateValidate;
import com.mdd.admin.vo.system.SystemAuthRoleVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.admin.Admin;
import com.mdd.common.entity.system.SystemRole;
import com.mdd.common.mapper.admin.AdminMapper;
import com.mdd.common.mapper.system.SystemRoleMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.mdd.common.entity.admin.AdminRole;

@Service
public class SystemRoleServiceImpl
implements ISystemRoleService {
    @Resource
    AdminMapper adminMapper;
    @Resource
    SystemRoleMapper systemRoleMapper;
    @Resource
    ISystemRoleMenuService iSystemRoleMenuService;
    @Resource
    IAdminRoleService iAdminRoleService;

    public List<SystemAuthRoleVo> all() {
        QueryWrapper<SystemRole> queryWrapper = new QueryWrapper<SystemRole>();
        queryWrapper.select(new String[]{"id,name,sort,create_time,update_time"});
        queryWrapper.orderByDesc(Arrays.asList("sort", "id"));
        queryWrapper.isNull("delete_time");
        List<SystemRole> systemAuthRoles = this.systemRoleMapper.selectList((Wrapper)queryWrapper);
        ArrayList<SystemAuthRoleVo> list = new ArrayList<SystemAuthRoleVo>();
        for (SystemRole systemAuthRole : systemAuthRoles) {
            SystemAuthRoleVo vo = new SystemAuthRoleVo();
            vo.setId(systemAuthRole.getId());
            vo.setName(systemAuthRole.getName());
            vo.setSort(systemAuthRole.getSort());
            vo.setCreateTime(TimeUtils.timestampToDate((Long)systemAuthRole.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)systemAuthRole.getUpdateTime()));
            vo.setNum(Integer.valueOf(0));
            vo.setDesc("");
            vo.setMenusId((Object)Collections.EMPTY_LIST);
            list.add(vo);
        }
        return list;
    }

    public PageResult<SystemAuthRoleVo> list(@Validated PageValidate pageValidate) {
        Integer page = pageValidate.getPage_no();
        Integer limit = pageValidate.getPage_size();
        QueryWrapper<SystemRole> queryWrapper = new QueryWrapper<SystemRole>();
        queryWrapper.orderByDesc(Arrays.asList("sort", "id"));
        queryWrapper.isNull("delete_time");
        IPage<SystemRole> iPage = this.systemRoleMapper.selectPage((IPage)new Page((long)page.intValue(), (long)limit.intValue()), (Wrapper)queryWrapper);
        ArrayList<SystemAuthRoleVo> list = new ArrayList<SystemAuthRoleVo>();
        for (SystemRole systemAuthRole : iPage.getRecords()) {
            SystemAuthRoleVo vo = new SystemAuthRoleVo();
            BeanUtils.copyProperties((Object)systemAuthRole, (Object)vo);
            LinkedList<Integer> roleIds = new LinkedList<Integer>();
            roleIds.add(systemAuthRole.getId());
            vo.setMenusId((Object)this.iSystemRoleMenuService.selectMenuIdsByRoleId(roleIds));
            vo.setNum(this.adminMapper.getCountByRoleId(systemAuthRole.getId()));
            vo.setCreateTime(TimeUtils.timestampToDate((Long)systemAuthRole.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)systemAuthRole.getUpdateTime()));
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public SystemAuthRoleVo detail(Integer id) {
        SystemRole systemAuthRole = (SystemRole)this.systemRoleMapper.selectOne(new QueryWrapper<SystemRole>().eq("id", (Object)id).last("limit 1"));
        Assert.notNull((Object)systemAuthRole, (String)"\u89d2\u8272\u5df2\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        LinkedList<Integer> roleIds = new LinkedList<Integer>();
        roleIds.add(systemAuthRole.getId());
        SystemAuthRoleVo vo = new SystemAuthRoleVo();
        BeanUtils.copyProperties((Object)systemAuthRole, (Object)vo);
        vo.setNum(Integer.valueOf(0));
        vo.setMenusId((Object)this.iSystemRoleMenuService.selectMenuIdsByRoleId(roleIds));
        vo.setCreateTime(TimeUtils.timestampToDate((Long)systemAuthRole.getCreateTime()));
        vo.setUpdateTime(TimeUtils.timestampToDate((Long)systemAuthRole.getUpdateTime()));
        return vo;
    }

    @Transactional
    public void add(SystemRoleCreateValidate createValidate) {
        Assert.isNull(this.systemRoleMapper.selectOne(new QueryWrapper<SystemRole>().select(new String[]{"id,name"}).eq("name", (Object)createValidate.getName().trim()).isNull("delete_time").last("limit 1")), (String)"\u89d2\u8272\u540d\u79f0\u5df2\u5b58\u5728!", (Object[])new Object[0]);
        SystemRole model = new SystemRole();
        model.setName(createValidate.getName().trim());
        model.setDesc(createValidate.getDesc());
        model.setSort(createValidate.getSort());
        model.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.systemRoleMapper.insert(model);
    }

    @Transactional
    public void edit(SystemRoleUpdateValidate updateValidate) {
        Assert.notNull((Object)this.systemRoleMapper.selectOne(new QueryWrapper<SystemRole>().select(new String[]{"id,name"}).eq("id", (Object)updateValidate.getId()).isNull("delete_time").last("limit 1")), (String)"\u89d2\u8272\u5df2\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        Assert.isNull(this.systemRoleMapper.selectOne(new QueryWrapper<SystemRole>().select(new String[]{"id,name"}).ne("id", (Object)updateValidate.getId()).eq("name", (Object)updateValidate.getName().trim()).isNull("delete_time").last("limit 1")), (String)"\u89d2\u8272\u540d\u79f0\u5df2\u5b58\u5728!", (Object[])new Object[0]);
        SystemRole model = new SystemRole();
        model.setId(updateValidate.getId());
        model.setName(updateValidate.getName().trim());
        model.setDesc(updateValidate.getDesc());
        model.setSort(updateValidate.getSort());
        model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.systemRoleMapper.updateById(model);
        this.iSystemRoleMenuService.batchDeleteByRoleId(updateValidate.getId());
        this.iSystemRoleMenuService.batchSaveByMenuIds(updateValidate.getId(), updateValidate.getMenuId());
    }

    @Transactional
    public void del(Integer id) {
        Assert.notNull((Object)this.systemRoleMapper.selectOne(new QueryWrapper<SystemRole>().select(new String[]{"id", "name"}).eq("id", (Object)id).last("limit 1")), (String)"\u89d2\u8272\u5df2\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        List<AdminRole> adminRoles = this.iAdminRoleService.getAdminIdByRoleId(id);
        Assert.isTrue((adminRoles != null && adminRoles.isEmpty() ? 1 : 0) != 0, (String)"\u89d2\u8272\u5df2\u88ab\u7ba1\u7406\u5458\u4f7f\u7528,\u8bf7\u5148\u79fb\u9664", (Object[])new Object[0]);
        this.systemRoleMapper.deleteById((Serializable)id);
        this.iSystemRoleMenuService.batchDeleteByRoleId(id);
    }

    public List<String> getRoleNameByAdminId(Integer adminId) {
        List<String> ret = new ArrayList<String>();
        Admin admin = (Admin)this.adminMapper.selectOne(new QueryWrapper<Admin>().eq("id", (Object)adminId).isNull("delete_time"));
        if (StringUtils.isNull(admin)) {
            return ret;
        }
        if (admin.getRoot().equals(1)) {
            ret.add("\u7cfb\u7edf\u7ba1\u7406\u5458");
        } else {
            List<Integer> roleIds = this.iAdminRoleService.getRoleIdAttr(adminId);
            if (roleIds.size() > 0) {
                ret = this.getNamesByIds(roleIds);
            } else {
                return ret;
            }
        }
        return ret;
    }

    private List<String> getNamesByIds(List<Integer> ids) {
        ArrayList<String> ret = new ArrayList<String>();
        List<SystemRole> adminRoleList = this.systemRoleMapper.selectList(new QueryWrapper<SystemRole>().in("id", ids).isNull("delete_time"));
        if (adminRoleList.size() > 0) {
            for (SystemRole item : adminRoleList) {
                ret.add(item.getName());
            }
        }
        return ret;
    }
}

