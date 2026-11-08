/*
 * Decompiled with CFR 0.152.
 */
package com.mdd.common.enums;

public enum FileEnum {
    IMAGE_TYPE(10, "\u56fe\u7247\u7c7b\u578b"),
    VIDEO_TYPE(20, "\u89c6\u9891\u7c7b\u578b"),
    FILE_TYPE(30, "\u6587\u4ef6\u7c7b\u578b"),
    SOURCE_ADMIN(0, "\u540e\u53f0"),
    SOURCE_USER(1, "\u7528\u6237");

    private final int code;
    private final String msg;

    private FileEnum(int code, String msg) {
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

