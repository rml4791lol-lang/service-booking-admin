/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IChannelMpConfigService
 *  com.mdd.admin.validate.channel.ChannelMpValidate
 *  com.mdd.admin.vo.channel.ChannelMpVo
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.channel.ChannelMpValidate;
import com.mdd.admin.vo.channel.ChannelMpVo;

public interface IChannelMpConfigService {
    public ChannelMpVo getConfig();

    public void setConfig(ChannelMpValidate var1);
}

