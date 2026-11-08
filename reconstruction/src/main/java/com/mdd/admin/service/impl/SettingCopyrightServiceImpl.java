/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSON
 *  com.mdd.admin.service.ISettingCopyrightService
 *  com.mdd.admin.service.impl.SettingCopyrightServiceImpl
 *  com.mdd.admin.validate.setting.SettingCopyrightValidate
 *  com.mdd.admin.vo.setting.SettingCopyrightVo
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.ListUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.alibaba.fastjson2.JSON;
import com.mdd.admin.service.ISettingCopyrightService;
import com.mdd.admin.validate.setting.SettingCopyrightValidate;
import com.mdd.admin.vo.setting.SettingCopyrightVo;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.ListUtils;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SettingCopyrightServiceImpl
implements ISettingCopyrightService {
    public List<SettingCopyrightVo> getCopyright() {
        String config = ConfigUtils.get((String)"copyright", (String)"config", (String)"[]");
        List<Map<String, String>> copyright = ListUtils.stringToListAsMapStr((String)config);
        LinkedList<SettingCopyrightVo> list = new LinkedList<SettingCopyrightVo>();
        for (Map<String, String> map : copyright) {
            SettingCopyrightVo vo = new SettingCopyrightVo();
            vo.setKey(map.getOrDefault("key", ""));
            vo.setValue(map.getOrDefault("value", ""));
            list.add(vo);
        }
        return list;
    }

    public void setCopyright(SettingCopyrightValidate copyrightValidate) {
        String s = JSON.toJSONString((Object)copyrightValidate.getConfig());
        ConfigUtils.set((String)"copyright", (String)"config", (String)s);
    }
}

