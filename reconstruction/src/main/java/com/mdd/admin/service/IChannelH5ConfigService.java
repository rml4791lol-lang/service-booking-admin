/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IChannelH5ConfigService
 *  com.mdd.admin.validate.channel.ChannelH5Validate
 *  com.mdd.admin.vo.channel.ChannelH5Vo
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.channel.ChannelH5Validate;
import com.mdd.admin.vo.channel.ChannelH5Vo;

public interface IChannelH5ConfigService {
    public ChannelH5Vo getConfig();

    public void setConfig(ChannelH5Validate var1);
}

