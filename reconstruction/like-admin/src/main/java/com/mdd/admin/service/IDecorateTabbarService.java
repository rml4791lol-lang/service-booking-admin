/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IDecorateTabbarService
 *  com.mdd.admin.validate.decorate.DecorateTabsValidate
 *  com.mdd.admin.vo.decorate.DecorateTabbarVo
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.decorate.DecorateTabsValidate;
import com.mdd.admin.vo.decorate.DecorateTabbarVo;

public interface IDecorateTabbarService {
    public DecorateTabbarVo detail();

    public void save(DecorateTabsValidate var1);
}

