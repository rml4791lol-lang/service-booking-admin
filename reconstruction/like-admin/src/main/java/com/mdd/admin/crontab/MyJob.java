/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.crontab.MyJob
 *  org.springframework.stereotype.Component
 */
package com.mdd.admin.crontab;

import org.springframework.stereotype.Component;

@Component(value="myJob")
public class MyJob {
    public void handle(String s) {
        System.out.println("\u6709\u53c2\u6570\u5b9a\u65f6\u4efb\u52a1\u6267\u884c\u903b\u8f91 : " + s);
    }
}

