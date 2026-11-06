/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.front.LikeFrontApplication
 *  org.mybatis.spring.annotation.MapperScan
 *  org.springframework.boot.SpringApplication
 *  org.springframework.boot.autoconfigure.SpringBootApplication
 *  org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration
 *  org.springframework.context.annotation.ComponentScan
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.scheduling.annotation.EnableScheduling
 *  org.springframework.transaction.annotation.EnableTransactionManagement
 */
package com.mdd.front;

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
public class LikeFrontApplication {
    public static void main(String[] args) {
        SpringApplication.run(LikeFrontApplication.class, (String[])args);
    }
}

