/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IChannelOpService
 *  com.mdd.admin.validate.channel.ChannelOpValidate
 *  com.mdd.admin.vo.channel.ChannelOpVo
 */
package com.mdd.admin.service;

import com.mdd.admin.validate.channel.ChannelOpValidate;
import com.mdd.admin.vo.channel.ChannelOpVo;

public interface IChannelOpService {
    public ChannelOpVo getConfig();

    public void setConfig(ChannelOpValidate var1);
}

