/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.system.ISystemRoleService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.system.SystemRoleCreateValidate
 *  com.mdd.admin.validate.system.SystemRoleUpdateValidate
 *  com.mdd.admin.vo.system.SystemAuthRoleVo
 *  com.mdd.common.core.PageResult
 *  org.springframework.validation.annotation.Validated
 */
package com.mdd.admin.service.system;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.system.SystemRoleCreateValidate;
import com.mdd.admin.validate.system.SystemRoleUpdateValidate;
import com.mdd.admin.vo.system.SystemAuthRoleVo;
import com.mdd.common.core.PageResult;
import java.util.List;
import org.springframework.validation.annotation.Validated;

public interface ISystemRoleService {
    public List<SystemAuthRoleVo> all();

    public PageResult<SystemAuthRoleVo> list(@Validated PageValidate var1);

    public SystemAuthRoleVo detail(Integer var1);

    public void add(SystemRoleCreateValidate var1);

    public void edit(SystemRoleUpdateValidate var1);

    public void del(Integer var1);

    public List<String> getRoleNameByAdminId(Integer var1);
}

