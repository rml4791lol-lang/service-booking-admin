/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONArray
 *  com.alibaba.fastjson2.JSONReader$Feature
 *  com.alibaba.fastjson2.JSONWriter$Feature
 *  com.mdd.admin.service.ISettingLoginService
 *  com.mdd.admin.service.impl.SettingLoginServiceImpl
 *  com.mdd.admin.validate.setting.SettingLoginValidate
 *  com.mdd.admin.vo.setting.SettingLoginVo
 *  com.mdd.common.util.ConfigUtils
 *  org.springframework.stereotype.Service
 *  org.springframework.util.Assert
 */
package com.mdd.admin.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.mdd.admin.service.ISettingLoginService;
import com.mdd.admin.validate.setting.SettingLoginValidate;
import com.mdd.admin.vo.setting.SettingLoginVo;
import com.mdd.common.util.ConfigUtils;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class SettingLoginServiceImpl
implements ISettingLoginService {
    public SettingLoginVo getRegisterConfig() {
        Map<String, String> config = ConfigUtils.get((String)"login");
        SettingLoginVo vo = new SettingLoginVo();
        vo.setLoginWay(JSONArray.parse((String)config.getOrDefault("login_way", ""), (JSONReader.Feature[])new JSONReader.Feature[0]));
        vo.setCoerceMobile(Integer.valueOf(Integer.parseInt(config.getOrDefault("coerce_mobile", "0"))));
        vo.setLoginAgreement(Integer.valueOf(Integer.parseInt(config.getOrDefault("login_agreement", "0"))));
        vo.setThirdAuth(Integer.valueOf(Integer.parseInt(config.getOrDefault("third_auth", "0"))));
        vo.setWechatAuth(Integer.valueOf(Integer.parseInt(config.getOrDefault("wechat_auth", "0"))));
        vo.setQqAuth(Integer.valueOf(Integer.parseInt(config.getOrDefault("qq_auth", "0"))));
        return vo;
    }

    public void setRegisterConfig(SettingLoginValidate loginValidate) {
        JSONArray loginway = loginValidate.getLoginWay();
        Assert.isTrue((loginway != null && loginway.size() > 0 ? 1 : 0) != 0, (String)"\u7cfb\u7edf\u901a\u7528\u767b\u5f55\u65b9\u5f0f\uff0c\u81f3\u5c11\u9009\u62e9\u4e00\u9879");
        ConfigUtils.set((String)"login", (String)"login_way", (String)loginValidate.getLoginWay().toJSONString(new JSONWriter.Feature[0]));
        ConfigUtils.set((String)"login", (String)"coerce_mobile", (String)String.valueOf(loginValidate.getCoerceMobile()));
        ConfigUtils.set((String)"login", (String)"login_agreement", (String)String.valueOf(loginValidate.getLoginAgreement()));
        ConfigUtils.set((String)"login", (String)"third_auth", (String)String.valueOf(loginValidate.getThirdAuth()));
        ConfigUtils.set((String)"login", (String)"wechat_auth", (String)String.valueOf(loginValidate.getWechatAuth()));
        ConfigUtils.set((String)"login", (String)"qq_auth", (String)String.valueOf(loginValidate.getQqAuth()));
    }
}

