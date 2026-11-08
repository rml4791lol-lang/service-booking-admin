/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Assert
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.mdd.admin.service.IChannelOaReplyService
 *  com.mdd.admin.service.impl.ChannelOaReplyServiceImpl
 *  com.mdd.admin.validate.channel.ChannelRpValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.vo.channel.ChannelRpVo
 *  com.mdd.common.core.PageResult
 *  com.mdd.common.entity.OfficialAccountReply
 *  com.mdd.common.mapper.OfficialAccountReplyMapper
 *  com.mdd.common.util.StringUtils
 *  javax.annotation.Resource
 *  org.springframework.beans.BeanUtils
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.admin.service.IChannelOaReplyService;
import com.mdd.admin.validate.channel.ChannelRpValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.channel.ChannelRpVo;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.OfficialAccountReply;
import com.mdd.common.mapper.OfficialAccountReplyMapper;
import com.mdd.common.util.StringUtils;
import java.util.Arrays;
import java.util.LinkedList;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChannelOaReplyServiceImpl
implements IChannelOaReplyService {
    @Resource
    OfficialAccountReplyMapper officialAccountReplyMapper;

    public PageResult<ChannelRpVo> list(PageValidate pageValidate, Integer replyType) {
        Integer pageNo = pageValidate.getPage_no();
        Integer pageSize = pageValidate.getPage_size();
        IPage<OfficialAccountReply> iPage = this.officialAccountReplyMapper.selectPage((IPage)new Page((long)pageNo.intValue(), (long)pageSize.intValue()), new QueryWrapper<OfficialAccountReply>().eq("reply_type", (Object)replyType).isNull("delete_time").orderByDesc(Arrays.asList("sort", "id")));
        LinkedList<ChannelRpVo> list = new LinkedList<ChannelRpVo>();
        for (OfficialAccountReply officialAccountReply : iPage.getRecords()) {
            ChannelRpVo vo = new ChannelRpVo();
            BeanUtils.copyProperties((Object)officialAccountReply, (Object)vo);
            list.add(vo);
        }
        return PageResult.iPageHandle((Long)iPage.getTotal(), (Long)iPage.getCurrent(), (Long)iPage.getSize(), list);
    }

    public ChannelRpVo detail(Integer id) {
        OfficialAccountReply officialAccountReply = (OfficialAccountReply)this.officialAccountReplyMapper.selectOne(new QueryWrapper<OfficialAccountReply>().eq("id", (Object)id).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)officialAccountReply, (String)"\u9ed8\u8ba4\u6570\u636e\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        ChannelRpVo vo = new ChannelRpVo();
        BeanUtils.copyProperties((Object)officialAccountReply, (Object)vo);
        return vo;
    }

    public void add(ChannelRpValidate defaultValidate) {
        if (defaultValidate.getReplyType().equals(3) && defaultValidate.getStatus().equals(1)) {
            OfficialAccountReply reply = new OfficialAccountReply();
            reply.setStatus(Integer.valueOf(0));
            this.officialAccountReplyMapper.update(reply, new QueryWrapper<OfficialAccountReply>().eq("reply_type", (Object)3));
        }
        if (defaultValidate.getReplyType().equals(2)) {
            Assert.isTrue((boolean)StringUtils.isNotEmpty((String)defaultValidate.getKeyword()), (String)"\u5173\u952e\u8bcd\u4e0d\u80fd\u4e3a\u7a7a", (Object[])new Object[0]);
        }
        OfficialAccountReply officialAccountReply = new OfficialAccountReply();
        officialAccountReply.setReplyType(defaultValidate.getReplyType());
        officialAccountReply.setContent(defaultValidate.getContent());
        officialAccountReply.setName(defaultValidate.getName());
        officialAccountReply.setContentType(defaultValidate.getContentType());
        officialAccountReply.setStatus(defaultValidate.getStatus());
        officialAccountReply.setKeyword(defaultValidate.getKeyword());
        officialAccountReply.setMatchingType(defaultValidate.getMatchingType());
        officialAccountReply.setSort(defaultValidate.getSort());
        officialAccountReply.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        officialAccountReply.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.officialAccountReplyMapper.insert(officialAccountReply);
    }

    @Transactional
    public void edit(ChannelRpValidate defaultValidate) {
        OfficialAccountReply officialAccountReply = (OfficialAccountReply)this.officialAccountReplyMapper.selectOne(new QueryWrapper<OfficialAccountReply>().eq("id", (Object)defaultValidate.getId()).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)officialAccountReply, (String)"\u9ed8\u8ba4\u56de\u590d\u6570\u636e\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        if (defaultValidate.getReplyType().equals(3) && defaultValidate.getStatus().equals(1)) {
            OfficialAccountReply reply = new OfficialAccountReply();
            reply.setStatus(Integer.valueOf(0));
            this.officialAccountReplyMapper.update(reply, new QueryWrapper<OfficialAccountReply>().eq("reply_type", (Object)officialAccountReply.getReplyType()));
        }
        if (defaultValidate.getReplyType().equals(2)) {
            Assert.isTrue((boolean)StringUtils.isNotEmpty((String)defaultValidate.getKeyword()), (String)"\u5173\u952e\u8bcd\u4e0d\u80fd\u4e3a\u7a7a", (Object[])new Object[0]);
        }
        officialAccountReply.setId(defaultValidate.getId());
        officialAccountReply.setName(defaultValidate.getName());
        officialAccountReply.setContent(defaultValidate.getContent());
        officialAccountReply.setContentType(defaultValidate.getContentType());
        officialAccountReply.setStatus(defaultValidate.getStatus());
        officialAccountReply.setSort(defaultValidate.getSort());
        officialAccountReply.setKeyword(defaultValidate.getKeyword());
        officialAccountReply.setMatchingType(defaultValidate.getMatchingType());
        officialAccountReply.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        officialAccountReply.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.officialAccountReplyMapper.updateById(officialAccountReply);
    }

    public void del(Integer id) {
        OfficialAccountReply officialAccountReply = (OfficialAccountReply)this.officialAccountReplyMapper.selectOne(new QueryWrapper<OfficialAccountReply>().eq("id", (Object)id).isNull("delete_time").last("limit 1"));
        Assert.notNull((Object)officialAccountReply, (String)"\u9ed8\u8ba4\u56de\u590d\u6570\u636e\u4e0d\u5b58\u5728\u4e86!", (Object[])new Object[0]);
        officialAccountReply.setDeleteTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.officialAccountReplyMapper.updateById(officialAccountReply);
    }

    public void status(Integer id) {
        int status;
        OfficialAccountReply officialAccountReply = (OfficialAccountReply)this.officialAccountReplyMapper.selectOne(new QueryWrapper<OfficialAccountReply>().isNull("delete_time").eq("id", (Object)id).last("limit 1"));
        Assert.notNull((Object)officialAccountReply, (String)"\u9ed8\u8ba4\u56de\u590d\u6570\u636e\u4e0d\u5b58\u5728!", (Object[])new Object[0]);
        int n = status = officialAccountReply.getStatus() == 1 ? 0 : 1;
        if (status == 1) {
            OfficialAccountReply reply = new OfficialAccountReply();
            reply.setStatus(Integer.valueOf(0));
            reply.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
            this.officialAccountReplyMapper.update(reply, new QueryWrapper<OfficialAccountReply>().eq("reply_type", (Object)officialAccountReply.getReplyType()));
        }
        officialAccountReply.setId(id);
        officialAccountReply.setStatus(Integer.valueOf(status));
        officialAccountReply.setUpdateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
        this.officialAccountReplyMapper.updateById(officialAccountReply);
    }
}

