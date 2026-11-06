/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 *  com.alibaba.fastjson2.JSONReader$Feature
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.mdd.common.entity.decorate.DecorateTabbar
 *  com.mdd.common.enums.YesNoEnum
 *  com.mdd.common.mapper.decorate.DecorateTabbarMapper
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.TimeUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.front.service.IDecorateTabbarService
 *  com.mdd.front.service.impl.DecorateTabbarServiceImpl
 *  com.mdd.front.vo.decorateTabbar.DecorateTabbarVo
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.front.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.decorate.DecorateTabbar;
import com.mdd.common.enums.YesNoEnum;
import com.mdd.common.mapper.decorate.DecorateTabbarMapper;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.TimeUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.front.service.IDecorateTabbarService;
import com.mdd.front.vo.decorateTabbar.DecorateTabbarVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class DecorateTabbarServiceImpl
implements IDecorateTabbarService {
    @Resource
    DecorateTabbarMapper decorateTabbarMapper;

    public List<DecorateTabbarVo> getTabbarLists() {
        List<DecorateTabbar> list = this.decorateTabbarMapper.selectList(new QueryWrapper<DecorateTabbar>().eq("is_show", (Object)YesNoEnum.YES.getCode()));
        ArrayList<DecorateTabbarVo> ret = new ArrayList<DecorateTabbarVo>();
        if (list.size() == 0) {
            return ret;
        }
        for (DecorateTabbar item : list) {
            DecorateTabbarVo vo = new DecorateTabbarVo();
            BeanUtils.copyProperties((Object)item, (Object)vo);
            if (StringUtils.isNotEmpty((String)item.getSelected())) {
                vo.setSelected(UrlUtils.toAbsoluteUrl((String)item.getSelected()));
            }
            if (StringUtils.isNotEmpty((String)item.getUnselected())) {
                vo.setUnselected(UrlUtils.toAbsoluteUrl((String)item.getUnselected()));
            }
            vo.setCreateTime(TimeUtils.timestampToDate((Long)item.getCreateTime()));
            vo.setUpdateTime(TimeUtils.timestampToDate((Long)item.getUpdateTime()));
            vo.setLink(StringUtils.isEmpty((String)item.getLink()) ? new JSONObject() : JSONObject.parse((String)item.getLink(), (JSONReader.Feature[])new JSONReader.Feature[0]));
            ret.add(vo);
        }
        return ret;
    }
}

