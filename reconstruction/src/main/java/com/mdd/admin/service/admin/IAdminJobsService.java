/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.admin.IAdminJobsService
 */
package com.mdd.admin.service.admin;

import java.util.List;

public interface IAdminJobsService {
    public List<Integer> getJobIdAttr(Integer var1);

    public void batchInsert(Integer var1, List<Integer> var2);

    public void deleteByAdminId(Integer var1);
}

