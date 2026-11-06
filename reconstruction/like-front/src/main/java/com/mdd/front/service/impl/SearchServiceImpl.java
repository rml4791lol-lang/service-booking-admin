/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.common.mapper.setting.HotSearchMapper
 *  com.mdd.front.service.ISearchService
 *  com.mdd.front.service.impl.SearchServiceImpl
 *  com.mdd.front.service.impl.SearchServiceImpl$1
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 */
package com.mdd.front.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.mapper.setting.HotSearchMapper;
import com.mdd.front.service.ISearchService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.mdd.common.entity.setting.HotSearch;
import com.mdd.common.util.ConfigUtils;

@Service
public class SearchServiceImpl
implements ISearchService {
    @Resource
    HotSearchMapper hotSearchMapper;

    public JSONObject hotLists() {
        final List<HotSearch> hotSearches = this.hotSearchMapper.selectList((Wrapper)new QueryWrapper<HotSearch>().orderByDesc("sort"));
        return new JSONObject() {
            {
                this.put("status", ConfigUtils.get("hot_search", "status", "0"));
                this.put("data", hotSearches);
            }
        };
    }
}

