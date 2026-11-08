package com.mdd.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 将外部配置绑定到静态全局配置，避免在代码里写死调试开关。
 *
 * <p>{@code like.debug} 缺省为 false：生产不配置该项即为关闭状态；
 * 本地开发如需保留调试行为，可在 application-dev.yml 中显式设置
 * {@code like.debug: true}。异常响应无论如何都只返回通用文案，
 * 调试开关不会让异常细节重新进入响应体。</p>
 */
@Component
public class GlobalConfigBinder {

    public GlobalConfigBinder(@Value("${like.debug:false}") String debug,
                              @Value("${like.log.mask-openid:true}") boolean maskOpenid,
                              @Value("${like.log.mask-mobile:true}") boolean maskMobile,
                              @Value("${like.log.mask-amount:true}") boolean maskAmount) {
        GlobalConfig.debug = Boolean.valueOf(debug != null && Boolean.parseBoolean(debug.trim()));
        GlobalConfig.maskOpenid = maskOpenid;
        GlobalConfig.maskMobile = maskMobile;
        GlobalConfig.maskAmount = maskAmount;
    }
}
