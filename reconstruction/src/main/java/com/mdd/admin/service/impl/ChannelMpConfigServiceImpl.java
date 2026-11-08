/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.service.IChannelMpConfigService
 *  com.mdd.admin.service.impl.ChannelMpConfigServiceImpl
 *  com.mdd.admin.validate.channel.ChannelMpValidate
 *  com.mdd.admin.vo.channel.ChannelMpVo
 *  com.mdd.common.util.ConfigUtils
 *  com.mdd.common.util.RequestUtils
 *  com.mdd.common.util.StringUtils
 *  com.mdd.common.util.UrlUtils
 *  com.mdd.common.util.YmlUtils
 *  org.springframework.stereotype.Service
 */
package com.mdd.admin.service.impl;

import com.mdd.admin.service.IChannelMpConfigService;
import com.mdd.admin.validate.channel.ChannelMpValidate;
import com.mdd.admin.vo.channel.ChannelMpVo;
import com.mdd.common.util.ConfigUtils;
import com.mdd.common.util.RequestUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.UrlUtils;
import com.mdd.common.util.YmlUtils;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChannelMpConfigServiceImpl
implements IChannelMpConfigService {
    public ChannelMpVo getConfig() {
        Map<String, String> config = ConfigUtils.get((String)"mnp_setting");
        String env = YmlUtils.get((String)"like.production");
        boolean envStatus = StringUtils.isNotNull(env) && env.equals("true");
        ChannelMpVo vo = new ChannelMpVo();
        vo.setName(config.getOrDefault("name", ""));
        vo.setOriginalId(config.getOrDefault("original_id", ""));
        vo.setAppId(envStatus ? "******" : config.getOrDefault("app_id", ""));
        vo.setAppSecret(envStatus ? "******" : config.getOrDefault("app_secret", ""));
        vo.setQrCode(UrlUtils.toAdminAbsoluteUrl((String)config.getOrDefault("qr_code", "")));
        // 域名优先读后台保存值；历史数据没有这些 key 时保持原行为，按当前请求域名推导（task-7）
        String domain = RequestUtils.domain();
        vo.setRequestDomain(this.prefer(config.get("request_domain"), domain));
        vo.setSocketDomain(this.prefer(config.get("socket_domain"),
                domain.replace("https://", "wss://").replace("http://", "wss://")));
        vo.setUploadFileDomain(this.prefer(config.get("upload_file_domain"), domain));
        vo.setDownloadFileDomain(this.prefer(config.get("download_file_domain"), domain));
        vo.setUdpDomain(this.prefer(config.get("udp_domain"),
                domain.replace("https://", "udp://").replace("http://", "udp://")));
        vo.setTcpDomain(this.prefer(config.get("tcp_domain"), domain));
        vo.setBusinessDomain(this.prefer(config.get("business_domain"), domain));
        return vo;
    }

    public void setConfig(ChannelMpValidate channelMpValidate) {
        ConfigUtils.set((String)"mnp_setting", (String)"name", (String)channelMpValidate.getName());
        ConfigUtils.set((String)"mnp_setting", (String)"original_id", (String)channelMpValidate.getOriginalId());
        ConfigUtils.set((String)"mnp_setting", (String)"app_id", (String)channelMpValidate.getAppId());
        ConfigUtils.set((String)"mnp_setting", (String)"app_secret", (String)channelMpValidate.getAppSecret());
        ConfigUtils.set((String)"mnp_setting", (String)"qr_code", (String)UrlUtils.toRelativeUrl((String)channelMpValidate.getQrCode()));
        // 渠道页提交的 7 个域名此前被 DTO 丢弃、未落库（task-7）；只有非空值才覆盖，避免空提交清空历史配置
        this.saveDomain("request_domain", channelMpValidate.getRequestDomain());
        this.saveDomain("socket_domain", channelMpValidate.getSocketDomain());
        this.saveDomain("upload_file_domain", channelMpValidate.getUploadFileDomain());
        this.saveDomain("download_file_domain", channelMpValidate.getDownloadFileDomain());
        this.saveDomain("udp_domain", channelMpValidate.getUdpDomain());
        this.saveDomain("tcp_domain", channelMpValidate.getTcpDomain());
        this.saveDomain("business_domain", channelMpValidate.getBusinessDomain());
    }

    private void saveDomain(String key, String value) {
        if (StringUtils.isNotEmpty(value)) {
            ConfigUtils.set("mnp_setting", key, value.trim());
        }
    }

    private String prefer(String stored, String fallback) {
        return StringUtils.isNotEmpty(stored) ? stored : fallback;
    }
}

