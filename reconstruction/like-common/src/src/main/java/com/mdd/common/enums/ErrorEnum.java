/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.enums;

public enum ErrorEnum {
    SHOW_MSG(1, "\u663e\u793a\u4fe1\u606f"),
    HIDE_MSG(0, "\u9690\u85cf\u4fe1\u606f"),
    SUCCESS(1, "\u6210\u529f"),
    FAILED(0, "\u5931\u8d25"),
    PARAMS_VALID_ERROR(0, "\u53c2\u6570\u6821\u9a8c\u9519\u8bef"),
    PARAMS_TYPE_ERROR(0, "\u53c2\u6570\u7c7b\u578b\u9519\u8bef"),
    REQUEST_METHOD_ERROR(0, "\u8bf7\u6c42\u65b9\u6cd5\u9519\u8bef"),
    ASSERT_ARGUMENT_ERROR(0, "\u65ad\u8a00\u53c2\u6570\u9519\u8bef"),
    ASSERT_MYBATIS_ERROR(0, "\u65ad\u8a00Mybatis\u9519\u8bef"),
    LOGIN_ACCOUNT_ERROR(0, "\u767b\u5f55\u8d26\u53f7\u6216\u5bc6\u7801\u9519\u8bef"),
    LOGIN_DISABLE_ERROR(0, "\u767b\u5f55\u8d26\u53f7\u5df2\u88ab\u7981\u7528\u4e86"),
    TOKEN_EMPTY(0, "\u767b\u5f55\u8d85\u65f6\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55"),
    TOKEN_INVALID(-1, "token\u53c2\u6570\u65e0\u6548"),
    CAPTCHA_ERROR(0, "\u9a8c\u8bc1\u7801\u9519\u8bef"),
    PAYMENT_ERROR(0, "\u53d1\u8d77\u652f\u4ed8\u5931\u8d25"),
    NO_PERMISSION(0, "\u65e0\u76f8\u5173\u6743\u9650"),
    REQUEST_404_ERROR(0, "\u8bf7\u6c42\u63a5\u53e3\u4e0d\u5b58\u5728"),
    SYSTEM_ERROR(0, "\u7cfb\u7edf\u9519\u8bef");

    private final int code;
    private final String msg;

    private ErrorEnum(int code, String msg) {
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

