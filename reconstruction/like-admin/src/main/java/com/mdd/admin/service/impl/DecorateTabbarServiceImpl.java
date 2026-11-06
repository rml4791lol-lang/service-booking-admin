/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSON
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.admin.service.IDecorateTabbarService
 *  com.mdd.admin.service.impl.DecorateTabbarServiceImpl
 *  com.mdd.admin.validate.decorate.DecorateTabsValidate
 *  com.mdd.admin.vo.decorate.DecorateTabbarVo
 *  com.mdd.admin.vo.decorate.DecorateTabsListsVo
 *  com.mdd.admin.vo.decorate.DecorateTabsStyleVo
 *  com.mdd.common.entity.decorate.DecorateTabbar
 *  com.mdd.common.mapper.decorate.DecorateTabbarMapper
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.MapUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.admin.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.admin.service.IDecorateTabbarService;
import com.mdd.admin.validate.decorate.DecorateTabsValidate;
import com.mdd.admin.vo.decorate.DecorateTabbarVo;
import com.mdd.admin.vo.decorate.DecorateTabsListsVo;
import com.mdd.admin.vo.decorate.DecorateTabsStyleVo;
import com.mdd.common.entity.decorate.DecorateTabbar;
import com.mdd.common.mapper.decorate.DecorateTabbarMapper;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.MapUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DecorateTabbarServiceImpl
implements IDecorateTabbarService {
    @Resource
    DecorateTabbarMapper decorateTabbarMapper;

    public DecorateTabbarVo detail() {
        List<DecorateTabbar> list = this.decorateTabbarMapper.selectList(new QueryWrapper<DecorateTabbar>().orderByAsc("id"));
        LinkedList<DecorateTabsListsVo> tabList = new LinkedList<DecorateTabsListsVo>();
        for (DecorateTabbar tab : list) {
            DecorateTabsListsVo vo = new DecorateTabsListsVo();
            vo.setId(tab.getId());
            vo.setName(tab.getName());
            vo.setSelected(tab.getSelected());
            vo.setUnselected(tab.getUnselected());
            vo.setLink(JSON.parse((String)tab.getLink()));
            vo.setIsShow(tab.getIsShow());
            vo.setCreateTime(TimeUtils.timestampToDate((Long)tab.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)tab.getUpdateTime()));
            tabList.add(vo);
        }
        String tabbar = ConfigUtils.get((String)"tabbar", (String)"style", (String)"{}");
        DecorateTabbarVo response = new DecorateTabbarVo();
        DecorateTabsStyleVo styleVo = new DecorateTabsStyleVo();
        Map<String, String> styleMap = MapUtils.jsonToMap((String)tabbar);
        styleVo.setDefaultColor((String)styleMap.get("defaultColor"));
        styleVo.setSelectedColor((String)styleMap.get("selectedColor"));
        response.setStyle(styleVo);
        response.setList(tabList);
        return response;
    }

    @Transactional
    public void save(DecorateTabsValidate tabsValidate) {
        this.decorateTabbarMapper.delete(new QueryWrapper<DecorateTabbar>().gt("id", (Object)0));
        for (DecorateTabsListsVo obj : tabsValidate.getList()) {
            DecorateTabbar model = new DecorateTabbar();
            model.setName(obj.getName());
            model.setLink(JSON.toJSONString((Object)obj.getLink()));
            model.setSelected(obj.getSelected());
            model.setUnselected(obj.getUnselected());
            // #67：底部导航显隐必须持久化，否则后台关闭的 tab 重进页面又会出现。
            model.setIsShow(obj.getIsShow() == null ? Integer.valueOf(1) : obj.getIsShow());
            model.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            model.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.decorateTabbarMapper.insert(model);
        }
        if (StringUtils.isNotNull(tabsValidate.getStyle())) {
            ConfigUtils.set((String)"tabbar", (String)"style", (String)JSON.toJSONString((Object)tabsValidate.getStyle()));
        }
    }
}

