/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.binarywang.wx.miniapp.api.WxMaService
 *  cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl
 *  cn.binarywang.wx.miniapp.config.WxMaConfig
 *  cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.github.binarywang.wxpay.service.WxPayService
 *  javax.annotation.Resource
 *  me.chanjar.weixin.mp.api.WxMpService
 *  me.chanjar.weixin.mp.api.impl.WxMpServiceImpl
 *  me.chanjar.weixin.mp.config.WxMpConfigStorage
 *  me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnClass
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 */
package com.mdd.common.config.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.service.WxPayService;
import com.mdd.common.entity.Config;
import com.mdd.common.mapper.ConfigMapper;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(value={WxPayService.class})
public class WxMnpConfiguration {
    @Resource
    private final ConfigMapper systemConfigMapper;

    @Bean
    @ConditionalOnMissingBean
    public WxMaService wxMnpService() {
        Map<String, String> config = this.getChannelConfig("mnp_setting");
        WxMaDefaultConfigImpl wxConfig = new WxMaDefaultConfigImpl();
        wxConfig.setAppid(config.getOrDefault("app_id", ""));
        wxConfig.setSecret(config.getOrDefault("app_secret", ""));
        WxMaServiceImpl wxService = new WxMaServiceImpl();
        wxService.setWxMaConfig((WxMaConfig)wxConfig);
        return wxService;
    }

    @Bean
    @ConditionalOnMissingBean
    public WxMpService wxOaService() {
        Map<String, String> config = this.getChannelConfig("oa_setting");
        WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
        wxMpDefaultConfig.setAppId(config.getOrDefault("app_id", "").trim());
        wxMpDefaultConfig.setSecret(config.getOrDefault("app_secret", "").trim());
        wxMpDefaultConfig.setToken(config.getOrDefault("token", "").trim());
        wxMpDefaultConfig.setAesKey(config.getOrDefault("encoding_aes_key", "").trim());
        WxMpServiceImpl wxService = new WxMpServiceImpl();
        wxService.setWxMpConfigStorage((WxMpConfigStorage)wxMpDefaultConfig);
        return wxService;
    }

    private Map<String, String> getChannelConfig(String type) {
        List<Config> configs = (List<Config>) this.systemConfigMapper.selectList(new QueryWrapper<Config>().select(new String[]{"id", "type", "name", "value"}).eq("type", type));
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        for (Config config : configs) {
            map.put(config.getName(), config.getValue());
        }
        return map;
    }

    public WxMnpConfiguration(ConfigMapper systemConfigMapper) {
        this.systemConfigMapper = systemConfigMapper;
    }
}

