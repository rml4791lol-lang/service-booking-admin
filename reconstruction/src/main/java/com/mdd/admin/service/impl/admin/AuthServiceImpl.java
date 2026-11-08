/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.admin.IAdminService
 *  com.mdd.admin.service.admin.IAuthService
 *  com.mdd.admin.service.impl.admin.AuthServiceImpl
 *  com.mdd.admin.vo.system.SystemAuthAdminDetailVo
 *  com.mdd.common.mapper.system.SystemMenuMapper
 *  com.mdd.common.mapper.system.SystemRoleMenuMapper
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl.admin;

import com.mdd.admin.service.admin.IAdminService;
import com.mdd.admin.service.admin.IAuthService;
import com.mdd.admin.vo.system.SystemAuthAdminDetailVo;
import com.mdd.common.mapper.system.SystemMenuMapper;
import com.mdd.common.mapper.system.SystemRoleMenuMapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl
implements IAuthService {
    @Resource
    IAdminService iAdminService;
    @Resource
    SystemMenuMapper systemMenuMapper;
    @Resource
    SystemRoleMenuMapper SystemRoleMenuMapper;

    public List<String> getBtnAuthByRoleId(Integer adminId) {
        List<String> ret = new ArrayList<String>();
        SystemAuthAdminDetailVo admin = this.iAdminService.detail(adminId);
        if (admin.getRoot().equals(1)) {
            ret.add("*");
            return ret;
        }
        List menuIds = this.SystemRoleMenuMapper.getMenuIds(adminId);
        if (menuIds == null || menuIds.isEmpty()) {
            return ret;
        }
        ret = this.systemMenuMapper.getPerms(menuIds);
        return ret;
    }
}

