/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.enums;

public enum SmsEnum {
    SEND_ING(0, "\u53d1\u9001\u4e2d"),
    SEND_SUCCESS(1, "\u53d1\u9001\u6210\u529f"),
    SEND_FAIL(2, "\u53d1\u9001\u5931\u8d25");

    private final int code;
    private final String msg;

    private SmsEnum(int code, String msg) {
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

