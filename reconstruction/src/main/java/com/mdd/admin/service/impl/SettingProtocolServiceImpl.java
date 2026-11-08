/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ISettingProtocolService
 *  com.mdd.admin.service.impl.SettingProtocolServiceImpl
 *  com.mdd.admin.validate.setting.SettingAgreementValidate
 *  com.mdd.admin.vo.setting.SettingAgreementVo
 *  com.mdd.common.util.ConfigUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.mdd.admin.service.ISettingProtocolService;
import com.mdd.admin.validate.setting.SettingAgreementValidate;
import com.mdd.admin.vo.setting.SettingAgreementVo;
import com.mdd.common.util.ConfigUtils;
import org.springframework.stereotype.Service;

@Service
public class SettingProtocolServiceImpl
implements ISettingProtocolService {
    public SettingAgreementVo getAgreement() {
        SettingAgreementVo vo = new SettingAgreementVo();
        vo.setServiceTitle(ConfigUtils.get((String)"agreement", (String)"service_title", (String)""));
        vo.setServiceContent(ConfigUtils.get((String)"agreement", (String)"service_content", (String)""));
        vo.setPrivacyTitle(ConfigUtils.get((String)"agreement", (String)"privacy_title", (String)""));
        vo.setPrivacyContent(ConfigUtils.get((String)"agreement", (String)"privacy_content", (String)""));
        return vo;
    }

    public void setAgreement(SettingAgreementValidate protocolValidate) {
        ConfigUtils.set((String)"agreement", (String)"service_title", (String)protocolValidate.getServiceTitle());
        ConfigUtils.set((String)"agreement", (String)"service_content", (String)protocolValidate.getServiceContent());
        ConfigUtils.set((String)"agreement", (String)"privacy_title", (String)protocolValidate.getPrivacyTitle());
        ConfigUtils.set((String)"agreement", (String)"privacy_content", (String)protocolValidate.getPrivacyContent());
    }
}

