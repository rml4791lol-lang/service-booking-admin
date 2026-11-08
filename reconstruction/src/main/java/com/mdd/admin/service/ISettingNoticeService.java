/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.mdd.admin.service.ISettingNoticeService
 *  com.mdd.admin.vo.setting.SettingNoticeDetailVo
 *  com.mdd.admin.vo.setting.SettingNoticeListedVo
 */
package com.mdd.admin.service;

import com.alibaba.fastjson2.JSONObject;
import com.mdd.admin.vo.setting.SettingNoticeDetailVo;
import com.mdd.admin.vo.setting.SettingNoticeListedVo;
import java.util.List;

public interface ISettingNoticeService {
    public List<SettingNoticeListedVo> list(Integer var1);

    public SettingNoticeDetailVo detail(Integer var1);

    public void save(JSONObject var1);
}

