/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.context.EnvironmentAware
 *  org.springframework.core.env.Environment
 *  org.springframework.stereotype.Component
 */
package com.mdd.common.util;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class YmlUtils
implements EnvironmentAware {
    private static Environment env;

    public void setEnvironment(Environment environment) {
        env = environment;
    }

    public static String get(String key) {
        return env.getProperty(key);
    }
}

