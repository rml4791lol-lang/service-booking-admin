/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ISettingDictDataService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.setting.DictDataCreateValidate
 *  com.mdd.admin.validate.setting.DictDataUpdateValidate
 *  com.mdd.admin.vo.setting.SettingDictDataVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.setting.DictDataCreateValidate;
import com.mdd.admin.validate.setting.DictDataUpdateValidate;
import com.mdd.admin.vo.setting.SettingDictDataVo;
import com.mdd.common.core.PageResult;
import java.util.List;
import java.util.Map;

public interface ISettingDictDataService {
    public List<SettingDictDataVo> all(Map<String, String> var1);

    public PageResult<SettingDictDataVo> list(PageValidate var1, Map<String, String> var2);

    public SettingDictDataVo detail(Integer var1);

    public void add(DictDataCreateValidate var1);

    public void edit(DictDataUpdateValidate var1);

    public void del(List<Integer> var1);
}

