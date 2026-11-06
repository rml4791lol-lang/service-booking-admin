/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.config.SwaggerConfig
 *  com.mdd.common.config.GlobalConfig
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  springfox.documentation.builders.ApiInfoBuilder
 *  springfox.documentation.builders.RequestHandlerSelectors
 *  springfox.documentation.oas.annotations.EnableOpenApi
 *  springfox.documentation.service.ApiInfo
 *  springfox.documentation.service.Contact
 *  springfox.documentation.spi.DocumentationType
 *  springfox.documentation.spring.web.plugins.Docket
 */
package com.mdd.admin.config;

import com.mdd.common.config.GlobalConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger/OpenAPI 文档装配（#149 生产关闭）。
 *
 * <p>默认关闭：like.swagger.enabled 未配置或为 false 时，Docket 以 enable(false)
 * 注册。springfox 的 DocumentationPluginsBootstrapper 会跳过 disabled 的插件，
 * DocumentationCache 无任何分组，/v3/api-docs 直接 404。注意不能用
 * {@code @ConditionalOnProperty} 把本类整个跳过：springfox 在无任何 Docket bean
 * 时会回退装配一个默认启用的 Docket（DocumentationPluginsManager#defaultDocumentationPlugin），
 * 反而会扫描全部接口。</p>
 *
 * <p>显式开启（dev/test/local profile 配 like.swagger.enabled=true）时按原逻辑生效；
 * pathMapping 未配置时按空串处理，不再因缺占位符启动失败（#54 缺省启动失败的兜底）。</p>
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {
    @Value(value="${like.swagger.enabled:false}")
    private boolean enabled;
    @Value(value="${like.swagger.pathMapping:}")
    private String pathMapping;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30).apiInfo(this.apiInfo()).enable(this.enabled).select().apis(RequestHandlerSelectors.basePackage((String)"com.mdd.admin.controller")).build().pathMapping(this.pathMapping);
    }

    private ApiInfo apiInfo() {
        String author = "FZR";
        String url = "https://gitee.com/likeadmin/likeadmin_java";
        String email = "tinyants@163.com";
        return new ApiInfoBuilder().title("LikeAdmin\u3010\u540e\u53f0\u3011\u63a5\u53e3\u6587\u6863").description("likeadmin\u662f\u4e00\u5957\u4f7f\u7528\u6d41\u884c\u7684\u6280\u672f\u6808\u7684\u5feb\u901f\u5f00\u53d1\u7ba1\u7406\u540e\u53f0").version(GlobalConfig.version).contact(new Contact(author, url, email)).build();
    }
}

