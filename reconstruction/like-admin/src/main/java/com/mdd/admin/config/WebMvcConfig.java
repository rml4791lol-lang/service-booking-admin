/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.LikeAdminInterceptor
 *  com.mdd.admin.config.WebMvcConfig
 *  com.mdd.common.config.GlobalConfig
 *  com.mdd.common.util.YmlUtils
 *  javax.annotation.Resource
 *  org.jetbrains.annotations.NotNull
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.web.servlet.HandlerInterceptor
 *  org.springframework.web.servlet.config.annotation.CorsRegistry
 *  org.springframework.web.servlet.config.annotation.InterceptorRegistry
 *  org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
 *  org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 */
package com.mdd.admin.config;

import com.mdd.admin.LikeAdminInterceptor;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.util.YmlUtils;
import javax.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig
implements WebMvcConfigurer {
    @Resource
    LikeAdminInterceptor likeAdminInterceptor;

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(new String[]{"*"}).allowedHeaders(new String[]{"*"}).allowedMethods(new String[]{"GET", "POST", "DELETE", "PUT"}).exposedHeaders(new String[]{"X-Request-Id"}).maxAge(3600L);
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor((HandlerInterceptor)this.likeAdminInterceptor).addPathPatterns(new String[]{"/**"});
    }

    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        String directory = YmlUtils.get((String)"like.upload-directory");
        registry.addResourceHandler(new String[]{"/" + GlobalConfig.adminPublicPrefix + "/**"}).addResourceLocations(new String[]{"file:" + directory});
    }
}

