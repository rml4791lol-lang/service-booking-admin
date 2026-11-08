/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IChannelOaReplyService
 *  com.mdd.admin.validate.channel.ChannelRpValidate
 *  com.mdd.admin.validate.commons.PageValidate
 *  com.mdd.admin.vo.channel.ChannelRpVo
 *  com.mdd.common.core.PageResult
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.channel.ChannelRpValidate;
import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.channel.ChannelRpVo;
import com.mdd.common.core.PageResult;

public interface IChannelOaReplyService {
    public PageResult<ChannelRpVo> list(PageValidate var1, Integer var2);

    public ChannelRpVo detail(Integer var1);

    public void add(ChannelRpValidate var1);

    public void edit(ChannelRpValidate var1);

    public void del(Integer var1);

    public void status(Integer var1);
}

