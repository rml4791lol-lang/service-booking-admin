/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.admin.IAdminRoleService
 *  com.mdd.common.entity.admin.AdminRole
 */
package com.mdd.admin.service.admin;

import com.mdd.common.entity.admin.AdminRole;
import java.util.List;

public interface IAdminRoleService {
    public List<Integer> getRoleIdAttr(Integer var1);

    public List<AdminRole> getAdminIdByRoleId(Integer var1);

    public void batchInsert(Integer var1, List<Integer> var2);

    public void deleteByAdminId(Integer var1);
}

