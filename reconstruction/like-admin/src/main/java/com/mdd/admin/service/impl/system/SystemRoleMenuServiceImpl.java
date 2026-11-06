/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.admin.service.impl.system.SystemRoleMenuServiceImpl
 *  com.mdd.admin.service.system.ISystemRoleMenuService
 *  com.mdd.common.entity.system.SystemRole
 *  com.mdd.common.entity.system.SystemRoleMenu
 *  com.mdd.common.mapper.system.SystemRoleMapper
 *  com.mdd.common.mapper.system.SystemRoleMenuMapper
 *  com.mdd.common.util.StringUtils
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.admin.service.impl.system;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.service.system.ISystemRoleMenuService;
import com.mdd.common.entity.system.SystemRole;
import com.mdd.common.entity.system.SystemRoleMenu;
import com.mdd.common.mapper.system.SystemRoleMapper;
import com.mdd.common.mapper.system.SystemRoleMenuMapper;
import com.mdd.common.util.StringUtils;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemRoleMenuServiceImpl
implements ISystemRoleMenuService {
    @Resource
    SystemRoleMenuMapper systemRoleMenuMapper;
    @Resource
    SystemRoleMapper systemAuthRoleMapper;

    public List<Integer> selectMenuIdsByRoleId(List<Integer> roleIds) {
        LinkedList<Integer> menus = new LinkedList<Integer>();
        if (roleIds.isEmpty()) {
            return menus;
        }
        SystemRole systemAuthRole = (SystemRole)this.systemAuthRoleMapper.selectOne(new QueryWrapper<SystemRole>().in("id", roleIds).last("limit 1"));
        if (StringUtils.isNull(systemAuthRole)) {
            return menus;
        }
        List<SystemRoleMenu> systemRoleMenus = this.systemRoleMenuMapper.selectList(new QueryWrapper<SystemRoleMenu>().in("role_id", roleIds));
        for (SystemRoleMenu systemRoleMenu : systemRoleMenus) {
            menus.add(systemRoleMenu.getMenuId());
        }
        return menus;
    }

    @Transactional
    public void batchSaveByMenuIds(Integer roleId, List<Integer> menuIds) {
        if (menuIds != null && !menuIds.isEmpty()) {
            menuIds.forEach(item -> {
                SystemRoleMenu model = new SystemRoleMenu();
                model.setRoleId(roleId);
                model.setMenuId(item);
                this.systemRoleMenuMapper.insert(model);
            });
        }
    }

    public void batchDeleteByRoleId(Integer roleId) {
        this.systemRoleMenuMapper.delete(new QueryWrapper<SystemRoleMenu>().eq("role_id", (Object)roleId));
    }

    public void batchDeleteByMenuId(Integer menuId) {
        this.systemRoleMenuMapper.delete(new QueryWrapper<SystemRoleMenu>().eq("menu_id", (Object)menuId));
    }
}

