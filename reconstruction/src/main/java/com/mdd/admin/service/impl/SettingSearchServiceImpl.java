/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.admin.service.ISettingSearchService
 *  com.mdd.admin.service.impl.SettingSearchServiceImpl
 *  com.mdd.admin.validate.setting.SettingSearchValidate
 *  com.mdd.admin.vo.setting.SettingSearchDetailVo
 *  com.mdd.admin.vo.setting.SettingSearchObjectVo
 *  com.mdd.common.entity.setting.HotSearch
 *  com.mdd.common.mapper.setting.HotSearchMapper
 *  com.mdd.common.util.ConfigUtils
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.service.ISettingSearchService;
import com.mdd.admin.validate.setting.SettingSearchValidate;
import com.mdd.admin.vo.setting.SettingSearchDetailVo;
import com.mdd.admin.vo.setting.SettingSearchObjectVo;
import com.mdd.common.entity.setting.HotSearch;
import com.mdd.common.mapper.setting.HotSearchMapper;
import com.mdd.common.util.ConfigUtils;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SettingSearchServiceImpl
implements ISettingSearchService {
    @Resource
    HotSearchMapper hotSearchMapper;

    public SettingSearchDetailVo getConfig() {
        Integer status = Integer.parseInt(ConfigUtils.get((String)"hot_search", (String)"status", (String)"0"));
        List list = this.hotSearchMapper.selectList(new QueryWrapper<HotSearch>().orderByDesc("sort"));
        SettingSearchDetailVo vo = new SettingSearchDetailVo();
        vo.setStatus(status);
        vo.setData(list);
        return vo;
    }

    public void setConfig(SettingSearchValidate searchValidate) {
        String isHotSearch = String.valueOf(searchValidate.getStatus());
        ConfigUtils.set((String)"hot_search", (String)"status", (String)isHotSearch);
        this.hotSearchMapper.delete(new QueryWrapper<HotSearch>().ge("id", (Object)0));
        for (SettingSearchObjectVo vo : searchValidate.getData()) {
            HotSearch hotSearch = new HotSearch();
            hotSearch.setName(vo.getName());
            hotSearch.setSort(vo.getSort());
            this.hotSearchMapper.insert(hotSearch);
        }
    }
}

