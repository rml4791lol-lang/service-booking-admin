/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.service.ISettingPaymentService
 *  com.mdd.admin.validate.setting.SettingPayConfigValidate
 *  com.mdd.admin.vo.setting.SettingPaymentMethodVo
 *  com.mdd.common.entity.setting.DevPayConfig
 */
package com.mdd.admin.service;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.validate.setting.SettingPayConfigValidate;
import com.mdd.admin.vo.setting.SettingPaymentMethodVo;
import com.mdd.common.entity.setting.DevPayConfig;
import java.util.HashMap;
import java.util.List;

public interface ISettingPaymentService {
    public HashMap getPayWay();

    public JSONObject list();

    public DevPayConfig getConfig(Integer var1);

    public void setConfig(SettingPayConfigValidate var1);

    public void setPayWay(HashMap<Integer, List<SettingPaymentMethodVo>> var1);
}

