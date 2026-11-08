/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ISettingCopyrightService
 *  com.mdd.admin.validate.setting.SettingCopyrightValidate
 *  com.mdd.admin.vo.setting.SettingCopyrightVo
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.setting.SettingCopyrightValidate;
import com.mdd.admin.vo.setting.SettingCopyrightVo;
import java.util.List;

public interface ISettingCopyrightService {
    public List<SettingCopyrightVo> getCopyright();

    public void setCopyright(SettingCopyrightValidate var1);
}

