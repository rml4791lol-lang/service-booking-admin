/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.system.ISystemRoleMenuService
 */
package com.mdd.admin.service.system;

import java.util.List;

public interface ISystemRoleMenuService {
    public List<Integer> selectMenuIdsByRoleId(List<Integer> var1);

    public void batchSaveByMenuIds(Integer var1, List<Integer> var2);

    public void batchDeleteByRoleId(Integer var1);

    public void batchDeleteByMenuId(Integer var1);
}

