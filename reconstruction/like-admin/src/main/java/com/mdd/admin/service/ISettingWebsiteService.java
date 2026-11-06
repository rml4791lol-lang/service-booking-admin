/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ISettingWebsiteService
 *  com.mdd.admin.validate.setting.SettingWebsiteValidate
 *  com.mdd.admin.vo.setting.SettingWebsiteVo
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.setting.SettingWebsiteValidate;
import com.mdd.admin.vo.setting.SettingWebsiteVo;

public interface ISettingWebsiteService {
    public SettingWebsiteVo getWebsite();

    public void setWebsite(SettingWebsiteValidate var1);
}

