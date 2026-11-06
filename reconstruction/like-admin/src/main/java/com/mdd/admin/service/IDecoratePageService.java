/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IDecoratePageService
 *  com.mdd.admin.validate.decorate.DecoratePageValidate
 *  com.mdd.admin.vo.decorate.DecoratePageVo
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.decorate.DecoratePageValidate;
import com.mdd.admin.vo.decorate.DecoratePageVo;

public interface IDecoratePageService {
    public DecoratePageVo detail(Integer var1);

    public void save(DecoratePageValidate var1);
}

