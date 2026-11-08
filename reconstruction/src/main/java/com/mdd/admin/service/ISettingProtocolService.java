/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ISettingProtocolService
 *  com.mdd.admin.validate.setting.SettingAgreementValidate
 *  com.mdd.admin.vo.setting.SettingAgreementVo
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.setting.SettingAgreementValidate;
import com.mdd.admin.vo.setting.SettingAgreementVo;

public interface ISettingProtocolService {
    public SettingAgreementVo getAgreement();

    public void setAgreement(SettingAgreementValidate var1);
}

