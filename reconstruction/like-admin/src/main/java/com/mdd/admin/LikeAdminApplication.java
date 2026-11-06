/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.LikeAdminApplication
 *  org.mybatis.spring.annotation.MapperScan
 *  org.springframework.boot.SpringApplication
 *  org.springframework.boot.autoconfigure.SpringBootApplication
 *  org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration
 *  org.springframework.context.annotation.ComponentScan
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.transaction.annotation.EnableTransactionManagement
 */
package com.mdd.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages={"com.mdd"})
@MapperScan(basePackages={"com.mdd.*.mapper"})
@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication(exclude={RedisRepositoriesAutoConfiguration.class})
public class LikeAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(LikeAdminApplication.class, (String[])args);
    }
}

