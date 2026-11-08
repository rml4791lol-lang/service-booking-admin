/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ISettingLoginService
 *  com.mdd.admin.validate.setting.SettingLoginValidate
 *  com.mdd.admin.vo.setting.SettingLoginVo
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.setting.SettingLoginValidate;
import com.mdd.admin.vo.setting.SettingLoginVo;

public interface ISettingLoginService {
    public SettingLoginVo getRegisterConfig();

    public void setRegisterConfig(SettingLoginValidate var1);
}

