/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IChannelOaConfigService
 *  com.mdd.admin.validate.channel.ChannelOaValidate
 *  com.mdd.admin.vo.channel.ChannelOaVo
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.channel.ChannelOaValidate;
import com.mdd.admin.vo.channel.ChannelOaVo;

public interface IChannelOaConfigService {
    public ChannelOaVo getConfig();

    public void setConfig(ChannelOaValidate var1);
}

