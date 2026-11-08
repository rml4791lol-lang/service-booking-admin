/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.enums;

public enum YesNoEnum {
    YES(1, "\u662f"),
    NO(0, "\u5426");

    private final int code;
    private final String msg;

    private YesNoEnum(int code, String msg) {
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

