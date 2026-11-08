/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.dev33.satoken.stp.StpInterface
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.admin.LikeAdminThreadLocal
 *  com.mdd.admin.config.stp.StpInterConfig
 *  com.mdd.admin.service.admin.IAdminRoleService
 *  com.mdd.common.entity.system.SystemMenu
 *  com.mdd.common.entity.system.SystemRoleMenu
 *  com.mdd.common.mapper.system.SystemMenuMapper
 *  com.mdd.common.mapper.system.SystemRoleMenuMapper
 *  com.mdd.common.util.StringUtils
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Component
 */
package com.mdd.admin.config.stp;

import cn.dev33.satoken.stp.StpInterface;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.LikeAdminThreadLocal;
import com.mdd.admin.service.admin.IAdminRoleService;
import com.mdd.common.entity.system.SystemMenu;
import com.mdd.common.entity.system.SystemRoleMenu;
import com.mdd.common.mapper.system.SystemMenuMapper;
import com.mdd.common.mapper.system.SystemRoleMenuMapper;
import com.mdd.common.util.StringUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class StpInterConfig
implements StpInterface {
    @Resource
    SystemRoleMenuMapper systemRoleMenuMapper;
    @Resource
    SystemMenuMapper systemAuthMenuMapper;
    @Resource
    IAdminRoleService iAdminRoleService;

    public List<String> getPermissionList(Object loginId, String loginType) {
        List<Integer> roleIds = LikeAdminThreadLocal.getRoleIds();
        LinkedList<String> perms = new LinkedList<String>();
        if (roleIds.isEmpty()) {
            return perms;
        }
        List<SystemRoleMenu> permList = this.systemRoleMenuMapper.selectList(new QueryWrapper<SystemRoleMenu>().select(new String[]{"role_id,menu_id"}).in("role_id", (Collection)roleIds));
        if (permList.isEmpty()) {
            return perms;
        }
        LinkedList<Integer> menuIds = new LinkedList<Integer>();
        for (SystemRoleMenu systemAuthPerm : permList) {
            menuIds.add(systemAuthPerm.getMenuId());
        }
        List<SystemMenu> systemAuthMenus = this.systemAuthMenuMapper.selectList(new QueryWrapper<SystemMenu>().select(new String[]{"id,perms"}).eq("is_disable", (Object)0).in("id", menuIds).in("type", Arrays.asList("C", "A")).orderByAsc(Arrays.asList("sort", "id")));
        for (SystemMenu item : systemAuthMenus) {
            if (!StringUtils.isNotNull(item.getPerms()) || !StringUtils.isNotEmpty((String)item.getPerms())) continue;
            perms.add(item.getPerms().trim());
        }
        return perms;
    }

    public List<String> getRoleList(Object loginId, String loginType) {
        return null;
    }
}

