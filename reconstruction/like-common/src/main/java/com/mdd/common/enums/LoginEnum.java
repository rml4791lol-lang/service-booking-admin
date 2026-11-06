/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.enums;

public enum LoginEnum {
    ACCOUNT_PASSWORD(1, "\u8d26\u53f7/\u624b\u673a\u53f7\u5bc6\u7801\u767b\u5f55"),
    MOBILE_CAPTCHA(2, "\u624b\u673a\u9a8c\u8bc1\u7801\u767b\u5f55"),
    THIRD_LOGIN(3, "\u7b2c\u4e09\u65b9\u767b\u5f55");

    private final int code;
    private final String msg;

    private LoginEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}

