/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.common.config.GlobalConfig
 *  com.mdd.common.util.YmlUtils
 *  com.mdd.front.LikeFrontInterceptor
 *  com.mdd.front.config.WebMvcConfig
 *  javax.annotation.Resource
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.lang.NonNull
 *  org.springframework.web.servlet.HandlerInterceptor
 *  org.springframework.web.servlet.config.annotation.CorsRegistry
 *  org.springframework.web.servlet.config.annotation.InterceptorRegistry
 *  org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
 *  org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 */
package com.mdd.front.config;

import com.mdd.common.config.GlobalConfig;
import com.mdd.common.util.YmlUtils;
import com.mdd.front.LikeFrontInterceptor;
import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig
implements WebMvcConfigurer {
    @Resource
    LikeFrontInterceptor likeFrontInterceptor;

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(new String[]{"*"}).allowedHeaders(new String[]{"*"}).allowedMethods(new String[]{"GET", "POST", "DELETE", "PUT"}).exposedHeaders(new String[]{"X-Request-Id"}).maxAge(3600L);
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor((HandlerInterceptor)this.likeFrontInterceptor).addPathPatterns(new String[]{"/**"});
    }

    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        String directory = YmlUtils.get((String)"like.upload-directory");
        registry.addResourceHandler(new String[]{"/" + GlobalConfig.publicPrefix + "/**"}).addResourceLocations(new String[]{"file:" + directory});
    }
}

