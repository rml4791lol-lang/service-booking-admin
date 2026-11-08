/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ISettingSearchService
 *  com.mdd.admin.validate.setting.SettingSearchValidate
 *  com.mdd.admin.vo.setting.SettingSearchDetailVo
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.setting.SettingSearchValidate;
import com.mdd.admin.vo.setting.SettingSearchDetailVo;

public interface ISettingSearchService {
    public SettingSearchDetailVo getConfig();

    public void setConfig(SettingSearchValidate var1);
}

