/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.system.ISystemLoginService
 *  com.mdd.admin.validate.system.SystemAdminLoginsValidate
 *  com.mdd.admin.vo.system.SystemCaptchaVo
 *  com.mdd.admin.vo.system.SystemLoginVo
 */
package com.mdd.admin.service.system;

import com.mdd.admin.validate.system.SystemAdminLoginsValidate;
import com.mdd.admin.vo.system.SystemCaptchaVo;
import com.mdd.admin.vo.system.SystemLoginVo;

public interface ISystemLoginService {
    public SystemCaptchaVo captcha();

    public SystemLoginVo login(SystemAdminLoginsValidate var1);

    public void logout(String var1);
}

