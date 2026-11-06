/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.mdd.admin.service.IDecoratePageService
 *  com.mdd.admin.service.impl.DecoratePageServiceImpl
 *  com.mdd.admin.validate.decorate.DecoratePageValidate
 *  com.mdd.admin.vo.decorate.DecoratePageVo
 *  com.mdd.common.entity.decorate.DecoratePage
 *  com.mdd.common.mapper.decorate.DecoratePageMapper
 *  com.mdd.common.util.TimeUtils
 *  javax.annotation.Resource
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mdd.admin.service.IDecoratePageService;
import com.mdd.admin.validate.decorate.DecoratePageValidate;
import com.mdd.admin.vo.decorate.DecoratePageVo;
import com.mdd.common.entity.decorate.DecoratePage;
import com.mdd.common.mapper.decorate.DecoratePageMapper;
import com.mdd.common.util.TimeUtils;
import java.io.Serializable;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class DecoratePageServiceImpl
implements IDecoratePageService {
    @Resource
    DecoratePageMapper decoratePageMapper;

    public DecoratePageVo detail(Integer id) {
        DecoratePage decoratePage = (DecoratePage)this.decoratePageMapper.selectById((Serializable)id);
        Assert.notNull((Object)decoratePage, (String)"\u6570\u636e\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        DecoratePageVo vo = new DecoratePageVo();
        vo.setId(decoratePage.getId());
        vo.setName(decoratePage.getName());
        vo.setType(decoratePage.getType());
        vo.setData(decoratePage.getData());
        vo.setMeta(decoratePage.getMeta());
        vo.setCreateTime(TimeUtils.timestampToDate((Long)decoratePage.getCreateTime()));
        vo.setUpdateTime(TimeUtils.timestampToDate((Long)decoratePage.getUpdateTime()));
        return vo;
    }

    public void save(DecoratePageValidate decoratePageValidate) {
        DecoratePage decoratePage = (DecoratePage)this.decoratePageMapper.selectById((Serializable)decoratePageValidate.getId());
        Assert.notNull((Object)decoratePage, (String)"\u6570\u636e\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        decoratePage.setName(decoratePageValidate.getName());
        decoratePage.setType(decoratePageValidate.getType());
        decoratePage.setMeta(decoratePageValidate.getMeta());
        decoratePage.setData(decoratePageValidate.getData());
        decoratePage.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.decoratePageMapper.updateById(decoratePage);
    }
}

