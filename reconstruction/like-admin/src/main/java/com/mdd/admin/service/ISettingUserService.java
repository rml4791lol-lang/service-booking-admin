/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ISettingUserService
 *  com.mdd.admin.validate.setting.SettingUserValidate
 *  com.mdd.admin.vo.setting.SettingUserVo
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.setting.SettingUserValidate;
import com.mdd.admin.vo.setting.SettingUserVo;

public interface ISettingUserService {
    public SettingUserVo getConfig();

    public void setConfig(SettingUserValidate var1);
}

