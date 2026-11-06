/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IChannelOpService
 *  com.mdd.admin.service.impl.ChannelOpServiceImpl
 *  com.mdd.admin.validate.channel.ChannelOpValidate
 *  com.mdd.admin.vo.channel.ChannelOpVo
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.YmlUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.mdd.admin.service.IChannelOpService;
import com.mdd.admin.validate.channel.ChannelOpValidate;
import com.mdd.admin.vo.channel.ChannelOpVo;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.YmlUtils;
import org.springframework.stereotype.Service;

@Service
public class ChannelOpServiceImpl
implements IChannelOpService {
    public ChannelOpVo getConfig() {
        String appId = ConfigUtils.get((String)"open_platform", (String)"app_id", (String)"");
        String appSecret = ConfigUtils.get((String)"open_platform", (String)"app_secret", (String)"");
        String env = YmlUtils.get((String)"like.production");
        boolean envStatus = StringUtils.isNotNull(env) && env.equals("true");
        ChannelOpVo vo = new ChannelOpVo();
        vo.setAppId(envStatus ? "******" : appId);
        vo.setAppSecret(envStatus ? "******" : appSecret);
        return vo;
    }

    public void setConfig(ChannelOpValidate opValidate) {
        ConfigUtils.set((String)"open_platform", (String)"app_id", (String)opValidate.getAppId());
        ConfigUtils.set((String)"open_platform", (String)"app_secret", (String)opValidate.getAppSecret());
    }
}

