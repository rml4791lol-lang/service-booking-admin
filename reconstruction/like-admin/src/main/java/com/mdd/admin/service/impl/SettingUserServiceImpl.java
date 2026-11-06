/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ISettingUserService
 *  com.mdd.admin.service.impl.SettingUserServiceImpl
 *  com.mdd.admin.validate.setting.SettingUserValidate
 *  com.mdd.admin.vo.setting.SettingUserVo
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.UrlUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.mdd.admin.service.ISettingUserService;
import com.mdd.admin.validate.setting.SettingUserValidate;
import com.mdd.admin.vo.setting.SettingUserVo;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.UrlUtils;
import org.springframework.stereotype.Service;

@Service
public class SettingUserServiceImpl
implements ISettingUserService {
    public SettingUserVo getConfig() {
        String defaultAvatar = ConfigUtils.get((String)"default_image", (String)"user_avatar", (String)"");
        SettingUserVo vo = new SettingUserVo();
        vo.setDefaultAvatar(UrlUtils.toAdminAbsoluteUrl((String)defaultAvatar));
        return vo;
    }

    public void setConfig(SettingUserValidate userValidate) {
        ConfigUtils.set((String)"default_image", (String)"user_avatar", (String)UrlUtils.toRelativeUrl((String)userValidate.getDefaultAvatar()));
    }
}

