/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IChannelH5ConfigService
 *  com.mdd.admin.service.impl.ChannelH5ConfigServiceImpl
 *  com.mdd.admin.validate.channel.ChannelH5Validate
 *  com.mdd.admin.vo.channel.ChannelH5Vo
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.YmlUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.mdd.admin.service.IChannelH5ConfigService;
import com.mdd.admin.validate.channel.ChannelH5Validate;
import com.mdd.admin.vo.channel.ChannelH5Vo;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.YmlUtils;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChannelH5ConfigServiceImpl
implements IChannelH5ConfigService {
    public ChannelH5Vo getConfig() {
        Map<String, String> config = ConfigUtils.get((String)"web_page");
        ChannelH5Vo vo = new ChannelH5Vo();
        vo.setStatus(Integer.valueOf(Integer.parseInt(config.getOrDefault("status", "1"))));
        vo.setPageStatus(Integer.valueOf(Integer.parseInt(config.getOrDefault("page_status", "1"))));
        String url = YmlUtils.get((String)"like.front-url");
        url = StringUtils.isNotEmpty((String)url) && url.endsWith("/") ? url + "/moblie" : url + "moblie";
        vo.setUrl(config.getOrDefault("url", url));
        vo.setPageUrl(config.getOrDefault("page_url", ""));
        return vo;
    }

    public void setConfig(ChannelH5Validate channelH5Validate) {
        ConfigUtils.set((String)"web_page", (String)"status", (String)String.valueOf(channelH5Validate.getStatus()));
        ConfigUtils.set((String)"web_page", (String)"page_status", (String)String.valueOf(channelH5Validate.getPageStatus()));
        ConfigUtils.set((String)"web_page", (String)"url", (String)channelH5Validate.getUrl());
        ConfigUtils.set((String)"web_page", (String)"page_url", (String)channelH5Validate.getPageUrl());
    }
}

