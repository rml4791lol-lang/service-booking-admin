/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.ISettingDictTypeService
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.validate.setting.DictTypeCreateValidate
 *  com.mdd.admin.validate.setting.DictTypeUpdateValidate
 *  com.mdd.admin.vo.setting.SettingDictTypeVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.setting.DictTypeCreateValidate;
import com.mdd.admin.validate.setting.DictTypeUpdateValidate;
import com.mdd.admin.vo.setting.SettingDictTypeVo;
import com.mdd.common.core.PageResult;
import java.util.List;
import java.util.Map;

public interface ISettingDictTypeService {
    public List<SettingDictTypeVo> all();

    public PageResult<SettingDictTypeVo> list(PageValidate var1, Map<String, String> var2);

    public SettingDictTypeVo detail(Integer var1);

    public void add(DictTypeCreateValidate var1);

    public void edit(DictTypeUpdateValidate var1);

    public void del(Integer var1);
}

