/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson2.JSONObject
 */
package com.mdd.common.enums;

import com.alibaba.fastjson2.JSONObject;
import java.util.ArrayList;
import java.util.List;

public enum NoticeEnum {
    STATUS_WAIT(0, "\u7b49\u5f85"),
    STATUS_OK(1, "\u6210\u529f"),
    STATUS_FAIL(2, "\u5931\u8d25"),
    VIEW_UNREAD(0, "\u672a\u8bfb"),
    VIEW_READ(0, "\u5df2\u8bfb"),
    SENDER_SYS(1, "\u7cfb\u7edf\u7c7b\u578b"),
    SENDER_SMS(2, "\u77ed\u4fe1\u7c7b\u578b"),
    SENDER_MNP(3, "\u5c0f\u7a0b\u5e8f\u7c7b\u578b"),
    SENDER_OA(4, "\u516c\u4f17\u53f7\u7c7b\u578b"),
    LOGIN_CAPTCHA(101, "\u767b\u5f55\u9a8c\u8bc1\u7801"),
    BIND_MOBILE_CAPTCHA(102, "\u7ed1\u5b9a\u624b\u673a\u9a8c\u8bc1\u7801"),
    CHANGE_MOBILE_CAPTCHA(103, "\u53d8\u66f4\u624b\u673a\u9a8c\u8bc1\u7801"),
    FIND_LOGIN_PASSWORD_CAPTCHA(104, "\u627e\u56de\u767b\u5f55\u5bc6\u7801\u9a8c\u8bc1\u7801");

    private final int code;
    private final String msg;

    private NoticeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static Integer getSceneByTag(String tag) {
        JSONObject scene = new JSONObject();
        scene.put("YZMDL", (Object)LOGIN_CAPTCHA.getCode());
        scene.put("BDSJHM", (Object)BIND_MOBILE_CAPTCHA.getCode());
        scene.put("BGSJHM", (Object)CHANGE_MOBILE_CAPTCHA.getCode());
        scene.put("ZHDLMM", (Object)FIND_LOGIN_PASSWORD_CAPTCHA.getCode());
        return scene.getInteger(tag);
    }

    public static List<Integer> getSmsScene() {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        ret.add(LOGIN_CAPTCHA.getCode());
        ret.add(BIND_MOBILE_CAPTCHA.getCode());
        ret.add(CHANGE_MOBILE_CAPTCHA.getCode());
        ret.add(FIND_LOGIN_PASSWORD_CAPTCHA.getCode());
        return ret;
    }
}

