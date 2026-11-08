/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
 *  com.github.binarywang.wxpay.config.WxPayConfig
 *  com.github.binarywang.wxpay.service.WxPayService
 *  com.github.binarywang.wxpay.service.impl.WxPayServiceImpl
 *  javax.annotation.Resource
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnClass
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 */
package com.mdd.common.config.wechat;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.mdd.common.entity.Config;
import com.mdd.common.entity.setting.DevPayConfig;
import com.mdd.common.enums.PaymentEnum;
import com.mdd.common.mapper.ConfigMapper;
import com.mdd.common.mapper.setting.DevPayConfigMapper;
import com.mdd.common.util.MapUtils;
import com.mdd.common.util.StringUtils;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(value={WxPayService.class})
public class WxPayConfiguration {
    @Resource
    DevPayConfigMapper devPayConfigMapper;
    @Resource
    ConfigMapper systemConfigMapper;

    @Bean
    @ConditionalOnMissingBean
    public WxPayService mnpPayService() {
        DevPayConfig config = (DevPayConfig)this.devPayConfigMapper.selectOne(new QueryWrapper<DevPayConfig>().eq("pay_way", (Object)PaymentEnum.WX_PAY.getCode()).last("limit 1"));
        Config systemConfig = (Config)this.systemConfigMapper.selectOne(new QueryWrapper<Config>().eq("type", (Object)"mnp_setting").eq("name", (Object)"app_id").last("limit 1"));
        String paramJson = StringUtils.isNull(config.getConfig()) ? "{}" : config.getConfig().toString();
        Map<String, String> params = MapUtils.jsonToMap(paramJson);
        String appId = StringUtils.isNull(systemConfig) ? "" : systemConfig.getValue();
        String mchId = params.getOrDefault("mch_id", "");
        String paySignKey = params.getOrDefault("pay_sign_key", "");
        byte[] privateKey = params.getOrDefault("apiclient_key", params.getOrDefault("private_key", "")).getBytes();
        byte[] privateCert = params.getOrDefault("apiclient_cert", params.getOrDefault("private_cert", "")).getBytes();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(appId);
        payConfig.setMchId(mchId);
        payConfig.setApiV3Key(paySignKey);
        payConfig.setPrivateKeyContent(privateKey);
        payConfig.setPrivateCertContent(privateCert);
        payConfig.setUseSandboxEnv(false);
        WxPayServiceImpl wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    @Bean
    @ConditionalOnMissingBean
    public WxPayService wxOaService() {
        DevPayConfig config = (DevPayConfig)this.devPayConfigMapper.selectOne(new QueryWrapper<DevPayConfig>().eq("pay_way", (Object)PaymentEnum.WX_PAY.getCode()).last("limit 1"));
        Config systemConfig = (Config)this.systemConfigMapper.selectOne(new QueryWrapper<Config>().eq("name", (Object)"app_id").eq("type", (Object)"oa_setting").last("limit 1"));
        String paramJson = StringUtils.isNull(config.getConfig()) ? "{}" : config.getConfig().toString();
        Map<String, String> params = MapUtils.jsonToMap(paramJson);
        String appId = StringUtils.isNull(systemConfig) ? "" : systemConfig.getValue();
        String mchId = params.getOrDefault("mch_id", "");
        String paySignKey = params.getOrDefault("pay_sign_key", "");
        byte[] privateKey = params.getOrDefault("apiclient_key", params.getOrDefault("private_key", "")).getBytes();
        byte[] privateCert = params.getOrDefault("apiclient_cert", params.getOrDefault("private_cert", "")).getBytes();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setMchId(mchId);
        payConfig.setAppId(appId);
        payConfig.setApiV3Key(paySignKey);
        payConfig.setPrivateKeyContent(privateKey);
        payConfig.setPrivateCertContent(privateCert);
        payConfig.setUseSandboxEnv(false);
        WxPayServiceImpl wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    public WxPayConfiguration(DevPayConfigMapper devPayConfigMapper, ConfigMapper systemConfigMapper) {
        this.devPayConfigMapper = devPayConfigMapper;
        this.systemConfigMapper = systemConfigMapper;
    }
}

