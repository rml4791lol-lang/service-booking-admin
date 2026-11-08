/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IChannelOaConfigService
 *  com.mdd.admin.service.impl.ChannelOaConfigServiceImpl
 *  com.mdd.admin.validate.channel.ChannelOaValidate
 *  com.mdd.admin.vo.channel.ChannelOaVo
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.RequestUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.common.util.YmlUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.mdd.admin.service.IChannelOaConfigService;
import com.mdd.admin.validate.channel.ChannelOaValidate;
import com.mdd.admin.vo.channel.ChannelOaVo;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.RequestUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.YmlUtils;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChannelOaConfigServiceImpl
implements IChannelOaConfigService {
    public ChannelOaVo getConfig() {
        Map<String, String> config = ConfigUtils.get((String)"oa_setting");
        ChannelOaVo vo = new ChannelOaVo();
        String env = YmlUtils.get((String)"like.production");
        boolean envStatus = StringUtils.isNotNull(env) && env.equals("true");
        String domain = RequestUtils.domain();
        vo.setQrCode(UrlUtils.toAdminAbsoluteUrl((String)config.getOrDefault("qr_code", "")));
        vo.setName(config.getOrDefault("name", ""));
        vo.setOriginalId(config.getOrDefault("original_id", ""));
        vo.setAppId(envStatus ? "******" : config.getOrDefault("app_id", ""));
        vo.setAppSecret(envStatus ? "******" : config.getOrDefault("app_secret", ""));
        vo.setUrl(domain + (StringUtils.isNotEmpty((String)config.getOrDefault("url", "")) ? config.getOrDefault("url", "") : "/adminapi/channel/oa/callback"));
        vo.setToken(config.getOrDefault("token", ""));
        vo.setEncodingAesKey(config.getOrDefault("encoding_aes_key", ""));
        vo.setEncryptionType(Integer.valueOf(Integer.parseInt(config.getOrDefault("encryption_type", "1"))));
        vo.setBusinessDomain(domain);
        vo.setJsSecureDomain(domain);
        vo.setWebAuthDomain(domain);
        return vo;
    }

    public void setConfig(ChannelOaValidate channelOaValidate) {
        ConfigUtils.set((String)"oa_setting", (String)"name", (String)channelOaValidate.getName());
        ConfigUtils.set((String)"oa_setting", (String)"original_id", (String)channelOaValidate.getOriginalId());
        ConfigUtils.set((String)"oa_setting", (String)"qr_code", (String)UrlUtils.toRelativeUrl((String)channelOaValidate.getQrCode()));
        ConfigUtils.set((String)"oa_setting", (String)"app_id", (String)channelOaValidate.getAppId());
        ConfigUtils.set((String)"oa_setting", (String)"app_secret", (String)channelOaValidate.getAppSecret());
        ConfigUtils.set((String)"oa_setting", (String)"token", (String)channelOaValidate.getToken());
        ConfigUtils.set((String)"oa_setting", (String)"encoding_aes_key", (String)channelOaValidate.getEncodingAesKey());
        ConfigUtils.set((String)"oa_setting", (String)"encryption_type", (String)String.valueOf(channelOaValidate.getEncryptionType()));
    }
}

